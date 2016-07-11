/**
 *
 * Copyright(c) ULTA, Inc. All Rights reserved.
 *
 *
 */

package com.ulta.core.activity.account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;
import com.ulta.core.util.map.LocationFinder;
import com.ulta.core.widgets.UltaProgressDialog;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;
import com.ulta.core.widgets.flyin.OnPermissionCheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

/**
 * The Class ForgotLoginActivity.
 */
public class ForgotLoginActivity extends UltaBaseActivity implements
		OnDoneClickedListener, OnSessionTimeOut, TextWatcher,OnPermissionCheck {

	/** The edit reenteremail. */
	private EditText editEmail, editReenteremail;

	/** The alert dialog. */
	AlertDialog alertDialog;

	TextView txtEmail, txtCall;
	/** The email. */
	private String email;

	/** The reenteremail. */
	private String reenteremail;

	private int fromCehckout;

	private Button mDoneButton;

	// /** The email length error title. */
	// private static String EMAIL_LENGTH_ERROR_TITLE =
	// "Email Address is Mandatory";

	/** The email length error message. */
	private static String EMAIL_LENGTH_ERROR_MESSAGE = "The entered email addresses are mismatch";

	/** The email length error message. */
	private static String EMAIL_EMPTY_ERROR_MESSAGE = "Email address can not be empty";

	/** The email validation message. */
	private static String EMAIL_VALIDATION_MESSAGE = "Please enter valid email addresses.";

	/** The profile error message. */
	/*
	 * private static String PROFILE_ERROR_MESSAGE =
	 * "No Profile found in our system for this email address.";
	 */
	/** The Constant EMAIL_ADDRESS_PATTERN. */
	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

	private ProgressDialog pd;
	private TextView emailErrorText, reemailErrorText;
	private Drawable originalDrawable;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotlogin);
		setTitle("Forgot Password");
        setActivity(ForgotLoginActivity.this);
		if (null != getIntent().getExtras()) {

			fromCehckout = getIntent().getExtras().getInt("fromCehckout", 0);
		}

		if (fromCehckout == 1) {
			disableMenu();
		}
		editEmail = (EditText) findViewById(R.id.editEmail);
		editEmail.addTextChangedListener(this);
		editReenteremail = (EditText) findViewById(R.id.editReenteremail);
		editReenteremail.addTextChangedListener(this);
		originalDrawable = editEmail.getBackground();
		txtCall = (TextView) findViewById(R.id.txtCall);
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		mDoneButton = (Button) findViewById(R.id.doneBtn);
		emailErrorText = (TextView) findViewById(R.id.emailErrorText);
		reemailErrorText = (TextView) findViewById(R.id.reemailErrorText);
		pd = new ProgressDialog(ForgotLoginActivity.this);
		setProgressDialogLoadingColor(pd);
		pd.setMessage(LOADING_PROGRESS_TEXT);
		pd.setCancelable(false);

		editReenteremail
				.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {

                        validation();
                        return false;
                    }
                });

		SpannableString spannableStringEmail = new SpannableString(txtEmail
				.getText().toString());
		spannableStringEmail.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
						Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("plain/text");
				intent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { UltaConstants.EMAIL_CUSTOMER_CARE });
				// intent.putExtra(Intent.EXTRA_CC, new
				// String[]{"appsupport@ulta.com","purchasesupport@ulta.com"});
				// intent.putExtra(Intent.EXTRA_BCC, new
				// String[]{"bcc@ulta.com"});
				intent.putExtra(Intent.EXTRA_SUBJECT, "Forgot Password.");
				intent.putExtra(Intent.EXTRA_TEXT,
                        UltaConstants.SENT_FROM_ANDROID);
				startActivity(Intent.createChooser(intent, "Title"));
			}
		}, 8, spannableStringEmail.length(), 0);
		txtEmail.setText(spannableStringEmail);
		txtEmail.setMovementMethod(LinkMovementMethod.getInstance());

		SpannableString spannableStringCall = new SpannableString(txtCall
				.getText().toString());
		spannableStringCall.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
//						Intent dialIntent = new Intent();
//				dialIntent.setAction(Intent.ACTION_DIAL);
//				dialIntent.setData(Uri.parse("tel:8669838582"));
//				startActivity(dialIntent);
                checkForAppPermissions(getApplicationContext(), WebserviceConstants.PERMISSION_CALL_PHONE, WebserviceConstants.PHONE_REQUEST_CODE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_TITLE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_MESSAGE);
			}
		}, 8, spannableStringCall.length(), 0);
		txtCall.setText(spannableStringCall);
		txtCall.setMovementMethod(LinkMovementMethod.getInstance());

		mDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				validation();

			}
		});

	}

	/**
	 * Validate email.
	 *
	 * @param email
	 *            the email
	 * @return true, if successful
	 */
	public static boolean validateEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	/**
	 * Validation.
	 */
	public void validation() {

		email = editEmail.getText().toString();
		reenteremail = editReenteremail.getText().toString();

		alertDialog = new AlertDialog.Builder(this).create();

		if (email.length() == 0) {
			try {
				// notifyUser(EMAIL_LENGTH_ERROR_TITLE,
				// EMAIL_EMPTY_ERROR_MESSAGE,
				// ForgotLoginActivity.this);
				setError(editEmail, emailErrorText, EMAIL_EMPTY_ERROR_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		}

		else if (!validateEmail(email)) {

			try {
				// notifyUser(EMAIL_VALIDATION_MESSAGE,
				// ForgotLoginActivity.this);
				setError(editEmail, emailErrorText, EMAIL_VALIDATION_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		}

		else if (reenteremail.length() == 0) {
			try {
				// notifyUser(EMAIL_LENGTH_ERROR_TITLE,
				// EMAIL_EMPTY_ERROR_MESSAGE,
				// ForgotLoginActivity.this);
				setError(editReenteremail, reemailErrorText,
						EMAIL_EMPTY_ERROR_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		}

		else if (!validateEmail(reenteremail)) {

			try {
//				notifyUser(EMAIL_VALIDATION_MESSAGE, ForgotLoginActivity.this);
				setError(editReenteremail, reemailErrorText,
						EMAIL_VALIDATION_MESSAGE);

			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		} else if (!email.equals(reenteremail)) {
			try {
				// notifyUser(EMAIL_LENGTH_ERROR_TITLE,
				// EMAIL_LENGTH_ERROR_MESSAGE, ForgotLoginActivity.this);
				setError(editReenteremail, reemailErrorText,
						EMAIL_LENGTH_ERROR_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		}

		else {
			pd.show();
			invokeForgotPassword();
		}
	}

	/**
	 * Invoke forgot password.
	 */
	private void invokeForgotPassword() {
		InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.FORGOT_PASSWORD_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateForgotPasswordParameters());
		invokerParams.setUltaBeanClazz(LoginBean.class);
		ForgotLoginHandler userCreationHandler = new ForgotLoginHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<ForgotLoginActivity><invokeForgotPassword><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Method to populate the URL parameter map.
	 *
	 * @return Map<String, String>
	 */
	private Map<String, String> populateForgotPasswordParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		// urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("emailAddress1", email);
		urlParams.put("emailAddress2", reenteremail);

		return urlParams;
	}

	/**
	 * The Class ForgotLoginHandler.
	 */
	public class ForgotLoginHandler extends UltaHandler {

		/**
		 * Handle message.
		 *
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			Logger.Log("<ForgotLoginHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(ForgotLoginActivity.this);
				} else {
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								ForgotLoginActivity.this);

					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}

				}
			} else {
				Logger.Log("<ForgotLogin><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				LoginBean ultaBean = (LoginBean) getResponseBean();
				List<String> result = ultaBean.getErrorInfos();

				Logger.Log("<ForgotLoginHandler><handleMessage><getResponseBean>>"
						+ result);

				if (null == result) {
					/*AlertDialog alertDialog = new AlertDialog.Builder(
							ForgotLoginActivity.this).create();
					alertDialog.setTitle("Alert");
					alertDialog
							.setMessage("An email will be sent to the email address associated with your customer account");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// here you can add functions
									finish();
									creatingPageName(WebserviceConstants.ACCOUNT_FORGOT_PASSWORD_THANKYOU);
								}
							});

					alertDialog.show();*/

					final Dialog alertDialog = showAlertDialog(
							ForgotLoginActivity.this, "Alert",
							"An email will be sent to the email address associated with your customer account", "Ok", "");
					alertDialog.show();

					mDisagreeButton.setVisibility(View.GONE);
					mAgreeButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							finish();
							creatingPageName(WebserviceConstants.ACCOUNT_FORGOT_PASSWORD_THANKYOU);
						}
					});

				}
				// login failure
				else {
					try {
						notifyUser(result.get(0), ForgotLoginActivity.this);

						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
					} catch (WindowManager.BadTokenException e) {

					} catch (Exception e) {
					}
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
		// validation();
	}

	public void creatingPageName(String categoryname) {

		trackAppState(this, categoryname);
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {
		if (isSuccess) {
			invokeForgotPassword();
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

	@SuppressWarnings("deprecation")
	@Override
	public void afterTextChanged(Editable s) {
		if (s.hashCode() == editEmail.getText().hashCode()) {
			editEmail.setBackgroundDrawable(originalDrawable);
			emailErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == editReenteremail.getText().hashCode()) {
			editReenteremail.setBackgroundDrawable(originalDrawable);
			reemailErrorText.setVisibility(View.GONE);
		}

	}

	public void setError(EditText editText, TextView errorTV, String message) {
		editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
		errorTV.setText("" + message);
		errorTV.setVisibility(View.VISIBLE);
	}
    /**
     * App permission check result for phone
     *
     * @param isSuccess
     * @param permissionRequestCode
     */
    @Override
    public void onPermissionCheckRequest(boolean isSuccess, int permissionRequestCode) {
        if (isSuccess) {
            if (permissionRequestCode == WebserviceConstants.PHONE_REQUEST_CODE) {
                Intent dialIntent = new Intent();
				dialIntent.setAction(Intent.ACTION_DIAL);
				dialIntent.setData(Uri.parse("tel:8669838582"));
				startActivity(dialIntent);
            }
        }

    }
}
