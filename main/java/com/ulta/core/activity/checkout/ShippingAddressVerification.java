package com.ulta.core.activity.checkout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.checkout.AddressBean;
import com.ulta.core.bean.checkout.AddressVerificationBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.checkout.GuestUserDataBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.fragments.checkout.AddressFragment;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShippingAddressVerification extends UltaBaseActivity implements
		OnClickListener, OnSessionTimeOut {
	/** Fragment declaration */
	private AddressFragment addressFragment;// Address fragment to populate
											// entered and verified address
	private ProgressDialog pd;// Prgoress dialog
	private ImageButton mShippingType, mAddress;// Button for navigating to
												// shipping
	// type and back to address
	private TextView mCheckout_shipping, mCheckout_payment,
			mCheckout_review_order;// Header Text view for shipping
	private FrameLayout mLoadingDialog;// Loading Dialog Layout
	AddressVerificationBean addressVerificationBean;
	HashMap<String, String> formdata;
	private HashMap<String, String> guestUserDeatails;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_address);
		setTitle("Select Shipping Address");
		initFooterViews();
		creatingPageName();
		initViews();
		if (!UltaDataCache.getDataCacheInstance().getOrderTotal()
				.equalsIgnoreCase("")) {
			mTotalValueTextView.setText("$"
					+ UltaDataCache.getDataCacheInstance().getOrderTotal());
			mExpandImageView.setVisibility(View.GONE);

		} else {
			mTotalLayout.setVisibility(View.GONE);
		}
		pd = new ProgressDialog(ShippingAddressVerification.this);
		setProgressDialogLoadingColor(pd);
		pd.setCancelable(false);
		mLoadingDialog.setVisibility(View.GONE);
		invokeVerifiedAddress();
	}

	private void initViews() {
		addressFragment = (AddressFragment) getSupportFragmentManager()
				.findFragmentById(R.id.addressFragment);
		mShippingType = (ImageButton) findViewById(R.id.shippingType);
		mShippingType.setOnClickListener(this);
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
		mAddress = (ImageButton) findViewById(R.id.address);
		mAddress.setOnClickListener(this);
		mLoadingDialog = (FrameLayout) findViewById(R.id.loadingDialog);
	}

	public void invokeVerifiedAddress() {
		pd.setMessage("Fetching Address.....");
		pd.show();
		InvokerParams<AddressVerificationBean> invokerParams = new InvokerParams<AddressVerificationBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.GET_VERIFIED_SHIPPING_ADDRESS);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateParameters());
		invokerParams.setUltaBeanClazz(AddressVerificationBean.class);
		AddressHandler userCreationHandler = new AddressHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");
		return urlParams;
	}

	public class AddressHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveSuggestedAddressHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(ShippingAddressVerification.this);
					mLoadingDialog.setVisibility(View.GONE);
				} else {
					if (null != pd && pd.isShowing()) {

						pd.dismiss();
					}
					try {

						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								ShippingAddressVerification.this);
						if (null != pd && pd.isShowing()) {
							pd.dismiss();
						}
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
						if (null != pd && pd.isShowing()) {
							pd.dismiss();
						}
						mLoadingDialog.setVisibility(View.GONE);
					}

				}
			} else {
				if (null != pd && pd.isShowing()) {

					pd.dismiss();
				}
				addressVerificationBean = (AddressVerificationBean) getResponseBean();
				if (null != addressVerificationBean) {
					if (null != addressVerificationBean.getEnteredShipAddress()) {
						addressFragment.addText("You Entered");
						showAddress(addressVerificationBean
								.getEnteredShipAddress());
					}
					if (null != addressVerificationBean
							.getVerifiedShipAddress()) {
						addressFragment.addText("Suggested Address");
						showAddress(addressVerificationBean
								.getVerifiedShipAddress());
					}
				}

				mLoadingDialog.setVisibility(View.GONE);
				if (null != pd && pd.isShowing()) {
					pd.dismiss();
				}
				mShippingType.setVisibility(View.VISIBLE);
				mAddress.setVisibility(View.VISIBLE);
			}
		}
	}

	private void showAddress(AddressBean addressbean) {
		if (null != addressbean) {
			String fname = null, add1 = null;
			if (null != addressbean.getAddress1())
				add1 = addressbean.getAddress1();
			if (null != addressbean.getAddress2())
				add1 = add1 + "," + addressbean.getAddress2();
			if (null != addressbean.getCity())
				add1 = add1 + "\n" + addressbean.getCity();
			if (null != addressbean.getState())
				add1 = add1 + "," + addressbean.getState();
			if (null != addressbean.getCountry())
				add1 = add1 + "\n" + addressbean.getCountry();
			if (null != addressbean.getPostalCode())
				add1 = add1 + "," + addressbean.getPostalCode();
			if (null != addressbean.getPhoneNumber())
				add1 = add1 + "\n" + addressbean.getPhoneNumber();

			if (null != addressbean.getFirstName())
				fname = addressbean.getFirstName();
			if (null != addressbean.getLastName()
					&& null != addressbean.getFirstName())
				fname = addressbean.getFirstName() + " "
						+ addressbean.getLastName();
			if (null != addressbean.getLastName()
					&& null != addressbean.getFirstName()
					&& null != addressbean.getNickName()) {
				fname = addressbean.getFirstName() + " "
						+ addressbean.getLastName() + " ["
						+ addressbean.getNickName() + " ]";
			}
			addressFragment.addNewRow(fname, add1);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shippingType:
			onFormSubmit();
			break;
		case R.id.address:
			finish();
			break;
		}
	}

	public void onFormSubmit() {
		int id = addressFragment.getCheckedId() - 100;
		pd.setMessage(UltaConstants.LOADING_PROGRESS_TEXT);
		pd.show();
		if (id == 1) {
			updateShippingAddressInCache();
			performSavingGuestData();
			invokeSetAddressAndCommitOrder();

		} else {
			invokeKeepAddressAndCommitOrder();
		}

	}

	private void updateShippingAddressInCache() {
		AddressBean addressbean = addressVerificationBean
				.getVerifiedShipAddress();
		formdata = new HashMap<String, String>();
		if (null != addressbean) {
			if (UltaDataCache.getDataCacheInstance().getShippingAddress()
					.get("shippingAsBilling").equals("true")) {
				formdata.put("first", addressbean.getFirstName());
				formdata.put("last", addressbean.getLastName());
				formdata.put("addressline1", addressbean.getAddress1());
				formdata.put("addressline2", addressbean.getAddress2());
				formdata.put("phone", addressbean.getPhoneNumber());
				formdata.put("state", addressbean.getState());
				formdata.put("city", addressbean.getCity());
				formdata.put("zipcode", addressbean.getPostalCode());
				formdata.put("shippingAsBilling", "" + true);
				formdata.put("Country", "US");
				UltaDataCache.getDataCacheInstance().setShippingAddress(
						formdata);
			} else {
				formdata.put("first", addressbean.getFirstName());
				formdata.put("last", addressbean.getLastName());
				formdata.put("addressline1", addressbean.getAddress1());
				formdata.put("addressline2", addressbean.getAddress2());
				formdata.put("phone", addressbean.getPhoneNumber());
				formdata.put("state", addressbean.getState());
				formdata.put("city", addressbean.getCity());
				formdata.put("zipcode", addressbean.getPostalCode());
				formdata.put("shippingAsBilling", "" + false);
				formdata.put("Country", "US");
				UltaDataCache.getDataCacheInstance().setShippingAddress(
						formdata);
			}

		}

	}

	private void invokeKeepAddressAndCommitOrder() {
		InvokerParams<CheckoutShippmentMethodBean> invokerParams = new InvokerParams<CheckoutShippmentMethodBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.KEEP_EXISTING_ADDRESS_AND_MOVE_TO_SHIPPING_METHOD);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateParameters());
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

	private void invokeSetAddressAndCommitOrder() {
		InvokerParams<CheckoutShippmentMethodBean> invokerParams = new InvokerParams<CheckoutShippmentMethodBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.UPDATE_VERIFIED_ADDRESS_AND_MOVE_TO_SHIPPING_METHOD);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateParameters());
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
					askRelogin(ShippingAddressVerification.this);
					if (null != pd && pd.isShowing()) {
						pd.dismiss();
					}
				} else {
					try {
						notifyUser(getErrorMessage(),
								ShippingAddressVerification.this);
						if (null != pd && pd.isShowing()) {
							pd.dismiss();
						}
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
						if (null != pd && pd.isShowing()) {
							pd.dismiss();
						}
					}

				}
			} else {
				Logger.Log("<ShippingAddressHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));

				CheckoutShippmentMethodBean ultaBean = (CheckoutShippmentMethodBean) getResponseBean();
				if (null != ultaBean) {
					if (null != ultaBean.getComponent()
							.getShippingMethodsAndRedeemLevels()
							.getAvailableShippingMethods()) {
						// String
						// method=ultaBean.getComponent().getShippingMethodsAndRedeemLevels().getAvailableShippingMethods().get(0).getShippingMethod();
						List<String> errors = ultaBean.getErrorInfos();
						if (null != errors && !(errors.isEmpty())) {
							try {
								notifyUser(errors.get(0),
										ShippingAddressVerification.this);
								if (null != pd && pd.isShowing()) {
									pd.dismiss();
								}
							} catch (WindowManager.BadTokenException e) {
							} catch (Exception e) {
								if (null != pd && pd.isShowing()) {
									pd.dismiss();
								}
							}
						} else {
							if (null != pd && pd.isShowing()) {
								pd.dismiss();
							}
							Intent goToShippingMethod = new Intent(
									ShippingAddressVerification.this,
									ShippingMethodActivity.class);
							goToShippingMethod.putExtra("ShippingMethodBean",
									ultaBean);
							startActivity(goToShippingMethod);

						}
					} else {
						try {
							notifyUser("Enter Valid details",
									ShippingAddressVerification.this);
							if (null != pd && pd.isShowing()) {
								pd.dismiss();
							}
						} catch (WindowManager.BadTokenException e) {
						} catch (Exception e) {
						}

					}
				}
			}

		}
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			// if (mInvokeVerifiedAddress) {
			// invokeVerifiedAddress();
			// } else if (mInvokeSetAddressAndCommitOrder) {
			// invokeSetAddressAndCommitOrder();
			// }
			navigateToBasketOnSessionTimeout(ShippingAddressVerification.this);
		} else {
			if (null != pd && pd.isShowing()) {

				pd.dismiss();
			}
		}
	}

	private void creatingPageName() {
		String pageName = "";
		if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
			pageName = WebserviceConstants.CHECKOUT_SHIPPING_ADDRESS_VERIFICATION_GUEST_MEMBER;
		} else if (Utility.retrieveBooleanFromSharedPreference(
				UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
				getApplicationContext())) {
			pageName = WebserviceConstants.CHECKOUT_SHIPPING_ADDRESS_VERIFICATION_LOYALITY_MEMBER;
		} else {
			pageName = WebserviceConstants.CHECKOUT_SHIPPING_ADDRESS_VERIFICATION_NON_LOYALITY_MEMBER;
		}

		trackAppState(ShippingAddressVerification.this, pageName);

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

}
