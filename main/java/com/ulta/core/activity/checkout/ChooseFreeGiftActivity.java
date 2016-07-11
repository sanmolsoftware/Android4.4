package com.ulta.core.activity.checkout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.R.color;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.ViewItemsInBasketActivity;
import com.ulta.core.bean.product.AddToCartBean;
import com.ulta.core.bean.product.CommerceItemBean;
import com.ulta.core.bean.product.FreeGiftBean;
import com.ulta.core.bean.product.FreeGiftDetailBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChooseFreeGiftActivity extends UltaBaseActivity {

	private LinearLayout mFreeGiftsLayout;
	private FreeGiftBean freeGiftBean;
	List<FreeGiftDetailBean> listOfFreeGifts;
	private String mPromotionId;
	private int mPromotionSize;
	private CommerceItemBean mCommerceItemBean;

	/** The Constant RADIO_BTN_ID_INDEX. */
	public final static int RADIO_BTN_ID_INDEX = 0;

	/** The Constant RADIO_BTN_ID_INDEX. */
	public final static int RADIO_GROUP_INDEX = 1000;

	/** The radiobtn id. */
	private int radiobtnId = RADIO_BTN_ID_INDEX;

	private int radioGroupId = RADIO_GROUP_INDEX;

	private TextView mNoOfGiftsToBeSelected;
	private TextView mChooseFreeGiftMessage;
	private TextView mChooseFreeGiftPdtNameTextView;
	// private TextView mAlreadySelectedTextView;

	private Button mChooseFreeGiftDoneBtn;
	private HashMap<Integer, String> productAndSkuIDMap = new HashMap<Integer, String>();
	private ArrayList<String> mProductIdAndSkuId = new ArrayList<String>();

	private int mAddToCartToBeCalled = 0;
	private int mCountToNavigate = 0;
	private ProgressDialog mPDialog;
	private int mCountToCallInvokeFreeGift = 0;
	private int isAddedCount = 0;
	private Iterator iterator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_freegift);
		setTitle("Choose free Gift");
		mFreeGiftsLayout = (LinearLayout) findViewById(R.id.freeGiftLinearLayout);

		mNoOfGiftsToBeSelected = (TextView) findViewById(R.id.noOfGiftsSelected);
		mChooseFreeGiftMessage = (TextView) findViewById(R.id.chooseFromCategory);
		mChooseFreeGiftDoneBtn = (Button) findViewById(R.id.chooseFreeGiftDoneBtn);
		mChooseFreeGiftPdtNameTextView = (TextView) findViewById(R.id.chooseFreeGiftPdtNameTextView);
		// mAlreadySelectedTextView = (TextView)
		// findViewById(R.id.alreadySelectedTextView);

		mPDialog = new ProgressDialog(ChooseFreeGiftActivity.this);
		setProgressDialogLoadingColor(mPDialog);
		mPDialog.setMessage("Loading..");
		mPDialog.setCancelable(false);

		if (null != getIntent().getExtras()) {
			if (0 != getIntent().getExtras().getInt("promotionsSize")) {
				mPromotionSize = getIntent().getExtras().getInt(
						"promotionsSize");
				mNoOfGiftsToBeSelected.setText("You get " + mPromotionSize
						+ " FREE Gift(s)");
				mChooseFreeGiftMessage.setVisibility(View.VISIBLE);
			}

			if (null != getIntent().getExtras().getString("pdtName")) {
				mChooseFreeGiftPdtNameTextView.setText(getIntent().getExtras()
						.getString("pdtName"));
			}

		}

		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getSerializable("giftList")) {
				mCommerceItemBean = (CommerceItemBean) getIntent().getExtras()
						.getSerializable("giftList");
				for (int i = 0; i < mPromotionSize; i++) {
					mPromotionId = mCommerceItemBean.getPromotions().get(i)
							.getId();
					mPDialog.show();
					invokeFreeGiftDetails(mPromotionId);
					mCountToCallInvokeFreeGift++;
				}
			}
		}

		mChooseFreeGiftDoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				trackAppState(ChooseFreeGiftActivity.this,
						WebserviceConstants.CHECKOUT_ADD_FREE_GIFT);
				iterator = productAndSkuIDMap.entrySet().iterator();
				mAddToCartToBeCalled = productAndSkuIDMap.size();

				if (iterator.hasNext()) {
					Map.Entry pairs = (Map.Entry) iterator.next();
					String productIdAndSkuId = pairs.getValue().toString();
					String productIdAndSkuIdArray[] = productIdAndSkuId
							.split(",");
					mCountToNavigate++;
					mPDialog.show();
					fnInvokeAddToCart(productIdAndSkuIdArray[0],
							productIdAndSkuIdArray[1]);
					iterator.remove();
				}

			}
		});

	}

	private void invokeFreeGiftDetails(String promtionId) {
		InvokerParams<FreeGiftBean> invokerParams = new InvokerParams<FreeGiftBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.FREEGIFTDETAILS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(populateRetrieveFreeGiftDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(FreeGiftBean.class);
		RetrieveFreeGiftDetailsHandler retrieveFreeGiftDetailsHandler = new RetrieveFreeGiftDetailsHandler();
		invokerParams.setUltaHandler(retrieveFreeGiftDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			mPDialog.dismiss();
			Logger.Log("<FreeGiftActivity><invokeFreeGiftDetails()><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Populate retrieve Free Gift details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populateRetrieveFreeGiftDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("promotionId", mPromotionId);

		/* urlParams.put("quantity","1"); */
		/*
		 * urlParams.put("atg-rest-return-form-handler-properties", "true");
		 * urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		 */
		return urlParams;
	}

	/**
	 * The Class RetrieveFreeGiftDetailsHandler.
	 */
	public class RetrieveFreeGiftDetailsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveFreeGiftDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				mPDialog.dismiss();
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							ChooseFreeGiftActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<RetrieveFreeGiftDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				freeGiftBean = (FreeGiftBean) getResponseBean();
				if (null != freeGiftBean) {
					Logger.Log("<FreeGiftActivity>" + "BeanPopulated");
					// mFreeGiftBean.add(freeGiftBean);
					listOfFreeGifts = freeGiftBean.getAtgResponse();
					populateListOfFreeGifts(listOfFreeGifts);
					// populateListOfFreeGifts(listOfFreeGifts);
				}
			}
		}
	}

	public void populateListOfFreeGifts(
			List<FreeGiftDetailBean> listOfSingleCategoryFreeGifts) {
		final List<FreeGiftDetailBean> finalListOfSingleCategoryFreeGifts = listOfSingleCategoryFreeGifts;
		try {
			TextView categoryHeadingTextView = new TextView(
					ChooseFreeGiftActivity.this);
			categoryHeadingTextView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			categoryHeadingTextView.setText("Category");
			categoryHeadingTextView.setPadding(30, 10, 16, 10);
			categoryHeadingTextView.setBackgroundColor(getResources().getColor(
					R.color.shippingtype_relative_bg));
			mFreeGiftsLayout.addView(categoryHeadingTextView);

			RadioGroup mCategoryRadioGroup = new RadioGroup(
					ChooseFreeGiftActivity.this);

			mCategoryRadioGroup.setPadding(16, 10, 16, 10);
			radioGroupId++;

			for (int i = 0; i < listOfSingleCategoryFreeGifts.size(); i++) {
				mProductIdAndSkuId.add(finalListOfSingleCategoryFreeGifts
						.get(i).getProductId()
						+ ","
						+ finalListOfSingleCategoryFreeGifts.get(i).getId());

				RadioButton radioButton = new RadioButton(
						ChooseFreeGiftActivity.this);
				DownloadImageTask downloadImageTask = new DownloadImageTask(
						radioButton);
				downloadImageTask.execute(listOfSingleCategoryFreeGifts.get(i)
						.getSmallImageUrl());

				// Drawable freeGiftImage = new
				// BitmapDrawable(getResources(),bitMap);
				radioButton.setGravity(Gravity.CENTER_VERTICAL);
				radioButton.setButtonDrawable(android.R.color.transparent);

				radioButton.setPadding(16, 50, 16, 50);
				LayoutParams params = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				radioButton.setLayoutParams(params);
				radioButton.setText(listOfSingleCategoryFreeGifts.get(i)
						.getDisplayName());
				radioButton.setId(radiobtnId);
				Drawable drawable = getResources().getDrawable(
						R.drawable.ulta_logo_icon);
				drawable.setBounds(0, 0, 100, 100);
				radioButton.setCompoundDrawables(drawable, null, null, null);

				if (finalListOfSingleCategoryFreeGifts.get(i).isAddedFreeGift()) {
					isAddedCount++;
					radioButton.setChecked(true);
					mCategoryRadioGroup.setEnabled(false);
					for (int j = 0; j < mCategoryRadioGroup.getChildCount(); j++) {
						mCategoryRadioGroup.getChildAt(j).setEnabled(false);
					}
					categoryHeadingTextView
							.setText("Category "
									+ (i + 1)
									+ ":"
									+ "Please remove free gift item from bag to change gift selection");

					// mAlreadySelectedTextView.setVisibility(View.VISIBLE);
					// productAndSkuIDMap.put(radioGroupId,
					// mProductIdAndSkuId.get(i));
					mChooseFreeGiftDoneBtn
							.setBackgroundResource(R.drawable.rectangular_boarder);
					mChooseFreeGiftDoneBtn.setTextColor(getResources()
							.getColor(R.color.shippingtype_linear_bg));
					mChooseFreeGiftDoneBtn.setEnabled(false);
				}

				radioButton.setTextColor(Color
						.parseColor(getString(color.black)));
				radiobtnId++;
				mCategoryRadioGroup.setId(radioGroupId);

				View view = new View(ChooseFreeGiftActivity.this);
				LayoutParams viewParams = new LayoutParams(
						LayoutParams.MATCH_PARENT, 1);
				view.setLayoutParams(viewParams);
				view.setPadding(0, 5, 0, 5);
				view.setBackgroundResource(R.drawable.divider);
				mCategoryRadioGroup.addView(radioButton);
				mCategoryRadioGroup.addView(view);
			}
			mFreeGiftsLayout.addView(mCategoryRadioGroup);

			mCategoryRadioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {

							if (isAddedCount == mPromotionSize) {
								mChooseFreeGiftDoneBtn.setEnabled(false);
							}
							else {

								mChooseFreeGiftDoneBtn.setEnabled(true);
								mChooseFreeGiftDoneBtn
										.setBackgroundResource(R.drawable.button_rectangular_border);
								mChooseFreeGiftDoneBtn.setTextColor(getResources()
										.getColor(R.color.button_textColor));
								int radioGroupId = group.getId();
								String productIdAndSkuId = mProductIdAndSkuId
										.get(checkedId);
								productAndSkuIDMap.put(radioGroupId,
										productIdAndSkuId);
							}

							// mProductId.add(finalListOfSingleCategoryFreeGifts.get(checkedId).getId());
						}
					});

			if (isAddedCount == mPromotionSize) {
				mChooseFreeGiftDoneBtn.setEnabled(false);
			} else {
				mChooseFreeGiftDoneBtn.setEnabled(true);
				mChooseFreeGiftDoneBtn
						.setBackgroundResource(R.drawable.button_rectangular_border);
				mChooseFreeGiftDoneBtn.setTextColor(getResources().getColor(
						R.color.button_textColor));
			}
		} catch (Exception e) {

		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		private RadioButton radioButton;

		public DownloadImageTask(RadioButton radioButton) {
			this.radioButton = radioButton;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				mPDialog.dismiss();
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			Drawable drawable = getResources().getDrawable(
					R.drawable.custom_btn_radio);
			drawable.setBounds(0, 0, 72, 72);
			Drawable freeGiftImage = new BitmapDrawable(getResources(), result);
			freeGiftImage.setBounds(0, 0, 200, 100);
			radioButton.setCompoundDrawables(freeGiftImage, null, drawable,
					null);
			if (mCountToCallInvokeFreeGift == mPromotionSize) {
				try {
					mPDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void fnInvokeAddToCart(String productId, String skuId) {
		InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.ADDITEMTOORDER_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(fnPopulateAddToCartHandlerParameters(
				productId, skuId));
		invokerParams.setUltaBeanClazz(AddToCartBean.class);
		AddToCartHandler addToCartHandler = new AddToCartHandler();
		invokerParams.setUltaHandler(addToCartHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			mPDialog.dismiss();
			Logger.Log("<FreeGiftActivity><fnInvokeAddToCart()><UltaException>>"
					+ ultaException);
		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @param productId
	 *            the product id
	 * @param skuId
	 *            the sku id
	 * @return the map
	 */
	private Map<String, String> fnPopulateAddToCartHandlerParameters(
			String productId, String skuId) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("productId", productId);// "xlsImpprod3250327"
		urlParams.put("skuId", skuId);// "2208227"
		urlParams.put("quantity", "1");
		urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");

		return urlParams;
	}

	/*
	 * Handler to handle the response from the web service
	 */
	/**
	 * The Class AddToCartHandler.
	 */
	public class AddToCartHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<AddToCartHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				mPDialog.dismiss();
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							ChooseFreeGiftActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<AddToCartHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));

				if (mAddToCartToBeCalled == mCountToNavigate) {
					mPDialog.dismiss();
					Intent viewBasketIntent = new Intent(
							ChooseFreeGiftActivity.this,
							ViewItemsInBasketActivity.class);
					viewBasketIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					startActivity(viewBasketIntent);
					finish();
				} else {
					if (iterator.hasNext()) {
						Map.Entry pairs = (Map.Entry) iterator.next();
						String productIdAndSkuId = pairs.getValue().toString();
						String productIdAndSkuIdArray[] = productIdAndSkuId
								.split(",");
						mCountToNavigate++;
						fnInvokeAddToCart(productIdAndSkuIdArray[0],
								productIdAndSkuIdArray[1]);
						iterator.remove();
					}
				}

			}
		}
	}

}
