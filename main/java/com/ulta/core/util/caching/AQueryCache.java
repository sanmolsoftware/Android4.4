/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util.caching;

import android.content.Context;
import android.os.Environment;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.ulta.core.util.log.Logger;

import java.io.File;



/**
 * The Class AQueryCache.
 *
 * @author viva
 */
public class AQueryCache {
	
	/** The Constant NETWORK_LIMIT. */
	private static final int NETWORK_LIMIT = 8;

	/** The Constant CACHE_LIMIT. */
	private static final int CACHE_LIMIT = 30;
	// 3M
	/** The Constant CACHE_CLEAR_TRIGGER_SIZE. */
	private static final long CACHE_CLEAR_TRIGGER_SIZE = megabyteToByteConvertor(3);
	// 2M
	/** The Constant CACHE_CLEAR_TARGET_SIZE. */
	private static final long CACHE_CLEAR_TARGET_SIZE = megabyteToByteConvertor(2);
	
	/** The Constant CACHE_DIRECTORY. */
	private static final String CACHE_DIRECTORY = "com.ulta.cache";
	
	/** The Constant isFileCachingRequired. */
	private static final boolean isFileCachingRequired = false;

	/**
	 * The cache instance for temporary storage.
	 */
	private static AQueryCache aqueryCacheInstance;

	/**
	 * Gets the single instance of UltaDataCache.
	 * 
	 * @return single instance of UltaDataCache
	 */
	public static AQueryCache getAqueryCacheInstance() {
		if (aqueryCacheInstance == null) {
			Logger.Log("<AQueryCache><getAqueryCacheInstance><Instantiating...>");
			aqueryCacheInstance = new AQueryCache();
			configureAQueryParameter();
			if (isFileCachingRequired) {
				configureAQueryCache(CACHE_DIRECTORY);
			}
			Logger.Log("<AQueryCache><getAqueryCacheInstance><Instantiated!!!>");
		}
		return aqueryCacheInstance;
	}
	
	/**
	 * Method to configure AQuery parameters.
	 */
	private static void configureAQueryParameter() {
		Logger.Log("<AQueryCache><configureAQueryParameter><ENTRY>");
		AjaxCallback.setNetworkLimit(NETWORK_LIMIT);
		BitmapAjaxCallback.setCacheLimit(CACHE_LIMIT);
		Logger.Log("<AQueryCache><configureAQueryParameter><RETURN>");

	}

	/**
	 * Method to configure AQuery Cache on to SD Card.
	 *
	 * @param cacheDirectoryName the cache directory name
	 */
	private static void configureAQueryCache(String cacheDirectoryName) {
		Logger.Log("<AQueryCache><configureAQueryCache><ENTRY>");
		File ext = Environment.getExternalStorageDirectory();
		File cacheDir = new File(ext, cacheDirectoryName);
		AQUtility.setCacheDir(cacheDir);
		Logger.Log("<AQueryCache><configureAQueryCache><RETURN>");
	}

	/**
	 * Method to clear the cache on low memory Clear all memory cached images
	 * when system is in low memory note that you can configure the max image
	 * cache count, see CONFIGURATION.
	 */
	public static void clearCacheOnLowMemory() {
		Logger.Log("<AQueryCache><clearCacheOnLowMemory><ENTRY>");
		BitmapAjaxCallback.clearCache();
		Logger.Log("<AQueryCache><clearCacheOnLowMemory><RETURN>");
	}

	/**
	 * Initiates AQuery cache clearning based on the predefined trigger size and
	 * target size.
	 *
	 * @param context the context
	 */
	public static void clearCacheBasedOnSize(Context context) {
		Logger.Log("<AQueryCache><clearCacheBasedOnSize(context)><ENTRY>");
		AQUtility.cleanCacheAsync(context, CACHE_CLEAR_TRIGGER_SIZE,
				CACHE_CLEAR_TARGET_SIZE);
		Logger.Log("<AQueryCache><clearCacheBasedOnSize(context)><RETURN>");

	}

	/**
	 * triggerSize - Trigger cache cleaning when size is larger than this.
	 * targetSize - Remove the least recently used files until cache size is
	 * less than this.
	 *
	 * @param context the context
	 * @param triggerSize the trigger size
	 * @param targetSize the target size
	 */
	public static void clearCacheBasedOnSize(Context context, long triggerSize,
			long targetSize) {
		Logger.Log("<AQueryCache><clearCacheBasedOnSize(context,triggerSize,targetSize)><ENTRY>");
		AQUtility.cleanCacheAsync(context, triggerSize, targetSize);
		Logger.Log("<AQueryCache><clearCacheBasedOnSize(context,triggerSize,targetSize)><RETURN>");

	}

	/**
	 * Method for converting megabyte to byte.
	 *
	 * @param megabyte the megabyte
	 * @return the long
	 */
	private static long megabyteToByteConvertor(int megabyte) {
		return (long) (megabyte * Math.pow(1024, 2));
	}
}
