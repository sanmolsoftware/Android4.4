/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.widgets.map;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.product.StoreRTIActivity;
import com.ulta.core.activity.stores.StoreDetailsActivity;
import com.ulta.core.activity.stores.StoresActivity;
import com.ulta.core.bean.product.RTIResponseBean;
import com.ulta.core.bean.store.StoreDetailBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class CustomStoreList.
 */
public class CustomStoreList extends LinearLayout {

	/** The context. */
	Context context;
	Activity activity;// to get context of Layout
	/** The layout. */
	private LinearLayout layout;
	private Typeface helveticaRegularTypeface;
	/** The list. */
	private ListView list;

	/** The store list adapter. */
	private StoreListAdapter storeListAdapter;

	/** The on store selected listener. */
	private OnStoreSelectedListener onStoreSelectedListener;

	// data
	/** The locations. */
	private List<Location> locations;

	private List<StoreDetailBean> stores;
	private boolean isRTI = false;
	Button mCheckAvailability;
	private String skuId;
	/** The hash map. */
	HashMap<String, String> hashMap = new HashMap<String, String>();
	ProgressBar store_status_bar;

	/**
	 * Instantiates a new custom store list.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
	public CustomStoreList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	/**
	 * Instantiates a new custom store list.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public CustomStoreList(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * Instantiates a new custom store list.
	 * 
	 * @param context
	 *            the context
	 */
	public CustomStoreList(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		// setLocations();
		activity = (Activity) context;
		storeListAdapter = new StoreListAdapter();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.store_list, null);
		list = (ListView) layout.findViewById(R.id.lists);
		addView(layout);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intentForStoreDetails = new Intent(context,
						StoreDetailsActivity.class);
				UltaDataCache.getDataCacheInstance().setStoreBeingViewed(
						position);
				intentForStoreDetails.putExtra("position", position);
				activity.startActivityForResult(intentForStoreDetails, 5000);

			}
		});
	}

	/**
	 * Show list.
	 */
	public void showList() {

		list.setAdapter(storeListAdapter);
		storeListAdapter.notifyDataSetChanged();
	}

	public void setRTI(String skuId) {
		isRTI = true;
		this.skuId = skuId;
	}

	/**
	 * The Class StoreListAdapter.
	 */
	class StoreListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return locations.size();
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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = (LinearLayout) inflater.inflate(R.layout.store_item,
					null);
			TextView txtName = (TextView) convertView
					.findViewById(R.id.store_item_name);
			TextView txtAddress = (TextView) convertView
					.findViewById(R.id.store_item_address);
			mCheckAvailability = (Button) convertView
					.findViewById(R.id.checkAvailability);
			txtName.setTypeface(setHelveticaRegulartTypeFace());
			final int viewMapClickedPosition = position;
			if (isRTI) {
				if (hashMap.get(""
						+ stores.get(viewMapClickedPosition).getStoreId()) == null) {
					mCheckAvailability.setText(getResources().getString(
							R.string.check_availability));
				} else if (hashMap.get(
						"" + stores.get(viewMapClickedPosition).getStoreId())
						.equals("OUT OF STOCK")) {
					mCheckAvailability.setText("OUT OF STOCK");
					if (Build.VERSION.SDK_INT > 16) {
						mCheckAvailability.setBackground(null);
					} else {
						mCheckAvailability.setBackgroundDrawable(null);
					}
					mCheckAvailability.setTextColor(getResources().getColor(
							R.color.greyBackground));
				} else if (hashMap.get(
						"" + stores.get(viewMapClickedPosition).getStoreId())
						.equals("IN STOCK")) {
					mCheckAvailability.setText("IN STOCK");
					if (Build.VERSION.SDK_INT > 16) {
						mCheckAvailability.setBackground(null);
					} else {
						mCheckAvailability.setBackgroundDrawable(null);
					}
				}
				mCheckAvailability.setVisibility(View.VISIBLE);
				mCheckAvailability.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						v.setVisibility(View.GONE);
						invokeRTIStatus(stores.get(viewMapClickedPosition)
								.getStoreId(), viewMapClickedPosition);
					}
				});
			}
			TextView mViewMap = (TextView) convertView
					.findViewById(R.id.viewMap);
			mViewMap.setTypeface(setHelveticaRegulartTypeFace());
			mViewMap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isRTI) {
						StoreRTIActivity store = (StoreRTIActivity) context;
						store.showMap(viewMapClickedPosition);
					} else {
						StoresActivity store = (StoresActivity) context;
						store.showMap(viewMapClickedPosition);
					}

				}
			});

			// To display store open/closed status
			// UI redesign 3.0

			TextView store_status = (TextView) convertView
					.findViewById(R.id.store_status);
			if (locations.get(position).isStoreOpen()) {
				txtName.setText(locations.get(position).getName());
				store_status.setText(getResources().getString(
						R.string.store_status_open));
				store_status.setBackgroundResource(R.color.orange);
			} else {
				txtName.setText(locations.get(position).getName());
				store_status.setText(getResources().getString(
						R.string.store_status_closed));
				store_status.setBackgroundResource(R.color.greyBackground);
			}
			txtAddress.setText(locations.get(position).getAddress() + "\n"
					+ locations.get(position).getCity() + ", "
					+ locations.get(position).getState() + "  "
					+ locations.get(position).getZipcode() + "\n"
					+ locations.get(position).getPhone());
			txtAddress.setTypeface(setHelveticaRegulartTypeFace());
			mCheckAvailability.setPadding(25, 25, 25, 25);
			return convertView;
		}

	}

	/**
	 * The listener interface for receiving onStoreSelected events. The class
	 * that is interested in processing a onStoreSelected event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addOnStoreSelectedListener<code> method. When
	 * the onStoreSelected event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnStoreSelectedEvent
	 */
	public interface OnStoreSelectedListener {

		/**
		 * On store selected.
		 * 
		 * @param position
		 *            the position
		 */
		public abstract void onStoreSelected(int position);
	}

	/**
	 * Gets the on store selected listener.
	 * 
	 * @return the on store selected listener
	 */
	public OnStoreSelectedListener getOnStoreSelectedListener() {
		return onStoreSelectedListener;
	}

	/**
	 * Sets the on store selected listener.
	 * 
	 * @param onStoreSelectedListener
	 *            the new on store selected listener
	 */
	public void setOnStoreSelectedListener(
			OnStoreSelectedListener onStoreSelectedListener) {
		this.onStoreSelectedListener = onStoreSelectedListener;
	}

	private void setLocations() {
		locations = new ArrayList<Location>();
		for (int i = 0; i < stores.size(); i++) {
			String name = stores.get(i).getDisplayName();
			Double latitude = stores.get(i).getLatitude();
			Double longitude = stores.get(i).getLongitude();
			boolean isStoreOpen = stores.get(i).isStoreOpen();

			locations.add(new Location(name, stores.get(i).getAddress1(),
					latitude, longitude, isStoreOpen, stores.get(i).getCity(),
					stores.get(i).getState(), stores.get(i).getZipCode(),
					stores.get(i).getContactNumber()));
		}
		UltaDataCache.getDataCacheInstance().setStores(stores);
	}

	public List<StoreDetailBean> getStores() {
		return stores;
	}

	public void setStores(List<StoreDetailBean> stores) {
		this.stores = stores;
		setLocations();
	}

	protected void invokeRTIStatus(String storeId, int position) {
		View v = list.getChildAt(position - list.getFirstVisiblePosition());
		store_status_bar = (ProgressBar) v.findViewById(R.id.store_status_bar);
		store_status_bar.setVisibility(View.VISIBLE);
		InvokerParams<RTIResponseBean> invokerParams = new InvokerParams<RTIResponseBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.DO_RTI_CHECK);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateParamsForRTICheck(storeId));
		invokerParams.setUltaBeanClazz(RTIResponseBean.class);
		RTIHandler storesHandler = new RTIHandler(storeId, position);
		invokerParams.setUltaHandler(storesHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateParamsForRTICheck(String storeId) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("skuId", skuId);
		urlParams.put("storeId", storeId);
		return urlParams;
	}

	/**
	 * The Class StoreHandler.
	 */
	public class RTIHandler extends UltaHandler {
		int id;
		private int mPosition;// position of activated offer

		public RTIHandler(String storeId, int mPosition) {
			id = Integer.parseInt(storeId);
			this.mPosition = mPosition;
		}

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			/*
			 * if (null != pd) { //pd.dismiss(); }
			 */
			if (null != getErrorMessage()) {
				try {
					// notifyUser(getErrorMessage(), context);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				View v = list.getChildAt(mPosition
						- list.getFirstVisiblePosition());

				if (v == null) {
					return;
				}
				RTIResponseBean rtiRespBean = (RTIResponseBean) getResponseBean();
				String stockStatus = rtiRespBean.getAtgResponse();
				hashMap.put("" + id, stockStatus);
				Button mCheckAvailability = (Button) v
						.findViewById(R.id.checkAvailability);
				ProgressBar store_status_bar = (ProgressBar) v
						.findViewById(R.id.store_status_bar);
				// Reported error on playstore : Fixed
				// ProgressBar progressbar=(ProgressBar)findViewById(id+1);
				// progressbar.setVisibility(View.GONE);
				store_status_bar.setVisibility(View.GONE);
				mCheckAvailability.setVisibility(View.VISIBLE);

				if (stockStatus.equals("OUT OF STOCK")) {
					mCheckAvailability.setText("OUT OF STOCK");
					if (Build.VERSION.SDK_INT > 16) {
						mCheckAvailability.setBackground(null);
					} else {
						mCheckAvailability.setBackgroundDrawable(null);
					}
					mCheckAvailability.setTextColor(getResources().getColor(
							R.color.shippingtype_linear_bg));
				} else if (stockStatus.equals("IN STOCK")) {
					mCheckAvailability.setText("IN STOCK");
					if (Build.VERSION.SDK_INT > 16) {
						mCheckAvailability.setBackground(null);
					} else {
						mCheckAvailability.setBackgroundDrawable(null);
					}
				}
				;
				mCheckAvailability.setPadding(25, 25, 25, 25);
				mCheckAvailability.setVisibility(View.VISIBLE);
			}
		}
	}

	public Typeface setHelveticaRegulartTypeFace() {

		try {
			helveticaRegularTypeface = Typeface.createFromAsset(
					context.getAssets(),
					WebserviceConstants.FONT_STYLE_FILENAME);
			return helveticaRegularTypeface;
		} catch (Exception e) {
			helveticaRegularTypeface = Typeface.SANS_SERIF;
			return helveticaRegularTypeface;
		}

	}

}
