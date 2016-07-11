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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.about.LearnMoreActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.activity.account.RegisterDetailsActivity;
import com.ulta.core.bean.UltaBean;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

public class NonSignedInRewardsActivity extends UltaBaseActivity implements OnSessionTimeOut, TextWatcher {

    private Button mLoginbtn;
    private Button mCreateAccountBtn;
    private ImageView mRewardsMainImage;
    private static final int REQ_CODE_LOGIN_REWARDS = 222;
    private static final int REQ_CODE_LEARN_MORE = 333;
    private ProfileBean profileBean;
    private String firstName, lastName, dateOfBirth, userName,
            phNumber, addLine1, addLine2,
            city, state, country, zip;
    String memberID;
    boolean isProfileWebService = true;
    EditText rewardIdET;
    TextView idErrorText;
    private Drawable originalDrawable;
    private boolean isRewardUser = false;
    private boolean isActivate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_signedin_rewards);
        setActivity(NonSignedInRewardsActivity.this);
        trackAppState(NonSignedInRewardsActivity.this,
                WebserviceConstants.FAVORITES_LOGGED_OUT);
        mLoginbtn = (Button) findViewById(R.id.btnRewardsLogin);
        mCreateAccountBtn = (Button) findViewById(R.id.btnRewardsCreateAccount);
        mRewardsMainImage = (ImageView) findViewById(R.id.rewardsMainImage);
        mLoginbtn.setTypeface(setHelveticaRegulartTypeFace());
        mCreateAccountBtn.setTypeface(setHelveticaRegulartTypeFace());
        if (null != getIntent().getExtras()) {
            if (null != getIntent().getExtras().getString("from")) {
                if (getIntent().getExtras().getString("from")
                        .equalsIgnoreCase("fromSideMenufavorites")) {
                    mCreateAccountBtn.setText(getResources().getString(
                            R.string.create_account));
                    mLoginbtn.setText(getResources().getString(
                            R.string.login_btn_signIn_Caps));
                    setFavoritesBanner();
                    setTitle("Favorites");
                } else {
                    setTitle("My Rewards");
                    setRewardBanner();
                }
            }
        } else {
            setTitle("My Rewards");
            setRewardBanner();
        }

        mLoginbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Coming from home screen rewards click
                if (null != getIntent().getExtras()
                        && null != getIntent().getExtras().getString("from")
                        && getIntent().getExtras().getString("from").equalsIgnoreCase("fromRewards")) {
                    Intent learnMoreIntent = new Intent(
                            NonSignedInRewardsActivity.this,
                            LearnMoreActivity.class);
                    startActivityForResult(learnMoreIntent, REQ_CODE_LEARN_MORE);
                }
                //Coming from favorites
                else {
                    Intent loginIntent = new Intent(
                            NonSignedInRewardsActivity.this, LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    loginIntent.putExtra("origin", "isfromMyRewards");
                    startActivity(loginIntent);
                    finish();
                }

            }
        });

        mCreateAccountBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Coming from home screen rewards click
                if (null != getIntent().getExtras()
                        && null != getIntent().getExtras().getString("from")
                        && getIntent().getExtras().getString("from").equalsIgnoreCase("fromRewards")) {
                    //SIGNED IN NON REWARDS USER
                    if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                        //Show alert Dialog
                        showBottomSheet();
                    }
                    //GUEST USER
                    else {
                        Intent loginIntent = new Intent(
                                NonSignedInRewardsActivity.this, LoginActivity.class);
                        loginIntent.putExtra("origin", "isfromMyRewards");
                        startActivityForResult(loginIntent, REQ_CODE_LOGIN_REWARDS);
                    }

                }
                //Coming from favorites
                else {
                    Intent registerIntent = new Intent(
                            NonSignedInRewardsActivity.this,
                            RegisterDetailsActivity.class);
                    registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(registerIntent);
                    finish();
                }
            }
        });

    }

    private void setFavoritesBanner() {
        if (haveInternet()) {
            checkDensityAndSetImage(mRewardsMainImage,
                    WebserviceConstants.FAVORITES_BANNER,
                    R.drawable.favorites, "Favorites", null, false);
        } else {
            mRewardsMainImage.setImageResource(R.drawable.favorites);
        }
    }

    private void setRewardBanner() {
        if (haveInternet()) {
            checkDensityAndSetImage(mRewardsMainImage,
                    WebserviceConstants.REWARDS_BANNER,
                    R.drawable.rewards, "Rewards", null, false);
        } else {
            mRewardsMainImage.setImageResource(R.drawable.rewards);
        }
    }

    private void showBottomSheet() {
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.alert_dialog); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView heading = (TextView) mBottomSheetDialog.findViewById(R.id.heading);
        heading.setText("Get Rewarded");
        TextView message = (TextView) mBottomSheetDialog.findViewById(R.id.message);
        message.setVisibility(View.GONE);
        Button mAgreeButton = (Button) mBottomSheetDialog.findViewById(R.id.btnAgree);
        mAgreeButton.setPadding(40, 0, 40, 10);
        mAgreeButton.setText("Yes, sign me up!");
        Button mDisagreeButton = (Button) mBottomSheetDialog.findViewById(R.id.btnDisagree);
        mDisagreeButton.setPadding(40, 0, 40, 10);
        mDisagreeButton.setText("I'm already a member");
        mAgreeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                invokeMyProfileDetails();
            }
        });
        mDisagreeButton.setOnClickListener(new OnClickListener() {

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
        final Dialog alertDialog = new Dialog(NonSignedInRewardsActivity.this, R.style.AppCompatAlertDialogStyle);
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
        mAgreeButton.setOnClickListener(new OnClickListener() {

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
        mDisagreeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_CODE_LOGIN_REWARDS) {
            if (Utility.retrieveBooleanFromSharedPreference(
                    UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
                    getApplicationContext())) {
                Intent intentRewards = new Intent(NonSignedInRewardsActivity.this,
                        RewardsActivity.class);
                startActivity(intentRewards);
                finish();
            } else {
                if (isUltaCustomer(NonSignedInRewardsActivity.this)) {
                    showBottomSheet();
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQ_CODE_LEARN_MORE) {
            //SIGNED IN NON REWARDS USER
            if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                //Show alert Dialog
                showBottomSheet();
            }
            //GUEST USER
            else {
                Intent loginIntent = new Intent(
                        NonSignedInRewardsActivity.this, LoginActivity.class);
                loginIntent.putExtra("origin", "isfromMyRewards");
                startActivityForResult(loginIntent, REQ_CODE_LOGIN_REWARDS);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void fnInvokeJoinRewardsProgramme(Boolean isJoin) {
        try {
            if (null == pd) {
                pd = new ProgressDialog(NonSignedInRewardsActivity.this);
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
        InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.JOIN_REWARDS);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateOrderDetailsHandlerParameters(isJoin));
        invokerParams.setUltaBeanClazz(UltaBean.class);
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
        urlParams.put("atg-rest-depth", "0");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");

        if (isJoin) {
            isActivate = false;
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
            isActivate = true;
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
                    isProfileWebService = false;
                    askRelogin(NonSignedInRewardsActivity.this);

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
                                NonSignedInRewardsActivity.this);
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
                UltaBean ultaBean = (UltaBean) getResponseBean();
                trackAppAction(
                        NonSignedInRewardsActivity.this,
                        WebserviceConstants.LOYALTY_ACCOUNT_CREATED_EVENT_ACTION);
                if (null != ultaBean) {
                    List<String> errors = ultaBean.getErrorInfos();
                    if (null != errors && !(errors.isEmpty())) {
                        try {
                            notifyUser(errors.get(0), NonSignedInRewardsActivity.this);

                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }
                    } else {
                        UltaDataCache.getDataCacheInstance().setRewardMember(
                                true);
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.IS_REWARD_MEMBER,
                                true, getApplicationContext());
                        String message = WebserviceConstants.JOIN_REWARDS_SUCCESS;
                        if (isActivate) {
                            message = WebserviceConstants.ACTIVATE_REWARDS_SUCCESS;
                        }
                        final Dialog alertDialog = showAlertDialog(
                                NonSignedInRewardsActivity.this,
                                message, "", "OK", "");
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        mDisagreeButton.setVisibility(View.GONE);
                        messageTV.setVisibility(View.GONE);
                        mAgreeButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                Intent intentRewards = new Intent(NonSignedInRewardsActivity.this,
                                        RewardsActivity.class);
                                startActivity(intentRewards);
                                finish();
                            }
                        });
                    }
                } else {
                    notifyUser("Please try later", NonSignedInRewardsActivity.this);
                }

            }
        }
    }

    /**
     * Invoke my profile details.
     */
    private void invokeMyProfileDetails() {
        pd = new ProgressDialog(NonSignedInRewardsActivity.this);
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
                    askRelogin(NonSignedInRewardsActivity.this);

                } else {
                    try {
                        if (null != pd && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                NonSignedInRewardsActivity.this);
                        setError(NonSignedInRewardsActivity.this, getErrorMessage());
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
                        setError(NonSignedInRewardsActivity.this, profileBean
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
                    if (isRewardUser) {
                        try {
                            if (null != pd && pd.isShowing()) {
                                pd.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        fnInvokeJoinRewardsProgramme(true);
                    }
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

    public void setError(EditText editText, TextView errorTV, String message) {
        errorTV.setText("" + message);
        errorTV.setVisibility(View.VISIBLE);
        editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == rewardIdET.getText().hashCode()) {
            rewardIdET.setBackgroundDrawable(originalDrawable);
            idErrorText.setVisibility(View.GONE);
        }
    }
}
