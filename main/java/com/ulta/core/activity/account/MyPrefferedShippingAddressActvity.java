/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.DefaultShippingAddressBean;
import com.ulta.core.bean.checkout.AddressBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.fragments.account.ProfileAddressFragment;
import com.ulta.core.fragments.account.ProfileAddressFragment.OnDefaultObtained;
import com.ulta.core.fragments.account.ProfileAddressFragment.OnDeleteDone;
import com.ulta.core.fragments.account.ProfileAddressFragment.OnEditCompletion;
import com.ulta.core.fragments.account.ProfileAddressFragment.OnFragmentDeleteAddressTimeout;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class MyPrefferedShippingAddressActvity.
 */
public class MyPrefferedShippingAddressActvity extends UltaBaseActivity
		implements OnEditCompletion, OnDefaultObtained, OnDeleteDone,
		OnSessionTimeOut, OnFragmentDeleteAddressTimeout {

	/** The default shipping address bean. */
	DefaultShippingAddressBean defaultShippingAddressBean;

	/** The shipping address bean. */
	AddressBean shippingAddressBean;

	/** The tv zip code. */
	TextView tvFirstname, tvCountryName, tvLastname, tvPhoneNumber, tvUserName,
			tvAddline1, tvAddline2, tvCityName, tvStateName, tvZipCode;

	/** The zip. */
	String firstName, lastName, phNumber, addLine1, addLine2, city, state,
			country, zip;

	ProfileAddressFragment fragment;

	/** The Layouts. */
	LinearLayout main_layout, loadingDialog;

	Button btnAddNewAddress;

	private boolean isRetrieveShippingAddressDetails;
	private boolean isDeleteAddressDetails;
	private String mNickname;
	private int mIndex;

	@Override
	public void onLogout() {
		// super.onLogout();
		finish();
	}

	public void refreshPage() {

		invokePreffredShippingaddressDetails();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// isOnCreateCalled=true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account_shipping_address);
		// setTitle("My Preffred Shipping Address");
		setTitle("Shipping");
		intViews();
		fragment = (ProfileAddressFragment) getSupportFragmentManager()
				.findFragmentById(R.id.profile_address_fragment);

		fragment.setOnEditCompletion(this);

		fragment.setOnDefaultObtained(this);

		fragment.setOnDeleteDone(this);

		fragment.setOnFragmentDeleteAddressTimeout(this);
		registerForLogoutBroadcast();
		invokePreffredShippingaddressDetails();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		invokePreffredShippingaddressDetails();

	}

	/**
	 * Int views.
	 */
	private void intViews() {
		tvFirstname = (TextView) findViewById(R.id.tvFirstName);
		tvLastname = (TextView) findViewById(R.id.tvLastName);
		tvPhoneNumber = (TextView) findViewById(R.id.tvPhone);
		tvUserName = (TextView) findViewById(R.id.tvEmail);
		tvAddline1 = (TextView) findViewById(R.id.tvaddLine1);
		tvAddline2 = (TextView) findViewById(R.id.tvaddLine2);
		tvCityName = (TextView) findViewById(R.id.tvcity);
		tvZipCode = (TextView) findViewById(R.id.tvZip);
		tvStateName = (TextView) findViewById(R.id.tvState);
		tvCountryName = (TextView) findViewById(R.id.tvCountry);
		main_layout = (LinearLayout) findViewById(R.id.main_layout);
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialogAccountShipping);

	}

	/**
	 * Invoke preffred shippingaddress details.
	 */
	private void invokePreffredShippingaddressDetails() {

		loadingDialog.setVisibility(View.VISIBLE);
		main_layout.setVisibility(View.GONE);
		InvokerParams<DefaultShippingAddressBean> invokerParams = new InvokerParams<DefaultShippingAddressBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.PREFFRED_SHIPPING_ADDRESS_DETAILS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populatePrefferdShippingAddressDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(DefaultShippingAddressBean.class);
		RetrieveMyPreffredShippingAddressDetailsHandler retrieveMyPreffredShippingAddressDetailsHandler = new RetrieveMyPreffredShippingAddressDetailsHandler();
		invokerParams
				.setUltaHandler(retrieveMyPreffredShippingAddressDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<MyPrefferedShippingAddressActivity><invokePreffredShippingaddressDetails()><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Populate prefferd shipping address details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populatePrefferdShippingAddressDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		urlParams.put("atg-rest-depth", "2");
		return urlParams;
	}

	/**
	 * The Class RetrieveMyPreffredShippingAddressDetailsHandler.
	 */
	public class RetrieveMyPreffredShippingAddressDetailsHandler extends
			UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveMyPreffredShippingAddressDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(MyPrefferedShippingAddressActvity.this);
				} else {
					loadingDialog.setVisibility(View.GONE);
					try {

						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								MyPrefferedShippingAddressActvity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				loadingDialog.setVisibility(View.GONE);
				Logger.Log("<RetrieveMyPreffredShippingAddressDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));

				defaultShippingAddressBean = (DefaultShippingAddressBean) getResponseBean();
				shippingAddressBean = defaultShippingAddressBean
						.getDefaultShippingAddress();
				if (null != shippingAddressBean) {
					Logger.Log("<MyPrefferedShippingAddressActivity>"
							+ "BeanPopulated");
					setViews();
					loadingDialog.setVisibility(View.GONE);
					main_layout.setVisibility(View.VISIBLE);
					tvFirstname.setVisibility(View.VISIBLE);
					tvLastname.setVisibility(View.VISIBLE);
					tvPhoneNumber.setVisibility(View.VISIBLE);
					tvAddline1.setVisibility(View.VISIBLE);
					tvAddline2.setVisibility(View.VISIBLE);
					tvCityName.setVisibility(View.VISIBLE);
					tvCountryName.setVisibility(View.VISIBLE);
					tvZipCode.setVisibility(View.VISIBLE);

				} else {

					main_layout.setVisibility(View.VISIBLE);
					loadingDialog.setVisibility(View.GONE);

					tvFirstname.setVisibility(View.INVISIBLE);
					tvLastname.setVisibility(View.INVISIBLE);
					tvPhoneNumber.setVisibility(View.INVISIBLE);
					tvAddline1.setVisibility(View.INVISIBLE);
					tvAddline2.setVisibility(View.INVISIBLE);
					tvCityName.setVisibility(View.INVISIBLE);
					tvCountryName.setVisibility(View.INVISIBLE);
					tvZipCode.setVisibility(View.INVISIBLE);
					tvStateName.setText("There is No Default Shipping Address");
				}

				fragment.populateList();

			}
		}
	}

	/**
	 * Sets the views.
	 */
	public void setViews() {

		firstName = shippingAddressBean.getFirstName();
		if (firstName != null)
			tvFirstname.setText(firstName + "  ");

		lastName = shippingAddressBean.getLastName();
		if (lastName != null)
			tvLastname.setText(lastName);

		phNumber = shippingAddressBean.getPhoneNumber();
		if (phNumber != null)
			tvPhoneNumber.setText(phNumber);

		addLine1 = shippingAddressBean.getAddress1();
		if (addLine1 != null)
			tvAddline1.setText(addLine1 + ", ");

		addLine2 = shippingAddressBean.getAddress2();
		if (addLine2 != null)
			tvAddline2.setText(addLine2 + ",");

		city = shippingAddressBean.getCity();
		if (city != null)
			tvCityName.setText(city + ", ");

		state = shippingAddressBean.getState();
		if (state != null)
			tvStateName.setText(state + ", ");

		country = shippingAddressBean.getCountry();
		if (country != null)
			tvCountryName.setText(country + " ");

		zip = shippingAddressBean.getPostalCode();
		if (zip != null)
			tvZipCode.setText(zip);

	}

	@Override
	public void onEditCompleted() {

	}

	@Override
	public void OnDefaultObtained(AddressBean address) {

		shippingAddressBean = address;
		setViews();
	}

	@Override
	public void OnDeleteDone() {
		// shippingAddressBean=address;
		Logger.Log(">>>>>>>>>>>>onDeleteDone<<<<<<<<<<<<<");
		invokePreffredShippingaddressDetails();
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			if (isRetrieveShippingAddressDetails) {
				invokePreffredShippingaddressDetails();
			} else {
				loadingDialog.setVisibility(View.GONE);
				if (isDeleteAddressDetails) {
					fragment.deleteFromList(mNickname, mIndex);
				}
			}

		} else {
			loadingDialog.setVisibility(View.GONE);
		}
	}

	@Override
	public void OnFragmentDeleteDetailsSessionTimeOut(
			boolean isDeleteShippingDetails, String nickName, int Position) {
		isDeleteAddressDetails = true;
		mNickname = nickName;
		mIndex = Position;
		askRelogin(MyPrefferedShippingAddressActvity.this);
	}

}
