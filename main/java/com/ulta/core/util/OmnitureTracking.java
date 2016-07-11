package com.ulta.core.util;

import android.app.Activity;

import com.adobe.mobile.Analytics;
import com.adobe.mobile.Config;
import com.ulta.core.conf.WebserviceConstants;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class OmnitureTracking {

	public Hashtable<String, Object> evarHashTable = new Hashtable<String, Object>();

	public static void startActivity(Activity activity) {

		Config.setContext(activity);
		Config.collectLifecycleData(activity);

	}

	public static void stopActivity() {

		Config.pauseCollectingLifecycleData();
		Config.setDebugLogging(true);

	}

	public static void setPageName(String pageName) {
		Analytics.trackState(pageName, null);

	}

	public static void setAppAction(String action) {
		Map<String, Object> omnitureData = new HashMap<String, Object>();
		omnitureData.put(action, "1");
		Analytics.trackAction(action, omnitureData);
	}
	
	public static void setEvars(String action,
			Map<String, Object> contextData) {
		Analytics.trackAction(action, contextData);
	}
	
	public static void setEvarsUsingPageName(String pageName,
			Map<String, Object> contextData) {
		Analytics.trackState(pageName, contextData);
	}

	public static void setErrors(Map<String, Object> contextData) {
		Analytics.trackAction(WebserviceConstants.ERROR_CODE_EVENT_ACTION,
				contextData);
	}
}
