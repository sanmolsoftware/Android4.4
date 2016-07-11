/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.product;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashstar.ui.activity.AddCardActivity;
import com.cashstar.ui.activity.CashStarHomeActivity;
import com.cashstar.ui.activity.DesignAmountActivity;
import com.cashstar.ui.activity.plastic.PlasticAmountActivity;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.WebViewActivity;
import com.ulta.core.activity.rewards.GiftCardsTabActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;


/**
 * The Class CashStarHomeUI.
 *
 * @author Infosys
 */

public class CashStarHomeUI extends UltaBaseActivity implements
        OnClickListener {


    private Button mCreateE_GiftCard;
    private Button mCreatePlastic_Giftcard;
    private ImageView mGiftCardBanner;
    private TextView mCheckGiftCardBalanceTV, mCorporateGiftCardTV, mFrequentlyAskedQuestionsTV, mTermsAndConditionsTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashstar_home);
        setTitle(getResources().getString(R.string.cashstar_title));
        init();
    }

    private void init() {
        //Banner image
        mGiftCardBanner = (ImageView) findViewById(R.id.giftCardBanner);
        setBanner();

        mCreateE_GiftCard = (Button) findViewById(R.id.btnEGiftCard);
        mCreatePlastic_Giftcard = (Button) findViewById(R.id.btnPlasticGiftCard);
        mCheckGiftCardBalanceTV = (TextView) findViewById(R.id.checkGiftCardBalanceTV);
        mCorporateGiftCardTV = (TextView) findViewById(R.id.corporateGiftCardTV);
        mFrequentlyAskedQuestionsTV = (TextView) findViewById(R.id.frequentlyAskedQuestionsTV);
        mTermsAndConditionsTV = (TextView) findViewById(R.id.termsAndConditionsTV);

        //listeners
        mCreateE_GiftCard.setOnClickListener(this);
        mCreatePlastic_Giftcard.setOnClickListener(this);
        mCheckGiftCardBalanceTV.setOnClickListener(this);
        mCorporateGiftCardTV.setOnClickListener(this);
        mFrequentlyAskedQuestionsTV.setOnClickListener(this);
        mTermsAndConditionsTV.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /*
       Method to invoke cashstar SDK E-Gift card and Plastic Gift card native page
     */
            case R.id.btnEGiftCard:

                Intent intentEGiftcard = new Intent(this, DesignAmountActivity.class);
                if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {

				/*intentEGiftcard.putExtra("cstar_first_name","",""));
                intentEGiftcard.putExtra("cstar_last_name","",""));
				intentEGiftcard.putExtra("cstar_sender_email",UltaDataCache.getDataCacheInstance()
				.getLoginName(),""));
				intentEGiftcard.putExtra("cstar_sender_phone","",""));
				intentEGiftcard.putExtra("cstar_sender_zip","",""));*/
                }

                startActivity(intentEGiftcard);
                break;

            case R.id.btnPlasticGiftCard:
                Intent intentPlasticGiftCard = new Intent(this, PlasticAmountActivity.class);
                startActivity(intentPlasticGiftCard);
                break;
            case R.id.checkGiftCardBalanceTV:
                Intent intentForGiftCard = new Intent(this,
                        CashStarHomeActivity.class);
                startActivity(intentForGiftCard);
                break;
            case R.id.corporateGiftCardTV:
                showCorporateGiftCardAlert();
                break;
            case R.id.frequentlyAskedQuestionsTV:
                faqTxtViewClick();
                break;
            case R.id.termsAndConditionsTV:
                termAndCondtionTxtViewClick();
                break;
            default:
                break;
        }
    }

    private void showCorporateGiftCardAlert() {
       final Dialog corporateGiftCardAlert=showAlertDialog(CashStarHomeUI.this, WebserviceConstants.CORPORATE_GIFTCARD_ALERT_TITLE,
               WebserviceConstants.CORPORATE_GIFTCARD_ALERT_MESSAGE, "OK", "CANCEL");
        mAgreeButton = (Button) corporateGiftCardAlert.findViewById(R.id.btnAgree);
        mDisagreeButton = (Button) corporateGiftCardAlert.findViewById(R.id.btnDisagree);
        mDisagreeButton.setVisibility(View.GONE);
        messageTV = (TextView) corporateGiftCardAlert.findViewById(R.id.message);
        messageTV.setTextSize(15);
        mAgreeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                corporateGiftCardAlert.dismiss();
            }
        });
        corporateGiftCardAlert.show();

    }

    private void faqTxtViewClick() {
        Intent faqIntent = new Intent(CashStarHomeUI.this,
                WebViewActivity.class);
        faqIntent.putExtra("navigateToWebView",
                WebserviceConstants.FROM_ULTAMATE_CARD);
        faqIntent.putExtra("title", "FAQs");
        faqIntent.putExtra("url", WebserviceConstants.GIFTCARD_FAQ_URL);
        startActivity(faqIntent);
    }

    private void termAndCondtionTxtViewClick() {
        Intent termIntent = new Intent(CashStarHomeUI.this,
                WebViewActivity.class);
        termIntent.putExtra("navigateToWebView",
                WebserviceConstants.FROM_ULTAMATE_CARD);
        termIntent.putExtra("title", "Terms & Conditions");
        termIntent.putExtra("url", WebserviceConstants.GIFTCARD_TERM_CONDITION_URL);
        startActivity(termIntent);
    }

    private void setBanner() {
        View header = getLayoutInflater()
                .inflate(R.layout.listviewheader, null);
        header.setPadding(0, 0, 0, 0);
        if (haveInternet()) {
            checkDensityAndSetImage(mGiftCardBanner,
                    WebserviceConstants.CASH_STAR_BANNER, R.drawable.app_giftcardbanner_fallback,
                    "GiftCard", null, false);
        } else {
            mGiftCardBanner.setImageResource(R.drawable.app_giftcardbanner_fallback);
        }
    }

}
