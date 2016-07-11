/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */
package com.ulta.core.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.account.OlapicActivity;
import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.account.WebViewActivity;
import com.ulta.core.activity.product.UltaProductDetailsActivity;
import com.ulta.core.activity.product.UltaProductListActivity;
import com.ulta.core.activity.rewards.NonSignedInRewardsActivity;
import com.ulta.core.activity.rewards.UltaMateCreditCardActivity;
import com.ulta.core.bean.product.homePageSectionSlotBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.CheckoutScreens;
import com.ulta.core.conf.types.DependentSubActivities;
import com.ulta.core.conf.types.NonCheckoutFormScreens;
import com.ulta.core.conf.types.UserSpecificRetrieveDataScreen;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import static com.ulta.core.conf.UltaConstants.AMPERSAND_SYMBOL;
import static com.ulta.core.conf.UltaConstants.EMPTY_STRING;
import static com.ulta.core.conf.UltaConstants.EQUALS_SYMBOL;
import static com.ulta.core.conf.UltaConstants.ISDEBUG;
import static com.ulta.core.conf.UltaConstants.NEXT_LINE;
import static com.ulta.core.conf.UltaConstants.TILDE_MARK_SYMBOL;
import static com.ulta.core.util.UltaException.CLIENT_ERROR;
import static com.ulta.core.util.UltaException.COMMON_ERROR_TITLE;
import static com.ulta.core.util.UltaException.NETWORK_ERROR_IO_EXCEPTION;
import static com.ulta.core.util.UltaException.SERVICE_UNAVAILABLE;
import static com.ulta.core.util.log.Logger.LOG_LOCATION;

/**
 * The Class Utility.
 *
 * @author viva
 */
public class Utility {

    /**
     * The Constant COOKIES.
     */
    public static final String COOKIES = "SHARED_PREFERANCE_COOKIES";
    /**
     * The date format of the cookie
     */
    private static final String COOKIE_DATE_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";

    /**
     * The Constant EMAIL_ADDRESS_PATTERN.
     */
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

    public static final String strNativePageName[] = {"productdetailpage",
            "productlistpage", "productlistpagebrand", "productlistpagepromo",
            "shoppage", "weeklyaddpage", "newarrivalpage", "rewardpage",
            "URL of webpage", "olapic", "acquisition"};

    public static String strNativePage = "";
    public static String strNativePageWithParams[];

    public static Intent homePageSectionIntent;

    private static SecretKeySpec secretKey = null;

    private static byte[] encodedUserNameBytes = null;

    private static byte[] encodedPasswordBytes = null;

	/* private static byte[] encodedBytes = null; */

    /**
     * Error 5xx prefix
     */
    private static final String ERROR_5XX_PREFIX = "5";
    /**
     * Error 4xx prefix
     */
    private static final String ERROR_4XX_PREFIX = "4";

    /*
     * Method to log errors.
     *
     * @param data the data
     */
    public static void savelog(String data) {
        FileWriter logger;
        try {
            if (ISDEBUG) {
                logger = new FileWriter(LOG_LOCATION, true);
                logger.write(data + NEXT_LINE);
                logger.flush();
                logger.close();
            }
        } catch (IOException ioException) {
            Logger.Log("Exception in saveLog" + ioException.getMessage());
        } catch (Exception e) {
            Logger.Log("Exception in saveLog" + e.getMessage());
        }
    }

    /**
     * Method for validation of email.
     *
     * @param email the email
     * @return true, if successful
     */
    public static boolean validateEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /**
     * Method for the separation of videoId from youtube url.
     *
     * @param link the link
     * @return the youtube video id
     */
    public static String getYoutubeVideoId(String link) {
        int fromPoint = 0;
        for (int iLoop = 0; iLoop < link.length(); iLoop++) {
            String letter = String.valueOf(link.charAt(iLoop));
            if (letter == "=" || letter.equals("=")) {
                fromPoint = iLoop;
            }
        }
        String videoId = link.substring(fromPoint + 1);
        return videoId;
    }

    /**
     * Method for calculating dimensions.
     *
     * @param factor    the factor
     * @param dimension (height, width etc)
     * @return <double> calculatedDimension
     */
    public static double calculateDimensions(double factor, int dimension) {
        return factor * dimension;
    }

    /**
     * Method to display the error alert dialog.
     *
     * @param message the message
     * @param context the context
     */
    public static void displayUserErrorMessage(String message,
                                               final Context context) {
        displayUserErrorMessage(null, message, context, null);
    }

    /**
     * Method to display the error alert dialog.
     *
     * @param title   the title
     * @param message the message
     * @param context the context
     * @param intent  the intent
     */
    public static void displayUserErrorMessage(String title, String message,
                                               final Context context, final Intent intent) {
        final String lblPositiveButton = "OK";
        final String errorTitle = COMMON_ERROR_TITLE;

	/*	final AlertDialog.Builder errorAlertDialogBuilder = new AlertDialog.Builder(
                context);
		errorAlertDialogBuilder.setMessage(message);

		if (title != null && title.trim().length() > 0) {
			errorAlertDialogBuilder.setTitle(title);
		} else {
			errorAlertDialogBuilder.setTitle(errorTitle);
		}
		errorAlertDialogBuilder.setCancelable(false);
		errorAlertDialogBuilder.setPositiveButton(lblPositiveButton,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						if (intent != null) {
							context.startActivity(intent);
						}
					}
				});
		final AlertDialog errorAlertDialog = errorAlertDialogBuilder.create();
		errorAlertDialog.show();*/

        String dialogHeading = "";
        if (title != null && title.trim().length() > 0) {
            dialogHeading = title;
        } else {
            dialogHeading = errorTitle;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        final Dialog errorDialog = new Dialog(context,
                R.style.AppCompatAlertDialogStyle);
        errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        errorDialog.setContentView(R.layout.alert_dialog);
        errorDialog.setCancelable(false);

        TextView headingTV = (TextView) errorDialog.findViewById(R.id.heading);
        TextView messageTV = (TextView) errorDialog.findViewById(R.id.message);
        Button mAgreeButton = (Button) errorDialog.findViewById(R.id.btnAgree);
        Button mDisagreeButton = (Button) errorDialog
                .findViewById(R.id.btnDisagree);

        headingTV.setText(dialogHeading);
        messageTV.setText(message);
        mAgreeButton.setText(lblPositiveButton);
        mDisagreeButton.setVisibility(View.GONE);
        errorDialog.getWindow().setLayout((6 * width) / 7,
                LayoutParams.WRAP_CONTENT);
        try {
            errorDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mAgreeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                errorDialog.dismiss();
                if (intent != null) {
                    context.startActivity(intent);
                }
            }
        });


    }

    // /**
    // * shows Activity indicator
    // * @param context
    // */
    // public static void displayActivityIndicator(Context context){
    // final AlertDialog.Builder activityIndicator = new
    // AlertDialog.Builder(context);
    // activityIndicator.setCancelable(false);
    // LayoutInflater imageInflater = LayoutInflater.from(context);
    // View view = imageInflater.inflate(R.layout.activity_indicator, null);
    // activityIndicator.setView(view);
    // final AlertDialog alertDialog = activityIndicator.create();
    // alertDialog.show();
    // }
    //
    // /**
    // * shows Notofication on the notification bar.
    // * @param context
    // * @param message
    // */
    //
    // @SuppressWarnings("static-access")
    // public static void showNotification(Context context,String message) {
    // NotificationManager nManager;
    // nManager = (NotificationManager)
    // context.getSystemService(context.NOTIFICATION_SERVICE);
    // Notification notification = new Notification(R.drawable.icon,
    // "Push Message Received",System.currentTimeMillis());
    // Intent intent=new
    // Intent(context,NotificationDummyActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
    // | Intent.FLAG_ACTIVITY_SINGLE_TOP );
    // intent.putExtra("message", message);
    // notification.setLatestEventInfo(context, "ChefRef", message,
    // PendingIntent.getActivity(context, 0, intent,
    // PendingIntent.FLAG_CANCEL_CURRENT));
    // notification.flags |= Notification.FLAG_ONGOING_EVENT;
    // notification.flags |=Notification.FLAG_AUTO_CANCEL;
    // nManager.notify(0, notification);
    //
    // }

    /**
     * the method reads raw files and return the content as string.
     *
     * @param context    the context
     * @param resourceId the resource id
     * @return the string
     * @throws UltaException the ulta exception
     */
    public static String readFromRawFile(Context context, int resourceId)
            throws UltaException {
        InputStream inputStream;
        Resources resources = context.getResources();
        inputStream = resources.openRawResource(resourceId);
        ByteArrayOutputStream outputStream = null;
        try {
            byte[] buffer = new byte[inputStream.available()];
            // read the text file as a stream, into the buffer
            inputStream.read(buffer);
            // create a output stream to write the buffer into
            outputStream = new ByteArrayOutputStream();
            // write this buffer to the output stream
            outputStream.write(buffer);
            // Close the Input and Output streams
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new UltaException("File read Failed", e);
        } finally {
            // close all streams
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new UltaException("File read Failed", e);
            }
        }
        // return the output stream as a String
        return outputStream.toString();
    }

    /**
     * Method for retrieving the string value of a cookie.
     *
     * @param retrievalKey the retrieval key
     * @return the cookie value
     */
    public static String getCookieValue(String retrievalKey) {
        return retrieveFromSharedPreference(COOKIES, retrievalKey,
                getUltaApplicationContext());
    }

    /**
     * Method for saving the string value of a cookie.
     *
     * @param keyForValueToSave the key for value to save
     * @param valueToSave       the value to save
     */
    public static void saveCookie(String keyForValueToSave, String valueToSave) {
        saveToSharedPreference(COOKIES, keyForValueToSave, valueToSave,
                getUltaApplicationContext());

    }

    /**
     * Gets the shared preference.
     *
     * @param sharedPreferenceName the shared preference name
     * @param context              the context
     * @return the shared preference
     */
    private static SharedPreferences getSharedPreference(
            String sharedPreferenceName, Context context) {
        return context.getSharedPreferences(sharedPreferenceName,
                Activity.MODE_PRIVATE);
    }

    /**
     * Common method for retrieve value from shared preference.
     *
     * @param retrievalKey the retrieval key
     * @param context      the context
     * @return the string
     */
    public static String retrieveFromSharedPreference(String retrievalKey,
                                                      Context context) {
        return retrieveFromSharedPreference(UltaConstants.PREFS_NAME,
                retrievalKey, context);
    }
    /**
     * Common method for retrieve value from shared preference.
     *
     * @param retrievalKey the retrieval key
     * @param context      the context
     * @return the string
     */
    public static Boolean retrieveBooleanFromSharedPreference(String retrievalKey,
                                                      Context context) {
        return getSharedPreference(UltaConstants.PREFS_NAME, context).getBoolean(
                retrievalKey, true);
    }

    /**
     * Common method for retrieve value from shared preference.
     *
     * @param sharedPreferenceName the shared preference name
     * @param retrievalKey         the retrieval key
     * @param context              the context
     * @return the string
     */
    public static String retrieveFromSharedPreference(
            String sharedPreferenceName, String retrievalKey, Context context) {
        return getSharedPreference(sharedPreferenceName, context).getString(
                retrievalKey, null);
    }

    /**
     * Common method to save value to shared preference.
     *
     * @param keyForValueToSave the key for value to save
     * @param valueToSave       the value to save
     * @param context           the context
     */
    public static void saveToSharedPreference(String keyForValueToSave,
                                              String valueToSave, Context context) {
        saveToSharedPreference(UltaConstants.PREFS_NAME, keyForValueToSave,
                valueToSave, context);
    }

    /**
     * Common method to save value to shared preference.
     *
     * @param keyForValueToSave the key for value to save
     * @param valueToSave       the value to save
     * @param context           the context
     */
    public static void saveToSharedPreference(String keyForValueToSave,
                                              Boolean valueToSave, Context context) {
        saveToSharedPreference(UltaConstants.PREFS_NAME, keyForValueToSave,
                valueToSave, context);
    }
    /**
     * Common method to save boolean value to shared preference.
     *
     * @param keyForValueToSave the key for value to save
     * @param valueToSave       the value to save
     * @param context           the context
     */
    public static void saveToSharedPreference(String sharedPreferenceName, String keyForValueToSave,
                                              Boolean valueToSave, Context context) {
        Editor editor = getSharedPreference(sharedPreferenceName, context)
                .edit();
        editor.putBoolean(keyForValueToSave, valueToSave);
        editor.commit();
    }

    /**
     * Common method for retrieve value for boolean from shared preference.
     *
     * @param sharedPreferenceName the shared preference name
     * @param retrievalKey         the retrieval key
     * @param context              the context
     * @return the string
     */
    public static boolean retrieveBooleanFromSharedPreference(
            String sharedPreferenceName, String retrievalKey, Context context) {
        return context.getSharedPreferences(sharedPreferenceName,
                Activity.MODE_PRIVATE).getBoolean(
                retrievalKey, false);
    }


    /**
     * Common method to save value to shared preference.
     *
     * @param sharedPreferenceName the shared preference name
     * @param keyForValueToSave    the key for value to save
     * @param valueToSave          the value to save
     * @param context              the context
     */
    public static void saveToSharedPreference(String sharedPreferenceName,
                                              String keyForValueToSave, String valueToSave, Context context) {
        Editor editor = getSharedPreference(sharedPreferenceName, context)
                .edit();
        editor.putString(keyForValueToSave, valueToSave);
        editor.commit();
    }

    /**
     * String null empty validator.
     *
     * @param stringToValidate the string to validate
     * @return boolean true : not null & not empty false : null or empty
     * @author viva
     * @since 08*Aug*12 Utility method for validating a string for not null as
     * well as not empty
     */
    public static boolean stringNullEmptyValidator(String stringToValidate) {
        return (stringToValidate != null && !stringToValidate.trim().isEmpty()) ? true
                : false;
    }

    /**
     * Method for getting the UltaApplication Context.
     *
     * @return the ulta application context
     */
    private static Context getUltaApplicationContext() {
        return (Context) Ulta.getUltaInstance();
    }

    /**
     * @param dateFromCookie
     * @return Date
     */
    public static Date cookieDateUtility(String dateFromCookie) {
        DateFormat formatter;
        Date date = null;
        Logger.Log("[Utility]{cookieDateUtility}(dateFromCookie)>>"
                + dateFromCookie);
        if (stringNullEmptyValidator(dateFromCookie)) {
            if (!"null".equalsIgnoreCase(dateFromCookie.trim())) {
                formatter = new SimpleDateFormat(COOKIE_DATE_FORMAT);
                formatter.setTimeZone(TimeZone.getDefault());
                try {
                    date = (Date) formatter.parse(dateFromCookie);
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return date;
    }

    /**
     * hiding softInput
     *
     * @param activity
     * @param v
     */
    public static void hideKeyBoard(Activity activity, View v) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Method for displaying the error related to http errors
     *
     * @param errorMessage
     * @return
     */
    public static String httpDisplayError(String errorMessage) {
        Logger.Log("[Utility]{httpDisplayError}(errorCode)>>" + errorMessage);
        String displayMessage = null;
        String[] errorMessageArray = null;
        String errorCode = null;
        String errorCodeSubstring = null;
        if (errorMessage != null) {
            errorMessageArray = errorMessage.split(TILDE_MARK_SYMBOL);
            errorCode = errorMessageArray[0];
            if (errorCode != null) {
                if (!"401".equalsIgnoreCase(errorCode)) {
                    errorCodeSubstring = errorCode.substring(0, 1);
                    Logger.Log("[Utility]{httpDisplayError}(errorCode)>>"
                            + errorCode);
                    Logger.Log("[Utility]{httpDisplayError}(errorCodeSubstring)>>"
                            + errorCodeSubstring);
                    if (ERROR_5XX_PREFIX.equalsIgnoreCase(errorCodeSubstring)) {
                        displayMessage = SERVICE_UNAVAILABLE;
                    } else if (ERROR_4XX_PREFIX
                            .equalsIgnoreCase(errorCodeSubstring)) {
                        displayMessage = CLIENT_ERROR;
                    }
                } else {
                    displayMessage = errorCode;
                }

            }
        }
        return displayMessage;
    }

    /**
     * Method for displaying the error related to http errors
     *
     * @param errorMessage
     * @return
     */
    public static String formatDisplayError(String errorMessage) {
        String formattedErrorMessage = null;
        if (stringNullEmptyValidator(errorMessage)) {
            String[] errors = errorMessage.split(TILDE_MARK_SYMBOL);
            if (errors != null && errors.length > 1) {
                if ("JSE".equalsIgnoreCase(errors[0])) {
                    formattedErrorMessage = SERVICE_UNAVAILABLE;
                }
            } else if ("IOEXCEPTION".equalsIgnoreCase(errorMessage)) {
                formattedErrorMessage = NETWORK_ERROR_IO_EXCEPTION;
            } else {
                formattedErrorMessage = htmlCharSanitizer(errorMessage);
            }
        }
        return formattedErrorMessage;
    }

    /**
     * Method for sanitizing the string for HTML Characters
     *
     * @param stringToSanitize
     * @return
     */
    public static String htmlCharSanitizer(String stringToSanitize) {
        String sanitizedString = null;
        // Update this part with the values that need to be included in the
        // sanityRoutine
        String[] htmlCharactersToBeSanitized = new String[]{"<i>", "</i>"};
        boolean isFirstSanityRoutineRun = true;
        if (stringToSanitize != null && stringToSanitize.trim().length() > 0) {
            for (String sanitizerValues : htmlCharactersToBeSanitized) {
                if (isFirstSanityRoutineRun) {
                    sanitizedString = stringToSanitize.replaceAll(
                            sanitizerValues, EMPTY_STRING);
                    isFirstSanityRoutineRun = false;
                } else {
                    sanitizedString = sanitizedString.replaceAll(
                            sanitizerValues, EMPTY_STRING);
                }
            }
        }
        return sanitizedString;
    }

    /**
     * Method for formatting a 10 digit phone number to XXX-XXX-XXXX
     *
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneNumber(String phoneNumber) {
        String formattedPhoneNumber = null;
        StringBuilder phoneNumberFormatBuilder = null;
        if (phoneNumber != null) {
            int phoneNumLen = phoneNumber.trim().length();
            if (phoneNumLen == 10) {
                phoneNumberFormatBuilder = new StringBuilder();
                for (int i = 0; i < phoneNumLen; i++) {
                    phoneNumberFormatBuilder.append(phoneNumber.charAt(i));
                    if (i == 2 || i == 5) {
                        phoneNumberFormatBuilder.append("-");
                    }
                }
                formattedPhoneNumber = phoneNumberFormatBuilder.toString();
            }
        } else {
            Logger.Log("[Utility]{formatPhoneNumber}(phoneNumber)<Invalid PhoneNumber Format>"
                    + phoneNumber);
        }
        return formattedPhoneNumber;
    }

    /**
     * @param htmlString
     * @return
     */
    public static String removeHTML(String htmlString) {
        // Remove HTML tag from java String
        String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");
        // Remove Carriage return from java String
        noHTMLString = noHTMLString.replaceAll("\r", "<br/>");
        // Remove New line from java string and replace html break
        noHTMLString = noHTMLString.replaceAll("\n", " ");
        noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
        noHTMLString = noHTMLString.replaceAll("\"", "&quot;");
        return noHTMLString;
    }

    /**
     * Method to convert the string coming in one format to the one with
     * required resolution.
     */
    public static String modifyImageResolution(String imageURL, int height,
                                               int width) {
        StringBuilder imageURLBuilder = null;
        String initialUrl = null;
        if (imageURL != null && imageURL.length() > 0) {
            // Logger.Log("[WebserviceExecutionHelper]{changeImageURL}()<imageUrl>"+
            // imageURL);
            initialUrl = imageURL;
            if (imageURL != null && imageURL.trim().length() > 0) {
                int len = imageURL.length();
                // Logger.Log("[WebserviceExecutionHelper]{changeImageURL}()<len>"+
                // len);

                String stringValidateString;
                /*
				 * Code breaks if this condition is not checked. As sometimes
				 * the url fetched is of length <4
				 */
                if (len > 4) {
                    stringValidateString = imageURL.substring(len - 4, len);
                } else {
                    stringValidateString = imageURL;
                }
                // Logger.Log("[WebserviceExecutionHelper]{changeImageURL}()<stringValidateString>"+
                // stringValidateString);
                if (stringValidateString != null
                        && stringValidateString.trim().length() > 0) {
                    if (validatorList().contains(stringValidateString)) {
                        imageURLBuilder = new StringBuilder(imageURL.replace(
                                stringValidateString,
                                replacerString(height, width)));
                        // Logger.Log("[WebserviceExecutionHelper]{changeImageURL}()<imageURLBuilder.toString()>"+
                        // imageURLBuilder.toString());
                    }
                }
            }
        }

        return imageURLBuilder != null ? imageURLBuilder.toString()
                : initialUrl;
    }

    /**
     * Add if any particular item need to be considered, include the same
     *
     * @return List<String>
     */
    private static List<String> validatorList() {
        List<String> validList = new ArrayList<String>();
        validList.add("$lg$");
        validList.add("$sm$");
        validList.add("$md$");
        return validList;
    }

    /**
     * @param height
     * @param width
     * @return String
     */
    private static String replacerString(int height, int width) {
        final String heightParam = "hei";
        final String widthParam = "wid";
        String replacerString = heightParam.concat(EQUALS_SYMBOL)
                .concat(String.valueOf(height)).concat(AMPERSAND_SYMBOL)
                .concat(widthParam).concat(EQUALS_SYMBOL)
                .concat(String.valueOf(width));
        // Logger.Log("[WebserviceExecutionHelper]{replacerString}()<replacerString>"+
        // replacerString);
        return replacerString;
    }

    public static String delimitingTheString(String str) {
        String[] temp;
        String delimiter = ":";
        temp = str.split(delimiter);
        String result = temp[0] + ":" + temp[1];
        return result;
    }

    public static String uriEscape(String data) {
        data = data.replaceAll("%", "%25");
        data = data.replaceAll("#", "%23");
        data = data.replaceAll("\\?", "%3f");
        // data=data.replace('\\', "%27");
        data.replace('\\', '\u0D05').replaceAll("\u018E", "%27");

        return data;

    }

    /**
     * Helper method for sorting of the day numbers. Considering that the day
     * starts on Monday. If it need to be on Sunday cut the Sunday part and put
     * it as dayNumberMap.put("Sunday", 0); before dayNumberMap.put("Monday",
     * 1); or dayNumberMap.put("Sunday", 7);
     *
     * @param dayKey
     * @return
     */
    private static int dayNumberValidator(String dayKey) {
        Map<String, Integer> dayNumberMap = new HashMap<String, Integer>();
        dayNumberMap.put("Sunday", 0);
        dayNumberMap.put("Monday", 1);
        dayNumberMap.put("Tuesday", 2);
        dayNumberMap.put("Wednesday", 3);
        dayNumberMap.put("Thursday", 4);
        dayNumberMap.put("Friday", 5);
        dayNumberMap.put("Saturday", 6);
        Integer dayNumVal = dayNumberMap.get(dayKey);
        return dayNumVal != null ? dayNumVal : -1;
    }

    /**
     * Method for sorting
     *
     * @param storeTimingsToSort
     * @return
     */
    public static ArrayList<String> sortDatesBasedOnNumbers(
            List<String> storeTimingsToSort) {
        String[] initialSortHolder = null;
        TreeMap<Integer, String> sortedStoreTimings = null;
        if (storeTimingsToSort != null && !storeTimingsToSort.isEmpty()) {
            sortedStoreTimings = new TreeMap<Integer, String>();
            for (String x : storeTimingsToSort) {
                Logger.Log("[Utility]{sortDatesBasedOnNumbers()}<storeTimingsDetails>(START)"
                        + x);
                if (x != null && x.trim().length() > 1) {
                    initialSortHolder = x.split("-");
                    if (initialSortHolder[0] != null
                            && initialSortHolder[0].length() > 0) {
                        Logger.Log("[Utility]{sortDatesBasedOnNumbers()}<storeTimingsDetails>(initialSortHolder[0])"
                                + initialSortHolder[0]);
                        int dayNumberValue = dayNumberValidator(initialSortHolder[0]
                                .trim());
                        Logger.Log("[Utility]{sortDatesBasedOnNumbers()}<storeTimingsDetails>(dayNumberValue)"
                                + dayNumberValue);
                        if (dayNumberValue > -1) {
                            sortedStoreTimings.put(dayNumberValue, x);
                        }
                        Logger.Log("[Utility]{sortDatesBasedOnNumbers()}<dayNumberValue>(3)"
                                + dayNumberValue);
                    }
                }
            }
            return new ArrayList<String>(sortedStoreTimings.values());
        }
        return new ArrayList<String>();
    }

    public static boolean isNonCheckoutFormScreen(String screenName) {
        HashSet<String> values = new HashSet<String>();
        for (NonCheckoutFormScreens screen : NonCheckoutFormScreens.values()) {
            values.add(screen.name());
        }
        return values.contains(screenName);
    }

    public static boolean isUserSpecificRetrieveDataScreen(String screenName) {
        HashSet<String> values = new HashSet<String>();
        for (UserSpecificRetrieveDataScreen screen : UserSpecificRetrieveDataScreen
                .values()) {
            values.add(screen.name());
        }
        return values.contains(screenName);
    }

    public static boolean isCheckoutScreen(String screenName) {
        HashSet<String> values = new HashSet<String>();
        for (CheckoutScreens screen : CheckoutScreens.values()) {
            values.add(screen.name());
        }
        return values.contains(screenName);
    }

    public static boolean isDependentSubactivity(String screenName) {
        HashSet<String> values = new HashSet<String>();
        for (DependentSubActivities screen : DependentSubActivities.values()) {
            values.add(screen.name());
        }
        return values.contains(screenName);
    }

    public static String getDeviceOsVersion() {
        String osVersion;
        osVersion = Build.VERSION.RELEASE;
        return osVersion;

    }

    public static String getLoginStatus() {

        if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
            return UltaDataCache.getDataCacheInstance().getLoginName();
        } else {
            return "Test Android";
        }

    }

    /**
     * Method for generating secret key for encoding
     */
    //
    public static void generateSecretKey() {

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            if (null != UltaDataCache.getDataCacheInstance().getEncryptionKey()) {
                sr.setSeed(UltaDataCache.getDataCacheInstance()
                        .getEncryptionKey().getBytes());
            } else {
                sr.setSeed("any data used as random seed".getBytes());
            }
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            secretKey = new SecretKeySpec((kg.generateKey()).getEncoded(),
                    "AES");
            UltaDataCache.getDataCacheInstance().setSecretKey(secretKey);
        } catch (Exception e) {
            Log.e("Set Up secret Key", "AES secret key spec error");
        }
    }

    // /**
    // * Method to decrypt data
    // */
    //
    public static String decryptString(String encodedString, String secKey) {

        byte[] encodedByte = Base64.decode(encodedString, Base64.DEFAULT);
        byte[] decodedBytes = null;
        byte[] encodedKey = Base64.decode(secKey, Base64.DEFAULT);
        secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, secretKey);

            decodedBytes = c.doFinal(encodedByte);

            return new String(decodedBytes);
        } catch (Exception e) {
            Log.e("Decryption", "AES decryption error");
            return " ";
        }

    }

    public static void encrypPasswordt(String stringTobeEncoded) {

        try {
            Cipher c = Cipher.getInstance("AES");
            Utility.generateSecretKey();
            c.init(Cipher.ENCRYPT_MODE, UltaDataCache.getDataCacheInstance()
                    .getSecretKey());
            encodedPasswordBytes = c.doFinal(stringTobeEncoded.getBytes());
            UltaDataCache.getDataCacheInstance().setEncodedPasswordBytes(
                    encodedPasswordBytes);
        } catch (Exception e) {
            Log.e("Encryption", "AES encryption error");
        }
    }

    public static void encryptUserName(String stringTobeEncoded) {

        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, UltaDataCache.getDataCacheInstance()
                    .getSecretKey());
            encodedUserNameBytes = c.doFinal(stringTobeEncoded.getBytes());
            UltaDataCache.getDataCacheInstance().setEncodedUserNameBytes(
                    encodedUserNameBytes);
        } catch (Exception e) {
            Log.e("Encryption", "AES encryption error");
        }
    }

    public static void parseString(String strValue) {

        String paramValues[] = strValue.split("-");
        // navigate to page without params
        if (paramValues.length == 1) {
            strNativePage = paramValues[0].trim().toLowerCase();
        } else {
            strNativePage = paramValues[0].trim().toLowerCase();
            strNativePageWithParams = paramValues;
        }

    }

    public static int arrayContains(String[] array, String value) {
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(value)) {
                index = i;
                return index;
            }
        }
        return index;

    }

    public static Intent navigateToPage(Context context, String serviceParam,
                                        homePageSectionSlotBean infoBean) {

        int indexPage;
        if (serviceParam.startsWith("http")) {
            indexPage = 8;
        } else {
            parseString(serviceParam);
            indexPage = arrayContains(strNativePageName, strNativePage);
        }

        if (indexPage >= 0) {
            switch (indexPage) {
                case 0:// productdetailpage
                    if (strNativePageWithParams.length == 2) {
                        homePageSectionIntent = new Intent(context,
                                UltaProductDetailsActivity.class);
                        homePageSectionIntent.putExtra("id",
                                strNativePageWithParams[1].trim());
                    } else {
                        Logger.Log("GPShopper Notification Failure - productdetailpage parameter count mismatch");
                    }
                    break;

                case 1:// productlistpage
                    if (strNativePageWithParams.length == 2) {
                        homePageSectionIntent = new Intent(context,
                                UltaProductListActivity.class);
                        homePageSectionIntent.putExtra("search",
                                strNativePageWithParams[1]);
                    } else {
                        Logger.Log("GPShopper Notification Failure - productListPage parameter count mismatch");
                    }

                    break;

                case 2: // productlistpagebrand

                    if (strNativePageWithParams.length == 3) {
                        homePageSectionIntent = new Intent(context,
                                UltaProductListActivity.class);
                        homePageSectionIntent.putExtra("fromShopByBrandsPage",
                                "fromShopByBrandsPage");
                        homePageSectionIntent.putExtra("selectedBrandId",
                                strNativePageWithParams[1]);
                        homePageSectionIntent.putExtra("altText",
                                strNativePageWithParams[2]);
                    } else {
                        Logger.Log("GPShopper Notification Failure - productListPage with brands parameter count mismatch");
                    }
                    break;

                case 3: // productlistpagepromo

                    if (strNativePageWithParams.length == 3) {
                        homePageSectionIntent = new Intent(context,
                                UltaProductListActivity.class);
                        homePageSectionIntent.setAction("fromPromotion");
                        homePageSectionIntent.putExtra("promotionId",
                                strNativePageWithParams[1]);
                        homePageSectionIntent.putExtra("altText",
                                strNativePageWithParams[2]);

                    } else {
                        Logger.Log("GPShopper Notification Failure - productListPage with Promo parameter count mismatch");
                    }
                    break;

                case 4:// shoppage
                    homePageSectionIntent = new Intent(context,
                            ShopListActivity.class);
                    break;

                case 5:// weeklyaddpage
                    homePageSectionIntent = new Intent(context,
                            WebViewActivity.class);
                    homePageSectionIntent.putExtra("navigateToWebView",
                            WebserviceConstants.FROM_WEEKLYAd);
                    homePageSectionIntent.putExtra("title", "Weekly Ads");

                    break;
                case 6:// newarrivalpage
                    homePageSectionIntent = new Intent(context,
                            UltaProductListActivity.class);
                    homePageSectionIntent.setAction("fromHomeByNewArrivals");

                    break;

                case 7:// rewardpage

                    homePageSectionIntent = new Intent(context,
                            NonSignedInRewardsActivity.class);

                    break;

                case 8:
                    homePageSectionIntent = new Intent(context,
                            WebViewActivity.class);
                    homePageSectionIntent.putExtra("navigateToWebView",
                            WebserviceConstants.FROM_PUSHNOTIFICATION);
                    homePageSectionIntent.putExtra("title", ""+infoBean.getTitle());
                    homePageSectionIntent.putExtra("url", serviceParam);
                    break;

                case 9:
                    homePageSectionIntent = new Intent(context,
                            OlapicActivity.class);

                    if (null != infoBean) {

                        if (null != infoBean.getServiceParameters()) {

                            String streamIdAndOlapicText = infoBean
                                    .getServiceParameters();
                            String streamId[] = streamIdAndOlapicText.split("-");

                            if (streamId.length > 1) {
                               /* if (null != streamId[1]) {
                                    UltaDataCache.getDataCacheInstance()
                                            .setStreamId(streamId[1].trim());
                                }*/

                                UltaDataCache.getDataCacheInstance()
                                        .setOlpaicHomeGalleryHeadingText(
                                                infoBean.getTitle());

                            } /*else {
                                if (null != streamId[0]) {
                                    UltaDataCache.getDataCacheInstance()
                                            .setStreamId(streamId[0].trim());
                                }
                            }*/

                        }
                    }
                    break;
                case 10:
                    homePageSectionIntent = new Intent(context,
                            UltaMateCreditCardActivity.class);
                    break;
                default:

                    break;
            }

            return homePageSectionIntent;

        }

        return null;
    }

}
