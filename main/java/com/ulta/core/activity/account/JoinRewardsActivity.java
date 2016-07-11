/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.rewards.RewardsActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.checkout.StateListBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinRewardsActivity extends UltaBaseActivity implements
		OnDoneClickedListener, TextWatcher {
	/** The spinner city. */
	Spinner spinnerCity;

	private List<String> result;
	/** The form layout. */
	LinearLayout signUp, membershipId, loadingDialog;

	/** The loading layout. */
	ScrollView mainLayout;
	// Button btnSubmit;
	/** The state list bean. */
	StateListBean stateListBean;
	/** The edt city. */
	EditText edtFirstName, edtLastName, edtAddressLine1, edtAddressLine2,
			edtPhone, edtZipCode, edtCity, edtUserName, edtConfirmUserName,
			edtDateOfBirth, editId;
	private String[] anArrayOfStrings;
	private boolean isSpinnerSelected = false;
	String strSelectedState1;
	String strState1;
	String stateValue;
	int stateLocation = 0;
	private RadioGroup radioGrp;
	/** The radio member. */
	private RadioButton radioSign, radioMember;
	private boolean member = false;
	private Button mDoneButton;

	private boolean sign = true;
	private static String CONFIRM_EMAIL_VALIDATION_MESSAGE = "Supplied email ids do not match";
	private TextView userNameErrorText, confirmUserNameErrorText,
			firstNameErrorText, lastNameErrorText, phoneErrorText,
			address1ErrorText, cityErrorText, stateErrorText, zipErrorText,idErrorText;
	private Drawable originalDrawable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_rewards);
		setTitle("Join Rewards");
		initViews();
		invokeStateList();
	}

	private void setViews() {
		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().get("userName"))
				edtUserName.setText(getIntent().getExtras().get("userName")
						.toString());
			if (null != getIntent().getExtras().get("firstName"))
				edtFirstName.setText(getIntent().getExtras().get("firstName")
						.toString());
			if (null != getIntent().getExtras().get("lastName"))
				edtLastName.setText(getIntent().getExtras().get("lastName")
						.toString());
			if (null != getIntent().getExtras().get("address1"))
				edtAddressLine1.setText(getIntent().getExtras().get("address1")
						.toString());
			if (null != getIntent().getExtras().get("address2"))
				edtAddressLine2.setText(getIntent().getExtras().get("address2")
						.toString());
			if (null != getIntent().getExtras().get("city")) {
				edtCity.setText(getIntent().getExtras().get("city").toString());
				isSpinnerSelected = true;
			}
			if (null != getIntent().getExtras().get("strZip"))
				edtZipCode.setText(getIntent().getExtras().get("strZip")
						.toString());
			if (null != getIntent().getExtras().get("phone")) {
				String phone = getIntent().getExtras().get("phone").toString();
				if (phone.contains("-")) {
					phone = phone.replaceAll("-", "");
					edtPhone.setText(phone);
				} else {
					edtPhone.setText(getIntent().getExtras().get("phone")
							.toString());
				}
			}
			if (null != getIntent().getExtras().get("dob"))
				edtDateOfBirth.setText(getIntent().getExtras().get("dob")
						.toString());
			if (null != getIntent().getExtras().get("state")) {
				for (int i = 0; i < result.size(); i++) {
					if (result.get(i).equalsIgnoreCase(
							getIntent().getExtras().get("state").toString())) {
						stateLocation = i;
					}
				}
			}
		}
		mainLayout.setVisibility(View.VISIBLE);
	}

	private void initViews() {
		// btnSubmit=(Button)findViewById(R.id.title_bar_done_icon);
		// btnSubmit.setVisibility(View.GONE);
		mainLayout = (ScrollView) findViewById(R.id.joinRewardLayout);
		edtFirstName = (EditText) findViewById(R.id.f_name);
		edtFirstName.addTextChangedListener(this);
		edtLastName = (EditText) findViewById(R.id.l_name);
		edtLastName.addTextChangedListener(this);
		edtAddressLine1 = (EditText) findViewById(R.id.al_1);
		edtAddressLine1.addTextChangedListener(this);
		edtAddressLine2 = (EditText) findViewById(R.id.al_2);
		edtPhone = (EditText) findViewById(R.id.phone);
		edtPhone.addTextChangedListener(this);
		edtZipCode = (EditText) findViewById(R.id.zip_code);
		edtZipCode.addTextChangedListener(this);
		edtCity = (EditText) findViewById(R.id.city);
		edtCity.addTextChangedListener(this);
		edtUserName = (EditText) findViewById(R.id.user_name);
		edtUserName.addTextChangedListener(this);
		edtConfirmUserName = (EditText) findViewById(R.id.confirm_user_name);
		edtConfirmUserName.addTextChangedListener(this);
		edtDateOfBirth = (EditText) findViewById(R.id.dob);
		spinnerCity = (Spinner) findViewById(R.id.spinner1);
		radioMember = (RadioButton) findViewById(R.id.radioMember);
		editId = (EditText) findViewById(R.id.editId);
		editId.addTextChangedListener(this);
		radioSign = (RadioButton) findViewById(R.id.radioSign);
		signUp = (LinearLayout) findViewById(R.id.lytSignUp);
		membershipId = (LinearLayout) findViewById(R.id.lytMembershipId);
		radioGrp = (RadioGroup) findViewById(R.id.joinRewards_radioGrp);
		mDoneButton = (Button) findViewById(R.id.doneBtn);
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);
		userNameErrorText = (TextView) findViewById(R.id.userNameErrorText);
		confirmUserNameErrorText = (TextView) findViewById(R.id.confirmUserNameErrorText);
		firstNameErrorText = (TextView) findViewById(R.id.firstNameErrorText);
		lastNameErrorText = (TextView) findViewById(R.id.lastNameErrorText);
		phoneErrorText = (TextView) findViewById(R.id.phoneErrorText);
		address1ErrorText = (TextView) findViewById(R.id.address1ErrorText);
		cityErrorText = (TextView) findViewById(R.id.cityErrorText);
		stateErrorText = (TextView) findViewById(R.id.stateErrorText);
		zipErrorText = (TextView) findViewById(R.id.zipErrorText);
		idErrorText=(TextView) findViewById(R.id.idErrorText);
		originalDrawable = edtZipCode.getBackground();
		radioGrp.clearCheck();
		radioMember.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// boolean member;
				if (radioMember.isChecked() == true) {
					member = true;
					sign = false;
					signUp.setVisibility(View.GONE);
					membershipId.setVisibility(View.VISIBLE);

					// membershipId.setVisibility(View.VISIBLE);
				}
			}
		});

		radioSign.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (radioSign.isChecked() == true) {
					sign = true;
					member = false;
					signUp.setVisibility(View.VISIBLE);
					membershipId.setVisibility(View.GONE);
				}
			}
		});

		mDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onDoneClicked();
			}
		});
	}

	@Override
	public void onDoneClicked() {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(editId.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		if (sign && isValidationSuccess()) {
			mainLayout.setVisibility(View.GONE);
			loadingDialog.setVisibility(View.VISIBLE);
			fnInvokeJoinRewardsProgramme();
		} else {
			if (member == true && editId.getText().toString().length() == 0) {

				try {
					/*notifyUser("Please enter Beauty Club Membership Id",
							JoinRewardsActivity.this);*/
					setError(editId, idErrorText, "Please enter Beauty Club Membership Id");
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else if ((member == true && editId.getText().toString().length() != 0)) {
				mainLayout.setVisibility(View.GONE);
				fnInvokeJoinRewardsProgramme();
			}
		}
	}

	private boolean isValidationSuccess() {
		if (edtUserName.getText().toString().equalsIgnoreCase("")
				|| edtUserName.getText().toString() == null) {
			setError(edtUserName, userNameErrorText,
					"Please fill the email field");
			/*
			 * notifyUser("Please fill the email field",
			 * JoinRewardsActivity.this);
			 */
			edtUserName.requestFocus();
			return false;
		} else if (edtConfirmUserName.getText().toString().equalsIgnoreCase("")
				|| edtConfirmUserName.getText().toString() == null) {
			setError(edtConfirmUserName, confirmUserNameErrorText,
					"Please fill the confirm email field");
			/*
			 * notifyUser("Please fill the confirm email field",
			 * JoinRewardsActivity.this);
			 */
			edtConfirmUserName.requestFocus();
			return false;
		} else if (!edtUserName.getText().toString()
				.equals(edtConfirmUserName.getText().toString())) {
			/*
			 * notifyUser(CONFIRM_EMAIL_VALIDATION_MESSAGE,
			 * JoinRewardsActivity.this);
			 */
			setError(edtConfirmUserName, confirmUserNameErrorText,
					CONFIRM_EMAIL_VALIDATION_MESSAGE);
			edtConfirmUserName.requestFocus();
			return false;
		} else if (edtFirstName.getText().toString().equalsIgnoreCase("")
				|| edtFirstName.getText().toString() == null) {
			setError(edtFirstName, firstNameErrorText,
					"Please fill the First Name");
			/*
			 * notifyUser("Please fill the First Name",
			 * JoinRewardsActivity.this);
			 */
			edtFirstName.requestFocus();
			return false;
		} else if (edtLastName.getText().toString().equalsIgnoreCase("")
				|| edtLastName.getText().toString() == null) {
			/*
			 * notifyUser("Please fill the Last Name",
			 * JoinRewardsActivity.this);
			 */
			setError(edtLastName, lastNameErrorText,
					"Please fill the Last Name");
			edtLastName.requestFocus();
			return false;
		} else if (edtPhone.getText().toString().equalsIgnoreCase("")
				|| edtPhone.getText().toString() == null
				|| edtPhone.getText().toString().length() < 10) {
			/*
			 * notifyUser("Please fill the Phone Number ",
			 * JoinRewardsActivity.this);
			 */
			setError(edtPhone, phoneErrorText, "Please fill the Phone Number");
			edtPhone.requestFocus();
			return false;
		} /*
		 * else if (edtDateOfBirth.getText().toString().equalsIgnoreCase("") ||
		 * edtDateOfBirth.getText().toString() == null) {
		 * notifyUser("Please enter the Date of Birth ",
		 * JoinRewardsActivity.this); setError(edtPhone, phoneErrorText,
		 * "Please enter the Date of Birth"); edtDateOfBirth.requestFocus();
		 * return false; }
		 */else if (edtAddressLine1.getText().toString().equalsIgnoreCase("")
				|| edtAddressLine1.getText().toString() == null) {
			/*
			 * notifyUser("Please fill the Address Line 1",
			 * JoinRewardsActivity.this);
			 */
			setError(edtAddressLine1, address1ErrorText,
					"Please fill the Address Line 1");
			edtAddressLine1.requestFocus();
			return false;
		} else if (edtCity.getText().toString().equalsIgnoreCase("")
				|| edtCity.getText().toString() == null) {
			/* notifyUser("Please fill the City", JoinRewardsActivity.this); */
			setError(edtCity, cityErrorText, "Please fill the City");
			edtCity.requestFocus();
			return false;
		} else if (!isSpinnerSelected) {
			/* notifyUser("Please select the State", JoinRewardsActivity.this); */
			spinnerCity.requestFocus();
			stateErrorText.setText("Please select the State");
			stateErrorText.setVisibility(View.VISIBLE);
			return false;
		} else
			return true;

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
				mainLayout.setVisibility(View.VISIBLE);
				notifyUser(Utility.formatDisplayError(getErrorMessage()),
						JoinRewardsActivity.this);
			} else {
				Logger.Log("<StatList><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				stateListBean = (StateListBean) getResponseBean();
				result = stateListBean.getStateList();
				setupCitySpinner();
				setViews();
			}
		}
	}

	/**
	 * Setup city spinner.
	 */
	private void setupCitySpinner() {
		anArrayOfStrings = new String[result.size()];
		result.toArray(anArrayOfStrings);
		MySpinnerAdapter<CharSequence> spinnerSrrayAdapter = new MySpinnerAdapter<CharSequence>(
				JoinRewardsActivity.this, anArrayOfStrings);

		spinnerCity.setAdapter(spinnerSrrayAdapter);
		spinnerCity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View arg1,
					int position, long arg3) {
				((TextView) parentView.getChildAt(0))
						.setTextColor(getResources().getColor(R.color.black));
				((TextView) parentView.getChildAt(0)).setTextSize(12);
				((TextView) parentView.getChildAt(0)).setPadding(5, 0, 0, 0);
				if (position != 0) {
					stateErrorText.setVisibility(View.GONE);
					isSpinnerSelected = true;
					strSelectedState1 = parentView.getItemAtPosition(position)
							.toString();

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

				if (null != getIntent().getExtras().get("state")) {
					stateValue = getIntent().getExtras().get("state")
							.toString();
					tv.setText(stateValue);
					spinnerCity.setSelection(stateLocation);
				} else {
					tv.setText("Select state");
				}

				tv.setTextColor(getResources().getColor(R.color.black));
				tv.setPadding(5, 0, 0, 0);
				return tv;
			}
			// let the array adapter takecare this time
			return super.getView(position, convertView, parent);
		}
	}

	private void fnInvokeJoinRewardsProgramme() {
		InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.JOIN_REWARDS);
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateOrderDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(UltaBean.class);
		OrderDetailsHandler orderDetailsHandler = new OrderDetailsHandler();
		invokerParams.setUltaHandler(orderDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateOrderDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");

		if (sign) {
			urlParams.put("memberData.emailAddress", edtUserName.getText()
					.toString());
			urlParams.put("memberData.firstName", edtFirstName.getText()
					.toString());
			urlParams.put("memberData.lastName", edtLastName.getText()
					.toString());
			urlParams.put("memberData.homeAddr1", edtAddressLine1.getText()
					.toString());
			urlParams.put("memberData.homeAddr2", edtAddressLine2.getText()
					.toString());
			urlParams.put("memberData.cityName", edtCity.getText().toString());
			urlParams.put("memberData.stateCode", spinnerCity.getSelectedItem()
					.toString());
			urlParams
					.put("memberData.zipCode", edtZipCode.getText().toString());
			urlParams.put("value.dateOfBirth", edtDateOfBirth.getText()
					.toString());
			urlParams.put("joinOrActivate", "join");
			urlParams.put("zip", edtZipCode.getText().toString());
			urlParams.put("memberData.phoneNum",
					Utility.formatPhoneNumber(edtPhone.getText().toString()));

		} else {
			urlParams.put("joinOrActivate", "Activate");
			urlParams.put("beautyClubNumber", editId.getText().toString());
		}
		return urlParams;
	}

	/**
	 * The Class RetrievePaymentDetailsHandler.
	 */
	public class OrderDetailsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<OrderDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				try {
					mainLayout.setVisibility(View.VISIBLE);
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							JoinRewardsActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				UltaBean ultaBean = (UltaBean) getResponseBean();

				trackAppAction(
						JoinRewardsActivity.this,
						WebserviceConstants.LOYALTY_ACCOUNT_CREATED_EVENT_ACTION);

				if (null != ultaBean) {
					List<String> errors = ultaBean.getErrorInfos();
					if (null != errors && !(errors.isEmpty())) {
						try {
							loadingDialog.setVisibility(View.GONE);
							mainLayout.setVisibility(View.VISIBLE);
							notifyUser(errors.get(0), JoinRewardsActivity.this);

						} catch (WindowManager.BadTokenException e) {
						} catch (Exception e) {
						}
					} else {
						UltaDataCache.getDataCacheInstance().setRewardMember(
								true);
						loadingDialog.setVisibility(View.GONE);
						mainLayout.setVisibility(View.VISIBLE);
						Intent intentRewards = new Intent(
								JoinRewardsActivity.this, RewardsActivity.class);
						startActivity(intentRewards);
						finish();
					}
				} else {
					loadingDialog.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
					notifyUser("Please try later", JoinRewardsActivity.this);
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

	@SuppressWarnings("deprecation")
	@Override
	public void afterTextChanged(Editable s) {
		if (s.hashCode() == edtFirstName.getText().hashCode()) {
			edtFirstName.setBackgroundDrawable(originalDrawable);
			firstNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtLastName.getText().hashCode()) {
			edtLastName.setBackgroundDrawable(originalDrawable);
			lastNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtUserName.getText().hashCode()) {
			edtUserName.setBackgroundDrawable(originalDrawable);
			userNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtConfirmUserName.getText().hashCode()) {
			edtConfirmUserName.setBackgroundDrawable(originalDrawable);
			confirmUserNameErrorText.setVisibility(View.GONE);
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
			zipErrorText.setVisibility(View.GONE);
		}
		else if (s.hashCode() == editId.getText().hashCode()) {
			editId.setBackgroundDrawable(originalDrawable);
			idErrorText.setVisibility(View.GONE);
		}
		

		changeAllEditTextBackground();

	}

	public void changeAllEditTextBackground() {
		changeEditTextBackground(edtFirstName);
		changeEditTextBackground(edtLastName);
		changeEditTextBackground(edtAddressLine1);
		changeEditTextBackground(edtAddressLine2);
		changeEditTextBackground(edtPhone);
		changeEditTextBackground(edtZipCode);
		changeEditTextBackground(edtCity);
		changeEditTextBackground(edtUserName);
		changeEditTextBackground(edtConfirmUserName);
		changeEditTextBackground(edtDateOfBirth);
		changeEditTextBackground(editId);
	}

	public void setError(EditText editText, TextView errorTV, String message) {
		errorTV.setText("" + message);
		errorTV.setVisibility(View.VISIBLE);
		changeAllEditTextBackground();
		editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
	}
}
