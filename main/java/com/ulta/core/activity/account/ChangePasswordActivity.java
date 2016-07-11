/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
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

/**
 * The Class ChangePasswordActivity.
 */
public class ChangePasswordActivity extends UltaBaseActivity implements
		OnDoneClickedListener, OnSessionTimeOut, TextWatcher {

	/** Edit text box declaration. */
	private EditText edOldPassword, edNewPassword, edConfirmPassword;

	/** String declaration to hold the value of edit text */
	private String oldPassword, newPassword, confirmPassword;

	/** The password length error message. */
	private static String PASSWORD_LENGTH_ERROR_MESSAGE = "Please enter new password";
	private static String OLD_PASSWORD_ERROR_MESSAGE = "Please enter old password";

	/** The new confirm password error message. */
	private static String NEW_CONFIRM_PASSWORD_ERROR_MESSAGE = "Supplied password does not match";
	/** Prgress dialogue declaration */
	private ProgressDialog pd;
	private Button mDoneButton;

	private SharedPreferences staySignedInSharedPreferences;

	private Editor staySignedInEditor;
	private TextView oldPasswordErrorText, newPasswordErrorText,
			confirmNewPasswordErrorText;
	private Drawable originalDrawable;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);

		edOldPassword = (EditText) findViewById(R.id.edOldPasword);
		edNewPassword = (EditText) findViewById(R.id.edNewPassword);
		edConfirmPassword = (EditText) findViewById(R.id.edConfirmPassword);
		oldPasswordErrorText = (TextView) findViewById(R.id.oldPasswordErrorText);
		newPasswordErrorText = (TextView) findViewById(R.id.newPasswordErrorText);
		confirmNewPasswordErrorText = (TextView) findViewById(R.id.confirmNewPasswordErrorText);
		edOldPassword.addTextChangedListener(this);
		edNewPassword.addTextChangedListener(this);
		edConfirmPassword.addTextChangedListener(this);
		originalDrawable = edOldPassword.getBackground();
		setTitle("Change Password");
		mDoneButton = (Button) findViewById(R.id.doneBtn);
		mDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						validationLogin();
			}
		});
	}

	/**
	 * Validation login.
	 */
	public void validationLogin() {

		oldPassword = edOldPassword.getText().toString();
		newPassword = edNewPassword.getText().toString();
		confirmPassword = edConfirmPassword.getText().toString();

		if (oldPassword.length() < 5) {

			// notifyUser(PASSWORD_LENGTH_ERROR_MESSAGE,
			// ChangePasswordActivity.this);
			setError(edOldPassword, oldPasswordErrorText,
					OLD_PASSWORD_ERROR_MESSAGE);
			edOldPassword.requestFocus();
		} else if (newPassword.length() < 5) {

			// notifyUser(PASSWORD_LENGTH_ERROR_MESSAGE,
			// ChangePasswordActivity.this);
			setError(edNewPassword, newPasswordErrorText,
					PASSWORD_LENGTH_ERROR_MESSAGE);
			edNewPassword.requestFocus();
		} else if (!newPassword.equals(confirmPassword)) {
			// notifyUser(NEW_CONFIRM_PASSWORD_ERROR_MESSAGE,
			// ChangePasswordActivity.this);
			setError(edConfirmPassword, confirmNewPasswordErrorText,
					NEW_CONFIRM_PASSWORD_ERROR_MESSAGE);
			edConfirmPassword.requestFocus();
		} else {
			// Showing progress bar once the service call is to be processed
			pd = new ProgressDialog(ChangePasswordActivity.this);
			pd.setMessage(LOADING_PROGRESS_TEXT);
			setProgressDialogLoadingColor(pd);
			pd.setCancelable(false);
			pd.show();
			invokeChangePassword();
		}
	}

	/**
	 * Invoke change password.
	 */
	private void invokeChangePassword() {
		InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.CHANGE_PASSWORD_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateChangePasswordParameters());
		invokerParams.setUltaBeanClazz(LoginBean.class);
		ChangePasswordHandler userCreationHandler = new ChangePasswordHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<RegisterationActivity><invokeUserCreation><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return Map<String, String>
	 */

	private Map<String, String> populateChangePasswordParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		// urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("value.oldpassword", oldPassword);
		urlParams.put("value.password", newPassword);
		urlParams.put("value.confirmpassword", confirmPassword);

		return urlParams;
	}

	/**
	 * The Class ChangePasswordHandler.
	 */
	public class ChangePasswordHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		// @SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			Logger.Log("<ChangePasswordHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(ChangePasswordActivity.this);
				} else {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								ChangePasswordActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				Logger.Log("<ChangePassword><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				LoginBean ultaBean = (LoginBean) getResponseBean();
				List<String> result = ultaBean.getErrorInfos();
				Logger.Log("<ChangepasswordHandler><handleMessage><getResponseBean>>"
						+ result);
				if (null == result) {
					Utility.encrypPasswordt(edNewPassword.getText().toString());
					byte[] loginPasswordBytes = UltaDataCache
							.getDataCacheInstance().getEncodedPasswordBytes();
					String loginPassword = Base64.encodeToString(
							loginPasswordBytes, Base64.DEFAULT);

					staySignedInSharedPreferences = getSharedPreferences(
							WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
							MODE_PRIVATE);
					staySignedInEditor = staySignedInSharedPreferences.edit();
					staySignedInEditor.putString(
							WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
							loginPassword);
					staySignedInEditor.commit();

					Dialog alertDialog = showAlertDialog(
							ChangePasswordActivity.this, "Success",
							"Your Password has been changed successfully",
							"OK", "");
					mAgreeButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							creatingPageName(WebserviceConstants.ACCOUNT_CHANGE_PASSWORD_THANKYOU);
							finish();
						}
					});
					mDisagreeButton.setVisibility(View.GONE);

					/*AlertDialog alertDialog = new AlertDialog.Builder(
							ChangePasswordActivity.this).create();
					alertDialog.setTitle("Success");
					alertDialog
							.setMessage("Your Password has been changed successfully");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// here you can add functions
									creatingPageName(WebserviceConstants.ACCOUNT_CHANGE_PASSWORD_THANKYOU);
									finish();
								}
							});*/

					alertDialog.show();
				}
				// login failure
				else {
					notifyUser(result.get(0), ChangePasswordActivity.this);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.widgets.flyin.OnDoneClickedListener#onDoneClicked()
	 */
	@Override
	public void onDoneClicked() {
		// validationLogin();
	}

	public void creatingPageName(String categoryname) {

		trackAppState(this, categoryname);
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			invokeChangePassword();
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
		if (s.hashCode() == edOldPassword.getText().hashCode()) {
			edOldPassword.setBackgroundDrawable(originalDrawable);
			oldPasswordErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edNewPassword.getText().hashCode()) {
			edNewPassword.setBackgroundDrawable(originalDrawable);
			newPasswordErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edConfirmPassword.getText().hashCode()) {
			edConfirmPassword.setBackgroundDrawable(originalDrawable);
			confirmNewPasswordErrorText.setVisibility(View.GONE);
		}
	}

	public void setError(EditText editText, TextView errorTV, String message) {
		editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
		errorTV.setText("" + message);
		errorTV.setVisibility(View.VISIBLE);
	}
}
