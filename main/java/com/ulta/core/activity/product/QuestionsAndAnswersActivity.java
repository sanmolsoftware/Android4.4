package com.ulta.core.activity.product;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.AnswerCountBean;
import com.ulta.core.bean.product.AnswersDeatilsBean;
import com.ulta.core.bean.product.QuestionCountBean;
import com.ulta.core.bean.product.QuestionDetailsBean;
import com.ulta.core.bean.product.QuestionsListBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionsAndAnswersActivity extends UltaBaseActivity {
	/** Button to navigate to Ask Ulta page */
	private Button btnAskQuesion;
	/** String for holding the product id of the product for which we need Q n A */
	private String page_id;
	/** Loading layout to show loading message */
	private LinearLayout mainLayout;
	private FrameLayout loadingDialog;
	/** Text view for displaying count of questions and Answers */
	private TextView txtQuestionsCount, txtAnswersCount;
	private List<QuestionDetailsBean> listOfQuestions;
	ListView lvQuestions;
	QuestionAnswersAdapter adapter;
	private int pageNum;
	ProgressDialog pd;

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_question_n_answers);
		if (null != getIntent().getExtras()
				&& null != getIntent().getExtras().getString("id")) {
			page_id = getIntent().getExtras().getString("id");
		}
		setTitle("Ask Ulta Beauty");
		initViews();
		invokeRetrieveQuestionsCount();
		btnAskQuesion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						Intent gotoAskQuestionActivity = new Intent(
						QuestionsAndAnswersActivity.this,
						AskQuestionActivity.class);
				gotoAskQuestionActivity.putExtra("id", page_id);
				startActivity(gotoAskQuestionActivity);
			}
		});
		pd = new ProgressDialog(QuestionsAndAnswersActivity.this);
		setProgressDialogLoadingColor(pd);
		pd.setMessage("Loading...");
		pd.setCancelable(false);
	}

	private void initViews() {
		btnAskQuesion = (Button) findViewById(R.id.btnAskQuestion);
		txtQuestionsCount = (TextView) findViewById(R.id.txtQuestionsCount);
		txtAnswersCount = (TextView) findViewById(R.id.txtAnswersCount);
		loadingDialog = (FrameLayout) findViewById(R.id.QnAloadingDialog);
		loadingDialog.setVisibility(View.VISIBLE);
		mainLayout = (LinearLayout) findViewById(R.id.questionsMainLayout);
		mainLayout.setVisibility(View.GONE);
		lvQuestions = (ListView) findViewById(R.id.lvquestion_answer);

	}

	private Map<String, String> populateGetCountParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("apikey", WebserviceConstants.API_KEY_FOR_QnA);
		urlParams.put("page_id", page_id);
		urlParams.put("merchant_id", WebserviceConstants.MERCHANT_ID_FOR_QnA);
		return urlParams;
	}

	private void invokeRetrieveQuestionsCount() {
		InvokerParams<QuestionCountBean> invokerParams = new InvokerParams<QuestionCountBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.GET_QUESTION_COUNT);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateGetCountParameters());

		invokerParams.setUltaBeanClazz(QuestionCountBean.class);
		RetrieveQuestionsCountHandler retrieveCountHandler = new RetrieveQuestionsCountHandler();
		invokerParams.setUltaHandler(retrieveCountHandler);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.POWER_REVIEWS_CONTEXT);
		invokerParams.setCookieHandlingSkip(true);

		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	public class RetrieveQuestionsCountHandler extends UltaHandler {
		public void handleMessage(Message msg) {
			Logger.Log("reviews error" + (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					loadingDialog.setVisibility(View.GONE);
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							QuestionsAndAnswersActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<RetrieveProductDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				QuestionCountBean countBean = (QuestionCountBean) getResponseBean();
				if (null == countBean) {
					/*final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							QuestionsAndAnswersActivity.this);

					alertDialog.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					alertDialog.setCancelable(false);
					alertDialog.setTitle("Sorry");
					alertDialog
							.setMessage("There are no questions for this product");
					alertDialog.create().show();*/

					final Dialog alertDialog = showAlertDialog(
							QuestionsAndAnswersActivity.this, "Sorry",
							"There are no questions for this product", "Ok", "");
					alertDialog.show();
					
					mDisagreeButton.setVisibility(View.GONE);
					mAgreeButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {

							alertDialog.dismiss();
						}
					});
					

				}

				else if (null != countBean) {
					txtQuestionsCount.setText(countBean.getCount().get(0)
							.getNumberOfQuestions()
							+ " Questions");
					invokeRetrieveAnswersCount();
				}
			}
		}
	}

	private void invokeRetrieveAnswersCount() {
		InvokerParams<AnswerCountBean> invokerParams = new InvokerParams<AnswerCountBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.GET_ANSWER_COUNT);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateGetCountParameters());

		invokerParams.setUltaBeanClazz(AnswerCountBean.class);
		RetrieveAnswersCountHandler retrieveCountHandler = new RetrieveAnswersCountHandler();
		invokerParams.setUltaHandler(retrieveCountHandler);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.POWER_REVIEWS_CONTEXT);
		invokerParams.setCookieHandlingSkip(true);

		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	public class RetrieveAnswersCountHandler extends UltaHandler {
		public void handleMessage(Message msg) {
			Logger.Log("reviews error" + (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					loadingDialog.setVisibility(View.GONE);
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							QuestionsAndAnswersActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<RetrieveProductDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				AnswerCountBean countBean = (AnswerCountBean) getResponseBean();
				if (null == countBean) {
					/*final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							QuestionsAndAnswersActivity.this);

					alertDialog.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					alertDialog.setCancelable(false);
					alertDialog.setTitle("Sorry");
					alertDialog
							.setMessage("There are no Answers for the questions asked!");
					alertDialog.create().show();*/
					
					final Dialog alertDialog = showAlertDialog(
							QuestionsAndAnswersActivity.this, "Sorry",
							"There are no Answers for the questions asked!", "Ok", "");
					alertDialog.show();
					
					mDisagreeButton.setVisibility(View.GONE);
					mAgreeButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {

							alertDialog.dismiss();
						}
					});
				}

				else if (null != countBean) {
					txtAnswersCount.setText(countBean.getCount().get(0)
							.getNumberOfAnswers()
							+ " Answers");
					invokeQuestionAnsAnswers();
				}
			}
		}
	}

	public void invokeQuestionAnsAnswers() {
		if (pageNum != 0) {
			pd.show();
		}
		InvokerParams<QuestionsListBean> invokerParams = new InvokerParams<QuestionsListBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.GET_QUESTIONS);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateQuestionDetailsParameters());

		invokerParams.setUltaBeanClazz(QuestionsListBean.class);
		RetrieveQuestionDetails retrieveQuestionDetails = new RetrieveQuestionDetails();
		invokerParams.setUltaHandler(retrieveQuestionDetails);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.POWER_REVIEWS_CONTEXT);
		invokerParams.setCookieHandlingSkip(true);

		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	private Map<String, String> populateQuestionDetailsParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("apikey", WebserviceConstants.API_KEY_FOR_QnA);
		urlParams.put("page_id", page_id);
		urlParams.put("merchant_id", WebserviceConstants.MERCHANT_ID_FOR_QnA);
		urlParams.put("page_sige", "10");
		urlParams.put("page", String.valueOf(pageNum));
		urlParams.put("sort", "q_created_dateasc");
		return urlParams;
	}

	public class RetrieveQuestionDetails extends UltaHandler {
		public void handleMessage(Message msg) {
			Logger.Log("QuestionDetails error" + (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					pd.dismiss();
					loadingDialog.setVisibility(View.GONE);
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							QuestionsAndAnswersActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {

				Logger.Log("<RetrieveQuestionDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				QuestionsListBean questionList = (QuestionsListBean) getResponseBean();
				if (questionList != null) {
					loadingDialog.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
					if (listOfQuestions == null) {
						listOfQuestions = questionList.getData();
						adapter = new QuestionAnswersAdapter(
								QuestionsAndAnswersActivity.this);
						lvQuestions.setAdapter(adapter);
					} else {

						listOfQuestions.addAll(questionList.getData());
						adapter.notifyDataSetChanged();

						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
					}
				}
			}
		}
	}

	public class QuestionAnswersAdapter extends BaseAdapter {

		/** The context. */
		private Context context;

		/**
		 * Instantiates a new product list adapter.
		 * 
		 * @param context
		 *            the context
		 */
		public QuestionAnswersAdapter(Context context) {
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view;
			view = inflater.inflate(R.layout.question_item, null);
			if (listOfQuestions != null && !listOfQuestions.isEmpty()) {
				final QuestionDetailsBean questionDetailsBean;
				questionDetailsBean = listOfQuestions.get(position);
				TextView tvQuestion = (TextView) view
						.findViewById(R.id.txtQuestion);
				TextView tvAnswerNumber = (TextView) view
						.findViewById(R.id.txtAnswersNumber);
				TextView tvQuestionDetails = (TextView) view
						.findViewById(R.id.txtQuestionDetails);
				final ImageView imgArrow = (ImageView) view
						.findViewById(R.id.imgArrow);
				imgArrow.setId(2);
				imgArrow.setBackgroundResource(R.drawable.arrow_right2);
				tvQuestion.setText("Q: " + questionDetailsBean.getQ_text());
				tvAnswerNumber.setText(questionDetailsBean
						.getQ_expert_answer_count() + " answer");
				final LinearLayout ansLayout = (LinearLayout) view
						.findViewById(R.id.answersLayout);
				ansLayout.setId(1);
				tvAnswerNumber.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
										if (ansLayout.getVisibility() == View.VISIBLE) {
							ansLayout.setVisibility(View.GONE);
							imgArrow.setBackgroundResource(R.drawable.arrow_right2);
						} else {
							ansLayout.setVisibility(View.VISIBLE);
							imgArrow.setBackgroundResource(R.drawable.arrow_down);
						}
					}
				});
				tvQuestionDetails
						.setText("Asked on "
								+ questionDetailsBean.getQ_created_date()
								+ " by "
								+ questionDetailsBean.getQ_name()
								+ " from "
								+ (questionDetailsBean.getQ_location()
										.isEmpty() ? "United States"
										: questionDetailsBean.getQ_location()));

				if (questionDetailsBean.getAnswers() != null
						&& !questionDetailsBean.getAnswers().isEmpty()) {
					List<AnswersDeatilsBean> answers = questionDetailsBean
							.getAnswers();
					for (int i = 0; i < answers.size(); i++) {
						final AnswersDeatilsBean answersDeatilsBean;
						answersDeatilsBean = answers.get(i);
						LayoutInflater inflater1 = (LayoutInflater) context
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View view1 = inflater1.inflate(R.layout.answer_item,
								null);

						TextView tvAnswer = (TextView) view1
								.findViewById(R.id.txtAnswer);
						TextView tvAnswerDetails = (TextView) view1
								.findViewById(R.id.txtAnswerDetails);
						tvAnswer.setText("A: " + answersDeatilsBean.getA_text());
						tvAnswerDetails
								.setText("Answered on "
										+ answersDeatilsBean
												.getA_created_date()
										+ " by "
										+ answersDeatilsBean.getA_name()
										+ " from "
										+ (answersDeatilsBean.getA_location()
												.isEmpty() ? "Ulta Beauty"
												: answersDeatilsBean
														.getA_location()));
						ansLayout.addView(view1);
					}
				}
				int noOfProductsPerPage = Integer
						.parseInt(WebserviceConstants.PAGE_SIZE_POWER_REVIEWS);
				if (position == (pageNum * noOfProductsPerPage + noOfProductsPerPage) - 1) {
					pageNum++;
					loadingDialog.setVisibility(View.VISIBLE);
					invokeQuestionAnsAnswers();
				}
			}

			return view;
		}

		@Override
		public int getCount() {

			return listOfQuestions.size();

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}
}
