package com.ulta.core.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;

public class SpecialOffersActivity extends UltaBaseActivity implements
		OnClickListener {

	private LinearLayout mBuyMoreSaveMoreLayout;
	private LinearLayout mGiftsWithPurchaseLayout;
	private LinearLayout mOnSaleLinearLayout;
	private ImageView mSpecialOfferBannerImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_list);
		setTitle(getResources().getString(R.string.onsale));
		mBuyMoreSaveMoreLayout = (LinearLayout) findViewById(R.id.buyMoreSaveMoreLayout);
		mGiftsWithPurchaseLayout = (LinearLayout) findViewById(R.id.giftWithPurchaseLayout);
		mOnSaleLinearLayout = (LinearLayout) findViewById(R.id.onSaleLinearLayout);
		mSpecialOfferBannerImageView = (ImageView) findViewById(R.id.specialOfferBanner);

		mBuyMoreSaveMoreLayout.setOnClickListener(this);
		mGiftsWithPurchaseLayout.setOnClickListener(this);
		mOnSaleLinearLayout.setOnClickListener(this);

		setBanner();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buyMoreSaveMoreLayout:
			Intent buyMoreSaveMoreIntent = new Intent(
					SpecialOffersActivity.this,
					BuyMoreSaveMoreLandingActivity.class);
			startActivity(buyMoreSaveMoreIntent);
			break;

		case R.id.giftWithPurchaseLayout:
			Intent gwpIntent = new Intent(SpecialOffersActivity.this,
					GWPLandingActivity.class);
			startActivity(gwpIntent);
			break;

		case R.id.onSaleLinearLayout:
			Intent onSaleIntent = new Intent(SpecialOffersActivity.this,
					UltaProductListActivity.class);
			onSaleIntent.putExtra("id", "onSaleFormHomePage");
			onSaleIntent.putExtra("altText", "Sale");
			startActivity(onSaleIntent);
			break;

		default:
			break;
		}
	}

	private void setBanner() {
		View header = getLayoutInflater()
				.inflate(R.layout.listviewheader, null);
		header.setPadding(0, 0, 0, 0);
		if (haveInternet()) {
			checkDensityAndSetImage(mSpecialOfferBannerImageView,
					WebserviceConstants.SALE_BANNER, R.drawable.app_shop,
					"SpecialOffers",null,false);
		} else {
			mSpecialOfferBannerImageView.setImageResource(R.drawable.app_shop);
		}
	}

}
