/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.rewards;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.CampaignOfferListBean;
import com.ulta.core.bean.RewardsBean;
import com.ulta.core.bean.UltaBean;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class MyBonusOffersActivity.
 */
public class MyBonusOffersActivity extends UltaBaseActivity {
	private LinearLayout emptyBonusLayout;
	private LinearLayout mainLayout;
	private RewardsBean rewardsBean;// bean response
	List<CampaignOfferListBean> campaignOfferList;// get offers list
	private ProgressDialog activateOfferDialog;// progress dialog for activating
												// offer
	ListView lvBonus; // list view for offers

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bonus_offers);
		setTitle("My Bonus Offers");
		emptyBonusLayout = (LinearLayout) findViewById(R.id.emptyBonusOfferLayout);
		mainLayout = (LinearLayout) findViewById(R.id.bonusMainLayout);
		Bundle bundleFromRewardsActivity = getIntent().getExtras();

		mainLayout.setVisibility(View.GONE);
		emptyBonusLayout.setVisibility(View.GONE);

		// get the campaign offer list from bean and perform null check

		if (null != bundleFromRewardsActivity) {
			rewardsBean = (RewardsBean) bundleFromRewardsActivity.get("offers");
			if (null != rewardsBean) {
				campaignOfferList = rewardsBean.getCampaignOfferList();
				if (null != campaignOfferList && campaignOfferList.size() != 0) {
					Logger.Log("MyBonusOffersActivity<<Offers not null");
					mainLayout.setVisibility(View.VISIBLE);
					setView();
				} else {
					emptyBonusLayout.setVisibility(View.VISIBLE);
				}
			}
		}
		activateOfferDialog = new ProgressDialog(MyBonusOffersActivity.this);
		setProgressDialogLoadingColor(activateOfferDialog);
		activateOfferDialog.setMessage("Activating..");
		activateOfferDialog.setCancelable(false);

	}

	/**
	 * Sets the view.
	 */
	private void setView() {
		lvBonus = (ListView) findViewById(R.id.lvViewBonusOffers);
		lvBonus.setAdapter(new CustomListAdapter(this));
	}

	/**
	 * The Class CustomListAdapter.
	 */
	private class CustomListAdapter extends BaseAdapter {

		/** The context. */
		private Context context;
		private Button isActive;

		/**
		 * Instantiates a new product list adapter.
		 * 
		 * @param context
		 *            the context
		 */
		public CustomListAdapter(Context context) {
			this.context = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return campaignOfferList.size();

		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */

		// loyalty bonus offer and activation
		// Added one button to activate the offer
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view;
			view = inflater.inflate(R.layout.my_bonus_list, null);
			LinearLayout dateHeadingLayout = (LinearLayout) view
					.findViewById(R.id.linearLayout1);
			TextView tvOfferHeading = (TextView) view
					.findViewById(R.id.tvOfferHeading);
			tvOfferHeading.setTypeface(setHelveticaRegulartTypeFace());
			TextView tvOfferDate = (TextView) view
					.findViewById(R.id.tvOfferDate);
			tvOfferDate.setTypeface(setHelveticaRegulartTypeFace());
			TextView tvOfferDescription = (TextView) view
					.findViewById(R.id.tvOfferDescription);
			tvOfferDescription.setTypeface(setHelveticaRegulartTypeFace());
			isActive = (Button) view.findViewById(R.id.isActive);
			isActive.setTypeface(setHelveticaRegulartTypeFace());
			if (campaignOfferList.get(position).getIsActive()) {
				isActive.setText("OFFER ACTIVATED");
				isActive.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.button_opt));
				isActive.setTextColor(getResources().getColor(
						R.color.activated_offer));
				isActive.setEnabled(false);
			}

			else {
				isActive.setTextColor(getResources().getColor(R.color.orange));
				isActive.setText("ACTIVATE NOW");
				isActive.setEnabled(true);
			}

			isActive.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					activateOffer(campaignOfferList.get(position), position);
					activateOfferDialog.show();

				}
			});

			// Remove the separators from description

			String[] split = campaignOfferList.get(position).getDescription()
					.split("<>");
			if (split.length == 4) {
				tvOfferDescription.setText(split[3]);
				tvOfferDate.setText(split[2]);
				tvOfferHeading.setText(split[1]);
			} else if (split.length != 4 || null == split) {
				tvOfferDescription.setText(campaignOfferList.get(position)
						.getDescription());
				dateHeadingLayout.setVisibility(View.GONE);
				tvOfferDate.setVisibility(View.GONE);
				tvOfferHeading.setVisibility(View.GONE);
			}

			return view;
		}

	}

	/**
	 * 
	 * To activate the loyalty bonus offer
	 * 
	 * @param campaignOfferListBean
	 *            Activate offer in the param campaignOfferListBean
	 * 
	 */
	public void activateOffer(CampaignOfferListBean campaignOfferListBean,
			int position) {
		InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.ACTIVATE_OFFER);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(fnActivateOfferParameters(campaignOfferListBean));
		invokerParams.setUltaBeanClazz(UltaBean.class);
		ActivateOfferHandler activateOfferHandler = new ActivateOfferHandler(
				position);
		invokerParams.setUltaHandler(activateOfferHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	/**
	 * 
	 * @param campaignOfferListBean
	 *            contains the details of offers to be activated
	 * @return the map with the parameters to pass to the web service
	 */
	private Map<String, String> fnActivateOfferParameters(
			CampaignOfferListBean campaignOfferListBean) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("activateOfferId",
				campaignOfferListBean.getCampaignOfferId());
		urlParams.put("campaignOfferMasterId",
				campaignOfferListBean.getCampaignOffermasterId());
		return urlParams;
	}

	/**
	 * 
	 * Handler class for activating offer web service
	 */
	public class ActivateOfferHandler extends UltaHandler {

		private int mPosition;// position of activated offer

		public ActivateOfferHandler(int mPosition) {

			this.mPosition = mPosition;
		}

		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {

			// If response is false then some error occured in activation

			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							MyBonusOffersActivity.this);
					activateOfferDialog.dismiss();
					setError(MyBonusOffersActivity.this, getErrorMessage());
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			}

			// Offer is Activated. Invoke rewards web service again and reload
			// the page

			else {
				if (null != activateOfferDialog
						&& activateOfferDialog.isShowing()) {
					activateOfferDialog.dismiss();
				}

				View v = lvBonus.getChildAt(mPosition
						- lvBonus.getFirstVisiblePosition());

				if (v == null) {
					return;
				}

				if (null != campaignOfferList.get(mPosition)
						.getCampaignOfferId()) {
					trackEvarsUsingActionName(MyBonusOffersActivity.this,
							WebserviceConstants.CHECKOUT_STEP_6_EVENT_ACTION,
							WebserviceConstants.LOYALTY_OFFER_ACTIVATION_KEY,
							campaignOfferList.get(mPosition)
									.getCampaignOfferId());
				}

				campaignOfferList.get(mPosition).setIsActive(true);
				Button isActive = (Button) v.findViewById(R.id.isActive);
				isActive.setText("OFFER ACTIVATED");
				isActive.setTypeface(setHelveticaRegulartTypeFace());
				isActive.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.button_opt));
				isActive.setTextColor(getResources().getColor(
						R.color.activated_offer));
				isActive.setEnabled(false);

			}

		}
	}

}
