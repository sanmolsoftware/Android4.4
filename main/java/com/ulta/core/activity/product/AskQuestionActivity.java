package com.ulta.core.activity.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.about.ContactUsActivity;
import com.ulta.core.bean.product.TypeOfQuestionBean;
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
import com.ulta.core.widgets.flyin.OnDoneClickedListener;
import com.ulta.core.widgets.flyin.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.util.Utility.validateEmail;

public class AskQuestionActivity extends UltaBaseActivity implements
		OnDoneClickedListener {
	private EditText edtQuestion, edtEmail, edtNickName, edtLocation;
	private String question_text, email, nickName, location, page_id,
			questionType;
	TitleBar titlebar;
	private int qtype = -1;
	FrameLayout loadingLayout;
	LinearLayout llTypeOfQuestions, llThankyouLayout, llEnterQuestionLayout;
	private static String INVALID_NICK_NAME = "Please enter a valid Nickname";
	private static String EMAIL_VALIDATION_MESSAGE = "Please enter valid Email";
	private static String QUETION_TEXT_ERROR = "Please enter valid and meaningful question";
	private static String TYPE_OF_QUESTION_ERROR = "Please select type of question";
	private RadioGroup radioGrpForTypeOfQ;
	ArrayList<String> listOfQuesionTypes;
	private TextView contactUs, productQnAGuidelines, shippingInfo;
	private Spinner typeOfQuestion;
	private TypeOfQuestionBean respBean;
	private RadioButton aboutService, aboutProduct;
	public boolean isSpinnerSelected = false;
	static boolean firsttime = true;
	int my_selection = -1;
	private Button mPreviewButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ask_question_layout);
		setTitle("Ask Ulta Beauty");
		initViews();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("qtype", qtype);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		qtype = savedInstanceState.getInt("qtype");
		if (qtype == 0) {
			aboutService.setChecked(true);
			llThankyouLayout.setVisibility(View.VISIBLE);
		} else if (qtype == 1) {
			aboutProduct.setChecked(true);
			llEnterQuestionLayout.setVisibility(View.VISIBLE);
			titlebar.enableDone();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (UltaDataCache.getDataCacheInstance().isQuestionSubmitted()) {
			UltaDataCache.getDataCacheInstance().setQuestionSubmitted(false);
			finish();
		} else {
			setTheTextClickable();
			if (null != getIntent().getExtras()
					&& null != getIntent().getExtras().getString("id")) {
				page_id = getIntent().getExtras().getString("id");
			}
			respBean = UltaDataCache.getDataCacheInstance().getTypeOfQuestion();
			if (respBean != null) {
				showTypeOfQuestions();
			} else {
				getTypesOfQuestion();
			}
		}

	}

	public void setTheTextClickable() {
		String text = getResources().getString(R.string.qna_contact);
		int end = text.length();
		SpannableString clickHere = new SpannableString(text);
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				startActivity(new Intent(AskQuestionActivity.this,
						ContactUsActivity.class));
			}
		};
		clickHere.setSpan(clickableSpan, (end - 4), end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		contactUs.setText(clickHere);
		contactUs.setMovementMethod(LinkMovementMethod.getInstance());

		String shippingInfoText = getResources().getString(
				R.string.shipping_info);
		int shippingInfoTextEnd = shippingInfoText.length();
		SpannableString shippngInfo = new SpannableString(shippingInfoText);
		ClickableSpan clickableSpanForShippingInfo = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(WebserviceConstants.SHIPPING_INFO_LINK));
				startActivity(browserIntent);

			}
		};
		shippngInfo.setSpan(clickableSpanForShippingInfo,
				shippingInfoTextEnd - 26, shippingInfoTextEnd - 6,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		shippingInfo.setText(shippngInfo);
		shippingInfo.setMovementMethod(LinkMovementMethod.getInstance());
		productQnAGuidelines.setText(Html.fromHtml("<a href='junk'>"
				+ getResources().getString(R.string.qna_guidelines) + "</a>"));
		productQnAGuidelines.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AskQuestionActivity.this,
						ProductQnAGuidelinesActivity.class));
			}
		});

	}

	private void initViews() {
		edtQuestion = (EditText) findViewById(R.id.edtQ_text);
		edtEmail = (EditText) findViewById(R.id.edtQ_Email);
		edtNickName = (EditText) findViewById(R.id.edtQ_nickName);
		edtLocation = (EditText) findViewById(R.id.edtQ_location);
		loadingLayout = (FrameLayout) findViewById(R.id.llLoading);
		loadingLayout.setVisibility(View.VISIBLE);
		llTypeOfQuestions = (LinearLayout) findViewById(R.id.llTypeOfQuestions);
		radioGrpForTypeOfQ = (RadioGroup) findViewById(R.id.radioGrpForTypeOfQ);
		llThankyouLayout = (LinearLayout) findViewById(R.id.thankyou_layout);
		llEnterQuestionLayout = (LinearLayout) findViewById(R.id.enter_question_layout);
		titlebar = (TitleBar) findViewById(R.id.titlebar);
		titlebar.displayPreviewButton();
		contactUs = (TextView) findViewById(R.id.contactus_text);
		shippingInfo = (TextView) findViewById(R.id.shipping_info);
		productQnAGuidelines = (TextView) findViewById(R.id.qna_guidelines);
		typeOfQuestion = (Spinner) findViewById(R.id.typeOfQ_spinner);
		aboutService = (RadioButton) findViewById(R.id.rd_aboutService);
		aboutProduct = (RadioButton) findViewById(R.id.rd_aboutProduct);
		mPreviewButton = (Button) findViewById(R.id.previewButton);

		mPreviewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(
						edtLocation.getApplicationWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				validateFields();
			}
		});
	}

	private void getTypesOfQuestion() {
		InvokerParams<TypeOfQuestionBean> invokerParams = new InvokerParams<TypeOfQuestionBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.TYPE_OF_QUESTION_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		invokerParams.setUrlParameters(urlParams);

		invokerParams.setUltaBeanClazz(TypeOfQuestionBean.class);
		RetrieveTypeOfQuestionHandler typeOfQuestionHandler = new RetrieveTypeOfQuestionHandler();
		invokerParams.setUltaHandler(typeOfQuestionHandler);

		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	public class RetrieveTypeOfQuestionHandler extends UltaHandler {
		public void handleMessage(Message msg) {
			Logger.Log("QuestionDetails error" + (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							AskQuestionActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("QuestionDetails Response" + getResponseBean());
				respBean = (TypeOfQuestionBean) getResponseBean();
				UltaDataCache.getDataCacheInstance()
						.setTypeOfQuestion(respBean);
				showTypeOfQuestions();
			}
		}
	}

	/**
	 * The Class ProductListAdapter.
	 */
	public void showTypeOfQuestions() {
		aboutService.setText(respBean.getAtgResponse().getQnType1());
		aboutService.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					qtype = 0;
					titlebar.disableDone();
					llThankyouLayout.setVisibility(View.VISIBLE);
					llEnterQuestionLayout.setVisibility(View.GONE);
				}
			}
		});
		aboutProduct.setText(respBean.getAtgResponse().getQnType2());
		aboutProduct.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					qtype = 1;
					// titlebar.enableDone();
					llThankyouLayout.setVisibility(View.GONE);
					llEnterQuestionLayout.setVisibility(View.VISIBLE);

				}
			}
		});
		setSpinner();
		loadingLayout.setVisibility(View.GONE);
	}

	protected void setSpinner() {
		List<String> list = null;

		if (respBean.getAtgResponse().getQnSubType2() != null
				&& respBean.getAtgResponse().getQnSubType2()
						.equals("Product Oriented")) {
			list = respBean.getAtgResponse().getProduct_Oriented();
		} else {

			list = respBean.getAtgResponse().getBusiness_Oriented();
		}
		final String[] anArrayOfStrings = new String[list.size()];
		list.toArray(anArrayOfStrings);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				AskQuestionActivity.this, R.layout.list_item, anArrayOfStrings) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				/*
				 * if(firsttime) { TextView tv = new
				 * TextView(AskQuestionActivity.this);
				 * tv.setText("Product Oriented");
				 * tv.setTextColor(getResources().getColor(R.color.black));
				 * tv.setPadding(5, 0, 0, 0); return tv; }
				 */
				return super.getView(position, convertView, parent);
			}
		};
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeOfQuestion.setAdapter(spinnerArrayAdapter);
		typeOfQuestion
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {
						questionType = anArrayOfStrings[position];
						Log.e("Jeeri", questionType);
						my_selection = position;
						firsttime = false;
						isSpinnerSelected = true;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}

	@Override
	public void onDoneClicked() {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(edtLocation.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		validateFields();
	}

	private void validateFields() {
		email = edtEmail.getText().toString();
		question_text = edtQuestion.getText().toString();
		nickName = edtNickName.getText().toString();
		location = edtLocation.getText().toString();
		location = location.trim();
		nickName = nickName.trim();
		question_text = question_text.trim();
		if (qtype != 0 && qtype != 1) {
			notifyUser(TYPE_OF_QUESTION_ERROR, AskQuestionActivity.this);
			radioGrpForTypeOfQ.requestFocus();
		} else if (question_text == null || question_text.isEmpty()) {
			try {
				notifyUser(QUETION_TEXT_ERROR, AskQuestionActivity.this);
				edtQuestion.requestFocus();

			} catch (WindowManager.BadTokenException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (email.length() == 0) {
			try {
				notifyUser(EMAIL_VALIDATION_MESSAGE, AskQuestionActivity.this);
				edtEmail.requestFocus();
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		} else if (!validateEmail(email)) {

			try {
				notifyUser(EMAIL_VALIDATION_MESSAGE, AskQuestionActivity.this);
				edtEmail.requestFocus();
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}

		} else if (nickName == null || nickName.isEmpty()) {
			try {
				notifyUser(INVALID_NICK_NAME, AskQuestionActivity.this);
				edtNickName.requestFocus();
			} catch (WindowManager.BadTokenException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (!isSpinnerSelected && !firsttime) {
			try {
				notifyUser(TYPE_OF_QUESTION_ERROR, AskQuestionActivity.this);
				edtLocation.requestFocus();
			} catch (WindowManager.BadTokenException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			loadingLayout.setVisibility(View.VISIBLE);
			Intent gotoPreviewQuestion = new Intent(AskQuestionActivity.this,
					PreviewQuestionActivity.class);
			gotoPreviewQuestion.putExtra("qtext", question_text);
			gotoPreviewQuestion.putExtra("skuid", page_id);
			gotoPreviewQuestion.putExtra("qname", nickName);
			gotoPreviewQuestion.putExtra("qlocation", location);
			gotoPreviewQuestion.putExtra("qemail", email);
			gotoPreviewQuestion.putExtra("qtype", String.valueOf(qtype));
			startActivity(gotoPreviewQuestion);
		}
	}
}
