/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.json;

import com.ulta.core.conf.types.ResponseType;
import com.ulta.core.util.log.Logger;



/**
 * The Class UltaJSONParserHelper.
 *
 * @author viva
 */
public class UltaJSONParserHelper {
	
	/** The is status only response identifier. */
	protected static boolean isStatusOnlyResponseIdentifier = false;
	
	/**
	 * Helper method to format JSON.
	 *
	 * @param jsonToFormat the json to format
	 * @param valueToCheckFor the value to check for
	 * @return String
	 */
	/*private static String jsonStringFormatter(String jsonToFormat, String valueToCheckFor){
		return jsonToFormat.replace("{\""+valueToCheckFor+"\":", "").replace("}}", "}");
	}*/
	
	/**
	 * Method to check if there is status only present in the response message.
	 *
	 * @param responseType the response type
	 * @return true, if successful
	 */
	protected static boolean statusOnlyResponseIdentifier(String responseType){
		boolean isStatusOnlyResponse = false;
		if (responseType != null) {
			if (ResponseType.LoginResponse.toString().equalsIgnoreCase(responseType)
					|| ResponseType.Boolean.toString().equalsIgnoreCase(responseType)
					|| ResponseType.string.toString().equalsIgnoreCase(responseType)) {
				isStatusOnlyResponse = true;
			}
		}
		Logger.Log("<UltaJSONParserHelper><statusOnlyResponseIdentifier><isStatusOnlyResponse>"+ isStatusOnlyResponse);
		isStatusOnlyResponseIdentifier = isStatusOnlyResponse;
		return isStatusOnlyResponse;
	}
	/**
	 * Method for resetting the status only condition as
	 * it may cause issues with search.
	 */
	protected static void invalidateStatusOnlyResponse(){
		Logger.Log("<UltaJSONParserHelper><invalidateStatusOnlyResponse><isStatusOnlyResponseIdentifier()><ENTRY>"+isStatusOnlyResponseIdentifier);
		isStatusOnlyResponseIdentifier = false;
		Logger.Log("<UltaJSONParserHelper><invalidateStatusOnlyResponse><isStatusOnlyResponseIdentifier()><RETURN>"+isStatusOnlyResponseIdentifier);
	}

}
