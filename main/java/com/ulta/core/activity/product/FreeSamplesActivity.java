/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.activity.checkout.AddShippingAddressGuestUserActivity;
import com.ulta.core.activity.checkout.AddShippingAddressLogginUserActivity;
import com.ulta.core.activity.checkout.PayPalWebviewActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.product.FreeSamplesBean;
import com.ulta.core.bean.product.FreeSamplesDetailBean;
import com.ulta.core.bean.product.SampleDetailsBean;
import com.ulta.core.bean.product.SkuDetailsBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class FreeSamplesActivity.
 */
public class FreeSamplesActivity extends UltaBaseActivity implements
		OnClickListener {
	boolean limit = false;
	/** The list. */
	private static List<Model> list;

	// /** The lv sample list view. */
	// private GridView freeSampleGridView;

	/** The lv sample list view. */
	private ListView freeSampleListView;

	/** The free sample list adapter. */
	private FreeSampleListAdapter freeSampleListAdapter;

	/** The list of free samples detail bean. */
	private List<FreeSamplesDetailBean> listOfFreeSamplesDetailBean;

	/** The list of normal free samples */
	private List<FreeSamplesDetailBean> listOfFreeSamples;

	/*
	 * private HashMap<String, String> hmWithFreeSamples;
	 */
	/** The free samples bean. */
	private FreeSamplesBean freeSamplesBean;

	/** The list of samples. */
	private List<String> listOfSamples;
	private String tokenId;

	private List<String> ListofSamplesSelected = new ArrayList<String>();
	/** Additional Free sample views */
	private CheckBox chkAdditionalFreeSample;
	private FreeSamplesDetailBean additionalFreeSample;
	private FrameLayout loadingLayout;
	private FrameLayout additionalSampleLayout;
	private boolean isAdditionalSampleSelected = true;
	// private UemAction fetchSamplesAction;
	// private UemAction addFreeSamplesAction;

	private TextView mFourthSampleHeading;

	private ImageButton mNoThanksButton;
	private ImageButton mContinueButton;
	ImageView imgAdditionalFreeSample;
	private LinearLayout mAdditionalFreeSampleLinearLayout;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freesamples);
		setViews();
		displayContinueButton();
		creatingPageName();
		setOnClickListeners();
		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getStringArrayList(
					"FreeSamplesList")) {
				ListofSamplesSelected = getIntent().getExtras()
						.getStringArrayList("FreeSamplesList");
				for (String s : ListofSamplesSelected) {
					Logger.Log("Naveen0---" + s);
				}
			}
			if (null != getIntent().getExtras().getString("token")) {
				tokenId = getIntent().getExtras().getString("token");
			}
		}

		trackAppAction(FreeSamplesActivity.this,
				WebserviceConstants.CHECKOUT_STEP_1_EVENT_ACTION);

		trackAppAction(FreeSamplesActivity.this,
				WebserviceConstants.CHECKOUT_STEP_1_VISIT_EVENT_ACTION);

		// UltaDataCache.getDataCacheInstance().setMovingBackInChekout(false);
		loadingLayout.setVisibility(View.VISIBLE);
		// fetchSamplesAction = UemAction
		// .enterAction(WebserviceConstants.ACTION_FETCH_SAMPLES_INVOCATION);
		fnInvokeRetrieveFreeSamples();
	}

	@Override
	public void onBackPressed() {
		UltaDataCache.getDataCacheInstance().setAnonymousCheckout(false);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	 * @Override protected void onResume() { super.onResume();
	 * loadingLayout.setVisibility(View.GONE); }
	 */

	/**
	 * The Class FreeSampleListAdapter.
	 */
	public class FreeSampleListAdapter extends BaseAdapter {

		/** The context. */
		private Context context;

		/**
		 * Instantiates a new free sample list adapter.
		 * 
		 * @param context
		 *            the context
		 */
		public FreeSampleListAdapter(Context context) {
			this.context = context;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			FreeSamplesDetailBean freeSamplesDetailBean = listOfFreeSamples
					.get(position);
			SampleDetailsBean sampleDetailsBean = freeSamplesDetailBean
					.getSampleDetails();
			SkuDetailsBean skuDetailsBean = freeSamplesDetailBean
					.getSkuDetails();
			final ViewHolder viewHolder;
			View view = null;
			LayoutInflater inflator = ((UltaBaseActivity) context)
					.getLayoutInflater();
			view = inflator.inflate(R.layout.new_free_samples_inflate_layout,
					null);
			viewHolder = new ViewHolder();
			viewHolder.imgSampleImage = (ImageView) view
					.findViewById(R.id.imgSampleImage);
			viewHolder.chkSampleSelect = (CheckBox) view
					.findViewById(R.id.chkSampleSelect);
			Drawable drawable = getResources().getDrawable(
					R.drawable.custom_btn_radio);
			drawable.setBounds(0, 0, 72, 72);
			viewHolder.chkSampleSelect
					.setButtonDrawable(android.R.color.transparent);
			viewHolder.chkSampleSelect.setCompoundDrawables(null, null,
					drawable, null);
			viewHolder.chkSampleSelect.setPadding(25, 25, 3, 10);
			for (int i = 0; i < ListofSamplesSelected.size(); i++) {
				Logger.Log("Naveen2-" + ListofSamplesSelected.size()
						+ ListofSamplesSelected.toString());
				Logger.Log("NAveen2-" + isAdditionalSampleSelected);
				if (ListofSamplesSelected.get(i).equals(skuDetailsBean.getId())) {
					viewHolder.chkSampleSelect.setChecked(true);
					mNoThanksButton.setEnabled(false);
					mNoThanksButton
							.setBackgroundResource(R.drawable.no_thanks_disabled);
				} else {
					viewHolder.chkSampleSelect.setChecked(false);
				}
			}

			viewHolder.imgSampleImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.chkSampleSelect.setChecked(true);
					mNoThanksButton
							.setBackgroundResource(R.drawable.no_thanks_disabled);

				}
			});

			viewHolder.chkSampleSelect
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							Model element = (Model) viewHolder.chkSampleSelect
									.getTag();
							if (isChecked) {
								Logger.Log("Naveen3--"
										+ ListofSamplesSelected.size()
										+ ListofSamplesSelected.toString());
								Logger.Log("NAveen3--"
										+ isAdditionalSampleSelected);
								if (ListofSamplesSelected.size() == 1) {
									viewHolder.chkSampleSelect
											.setChecked(false);

									final Dialog alertDialog = showAlertDialog(
											FreeSamplesActivity.this,
											"Alert",
											"You can select only 1 free sample for adding to the cart.",
											"OK", "");
									alertDialog.show();
									mDisagreeButton.setVisibility(View.GONE);
									mAgreeButton
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													alertDialog.dismiss();
												}
											});

									/*
									 * AlertDialog.Builder alertDialog = new
									 * AlertDialog.Builder(
									 * FreeSamplesActivity.this);
									 * alertDialog.setTitle("Alert");
									 * alertDialog .setMessage(
									 * "You can select only 1 free sample for adding to the cart."
									 * ); alertDialog .setPositiveButton( "OK",
									 * new DialogInterface.OnClickListener() {
									 * 
									 * @Override public void onClick(
									 * DialogInterface dialog, int which) {
									 * dialog.dismiss(); } });
									 * alertDialog.show();
									 */
								} else {
									int selectedposition = -1;
									for (int i = 0; i < ListofSamplesSelected
											.size(); i++) {
										if (ListofSamplesSelected
												.get(i)
												.equals(listOfFreeSamplesDetailBean
														.get(position)
														.getSkuDetails()
														.getId())) {
											selectedposition = i;
										}
									}
									if (selectedposition == -1) {

										ListofSamplesSelected
												.add(listOfFreeSamplesDetailBean
														.get(position)
														.getSkuDetails()
														.getId());
										element.setSelected(true);
										mNoThanksButton.setEnabled(false);
										mNoThanksButton
												.setBackgroundResource(R.drawable.no_thanks_disabled);
									}
								}

							} else {
								for (int i = 0; i < ListofSamplesSelected
										.size(); i++) {
									if (ListofSamplesSelected.get(i).equals(
											listOfFreeSamplesDetailBean
													.get(position)
													.getSkuDetails().getId())) {
										ListofSamplesSelected.remove(i);
									}
								}
								element.setSelected(false);
								mNoThanksButton.setEnabled(true);
								// mNoThanksButton.setText("");
								mNoThanksButton
										.setBackgroundResource(R.drawable.no_thanks);
							}
							Logger.Log("Naveen4--"
									+ ListofSamplesSelected.size()
									+ ListofSamplesSelected.toString());
							Logger.Log("NAveen4--" + isAdditionalSampleSelected);
						}

					});
			viewHolder.chkSampleSelect.setTag(list.get(position));
			viewHolder.imgSampleImage.setTag(list.get(position));

			String imageUrl = createFreeSamplesUrl(
					sampleDetailsBean.getSmallImageUrl(), false);

			new AQuery(viewHolder.imgSampleImage).image(imageUrl, false, false,
					0, R.drawable.dummy_product, null, AQuery.FADE_IN);
			for (int i = 0; i < ListofSamplesSelected.size(); i++) {
				if (ListofSamplesSelected.get(i).equals(skuDetailsBean.getId())) {
					viewHolder.chkSampleSelect.setChecked(true);
					Model element = (Model) viewHolder.chkSampleSelect.getTag();
					element.setSelected(true);
				}
			}

			if (null != viewHolder) {
				viewHolder.chkSampleSelect.setChecked(list.get(position)
						.isSelected());

			}
			return view;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return (listOfFreeSamples != null && listOfFreeSamples.isEmpty()) ? 0
					: listOfFreeSamples.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	/**
	 * The Class ViewHolder.
	 */
	static class ViewHolder {
		/** The img sample image. */
		protected ImageView imgSampleImage;

		/** The chk sample select. */
		protected CheckBox chkSampleSelect;
	}

	/**
	 * The Class Model.
	 */
	public class Model {

		/** The selected. */
		private boolean selected;

		/**
		 * Instantiates a new model.
		 * 
		 * @param selected
		 *            the selected
		 */
		public Model(boolean selected) {
			this.selected = selected;
		}

		/**
		 * Checks if is selected.
		 * 
		 * @return true, if is selected
		 */
		public boolean isSelected() {
			return selected;
		}

		/**
		 * Sets the selected.
		 * 
		 * @param selected
		 *            the new selected
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}

	/**
	 * Fn check if logged in.
	 */
	public void fnCheckIfLoggedIn() {
		if (UltaDataCache.getDataCacheInstance().isLoggedIn() == true) {
			/*
			 * Intent intentForCheckoutActivity = new Intent(
			 * FreeSamplesActivity.this, ShippingAdressActivity.class);
			 * intentForCheckoutActivity
			 * .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * startActivity(intentForCheckoutActivity);
			 */
			loadingLayout.setVisibility(View.GONE);
			Intent intentForCheckoutActivity = new Intent(
					FreeSamplesActivity.this,
					AddShippingAddressLogginUserActivity.class);
			intentForCheckoutActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentForCheckoutActivity);
		} else {
			/*
			 * Intent intentForCheckoutActivity = new Intent(
			 * FreeSamplesActivity.this, ShippingAdressActivity.class);
			 * startActivityForResult(intentForCheckoutActivity, 1);
			 */
			loadingLayout.setVisibility(View.GONE);
			Intent intentForCheckoutActivity = new Intent(
					FreeSamplesActivity.this,
					AddShippingAddressLogginUserActivity.class);
			startActivityForResult(intentForCheckoutActivity, 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				Bundle extras = intent.getExtras();
				if (extras.getString("success").equals("success")) {
					if (UltaDataCache.getDataCacheInstance().isLoggedIn() == true) {
						/*
						 * Intent intentForCheckoutActivity = new Intent(
						 * FreeSamplesActivity.this,
						 * ShippingAdressActivity.class);
						 * intentForCheckoutActivity
						 * .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						 * startActivity(intentForCheckoutActivity);
						 */
						loadingLayout.setVisibility(View.GONE);
						Intent intentForCheckoutActivity = new Intent(
								FreeSamplesActivity.this,
								AddShippingAddressLogginUserActivity.class);
						intentForCheckoutActivity
								.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intentForCheckoutActivity);
					} else {
						/*
						 * Intent intentForCheckoutActivity = new Intent(
						 * FreeSamplesActivity.this,
						 * ShippingAdressActivity.class);
						 * startActivityForResult(intentForCheckoutActivity, 1);
						 */
						loadingLayout.setVisibility(View.GONE);
						Intent intentForCheckoutActivity = new Intent(
								FreeSamplesActivity.this,
								AddShippingAddressLogginUserActivity.class);
						startActivityForResult(intentForCheckoutActivity, 1);
					}
				}
				break;
			}
		}
	}

	/**
	 * Sets the views.
	 */
	private void setViews() {
		freeSampleListView = (ListView) findViewById(R.id.freeSamplesListView);
		chkAdditionalFreeSample = (CheckBox) findViewById(R.id.chkAdditionalSample);
		Drawable drawable = getResources().getDrawable(
				R.drawable.custom_checkbox);
		drawable.setBounds(0, 0, 72, 72);
		chkAdditionalFreeSample.setButtonDrawable(android.R.color.white);
		chkAdditionalFreeSample
				.setCompoundDrawables(null, null, drawable, null);
		chkAdditionalFreeSample.setPadding(25, 25, 3, 10);
		loadingLayout = (FrameLayout) findViewById(R.id.freeSampleLoadingLayout);
		additionalSampleLayout = (FrameLayout) findViewById(R.id.framLayoutForAdditionalSample);
		mFourthSampleHeading = (TextView) findViewById(R.id.fourthSampleMessage);
		mNoThanksButton = (ImageButton) findViewById(R.id.noThanksButton);
		mContinueButton = (ImageButton) findViewById(R.id.continueButton);
		imgAdditionalFreeSample = (ImageView) findViewById(R.id.imgAdditionalSample);
		mAdditionalFreeSampleLinearLayout = (LinearLayout) findViewById(R.id.additionalFreeSampleLayout);
		mNoThanksButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String removeSkus = null;
				if (additionalFreeSample != null && isAdditionalSampleSelected) {
					removeSkus = additionalFreeSample.getSkuDetails().getId();
				}
				loadingLayout.setVisibility(View.VISIBLE);
				// addFreeSamplesAction = UemAction
				// .enterAction(WebserviceConstants.ACTION_ADD_SAMPLES_INVOCATION);
				fnInvokeAddFreeSamples(removeSkus);
			}
		});

		mContinueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onContinueClicked();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

	}

	/**
	 * Sets the on click listeners.
	 */
	private void setOnClickListeners() {
		// btnContinueToCheckOut.setOnClickListener(this);
	}

	/**
	 * Check for login.
	 * 
	 * @return true, if successful
	 */
	private boolean checkForLogin() {

		if (UltaDataCache.getDataCacheInstance().isExpressCheckout()) {
			startPayPalActivity();
		} else if (isUltaCustomer(FreeSamplesActivity.this)) {
			/*
			 * Intent intentForLoginActivity = new Intent(
			 * FreeSamplesActivity.this, ShippingAdressActivity.class);
			 * startActivity(intentForLoginActivity); finish();
			 */
			Intent intentForLoginActivity = new Intent(
					FreeSamplesActivity.this,
					AddShippingAddressLogginUserActivity.class);
			startActivity(intentForLoginActivity);

		}
		// 3.2 release
		else if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
			/*
			 * Intent enterShippingAddress = new
			 * Intent(FreeSamplesActivity.this,
			 * AddNewShippingAddressActivity.class);
			 */
			Intent enterShippingAddress = new Intent(FreeSamplesActivity.this,
					AddShippingAddressGuestUserActivity.class);
			startActivity(enterShippingAddress);

		} else {
			// take user to Sign in page
			Intent intentForLogin = new Intent(FreeSamplesActivity.this,
					LoginActivity.class);
			intentForLogin.putExtra("origin", "basket");
			intentForLogin.putExtra("fromCehckout", 1);
			// startActivityForResult(intentForLogin, REQ_CODE_LOGIN);
			startActivity(intentForLogin);
			finish();
		}
		return false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		loadingLayout.setVisibility(View.GONE);
	}

	private void startPayPalActivity() {
		Intent startPaypalIntent = new Intent(FreeSamplesActivity.this,
				PayPalWebviewActivity.class);
		Log.e("PayPal", "TokenId from web service: " + tokenId);
		startPaypalIntent.putExtra("token", tokenId);
		startActivity(startPaypalIntent);
		finish();
	}

	/*
	 * Service to retrieve free samples
	 */

	/**
	 * Fn invoke retrieve free samples.
	 */
	private void fnInvokeRetrieveFreeSamples() {
		InvokerParams<FreeSamplesBean> invokerParams = new InvokerParams<FreeSamplesBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.FETCHFREESAMPLES_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(fnPopulateRetrievFreeSamplesHandlerParameters());
		invokerParams.setUltaBeanClazz(FreeSamplesBean.class);
		RetrieveFreeSamplesHandler retrieveFreeSamplesHandler = new RetrieveFreeSamplesHandler();
		invokerParams.setUltaHandler(retrieveFreeSamplesHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<FreeSamplesActivity><fnInvokeRetrieveFreeSamples()><UltaException>>"
					+ ultaException);
		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return the map
	 */
	private Map<String, String> fnPopulateRetrievFreeSamplesHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-depth", "1");
		return urlParams;
	}

	/*
	 * Handler to handle the response from the web service
	 */
	/**
	 * The Class RetrieveFreeSamplesHandler.
	 */
	public class RetrieveFreeSamplesHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveFreeSamplesHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				try {
					loadingLayout.setVisibility(View.GONE);
					// fetchSamplesAction
					// .reportError(
					// WebserviceConstants.FORM_EXCEPTION_OCCURED,
					// WebserviceConstants.DYN_ERRCODE_FREE_SAMPLE_ACTIVITY);
					// fetchSamplesAction.leaveAction();
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							FreeSamplesActivity.this);
					setError(FreeSamplesActivity.this, getErrorMessage());
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<RetrieveFreeSamplesHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				freeSamplesBean = (FreeSamplesBean) getResponseBean();

				if (null != freeSamplesBean) {
					// tvSampleSelectedNumber.setText("dummy text");
					if (null != freeSamplesBean.getErrorInfos()) {
						setError(FreeSamplesActivity.this, freeSamplesBean
								.getErrorInfos().get(0));
					}
					listOfFreeSamplesDetailBean = freeSamplesBean
							.getFreeSamples();
					listOfFreeSamples = new ArrayList<FreeSamplesDetailBean>();
					if (null != listOfFreeSamplesDetailBean) {
						list = new ArrayList<FreeSamplesActivity.Model>();
						for (int i = 0; i < listOfFreeSamplesDetailBean.size(); i++) {
							if (!listOfFreeSamplesDetailBean.get(i)
									.isCompulsoryFreeSample()) {
								listOfFreeSamples
										.add(listOfFreeSamplesDetailBean.get(i));
								Model model = new Model(false);
								list.add(model);
							} else {
								additionalFreeSample = listOfFreeSamplesDetailBean
										.get(i);
								if (null != additionalFreeSample
										.getSkuDetails()) {
									if (ListofSamplesSelected
											.contains(additionalFreeSample
													.getSkuDetails().getId())) {
										ListofSamplesSelected
												.remove(additionalFreeSample
														.getSkuDetails()
														.getId());
									}
								}

							}
						}
					}

					if (null == freeSampleListAdapter) {
						freeSampleListAdapter = new FreeSampleListAdapter(
								FreeSamplesActivity.this);
					}
					freeSampleListView.setVisibility(View.VISIBLE);
					freeSampleListView.setAdapter(freeSampleListAdapter);
					if (additionalFreeSample != null) {
						mAdditionalFreeSampleLinearLayout
								.setVisibility(View.VISIBLE);
						additionalSampleLayout.setVisibility(View.VISIBLE);
						mFourthSampleHeading.setVisibility(View.VISIBLE);
						setAdditionalFreeSampleUI();
					} else {
						mAdditionalFreeSampleLinearLayout
								.setVisibility(View.GONE);
						loadingLayout.setVisibility(View.GONE);
						additionalSampleLayout.setVisibility(View.GONE);
						mFourthSampleHeading.setVisibility(View.GONE);
					}
				}
				// fetchSamplesAction.reportEvent("Fetching samples completed");
				// fetchSamplesAction.leaveAction();
			}
		}
	}

	/*
	 * End of service
	 */

	/*
	 * Service to add free samples
	 */

	/**
	 * Fn invoke add free samples.
	 * 
	 * @param skuIds
	 *            the sku ids
	 */
	private void fnInvokeAddFreeSamples(String skuIds) {
		InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.ADD_FREESAMPLES_FORMOBILE_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(fnPopulateAddFreeSamplesHandlerParameters(skuIds));
		invokerParams.setUltaBeanClazz(UltaBean.class);
		AddFreeSamplesHandler addFreeSamplesHandler = new AddFreeSamplesHandler();
		invokerParams.setUltaHandler(addFreeSamplesHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<FreeSamplesActivity><fnInvokeAddFreeSamples()><UltaException>>"
					+ ultaException);
		}
	}

	public void setAdditionalFreeSampleUI() {

		chkAdditionalFreeSample.setChecked(true);
		isAdditionalSampleSelected = true;
		chkAdditionalFreeSample
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isAdditionalSampleSelected = true;
						} else {
							isAdditionalSampleSelected = false;

						}
					}
				});

		String imageUrl = createFreeSamplesUrl(additionalFreeSample
				.getSampleDetails().getSmallImageUrl(), true);

		new AQuery(imgAdditionalFreeSample).image(imageUrl, false, false, 0,
				R.drawable.dummy_product, null, AQuery.FADE_IN);
		loadingLayout.setVisibility(View.GONE);

		// new
		// DownloadImageTask(imgAdditionalFreeSample).execute(additionalFreeSample.getSampleDetails()
		// .getSmallImageUrl());

		// new DownloadImageTask(imgAdditionalFreeSample)
		// .execute(additionalFreeSample.getSampleDetails()
		// .getSmallImageUrl());

	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @param skuIds
	 *            the sku ids
	 * @return the map
	 */
	private Map<String, String> fnPopulateAddFreeSamplesHandlerParameters(
			String skuIds) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		Logger.Log("skuIds ::" + skuIds);
		urlParams.put("skuIds", skuIds);
		if (null != skuIds) {
			UltaDataCache.getDataCacheInstance().setFreeSampleSelected(true);
		} else {
			UltaDataCache.getDataCacheInstance().setFreeSampleSelected(false);
		}

		return urlParams;
	}

	/*
	 * Handler to handle the response from the web service
	 */
	/**
	 * The Class AddFreeSamplesHandler.
	 */
	public class AddFreeSamplesHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveFreeSamplesHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}

			if (null != getErrorMessage()) {
				try {
					// addFreeSamplesAction
					// .reportError(
					// WebserviceConstants.FORM_EXCEPTION_OCCURED,
					// WebserviceConstants.DYN_ERRCODE_FREE_SAMPLE_ACTIVITY);
					// addFreeSamplesAction.leaveAction();
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							FreeSamplesActivity.this);
					setError(FreeSamplesActivity.this, getErrorMessage());
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<RetrieveFreeSamplesHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				UltaBean ultaBean = (UltaBean) getResponseBean();
				if (null != ultaBean) {
					checkForLogin();
				}
				// addFreeSamplesAction.leaveAction();
			}
		}
	}

	public void onContinueClicked() {

		if (ListofSamplesSelected.size() == 0) {
			/*
			 * final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			 * alert.setPositiveButton("No Thanks", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * {String removeSkus = null; if (additionalFreeSample != null &&
			 * isAdditionalSampleSelected) { removeSkus = additionalFreeSample
			 * .getSkuDetails().getId(); }
			 * loadingLayout.setVisibility(View.VISIBLE); //
			 * addFreeSamplesAction = UemAction //
			 * .enterAction(WebserviceConstants.ACTION_ADD_SAMPLES_INVOCATION);
			 * fnInvokeAddFreeSamples(removeSkus); } });
			 * alert.setNegativeButton("OK", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { dialog.dismiss(); } });
			 * alert.setMessage("Select Free Samples"); alert.create().show();
			 */

			final Dialog alert = showAlertDialog(FreeSamplesActivity.this,
					"Alert", "Select Free Samples", "OK", "No Thanks");
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

					String removeSkus = null;
					if (additionalFreeSample != null
							&& isAdditionalSampleSelected) {
						removeSkus = additionalFreeSample.getSkuDetails()
								.getId();
					}
					loadingLayout.setVisibility(View.VISIBLE);
					// addFreeSamplesAction = UemAction
					// .enterAction(WebserviceConstants.ACTION_ADD_SAMPLES_INVOCATION);
					fnInvokeAddFreeSamples(removeSkus);
					alert.dismiss();

				}
			});

		} else {
			listOfSamples = new ArrayList<String>();
			for (int loop = 0; loop < listOfFreeSamplesDetailBean.size(); loop++) {
				FreeSamplesDetailBean freeSamplesDetailBean = listOfFreeSamplesDetailBean
						.get(loop);
				for (int i = 0; i < ListofSamplesSelected.size(); i++) {
					if (ListofSamplesSelected.get(i).equals(
							freeSamplesDetailBean.getSkuDetails().getId())) {
						listOfSamples.add(freeSamplesDetailBean.getSkuDetails()
								.getId());
					}
				}
			}
			String skuIds = null;
			if (listOfSamples.size() == 1) {
				skuIds = listOfSamples.get(0);
			}
			if (listOfSamples.size() == 1 && additionalFreeSample != null
					&& isAdditionalSampleSelected) {
				skuIds = listOfSamples.get(0) + ","
						+ additionalFreeSample.getSkuDetails().getId();
			}
			loadingLayout.setVisibility(View.VISIBLE);
			// addFreeSamplesAction = UemAction
			// .enterAction(WebserviceConstants.ACTION_ADD_SAMPLES_INVOCATION);
			fnInvokeAddFreeSamples(skuIds);
		}

	}

	public String createFreeSamplesUrl(String url, boolean isFourthSample) {

		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append(url);

		int density = getResources().getDisplayMetrics().densityDpi;

		if (density >= 400) {
			// "xxxhdpi";

			if (isFourthSample) {
				urlBuffer.append(WebserviceConstants.FOURTH_APPEND_TO_XXXHDPI);
			} else {
				urlBuffer.append(WebserviceConstants.APPEND_TO_XXXHDPI);
			}

		} else if (density >= 300 && density < 400) {
			// xxhdpi

			if (isFourthSample) {
				urlBuffer.append(WebserviceConstants.FOURTH_APPEND_TO_XXHDPI);
			} else {
				urlBuffer.append(WebserviceConstants.APPEND_TO_XXHDPI);
			}

		} else if (density >= 200 && density < 300) {
			// xhdpi

			if (isFourthSample) {
				urlBuffer.append(WebserviceConstants.FOURTH_APPEND_TO_XHDPI);
			} else {
				urlBuffer.append(WebserviceConstants.APPEND_TO_XHDPI);
			}

		} else if (density >= 150 && density < 200) {
			// hdpi

			if (isFourthSample) {
				urlBuffer.append(WebserviceConstants.FOURTH_APPEND_TO_HDPI);
			} else {
				urlBuffer.append(WebserviceConstants.APPEND_TO_HDPI);
			}

		} else if (density >= 100 && density < 150) {
			// mdpi

			if (isFourthSample) {
				urlBuffer.append(WebserviceConstants.FOURTH_APPEND_TO_MDPI);
			} else {
				urlBuffer.append(WebserviceConstants.APPEND_TO_MDPI);
			}

		} else {
			// default hdpi
			if (isFourthSample) {
				urlBuffer.append(WebserviceConstants.FOURTH_APPEND_TO_HDPI);
			} else {
				urlBuffer.append(WebserviceConstants.APPEND_TO_HDPI);
			}
		}

		return urlBuffer.toString();
	}

	private void creatingPageName() {
		String pageName = "";
		if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
			pageName = WebserviceConstants.CHECKOUT_FREESAMPLES_GUEST_MEMBER;
		} else if (Utility.retrieveBooleanFromSharedPreference(
				UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
				getApplicationContext())) {
			pageName = WebserviceConstants.CHECKOUT_FREESAMPLES_LOYALITY_MEMBER;
		} else {
			pageName = WebserviceConstants.CHECKOUT_FREESAMPLES_NON_LOYALITY_MEMBER;
		}

		trackAppState(FreeSamplesActivity.this, pageName);

	}
}
