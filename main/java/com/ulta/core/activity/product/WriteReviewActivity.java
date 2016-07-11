/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.ProductReviewPostBean;
import com.ulta.core.bean.product.ProductReviewPostDetailsBean;
import com.ulta.core.bean.product.ProductReviewPostResponseBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.util.HashMap;
import java.util.Map;

public class WriteReviewActivity extends UltaBaseActivity implements
		OnDoneClickedListener, OnClickListener, TextWatcher {

	private EditText edtCommments, edtHeadline, edtNickname, edtYourlocation;
	private ImageView imgRating1, imgRating2, imgRating3, imgRating4,
			imgRating5;
	private String commments, headline, nickname, yourlocation;
	String rating = "0";
	private String json;

	private String mProductName;
	private String mProductPrice;
	private TextView mProductnameTextView;
	private TextView mProductPriceTextView;
	private TextView mProductMinidescription;
	private Button mWriteReviewDonebtn;
	private ProgressDialog mPdialog;

	private static String NICK_NAME_VALIDATION_MESSAGE = "Please enter Nickname";
	private static String COMMENTS_VALIDATION_MESSAGE = "Please enter comments";
	private static String REVIEW_HEADLINE_VALIDATION_MESSAGE = "Please enter review headline";
	private static String RATING_VALIDATION_MESSAGE = "Please select rating ";
	private static String YOUR_LOCATION_VALIDATION_MESSAGE = "Please enter your location";
	private ProductReviewPostDetailsBean productReviewPostDetailsBean;
	private ProductReviewPostBean productReviewPostBean;
	private AlertDialog alertDialog;
	private TextView reviewErrorText, commentsErrorText, nickNameErrorText,
			locationErrorText, ratingErrorText;
	private Drawable originalDrawable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_review);
		setTitle("Write Review");
		edtCommments = (EditText) findViewById(R.id.editComments);
		edtHeadline = (EditText) findViewById(R.id.editReviewHeadline);
		edtNickname = (EditText) findViewById(R.id.editNickname);
		edtYourlocation = (EditText) findViewById(R.id.editYourLocation);
		originalDrawable = edtYourlocation.getBackground();

		edtCommments.addTextChangedListener(this);
		edtHeadline.addTextChangedListener(this);
		edtNickname.addTextChangedListener(this);
		edtYourlocation.addTextChangedListener(this);

		reviewErrorText = (TextView) findViewById(R.id.reviewErrorText);
		commentsErrorText = (TextView) findViewById(R.id.commentsErrorText);
		nickNameErrorText = (TextView) findViewById(R.id.nickNameErrorText);
		locationErrorText = (TextView) findViewById(R.id.locationErrorText);
		ratingErrorText = (TextView) findViewById(R.id.ratingErrorText);

		imgRating1 = (ImageView) findViewById(R.id.imgRating1);
		imgRating2 = (ImageView) findViewById(R.id.imgRating2);
		imgRating3 = (ImageView) findViewById(R.id.imgRating3);
		imgRating4 = (ImageView) findViewById(R.id.imgRating4);
		imgRating5 = (ImageView) findViewById(R.id.imgRating5);

		imgRating1.setOnClickListener(this);
		imgRating2.setOnClickListener(this);
		imgRating3.setOnClickListener(this);
		imgRating4.setOnClickListener(this);
		imgRating5.setOnClickListener(this);

		mProductnameTextView = (TextView) findViewById(R.id.txtWriteReviewProductName);
		mProductPriceTextView = (TextView) findViewById(R.id.txtWriteReviewProductPrice);
		mWriteReviewDonebtn = (Button) findViewById(R.id.writeReviewDonebtn);

		mProductMinidescription = (TextView) findViewById(R.id.txtProductDescription);

		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getString("productName")) {
				mProductName = getIntent().getExtras().getString("productName");
				mProductnameTextView.setText(mProductName);
			}
			if (null != getIntent().getExtras().getString("productPrice")) {
				mProductPrice = getIntent().getExtras().getString(
						"productPrice");
				try {
					mProductPriceTextView.setText(getResources().getString(
							R.string.dollar_sign)
							+ String.format("%.2f",
									Double.valueOf(mProductPrice)));
				} catch (NumberFormatException e) {
					mProductPriceTextView.setText(getResources().getString(
							R.string.dollar_sign)
							+ mProductPrice);
				} catch (NotFoundException e) {
					mProductPriceTextView.setText(getResources().getString(
							R.string.dollar_sign)
							+ mProductPrice);
				}
			}
			if (null != getIntent().getExtras()
					.getString("prodMiniDescription")) {
				mProductMinidescription.setText(getIntent().getExtras()
						.getString("prodMiniDescription"));
			}

		}

		mWriteReviewDonebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				validate();
			}
		});

	}

	@Override
	public void onDoneClicked() {
		validate();
	}

	public void validate() {
		commments = edtCommments.getText().toString();
		headline = edtHeadline.getText().toString();
		nickname = edtNickname.getText().toString();
		yourlocation = edtYourlocation.getText().toString();

		if (rating.equalsIgnoreCase("0")) {
			try {
				// notifyUser(RATING_VALIDATION_MESSAGE,
				// WriteReviewActivity.this);
				ratingErrorText.setText(RATING_VALIDATION_MESSAGE);
				ratingErrorText.setVisibility(View.VISIBLE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		} else if (headline.length() == 0) {
			try {
				// notifyUser(REVIEW_HEADLINE_VALIDATION_MESSAGE,
				// WriteReviewActivity.this);
				setError(edtHeadline, reviewErrorText,
						REVIEW_HEADLINE_VALIDATION_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		}

		else if (commments.length() == 0) {
			try {
				// notifyUser(COMMENTS_VALIDATION_MESSAGE,
				// WriteReviewActivity.this);
				setError(edtCommments, commentsErrorText,
						COMMENTS_VALIDATION_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		}

		else if (nickname.length() == 0) {
			try {
				// notifyUser(NICK_NAME_VALIDATION_MESSAGE,
				// WriteReviewActivity.this);
				setError(edtNickname, nickNameErrorText,
						NICK_NAME_VALIDATION_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		}

		else if (yourlocation.length() == 0) {
			try {
				// notifyUser(YOUR_LOCATION_VALIDATION_MESSAGE,
				// WriteReviewActivity.this);
				setError(edtYourlocation, locationErrorText,
						YOUR_LOCATION_VALIDATION_MESSAGE);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		} else {

			productReviewPostDetailsBean = new ProductReviewPostDetailsBean();
			// Product ID
			String id = getIntent().getExtras().getString("id");
			productReviewPostDetailsBean.setPage_id(id);
			// Locale
			productReviewPostDetailsBean.setLocale("en_US");
			// headline
			productReviewPostDetailsBean.setHeadline(headline);
			// Rating no as string

			Logger.Log("<<<<" + rating);

			productReviewPostDetailsBean.setRating(rating);
			// Comments
			productReviewPostDetailsBean.setComments(commments);
			// location
			productReviewPostDetailsBean.setLocation(yourlocation);
			// NickName
			productReviewPostDetailsBean.setNickName(nickname);

			productReviewPostBean = new ProductReviewPostBean();
			productReviewPostBean.setReview(productReviewPostDetailsBean);
			json = WebserviceUtility.jsonMarshller(productReviewPostBean);
			Logger.Log("@@@@@@@"
					+ WebserviceUtility.jsonMarshller(productReviewPostBean));
			invokeWriteReview();
		}
	}

	@Override
	public void onClick(View v) {
		ratingErrorText.setVisibility(View.GONE);
		switch (v.getId()) {
		case R.id.imgRating1:
			if (imgRating1.isSelected()) {
				imgRating1.setSelected(true);
				imgRating2.setSelected(false);
				imgRating3.setSelected(false);
				imgRating4.setSelected(false);
				imgRating5.setSelected(false);
				rating = "1";
			} else {
				imgRating1.setSelected(true);
				rating = "1";
			}
			break;

		case R.id.imgRating2:
			if (imgRating2.isSelected()) {

				imgRating2.setSelected(true);
				imgRating3.setSelected(false);
				imgRating4.setSelected(false);
				imgRating5.setSelected(false);
				rating = "2";
			} else {
				imgRating1.setSelected(true);
				imgRating2.setSelected(true);
				rating = "2";
			}
			break;

		case R.id.imgRating3:
			if (imgRating3.isSelected()) {
				imgRating3.setSelected(true);
				imgRating4.setSelected(false);
				imgRating5.setSelected(false);
				rating = "3";
			} else {
				imgRating1.setSelected(true);
				imgRating2.setSelected(true);
				imgRating3.setSelected(true);
				rating = "3";
			}
			break;

		case R.id.imgRating4:
			if (imgRating4.isSelected()) {
				imgRating4.setSelected(true);
				imgRating5.setSelected(false);
				rating = "4";
			} else {
				imgRating1.setSelected(true);
				imgRating2.setSelected(true);
				imgRating3.setSelected(true);
				imgRating4.setSelected(true);
				rating = "4";
			}
			break;

		case R.id.imgRating5:
			if (imgRating5.isSelected()) {
				imgRating5.setSelected(true);
				rating = "5";
			} else {
				imgRating1.setSelected(true);
				imgRating2.setSelected(true);
				imgRating3.setSelected(true);
				imgRating4.setSelected(true);
				imgRating5.setSelected(true);
				rating = "5";
			}
			break;

		default:
			break;
		}
	}

	private void invokeWriteReview() {

		mPdialog = new ProgressDialog(WriteReviewActivity.this);
		setProgressDialogLoadingColor(mPdialog);
		mPdialog.setMessage("Posting review..");
		mPdialog.show();

		InvokerParams<ProductReviewPostResponseBean> invokerParams = new InvokerParams<ProductReviewPostResponseBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.WRITE_REVIEWS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateLoginParameters());
		invokerParams.setUltaBeanClazz(ProductReviewPostResponseBean.class);
		// invokerParams.setUserSessionClearingRequired(true);
		WriteReviewHandler writeReviewHandler = new WriteReviewHandler();
		invokerParams.setUltaHandler(writeReviewHandler);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.POWER_REVIEWS_WRITE_CONTEXT);
		invokerParams.setCookieHandlingSkip(true);

		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			mPdialog.dismiss();
			Logger.Log("<WriteLoginActivity><invokeLogin><UltaException>>"
					+ ultaException);
		}
	}

	private Map<String, String> populateLoginParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		/*
		 * urlParams.put("atg-rest-output", "json");
		 * urlParams.put("atg-rest-depth", "1");
		 * urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		 */
		urlParams.put("merchant_id", "6406");
		urlParams.put("apikey", "b60759f2-569d-417b-89bf-11b9bb93d9e7");
		// urlParams.put("page_id", "xlsImpprod2670053");
		urlParams.put("format", "json");
		urlParams.put("review_data", json);
		// urlParams.put("locale", "en_US");
		// urlParams.put("atg-rest-return-form-handler-properties", "true");
		return urlParams;

	}

	public class WriteReviewHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			// http: //
			// api.powerreviews.com/api/reviews/template?page_id=xlsImpprod2670053&format=json&merchant_id=6406&review_data={"review":{"comments":"fjxj","headline":"dhx","locale":"en_US","location":"xhxu","name":"xhz","page_id":"xlsImpprod2670053","rating":"5"}}&locale=en_US&apikey=b60759f2-569d-417b-89bf-11b9bb93d9e7

			Logger.Log("<WriteReviewHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				mPdialog.dismiss();
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							WriteReviewActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				mPdialog.dismiss();
				Logger.Log("<WriteReviewHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				ProductReviewPostResponseBean ultaBean = (ProductReviewPostResponseBean) getResponseBean();
				String result = ultaBean.getResponse_code();
				Logger.Log("<WriteReviewHandlerr><handleMessage><getResponseBean>>"
						+ result);
				if (result.equalsIgnoreCase("200")) {

					/*
					 * alertDialog = new AlertDialog.Builder(
					 * WriteReviewActivity.this).create();
					 * 
					 * alertDialog.setTitle("Review"); alertDialog .setMessage(
					 * "Your Review has been posted. It will be updated soon.");
					 * 
					 * alertDialog.setButton("Ok", new
					 * DialogInterface.OnClickListener() {
					 * 
					 * public void onClick(DialogInterface arg0, int arg1) {
					 * finish(); alertDialog.dismiss();
					 * 
					 * } }); alertDialog.show();
					 */

					final Dialog alertDialog = showAlertDialog(
							WriteReviewActivity.this,
							"Review",
							"Your Review has been posted. It will be updated soon.",
							"Ok", "");
					alertDialog.show();

					mDisagreeButton.setVisibility(View.GONE);
					mAgreeButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							finish();
							alertDialog.dismiss();
						}
					});

				} else {
					notifyUser("Review", "Review not posted.",
							WriteReviewActivity.this);

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

	@Override
	public void afterTextChanged(Editable s) {
		if (s.hashCode() == edtHeadline.getText().hashCode()) {
			edtHeadline.setBackgroundDrawable(originalDrawable);
			reviewErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtCommments.getText().hashCode()) {
			edtCommments.setBackgroundDrawable(originalDrawable);
			commentsErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtNickname.getText().hashCode()) {
			edtNickname.setBackgroundDrawable(originalDrawable);
			nickNameErrorText.setVisibility(View.GONE);
		} else if (s.hashCode() == edtYourlocation.getText().hashCode()) {
			edtYourlocation.setBackgroundDrawable(originalDrawable);
			locationErrorText.setVisibility(View.GONE);
		}

		changeAllEditTextBackground();
	}

	public void changeAllEditTextBackground() {
		changeEditTextBackground(edtHeadline);
		changeEditTextBackground(edtCommments);
		changeEditTextBackground(edtNickname);
		changeEditTextBackground(edtYourlocation);
	}

	public void setError(EditText editText, TextView errorTV, String message) {
		editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
		errorTV.setText("" + message);
		errorTV.setVisibility(View.VISIBLE);
	}
}
