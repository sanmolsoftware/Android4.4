/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.rewards;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;

/**
 * The Class AlertsActivity.
 */
public class AlertsActivity extends UltaBaseActivity
{
	private ArrayList<String> alertsList;
	private LinearLayout emptyAlertLayout;
	private LinearLayout mainLayout;

	/** The loading layout. */
/*	private LinearLayout loadingDialog;*/

	/*
	 * (non-Javadoc)
	 * @see com.ulta.core.activity.UltaActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_alerts);
		trackAppState(AlertsActivity.this, WebserviceConstants.REWARDS_ALERT);
		setTitle("My Alerts");
		emptyAlertLayout = (LinearLayout) findViewById(R.id.emptyAlertLayout);
		mainLayout = (LinearLayout) findViewById(R.id.alertMainLayout);
		alertsList = new ArrayList<String>();

		mainLayout.setVisibility(View.GONE);
		emptyAlertLayout.setVisibility(View.GONE);
		Bundle bundleFromRewardsActivity = getIntent().getExtras();
		if (null != bundleFromRewardsActivity) {
			alertsList = bundleFromRewardsActivity.getStringArrayList("alerts");
			if (null != alertsList && alertsList.size() != 0) {
				Logger.Log("AlertsActivity<<Alerts not null");
				mainLayout.setVisibility(View.VISIBLE);
				setView();
			}
			else {
				emptyAlertLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * Sets the view.
	 */
	private void setView() {
		ListView lvBonus = (ListView) findViewById(R.id.lvViewAlerts);
		lvBonus.setAdapter(new CustomListAdapter(this));
	}

	/**
	 * The Class CustomListAdapter.
	 */
	private class CustomListAdapter extends BaseAdapter
	{

		/** The context. */
		private Context context;

		/**
		 * Instantiates a new product list adapter.
		 * 
		 * @param context
		 *            the context
		 */
		public CustomListAdapter(Context context) {
			this.context = context;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
				return alertsList.size();
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
				return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
				return 0;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view;

			// if (convertView == null) {
			view = inflater.inflate(R.layout.my_alerts_list, null);

			TextView tvAlertDescription = (TextView) view
					.findViewById(R.id.tvAlertDescription);
			tvAlertDescription.setText(alertsList.get(position));
			return view;
		}

	}
}
