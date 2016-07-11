/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.gimbal.android.Gimbal;
import com.ulta.R;
import com.ulta.core.conf.types.LogLevel;
import com.ulta.core.pushnotification.UAGimbalAdapter;
import com.ulta.core.util.ConversantUtility;
import com.ulta.core.util.CryptoUtil;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.urbanairship.UAirship;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;

import conversant.tagmanager.sdk.CNVRTagManager;
import io.fabric.sdk.android.Fabric;

import static com.ulta.core.conf.UltaConstants.TOKEN_TYPE;
import static com.ulta.core.util.caching.AQueryCache.clearCacheOnLowMemory;



/**
 * The Class Ulta.
 *
 * @author Infosys
 */

public class Ulta extends Application {

	/**
	 * The Constant ENVIRONMENT_DETAILS_PARAM.
	 */
	private static final String ENVIRONMENT_DETAILS_PARAM = "environment";
	/**
	 * The Constant SERVER_ADDRESS.
	 */
	private static final String SERVER_ADDRESS = "serverAddress";
	/**
	 * The Constant IS_LOG_ENABLED.
	 */
	private static final String IS_LOG_ENABLED= "isLogEnabled";

	/**
	 * The Constant IS_FILE_LOG_ENABLED.
	 */
	private static final String IS_FILE_LOG_ENABLED= "isFileLogEnabled";
	/**
	 * The Constant IS_COOKIE_HANDLING_ENABLED.
	 */
	private static final String IS_COOKIE_HANDLING_ENABLED= "isCookieHandlingEnabled";
	/**
	 * The Constant IS_BEANLEGIBILITY_ENABLED.
	 */
	private static final String IS_BEANLEGIBILITY_ENABLED= "isBeanLegibilityEnabled";
	/**
	 * The Constant IS_BEANLEGIBILITY_ENABLED.
	 */
	private static final String CURRENT_ADS_URL = "currentAdsURL";

	/**
	 * Omniture Details
	 */
	private static final String REPORTING_SUITE="reportingSuite";

	private static final String TRACKING_SERVER="trackingServer";

	/** app instance. */
	public static   Ulta ultaInstance;
    private static final String Urban_KEY = "urban key";




	/**
	 * Gets the app instance.
	 *
	 * @return app instance
	 */
	public static Ulta getUltaInstance() {
		return ultaInstance;
	}


	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
//		Putting sysout here as logger will not be instantiated at this point. Uncomment only for debugging purpose.
//		System.out.println("<ViVa><Ulta><onCreate><ENTRY>");
		super.onCreate();
		//Fabric.with(this, new Crashlytics());
		//Selecting the environment and reading the details
		selectEnvironmentAndParameters();
//		Putting sysout here as logger will not be instantiated at this point. Uncomment only for debugging purpose.
//		System.out.println("<ViVa><Ulta><onCreate><RETURN>");
		ultaInstance=this;


		//ConversantUtility.conversantTag(WebserviceConstants.APP_lAUNCH_EVENTS, WebserviceConstants.APP_lAUNCH_GROUP);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final boolean isFirstRun = preferences.getBoolean(Urban_KEY, true);
		if (isFirstRun) {
			preferences.edit().putBoolean(Urban_KEY, false).apply();
		}

        /*
          Optionally, customize your config at runtime:

             AirshipConfigOptions options = new AirshipConfigOptions.Builder()
                    .setInProduction(!BuildConfig.DEBUG)
                    .setDevelopmentAppKey("Your Development App Key")
                    .setDevelopmentAppSecret("Your Development App Secret")
                    .setProductionAppKey("Your Production App Key")
                    .setProductionAppSecret("Your Production App Secret")
                    .setNotificationAccentColor(ContextCompat.getColor(this, R.color.color_accent))
                    .setNotificationIcon(R.drawable.ic_notification)
                    .build();

            UAirship.takeOff(this, options);
         */

		Gimbal.setApiKey(this, "2c1ad9b2-3c45-4e84-80a9-bbaad32a38f6");

		UAirship.takeOff(this, new UAirship.OnReadyCallback() {
			@Override
			public void onAirshipReady(UAirship airship) {
				if (isFirstRun) {
					airship.getPushManager().setUserNotificationsEnabled(true);
				}
			}
		});

        UAGimbalAdapter.shared().start();

	}
	public static void enableFabric()
	{
		Fabric.with(ultaInstance, new Crashlytics());
	}

	public static void enableConversant()
	{
		//GMOB-3500 Conversant tag
		CNVRTagManager.initialize(ultaInstance);
		//initialize SDK
		ConversantUtility.startTag();
		//Tag launch event
		ConversantUtility.lauchApp();

	}


	/**
	 * The method to identify the device type.
	 *
	 * @param context
	 *            - the activity
	 * @return - true if the device is a tablet , false otherwise.
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * The method for reading from properties file and setting the values to the UltaDataCache.
	 */
	private void selectEnvironmentAndParameters(){
//		Putting sysout here as logger will not be instantiated at this point. Uncomment only for debugging purpose.
//		System.out.println("<ViVa><Ulta><selectEnvironmentAndParameters><ENTRY>");
		Properties environmentProperties = null;
		InputStream inputStream = null;
		try {
		    inputStream = getResources().openRawResource(R.raw.environment);
		    environmentProperties = new Properties();
		    environmentProperties.load(inputStream);
		    inputStream.close();
		    try {
				KeyStore trustedKS = KeyStore.getInstance(KeyStore.getDefaultType());
				inputStream = getResources().openRawResource(R.raw.uks);
				try {
					trustedKS.load(inputStream, getUltaKey());
//					Putting sysout here as logger will not be instantiated at this point. Uncomment only for debugging purpose.
//					System.out.println("<ViVa><Ulta><selectEnvironmentAndParameters><trustedKS>"+trustedKS.size());
					getUltaDataCacheInstance().setUltaSecureStore(trustedKS);
				} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
					Logger.Log(noSuchAlgorithmException);
					noSuchAlgorithmException.printStackTrace();
				} catch (CertificateException certificateException) {
					Logger.Log(certificateException);
					certificateException.printStackTrace();
				} finally {
					inputStream.close();
				}

			} catch (KeyStoreException keyStoreException) {
					keyStoreException.printStackTrace();
			}
		} catch (IOException ioException) {
			Logger.Log("Failed to open property file", LogLevel.ERR);
		}
		if(environmentProperties!=null && !environmentProperties.isEmpty()){
			UltaDataCache ultaDataCacheInstance = getUltaDataCacheInstance();
			final String isLoggingEnabled = environmentProperties.getProperty(IS_LOG_ENABLED);
			if(isLoggingEnabled!=null && isLoggingEnabled.trim().length()>0){
				ultaDataCacheInstance.setLogEnabled(isLoggingEnabled.trim().equalsIgnoreCase(String.valueOf(true)) ? true : false);
			}
			final String isBeanLegibilityEnabled = environmentProperties.getProperty(IS_BEANLEGIBILITY_ENABLED);
			if(isBeanLegibilityEnabled!=null && isBeanLegibilityEnabled.trim().length()>0){
				ultaDataCacheInstance.setBeanLegibilityEnabled(isBeanLegibilityEnabled.trim().equalsIgnoreCase(String.valueOf(true)) ? true : false);
			}
			final String environmentName = environmentProperties.getProperty(ENVIRONMENT_DETAILS_PARAM);
			if(environmentName!=null && environmentName.trim().length()>0){
				ultaDataCacheInstance.setAppEnvironment(environmentName.trim());
			}
			final String serverAddress = environmentProperties.getProperty(SERVER_ADDRESS);
			if(serverAddress!=null && serverAddress.trim().length()>0){
				ultaDataCacheInstance.setServerAddress(serverAddress.trim());
			}
			final String fileLogging = environmentProperties.getProperty(IS_FILE_LOG_ENABLED);
			if(fileLogging!=null && fileLogging.trim().length()>0){
				ultaDataCacheInstance.setFileLoggingEnabled(Boolean.valueOf(fileLogging));
			}
			final String cookieHandling = environmentProperties.getProperty(IS_COOKIE_HANDLING_ENABLED);
			if(cookieHandling!=null && cookieHandling.trim().length()>0){
				ultaDataCacheInstance.setCookieHandlingEnabled(Boolean.valueOf(cookieHandling));
			}
			final String currentAds = environmentProperties.getProperty(CURRENT_ADS_URL);
			if(currentAds!=null && currentAds.trim().length()>0){
				ultaDataCacheInstance.setCurrentAdsURL(currentAds);
			}

			final String reportingSuite=environmentProperties.getProperty(REPORTING_SUITE);
			if(reportingSuite!=null && reportingSuite.trim().length()>0){
				ultaDataCacheInstance.setReportingSuite(reportingSuite);
			}

			final String trackingServer=environmentProperties.getProperty(TRACKING_SERVER);
			if(trackingServer!=null && trackingServer.trim().length()>0){
				ultaDataCacheInstance.setTrackingServer(trackingServer);
			}

			ultaDataCacheInstance.setLoadedFromProperties(true);
			boolean isLoggingOfEnvironmentParamsRequired = true;
			if(isLoggingOfEnvironmentParamsRequired){
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><isLoggingEnabled>"+isLoggingEnabled);
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><isBeanLegibilityEnabled>"+isBeanLegibilityEnabled);
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><environmentName>"+environmentName);
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><serverAddress>"+serverAddress);
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><ultaDataCacheInstance><LoadedFromProperties>"+ultaDataCacheInstance.isLoadedFromProperties());
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><ultaDataCacheInstance><isLoggingEnabled>"+ultaDataCacheInstance.isLogEnabled());
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><ultaDataCacheInstance><isBeanLegibilityEnabled>"+ultaDataCacheInstance.isBeanLegibilityEnabled());
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><ultaDataCacheInstance><isFileLogEnabled>"+ultaDataCacheInstance.isFileLoggingEnabled());
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><ultaDataCacheInstance><isCookieHandlingEnabled>"+ultaDataCacheInstance.isCookieHandlingEnabled());
				Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><ultaDataCacheInstance><CurrentAdsURL>"+ultaDataCacheInstance.getCurrentAdsURL());
			}
		}
		Logger.Log("<ViVa><Ulta><selectEnvironmentAndParameters><RETURN>");
	}

	/**
	 * Method that returs the key.
	 *
	 * @return char[]
	 */
	private char[] getUltaKey(){
		final String ultaKsp = "1A104F87A0DE27E244CB71B326221CB0";
		final String ultaKey = new CryptoUtil(TOKEN_TYPE).decrypt(ultaKsp);
		return ultaKey.toCharArray();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		clearCacheOnLowMemory();
	}

	/**
	 * Method to return the instance of UltaDataCache.
	 *
	 * @return the ulta data cache instance
	 */
	private UltaDataCache getUltaDataCacheInstance(){
		return UltaDataCache.getDataCacheInstance();
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
