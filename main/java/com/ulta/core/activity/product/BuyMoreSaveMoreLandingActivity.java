package com.ulta.core.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.BuyMoreSaveMoreAtgResponseBean;
import com.ulta.core.bean.product.BuyMoreSaveMoreBean;
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

public class BuyMoreSaveMoreLandingActivity extends UltaBaseActivity {

	private ListView promotionsListView;
	private PromotionListAdapter mPromotionListAdapter;
	private List<BuyMoreSaveMoreAtgResponseBean> buyMoreSaveMoreAtgResponseBean;
	private FrameLayout mSpeicalOfferLoading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_more_save_more_promotions);
		setTitle("Buy More Save More");
		promotionsListView = (ListView) findViewById(R.id.promotionsListView);
		mSpeicalOfferLoading = (FrameLayout) findViewById(R.id.specialOfferLoading);

		invokePromotionsList();
	}

	/**
	 * Invoke Promotions.
	 * 
	 */
	private void invokePromotionsList() {

		InvokerParams<BuyMoreSaveMoreBean> invokerParams = new InvokerParams<BuyMoreSaveMoreBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.BUY_MORE_SAVE_MORE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateBuyMoreSaveMoreParameters());
		invokerParams.setUltaBeanClazz(BuyMoreSaveMoreBean.class);

		PromotionsHandler promotionsHandler = new PromotionsHandler();
		invokerParams.setUltaHandler(promotionsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			mSpeicalOfferLoading.setVisibility(View.GONE);
			Logger.Log("<BuyMoreSaveMoreActivity><buyMoreSaveMore><UltaException>>"
					+ ultaException);
		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 */
	private Map<String, String> populateBuyMoreSaveMoreParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("promotionType", "b");

		return urlParams;
	}

	/**
	 * The Class LoginHandler.
	 */
	public class PromotionsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<PromotionsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				mSpeicalOfferLoading.setVisibility(View.GONE);
				if (!getErrorMessage().startsWith("401")) {
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								BuyMoreSaveMoreLandingActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				mSpeicalOfferLoading.setVisibility(View.GONE);
				// Logger.Log("<Promotions><handleMessage><getResponseBean>>"
				// + (getResponseBean()));
				BuyMoreSaveMoreBean buyMoreSaveMoreBean = (BuyMoreSaveMoreBean) getResponseBean();
				buyMoreSaveMoreAtgResponseBean = buyMoreSaveMoreBean
						.getAtgResponse();
				Logger.Log("<PromotionsHandler><handleMessage><getResponseBean>>"
						+ buyMoreSaveMoreAtgResponseBean);

				mPromotionListAdapter = new PromotionListAdapter();
				promotionsListView.setAdapter(mPromotionListAdapter);

			}
		}
	}

	public class PromotionListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return buyMoreSaveMoreAtgResponseBean.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final int selectedPosition = position;

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.promotions_bmsm, null);
				convertView.setPadding(0, 20, 0, 0);
			}

			TextView promotionsDisplayName = (TextView) convertView
					.findViewById(R.id.promotionTextView);
			TextView promotionsDescriptionName = (TextView) convertView
					.findViewById(R.id.promotionDescriptionTextView);
			TextView promotionsExpiryDateName = (TextView) convertView
					.findViewById(R.id.promotionValidityTextView);
			LinearLayout promotionsLinearLayout = (LinearLayout) convertView
					.findViewById(R.id.promotionsLinearLayout);

			if (null != buyMoreSaveMoreAtgResponseBean.get(position)
					.getPromoOfferAdbug()
					&& null != buyMoreSaveMoreAtgResponseBean.get(position)
							.getPromoOfferLink()) {
				promotionsDisplayName.setText(buyMoreSaveMoreAtgResponseBean
						.get(position).getPromoOfferAdbug());
				promotionsDisplayName
						.setTypeface(setHelveticaRegulartTypeFace());
				promotionsDisplayName.setVisibility(View.VISIBLE);

				promotionsDescriptionName
						.setText(buyMoreSaveMoreAtgResponseBean.get(position)
								.getPromoOfferLink());
				promotionsDescriptionName.setVisibility(View.VISIBLE);
				promotionsDescriptionName
						.setTypeface(setHelveticaRegulartTypeFace());
				promotionsDisplayName.setTextColor(getResources().getColor(
						R.color.promotion_color));
			} else {
				if (null != buyMoreSaveMoreAtgResponseBean.get(position)
						.getPromoDescription()) {
					promotionsDescriptionName
							.setText(buyMoreSaveMoreAtgResponseBean.get(
									position).getPromoDescription());
					promotionsDescriptionName
							.setTypeface(setHelveticaRegulartTypeFace());
					promotionsDescriptionName.setVisibility(View.VISIBLE);
					promotionsDisplayName.setVisibility(View.GONE);
				}
			}
			if (null != buyMoreSaveMoreAtgResponseBean.get(position)
					.getPromoOfferDate()) {
				promotionsExpiryDateName.setText(buyMoreSaveMoreAtgResponseBean
						.get(position).getPromoOfferDate());
			}

			promotionsLinearLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent ultaProductListIntent = new Intent(
							BuyMoreSaveMoreLandingActivity.this,
							UltaProductListActivity.class);
					ultaProductListIntent.putExtra("promotionId",
							buyMoreSaveMoreAtgResponseBean
									.get(selectedPosition).getId());
					ultaProductListIntent.putExtra("page","buymoresavemore");
					ultaProductListIntent.setAction("fromPromotion");
					
					
					String altText = "";
					if (null != buyMoreSaveMoreAtgResponseBean
						.get(selectedPosition).getPromoOfferAdbug()) {
						altText = buyMoreSaveMoreAtgResponseBean
								.get(selectedPosition).getPromoOfferAdbug();
					} else {
						if (null != buyMoreSaveMoreAtgResponseBean.get(selectedPosition)
						.getPromoDescription()) {
							altText = buyMoreSaveMoreAtgResponseBean.get(selectedPosition)
									.getPromoDescription();
						}
					}
					
					
					ultaProductListIntent.putExtra("altText",altText);
					startActivity(ultaProductListIntent);

				}
			});

			return convertView;
		}

	}

}
