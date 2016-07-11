/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.widgets.map;

/**
 * Android imports
 */

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.maps.MapController;
import com.ulta.R;
import com.ulta.core.activity.stores.StoreDetailsActivity;
import com.ulta.core.bean.store.StoreDetailBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.List;
/**
 * Imports from third parties (com, junit, net, org) 
 */
/**
 * java or javax imports
 */

/**
 * The Class CustomMap.
 */
public class CustomMap extends FrameLayout {

	/** The context. */
	private Context context;

	/** The layout. */
	private FrameLayout layout;


	/** The drawable. */
	Drawable drawable;


	/** The map controller. */
//	MapController mapController;



	// data
	/** The locations. */
	private List<Location> locations;

	private List<StoreDetailBean> stores;

	private GoogleMap mMap;// Google map variable
	Activity activity;// to get context of Layout
	Location location;// details of particular store
	private TextView mAddress1;//Address 1
	private TextView mTitle;// title
	private TextView storeStatus;//store status text view

	/**
	 * Instantiates a new custom map.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
	public CustomMap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	/**
	 * Instantiates a new custom map.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public CustomMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * Instantiates a new custom map.
	 * 
	 * @param context
	 *            the context
	 */
	public CustomMap(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * initializing the map.
	 */
	private void init() {

		Activity activity = (Activity) context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		layout = (FrameLayout) inflater.inflate(R.layout.map, null);
		mMap = ((MapFragment) activity.getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if(status==ConnectionResult.SUCCESS)
        {
			mMap.getUiSettings().setZoomControlsEnabled(false);
        }
          
        
		addView(layout);
	}
	/**
	 * This method is used to set markers in map on store locations.
	 * Method contents changed to upgrade map to V2
	 * fetch location of stores and add markers on map
	 */
	public void showMarkers() {


		
		activity = (Activity) context;
		mMap.clear();
		for (int i = 0; i <= locations.size() - 1; i++) {

			int mLatitude = (int) (locations.get(i).getLatitude() * 1E6);
			int mLongitude = (int) (locations.get(i).getLongitude() * 1E6);
			LatLng mStore = new LatLng(getPointedLatLog(mLatitude),
					getPointedLatLog(mLongitude));
			Marker mMarker = mMap
					.addMarker(new MarkerOptions().position(mStore));
		/*	MarkerOptions mopt=new MarkerOptions();
			mopt.position(mStore);
			mMap.addMarker(mopt);*/
			mMarker.setSnippet("" + i);

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mStore, 11));
		
			
			  //set info window to show store details on marker click
			 
			mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker arg0) {
					
					View v = activity.getLayoutInflater().inflate(
							R.layout.map_balloon, null);
					int mStoreLocation = Integer.parseInt(arg0.getSnippet());
					location = locations.get(Integer.parseInt(arg0.getSnippet()));

					mTitle = (TextView) v.findViewById(R.id.balloon_item_title);
					mAddress1 = (TextView) v
							.findViewById(R.id.balloon_item_address1);
//					mCity = (TextView) v.findViewById(R.id.balloon_item_city);
//					mState = (TextView) v.findViewById(R.id.balloon_item_state);
//					mZip = (TextView) v.findViewById(R.id.balloon_item_zip);
					//mStoreStatusLayout = (LinearLayout) v.findViewById(R.id.imageView1);
					storeStatus = (TextView) v.findViewById(R.id.storeStatus);
					if (location.isStoreOpen()) {
						storeStatus.setBackgroundResource(R.color.orange);
						storeStatus.setText(getResources().getString(R.string.store_status_open));
						mTitle.setText(stores.get(mStoreLocation)
								.getDisplayName() );
					} else {
						storeStatus.setText(getResources().getString(R.string.store_status_closed));
						storeStatus.setBackgroundResource(R.color.greyBackground);
						mTitle.setText(stores.get(mStoreLocation)
								.getDisplayName());
					}
					//mAddress1.setText(stores.get(mStoreLocation).getAddress1());
					mAddress1.setText(locations.get(mStoreLocation).getAddress()+"\n"+locations.get(mStoreLocation).getCity()+", "+locations.get(mStoreLocation).getState()+"  "+locations.get(mStoreLocation).getZipcode()+"\n"+locations.get(mStoreLocation).getPhone());
//					if (null != stores.get(mStoreLocation).getCity()) {
//						mCity.setText(stores.get(mStoreLocation).getCity());
//					} else {
//						mCity.setVisibility(View.GONE);
//					}
//					if (null != stores.get(mStoreLocation).getState()) {
//						mState.setText(","
//								+ stores.get(mStoreLocation).getState());
//					} else {
//						mState.setVisibility(View.GONE);
//					}
//					if (null != stores.get(mStoreLocation).getZipCode()) {
//						mZip.setText(" "
//								+ stores.get(mStoreLocation).getZipCode());
//					} else {
//						mZip.setVisibility(View.GONE);
//					}

					// Returning the view containing InfoWindow contents
					return v;
					
				}

				@Override
				public View getInfoContents(Marker arg0) {
					return null;

				}
			});

			
			 //Info window click listener--navigate to store details page on
			// click of info window
			 
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {

					Intent intentForStoreDetails = new Intent(context,
							StoreDetailsActivity.class);
					UltaDataCache.getDataCacheInstance().setStoreBeingViewed(
							Integer.parseInt(arg0.getSnippet()));
					intentForStoreDetails.putExtra("position",
							Integer.parseInt(arg0.getSnippet()));
					activity.startActivityForResult(intentForStoreDetails, 5000);
				}
			});
		}

		
	}

	/**
	 * @param position
	 * Used to set markers in map on store locations
	 *  and show info window of particular position marker.
	 * Method contents changed to upgrade map to V2
	 * set markers again on all location and show info window of
		 * particular location and animate to the location
	 */
	public void showMarkers(int position) {

		mMap.clear();
		for (int i = 0; i <= locations.size() - 1; i++) {

			int mLatitude = (int) (locations.get(i).getLatitude() * 1E6);
			int mLongitude = (int) (locations.get(i).getLongitude() * 1E6);
			LatLng mStore = new LatLng(getPointedLatLog(mLatitude),
					getPointedLatLog(mLongitude));
			Marker mMarker = mMap
					.addMarker(new MarkerOptions().position(mStore));
			mMarker.setSnippet("" + i);
			mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker arg0) {
					View v = activity.getLayoutInflater().inflate(
							R.layout.map_balloon, null);
					int mStoreLocation = Integer.parseInt(arg0.getSnippet());
					location = locations.get(Integer.parseInt(arg0.getSnippet()));


					mTitle = (TextView) v.findViewById(R.id.balloon_item_title);
					mAddress1 = (TextView) v
							.findViewById(R.id.balloon_item_address1);
//					mCity = (TextView) v.findViewById(R.id.balloon_item_city);
//					mState = (TextView) v.findViewById(R.id.balloon_item_state);
//					mZip = (TextView) v.findViewById(R.id.balloon_item_zip);

					storeStatus = (TextView) v.findViewById(R.id.storeStatus);
					if (location.isStoreOpen()) {
						storeStatus.setBackgroundResource(R.color.orange);
						storeStatus.setText(getResources().getString(R.string.store_status_open));
						mTitle.setText(stores.get(mStoreLocation)
								.getDisplayName() );
					} else {
						storeStatus.setText(getResources().getString(R.string.store_status_closed));
						storeStatus.setBackgroundResource(R.color.greyBackground);
						mTitle.setText(stores.get(mStoreLocation)
								.getDisplayName());
					}
					mAddress1.setText(locations.get(mStoreLocation).getAddress()+"\n"+locations.get(mStoreLocation).getCity()+", "+locations.get(mStoreLocation).getState()+"  "+locations.get(mStoreLocation).getZipcode()+"\n"+locations.get(mStoreLocation).getPhone());
					//mAddress1.setText(stores.get(mStoreLocation).getAddress1());

//					if (null != stores.get(mStoreLocation).getCity()) {
//						mCity.setText(stores.get(mStoreLocation).getCity());
//					} else {
//						mCity.setVisibility(View.GONE);
//					}
//					if (null != stores.get(mStoreLocation).getState()) {
//						mState.setText(","
//								+ stores.get(mStoreLocation).getState());
//					} else {
//						mState.setVisibility(View.GONE);
//					}
//					if (null != stores.get(mStoreLocation).getZipCode()) {
//						mZip.setText(" "
//								+ stores.get(mStoreLocation).getZipCode());
//					} else {
//						mZip.setVisibility(View.GONE);
//					}

					// Returning the view containing InfoWindow contents
					return v;

					
				}

				@Override
				public View getInfoContents(Marker arg0) {
					return null;
				}
			});
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					Intent intentForStoreDetails = new Intent(context,
							StoreDetailsActivity.class);
					UltaDataCache.getDataCacheInstance().setStoreBeingViewed(
							Integer.parseInt(arg0.getSnippet()));
					intentForStoreDetails.putExtra("position",
							Integer.parseInt(arg0.getSnippet()));
					activity.startActivityForResult(intentForStoreDetails, 5000);
				}
			});
			if (i == position) {
				mMarker.showInfoWindow();
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mStore, 11));
			}

		}

	}


	/**
	 *
	 * @param locationPosition- the location position
	 *  animate to particular location
	 *  Method contents changed to upgrade map to V2
	 */
	public void panTo(int locationPosition) {

		int latitude = (int) (locations.get(locationPosition).getLatitude() * 1E6);
		int longitude = (int) (locations.get(locationPosition).getLongitude() * 1E6);

		LatLng store = new LatLng(getPointedLatLog(latitude),
				getPointedLatLog(longitude));
		CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(store);
		mMap.moveCamera(updatePosition);
		mMap.animateCamera(updatePosition);

		mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

	}


	private void setLocations() {
		locations = new ArrayList<Location>();
		for (int i = 0; i < stores.size(); i++) {
			String name = stores.get(i).getDisplayName();
			Double latitude = stores.get(i).getLatitude();
			Double longitude = stores.get(i).getLongitude();
			boolean isStoreOpen = stores.get(i).isStoreOpen();
			locations.add(new Location(name, stores.get(i).getAddress1(),
					latitude, longitude, isStoreOpen,stores.get(i).getCity(),stores.get(i).getState(),stores.get(i).getZipCode(),stores.get(i).getContactNumber()));
		}
		Logger.Log("NO of stores being set::::" + stores.size());
		UltaDataCache.getDataCacheInstance().setStores(stores);
	}

	public List<StoreDetailBean> getStores() {
		return stores;
	}

	public void setStores(List<StoreDetailBean> stores) {
		this.stores = stores;
		setLocations();
	}


	/**
	 * getter and setter for google map
	 */
	public GoogleMap getMap() {
		return mMap;
	}

	public void setMap(GoogleMap map) {
		this.mMap = map;
	}

	/**
	 * 
	 * @param value
	 *            -latitude or longitude
	 * @return pointed latitude or longitude
	 * 
	 *         New map api v2 accept latitude and longitude with pointed values
	 */
	public double getPointedLatLog(double value) {
		double pointedVal = value / WebserviceConstants.pointPosition;
		return pointedVal;
	}
	
	

}
