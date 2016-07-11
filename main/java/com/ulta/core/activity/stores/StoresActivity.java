/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.stores;

/**
 * Android imports
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.StoreBrandFiler;
import com.ulta.core.bean.product.StoreFilterBean;
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
import com.ulta.core.widgets.flyin.TitleBar;
import com.ulta.core.widgets.map.CustomMap;
import com.ulta.core.widgets.map.CustomStoreList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Imports from third parties (com, junit, net, org)
 * <p/>
 * java or javax imports
 */
/**
 * java or javax imports
 */

/**
 * The Class StoresActivity.
 */
public class StoresActivity extends UltaBaseActivity implements
        OnClickListener, OnGeocodeFoundListener,
        DialogInterface.OnClickListener, Handler.Callback, OnPermissionCheck {

    protected static final int GPS_REQUEST_CODE = 0;
    protected static final int STORE_DETAIL_REQUEST_CODE = 50000;
    protected static final String NO_STORES_FOUND_MSG = "No store found near your current location."
            + " Please use zip code search to find stores in other locations.";
    protected static final String NO_STORES_FOUND_ZIP_MSG = "No stores found near"
            + " the location entered. Please try another location.";

    /** The map. */
    CustomMap map;

    /** The list. */
    CustomStoreList list;

    /** The btn map. */
    LinearLayout btnMap;

    /** The btn list. */
    LinearLayout btnList;

    ImageView btnFindStores;
    ImageView imgMyLocation;
    EditText edtZipcode;
    LinearLayout noStoresLayout;

    ProgressDialog pd;
    LocationManager locationManager;

    private String strEnteredLocation;
    private String strLatitude;
    private String strLongitude;
    private String strRadius;

    private boolean isZipBased;
    private boolean isLocationBased;

    private boolean isNoStoresFound = true;

    Location currentLocation;

    private boolean isListOnscreen = false;

    // 3.5 release changes
    private TextView txtFilterByBrand, txtClearFilter;
    // private TextView txtFiltering;
    private List<StoreBrandFiler> listOfBrands;
    private AlertDialog filterDialog;
    String brandIdSelected = "";
    private LinearLayout filterLayout;

    private LinearLayout mMap_list_selector_layout;// map or list selector
    // layout
    private ImageView mMap_list_selectoriv;// map or list image icon view
    private Button btnClearSearch;// clear the search box

    private ArrayList<SimpleGeocodeResult> mPossibleLocations;

    @Override
    protected void onResume() {

        super.onResume();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.ulta.core.activity.stores.MapBaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stores);
        setTitle("Find Store");
        setActivity(StoresActivity.this);
        isLocationBased = true;
        initViews();
        if (UltaDataCache.getDataCacheInstance().getStoreBrandFilters() == null) {
            invokeFetchBrandsFilter();
        } else
            listOfBrands = UltaDataCache.getDataCacheInstance()
                    .getStoreBrandFilters();
        setUpZipSearch();
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if (status == ConnectionResult.SUCCESS) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                startFetchingLocation();
            } else {
                buildAlertMessageNoGps();
            }

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

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
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
                    filterLayout.setVisibility(View.GONE);
                    selectMap();
                }

                // If already map is showing change to list view

                else {
                    mMap_list_selectoriv.setImageResource(R.drawable.icon_map);
                    selectList();
                }
                break;
            case R.id.storesMyLocation:
                edtZipcode.setText("");
                strEnteredLocation = null;
                Utility.hideKeyBoard(StoresActivity.this, edtZipcode);
                isZipBased = false;
                isLocationBased = true;
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    startFetchingLocation();
                } else {
                    buildAlertMessageNoGps();
                }
                break;
            case R.id.storesFindStores:
                initiateTextBasedSearch();
                break;
            case R.id.txtFilterByBrand:
                if (isNoStoresFound) {
                    try {
                        notifyUser(
                                null,
                                "Please enter a valid zipcode first to locate an Ulta store near you",
                                StoresActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showFilter();
                }
                break;
            case R.id.txtClearFilter:
                if (brandIdSelected != null && !brandIdSelected.isEmpty()) {
                    brandIdSelected = "";
                    fetchStoreList();
                } else {
                    Toast.makeText(getApplicationContext(), "No filter is applied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    /**
     * This method fetches the list of brands that are to be shown to the user
     * for filtering
     */

    private void invokeFetchBrandsFilter() {
        InvokerParams<StoreFilterBean> invokerParams = new InvokerParams<StoreFilterBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.GET_BRANDS_FOR_FILTERING);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        // Setting URL params
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-depth", "0");
        urlParams.put("atg-rest-output", "json");
        invokerParams.setUrlParameters(urlParams);
        invokerParams.setUltaBeanClazz(StoreFilterBean.class);
        BrandFilterHandler brandFilterHandler = new BrandFilterHandler();
        invokerParams.setUltaHandler(brandFilterHandler);
        try {
            Logger.Log("<UltaProductListActivity><fnInvokeSearch()>"
                    + "<Going to Execute the Delegator>>");
            new ExecutionDelegator(invokerParams);
            Logger.Log("<UltaProductListActivity><fnInvokeSearch()><Executed the Delegator>>");
        } catch (UltaException exception) {
            Logger.Log("<UltaProductListActivity><fnInvokeSearch()><UltaException>>"
                    + exception);
        }
    }

    public class BrandFilterHandler extends UltaHandler {
        /**
         * on Handle message.
         *
         * @param msg
         *            the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<BrandFilterHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            StoresActivity.this);
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                StoreFilterBean brandFilterBean = (StoreFilterBean) getResponseBean();
                if (brandFilterBean != null) {
                    listOfBrands = brandFilterBean.getAtgResponse();
                    UltaDataCache.getDataCacheInstance().setStoreBrandFilters(
                            listOfBrands);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void findGeocode(String strPostalCode2) {
        ArrayList<String> passing = new ArrayList<String>();
        passing.add(strPostalCode2);

        GeocoderTask geocoderTask = new GeocoderTask();
        geocoderTask.setOnGeocodeFoundListener(this);

        pd = new UltaProgressDialog(this, "Fetching stores..");
        pd.show();
        geocoderTask.execute(passing);
    }

    // Method to show the brand filters in store locator
    public void showFilter() {
        String[] serviceTypes = null;
        String[] amenityIds = null;
        if (listOfBrands != null & !listOfBrands.isEmpty()) {
            serviceTypes = new String[listOfBrands.size()];
            amenityIds = new String[listOfBrands.size()];
            for (int i = 0; i < listOfBrands.size(); i++) {
                serviceTypes[i] = listOfBrands.get(i).getServiceType();
                amenityIds[i] = listOfBrands.get(i).getAmenityId();
            }
        }
        trackAppState(StoresActivity.this,
                WebserviceConstants.LOCATE_STORE_FILTER);
        boolean markedBooleanArray[] = null;
        // Code to mark what are brands all ready selected
        if (brandIdSelected != null && !brandIdSelected.isEmpty()) {
            String marked[] = brandIdSelected.split(",");
            markedBooleanArray = new boolean[serviceTypes.length];
            for (int j = 0; j < marked.length; j++) {
                for (int i = 0; i < serviceTypes.length; i++) {
                    if (marked[j].equals(listOfBrands.get(i).getServiceType())) {
                        markedBooleanArray[i] = true;
                        continue;
                    }
                }
            }
        }
        final AlertDialog.Builder f = new AlertDialog.Builder(this);
        f.setTitle("Filter by Brands/ServiceTypes");
        f.setMultiChoiceItems(serviceTypes, markedBooleanArray,
                new OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            if (brandIdSelected.equals("")) {
                                brandIdSelected = listOfBrands.get(which)
                                        .getServiceType();
                            } else {
                                brandIdSelected += ","
                                        + listOfBrands.get(which)
                                        .getServiceType();
                            }
                        } else {
                            if (!brandIdSelected.equals("")) {
                                String[] temp = brandIdSelected.split(",");
                                brandIdSelected = "";
                                for (int k = 0; k < temp.length; k++) {
                                    if (!temp[k].equals(listOfBrands.get(which)
                                            .getServiceType())) {
                                        if (!brandIdSelected.isEmpty()) {
                                            brandIdSelected += "," + temp[k];
                                        } else {
                                            brandIdSelected = temp[k];
                                        }
                                    }
                                }
                            }
                        }

                    }
                });
        f.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                fetchStoreList();
            }
        });
        f.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        filterDialog = f.create();
        filterDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {
                startFetchingLocation();
            } else {
                buildAlertMessageNoGps();
            }
        } else if (requestCode == STORE_DETAIL_REQUEST_CODE
                && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                int position = data.getExtras().getInt("position");

                selectMap();
                map.panTo(position);
            }
        }

    }

    @Override
    protected void onPause() {

        if (null != pd) {
            pd.dismiss();
        }

        super.onPause();
    }

    /**
     * Select map.
     */
    private void selectMap() {
        trackAppState(StoresActivity.this, WebserviceConstants.LOCATE_STORE_MAP);
        isListOnscreen = false;
        noStoresLayout.setVisibility(View.INVISIBLE);
        filterLayout.setVisibility(View.GONE);
        list.setVisibility(View.INVISIBLE);
        map.setVisibility(View.VISIBLE);
        // btnMap.setBackgroundResource(R.drawable.top_button_selected);
        // btnList.setBackgroundResource(R.drawable.top_button_unselected);
    }

    /**
     * Select list.
     */
    private void selectList() {
        trackAppState(StoresActivity.this,
                WebserviceConstants.LOCATE_STORE_LIST);
        list.setVisibility(View.VISIBLE);
        if (isNoStoresFound) {
            noStoresLayout.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.GONE);
        }
        map.setVisibility(View.INVISIBLE);
        // btnMap.setBackgroundResource(R.drawable.top_button_unselected);
        // btnList.setBackgroundResource(R.drawable.top_button_selected);
        isListOnscreen = true;
    }

    /**
     * Instantiating the variable holding the child views.
     */
    private void initViews() {
        // btnList = (LinearLayout) findViewById(R.id.store_list_btn);
        // btnMap = (LinearLayout) findViewById(R.id.store_map_btn);
        list = (CustomStoreList) findViewById(R.id.customStoreList);
        map = (CustomMap) findViewById(R.id.customMap);
        btnFindStores = (ImageView) findViewById(R.id.storesFindStores);
        edtZipcode = (EditText) findViewById(R.id.storesZipCode);
        imgMyLocation = (ImageView) findViewById(R.id.storesMyLocation);
        noStoresLayout = (LinearLayout) findViewById(R.id.storesNoStoresLayout);
        noStoresLayout.setVisibility(View.GONE);
        // btnList.setOnClickListener(this);
        // btnMap.setOnClickListener(this);
        btnFindStores.setOnClickListener(this);
        imgMyLocation.setOnClickListener(this);
        // 3.5 release changes
        txtFilterByBrand = (TextView) findViewById(R.id.txtFilterByBrand);
        txtFilterByBrand.setOnClickListener(this);
        txtClearFilter = (TextView) findViewById(R.id.txtClearFilter);
        txtClearFilter.setOnClickListener(this);
        filterLayout = (LinearLayout) findViewById(R.id.store_filter_layout);
        // layout and clear button for search box
        mMap_list_selector_layout = (LinearLayout) findViewById(R.id.map_list_selector_layout);
        mMap_list_selector_layout.setOnClickListener(this);
        mMap_list_selectoriv = (ImageView) findViewById(R.id.map_list_selectoriv);
        btnClearSearch = (Button) findViewById(R.id.btnClearSearch);
        btnClearSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edtZipcode.setText("");
            }
        });

        titleBar = (TitleBar) findViewById(R.id.titlebar);

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

    private void startFetchingLocation() {
        checkForAppPermissions(StoresActivity.this, WebserviceConstants.PERMISSION_ACCESS_FINE_LOCATION, WebserviceConstants.ACCESS_FINE_LOCATION_REQUEST_CODE, WebserviceConstants.ACCESS_FINE_LOCATION_DIALOG_TITLE, WebserviceConstants.ACCESS_FINE_LOCATION_DIALOG_MESSAGE);


//		pd = new UltaProgressDialog(this, "Fetching your Location..");
//		pd.show();
//
//		LocationFinder locationFinder = new LocationFinder();
//		locationFinder.setLocationHandler(new Handler(this));
//
//		boolean result = locationFinder.getLocation(this, locationResult);
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
                    pd.dismiss();
                }
                Logger.Log("#### Current lattitude: " + location.getLatitude());
                Logger.Log("#### Current Longitude: " + location.getLongitude());

                // hard coding the latitude and longutude from current
                // location check

				/*
				 * location.setLatitude(41.902007);
				 * location.setLongitude(-87.677815);
				 */

                currentLocation = location;
                UltaDataCache.getDataCacheInstance().setUserLocation(
                        currentLocation);

                fetchStoreList(location.getLatitude(), location.getLongitude());
            }
        }

    };

    private void fetchStoreList(double latitude, double longitude) {
        if (null != pd) {
            pd.dismiss();
        }

        try {
            pd = new UltaProgressDialog(this, "Fetching your Location..");
            pd.show();
        } catch (Exception e) {
            Log.e("StoresActivity", "Window has leaked");
        }

        invokeStores();

    }

    private void fetchStoreList() {
        if (null != pd) {
            pd.dismiss();
        }

        try {
            pd = new UltaProgressDialog(this, "Fetching stores..");
            pd.show();
        } catch (Exception e) {
            Log.e("StoresActivity", "Window has leaked");
        }

        invokeStores();

    }

    /**
     * Invoke shippment.
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
        if (null != currentLocation) {
            strLatitude = String.valueOf(currentLocation.getLatitude());
            strLongitude = String.valueOf(currentLocation.getLongitude());
        }
        strRadius = String.valueOf(20);
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        if (isLocationBased) {
            urlParams.put("latitude", strLatitude);
            urlParams.put("longitude", strLongitude);
            urlParams.put("radius", strRadius);
        } else if (isZipBased) {
            strLatitude = String.valueOf(zipLat);
            strLongitude = String.valueOf(zipLng);
            urlParams.put("latitude", strLatitude);
            urlParams.put("longitude", strLongitude);
            urlParams.put("radius", strRadius);
        }
        urlParams.put("amenityIds", brandIdSelected);
        return urlParams;
    }

    /**
     * The Class ShippingAddressHandler.
     */
    public class StoresHandler extends UltaHandler {

        private List<StoreDetailBean> stores;

        /**
         * Handle message.
         *
         * @param msg
         *            the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            if (null != pd && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(getErrorMessage(), StoresActivity.this);
                    brandIdSelected = "";
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<ShippingAddressHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                StoreBean storeBean = (StoreBean) getResponseBean();

                if (null == storeBean || storeBean.getStores().isEmpty()) {
                    isNoStoresFound = true;
                    stores = storeBean.getStores();
                    if (isLocationBased) {
                        notifyUser(NO_STORES_FOUND_MSG, StoresActivity.this);
                    } else {
                        notifyUser(NO_STORES_FOUND_ZIP_MSG, StoresActivity.this);
                    }
                    map.setStores(stores);
                    map.showMarkers();
                    list.setStores(stores);
                    list.showList();
                    if (isListOnscreen) {
                        noStoresLayout.setVisibility(View.VISIBLE);
                        filterLayout.setVisibility(View.GONE);
                    }
                } else {

                    stores = storeBean.getStores();
                    setStoreStatus(stores);
                    UltaDataCache.getDataCacheInstance().setStores(stores);
                    isNoStoresFound = false;
                    noStoresLayout.setVisibility(View.GONE);
                    map.setStores(stores);
                    map.showMarkers();
                    if (null != storeBean && !storeBean.getStores().isEmpty()) {
                        map.panTo(0);
                    }
                    list.setStores(stores);
                    if (isListOnscreen) {
                        filterLayout.setVisibility(View.GONE);
                    }
                    list.showList();

                    if (isLocationBased) {
                        selectMap();
                    }
                }
            }
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
                if (null != endTimes && !endTimes.isEmpty()) {
                    for (int i = 0; i < endTimes.size(); i++) {
                        String[] arrayOfDays = endTimes.get(i).split("-");
                        String[] arrayOfEndTimings = endTimes.get(i)
                                .split("to");
                        String[] arrayOfStartTimings = arrayOfDays[1]
                                .split("to");
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
                }
                setStatus(dayCloseTime, loop);
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
    }

    private Double zipLat;
    private Double zipLng;

    @Override
    public void onGeocodeFound(Coordinates coordinates) {
        zipLat = coordinates.getLatitude();
        zipLng = coordinates.getLongitude();

        fetchStoreList(zipLat, zipLng);

    }

    @Override
    public void onGeocodesFound(
            ArrayList<SimpleGeocodeResult> possibleLocationsList) {
        // show the list of rocations and let the user choose
        mPossibleLocations = possibleLocationsList;
        AlertDialog.Builder b = new Builder(this);
        b.setTitle("Choose a city");
        ArrayList<String> namedLocations = new ArrayList<String>();
        for (int i = 0; i < mPossibleLocations.size(); i++) {
            namedLocations.add(mPossibleLocations.get(i).getFormattedAddress());
        }
        String[] possiblePlaces = namedLocations
                .toArray(new String[namedLocations.size()]);
        b.setItems(possiblePlaces, this);
        b.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Coordinates coords = mPossibleLocations.get(which).getLocation();
        zipLat = coords.getLatitude();
        zipLng = coords.getLongitude();
        fetchStoreList();
    }

    private void setUpZipSearch() {
        edtZipcode.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    initiateTextBasedSearch();
                    return true;
                }
                return false;
            }
        });

    }

    private void initiateTextBasedSearch() {
        Utility.hideKeyBoard(StoresActivity.this,
                findViewById(R.id.storesFindStores));
        if (null != edtZipcode.getText()) {
            strEnteredLocation = edtZipcode.getText().toString();
        }
        if (null != strEnteredLocation && !"".equals(strEnteredLocation)) {
            isZipBased = true;
            isLocationBased = false;
            if (strEnteredLocation.matches("[0-9]{5}")
                    || strEnteredLocation.matches("[0-9]{5}-[0-9]{4}")) {
                findGeocode(strEnteredLocation);
            } else {
                findGeocode(strEnteredLocation);
            }

            brandIdSelected = "";
        } else {
            try {
                notifyUser(null, "Please enter valid location information",
                        StoresActivity.this);
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showMap(int position) {
        selectMap();
        map.showMarkers(position);
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
