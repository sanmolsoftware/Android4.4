/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;
/*
import android.content.Context;*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.orderstatus.tab.SlidingTabLayout;
import com.ulta.core.bean.checkout.CheckoutCartBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.fragments.account.OrderStatusAddressFragment;
import com.ulta.core.fragments.account.OrderStatusAmountFragment;
import com.ulta.core.fragments.checkout.OrderDetailsFragment;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.InfoPopup.OnEditListener;

/*import android.view.LayoutInflater;*/
/*import android.view.ViewGroup;
import android.widget.BaseAdapter;*/
/*import com.ulta.core.util.caching.UltaDataCache;*/

public class MyOrderDetailsActivity extends UltaBaseActivity implements
		OnClickListener, OnEditListener {

	/** The amount tab. */
	LinearLayout amountTab;

	/** The address tab. */
	LinearLayout addressTab;

	/*	*//** The gift samples tab. */
	/*
	 * LinearLayout giftSamplesTab;
	 */

	/** The amount list fragment. */
	OrderDetailsFragment amountListFragment;

	/** The amount layout. */
	LinearLayout amountLayout;

	/** The address layout. */
	LinearLayout addressLayout;
	LinearLayout addressLayoutforAmount;

	/** The gift samples layout. */
	LinearLayout giftSamplesLayout;

	/** The shipping address layout. */
	LinearLayout shippingAddressLayout;

	/** The billing address layout. */
	LinearLayout billingAddressLayout;

	/** The credit card layout. */
	LinearLayout creditCardLayout;

	/** The gifts grid. */
	GridView giftsGrid;

	/** The free sample name1. */
	TextView freeSampleName1;

	/** The free sample name2. */
	TextView freeSampleName2;

	/** The free sample name3. */
	TextView freeSampleName3;

	/** The free sample brand name1. */
	TextView freeSampleBrandName1;

	/** The free sample brand name2. */
	TextView freeSampleBrandName2;

	/** The free sample brand name3. */
	TextView freeSampleBrandName3;

	TextView amountTV, addressTV;

	/** The free sample img1. */
	ImageView freeSampleImg1;

	/** The free sample img2. */
	ImageView freeSampleImg2;

	/** The free sample img3. */
	ImageView freeSampleImg3;

	/** The free sample1 layout. */
	LinearLayout freeSample1Layout;

	/** The free sample2 layout. */
	LinearLayout freeSample2Layout;

	/** The free sample3 layout. */
	LinearLayout freeSample3Layout;

	/** The amount. */
	Bundle amount = new Bundle();

	/** The str shipping postal code. */
	String strShippingFirstName = "", strShippingLastName = "",
			strShippingAddressLine1 = "", strShippingState = "",
			strShippingCity = "", strShippingPostalCode = "";

	/** The str billing postal code. */
	String strBillingFirstName = "", strBillingLastName = "",
			strBillingAddressLine1 = "", strBillingState = "",
			strBillingCity = "", strBillingPostalCode = "";

	/** The str card expiry year. */
	String strCardType = "", strCardNumber = "", strCardExpiryMonth = "",
			strCardExpiryYear = "", strCreditCardType = "";

	/** The total. */
	String noOfItems = "", rawSubTotal = "", shippingCharge = "", tax = "",
			total = "";

	/** The text card details line2. */
	TextView textShippingAddressLine1, textShippingAddressLine2,
			textBillingAddressLine1, textBillingAddressLine2,
			textCardDetailsLine1, textCardDetailsLine2;

	/** The checkout shippment method bean. */
	CheckoutShippmentMethodBean checkoutShippmentMethodBean;

	/** The review order bean. */
	CheckoutCartBean reviewOrderBean;

	/** The free gifts count. */
	int freeGiftsCount = 2;

	/** The free samples count. */
	int freeSamplesCount = 2;

	private ViewPager viewPager;
	private SlidingTabLayout tabLayout;

	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_my_order_details);
		setTitle("Order Details");
		/* initViews(); */
		// displayContinueButton();
		if (null != getIntent().getExtras().get("order")) {
			reviewOrderBean = (CheckoutCartBean) getIntent().getExtras().get(
					"order");
		} else {
			Logger.Log("PROBLEM");
		}
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
		tabLayout.setDistributeEvenly(true);
		tabLayout.setCustomTabView(R.layout.custom_tab_view, R.id.tabtext);
		tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

			@Override
			public int getIndicatorColor(int position) {
						return getResources().getColor(R.color.accentColor);
			}
		});
		tabLayout.setViewPager(viewPager);

		/*
		 * setAddress(); setAmount(); setAmountAsCurrent();
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		/*switch (v.getId()) {
		case R.id.reviewOrderAmountTab:
			setAmountAsCurrent();
			break;
		case R.id.reviewOrderAddressTab:
			setAddressAsCurrent();
			break;
		case R.id.reviewOrderGiftSamplesTab:
			setGiftAsCurrent();
			break;
		default:
			break;
		}
*/
	}

	/**
	 * Inits the views.
	 */
	/*private void initViews() {
		amountTab = (LinearLayout) findViewById(R.id.reviewOrderAmountTab);
		addressTab = (LinearLayout) findViewById(R.id.reviewOrderAddressTab);
		
		 * giftSamplesTab = (LinearLayout)
		 * findViewById(R.id.reviewOrderGiftSamplesTab);
		 
		amountTab.setOnClickListener(this);
		addressTab.setOnClickListener(this);
		 giftSamplesTab.setOnClickListener(this); 

		amountListFragment = (OrderDetailsFragment) getSupportFragmentManager()
				.findFragmentById(R.id.orderDetailsListFragment);
		amountLayout = (LinearLayout) findViewById(R.id.reviewOrderAmountLayout);
		addressLayout = (LinearLayout) findViewById(R.id.reviewOrderAddressLayout_fragment);
		addressLayoutforAmount = (LinearLayout) findViewById(R.id.reviewOrderAddressLayout);
		giftSamplesLayout = (LinearLayout) findViewById(R.id.reviewOrderGiftSamplesLayout);
		amountTV = (TextView) findViewById(R.id.amountTV);
		addressTV = (TextView) findViewById(R.id.addressTV);

		giftsGrid = (GridView) findViewById(R.id.reviewOrderFreeGifts);

		freeSampleImg1 = (ImageView) findViewById(R.id.reviewOrderGiftImg1);
		freeSampleImg2 = (ImageView) findViewById(R.id.reviewOrderGiftImg2);
		freeSampleImg3 = (ImageView) findViewById(R.id.reviewOrderGiftImg3);

		freeSampleBrandName1 = (TextView) findViewById(R.id.reviewOrderGiftBrandName1);
		freeSampleBrandName2 = (TextView) findViewById(R.id.reviewOrderGiftBrandName2);
		freeSampleBrandName3 = (TextView) findViewById(R.id.reviewOrderGiftBrandName3);

		freeSampleName1 = (TextView) findViewById(R.id.reviewOrderGiftName1);
		freeSampleName2 = (TextView) findViewById(R.id.reviewOrderGiftName2);
		freeSampleName3 = (TextView) findViewById(R.id.reviewOrderGiftName3);

		freeSample1Layout = (LinearLayout) findViewById(R.id.reviewOrderFreeSample1);
		freeSample2Layout = (LinearLayout) findViewById(R.id.reviewOrderFreeSample2);
		freeSample3Layout = (LinearLayout) findViewById(R.id.reviewOrderFreeSample3);

		shippingAddressLayout = (LinearLayout) findViewById(R.id.reviewOrderShippningAddressLayout_fragment);
		billingAddressLayout = (LinearLayout) findViewById(R.id.reviewOrderBillingAddressLayout_fragment);
		creditCardLayout = (LinearLayout) findViewById(R.id.reviewOrderCreditCardLayout_fragment);
		shippingAddressLayout.setOnClickListener(this);
		billingAddressLayout.setOnClickListener(this);
		creditCardLayout.setOnClickListener(this);

		textShippingAddressLine1 = (TextView) findViewById(R.id.txtShippingAddress1_fragment);
		textShippingAddressLine2 = (TextView) findViewById(R.id.txtShippingAddress2_fragment);
		textBillingAddressLine1 = (TextView) findViewById(R.id.txtBillingAddress1_fragment);
		textBillingAddressLine2 = (TextView) findViewById(R.id.txtBillingAddress2_fragment);
		textCardDetailsLine1 = (TextView) findViewById(R.id.txtCardDetails1_fragment);
		textCardDetailsLine2 = (TextView) findViewById(R.id.txtCardDetails2_fragment);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
		tabLayout.setViewPager(viewPager);
	}*/

	/**
	 * Sets the gift.
	 */
	/*
	 * private void setGift() { if (freeGiftsCount > 0) {
	 * giftsGrid.setAdapter(new GiftsAdapter());
	 * 
	 * }
	 * 
	 * if (freeSamplesCount == 0) { freeSample1Layout.setVisibility(View.GONE);
	 * freeSample2Layout.setVisibility(View.GONE);
	 * freeSample3Layout.setVisibility(View.GONE); } else if (freeSamplesCount
	 * == 1) { populateGiftOne(null, "FreeSample1", "brand1");
	 * freeSample2Layout.setVisibility(View.GONE);
	 * freeSample3Layout.setVisibility(View.GONE); } else if (freeSamplesCount
	 * == 2) { populateGiftOne(null, "FreeSample1", "brand1");
	 * populateGiftTwo(null, "FreeSample2", "brand2");
	 * freeSample3Layout.setVisibility(View.GONE); } else if (freeGiftsCount ==
	 * 3) { populateGiftOne(null, "FreeSample1", "brand1");
	 * populateGiftTwo(null, "FreeSample2", "brand2"); populateGiftThree(null,
	 * "FreeSample3", "brand3"); }
	 * 
	 * }
	 */

	/**
	 * Populate gift one.
	 * 
	 * @param imageUrl
	 *            the image url
	 * @param name
	 *            the name
	 * @param brand
	 *            the brand
	 */
	/*
	 * private void populateGiftOne(String imageUrl, String name, String brand)
	 * { freeSampleBrandName1.setText(brand); freeSampleName1.setText(name); }
	 */

	/**
	 * Populate gift two.
	 * 
	 * @param imageUrl
	 *            the image url
	 * @param name
	 *            the name
	 * @param brand
	 *            the brand
	 */
	/*
	 * private void populateGiftTwo(String imageUrl, String name, String brand)
	 * { freeSampleBrandName2.setText(brand); freeSampleName2.setText(name); }
	 */

	/**
	 * Populate gift three.
	 * 
	 * @param imageUrl
	 *            the image url
	 * @param name
	 *            the name
	 * @param brand
	 *            the brand
	 */
	/*
	 * private void populateGiftThree(String imageUrl, String name, String
	 * brand) { freeSampleBrandName3.setText(brand);
	 * freeSampleName3.setText(name); }
	 */

	/**
	 * Sets the address.
	 */
/*	private void setAddress() {
		if (UltaDataCache.getDataCacheInstance().isOnlyEgiftCard()) {
			shippingAddressLayout.setVisibility(View.GONE);
		}
		if (null != reviewOrderBean.getHardGoodShippingGroups()
				&& !reviewOrderBean.getHardGoodShippingGroups().isEmpty()) {
			if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
					.getFirstName())
				strShippingFirstName = reviewOrderBean
						.getHardGoodShippingGroups().get(0).getFirstName();
			if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
					.getLastName())
				strShippingLastName = reviewOrderBean
						.getHardGoodShippingGroups().get(0).getLastName();
			if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
					.getAddress1())
				strShippingAddressLine1 = reviewOrderBean
						.getHardGoodShippingGroups().get(0).getAddress1();
			if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
					.getState())
				strShippingState = reviewOrderBean.getHardGoodShippingGroups()
						.get(0).getState();
			if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
					.getCity())
				strShippingCity = reviewOrderBean.getHardGoodShippingGroups()
						.get(0).getCity();
			if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
					.getPostalCode())
				strShippingPostalCode = reviewOrderBean
						.getHardGoodShippingGroups().get(0).getPostalCode();

			textShippingAddressLine1.setText(strShippingFirstName + " "
					+ strShippingLastName);
			textShippingAddressLine2.setText(strShippingAddressLine1 + " "
					+ strShippingCity + " " + strShippingState + " "
					+ strShippingPostalCode);
		}
		if (null != reviewOrderBean.getCreditCardPaymentGroups()
				&& !(reviewOrderBean.getCreditCardPaymentGroups().isEmpty())) {
			billingAddressLayout.setVisibility(View.VISIBLE);
			strBillingFirstName = reviewOrderBean.getCreditCardPaymentGroups()
					.get(0).getFirstName();
			if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
					.getLastName())
				strBillingLastName = reviewOrderBean
						.getCreditCardPaymentGroups().get(0).getLastName();
			if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
					.getAddress1())
				strBillingAddressLine1 = reviewOrderBean
						.getCreditCardPaymentGroups().get(0).getAddress1();
			if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
					.getCity())
				strBillingCity = reviewOrderBean.getCreditCardPaymentGroups()
						.get(0).getCity();
			if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
					.getState())
				strBillingState = reviewOrderBean.getCreditCardPaymentGroups()
						.get(0).getState();
			if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
					.getPostalCode())
				strBillingPostalCode = reviewOrderBean
						.getCreditCardPaymentGroups().get(0).getPostalCode();
			textBillingAddressLine1.setText(strBillingFirstName + " "
					+ strBillingLastName);
			textBillingAddressLine2.setText(strBillingAddressLine1 + " "
					+ strBillingCity + " " + strBillingState + " "
					+ strBillingPostalCode);
			if (null != reviewOrderBean.getGiftCardPaymentGroups()
					&& !reviewOrderBean.getGiftCardPaymentGroups().isEmpty()) {
				strCardType = reviewOrderBean.getGiftCardPaymentGroups().get(0)
						.getPaymentMethod();
				strCardNumber = reviewOrderBean.getGiftCardPaymentGroups()
						.get(0).getGiftCardNumber();

				strCardType = getCardtype(strCardType);

				textCardDetailsLine1.setText(strCardType
						+ " "
						+ strCardNumber
						+ "\n"
						+ "Amount : "
						+ "$"
						+ String.format(
								"%.2f",
								Double.valueOf(reviewOrderBean
										.getGiftCardPaymentGroups().get(0)
										.getAmount())));
				// This method will get the new values to variables from credit
				// card details
				getCreditCardInfo();
				textCardDetailsLine2.setText(strCardType
						+ " "
						+ strCreditCardType
						+ " : "
						+ strCardNumber
						+ "\n"
						+ "Amount : "
						+ "$"
						+ String.format(
								"%.2f",
								Double.valueOf(reviewOrderBean
										.getCreditCardPaymentGroups().get(0)
										.getAmount())) + "\n" + "Expiry : "
						+ strCardExpiryMonth + "/" + strCardExpiryYear);
			} else {
				// This method will get the new values to variables from credit
				// card details
				getCreditCardInfo();
				textCardDetailsLine1.setText(strCardType
						+ " "
						+ strCreditCardType
						+ " : "
						+ strCardNumber
						+ "\n"
						+ "Amount : "
						+ "$"
						+ String.format(
								"%.2f",
								Double.valueOf(reviewOrderBean
										.getCreditCardPaymentGroups().get(0)
										.getAmount())));
				textCardDetailsLine2.setText("Expiry : " + strCardExpiryMonth
						+ "/" + strCardExpiryYear);
			}
		} else if (reviewOrderBean.getCreditCardPaymentGroups() != null
				&& reviewOrderBean.getCreditCardPaymentGroups().isEmpty()
				&& null != reviewOrderBean.getGiftCardPaymentGroups()
				&& !(reviewOrderBean.getGiftCardPaymentGroups().isEmpty())) {
			billingAddressLayout.setVisibility(View.GONE);
			strCardType = reviewOrderBean.getGiftCardPaymentGroups().get(0)
					.getPaymentMethod();

			strCardType = getCardtype(strCardType);

			strCardNumber = reviewOrderBean.getGiftCardPaymentGroups().get(0)
					.getGiftCardNumber();
			textCardDetailsLine1.setText(strCardType
					+ " "
					+ strCardNumber
					+ "\n"
					+ "Amount : "
					+ "$"
					+ String.format(
							"%.2f",
							Double.valueOf(reviewOrderBean
									.getGiftCardPaymentGroups().get(0)
									.getAmount())));
			textCardDetailsLine2.setVisibility(View.GONE);
		} else if (reviewOrderBean.getPaypalPaymentGroups() != null
				&& !reviewOrderBean.getPaypalPaymentGroups().isEmpty()) {
			// paypalImage.setVisibility(View.VISIBLE);
			billingAddressLayout.setVisibility(View.GONE);
			textCardDetailsLine1.setText("PayPal Account");
			if (reviewOrderBean.getPaypalPaymentGroups().get(0).getEmailId() != null
					&& !reviewOrderBean.getPaypalPaymentGroups().get(0)
							.getEmailId().isEmpty()) {
				textCardDetailsLine2.setText(reviewOrderBean
						.getPaypalPaymentGroups().get(0).getEmailId());
			}
		}
	}*/

	/*private void getCreditCardInfo() {
		strCardType = reviewOrderBean.getCreditCardPaymentGroups().get(0)
				.getPaymentMethod();

		strCardType = getCardtype(strCardType);

		if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
				.getCreditCardNumber()) {
			String numberOfCross = "";
			strCardNumber = reviewOrderBean.getCreditCardPaymentGroups().get(0)
					.getCreditCardNumber();
			for (int i = 0; i < strCardNumber.length() - 4; i++) {
				numberOfCross = numberOfCross + "x";
			}
			strCardNumber = numberOfCross
					+ strCardNumber.substring(strCardNumber.length() - 4,
							strCardNumber.length());
		}
		if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
				.getExpirationMonth())
			strCardExpiryMonth = reviewOrderBean.getCreditCardPaymentGroups()
					.get(0).getExpirationMonth();
		if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
				.getExpirationYear())
			strCardExpiryYear = reviewOrderBean.getCreditCardPaymentGroups()
					.get(0).getExpirationYear();
		if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
				.getCreditCardType()) {
			strCreditCardType = reviewOrderBean.getCreditCardPaymentGroups()
					.get(0).getCreditCardType();
		}
	}*/

	/**
	 * Sets the amount.
	 */
/*	private void setAmount() {
		if (null != reviewOrderBean) {

			OrderDetailsFragment reviewOrderListFragment = (OrderDetailsFragment) getSupportFragmentManager()
					.findFragmentById(R.id.orderDetailsListFragment);

			reviewOrderListFragment.populateListData(reviewOrderBean);
			amount.putString("noOfItems", noOfItems);
			amount.putString("rawSubTotal", rawSubTotal);
			amount.putString("shippingCharge", shippingCharge);
			amount.putString("tax", tax);
			amount.putString("total", total);
			reviewOrderListFragment.setListFooterData(reviewOrderBean);
		}
	}*/

/*	*//**
	 * Sets the amount as current.
	 *//*
	private void setAmountAsCurrent() {
		addressLayout.setVisibility(View.INVISIBLE);
		giftSamplesLayout.setVisibility(View.INVISIBLE);
		amountLayout.setVisibility(View.VISIBLE);
		shippingAddressLayout.setVisibility(View.INVISIBLE);
		addressLayoutforAmount.setVisibility(View.INVISIBLE);
		amountTab.setBackgroundResource(R.drawable.top_button_selected);
		addressTab.setBackgroundResource(R.drawable.top_button_unselected);
		 giftSamplesTab.setBackgroundResource(R.drawable.tab3); 
		amountTV.setTextColor(getResources().getColor(R.color.accentColor));
		addressTV.setTextColor(getResources().getColor(R.color.black));
	}*/

	/**
	 * Sets the address as current.
	 */
	/*private void setAddressAsCurrent() {
		giftSamplesLayout.setVisibility(View.INVISIBLE);
		amountLayout.setVisibility(View.GONE);
		addressLayout.setVisibility(View.VISIBLE);
		shippingAddressLayout.setVisibility(View.VISIBLE);
		amountTab.setBackgroundResource(R.drawable.top_button_unselected);
		addressTab.setBackgroundResource(R.drawable.top_button_selected);
		 giftSamplesTab.setBackgroundResource(R.drawable.tab3); 
		addressTV.setTextColor(getResources().getColor(R.color.accentColor));
		amountTV.setTextColor(getResources().getColor(R.color.black));
	}*/

	/**
	 * Sets the gift as current.
	 */
/*	private void setGiftAsCurrent() {
		giftSamplesLayout.setVisibility(View.VISIBLE);
		amountLayout.setVisibility(View.INVISIBLE);
		addressLayout.setVisibility(View.INVISIBLE);
		amountTab.setBackgroundResource(R.drawable.tab1);
		addressTab.setBackgroundResource(R.drawable.tab2);
		 giftSamplesTab.setBackgroundResource(R.drawable.tab3_selected); 
	}*/

	/**
	 * The Class GiftsAdapter.
	 */
/*	class GiftsAdapter extends BaseAdapter {

		
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 
		@Override
		public int getCount() {

			return 7;
		}

		
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 
		@Override
		public Object getItem(int position) {
				return null;
		}

		
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 
		@Override
		public long getItemId(int position) {
				return 0;
		}

		
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) MyOrderDetailsActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(
					R.layout.product_list_inflate_image_in_grid_view, null);

			return view;
		}

	}*/

	@Override
	public void onEditClicked() {
		finish();
	}

/*	private String getCardtype(String strCardTypeValue) {
		if (strCardTypeValue.equalsIgnoreCase("creditcard"))
			strCardTypeValue = "Credit Card";
		return strCardTypeValue;
	}*/

	class MyPagerAdapter extends FragmentPagerAdapter {
		String[] tabs = { "AMOUNT", "ADDRESS & BILLING" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
				return tabs[position];
		}

		@Override
		public Fragment getItem(int arg0) {
				if (arg0 == 0)
				return new OrderStatusAmountFragment(reviewOrderBean);
			if (arg0 == 1)
				return new OrderStatusAddressFragment(reviewOrderBean,MyOrderDetailsActivity.this);
			return null;
		}

		@Override
		public int getCount() {
				return 2;
		}

	}
}
