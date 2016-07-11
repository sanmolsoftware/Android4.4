/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.activity.product.SpecialOffersActivity;
import com.ulta.core.activity.product.UltaProductListActivity;
import com.ulta.core.activity.stores.StoresActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;

import java.util.List;

/**
 * The Class SplashActivity.
 */
public class SplashActivity extends Activity {

	/** The splash display length. */
	private final int SPLASH_DISPLAY_LENGTH = 3000;
	private TextView versionInfo;
	private String versionNo;

	private SharedPreferences prefs;
	private String PROPERTY_APP_VERSION = "appversion";

	// smart link parameters
	String mUrlParameter, mSmartLinkPageToNavigate;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param icicle
	 *            the icicle
	 */

	public void onCreate(Bundle icicle) {
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);

		if (needStartApp()) {

			AppRater.app_did_launched(this);
			checkIfUserIsStaySignedInAndLoggedInCurrently();
			// smart link
			Intent intent = getIntent();
			Log.i("data", "******" + intent.getData());

			// get data from smart link
			if (null != intent.getData()) {
				mUrlParameter = intent.getData().toString();
			}

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (!Ulta.isTablet(this)) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			setContentView(R.layout.splash);
			// These are added here to
			// CompuwareUEM.startup(this,
			// WebserviceConstants.DYNATRACE_APP_IDENTIFIER,
			// WebserviceConstants.DYNATRACE_APP_SERVER_URL, false, null);
			// CompuwareUEM.uemCaptureStatus();
			// CompuwareUEM.enableCrashReporting(true);
		
			// versionNo = "2.4";
			/*try {
				versionNo = getPackageManager().getPackageInfo(
						getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			versionInfo = (TextView) findViewById(R.id.versionNumber);
			versionInfo.setText("Version :" + versionNo);
			MediaController controller = new MediaController(this);
			controller.hide();*/
			/**
			 * New Handler to start the HomeView and close this Splash-Screen
			 * after some seconds.
			 */
			new Handler().postDelayed(new Runnable() {
				public void run() {

					// if mUrlParameter is ulta://go/home or
					// ulta://go/store/filterList
					// split the string based on '/'
					if (null != mUrlParameter && !"".equals(mUrlParameter)) {
						String[] smartLinkParameterArray = mUrlParameter
								.split("/");
						if (null != smartLinkParameterArray
								&& smartLinkParameterArray.length > 3) {
							mSmartLinkPageToNavigate = smartLinkParameterArray[3];
						}

					}
					Intent mainIntent = null;
					// check if coming from smart link navigate to corresponding
					// pages
					if (null != mSmartLinkPageToNavigate
							&& !"".equals(mSmartLinkPageToNavigate)) {

						if (mSmartLinkPageToNavigate
								.equalsIgnoreCase(WebserviceConstants.SMART_LINK_HOME)) {
							mainIntent = new Intent(SplashActivity.this,
									HomeActivity.class);
							mainIntent.putExtra("launch", "true");
						} else if (mSmartLinkPageToNavigate
								.equalsIgnoreCase(WebserviceConstants.SMART_LINK_SHOP)) {
							mainIntent = new Intent(SplashActivity.this,
									ShopListActivity.class);
						} else if (mSmartLinkPageToNavigate
								.equalsIgnoreCase(WebserviceConstants.SMART_LINK_STORE)) {
							mainIntent = new Intent(SplashActivity.this,
									StoresActivity.class);
						} else if (mSmartLinkPageToNavigate
								.equalsIgnoreCase(WebserviceConstants.SMART_LINK_SALE)) {
							mainIntent = new Intent(SplashActivity.this,
									SpecialOffersActivity.class);
						} else if (mSmartLinkPageToNavigate
								.equalsIgnoreCase(WebserviceConstants.SMART_LINK_NEW_ARRIVAL)) {
							mainIntent = new Intent(SplashActivity.this,
									UltaProductListActivity.class);
							mainIntent.setAction("fromHomeByNewArrivals");
						} else {
							mainIntent = new Intent(SplashActivity.this,
									HomeActivity.class);
						}
					}
					/*
					 * Create an Intent that will start the Home-Activity or
					 * carousel Activity depending on the value of shared
					 * preference.
					 */
					else {
						prefs = getSharedPreferences(
								WebserviceConstants.CHANNEL_ID_PREF, 0);
						int registeredVersion = prefs.getInt(
								PROPERTY_APP_VERSION, Integer.MIN_VALUE);
						int currentVersion = getAppVersion(SplashActivity.this);
						if (registeredVersion != currentVersion) {
							SharedPreferences preferences = SplashActivity.this
									.getSharedPreferences("userdetails",
											MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences
									.edit();
							editor.clear();
							editor.putBoolean("isFirstTime", true);
							editor.commit();
						}

						if (getSharedPreference()) {
							mainIntent = new Intent(SplashActivity.this,
									CarouselActivity.class);
						} else {
							mainIntent = new Intent(SplashActivity.this,
									HomeActivity.class);
							mainIntent.putExtra("launch", "true");
						}
					}

					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
			}, SPLASH_DISPLAY_LENGTH);
			// Push Notification Initialization

		} else {
			finish();
		}

	}

	private Boolean getSharedPreference() {
		SharedPreferences preferences = this.getSharedPreferences(
				"userdetails", MODE_PRIVATE);
		Boolean isFirstTime = preferences.getBoolean("isFirstTime", true);

		WebserviceConstants.isULTA_SITE_VALUE = preferences.getBoolean("isULTA_SITE_VALUE", false);
		WebserviceConstants.ULTA_SITE_VALUE =preferences.getString("ULTA_SITE_VALUE","B");

		return isFirstTime;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("GCM geo test", "onResume(): ");
		// mGeofenceMan.addGeofenceEventListener(this);
		// mGeofenceMan.startService();

	}

	public void onPause() {
		super.onPause();
		// mGeofenceMan.addGeofenceEventListener(null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	private boolean needStartApp() {
		final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1024);

		if (!tasksInfo.isEmpty()) {
			final String ourAppPackageName = getPackageName();
			RunningTaskInfo taskInfo;
			final int size = tasksInfo.size();
			for (int i = 0; i < size; i++) {
				taskInfo = tasksInfo.get(i);
				if (ourAppPackageName.equals(taskInfo.baseActivity
						.getPackageName())) {
					// continue application start only if there is the only
					// Activity in the task
					// (BTW in this case this is the StartupActivity)
					return taskInfo.numActivities == 1;
				}
			}
		}

		return true;
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	
	
	public void checkIfUserIsStaySignedInAndLoggedInCurrently() {
		SharedPreferences staySignedInSharedPreferences = getSharedPreferences(
				WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);
		boolean isStaySignedIn = staySignedInSharedPreferences.getBoolean(
				WebserviceConstants.IS_STAY_SIGNED_IN, false);
		boolean isLoggedIn = staySignedInSharedPreferences.getBoolean(
				WebserviceConstants.IS_LOGGED_IN, false);
		SharedPreferences refreshTimeOutSharedPreferences = getSharedPreferences(WebserviceConstants.HOME_BANNER_REFRESH_SHAREDPREF,
				MODE_PRIVATE);
		Editor refreshTimeOutEditor = refreshTimeOutSharedPreferences.edit();
		refreshTimeOutEditor.putBoolean(WebserviceConstants.IS_REFRESH_TIME_EXPIRED, false);
		refreshTimeOutEditor.commit();
		
		Utility.saveCookie(WebserviceConstants.SESSION_ID_COOKIE, null);
		Utility.saveCookie(WebserviceConstants.DYN_USER_ID_COOKIE, null);
		Utility.saveCookie(WebserviceConstants.DYN_USER_CONFIRM_COOKIE, null);
		if(!isLoggedIn)
		{
			SharedPreferences basketPreferences = this.getSharedPreferences(
					WebserviceConstants.COUNTS_PREFS_NAME, MODE_PRIVATE);
			Editor edit = basketPreferences.edit();
			edit.putInt(WebserviceConstants.BASKET_COUNT, 0);
			edit.commit();
		}
		
		if (!isStaySignedIn && isLoggedIn) {
			/*Utility.saveCookie(WebserviceConstants.SESSION_ID_COOKIE, "");
			Utility.saveCookie(WebserviceConstants.DYN_USER_ID_COOKIE, "");
			Utility.saveCookie(WebserviceConstants.DYN_USER_CONFIRM_COOKIE, "");*/
			UltaDataCache.getDataCacheInstance().setIfNotSignedInClearSession(
					true);
		} else if (isStaySignedIn) {
			UltaDataCache.getDataCacheInstance().setUpdateBasketAndFavCount(
					true);
		}

	}

}
