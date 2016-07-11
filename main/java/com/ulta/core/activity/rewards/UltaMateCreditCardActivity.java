package com.ulta.core.activity.rewards;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.activity.account.WebViewActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.account.MyPurchasesBean;
import com.ulta.core.bean.account.ProfileBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

public class UltaMateCreditCardActivity extends UltaBaseActivity implements View.OnClickListener, TextWatcher, OnSessionTimeOut {
    private ImageView mUltamate_creditcard_landing_image;
    private ProgressBar mProgressBar;
    private Button mManageAccount, mApplyNow;
    private LinearLayout mHyperlink;
    private TextView mTxtViewFAQ, mTxtViewTermAndCondition;

    private static final int REQ_CODE_ULTAMATE_CARD_APPLY = 444;
    private static final int REQ_CODE_LOGIN_ULTAMATE_CARD_APPLY = 555;
    EditText rewardIdET;
    TextView idErrorText;
    private Drawable originalDrawable;
    String memberID;
    private String firstName, lastName, dateOfBirth, userName,
            phNumber, addLine1, addLine2,
            city, state, country, zip;
    private ProfileBean profileBean;
    boolean isProfileWebService = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivity(UltaMateCreditCardActivity.this);
        setContentView(R.layout.activity_ultamate_creditcard);
        init();
        setSmallFontTitle("Rewards Credit Card");
    }

    private void init() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mUltamate_creditcard_landing_image = (ImageView) findViewById(R.id.ultamate_creditcard_landing_image);
        mHyperlink = (LinearLayout) findViewById(R.id.hyperlink);
        checkDensityAndSetImage(mUltamate_creditcard_landing_image,
                WebserviceConstants.ULTAMATE_CREDIT_CARD_LANDING,
                R.drawable.cardhome_fallback, "ULTAMATECARDLANDING", mProgressBar, false);
        mManageAccount = (Button) findViewById(R.id.manageAccount);
        mApplyNow = (Button) findViewById(R.id.applyNow);
        mTxtViewFAQ = (TextView) findViewById(R.id.txtCreditcardfaq);
        mTxtViewTermAndCondition = (TextView) findViewById(R.id.txtCreditcard_termsandconditions);
        mApplyNow.setOnClickListener(this);
        mManageAccount.setOnClickListener(this);
        mTxtViewFAQ.setOnClickListener(this);
        mTxtViewTermAndCondition.setOnClickListener(this);
        ViewTreeObserver observer = mUltamate_creditcard_landing_image.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                checkImageVisibility();
            }
        });
    }

    private void checkImageVisibility() {
        int height = mUltamate_creditcard_landing_image.getHeight();
        if (height > 100) {
            mHyperlink.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applyNow:
                onApplyButtonClick();
                break;
            case R.id.manageAccount:
                manageAccountButtonClick();
                break;
            case R.id.txtCreditcardfaq:
                faqTxtViewClick();
                break;
            case R.id.txtCreditcard_termsandconditions:
                termAndCondtionTxtViewClick();
                break;
            default:
                break;
        }
    }

    private void faqTxtViewClick() {
        Intent faqIntent = new Intent(UltaMateCreditCardActivity.this,
                WebViewActivity.class);
        faqIntent.putExtra("navigateToWebView",
                WebserviceConstants.FROM_ULTAMATE_CARD);
        faqIntent.putExtra("title", "FAQs");
        faqIntent.putExtra("url", WebserviceConstants.CREDITCARD_FAQ_URL);
        startActivity(faqIntent);
    }

    private void termAndCondtionTxtViewClick() {
        Intent termIntent = new Intent(UltaMateCreditCardActivity.this,
                WebViewActivity.class);
        termIntent.putExtra("navigateToWebView",
                WebserviceConstants.FROM_ULTAMATE_CARD);
        termIntent.putExtra("title", "Terms & Conditions");
        termIntent.putExtra("url", WebserviceConstants.CREDITCARD_TERM_CONDITION_URL);
        startActivity(termIntent);
    }

    private void manageAccountButtonClick() {
        Intent manageAccountIntent = new Intent(UltaMateCreditCardActivity.this,
                WebViewActivity.class);
        manageAccountIntent.putExtra("navigateToWebView",
                WebserviceConstants.FROM_ULTAMATE_CARD);
        manageAccountIntent.putExtra("title", "Manage Account");
        //logged in user with ultamate card
        if (UltaDataCache.getDataCacheInstance().isLoggedIn() && null != Utility.retrieveFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                UltaMateCreditCardActivity.this) && !Utility.retrieveFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                UltaMateCreditCardActivity.this).trim().isEmpty()) {
            if (null != UltaDataCache.getDataCacheInstance().getAppConfig()) {
                //Ultamate rewards credit card user
                if (Utility.retrieveFromSharedPreference(
                        UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                        UltaMateCreditCardActivity.this).trim().equalsIgnoreCase("Ultamate Rewards Credit Card")) {
                    manageAccountIntent.putExtra("url", UltaDataCache.getDataCacheInstance().getAppConfig().getManageAccountPLCC());
                } else {
                    //Ultamate Rewards MasterCard user
                    manageAccountIntent.putExtra("url", UltaDataCache.getDataCacheInstance().getAppConfig().getManageAccountCBCC());
                }
            }


        }
        //guest user or logged in user with no card
        else {
            if (null != UltaDataCache.getDataCacheInstance().getAppConfig()) {
                manageAccountIntent.putExtra("url", UltaDataCache.getDataCacheInstance().getAppConfig().getManageAccountNoCard());
            }
        }
        startActivity(manageAccountIntent);

    }

    private void rewardUserApplyFn() {
        if (UltaDataCache.getDataCacheInstance().isLoggedIn() && Utility.retrieveBooleanFromSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, getApplicationContext())) {
            //Check if already card is available for user
            if (UltaDataCache.getDataCacheInstance().isLoggedIn() && null != Utility.retrieveFromSharedPreference(
                    UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                    UltaMateCreditCardActivity.this) && !Utility.retrieveFromSharedPreference(
                    UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                    UltaMateCreditCardActivity.this).trim().isEmpty()) {
                if (Utility.retrieveFromSharedPreference(
                        UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                        UltaMateCreditCardActivity.this).trim().equalsIgnoreCase("Ultamate Rewards Credit Card") || Utility.retrieveFromSharedPreference(
                        UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                        UltaMateCreditCardActivity.this).trim().equalsIgnoreCase("Ultamate Rewards MasterCard")) {
                    String message=WebserviceConstants.USER_HAVING_CARD_MESSAGE;
                    final Dialog alertDialog = showAlertDialog(
                            UltaMateCreditCardActivity.this,
                            message, "", "OK", "");
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    mDisagreeButton.setVisibility(View.GONE);
                    messageTV.setVisibility(View.GONE);
                    mAgreeButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                }
            } else {
                //redirect to application Webview
                String url = UltaDataCache.getDataCacheInstance().getAppConfig().getApplyUltaCC() +
                        Utility.retrieveFromSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.BEAUTY_CLUB_NUMBER, getApplicationContext());
                Intent applyNowWebView = new Intent(UltaMateCreditCardActivity.this,
                        WebViewActivity.class);
                applyNowWebView.putExtra("navigateToWebView",
                        WebserviceConstants.FROM_ULTAMATE_CARD);
                applyNowWebView.putExtra("title", "Apply Now");
                applyNowWebView.putExtra("url", url);
                startActivity(applyNowWebView);
            }
        } else {
            showBottomSheet();
        }
    }


    private void onApplyButtonClick() {
        //Logged In user
        if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
            //Reward Member
            if (Utility.retrieveBooleanFromSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, getApplicationContext())) {
                //redirect to application Webview
                rewardUserApplyFn();
            }
            //Non reward member
            else {
                showBottomSheet();

            }
        }
        //Guest user
        else {
            //Redirect to Login/Create Account page
            Intent loginIntent = new Intent(
                    UltaMateCreditCardActivity.this, LoginActivity.class);
            loginIntent.putExtra("origin", "isfromApplyUltamateCard");
            startActivityForResult(loginIntent, REQ_CODE_LOGIN_ULTAMATE_CARD_APPLY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_CODE_ULTAMATE_CARD_APPLY) {
            rewardUserApplyFn();
        } else if (resultCode == RESULT_OK && requestCode == REQ_CODE_LOGIN_ULTAMATE_CARD_APPLY) {
            if (isUltaCustomer(UltaMateCreditCardActivity.this)) {
                rewardUserApplyFn();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showBottomSheet() {
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.alert_dialog); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView heading = (TextView) mBottomSheetDialog.findViewById(R.id.heading);
        heading.setText("Ultamate Rewards Required to Apply");
        heading.setTextSize(18);
        TextView message = (TextView) mBottomSheetDialog.findViewById(R.id.message);
        message.setVisibility(View.GONE);
        Button mAgreeButton = (Button) mBottomSheetDialog.findViewById(R.id.btnAgree);
        mAgreeButton.setPadding(40, 0, 40, 10);
        mAgreeButton.setText("Yes, sign me up!");
        Button mDisagreeButton = (Button) mBottomSheetDialog.findViewById(R.id.btnDisagree);
        mDisagreeButton.setPadding(40, 0, 40, 10);
        mDisagreeButton.setText("I'm already a member");
        mAgreeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                invokeMyProfileDetails();
            }
        });
        mDisagreeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showLinkUltmateRewardsPopup();
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.show();
    }

    private void showLinkUltmateRewardsPopup() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        final Dialog alertDialog = new Dialog(UltaMateCreditCardActivity.this, R.style.AppCompatAlertDialogStyle);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.link_rewards_account_layout);
        TextView headingTV = (TextView) alertDialog.findViewById(R.id.heading);
        rewardIdET = (EditText) alertDialog.findViewById(R.id.rewardId);
        rewardIdET.addTextChangedListener(this);
        originalDrawable = rewardIdET.getBackground();
        idErrorText = (TextView) alertDialog.findViewById(R.id.idErrorText);
        Button mAgreeButton = (Button) alertDialog.findViewById(R.id.btnAgree);
        Button mDisagreeButton = (Button) alertDialog.findViewById(R.id.btnDisagree);
        headingTV.setText("Link your Ultamate Rewards");
        mAgreeButton.setText("Link");
        mDisagreeButton.setText("Cancel");
        alertDialog.getWindow().setLayout((6 * width) / 7,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mAgreeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                memberID = rewardIdET.getText().toString().trim();
                if (null != memberID && !memberID.isEmpty() && memberID.length() > 0) {
                    fnInvokeJoinRewardsProgramme(false);
                    alertDialog.dismiss();
                } else {
                    setError(rewardIdET, idErrorText, "Enter Beauty Club Membership Id");
                }

            }
        });
        mDisagreeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void setError(EditText editText, TextView errorTV, String message) {
        errorTV.setText("" + message);
        errorTV.setVisibility(View.VISIBLE);
        editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (null != rewardIdET) {
            if (s.hashCode() == rewardIdET.getText().hashCode()) {
                rewardIdET.setBackgroundDrawable(originalDrawable);
                idErrorText.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Invoke my profile details.
     */
    private void invokeMyProfileDetails() {
        pd = new ProgressDialog(UltaMateCreditCardActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setMessage(LOADING_PROGRESS_TEXT);
        pd.setCancelable(false);
        pd.show();
        InvokerParams<ProfileBean> invokerParams = new InvokerParams<ProfileBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.MY_PROFILE_DETAILS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setCookieHandlingSkip(false);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populateRetrieveMyProfileDetailsHandlerParameters());
        invokerParams.setUltaBeanClazz(ProfileBean.class);
        RetrieveMyProfileDetailsHandler retrieveMyProfileDetailsHandler = new RetrieveMyProfileDetailsHandler();
        invokerParams.setUltaHandler(retrieveMyProfileDetailsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<MyAccountActivity><invokeMyProfileDetails()><UltaException>>"
                    + ultaException);

        }

    }

    /**
     * Populate retrieve my profile details handler parameters.
     *
     * @return the map
     */
    private Map<String, String> populateRetrieveMyProfileDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "2");
        return urlParams;
    }

    /**
     * The Class RetrieveMyProfileDetailsHandler.
     */
    public class RetrieveMyProfileDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveMyProfileDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            // askRelogin("");
            if (null != getErrorMessage()) {

                if (getErrorMessage().startsWith("401")) {
                    isProfileWebService = true;
                    askRelogin(UltaMateCreditCardActivity.this);

                } else {
                    try {

                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                UltaMateCreditCardActivity.this);
                        setError(UltaMateCreditCardActivity.this, getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                Logger.Log("<RetrieveMyProfileDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                profileBean = (ProfileBean) getResponseBean();
                if (null != profileBean) {
                    if (null != profileBean.getErrorInfos()) {
                        setError(UltaMateCreditCardActivity.this, profileBean
                                .getErrorInfos().get(0));
                    }
                    String beautyClubMember = profileBean.getBeautyClubNumber();
                    if (null != beautyClubMember) {
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.BEAUTY_CLUB_NUMBER, beautyClubMember,
                                getApplicationContext());
                    }
                    dateOfBirth = profileBean.getDateOfBirth();
                    firstName = profileBean.getFirstName();
                    lastName = profileBean.getLastName();
                    userName = profileBean.getEmail();
                    city = profileBean.getHomeAddress().getCity();
                    phNumber = profileBean.getHomeAddress().getPhoneNumber();
                    addLine1 = profileBean.getHomeAddress().getAddress1();
                    addLine2 = profileBean.getHomeAddress().getAddress2();
                    state = profileBean.getHomeAddress().getState();
                    country = profileBean.getHomeAddress().getCountry();
                    zip = profileBean.getHomeAddress().getPostalCode();
                    fnInvokeJoinRewardsProgramme(true);

                }
            }
        }
    }

    private void fnInvokeJoinRewardsProgramme(Boolean isJoin) {
        try {
            if (null == pd) {
                pd = new ProgressDialog(UltaMateCreditCardActivity.this);
                setProgressDialogLoadingColor(pd);
                pd.setMessage(LOADING_PROGRESS_TEXT);
                pd.setCancelable(false);
            }
            if (null != pd && !pd.isShowing()) {
                pd.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        InvokerParams<MyPurchasesBean> invokerParams = new InvokerParams<MyPurchasesBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.JOIN_REWARDS);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateOrderDetailsHandlerParameters(isJoin));
        invokerParams.setUltaBeanClazz(MyPurchasesBean.class);
        JoinRewardsHandler orderDetailsHandler = new JoinRewardsHandler();
        invokerParams.setUltaHandler(orderDetailsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);

        }
    }

    private Map<String, String> populateOrderDetailsHandlerParameters(Boolean isJoin) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");

        if (isJoin) {
            urlParams.put("memberData.emailAddress", userName);
            urlParams.put("memberData.firstName", firstName);
            urlParams.put("memberData.lastName", lastName);
            urlParams.put("memberData.homeAddr1", addLine1);
            urlParams.put("memberData.homeAddr2", addLine2);
            urlParams.put("memberData.cityName", city);
            urlParams.put("memberData.stateCode", state);
            urlParams.put("memberData.zipCode", zip);
            urlParams.put("value.dateOfBirth", dateOfBirth);
            urlParams.put("joinOrActivate", "join");
            urlParams.put("zip", zip);
            urlParams.put("memberData.phoneNum",
                    Utility.formatPhoneNumber(phNumber));

        } else {
            urlParams.put("joinOrActivate", "Activate");
            urlParams.put("beautyClubNumber", memberID);
        }
        return urlParams;
    }

    /**
     * The Class RetrievePaymentDetailsHandler.
     */
    public class JoinRewardsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<OrderDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    try {
                        if (null != pd && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isProfileWebService = false;
                    askRelogin(UltaMateCreditCardActivity.this);

                } else {
                    try {
                        if (null != pd && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        notifyUser(Utility.formatDisplayError(getErrorMessage()),
                                UltaMateCreditCardActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                try {
                    if (null != pd && pd.isShowing()) {
                        pd.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyPurchasesBean ultaBean = (MyPurchasesBean) getResponseBean();
                trackAppAction(
                        UltaMateCreditCardActivity.this,
                        WebserviceConstants.LOYALTY_ACCOUNT_CREATED_EVENT_ACTION);
                if (null != ultaBean) {
                    List<String> errors = ultaBean.getErrorInfos();
                    if (null != errors && !(errors.isEmpty())) {
                        try {
                            notifyUser(errors.get(0), UltaMateCreditCardActivity.this);

                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }
                    } else {
                        if (null != ultaBean) {
                            if (null != ultaBean.getComponent() && null != ultaBean.getComponent().getBeautyClubNumber()) {
                                UltaDataCache.getDataCacheInstance().setRewardMember(
                                        true);
                                Utility.saveToSharedPreference(
                                        UltaConstants.REWARD_MEMBER,
                                        UltaConstants.IS_REWARD_MEMBER,
                                        true, getApplicationContext());
                                Utility.saveToSharedPreference(
                                        UltaConstants.REWARD_MEMBER,
                                        UltaConstants.BEAUTY_CLUB_NUMBER, ultaBean.getComponent().getBeautyClubNumber(),
                                        getApplicationContext());
                                rewardUserApplyFn();
                            }
                        }

                    }
                } else {
                    notifyUser("Please try later", UltaMateCreditCardActivity.this);
                }

            }
        }
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            if (isProfileWebService) {
                invokeMyProfileDetails();
            } else {
                fnInvokeJoinRewardsProgramme(false);
            }
        }
    }

}
