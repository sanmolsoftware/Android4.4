package com.ulta.core.activity.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.RTIResponseBean;
import com.ulta.core.bean.store.StoreBean;
import com.ulta.core.bean.store.StoreDetailBean;
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
import com.ulta.core.util.map.Coordinates;
import com.ulta.core.util.map.GeocoderTask;
import com.ulta.core.util.map.LocationFinder;
import com.ulta.core.util.map.LocationFinder.LocationResult;
import com.ulta.core.util.map.OnGeocodeFoundListener;
import com.ulta.core.util.map.SimpleGeocodeResult;
import com.ulta.core.widgets.UltaProgressDialog;
import com.ulta.core.widgets.flyin.OnPermissionCheck;
import com.ulta.core.widgets.map.CustomMap;
import com.ulta.core.widgets.map.CustomStoreList;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreRTIActivity extends UltaBaseActivity implements
        OnClickListener, OnGeocodeFoundListener, Handler.Callback, OnPermissionCheck {
    protected static final int GPS_REQUEST_CODE = 0;
    private ImageView btnFindStores;
    private EditText edtPostalCode;
    private String postalCodeEntered;
    private Double zipLat;
    private Double zipLong;
    LinearLayout noStoresLayout, loadingDialog, zipCodeLayout;
    private List<StoreDetailBean> stores;
    private String skuId;
    ProgressBar progressbar;
    // TextView tvStatus;
    protected ProgressDialog pd;
    View view;
    int loop;
    private static Location currentLocation;
    boolean result;
    /**
     * The list.
     */
    CustomStoreList list;
    private ImageView mMap_list_selectoriv;// map or list image icon view
    private boolean isListOnscreen = true;
    private boolean isNoStoresFound = true;
    /**
     * The map.
     */
    CustomMap map;
    private LinearLayout mMap_list_selector_layout;// map or list selector
    private LinearLayout filterLayout;
    private LinearLayout mNear_By_Stores;
    ImageView imgMyLocation;
    private boolean isLocationBased = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stores);
        setActivity(StoreRTIActivity.this);
        setTitle("Find In Store");
        initiateViews();

        if (getIntent().getExtras() != null
                && getIntent().getExtras().getString("skuId") != null) {
            skuId = getIntent().getExtras().getString("skuId");
        }

        setUpZipSearch();
        btnFindStores.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initiateZipBasedSearch();
            }
        });
        /*
		 * if(UltaDataCache.getDataCacheInstance().getUserLocation()==null) {
		 * zipCodeLayout.setVisibility(View.GONE);
		 * loadingDialog.setVisibility(View.VISIBLE);
		 */

		/*
		 * } else {
		 * currentLocation=UltaDataCache.getDataCacheInstance().getUserLocation
		 * (); }
		 */
        list.setRTI(skuId);
        filterLayout.setVisibility(View.GONE);
//		mNear_By_Stores.setVisibility(View.VISIBLE);
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        if (status == ConnectionResult.SUCCESS) {
            fetchCurrentLocation();

        } else {

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }

    }

    private void fetchCurrentLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            startFetchingLocation();
        } else {
            buildAlertMessageNoGps();
        }

    }

    public void showMap(int position) {
        selectMap();
        map.showMarkers(position);
    }

    private void startFetchingLocation() {
        checkForAppPermissions(StoreRTIActivity.this, WebserviceConstants.PERMISSION_ACCESS_FINE_LOCATION, WebserviceConstants.ACCESS_FINE_LOCATION_REQUEST_CODE, WebserviceConstants.ACCESS_FINE_LOCATION_DIALOG_TITLE, WebserviceConstants.ACCESS_FINE_LOCATION_DIALOG_MESSAGE);

//		pd = new UltaProgressDialog(this, "Fetching your Location..");
//		pd.show();
//
//		LocationFinder locationFinder = new LocationFinder();
//		locationFinder.setLocationHandler(new Handler(this));
//		result = locationFinder.getLocation(this, locationResult);
//		if (!result) {
//			if (null != pd) {
//				pd.dismiss();
//			}
//			buildAlertMessageNoGps();
//		}
    }

    /**
     * Handle the LocationManager Callback.
     */
    @Override
    public boolean handleMessage(Message msg) {
        if (null != pd) {
            pd.dismiss();
        }
        if (msg.what == 1) {
            Toast.makeText(this,
                    "Your current location is temporarily unavailable",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    LocationResult locationResult = new LocationResult() {
        @Override
        public void gotLocation(Location location) {
            if (null != location) {
                if (null != pd) {
                    try {
                        pd.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Logger.Log("#### Current lattitude: " + location.getLatitude()
                        + "#### Current Longitude: " + location.getLongitude());
                currentLocation = location;
                UltaDataCache.getDataCacheInstance().setUserLocation(
                        currentLocation);
                isLocationBased = true;
                fetchStoreList(location.getLatitude(), location.getLongitude());
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPS_REQUEST_CODE && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {
                startFetchingLocation();
            } else {
                buildAlertMessageNoGps();
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable MyLocation source in system setting.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        startActivityForResult(new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                GPS_REQUEST_CODE);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("stores", (Serializable) stores);
        super.onSaveInstanceState(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stores = (List<StoreDetailBean>) savedInstanceState
                .getSerializable("stores");
        if (stores != null && !stores.isEmpty()) {
            isNoStoresFound = false;
            list.setStores(stores);
            list.showList();
            map.setStores(stores);
            map.showMarkers();
            mMap_list_selectoriv.setImageResource(R.drawable.icon_map);
            selectList();
        }
    }

    private void initiateViews() {
        mNear_By_Stores = (LinearLayout) findViewById(R.id.near_By_Stores);
        btnFindStores = (ImageView) findViewById(R.id.storesFindStores);
        map = (CustomMap) findViewById(R.id.customMap);
        imgMyLocation = (ImageView) findViewById(R.id.storesMyLocation);
        imgMyLocation.setOnClickListener(this);
        mMap_list_selectoriv = (ImageView) findViewById(R.id.map_list_selectoriv);
        mMap_list_selector_layout = (LinearLayout) findViewById(R.id.map_list_selector_layout);
        mMap_list_selector_layout.setOnClickListener(this);
        edtPostalCode = (EditText) findViewById(R.id.storesZipCode);
        noStoresLayout = (LinearLayout) findViewById(R.id.storesNoStoresLayout);
        noStoresLayout.setVisibility(View.VISIBLE);
        list = (CustomStoreList) findViewById(R.id.customStoreList);
        filterLayout = (LinearLayout) findViewById(R.id.store_filter_layout);
    }

    private void initiateZipBasedSearch() {
        Utility.hideKeyBoard(StoreRTIActivity.this,
                findViewById(R.id.storesFindStores));
        if (null != edtPostalCode.getText()) {
            postalCodeEntered = edtPostalCode.getText().toString();
        }
        if (null != postalCodeEntered && !"".equals(postalCodeEntered)) {
            findGeocode(postalCodeEntered);
        } else {
            try {
                notifyUser(null, "Please enter a valid zip code",
                        StoreRTIActivity.this);
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void findGeocode(String strPostalCode2) {
        ArrayList<String> passing = new ArrayList<String>();
        passing.add(strPostalCode2);
        isLocationBased = false;
        GeocoderTask geocoderTask = new GeocoderTask();
        geocoderTask.setOnGeocodeFoundListener(this);

        pd = new UltaProgressDialog(this, "Fetching stores..");
        pd.show();

        geocoderTask.execute(passing);
    }

    @Override
    public void onGeocodeFound(Coordinates coordinates) {
        zipLat = coordinates.getLatitude();
        zipLong = coordinates.getLongitude();
        isLocationBased = false;
        fetchStoreList(zipLat, zipLong);

    }

    public void onGeocodesFound(ArrayList<SimpleGeocodeResult> possibleLocations) {
        // do nothing
    }

    private void fetchStoreList(double latitude, double longitude) {
        try {
            if (null != pd) {
                pd.dismiss();
            }

            pd = new UltaProgressDialog(this, "Fetching stores..");
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        invokeStores();

    }

    /**
     * Invoke Fetch stores.
     */
    private void invokeStores() {
        InvokerParams<StoreBean> invokerParams = new InvokerParams<StoreBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.GET_STORE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateStoresParameters());
        invokerParams.setUltaBeanClazz(StoreBean.class);
        StoresHandler storesHandler = new StoresHandler();
        invokerParams.setUltaHandler(storesHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
                    + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @return Map<String, String>
     */
    private Map<String, String> populateStoresParameters() {
        String strLatitude = "0.0", strLongitude = "0.0";
        if (null != currentLocation) {
            strLatitude = String.valueOf(currentLocation.getLatitude());
            strLongitude = String.valueOf(currentLocation.getLongitude());
        }

        String strRadius = String.valueOf(20);
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        if (isLocationBased) {
            urlParams.put("latitude", strLatitude);
            urlParams.put("longitude", strLongitude);
        } else {
            urlParams.put("latitude", String.valueOf(zipLat));
            urlParams.put("longitude", String.valueOf(zipLong));
        }

        urlParams.put("radius", strRadius);
        urlParams.put("amenityIds", "");
        return urlParams;
    }

    /**
     * The Class StoreHandler.
     */
    public class StoresHandler extends UltaHandler {
        protected static final String NO_STORES_FOUND_ZIP_MSG = "No stores could be fetched based on the zip code entered. Please try with another zip code";
        protected static final String NO_STORES_FOUND_MSG = "No store found near your current location."
                + " Please use zip code search to find stores in other locations.";

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            if (null != pd) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(getErrorMessage(), StoreRTIActivity.this);
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<ShippingAddressHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                StoreBean storeBean = (StoreBean) getResponseBean();
                if (null == storeBean || storeBean.getStores().isEmpty()) {
                    if (isLocationBased) {
                        notifyUser(NO_STORES_FOUND_MSG, StoreRTIActivity.this);
                    } else {
                        notifyUser(NO_STORES_FOUND_ZIP_MSG,
                                StoreRTIActivity.this);
                    }
                } else {
                    stores = new ArrayList<StoreDetailBean>();

                    UltaDataCache.getDataCacheInstance().setStores(
                            storeBean.getStores());
                    noStoresLayout.setVisibility(View.GONE);
                    // Logic has to be written here to show the stores found in
                    // the zip search
                    stores = storeBean.getStores();
                    setStoreStatus(stores);
                    UltaDataCache.getDataCacheInstance().setStores(stores);
                    if (stores != null) {
                        isNoStoresFound = false;
                        list.setStores(stores);
                        list.showList();
                        map.setStores(stores);
                        map.showMarkers();
                        mMap_list_selectoriv
                                .setImageResource(R.drawable.icon_map);
                        selectList();
                    } else {
                        isNoStoresFound = true;
                        noStoresLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void setUpZipSearch() {
        edtPostalCode.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    initiateZipBasedSearch();
                    return true;
                }
                return false;
            }
        });

    }

    @SuppressWarnings("deprecation")
    private void setStoreStatus(List<StoreDetailBean> stores) {
        Date now = new Date();
        int date = now.getDate();
        int month = now.getMonth();
        int year = now.getYear();
        for (int loop = 0; loop < stores.size(); loop++) {
            List<String> endTimes = new ArrayList<String>();
            endTimes = stores.get(loop).getStoreTimingsDetails();
            HashMap<String, java.util.Date> dayCloseTime = new HashMap<String, java.util.Date>();
            if (null != endTimes && endTimes.size() != 0) {
                for (int i = 0; i < endTimes.size(); i++) {
                    String[] arrayOfDays = endTimes.get(i).split("-");
                    String[] arrayOfEndTimings = endTimes.get(i).split("to");
                    String[] arrayOfStartTimings = arrayOfDays[1].split("to");
                    String key = arrayOfDays[0];
                    String value = arrayOfEndTimings[1];
                    String startValue = arrayOfStartTimings[0].trim();

                    // To replace the start time if it is 0:00
                    if (startValue.equals("0:00 AM")) {
                        startValue = "12:00 AM";
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "hh:mm a");
                    Date parsedEndTime = null, parsedStartTime = null;
                    try {
                        parsedEndTime = dateFormat.parse(value.trim());
                        parsedEndTime.setDate(date);
                        parsedEndTime.setMonth(month);
                        parsedEndTime.setYear(year);
                        parsedStartTime = dateFormat.parse(startValue);
                        parsedStartTime.setDate(date);
                        parsedStartTime.setMonth(month);
                        parsedStartTime.setYear(year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dayCloseTime.put(key.trim(), parsedEndTime);
                    dayCloseTime.put(key.trim() + "1", parsedStartTime);
                }
                setStatus(dayCloseTime, loop);

            }
        }
    }

    private void setStatus(HashMap<String, Date> dayCloseTime, int loop) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        Date parsedDate = new Date();
        switch (day) {

            case Calendar.SUNDAY:
                if (dayCloseTime.get("Sunday1") != null
                        && dayCloseTime.get("Sunday") != null
                        && dayCloseTime.get("Sunday1").compareTo(parsedDate) < 0
                        && dayCloseTime.get("Sunday").compareTo(parsedDate) > 0) {
                    stores.get(loop).setStoreOpen(true);
                }
                break;
            case Calendar.MONDAY:
                if (dayCloseTime.get("Monday1") != null
                        && dayCloseTime.get("Monday") != null
                        && dayCloseTime.get("Monday1").compareTo(parsedDate) < 0
                        && dayCloseTime.get("Monday").compareTo(parsedDate) > 0) {
                    stores.get(loop).setStoreOpen(true);
                }
                break;
            case Calendar.TUESDAY:
                if (dayCloseTime.get("Tuesday1") != null
                        && dayCloseTime.get("Tuesday") != null
                        && dayCloseTime.get("Tuesday1").compareTo(parsedDate) < 0
                        && dayCloseTime.get("Tuesday").compareTo(parsedDate) > 0) {
                    stores.get(loop).setStoreOpen(true);
                }
                break;
            case Calendar.WEDNESDAY:
                if (dayCloseTime.get("Wednesday1") != null
                        && dayCloseTime.get("Wednesday") != null
                        && dayCloseTime.get("Wednesday1").compareTo(parsedDate) < 0
                        && dayCloseTime.get("Wednesday").compareTo(parsedDate) > 0) {
                    stores.get(loop).setStoreOpen(true);
                }
                break;
            case Calendar.THURSDAY:
                if (dayCloseTime.get("Thursday") != null
                        && dayCloseTime.get("Thursday1") != null
                        && dayCloseTime.get("Thursday1").compareTo(parsedDate) < 0
                        && dayCloseTime.get("Thursday").compareTo(parsedDate) > 0) {
                    stores.get(loop).setStoreOpen(true);
                }
                break;
            case Calendar.FRIDAY:
                if (dayCloseTime.get("Friday1") != null
                        && dayCloseTime.get("Friday") != null
                        && dayCloseTime.get("Friday1").compareTo(parsedDate) < 0
                        && dayCloseTime.get("Friday").compareTo(parsedDate) > 0) {
                    stores.get(loop).setStoreOpen(true);
                }
                break;
            case Calendar.SATURDAY:
                if (dayCloseTime.get("Saturday1") != null
                        && dayCloseTime.get("Saturday") != null
                        && dayCloseTime.get("Saturday1").compareTo(parsedDate) < 0
                        && dayCloseTime.get("Saturday").compareTo(parsedDate) > 0) {
                    stores.get(loop).setStoreOpen(true);
                }
                break;

            default:
                stores.get(loop).setStoreOpen(false);
        }
    }

    protected void invokeRTIStatus(String storeId) {
        InvokerParams<RTIResponseBean> invokerParams = new InvokerParams<RTIResponseBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.DO_RTI_CHECK);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateParamsForRTICheck(storeId));
        invokerParams.setUltaBeanClazz(RTIResponseBean.class);
        RTIHandler storesHandler = new RTIHandler(storeId);
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

        public RTIHandler(String storeId) {
            id = Integer.parseInt(storeId);
        }

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            if (null != pd) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(getErrorMessage(), StoreRTIActivity.this);
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                RTIResponseBean rtiRespBean = (RTIResponseBean) getResponseBean();
                String stockStatus = rtiRespBean.getAtgResponse();
                // Reported error on playstore : Fixed
                // ProgressBar progressbar=(ProgressBar)findViewById(id+1);
                // progressbar.setVisibility(View.GONE);
                TextView tvStatus = (TextView) findViewById(id);
                tvStatus.setText(stockStatus);
                tvStatus.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
		/*
		 * case R.id.store_list_btn: selectList(); break;
		 */
		/*
		 * case R.id.store_map_btn: filterLayout.setVisibility(View.GONE);
		 * selectMap(); break;
		 */

            // map or list selector layout click Changes as per UI
            // redesign 3.0

            case R.id.map_list_selector_layout:

                // If already list is showing change to map view

                if (isListOnscreen) {
                    mMap_list_selectoriv.setImageResource(R.drawable.icon_listview);
                    selectMap();
                }

                // If already map is showing change to list view

                else {
                    mMap_list_selectoriv.setImageResource(R.drawable.icon_map);
                    selectList();
                }
                break;

            case R.id.storesMyLocation:
                fetchCurrentLocation();
                break;

            default:
                break;
        }
    }

    /**
     * Select map.
     */
    public void selectMap() {
        trackAppState(StoreRTIActivity.this,
                WebserviceConstants.LOCATE_STORE_MAP);
        filterLayout.setVisibility(View.GONE);
        isListOnscreen = false;
        noStoresLayout.setVisibility(View.INVISIBLE);
        list.setVisibility(View.INVISIBLE);
        map.setVisibility(View.VISIBLE);
    }

    /**
     * Select list.
     */
    private void selectList() {
        trackAppState(StoreRTIActivity.this,
                WebserviceConstants.LOCATE_STORE_LIST);
        filterLayout.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
        if (isNoStoresFound) {
            noStoresLayout.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
        }
        map.setVisibility(View.INVISIBLE);
        isListOnscreen = true;
    }

    /**
     * App permission check result for camera
     *
     * @param isSuccess
     * @param permissionRequestCode
     */
    @Override
    public void onPermissionCheckRequest(boolean isSuccess, int permissionRequestCode) {
        if (isSuccess) {
            if (permissionRequestCode == WebserviceConstants.ACCESS_FINE_LOCATION_REQUEST_CODE) {
                pd = new UltaProgressDialog(this, "Fetching your Location..");
                pd.show();

                LocationFinder locationFinder = new LocationFinder();
                locationFinder.setLocationHandler(new Handler(this));

                boolean result = locationFinder.getLocation(this, locationResult);
                if (!result) {
                    if (null != pd) {
                        pd.dismiss();
                    }
                    buildAlertMessageNoGps();
                }
            } else if (permissionRequestCode == WebserviceConstants.CAMERA_REQUEST_CODE) {
                invokeScan();
            }
        }

    }
}
