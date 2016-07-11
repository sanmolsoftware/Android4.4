package com.ulta.core.activity.stores;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.store.StoreDetailBean;
import com.ulta.core.bean.store.StoreEventBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.util.map.LocationFinder;
import com.ulta.core.util.map.LocationFinder.LocationResult;
import com.ulta.core.widgets.UltaProgressDialog;
import com.ulta.core.widgets.flyin.OnPermissionCheck;

import java.util.List;

import static com.ulta.core.util.Utility.sortDatesBasedOnNumbers;

public class StoreDetailsActivity extends UltaBaseActivity implements
        OnClickListener, Handler.Callback, OnPermissionCheck {

    private TextView txtAddress1;
    private LinearLayout getDirection;
    private TextView txtPhoneNo;
    private LinearLayout callStore;
    private LinearLayout storeAmenities;
    // 3.5 release
    private TextView globalMessage;
    private TextView txtName;
    boolean result;
    private ListView hoursList;
    private LinearLayout storeHoursLayout;
    private LinearLayout storeAmenitiesLayout;
    private LinearLayout storeEvents;
    protected ProgressDialog pd;
    private StoreDetailBean storeDetails;
    private int positionInList;
    protected static final int GPS_REQUEST_CODE = 0;
    LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail);
        setActivity(StoreDetailsActivity.this);
        setTitle("Store Details");
        trackAppState(StoreDetailsActivity.this,
                WebserviceConstants.LOCATE_STORE_DETAILS);
        initViews();
        setStore();
        displayDetails();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK && requestCode == 4000) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.storeGetDirection:
                checkForAppPermissions(StoreDetailsActivity.this, WebserviceConstants.PERMISSION_ACCESS_FINE_LOCATION, WebserviceConstants.ACCESS_FINE_LOCATION_REQUEST_CODE, WebserviceConstants.ACCESS_FINE_LOCATION_DIALOG_TITLE, WebserviceConstants.ACCESS_FINE_LOCATION_DIALOG_MESSAGE);
                break;

            case R.id.storeCallStore:
                checkForAppPermissions(StoreDetailsActivity.this, WebserviceConstants.PERMISSION_CALL_PHONE, WebserviceConstants.PHONE_REQUEST_CODE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_TITLE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_MESSAGE);
                break;

            default:
                break;
        }

    }

    private ListView storeEventList;
    private List<StoreEventBean> storeEvent;
    StoreDetailBean storeDetailBean;
    public static int count = 0;

    private void storeEventMethod() {

        String extra = storeDetails.toString();
        Log.d(extra, "Extra");
        storeDetailBean = (StoreDetailBean) storeDetails;
        storeEvent = storeDetailBean.getStoreEvents();
        StoreListAdapter adapter = new StoreListAdapter();
        storeEventList.setAdapter(adapter);
    }

    private void buildAlertMessageNoGps() {
        final Dialog alert = showAlertDialog(StoreDetailsActivity.this,
                "Alert", "Please enable MyLocation source in system setting.",
                "OK", "Cancel");
        alert.show();
        alert.setCancelable(false);

        mAgreeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (ContextCompat.checkSelfPermission(StoreDetailsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    alert.cancel();

                } else {
                    startActivityForResult(new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                            GPS_REQUEST_CODE);
                    alert.cancel();
                }

            }
        });

        mDisagreeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alert.cancel();

            }
        });
    }

    class StoreListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return storeEvent.size();
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
            LayoutInflater inflater = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            LinearLayout mainLayout = (LinearLayout) inflater.inflate(
                    R.layout.item_subitem, null);
            TextView title = (TextView) mainLayout.findViewById(R.id.itemTitle);
            TextView desc = (TextView) mainLayout.findViewById(R.id.itemDesc);
            title.setText(storeEvent.get(position).getEventTitle1());
            desc.setText(storeEvent.get(position).getEventDescription1());
            return mainLayout;
        }

    }

    private void displayDetails() {
        if (null == storeDetails) {
            finish();
        }
        /* StoreEvents */
        storeEventList = (ListView) findViewById(R.id.storeAmenityList);
        LinearLayout storeTotalLayout = (LinearLayout) findViewById(R.id.storesEventsTotal);
        if (null != storeDetails && null != storeDetails.getStoreEvents()
                && !(storeDetails.getStoreEvents().isEmpty())) {
            storeTotalLayout.setVisibility(View.VISIBLE);
            storeEventList.setVisibility(View.VISIBLE);
            storeEventMethod();
        } else {
            storeTotalLayout.setVisibility(View.GONE);
        }

        if (null != storeDetails && null != storeDetails.getStoreAmenityImageDetails()) {
            LayoutInflater inflater1 = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);

            for (int i = 0; i < storeDetails.getStoreAmenityImageDetails()
                    .size(); i++) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                layoutParams.gravity = Gravity.LEFT;
                layoutParams1.gravity = Gravity.CENTER;

                LinearLayout linear = (LinearLayout) inflater1.inflate(
                        R.layout.amenities_list, null);
                linear.setOrientation(LinearLayout.HORIZONTAL);

                // String
                // url="http://"+UltaDataCache.getDataCacheInstance().getServerAddress()+storeDetails.getStoreAmenityImageDetails().get(i).getImageName();
                /** Entire url is sent in the reponse */
                // 3.3. release
                String url = storeDetails.getStoreAmenityImageDetails().get(i)
                        .getImageName();

                ImageView image = new ImageView(this);
                image.setPadding(15, 5, 10, 5);
                image.setLayoutParams(layoutParams);
                TextView text = new TextView(this);
                text.setTextColor(Color.BLACK);
                text.setPadding(15, 5, 10, 5);
                text.setLayoutParams(layoutParams1);

                new AQuery(image).image(url, true, true, 0,
                        R.drawable.dummy_product, null, AQuery.FADE_IN);
                linear.addView(image);
                if (null != storeDetails.getStoreAmenityImageDetails().get(i)
                        .getId()) {
                    text.setText(storeDetails.getStoreAmenityImageDetails()
                            .get(i).getId());
                    linear.addView(text);
                }

                storeAmenitiesLayout.addView(linear);
            }

            txtName.setText(storeDetails.getDisplayName());
            txtAddress1.setText(storeDetails.getAddress1() + "\n"
                    + storeDetails.getCity() + ", " + storeDetails.getState()
                    + "  " + storeDetails.getZipCode());
            txtPhoneNo.setText(storeDetails.getContactNumber());
            // 3.5 Release
            if (null != storeDetails && null != storeDetails.getGlobalMessage()
                    && !"".equals(storeDetails.getGlobalMessage())) {
                globalMessage.setText(storeDetails.getGlobalMessage());
                globalMessage.setVisibility(View.VISIBLE);
            } else {
                globalMessage.setVisibility(View.GONE);
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                    StoreDetailsActivity.this, R.layout.simple_text_list_item,
                    sortDatesBasedOnNumbers(storeDetails
                            .getStoreTimingsDetails()));
            hoursList.setAdapter(adapter1);
            hoursList.setVisibility(View.GONE);

            if (null != storeDetails && null != storeDetails.getStoreTimingsDetails()) {
                storeDetails
                        .setStoreTimingsDetails(sortDatesBasedOnNumbers(storeDetails
                                .getStoreTimingsDetails()));
                LayoutInflater inflater = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < storeDetails.getStoreTimingsDetails()
                        .size(); i++) {
                    TextView dateTV = (TextView) inflater.inflate(
                            R.layout.simple_text_list_item, null);
                    String parsedHourArray[] = parseStoreHours(storeDetails
                            .getStoreTimingsDetails().get(i));
                    dateTV.setText(parsedHourArray[0]);

                    RelativeLayout relativeLayout = new RelativeLayout(this);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
                    relativeLayout.setLayoutParams(rlp);

                    RelativeLayout.LayoutParams paramsDateTV = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    paramsDateTV.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                    dateTV.setLayoutParams(paramsDateTV);
                    relativeLayout.addView(dateTV);

                    TextView timeTV = (TextView) inflater.inflate(
                            R.layout.simple_text_list_item, null);
                    timeTV.setText(parsedHourArray[1]);

                    RelativeLayout.LayoutParams paramsTimeTV = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    paramsTimeTV.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    paramsTimeTV.setMargins(0, 3, 5, 0);
                    timeTV.setLayoutParams(paramsTimeTV);
                    timeTV.setPadding(0, 0, 10, 0);
                    relativeLayout.addView(timeTV);

                    storeHoursLayout.addView(relativeLayout);

                }
            }
        }
    }

    private void setStore() {
        List<StoreDetailBean> stores = null;
        positionInList = UltaDataCache.getDataCacheInstance()
                .getStoreBeingViewed();
        if (getIntent().getExtras() != null
                && getIntent().getExtras().get("storeDetails") != null) {
            storeDetails = (StoreDetailBean) getIntent().getExtras().get(
                    "storeDetails");
        }
        if (null == storeDetails) {
            stores = UltaDataCache.getDataCacheInstance().getStores();
            if (null != stores) {
                storeDetails = stores.get(positionInList);
            }
        }
        if (null == storeDetails || null == storeDetails.getStoreAmenityImageDetails()
                || storeDetails.getStoreAmenityImageDetails().isEmpty()) {
            storeAmenities.setVisibility(View.GONE);
        } else {
            storeAmenities.setVisibility(View.VISIBLE);
            storeAmenities.setClickable(true);
        }
        if (null == storeDetails || null == storeDetails.getStoreEvents()
                || storeDetails.getStoreEvents().isEmpty()) {
            storeEvents.setVisibility(View.GONE);
        } else {
            storeEvents.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Assign
     */
    private void initViews() {
        txtName = (TextView) findViewById(R.id.storeName);
        txtAddress1 = (TextView) findViewById(R.id.storeAddress1);
        getDirection = (LinearLayout) findViewById(R.id.storeGetDirection);
        txtPhoneNo = (TextView) findViewById(R.id.storePhoneNo);
        callStore = (LinearLayout) findViewById(R.id.storeCallStore);
        storeAmenities = (LinearLayout) findViewById(R.id.storesAmenities);
        hoursList = (ListView) findViewById(R.id.storeEventList);
        storeHoursLayout = (LinearLayout) findViewById(R.id.storeHoursLayout);
        storeAmenitiesLayout = (LinearLayout) findViewById(R.id.storeAmenitiesLayout);
        storeEvents = (LinearLayout) findViewById(R.id.storesEvents);
        // 3.5 Release
        globalMessage = (TextView) findViewById(R.id.storeGlobalMessage);
        getDirection.setOnClickListener(this);
        callStore.setOnClickListener(this);
        storeAmenities.setOnClickListener(this);
        storeEvents.setOnClickListener(this);

    }

    public String[] parseStoreHours(String storeHour) {
        String[] parsedHourArray = storeHour.split("-");
        return parsedHourArray;

    }

    private void startFetchingLocation() {
        pd = new UltaProgressDialog(this, "Fetching your Location..");
        pd.show();
        if (ContextCompat.checkSelfPermission(StoreDetailsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationFinder locationFinder = new LocationFinder();
            locationFinder.setLocationHandler(new Handler(this));
            result = locationFinder.getLocation(this, locationResult);
        }
        if (!result) {
            if (null != pd) {
                pd.dismiss();
            }
            buildAlertMessageNoGps();
        }
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
            Toast.makeText(this, "Your current location is temporarily unavailable",
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
                Logger.Log("#### Current lattitude: " + location.getLatitude()
                        + "#### Current Longitude: " + location.getLongitude());
                String sourceLatString = String.valueOf(location.getLatitude());
                String sourceLongString = String.valueOf(location
                        .getLongitude());
                String destLatString = String.valueOf(storeDetails
                        .getLatitude());
                String destLongString = String.valueOf(storeDetails
                        .getLongitude());
                String url = "http://maps.google.com/maps?saddr="
                        + sourceLatString + "," + sourceLongString + "&daddr="
                        + destLatString + "," + destLongString;

                Intent intentForGetDirection = new Intent(
                        android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentForGetDirection);
            }
        }

    };

    /**
     * App permission check result for phone
     *
     * @param isSuccess
     * @param permissionRequestCode
     */
    @Override
    public void onPermissionCheckRequest(boolean isSuccess, int permissionRequestCode) {
        if (isSuccess) {
            if (permissionRequestCode == WebserviceConstants.PHONE_REQUEST_CODE) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"
                        + storeDetails.getContactNumber().toString()));
                startActivity(callIntent);

            } else if (permissionRequestCode == WebserviceConstants.ACCESS_FINE_LOCATION_REQUEST_CODE) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    startFetchingLocation();
                } else {
                    buildAlertMessageNoGps();
                }
            }
        }

    }
}
