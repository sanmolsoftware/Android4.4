/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.checkout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.ViewItemsInBasketActivity;
import com.ulta.core.bean.checkout.CheckoutLoyaltyPointsPaymentGroupBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.checkout.ReviewOrderBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.fragments.checkout.ReviewOrderListFragment;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.InfoPopup.OnEditListener;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class ReviewOrderActivity.
 */
public class ReviewOrderActivity extends UltaBaseActivity implements
		OnDoneClickedListener, OnClickListener, OnEditListener,
		OnSessionTimeOut {

	/** The gift samples tab. */

	/** The amount list fragment. */
	ReviewOrderListFragment amountListFragment;

	/** The amount layout. */
	LinearLayout amountLayout;

	/** The address layout. */
	LinearLayout addressLayout;

	/** The free sample name1. */

	/** The amount. */
	Bundle amount = new Bundle();

	/** The total. */
	String noOfItems = "", rawSubTotal = "", shippingCharge = "", tax = "",
			total = "";

	/** Strings for paypal payment verifications */
	String appID = null;
	String payKey = null;
	String paymentId = null;

	// ImageView paypalImage;

	/** The checkout shippment method bean. */
	CheckoutShippmentMethodBean checkoutShippmentMethodBean;

	/** The review order bean. */
	ReviewOrderBean reviewOrderBean;
	// UemAction commitOrderAction;
	/** The free gifts count. */
	int freeGiftsCount = 2;

	/** The free samples count. */
	int freeSamplesCount = 2;

	private TextView mOrderTotal;
	private TextView mNumberOfItemstextView;

	private ImageButton mPaymentButton;
	private ImageButton mPlaceOrderButton;

	private TextView mCheckout_payment;
	private TextView mCheckout_shipping;
	private TextView mCheckoutReviewOrder;
	List<String> errors;

	@Override
	protected void onResume() {
		super.onResume();
		pd.dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_review_order);
		initViews();
		setTitle("Review Order");
		// 3.3
		// setting the header to indicate payment page
		mCheckout_payment = (TextView) findViewById(R.id.checkout_payment);
		mCheckout_shipping = (TextView) findViewById(R.id.checkout_shipping);
		mCheckoutReviewOrder = (TextView) findViewById(R.id.checkout_review_order);
		Drawable drawable = getResources().getDrawable(
				R.drawable.tick_completed);
		mCheckout_shipping.setCompoundDrawablesWithIntrinsicBounds(drawable,
				null, null, null);
		mCheckout_payment.setCompoundDrawablesWithIntrinsicBounds(drawable,
				null, null, null);
		mCheckoutReviewOrder.setBackgroundColor(getResources().getColor(
				R.color.chekout_header_highlighted));
		mCheckout_payment.setBackgroundColor(getResources().getColor(
				R.color.olapic_detail_caption));
		mCheckout_shipping.setBackgroundColor(getResources().getColor(
				R.color.olapic_detail_caption));
		if (getIntent().getExtras() != null) {
			reviewOrderBean = (ReviewOrderBean) getIntent().getExtras().get(
					"order");
			errors = reviewOrderBean.getErrorInfos();
			amountListFragment.setListFooterData(reviewOrderBean);
			setAmount();
			// setAddress();
			// createProductsForOmniture();
			if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
				trackAppState(this, WebserviceConstants.REVIEW_ORDER_GUEST_PAGE);
			} else if (null != UltaDataCache.getDataCacheInstance()
					.getRewardsBalancePoints()) {
				trackAppState(this,
						WebserviceConstants.REVIEW_ORDER_LOYALITY_PAGE);
			} else {
				trackAppState(this,
						WebserviceConstants.REVIEW_ORDER_NON_LOYALITY_PAGE);
			}
			setAmountAsCurrent();
			setAddressAsCurrent();
		}

		// setAddressAsCurrent();

		pd = new ProgressDialog(ReviewOrderActivity.this);
		setProgressDialogLoadingColor(pd);
		if (UltaDataCache.getDataCacheInstance().isExpressCheckout()) {
			mPaymentButton.setVisibility(View.INVISIBLE);
		}
		if (null != errors && errors.size() > 0) {
			mPlaceOrderButton.setVisibility(View.GONE);
			notifyUser("" + errors.get(0), ReviewOrderActivity.this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.widgets.flyin.OnDoneClickedListener#onDoneClicked()
	 */
	double totalamount = 0.0;
	String currencyCode = "USD";

	@Override
	public void onDoneClicked() {

		pd.show();
		invokeCommitOrderService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reviewOrderGiftSamplesTab:
			// setGiftAsCurrent();
			break;

		default:
			break;
		}

	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		amountListFragment = (ReviewOrderListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.reviewOrderListFragment);
		amountLayout = (LinearLayout) findViewById(R.id.reviewOrderAmountLayout);
		addressLayout = (LinearLayout) findViewById(R.id.reviewOrderAddressLayout);

		mOrderTotal = (TextView) findViewById(R.id.reviewOrderTotalPrice);
		mPaymentButton = (ImageButton) findViewById(R.id.backButton);
		mPlaceOrderButton = (ImageButton) findViewById(R.id.placeOrderButton);
		mNumberOfItemstextView = (TextView) findViewById(R.id.reviewOrder_numberOfItems);

		mPaymentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * Intent paymentMethod = new Intent(ReviewOrderActivity.this,
				 * PaymentMethodActivity.class);
				 * paymentMethod.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				 * Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				 * startActivity(paymentMethod);
				 */
				finish();
			}
		});

		mPlaceOrderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pd.show();
				invokeCommitOrderService();

				if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
					trackAppState(
							ReviewOrderActivity.this,
							WebserviceConstants.CHECKOUT_ORDER_CONFIRMATION_GUEST_MEMBER);
				} else if (Utility
						.retrieveBooleanFromSharedPreference(
								UltaConstants.REWARD_MEMBER,
								UltaConstants.IS_REWARD_MEMBER,
								getApplicationContext())) {
					trackAppState(
							ReviewOrderActivity.this,
							WebserviceConstants.CHECKOUT_ORDER_CONFIRMATION_LOYALITY_MEMBER);
				} else {
					trackAppState(
							ReviewOrderActivity.this,
							WebserviceConstants.CHECKOUT_ORDER_CONFIRMATION_NON_LOYALITY_MEMBER);
				}
				trackAppAction(ReviewOrderActivity.this,
						WebserviceConstants.CHECKOUT_STEP_7_EVENT_ACTION);

				trackAppAction(ReviewOrderActivity.this,
						WebserviceConstants.CHECKOUT_STEP_7_VISIT_EVENT_ACTION);

			}
		});
	}

	/**
	 * Sets the amount.
	 */
	private void setAmount() {
		if (null != reviewOrderBean) {

			ReviewOrderListFragment reviewOrderListFragment = (ReviewOrderListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.reviewOrderListFragment);

			setOrderTotal();
			reviewOrderListFragment.populateListData(reviewOrderBean);
			amount.putString("noOfItems", noOfItems);
			amount.putString("rawSubTotal", rawSubTotal);
			amount.putString("shippingCharge", shippingCharge);
			amount.putString("tax", tax);
			amount.putString("total", total);
			reviewOrderListFragment.setListFooterData(reviewOrderBean);
		}
	}

	/**
	 * Sets the amount as current.
	 */
	private void setAmountAsCurrent() {
		addressLayout.setVisibility(View.VISIBLE);
		amountLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * Sets the address as current.
	 */
	private void setAddressAsCurrent() {
		amountLayout.setVisibility(View.VISIBLE);
		addressLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * Invoke commit order service.
	 */
	private void invokeCommitOrderService() {
		// commitOrderAction = UemAction
		// .enterAction(WebserviceConstants.ACTION_COMMITORDER_INVOCATION);
		// commitOrderAction.reportEvent("Submit button clicked");
		InvokerParams<CheckoutShippmentMethodBean> invokerParams = new InvokerParams<CheckoutShippmentMethodBean>();
		// if (UltaDataCache.getDataCacheInstance().isExpressCheckout()) {
		pd.setMessage(UltaConstants.COMMITTING_ORDER_TEXT);
		pd.setCancelable(false);
		pd.show();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.COMMIT_ORDER_SERVICE);
		/*
		 * } else { pd.setMessage(UltaConstants.VERIFYING_ADDRESS);
		 * pd.setCancelable(false); pd.show(); invokerParams
		 * .setServiceToInvoke(
		 * WebserviceConstants.VERIFY_ADDRESS_AND_COMMIT_ORDER_SERVICE); }
		 */
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateShippmentParameters());
		invokerParams.setUltaBeanClazz(CheckoutShippmentMethodBean.class);
		PaymentHandler userCreationHandler = new PaymentHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> populateShippmentParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();

		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");
		if (UltaDataCache.getDataCacheInstance()
				.isAnonymousCheckout()) {
			urlParams.put("guestUserEmailOptIn", "" + amountListFragment.isEmailOpted);
		}
		return urlParams;
	}

	/**
	 * The Class PaymentHandler.
	 */
	public class PaymentHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(ReviewOrderActivity.this);
					// commitOrderAction.reportError(
					// WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
					// commitOrderAction.leaveAction();
				} else {
					try {
						// commitOrderAction
						// .reportError(
						// getErrorMessage(),
						// WebserviceConstants.DYN_ERRCODE_REVIEW_ORDER_ACTIVITY);
						// commitOrderAction.leaveAction();
						notifyUser(getErrorMessage(), ReviewOrderActivity.this);
						pd.dismiss();
						setError(ReviewOrderActivity.this, getErrorMessage());
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				CheckoutShippmentMethodBean ultaBean = (CheckoutShippmentMethodBean) getResponseBean();
				List<String> errors = ultaBean.getErrorInfos();
				if (null != errors && !(errors.isEmpty())) {
					try {
						// commitOrderAction
						// .reportError(
						// errors.get(0),
						// WebserviceConstants.DYN_ERRCODE_REVIEW_ORDER_ACTIVITY);
						// commitOrderAction.leaveAction();
						notifyUser(errors.get(0), ReviewOrderActivity.this);
						pd.dismiss();
						setError(ReviewOrderActivity.this, errors.get(0));
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				} else {
					clearAOCookie();

					Intent goToOrderSummary = new Intent(
							ReviewOrderActivity.this,
							OrderSummaryActivity.class);
					goToOrderSummary.putExtra("revieworderBean",
							reviewOrderBean);
					goToOrderSummary.putExtra("revieworder", ultaBean);
					// UltaDataCache.getDataCacheInstance()
					// .setMovingBackInChekout(true);
					UltaDataCache.getDataCacheInstance().setGiftTheOrder(false);
					if (UltaDataCache.getDataCacheInstance().isPayPalPayment()) {
						UltaDataCache.getDataCacheInstance().setPayPalPayment(
								false);
					}
					if (UltaDataCache.getDataCacheInstance()
							.isAnonymousCheckout()) {
						goToOrderSummary.putExtra("user", "guest");
						UltaDataCache.getDataCacheInstance()
								.setAnonymousCheckout(false);
						UltaDataCache.getDataCacheInstance().setOrderSubmitted(
								true);
					}
					if (UltaDataCache.getDataCacheInstance()
							.isExpressCheckout()) {
						UltaDataCache.getDataCacheInstance()
								.setExpressCheckout(false);
					}
					UltaDataCache.getDataCacheInstance()
							.setMoveBackTo("Basket");
					setItemCountInBasket(0);
					setFavoritesCountInNavigationDrawer(0);
					startActivity(goToOrderSummary);
					// commitOrderAction.reportEvent("Order committed");
					// commitOrderAction.leaveAction();
					pd.dismiss();
					finish();
				}
			}
		}
	}

	public void setOrderTotal() {
		double amountAdjusted = 0;
		CheckoutLoyaltyPointsPaymentGroupBean checkOutLoyality;
		checkOutLoyality = new CheckoutLoyaltyPointsPaymentGroupBean();
		if (reviewOrderBean.getComponent().getPaymentDetails()
				.getLoyaltyPointsPaymentGroups() != null
				&& !reviewOrderBean.getComponent().getPaymentDetails()
						.getLoyaltyPointsPaymentGroups().isEmpty()) {
			if (reviewOrderBean.getComponent().getPaymentDetails()
					.getLoyaltyPointsPaymentGroups().size() != 0) {
				for (int i = 0; i < reviewOrderBean.getComponent()
						.getPaymentDetails().getLoyaltyPointsPaymentGroups()
						.size(); i++) {
					checkOutLoyality = reviewOrderBean.getComponent()
							.getPaymentDetails()
							.getLoyaltyPointsPaymentGroups().get(i);
					amountAdjusted += Double.valueOf(checkOutLoyality
							.getAmount());
				}
			}
		}
		/*
		 * if (amountAdjusted != 0) { mOrderTotal.setText("$" + String.format(
		 * "%.2f", Double.valueOf(reviewOrderBean.getComponent()
		 * .getPaymentDetails().getOrderDetails() .getTotal()) -
		 * amountAdjusted)); } else {
		 */
		mOrderTotal.setText("$"
				+ String.format(
						"%.2f",
						Double.valueOf(reviewOrderBean.getComponent()
								.getPaymentDetails().getOrderDetails()
								.getTotalNew())));
		/* } */
		int quantity = 0;
		if (null != reviewOrderBean.getComponent().getPaymentDetails()
				.getCommerceItems()) {
			for (int i = 0; i < reviewOrderBean.getComponent()
					.getPaymentDetails().getCommerceItems().size(); i++) {
				quantity = quantity
						+ Integer.parseInt(reviewOrderBean.getComponent()
								.getPaymentDetails().getCommerceItems().get(i)
								.getQuantity());
				UltaDataCache.getDataCacheInstance().setQuantity("" + quantity);
			}
		}
		if (null != reviewOrderBean.getComponent().getPaymentDetails()
				.getCommerceItems()
				&& null != reviewOrderBean.getComponent().getPaymentDetails()
						.getElectronicGiftCardCommerceItems()) {
			mNumberOfItemstextView.setText(quantity
					+ reviewOrderBean.getComponent().getPaymentDetails()
							.getElectronicGiftCardCommerceItems().size()
					+ getResources().getString(R.string.items_to_buy));
		} else if (null != reviewOrderBean.getComponent().getPaymentDetails()
				.getCommerceItems()) {
			mNumberOfItemstextView.setText(quantity
					+ getResources().getString(R.string.items_to_buy));
		} else if (null != reviewOrderBean.getComponent().getPaymentDetails()
				.getElectronicGiftCardCommerceItems()) {
			mNumberOfItemstextView.setText(reviewOrderBean.getComponent()
					.getPaymentDetails().getElectronicGiftCardCommerceItems()
					.size()
					+ getResources().getString(R.string.items_to_buy));
		}
	}

	@Override
	public void onEditClicked() {
		finish();
	}

	@Override
	public void onBackPressed() {
		if (UltaDataCache.getDataCacheInstance().isExpressCheckout()) {
			Intent backToBasket = new Intent(ReviewOrderActivity.this,
					ViewItemsInBasketActivity.class);
			backToBasket.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(backToBasket);
		} else {
			finish();
		}

	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			// invokeCommitOrderService();
			navigateToBasketOnSessionTimeout(ReviewOrderActivity.this);
		} else {
			pd.dismiss();
		}
	}
}
