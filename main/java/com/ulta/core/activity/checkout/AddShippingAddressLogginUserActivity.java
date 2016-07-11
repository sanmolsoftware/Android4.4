package com.ulta.core.activity.checkout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.PaymentMethodBean;
import com.ulta.core.bean.checkout.AddressBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.checkout.PaymentDetailsBean;
import com.ulta.core.bean.checkout.ShippingAddressesBean;
import com.ulta.core.bean.checkout.StateListBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.fragments.checkout.ShippingAddressFragment;
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

//import com.compuware.apm.uem.mobile.android.UemAction;

public class AddShippingAddressLogginUserActivity extends UltaBaseActivity
		implements OnSessionTimeOut, TextWatcher {
	/** The loading layout. */
	FrameLayout loadingLayout;
	// private UemAction fetchShippingAddressAction, addshippingAddressAction;
	/** The address fragment. */
	ShippingAddressFragment addressFragmentForShipping;
	/** The add new address. */
	LinearLayout addNewAddress, fragment;
	private ProgressDialog pd;// Prgoress dialog
	/** The form layout. */
	LinearLayout formLayout;

	/** The edit text. */
	EditText edtFirstNameShipping, edtLastNameShipping,
			edtAddressLine1Shipping, edtAddressLine2Shipping,
			edtPhoneNumberShipping, edtZipCodeShipping, edtCityShipping,
			edtEmail;
	String phone1 = "";
	/** The zip code length. */
	int zipCodeLength, end = 0;

	/** The payment method bean. */
	ShippingAddressesBean shippingAddressesBean;
	private List<AddressBean> shippingaddressesList;
	boolean flag = false;
	int count = 0;
	LinearLayout mShipping_address_layout;// layout containg add shipping
											// address
	RelativeLayout mEnterNewShippingAddressLayout;
	private boolean hasShippingAddressSaved = false;
	/**
	 * Toggle or switch
	 */
	private Switch mSame_as_shipping_address_switch;// billing address same as
	// shipping address switch
	private Switch mSave_shipping_address_switch;// save shipping address
	private Switch mEnter_new_shipping_address_switch;
	private Switch mEnter_new_billing_address_switch;

	/** The save shipping for future. */
	String saveShippingForFuture = "true";

	/** The save shipping as billing. */
	String saveShippingAsBilling = "false";
	private LinearLayout mBilling_address_layout;
	LinearLayout billingFragment, mPrepopultaedBillingAddressLayout;
	RelativeLayout mEnterNewBillingAddressLayout;
	private boolean hasBillingAddressSaved = false;
	/** The state list bean. */
	StateListBean stateListBean;
	private List<String> result, resultNew;
	private String[] anArrayOfStrings;
	/** The spinner. */
	Spinner spinnerCityShipping;
	/** The str zip code length. */
	int strZipCodeLengthBilling;

	/**
	 * Strings for shipping address
	 */

	/** The posofspinneritem. */
	String strFirstName, strLastName, strAddressLine1, strAddressLine2, phone,
			strZipcode, strCity, shippingFirstName, strSelectedState,
			posofspinneritem, strSelectedState1 = "* Select state";

	/** The spinner state. */
	private Spinner spinnerStateBilling;

	/**
	 * Billing address
	 */
	/** The str city. */
	String strFirstNameBilling, strLastNameBilling, strAddress1Billing,
			strAddress2Billing, phoneBilling, strZipCodeBilling,
			strStateBilling, ctBilling, shippingFirstNameBilling,
			strCityBilling, strState1Billing = "* Select state";

	int posofspinneritemBilling, endBilling = 0;
	private int id, idShipping;

	/**
	 * Billing address
	 */
	/** The edt city. */
	EditText edtFirstNameBilling, edtLastNameBilling, edtAddressLine1Billing,
			edtAddressLine2Billing, edtPhoneNumberBilling, edtZipCodeBilling,
			edtCityBilling;
	String phone1Billing = " ";

	/**
	 * Navigation to payment type page
	 */
	ImageButton mShippingTypeButton;

	/** The get data. */
	HashMap<String, String> getData = new HashMap<String, String>();
	HashMap<String, String> formdata = new HashMap<String, String>();
	/**
	 * Get Billing address
	 */

	/** The payment method bean. */
	PaymentMethodBean paymentMethodBean;
	/** The credit cards list. */
	private List<PaymentDetailsBean> creditCardsList;
	/** The address fragment. */
	ShippingAddressFragment addressFragment;
	TextView mCheckout_shipping, mCheckout_payment, mCheckout_review_order;
	ScrollView sv;
	boolean onPause = false;
	private boolean mEnterNewShipping = false;
	private boolean mEnterNewBilling = false;
	private SharedPreferences staySignedInSharedPreferences;
	private Drawable originalDrawable;
	private TextView firstNameErrorText, lastNameErrorText, phoneErrorText,
			address1ErrorText, cityErrorText, stateErrorText, zipCodeErrorText,
			firstNameBillingErrorText, lastNameBillingErrorText,
			phoneBillingErrorText, address1BillingErrorText,
			cityBillingErrorText, zipBillingErrorText, stateBillingErrorText;

	@Override
	protected void onResume() {
		super.onResume();
		if (onPause) {
			loadingLayout.setVisibility(View.GONE);
			formLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		onPause = true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_shipping_address_loggedin_user);
		setTitle("Shipping Address");
		if (Utility.retrieveBooleanFromSharedPreference(
				UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
				getApplicationContext())) {
			trackAppState(
					this,
					WebserviceConstants.CHECKOUT_SHIPPING_ADDRESS_LOYALITY_MEMBER);
		} else {
			trackAppState(
					this,
					WebserviceConstants.CHECKOUT_SHIPPING_ADDRESS_NON_LOYALITY_MEMBER);
		}

		pd = new ProgressDialog(AddShippingAddressLogginUserActivity.this);
		setProgressDialogLoadingColor(pd);
		pd.setCancelable(false);
		initFooterViews();
		initViews();

		if (!UltaDataCache.getDataCacheInstance().getOrderTotal()
				.equalsIgnoreCase("")) {
			mTotalValueTextView.setText("$"
					+ UltaDataCache.getDataCacheInstance().getOrderTotal());
			mExpandImageView.setVisibility(View.GONE);

		} else {
			mTotalLayout.setVisibility(View.GONE);
		}
		loadingLayout.setVisibility(View.VISIBLE);
		formLayout.setVisibility(View.GONE);
		invokeShippingaddressesDetails();
		invokeStateList();
		invokePaymentMethodDetails();

	}

	private void initViews() {
		sv = (ScrollView) findViewById(R.id.scrollview);
		edtEmail = (EditText) findViewById(R.id.email);
		String mailId = getRegisteredMailId(
				AddShippingAddressLogginUserActivity.this).trim();
		if (mailId != null && mailId.length() != 0) {
			edtEmail.setText(""
					+ getRegisteredMailId(AddShippingAddressLogginUserActivity.this));
		} else {
			staySignedInSharedPreferences = getSharedPreferences(
					WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);
			String userName = staySignedInSharedPreferences.getString(
					WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ").trim();
			String secretKey = staySignedInSharedPreferences.getString(
					WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
			userName = Utility.decryptString(userName, secretKey);
			if (userName != null && userName.length() != 0) {
				edtEmail.setText(userName);
			}
		}
		edtEmail.setKeyListener(null);
		edtEmail.setFocusable(false);
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
		addressFragmentForShipping = (ShippingAddressFragment) getSupportFragmentManager()
				.findFragmentById(R.id.addressFragmentForShipping);
		loadingLayout = (FrameLayout) findViewById(R.id.loadingDialog);
		formLayout = (LinearLayout) findViewById(R.id.new_shipping_form_layout);
		// noAddressList = (LinearLayout) findViewById(R.id.noAddressList);
		fragment = (LinearLayout) findViewById(R.id.linearFragment);
		billingFragment = (LinearLayout) findViewById(R.id.linearBillingFragment);
		spinnerCityShipping = (Spinner) findViewById(R.id.spinner1);
		addressFragment = (ShippingAddressFragment) getSupportFragmentManager()
				.findFragmentById(R.id.addressFragment);
		spinnerStateBilling = (Spinner) findViewById(R.id.stateSpinner);
		mPrepopultaedBillingAddressLayout = (LinearLayout) findViewById(R.id.prepopultaedBillingAddressLayout);
		mEnterNewBillingAddressLayout = (RelativeLayout) findViewById(R.id.enterNewBillingAddressLayout);
		mBilling_address_layout = (LinearLayout) findViewById(R.id.billing_address_layout);
		mShipping_address_layout = (LinearLayout) findViewById(R.id.shipping_address_layout);
		mEnterNewShippingAddressLayout = (RelativeLayout) findViewById(R.id.enterNewShippingAddressLayout);

		mEnter_new_billing_address_switch = (Switch) findViewById(R.id.enter_new_billing_address_switch);
		mEnter_new_shipping_address_switch = (Switch) findViewById(R.id.enter_new_shipping_address_switch);
		mSame_as_shipping_address_switch = (Switch) findViewById(R.id.same_as_shipping_address_switch);
		mSave_shipping_address_switch = (Switch) findViewById(R.id.save_shipping_address_switch);
		mSame_as_shipping_address_switch.setVisibility(View.VISIBLE);
		mSame_as_shipping_address_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {

							saveShippingAsBilling = "true";
							mBilling_address_layout.setVisibility(View.GONE);
							mEnterNewBillingAddressLayout
									.setVisibility(View.GONE);
							mPrepopultaedBillingAddressLayout
									.setVisibility(View.GONE);

						} else {
							saveShippingAsBilling = "false";

							if (hasBillingAddressSaved && mEnterNewBilling) {
								mPrepopultaedBillingAddressLayout
										.setVisibility(View.VISIBLE);
								mEnterNewBillingAddressLayout
										.setVisibility(View.VISIBLE);
								mBilling_address_layout
										.setVisibility(View.VISIBLE);
							} else if (hasBillingAddressSaved
									&& !mEnterNewBilling) {
								mPrepopultaedBillingAddressLayout
										.setVisibility(View.VISIBLE);
								mEnterNewBillingAddressLayout
										.setVisibility(View.VISIBLE);
							} else {
								mBilling_address_layout
										.setVisibility(View.VISIBLE);
							}

						}
					}
				});

		mSave_shipping_address_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg0.isChecked()) {
							saveShippingForFuture = "true";
						} else {
							saveShippingForFuture = "false";
						}
					}
				});

		mEnter_new_shipping_address_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg0.isChecked()) {
							mShipping_address_layout
									.setVisibility(View.VISIBLE);
							mEnterNewShipping = true;
							edtFirstNameShipping.requestFocus();
							// mShipping_address_layout.requestFocus();

						} else {
							mShipping_address_layout.setVisibility(View.GONE);
							mEnterNewShipping = false;
						}
					}
				});
		mEnter_new_billing_address_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg0.isChecked()) {
							mBilling_address_layout.setVisibility(View.VISIBLE);
							mEnterNewBilling = true;
						} else {
							mBilling_address_layout.setVisibility(View.GONE);
							mEnterNewBilling = false;
						}
					}
				});

		// Billing Address

		edtFirstNameBilling = (EditText) findViewById(R.id.f_name);
		edtLastNameBilling = (EditText) findViewById(R.id.l_name);
		edtAddressLine1Billing = (EditText) findViewById(R.id.al_1);
		edtAddressLine2Billing = (EditText) findViewById(R.id.al_2);
		edtPhoneNumberBilling = (EditText) findViewById(R.id.al_3);
		edtZipCodeBilling = (EditText) findViewById(R.id.zip_code);
		edtCityBilling = (EditText) findViewById(R.id.city);
		spinnerStateBilling = (Spinner) findViewById(R.id.stateSpinner);

		mEnterNewBillingAddressLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBilling_address_layout.getVisibility() == View.VISIBLE) {
					if (hasBillingAddressSaved) {
						mPrepopultaedBillingAddressLayout
								.setVisibility(View.VISIBLE);
					}
					mBilling_address_layout.setVisibility(View.GONE);
				} else {
					mPrepopultaedBillingAddressLayout.setVisibility(View.GONE);
					mBilling_address_layout.setVisibility(View.VISIBLE);
				}

			}
		});

		// Shipping address

		edtFirstNameShipping = (EditText) findViewById(R.id.f_name1);

		edtLastNameShipping = (EditText) findViewById(R.id.l_name1);
		edtAddressLine1Shipping = (EditText) findViewById(R.id.al_11);
		edtAddressLine2Shipping = (EditText) findViewById(R.id.edtAdress2);
		edtPhoneNumberShipping = (EditText) findViewById(R.id.phone1);
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
		originalDrawable = edtZipCodeShipping.getBackground();

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

	}

	/**
	 * On form submit.
	 */
	private void onFormSubmit() {
		getAllValues();
		// addshippingAddressAction = UemAction
		// .enterAction(WebserviceConstants.ACTION_SHIPMENT_INVOCATION);

		/**
		 * Billing same as shipping - No billing only shipping to validate
		 */
		if (saveShippingAsBilling.equalsIgnoreCase("true")) {
			/**
			 * If user selects the saved card. No need of validation
			 */
			if (hasShippingAddressSaved && !mEnterNewShipping) {
				saveShippingAddressToCache();
				loadingLayout.setVisibility(View.VISIBLE);
				formLayout.setVisibility(View.GONE);
				invokeShippment();
			}
			/**
			 * User is entering new shipping address-Need to validate
			 */
			else if (isValidationSuccess()) {
				saveShippingAddressToCache();
				loadingLayout.setVisibility(View.VISIBLE);
				formLayout.setVisibility(View.GONE);
				invokeShippment();
				// Toast.makeText(this, "same", 1000).show();
			}

		}
		/**
		 * Billing is different to shipping- Both shipping and billing to
		 * validate
		 */
		else {
			/**
			 * If user selects saved billing address
			 */
			if (hasBillingAddressSaved && !mEnterNewBilling) {
				/**
				 * If user selected saved shipping address - No need to validate
				 * shipping and billing(both saved)
				 */
				if (hasShippingAddressSaved && !mEnterNewShipping) {
					if (addressFragment.getCheckedId() >= 100) {
						saveShippingAddressToCache();
						saveBillingAddressToCache();
						loadingLayout.setVisibility(View.VISIBLE);
						formLayout.setVisibility(View.GONE);
						invokeShippment();
					} else {
						notifyUser("Billing Address Required",
								AddShippingAddressLogginUserActivity.this);
					}

				}
				/**
				 * User is entering new shipping address- Need to valdiate
				 * shipping address only
				 */
				else {
					if (isValidationSuccess()) {
						saveShippingAddressToCache();
						saveBillingAddressToCache();
						loadingLayout.setVisibility(View.VISIBLE);
						formLayout.setVisibility(View.GONE);
						invokeShippment();
					}
				}
			}
			/**
			 * User is entering new billing address
			 */
			else {
				/**
				 * User selected saved shipping address-Need to validate on;y
				 * billing address
				 * 
				 */
				if (hasShippingAddressSaved && !mEnterNewShipping) {
					if (isBillingAddressValidationSuccess()) {
						saveShippingAddressToCache();
						loadingLayout.setVisibility(View.VISIBLE);
						formLayout.setVisibility(View.GONE);
						invokeShippment();
					}
				}
				/**
				 * User is entering new shipping address- Need to validate both
				 * shipping and billing
				 */
				else if (isValidationSuccess()
						&& isBillingAddressValidationSuccess()) {
					saveShippingAddressToCache();
					saveBillingAddressToCache();
					loadingLayout.setVisibility(View.VISIBLE);
					formLayout.setVisibility(View.GONE);
					invokeShippment();
					// Toast.makeText(this, "diff", 1000).show();
				}
			}
		}

	}

	private void saveShippingAddressToCache() {

		formdata.put("first", strFirstName);
		formdata.put("last", strLastName);
		formdata.put("addressline1", strAddressLine1);
		formdata.put("addressline2", strAddressLine2);
		formdata.put("phone", phone1);
		formdata.put("state", strSelectedState);
		formdata.put("city", strCity);
		formdata.put("zipcode", strZipcode);
		formdata.put("shippingAsBilling", saveShippingAsBilling);
		formdata.put("saveShippingForFuture", saveShippingForFuture);
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
	 * Gets the all values.
	 * 
	 * @return the all values
	 */
	private void getAllValues() {
		if (hasShippingAddressSaved && !mEnterNewShipping) {
			if (shippingaddressesList.size() != 0) {
				idShipping = addressFragmentForShipping.getCheckedId() - 100;
				if (idShipping >= 0) {
					strFirstName = shippingaddressesList.get(idShipping)
							.getFirstName();
					strLastName = shippingaddressesList.get(idShipping)
							.getLastName();
					strAddressLine1 = shippingaddressesList.get(idShipping)
							.getAddress1();
					strAddressLine2 = shippingaddressesList.get(idShipping)
							.getAddress2();
					strCity = shippingaddressesList.get(idShipping).getCity();
					strZipcode = shippingaddressesList.get(idShipping)
							.getPostalCode();
					if (null != strZipcode) {
						zipCodeLength = strZipcode.length();
					}
					phone = shippingaddressesList.get(idShipping)
							.getPhoneNumber();
					if (null != phone && phone.length() == 10)

					{
						phone1 = Utility.formatPhoneNumber(phone);
					} else {
						phone1 = phone;
					}
					strSelectedState1 = shippingaddressesList.get(idShipping)
							.getState().substring(0, 2);
					strSelectedState = shippingaddressesList.get(idShipping)
							.getState().substring(0, 2);
				}

			}
		} else {
			// Shipping Address
			strFirstName = edtFirstNameShipping.getText().toString().trim();
			strLastName = edtLastNameShipping.getText().toString().trim();
			strAddressLine1 = edtAddressLine1Shipping.getText().toString()
					.trim();
			strAddressLine2 = edtAddressLine2Shipping.getText().toString()
					.trim();

			phone = edtPhoneNumberShipping.getText().toString().trim();

			/*
			 * String phone1=phone.substring(0, 3); String
			 * phone2=phone.substring(3, 6); String phone3=phone.substring(6,
			 * 10); phone=phone1+"-"+phone2+"-"+phone3;
			 */
			if (null != phone && phone.length() == 10) {
				phone1 = Utility.formatPhoneNumber(phone);
			} else {
				phone1 = phone;
			}
			/*
			 * else{ notifyUser("Please enter 10 digit phone number",
			 * AddNewShippingAddressActivity.this);
			 * edtAddressLine3.requestFocus(); }
			 */
			strZipcode = edtZipCodeShipping.getText().toString().trim();
			if (null != strZipcode) {
				zipCodeLength = strZipcode.length();
			}
			strCity = edtCityShipping.getText().toString().trim();
			strSelectedState1 = spinnerCityShipping.getSelectedItem()
					.toString().trim();
			if (!strSelectedState1.equalsIgnoreCase("* Select state")) {
				strSelectedState = strSelectedState1.substring(0, 2);
			} else {
				strSelectedState = strSelectedState1;
			}
		}
		if (hasBillingAddressSaved && !mEnterNewBilling) {
			if (null != creditCardsList) {
				if (addressFragment.getCheckedId() >= 100) {
					id = addressFragment.getCheckedId() - 100;
					strFirstNameBilling = creditCardsList.get(id)
							.getFirstName();
					strLastNameBilling = creditCardsList.get(id).getLastName();
					strAddress1Billing = creditCardsList.get(id).getAddress1();
					strAddress2Billing = creditCardsList.get(id).getAddress2();
					phoneBilling = creditCardsList.get(id).getPhoneNumber();
					if (null != phoneBilling && phoneBilling.length() == 10) {
						phone1Billing = Utility.formatPhoneNumber(phoneBilling);
					} else {
						phone1Billing = phoneBilling;
					}
					strZipCodeBilling = creditCardsList.get(id).getPostalCode();
					if (null != strZipCodeBilling) {
						strZipCodeLengthBilling = strZipCodeBilling.length();
					}
					strCityBilling = creditCardsList.get(id).getCity();
					strState1Billing = creditCardsList.get(id).getState();
					strStateBilling = creditCardsList.get(id).getState();
				}

			}
		} else {

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
			if (null != phoneBilling && phoneBilling.length() == 10) {
				phone1Billing = Utility.formatPhoneNumber(phoneBilling);
			} else {
				phone1Billing = phoneBilling;
			}
			strZipCodeBilling = edtZipCodeBilling.getText().toString().trim();
			if (null != strZipCodeBilling) {
				strZipCodeLengthBilling = strZipCodeBilling.length();
			}
			strCityBilling = edtCityBilling.getText().toString().trim();

			if(null!=spinnerStateBilling) {
				strState1Billing = spinnerStateBilling.getSelectedItem().toString()
						.trim();
				if (!strState1Billing.equalsIgnoreCase("* Select state")) {
					strStateBilling = strState1Billing.substring(0, 2);
				} else {
					strStateBilling = strState1Billing;
				}
			}
		}
		// Billing Address

		getData.put("FirstName", strFirstNameBilling);
		getData.put("LastName", strLastNameBilling);
		getData.put("Address1", strAddress1Billing);
		getData.put("Address2", strAddress2Billing);
		getData.put("Phone", phoneBilling);
		getData.put("ZipCode", strZipCodeBilling);
		getData.put("City", strCityBilling);
		getData.put("State", "" + posofspinneritemBilling);
		UltaDataCache.getDataCacheInstance().setBillingAddress(getData);
		if (null != shippingaddressesList
				&& null != shippingaddressesList.get(id)) {
			formdata.put("first", shippingaddressesList.get(id).getFirstName());
			formdata.put("last", shippingaddressesList.get(id).getLastName());
			formdata.put("addressline1", shippingaddressesList.get(id)
					.getAddress1());
			formdata.put("addressline2", shippingaddressesList.get(id)
					.getAddress2());
			formdata.put("phone", shippingaddressesList.get(id)
					.getPhoneNumber());
			formdata.put("state", shippingaddressesList.get(id).getState());
			formdata.put("city", shippingaddressesList.get(id).getCity());
			formdata.put("zipcode", shippingaddressesList.get(id)
					.getPostalCode());
			formdata.put("shippingAsBilling", saveShippingAsBilling);
			formdata.put("nickName", shippingaddressesList.get(id)
					.getNickName());
		}
		UltaDataCache.getDataCacheInstance().setShippingAddress(formdata);

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
			setError(edtFirstNameShipping, firstNameErrorText,
					"Fill the First Name in Shipping Address");
			edtFirstNameShipping.requestFocus();
			return false;
		} else if (strFirstName.startsWith(" ")) {
			setError(edtFirstNameShipping, firstNameErrorText,
					"First Name can not start with a space");
			edtFirstNameShipping.requestFocus();
			return false;
		} else if (strLastName.equalsIgnoreCase("") || strLastName == null) {
			setError(edtLastNameShipping, lastNameErrorText,
					"Fill the Last Name in Shipping Address");
			edtLastNameShipping.requestFocus();
			return false;
		} else if (strLastName.startsWith(" ")) {
			setError(edtLastNameShipping, lastNameErrorText,
					"Last Name can not start with a space");
			edtLastNameShipping.requestFocus();
			return false;
		} else if (phone1.equalsIgnoreCase("") || phone1 == null
				|| phone1.startsWith(" ") || phone1.length() < 12) {
			setError(edtPhoneNumberShipping, phoneErrorText,
					"Fill the 10 digit Phone Number in Shipping Address ");
			edtPhoneNumberShipping.requestFocus();
			return false;

		} else if (strAddressLine1.equalsIgnoreCase("")
				|| strAddressLine1 == null) {
			setError(edtAddressLine1Shipping, address1ErrorText,
					"Fill the Address Line 1 in Shipping Address ");
			edtAddressLine1Shipping.requestFocus();
			return false;
		} else if (strAddressLine1.startsWith(" ")) {
			setError(edtAddressLine1Shipping, address1ErrorText,
					"Address Line1 can not start with a space ");
			edtAddressLine1Shipping.requestFocus();
			return false;
		} else if (strCity.equalsIgnoreCase("") || strCity == null) {
			setError(edtCityShipping, cityErrorText,
					"Fill the City in Shipping Address ");
			edtCityShipping.requestFocus();
			return false;
		} else if (strCity.startsWith(" ")) {
			setError(edtCityShipping, cityErrorText,
					"City can not start with a space ");
			edtCityShipping.requestFocus();
			return false;
		} else if (strSelectedState1.equalsIgnoreCase("")
				|| strSelectedState1 == null
				|| strSelectedState1.equalsIgnoreCase("* Select state")) {
			stateErrorText.setText("Select the State in Shipping Address");
			stateErrorText.setVisibility(View.VISIBLE);
			spinnerCityShipping.requestFocus();
			return false;
		}

		else if (strZipcode.equalsIgnoreCase("") || strZipcode == null
				|| strZipcode.startsWith(" ")) {
			setError(edtZipCodeShipping, zipCodeErrorText,
					"Fill the Zip Code properly in Shipping Address ");
			edtZipCodeShipping.requestFocus();
			return false;
		} else if (zipCodeLength < 5) {
			setError(edtZipCodeShipping, zipCodeErrorText,
					"Zip Code cannot be less than 5 digits");
			edtZipCodeShipping.requestFocus();
			return false;
		} else if (zipCodeLength > 5) {
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
			setError(edtFirstNameBilling, firstNameBillingErrorText,
					"Fill the First Name in Billing Address");
			edtFirstNameBilling.requestFocus();
			return false;
		} else if (strFirstNameBilling.startsWith(" ")) {
			setError(edtFirstNameBilling, firstNameBillingErrorText,
					"First Name can not start with a space");
			edtFirstNameBilling.requestFocus();
			return false;
		} else if (strLastNameBilling.equalsIgnoreCase("")
				|| strLastNameBilling == null) {
			setError(edtLastNameBilling, lastNameBillingErrorText,
					"Fill the Last Name in Billing Address");
			edtLastNameBilling.requestFocus();
			return false;
		} else if (strLastNameBilling.startsWith(" ")) {
			setError(edtLastNameBilling, lastNameBillingErrorText,
					"Last Name can not start with a space");
			edtLastNameBilling.requestFocus();
			return false;
		} else if (phone1Billing.equalsIgnoreCase("") || phone1Billing == null
				|| phone1Billing.startsWith(" ") || phone1Billing.length() < 12) {
			setError(edtPhoneNumberBilling, phoneBillingErrorText,
					"Fill the 10 digit Phone Number in Billing Address");
			edtPhoneNumberBilling.requestFocus();
			return false;
		} else if (strAddress1Billing.equalsIgnoreCase("")
				|| strAddress1Billing == null) {
			setError(edtAddressLine1Billing, address1BillingErrorText,
					"Fill the Address Line 1 in Billing Address");
			edtAddressLine1Billing.requestFocus();
			return false;
		} else if (strAddress1Billing.startsWith(" ")) {
			setError(edtAddressLine1Billing, address1BillingErrorText,
					"Address Line1 can not start with a space");
			edtAddressLine1Billing.requestFocus();
			return false;
		} else if (strCityBilling.equalsIgnoreCase("")
				|| strCityBilling == null) {
			setError(edtCityBilling, cityBillingErrorText,
					"Fill the City in Billing Address");
			edtCityBilling.requestFocus();

			return false;
		} else if (strState1Billing.equalsIgnoreCase("")
				|| strState1Billing == null
				|| strState1Billing.equalsIgnoreCase("* Select state")) {
			stateBillingErrorText
					.setText("Select the State in Billing Address");
			stateBillingErrorText.setVisibility(View.VISIBLE);
			spinnerCityShipping.requestFocus();
			spinnerStateBilling.requestFocus();
			return false;
		} else if (strZipCodeBilling.equalsIgnoreCase("")
				|| strZipCodeBilling == null
				|| strZipCodeBilling.startsWith(" ")) {
			setError(edtZipCodeBilling, zipBillingErrorText,
					"Fill the Zipcode properly in Billing Address");
			edtZipCodeBilling.requestFocus();
			return false;
		} else if (strZipCodeLengthBilling < 5) {
			setError(edtZipCodeBilling, zipBillingErrorText,
					"Zip Code cannot be less than 5 digits");
			edtZipCodeBilling.requestFocus();
			return false;
		} else if (strZipCodeLengthBilling > 5) {
			setError(edtZipCodeBilling, zipBillingErrorText,
					"Zip Code cannot be more than 5 digits");
			edtZipCodeBilling.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Get Saved shipped Address
	 */

	private void invokeShippingaddressesDetails() {
		if (null != pd) {
			pd.setMessage("Fetching Address.....");
			pd.show();
		}
		InvokerParams<ShippingAddressesBean> invokerParams = new InvokerParams<ShippingAddressesBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.LISTOF_SHIPPING_ADDRESS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populateShippingaddressesDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(ShippingAddressesBean.class);
		RetrieveShippingaddressesHandler retrieveShippingaddressesDetailsHandler = new RetrieveShippingaddressesHandler();
		invokerParams.setUltaHandler(retrieveShippingaddressesDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Populate parameters to pass
	 * 
	 * @return
	 */

	private Map<String, String> populateShippingaddressesDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		urlParams.put("atg-rest-depth", "0");
		return urlParams;
	}

	/**
	 * handler for fetching shipping address
	 * 
	 * 
	 */
	public class RetrieveShippingaddressesHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveShippingaddressesDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(AddShippingAddressLogginUserActivity.this);
					// fetchShippingAddressAction.reportError(
					// WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
					// fetchShippingAddressAction.leaveAction();
				} else {
					try {
						if (null != pd && pd.isShowing()) {
                            pd.dismiss();
                        }
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						// fetchShippingAddressAction
						// .reportError(
						// getErrorMessage(),
						// WebserviceConstants.DYN_ERRCODE_SHIPPING_ADDRESS_ACTIVITY);
						// fetchShippingAddressAction.leaveAction();
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								AddShippingAddressLogginUserActivity.this);
						loadingLayout.setVisibility(View.GONE);
						formLayout.setVisibility(View.VISIBLE);
						setError(AddShippingAddressLogginUserActivity.this,
								getErrorMessage());
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}

				}
			} else {
				try {
					if (null != pd && pd.isShowing()) {
                        pd.dismiss();
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
				Logger.Log("<RetrieveShippingaddressesDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				shippingAddressesBean = (ShippingAddressesBean) getResponseBean();
				if (null != shippingAddressesBean) {

					Logger.Log("<HomeActivity>" + "BeanPopulated");

					shippingaddressesList = shippingAddressesBean
							.getShippingAddresses();

					if (null != shippingaddressesList
							&& shippingaddressesList.size() != 0) {
						fragment.setVisibility(View.VISIBLE);
						// noAddressList.setVisibility(View.GONE);
						String fname = null, add1 = null, shippingId = null;
						AddressBean address;
						for (int i = 0; i < shippingaddressesList.size(); i++) {
							address = shippingaddressesList.get(i);
							if (null != address) {
								if (null != address.getAddress1())
									add1 = address.getAddress1();
								if (null != address.getAddress2())
									add1 = add1 + " " + address.getAddress2();
								if (null != address.getCity())
									add1 = add1 + "\n" + address.getCity();
								if (null != address.getState())
									add1 = add1 + ", " + address.getState();
								// if (null != address.getCountry())
								// add1 = add1 + "" + address.getCountry();
								if (null != address.getPostalCode())
									add1 = add1 + "  "
											+ address.getPostalCode();
								// if (null != address.getPhoneNumber())
								// add1 = add1 + "\n"
								// + address.getPhoneNumber();

								if (null != address.getFirstName())
									fname = address.getFirstName();
								if (null != address.getLastName()
										&& null != address.getFirstName())
									fname = address.getFirstName() + " "
											+ address.getLastName();
								// if (null != address.getLastName()
								// && null != address.getFirstName()
								// && null != address.getNickName()) {
								// fname = address.getFirstName() + " "
								// + address.getLastName() + " ["
								// + address.getNickName() + " ]";
								// }
								if (null != address.getId()) {
									shippingId = address.getId();
								}

							}

							if (flag == false) {
								if (!(shippingId
										.equalsIgnoreCase(shippingAddressesBean
												.getDefaultShippingAddressId()))) {

									count++;

								} else {
									flag = true;
								}
							}
							if (shippingAddressesBean
									.getDefaultShippingAddressId().equals(
											address.getId())) {
								addressFragmentForShipping
										.addDefaultAddressRow(fname, add1);
							} else {
								addressFragmentForShipping.addNewRow(fname,
										add1);
							}

							Logger.Log(">>>>count<<<<<<" + count);
							addressFragmentForShipping
									.setDefaultShippingAddressId(count);
							// fetchShippingAddressAction
							// .reportEvent("Addresses fetched successfully");
							// fetchShippingAddressAction.leaveAction();
						}
						hasShippingAddressSaved = true;
						loadingLayout.setVisibility(View.GONE);
						formLayout.setVisibility(View.VISIBLE);
					}

					else {
						fragment.setVisibility(View.GONE);
						// noAddressList.setVisibility(View.VISIBLE);
						loadingLayout.setVisibility(View.GONE);
						formLayout.setVisibility(View.VISIBLE);
					}

				} else {
					fragment.setVisibility(View.GONE);
					// noAddressList.setVisibility(View.VISIBLE);
					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);
				}
			}
		}

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
						AddShippingAddressLogginUserActivity.this);
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
				AddShippingAddressLogginUserActivity.this, anArrayOfStrings);

		spinnerCityShipping.setAdapter(spinnerSrrayAdapter);
		spinnerCityShipping
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View arg1, int position, long arg3) {
						if (null != parentView.getChildAt(0)) {
							((TextView) parentView.getChildAt(0))
									.setTextColor(getResources().getColor(
											R.color.black));
							((TextView) parentView.getChildAt(0))
									.setTextSize(12);
							((TextView) parentView.getChildAt(0)).setPadding(5,
									0, 0, 0);
						}

						if (position != 0) {
							/* isSpinnerSelected = true; */
							stateErrorText.setVisibility(View.GONE);
							strSelectedState1 = parentView.getItemAtPosition(
									position).toString();
							if (!strSelectedState1
									.equalsIgnoreCase("* Select state")) {
								strSelectedState = strSelectedState1.substring(
										0, 2);
							} else {
								strSelectedState = strSelectedState1;
							}
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

						if (null != parentView.getChildAt(0)) {
							((TextView) parentView.getChildAt(0))
									.setTextColor(getResources().getColor(
											R.color.black));
							((TextView) parentView.getChildAt(0))
									.setTextSize(12);
							((TextView) parentView.getChildAt(0)).setPadding(5,
									0, 0, 0);
						}

						if (position != 0) {
							stateBillingErrorText.setVisibility(View.GONE);
							strState1Billing = parentView.getItemAtPosition(
									position).toString();
							if (!strState1Billing
									.equalsIgnoreCase("* Select state")) {
								strStateBilling = strState1Billing.substring(0,
										2);
							} else {
								strStateBilling = strState1Billing;
							}
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

				tv.setText("* Select state");

				tv.setTextColor(getResources().getColor(R.color.black));
				tv.setPadding(5, 0, 0, 0);
				return tv;
			}
			// let the array adapter takecare this time
			return super.getView(position, convertView, parent);
		}
	}

	/**
	 * Invoke shippment.
	 */
	public void invokeShippment() {
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
			Logger.Log("<ShippingAddressActivity><invokeForgotPassword><UltaException>>"
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
		// if (fragment.getVisibility() == View.VISIBLE) {
		//
		// urlParams.put("atg-rest-output", "json");
		// urlParams.put("atg-rest-return-form-handler-properties", "true");
		// urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		// urlParams.put("atg-rest-depth", "2");
		// urlParams.put("firstName", shippingaddressesList.get(idShipping)
		// .getFirstName());
		// urlParams.put("lastName", shippingaddressesList.get(idShipping)
		// .getLastName());
		// urlParams.put("address1", shippingaddressesList.get(idShipping)
		// .getAddress1());
		// urlParams.put("address2", shippingaddressesList.get(idShipping)
		// .getAddress2());
		// urlParams.put("city", shippingaddressesList.get(idShipping)
		// .getCity());
		// urlParams.put("country", "US");
		// urlParams.put("postalCode", shippingaddressesList.get(idShipping)
		// .getPostalCode());
		// urlParams.put("phoneNumber", shippingaddressesList.get(idShipping)
		// .getPhoneNumber());
		// urlParams.put("state", shippingaddressesList.get(idShipping)
		// .getState().substring(0, 2));
		// urlParams.put("useShippingForBilling", saveShippingAsBilling);
		// urlParams.put("isReqFromMobile", "true");
		// // urlParams.put("saveShippingAddress", saveShippingForFuture);
		//
		// } else {
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "2");
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
		urlParams.put("saveShippingAddress", saveShippingForFuture);
		urlParams.put("isReqFromMobile", "true");
		// }
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
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(AddShippingAddressLogginUserActivity.this);
					// addshippingAddressAction.reportError(
					// WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
					// addshippingAddressAction.leaveAction();
				} else {
					try {
						// addshippingAddressAction
						// .reportError(
						// WebserviceConstants.FORM_EXCEPTION_OCCURED,
						// 113);
						// addshippingAddressAction.leaveAction();
						notifyUser(getErrorMessage(),
								AddShippingAddressLogginUserActivity.this);
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

					List<String> errors = ultaBean.getErrorInfos();
					if (null != errors && !(errors.isEmpty())) {
						try {
							// addshippingAddressAction
							// .reportError(
							// errors.get(0),
							// WebserviceConstants.DYN_ERRCODE_SHIPPING_ADDRESS_ACTIVITY);
							// addshippingAddressAction.leaveAction();
							notifyUser(errors.get(0),
									AddShippingAddressLogginUserActivity.this);
						} catch (WindowManager.BadTokenException e) {
						} catch (Exception e) {
						}
						loadingLayout.setVisibility(View.GONE);
						formLayout.setVisibility(View.VISIBLE);
					} else {
						// addshippingAddressAction
						// .reportEvent("Address sucessfully added for the order");
						// addshippingAddressAction.leaveAction();
						boolean mobileStatusVertexDAV = ultaBean.getComponent()
								.getMobileStatusVertexDAV();
						if (mobileStatusVertexDAV) {
							Intent goToShippingMethod = new Intent(
									AddShippingAddressLogginUserActivity.this,
									ShippingAddressVerification.class);
							goToShippingMethod.putExtra("ShippingMethodBean",
									ultaBean);
							startActivity(goToShippingMethod);
						} else {
							Intent goToShippingMethod = new Intent(
									AddShippingAddressLogginUserActivity.this,
									ShippingMethodActivity.class);
							goToShippingMethod.putExtra("ShippingMethodBean",
									ultaBean);
							startActivity(goToShippingMethod);
						}
					}
				} else {
					try {
						notifyUser("Enter Valid details",
								AddShippingAddressLogginUserActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}

					loadingLayout.setVisibility(View.GONE);
					formLayout.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	/**
	 * Invoke billing address.
	 */
	private void invokePaymentMethodDetails() {
		// fetchBillingAddressAction = UemAction
		// .enterAction(WebserviceConstants.ACTION_FETCH_BILLING_ADDRESS_INVOCATION);
		// fetchBillingAddressAction
		// .reportEvent("started fetching billing addresses for the registred use");
		InvokerParams<PaymentMethodBean> invokerParams = new InvokerParams<PaymentMethodBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.PAYMENT_METHOD_DETAILS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populatePaymentMethodDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(PaymentMethodBean.class);
		RetrievePaymentDetailsHandler retrievePaymentDetailsHandler = new RetrievePaymentDetailsHandler();
		invokerParams.setUltaHandler(retrievePaymentDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> populatePaymentMethodDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		urlParams.put("atg-rest-depth", "0");
		return urlParams;
	}

	/**
	 * The Class BillingAddressHandler.
	 */
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
		public void handleMessage(Message msg) {
			Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {

				if (getErrorMessage().startsWith("401")) {
					askRelogin(AddShippingAddressLogginUserActivity.this);
					// fetchBillingAddressAction.reportError(
					// WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
					// fetchBillingAddressAction.leaveAction();
				} else {
					try {
						// fetchBillingAddressAction
						// .reportError(
						// getErrorMessage(),
						// WebserviceConstants.DYN_ERRCODE_BILLING_ADDRESS_ACTIVITY);
						// fetchBillingAddressAction.leaveAction();
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								AddShippingAddressLogginUserActivity.this);
						setError(AddShippingAddressLogginUserActivity.this,
								getErrorMessage());
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
					loadingLayout.setVisibility(View.GONE);
					/* formLayout.setVisibility(View.VISIBLE); */

				}
			} else {

				Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				paymentMethodBean = (PaymentMethodBean) getResponseBean();
				if (null != paymentMethodBean) {

					Logger.Log("<HomeActivity>" + "BeanPopulated");

					creditCardsList = paymentMethodBean.getCreditCards();
					/*
					 * defaultCreditCardId = paymentMethodBean
					 * .getDefaultCreditCardId();
					 */
					// if(null!=defaultCreditCardId){

					if (null != creditCardsList && creditCardsList.size() != 0) {
						billingFragment.setVisibility(View.VISIBLE);
						// noBillingAddressList.setVisibility(View.GONE);

						String fname = null, add1 = null;
						for (int i = 0; i < creditCardsList.size(); i++) {

							if (null != creditCardsList.get(i).getAddress1())
								add1 = creditCardsList.get(i).getAddress1();
							if (null != creditCardsList.get(i).getAddress2())
								add1 = add1 + ","
										+ creditCardsList.get(i).getAddress2();
							if (null != creditCardsList.get(i).getCity())
								add1 = add1 + "\n"
										+ creditCardsList.get(i).getCity();
							if (null != creditCardsList.get(i).getState())
								add1 = add1 + ","
										+ creditCardsList.get(i).getState();
							if (null != creditCardsList.get(i).getPostalCode())
								add1 = add1
										+ "\n"
										+ "US ,"
										+ creditCardsList.get(i)
												.getPostalCode();

							if (null != creditCardsList.get(i).getPhoneNumber())
								add1 = add1
										+ "\n"
										+ creditCardsList.get(i)
												.getPhoneNumber();

							if (null != creditCardsList.get(i).getFirstName())
								fname = creditCardsList.get(i).getFirstName();
							if (null != creditCardsList.get(i).getLastName()
									&& null != creditCardsList.get(i)
											.getFirstName())
								fname = creditCardsList.get(i).getFirstName()
										+ " "
										+ creditCardsList.get(i).getLastName();
							if (null != creditCardsList.get(i).getLastName()
									&& null != creditCardsList.get(i)
											.getFirstName()
									&& null != creditCardsList.get(i)
											.getNickName())
								fname = creditCardsList.get(i).getFirstName()
										+ " "
										+ creditCardsList.get(i).getLastName()
										+ " ["
										+ creditCardsList.get(i).getNickName()
										+ " ]";
							if (null != creditCardsList.get(i).getId()) {
								if (null != paymentMethodBean
										.getDefaultCreditCardId()) {
									if (paymentMethodBean
											.getDefaultCreditCardId().equals(
													creditCardsList.get(i)
															.getId())) {
										addressFragment.addDefaultAddressRow(
												fname, add1);
									} else {
										addressFragment.addNewRow(fname, add1);
									}
								} else {
									addressFragment.addNewRow(fname, add1);
								}
							}
							// addressFragment.addNewRow(fname, add1);
						}
						hasBillingAddressSaved = true;
						mPrepopultaedBillingAddressLayout
								.setVisibility(View.VISIBLE);
						mEnterNewBillingAddressLayout
								.setVisibility(View.VISIBLE);

					} else {
						mPrepopultaedBillingAddressLayout
								.setVisibility(View.GONE);
						mEnterNewBillingAddressLayout.setVisibility(View.GONE);
						mBilling_address_layout.setVisibility(View.VISIBLE);
					}
				} else {
					mPrepopultaedBillingAddressLayout.setVisibility(View.GONE);
					mEnterNewBillingAddressLayout.setVisibility(View.GONE);
					mBilling_address_layout.setVisibility(View.VISIBLE);
				}
				// fetchBillingAddressAction
				// .reportEvent("Billing addresses fetched successfullly");
				// fetchBillingAddressAction.leaveAction();
			}
		}

	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			// if (mInvokeShippingaddressesDetails) {
			// invokeShippingaddressesDetails();
			// } else if (mInvokeShippment) {
			// invokeShippment();
			// } else if (mInvokePaymentMethodDetails) {
			// invokePaymentMethodDetails();
			// }
			navigateToBasketOnSessionTimeout(AddShippingAddressLogginUserActivity.this);
		} else {
			if (null != pd && pd.isShowing()) {
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
			edtFirstNameBilling.setBackgroundDrawable(originalDrawable);
			firstNameBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtLastNameBilling.getText().hashCode()) {
			edtLastNameBilling.setBackgroundDrawable(originalDrawable);
			lastNameBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtAddressLine1Billing.getText().hashCode()) {
			edtAddressLine1Billing.setBackgroundDrawable(originalDrawable);
			address1BillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtPhoneNumberBilling.getText().hashCode()) {
			edtPhoneNumberBilling.setBackgroundDrawable(originalDrawable);
			phoneBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtCityBilling.getText().hashCode()) {
			edtCityBilling.setBackgroundDrawable(originalDrawable);
			cityBillingErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtZipCodeBilling.getText().hashCode()) {
			edtZipCodeBilling.setBackgroundDrawable(originalDrawable);
			zipBillingErrorText.setVisibility(View.GONE);
		}

		changeAllEditTextBackground();
	}

	public void changeAllEditTextBackground() {
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
		UltaDataCache.getDataCacheInstance().setAnonymousCheckout(false);
		finish();
	}
}
