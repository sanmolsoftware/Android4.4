package com.ulta.core.pushnotification;


import android.location.Location;
import android.util.Log;

import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;
import com.urbanairship.UAirship;
import com.urbanairship.location.RegionEvent;
import com.urbanairship.util.DateUtils;

public class UAGimbalAdapter {

    /**
     * GimbalAdapter logging tag.
     */
    private static final String TAG = "GimbalAdapter ";
    /**
     * GimbalAdapter shared instance.
     */
    private static UAGimbalAdapter instance = new UAGimbalAdapter();
    /**
     * Analytics event source.
     */
    private static final String SOURCE = "Gimbal";
    /**
     * Boolean representing the started state of the GimbalAdapter.
     */
    private boolean isStarted = false;
    /**
     * Listener for Gimbal place events. Creates an analytics event
     * corresponding to boundary event type.
     */
    private PlaceEventListener placeEventListener = new PlaceEventListener() {
        @Override
        public void onVisitStart(Visit visit) {
            Log.i(TAG, "Entered place: " + visit.getPlace().getName() + "Entrance date: " +
                    DateUtils.createIso8601TimeStamp(visit.getArrivalTimeInMillis()));
            RegionEvent enter = new RegionEvent(visit.getPlace().getIdentifier(), SOURCE, RegionEvent.BOUNDARY_EVENT_ENTER);
            UAirship.shared().getAnalytics().addEvent(enter);
        }

        @Override
        public void onVisitEnd(Visit visit) {
            Log.i(TAG, "Exited place: " + visit.getPlace().getName() + "Entrance date: " +
                    DateUtils.createIso8601TimeStamp(visit.getArrivalTimeInMillis()) + "Exit date:" +
                    DateUtils.createIso8601TimeStamp(visit.getDepartureTimeInMillis()));
            RegionEvent exit = new RegionEvent(visit.getPlace().getIdentifier(), SOURCE, RegionEvent.BOUNDARY_EVENT_EXIT);
            UAirship.shared().getAnalytics().addEvent(exit);
        }

        @Override
        public void locationDetected(Location location) {
            super.locationDetected(location);

            Log.d(TAG, "" + location.getLongitude());
        }
    };


    /**
     * Hidden to support the singleton pattern.
     */
    UAGimbalAdapter() {
    }

    /**
     * GimbalAdapter shared instance.
     */
    public synchronized static UAGimbalAdapter shared() {
        return instance;
    }

    /**
     * Starts tracking places.
     */
    public void start() {
        if (isStarted) {
            return;
        }

        isStarted = true;


        PlaceManager.getInstance().addListener(placeEventListener);
        PlaceManager.getInstance().startMonitoring();
        Log.d(TAG, "Gimbal Started");
        Log.d(TAG, "" + PlaceManager.getInstance().currentVisits());
    }

    /**
     * Stops tracking places.
     */
    public void stop() {
        if (!isStarted) {
            return;
        }
        isStarted = false;

        PlaceManager.getInstance().stopMonitoring();
        PlaceManager.getInstance().removeListener(placeEventListener);

        Log.i(TAG, "Adapter Stopped");
    }
}
