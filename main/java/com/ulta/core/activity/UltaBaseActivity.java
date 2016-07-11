/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.cashstar.ui.activity.CashStarHomeActivity;
import com.google.zxing.integration.android.IntentResult;
import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.about.AboutUsActivity;
import com.ulta.core.activity.about.LegalActivity;
import com.ulta.core.activity.about.PrivacyPolicyActivity;
import com.ulta.core.activity.account.ForgotLoginActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.activity.account.MyAccountActivity;
import com.ulta.core.activity.account.MyOrderHistoryActivity;
import com.ulta.core.activity.account.OlapicActivity;
import com.ulta.core.activity.account.RegisterDetailsActivity;
import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.account.WebViewActivity;
import com.ulta.core.activity.checkout.ReviewOrderActivity;
import com.ulta.core.activity.myprofile.FavoritesActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.activity.product.ViewItemsInBasketActivity;
import com.ulta.core.activity.rewards.GiftCardsTabActivity;
import com.ulta.core.activity.rewards.NonSignedInRewardsActivity;
import com.ulta.core.activity.rewards.UltaMateCreditCardActivity;
import com.ulta.core.activity.stores.StoresActivity;
import com.ulta.core.bean.StatusOnlyResponseBean;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.bean.account.PushNotificationBean;
import com.ulta.core.bean.checkout.ReviewOrderBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.ConversantUtility;
import com.ulta.core.util.OmnitureTracking;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.util.scan.ScanIntentIntegrator;
import com.ulta.core.widgets.flyin.OnBagPressedListener;
import com.ulta.core.widgets.flyin.OnMenuPressedListener;
import com.ulta.core.widgets.flyin.OnPermissionCheck;
import com.ulta.core.widgets.flyin.OnScanPressedListener;
import com.ulta.core.widgets.flyin.OnSearchPressedListener;
import com.ulta.core.widgets.flyin.OnTitleBarPressed;
import com.ulta.core.widgets.flyin.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.util.Utility.displayUserErrorMessage;

//
//import android.widget.Toast;
//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class UltaBaseActivity.
 */
public class UltaBaseActivity extends ActionBarActivity implements
        OnMenuPressedListener, OnItemClickListener, OnTitleBarPressed,
        OnLogout, OnSearchPressedListener, OnScanPressedListener,
        OnBagPressedListener, TextWatcher {

    public static final int REQ_CODE_RELOGIN = 10000;

    /**
     * The Constant OPTION_HOME.
     */
    private static final int OPTION_HOME = 1;

    /**
     * The Constant OPTION_SCAN.
     */
    private static final int OPTION_FAVORITES = 2;
    /**
     * The Constant OPTION_BASKET.
     */
    private static final int OPTION_STORE = 3;
    /**
     * The Constant OPTION_SHOP.
     */
    private static final int OPTION_SHOP = 4;

    /**
     * The Constant OPTION_ORDER_STATUS
     */
    private static final int OPTION_CHECK_ORDER_STATUS = 5;

    /**
     * The Constant OPTION_GIFT_CARD.
     */
    private static final int OPTION_GIFT_CARD_BAL = 6;

    /**
     * The Constant OPTION_CONTACT_US.-EMAIL
     */
    private static final int OPTION_CONTACT_US = 8;

    /**
     * The Constant OPTION_USER_AGREEMENT.-LEGAL
     */
    private static final int OPTION_USER_AGREEMENT = 9;

    /**
     * The Constant OPTION_PRIVACY_POLICY.
     */
    private static final int OPTION_PRIVACY_POLICY = 10;
    /**
     * The Contstant OPTION_CALL_SERVIE-CALL
     */

    private static final int OPTION_CALL_SERVIE = 11;

    /**
     * The Constant OPTION_ULTAMATE_CREDIT_CARD.
     */
    private static final int OPTION_ULTAMATE_CREDIT_CARD = 12;
    /**
     * The Constant OPTION_MY_ACCOUNT.
     */
    private static final int OPTION_MY_ACCOUNT = 13;

    /**
     * The Constant OPTION_ABOUT.
     */
    private static final int OPTION_ABOUT = 14;


    /**
     * The title bar.
     */
    public TitleBar titleBar;

    /**
     * The pd.
     */
    protected ProgressDialog pd;

    SeparatedListAdapter adapter;
    CustomAdapter miscAdapter;
    private static int itemCountInBasket;
    private static int activityIndicator = 0;
    // private EditText txtSearch;
    // private Button btnSearch;
    // private LinearLayout mainLayout;
    private String result1[];
    ArrayList<String> resultContact = new ArrayList<String>();

    private UltaBaseActivity activity;
    /*
     * private boolean is401; private String check;
     */
    boolean isOnCreateCalled = false;

    private boolean isAppClosedAndStaySignedIn = false;

    // private UemAction loginAction;

    private String userName;

	/* private String password; */

    private boolean isAskRelogin = false;

    private SharedPreferences staySignedInSharedPreferences,
            pushNotificationSharedPreferences;

    private SharedPreferences mRegistrationIdSharedPreferences;

    private Editor staySignedInEditor;

    public boolean isPersisted = false;

    private Typeface helveticaRegularTypeface;

    /**
     * Footer Views
     **/
    public LinearLayout mSubTotalFooterLayout;
    public LinearLayout mShippingTypeFooterLayout;
    public LinearLayout mTaxFooterLayout;
    public LinearLayout mTotalLayout;
    public LinearLayout mGiftBoxAndNoteFooterLayout;
    public LinearLayout mRedeemPointsFooterLayout;
    public LinearLayout mCouponDiscountLayout;
    public LinearLayout mAdditionalDiscountLayout;

    public TextView mSubTotalValueTextView;
    public TextView mTaxValueTextView;
    public TextView mTotalValueTextView;
    public TextView mShippingTypeTextView;
    public TextView mShippingTypeValueTextView;
    public TextView mGiftBoxAndNoteValueTextView;
    public TextView mGiftBoxAndNoteTextView;
    private TextView mSubTotalTextView;
    private TextView mTaxTextView;
    public TextView mReedemablePointsTextView;
    public TextView mReedemablePointsTextViewValue;
    public TextView mTvCouponDiscount;
    public TextView mTvCouponDiscountValue;
    public TextView mTvAdditionalDiscount;
    public TextView mTvAdditionalDiscountValue;

    public ImageView mExpandImageView;

    public int toShowCouponCodeLayout = 1;

    public String mStandardAmount;
    public String mFreeAmount;
    public String mUpsTwoDayAirAmount;
    public String mUpsNextDayAirAmount;
    public String mGiftBoxAmount;

    private OnSessionTimeOut mOnSessionTimeOut;

    private OnPermissionCheck mOnPermissionCheck;
    // navigation drawer
    DrawerLayout drawerLayout;
    LinearLayout navigationDrawer;
    boolean orderstaus = false;
    private Dialog dialog, alertDialog;
    private String username, passwordLogin;
    private EditText editUsername, editPasswordLogin;
    private TextView usernameErrorText, passwordErrorText;
    private Drawable originalDrawable;
    // private FrameLayout loading;
    /**
     * The username length error message.
     */
    private static String USERNAME_LENGTH_ERROR_MESSAGE = "Please enter your User name to Login";

    /**
     * The password length error message.
     */
    private static String PASSWORD_LENGTH_ERROR_MESSAGE = "Please enter your password to Login";
    private String origin = null;
    private boolean isStaySignedIn = true;
    private byte[] loginPasswordBytes;

    private byte[] loginUserNameBytes;
    private String loginPassword;

    private String loginUsername;
    static Button notifCount;

    protected Button mAgreeButton, mDisagreeButton;
    protected TextView messageTV;

    public boolean isOrderstaus() {
        return orderstaus;
    }

    public void setOrderstaus(boolean orderstaus) {
        this.orderstaus = orderstaus;
    }

    Bitmap bitmapValue = null;
    private boolean sideMenuCall = false;

    @Override
    protected void onPause() {
        // if (isOpened()) {
        // toggle();
        // }
        if (null != drawerLayout
                && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(navigationDrawer);
        }
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("oldUser", Utility.retrieveFromSharedPreference(
                UltaConstants.LOGGED_MAIL_ID, UltaBaseActivity.this));
        editor.putBoolean("onPauseCalled", true);
        editor.commit();
        super.onPause();
        // OmnitureMeasurement.stopActivity();
    }

    @Override
    protected void onResume() {
        // invalidateSideMenu();
        setBasketCount(getItemCountInBasket());
        if (!isOnCreateCalled) {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            String oldUser = preferences.getString("oldUser", null);
            String newUser = Utility.retrieveFromSharedPreference(
                    UltaConstants.LOGGED_MAIL_ID, UltaBaseActivity.this);
            if (Utility.isNonCheckoutFormScreen(getClass().getSimpleName()
                    .toUpperCase())
                    || Utility.isDependentSubactivity(getClass()
                    .getSimpleName().toUpperCase())) {
                if (!isUltaCustomer(UltaBaseActivity.this) || null == newUser
                        || (null != oldUser && !oldUser.equals(newUser))) {
                    Logger.Log(":::::::::::::::::: "
                            + getClass().getSimpleName().toUpperCase());
                    finish();
                }
            } else if (Utility.isUserSpecificRetrieveDataScreen(getClass()
                    .getSimpleName().toUpperCase())) {
                if (!isUltaCustomer(UltaBaseActivity.this) || null == newUser) {
                    finish();
                } else if (null != oldUser && !oldUser.equals(newUser)) {
                    refreshPage();
                }
            } else if (Utility.isCheckoutScreen(getClass().getSimpleName()
                    .toUpperCase())) {
                if ((!isUltaCustomer(UltaBaseActivity.this) && !UltaDataCache
                        .getDataCacheInstance().isAnonymousCheckout())
                        || null == newUser
                        || (null != oldUser && !oldUser.equals(newUser))) {
                    Intent intentForBasket = new Intent(UltaBaseActivity.this,
                            ViewItemsInBasketActivity.class);
                    intentForBasket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentForBasket);
                }
            }
        }

        staySignedInSharedPreferences = getSharedPreferences(
                WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);

        mRegistrationIdSharedPreferences = getSharedPreferences(
                WebserviceConstants.CHANNEL_ID_PREF, 0);

        boolean isActive = staySignedInSharedPreferences.getBoolean(
                WebserviceConstants.IS_PUSH_NOTIFICATION_ON, true);

        String registrationId = mRegistrationIdSharedPreferences.getString(
                WebserviceConstants.PUSH_CHANNEL_ID, "");

        String userName = staySignedInSharedPreferences.getString(
                WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");

        String loginPassword = staySignedInSharedPreferences.getString(
                WebserviceConstants.STAY_SIGNED_IN_PASSWORD, " ");

        String secretKey = staySignedInSharedPreferences.getString(
                WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");

        if (!UltaDataCache.getDataCacheInstance().isLoggedIn()) {

            if (!loginPassword.equalsIgnoreCase(" ")
                    && !userName.equalsIgnoreCase(" ")) {
                loginPassword = Utility.decryptString(loginPassword, secretKey);
                userName = Utility.decryptString(userName, secretKey);
            }

            if (!(userName.equalsIgnoreCase(" ") && loginPassword
                    .equalsIgnoreCase(" "))) {
                isPersisted = true;
                invokeLogin(userName, loginPassword, registrationId, isActive);
            }
        }

        isOnCreateCalled = false;
        super.onResume();
    }

	/*
     * (non-Javadoc)
	 *
	 * @see
	 * com.suse.android.sidemenu.SidemenuActivity#onCreate(android.os.Bundle)
	 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isOnCreateCalled = true;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (!Ulta.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        createMenuData();
        // setUpSideMenu();
        if (UltaDataCache.getDataCacheInstance().isIfNotSignedInClearSession()) {
            SharedPreferences staySignedInSharedPref = getSharedPreferences(
                    WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);

            boolean isStaySignedIn = staySignedInSharedPref.getBoolean(
                    WebserviceConstants.IS_STAY_SIGNED_IN, false);

            if (!isStaySignedIn) {
                invokeLogout();
                UltaDataCache.getDataCacheInstance()
                        .setUpdateBasketAndFavCount(false);
                UltaDataCache.getDataCacheInstance()
                        .setIfNotSignedInClearSession(false);
            }
        }
        // If we are supposed to call login service to update basket and fav
        // count please uncomment the below piece of code.

        // if (UltaDataCache.getDataCacheInstance().isUpdateBasketAndFavCount())
        // {
        // SharedPreferences staySignedInSharedPref = getSharedPreferences(
        // WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);
        //
        // boolean isStaySignedIn = staySignedInSharedPref.getBoolean(
        // WebserviceConstants.IS_STAY_SIGNED_IN, false);
        //
        // if (isStaySignedIn) {
        // isAppClosedAndStaySignedIn = true;
        // boolean isActive = staySignedInSharedPref.getBoolean(
        // WebserviceConstants.IS_PUSH_NOTIFICATION_ON, true);
        //
        // mRegistrationIdSharedPreferences = getSharedPreferences(
        // WebserviceConstants.CHANNEL_ID_PREF, 0);
        //
        // String registrationId = mRegistrationIdSharedPreferences
        // .getString(WebserviceConstants.PUSH_CHANNEL_ID, "");
        // String secretKey = staySignedInSharedPref.getString(
        // WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
        // String loginPassword = Utility.decryptString(
        // staySignedInSharedPref.getString(
        // WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
        // " "), secretKey);
        // String userName = Utility.decryptString(staySignedInSharedPref
        // .getString(WebserviceConstants.STAY_SIGNED_IN_USERNAME,
        // " "), secretKey);
        //
        // invokeLogin(userName, loginPassword, registrationId, isActive);
        // UltaDataCache.getDataCacheInstance()
        // .setUpdateBasketAndFavCount(false);
        // }
        // }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 5000) {
            final IntentResult scanResult = ScanIntentIntegrator
                    .parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null) {
                UltaDataCache.getDataCacheInstance().setCalledOnlyOnce(false);
                final String barcode = scanResult.getContents();
                Logger.Log("Scanned barcode is " + barcode);
                Intent intentToSearchActivity = new Intent(
                        UltaBaseActivity.this, SearchActivity.class);
                intentToSearchActivity.setAction("fromSearch");
                intentToSearchActivity.putExtra("search", barcode);
                intentToSearchActivity.putExtra("scan", "scan");
                startActivity(intentToSearchActivity);
            }
        }
        if (resultCode == RESULT_OK
                && requestCode == UltaConstants.REQ_CODE_RELOGIN) {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            String oldUser = preferences.getString("oldUser", null);
            String newUser = Utility.retrieveFromSharedPreference(
                    UltaConstants.LOGGED_MAIL_ID, UltaBaseActivity.this);
            if (Utility.isCheckoutScreen(getClass().getSimpleName()
                    .toUpperCase())) {
                if (!isUltaCustomer(UltaBaseActivity.this) || null == newUser
                        || (null != oldUser && !oldUser.equals(newUser))) {
                    Intent intentForBasket = new Intent(UltaBaseActivity.this,
                            ViewItemsInBasketActivity.class);
                    intentForBasket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentForBasket);
                }
            }

        } else if (resultCode == RESULT_CANCELED
                && requestCode == UltaConstants.REQ_CODE_RELOGIN) {
            Logger.Log(":::::::::::::: Relogin request Cancelled");
            // A logout happened and user did not login again.
            if (Utility.isCheckoutScreen(getClass().getSimpleName()
                    .toUpperCase())) {
                Intent intentForBasket = new Intent(UltaBaseActivity.this,
                        ViewItemsInBasketActivity.class);
                intentForBasket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentForBasket);
            }
        }
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbarTop) {
            if (null != title) {
                int titleId = getResources().getIdentifier("action_bar_title",
                        "id", "android");

                TextView toolbarTitle = (TextView) findViewById(titleId);
                if (null != toolbarTitle) {
                    toolbarTitle.setTextColor(getResources().getColor(
                            R.color.white));
                }
                toolbarTop.setTitleTextColor(getResources().getColor(
                        R.color.white));
                if (activity instanceof HomeActivity) {
                    toolbarTop.setTitle("");
                } else {
                    toolbarTop.setTitle(title);
                }
            } else {
                toolbarTop.setTitle(title);
            }
        }
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setSmallFontTitle(String title) {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbarTop) {
            if (null != title) {
                int titleId = getResources().getIdentifier("action_bar_title",
                        "id", "android");
                TextView toolbarTitle = (TextView) findViewById(titleId);
                if (null != toolbarTitle) {
                    toolbarTitle.setTextColor(getResources().getColor(
                            R.color.white));
                }
                toolbarTop.setTitleTextColor(getResources().getColor(
                        R.color.white));
                Spannable text = new SpannableString(title);
                text.setSpan(new RelativeSizeSpan(0.8f), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                toolbarTop.setTitle(text);

            } else {
                toolbarTop.setTitle("");
            }
        }
    }

    /**
     * Sets basket count
     */

    public void setBasketCount(int basketCount) {
        // titleBar = (TitleBar) findViewById(R.id.titlebar);
        // if (null != titleBar) {
        // titleBar.setBasketCount(basketCount);
        // }
        invalidateOptionsMenu();
        createMenuData();
        setUpNavigationDrawer();

        UltaDataCache.getDataCacheInstance().setItemsInBasket(basketCount);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.widgets.flyin.OnMenuPressedListener#onMenuPressed()
     */
    @Override
    public void onMenuPressed() {
        // Logger.Log(">>> Menu Pressed UltaActivity2");
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(navigationDrawer);
        } else {
            drawerLayout.openDrawer(navigationDrawer);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
    }

    /**
     * Notify user.
     *
     * @param message the message
     * @param context the context
     */
    protected void notifyUser(String message, Context context) {
        notifyUser(null, message, context, null);
    }

    /**
     * Notify user.
     *
     * @param title   the title
     * @param message the message
     * @param context the context
     */
    protected void notifyUser(String title, String message, Context context) {
        notifyUser(title, message, context, null);
    }

    /**
     * Method to display the error alert dialog.
     *
     * @param title   the title
     * @param message the message
     * @param context the context
     * @param intent  the intent
     */
    protected void notifyUser(String title, String message,
                              final Context context, final Intent intent) {
        displayUserErrorMessage(title, message, context, intent);
    }

    /**
     * Method to know if the Ulta customer is logged in.
     *
     * @param context the context
     * @return true, if is ulta customer
     */
    protected boolean isUltaCustomer(Context context) {
        String rememberedUserName = Utility.retrieveFromSharedPreference(
                UltaConstants.REMEMBER_ME, context);
        if (UltaConstants.REMEMBER_CLICKED.equals(rememberedUserName)) {
            UltaDataCache.getDataCacheInstance().setLoggedIn(true);
        }
        return UltaDataCache.getDataCacheInstance().isLoggedIn();
    }

    /**
     * Method to get the registered mail Id.
     *
     * @param context the context
     * @return mailId
     */
    protected String getRegisteredMailId(Context context) {
        String mailId = UltaConstants.EMPTY_STRING;
        mailId = Utility.retrieveFromSharedPreference(
                UltaConstants.LOGGED_MAIL_ID, context);
        Logger.Log("<Utility><getRegisteredMailId><mailId>>" + (mailId));
        return mailId != null ? mailId : UltaConstants.EMPTY_STRING;
    }

    /**
     * The main menu.
     */
    private String[] mainMenu;

    /**
     * The my data menu.
     */
    private String[] myDataMenu;

    /**
     * The misc menu.
     */
    @SuppressWarnings("unused")
    private String[] miscMenu;

    /**
     * The main menu images.
     */
    private int[] mainMenuImages, mainMenuHighlightedImages, mainMenuTagPositions;

    /**
     * The my data menu images.
     */
    private int[] myDataMenuImages, myDataMenuHighlightedImages, myDataMenuTagPositions;

    /**
     * The misc menu images.
     */
    @SuppressWarnings("unused")
    private int[] miscMenuImages;

    /**
     * Creates the menu data.
     */
    protected void createMenuData() {
        int storedBasketCount = getStoredBasketCount();
        int favoritesCount = getStoredFavoritesCount();
        String basketString = "Bag";
        String favoritesString = "Favorites";
        if (itemCountInBasket > 0 || storedBasketCount > 0) {
            if (!(itemCountInBasket > 0)) {
                itemCountInBasket = storedBasketCount;
            }
            basketString = "Bag" + " (" + itemCountInBasket + ")";
        }
        if (favoritesCount > 0) {
            favoritesString = "Favorites" + " (" + favoritesCount + ")";
        }
        myDataMenu = new String[]{"Call Us: ", "Email Us", "About", "Legal", "Privacy Policy"};
        if (UltaDataCache.getDataCacheInstance().isShowButterfly()) {
            mainMenu = new String[]{"Home", "My Account", favoritesString,
                    "Find Store", "Shop",
                    "Order Status", "Ultamate Rewards Credit Card", "Gift Card Balance"};
            mainMenuTagPositions = new int[]{1, 13, 2, 3, 4, 5, 12, 6};
            myDataMenuTagPositions = new int[]{11, 8, 14, 9, 10};
            mainMenuImages = new int[]{R.drawable.icon_home, R.drawable.icon_my_accounts,
                    R.drawable.icon_fav_inactive, R.drawable.icon_store,
                    R.drawable.icon_shop, R.drawable.icon_myorderhistory, R.drawable.icon_creditcard,
                    R.drawable.icon_giftcard};
            mainMenuHighlightedImages = new int[]{R.drawable.icon_home_active, R.drawable.icon_my_accounts_active,
                    R.drawable.icon_fav_active, R.drawable.icon_store_active,
                    R.drawable.icon_shop_active,
                    R.drawable.icon_myorderhistory_active, R.drawable.icon_creditcard_active,
                    R.drawable.icon_giftcard_active};
        } else {
            mainMenu = new String[]{"Home", "My Account", favoritesString,
                    "Find Store", "Shop",
                    "Order Status", "Gift Card Balance"};
            mainMenuTagPositions = new int[]{1, 13, 2, 3, 4, 5, 6};
            myDataMenuTagPositions = new int[]{11, 8, 14, 9, 10};
            mainMenuImages = new int[]{R.drawable.icon_home, R.drawable.icon_my_accounts,
                    R.drawable.icon_fav_inactive, R.drawable.icon_store,
                    R.drawable.icon_shop, R.drawable.icon_myorderhistory,
                    R.drawable.icon_giftcard};
            mainMenuHighlightedImages = new int[]{R.drawable.icon_home_active, R.drawable.icon_my_accounts_active,
                    R.drawable.icon_fav_active, R.drawable.icon_store_active,
                    R.drawable.icon_shop_active,
                    R.drawable.icon_myorderhistory_active,
                    R.drawable.icon_giftcard_active};
        }
        myDataMenuImages = new int[]{R.drawable.icon_contact_us, R.drawable.icon_email, R.drawable.icon_help,
                R.drawable.icon_user_agreement, R.drawable.icon_privacy,


        };
        myDataMenuHighlightedImages = new int[]{
                R.drawable.icon_contact_us_active,
                R.drawable.icon_email_active, R.drawable.icon_help_active, R.drawable.icon_user_agreement_active,
                R.drawable.icon_privacy_active,


        };
        miscMenuImages = new int[]{R.drawable.icon_contact_us, R.drawable.icon_help,
                R.drawable.icon_email, R.drawable.icon_user_agreement,
                R.drawable.icon_privacy,
                R.drawable.icon_exit};


    }

    /**
     * The Class CustomAdapter.
     */
    public class CustomAdapter extends BaseAdapter {

        /**
         * The options.
         */
        String[] options;

        /**
         * The image ids.
         */
        int[] imageIds;
        int[] tagPositions;
        String optionArea;

        /**
         * Instantiates a new custom adapter.
         *
         * @param options  the options
         * @param imageIds the image ids
         */
        public CustomAdapter(String[] options, int[] imageIds, int tagPositions[], String optionArea) {
            super();
            this.options = options;
            this.imageIds = imageIds;
            this.tagPositions = tagPositions;
            this.optionArea = optionArea;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return options.length;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            LinearLayout itemLayout = (LinearLayout) inflater.inflate(
                    R.layout.menu_item, null);
            LinearLayout rowLayout = (LinearLayout) itemLayout
                    .findViewById(R.id.menu_row);
            if (options[position].contains("Call Us")) {
                TextView t = (TextView) itemLayout
                        .findViewById(R.id.menu_string);
                t.setText(options[position]);
                TextView t1 = (TextView) itemLayout
                        .findViewById(R.id.menu_string_new);
                t1.setText(WebserviceConstants.ULTA_PHONE_NUMBER);
                t1.setVisibility(View.VISIBLE);

            }
            if (options[position].contains("Gift Card Balance")) {
                View dividerView = itemLayout.findViewById(R.id.dividerview);
                dividerView.setVisibility(View.VISIBLE);
            }
            if (!UltaDataCache.getDataCacheInstance().isShowButterfly() && options[position].contains("Sign Up For Credit Card")) {
                itemLayout.setVisibility(View.GONE);
            }
            ((TextView) (itemLayout.findViewById(R.id.menu_string)))
                    .setText(options[position]);
            /*
             * itemLayout.setOnClickListener(new OnClickListener() {
			 *
			 * @Override public void onClick(View v) { openPage(position); } });
			 */
            ((ImageView) (itemLayout.findViewById(R.id.menu_icon)))
                    .setImageResource(imageIds[position]);

            if (activity instanceof HomeActivity
                    && options[position].contains("Home")
                    || activity instanceof FavoritesActivity
                    && options[position].contains("Favorites")
                    || activity instanceof NonSignedInRewardsActivity
                    && options[position].contains("Favorites")
                    || activity instanceof ViewItemsInBasketActivity
                    && options[position].contains("Bag")
                    || activity instanceof ShopListActivity
                    && options[position].contains("Shop")
                    || activity instanceof MyOrderHistoryActivity
                    && options[position].contains("Order Status")
                    || (activity instanceof GiftCardsTabActivity
                    && options[position].contains("Order Status") && orderstaus)
                    || (activity instanceof GiftCardsTabActivity
                    && options[position].contains("Gift Card Balance") && !orderstaus)
                    || activity instanceof MyAccountActivity
                    && options[position].contains("My Account")
                    || activity instanceof LoginActivity
                    && options[position].contains("My Account")
                    || activity instanceof AboutUsActivity
                    && options[position].contains("About")
                    || activity instanceof LegalActivity
                    && options[position].contains("Legal")
                    || activity instanceof PrivacyPolicyActivity
                    && options[position].contains("Privacy Policy")
                    || activity instanceof UltaMateCreditCardActivity
                    && options[position].contains("Ultamate Rewards Credit Card")
                    || activity instanceof StoresActivity
                    && options[position].contains("Find Store")) {
                rowLayout.setBackgroundColor(getResources().getColor(
                        R.color.lightgrey));
                ((TextView) (itemLayout.findViewById(R.id.menu_string)))
                        .setTextColor(getResources().getColor(R.color.melon));
                if (optionArea.equalsIgnoreCase("Menu")) {
                    ((ImageView) (itemLayout.findViewById(R.id.menu_icon)))
                            .setImageResource(mainMenuHighlightedImages[position]);

                } else {
                    ((ImageView) (itemLayout.findViewById(R.id.menu_icon)))
                            .setImageResource(myDataMenuHighlightedImages[position]);
                }
            }
            itemLayout.setTag(tagPositions[position]);
            return itemLayout;
        }
    }

    /**
     * The Class Header.
     */
    class Header {

        /**
         * The header.
         */
        String header;

        /**
         * The level.
         */
        int level;
    }

    /**
     * The Class SeparatedListAdapter.
     */
    public class SeparatedListAdapter extends BaseAdapter {

        /**
         * The sections.
         */
        public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();

        /**
         * The headers.
         */
        public final ArrayAdapter<String> headers;

        /**
         * The Constant TYPE_SECTION_HEADER.
         */
        public final static int TYPE_SECTION_HEADER = 0;

        /**
         * Instantiates a new separated list adapter.
         *
         * @param context the context
         */
        public SeparatedListAdapter(Context context) {
            headers = new ArrayAdapter<String>(context,
                    R.layout.seperated_list_header);
        }

        /**
         * Adds the section.
         *
         * @param section the section
         * @param adapter the adapter
         */
        public void addSection(String section, Adapter adapter) {
            /*
             * Header header=new Header(); header.header=section;
			 * header.level=level;
			 */
            this.headers.add(section);
            this.sections.put(section, adapter);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItem(int)
         */
        public Object getItem(int position) {
            for (Object section : this.sections.keySet()) {
                Adapter adapter = sections.get(section);
                int size = adapter.getCount() + 1;

                // check if position inside this section
                if (position == 0)
                    return section;
                if (position < size)
                    return adapter.getItem(position - 1);

                // otherwise jump into next section
                position -= size;
            }
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getCount()
         */
        public int getCount() {
            // total together all sections, plus one for each section header
            int total = 0;
            for (Adapter adapter : this.sections.values())
                total += adapter.getCount() + 1;
            return total;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.BaseAdapter#getViewTypeCount()
         */
        public int getViewTypeCount() {
            // assume that headers count as one, then total all sections
            int total = 1;
            for (Adapter adapter : this.sections.values())
                total += adapter.getViewTypeCount();
            return total;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.BaseAdapter#getItemViewType(int)
         */
        public int getItemViewType(int position) {
            int type = 1;
            for (Object section : this.sections.keySet()) {
                Adapter adapter = sections.get(section);
                int size = adapter.getCount() + 1;

                // check if position inside this section
                if (position == 0)
                    return TYPE_SECTION_HEADER;
                if (position < size)
                    return type + adapter.getItemViewType(position - 1);

                // otherwise jump into next section
                position -= size;
                type += adapter.getViewTypeCount();
            }
            return -1;
        }

        /**
         * Are all items selectable.
         *
         * @return true, if successful
         */
        public boolean areAllItemsSelectable() {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.BaseAdapter#isEnabled(int)
         */
        public boolean isEnabled(int position) {
            return (getItemViewType(position) != TYPE_SECTION_HEADER);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // final int temp=position;
            int sectionnum = 0;
            for (Object section : this.sections.keySet()) {
                Adapter adapter = sections.get(section);
                int size = adapter.getCount() + 1;

                // check if position inside this section
                if (position == 0)
                    return headers.getView(sectionnum, convertView, parent);
                if (position < size) {
                    View view = adapter.getView(position - 1, convertView,
                            parent);
                    return view;
                    // return adapter.getView(position - 1, convertView,
                    // parent);
                }

                // otherwise jump into next section
                position -= size;
                sectionnum++;
            }
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    /**
     * Invoke logout.
     */
    protected void invokeLogout() {
        InvokerParams<StatusOnlyResponseBean> invokerParams = new InvokerParams<StatusOnlyResponseBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.LOGOUT_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateChangePasswordParameters());
        invokerParams.setUltaBeanClazz(StatusOnlyResponseBean.class);
        invokerParams.setUserSessionClearingRequired(true);
        LogoutHandler userCreationHandler = new LogoutHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<MyAccountActivity><invokeUserCreation><UltaException>>"
                    + ultaException);
            mOnSessionTimeOut.onLoginDoneAfterUnauthorizedError(false);
        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @return Map<String, String>
     */
    private Map<String, String> populateChangePasswordParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        // urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "1");

        return urlParams;
    }

    /**
     * The Class LogoutHandler.
     */
    public class LogoutHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<LogoutHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (null != getErrorMessage()) {
                if (!UltaDataCache.getDataCacheInstance().isAppClosed()) {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaBaseActivity.this);
                }
                if (null != mOnSessionTimeOut) {
                    mOnSessionTimeOut.onLoginDoneAfterUnauthorizedError(false);
                }
            } else {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                Logger.Log("<Logout><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                StatusOnlyResponseBean ultaBean = (StatusOnlyResponseBean) getResponseBean();
                String responseStatus = ultaBean.getResponseStatus();
                Logger.Log("<LogoutHandler><handleMessage><getResponseBean>>"
                        + responseStatus);
                /**
                 * Auto Login
                 */
                UltaDataCache.getDataCacheInstance().setGiftTheOrder(false);
                Utility.saveToSharedPreference(UltaConstants.REMEMBER_ME,
                        UltaConstants.BLANK_STRING, getApplicationContext());
                UltaDataCache.getDataCacheInstance().setLoggedIn(false);
                UltaDataCache.getDataCacheInstance().setStaySignedIn(true);
                UltaDataCache.getDataCacheInstance().setRewardMember(false);
                Utility.saveToSharedPreference(UltaConstants.REWARD_MEMBER,
                        UltaConstants.IS_REWARD_MEMBER, false,
                        getApplicationContext());
                //email optin value
                Utility.saveToSharedPreference(UltaConstants.EMAIL_OPT_IN, true,
                        getApplicationContext());
                Utility.saveToSharedPreference(
                        UltaConstants.REWARD_MEMBER,
                        UltaConstants.ULTAMATE_CARD_TYPE, "",
                        getApplicationContext());
                Utility.saveToSharedPreference(
                        UltaConstants.REWARD_MEMBER,
                        UltaConstants.BEAUTY_CLUB_NUMBER, "",
                        getApplicationContext());
                UltaDataCache.getDataCacheInstance().setRedeemLevelPoints(null);
                // if (!UltaDataCache.getDataCacheInstance().isStaySignedIn()) {
                // remember me is changed to unclicked state

                staySignedInSharedPreferences = getSharedPreferences(
                        WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                        MODE_PRIVATE);
                staySignedInEditor = staySignedInSharedPreferences.edit();
                staySignedInEditor.putBoolean(
                        WebserviceConstants.IS_STAY_SIGNED_IN, false);
                staySignedInEditor.putBoolean(WebserviceConstants.IS_LOGGED_IN,
                        false);
                staySignedInEditor.putString(
                        WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");
                staySignedInEditor.putString(
                        WebserviceConstants.STAY_SIGNED_IN_PASSWORD, " ");
                staySignedInEditor.putString(
                        WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
                staySignedInEditor.putString(
                        WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME, " ");
                staySignedInEditor.commit();
                // }

                if (isUltaCustomer(UltaBaseActivity.this)) {
                    miscMenu = new String[]{"About", "Email Us", "Legal",
                            "Privacy Policy", "Contact Us: "};
                } else {
                    miscMenu = new String[]{"About", "Email Us", "Legal",
                            "Privacy Policy", "Contact Us: "};
                }
                // invalidateSideMenu();
                setItemCountInBasket(0);
                setFavoritesCountInNavigationDrawer(0);
                if (isPersisted) {
                    createMenuData();
                }
                UltaDataCache.getDataCacheInstance().setCreditCardDetails(null);
                UltaDataCache.getDataCacheInstance().setBillingAddress(null);
                UltaDataCache.getDataCacheInstance().setGiftCards(null);
                UltaDataCache.getDataCacheInstance()
                        .setAnonymousCheckout(false);

                if (UltaDataCache.getDataCacheInstance().isAppClosed()) {
                    UltaDataCache.getDataCacheInstance().setAppClosed(false);
                } else if (UltaDataCache.getDataCacheInstance()
                        .isIfNotSignedInClearSession()) {
                    if (null != mOnSessionTimeOut) {
                        mOnSessionTimeOut
                                .onLoginDoneAfterUnauthorizedError(false);
                    }

                } else {
                    Intent intentForHome = new Intent(UltaBaseActivity.this,
                            HomeActivity.class);
                    if (isAskRelogin) {
                        intentForHome = new Intent(UltaBaseActivity.this,
                                LoginActivity.class);
                    } else {
                        intentForHome = new Intent(UltaBaseActivity.this,
                                HomeActivity.class);
                    }
                    intentForHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentForHome);
                }

                // }
            }
        }
    }

    @Override
    public void onLogout() {
    }

    // private void invalidateSideMenu() {
    // createMenuData();
    // ListView menu = (ListView) mainLayout.findViewById(R.id.menu_list);
    // adapter = new SeparatedListAdapter(this);
    // adapter.addSection(" ", new CustomAdapter(mainMenu, mainMenuImages));
    // adapter.addSection("My Data", new CustomAdapter(myDataMenu,
    // myDataMenuImages));
    // menu.setAdapter(adapter);
    // }

    private LogoutBroadcastReciver logoutBroadcastReciver;

    public void registerForLogoutBroadcast() {
        logoutBroadcastReciver = new LogoutBroadcastReciver();
        registerReceiver(logoutBroadcastReciver, new IntentFilter(
                "com.ulta.core.action.LOGOUT"));
    }

    public class LogoutBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.ulta.core.action.LOGOUT")) {
                unregisterReceiver(logoutBroadcastReciver);
                finish();
            }
        }
    }

    /**
     * Method to make the menu invisible. To be used in checkout pages
     */
    public void disableMenu() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.setMenu(UltaBaseActivity.this, "false");
        }

    }

    /**
     * method to make done button invisible. To be used while loading data from
     * WS. So as to block the user from leaving the operation incomplete.
     */
    public void disableDone() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.disableDone();
        }

    }

    /**
     * method to make bag icon invisible. To be used while loading data from WS.
     * So as to block the user from leaving the operation incomplete.
     */
    public void disableBagIcon() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.disableBagIcon();
        }

    }

    /**
     * method to make done button visible.
     */
    public void enableDone() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.enableDone();
        }

    }

    /**
     * method to make done button visible.
     */
    public void displayCheckoutButton() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.displayCheckoutButton();
        }

    }

    /**
     * method to make done button visible.
     */
    public void displayNextButton() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.displayNextButton();
        }

    }

    public void displayContinueButton() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.displayContinueButton();
        }

    }

    public void displayApplyButton() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.displayApplyButton();
        }

    }

    public void displayDoneButton() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (null == titleBar) {
            throw new RuntimeException(
                    "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
        } else {
            titleBar.displayDoneButton();
        }

    }

    public void setActivity(UltaBaseActivity activity) {
        this.activity = activity;
    }

    public UltaBaseActivity getActivity() {
        return activity;
    }

    public int getItemCountInBasket() {
        return itemCountInBasket;
    }

    public void setFavoritesCountInNavigationDrawer(int favoritesCount) {
        SharedPreferences basketPreferences = this.getSharedPreferences(
                WebserviceConstants.COUNTS_PREFS_NAME, MODE_PRIVATE);
        Editor edit = basketPreferences.edit();
        edit.putInt(WebserviceConstants.FAVORITES_COUNT, favoritesCount);
        edit.commit();

        createMenuData();
        setUpNavigationDrawer();
    }

    public void setItemCountInBasket(int itemCountInBasket) {
        UltaBaseActivity.itemCountInBasket = itemCountInBasket;
        SharedPreferences basketPreferences = this.getSharedPreferences(
                WebserviceConstants.COUNTS_PREFS_NAME, MODE_PRIVATE);
        Editor edit = basketPreferences.edit();
        edit.putInt(WebserviceConstants.BASKET_COUNT, itemCountInBasket);
        edit.commit();
        setBasketCount(itemCountInBasket);
        // invalidateSideMenu();
    }

    public String[] loadFromContacts() {
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                null, null, null, null);
        int nameId = cursor
                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME);
        int cityId = cursor
                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.CITY);
        int streetId = cursor
                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.STREET);
        int postcodeId = cursor
                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE);
        int stateId = cursor
                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.REGION);
        result1 = new String[cursor.getCount()];

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(nameId);
                String street = cursor.getString(streetId);
                String city = cursor.getString(cityId);
                String postcode = cursor.getString(postcodeId);
                String state = cursor.getString(stateId);

                result1[cursor.getPosition()] = name + "@" + street + "@"
                        + city + "@" + state + "@" + postcode;
            } while (cursor.moveToNext());
        }
        return result1;
    }

    public void askRelogin(Context check) {

        try {
            mOnSessionTimeOut = (OnSessionTimeOut) check;
            SharedPreferences staySignedInSharedPref = getSharedPreferences(
                    WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);

            boolean isStaySignedIn = staySignedInSharedPref.getBoolean(
                    WebserviceConstants.IS_STAY_SIGNED_IN, false);

            boolean isActive = staySignedInSharedPref.getBoolean(
                    WebserviceConstants.IS_PUSH_NOTIFICATION_ON, true);
            isAppClosedAndStaySignedIn = false;
            mRegistrationIdSharedPreferences = getSharedPreferences(
                    WebserviceConstants.CHANNEL_ID_PREF, 0);

            String registrationId = mRegistrationIdSharedPreferences.getString(
                    WebserviceConstants.PUSH_CHANNEL_ID, "");

            if (isStaySignedIn) {

                String secretKey = staySignedInSharedPref.getString(
                        WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
                String loginPassword = Utility.decryptString(
                        staySignedInSharedPref.getString(
                                WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
                                " "), secretKey);
                String userName = Utility.decryptString(staySignedInSharedPref
                        .getString(WebserviceConstants.STAY_SIGNED_IN_USERNAME,
                                " "), secretKey);
                invokeLogin(userName, loginPassword, registrationId, isActive);

            } else {

                isAskRelogin = true;

                if (UltaDataCache.getDataCacheInstance()
                        .isIfNotSignedInClearSession()) {

                } else {
                    pd = new ProgressDialog(UltaBaseActivity.this);
                    setProgressDialogLoadingColor(pd);
                    pd.setMessage(LOADING_PROGRESS_TEXT);
                    pd.setCancelable(false);
                    try {
                        pd.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                UltaDataCache.getDataCacheInstance().setAskRelogin(true);
                invokeLogout();
                // Intent intent = new Intent(UltaBaseActivity.this,
                // LoginActivity.class);
                // startActivity(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to invoke paypal payment details
     */
    public void invokePayPalPaymentDetails() {

        InvokerParams<ReviewOrderBean> invokerParams = new InvokerParams<ReviewOrderBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.PAYPAL_PAYMENT_DETAILS);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populatePaymentMethodDetailsHandlerParameters());
        invokerParams.setUltaBeanClazz(ReviewOrderBean.class);
        RetrievePayPalDetailsHandler retrievePaymentDetailsHandler = new RetrievePayPalDetailsHandler();
        invokerParams.setUltaHandler(retrievePaymentDetailsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);

        }

    }

    /**
     * Method to populate the URL parameter map.
     *
     * @return Map<String, String>
     */
    private Map<String, String> populatePaymentMethodDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("isReqFromNewApp", "true");
        if (UltaDataCache.getDataCacheInstance().isExpressCheckout()) {
            urlParams.put("isExpressCheckout", "TRUE");
        } else {
            urlParams.put("isExpressCheckout", "FALSE");
        }
        return urlParams;
    }

    public class RetrievePayPalDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    askRelogin(UltaBaseActivity.this);
                } else {
                    try {
                        notifyUser(getErrorMessage(), UltaBaseActivity.this);
                        finish();
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                ReviewOrderBean ultaBean = (ReviewOrderBean) getResponseBean();
                if (ultaBean != null && ultaBean.getComponent() != null) {
                    Intent gotoReviewOrder = new Intent(UltaBaseActivity.this,
                            ReviewOrderActivity.class);
                    gotoReviewOrder.putExtra("order", ultaBean);
                    startActivity(gotoReviewOrder);
                }
            }
        }
    }

    protected void refreshPage() {
    }

    ;

    /**
     * Invoke login.
     *
     * @param username      the username
     * @param passwordLogin the password login
     */
    private void invokeLogin(String username, String passwordLogin,
                             String registrationId, boolean isActive) {
        InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.LOGIN_SERVICE);
        invokerParams.setCookieHandlingSkip(false);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateLoginParameters(username,
                passwordLogin, registrationId, isActive));

        invokerParams.setUltaBeanClazz(LoginBean.class);
        // invokerParams.setUserSessionClearingRequired(true);
        LoginHandler loginHandler = new LoginHandler();
        invokerParams.setUltaHandler(loginHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<LoginsActivity><invokeLogin><UltaException>>"
                    + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @param username      the username
     * @param passwordLogin the password login
     * @return Map<String, String>
     */
    private Map<String, String> populateLoginParameters(String username,
                                                        String passwordLogin, String registrationId, boolean isActive) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("value.login", username);
        urlParams.put("value.password", passwordLogin);
        urlParams.put("token", registrationId);
        if (isActive) {
            urlParams.put("isActive", "true");
        } else {
            urlParams.put("isActive", "false");
        }

        return urlParams;

    }

    /**
     * The Class LoginHandler.
     */
    public class LoginHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<LoginHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    // loginAction.reportError(
                    // WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
                    // loginAction.leaveAction();
                    askRelogin(UltaBaseActivity.this);
                } else {
                    if (null == origin && null != mOnSessionTimeOut) {
                        mOnSessionTimeOut
                                .onLoginDoneAfterUnauthorizedError(false);
                    }
                    try {
                        // loginAction.reportError(
                        // WebserviceConstants.FORM_EXCEPTION_OCCURED,
                        // WebserviceConstants.DYN_ERRCODE_LOGIN_ACTIVITY);
                        // loginAction.leaveAction();
                        try {

                            if (!(activity instanceof HomeActivity)) {
                                notifyUser(
                                        Utility.formatDisplayError(getErrorMessage()),
                                        UltaBaseActivity.this);
                            }
                        } catch (Exception e) {

                        }
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                Logger.Log("<Login><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                LoginBean ultaBean = (LoginBean) getResponseBean();
                String isLoggedIn = ultaBean.getResult();
                List<String> result = ultaBean.getErrorInfos();
                Logger.Log("<LoginHandler><handleMessage><getResponseBean>>"
                        + result);

                resetCache();
                if (null != ultaBean.getComponent()) {
                    //email optin value
                    Utility.saveToSharedPreference(UltaConstants.EMAIL_OPT_IN, ultaBean.getComponent().getEmailOptIn(),
                            getApplicationContext());
                    //Ultamate Card Details
                    if (null != ultaBean.getComponent().getCreditCardType()) {
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.ULTAMATE_CARD_TYPE, "" + ultaBean.getComponent().getCreditCardType(),
                                getApplicationContext());
                    }
                    if (null != ultaBean.getComponent().get_beautyClubNumber()) {
                        UltaDataCache.getDataCacheInstance().setRewardMember(
                                true);
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.IS_REWARD_MEMBER, true,
                                getApplicationContext());
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.BEAUTY_CLUB_NUMBER, ultaBean.getComponent().get_beautyClubNumber(),
                                getApplicationContext());
                    } else

                    {
                        UltaDataCache.getDataCacheInstance().setRewardMember(
                                false);
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.IS_REWARD_MEMBER, false,
                                getApplicationContext());
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.BEAUTY_CLUB_NUMBER, "",
                                getApplicationContext());
                    }
                }
                if (null != origin) {

                    if (null == result
                            || result.get(0).equalsIgnoreCase("")
                            || result
                            .get(0)
                            .startsWith(
                                    "Thank you for activating your rewards account")
                            || result
                            .get(0)
                            .startsWith(
                                    "Some of the information we have on file for you needs updating")) {
                        dismissLoginDialog();
                        UltaDataCache.getDataCacheInstance().setLoggedIn(true);
                        UltaDataCache.getDataCacheInstance().setLoginName(
                                username);
                        ConversantUtility.loginTag(username);

                        clearAOCookie();
                        if (null != ultaBean.getComponent()) {
                            if (null != ultaBean.getComponent()
                                    .get_balancePoints()) {
                                UltaDataCache.getDataCacheInstance()
                                        .setRewardsBalancePoints(
                                                ultaBean.getComponent()
                                                        .get_balancePoints());
                            }
                            if (null != ultaBean.getComponent()
                                    .get_beautyClubNumber()) {
                                UltaDataCache
                                        .getDataCacheInstance()
                                        .setRewardsBeautyClubNumber(
                                                ultaBean.getComponent()
                                                        .get_beautyClubNumber());
                            }
                            boolean isRewardMember = false;
                            if (null != ultaBean.getComponent()
                                    .get_beautyClubNumber()) {
                                isRewardMember = true;
                                UltaDataCache.getDataCacheInstance()
                                        .setRewardMember(isRewardMember);
                                Utility.saveToSharedPreference(
                                        UltaConstants.REWARD_MEMBER,
                                        UltaConstants.IS_REWARD_MEMBER,
                                        isRewardMember, getApplicationContext());
                                Utility.saveToSharedPreference(
                                        UltaConstants.REWARD_MEMBER,
                                        UltaConstants.BEAUTY_CLUB_NUMBER, ultaBean.getComponent().get_beautyClubNumber(),
                                        getApplicationContext());
                            } else

                            {
                                isRewardMember = false;
                                UltaDataCache.getDataCacheInstance()
                                        .setRewardMember(isRewardMember);
                                Utility.saveToSharedPreference(
                                        UltaConstants.REWARD_MEMBER,
                                        UltaConstants.IS_REWARD_MEMBER,
                                        isRewardMember, getApplicationContext());
                                Utility.saveToSharedPreference(
                                        UltaConstants.REWARD_MEMBER,
                                        UltaConstants.BEAUTY_CLUB_NUMBER, "",
                                        getApplicationContext());
                            }

                            if (null != ultaBean.getComponent().get_firstName()) {
                                UltaDataCache.getDataCacheInstance()
                                        .setUserFirstName(
                                                ultaBean.getComponent()
                                                        .get_firstName());
                            }

                            if (null != ultaBean.getComponent()
                                    .getShoppingCartCount()
                                    && !ultaBean.getComponent()
                                    .getShoppingCartCount().isEmpty()) {
                                try {
                                    int bagCount = Integer.parseInt(ultaBean
                                            .getComponent()
                                            .getShoppingCartCount());
                                    if (bagCount >= 0) {
                                        setItemCountInBasket(bagCount);
                                        if (isPersisted) {
                                            createMenuData();
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }

                            if (null != ultaBean.getComponent()
                                    .getFavoritesItemCount()
                                    && !ultaBean.getComponent()
                                    .getFavoritesItemCount().isEmpty()) {
                                try {
                                    int favCount = Integer.parseInt(ultaBean
                                            .getComponent()
                                            .getFavoritesItemCount());
                                    if (favCount > 0) {
                                        setFavoritesCountInNavigationDrawer(favCount);
                                        if (isPersisted) {
                                            createMenuData();
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        if (isStaySignedIn) {

                            Utility.encrypPasswordt(editPasswordLogin.getText()
                                    .toString());

                            Utility.encryptUserName(editUsername.getText()
                                    .toString());

                            loginPasswordBytes = UltaDataCache
                                    .getDataCacheInstance()
                                    .getEncodedPasswordBytes();

                            loginUserNameBytes = UltaDataCache
                                    .getDataCacheInstance()
                                    .getEncodedUserNameBytes();

                            String secretKey = Base64.encodeToString(
                                    UltaDataCache.getDataCacheInstance()
                                            .getSecretKey().getEncoded(),
                                    Base64.DEFAULT);

                            loginPassword = Base64.encodeToString(
                                    loginPasswordBytes, Base64.DEFAULT);
                            loginUsername = Base64.encodeToString(
                                    loginUserNameBytes, Base64.DEFAULT);
                            staySignedInSharedPreferences = getSharedPreferences(
                                    WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                                    MODE_PRIVATE);
                            staySignedInEditor = staySignedInSharedPreferences
                                    .edit();
                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
                                            loginPassword);
                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_USERNAME,
                                            loginUsername);
                            if (null != ultaBean.getComponent().get_firstName()) {

                                staySignedInEditor
                                        .putString(
                                                WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME,
                                                ultaBean.getComponent()
                                                        .get_firstName());
                            }
                            staySignedInEditor
                                    .putBoolean(
                                            WebserviceConstants.IS_STAY_SIGNED_IN,
                                            true);
                            staySignedInEditor.putBoolean(
                                    WebserviceConstants.IS_LOGGED_IN, true);
                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY,
                                            secretKey);

                            staySignedInEditor.commit();
                        } else {
                            staySignedInSharedPreferences = getSharedPreferences(
                                    WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                                    MODE_PRIVATE);
                            staySignedInEditor = staySignedInSharedPreferences
                                    .edit();
                            staySignedInEditor.putBoolean(
                                    WebserviceConstants.IS_STAY_SIGNED_IN,
                                    false);
                            staySignedInEditor.putBoolean(
                                    WebserviceConstants.IS_LOGGED_IN, true);
                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_USERNAME,
                                            " ");
                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
                                            " ");
                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY,
                                            " ");
                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME,
                                            ultaBean.getComponent()
                                                    .get_firstName());
                            staySignedInEditor.commit();
                        }
                        Utility.saveToSharedPreference(
                                UltaConstants.REMEMBER_ME,
                                UltaConstants.REMEMBER_CLICKED,
                                getApplicationContext());

                        Utility.saveToSharedPreference(
                                UltaConstants.LOGGED_MAIL_ID, username,
                                getApplicationContext());

                        if (UltaDataCache.getDataCacheInstance()
                                .isOrderSubmitted()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setOrderSubmitted(false);
                        }
                        if (null != origin
                                && origin.equalsIgnoreCase("homeScreen")) {

                            Log.d("homeScreen", "homeScreen");
                            Intent homeIntent = new Intent(
                                    UltaBaseActivity.this, HomeActivity.class);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK |

                                    Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(homeIntent);

                        } else if (null != origin
                                && origin.equalsIgnoreCase("myAccountScreen")) {
                            Intent myAccountsIntent = new Intent(
                                    UltaBaseActivity.this,
                                    MyAccountActivity.class);
                            myAccountsIntent
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK
                                            |

                                            Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(myAccountsIntent);
                        }
                    } else {

                        try {

                            if (pd != null && pd.isShowing()) {
                                pd.dismiss();
                            }
                            // loginAction.reportError(result.get(0), 105);
                            // loginAction.leaveAction();

                            if (!result.get(0).startsWith(
                                    "You are already logged")) {
                                notifyUser(Utility.formatDisplayError(result
                                        .get(0)), UltaBaseActivity.this);
                            }

                            setError(UltaBaseActivity.this, result.get(0));
                            editUsername.setText("");
                            editPasswordLogin.setText("");
                            editUsername.requestFocus();
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {

                        }
                    }
                } else {

                    if (null != ultaBean) {
                        if (null != ultaBean.getComponent()
                                && null != ultaBean.getComponent()
                                .get_beautyClubNumber()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardsBeautyClubNumber(
                                            ultaBean.getComponent()
                                                    .get_beautyClubNumber());
                        }
                        if (null != ultaBean.getComponent()) {
                            if (null != ultaBean.getComponent().get_firstName()) {
                                UltaDataCache.getDataCacheInstance()
                                        .setUserFirstName(
                                                ultaBean.getComponent()
                                                        .get_firstName());
                            }

                            if (null != ultaBean.getComponent()
                                    .getShoppingCartCount()
                                    && !ultaBean.getComponent()
                                    .getShoppingCartCount().isEmpty()) {
                                try {
                                    int bagCount = Integer.parseInt(ultaBean
                                            .getComponent()
                                            .getShoppingCartCount());
                                    if (bagCount >= 0) {
                                        setItemCountInBasket(bagCount);
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }

                            if (null != ultaBean.getComponent()
                                    .getFavoritesItemCount()
                                    && !ultaBean.getComponent()
                                    .getFavoritesItemCount().isEmpty()) {
                                try {
                                    int favCount = Integer.parseInt(ultaBean
                                            .getComponent()
                                            .getFavoritesItemCount());
                                    if (favCount > 0) {
                                        setFavoritesCountInNavigationDrawer(favCount);
                                        if (isPersisted) {
                                            createMenuData();
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (null != ultaBean.getComponent().get_email()) {
                                userName = ultaBean.getComponent().get_email();
                            }
                        }

                    }

                    if (isLoggedIn.equalsIgnoreCase("true")) {
                        if (isPersisted) {
                            createMenuData();
                        }

                        UltaDataCache.getDataCacheInstance().setLoggedIn(true);
                        UltaDataCache.getDataCacheInstance().setLoginName(
                                userName);

                        Utility.saveToSharedPreference(
                                UltaConstants.REMEMBER_ME,
                                UltaConstants.REMEMBER_CLICKED,
                                getApplicationContext());

                        Utility.saveToSharedPreference(
                                UltaConstants.LOGGED_MAIL_ID, userName,
                                getApplicationContext());

                        if (UltaDataCache.getDataCacheInstance()
                                .isOrderSubmitted()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setOrderSubmitted(false);
                            finish();
                        }

                    }
                    // login failure
                    else {

                        try {
                            if (result.get(0).startsWith(
                                    "The password is incorrect")) {
                                isAskRelogin = true;
                                invokeLogout();
                                isAppClosedAndStaySignedIn = true;
                            } else if (result.get(0).startsWith(
                                    "A session already exists")) {
                                isAskRelogin = true;
                                // invokeLogout();
                                isAppClosedAndStaySignedIn = false;
                            } else if (result.get(0).startsWith(
                                    "You are already logged")) {
                                isAppClosedAndStaySignedIn = false;
                            } else if (result.get(0).contains(
                                    "user name/password is incorrect")) {
                                isAskRelogin = true;
                                invokeLogout();
                                isAppClosedAndStaySignedIn = false;
                            } else {
                                isAppClosedAndStaySignedIn = true;
                            }

                            if (null != editUsername
                                    && null != editPasswordLogin) {
                                editUsername.setText("");
                                editUsername.requestFocus();
                                editPasswordLogin.setText("");
                            }

                        } catch (WindowManager.BadTokenException e) {
                            isAppClosedAndStaySignedIn = true;
                            invokeLogout();
                        } catch (Exception e) {
                            isAppClosedAndStaySignedIn = true;
                            invokeLogout();
                        }
                    }

                    // If we are supposed to call login service to update basket
                    // and fav count please uncomment the below piece of code.
                    if (!isAppClosedAndStaySignedIn) {
                        String sessionIdCookie = Utility
                                .getCookieValue(WebserviceConstants.SESSION_ID_COOKIE);
                        if (sessionIdCookie != null
                                && sessionIdCookie.trim().length() > 0 && null != mOnSessionTimeOut) {
                            mOnSessionTimeOut
                                    .onLoginDoneAfterUnauthorizedError(true);
                        } else {
                            if (null != mOnSessionTimeOut) {
                                mOnSessionTimeOut
                                        .onLoginDoneAfterUnauthorizedError(false);
                            }
                        }
                    }
                    isAppClosedAndStaySignedIn = false;

                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr
                .getRunningTasks(70);

        if (taskList.get(0).numActivities == 1) {
            Utility.saveCookie(WebserviceConstants.SESSION_ID_COOKIE, "");
        }

    }

    public boolean checkIfUserIsStaySignedIn() {
        staySignedInSharedPreferences = getSharedPreferences(
                WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);

        String userName = staySignedInSharedPreferences.getString(
                WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");

        String loginPassword = staySignedInSharedPreferences.getString(
                WebserviceConstants.STAY_SIGNED_IN_PASSWORD, " ");

        if (!loginPassword.equalsIgnoreCase(" ")
                && !userName.equalsIgnoreCase(" ")) {
            return true;
        }

        return false;
    }

    @Override
    public void onSearchPressed() {
        Intent intentToSearchActivity = new Intent(UltaBaseActivity.this,
                SearchActivity.class);
        startActivity(intentToSearchActivity);

    }

    @Override
    public void onScanPressed() {
        trackAppState(this, WebserviceConstants.SCAN);
        //check camera permission
        checkForAppPermissions(UltaBaseActivity.this, WebserviceConstants.PERMISSION_CAMERA, WebserviceConstants.CAMERA_REQUEST_CODE, WebserviceConstants.PERMISSION_CAMERA_DIALOG_TITLE, WebserviceConstants.PERMISSION_CAMERA_DIALOG_MESSAGE);


//        ScanIntentIntegrator.initiateScan(UltaBaseActivity.this);
    }

    @Override
    public void onBagPressed() {
        Intent intentForViewItemsInBasketActivity = new Intent(
                UltaBaseActivity.this, ViewItemsInBasketActivity.class);
        startActivity(intentForViewItemsInBasketActivity);

    }

    @Override
    public void onTitleBarPressed() {

    }

    public Typeface setHelveticaRegulartTypeFace() {
        try {
            helveticaRegularTypeface = Typeface.createFromAsset(getAssets(),
                    WebserviceConstants.FONT_STYLE_FILENAME);
            return helveticaRegularTypeface;
        } catch (Exception e) {
            helveticaRegularTypeface = Typeface.SANS_SERIF;
            return helveticaRegularTypeface;
        }

    }

    public void initFooterViews() {
        mSubTotalFooterLayout = (LinearLayout) findViewById(R.id.subTotalLayout);
        mShippingTypeFooterLayout = (LinearLayout) findViewById(R.id.shippingTypeLayout);
        mTaxFooterLayout = (LinearLayout) findViewById(R.id.taxLayout);
        mTotalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        mGiftBoxAndNoteFooterLayout = (LinearLayout) findViewById(R.id.giftBoxNoteLayout);
        mRedeemPointsFooterLayout = (LinearLayout) findViewById(R.id.redeemPointsLayout);
        mCouponDiscountLayout = (LinearLayout) findViewById(R.id.couponDiscountLayout);
        mAdditionalDiscountLayout = (LinearLayout) findViewById(R.id.additionalDiscountLayout);

        mSubTotalValueTextView = (TextView) findViewById(R.id.subTotalTextViewValue);
        mSubTotalTextView = (TextView) findViewById(R.id.subTotalTextView);
        mTaxTextView = (TextView) findViewById(R.id.taxTextView);
        mTaxValueTextView = (TextView) findViewById(R.id.taxTextViewValue);
        mTotalValueTextView = (TextView) findViewById(R.id.totalTextViewValue);
        mShippingTypeTextView = (TextView) findViewById(R.id.shippingTypeTextView);
        mShippingTypeValueTextView = (TextView) findViewById(R.id.shippingTypeTextViewValue);
        mGiftBoxAndNoteValueTextView = (TextView) findViewById(R.id.giftBoxNoteTextViewValue);
        mGiftBoxAndNoteTextView = (TextView) findViewById(R.id.giftBoxNoteTextView);
        mReedemablePointsTextView = (TextView) findViewById(R.id.redeemPointsTextView);
        mReedemablePointsTextViewValue = (TextView) findViewById(R.id.redeemPointsTextViewValue);
        mTvCouponDiscount = (TextView) findViewById(R.id.tvCouponDiscount);
        mTvCouponDiscountValue = (TextView) findViewById(R.id.tvCouponDiscountValue);
        mTvAdditionalDiscount = (TextView) findViewById(R.id.tvAdditionalDiscount);
        mTvAdditionalDiscountValue = (TextView) findViewById(R.id.tvAdditionalDiscountValue);

        mExpandImageView = (ImageView) findViewById(R.id.expandFooterImageView);

        mSubTotalValueTextView.setTypeface(setHelveticaRegulartTypeFace());
        mSubTotalTextView.setTypeface(setHelveticaRegulartTypeFace());
        mTaxTextView.setTypeface(setHelveticaRegulartTypeFace());
        mTaxValueTextView.setTypeface(setHelveticaRegulartTypeFace());
        mTotalValueTextView.setTypeface(setHelveticaRegulartTypeFace());
        mShippingTypeTextView.setTypeface(setHelveticaRegulartTypeFace());
        mShippingTypeValueTextView.setTypeface(setHelveticaRegulartTypeFace());
        mGiftBoxAndNoteValueTextView
                .setTypeface(setHelveticaRegulartTypeFace());
        mGiftBoxAndNoteTextView.setTypeface(setHelveticaRegulartTypeFace());
        mReedemablePointsTextView.setTypeface(setHelveticaRegulartTypeFace());
        mReedemablePointsTextViewValue
                .setTypeface(setHelveticaRegulartTypeFace());
        mTvCouponDiscount
                .setTypeface(setHelveticaRegulartTypeFace());
        mTvCouponDiscountValue.setTypeface(setHelveticaRegulartTypeFace());
        mTvAdditionalDiscount.setTypeface(setHelveticaRegulartTypeFace());
        mTvAdditionalDiscountValue
                .setTypeface(setHelveticaRegulartTypeFace());

    }

    public void invokePushNotificationService(String registrationId,
                                              String isActive) {
        InvokerParams<PushNotificationBean> invokerParams = new InvokerParams<PushNotificationBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.PUSH_NOTIFICATION_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populatePushNotificationParameters(registrationId));
        invokerParams.setUltaBeanClazz(PushNotificationBean.class);
        PushNotificationHandler pushNotificationHandler = new PushNotificationHandler();
        invokerParams.setUltaHandler(pushNotificationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<HomeActivity><invokePushNotificationService><UltaException>>"
                    + ultaException);

        }

    }

    private Map<String, String> populatePushNotificationParameters(
            String registrationId) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("token", registrationId);
        urlParams.put("isActive", "true");
        return urlParams;
    }

    public class PushNotificationHandler extends UltaHandler {
        public void handleMessage(Message msg) {
            Logger.Log("<PushNotificationHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (null != getErrorMessage()) {
                try {
                    setError(UltaBaseActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

            }
        }
    }

    public void trackAppState(Activity activity, String pageName) {
        OmnitureTracking.startActivity(activity);
        OmnitureTracking.setPageName(pageName);
        OmnitureTracking.stopActivity();

    }

    public void trackAppAction(Activity activity, String actionName) {
        OmnitureTracking.startActivity(activity);
        OmnitureTracking.setAppAction(actionName);
        OmnitureTracking.stopActivity();
    }

    public void trackEvarsUsingActionName(Activity activity, String actionName,
                                          String key, String value) {
        OmnitureTracking.startActivity(activity);

        Map<String, Object> omnitureData = new HashMap<String, Object>();
        omnitureData.put(key, value);

        OmnitureTracking.setEvars(actionName, omnitureData);
        OmnitureTracking.stopActivity();
    }

    public void trackEvarsUsingPageName(Activity activity, String pageName,
                                        String key, String value) {
        OmnitureTracking.startActivity(activity);

        Map<String, Object> omnitureData = new HashMap<String, Object>();
        omnitureData.put(key, value);

        OmnitureTracking.setEvarsUsingPageName(pageName, omnitureData);
        OmnitureTracking.stopActivity();
    }

    public void setError(Activity activity, String error) {
        OmnitureTracking.startActivity(activity);

        Map<String, Object> omnitureData = new HashMap<String, Object>();
        omnitureData.put(WebserviceConstants.ERROR_CODE_KEY, error);

        OmnitureTracking.setErrors(omnitureData);
        OmnitureTracking.stopActivity();

    }

    public void navigateToBasketOnSessionTimeout(Context context) {
        Intent intentForBasket = new Intent(context,
                ViewItemsInBasketActivity.class);
        intentForBasket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentForBasket);
    }

    protected boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            return true;
        }
        return true;
    }

    public void setUpNavigationDrawer() {
        createMenuData();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (null != drawerLayout) {
            navigationDrawer = (LinearLayout) findViewById(R.id.navigation_drawer);
            ListView navigationList = (ListView) findViewById(R.id.naviagtion_list);
            adapter = new SeparatedListAdapter(this);
            adapter.addSection(" ", new CustomAdapter(mainMenu, mainMenuImages, mainMenuTagPositions,
                    "Menu"));
            adapter.addSection("MY DATA", new CustomAdapter(myDataMenu,
                    myDataMenuImages, myDataMenuTagPositions, "Data"));
            navigationList.setAdapter(adapter);
            navigationList.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String tagName = view.getTag().toString();
                    try {
                        position = Integer.parseInt(tagName);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    switch (position) {
                        case OPTION_HOME:
                            if (activity instanceof HomeActivity) {
                                drawerLayout.closeDrawer(navigationDrawer);
                            } else {
                                drawerLayout.closeDrawer(navigationDrawer);
                                Intent intentForHome = new Intent(
                                        UltaBaseActivity.this, HomeActivity.class);
                                intentForHome.putExtra("fromSideMenu", true);
//                                intentForHome
//                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentForHome);
                            }
                            break;
                        case OPTION_FAVORITES:
                            drawerLayout.closeDrawer(navigationDrawer);
                            // ScanIntentIntegrator.initiateScan(UltaBaseActivity.this);

                            if (isUltaCustomer(UltaBaseActivity.this)) {
                                trackAppState(UltaBaseActivity.this,
                                        WebserviceConstants.FAVORITES_LOGGED_IN);
                                Intent intentForFavoritesActivity = new Intent(
                                        UltaBaseActivity.this,
                                        FavoritesActivity.class);
                                startActivity(intentForFavoritesActivity);

                            } else {
                                Intent intentForLogin = new Intent(
                                        UltaBaseActivity.this,
                                        NonSignedInRewardsActivity.class);
                                intentForLogin.putExtra("from",
                                        "fromSideMenufavorites");
                                startActivity(intentForLogin);

                            }

                            break;
                        case OPTION_SHOP:
                            drawerLayout.closeDrawer(navigationDrawer);
                            if (activity instanceof ShopListActivity) {
                                drawerLayout.closeDrawer(navigationDrawer);
                            } else {
                            /*
                             * Intent intentForShop = new
							 * Intent(UltaBaseActivity.this,
							 * ShopExtendedMenuListActivity.class);
							 * startActivity(intentForShop);
							 */
                                drawerLayout.closeDrawer(navigationDrawer);
                                Intent intentForShopList = new Intent(
                                        UltaBaseActivity.this,
                                        ShopListActivity.class);
                                startActivity(intentForShopList);
                            }
                            break;
                        case OPTION_STORE:
                            Intent intentForStoresActivity = new Intent(
                                    UltaBaseActivity.this,
                                    StoresActivity.class);
                            startActivity(intentForStoresActivity);
                            break;
                        // 3.2 release
                        case OPTION_GIFT_CARD_BAL:
                            drawerLayout.closeDrawer(navigationDrawer);

                            if (WebserviceConstants.isCashStarSDKEnabled) {
                                Intent intentCashStarHome = new Intent(
                                        UltaBaseActivity.this, CashStarHomeActivity.class);
                                activityIndicator = 0;
                                startActivity(intentCashStarHome);

                            } else {
                                Intent intentGiftCard = new Intent(
                                        UltaBaseActivity.this,
                                        GiftCardsTabActivity.class);
                                activityIndicator = 0;
                                startActivity(intentGiftCard);
                            }

                            break;
                        // 3.2 release
                        case OPTION_CHECK_ORDER_STATUS:
                            drawerLayout.closeDrawer(navigationDrawer);
                            if (activity instanceof GiftCardsTabActivity
                                    && activityIndicator == 1) {
                            } else if (activity instanceof MyOrderHistoryActivity
                                    && activityIndicator == 1) {

                            } else if (UltaDataCache.getDataCacheInstance()
                                    .isLoggedIn()) {
                                trackAppState(UltaBaseActivity.this,
                                        WebserviceConstants.ORDER_STATUS_LOGGED_IN);
                                Intent intentForOrderStatus = new Intent(
                                        UltaBaseActivity.this,
                                        MyOrderHistoryActivity.class);
                                startActivity(intentForOrderStatus);
                            } else {
                                trackAppState(UltaBaseActivity.this,
                                        WebserviceConstants.ORDER_STATUS_LOGGED_OUT);
                                Intent intentForOrderStatus = new Intent(
                                        UltaBaseActivity.this,
                                        GiftCardsTabActivity.class);
                                intentForOrderStatus.putExtra("MenuKey",
                                        "FromSideMenu");
                                activityIndicator = 1;
                                startActivity(intentForOrderStatus);
                            }
                            break;
                        case OPTION_CALL_SERVIE: {
                            sideMenuCall = true;
                            drawerLayout.closeDrawer(navigationDrawer);
                            checkForAppPermissions(getApplicationContext(), WebserviceConstants.PERMISSION_CALL_PHONE, WebserviceConstants.PHONE_REQUEST_CODE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_TITLE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_MESSAGE);
                            break;
                        }
                        case OPTION_CONTACT_US:
                            drawerLayout.closeDrawer(navigationDrawer);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("plain/text");
                            intent.putExtra(
                                    Intent.EXTRA_EMAIL,
                                    new String[]{UltaConstants.EMAIL_CUSTOMER_CARE});
                            try {
                                intent.putExtra(
                                        Intent.EXTRA_SUBJECT,
                                        "Feedback from Ulta Beauty Android Version "
                                                + getPackageManager()
                                                .getPackageInfo(
                                                        getPackageName(), 0).versionName);
                            } catch (NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra(Intent.EXTRA_TEXT,
                                    UltaConstants.SENT_FROM_ANDROID);
                            startActivity(Intent.createChooser(intent, "ULTA"));
                            break;
                        case OPTION_USER_AGREEMENT:
                            drawerLayout.closeDrawer(navigationDrawer);
                            if (activity instanceof LegalActivity) {
                            } else {
                                trackAppState(UltaBaseActivity.this,
                                        WebserviceConstants.LEGAL);
                                Intent intentForLegal = new Intent(
                                        UltaBaseActivity.this, LegalActivity.class);
                                startActivity(intentForLegal);
                            }
                            break;
                        case OPTION_PRIVACY_POLICY:
                            drawerLayout.closeDrawer(navigationDrawer);
                            if (activity instanceof PrivacyPolicyActivity) {
                            } else {

                                trackAppState(UltaBaseActivity.this,
                                        WebserviceConstants.PRIVACY_POLICY);

                                Intent intentForprivacyPolicy = new Intent(
                                        UltaBaseActivity.this,
                                        PrivacyPolicyActivity.class);
                                startActivity(intentForprivacyPolicy);
                            }
                            break;
                        case OPTION_ULTAMATE_CREDIT_CARD:
                            drawerLayout.closeDrawer(navigationDrawer);
                            Intent ultamateCredtCardIntent = new Intent(UltaBaseActivity.this,
                                    UltaMateCreditCardActivity.class);
                            startActivity(ultamateCredtCardIntent);
                            break;
                        case OPTION_MY_ACCOUNT:
                            if (activity instanceof MyAccountActivity
                                    || activity instanceof LoginActivity) {
                                drawerLayout.closeDrawer(navigationDrawer);
                            } else {
                                if (isUltaCustomer(UltaBaseActivity.this)) {
                                    Intent intentAccounts = new Intent(
                                            UltaBaseActivity.this,
                                            MyAccountActivity.class);
                                    startActivity(intentAccounts);
                                } else {
                                    showLoginDialog(UltaBaseActivity.this,
                                            "myAccountScreen");
                                }
                            }
                            break;
                        case OPTION_ABOUT:
                            if (activity instanceof AboutUsActivity) {
                                drawerLayout.closeDrawer(navigationDrawer);
                            } else {
                                trackAppState(UltaBaseActivity.this,
                                        WebserviceConstants.ABOUT);
                                Intent intentForHelp = new Intent(
                                        UltaBaseActivity.this,
                                        AboutUsActivity.class);
                                startActivity(intentForHelp);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (activity instanceof OlapicActivity || activity instanceof WebViewActivity) {
            // getMenuInflater().inflate(R.menu.olapic_menu, menu);
        } else {

            getMenuInflater().inflate(R.menu.actionbar_menu, menu);
            RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(
                    R.id.action_bag).getActionView();
            TextView basketCount = (TextView) badgeLayout
                    .findViewById(R.id.actionbar_notifcation_textview);
            ImageView bagImageView = (ImageView) badgeLayout
                    .findViewById(R.id.bagImageView);
            bagImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("storedBasketCount", "storedBasketCount");
                    Intent intentForViewItemsInBasketActivity = new Intent(
                            UltaBaseActivity.this,
                            ViewItemsInBasketActivity.class);
                    startActivity(intentForViewItemsInBasketActivity);
                }
            });
            int storedBasketCount = getStoredBasketCount();

            if (itemCountInBasket > 0 || storedBasketCount > 0) {
                basketCount.setText(String.valueOf(itemCountInBasket));
            } else {
                basketCount.setVisibility(View.GONE);
            }
            badgeLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("badgeLayout", "badgeLayout");
                    Intent intentForViewItemsInBasketActivity = new Intent(
                            UltaBaseActivity.this,
                            ViewItemsInBasketActivity.class);
                    startActivity(intentForViewItemsInBasketActivity);
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNavigationDrawer();
        setUpToolBar();

    }

    public int getStoredBasketCount() {
        SharedPreferences basketPreferences = this.getSharedPreferences(
                WebserviceConstants.COUNTS_PREFS_NAME, MODE_PRIVATE);
        int storedBasketCount = basketPreferences.getInt(
                WebserviceConstants.BASKET_COUNT, 0);
        return storedBasketCount;
    }

    public int getStoredFavoritesCount() {
        SharedPreferences basketPreferences = this.getSharedPreferences(
                WebserviceConstants.COUNTS_PREFS_NAME, MODE_PRIVATE);
        int storedBasketCount = basketPreferences.getInt(
                WebserviceConstants.FAVORITES_COUNT, 0);
        return storedBasketCount;
    }

    public void setUpToolBar() {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        // TextView mTitle = (TextView) toolbarTop
        // .findViewById(R.id.title_bar_title);
        if (null != toolbarTop) {

            setSupportActionBar(toolbarTop);
            toolbarTop.setNavigationIcon(R.drawable.hamburger_icon);
            if (activity instanceof HomeActivity) {
                toolbarTop.setLogo(R.drawable.ulta_logo_icon);
            }
            /* toolbarTop.setTitle(""); */
            if (activity instanceof OlapicActivity || activity instanceof WebViewActivity) {
                // toolbarTop.inflateMenu(R.menu.olapic_menu);
            } else {
                toolbarTop.inflateMenu(R.menu.actionbar_menu);
            }
            toolbarTop
                    .setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem arg0) {
                            // Toast.makeText(UltaBaseActivity.this, ""+arg0,
                            // Toast.LENGTH_LONG).show();
                            if (arg0.toString().equals("Bag")) {
                                Intent intentForViewItemsInBasketActivity = new Intent(
                                        UltaBaseActivity.this,
                                        ViewItemsInBasketActivity.class);
                                startActivity(intentForViewItemsInBasketActivity);

                            } else if (arg0.toString().equals("Search")) {
                                Intent intentToSearchActivity = new Intent(
                                        UltaBaseActivity.this,
                                        SearchActivity.class);
                                startActivity(intentToSearchActivity);
                            } else if (arg0.toString().equals("My Account")) {
                                if (activity instanceof MyAccountActivity
                                        || activity instanceof LoginActivity) {
                                } else {
                                    if (isUltaCustomer(UltaBaseActivity.this)) {
                                        Intent intentAccounts = new Intent(
                                                UltaBaseActivity.this,
                                                MyAccountActivity.class);
                                        startActivity(intentAccounts);
                                    } else {
                                        showLoginDialog(UltaBaseActivity.this,
                                                "myAccountScreen");
                                    }
                                }
                            } else if (arg0.toString().equals("Find Store")) {
                                Intent intentForStoresActivity = new Intent(
                                        UltaBaseActivity.this,
                                        StoresActivity.class);
                                startActivity(intentForStoresActivity);
                            } else if (arg0.toString().equals("About")) {
                                if (activity instanceof AboutUsActivity) {
                                } else {
                                    trackAppState(UltaBaseActivity.this,
                                            WebserviceConstants.ABOUT);
                                    Intent intentForHelp = new Intent(
                                            UltaBaseActivity.this,
                                            AboutUsActivity.class);
                                    startActivity(intentForHelp);
                                }
                            } else if (arg0.toString().equals("Scan")) {
                                trackAppState(UltaBaseActivity.this,
                                        WebserviceConstants.SCAN);
                                //check camera permission
                                checkForAppPermissions(UltaBaseActivity.this, WebserviceConstants.PERMISSION_CAMERA, WebserviceConstants.CAMERA_REQUEST_CODE, WebserviceConstants.PERMISSION_CAMERA_DIALOG_TITLE, WebserviceConstants.PERMISSION_CAMERA_DIALOG_MESSAGE);


//                                ScanIntentIntegrator
//                                        .initiateScan(UltaBaseActivity.this);

                            } else if (arg0.toString().equals("UPLOAD")) {
                            }
                            return false;
                        }
                    });
            toolbarTop.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (UltaBaseActivity.this.getCurrentFocus() != null
                            && UltaBaseActivity.this.getCurrentFocus() instanceof EditText) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(UltaBaseActivity.this
                                        .getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    if (null != drawerLayout) {
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(navigationDrawer);
                        } else {
                            drawerLayout.openDrawer(navigationDrawer);
                        }
                    }
                }
            });

        }
    }

    public void sideMenuClick() {
        if (null != drawerLayout) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(navigationDrawer);
            } else {
                drawerLayout.openDrawer(navigationDrawer);
            }
        }
    }

    public Dialog showAlertDialog(final Context context, final String title,
                                  final String message, final String agreeMessage,
                                  final String disagreeMessage) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        alertDialog = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_dialog);
        TextView headingTV = (TextView) alertDialog.findViewById(R.id.heading);
        messageTV = (TextView) alertDialog.findViewById(R.id.message);
        mAgreeButton = (Button) alertDialog.findViewById(R.id.btnAgree);
        mDisagreeButton = (Button) alertDialog.findViewById(R.id.btnDisagree);
        headingTV.setText(title);
        messageTV.setText(message);
        mAgreeButton.setText(agreeMessage);
        mDisagreeButton.setText(disagreeMessage);
        alertDialog.getWindow().setLayout((6 * width) / 7,
                LayoutParams.WRAP_CONTENT);
        return alertDialog;
    }

    public void showLoginDialog(final Context context, final String parentPage) {
        origin = parentPage;
        dialog = new Dialog(context, R.style.AppCompatAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_dialog);
        // set the custom dialog components - text and button

        setHelveticaRegulartTypeFace();
        Button closeDialog = (Button) dialog.findViewById(R.id.close_button);

        Switch staySignedInSwitch = (Switch) dialog
                .findViewById(R.id.stay_signed_in_switch);
        staySignedInSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            UltaDataCache.getDataCacheInstance()
                                    .setStaySignedIn(true);
                            isStaySignedIn = true;

                        } else {
                            UltaDataCache.getDataCacheInstance()
                                    .setStaySignedIn(false);
                            isStaySignedIn = false;
                        }
                    }
                });

        closeDialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != dialog && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        editUsername = (EditText) dialog.findViewById(R.id.editUsername);
        editUsername.setTypeface(helveticaRegularTypeface);
        editUsername.requestFocus();
        usernameErrorText = (TextView) dialog
                .findViewById(R.id.usernameErrorText);
        passwordErrorText = (TextView) dialog
                .findViewById(R.id.passwordErrorText);
        editUsername.addTextChangedListener(this);
        originalDrawable = editUsername.getBackground();

        editPasswordLogin = (EditText) dialog
                .findViewById(R.id.editPasswordLogin);
        editPasswordLogin.setTypeface(helveticaRegularTypeface);

        editPasswordLogin.addTextChangedListener(this);

        TextView txtForgotUsername = (TextView) dialog
                .findViewById(R.id.txtForgotUsername);

        Button btnLogin = (Button) dialog.findViewById(R.id.btnLogin);
        btnLogin.setTypeface(helveticaRegularTypeface);
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editUsername.getWindowToken(), 0);
                validationLogin(editUsername, editPasswordLogin,
                        usernameErrorText, passwordErrorText);
            }
        });

        txtForgotUsername.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                trackAppState(UltaBaseActivity.this,
                        WebserviceConstants.ACCOUNT_FORGOT_PASSWORD_STEP1);
                Intent txtForgotUsernameIntent = new Intent(
                        UltaBaseActivity.this, ForgotLoginActivity.class);
                txtForgotUsernameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(txtForgotUsernameIntent);
            }
        });
        Button btnRegister = (Button) dialog.findViewById(R.id.btnRegister);
        btnRegister.setTypeface(helveticaRegularTypeface);
        btnRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(UltaBaseActivity.this,
                        RegisterDetailsActivity.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                if (null != parentPage) {
                    registerIntent.putExtra("origin", "homeScreen");

                }
                startActivity(registerIntent);

            }
        });
        dialog.show();
    }

    public void validationLogin(EditText editUsername,
                                EditText editPasswordLogin, TextView mUsernameErrorText,
                                TextView mPasswordErrorText) {

        username = editUsername.getText().toString();
        passwordLogin = editPasswordLogin.getText().toString();

        if (username.length() == 0) {

            try {
                editUsername.requestFocus();
                editUsername
                        .setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
                mUsernameErrorText.setText("" + USERNAME_LENGTH_ERROR_MESSAGE);
                mUsernameErrorText.setVisibility(View.VISIBLE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (passwordLogin.length() == 0) {
            try {

                editPasswordLogin
                        .setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
                mPasswordErrorText.setText("" + PASSWORD_LENGTH_ERROR_MESSAGE);
                mPasswordErrorText.setVisibility(View.VISIBLE);

            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        }
        /*
         * else if (passwordLogin.length() < 8) { try {
		 *
		 * editPasswordLogin
		 * .setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light
		 * ); mPasswordErrorText.setText("" + PASSWORD_VALIDATION_MESSAGE);
		 * mPasswordErrorText.setVisibility(View.VISIBLE);
		 *
		 * } catch (WindowManager.BadTokenException e) { } catch (Exception e) {
		 * }
		 *
		 * }
		 */

        else {
            Utility.generateSecretKey();
            pd = new ProgressDialog(UltaBaseActivity.this);
            setProgressDialogLoadingColor(pd);
            pd.setMessage(LOADING_PROGRESS_TEXT);
            pd.show();
            mRegistrationIdSharedPreferences = getSharedPreferences(
                    WebserviceConstants.CHANNEL_ID_PREF, 0);
            pushNotificationSharedPreferences = getSharedPreferences(
                    WebserviceConstants.PUSH_NOTIFICATION_SHAREDPREF,
                    MODE_PRIVATE);
            boolean isActive = pushNotificationSharedPreferences.getBoolean(
                    WebserviceConstants.IS_PUSH_NOTIFICATION_ON, true);
            String registrationId = mRegistrationIdSharedPreferences.getString(
                    WebserviceConstants.PUSH_CHANNEL_ID, "");
            invokeLogin(username, passwordLogin, registrationId, isActive);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == editUsername.getText().hashCode()) {
            editUsername.setBackgroundDrawable(originalDrawable);
            usernameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editPasswordLogin.getText().hashCode()) {
            editPasswordLogin.setBackgroundDrawable(originalDrawable);
            passwordErrorText.setVisibility(View.GONE);
        }
    }

    public void dismissLoginDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void clearAOCookie() {
        String aoCookie = Utility.getCookieValue(WebserviceConstants.AO_COOKIE);
        if (aoCookie != null && aoCookie.trim().length() > 0) {
            Utility.saveCookie(WebserviceConstants.AO_COOKIE, null);
            Utility.saveCookie(WebserviceConstants.EXPIRY_DATE, null);
            /*
             * Toast.makeText(UltaBaseActivity.this, "AO COOKIE DELETE",
			 * Toast.LENGTH_LONG).show();
			 */
        }
    }

    public boolean isDrawerLayoutOpen() {
        if (null != drawerLayout
                && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(navigationDrawer);
            return true;
        } else {
            return false;

        }
    }

    public void checkDensityAndSetImage(ImageView imageView, String url,
                                        int fallBackId, String from, ProgressBar progress, boolean isCacheImage) {


        int density = getResources().getDisplayMetrics().densityDpi;

        String appendDensity = "";

        if (density >= 400) {
            // "xxxhdpi";

            if (from.equalsIgnoreCase("Shop")) {
                appendDensity = getString(R.string.image_shop_xxxhdpi);
            } else if (from.equalsIgnoreCase("LearnMore")) {
                appendDensity = getString(R.string.image_shop_xxxhdpi) + ".png";
            } else {
                appendDensity = getString(R.string.image_xxxhdpi);
            }

        } else if (density >= 300 && density < 400) {
            // xxhdpi

            if (from.equalsIgnoreCase("Shop")) {
                appendDensity = getString(R.string.image_shop_xxhdpi);
            } else if (from.equalsIgnoreCase("LearnMore")) {
                appendDensity = getString(R.string.image_shop_xxhdpi) + ".png";
            } else {
                appendDensity = getString(R.string.image_xxhdpi);
            }

        } else if (density >= 200 && density < 300) {
            // xhdpi

            if (from.equalsIgnoreCase("Shop")) {
                appendDensity = getString(R.string.image_shop_xhdpi);
            } else if (from.equalsIgnoreCase("LearnMore")) {
                appendDensity = getString(R.string.image_shop_xhdpi) + ".png";
            } else {
                appendDensity = getString(R.string.image_xhdpi);
            }

        } else if (density >= 150 && density < 200) {
            // hdpi

            if (from.equalsIgnoreCase("Shop")) {
                appendDensity = getString(R.string.image_shop_hdpi);
            } else if (from.equalsIgnoreCase("LearnMore")) {
                appendDensity = getString(R.string.image_shop_hdpi) + ".png";
            } else {
                appendDensity = getString(R.string.image_hdpi);
            }

        } else if (density >= 100 && density < 150) {
            // mdpi

            if (from.equalsIgnoreCase("Shop")) {
                appendDensity = getString(R.string.image_shop_mdpi);
            } else if (from.equalsIgnoreCase("LearnMore")) {
                appendDensity = getString(R.string.image_shop_mdpi) + ".png";
            } else {
                appendDensity = getString(R.string.image_mdpi);
            }

        } else {
            // hdpi
            if (from.equalsIgnoreCase("Shop")) {
                appendDensity = getString(R.string.image_shop_hdpi);
            } else if (from.equalsIgnoreCase("LearnMore")) {
                appendDensity = getString(R.string.image_shop_hdpi) + ".png";
            } else {
                appendDensity = getString(R.string.image_hdpi);
            }

        }
        if (from.equalsIgnoreCase("REWARDSCARDIMAGE")) {
            appendDensity = "@2x?scl=1";
        }

        url = url.concat(appendDensity);
        url.trim();
        if (null != progress) {
            new AQuery(imageView).progress(progress).image(url, isCacheImage, isCacheImage, 0, fallBackId, null,
                    AQuery.FADE_IN);
        } else {
            new AQuery(imageView).image(url, isCacheImage, isCacheImage, 0, fallBackId, null,
                    AQuery.FADE_IN);
        }

    }

    public void changeEditTextBackground(EditText editText) {

        if (null != editText.getText().toString()
                && !editText.getText().toString().equals("")) {
            editText.setBackgroundResource(R.drawable.apptheme_textfield_black_);
        } else {
            editText.setBackgroundResource(R.drawable.apptheme_textfield_grey_);
        }
    }

    public void resetCache() {

        UltaDataCache.getDataCacheInstance().setGiftTheOrder(false);
        UltaDataCache.getDataCacheInstance().setCreditCardDetails(null);
        UltaDataCache.getDataCacheInstance().setRedeemLevelPoints(null);
    }

    @SuppressWarnings("deprecation")
    public void setProgressDialogLoadingColor(ProgressDialog pDialog) {
        if (Build.VERSION.SDK_INT >= 21) {
            pDialog.setIndeterminateDrawable(getDrawable(R.drawable.progressdialog_loadingcolor));
        } else {
            pDialog.setIndeterminateDrawable(getResources().getDrawable(
                    R.drawable.progressdialog_loadingcolor));
        }

    }

    private void dialPhone() {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:"
                + WebserviceConstants.ULTA_PHONE_NUMBER));
        startActivity(dialIntent);
    }

    public void invokeScan() {
        ScanIntentIntegrator.initiateScan(UltaBaseActivity.this);
    }


    /**
     * Check for App permissions for marshmallow and pre marshmallow
     *
     * @param phonePermission       Permission Name
     * @param permissionRequestCode Permission request Code
     * @param dialogTitle           Title of the rationale  dialog
     * @param dialogMessage         Explanation message why we are requesting this permission
     */
    public void checkForAppPermissions(Context context, String phonePermission, int permissionRequestCode, String dialogTitle, String dialogMessage) {

        try {
            mOnPermissionCheck = (OnPermissionCheck) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean permissionGranted = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission(phonePermission)) {
                requestPermission(phonePermission, permissionRequestCode, dialogTitle, dialogMessage);
            } else {
                permissionGranted = true;
            }
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            switch (permissionRequestCode) {
                case WebserviceConstants.PHONE_REQUEST_CODE:
                    if (null != mOnPermissionCheck && !sideMenuCall) {
                        mOnPermissionCheck.onPermissionCheckRequest(true, permissionRequestCode);
                    } else {
                        dialPhone();
                    }
                    break;
                default:
                    if (null != mOnPermissionCheck) {
                        mOnPermissionCheck.onPermissionCheckRequest(true, permissionRequestCode);
                    } else {
                        if (permissionRequestCode == WebserviceConstants.CAMERA_REQUEST_CODE) {
                            invokeScan();
                        }
                    }
                    break;

            }

        }
    }

    /**
     * Requesting Permissions
     *
     * @param permission  permission name
     * @param requestCode permission request code
     */
    private void requestPermission(String permission, int requestCode, String dialogTitle, String dialogMessage) {
        if (null == activity) {
            setActivity(UltaBaseActivity.this);
        }
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showAlertDialog(dialogTitle, dialogMessage, permission, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * After clicking allow/ deny this call back method will get invoked
     *
     * @param requestCode  Request code for permission
     * @param permissions  permissions names
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (null == activity) {
            setActivity(UltaBaseActivity.this);
        }
        switch (requestCode) {
            case WebserviceConstants.CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (null != mOnPermissionCheck) {
                        mOnPermissionCheck.onPermissionCheckRequest(true, requestCode);
                    } else {

                        invokeScan();

                    }
                } else {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0]);
                    if (!showRationale) {
                        Toast.makeText(activity, WebserviceConstants.PERMISSION_CAMERA_DENIED, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case WebserviceConstants.ACCESS_FINE_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (null != mOnPermissionCheck) {
                        mOnPermissionCheck.onPermissionCheckRequest(true, requestCode);
                    }
                } else {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0]);
                    if (!showRationale) {
                        Toast.makeText(activity, WebserviceConstants.ACCESS_FINE_LOCATION_DENIED, Toast.LENGTH_LONG).show();
                    }

                }
                break;

            case WebserviceConstants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (null != mOnPermissionCheck) {
                        mOnPermissionCheck.onPermissionCheckRequest(true, requestCode);
                    }
                } else {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0]);
                    if (!showRationale) {
                        Toast.makeText(activity, WebserviceConstants.WRITE_EXTERNAL_STORAGE_DENIED, Toast.LENGTH_LONG).show();
                    }
                    mOnPermissionCheck.onPermissionCheckRequest(false, requestCode);
                }
                break;

            case WebserviceConstants.PHONE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (null != mOnPermissionCheck && !sideMenuCall) {
                        mOnPermissionCheck.onPermissionCheckRequest(true, WebserviceConstants.PHONE_REQUEST_CODE);
                    } else {
                        dialPhone();
                    }
                } else {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0]);
                    if (!showRationale) {
                        Toast.makeText(activity, WebserviceConstants.PERMISSION_CALL_PHONE_DENIED, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    /**
     * Check if the permission is Already Granted
     *
     * @param permission
     * @return
     */
    private boolean checkPermission(String permission) {
        if (null == activity) {
            setActivity(UltaBaseActivity.this);
        }
        int result = -1;
        try {
            if (null != permission) {
                result = ContextCompat.checkSelfPermission(activity, permission);
            }
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Show Alert dialog
     *
     * @param title       Title of the alert Dialog
     * @param message     Explanation message why we are requesting this permission
     * @param permission  permission name
     * @param requestCode permission request code
     */
    private void showAlertDialog(String title, String message, final String permission, final int requestCode) {
        if (null == activity) {
            setActivity(UltaBaseActivity.this);
        }
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Request Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);

                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * General class which will identify the card type for the card number
     *
     * @param cardNumber
     * @return
     */
    public CreditCardInfoBean identifyCardType(String cardNumber) {
        CreditCardInfoBean singleCardInfo = null, identifiedCardBean = null;
        if (null != UltaDataCache.getDataCacheInstance().getCreditCardsInfo()) {
            List<CreditCardInfoBean> creditCardsInfo = UltaDataCache.getDataCacheInstance().getCreditCardsInfo();
            if (null != creditCardsInfo && !creditCardsInfo.isEmpty()) {
                for (int i = 0; i < creditCardsInfo.size(); i++) {
                    singleCardInfo = creditCardsInfo.get(i);
                    if (null != singleCardInfo) {
                        /**
                         * CardBin range defines the starting range of each card type
                         */
                        List<String> cardBinRange = singleCardInfo.getCardBINRange();
                        if (null != cardBinRange && !cardBinRange.isEmpty()) {
                            for (int j = 0; j < cardBinRange.size(); j++) {
                                if (cardNumber.startsWith(cardBinRange.get(j))) {
                                    identifiedCardBean = singleCardInfo;
                                }
                            }
                        }
                    }
                }
            }
        }
        return identifiedCardBean;
    }

    /**
     * Check if the card requires expiration date//Edit card details pre populate function
     *
     * @param cardType
     */
    public boolean checkIfExpirationNeeded(String cardType) {
        CreditCardInfoBean singleCardInfo = null;
        boolean expirationNeeded = true;
        if (null != UltaDataCache.getDataCacheInstance().getCreditCardsInfo()) {
            List<CreditCardInfoBean> creditCardsInfo = UltaDataCache.getDataCacheInstance().getCreditCardsInfo();
            if (null != creditCardsInfo && !creditCardsInfo.isEmpty()) {
                for (int i = 0; i < creditCardsInfo.size(); i++) {
                    singleCardInfo = creditCardsInfo.get(i);
                    if (null != singleCardInfo && cardType.equalsIgnoreCase(singleCardInfo.getCardType())) {
                        if (!singleCardInfo.getCardUsesExpirationDate()) {
                            expirationNeeded = false;
                        }
                    }
                }
            }
        }
        return expirationNeeded;
    }

    public void onPagescrolledEvent(int position, float positionOffset) {

    }
}
