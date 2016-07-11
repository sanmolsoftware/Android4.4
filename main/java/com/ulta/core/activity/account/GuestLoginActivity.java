package com.ulta.core.activity.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.ViewItemsInBasketActivity;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.bean.checkout.GuestUserDataBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.util.Utility.validateEmail;
//import com.compuware.apm.uem.mobile.android.CompuwareUEM;
//import com.compuware.apm.uem.mobile.android.UemAction;

public class GuestLoginActivity extends UltaBaseActivity implements TextWatcher {
	// private TextView txtView1, txtView2;
	private EditText edtText1, edtText2;
	private Button button;
	private String emailId, confirmEmailId;
	private static String EMAIL_VALIDATION_MESSAGE = "Please enter valid Email";
	private static String CONFIRM_EMAIL_VALIDATION_MESSAGE = "Supplied email ids do not match";
	private ProgressDialog pd;
	// private UemAction guestLoginAction;
	private boolean ifFromPayPal;
	private HashMap<String, String> guestUserDeatails;
	Gson gson;
	String json;
	private TextView firstFieldErrorText, secondFieldErrorText;
	private Drawable originalDrawable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_cards_tab);
		trackAppState(GuestLoginActivity.this,
				WebserviceConstants.CHECKOUT_LOGIN_GUEST);
		setTitle("Guest Checkout");
		if (null != getIntent().getExtras()) {
			ifFromPayPal = getIntent().getExtras().getBoolean("fromPayPal",
					false);
		}
		initializeViews();
		prePopulateGuestUserdata();
	}

	private void prePopulateGuestUserdata() {
		try {

			if (null != UltaDataCache.getDataCacheInstance()
					.getGuestUserDeatails()) {
				HashMap<String, String> guestDeatails = UltaDataCache
						.getDataCacheInstance().getGuestUserDeatails();
				if (null != guestDeatails.get("guest")) {
					gson = new Gson();
					json = guestDeatails.get("guest");
					GuestUserDataBean guestUserDataBean = gson.fromJson(json,
							GuestUserDataBean.class);
					if (null != guestUserDataBean
							&& null != guestUserDataBean.getGuestMailId()) {
						edtText1.setText(""
								+ guestUserDataBean.getGuestMailId());
					}
				}

			}
		} catch (JsonSyntaxException e) {
			Log.e("ShippingAddressGuestUser", "Error in JsonParsing");
		}
	}

	public void initializeViews() {
		// txtView1 = (TextView) findViewById(R.id.textView1);
		// txtView1.setText("* Email");
		// txtView2 = (TextView) findViewById(R.id.textView2);
		// txtView2.setText("* Confirm Email");

		firstFieldErrorText = (TextView) findViewById(R.id.firstFieldErrorText);
		secondFieldErrorText = (TextView) findViewById(R.id.secondFieldErrorText);
		edtText1 = (EditText) findViewById(R.id.edtText1);
		edtText1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		edtText2 = (EditText) findViewById(R.id.edtText2);
		edtText2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		button = (Button) findViewById(R.id.actionButton);
		button.setText("CONTINUE AS GUEST");
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						validateFields();
			}
		});
		edtText1.addTextChangedListener(this);
		edtText2.addTextChangedListener(this);
		originalDrawable = edtText2.getBackground();
		edtText1.setHint("* Email");
		edtText2.setHint("* Confirm Email");
	}

	protected void validateFields() {
		emailId = edtText1.getText().toString().trim();
		confirmEmailId = edtText2.getText().toString().trim();
		if (emailId.length() == 0) {
			try {
				// notifyUser(EMAIL_VALIDATION_MESSAGE,
				// GuestLoginActivity.this);
				setError(edtText1, firstFieldErrorText,
						EMAIL_VALIDATION_MESSAGE);
				edtText1.requestFocus();
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		}

		else if (!validateEmail(emailId)) {

			try {
				// notifyUser(EMAIL_VALIDATION_MESSAGE,
				// GuestLoginActivity.this);
				setError(edtText1, firstFieldErrorText,
						EMAIL_VALIDATION_MESSAGE);
				edtText1.requestFocus();
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		} else if (confirmEmailId.length() == 0) {
			try {
				// notifyUser(CONFIRM_EMAIL_VALIDATION_MESSAGE,
				// GuestLoginActivity.this);
				edtText2.setText("");
				setError(edtText2, secondFieldErrorText,
						CONFIRM_EMAIL_VALIDATION_MESSAGE);
				edtText2.requestFocus();
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		} else if (!emailId.equalsIgnoreCase(confirmEmailId)) {
			try {
				// notifyUser(CONFIRM_EMAIL_VALIDATION_MESSAGE,
				// GuestLoginActivity.this);
				edtText2.setText("");
				setError(edtText2, secondFieldErrorText,
						CONFIRM_EMAIL_VALIDATION_MESSAGE);
				edtText2.requestFocus();
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		}

		else {
			pd = new ProgressDialog(GuestLoginActivity.this);
			setProgressDialogLoadingColor(pd);
			pd.setMessage("Loading...");
			pd.setCancelable(false);
			pd.show();
			// guestLoginAction = CompuwareUEM
			// .enterAction(WebserviceConstants.ACTION_GUEST_LOGIN_INVOCATION);
			// guestLoginAction.reportEvent("Guest is logging in");

			InvokeGuestUserLogin();
		}
	}

	/**
	 * Save the guest user mail id
	 */
	private void performSavingGuestData() {
		try {
			if (null != UltaDataCache.getDataCacheInstance()
					.getGuestUserDeatails()) {
				guestUserDeatails = UltaDataCache.getDataCacheInstance()
						.getGuestUserDeatails();
				if (null != guestUserDeatails.get("guest")) {
					Gson gson = new Gson();
					String json = guestUserDeatails.get("guest");
					GuestUserDataBean guestUserDataBean = gson.fromJson(json,
							GuestUserDataBean.class);
					if (null != guestUserDataBean) {
						if (!guestUserDataBean.getGuestMailId()
								.equalsIgnoreCase(emailId)) {
							GuestUserDataBean guestUserBean = new GuestUserDataBean();
							guestUserBean.setGuestMailId(emailId);
							gson = new Gson();
							json = gson.toJson(guestUserBean);
							guestUserDeatails.put("guest", json);
							UltaDataCache.getDataCacheInstance()
									.setGuestUserDeatails(guestUserDeatails);
						}
					}
				}
			} else {
				guestUserDeatails = new HashMap<String, String>();
				GuestUserDataBean guestUserBean = new GuestUserDataBean();
				guestUserBean.setGuestMailId(emailId);
				gson = new Gson();
				json = gson.toJson(guestUserBean);
				guestUserDeatails.put("guest", json);
				UltaDataCache.getDataCacheInstance().setGuestUserDeatails(
						guestUserDeatails);

			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			Log.e("ShippingAddressGuestUser", "Error in JsonParsing");
		}

	}

	/**
	 * This method is used to create the web service request for anonymous login
	 * Guest user's email id is the required param
	 **/
	private void InvokeGuestUserLogin() {
		InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.GUEST_USER_LOGIN_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateGuestUserLoginParams());
		invokerParams.setUltaBeanClazz(LoginBean.class);
		AnonymousLoginHandler anonymousLoginHandler = new AnonymousLoginHandler();
		invokerParams.setUltaHandler(anonymousLoginHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<GuestLoginActivity><invokeLogin><UltaException>>"
					+ ultaException);

		}
	}

	/** This method set the params to the request */
	protected Map<String, String> populateGuestUserLoginParams() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("anonymousEmailAddress", emailId);
		urlParams.put("anonymousConfirmEmailAddress", confirmEmailId);
		return urlParams;
	}

	/**
	 * The Class AnonymousLoginHandler.
	 */
	public class AnonymousLoginHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<AnonymousLoginHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					// guestLoginAction
					// .reportError(
					// getErrorMessage(),
					// WebserviceConstants.DYN_ERRCODE_GUEST_LOGIN_ACTIVITY);
					// guestLoginAction.leaveAction();
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							GuestLoginActivity.this);
					pd.dismiss();
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				LoginBean ultaBean = (LoginBean) getResponseBean();
				List<String> result = ultaBean.getErrorInfos();
				// guestLoginAction.reportEvent("guest login success");
				// guestLoginAction.leaveAction();
				pd.dismiss();
				Logger.Log("<LoginHandler><handleMessage><getResponseBean>>"
						+ result);
				if (null == result || result.get(0).equalsIgnoreCase("")) {
					Utility.saveToSharedPreference(
							UltaConstants.LOGGED_MAIL_ID, emailId,
							getApplicationContext());
					performSavingGuestData();
					Logger.Log("<Login><handleMessage><getResponseBean>>"
							+ (getResponseBean()));
					UltaDataCache.getDataCacheInstance().setAnonymousCheckout(
							true);
					if (ifFromPayPal) {
						pd = new ProgressDialog(GuestLoginActivity.this);
						setProgressDialogLoadingColor(pd);
						pd.setMessage("Loading...");
						pd.setCancelable(false);
						pd.show();
						invokePayPalPaymentDetails();
					} else {
						// Basket is shown once the anonymouse user logs in to
						// maintain consistency in
						// both iPhone and android
						Intent gotoBasket = new Intent(GuestLoginActivity.this,
								ViewItemsInBasketActivity.class);
						startActivity(gotoBasket);
						finish();
					}
					
				} else {
					try {
						// guestLoginAction.reportError(result.get(0), 107);
						// guestLoginAction.leaveAction();
						notifyUser(Utility.formatDisplayError(result.get(0)),
								GuestLoginActivity.this);
						pd.dismiss();
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			}
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
