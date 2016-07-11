/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.DefaultShippingAddressBean;
import com.ulta.core.bean.checkout.StateListBean;
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
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

public class AddShippingAddressActivity extends UltaBaseActivity implements
		OnDoneClickedListener, OnSessionTimeOut, TextWatcher {

	/** The edt city. */
	EditText edtFirstName, edtLastName, edtAddressLine1, edtAddressline2,
			edtNickName, edtPhone, edtZipCode, edtCity;

	/** The spinner city. */
	// Spinner spinnerCity;

	private List<String> result,resultNew;

	String phone1 = "";

	/** The zip code length. */
	int zipCodeLength, end = 0;

	/** The posofspinneritem. */
	String strFirstName, strLastName, strAddressLine1, strAddressLine2,
			strNickName, phone, strCountry, strZipCode, strCity,
			strSelectedState, posofspinneritem;

	/** The save shipping for future. */
	String setasDefault = "false";

	/** The save shipping as billing. */
	String saveShippingAsBilling = "false";

	/** The state list bean. */
	StateListBean stateListBean;

	/** The btn submit. */
	Button titleBarButton;

	/** The form layout. */
	LinearLayout formLayout;

	/** The loading layout. */
	FrameLayout loadingLayout;

	CheckBox setDefault;

	private String[] anArrayOfStrings;
	/* private boolean isSpinnerSelected = false; */
	private Spinner spState;

	private ProgressDialog pd;

	private String state;
	private Button mDoneButton;

	private boolean mInvokeSetasDefaultShippingaddress;
	private boolean mInvokeAddShippingaddressDetails;
	private Drawable originalDrawable;
	private TextView firstNameErrorText, lastNameErrorText, phoneErrorText,
			address1ErrorText, cityErrorText, stateErrorText, zipCodeErrorText,
			nickNameErrorText;

	/*
	 * @Override protected void onResume() {
	 * if(!isUltaCustomer(AddShippingAddressActivity.this)){ finish(); }
	 * super.onResume(); }
	 */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_shipping_address);
		setTitle("New Shipping Address");
		initViews();
		disableDone();
		loadingLayout.setVisibility(View.VISIBLE);
		formLayout.setVisibility(View.GONE);
		invokeStateList();
		pd = new ProgressDialog(AddShippingAddressActivity.this);
		setProgressDialogLoadingColor(pd);
		pd.setMessage(LOADING_PROGRESS_TEXT);
		pd.setCancelable(false);

		setDefault.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg0.isChecked()) {
					// notifyUser ("Default Shipping address is clicked",
					// AddShippingAddressActivity.this);
					setasDefault = "true";
				} else {
					setasDefault = "false";
				}
			}
		});

		mDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onFormSubmit();
			}
		});

	}

	private void initViews() {
		titleBarButton = (Button) findViewById(R.id.title_bar_done_icon);
		loadingLayout = (FrameLayout) findViewById(R.id.loadingDialogaddAddress);
		formLayout = (LinearLayout) findViewById(R.id.new_shipping_method_form_layout);
		edtFirstName = (EditText) findViewById(R.id.f_name1);
		edtLastName = (EditText) findViewById(R.id.l_name1);
		edtAddressLine1 = (EditText) findViewById(R.id.al_11);
		edtAddressline2 = (EditText) findViewById(R.id.edtAdress2);
		edtNickName = (EditText) findViewById(R.id.n_name1);
		edtPhone = (EditText) findViewById(R.id.phone1);
		edtZipCode = (EditText) findViewById(R.id.zip_code1);
		edtCity = (EditText) findViewById(R.id.city1);
		spState = (Spinner) findViewById(R.id.spinner1);
		setDefault = (CheckBox) findViewById(R.id.setDefault1);
		Drawable drawable = getResources().getDrawable(
				R.drawable.beauty_pref_check_box);
		drawable.setBounds(0, 0, 60, 60);
		setDefault.setButtonDrawable(android.R.color.transparent);
		setDefault.setCompoundDrawables(drawable, null, null, null);
		setDefault.setCompoundDrawablePadding(15);
		setDefault.setPadding(15, 25, 3, 10);
		mDoneButton = (Button) findViewById(R.id.doneBtn);

		// Error text view
		firstNameErrorText = (TextView) findViewById(R.id.firstNameErrorText);
		lastNameErrorText = (TextView) findViewById(R.id.lastNameErrorText);
		phoneErrorText = (TextView) findViewById(R.id.phoneErrorText);
		address1ErrorText = (TextView) findViewById(R.id.address1ErrorText);
		cityErrorText = (TextView) findViewById(R.id.cityErrorText);
		stateErrorText = (TextView) findViewById(R.id.stateErrorText);
		nickNameErrorText = (TextView) findViewById(R.id.nickNameErrorText);
		zipCodeErrorText = (TextView) findViewById(R.id.zipCodeErrorText);
		originalDrawable = edtFirstName.getBackground();
		// Billing Address
		edtFirstName.addTextChangedListener(this);
		edtLastName.addTextChangedListener(this);
		edtNickName.addTextChangedListener(this);
		edtPhone.addTextChangedListener(this);
		edtAddressLine1.addTextChangedListener(this);
		edtCity.addTextChangedListener(this);
		edtZipCode.addTextChangedListener(this);
	}

	private void invokeStateList() {
		InvokerParams<StateListBean> invokerParams = new InvokerParams<StateListBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.STATE_LIST_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateStatListParameters());
		invokerParams.setUltaBeanClazz(StateListBean.class);
		StatListHandler userCreationHandler = new StatListHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<StatList><invokeStatList><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> populateStatListParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();

		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "2");

		return urlParams;
	}

	/**
	 * The Class StatListHandler.
	 */
	public class StatListHandler extends UltaHandler {
		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<UserCreationHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							AddShippingAddressActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<StatList><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				loadingLayout.setVisibility(View.GONE);
				titleBarButton.setVisibility(View.GONE);
				formLayout.setVisibility(View.VISIBLE);
				stateListBean = (StateListBean) getResponseBean();
				result = stateListBean.getStateList();
				resultNew = new ArrayList<String>();
				resultNew.add("* Select state");
				resultNew.addAll(result);
				setupCitySpinner();
				UltaDataCache.getDataCacheInstance().setStateList(result);
				loadingLayout.setVisibility(View.GONE);
				formLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * Setup city spinner.
	 */
	private void setupCitySpinner() {
		anArrayOfStrings = new String[resultNew.size()];
		resultNew.toArray(anArrayOfStrings);

		MySpinnerAdapter<CharSequence> spinnerArrayAdapter = new MySpinnerAdapter<CharSequence>(
				AddShippingAddressActivity.this, anArrayOfStrings);
		spState.setAdapter(spinnerArrayAdapter);
		spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				stateErrorText.setVisibility(View.GONE);
				((TextView) parentView.getChildAt(0))
						.setTextColor(getResources().getColor(R.color.black));
				((TextView) parentView.getChildAt(0)).setTextSize(12);
				((TextView) parentView.getChildAt(0)).setPadding(5, 0, 0, 0);
				/*
				 * if (position != 0) { isSpinnerSelected = true; }
				 */
				// opcoNamePosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private class MySpinnerAdapter<T> extends ArrayAdapter<T> {
		private Context m_cContext;

		/**
		 * Instantiates a new my spinner adapter.
		 * 
		 * @param context
		 *            the context
		 * @param resource
		 *            the resource
		 * @param textViewResourceId
		 *            the text view resource id
		 * @param result
		 *            the result
		 */
		public MySpinnerAdapter(Context ctx, T[] objects) {
			super(ctx, android.R.layout.simple_spinner_item, objects);
			this.m_cContext = ctx;
		}

		boolean firsttime = true;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (firsttime) {
				firsttime = false;
				// just return some empty view
				TextView tv = new TextView(m_cContext);
				tv.setText("* Select state");
				tv.setTextColor(getResources().getColor(R.color.black));
				tv.setPadding(5, 0, 0, 0);
				return tv;
			}
			// let the array adapter takecare this time
			return super.getView(position, convertView, parent);
		}
	}

	@Override
	public void onDoneClicked() {
		onFormSubmit();

	}

	private void onFormSubmit() {
		getAllValues();
		if (isValidationSuccess()) {
			pd.show();
			invokeAddShippingaddressDetails();
		}
	}

	private void invokeSetasDefaultShippingaddress() {
		pd.show();
		InvokerParams<DefaultShippingAddressBean> invokerParams = new InvokerParams<DefaultShippingAddressBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.SET_AS_DEFAULT_SHIPPING_ADDRESS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populateSetDefaultShippingAddressDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(DefaultShippingAddressBean.class);
		RetreiveSetDefaultShippingaddressDetailsHandler retreiveSetDefaultShippingaddressDetailsHandler = new RetreiveSetDefaultShippingaddressDetailsHandler();
		invokerParams
				.setUltaHandler(retreiveSetDefaultShippingaddressDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<AddShippingAddressActivity><invokeSetasDefaultShippingaddress()><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Populate prefferd shipping address details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populateSetDefaultShippingAddressDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("defaultShippingAddress", strNickName.toUpperCase());

		return urlParams;
	}

	/**
	 * The Class RetrieveMyPreffredShippingAddressDetailsHandler.
	 */
	public class RetreiveSetDefaultShippingaddressDetailsHandler extends
			UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetreiveSetDefaultShippingaddressDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					mInvokeSetasDefaultShippingaddress = true;
					askRelogin(AddShippingAddressActivity.this);
					// finish();
				} else {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								AddShippingAddressActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				Logger.Log("<RetreiveSetDefaultShippingaddressDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				loadingLayout.setVisibility(View.GONE);
				formLayout.setVisibility(View.VISIBLE);
				setResult(RESULT_OK);
				finish();
			}

		}

	}

	private void getAllValues() {
		strFirstName = edtFirstName.getText().toString().trim();
		strLastName = edtLastName.getText().toString().trim();
		strAddressLine1 = edtAddressLine1.getText().toString().trim();
		strAddressLine2 = edtAddressline2.getText().toString().trim();
		state = spState.getSelectedItem().toString();
		if(!state.equalsIgnoreCase("* Select state"))
		{
		state = state.substring(0, 2);
		}
		phone = edtPhone.getText().toString().trim();
		if (phone.length() == 10)
			phone1 = Utility.formatPhoneNumber(phone);
		strZipCode = edtZipCode.getText().toString().trim();
		zipCodeLength = strZipCode.length();
		strCity = edtCity.getText().toString().trim();
		strNickName = edtNickName.getText().toString().trim();

	}

	/**
	 * Checks if is validation success.
	 * 
	 * @return true, if is validation success
	 */
	private boolean isValidationSuccess() {
		if (strFirstName.equalsIgnoreCase("") || strFirstName == null) {
			// notifyUser("Fill the First Name",
			// AddShippingAddressActivity.this);
			setError(edtFirstName, firstNameErrorText, "Fill the First Name");
			edtFirstName.requestFocus();
			return false;
		} else if (strFirstName.startsWith(" ")) {
			// notifyUser("First Name can not start with a space",
			// AddShippingAddressActivity.this);
			setError(edtFirstName, firstNameErrorText,
					"First Name can not start with a space");
			edtFirstName.requestFocus();
			return false;
		}

		else if (strNickName.equalsIgnoreCase("")||strNickName==null) {
			// notifyUser("Nick Name can not start with a space",
			// AddShippingAddressActivity.this);
			setError(edtNickName, nickNameErrorText,
					"Fill the Nickname");
			edtNickName.requestFocus();
			return false;
		}

		else if (strLastName.equalsIgnoreCase("") || strLastName == null) {
			// notifyUser("Fill the Last Name",
			// AddShippingAddressActivity.this);
			setError(edtLastName, lastNameErrorText, "Fill the Last Name");
			edtLastName.requestFocus();
			return false;
		} else if (strLastName.startsWith(" ")) {
			// notifyUser("Last Name can not start with a space",
			// AddShippingAddressActivity.this);
			setError(edtLastName, lastNameErrorText,
					"Last Name can not start with a space");
			edtLastName.requestFocus();
			return false;
		}

		else if (strAddressLine1.equalsIgnoreCase("")
				|| strAddressLine1 == null) {
			// notifyUser("Fill the Address Line 1",
			// AddShippingAddressActivity.this);
			setError(edtAddressLine1, address1ErrorText,
					"Fill the Address Line 1");
			edtAddressLine1.requestFocus();
			return false;
		}
		/*
		 * else if (strAddressLine2.equalsIgnoreCase("") || strAddressLine2 ==
		 * null) { notifyUser("Fill the Address Line 2",
		 * AddShippingAddressActivity.this); edtAddressline2.requestFocus();
		 * return false; }
		 */
		else if (strAddressLine1.startsWith(" ")) {
			// notifyUser("Address Line1 can not start with a space",
			// AddShippingAddressActivity.this);
			setError(edtAddressLine1, address1ErrorText,
					"Address Line1 can not start with a space");
			edtAddressLine1.requestFocus();
			return false;
		} else if (strCity.equalsIgnoreCase("") || strCity == null) {
			// notifyUser("Fill the City", AddShippingAddressActivity.this);
			setError(edtCity, cityErrorText, "Fill the City");
			edtCity.requestFocus();
			return false;
		} else if (strCity.startsWith(" ")) {
			// notifyUser("City can not start with a space",
			// AddShippingAddressActivity.this);
			setError(edtCity, cityErrorText, "City can not start with a space");
			edtCity.requestFocus();
			return false;
		} else if (state.equalsIgnoreCase("") || state == null) {
			// notifyUser("Select the State", AddShippingAddressActivity.this);
			stateErrorText.setText("Select the State");
			stateErrorText.setVisibility(View.VISIBLE);
			spState.requestFocus();
			return false;
		}
		else if (state.equalsIgnoreCase("")
				|| state == null
				|| state.equalsIgnoreCase("* Select state")) {
			stateErrorText.setText("Select the State in Shipping Address");
			stateErrorText.setVisibility(View.VISIBLE);
			spState.requestFocus();
			return false;
		}

		else if (phone1.equalsIgnoreCase("") || phone1 == null
				|| phone1.startsWith(" ") || phone1.length() < 12) {
			// notifyUser("Fill the 10 digit Phone Number ",
			// AddShippingAddressActivity.this);
			setError(edtPhone, phoneErrorText, "Fill the 10 digit Phone Number");
			edtPhone.requestFocus();
			return false;

		} else if (strZipCode.equalsIgnoreCase("") || strZipCode == null
				|| strZipCode.startsWith(" ")) {
			// notifyUser("Fill the Zipcode properly",
			// AddShippingAddressActivity.this);
			setError(edtZipCode, zipCodeErrorText, "Fill the Zip Code properly");
			edtZipCode.requestFocus();
			return false;
		}

		else if (zipCodeLength < 5) {
			// notifyUser("Zip Code cannot be less than 5 letters",
			// AddShippingAddressActivity.this);
			setError(edtZipCode, zipCodeErrorText,
					"Zip Code cannot be less than 5 digits");
			edtZipCode.requestFocus();
			return false;
		} else if (zipCodeLength > 5) {
			// notifyUser("Zip Code cannot be more than 5 letters",
			// AddShippingAddressActivity.this);
			setError(edtZipCode, zipCodeErrorText,
					"Zip Code cannot be more than 5 digits");
			edtZipCode.requestFocus();
			return false;
		}
		return true;

	}

	private void invokeAddShippingaddressDetails() {
		InvokerParams<DefaultShippingAddressBean> invokerParams = new InvokerParams<DefaultShippingAddressBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.ADD_NEW_SHIPPING_ADDRESS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populateShippingAddressDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(DefaultShippingAddressBean.class);
		RetrieveShippingAddressDetailsHandler retrieveShippingAddressDetailsHandler = new RetrieveShippingAddressDetailsHandler();
		invokerParams.setUltaHandler(retrieveShippingAddressDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<AddShippingAddressActivity><invokeSetasDefaultShippingaddress()><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Populate prefferd shipping address details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populateShippingAddressDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("editValue.nickname", strNickName.toUpperCase());
		Logger.Log(">>>>>>NickName in adding address" + strNickName);
		urlParams.put("editValue.firstName", strFirstName);
		urlParams.put("editValue.lastName", strLastName);
		urlParams.put("editValue.address1", strAddressLine1);
		urlParams.put("editValue.address2", strAddressLine2);
		urlParams.put("editValue.city", strCity);
		urlParams.put("editValue.state", state);
		urlParams.put("editValue.phoneNumber", phone1);
		urlParams.put("editValue.postalCode", strZipCode);
		urlParams.put("editValue.country", "US");
		urlParams.put("zip", strZipCode);
		/*
		 * urlParams.put("primaryShippingStatus","pShipping");
		 * urlParams.put("primaryBillingStatus","pBilling");
		 */
		return urlParams;
	}

	/**
	 * The Class RetrieveMyPreffredShippingAddressDetailsHandler.
	 */
	public class RetrieveShippingAddressDetailsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */

		public void handleMessage(Message msg) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			Logger.Log("<RetrieveShippingAddressDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(AddShippingAddressActivity.this);
				} else {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								AddShippingAddressActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				if (setasDefault.equalsIgnoreCase("true")) {
					Logger.Log(">>>>>>>>>>>>> default shipping selected");

					invokeSetasDefaultShippingaddress();
				} else {
					setResult(RESULT_OK);
					finish();

				}
			}

		}

	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			if (mInvokeAddShippingaddressDetails) {
				invokeAddShippingaddressDetails();
			} else if (mInvokeSetasDefaultShippingaddress) {
				invokeSetasDefaultShippingaddress();
			}
		} else {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
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
		if (s.hashCode() == edtFirstName.getText().hashCode()) {
			edtFirstName.setBackgroundDrawable(originalDrawable);
			firstNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtLastName.getText().hashCode()) {
			edtLastName.setBackgroundDrawable(originalDrawable);
			lastNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtNickName.getText().hashCode()) {
			edtNickName.setBackgroundDrawable(originalDrawable);
			nickNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtPhone.getText().hashCode()) {
			edtPhone.setBackgroundDrawable(originalDrawable);
			phoneErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtAddressLine1.getText().hashCode()) {
			edtAddressLine1.setBackgroundDrawable(originalDrawable);
			address1ErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtCity.getText().hashCode()) {
			edtCity.setBackgroundDrawable(originalDrawable);
			cityErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtZipCode.getText().hashCode()) {
			edtZipCode.setBackgroundDrawable(originalDrawable);
			zipCodeErrorText.setVisibility(View.GONE);
		}
		
		changeAllEditTextBackground();
	}
	public void changeAllEditTextBackground(){
		
		changeEditTextBackground(edtFirstName);
		changeEditTextBackground(edtLastName);
		changeEditTextBackground(edtAddressLine1);
		changeEditTextBackground(edtAddressline2);
		changeEditTextBackground(edtNickName);
		changeEditTextBackground(edtPhone);
		changeEditTextBackground(edtZipCode);
		changeEditTextBackground(edtCity);
	}
	

	public void setError(EditText editText, TextView errorTV, String message) {
		errorTV.setText("" + message);
		errorTV.setVisibility(View.VISIBLE);
		changeAllEditTextBackground();
		editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
	}
}
