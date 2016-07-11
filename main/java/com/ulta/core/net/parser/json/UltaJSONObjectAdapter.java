/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.json;

import com.ulta.core.bean.ResponseMessageBean;
import com.ulta.core.util.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * The Class UltaJSONObjectAdapter.
 *
 * @author viva
 */
public class UltaJSONObjectAdapter extends UltaJSONParserHelper{
	
	/** The Constant RESPONSE_MESSAGE. */
	private static final String RESPONSE_MESSAGE = "responseMessage";
	
	/** The Constant FAULT_MESSAGE. */
	private static final String FAULT_MESSAGE = "faultMessage";
	
	/** The Constant RESPONSE_HEADER. */
	private static final String RESPONSE_HEADER = "responseHeader";
	
	/** /** The Constant RESPONSE_OBJECT. */
	private static final String RESPONSE_OBJECT = "responseObject";
	
	/** The Constant RESPONSE_TYPE. */ 
	private static final String RESPONSE_TYPE = "responseType";
	
	/** The Constant RESPONSE_LIST. */
	private static final String RESPONSE_LIST = "list";
	
	/**
	 * Method to format JSON Response.
	 *
	 * @param jsonResponseString the json response string
	 * @return UltaWebserviceResponse
	 * @throws UltaJSONParserException the ulta json parser exception
	 */
	protected static UltaWebserviceResponse formatJSONResponse(
			String jsonResponseString) throws UltaJSONParserException {
		final UltaWebserviceResponse ultaWebserviceResponse = new UltaWebserviceResponse();
		JSONObject jsonResponseObject;
		JSONObject jsonResponseMessageObject;
		if (jsonResponseString != null) {
			Logger.Log("<UltaJSONObjectAdapter><createJSONObject><jsonResponseString><BEGIN>"+ jsonResponseString);
			try {
				if (jsonResponseString.contains(RESPONSE_MESSAGE)) {
					jsonResponseObject = new JSONObject(jsonResponseString).getJSONObject(RESPONSE_MESSAGE);
					ResponseMessageBean responseMessageBean =  populateResponseMessageBean(jsonResponseObject);
					ultaWebserviceResponse.setResponseMessageBean(responseMessageBean);
					Logger.Log("<UltaJSONObjectAdapter><createJSONObject><responseMessageBean.getList()>>"+ responseMessageBean.getList());
					if(!statusOnlyResponseIdentifier(responseMessageBean.getResponseType()) && !responseMessageBean.getList()){
						jsonResponseMessageObject = jsonResponseObject.getJSONObject(RESPONSE_OBJECT);
						ultaWebserviceResponse.setResponseMessage(jsonResponseMessageObject);
					}
					if(responseMessageBean.getList()){
						Logger.Log("<UltaJSONObjectAdapter><createJSONObject><responseMessageBean.getList()><INSIDE>");
						jsonResponseMessageObject = jsonResponseObject;
						ultaWebserviceResponse.setResponseMessage(jsonResponseMessageObject);
					}
					Logger.Log("<UltaJSONObjectAdapter><createJSONObject><responseMessageBean.getList()><INSIDE>");
				} else if (jsonResponseString.contains(FAULT_MESSAGE)) {
					invalidateStatusOnlyResponse();
					jsonResponseObject = new JSONObject(jsonResponseString).getJSONObject(FAULT_MESSAGE);
					ultaWebserviceResponse.setFaultMessage(jsonResponseObject);
				} else if (jsonResponseString.contains(RESPONSE_HEADER)){
					invalidateStatusOnlyResponse();
					Logger.Log("<UltaJSONObjectAdapter><createJSONObject><RESPONSE_HEADER><INSIDE>"+RESPONSE_HEADER);
					jsonResponseObject = new JSONObject(jsonResponseString);
					Logger.Log("<UltaJSONObjectAdapter><createJSONObject><new JSONObject(jsonResponseString)>>"+new JSONObject(jsonResponseString));
					ResponseMessageBean responseMessageBean =  populateResponseMessageBean(jsonResponseObject);
					ultaWebserviceResponse.setResponseMessageBean(responseMessageBean);
					jsonResponseMessageObject =  jsonResponseObject;
					ultaWebserviceResponse.setResponseMessage(jsonResponseObject);
				}
			} catch (JSONException jsonException) {
				throw new UltaJSONParserException(jsonException);
			}
		}
		return ultaWebserviceResponse;
	}
	
	/**
	 * Method for populating the.
	 *
	 * @param jsonResponseObject the json response object
	 * @return the response message bean
	 */
	private static ResponseMessageBean populateResponseMessageBean(JSONObject jsonResponseObject){
		ResponseMessageBean responseMessageBean = null;
		if(jsonResponseObject != null){
			responseMessageBean = new ResponseMessageBean();
			responseMessageBean.setResponseObject(jsonResponseObject.optString(RESPONSE_OBJECT));
			responseMessageBean.setResponseType(jsonResponseObject.optString(RESPONSE_TYPE));
			responseMessageBean.setList(jsonResponseObject.optBoolean(RESPONSE_LIST));
			Logger.Log("<UltaJSONObjectAdapter><populateResponseMessageBean><responseMessageBean>"+ responseMessageBean);
		}
		return responseMessageBean;
	}
	
	
}
