/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.ulta.core.bean.GooglePlacesBean;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ulta.core.conf.types.LogLevel.ERR;
import static com.ulta.core.net.parser.json.UltaJSONParserException.HTTP_ERROR_503;
import static com.ulta.core.net.parser.json.UltaJSONParserException.SERVER_UNDER_MAINTENANCE;

/**
 * The Class UltaJsonParser.
 * 
 * @author viva
 */
public class UltaJsonParser extends UltaJSONObjectAdapter {

	/**
	 * Method returns the parsed and populated bean of type UltaBean.
	 * 
	 * @param <T> the generic type
	 * @param jsonResponseString the json response string
	 * @param clazz the clazz
	 * @param additionalRequestInformation the additional request information
	 * @return <T extends UltaBean> T
	 * @throws UltaJSONParserException the ulta json parser exception
	 */
	public static <T extends UltaBean> T mapObjectFromJson(
			String jsonResponseString, Class<T> clazz,
			String additionalRequestInformation) throws UltaJSONParserException {

		T t = null;
		/*ResponseMessageBean responseMessageBean = null;
		FaultMessageBean faultMessageBean = null;*/

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		try {
			if (WebserviceConstants.ADDITIONAL_INFO_GOOGLE_API
					.equalsIgnoreCase(additionalRequestInformation)) {

				Logger.Log("<UltaJsonParser><mapObjectFromJson><jsonResponseString>>"+ jsonResponseString);
				JSONObject jsonObject = new JSONObject(jsonResponseString).getJSONObject("result");
				Logger.Log("<UltaJsonParser><mapObjectFromJson><additionalRequestInformation>>"+ additionalRequestInformation);
				GooglePlacesBean googlePlacesBean = new GooglePlacesBean();
				String placesUrl = jsonObject.optString("url");
				googlePlacesBean.setPlacesUrl(placesUrl);
				t = (T) googlePlacesBean;
			} else {
				// UltaWebserviceResponse ultaWebserviceJsonResponse =
				// formatJSONResponse(jsonResponseString);
				// responseMessageBean =
				// ultaWebserviceJsonResponse.getResponseMessageBean();
				if (jsonResponseString != null) {
					boolean isStatusOnlyResponse = isStatusOnlyResponseIdentifier;
					Logger.Log("<UltaJsonParser><mapObjectFromJson><isStatusOnlyResponse>>"
							+ isStatusOnlyResponse);
					// Logger.Log("<UltaJsonParser><mapObjectFromJson><UltaWebserviceJsonResponse.getResponseMessage()>>"+ultaWebserviceJsonResponse.getResponseMessage());
					// if (ultaWebserviceJsonResponse.getResponseMessage() !=
					// null || isStatusOnlyResponse) {
					// if(isStatusOnlyResponse){
					// UltaBean responseStatusBean = new UltaBean();
					// responseStatusBean.setResponseStatus(responseMessageBean.getResponseObject());
					// t = (T) responseStatusBean;
					// } else {
					Logger.Log("<UltaJsonParser><mapObjectFromJson><isStatusOnlyResponse>>"
							+ String.valueOf(false));
					t = gson.fromJson(jsonResponseString, clazz);
					// }
				}
				// }
				// if (ultaWebserviceJsonResponse.getFaultMessage() != null ) {
				// faultMessageBean =
				// gson.fromJson(ultaWebserviceJsonResponse.getFaultMessage().toString(),
				// FaultMessageBean.class);
				// Logger.Log("<UltaJsonParser><mapObjectFromJson><faultMessageBean>>"+faultMessageBean);
				// Logger.Log("<UltaJsonParser><mapObjectFromJson><NullPointerException>");
				// throw new
				// UltaJSONParserException(faultMessageBean.getErrorCode(),
				// faultMessageBean.getMessage());
				// }
			}
		} catch (JsonSyntaxException jsonSyntaxException) {
			Logger.Log("<UltaJsonParser><mapObjectFromJson><JsonSyntaxException>>"+ jsonSyntaxException, ERR);
			try{
			jsonResponseString="{\"count\":"+jsonResponseString+"}";
			t = gson.fromJson(jsonResponseString, clazz);
			}
			catch(JsonSyntaxException jsonSyntaxExcep)
			{
				throw new UltaJSONParserException("JSE", "JsonSyntaxException");
			}
		} catch (JSONException jsonException) {
			Logger.Log("<UltaJsonParser><mapObjectFromJson><JSONException>>"+ jsonException, ERR);
			throw new UltaJSONParserException("JSE", "JSONException");
		}
		if (t == null) {
			Logger.Log("<UltaJsonParser><mapObjectFromJson><SERVER_UNDER_MAINTENANCE>>"+ SERVER_UNDER_MAINTENANCE, ERR);
			throw new UltaJSONParserException(HTTP_ERROR_503,SERVER_UNDER_MAINTENANCE);
		}
		return t;
	}

}
