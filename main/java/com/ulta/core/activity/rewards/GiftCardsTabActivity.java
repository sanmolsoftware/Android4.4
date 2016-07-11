/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.rewards;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.MyOrderHistoryActivity;
import com.ulta.core.bean.account.GiftCardBean;
import com.ulta.core.bean.account.OrderStatusBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.util.Utility.validateEmail;

/**
 * The Class GiftCardsTabActivity. this is used for both gifr card balance as
 * well as order status the layouts are named according to the call made to this
 * activity
 */
public class GiftCardsTabActivity extends UltaBaseActivity implements
		OnSessionTimeOut, TextWatcher {

	// TextView txtView1, txtView2;
	EditText edtText1, edtText2;
	Button button;
	TextView txtStatus;
	private static String giftCardNumber, giftcardPin, emailAddress, orderId;
	private String menuKey;
	LinearLayout formLayout, loadingLayout;
	private TextView firstFieldErrorText, secondFieldErrorText;
	private Drawable originalDrawable;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_cards_tab);
		initViews();
		setActivity(GiftCardsTabActivity.this);

		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getString("MenuKey")) {
				menuKey = getIntent().getExtras().getString("MenuKey");
			}
		}
		if (menuKey != null && menuKey.equals("FromSideMenu")) {
			setTitle("Order Status");
			setOrderstaus(true);
			// txtView1.setText("Email Address");
			// txtView2.setText("Order Id");
			//
			// txtView2.setText("Email Address");
			// txtView1.setText("Order Id");
			edtText1.setHint("Order Id ");
			edtText2.setHint("Email Address");
			button.setText("CHECK STATUS");
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
								// 3.2 Release
					if (edtText1.getText().toString().length() != 0
							&& edtText2.getText().toString().length() != 0) {
						emailAddress = edtText2.getText().toString();
						if (!validateEmail(emailAddress)) {
							/*
							 * notifyUser("Please enter valid Email",
							 * GiftCardsTabActivity.this);
							 */
							setError(edtText2, secondFieldErrorText,
									"Please enter valid Email");
						} else {
							orderId = edtText1.getText().toString();
							loadingLayout.setVisibility(View.VISIBLE);

							formLayout.setVisibility(View.GONE);
							invokeOrderStatus(orderId, emailAddress);
						}

					} else if (edtText1.getText().toString().length() == 0) {
						// notifyUser("Please fill all the Fields",
						// GiftCardsTabActivity.this);
						setError(edtText1, firstFieldErrorText,
								"Please enter Order Id");
					} else if (edtText2.getText().toString().length() == 0) {
						// notifyUser("Please fill all the Fields",
						// GiftCardsTabActivity.this);
						setError(edtText2, secondFieldErrorText,
								"Please enter Email Id");
					}
				}
			});

		} else {

			// txtView1.setText("Giftcard Number");
			// txtView2.setText("Pin");

			edtText1.setHint("* Giftcard Number (1234567890)");
			edtText2.setHint("* Pin (12345)");
			setTitle("Gift Card");
			edtText2.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			button.setText("CHECK BALANCE");
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
								if (edtText1.getText().toString().length() != 0
							&& edtText2.getText().toString().length() != 0) {
						giftCardNumber = edtText1.getText().toString();
						giftcardPin = edtText2.getText().toString();
						loadingLayout.setVisibility(View.VISIBLE);
						formLayout.setVisibility(View.GONE);
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edtText2.getWindowToken(),
								0);

						invokeCheckBalance();
					} else if (edtText1.getText().toString().length() == 0) {
						// notifyUser("Please fill all the Fields",
						// GiftCardsTabActivity.this);
						setError(edtText1, firstFieldErrorText,
								"Please enter Gift Card Number");
					} else if (edtText2.getText().toString().length() == 0) {
						// notifyUser("Please fill all the Fields",
						// GiftCardsTabActivity.this);
						setError(edtText2, secondFieldErrorText,
								"Please enter Gift Card Pin");
					}
				}

			});
		}

	}

	private void creatingPageName(String pageName) {
		trackAppState(this, pageName);
	}

	private void initViews() {
		// txtView1 = (TextView) findViewById(R.id.textView1);
		// txtView2 = (TextView) findViewById(R.id.textView2);
		firstFieldErrorText = (TextView) findViewById(R.id.firstFieldErrorText);
		secondFieldErrorText = (TextView) findViewById(R.id.secondFieldErrorText);
		edtText1 = (EditText) findViewById(R.id.edtText1);
		edtText2 = (EditText) findViewById(R.id.edtText2);
		originalDrawable = edtText2.getBackground();
		button = (Button) findViewById(R.id.actionButton);
		txtStatus = (TextView) findViewById(R.id.txtStatus);
		formLayout = (LinearLayout) findViewById(R.id.formDialog);
		loadingLayout = (LinearLayout) findViewById(R.id.loadingDialog);
		loadingLayout.setVisibility(View.GONE);
		formLayout.setVisibility(View.VISIBLE);
		edtText1.addTextChangedListener(this);
		edtText2.addTextChangedListener(this);
	}

	private void invokeCheckBalance() {

		InvokerParams<GiftCardBean> invokerParams = new InvokerParams<GiftCardBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.CHECK_BALANCE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateCheckBalanceParameters());
		invokerParams.setUltaBeanClazz(GiftCardBean.class);
		CheckBalanceHandler userCreationHandler = new CheckBalanceHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<giftCardActivity><invokeCheckbalance><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> populateCheckBalanceParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();

		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("giftcardNumber", giftCardNumber);
		urlParams.put("giftcardPin", giftcardPin);

		return urlParams;
	}

	/**
	 * The Class PaymentHandler.
	 */
	public class CheckBalanceHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			// Logger.Log("<ForgotLoginHandler><handleMessage><getErrorMessage>>"
			// + (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(GiftCardsTabActivity.this);
				} else {
					try {
						notifyUser(getErrorMessage(), GiftCardsTabActivity.this);

					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);
					/*
					 * loadingLayout.setVisibility(View.GONE);
					 * titleBarButton.setVisibility(View.VISIBLE);
					 * formLayout.setVisibility(View.VISIBLE);
					 */
				}
				txtStatus.setText("");
			} else {
				creatingPageName(WebserviceConstants.GIFTCARD_BALANCE);
				Logger.Log("<ShippingAddressHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				GiftCardBean ultaBean = (GiftCardBean) getResponseBean();
				List<String> errors = ultaBean.getErrorInfos();
				if (null != errors && !(errors.isEmpty())) {
					try {
						/*AlertDialog.Builder builder = new AlertDialog.Builder(
								GiftCardsTabActivity.this);
						builder.setTitle("Alert")
								.setMessage(errors.get(0))
								.setCancelable(false)
								.setNegativeButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();*/
						
						final Dialog alertDialog = showAlertDialog(
								GiftCardsTabActivity.this, "Alert",
								errors.get(0), "Ok", "");
						alertDialog.show();
						alertDialog.setCancelable(false);
						mDisagreeButton.setVisibility(View.GONE);
						mAgreeButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {

								alertDialog.dismiss();
							}
						});

					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
					txtStatus.setText("");
					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);
				} else {
					Logger.Log(ultaBean.getComponent().getGiftcardBalance());
					Logger.Log(ultaBean.getComponent().getGiftcardNumber());
					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);
					txtStatus.setText("You have $"
							+ ultaBean.getComponent().getGiftcardBalance());
				}
			}
		}
	}

	// 3.2 Release
	private void invokeOrderStatus(String orderId, String emailAddress) {
		InvokerParams<OrderStatusBean> invokerParams = new InvokerParams<OrderStatusBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.FETCH_ORDER_DETAILS);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateOderHistoryParameters(orderId,
				emailAddress));
		invokerParams.setUltaBeanClazz(OrderStatusBean.class);
		OrderStatusHandler handler = new OrderStatusHandler();
		invokerParams.setUltaHandler(handler);
		try {
			new ExecutionDelegator(invokerParams);

		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateOderHistoryParameters(String orderId,
			String emailAddress) {
		Map<String, String> urlParams = new HashMap<String, String>();

		urlParams.put("atg-rest-depth", "0");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("numOrders", "10");
		urlParams.put("startIndex", "0");
		urlParams.put("orderNumber", orderId);
		urlParams.put("emailId", emailAddress);

		return urlParams;
	}

	public class OrderStatusHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<OrderDetailsHandlerGiftCardsTab><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							GiftCardsTabActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}

				loadingLayout.setVisibility(View.GONE);
				formLayout.setVisibility(View.VISIBLE);
			} else {
				OrderStatusBean ultaBean1 = (OrderStatusBean) getResponseBean();

				if (null != ultaBean1) {
					if (null != ultaBean1.getAtgResponse()) {
						if (null != ultaBean1.getAtgResponse()
								.getErrorMessage()) {
							notifyUser(ultaBean1.getAtgResponse()
									.getErrorMessage(),
									GiftCardsTabActivity.this);

							loadingLayout.setVisibility(View.GONE);
							formLayout.setVisibility(View.VISIBLE);
							Logger.Log("order failure");
						} else {
							Logger.Log("order success");
							Intent goToMyOrderHistory = new Intent(
									GiftCardsTabActivity.this,
									MyOrderHistoryActivity.class);
							goToMyOrderHistory.putExtra("OrderStausBean",
									ultaBean1.getAtgResponse());
							goToMyOrderHistory.putExtra(
									"orderStatusFromGiftTab",
									"For Anonymous User");
							startActivity(goToMyOrderHistory);
							loadingLayout.setVisibility(View.GONE);
							formLayout.setVisibility(View.VISIBLE);

						}
					}
				}

			}

		}
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			invokeCheckBalance();
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
		if (s.hashCode() == edtText1.getText().hashCode()) {
			edtText1.setBackgroundDrawable(originalDrawable);
			firstFieldErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtText2.getText().hashCode()) {
			edtText2.setBackgroundDrawable(originalDrawable);
			secondFieldErrorText.setVisibility(View.GONE);
		}
	}

	public void setError(EditText editText, TextView errorTV, String message) {
		editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
		errorTV.setText("" + message);
		errorTV.setVisibility(View.VISIBLE);
	}
}
