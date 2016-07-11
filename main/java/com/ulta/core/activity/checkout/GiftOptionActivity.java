/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.checkout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.checkout.CheckoutOrderAdjustmentBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.product.AddToCartBean;
import com.ulta.core.bean.product.RedeemLevelPointsBean;
import com.ulta.core.bean.product.RedeemPointBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class GiftOptionActivity.
 */
public class GiftOptionActivity extends UltaBaseActivity implements
		OnDoneClickedListener {

	/** The Constant CHOICE. */
	public static final String CHOICE = "choice";

	/** The btn done. */
	Button btnDone, titleBarButton;

	/** The no of characters. */
	TextView noOfCharacters;

	/** The gift note. */
	ImageView giftBox, giftNote;

	/** The edt note for gift. */
	EditText edtNoteForGift;

	/** The form layout. */
	LinearLayout formLayout;

	/** The loading layout. */
	LinearLayout loadingLayout;

	/** The check. */
	String check = "";

	/** The choice of gift. */
	String choiceOfGift = "GiftBox";

	/** The hashmap data carrier. */
	HashMap<String, String> hashmapDataCarrier = new HashMap<String, String>();
	// UemAction giftOptionsAction;

	private LinearLayout mGiftBoxAndNoteLayout;
	private LinearLayout mGiftNoteLayout;
	private ImageButton mShippingTypeButton;
	private ImageButton mPaymentButton;
	/* private Switch mGiftthisOrderSwitch; */
	private CheckedTextView giftThisOrderCheckedTextView;
	private LinearLayout mGiftSelectionLayout;
	private TextView mChoosePreferredOptionTextView;

	private TextView mCheckout_shipping, mCheckout_payment,
			mCheckout_review_order;
	private CheckoutShippmentMethodBean mCheckoutShippmentMethodBean;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		loadingLayout.setVisibility(View.GONE);
		titleBarButton.setVisibility(View.VISIBLE);
		formLayout.setVisibility(View.VISIBLE);
		// if (UltaDataCache.getDataCacheInstance().isMovingBackInChekout()) {
		// finish();
		// } else {
		//
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift_options);
		setTitle("Gift Box");
		displayContinueButton();
		initFooterViews();
		initViews();
		trackAppAction(GiftOptionActivity.this,
				WebserviceConstants.CHECKOUT_STEP_4_EVENT_ACTION);

		trackAppAction(GiftOptionActivity.this,
				WebserviceConstants.CHECKOUT_STEP_4_VISIT_EVENT_ACTION);
		if (null != getIntent().getExtras()) {
			mCheckoutShippmentMethodBean = (CheckoutShippmentMethodBean) getIntent()
					.getExtras().get("checkoutGiftOptionsBean");
		}

		if (UltaDataCache.getDataCacheInstance().isOnlyEgiftCard()) {
			Intent goToNewCreditCardMethod = new Intent(
					GiftOptionActivity.this, PaymentMethodActivity.class);
			goToNewCreditCardMethod.putExtra("checkoutShipmentBean",
					mCheckoutShippmentMethodBean);
			startActivity(goToNewCreditCardMethod);
		}
		setFooterValues(mCheckoutShippmentMethodBean);

		edtNoteForGift.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				count = (edtNoteForGift.getText().toString().length());

				// Change by Shyam
				edtNoteForGift.setImeActionLabel("actionDone",
						EditorInfo.IME_FLAG_NO_ENTER_ACTION);

				// End of Change by Shyam

				noOfCharacters.setText(125 - count + " characters remaining");
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

		/*
		 * giftBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton arg0, boolean
		 * arg1) {
		 * UltaDataCache.getDataCacheInstance().setGiftWrapPrice("3.99"); if
		 * (arg0.isChecked()) { giftNote.setChecked(false); choiceOfGift =
		 * "GiftBox"; } else { choiceOfGift = "NoGift"; } } });
		 */
		/*
		 * giftNote.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton arg0, boolean
		 * arg1) {
		 * 
		 * UltaDataCache.getDataCacheInstance().setGiftWrapPrice("0.00"); if
		 * (arg0.isChecked()) { giftBox.setChecked(false); choiceOfGift =
		 * "GiftNote"; } else { choiceOfGift = "NoGift"; } } });
		 */
	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		titleBarButton = (Button) findViewById(R.id.title_bar_done_icon);
		loadingLayout = (LinearLayout) findViewById(R.id.loadingDialog);
		formLayout = (LinearLayout) findViewById(R.id.new_gift_option_form_layout);
		giftBox = (ImageView) findViewById(R.id.giftBox);
		giftNote = (ImageView) findViewById(R.id.giftNote);
		edtNoteForGift = (EditText) findViewById(R.id.noteForGift);
		noOfCharacters = (TextView) findViewById(R.id.txtNoOfCharacters);

		mGiftBoxAndNoteLayout = (LinearLayout) findViewById(R.id.giftBoxAndNoteLayout);
		mGiftNoteLayout = (LinearLayout) findViewById(R.id.giftNoteLayout);

		mShippingTypeButton = (ImageButton) findViewById(R.id.shippingTypeButton);
		mPaymentButton = (ImageButton) findViewById(R.id.paymentButton);
		/*
		 * mGiftthisOrderSwitch = (Switch)
		 * findViewById(R.id.giftThisorderSwitch);
		 */
		giftThisOrderCheckedTextView = (CheckedTextView) findViewById(R.id.giftThisOrderCheckbox);
		Drawable drawable = getResources().getDrawable(
				(R.drawable.custom_btn_radio));
		drawable.setBounds(0, 0, 72, 72);

		giftThisOrderCheckedTextView.setCompoundDrawables(null, null, drawable,
				null);
		mGiftSelectionLayout = (LinearLayout) findViewById(R.id.giftSelectionLayout);
		mChoosePreferredOptionTextView = (TextView) findViewById(R.id.choosePreferredOption);

		mCheckout_shipping = (TextView) findViewById(R.id.checkout_shipping);
		mCheckout_payment = (TextView) findViewById(R.id.checkout_payment);
		mCheckout_review_order = (TextView) findViewById(R.id.checkout_review_order);
		mCheckout_shipping.setBackgroundColor(getResources().getColor(
				R.color.chekout_header_highlighted));
		mCheckout_shipping.setTextColor(getResources().getColor(R.color.white));
		mCheckout_payment.setBackgroundColor(getResources().getColor(
				R.color.olapic_detail_caption));
		mCheckout_review_order.setBackgroundColor(getResources().getColor(
				R.color.olapic_detail_caption));
		mGiftBoxAndNoteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				giftBox.setVisibility(View.VISIBLE);
				giftNote.setVisibility(View.INVISIBLE);
				choiceOfGift = "GiftBox";
				setFooterValues(mCheckoutShippmentMethodBean);
			}
		});

		mGiftNoteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				giftBox.setVisibility(View.INVISIBLE);
				giftNote.setVisibility(View.VISIBLE);
				choiceOfGift = "GiftNote";
				setFooterValues(mCheckoutShippmentMethodBean);
			}
		});

		mShippingTypeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		mPaymentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onFormSubmit();
			}
		});
		giftThisOrderCheckedTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (giftThisOrderCheckedTextView.isChecked()) {
					UltaDataCache.getDataCacheInstance().setGiftTheOrder(false);
					giftThisOrderCheckedTextView.setChecked(false);
					mGiftSelectionLayout.setVisibility(View.INVISIBLE);
					mChoosePreferredOptionTextView
							.setVisibility(View.INVISIBLE);
					giftBox.setVisibility(View.INVISIBLE);
					giftNote.setVisibility(View.VISIBLE);
					choiceOfGift = "NoGift";
					edtNoteForGift.setText("");
					

				} else {

					UltaDataCache.getDataCacheInstance().setGiftTheOrder(true);
					giftThisOrderCheckedTextView.setChecked(true);
					mGiftSelectionLayout.setVisibility(View.VISIBLE);
					mChoosePreferredOptionTextView.setVisibility(View.VISIBLE);
					giftBox.setVisibility(View.VISIBLE);
					giftNote.setVisibility(View.INVISIBLE);
					choiceOfGift = "GiftBox";
				}
				setFooterValues(mCheckoutShippmentMethodBean);

			}
		});
		/*
		 * mGiftthisOrderSwitch .setOnCheckedChangeListener(new
		 * OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) {
		 * 
		 * if (isChecked) { mGiftSelectionLayout.setVisibility(View.VISIBLE);
		 * mChoosePreferredOptionTextView .setVisibility(View.VISIBLE);
		 * giftBox.setVisibility(View.VISIBLE);
		 * giftNote.setVisibility(View.INVISIBLE); choiceOfGift = "GiftBox"; }
		 * else { mGiftSelectionLayout.setVisibility(View.INVISIBLE);
		 * mChoosePreferredOptionTextView .setVisibility(View.INVISIBLE);
		 * giftBox.setVisibility(View.INVISIBLE);
		 * giftNote.setVisibility(View.VISIBLE); choiceOfGift = "NoGift"; }
		 * setFooterValues(mCheckoutShippmentMethodBean); } });
		 */
		mExpandImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (toShowCouponCodeLayout % 2 != 0) {
					mSubTotalFooterLayout.setVisibility(View.VISIBLE);
					mShippingTypeFooterLayout.setVisibility(View.VISIBLE);
					mTaxFooterLayout.setVisibility(View.VISIBLE);
//					mGiftBoxAndNoteFooterLayout.setVisibility(View.VISIBLE);
					if (null != mTvCouponDiscountValue.getText().toString().trim() && !mTvCouponDiscountValue.getText().toString().trim().isEmpty()) {
						mCouponDiscountLayout.setVisibility(View.VISIBLE);
					}
					if (null != mTvAdditionalDiscountValue.getText().toString().trim() && !mTvAdditionalDiscountValue.getText().toString().trim().isEmpty()) {
						mAdditionalDiscountLayout.setVisibility(View.VISIBLE);
					}
					mExpandImageView.setImageDrawable(getResources()
							.getDrawable(R.drawable.minus));

				} else {
					mSubTotalFooterLayout.setVisibility(View.GONE);
					mShippingTypeFooterLayout.setVisibility(View.GONE);
					mTaxFooterLayout.setVisibility(View.GONE);
//					mGiftBoxAndNoteFooterLayout.setVisibility(View.GONE);
					mExpandImageView.setImageDrawable(getResources()
							.getDrawable(R.drawable.plus));
					mCouponDiscountLayout.setVisibility(View.GONE);
					mAdditionalDiscountLayout.setVisibility(View.GONE);
				}
				toShowCouponCodeLayout++;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.widgets.flyin.OnDoneClickedListener#onDoneClicked()
	 */
	@Override
	public void onDoneClicked() {
		onFormSubmit();
	}

	/**
	 * On form submit.
	 */
	private void onFormSubmit() {

		getAllValues();
		if ((check.length() != 0 || check.contains(" "))
				&& choiceOfGift.equals("NoGift")) {
			notifyUser("please select an option of gift-box or gift-note",
					GiftOptionActivity.this);
		} else if ((check.length() == 0 || check.isEmpty() || check
				.startsWith(" ")) && !choiceOfGift.equals("NoGift")) {

			// Changes By Shyam
			/*
			 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 * 
			 * builder.setTitle("Confirm");
			 * builder.setMessage("Fill the Gift Note");
			 * 
			 * builder.setPositiveButton("OK", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * public void onClick(DialogInterface dialog, int which) { // Do
			 * nothing but close the dialog
			 * 
			 * dialog.dismiss(); }
			 * 
			 * });
			 * 
			 * builder.setNegativeButton("NO THANKS", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { // Do nothing // giftOptionsAction =
			 * UemAction.enterAction(WebserviceConstants
			 * .ACTION_GIFT_OPTION_INVOCATION); //
			 * giftOptionsAction.reportEvent("applying gift options to order");
			 * hashmapDataCarrier.put("choice", choiceOfGift);
			 * hashmapDataCarrier.put("giftNoteWritten", null);
			 * UltaDataCache.getDataCacheInstance
			 * ().setGiftOption(hashmapDataCarrier);
			 * loadingLayout.setVisibility(View.VISIBLE);
			 * titleBarButton.setVisibility(View.GONE);
			 * formLayout.setVisibility(View.GONE); invokeApplyGiftOption(); }
			 * });
			 * 
			 * AlertDialog alert = builder.create(); alert.show();
			 */
			final Dialog alert = showAlertDialog(GiftOptionActivity.this,
					"Confirm", "Fill the Gift Note", "OK", "NO THANKS");
			alert.show();

			mAgreeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alert.dismiss();
				}
			});
			mDisagreeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// giftOptionsAction =
					// UemAction.enterAction(WebserviceConstants.ACTION_GIFT_OPTION_INVOCATION);
					// giftOptionsAction.reportEvent("applying gift options to order");
					hashmapDataCarrier.put("choice", choiceOfGift);
					hashmapDataCarrier.put("giftNoteWritten", null);
					UltaDataCache.getDataCacheInstance().setGiftOption(
							hashmapDataCarrier);
					loadingLayout.setVisibility(View.VISIBLE);
					titleBarButton.setVisibility(View.GONE);
					formLayout.setVisibility(View.GONE);
					invokeApplyGiftOption();
					alert.dismiss();
				}
			});

			// notifyUser("Fill the Gift Note", GiftOptionActivity.this);
		} else if (check.length() > 125) {
			notifyUser("Gift Note length must be less than 125 characters",
					GiftOptionActivity.this);
		} else if (check.length() == 0
				&& choiceOfGift.equalsIgnoreCase("NoGift")) {
			hashmapDataCarrier.put("choice", choiceOfGift);
			hashmapDataCarrier.put("giftNoteWritten", check);
			UltaDataCache.getDataCacheInstance().setGiftOption(
					hashmapDataCarrier);
			// giftOptionsAction = UemAction
			// .enterAction(WebserviceConstants.ACTION_GIFT_OPTION_INVOCATION);
			// giftOptionsAction.reportEvent("No gift option selected");
			loadingLayout.setVisibility(View.VISIBLE);
			titleBarButton.setVisibility(View.GONE);
			formLayout.setVisibility(View.GONE);
			invokeApplyGiftOption();

		} else {
			// giftOptionsAction =
			// UemAction.enterAction(WebserviceConstants.ACTION_GIFT_OPTION_INVOCATION);
			// giftOptionsAction.reportEvent("applying gift options to order");
			hashmapDataCarrier.put("choice", choiceOfGift);
			hashmapDataCarrier.put("giftNoteWritten", check);
			UltaDataCache.getDataCacheInstance().setGiftOption(
					hashmapDataCarrier);
			loadingLayout.setVisibility(View.VISIBLE);
			titleBarButton.setVisibility(View.GONE);
			formLayout.setVisibility(View.GONE);
			invokeApplyGiftOption();
		}
	}

	/**
	 * Invoke apply gift option.
	 */
	private void invokeApplyGiftOption() {
		InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.GIFT_OPTION_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateGiftOptionPrameters());
		invokerParams.setUltaBeanClazz(AddToCartBean.class);
		GiftOptionsHandler giftOptionsHandler = new GiftOptionsHandler();
		invokerParams.setUltaHandler(giftOptionsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<GiftOptionsActivity><invokeGiftOptionService><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Populate gift option prameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populateGiftOptionPrameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		String gftWrap, gftNote;
		if (choiceOfGift.equalsIgnoreCase("NoGift")) {
			gftWrap = "false";
			gftNote = "false";
		} else if (choiceOfGift.equalsIgnoreCase("GiftNote")) {
			gftWrap = "false";
			gftNote = "true";
		} else {
			gftWrap = "true";
			gftNote = "true";
		}
		Logger.Log(">>>>>>>>>>GiftWrap<<<<<<" + gftWrap);
		Logger.Log(">>>>>>>>>>GiftNote<<<<<<" + gftNote);

		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("giftNoteSelected", gftNote);
		urlParams.put("giftWrapSelected", gftWrap);
		urlParams.put("giftMessage", check);

		return urlParams;
	}

	/**
	 * Gets the all values.
	 * 
	 * @return the all values
	 */
	private void getAllValues() {
		check = edtNoteForGift.getText().toString().trim();
	}

	/**
	 * The Class GiftOptionsHandler.
	 */
	public class GiftOptionsHandler extends UltaHandler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<GiftOptionsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					// giftOptionsAction
					// .reportError(
					// getErrorMessage(),
					// WebserviceConstants.DYN_ERRCODE_GIFT_OPTION_ACTIVITY);
					// giftOptionsAction.leaveAction();
					notifyUser(getErrorMessage(), GiftOptionActivity.this);
					setError(GiftOptionActivity.this, getErrorMessage());
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
				loadingLayout.setVisibility(View.GONE);
				titleBarButton.setVisibility(View.VISIBLE);
				formLayout.setVisibility(View.VISIBLE);
			} else {
				Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				AddToCartBean ultaBean = (AddToCartBean) getResponseBean();

				List<String> errors = ultaBean.getErrorInfos();
				if (null != errors && !(errors.isEmpty()))
					try {
						// giftOptionsAction
						// .reportError(
						// errors.get(0),
						// WebserviceConstants.DYN_ERRCODE_GIFT_OPTION_ACTIVITY);
						// giftOptionsAction.leaveAction();
						notifyUser(errors.get(0), GiftOptionActivity.this);
						setError(GiftOptionActivity.this, errors.get(0));
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				else { // 3.2Release
					double total = 0.0;
					String currencyCode = null;
					if (ultaBean.getComponent().getCart().getOrderDetails()
							.getTotal() != 0) {
						total = ultaBean.getComponent().getCart()
								.getOrderDetails().getTotal();
					}
					if (ultaBean.getComponent().getCart().getOrderDetails()
							.getCurrencyCode() != null) {
						currencyCode = ultaBean.getComponent().getCart()
								.getOrderDetails().getCurrencyCode();
					}
					RedeemLevelPointsBean redeemPointLvel = null;
					ArrayList<RedeemPointBean> arryRedeemPoints = null;

					if (ultaBean.getComponent().getRedeemLevels() != null) {
						redeemPointLvel = new RedeemLevelPointsBean();
						redeemPointLvel = (RedeemLevelPointsBean) ultaBean
								.getComponent().getRedeemLevels();
						arryRedeemPoints = new ArrayList<RedeemPointBean>();
						arryRedeemPoints = redeemPointLvel.getRedeemLevels();
					}

					Intent paymentOptionsIntent = new Intent(
							GiftOptionActivity.this,
							PaymentMethodActivity.class);
					paymentOptionsIntent.putExtra("checkoutAddtoCartBean",
							ultaBean);
					paymentOptionsIntent.putExtra("choiceOfGift", choiceOfGift);
					if(null!=check) {
						paymentOptionsIntent.putExtra("message", check);
					}
					if (arryRedeemPoints != null
							&& arryRedeemPoints.size() != 0) {
						paymentOptionsIntent.putExtra("OrderForRedeemPoint",
								(Serializable) arryRedeemPoints);
					}
					if (total != 0.0) {
						paymentOptionsIntent.putExtra("PayWithPaypal", total);
					}
					if (currencyCode != null) {
						paymentOptionsIntent.putExtra("CurrencyCode",
								currencyCode);
					}
					startActivity(paymentOptionsIntent);
					// giftOptionsAction.reportEvent("Gift option page completed");
					// giftOptionsAction.leaveAction();
				}
			}
		}
	}

	public void setFooterValues(
			CheckoutShippmentMethodBean checkoutShippmentMethodBean) {
		try {
			if (null != checkoutShippmentMethodBean) {
				if (null != checkoutShippmentMethodBean.getComponent()) {
					if (null != checkoutShippmentMethodBean.getComponent()
							.getCart()) {
						if (null != checkoutShippmentMethodBean.getComponent()
								.getCart().getOrderDetails()) {
							Double totalAdjustment = 0.0;
							List<CheckoutOrderAdjustmentBean> orderAdjustments = checkoutShippmentMethodBean
									.getComponent().getCart()
									.getOrderAdjustments();
//							if (null != orderAdjustments
//									&& !orderAdjustments.isEmpty()) {
//								for (int i = 0; i < orderAdjustments.size(); i++) {
//									totalAdjustment = totalAdjustment
//											+ orderAdjustments.get(i)
//													.getTotalAdjustment();
//								}
//								mSubTotalValueTextView
//										.setText("$"
//												+ String.format(
//														"%.2f",
//														Double.valueOf(totalAdjustment)));
//							} else {
								mSubTotalValueTextView.setText("$"
										+ String.format("%.2f",
												checkoutShippmentMethodBean
														.getComponent()
														.getCart()
														.getOrderDetails()
														.getRawSubtotal()));
//							}
							//tiered price: Additional discount

							if (null != checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount() &&
									!checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount().isEmpty()) {
								mTvAdditionalDiscountValue.setText("-$"
										+ String.format("%.2f",
										Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount())));
							}

							mTaxValueTextView
									.setText("$"
											+ String.format("%.2f",
													checkoutShippmentMethodBean
															.getComponent()
															.getCart()
															.getOrderDetails()
															.getTax()));
							if (null != UltaDataCache.getDataCacheInstance()
									.getShippingType()) {
								mShippingTypeTextView.setText(UltaDataCache
										.getDataCacheInstance()
										.getShippingType());
							}
							mShippingTypeValueTextView.setText("$"
									+ String.format("%.2f",
											checkoutShippmentMethodBean
													.getComponent().getCart()
													.getOrderDetails()
													.getShipping()));
							if (Double.valueOf(checkoutShippmentMethodBean
									.getComponent().getCart()
									.getOrderDetails()
									.getShipping()).equals(0.0)) {
								mShippingTypeValueTextView.setText("FREE");
							}

//							double giftValue = 0.00;
//							if (choiceOfGift.equalsIgnoreCase("GiftBox")) {
//								giftValue = 3.99;
//							} else {
//								giftValue = 0.00;
//							}

//							double total = checkoutShippmentMethodBean
//									.getComponent().getCart().getOrderDetails()
//									.getTotal()
//									+ giftValue;
							double total = checkoutShippmentMethodBean
									.getComponent().getCart().getOrderDetails()
									.getTotal();
//							mGiftBoxAndNoteValueTextView.setText("$"
//									+ String.format("%.2f", giftValue));
							mTotalValueTextView.setText("$"
									+ String.format("%.2f", total));
						}

						//Coupon Value
						if (null != checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount() &&
								null != checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getOrderDiscount() &&
								null != checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getTotalAdjustment()) {
							if (Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getOrderDiscount()).equals(0.0)) {
								mCouponDiscountLayout.setVisibility(View.GONE);
							} else {
								mTvCouponDiscountValue.setText("-$"
										+ String.format(
										"%.2f",
										Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getTotalAdjustment())));
							}

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
