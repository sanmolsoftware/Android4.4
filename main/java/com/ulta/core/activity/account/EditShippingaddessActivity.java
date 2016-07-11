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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.DefaultShippingAddressBean;
import com.ulta.core.bean.checkout.AddressBean;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

public class EditShippingaddessActivity extends UltaBaseActivity implements
		OnDoneClickedListener, OnSessionTimeOut {
	/** The edt city. */
	EditText edtFirstName, edtLastName, edtAddressLine1, edtAddressLine2,
			edtNickName, edtPhone, edtZipCode, edtCity;

	/** The spinner city. */
	Spinner spinnerCity;
	private ProgressDialog pd;

	private List<String> result;

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

	/** The button submit. */
	Button titleBarButton;

	/** The form layout. */
	LinearLayout formLayout;

	/** The loading layout. */
	FrameLayout loadingLayout;

	CheckBox setDefault;

	AddressBean addressToEdit;

	String defaultShippingId;
	private Button mDoneButton;

	private boolean mStateListHandler;
	private boolean minvokeSetasDefaultShippingaddress;

	/*
	 * @Override protected void onResume() {
	 * if(!isUltaCustomer(EditShippingaddessActivity.this)){ finish(); }
	 * super.onResume(); }
	 */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_shipping_address);
		setTitle("Shipping Address");
		initViews();

		addressToEdit = (AddressBean) getIntent().getExtras().get(
				"AddressToEdit");
		if (getIntent().getExtras().containsKey("defaultShippingId")) {
			defaultShippingId = getIntent().getExtras().getString(
					"defaultShippingId");
			if (defaultShippingId.equals(addressToEdit.getId())) {
				setDefault.setChecked(true);
			}
		}
		populateForm();
		invokeStateList();

		pd = new ProgressDialog(EditShippingaddessActivity.this);
		setProgressDialogLoadingColor(pd);
		pd.setMessage(LOADING_PROGRESS_TEXT);
		pd.setCancelable(false);

		setDefault.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg0.isChecked()) {
					// notifyUser ("c2 is clicked",
					// EditShippingaddessActivity.this);
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

	private void populateForm() {
		String phnum;
		if (null != addressToEdit.getPhoneNumber()) {
			String[] phones = addressToEdit.getPhoneNumber().split("-");
			phnum = phones[0];
			for (int i = 1; i < phones.length; i++) {
				phnum = phnum + phones[i];
			}
			edtPhone.setText(phnum);
		}
		edtFirstName.setText(addressToEdit.getFirstName());
		edtLastName.setText(addressToEdit.getLastName());
		edtAddressLine1.setText(addressToEdit.getAddress1());
		edtAddressLine2.setText(addressToEdit.getAddress2());
		edtNickName.setText(addressToEdit.getNickName());
		edtZipCode.setText(addressToEdit.getPostalCode());
		edtCity.setText(addressToEdit.getCity());
		/* edtCountry.setText(addressToEdit.getCountry()); */
	}

	private void initViews() {
		titleBarButton = (Button) findViewById(R.id.title_bar_done_icon);
		loadingLayout = (FrameLayout) findViewById(R.id.loadingDialogaddAddress);
		formLayout = (LinearLayout) findViewById(R.id.new_shipping_method_form_layout);
		edtFirstName = (EditText) findViewById(R.id.f_name1);
		edtLastName = (EditText) findViewById(R.id.l_name1);
		edtAddressLine1 = (EditText) findViewById(R.id.al_11);
		edtAddressLine2 = (EditText) findViewById(R.id.edtAdress2);
		edtNickName = (EditText) findViewById(R.id.n_name1);
		edtPhone = (EditText) findViewById(R.id.phone1);
		edtZipCode = (EditText) findViewById(R.id.zip_code1);
		edtCity = (EditText) findViewById(R.id.city1);
		/* edtCountry = (EditText) findViewById(R.id.country1); */
		spinnerCity = (Spinner) findViewById(R.id.spinner1);
		setDefault = (CheckBox) findViewById(R.id.setDefault1);
		Drawable drawable = getResources().getDrawable(
				R.drawable.beauty_pref_check_box);
		drawable.setBounds(0, 0, 60, 60);
		setDefault.setButtonDrawable(android.R.color.transparent);
		setDefault.setCompoundDrawables(drawable, null, null, null);
		setDefault.setCompoundDrawablePadding(15);
		setDefault.setPadding(15, 25, 3, 10);
		mDoneButton = (Button) findViewById(R.id.doneBtn);
	}

	private void invokeStateList() {

		/*
		 * result = UltaDataCache.getDataCacheInstance().getStateList();
		 * if(result == null){
		 */
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
		/*
		 * }else{ loadingLayout.setVisibility(View.GONE);
		 * formLayout.setVisibility(View.VISIBLE); setupCitySpinner(); }
		 */
	}

	/**
	 * Method to populate the URL parameter map.
	 *
	 * @return Map<String, String>
	 **/
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
				if (getErrorMessage().startsWith("401")) {
					mStateListHandler = true;
					askRelogin(EditShippingaddessActivity.this);
				} else {
					try {

						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								EditShippingaddessActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}

				}
			} else {
				Logger.Log("<StatList><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				loadingLayout.setVisibility(View.GONE);
				titleBarButton.setVisibility(View.GONE);
				formLayout.setVisibility(View.VISIBLE);
				stateListBean = (StateListBean) getResponseBean();
				result = stateListBean.getStateList();
				setupCitySpinner();
				UltaDataCache.getDataCacheInstance().setStateList(result);
				Logger.Log(">>>> state passed" + addressToEdit.getState());
				for (int i = 0; i < result.size(); i++) {
					Logger.Log(">>>> state from list" + result.get(i));
					if (result.get(i).startsWith(addressToEdit.getState())) {
						spinnerCity.setSelection(i);
					}
				}
			}
		}
	}

	private class MySpinnerAdapter extends ArrayAdapter {

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
		@SuppressWarnings("unchecked")
		public MySpinnerAdapter(Context context, int resource,
								int textViewResourceId, List<String> result) {

			super(context, resource, textViewResourceId, result);
		}
	}

	/**
	 * Setup city spinner.
	 */
	private void setupCitySpinner() {
		/*
		 * ArrayAdapter<CharSequence>
		 * adapter_themes=ArrayAdapter.createFromResource(this, R.array.city,
		 * android.R.layout.simple_spinner_item);
		 * adapter_themes.setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item);
		 * spinnerCity.setAdapter(adapter_themes);
		 */
		/*
		 * ArrayAdapter<String> adapter= new
		 * ArrayAdapter<String>(AddNewShippingAddressActivity.this,
		 * android.R.layout.simple_spinner_item, result);
		 * adapter.setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item);
		 * spinnerCity.setAdapter(adapter);
		 */

		MySpinnerAdapter spinnerSrrayAdapter = new MySpinnerAdapter(
				EditShippingaddessActivity.this, R.layout.state_list,
				R.id.textState, result);

		spinnerCity.setAdapter(spinnerSrrayAdapter);

		spinnerCity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				strSelectedState = arg0.getItemAtPosition(arg2).toString();
				// strSelectedState=strSelectedState.substring(0, 2);
				posofspinneritem = "" + arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	@Override
	public void onDoneClicked() {
		onFormSubmit();

	}

	private void onFormSubmit() {
		getAllValues();
		if (isValidationSuccess()) {

			invokeEditShippingaddressDetails();
		}
	}

	private void invokeSetasDefaultShippingaddress() {
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
			Logger.Log("<EditShippingAddressActivity><invokeSetasDefaultShippingaddress()><UltaException>>"
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
		urlParams.put("defaultShippingAddress", strNickName);
		// urlParams.put("defaultShippingAddress","Home(primary billing)");

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
					// finish();
					minvokeSetasDefaultShippingaddress = true;
					askRelogin(EditShippingaddessActivity.this);
				} else {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								EditShippingaddessActivity.this);
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
		strAddressLine2 = edtAddressLine2.getText().toString().trim();

		phone = edtPhone.getText().toString().trim();

		/*
		 * String phone1=phone.substring(0, 3); String phone2=phone.substring(3,
		 * 6); String phone3=phone.substring(6, 10);
		 * phone=phone1+"-"+phone2+"-"+phone3;
		 */
		if (phone.length() == 10)
			phone1 = Utility.formatPhoneNumber(phone);
		/*
		 * else{ notifyUser("Please enter 10 digit phone number",
		 * AddNewShippingAddressActivity.this); edtAddressLine3.requestFocus();
		 * }
		 */
		strZipCode = edtZipCode.getText().toString().trim();
		zipCodeLength = strZipCode.length();
		strCity = edtCity.getText().toString().trim();
		strNickName = edtNickName.getText().toString().trim();
		/* strCountry = edtCountry.getText().toString().trim(); */

	}

	/**
	 * Checks if is validation success.
	 *
	 * @return true, if is validation success
	 */
	private boolean isValidationSuccess() {
		if (strFirstName == null || strFirstName.equalsIgnoreCase("")) {
			// Toast.makeText(AddNewShippingAddressActivity.this,
			// "Fill the First Name", 2000).show();
			notifyUser("Fill the First Name", EditShippingaddessActivity.this);
			edtFirstName.requestFocus();
			return false;
		} else if (strFirstName.startsWith(" ")) {
			// Toast.makeText(AddShippingAddressActivity.this,
			// "First Name can not start with a space", 2000).show();
			notifyUser("First Name can not start with a space",
					EditShippingaddessActivity.this);
			edtFirstName.requestFocus();
			return false;
		} else if (strLastName == null || strLastName.equalsIgnoreCase("")) {
			// Toast.makeText(AddShippingAddressActivity.this,
			// "Fill the Last Name", 2000).show();
			notifyUser("Fill the Last Name", EditShippingaddessActivity.this);
			edtLastName.requestFocus();
			return false;
		} else if (strLastName.startsWith(" ")) {
			notifyUser("Last Name can not start with a space",
					EditShippingaddessActivity.this);
			edtLastName.requestFocus();
			return false;
		} else if (strNickName == null || strNickName.equalsIgnoreCase("")) {
			// Toast.makeText(AddShippingAddressActivity.this,
			// "Fill the Last Name", 2000).show();
			notifyUser("Fill the Nickname", EditShippingaddessActivity.this);
			edtNickName.requestFocus();
			return false;
		} else if (strNickName.startsWith(" ")) {
			notifyUser("Nickname can not start with a space",
					EditShippingaddessActivity.this);
			edtNickName.requestFocus();
			return false;
		} else if (phone1 == null || phone1.equalsIgnoreCase("")
				|| phone1.startsWith(" ") || phone1.length() < 12) {
			notifyUser("Fill the 10 digit Phone Number ",
					EditShippingaddessActivity.this);
			edtPhone.requestFocus();
			return false;

		} else if (strAddressLine1 == null
				|| strAddressLine1.equalsIgnoreCase("")) {
			notifyUser("Fill the Address Line 1",
					EditShippingaddessActivity.this);
			edtAddressLine1.requestFocus();
			return false;
		} else if (strAddressLine1.startsWith(" ")) {
			notifyUser("Address Line1 can not start with a space",
					EditShippingaddessActivity.this);
			edtAddressLine1.requestFocus();
			return false;
		}

		/*
		 * addr2 is optional else if (strAddressLine2.equalsIgnoreCase("") ||
		 * strAddressLine2 == null) { notifyUser("Fill the Address Line 2",
		 * EditShippingaddessActivity.this); edtAddressLine2.requestFocus();
		 * return false; }
		 */

		else if (strAddressLine2.startsWith(" ")) {
			notifyUser("Address Line2 can not start with a space",
					EditShippingaddessActivity.this);
			edtAddressLine2.requestFocus();
			return false;
		}
		/*
		 * else if(strAddressLine2.equalsIgnoreCase("") || strAddressLine2==null
		 * || strAddressLine2.startsWith(" ")){
		 * Toast.makeText(AddShippingAddressActivity.this,
		 * "Fill the Address Line 2", 2000).show(); return false; }
		 */
		/*
		 * else if(!validatePhoneNumber()){ notifyUser
		 * ("Please enter phone number in the format XXX-XXX-XXXX ",
		 * AddShippingAddressActivity.this); edtAddressLine3.requestFocus();
		 * return false; }
		 */
		else if (strSelectedState == null
				|| strSelectedState.equalsIgnoreCase("")) {
			notifyUser("Select the State", EditShippingaddessActivity.this);
			spinnerCity.requestFocus();
			return false;
		} else if (strCity == null || strCity.equalsIgnoreCase("")) {
			notifyUser("Fill the City", EditShippingaddessActivity.this);
			edtCity.requestFocus();
			return false;
		} else if (strCity.startsWith(" ")) {
			notifyUser("City can not start with a space",
					EditShippingaddessActivity.this);
			edtCity.requestFocus();
			return false;
		} else if (strZipCode == null || strZipCode.equalsIgnoreCase("")
				|| strZipCode.startsWith(" ")) {
			notifyUser("Fill the Zipcode properly",
					EditShippingaddessActivity.this);
			edtZipCode.requestFocus();
			return false;
		} else if (zipCodeLength < 5) {
			notifyUser("Zip Code cannot be less than 5 letters",
					EditShippingaddessActivity.this);
			edtZipCode.requestFocus();
			return false;
		} else if (zipCodeLength > 5) {
			notifyUser("Zip Code cannot be more than 5 letters",
					EditShippingaddessActivity.this);
			edtZipCode.requestFocus();
			return false;
		}
		/*
		 * else if(strCountry.equalsIgnoreCase("") || strCountry==null ){
		 * notifyUser ("Fill the Country", EditShippingaddessActivity.this);
		 * edtCountry.requestFocus(); return false; } else
		 * if(strCountry.startsWith(" ")){ notifyUser
		 * ("Country can not start with a space",
		 * EditShippingaddessActivity.this); edtCountry.requestFocus(); return
		 * false; }
		 */
		return true;

	}

	private void invokeEditShippingaddressDetails() {
		pd.show();
		InvokerParams<DefaultShippingAddressBean> invokerParams = new InvokerParams<DefaultShippingAddressBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.EDIT_SHIPPING_ADDRESS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populateEditShippingAddressDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(DefaultShippingAddressBean.class);
		RetrieveEditShippingAddressDetailsHandler retrieveEditShippingAddressDetailsHandler = new RetrieveEditShippingAddressDetailsHandler();
		invokerParams.setUltaHandler(retrieveEditShippingAddressDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<EditShippingAddressActivity><invokeEditShippingaddressDetails()><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Populate prefferd shipping address details handler parameters.
	 *
	 * @return the map
	 */
	private Map<String, String> populateEditShippingAddressDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("editValue.nickname", addressToEdit.getNickName());
		urlParams.put("editValue.firstName", strFirstName);
		urlParams.put("editValue.lastName", strLastName);
		urlParams.put("editValue.address1", strAddressLine1);
		urlParams.put("editValue.address2", strAddressLine2);
		urlParams.put("editValue.city", strCity);
		urlParams.put("editValue.state", strSelectedState);
		urlParams.put("editValue.phoneNumber", phone1);
		urlParams.put("editValue.postalCode", strZipCode);
		urlParams.put("editValue.country", strCountry);
		urlParams.put("editValue.newNickname", strNickName.toUpperCase());
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
	public class RetrieveEditShippingAddressDetailsHandler extends UltaHandler {

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
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							EditShippingaddessActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				if (setasDefault.equalsIgnoreCase("true")) {
					pd.show();
					invokeSetasDefaultShippingaddress();
				} else {
					finish();
				}

				Logger.Log("<RetrieveShippingAddressDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
			}

		}

	}

	@Override
	protected void refreshPage() {

	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			if (mStateListHandler) {
				invokeStateList();
			} else if (minvokeSetasDefaultShippingaddress) {
				invokeSetasDefaultShippingaddress();
			}
		} else {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
		}
	}
}
