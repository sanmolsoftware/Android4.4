package com.ulta.core.activity.checkout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.PaymentMethodBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.checkout.GuestUserDataBean;
import com.ulta.core.bean.checkout.StateListBean;
import com.ulta.core.conf.UltaConstants;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddShippingAddressGuestUserActivity extends UltaBaseActivity
		implements OnSessionTimeOut, TextWatcher {

	/**
	 * Shipping Address
	 */

	/** The edit text. */
	EditText edtFirstNameShipping, edtLastNameShipping,
			edtAddressLine1Shipping, edtAddressLine2Shipping,
			edtPhoneNumberShipping, edtZipCodeShipping, edtCityShipping;

	/** The spinner. */
	Spinner spinnerCityShipping;

	/**
	 * Toggle or switch
	 */
	private Switch mSame_as_shipping_address_switch;// billing address same as
													// shipping address switch
	// private Switch mSave_shipping_address_switch;// save shipping address
	// private ToggleButton mSave_shipping_address_toggle_button;

	private LinearLayout mBilling_address_layout;
	TextView mCheckout_shipping, mCheckout_payment, mCheckout_review_order;
	/** The loading layout. */
	FrameLayout loadingLayout;

	/** The form layout. */
	LinearLayout formLayout;

	/** The save shipping for future. */
	// String saveShippingForFuture = "false";

	/** The save shipping as billing. */
	String saveShippingAsBilling = "false";

	/** The state list bean. */
	StateListBean stateListBean;
	private List<String> result, resultNew;
	private String[] anArrayOfStrings;

	/**
	 * Strings for shipping address
	 */

	/** The posofspinneritem. */
	String strFirstName, strLastName, strAddressLine1, strAddressLine2, phone,
			strZipcode, strCity, shippingFirstName, strSelectedState,
			posofspinneritem, strSelectedState1 = "* Select state";
	String phone1 = "";
	/** The zip code length. */
	int zipCodeLength, end = 0;
	/** The formdata. */
	/** The str zip code length. */
	int strZipCodeLengthBilling;

	/**
	 * Navigation to payment type page
	 */
	ImageButton mShippingTypeButton;
	HashMap<String, String> formdata = new HashMap<String, String>();

	/**
	 * Billing address
	 */
	/** The edt city. */
	EditText edtFirstNameBilling, edtLastNameBilling, edtAddressLine1Billing,
			edtAddressLine2Billing, edtPhoneNumberBilling, edtZipCodeBilling,
			edtCityBilling, edtEmail;

	/** The spinner state. */
	private Spinner spinnerStateBilling;

	/** The str city. */
	String strFirstNameBilling, strLastNameBilling, strAddress1Billing,
			strAddress2Billing, phoneBilling, strZipCodeBilling,
			strStateBilling, ctBilling, shippingFirstNameBilling,
			strCityBilling, strState1Billing = "* Select state";
	String phone1Billing = " ";

	int posofspinneritemBilling, endBilling = 0;

	/** The get data. */
	HashMap<String, String> getData = new HashMap<String, String>();

	/**
	 * Get Billing address
	 */

	// private UemAction fetchBillingAddressAction;

	/** The payment method bean. */
	PaymentMethodBean paymentMethodBean;
	/** The credit cards list. */
	// private List<PaymentDetailsBean> creditCardsList;

	// LinearLayout billingFragment, mPrepopultaedBillingAddressLayout,
	// mEnterNewBillingAddressLayout;
	/** The address fragment. */
	// AddressFragment addressFragment;

	// private boolean hasBillingAddressSaved = false;
	// private int id;
	ScrollView sv;

	// RelativeLayout save_shipping_address_switch_relative;
	private HashMap<String, String> guestUserDeatails;
	Gson gson;
	String json;
	private Drawable originalDrawable,billingDrawable;
	private TextView firstNameErrorText, lastNameErrorText, phoneErrorText,
			address1ErrorText, cityErrorText, stateErrorText, zipCodeErrorText,
			firstNameBillingErrorText, lastNameBillingErrorText,
			phoneBillingErrorText, address1BillingErrorText,
			cityBillingErrorText, zipBillingErrorText, stateBillingErrorText;

	@Override
	protected void onResume() {
		super.onResume();
		loadingLayout.setVisibility(View.GONE);
		formLayout.setVisibility(View.VISIBLE);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_shipping_address_guest_user);
		initFooterViews();
		trackAppState(this,
				WebserviceConstants.CHECKOUT_SHIPPING_ADDRESS_GUEST_MEMBER);
		if (!UltaDataCache.getDataCacheInstance().getOrderTotal()
				.equalsIgnoreCase("")) {
			mTotalValueTextView.setText("$"
					+ UltaDataCache.getDataCacheInstance().getOrderTotal());
			mExpandImageView.setVisibility(View.GONE);

		} else {
			mTotalLayout.setVisibility(View.GONE);
		}

		initView();
		if (null != getIntent().getExtras()) {
			edtFirstNameShipping.setText(getIntent().getExtras()
					.get("firstName").toString());
			edtAddressLine1Shipping.setText(getIntent().getExtras()
					.get("address1").toString());
			edtCityShipping.setText(getIntent().getExtras().get("city")
					.toString());
			edtZipCodeShipping.setText(getIntent().getExtras().get("strZip")
					.toString());
		}

		loadingLayout.setVisibility(View.VISIBLE);
		formLayout.setVisibility(View.GONE);
		invokeStateList();

		// invokePaymentMethodDetails();
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
							&& null != guestUserDataBean.getGuestMailId()
							&& null != guestUserDataBean
									.getStrFirstNameShipping()) {
						edtFirstNameShipping.setText(guestUserDataBean
								.getStrFirstNameShipping());
						edtLastNameShipping.setText(guestUserDataBean
								.getStrLastNameShipping());
						edtAddressLine1Shipping.setText(guestUserDataBean
								.getStrAddressLine1Shipping());
						edtAddressLine2Shipping.setText(guestUserDataBean
								.getStrAddressLine2Shipping());
						edtPhoneNumberShipping.setText(guestUserDataBean
								.getStrphoneShipping().replaceAll("-", ""));
						edtZipCodeShipping.setText(guestUserDataBean
								.getStrZipcodeShipping());
						edtCityShipping.setText(guestUserDataBean
								.getStrCityShipping());
						if (null != resultNew) {
							for (int i = 0; i < resultNew.size(); i++) {

								if (resultNew
										.get(i)
										.substring(0, 2)
										.equalsIgnoreCase(
												guestUserDataBean
														.getStrSelectedStateShipping())) {
									spinnerCityShipping.setSelection(i);
								}
							}
						}

						if (guestUserDataBean.getStrsaveShippingAsBilling()
								.equalsIgnoreCase("false")) {
							mSame_as_shipping_address_switch.setChecked(false);
							edtFirstNameBilling.setText(guestUserDataBean
									.getStrFirstNameBilling());
							edtLastNameBilling.setText(guestUserDataBean
									.getStrLastNameBilling());
							edtAddressLine1Billing.setText(guestUserDataBean
									.getStrAddressLine1Billing());
							edtAddressLine2Billing.setText(guestUserDataBean
									.getStrAddressLine2Billing());
							edtPhoneNumberBilling.setText(guestUserDataBean
									.getStrphoneBilling().replaceAll("-", ""));
							edtZipCodeBilling.setText(guestUserDataBean
									.getStrZipcodeBilling());
							edtCityBilling.setText(guestUserDataBean
									.getStrCityBilling());
							if (null != resultNew) {
								for (int i = 0; i < resultNew.size(); i++) {

									if (resultNew
											.get(i)
											.substring(0, 2)
											.equalsIgnoreCase(
													guestUserDataBean
															.getStrSelectedStateBilling())) {
										spinnerStateBilling.setSelection(i);
									}
								}
							}
						} else {
							mSame_as_shipping_address_switch.setChecked(true);
						}
					}
				}

			}
		} catch (JsonSyntaxException e) {
			Log.e("ShippingAddressGuestUser", "Error in JsonParsing");
		}
	}

	private void initView() {
		sv = (ScrollView) findViewById(R.id.scrollview);
		loadingLayout = (FrameLayout) findViewById(R.id.loadingDialog);
		formLayout = (LinearLayout) findViewById(R.id.new_shipping_form_layout);
		edtFirstNameShipping = (EditText) findViewById(R.id.f_name1);
		edtLastNameShipping = (EditText) findViewById(R.id.l_name1);
		edtAddressLine1Shipping = (EditText) findViewById(R.id.al_11);
		edtAddressLine2Shipping = (EditText) findViewById(R.id.edtAdress2);
		edtPhoneNumberShipping = (EditText) findViewById(R.id.phone1);
		edtEmail = (EditText) findViewById(R.id.email);
		String mailId = Utility.retrieveFromSharedPreference(
				UltaConstants.LOGGED_MAIL_ID, this);
		if (mailId != null) {
			edtEmail.setText(mailId);
		}
		edtEmail.setKeyListener(null);
		edtEmail.setFocusable(false);
		edtZipCodeShipping = (EditText) findViewById(R.id.zip_code1);
		edtCityShipping = (EditText) findViewById(R.id.city1);
		spinnerCityShipping = (Spinner) findViewById(R.id.spinner1);
		mShippingTypeButton = (ImageButton) findViewById(R.id.shippingTypeButton);
		mShippingTypeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onFormSubmit();
			}
		});
		// Error text view
		firstNameErrorText = (TextView) findViewById(R.id.firstNameErrorText);
		lastNameErrorText = (TextView) findViewById(R.id.lastNameErrorText);
		phoneErrorText = (TextView) findViewById(R.id.phoneErrorText);
		address1ErrorText = (TextView) findViewById(R.id.address1ErrorText);
		cityErrorText = (TextView) findViewById(R.id.cityErrorText);
		stateErrorText = (TextView) findViewById(R.id.stateErrorText);
		zipCodeErrorText = (TextView) findViewById(R.id.zipCodeErrorText);
		firstNameBillingErrorText = (TextView) findViewById(R.id.firstNameBillingErrorText);
		lastNameBillingErrorText = (TextView) findViewById(R.id.lastNameBillingErrorText);
		phoneBillingErrorText = (TextView) findViewById(R.id.phoneBillingErrorText);
		address1BillingErrorText = (TextView) findViewById(R.id.address1BillingErrorText);
		cityBillingErrorText = (TextView) findViewById(R.id.cityBillingErrorText);
		zipBillingErrorText = (TextView) findViewById(R.id.zipBillingErrorText);
		stateBillingErrorText = (TextView) findViewById(R.id.stateBillingErrorText);
		// Billing Address

		edtFirstNameBilling = (EditText) findViewById(R.id.f_name);
		edtLastNameBilling = (EditText) findViewById(R.id.l_name);
		edtAddressLine1Billing = (EditText) findViewById(R.id.al_1);
		edtAddressLine2Billing = (EditText) findViewById(R.id.al_2);
		edtPhoneNumberBilling = (EditText) findViewById(R.id.al_3);
		edtZipCodeBilling = (EditText) findViewById(R.id.zip_code);
		edtCityBilling = (EditText) findViewById(R.id.city);
		spinnerStateBilling = (Spinner) findViewById(R.id.stateSpinner);
		
		originalDrawable = edtZipCodeShipping.getBackground();
		billingDrawable = edtZipCodeBilling.getBackground();

		edtFirstNameShipping.addTextChangedListener(this);
		edtLastNameShipping.addTextChangedListener(this);
		edtPhoneNumberShipping.addTextChangedListener(this);
		edtAddressLine1Shipping.addTextChangedListener(this);
		edtCityShipping.addTextChangedListener(this);
		edtZipCodeShipping.addTextChangedListener(this);
		edtFirstNameBilling.addTextChangedListener(this);
		edtLastNameBilling.addTextChangedListener(this);
		edtAddressLine1Billing.addTextChangedListener(this);
		edtPhoneNumberBilling.addTextChangedListener(this);
		edtCityBilling.addTextChangedListener(this);
		edtZipCodeBilling.addTextChangedListener(this);

		mBilling_address_layout = (LinearLayout) findViewById(R.id.billing_address_layout);
		mCheckout_shipping = (TextView) findViewById(R.id.checkout_shipping);
		mCheckout_payment = (TextView) findViewById(R.id.checkout_payment);
		mCheckout_review_order = (TextView) findViewById(R.id.checkout_review_order);
		mCheckout_shipping.setBackgroundColor(getResources().getColor(
				R.color.chekout_header_highlighted));
		mCheckout_payment.setBackgroundColor(getResources().getColor(
				R.color.olapic_detail_caption));
		mCheckout_review_order.setBackgroundColor(getResources().getColor(
				R.color.olapic_detail_caption));

		// Billing address Switch click listener
		mSame_as_shipping_address_switch = (Switch) findViewById(R.id.same_as_shipping_address_switch);
		mSame_as_shipping_address_switch.setVisibility(View.VISIBLE);
		mSame_as_shipping_address_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {

							saveShippingAsBilling = "true";
							mBilling_address_layout.setVisibility(View.GONE);

						} else {
							saveShippingAsBilling = "false";
							// if (UltaDataCache.getDataCacheInstance()
							// .isAnonymousCheckout()) {
							mBilling_address_layout.setVisibility(View.VISIBLE);

						}
					}
				});

	}

	/**
	 * Invoke state list.
	 */
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
				notifyUser(Utility.formatDisplayError(getErrorMessage()),
						AddShippingAddressGuestUserActivity.this);
			} else {
				Logger.Log("<StatList><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				loadingLayout.setVisibility(View.GONE);
				formLayout.setVisibility(View.VISIBLE);
				stateListBean = (StateListBean) getResponseBean();
				result = stateListBean.getStateList();
				resultNew = new ArrayList<String>();
				resultNew.add("* Select state");
				resultNew.addAll(result);
				setupCitySpinner();
				prePopulateGuestUserdata();
				UltaDataCache.getDataCacheInstance().setStateList(result);
			}
		}
	}

	/**
	 * Setup city spinner.
	 */
	private void setupCitySpinner() {
		anArrayOfStrings = new String[resultNew.size()];
		resultNew.toArray(anArrayOfStrings);
		MySpinnerAdapter<CharSequence> spinnerSrrayAdapter = new MySpinnerAdapter<CharSequence>(
				AddShippingAddressGuestUserActivity.this, anArrayOfStrings);

		spinnerCityShipping.setAdapter(spinnerSrrayAdapter);
		spinnerCityShipping
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View arg1, int position, long arg3) {
						((TextView) parentView.getChildAt(0))
								.setTextColor(getResources().getColor(
										R.color.black));
						((TextView) parentView.getChildAt(0)).setTextSize(12);
						((TextView) parentView.getChildAt(0)).setPadding(5, 0,
								0, 0);
						if (position != 0) {
							stateErrorText.setVisibility(View.GONE);
							strSelectedState1 = parentView.getItemAtPosition(
									position).toString();
							strSelectedState = strSelectedState1
									.substring(0, 2);
							posofspinneritem = "" + position;

						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinnerStateBilling.setAdapter(spinnerSrrayAdapter);
		spinnerStateBilling
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View arg1, int position, long arg3) {
						((TextView) parentView.getChildAt(0))
								.setTextColor(getResources().getColor(
										R.color.black));
						((TextView) parentView.getChildAt(0)).setTextSize(12);
						((TextView) parentView.getChildAt(0)).setPadding(5, 0,
								0, 0);
						if (position != 0) {
							stateBillingErrorText.setVisibility(View.GONE);
							strState1Billing = parentView.getItemAtPosition(
									position).toString();
							strStateBilling = strState1Billing.substring(0, 2);
							posofspinneritemBilling = position;
						}

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
				tv.setText(anArrayOfStrings[position]);
				tv.setTextColor(getResources().getColor(R.color.black));
				tv.setPadding(5, 0, 0, 0);
				return tv;
			}

			// let the array adapter takecare this time
			return super.getView(position, convertView, parent);
		}
	}

	/**
	 * Gets the all values.
	 * 
	 * @return the all values
	 */
	private void getAllValues() {

		// Shipping Address
		strFirstName = edtFirstNameShipping.getText().toString().trim();
		strLastName = edtLastNameShipping.getText().toString().trim();
		strAddressLine1 = edtAddressLine1Shipping.getText().toString().trim();
		strAddressLine2 = edtAddressLine2Shipping.getText().toString().trim();
		phone = edtPhoneNumberShipping.getText().toString().trim();
		/*
		 * String phone1=phone.substring(0, 3); String phone2=phone.substring(3,
		 * 6); String phone3=phone.substring(6, 10);
		 * phone=phone1+"-"+phone2+"-"+phone3;
		 */
		if (phone.length() == 10) {
			phone1 = Utility.formatPhoneNumber(phone);
		} else {
			phone1 = phone;
		}

		strZipcode = edtZipCodeShipping.getText().toString().trim();
		zipCodeLength = strZipcode.length();
		strCity = edtCityShipping.getText().toString().trim();

		// Billing Address
		if (saveShippingAsBilling.equalsIgnoreCase("false")) {
			strFirstNameBilling = edtFirstNameBilling.getText().toString()
					.trim();
			strLastNameBilling = edtLastNameBilling.getText().toString().trim();
			strAddress1Billing = edtAddressLine1Billing.getText().toString()
					.trim();
			strAddress2Billing = edtAddressLine2Billing.getText().toString()
					.trim();
			phoneBilling = edtPhoneNumberBilling.getText().toString();
			/*
			 * String phone1=phone.substring(0, 3); String
			 * phone2=phone.substring(3, 6); String phone3=phone.substring(6,
			 * 10); phone=phone1+"-"+phone2+"-"+phone3;
			 */
			if (phoneBilling.length() == 10) {
				phone1Billing = Utility.formatPhoneNumber(phoneBilling);
			} else {
				phone1Billing = phoneBilling;
			}
			/*
			 * else{ notifyUser("Please enter 10 digit phone number",
			 * AddNewBillingAddressActivity.this);
			 * edtPhoneNumber.requestFocus(); }
			 */
			strZipCodeBilling = edtZipCodeBilling.getText().toString().trim();
			strZipCodeLengthBilling = strZipCodeBilling.length();
			strCityBilling = edtCityBilling.getText().toString().trim();
		}
	}

	/**
	 * Checks if is validation success.
	 * 
	 * @return true, if is validation success
	 */
	private boolean isValidationSuccess() {
		if (strFirstName.equalsIgnoreCase("") || strFirstName == null) {
			// Toast.makeText(AddNewShippingAddressActivity.this,
			// "Fill the First Name", 2000).show();
			// notifyUser("Fill the First Name in Shipping Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtFirstNameShipping, firstNameErrorText,
					"Fill the First Name in Shipping Address");
			edtFirstNameShipping.requestFocus();
			return false;
		} else if (strFirstName.startsWith(" ")) {
			// Toast.makeText(AddNewShippingAddressActivity.this,
			// "First Name can not start with a space", 2000).show();
			// notifyUser("First Name can not start with a space",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtFirstNameShipping, firstNameErrorText,
					"First Name can not start with a space");
			edtFirstNameShipping.requestFocus();
			return false;
		} else if (strLastName.equalsIgnoreCase("") || strLastName == null) {
			// Toast.makeText(AddNewShippingAddressActivity.this,
			// "Fill the Last Name", 2000).show();
			// notifyUser("Fill the Last Name in Shipping Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtLastNameShipping, lastNameErrorText,
					"Fill the Last Name in Shipping Address");
			edtLastNameShipping.requestFocus();
			return false;
		} else if (strLastName.startsWith(" ")) {
			// notifyUser("Last Name can not start with a space",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtLastNameShipping, lastNameErrorText,
					"Last Name can not start with a space");
			edtLastNameShipping.requestFocus();
			return false;
		} else if (phone1.equalsIgnoreCase("") || phone1 == null
				|| phone1.startsWith(" ") || phone1.length() < 12) {
			// notifyUser("Fill the 10 digit Phone Number in Shipping Address ",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtPhoneNumberShipping, phoneErrorText,
					"Fill the 10 digit Phone Number in Shipping Address ");
			edtPhoneNumberShipping.requestFocus();
			return false;

		} else if (strAddressLine1.equalsIgnoreCase("")
				|| strAddressLine1 == null) {
			// notifyUser("Fill the Address Line 1 in Shipping Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtAddressLine1Shipping, address1ErrorText,
					"Fill the Address Line 1 in Shipping Address ");
			edtAddressLine1Shipping.requestFocus();
			return false;
		} else if (strAddressLine1.startsWith(" ")) {
			// notifyUser("Address Line1 can not start with a space",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtAddressLine1Shipping, address1ErrorText,
					"Address Line1 can not start with a space ");
			edtAddressLine1Shipping.requestFocus();
			return false;
		} else if (strCity.equalsIgnoreCase("") || strCity == null) {
			// notifyUser("Fill the City in Shipping Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtCityShipping, cityErrorText,
					"Fill the City in Shipping Address ");
			edtCityShipping.requestFocus();
			return false;
		} else if (strCity.startsWith(" ")) {
			// notifyUser("City can not start with a space",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtCityShipping, cityErrorText,
					"City can not start with a space ");
			edtCityShipping.requestFocus();
			return false;
		} else if (strSelectedState1.equalsIgnoreCase("")
				|| strSelectedState1 == null
				|| strSelectedState1.equalsIgnoreCase("* Select state")) {
			// notifyUser("Select the State in Shipping Address",
			// AddShippingAddressGuestUserActivity.this);
			stateErrorText.setText("Select the State in Shipping Address");
			stateErrorText.setVisibility(View.VISIBLE);
			spinnerCityShipping.requestFocus();
			// spinnerCityShipping.requestFocus();
			return false;
		}

		else if (strZipcode.equalsIgnoreCase("") || strZipcode == null
				|| strZipcode.startsWith(" ")) {
			// notifyUser("Fill the Zip Code properly in Shipping Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtZipCodeShipping, zipCodeErrorText,
					"Fill the Zip Code properly in Shipping Address ");
			edtZipCodeShipping.requestFocus();
			return false;
		} else if (zipCodeLength < 5) {
			// notifyUser("Zip Code cannot be less than 5 digits",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtZipCodeShipping, zipCodeErrorText,
					"Zip Code cannot be less than 5 digits");
			edtZipCodeShipping.requestFocus();
			return false;
		} else if (zipCodeLength > 5) {
			// notifyUser("Zip Code cannot be more than 5 digits",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtZipCodeShipping, zipCodeErrorText,
					"Zip Code cannot be more than 5 digits");
			edtZipCodeShipping.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Checks if is validation success.
	 * 
	 * @return true, if is validation success
	 */
	private boolean isBillingAddressValidationSuccess() {

		if (strFirstNameBilling.equalsIgnoreCase("")
				|| strFirstNameBilling == null) {
			// notifyUser("Fill the First Name in Billing Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtFirstNameBilling, firstNameBillingErrorText,
					"Fill the First Name in Billing Address");
			edtFirstNameBilling.requestFocus();
			return false;
		} else if (strFirstNameBilling.startsWith(" ")) {
			// notifyUser("First Name can not start with a space",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtFirstNameBilling, firstNameBillingErrorText,
					"First Name can not start with a space");
			edtFirstNameBilling.requestFocus();
			return false;
		} else if (strLastNameBilling.equalsIgnoreCase("")
				|| strLastNameBilling == null) {
			// notifyUser("Fill the Last Name in Billing Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtLastNameBilling, lastNameBillingErrorText,
					"Fill the Last Name in Billing Address");
			edtLastNameBilling.requestFocus();
			return false;
		} else if (strLastNameBilling.startsWith(" ")) {
			// notifyUser("Last Name can not start with a space",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtLastNameBilling, lastNameBillingErrorText,
					"Last Name can not start with a space");
			edtLastNameBilling.requestFocus();
			return false;
		} else if (phone1Billing.equalsIgnoreCase("") || phone1Billing == null
				|| phone1Billing.startsWith(" ") || phone1Billing.length() < 12) {
			// notifyUser("Fill the 10 digit Phone Number in Billing Address ",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtPhoneNumberBilling, phoneBillingErrorText,
					"Fill the 10 digit Phone Number in Billing Address");
			edtPhoneNumberBilling.requestFocus();
			return false;
		} else if (strAddress1Billing.equalsIgnoreCase("")
				|| strAddress1Billing == null) {
			// notifyUser("Fill the Address Line 1 in Billing Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtAddressLine1Billing, address1BillingErrorText,
					"Fill the Address Line 1 in Billing Address");
			edtAddressLine1Billing.requestFocus();
			return false;
		} else if (strAddress1Billing.startsWith(" ")) {
			// notifyUser("Address Line1 can not start with a space",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtAddressLine1Billing, address1BillingErrorText,
					"Address Line1 can not start with a space");
			edtAddressLine1Billing.requestFocus();
			return false;
		} else if (strCityBilling.equalsIgnoreCase("")
				|| strCityBilling == null) {
			// notifyUser("Fill the City in Billing Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtCityBilling, cityBillingErrorText,
					"Fill the City in Billing Address");
			edtCityBilling.requestFocus();

			return false;
		} else if (strSelectedState1.equalsIgnoreCase("")
				|| strSelectedState1 == null
				|| strSelectedState1.equalsIgnoreCase("* Select state")) {
			// notifyUser("Select the State in Billing Address",
			// AddShippingAddressGuestUserActivity.this);
			stateBillingErrorText
					.setText("Select the State in Billing Address");
			stateBillingErrorText.setVisibility(View.VISIBLE);
			spinnerStateBilling.requestFocus();
			return false;
		} else if (strState1Billing.equalsIgnoreCase("")
				|| strState1Billing == null
				|| strState1Billing.equalsIgnoreCase("* Select state")) {
			// notifyUser("Select the State",
			// AddShippingAddressGuestUserActivity.this);
			stateBillingErrorText
					.setText("Select the State in Billing Address");
			stateBillingErrorText.setVisibility(View.VISIBLE);
			spinnerCityShipping.requestFocus();
			spinnerStateBilling.requestFocus();
			return false;
		} else if (strZipCodeBilling.equalsIgnoreCase("")
				|| strZipCodeBilling == null
				|| strZipCodeBilling.startsWith(" ")) {
			// notifyUser("Fill the Zipcode properly in Billing Address",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtZipCodeBilling, zipBillingErrorText,
					"Fill the Zipcode properly in Billing Address");
			edtZipCodeBilling.requestFocus();
			return false;
		} else if (strZipCodeLengthBilling < 5) {
			// notifyUser("Zip Code cannot be less than 5 digits",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtZipCodeBilling, zipBillingErrorText,
					"Zip Code cannot be less than 5 digits");
			edtZipCodeBilling.requestFocus();
			return false;
		} else if (strZipCodeLengthBilling > 5) {
			// notifyUser("Zip Code cannot be more than 5 digits",
			// AddShippingAddressGuestUserActivity.this);
			setError(edtZipCodeBilling, zipBillingErrorText,
					"Zip Code cannot be more than 5 digits");
			edtZipCodeBilling.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	private void saveShippingAddressToCache() {
		formdata.put("postionofspinneritem", Integer.valueOf(posofspinneritem)
				.toString());
		formdata.put("first", strFirstName);
		formdata.put("last", strLastName);
		formdata.put("addressline1", strAddressLine1);
		formdata.put("addressline2", strAddressLine2);

		formdata.put("phone", phone);
		formdata.put("state", strSelectedState);
		formdata.put("stateLocation", posofspinneritem);
		formdata.put("city", strCity);
		formdata.put("zipcode", strZipcode);
		formdata.put("shippingAsBilling", saveShippingAsBilling);
		formdata.put("saveShippingForFuture", "false");
		UltaDataCache.getDataCacheInstance().setShippingAddress(formdata);
	}

	private void saveBillingAddressToCache() {
		getData.put("FirstName", strFirstNameBilling);
		getData.put("LastName", strLastNameBilling);
		getData.put("Address1", strAddress1Billing);
		getData.put("Address2", strAddress2Billing);
		getData.put("Phone", phone1Billing);
		getData.put("ZipCode", strZipCodeBilling);
		getData.put("City", strCityBilling);
		getData.put("State", "" + strStateBilling);
		getData.put("Country", "US");
		UltaDataCache.getDataCacheInstance().setBillingAddress(getData);
	}

	/**
	 * On form submit.
	 */
	private void onFormSubmit() {
		getAllValues();
		if (saveShippingAsBilling.equals("true")) {
			if (isValidationSuccess()) {
				saveShippingAddressToCache();
				loadingLayout.setVisibility(View.VISIBLE);
				formLayout.setVisibility(View.GONE);
				invokeShippment();

			}
		} else {
			if (isValidationSuccess() && isBillingAddressValidationSuccess()) {
				saveShippingAddressToCache();
				saveBillingAddressToCache();
				loadingLayout.setVisibility(View.VISIBLE);
				formLayout.setVisibility(View.GONE);
				invokeShippment();
			}

		}

	}

	/**
	 * Invoke shippment.
	 */
	private void invokeShippment() {
		InvokerParams<CheckoutShippmentMethodBean> invokerParams = new InvokerParams<CheckoutShippmentMethodBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.SHIPPMENT_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateShippmentParameters());
		invokerParams.setUltaBeanClazz(CheckoutShippmentMethodBean.class);
		ShippingAddressHandler userCreationHandler = new ShippingAddressHandler();
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
		urlParams.put("firstName", strFirstName);
		urlParams.put("lastName", strLastName);
		urlParams.put("address1", strAddressLine1);
		urlParams.put("address2", strAddressLine2);
		urlParams.put("city", strCity);
		urlParams.put("country", "US");
		urlParams.put("postalCode", strZipcode);
		urlParams.put("phoneNumber", phone1);
		urlParams.put("state", strSelectedState);
		urlParams.put("useShippingForBilling", saveShippingAsBilling);
		urlParams.put("saveShippingAddress", "false");
		urlParams.put("isReqFromMobile", "true");

		return urlParams;
	}

	/**
	 * The Class ShippingAddressHandler.
	 */
	public class ShippingAddressHandler extends UltaHandler {

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
					askRelogin(AddShippingAddressGuestUserActivity.this);
				} else {
					try {
						notifyUser(getErrorMessage(),
								AddShippingAddressGuestUserActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}

					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);

				}
			} else {
				Logger.Log("<ShippingAddressHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));

				CheckoutShippmentMethodBean ultaBean = (CheckoutShippmentMethodBean) getResponseBean();

				if (null != ultaBean.getComponent()
						.getShippingMethodsAndRedeemLevels()
						.getAvailableShippingMethods()) {
					// String
					// method=ultaBean.getComponent().getShippingMethodsAndRedeemLevels().getAvailableShippingMethods().get(0).getShippingMethod();
					List<String> errors = ultaBean.getErrorInfos();
					if (null != errors && !(errors.isEmpty())) {
						try {
							notifyUser(errors.get(0),
									AddShippingAddressGuestUserActivity.this);
						} catch (WindowManager.BadTokenException e) {
						} catch (Exception e) {
						}
						loadingLayout.setVisibility(View.GONE);
						formLayout.setVisibility(View.VISIBLE);
					} else {
						boolean mobileStatusVertexDAV = ultaBean.getComponent()
								.getMobileStatusVertexDAV();
						if (mobileStatusVertexDAV) {
							performSavingGuestData();
							Intent goToShippingMethod = new Intent(
									AddShippingAddressGuestUserActivity.this,
									ShippingAddressVerification.class);
							goToShippingMethod.putExtra("ShippingMethodBean",
									ultaBean);
							startActivity(goToShippingMethod);
						} else {
							performSavingGuestData();
							Intent goToShippingMethod = new Intent(
									AddShippingAddressGuestUserActivity.this,
									ShippingMethodActivity.class);
							goToShippingMethod.putExtra("ShippingMethodBean",
									ultaBean);
							startActivity(goToShippingMethod);
						}
					}
				} else {
					try {
						notifyUser("Enter Valid details",
								AddShippingAddressGuestUserActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}

					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);
				}
			}

		}
	}

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
						// Shipping details
						guestUserDataBean.setStrFirstNameShipping(formdata
								.get("first"));
						guestUserDataBean.setStrLastNameShipping(formdata
								.get("last"));
						guestUserDataBean.setStrphoneShipping(formdata
								.get("phone"));
						guestUserDataBean.setStrAddressLine1Shipping(formdata
								.get("addressline1"));
						guestUserDataBean.setStrAddressLine2Shipping(formdata
								.get("addressline2"));
						guestUserDataBean.setStrSelectedStateShipping(formdata
								.get("state"));
						guestUserDataBean.setStrCityShipping(formdata
								.get("city"));
						guestUserDataBean.setStrZipcodeShipping(formdata
								.get("zipcode"));
						guestUserDataBean.setStrSaveShippingofFuture(formdata
								.get("saveShippingForFuture"));
						guestUserDataBean.setStrsaveShippingAsBilling(formdata
								.get("shippingAsBilling"));

						// Billing details
						if (formdata.get("shippingAsBilling").equals("true")) {
							guestUserDataBean.setStrFirstNameBilling(formdata
									.get("first"));
							guestUserDataBean.setStrLastNameBilling(formdata
									.get("last"));
							guestUserDataBean.setStrphoneBilling(formdata
									.get("phone"));
							guestUserDataBean
									.setStrAddressLine1Billing(formdata
											.get("strAddressLine1"));
							guestUserDataBean
									.setStrAddressLine2Billing(formdata
											.get("strAddressLine2"));
							guestUserDataBean
									.setStrSelectedStateBilling(formdata
											.get("state"));
							guestUserDataBean.setStrCityBilling(formdata
									.get("city"));
							guestUserDataBean.setStrZipcodeBilling(formdata
									.get("zipcode"));
						} else {
							if (null != UltaDataCache.getDataCacheInstance()
									.getBillingAddress()) {
								HashMap<String, String> billingdata = new HashMap<String, String>();
								billingdata = UltaDataCache
										.getDataCacheInstance()
										.getBillingAddress();

								guestUserDataBean
										.setStrFirstNameBilling(billingdata
												.get("FirstName"));
								guestUserDataBean
										.setStrLastNameBilling(billingdata
												.get("LastName"));
								guestUserDataBean
										.setStrphoneBilling(billingdata
												.get("Phone"));
								guestUserDataBean
										.setStrAddressLine1Billing(billingdata
												.get("Address1"));
								guestUserDataBean
										.setStrAddressLine2Billing(billingdata
												.get("Address2"));
								guestUserDataBean
										.setStrSelectedStateBilling(billingdata
												.get("State").substring(0, 2));
								guestUserDataBean.setStrCityBilling(billingdata
										.get("City"));
								guestUserDataBean
										.setStrZipcodeBilling(billingdata
												.get("ZipCode"));
							}

						}
					}

					gson = new Gson();
					json = gson.toJson(guestUserDataBean);
					guestUserDeatails.put("guest", json);
					UltaDataCache.getDataCacheInstance().setGuestUserDeatails(
							guestUserDeatails);
				}
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			Log.e("ShippingAddressGuestUser", "Error in JsonParsing");
		}
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {
		if (isSuccess) {
			// invokeShippment();
			navigateToBasketOnSessionTimeout(AddShippingAddressGuestUserActivity.this);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void afterTextChanged(Editable s) {

		if (s.hashCode() == edtFirstNameShipping.getText().hashCode()) {
			edtFirstNameShipping.setBackgroundDrawable(originalDrawable);
			firstNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtLastNameShipping.getText().hashCode()) {
			edtLastNameShipping.setBackgroundDrawable(originalDrawable);
			lastNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtPhoneNumberShipping.getText().hashCode()) {
			edtPhoneNumberShipping.setBackgroundDrawable(originalDrawable);
			phoneErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtAddressLine1Shipping.getText().hashCode()) {
			edtAddressLine1Shipping.setBackgroundDrawable(originalDrawable);
			address1ErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtCityShipping.getText().hashCode()) {
			edtCityShipping.setBackgroundDrawable(originalDrawable);
			cityErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtZipCodeShipping.getText().hashCode()) {
			edtZipCodeShipping.setBackgroundDrawable(originalDrawable);
			zipCodeErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtFirstNameBilling.getText().hashCode()) {
			edtFirstNameBilling.setBackgroundDrawable(billingDrawable);
			firstNameBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtLastNameBilling.getText().hashCode()) {
			edtLastNameBilling.setBackgroundDrawable(billingDrawable);
			lastNameBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtAddressLine1Billing.getText().hashCode()) {
			edtAddressLine1Billing.setBackgroundDrawable(billingDrawable);
			address1BillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtPhoneNumberBilling.getText().hashCode()) {
			edtPhoneNumberBilling.setBackgroundDrawable(billingDrawable);
			phoneBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtCityBilling.getText().hashCode()) {
			edtCityBilling.setBackgroundDrawable(billingDrawable);
			cityBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtZipCodeBilling.getText().hashCode()) {
			edtZipCodeBilling.setBackgroundDrawable(billingDrawable);
			zipBillingErrorText.setVisibility(View.GONE);
		}
		
		changeAllEditTextBackground();

	}
	
	public void changeAllEditTextBackground(){
		changeEditTextBackground(edtFirstNameShipping);
		changeEditTextBackground(edtLastNameShipping);
		changeEditTextBackground(edtPhoneNumberShipping);
		changeEditTextBackground(edtAddressLine1Shipping);
		changeEditTextBackground(edtAddressLine2Shipping);
		changeEditTextBackground(edtCityShipping);
		changeEditTextBackground(edtZipCodeShipping);
		changeEditTextBackground(edtFirstNameBilling);
		changeEditTextBackground(edtLastNameBilling);
		changeEditTextBackground(edtAddressLine1Billing);
		changeEditTextBackground(edtAddressLine2Billing);
		changeEditTextBackground(edtCityBilling);
		changeEditTextBackground(edtZipCodeBilling);

	}

	public void setError(EditText editText, TextView errorTV, String message) {
		errorTV.setText("" + message);
		errorTV.setVisibility(View.VISIBLE);
		changeAllEditTextBackground();
		editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
	}
	@Override
	public void onBackPressed() {
		UltaDataCache.getDataCacheInstance().setAnonymousCheckout(
				false);
		finish();
	}
}
