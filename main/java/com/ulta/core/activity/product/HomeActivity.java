/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.product;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.AlarmReceiver;
import com.ulta.core.activity.AppRater;
import com.ulta.core.activity.CustomGallery;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.OlapicActivity;
import com.ulta.core.activity.account.RegisterDetailsActivity;
import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.account.WebViewActivity;
import com.ulta.core.activity.rewards.MobileOffersActivity;
import com.ulta.core.activity.rewards.NonSignedInRewardsActivity;
import com.ulta.core.activity.rewards.RewardsActivity;
import com.ulta.core.bean.RewardsBean;
import com.ulta.core.bean.account.AppConfigurableBean;
import com.ulta.core.bean.account.AtgResponseBean;
import com.ulta.core.bean.account.ComponentBean;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.bean.account.PushNotificationBean;
import com.ulta.core.bean.product.MobileCouponInfoAttributesBean;
import com.ulta.core.bean.product.MobileCouponInfoBean;
import com.ulta.core.bean.product.SlideShowBean;
import com.ulta.core.bean.product.atgResponseBean;
import com.ulta.core.bean.product.homePageSectionSlotBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.pushnotification.UltaAirshipReceiver;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.ConversantUtility;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.util.UAStringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * The Class HomeActivity.
 *
 * @author Infosys
 */

public class HomeActivity extends UltaBaseActivity implements OnClickListener, OnSessionTimeOut {

    private LinearLayout mShopLayout;
    private LinearLayout mRewardsLayout;
    private LinearLayout mWeeklyAdLayout;
    private LinearLayout mNewArrivalLayout;
    private LinearLayout mSaleLayout;
    private LinearLayout mOlapicgalleryLayout;
    private LinearLayout mGiftcardLayout;
    private Button mLoginbtn;
    private Button mCreateAccountBtn;
    private TextView mMyRewardsFirstMessageTxtView;
    private TextView mMyRewardsSecondMessageTextView;
    private LinearLayout mLoadingDialog;
    private LinearLayout mMyRewardsMsgSubLayout;

    private TextView mShopTextView;
    private TextView mShopSubTextView;
    private TextView mRewardsTextView;
    private TextView mWeeklyAdsTextView;
    private TextView mWeeklyAdsSubTextview;
    private TextView mNewArrivalsTextView;
    private TextView mSalesTextView;
    private TextView mGiftcardsTextView;
    private TextView mOlapicGalleryTextView;
    private TextView mOlapicSubTextView;
    private TextView mGiftCardSubTextView;
    private TextView mSaleSubTextView;
    private TextView mnewArrivalSubTextView;


    private SharedPreferences mStaySignedInSharedPreferences;
    private String userName;

    private SharedPreferences staySignedInSharedPreferences;
    private Editor staySignedInEditor;

    private RewardsBean rewardsBean;
    private AtgResponseBean atgResponseBean;
    private List<homePageSectionSlotBean> homePageSectionInfo;

    private boolean isRewardMember = false;

    private LinearLayout bubblesLayout;
    private CustomGallery gallery;
    private CustomGallery mOffersGallery;
    private ImageView imgPromoBanner;
    private Handler handler = new Handler();
    private List<atgResponseBean> responseBeanList;

    private List<MobileCouponInfoBean> mMobileCouponInfoBeanList;
    private MobileCouponInfoAttributesBean mMobileCouponInfoAttributesBean;
    private SlideShowBean mSlideShowBean;
    private int size = 1;
    private int PicPosition;
    private int PrevPicPosition = 0;
    private int prevPosition = 0;
    private int mCouponPosition;
    public boolean timer = true;
    public boolean bubble = true;
    private String parameter;
    private String altText;
    private Typeface type;
    private long mScrollCounter;

    // Home page banner refresh variables
    private AlarmManager manager;
    int interval = 1000 * 60 * 60 * 12;// default 12 hour
    private PendingIntent pendingIntent;
    private SharedPreferences refreshTimeOutSharedPreferences;
    private SharedPreferences mMobileOfferSharedPreferences;
    private Editor refreshTimeOutEditor, mMobileOfferSharedtEditor;
    AppConfigurableBean ultaBean;

    // Home page Section
    private SharedPreferences mRegistrationIdSharedPreferences;
    LinearLayout mHomePageSectionLayout;
    Intent homePageSectionIntent;
    homePageSectionSlotBean infoBean;
    String mobileOfferArray;
    private String PROPERTY_APP_VERSION = "appversion";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivity(HomeActivity.this);
        setContentView(R.layout.activity_home);
        AppRater.app_launched(this);

        String channelId = UAirship.shared().getPushManager().getChannelId();
        Log.d("Channel ID", "" + channelId);

        initViews();
        trackAppState(this, WebserviceConstants.HOME_PAGE);
        setTitle("");
        mStaySignedInSharedPreferences = getSharedPreferences(WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                MODE_PRIVATE);

        //  getNotificationRegistrationId();
        invokeSlideShow();

        isRewardMember = Utility.retrieveBooleanFromSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, getApplicationContext());


        mLoadingDialog.setVisibility(View.VISIBLE);

        invokeAppConfigurables();
        if (!UltaDataCache.getDataCacheInstance().isLoggedIn()) {

            Log.d("isLoggedIn not", "isLoggedIn not");
            updateRewardsMessageText();

        } else {
            if (UltaDataCache.getDataCacheInstance().isUpdateBasketAndFavCount()) {
                Log.d("UltaDataCache", "UltaDataCache");
                SharedPreferences staySignedInSharedPref = getSharedPreferences(
                        WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);

                boolean isStaySignedIn = staySignedInSharedPref.getBoolean(
                        WebserviceConstants.IS_STAY_SIGNED_IN, false);

                if (isStaySignedIn) {

                    Log.d("isStaySignedIn", "isStaySignedIn");

                    boolean isActive = staySignedInSharedPref.getBoolean(
                            WebserviceConstants.IS_PUSH_NOTIFICATION_ON, true);

                    mRegistrationIdSharedPreferences = getSharedPreferences(
                            WebserviceConstants.CHANNEL_ID_PREF, 0);

                    String registrationId = mRegistrationIdSharedPreferences
                            .getString(WebserviceConstants.PUSH_CHANNEL_ID, "");
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
                    UltaDataCache.getDataCacheInstance()
                            .setUpdateBasketAndFavCount(false);
                }

            } else if (getIntent().getBooleanExtra("fromSideMenu", false) || isRewardMember) {
                updateRewardsMessageText();
            }

        }
        // Start the alarm manager only whle launching the app
        if (null != getIntent() && null != getIntent().getStringExtra("launch")) {
            if (getIntent().getStringExtra("launch").equals("true")) {
                invokeBannerRefresh();
            }

        }

    }

    /**
     * To start the alarm manager
     */
    private void invokeBannerRefresh() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        interval = refreshTimeOutSharedPreferences.getInt(WebserviceConstants.BANNER_REFRESH_TIME, 60 * 60 * 12);
        interval = interval * 1000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }

    /**
     * To stop the alaram manager
     */
    private void stopBannerRefresh() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkIfUserIsStaySignedIn() || UltaDataCache.getDataCacheInstance().isLoggedIn()) {
            mLoginbtn.setVisibility(View.GONE);
            mCreateAccountBtn.setVisibility(View.GONE);
        } else {
            mLoginbtn.setVisibility(View.VISIBLE);
            mCreateAccountBtn.setVisibility(View.VISIBLE);
        }

        // Change the rewards message
        if (null != staySignedInSharedPreferences) {
            if (!UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                String userName = staySignedInSharedPreferences.getString(WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");
                String loginPassword = staySignedInSharedPreferences.getString(WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
                        " ");
                if (loginPassword.equalsIgnoreCase(" ") && userName.equalsIgnoreCase(" ")) {
                    if (null != mMyRewardsMsgSubLayout && null != mMyRewardsFirstMessageTxtView) {
                        mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);
                        mMyRewardsFirstMessageTxtView.setVisibility(View.VISIBLE);
                        mMyRewardsFirstMessageTxtView.setText(getResources().getString(R.string.my_rewards_sign_in_msg));
                        if (null != mMyRewardsSecondMessageTextView) {
                            mMyRewardsSecondMessageTextView.setVisibility(View.GONE);
                        }
                    }

                }
            }
        }

        // update the rewards message and value in shared preference
        if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
            isRewardMember = Utility.retrieveBooleanFromSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, getApplicationContext());
            updateRewardsMessageText();
        }
        // Check if refresh time is expired in shared preference. If yes call
        // the services.
        if (refreshTimeOutSharedPreferences.getBoolean(WebserviceConstants.IS_REFRESH_TIME_EXPIRED, false)) {

            invokeSlideShow();
            invokeAppConfigurables();
            refreshTimeOutEditor.putBoolean(WebserviceConstants.IS_REFRESH_TIME_EXPIRED, false);
            refreshTimeOutEditor.commit();
        }

// Sanmol software
        // Register a local broadcast manager to listen for ACTION_UPDATE_CHANNEL
        LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        // Use local broadcast manager to receive registration events to update the channel
        IntentFilter channelIdUpdateFilter;
        channelIdUpdateFilter = new IntentFilter();
        channelIdUpdateFilter.addAction(UltaAirshipReceiver.ACTION_UPDATE_CHANNEL);
        locationBroadcastManager.registerReceiver(channelIdUpdateReceiver, channelIdUpdateFilter);

        // Update the channel field
        refreshChannelId();

    }

    private void updateRewardsMessageText() {
        mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);
        if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
            //Non reward member
            if (null == UltaDataCache.getDataCacheInstance().getRewardsBalancePoints()
                    || null == UltaDataCache.getDataCacheInstance().getRewardsBeautyClubNumber()) {
                mMyRewardsFirstMessageTxtView.setVisibility(View.VISIBLE);
                String message = getRewardsPersonalizedGreeting() + " " + getResources().getString(R.string.my_rewards_no_rewards_message);
                mMyRewardsFirstMessageTxtView
                        .setText(message);
                if (null != mMyRewardsSecondMessageTextView) {
                    mMyRewardsSecondMessageTextView.setVisibility(View.GONE);
                }
                return;
            }

            // set and show the named greeting.
            mMyRewardsFirstMessageTxtView.setVisibility(View.VISIBLE);
            mMyRewardsFirstMessageTxtView
                    .setText(getRewardsPersonalizedGreeting());

            // set and show the points (right hand side) rewards subtext
            mMyRewardsSecondMessageTextView.setVisibility(View.VISIBLE);
            mMyRewardsSecondMessageTextView
                    .setText(getRewardsMessageForPoints(UltaDataCache.getDataCacheInstance().getRewardsBalancePoints()));
        } else {
            mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);
            mMyRewardsFirstMessageTxtView.setVisibility(View.VISIBLE);
            mMyRewardsFirstMessageTxtView.setText(getResources().getString(R.string.my_rewards_sign_in_msg));
            if (null != mMyRewardsSecondMessageTextView) {
                mMyRewardsSecondMessageTextView.setVisibility(View.GONE);
            }
        }

    }

    // Sanmol
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        locationBroadcastManager.unregisterReceiver(channelIdUpdateReceiver);
    }

    private void initViews() {

        ProgressBar spinner = new android.widget.ProgressBar(this, null, android.R.attr.progressBarStyle);

        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#f38250"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        mShopLayout = (LinearLayout) findViewById(R.id.shop_layout);
        mRewardsLayout = (LinearLayout) findViewById(R.id.my_rewards_layout);
        mWeeklyAdLayout = (LinearLayout) findViewById(R.id.weekly_ad_layout);
        mNewArrivalLayout = (LinearLayout) findViewById(R.id.now_trending_layout);
        mSaleLayout = (LinearLayout) findViewById(R.id.sale_layout);
        mGiftcardLayout = (LinearLayout) findViewById(R.id.giftCard_layout);
        mOlapicgalleryLayout = (LinearLayout) findViewById(R.id.olapic_gallery_layout);
        mLoginbtn = (Button) findViewById(R.id.btnLogin);
        mCreateAccountBtn = (Button) findViewById(R.id.btnCreateAccount);
        mMyRewardsFirstMessageTxtView = (TextView) findViewById(R.id.my_rewards_msg);
        mMyRewardsSecondMessageTextView = (TextView) findViewById(R.id.my_rewards_second_msg);
        mLoadingDialog = (LinearLayout) findViewById(R.id.homeloadingDialog);
        mMyRewardsMsgSubLayout = (LinearLayout) findViewById(R.id.myrewardsMessageLayout);
        mShopTextView = (TextView) findViewById(R.id.shopTextView);
        mShopSubTextView = (TextView) findViewById(R.id.shopSubText);
        mRewardsTextView = (TextView) findViewById(R.id.myRewardsTextView);
        mNewArrivalsTextView = (TextView) findViewById(R.id.newArrivalsTextView);
        mWeeklyAdsTextView = (TextView) findViewById(R.id.weeklyAdsTextView);
        mWeeklyAdsSubTextview = (TextView) findViewById(R.id.weeklyAdsSubText);
        mSalesTextView = (TextView) findViewById(R.id.saleTextView);
        mGiftcardsTextView = (TextView) findViewById(R.id.giftCardTextView);
        mOlapicGalleryTextView = (TextView) findViewById(R.id.olapicGalleryTextView);
        mOlapicSubTextView = (TextView) findViewById(R.id.olapicSubText);
        mnewArrivalSubTextView = (TextView) findViewById(R.id.newArrivalsSubText);
        mGiftCardSubTextView = (TextView) findViewById(R.id.giftCardSubText);
        mSaleSubTextView = (TextView) findViewById(R.id.saleSubText);
        mHomePageSectionLayout = (LinearLayout) findViewById(R.id.homePageSectionLayout);
        try {
            type = Typeface.createFromAsset(getAssets(), "Helvetica_Reg.ttf");
        } catch (Exception e) {
            type = Typeface.SANS_SERIF;
        }

        mShopTextView.setTypeface(type);
        mRewardsTextView.setTypeface(setHelveticaRegulartTypeFace());
        mNewArrivalsTextView.setTypeface(setHelveticaRegulartTypeFace());
        mWeeklyAdsTextView.setTypeface(setHelveticaRegulartTypeFace());
        mSalesTextView.setTypeface(setHelveticaRegulartTypeFace());
        mGiftcardsTextView.setTypeface(setHelveticaRegulartTypeFace());
        mOlapicGalleryTextView.setTypeface(setHelveticaRegulartTypeFace());
        mOlapicSubTextView.setTypeface(setHelveticaRegulartTypeFace());
        mnewArrivalSubTextView.setTypeface(setHelveticaRegulartTypeFace());
        mGiftCardSubTextView.setTypeface(setHelveticaRegulartTypeFace());
        mSaleSubTextView.setTypeface(setHelveticaRegulartTypeFace());

        mShopSubTextView.setTypeface(setHelveticaRegulartTypeFace());
        mMyRewardsFirstMessageTxtView.setTypeface(setHelveticaRegulartTypeFace());
        mMyRewardsSecondMessageTextView.setTextColor(getResources().getColor(R.color.primaryColor));
        mMyRewardsSecondMessageTextView.setTypeface(setHelveticaRegulartTypeFace());
        mMyRewardsSecondMessageTextView.setTextColor(getResources().getColor(R.color.primaryColor));
        mWeeklyAdsSubTextview.setTypeface(setHelveticaRegulartTypeFace());

        bubblesLayout = (LinearLayout) findViewById(R.id.bubbles_layout);
        imgPromoBanner = (ImageView) findViewById(R.id.homePromoImage);
        gallery = (CustomGallery) findViewById(R.id.gallery1);
        mOffersGallery = (CustomGallery) findViewById(R.id.offersGallery);


        gallery.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (bubble) {
                    timer = false;

                    int bubblePosition = gallery.getSelectedItemPosition();

                    if (bubblePosition >= 0 && bubblePosition < prevPosition) {

                        ((ImageView) bubblesLayout.getChildAt(bubblePosition)).setImageDrawable(getResources()
                                .getDrawable(R.drawable.dot_selected));
                        ((ImageView) bubblesLayout.getChildAt(prevPosition)).setImageDrawable(getResources()
                                .getDrawable(R.drawable.dot_unselected));
                        prevPosition--;
                    } else if (bubblePosition == 0) {
                        ((ImageView) bubblesLayout.getChildAt(bubblePosition)).setImageDrawable(getResources()
                                .getDrawable(R.drawable.dot_selected));

                    } else if (bubblePosition > prevPosition) {
                        ((ImageView) bubblesLayout.getChildAt(bubblePosition)).setImageDrawable(getResources()
                                .getDrawable(R.drawable.dot_selected));
                        ((ImageView) bubblesLayout.getChildAt(prevPosition)).setImageDrawable(getResources()
                                .getDrawable(R.drawable.dot_unselected));
                        prevPosition++;
                    }
                }
                return false;
            }
        });

        gallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String serviceToFire;
                // check if the service name is null or empty
                if (null != responseBeanList.get(arg2).getServiceName()
                        && responseBeanList.get(arg2).getServiceName().trim().length() > 0) {
                    serviceToFire = responseBeanList.get(arg2).getServiceName();
                } else {
                    serviceToFire = "none";
                }
                if (serviceToFire.equalsIgnoreCase("Product Details")) {

                    parameter = responseBeanList.get(arg2).getServiceParameters();
                    Intent intentForUltaProductDetailsActivity = new Intent(HomeActivity.this,
                            UltaProductDetailsActivity.class);
                    intentForUltaProductDetailsActivity.putExtra("id", parameter);

                    intentForUltaProductDetailsActivity.putExtra("altText", responseBeanList.get(arg2).getAltText());
                    intentForUltaProductDetailsActivity.setAction("fromHome");
                    startActivity(intentForUltaProductDetailsActivity);
                } else if (serviceToFire.equalsIgnoreCase("Promotions")) {

                    parameter = responseBeanList.get(arg2).getServiceParameters();
                    if (parameter.equalsIgnoreCase("B")) {
                        Intent buyMoreSaveMoreIntent = new Intent(HomeActivity.this,
                                BuyMoreSaveMoreLandingActivity.class);
                        startActivity(buyMoreSaveMoreIntent);
                    } else if (parameter.equalsIgnoreCase("G")) {
                        Intent gwpIntent = new Intent(HomeActivity.this, GWPLandingActivity.class);
                        startActivity(gwpIntent);
                    } else {
                        Intent intentForPromotionsListActivity = new Intent(HomeActivity.this,
                                PromotionsListActivity.class);
                        intentForPromotionsListActivity.putExtra("flag", parameter);

                        intentForPromotionsListActivity.putExtra("altText", responseBeanList.get(arg2).getAltText());
                        intentForPromotionsListActivity.setAction("fromHome");
                        startActivity(intentForPromotionsListActivity);

                    }

                } else if (serviceToFire.equalsIgnoreCase("Product Listing By Promo Id")) {
                    parameter = responseBeanList.get(arg2).getServiceParameters();
                    Intent intentToSearchActivity = new Intent(HomeActivity.this, UltaProductListActivity.class);
                    intentToSearchActivity.setAction("fromPromotion");
                    intentToSearchActivity.putExtra("promotionId", parameter);

                    intentToSearchActivity.putExtra("altText", responseBeanList.get(arg2).getAltText());
                    startActivity(intentToSearchActivity);
                } else if (serviceToFire.equalsIgnoreCase("Product Listing By Category Id")) {
                    parameter = responseBeanList.get(arg2).getServiceParameters();
                    altText = responseBeanList.get(arg2).getAltText();
                    Intent intent = new Intent(getApplicationContext(), UltaProductListActivity.class);
                    intent.putExtra("catagoryIdFromRoot", parameter);
                    intent.putExtra("catNam", altText);
                    intent.putExtra("From", "ExtendedListActivity");
                    startActivity(intent);

                } else if (serviceToFire.equalsIgnoreCase("Slides")) {
                } else if (serviceToFire.equalsIgnoreCase("Static Content")) {
                    Logger.Log("Static banner");
                } else if (serviceToFire.equalsIgnoreCase("none")) {
                    Logger.Log("Static banner");
                } else if (serviceToFire.equalsIgnoreCase("Product Listing By Brand Name")) {
                    parameter = responseBeanList.get(arg2).getServiceParameters();
                    String brandId = responseBeanList.get(arg2).getBrandId();
                    Intent intentToSearchActivity = new Intent(HomeActivity.this, UltaProductListActivity.class);
                    intentToSearchActivity.setAction("fromHomeByBrand");
                    intentToSearchActivity.putExtra("search", parameter);
                    if (null != brandId && !brandId.equalsIgnoreCase("NA")) {
                        intentToSearchActivity.putExtra("brandId", brandId);
                    }

                    intentToSearchActivity.putExtra("altText", responseBeanList.get(arg2).getAltText());
                    startActivity(intentToSearchActivity);
                } else if (serviceToFire.equalsIgnoreCase("Product listing Spl code")) {

                    parameter = responseBeanList.get(arg2).getServiceParameters();
                    Intent intentToSearchActivity = new Intent(HomeActivity.this, UltaProductListActivity.class);
                    intentToSearchActivity.setAction("fromHomeBySplCode");
                    intentToSearchActivity.putExtra("search", parameter);

                    intentToSearchActivity.putExtra("altText", responseBeanList.get(arg2).getAltText());
                    startActivity(intentToSearchActivity);
                } else if (serviceToFire.equalsIgnoreCase(getResources().getString(R.string.checkIfMobileWebView))) {
                    parameter = responseBeanList.get(arg2).getServiceParameters();
                    Intent intentToWebViewActivity = new Intent(HomeActivity.this, WebViewActivity.class);
                    intentToWebViewActivity.setAction("fromHomeBySplCode");
                    intentToWebViewActivity.putExtra("url", parameter);
                    intentToWebViewActivity.putExtra("altText", responseBeanList.get(arg2).getAltText());
                    intentToWebViewActivity.putExtra("navigateToWebView", WebserviceConstants.FROM_BLACK_FRIDAY);
                    if (null != responseBeanList.get(arg2).getTitle()) {
                        intentToWebViewActivity.putExtra("title", responseBeanList.get(arg2).getTitle());
                    }
                    startActivity(intentToWebViewActivity);
                } else {
                    Toast.makeText(HomeActivity.this, "No Products Found", Toast.LENGTH_LONG).show();
                }
            }

        });

        mOffersGallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                onCouponOffersClicked(arg2);

            }
        });
        mCreateAccountBtn.setTypeface(setHelveticaRegulartTypeFace());
        mLoginbtn.setTypeface(setHelveticaRegulartTypeFace());
        mShopLayout.setOnClickListener(this);
        mRewardsLayout.setOnClickListener(this);
        mWeeklyAdLayout.setOnClickListener(this);
        mNewArrivalLayout.setOnClickListener(this);
        mLoginbtn.setOnClickListener(this);
        mCreateAccountBtn.setOnClickListener(this);
        mSaleLayout.setOnClickListener(this);
        mGiftcardLayout.setOnClickListener(this);
        mOlapicgalleryLayout.setOnClickListener(this);

        refreshTimeOutSharedPreferences = getSharedPreferences(WebserviceConstants.HOME_BANNER_REFRESH_SHAREDPREF,
                MODE_PRIVATE);
        mMobileOfferSharedPreferences = getSharedPreferences(WebserviceConstants.MOBILE_OFFER_SHAREDPREF, MODE_PRIVATE);
        refreshTimeOutEditor = refreshTimeOutSharedPreferences.edit();
        refreshTimeOutEditor.putBoolean(WebserviceConstants.PROD_LARGE_IMAGE_URL, true);
        refreshTimeOutEditor.commit();
        mMobileOfferSharedtEditor = mMobileOfferSharedPreferences.edit();
        staySignedInSharedPreferences = getSharedPreferences(WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                MODE_PRIVATE);

    }

    private void updateBannerRefreshExpiry() {
        refreshTimeOutEditor.putBoolean(WebserviceConstants.IS_REFRESH_TIME_EXPIRED, false);
        refreshTimeOutEditor.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

		/*
         * case R.id.banners_imgview: // To be implemented
		 * 
		 * break;
		 */

            case R.id.shop_layout:

                Intent intentForShopList = new Intent(HomeActivity.this, ShopListActivity.class);
                intentForShopList.putExtra("slideShowBean", mSlideShowBean);
                startActivity(intentForShopList);

                break;

            case R.id.my_rewards_layout:

                if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                    trackAppState(HomeActivity.this, WebserviceConstants.HOME_MY_REWARDS_LOGGED_IN);
                    if (isRewardMember) {
                        Intent intentRewards = new Intent(HomeActivity.this, RewardsActivity.class);
                        startActivity(intentRewards);

                    } else {
                        Intent intentMyAccount = new Intent(HomeActivity.this, NonSignedInRewardsActivity.class);
                        intentMyAccount.putExtra("from",
                                "fromRewards");
                        startActivity(intentMyAccount);
                    }
                } else {
                    trackAppState(HomeActivity.this, WebserviceConstants.HOME_MY_REWARDS_LOGGED_OUT);
                    Intent intentRewards = new Intent(HomeActivity.this, NonSignedInRewardsActivity.class);
                    intentRewards.putExtra("from",
                            "fromRewards");
                    startActivity(intentRewards);
                }

                break;

            case R.id.weekly_ad_layout:
                trackAppState(HomeActivity.this, WebserviceConstants.HOME_PAGE_WEEKILY_AD);
                Intent intentForWeeklyAd = new Intent(HomeActivity.this, WebViewActivity.class);
                intentForWeeklyAd.putExtra("navigateToWebView", WebserviceConstants.FROM_WEEKLYAd);
                intentForWeeklyAd.putExtra("title", getResources().getString(R.string.webViewTitle_weekly_ad));
                startActivity(intentForWeeklyAd);

                break;

            case R.id.now_trending_layout:
                trackAppState(HomeActivity.this, WebserviceConstants.HOME_PAGE_NEW_ARRIVALS);
                Intent intentToNewArrivals = new Intent(HomeActivity.this, UltaProductListActivity.class);
                intentToNewArrivals.setAction("fromHomeByNewArrivals");
                startActivity(intentToNewArrivals);
                break;

            case R.id.sale_layout:
                trackAppState(HomeActivity.this, WebserviceConstants.HOME_PAGE_SALE);
                Intent intent = new Intent(HomeActivity.this, SpecialOffersActivity.class);
                startActivity(intent);

                break;

            case R.id.giftCard_layout:
                Intent giftCardIntent = null;
                giftCardIntent = new Intent(HomeActivity.this, WebViewActivity.class);
                giftCardIntent.putExtra("navigateToWebView", WebserviceConstants.FROM_GIFT_CARD);
                giftCardIntent.putExtra("title", getResources().getString(R.string.webViewTitle_cashStar));
                giftCardIntent.putExtra("url", UltaDataCache.getDataCacheInstance().getGiftCardUrl());
                startActivity(giftCardIntent);
                break;

            case R.id.olapic_gallery_layout:

                Intent olapicIntent = new Intent(HomeActivity.this, OlapicActivity.class);
                startActivity(olapicIntent);

                break;

            case R.id.btnCreateAccount:
                Intent registerIntent = new Intent(HomeActivity.this, RegisterDetailsActivity.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

                registerIntent.putExtra("origin", "homeScreen");
                startActivity(registerIntent);
                break;

            case R.id.btnLogin:

                showLoginDialog(this, "homeScreen");
                break;

            default:
                break;
        }

    }

    public void invokePushNotificationService(String registrationId) {
        InvokerParams<PushNotificationBean> invokerParams = new InvokerParams<PushNotificationBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.PUSH_NOTIFICATION_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populatePushNotificationParameters(registrationId));
        invokerParams.setUltaBeanClazz(PushNotificationBean.class);
        PushNotificationHandler pushNotificationHandler = new PushNotificationHandler();
        invokerParams.setUltaHandler(pushNotificationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<HomeActivity><invokePushNotificationService><UltaException>>" + ultaException);

        }

    }

    private Map<String, String> populatePushNotificationParameters(String registrationId) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("token", registrationId);
        urlParams.put("isActive", "true");
        return urlParams;
    }

    public class PushNotificationHandler extends UltaHandler {
        public void handleMessage(Message msg) {
            Logger.Log("<PushNotificationHandler><handleMessage><getErrorMessage>>" + (getErrorMessage()));
            if (null != getErrorMessage()) {
                try {
                    setError(HomeActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

            }
        }
    }


    protected void invokeAppConfigurables() {
        updateBannerRefreshExpiry();
        InvokerParams<AppConfigurableBean> invokerParams = new InvokerParams<AppConfigurableBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.APPCONFIGURABLES_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);

        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateAppConfigurableParameters());
        invokerParams.setUltaBeanClazz(AppConfigurableBean.class);
        AppConfigurableHandler appConfigurableHandler = new AppConfigurableHandler();
        invokerParams.setUltaHandler(appConfigurableHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            mLoadingDialog.setVisibility(View.GONE);
            Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>" + ultaException);

        }

    }

    private Map<String, String> populateAppConfigurableParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "1");
        return urlParams;
    }

    public class AppConfigurableHandler extends UltaHandler {
        public void handleMessage(Message msg) {
            Logger.Log("<PurchaseHandler><handleMessage><getErrorMessage>>" + (getErrorMessage()));

            if (null != getErrorMessage()) {
                mLoadingDialog.setVisibility(View.GONE);
                try {

                    setError(HomeActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

                mLoadingDialog.setVisibility(View.GONE);
                ultaBean = (AppConfigurableBean) getResponseBean();

                if (null != ultaBean) {
                    Logger.Log("<HomeActivity>" + "BeanPopulated");
                    //Card info details
                    List<CreditCardInfoBean> creditCardInfoBeanList = ultaBean.getCreditCardsInfo();
                    if (null != creditCardInfoBeanList && !creditCardInfoBeanList.isEmpty()) {
                        UltaDataCache.getDataCacheInstance().setCreditCardsInfo(creditCardInfoBeanList);
                    }
                    atgResponseBean = ultaBean.getAtgResponse();
                    homePageSectionInfo = ultaBean.getHomePageSectionSlotInfoV2();
                    Log.d("homePageSectionInfo", "" + homePageSectionInfo);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                    if (null != atgResponseBean) {
                        UltaDataCache.getDataCacheInstance().setAppConfig(atgResponseBean);
                        String encryptionKey = atgResponseBean.getEncrpytion_Key();
                        String giftCardURL = atgResponseBean.getShowGiftCards();
                        String weeklyAdURL = atgResponseBean.getWeeklyAdURL();
                        UltaDataCache.getDataCacheInstance().setGiftCardUrl(giftCardURL);
                        UltaDataCache.getDataCacheInstance().setWeeklyAdURL(weeklyAdURL);

                        String isFabricEnabled = atgResponseBean.getAndroidApp_Fabric();

                        if (null != atgResponseBean.getAndroidApp_CashStar() && atgResponseBean.getAndroidApp_CashStar().equalsIgnoreCase("true") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            WebserviceConstants.isCashStarSDKEnabled = true;

                        String isConversantTag = atgResponseBean.getAndroidApp_ConversantTag();

                        String isMessage = atgResponseBean.getMessageInfo();
                        String responseTime = atgResponseBean.getResponseTime();
                        String connectionTimeout = atgResponseBean.getConnectionTimeout();


                        String bannerRefreshTime = atgResponseBean.getBannerRefreshTime();
                        String prodImageLarge = atgResponseBean.getProdImageLarge();
                        if (null != prodImageLarge && prodImageLarge.equalsIgnoreCase("false")) {
                            refreshTimeOutEditor.putBoolean(WebserviceConstants.PROD_LARGE_IMAGE_URL, false);
                            refreshTimeOutEditor.commit();
                        }

                        if (null != bannerRefreshTime) {
                            refreshTimeOutEditor.putInt(WebserviceConstants.BANNER_REFRESH_TIME,
                                    Integer.parseInt(bannerRefreshTime));
                            refreshTimeOutEditor.commit();
                            if (manager != null) {
                                manager.cancel(pendingIntent);
                            }
                            invokeBannerRefresh();
                        }
                        Utility.saveToSharedPreference(UltaConstants.RESPONSE_TIME, responseTime,
                                getApplicationContext());
                        Utility.saveToSharedPreference(UltaConstants.CONNECTION_TIMEOUT, connectionTimeout,
                                getApplicationContext());

                        if (null != encryptionKey) {
                            UltaDataCache.getDataCacheInstance().setEncryptionKey(encryptionKey);

                        }

                        //set olapic stream id
                        if (null != atgResponseBean.getTestStream()) {
                            String streamId[] = atgResponseBean.getTestStream().split(",");
                            if (null != streamId[0])
                                UltaDataCache.getDataCacheInstance()
                                        .setStreamId(streamId[0].trim());
                        }

                        if (null != isMessage && isMessage.equalsIgnoreCase("true")
                                && UltaDataCache.getDataCacheInstance().isAppLaunched()) {
                            showMessage(atgResponseBean.getMessageDetails());
                            UltaDataCache.getDataCacheInstance().setAppLaunched(false);
                        }

                        //Initialize Fabric , Conversant SDK and App message only once on app launch

                        if (!UltaDataCache.getDataCacheInstance().isThirdPartySDKEnabled()) {
                            if (null != isFabricEnabled && isFabricEnabled.equalsIgnoreCase("true")) {
                                Ulta.enableFabric();
                            }
                            if (null != isConversantTag && isConversantTag.equalsIgnoreCase("true")) {
                                Ulta.enableConversant();
                            }
                            UltaDataCache.getDataCacheInstance().setIsThirdPartySDKEnabed(true);
                        }

                    }

                    if (null != homePageSectionInfo) {
                        mHomePageSectionLayout.removeAllViews();
                        for (int i = 0; i < homePageSectionInfo.size(); i++) {
                            infoBean = homePageSectionInfo.get(i);
                            View sectionView = inflater.inflate(R.layout.homepagesection, null, false);
                            LinearLayout dynamicLayoutForHomeSection = (LinearLayout) sectionView
                                    .findViewById(R.id.dynamicLayoutForHomeSection);
                            TextView mainText = (TextView) sectionView.findViewById(R.id.sectionMainTextView);
                            TextView subText = (TextView) sectionView.findViewById(R.id.sectionSubText);

                            mainText.setTypeface(type);
                            subText.setTypeface(type);

                            Log.d("getSlotDisplayName()", "" + infoBean.getSlotDisplayName());

                            View horizontalbar = sectionView.findViewById(R.id.horizontalBarView);
                            if (null != infoBean.getSlotDisplayName()) {
                                mainText.setText(infoBean.getSlotDisplayName());
                                mainText.setVisibility(View.VISIBLE);
                            }
                            if (null != infoBean.getSlotDescription()) {
                                subText.setText(infoBean.getSlotDescription());
                                subText.setVisibility(View.VISIBLE);
                            }
                            if (i == (homePageSectionInfo.size() - 1)) {
                                horizontalbar.setVisibility(View.GONE);
                            }
                            if (infoBean.getServiceName().equalsIgnoreCase("butterfly")) {
                                UltaDataCache.getDataCacheInstance().setShowButterfly(true);
                                UltaDataCache.getDataCacheInstance().setButterflyResponse(ultaBean);
                                setUpNavigationDrawer();
                                if (UltaDataCache.getDataCacheInstance().isLoggedIn() &&
                                        null != Utility.retrieveFromSharedPreference(
                                                UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                                                getApplicationContext()) &&
                                        !Utility.retrieveFromSharedPreference(
                                                UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                                                getApplicationContext()).trim().isEmpty()) {

                                    subText.setText(WebserviceConstants.LOGGED_IN_WITH_CARD_USER_SUB_TEXT);

                                } else {
                                    subText.setText(WebserviceConstants.GUEST_USER_SUB_TEXT);
                                }
                            }
                            final int ClickPosition = i;
                            dynamicLayoutForHomeSection.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    infoBean = homePageSectionInfo.get(ClickPosition);

                                    // Enable the cash star sdk native page
                                    Logger.Log("<LoginsActivity>service name :" + infoBean.getServiceName());
                                    if ((infoBean.getServiceName().equalsIgnoreCase("gift card") || infoBean.getSlotDisplayName().equalsIgnoreCase("GIFT CARDS")) && WebserviceConstants.isCashStarSDKEnabled) {

                                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                                            homePageSectionIntent = new Intent(HomeActivity.this, CashStarHomeUI.class);
                                        } else {

                                            homePageSectionIntent = new Intent(HomeActivity.this, WebViewActivity.class);
                                            homePageSectionIntent.putExtra("navigateToWebView", WebserviceConstants.FROM_GIFT_CARD);
                                            homePageSectionIntent.putExtra("title", infoBean.getTitle());
                                            homePageSectionIntent.putExtra("url", infoBean.getServiceParameters());
                                        }
                                    } else {
                                        homePageSectionIntent = Utility.navigateToPage(HomeActivity.this,
                                                infoBean.getServiceParameters(), infoBean);
                                    }
                                    startActivity(homePageSectionIntent);
                                }
                            });
                            mHomePageSectionLayout.addView(sectionView);
                        }
                    }


                }

            }
        }
    }

    /**
     * Invoke login.
     *
     * @param username      the username
     * @param passwordLogin the password login
     */
    private void invokeLogin(String username, String passwordLogin, String registrationId, boolean isActive) {
        InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.LOGIN_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateLoginParameters(username, passwordLogin, registrationId, isActive));
        invokerParams.setUltaBeanClazz(LoginBean.class);
        // invokerParams.setUserSessionClearingRequired(true);
        LoginHandler loginHandler = new LoginHandler();
        invokerParams.setUltaHandler(loginHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            mLoadingDialog.setVisibility(View.GONE);
            Logger.Log("<LoginsActivity><invokeLogin><UltaException>>" + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @param username      the username
     * @param passwordLogin the password login
     * @return Map<String, String>
     */
    private Map<String, String> populateLoginParameters(String username, String passwordLogin, String registrationId,
                                                        boolean isActive) {
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
            Logger.Log("<LoginHandler><handleMessage><getErrorMessage>>" + (getErrorMessage()));

            if (null != getErrorMessage()) {
                mLoadingDialog.setVisibility(View.GONE);
                if (getErrorMessage().startsWith("401")) {

                    askRelogin(HomeActivity.this);
                } else {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                    try {
                        mLoadingDialog.setVisibility(View.GONE);

                    } catch (WindowManager.BadTokenException e) {
                        mLoadingDialog.setVisibility(View.GONE);
                    } catch (Exception e) {
                        mLoadingDialog.setVisibility(View.GONE);
                    }
                }
            } else {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                // invokeRewards();
                Logger.Log("<Login><handleMessage><getResponseBean>>" + (getResponseBean()));
                LoginBean ultaBean = (LoginBean) getResponseBean();
                String isLoggedIn = ultaBean.getResult();
                List<String> result = ultaBean.getErrorInfos();


                Logger.Log("<LoginHandler><handleMessage><getResponseBean>>" + result);
                if (isLoggedIn.equalsIgnoreCase("true")) {
                    if (isPersisted) {
                        createMenuData();
                    }
                    if (null != ultaBean && null != ultaBean.getComponent() && null != ultaBean.getComponent().get_email()) {
                        userName = ultaBean.getComponent().get_email();
                    }
                    UltaDataCache.getDataCacheInstance().setLoggedIn(true);
                    UltaDataCache.getDataCacheInstance().setLoginName(userName);

                    ConversantUtility.loginTag(userName);

                    Utility.saveToSharedPreference(UltaConstants.REMEMBER_ME, UltaConstants.REMEMBER_CLICKED,
                            getApplicationContext());

                    Utility.saveToSharedPreference(UltaConstants.LOGGED_MAIL_ID, userName, getApplicationContext());

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
                        mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);
                        ComponentBean componentBean = ultaBean.getComponent();

                        //update basket count

                        if (null != ultaBean.getComponent()
                                .getShoppingCartCount()
                                && !ultaBean.getComponent()
                                .getShoppingCartCount().isEmpty()) {
                            try {
                                int bagCount = Integer.parseInt(ultaBean
                                        .getComponent()
                                        .getShoppingCartCount());
                                if (bagCount > 0) {
                                    setItemCountInBasket(bagCount);

                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }


                        // show the rewards subtext area
                        mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);
                        if (null != componentBean.get_beautyClubNumber()) {
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
                        if (null != ultaBean.getComponent()
                                .get_beautyClubNumber()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardsBeautyClubNumber(
                                            ultaBean.getComponent()
                                                    .get_beautyClubNumber());
                        }

                        UltaDataCache.getDataCacheInstance()
                                .setRewardsBalancePoints(
                                        componentBean.get_balancePoints());

                        updateRewardsMessageText();
                    } else {
                        updateRewardsMessageText();


                    }

                    if (UltaDataCache.getDataCacheInstance().isOrderSubmitted()) {
                        UltaDataCache.getDataCacheInstance().setOrderSubmitted(false);
                        finish();
                    }

                }
                // login failure
                else {
                    mLoadingDialog.setVisibility(View.GONE);
                    try {

                        if (result.get(0).contains(
                                "user name/password is incorrect") || result.get(0).contains("The password is incorrect") || result.get(0).contains("Please enter valid username.")) {

                            invokeLogout();

                        }


                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }

            }
        }
    }

    /**
     * Invoke rewards.
     */
    private void invokeRewards() {
        InvokerParams<RewardsBean> invokerParams = new InvokerParams<RewardsBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.REWARDS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setCookieHandlingSkip(false);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateRewardsParameters());
        invokerParams.setUltaBeanClazz(RewardsBean.class);
        RewardsHandler userCreationHandler = new RewardsHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            mLoadingDialog.setVisibility(View.GONE);
            Logger.Log("<RewardsActivity><invokeForgotPassword><UltaException>>" + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @return Map<String, String>
     */
    private Map<String, String> populateRewardsParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "2");
        return urlParams;
    }

    /**
     * The Class RewardsHandler.
     */
    public class RewardsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */

        public void handleMessage(Message msg) {
            Logger.Log("<RewardsHandler><handleMessage><getErrorMessage>>" + (getErrorMessage()));
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    if (!UltaDataCache.getDataCacheInstance().isLoggedIn())
                        askRelogin(HomeActivity.this);
                } else {
                    mLoadingDialog.setVisibility(View.GONE);
                    try {


                        mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);
                        mMyRewardsFirstMessageTxtView
                                .setVisibility(View.VISIBLE);
                        if (null == getFirstNameForRewardsGreeting()) {
                            mMyRewardsFirstMessageTxtView
                                    .setText(R.string.my_rewards_no_rewards_message);
                        }

                        setError(HomeActivity.this, getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                mLoadingDialog.setVisibility(View.GONE);
                Logger.Log("<RewardsHandler><handleMessage><getResponseBean>>" + (getResponseBean()));
                rewardsBean = (RewardsBean) getResponseBean();

                // show the rewards subtext area
                mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);

                // early exit
                if (null == rewardsBean || null == rewardsBean.getBalancePoints()) {
                    mMyRewardsFirstMessageTxtView.setVisibility(View.VISIBLE);
                    mMyRewardsFirstMessageTxtView.setText(R.string.my_rewards_no_rewards_message);
                    return;
                }

                //set and show the named greeting.
                mMyRewardsFirstMessageTxtView.setVisibility(View.VISIBLE);
                mMyRewardsFirstMessageTxtView.setText(getRewardsPersonalizedGreeting());

                // set and show the points (right hand side) rewards subtext
                mMyRewardsSecondMessageTextView.setVisibility(View.VISIBLE);
                mMyRewardsSecondMessageTextView.setText(getRewardsMessageForPoints(rewardsBean.getBalancePoints()));
            }
        }
    }

    private String getRewardsMessageForPoints(String rewardsBeanPoints) {
        int memberValue = 0;
        try {
            float t = Float.valueOf(rewardsBeanPoints);
            memberValue = (int) t;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String pointsMessage;
        if (memberValue <= 0) {
            pointsMessage = getResources().getString(R.string.my_rewards_second_message_zeropts);
        } else {
            final String youHave = getResources().getString(R.string.my_rewards_second_message);
            pointsMessage = youHave + " " + memberValue + " points!";
        }
        return pointsMessage;
    }

    private String getRewardsPersonalizedGreeting() {
        final String firstName = getFirstNameForRewardsGreeting();
        final String hiText = getResources().getString(R.string.my_rewards_first_message);
        StringBuilder greetingBuilder = new StringBuilder(hiText);
        if (null != firstName) {
            greetingBuilder.append(", ").append(firstName);
        }
        greetingBuilder.append("!");
        return greetingBuilder.toString();
    }

    private String getFirstNameForRewardsGreeting() {
        String firstName;
        String possibleFirstName = mStaySignedInSharedPreferences.getString(
                WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME, " ");
        if (possibleFirstName.trim().length() != 0) {
            firstName = possibleFirstName;
        } else if (UltaDataCache.getDataCacheInstance().getUserFirstName().length() != 0) {
            firstName = UltaDataCache.getDataCacheInstance().getUserFirstName();
        } else {
            firstName = null;
        }

        // correct the capitalization of a valid, non-null name
        if (firstName != null) {
            firstName = firstName.substring(0, 1).toUpperCase(Locale.US)
                    + firstName.substring(1).toLowerCase(Locale.US);
        }
        return firstName;
    }

    protected void invokeSlideShow() {
        updateBannerRefreshExpiry();
        InvokerParams<SlideShowBean> invokerParams = new InvokerParams<SlideShowBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.HOMEPAFEINFO_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateShippmentParameters());
        invokerParams.setUltaBeanClazz(SlideShowBean.class);
        PurchaseHandler userCreationHandler = new PurchaseHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>" + ultaException);

        }

    }

    private Map<String, String> populateShippmentParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();

        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "1");

        return urlParams;
    }

    public class PurchaseHandler extends UltaHandler {
        public void handleMessage(Message msg) {
            Logger.Log("<PurchaseHandler><handleMessage><getErrorMessage>>" + (getErrorMessage()));

            if (null != getErrorMessage()) {
                showPromoBanner();
                try {

                    setError(HomeActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

                SlideShowBean ultaBean = (SlideShowBean) getResponseBean();
                mSlideShowBean = ultaBean;

                List<String> errors = ultaBean.getErrorInfos();
                if (null != errors && !(errors.isEmpty()))
                    try {

                        setError(HomeActivity.this, errors.get(0));
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                else {

                    if (null != ultaBean) {
                        Logger.Log("<HomeActivity>" + "BeanPopulated");
                        responseBeanList = ultaBean.getResponse();
                        imgPromoBanner.setVisibility(View.GONE);
                        gallery.setVisibility(View.VISIBLE);
                        bubblesLayout.setVisibility(View.GONE);

                        if (!responseBeanList.isEmpty()) {
                            size = responseBeanList.size();

                            if (responseBeanList.size() == 0) {
                                gallery.setVisibility(View.GONE);
                                bubblesLayout.setVisibility(View.GONE);
                                imgPromoBanner.setVisibility(View.VISIBLE);
                                showPromoBanner();
                            } else {
                                gallery.setAdapter(new ImageAdapter(HomeActivity.this));
                                int bubblePosition = gallery.getSelectedItemPosition();

                                for (int j = 0; j < size; j++) {
                                    ImageView imageBubble = new ImageView(HomeActivity.this);

                                    imageBubble.setImageDrawable(getResources().getDrawable(R.drawable.dot_unselected));

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                    params.gravity = Gravity.BOTTOM;
                                    imageBubble.setPadding(0, 0, 15, 0);
                                    imageBubble.setLayoutParams(params);
                                    bubblesLayout.addView(imageBubble);
                                }

                                mScrollCounter = 0;
                                ((ImageView) bubblesLayout.getChildAt(bubblePosition))
                                        .setImageResource(R.drawable.dot_selected);

                                if (size == 1) {
                                    bubble = false;
                                } else {
                                    bubble = true;

                                    handler.removeCallbacks(runnable);
                                    handler.postDelayed(runnable, 1000);
                                }
                            }

                        } else {
                            gallery.setVisibility(View.GONE);
                            bubblesLayout.setVisibility(View.GONE);
                            imgPromoBanner.setVisibility(View.VISIBLE);
                            showPromoBanner();
                        }

                        // To show the coupon even if banner is not available
                        mMobileCouponInfoBeanList = ultaBean.getMobileCouponInfo();

                        mobileOfferArray = "";

                        if (null != mMobileCouponInfoBeanList) {
                            for (int i = 0; i < mMobileCouponInfoBeanList.size(); i++) {

                                mMobileCouponInfoAttributesBean = mMobileCouponInfoBeanList.get(i).getAttributes();

                                if (null != mMobileCouponInfoAttributesBean) {
                                    String eCouponActive = mMobileCouponInfoAttributesBean.geteCouponActive();
                                    if (null != eCouponActive && eCouponActive.equalsIgnoreCase("Y")) {
                                        if (null != mMobileCouponInfoBeanList.get(i).getPath()) {
                                            mobileOfferArray = mobileOfferArray
                                                    + mMobileCouponInfoBeanList.get(i).getPath() + ",";
                                        }

                                    } else {
                                        mMobileCouponInfoBeanList.remove(i);
                                        i--;
                                        if (i < 0) {
                                            break;
                                        }
                                    }
                                }

                            }
                            mMobileOfferSharedtEditor.putString(WebserviceConstants.MOBILE_OFFER_ARRAY,
                                    mobileOfferArray);
                            mMobileOfferSharedtEditor.commit();

                        }

                        if (null != mMobileCouponInfoBeanList && mobileOfferArray.length() > 0) {

                            OffersAdapter offersAdapter = new OffersAdapter(HomeActivity.this);
                            mOffersGallery.setAdapter(offersAdapter);
                            mMobileOfferSharedtEditor.putString(WebserviceConstants.MOBILE_OFFER_ARRAY,
                                    mobileOfferArray);
                            mMobileOfferSharedtEditor.commit();

                        } else {
                            mOffersGallery.setVisibility(View.GONE);
                        }
                    }
                }

            }
        }
    }


    private void showPromoBanner() {
        mMyRewardsMsgSubLayout.setVisibility(View.VISIBLE);
        if (!haveInternet()) {
            String firstName = mStaySignedInSharedPreferences.getString(WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME,
                    " ");
            if (firstName.trim().length() != 0) {
                mMyRewardsFirstMessageTxtView.setVisibility(View.VISIBLE);
                mMyRewardsFirstMessageTxtView.setText(getResources().getString(R.string.my_rewards_first_message) + " "
                        + firstName + "!");
                mMyRewardsSecondMessageTextView.setText(getResources().getString(
                        R.string.my_rewards_second_message_zeropts));
                mMyRewardsSecondMessageTextView.setVisibility(View.VISIBLE);
            } else {
                mMyRewardsFirstMessageTxtView.setVisibility(View.GONE);
                mMyRewardsFirstMessageTxtView
                        .setText(getResources().getString(R.string.my_rewards_first_message) + "!");
                mMyRewardsSecondMessageTextView.setVisibility(View.VISIBLE);
                mMyRewardsSecondMessageTextView.setText(getResources().getString(
                        R.string.my_rewards_second_message_zeropts));
            }

        }
        imgPromoBanner.setVisibility(View.VISIBLE);
        imgPromoBanner.setBackgroundResource(R.drawable.home_header);
    }

    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        // private ImageView[] mImages;
        public ImageAdapter(Context c) {
            mContext = c;
            // mImages = new ImageView[size];
            TypedArray a = c.obtainStyledAttributes(R.styleable.BezelImageView);
            mGalleryItemBackground = a.getResourceId(R.styleable.AppTheme_actionbarCompatItemHomeStyle, 0);
            a.recycle();
        }

        @Override
        public int getCount() {
            return responseBeanList.size();

        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ImageView i = new ImageView(mContext);
            if (null != responseBeanList) {
                // Down sizing is removed by changing target width to zero
                // Appending _android to the image Url to get android mobile
                // specific images
                String urlForAndroid = responseBeanList.get(position).getPath();
                new AQuery(i).image(urlForAndroid, true, true, 0, R.drawable.home_header, null, AQuery.FADE_IN);
            }
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;

            if (bubble) {
                i.setLayoutParams(new CustomGallery.LayoutParams(width, CustomGallery.LayoutParams.MATCH_PARENT));
            } else {
                i.setLayoutParams(new CustomGallery.LayoutParams(CustomGallery.LayoutParams.MATCH_PARENT,
                        CustomGallery.LayoutParams.MATCH_PARENT));
            }
            i.setScaleType(ImageView.ScaleType.FIT_XY);

            // i.setBackgroundResource(mGalleryItemBackground);
            return i;
        }

    }

    private class OffersAdapter extends BaseAdapter {

        public OffersAdapter(Context c) {
            // mImages = new ImageView[size];
            TypedArray a = c.obtainStyledAttributes(R.styleable.BezelImageView);
            a.recycle();
        }

        @Override
        public int getCount() {

            return mMobileCouponInfoBeanList.size();
        }

        @Override
        public Object getItem(int arg0) {

            return null;
        }

        @Override
        public long getItemId(int arg0) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            // TextView offersText = new TextView(mContext);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.mobile_offers, null);
            }

            try {
                type = Typeface.createFromAsset(getAssets(), "Helvetica_Reg.ttf");
            } catch (Exception e) {
                type = Typeface.SANS_SERIF;
            }

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;

            TextView mobileOffersTextview = (TextView) convertView.findViewById(R.id.home_offers_message);

            mobileOffersTextview.setWidth(width);

            mobileOffersTextview.setText(mMobileCouponInfoBeanList.get(position).getCouponDescription());

            return convertView;
        }

    }

    public void onCouponOffersClicked(int position) {
        trackAppState(HomeActivity.this, WebserviceConstants.HOME_PAGE_COUPON);
        Log.e("Mobile Offers", "Offerslayout is clicked " + position);
        if (null != mMobileCouponInfoBeanList.get(position).getPath()
                && mMobileCouponInfoBeanList.get(position).getPath().trim().length() > 0) {
            Intent intentMobileOffers = new Intent(HomeActivity.this, MobileOffersActivity.class);
            intentMobileOffers.putExtra("couponUrl", mMobileCouponInfoBeanList.get(position).getPath());
            intentMobileOffers.putExtra("position", position);
            if (null != mMobileCouponInfoBeanList.get(position).getAttributes() && null != mMobileCouponInfoBeanList.get(position).getAttributes().getTitle()) {
                intentMobileOffers.putExtra("title", mMobileCouponInfoBeanList.get(position).getAttributes().getTitle());
            }
            String[] offerUrl = mobileOfferArray.split(",");
            intentMobileOffers.putExtra("couponBean", offerUrl);
            startActivity(intentMobileOffers);
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            mScrollCounter++;
            if (mScrollCounter % 7 == 0) {
                myslideshow();
            }
            if (mScrollCounter % 3 == 0) {

                if (mMobileCouponInfoBeanList.size() != 0 && mMobileCouponInfoBeanList.size() != 1) {
                    couponSlideShow();
                }
            }

            handler.postDelayed(runnable, 1000);
        }
    };

    private void myslideshow() {
        PicPosition = gallery.getSelectedItemPosition();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        if (PicPosition == 0) {
            PicPosition = gallery.getSelectedItemPosition() + 1;
            PrevPicPosition = gallery.getSelectedItemPosition();
            gallery.setSelection(PicPosition);
            gallery.startAnimation(animation);

        } else if (PicPosition > PrevPicPosition) {
            PicPosition = gallery.getSelectedItemPosition() + 1;
            PrevPicPosition = gallery.getSelectedItemPosition();
            if (PrevPicPosition == size - 1) {
                PrevPicPosition = gallery.getSelectedItemPosition();
                PicPosition = gallery.getSelectedItemPosition() - 1;
                gallery.setSelection(PicPosition);
            } else {
                gallery.setSelection(PicPosition);
            }
            gallery.startAnimation(animation);
        } else {
            PicPosition = gallery.getSelectedItemPosition() - 1;
            PrevPicPosition = gallery.getSelectedItemPosition() - 1;
            gallery.setSelection(PicPosition);
            gallery.startAnimation(animation);
        }
        int bubblePosition = gallery.getSelectedItemPosition();
        if (bubblePosition < prevPosition) {
            ((ImageView) bubblesLayout.getChildAt(bubblePosition)).setImageDrawable(getResources().getDrawable(
                    R.drawable.dot_selected));
            ((ImageView) bubblesLayout.getChildAt(prevPosition)).setImageDrawable(getResources().getDrawable(
                    R.drawable.dot_unselected));
            prevPosition--;
        } else if (bubblePosition == 0) {
            ((ImageView) bubblesLayout.getChildAt(bubblePosition)).setImageDrawable(getResources().getDrawable(
                    R.drawable.dot_selected));

        } else if (bubblePosition > prevPosition) {
            ((ImageView) bubblesLayout.getChildAt(bubblePosition)).setImageDrawable(getResources().getDrawable(
                    R.drawable.dot_selected));
            ((ImageView) bubblesLayout.getChildAt(prevPosition)).setImageDrawable(getResources().getDrawable(
                    R.drawable.dot_unselected));
            prevPosition++;
        }
    }

    private void couponSlideShow() {

        mCouponPosition++;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        if (mCouponPosition < mMobileCouponInfoBeanList.size()) {

            mOffersGallery.setSelection(mCouponPosition);
            mOffersGallery.startAnimation(animation);

        } else if (mMobileCouponInfoBeanList.size() == mCouponPosition) {

            mCouponPosition = 0;
            mOffersGallery.setSelection(0);
            mOffersGallery.startAnimation(animation);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the alarm manager if the app is exiting
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(70);

        if (taskList.get(0).numActivities == 1) {
            stopBannerRefresh();
        }
        if (isTaskRoot()) {
            AQUtility.cleanCacheAsync(this);
        }
    }

    @Override
    public void onBackPressed() {


        if (isDrawerLayoutOpen()) {

        } else {

            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(70);

            if (taskList.get(0).numActivities == 1) {

                staySignedInSharedPreferences = getSharedPreferences(WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                        MODE_PRIVATE);
                boolean isStaySignedIn = staySignedInSharedPreferences.getBoolean(
                        WebserviceConstants.IS_STAY_SIGNED_IN, true);
                if (!isStaySignedIn) {
                    staySignedInEditor = staySignedInSharedPreferences.edit();
                    staySignedInEditor.putBoolean(WebserviceConstants.IS_STAY_SIGNED_IN, false);
                    staySignedInEditor.putString(WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");
                    staySignedInEditor.putString(WebserviceConstants.STAY_SIGNED_IN_PASSWORD, " ");
                    staySignedInEditor.putString(WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
                    staySignedInEditor.commit();
                    UltaDataCache.getDataCacheInstance().setAppClosed(true);

                    invokeLogout();

                }
            }
            super.onBackPressed();
        }
        // }

    }

    // show message
    @SuppressWarnings("deprecation")
    public void showMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();
        TextView messageView = (TextView) alertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess && UltaDataCache.getDataCacheInstance().isLoggedIn()) {
//			invokeRewards();
        } else {
            mLoadingDialog.setVisibility(View.GONE);
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (UltaDataCache.getDataCacheInstance().isIfNotSignedInClearSession()) {
                UltaDataCache.getDataCacheInstance().setIfNotSignedInClearSession(false);
                mLoginbtn.setVisibility(View.VISIBLE);
                mCreateAccountBtn.setVisibility(View.VISIBLE);
            }
        }
    }


//    public void getNotificationRegistrationId() {
//        PushNotificationGCMRegister pushNotificationGCMRegister = new PushNotificationGCMRegister(this);
//        pushNotificationGCMRegister.getRegistrationId();
//
//    }

//   @Override
//    public void OnRegistrationIdReceived(String regId, boolean mAlreadyRegistered) {
//
//        if (!mAlreadyRegistered) {
//            invokePushNotificationService(regId);
//        }
//    }

    private final BroadcastReceiver channelIdUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshChannelId();
        }
    };

    private void refreshChannelId() {
        String channelId = UAirship.shared().getPushManager().getChannelId();
        channelId = UAStringUtil.isEmpty(channelId) ? "" : channelId;

        if (channelId == null || channelId.equals("")) {
            Log.i("Not register", "Not register for notification ");
        } else {
            SharedPreferences prefs = getActivity().getSharedPreferences(
                    WebserviceConstants.CHANNEL_ID_PREF, 0);
            int appVersion = getAppVersion(getActivity());

            Log.i("Register", "Registered for notification ");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(WebserviceConstants.PUSH_CHANNEL_ID, channelId);
            editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.commit();
            invokePushNotificationService(channelId);
        }
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


}
