/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.rewards;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.MyOrderHistoryActivity;
import com.ulta.core.activity.account.WebViewActivity;
import com.ulta.core.activity.checkout.PromoCodeActivity;
import com.ulta.core.bean.CampaignOfferListBean;
import com.ulta.core.bean.RewardsBean;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class RewardsActivity.
 */
public class RewardsActivity extends UltaBaseActivity implements
        OnClickListener, OnSessionTimeOut {
    private int highestTierThreshold;
    private boolean isPlatinum = false;
    private boolean isReQualified = false;
    private int progress;
    /**
     * The lyt status.
     */
    private LinearLayout lytBonus, lytPlatinum, lytExclusive, lytBenefits,
            lytEarning, lytCard, lytPoints, lytStatus, layoutpointsexpiry;

    private FrameLayout lytAlerts, frameLayoutProgress;

    /**
     * The profile bean.
     */
    private ProfileBean profileBean;

    /**
     * linear layout for view my reward card added for 3.2 release
     */
    private LinearLayout lytViewMyRewardsCard;

    /**
     * The pb points.
     */
    private ProgressBar pbPoints;

    /**
     * The alert list.
     */
    private List<String> alertList;

    /**
     * The point redeem.
     */
    /* private int pointRedeem; */

    private double ytdSpent;
    private int currentYear;
    private int nextYear;
    private double dollarSpendForHighestLevel;
    /**
     * The points expiration date.
     */

    private String memberSince;

    /**
     * 3.2 release string for holding memberId
     */

    private TextView tvPointExpiryDate, tvPointExpiryValue,
            AlertsCount, tvPointsAway,
            tvmemberSince, tvmemberId, tvBalancePoints, tvRedeemablePoints;
    private LinearLayout upperArrowLayout, pointsExpiryView;
    private TextView tvQualifyingYear;
    private TextView bonusOffers, txtPlanDesc, txtHighesThreshold,
            txtMiddleThreshold;
    private RewardsBean rewardsBean;
    // private ProgressDialog pd;
    private LinearLayout main_layout, loadingDialog, llmemberSince, llmemberId,
            lytMyProfile, lytPurchase;

    /**
     * The reward.
     */
    public static ArrayList<String> reward = new ArrayList<String>();
    ;

    private String beautyClubMember;

    private TextView txtMemberId;

    private ImageView imgBarCode, cardImage;

    private int screenWidth;
    // private int screenHeight;

    private TextView mRewardsMembershipHeadingTextView;
    private TextView mMemberShipNumTextView;
    private TextView mStatusHeadingTextView;
    private TextView mMemberSinceTextView;
    private TextView mMyPointsHeadingTextView;
    private TextView mBalancePointHeadingTextView;
    private TextView mReedemablePointsTextView;
    private TextView mOffersAndRewardsHeadingTextView;

    private boolean mInvokeRewards;
    private boolean mInvokeMyProfileDetails;

    private List<CampaignOfferListBean> mCampaignOfferList;// to get the offer  details
    Button learnMoreOrManageBtn;
    private boolean isUserHavingUltamateCard = false;

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        registerForLogoutBroadcast();
        lytPurchase = (LinearLayout) findViewById(R.id.lytMyPurchaseHistory);
        lytCard = (LinearLayout) findViewById(R.id.lytGiftCard);
        lytBonus = (LinearLayout) findViewById(R.id.lytBonusOffer);
        lytBenefits = (LinearLayout) findViewById(R.id.lytBenefits);
        lytExclusive = (LinearLayout) findViewById(R.id.lytExclusiveBonusOffers);
        lytEarning = (LinearLayout) findViewById(R.id.lytEarninig);
        lytPlatinum = (LinearLayout) findViewById(R.id.lytPlatinum);
        lytViewMyRewardsCard = (LinearLayout) findViewById(R.id.lytViewMyRewardsCard);
        lytPoints = (LinearLayout) findViewById(R.id.lytMyPoints);
        lytStatus = (LinearLayout) findViewById(R.id.lytMyStatus);
        lytAlerts = (FrameLayout) findViewById(R.id.lytAlerts);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);

        llmemberId = (LinearLayout) findViewById(R.id.llmemberId);
        AlertsCount = (TextView) findViewById(R.id.AlertsCount);
        bonusOffers = (TextView) findViewById(R.id.bonusOffers);
        tvmemberId = (TextView) findViewById(R.id.tvmemberId);
        lytMyProfile = (LinearLayout) findViewById(R.id.loMyProfile);
        txtPlanDesc = (TextView) findViewById(R.id.txtPlanDesc2);
        txtHighesThreshold = (TextView) findViewById(R.id.fnHighesThreshold);
        txtMiddleThreshold = (TextView) findViewById(R.id.middleThreshold);
        upperArrowLayout = (LinearLayout) findViewById(R.id.upperArrowLayout);
        pointsExpiryView = (LinearLayout) findViewById(R.id.pointsExpiryView);
        frameLayoutProgress = (FrameLayout) findViewById(R.id.frameLayoutProgress);

        txtMemberId = (TextView) findViewById(R.id.txtRewardsMemberId);
        imgBarCode = (ImageView) findViewById(R.id.img_2D_bar_code);
        txtMemberId.setTypeface(setHelveticaRegulartTypeFace());

        mRewardsMembershipHeadingTextView = (TextView) findViewById(R.id.rewardsMembershipHeading);
        mMemberShipNumTextView = (TextView) findViewById(R.id.membershipNumTextView);
        mStatusHeadingTextView = (TextView) findViewById(R.id.txtPlanDesc1);
        mMemberSinceTextView = (TextView) findViewById(R.id.memberSinceTextView);
        mMyPointsHeadingTextView = (TextView) findViewById(R.id.my_pointsHeading);
        mBalancePointHeadingTextView = (TextView) findViewById(R.id.txtBalancePoints);
        mReedemablePointsTextView = (TextView) findViewById(R.id.txtRedeemablePoints);
        mOffersAndRewardsHeadingTextView = (TextView) findViewById(R.id.OffersAndRewardsHeading);

        cardImage = (ImageView) findViewById(R.id.cardImage);
        learnMoreOrManageBtn = (Button) findViewById(R.id.learnMoreOrManageBtn);


        setTitle("My Rewards");
        learnMoreOrManageBtn.setOnClickListener(this);
        lytCard.setOnClickListener(this);
        lytBonus.setOnClickListener(this);
        lytBenefits.setOnClickListener(this);
        lytExclusive.setOnClickListener(this);
        lytEarning.setOnClickListener(this);
        lytPlatinum.setOnClickListener(this);
        lytViewMyRewardsCard.setOnClickListener(this);

        lytStatus.setOnClickListener(this);
        lytAlerts.setOnClickListener(this);
        lytPurchase.setOnClickListener(this);
        loadingDialog.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
        invokeRewards();
        setUltamateCardDetails();
        setTypeFace();
    }

    private void setUltamateCardDetails() {
        //User having ultamate card
        if (UltaDataCache.getDataCacheInstance().isLoggedIn() && null != Utility.retrieveFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                RewardsActivity.this) && !Utility.retrieveFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                RewardsActivity.this).trim().isEmpty()) {
            learnMoreOrManageBtn.setText(getResources().getString(R.string.manage_account));
            isUserHavingUltamateCard = true;
            if (Utility.retrieveFromSharedPreference(
                    UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                    RewardsActivity.this).trim().equalsIgnoreCase("Ultamate Rewards Credit Card")) {
                checkDensityAndSetImage(cardImage,
                        WebserviceConstants.ULTAMATE_CC_CARD,
                        R.drawable.creditcard_default, "REWARDSCARDIMAGE", null, false);
            }
            else  if (Utility.retrieveFromSharedPreference(
                    UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                    RewardsActivity.this).trim().equalsIgnoreCase("Ultamate Rewards MasterCard")){
                //Ultamate Rewards MasterCard
                checkDensityAndSetImage(cardImage,
                        WebserviceConstants.ULTAMATE_MC_CARD,
                        R.drawable.creditcard_default, "REWARDSCARDIMAGE", null, false);
            }
            else
            {
                checkDensityAndSetImage(cardImage,
                        WebserviceConstants.ULTAMATE_MC_CARD,
                        R.drawable.creditcard_default, "REWARDSCARDIMAGE", null, false);
                learnMoreOrManageBtn.setText(getResources().getString(R.string.rewards_learn_more));
            }

        }
        //guest or user not having ultamate card
        else {
            checkDensityAndSetImage(cardImage,
                    WebserviceConstants.ULTAMATE_MC_CARD,
                    R.drawable.creditcard_default, "REWARDSCARDIMAGE", null, false);
            learnMoreOrManageBtn.setText(getResources().getString(R.string.rewards_learn_more));
            isUserHavingUltamateCard = false;
        }

    }

    public void setTypeFace() {
        mRewardsMembershipHeadingTextView
                .setTypeface(setHelveticaRegulartTypeFace());
        mMyPointsHeadingTextView.setTypeface(setHelveticaRegulartTypeFace());

        mMemberShipNumTextView.setTypeface(setHelveticaRegulartTypeFace());
        mStatusHeadingTextView.setTypeface(setHelveticaRegulartTypeFace());
        mMemberSinceTextView.setTypeface(setHelveticaRegulartTypeFace());
        mBalancePointHeadingTextView
                .setTypeface(setHelveticaRegulartTypeFace());
        mReedemablePointsTextView.setTypeface(setHelveticaRegulartTypeFace());
        mOffersAndRewardsHeadingTextView
                .setTypeface(setHelveticaRegulartTypeFace());
        tvmemberId.setTypeface(setHelveticaRegulartTypeFace());

        bonusOffers.setTypeface(setHelveticaRegulartTypeFace());
        txtPlanDesc.setTypeface(setHelveticaRegulartTypeFace());
        txtHighesThreshold.setTypeface(setHelveticaRegulartTypeFace());
        txtMiddleThreshold.setTypeface(setHelveticaRegulartTypeFace());
        bonusOffers.setTypeface(setHelveticaRegulartTypeFace());
        bonusOffers.setTypeface(setHelveticaRegulartTypeFace());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Invoke rewards.
     */
    private void invokeRewards() {
        InvokerParams<RewardsBean> invokerParams = new InvokerParams<RewardsBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.REWARDS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateRewardsParameters());
        invokerParams.setUltaBeanClazz(RewardsBean.class);
        RewardsHandler userCreationHandler = new RewardsHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<RewardsActivity><invokeForgotPassword><UltaException>>"
                    + ultaException);

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
            Logger.Log("<RewardsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    mInvokeRewards = true;
                    askRelogin(RewardsActivity.this);
                } else {
                    loadingDialog.setVisibility(View.GONE);
                    main_layout.setVisibility(View.VISIBLE);
                    try {
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                RewardsActivity.this);
                        setError(RewardsActivity.this, getErrorMessage());

                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }

                }
            } else {
                invokeMyProfileDetails();
                main_layout.setVisibility(View.VISIBLE);
                Logger.Log("<RewardsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                rewardsBean = (RewardsBean) getResponseBean();

                if (null != rewardsBean) {
                    String responseStatus = rewardsBean.getResponseStatus();

                    Logger.Log("<RewardsHandler><handleMessage><getResponseBean>>"
                            + responseStatus);
                    alertList = rewardsBean.getAlertList();
                    if (null != rewardsBean.getErrorInfos()) {
                        setError(RewardsActivity.this, rewardsBean
                                .getErrorInfos().get(0));
                    }
                    if (null != alertList) {
                        if (alertList.size() >= 0) {
                            AlertsCount.setText(""
                                    + Integer.valueOf(alertList.size()));
                        }
                    }

                    // get the campaignOfferList from bean and display number of
                    // offers available

                    mCampaignOfferList = rewardsBean.getCampaignOfferList();
                    if (null != mCampaignOfferList) {
                        if (mCampaignOfferList.size() >= 0) {
                            bonusOffers
                                    .setText("("
                                            + Integer
                                            .valueOf(mCampaignOfferList
                                                    .size()) + ")");
                        }
                    }

                    isReQualified = rewardsBean.isRequalified();
                    isPlatinum = rewardsBean.isPlatinum();

                    memberSince = rewardsBean.getMemberSince();
                    currentYear = rewardsBean.getCurrentYear();
                    nextYear = rewardsBean.getNextYear();
                    ytdSpent = rewardsBean.getYtdSpent();
                    dollarSpendForHighestLevel = rewardsBean
                            .getDollarSpendForHighestLevel();
                    highestTierThreshold = rewardsBean
                            .getHighestTierThreshold();
                    setExpiryDateAndDisplayPoints(
                            rewardsBean.getPointsExpirationDate(),
                            rewardsBean.getPointsExpiring());
                    setBalancePointss(rewardsBean.getBalancePoints());
                    setRedeemablePoints(rewardsBean.getPointRedeem(),
                            rewardsBean.getPointRedeemValue());
                    if (null == memberSince) {
                        lytMyProfile.setVisibility(LinearLayout.GONE);
                    } else {
                        setMyStatus();
                    }


                }

                loadingDialog.setVisibility(View.GONE);
            }
        }
    }

    public void setMyStatus() {

        tvPointsAway = (TextView) findViewById(R.id.tvPointsAway);
        tvmemberSince = (TextView) findViewById(R.id.tvmemberSince);
        tvQualifyingYear = (TextView) findViewById(R.id.tvQualifyingYear);
        pbPoints = (ProgressBar) findViewById(R.id.pbPoints);
        llmemberSince = (LinearLayout) findViewById(R.id.llmemberSince);

        tvPointsAway.setTypeface(setHelveticaRegulartTypeFace());
        tvmemberSince.setTypeface(setHelveticaRegulartTypeFace());
        tvQualifyingYear.setTypeface(setHelveticaRegulartTypeFace());

        if (isPlatinum) {
            txtPlanDesc.setText(""
                    + getResources().getString(R.string.platinum));
        } else {
            txtPlanDesc.setText("" + getResources().getString(R.string.member));
        }


        tvQualifyingYear.setText(currentYear
                + " QUALIFYING SPEND TO PLATINUM STATUS");
        /**
         * Set the progress bar for points
         */
        if (highestTierThreshold != 0) {

            // setting bottom right threshold value and middle value
            txtHighesThreshold.setText("$" + highestTierThreshold);
            txtMiddleThreshold.setText("$"
                    + Math.round((highestTierThreshold / 2)));
            // calculating progress value
            progress = (int) Math
                    .round(((ytdSpent / highestTierThreshold) * 100));

            // Dynamically Create text view and set amount spent value on top of
            // progress bar
            final TextView upperArrowText = new TextView(this);
            upperArrowText.setTypeface(setHelveticaRegulartTypeFace());
            upperArrowText.setTextSize(9);
            if(ytdSpent>=highestTierThreshold)
            {
                upperArrowText.setText("Congrats!");
            }
            else {
                upperArrowText.setText("$" + String.format("%.2f", ytdSpent));
            }
            upperArrowText.setTextColor(getResources().getColor(R.color.black));

            // After progress bar UI is created in layout, we can take the
            // leading edge position and set X value for vertical line and top
            // amount value
            pbPoints.post(new Runnable() {
                @Override
                public void run() {
                    // Calculate estimate of ProgressBar leading edge position
                    FrameLayout.LayoutParams params;
                    params = (FrameLayout.LayoutParams) pbPoints
                            .getLayoutParams();
                    int contentWidth = pbPoints.getWidth();
                    int leadingEdge = ((contentWidth * pbPoints.getProgress()) / pbPoints
                            .getMax()) + params.leftMargin;

                    // Setting the positon for amount spent text view
                    upperArrowText.setX(leadingEdge);

                    // converting the margin value in dp to int
                    int dpValue = 10; // margin in dp
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in int

                    // Vertical line view
                    View verticalLine = new View(RewardsActivity.this);
                    verticalLine.setBackgroundColor(getResources().getColor(
                            R.color.black));
                    // Layout params for vertical line
                    LayoutParams veticalParams = new LayoutParams(5,
                            LayoutParams.FILL_PARENT);
                    // Checking if the points exceeds threshold.
                    if (progress > 90) {
                        upperArrowText.setX(pbPoints.getWidth() - (margin * 2));
                    }
                    if (progress == 0) {
                        verticalLine.setLayoutParams(veticalParams);
                        verticalLine.setX(leadingEdge);
                        frameLayoutProgress.addView(verticalLine, 0);
                    } else if (progress < 100) {
                        verticalLine.setLayoutParams(veticalParams);
                        verticalLine.setX(leadingEdge);
                        frameLayoutProgress.addView(verticalLine, 0);
                    }
                    // Set the vertical line at extreme right position
                    else {
                        verticalLine.setLayoutParams(veticalParams);
                        verticalLine.setX(pbPoints.getWidth() + margin);
                        frameLayoutProgress.addView(verticalLine, 0);
                    }
                    upperArrowLayout.addView(upperArrowText);

                }
            });
            pbPoints.setProgress(progress);

        }
        if (isPlatinum && isReQualified) {
            tvPointsAway.setText("You're Platinum through "
                    + nextYear + "!");
        } else {
            if (isPlatinum && !isReQualified) {
                tvPointsAway.setText("You are $"
                        + String.format("%.2f",
                        Double.valueOf(dollarSpendForHighestLevel))
                        + " from keeping your platinum status in " + nextYear);
            } else {
                tvPointsAway.setText("You are $"
                        + String.format("%.2f",
                        Double.valueOf(dollarSpendForHighestLevel))
                        + " away from becoming Platinum");
            }

        }
        if (null != memberSince) {
            tvmemberSince.setText(memberSince);
        } else {
            llmemberSince.setVisibility(View.GONE);
        }

    }

    public void setRedeemablePoints(int pointRedeem2, double pointRedeemValue2) {
        tvRedeemablePoints = (TextView) findViewById(R.id.txtRedeemeablePointsValue);
        tvRedeemablePoints.setTypeface(setHelveticaRegulartTypeFace());
        tvRedeemablePoints.setText(pointRedeem2 + " (Value of " + "$"
                + String.format("%.2f", Double.valueOf(pointRedeemValue2))
                + ")");
    }

    public void setExpiryDateAndDisplayPoints(String pointExpiryDate,
                                              int displayPoints) {
        tvPointExpiryValue = (TextView) findViewById(R.id.tvPointExpiryValue);
        if (displayPoints != 0) {
            if (null != pointExpiryDate) {
                pointsExpiryView.setVisibility(View.VISIBLE);
                tvPointExpiryValue.setText(displayPoints + " (On " + pointExpiryDate + ")");
//				tvPointExpiryDate.setText(pointExpiryDate);
            }
        } else {
            tvPointExpiryValue.setText("" + displayPoints);

        }
    }

    public void setBalancePointss(String balancePoints) {
        tvBalancePoints = (TextView) findViewById(R.id.txtBalancePointsValue);
        tvBalancePoints.setText(String.valueOf(balancePoints).split("\\.")[0]);
        tvBalancePoints.setTypeface(setHelveticaRegulartTypeFace());
    }

    @Override
    public void onLogout() {
        finish();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lytGiftCard:
                Intent intentMyRewards = new Intent(RewardsActivity.this,
                        PromoCodeActivity.class);
                intentMyRewards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intentMyRewards.putExtra("isfromRewards", 1);
                startActivity(intentMyRewards);
                break;

            case R.id.lytBonusOffer:
                trackAppState(this, WebserviceConstants.REWARDS_BONUS_OFFER);
                Intent intentBonusOffers = new Intent(RewardsActivity.this,
                        MyBonusOffersActivity.class);
                intentBonusOffers.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intentBonusOffers.putExtra("offers", rewardsBean);

                startActivity(intentBonusOffers);
                break;
            case R.id.lytBenefits:
                trackAppState(this, WebserviceConstants.REWARDS_BENEFITS);
                Intent gotoBenefits = new Intent(RewardsActivity.this,
                        RewardsWebViewActivity.class);
                gotoBenefits.putExtra("rewardsFrom",
                        WebserviceConstants.FROM_BENIFITS);
                gotoBenefits.putExtra("key", "benefits");
                startActivity(gotoBenefits);
                break;

            case R.id.lytEarninig:
                trackAppState(this, WebserviceConstants.REWARDS_EARNING_REDEEMING);
                Intent intentEarning = new Intent(RewardsActivity.this,
                        RewardsWebViewActivity.class);
                intentEarning.putExtra("rewardsFrom",
                        WebserviceConstants.FROM_EARNINGSANDREDEEMING);

                startActivity(intentEarning);
                break;

            case R.id.lytPlatinum:
                trackAppState(this, WebserviceConstants.REWARDS_PLATINMUM_PROGRAM);
                Intent intentPlatinum = new Intent(RewardsActivity.this,
                        RewardsWebViewActivity.class);
                intentPlatinum.putExtra("rewardsFrom",
                        WebserviceConstants.FROM_PLATINUM);
                intentPlatinum.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intentPlatinum);
                break;
            case R.id.lytViewMyRewardsCard:
                trackAppState(this, WebserviceConstants.VIEW_REWARDS_CARD);
                Intent viewMyRewardCard = new Intent(RewardsActivity.this,
                        ViewMyRewardsCardActivity.class);
                if (beautyClubMember != null) {
                    viewMyRewardCard.putExtra("MemberId", beautyClubMember);
                }
                startActivity(viewMyRewardCard);
                break;

            case R.id.lytAlerts:
                Intent intentAlerts = new Intent(RewardsActivity.this,
                        AlertsActivity.class);

                intentAlerts.putStringArrayListExtra("alerts",
                        (ArrayList<String>) alertList);
                startActivity(intentAlerts);
                break;


            case R.id.lytMyPurchaseHistory:
                Intent intentPurchaseHistory = new Intent(RewardsActivity.this,
                        MyOrderHistoryActivity.class);
                intentPurchaseHistory.setAction("PurchaseHistory");
                startActivity(intentPurchaseHistory);
                break;
            case R.id.learnMoreOrManageBtn:
                Intent manageAccountIntent = new Intent(RewardsActivity.this,
                        WebViewActivity.class);
                manageAccountIntent.putExtra("navigateToWebView",
                        WebserviceConstants.FROM_ULTAMATE_CARD);
                manageAccountIntent.putExtra("title", "Manage Account");
                if (isUserHavingUltamateCard) {
                    if (Utility.retrieveFromSharedPreference(
                            UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                            RewardsActivity.this).trim().equalsIgnoreCase("Ultamate Rewards Credit Card")) {
                        manageAccountIntent.putExtra("url", UltaDataCache.getDataCacheInstance().getAppConfig().getManageAccountPLCC());
                    } else  if (Utility.retrieveFromSharedPreference(
                            UltaConstants.REWARD_MEMBER, UltaConstants.ULTAMATE_CARD_TYPE,
                            RewardsActivity.this).trim().equalsIgnoreCase("Ultamate Rewards MasterCard")){
                        //Ultamate Rewards MasterCard
                        manageAccountIntent.putExtra("url", UltaDataCache.getDataCacheInstance().getAppConfig().getManageAccountCBCC());
                    }
                } else {
                    manageAccountIntent=new Intent(RewardsActivity.this,
                            UltaMateCreditCardActivity.class);
                }
                startActivity(manageAccountIntent);
                break;

            default:
                break;

        }
    }

    // My Profile Webservice Part

    /**
     * Invoke my profile details.
     */
    private void invokeMyProfileDetails() {
        InvokerParams<ProfileBean> invokerParams = new InvokerParams<ProfileBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.MY_PROFILE_DETAILS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
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
                    mInvokeMyProfileDetails = true;
                    askRelogin(RewardsActivity.this);
                    if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                        refreshPage();
                    }

                } else {
                    loadingDialog.setVisibility(View.GONE);
                    main_layout.setVisibility(View.VISIBLE);
                    try {
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                RewardsActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                loadingDialog.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                Logger.Log("<RetrieveMyProfileDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                profileBean = (ProfileBean) getResponseBean();
                if (null != profileBean) {
                    Logger.Log("<MyProfileActivity>" + "BeanPopulated");
                    beautyClubMember = profileBean.getBeautyClubNumber();
                    if (null != beautyClubMember) {
                        if (null != profileBean.getBalancePoints()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardsBalancePoints(
                                            profileBean.getBalancePoints());
                        }
                        if (null != profileBean.getBeautyClubNumber()) {
                            UltaDataCache
                                    .getDataCacheInstance()
                                    .setRewardsBeautyClubNumber(
                                            profileBean.getBeautyClubNumber());
                        }
                        Utility.saveToSharedPreference(
                                UltaConstants.REWARD_MEMBER,
                                UltaConstants.BEAUTY_CLUB_NUMBER, beautyClubMember,
                                getApplicationContext());
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(
                                metrics);
                        setUltamateCardDetails();
                        screenWidth = metrics.widthPixels;

                        Bitmap bitmap = null;
                        try {
                            bitmap = encodeAsBitmap(beautyClubMember,
                                    BarcodeFormat.EAN_13, screenWidth, 170);
                            // Setting the QR code generated
                            imgBarCode.setImageBitmap(bitmap);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        tvmemberId.setText(beautyClubMember);
                        txtMemberId.setText("MEMBER ID# " + beautyClubMember);
                    } else {
                        llmemberId.setVisibility(View.GONE);
                    }

                }
                trackEvarsUsingActionName(RewardsActivity.this,
                        WebserviceConstants.CHECKOUT_STEP_6_EVENT_ACTION,
                        WebserviceConstants.LOYALTY_CODE_KEY, beautyClubMember);

                trackAppAction(RewardsActivity.this,
                        WebserviceConstants.CHECKOUT_STEP_6_VISIT_EVENT_ACTION);
            }
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

    /*
     * This method encodes the data as bitmap which takes string to be encoded,
     * format for encryption,width and height as parameters
     */
    private Bitmap encodeAsBitmap(String contentsToEncode,
                                  BarcodeFormat format, int img_width, int img_height)
            throws WriterException {

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contentsToEncode, format, img_width,
                    img_height);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        if (result != null) {
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    // Below line of code generates black and white code
                    pixels[offset + x] = result.get(x, y) ? 0xFF000000
                            : 0xFFFFFFFF;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } else
            return null;
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {
        if (isSuccess) {
            if (mInvokeRewards) {
                invokeRewards();
            } else if (mInvokeMyProfileDetails) {
                invokeMyProfileDetails();
            }
        }
    }

}
