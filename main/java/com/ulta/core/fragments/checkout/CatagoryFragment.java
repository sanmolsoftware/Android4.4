/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.fragments.checkout;

import android.app.Activity;
import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;



/**
 * The Class CatagoryFragment.
 *
 * @param <OnTitlesSelectedListener> the generic type
 */
public class CatagoryFragment<OnTitlesSelectedListener> extends ListFragment {

	/** The titles selected listener. */
	private OnTitlesSelectedListener titlesSelectedListener;

	/** The values. */
	String[] values;
	
	/** The selected tab flag. */
	int selectedTabFlag = 0;
	
	/** The adapter. */
	CustomListAdapter adapter;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, container, false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		values = new String[] { "Shipping Address", "Gift Option", "Payment","Rewards and EmailSignUp", "Submit Order" };
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(getActivity(),R.layout.item, values);

		// setListAdapter(adapter);
		adapter = new CustomListAdapter();
		setListAdapter(adapter);

	}

	/**
	 * The listener interface for receiving onTitlesSelected events.
	 * The class that is interested in processing a onTitlesSelected
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnTitlesSelectedListener<code> method. When
	 * the onTitlesSelected event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnTitlesSelectedEvent
	 */
	public interface OnTitlesSelectedListener {
		
		/**
		 * On titles selected.
		 *
		 * @param position the position
		 */
		public void onTitlesSelected(int position);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			titlesSelectedListener = (OnTitlesSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTitlesSelectedListener");
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		@SuppressWarnings("unused")
		String item = (String) getListAdapter().getItem(position);
		Logger.Log("Position" + position);

		if (position == 0) {

//			titlesSelectedListener.onTitlesSelected(position);

			selectedTabFlag = position;
			adapter.notifyDataSetChanged();
		}
		if (position == 1
				&& UltaDataCache.getDataCacheInstance().getShippingMethod() != null
				&& !(UltaDataCache.getDataCacheInstance().getShippingMethod()
						.isEmpty())) {
			selectedTabFlag = position;
			adapter.notifyDataSetChanged();
//			titlesSelectedListener.onTitlesSelected(position);
		} else if (position == 2
				&& UltaDataCache.getDataCacheInstance().getShippingAddress() != null
				&& !(UltaDataCache.getDataCacheInstance().getShippingAddress()
						.isEmpty())
						&& UltaDataCache.getDataCacheInstance().getGiftOption() != null
						&& !(UltaDataCache.getDataCacheInstance().getGiftOption()
								.isEmpty())) {
			selectedTabFlag = position;
			adapter.notifyDataSetChanged();
//			titlesSelectedListener.onTitlesSelected(position);
		} else if (position == 3
				&& UltaDataCache.getDataCacheInstance().getShippingAddress() != null
				&& !(UltaDataCache.getDataCacheInstance().getShippingAddress()
						.isEmpty())
						&& UltaDataCache.getDataCacheInstance().getGiftOption() != null
						&& !(UltaDataCache.getDataCacheInstance().getGiftOption())
						.isEmpty()
						&& UltaDataCache.getDataCacheInstance().getPaymentMethod() != null
						&& !(UltaDataCache.getDataCacheInstance().getPaymentMethod()
								.isEmpty())) {
			selectedTabFlag = position;
			adapter.notifyDataSetChanged();
//			titlesSelectedListener.onTitlesSelected(position);
		}
		else if(position == 4 
				&& UltaDataCache.getDataCacheInstance().getShippingAddress() != null
				&& !(UltaDataCache.getDataCacheInstance().getShippingAddress()
						.isEmpty())
						&& UltaDataCache.getDataCacheInstance().getGiftOption() != null
						&& !(UltaDataCache.getDataCacheInstance().getGiftOption())
						.isEmpty()
						&& UltaDataCache.getDataCacheInstance().getPaymentMethod() != null
						&& !(UltaDataCache.getDataCacheInstance().getPaymentMethod()
								.isEmpty())){
			selectedTabFlag = position;
			adapter.notifyDataSetChanged();
//			titlesSelectedListener.onTitlesSelected(position);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#setSelection(int)
	 */
	@Override
	public void setSelection(int position) {
		
		selectedTabFlag = position;
		if(null!=adapter){
			adapter.notifyDataSetChanged();
		}
//        TODO alternative
//		titlesSelectedListener.onTitlesSelected(position);
		super.setSelection(position);
	}

	/**
	 * The Class CustomListAdapter.
	 */
	public class CustomListAdapter extends BaseAdapter {

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
				return values.length;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
				return null;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
				return 0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflator = (LayoutInflater) getActivity()
			.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView=inflator.inflate(R.layout.item, null);
			//TextView txt = (TextView) inflator.inflate(R.layout.item, null);
			TextView txt1=(TextView)convertView.findViewById(R.id.textView1);
			TextView txt2=(TextView)convertView.findViewById(R.id.textView2);
			TextView txt3=(TextView)convertView.findViewById(R.id.textView3);
			if (selectedTabFlag == position) {
				convertView.setBackgroundColor(Color
						.parseColor(getString(R.color.jaffa)));
				txt1.setTextColor(Color.parseColor(getString(R.color.white)));
				txt2.setTextColor(Color.parseColor(getString(R.color.white)));
				txt3.setTextColor(Color.parseColor(getString(R.color.white)));
			} else {
				convertView.setBackgroundColor(Color
						.parseColor(getString(R.color.pampas)));
				txt1.setTextColor(Color.parseColor(getString(R.color.riverBed)));
				txt2.setTextColor(Color.parseColor(getString(R.color.riverBed)));
				txt3.setTextColor(Color.parseColor(getString(R.color.riverBed)));
			}
			txt1.setText(values[position]);
			return convertView;
		}

	}

	/**
	 * Gets the custom list adapter.
	 *
	 * @return the custom list adapter
	 */
	public CustomListAdapter getCustomListAdapter() {
		return adapter;
	}

	/**
	 * Sets the adapter.
	 *
	 * @param adapter the new adapter
	 */
	public void setAdapter(CustomListAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Gets the selected tab flag.
	 *
	 * @return the selected tab flag
	 */
	public int getSelectedTabFlag() {
		return selectedTabFlag;
	}

	/**
	 * Sets the selected tab flag.
	 *
	 * @param selectedTabFlag the new selected tab flag
	 */
	public void setSelectedTabFlag(int selectedTabFlag) {
		this.selectedTabFlag = selectedTabFlag;
	}

}
