/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.checkout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class PromoCodeActivity.
 */
public class PromoCodeActivity extends UltaBaseActivity implements
		OnDoneClickedListener, OnSessionTimeOut {

	/** The edt promo code. */
	EditText edtGiftCardNumber, edtGiftCardPin, edtPromoCode;

	/** The btn done. */
	Button btnDone;

	LinearLayout formLayout, loadingLayout;

	String cardNumber, cardPin, redeemPointValue;
//	private UemAction giftCardCheckoutAction;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_promo_code);
		setTitle("Gift Card");
		displayContinueButton();

		edtGiftCardNumber = (EditText) findViewById(R.id.giftCardNumber);
		edtGiftCardPin = (EditText) findViewById(R.id.giftCardPin);
		edtPromoCode = (EditText) findViewById(R.id.promotionCode);
		btnDone = (Button) findViewById(R.id.button1);
		formLayout = (LinearLayout) findViewById(R.id.formDialog);
		loadingLayout = (LinearLayout) findViewById(R.id.loadingDialog);
		loadingLayout.setVisibility(View.GONE);
		formLayout.setVisibility(View.VISIBLE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.widgets.flyin.OnDoneClickedListener#onDoneClicked()
	 */
	@Override
	public void onDoneClicked() {
		if (isValidationSuccess()) {
			formSubmit();
		} else {
			notifyUser("Please enter the GiftCard details",
					PromoCodeActivity.this);
		}

	}

	private void formSubmit() {
		getAllValues();
		formLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.VISIBLE);
//		giftCardCheckoutAction = UemAction
//				.enterAction(WebserviceConstants.ACTION_APPLY_GIFT_INVOCATION);
		invokeApplyGiftCard();

	}

	private void invokeApplyGiftCard() {
		InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.APPLY_GIFT_CARD);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populateApplyGiftCardHandlerParameters());
		invokerParams.setUltaBeanClazz(UltaBean.class);
		RetrievePaymentDetailsHandler retrievePaymentDetailsHandler = new RetrievePaymentDetailsHandler();
		invokerParams.setUltaHandler(retrievePaymentDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateApplyGiftCardHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("paymentMethod", "giftCard");
		urlParams.put("giftCardNumber", cardNumber);
		urlParams.put("giftCardPin", cardPin);
		return urlParams;
	}

	/**
	 * The Class RetrievePaymentDetailsHandler.
	 */
	public class RetrievePaymentDetailsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(PromoCodeActivity.this);
				} else {
					try {
//						giftCardCheckoutAction
//								.reportError(
//										getErrorMessage(),
//										WebserviceConstants.DYN_ERRCODE_GIFT_CARD_CHECKOUT_ACTIVITY);
//						giftCardCheckoutAction.leaveAction();
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								PromoCodeActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);

				}
			} else {
				UltaBean ultaBean = (UltaBean) getResponseBean();
				List<String> errors = ultaBean.getErrorInfos();
				if (null != errors && !(errors.isEmpty())) {

					try {
//						giftCardCheckoutAction
//								.reportError(
//										WebserviceConstants.FORM_EXCEPTION_OCCURED,
//										WebserviceConstants.DYN_ERRCODE_GIFT_CARD_CHECKOUT_ACTIVITY);
//						giftCardCheckoutAction.leaveAction();
					/*	AlertDialog alertDialog = new AlertDialog.Builder(
								PromoCodeActivity.this).create();
						alertDialog.setTitle("Alert");
						alertDialog.setMessage(errors.get(0));
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										Intent data = new Intent(
												PromoCodeActivity.this,
												PaymentMethodActivity.class);
										data.putExtra("GiftCard",
												edtGiftCardNumber.getText()
														.toString());
										data.putExtra("GiftCardPin",
												edtGiftCardPin.getText()
														.toString());
										// 3.2 Release
										if (null != getIntent().getExtras()) {
											if (null != getIntent().getExtras()
													.get("redeemPointValue")) {
												data.putExtra(
														"redeemPointValue",
														(String) getIntent()
																.getExtras()
																.get("redeemPointValue"));
											}
										}
										setResult(RESULT_CANCELED, data);
									}
								});
						alertDialog.show();*/
						
						
						final Dialog alertDialog = showAlertDialog(
								PromoCodeActivity.this, "Alert",
								errors.get(0), "Ok", "");
						alertDialog.show();
						
						mDisagreeButton.setVisibility(View.GONE);
						mAgreeButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {



								Intent data = new Intent(
										PromoCodeActivity.this,
										PaymentMethodActivity.class);
								data.putExtra("GiftCard",
										edtGiftCardNumber.getText()
												.toString());
								data.putExtra("GiftCardPin",
										edtGiftCardPin.getText()
												.toString());
								// 3.2 Release
								if (null != getIntent().getExtras()) {
									if (null != getIntent().getExtras()
											.get("redeemPointValue")) {
										data.putExtra(
												"redeemPointValue",
												(String) getIntent()
														.getExtras()
														.get("redeemPointValue"));
									}
								}
								setResult(RESULT_CANCELED, data);
							
							}
						});
						
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);

				} else {
					Intent data = new Intent(PromoCodeActivity.this,
							PaymentMethodActivity.class);
					data.putExtra("GiftCard", edtGiftCardNumber.getText()
							.toString());
					data.putExtra("GiftCardPin", edtGiftCardPin.getText()
							.toString());
					// 3.2 Release
					if (null != getIntent().getExtras()
							&& null != getIntent().getExtras().get(
									"redeemPointValue")) {
						data.putExtra("redeemPointValue", (String) getIntent()
								.getExtras().get("redeemPointValue"));
					}
					setResult(RESULT_OK, data);
//					giftCardCheckoutAction
//							.reportEvent("Gift card applied successfully");
//					giftCardCheckoutAction.leaveAction();
					finish();
				}

			}

		}
	}

	private void getAllValues() {
		cardNumber = edtGiftCardNumber.getText().toString();
		cardPin = edtGiftCardPin.getText().toString();
	}

	private boolean isValidationSuccess() {
		if (edtGiftCardNumber.getText().toString().length() != 0
				&& edtGiftCardPin.getText().toString().length() != 0)
			return true;
		else
			return false;
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			navigateToBasketOnSessionTimeout(PromoCodeActivity.this);
		} else {
			loadingLayout.setVisibility(View.GONE);
		}
	}
}
