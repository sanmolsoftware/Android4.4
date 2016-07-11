
package com.ulta.core.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.about.LegalActivity;
import com.ulta.core.activity.about.PrivacyPolicyActivity;
import com.ulta.core.bean.product.ProductReviewPostResponseBean;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PreviewQuestionActivity extends UltaBaseActivity
{
	private String question_text, email, nickName, location, page_id, qtype;
	private TextView txtQuestion, txtQuestionDetails, txtNotificationMail,
			txtTermsnConditions, txtPrivacyPolicy;
	private Button btnEdit, btnSubmit;
	private CheckBox chkAgree;
	private LinearLayout loadingLayout;
	private boolean isPolicyAgreed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_question_layout);
		setTitle("Preview");
		initViews();
		setValuesForPreview();
		setActions();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setTheTextClickable();
		if (UltaDataCache.getDataCacheInstance().isQuestionSubmitted()) {
			finish();
		}
	}

	private void setActions() {
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						finish();
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						loadingLayout.setVisibility(View.VISIBLE);
				if (isPolicyAgreed) {
					invokeSubmitQuestion();
				}
				else {
					notifyUser(
							"Please agree to the terms & conditions for submitting your question",
							PreviewQuestionActivity.this);
					loadingLayout.setVisibility(View.GONE);
				}
			}
		});
		chkAgree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
						if (isChecked) {
					isPolicyAgreed = true;
				}
				else
					isPolicyAgreed = false;
			}
		});
	}

	private void setValuesForPreview() {
		Bundle previewDetails = getIntent().getExtras();

		if (previewDetails != null && !previewDetails.isEmpty()) {
			Calendar c = Calendar.getInstance();
			String date = c.get(Calendar.MONTH) + 1 + "/"
					+ c.get(Calendar.DATE) + "/" + c.get(Calendar.YEAR);
			question_text = previewDetails.getString("qtext");
			email = previewDetails.getString("qemail");
			nickName = previewDetails.getString("qname");
			location = previewDetails.getString("qlocation");
			page_id = previewDetails.getString("skuid");
			qtype = previewDetails.getString("qtype");
			if (question_text != null && !question_text.isEmpty()) {
				txtQuestion.setText(question_text);
			}
			if (email != null && !email.isEmpty()) {
				txtNotificationMail.setText(email);
			}
			if (nickName != null && !nickName.isEmpty() && location != null
					&& !location.isEmpty()) {

				txtQuestionDetails.setText("By " + nickName + " from "
						+ location + " on " + date);
			}
			else if (nickName != null && !nickName.isEmpty()) {
				location = "US";
				txtQuestionDetails.setText("By " + nickName + " from US on "
						+ date);
			}

		}
		else {
			notifyUser(
					"Please go back to the previous page and fill the details",
					PreviewQuestionActivity.this);
		}
		loadingLayout.setVisibility(View.GONE);
	}

	private void setTheTextClickable() {
		String text = getResources().getString(R.string.terms_text);
		SpannableString clickHere = new SpannableString(text);
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				startActivity(new Intent(PreviewQuestionActivity.this,
						LegalActivity.class));
			}
		};
		clickHere.setSpan(clickableSpan, 15, 33,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		txtTermsnConditions.setText(clickHere);
		txtTermsnConditions.setMovementMethod(LinkMovementMethod.getInstance());

		txtPrivacyPolicy.setText(Html.fromHtml("<a href='junk'>"
				+ getResources().getString(R.string.privacy_text) + "</a>"));
		txtPrivacyPolicy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						startActivity(new Intent(PreviewQuestionActivity.this,
						PrivacyPolicyActivity.class));
			}
		});

	}

	private void initViews() {
		txtNotificationMail = (TextView) findViewById(R.id.preview_txtNotificationMail);
		txtQuestion = (TextView) findViewById(R.id.preview_txtQuestion);
		txtQuestionDetails = (TextView) findViewById(R.id.preview_txtQuestionDetails);
		btnEdit = (Button) findViewById(R.id.btnEditQuestion);
		btnSubmit = (Button) findViewById(R.id.btnSubmitQuestion);
		chkAgree = (CheckBox) findViewById(R.id.preview_chkIAgree);
		txtTermsnConditions = (TextView) findViewById(R.id.terms_text);
		txtPrivacyPolicy = (TextView) findViewById(R.id.privacy_text);
		loadingLayout = (LinearLayout) findViewById(R.id.preview_loading_layout);
		loadingLayout.setVisibility(View.VISIBLE);
	}

	private void invokeSubmitQuestion() {
		InvokerParams<ProductReviewPostResponseBean> invokerParams = new InvokerParams<ProductReviewPostResponseBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.WRITE_QUESTION_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateAskQuestionParameters());
		invokerParams.setUltaBeanClazz(ProductReviewPostResponseBean.class);
		// invokerParams.setUserSessionClearingRequired(true);
		AskQuestionHandler askQuestionHandler = new AskQuestionHandler();
		invokerParams.setUltaHandler(askQuestionHandler);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.POWER_REVIEWS_WRITE_CONTEXT);
		invokerParams.setCookieHandlingSkip(true);

		try {
			new ExecutionDelegator(invokerParams);
		}
		catch (UltaException ultaException) {
			Logger.Log("<WriteLoginActivity><invokeLogin><UltaException>>"
					+ ultaException);
		}
	}

	private Map<String, String> populateAskQuestionParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("merchant_id", WebserviceConstants.MERCHANT_ID_FOR_QnA);
		urlParams.put("page_id", page_id);
		urlParams.put("locale", "en_US");
		urlParams.put("q_name", nickName);
		urlParams.put("q_location", location);
		urlParams.put("q_text", question_text);
		urlParams.put("q_email", email);
		urlParams.put("q_type", qtype);
		urlParams.put("apikey", WebserviceConstants.API_KEY_FOR_QnA);
		return urlParams;

	}

	public class AskQuestionHandler extends UltaHandler
	{
		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<WriteReviewHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				try {
					loadingLayout.setVisibility(View.GONE);
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							PreviewQuestionActivity.this);
				}
				catch (WindowManager.BadTokenException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				Logger.Log("<WriteReviewHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				ProductReviewPostResponseBean ultaBean = (ProductReviewPostResponseBean) getResponseBean();
				String result = ultaBean.getResponse_code();
				Logger.Log("<WriteReviewHandlerr><handleMessage><getResponseBean>>"
						+ result);
				if (result.equalsIgnoreCase("200")) {
					UltaDataCache.getDataCacheInstance().setQuestionSubmitted(
							true);
					startActivity(new Intent(PreviewQuestionActivity.this,
							QnAThankyouActivity.class).putExtra("id", page_id));

				}
				else {
					notifyUser("Ask Ulta Beauty", "Question is not posted.",
							PreviewQuestionActivity.this);
				}
			}
		}
	}
}
