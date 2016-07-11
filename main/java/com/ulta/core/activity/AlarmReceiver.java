package com.ulta.core.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

	private static final int MODE_PRIVATE = 0;
	private static final String ACTIVITY_SERVICE = "activity";
	private SharedPreferences refreshTimeOutSharedPreferences;
	private Editor refreshTimeOutEditor;

	@Override
	public void onReceive(Context context, Intent intent) {
		refreshTimeOutSharedPreferences = context.getSharedPreferences(
				WebserviceConstants.HOME_BANNER_REFRESH_SHAREDPREF,
				MODE_PRIVATE);
		refreshTimeOutEditor = refreshTimeOutSharedPreferences.edit();
		// set value for shop page

		refreshTimeOutEditor.putBoolean(
				WebserviceConstants.IS_REFRESH_TIME_EXPIRED, true);
		// Set the value in shared preference
		refreshTimeOutEditor.putBoolean(
				WebserviceConstants.IS_REFRESH_TIME_EXPIRED_SHOP_PAGE, true);
		refreshTimeOutEditor.commit();
		UltaDataCache.getDataCacheInstance().setAppLaunched(true);
		// check which activity is on top. If home page need to refresh. Else
		// refresh in resume of home page
		ActivityManager mngr = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskList = mngr
				.getRunningTasks(70);
		// Toast.makeText(context, "Time expired", Toast.LENGTH_SHORT).show();
		// If home is on top check if the app is on foreground or background
		if (!taskList.isEmpty()) {
			if (taskList.get(0).numActivities == 1) {

				try {
					ActivityManager am = (ActivityManager) context
							.getSystemService(ACTIVITY_SERVICE);
					RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1)
							.get(0);
					String foregroundTaskPackageName = foregroundTaskInfo.topActivity
							.getPackageName();
					PackageManager pm = context.getPackageManager();
					PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(
							foregroundTaskPackageName, 0);
					String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo
							.loadLabel(pm).toString();

					// if application is in foreground navigate to home page
					if (foregroundTaskAppName.equalsIgnoreCase("Ulta")) {
						// Toast.makeText(context, "Banner refreshed for home",
						// Toast.LENGTH_SHORT).show();
						Intent homeIntent = new Intent(context,
								HomeActivity.class);
						homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						context.startActivity(homeIntent);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				try {
					ActivityManager am = (ActivityManager) context
							.getSystemService(ACTIVITY_SERVICE);
					RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1)
							.get(0);
					String foregroundTaskPackageName = foregroundTaskInfo.topActivity
							.getPackageName();
					PackageManager pm = context.getPackageManager();
					PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(
							foregroundTaskPackageName, 0);
					String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo
							.loadLabel(pm).toString();

					// if application is in foreground navigate to home page
					if (foregroundTaskAppName.equalsIgnoreCase("Ulta")) {
						String foregroundActivityName = foregroundTaskInfo.topActivity
								.getClassName();
						// Toast.makeText(context, foregroundActivityName,
						// Toast.LENGTH_LONG).show();
						if (foregroundActivityName
								.equals("com.ulta.core.activity.account.ShopListActivity")) {
							Intent shopIntent = new Intent(context,
									ShopListActivity.class);
							shopIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_SINGLE_TOP);
							context.startActivity(shopIntent);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
