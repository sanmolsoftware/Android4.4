/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.account.PaymentMethodBean;
import com.ulta.core.bean.checkout.PaymentDetailsBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.fragments.account.ProfilePaymentFragment;
import com.ulta.core.fragments.account.ProfilePaymentFragment.OnDeletePayment;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
import android.content.Context;*/
/*import android.widget.Button;*/

/**
 * The Class PaymentMethodListActvity.
 */
public class PaymentMethodListActvity extends UltaBaseActivity implements
		OnDeletePayment, OnSessionTimeOut
{

	/** The payment method bean. */
	PaymentMethodBean paymentMethodBean;

	/** The list of credit cards. */
	ListView listOfCreditCards;

	/** The context. */
/*	private Context context;*/

	/** The credit cards list. */
	private List<PaymentDetailsBean> creditCardsList;

	/** The Linear Layouts. */
	LinearLayout main_layout,NoContent;
	
	FrameLayout loadingDialog;

	/** The default credit card id. */
	private String defaultCreditCardId;

	/*private Button btnAddCreditCard;*/

	ProfilePaymentFragment fragment;
	TextView mSavedCardTitleTV,mNo_saved_cards_message;

	/*
	 * boolean isOnCreateCalled=false;
	 * @Override protected void onPause() { SharedPreferences preferences=
	 * getPreferences(MODE_PRIVATE); Editor editor=preferences.edit();
	 * editor.putBoolean("isLoggedIn",
	 * isUltaCustomer(PaymentMethodListActvity.this));
	 * editor.putBoolean("onPauseCalled", true); editor.commit();
	 * super.onPause(); }
	 * @Override protected void onResume() {
	 * if(!isUltaCustomer(PaymentMethodListActvity.this)){ finish(); }else
	 * if(!isOnCreateCalled){ SharedPreferences preferences=
	 * getPreferences(MODE_PRIVATE); boolean
	 * onPausedCalled=preferences.getBoolean("onPauseCalled", false);
	 * if(onPausedCalled){ refreshPage(); } } isOnCreateCalled=false;
	 * super.onResume(); }
	 */

	@Override
	protected void onResume() {
		super.onResume();
		refreshPage();
	}

	public void refreshPage() {
		NoContent.setVisibility(View.GONE);
		loadingDialog.setVisibility(View.VISIBLE);
		invokePrefferedPaymentMethodDetails();

	}

	/*
	 * (non-Javadoc)
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// isOnCreateCalled=true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account_payment_method);
		setTitle("Payment");
		main_layout = (LinearLayout) findViewById(R.id.main_layout);
		loadingDialog = (FrameLayout) findViewById(R.id.loadingDialogPayment);
		NoContent = (LinearLayout) findViewById(R.id.NoContent);
		NoContent.setVisibility(View.GONE);
		mSavedCardTitleTV= (TextView) findViewById(R.id.tv);
		mNo_saved_cards_message=(TextView)findViewById(R.id.noSavedCardsMessageTV);
		// setTitle("Payment Method");

		loadingDialog.setVisibility(View.VISIBLE);
		// main_layout.setVisibility(View.GONE);
		// NoContent.setVisibility(View.GONE);
		fragment = (ProfilePaymentFragment) getSupportFragmentManager()
				.findFragmentById(R.id.profile_payment_fragment);

		fragment.setOnDeletePayment(this);

		registerForLogoutBroadcast();

		invokePrefferedPaymentMethodDetails();

	}

	/*
	 * @Override protected void onResume() {
	 * super.onResume(); invokePrefferedPaymentMethodDetails(); }
	 */
	@Override
	public void onLogout() {
		finish();
	}

	/**
	 * Invoke preffred shippingaddress details.
	 */
	private void invokePrefferedPaymentMethodDetails() {

		InvokerParams<PaymentMethodBean> invokerParams = new InvokerParams<PaymentMethodBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.PAYMENT_METHOD_DETAILS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populatePaymentMethodDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(PaymentMethodBean.class);
		RetrievePaymentDetailsHandler retrievePaymentDetailsHandler = new RetrievePaymentDetailsHandler();
		invokerParams.setUltaHandler(retrievePaymentDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		}
		catch (UltaException ultaException) {
			Logger.Log("<PaymentMethodListActivity><invokePreffredPaymentMethodDetails()><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Populate payment method details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populatePaymentMethodDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		urlParams.put("atg-rest-depth", "0");
		return urlParams;
	}

	/**
	 * The Class RetrievePaymentDetailsHandler.
	 */
	public class RetrievePaymentDetailsHandler extends UltaHandler
	{

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(PaymentMethodListActvity.this);
				}
				else {
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								PaymentMethodListActvity.this);
					}
					catch (WindowManager.BadTokenException e) {
					}
					catch (Exception e) {
					}
				}
			}
			else {

				Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				paymentMethodBean = (PaymentMethodBean) getResponseBean();
				TextView nameOnCard = (TextView) findViewById(R.id.creditcard_name_on_card);
				TextView cardType = (TextView) findViewById(R.id.creditcard_cardtype);
				TextView cardNumber = (TextView) findViewById(R.id.creditcard_card_no);
				TextView expYear = (TextView) findViewById(R.id.creditcard_expiryDate);
				TextView textView01 = (TextView) findViewById(R.id.textView01);
				TextView textView02 = (TextView) findViewById(R.id.textView02);
				TextView textView03 = (TextView) findViewById(R.id.textView03);
				TextView textView04 = (TextView) findViewById(R.id.creditcard_exp_date);
				ImageView cardTypeImage= (ImageView) findViewById(R.id.cardTypeImage);
				LinearLayout expiryLayout= (LinearLayout) findViewById(R.id.expiryLayout);
				if (null != paymentMethodBean) {
					mSavedCardTitleTV.setText("Additional Payment Methods");
					Logger.Log("<PaymentMethodListActivity>" + "BeanPopulated");

					creditCardsList = paymentMethodBean.getCreditCards();

					defaultCreditCardId = paymentMethodBean
							.getDefaultCreditCardId();

					loadingDialog.setVisibility(View.GONE);
					main_layout.setVisibility(View.VISIBLE);
					mNo_saved_cards_message.setVisibility(View.GONE);
					if (null != defaultCreditCardId) {
						loadingDialog.setVisibility(View.GONE);
						main_layout.setVisibility(View.VISIBLE);

						if (null != creditCardsList
								&& !creditCardsList.isEmpty()) {

							int position = 0;
							for (int i = 0; i < creditCardsList.size(); i++) {

								if (null != creditCardsList.get(i).getId()
										&& creditCardsList.get(i).getId()
												.equals(defaultCreditCardId)) {
									position = i;
									Logger.Log("POSITION IN LOOP" + position);
									break;
								}
							}
							Logger.Log("POSITION" + position);
							Logger.Log("defaulyID" + defaultCreditCardId);
							if(null != creditCardsList.get(position)
									.getCreditCardNumber()){
								cardNumber.setText("************"
										+ creditCardsList.get(position)
												.getCreditCardNumber()
												.substring(12));
							}
							if(null!= creditCardsList.get(position).getNickName())
							{
								textView01.setText(creditCardsList.get(position).getNickName());
							}
							// cardNumber.setText(creditCardsList.get(position).getCreditCardNumber());
							cardType.setText(creditCardsList.get(position)
									.getCreditCardType());
							CreditCardInfoBean cardInfoBean=identifyCardType(creditCardsList.get(position).getCreditCardNumber());
							cardTypeImage.setVisibility(View.VISIBLE);
							if(null!=cardInfoBean) {
								LoadImageFromWebOperations(cardInfoBean.getCardImage(),cardTypeImage);
							}
							expYear.setText("Expiration Date: "+creditCardsList.get(position)
									.getExpirationMonth()
									+ "/"
									+ creditCardsList.get(position)
											.getExpirationYear());
							nameOnCard.setText("Name on card: "+creditCardsList.get(position)
									.getNameOnCard());
							if(!checkIfExpirationNeeded(creditCardsList.get(position)
									.getCreditCardType()))
							{
								expiryLayout.setVisibility(View.GONE);
							}
							else
							{
								expiryLayout.setVisibility(View.VISIBLE);
							}

						}


					}
					else {
						loadingDialog.setVisibility(View.GONE);
						// NoContent.setVisibility(View.VISIBLE);
						main_layout.setVisibility(View.VISIBLE);
						mNo_saved_cards_message.setVisibility(View.GONE);
						if(null!=creditCardsList&&creditCardsList.size()==0)
						{
							main_layout.setVisibility(View.GONE);
							mNo_saved_cards_message.setVisibility(View.VISIBLE);
							mSavedCardTitleTV.setText("No Saved Cards Found");
						}
						/*
						 * textView01.setVisibility(View.INVISIBLE);
						 * textView03.setVisibility(View.INVISIBLE);
						 * textView04.setVisibility(View.INVISIBLE);
						 * nameOnCard.setVisibility(View.INVISIBLE);
						 */
						cardNumber.setText("");
						textView01.setText("");
						cardType.setText("");
						expYear.setText("");
						nameOnCard.setText("");
						textView01.setVisibility(View.INVISIBLE);
						cardTypeImage.setImageResource(R.drawable.creditcard_default);
						cardTypeImage.setVisibility(View.VISIBLE);
						cardNumber.setText("There are no Default Payment Details");
						/*
						 * cardNumber.setVisibility(View.INVISIBLE);
						 * expYear.setVisibility(View.INVISIBLE);
						 */
					}

				}
				else {
					loadingDialog.setVisibility(View.GONE);
					mSavedCardTitleTV.setText("No Saved Cards Found");
					mNo_saved_cards_message.setVisibility(View.VISIBLE);
					// NoContent.setVisibility(View.VISIBLE);
					main_layout.setVisibility(View.GONE);
					textView01.setVisibility(View.INVISIBLE);
					textView02.setVisibility(View.INVISIBLE);
					textView03.setVisibility(View.INVISIBLE);
					textView04.setVisibility(View.INVISIBLE);
					nameOnCard.setVisibility(View.INVISIBLE);
					cardTypeImage.setImageResource(R.drawable.creditcard_default);
					cardTypeImage.setVisibility(View.VISIBLE);
					cardNumber.setText("There are no Default Payment Details");
					expYear.setVisibility(View.INVISIBLE);
				}

				ProfilePaymentFragment fragmentforcards = (ProfilePaymentFragment) getSupportFragmentManager()
						.findFragmentById(R.id.profile_payment_fragment);
				if (null != fragmentforcards) {
					fragmentforcards.populateCardList(creditCardsList,
							defaultCreditCardId);
				}

			}
		}

	}

	@Override
	public void OnDeletePayment() {
		invokePrefferedPaymentMethodDetails();
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			invokePrefferedPaymentMethodDetails();
		}
	}

	/* *
	 * end of List of credit cards
	 */
	/**
	 * Download image from url using Aquery and set to the card type
	 *
	 * @param url
	 */
	public void LoadImageFromWebOperations(String url,final ImageView cardTypeImageView) {
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			AQuery androidQuery = new AQuery(PaymentMethodListActvity.this);
			androidQuery.ajax(url, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
				@Override
				public void callback(String url, Bitmap bitmap, AjaxStatus status) {
					Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 62, false));
					cardTypeImageView.setImageDrawable(drawable);

				}
			});
		} catch (Exception e) {
			Logger.Log("<UltaException>>"
					+ e);
		}
	}
}
