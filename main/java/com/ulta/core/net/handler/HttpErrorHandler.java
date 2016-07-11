/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.handler;

import com.ulta.core.util.log.Logger;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Set;

import static com.ulta.core.conf.UltaConstants.TILDE_MARK_SYMBOL;
import static com.ulta.core.util.Utility.stringNullEmptyValidator;



/**
 * The Class HttpErrorHandler.
 *
 * @author viva
 * Class for handling http error at protocol level
 */
/*
 * Revision History
 * ----------------------------------------------------------------------------
 * Version | 	Date      		| Revision Comment
 * ----------------------------------------------------------------------------
 *    1.0  |   25-Jul-12 		| Handling for 4xx & 5xx errors
 */ 
public class HttpErrorHandler {

	/**
	 * Method for handling the http error.
	 *
	 * @param httpResponse the http response
	 * @return the string
	 */
	public static String handleHttpError(HttpResponse httpResponse){
		String httpError = null;
		if(httpResponse!=null){
			int httpErrorCode = httpResponse.getStatusLine().getStatusCode();
			Logger.Log("[HttpErrorHandler]{httpErrorHanlder}<httpErrorCode>>"+httpErrorCode);
			Set<Integer> error5xx = httpServerError5xx().keySet();
			Set<Integer> error4xx = httpClientError4xx().keySet();
			if(error5xx.contains(httpErrorCode)){
				httpError = httpServerError5xx().get(httpErrorCode);
			}
			if(error4xx.contains(httpErrorCode)){
				httpError = httpClientError4xx().get(httpErrorCode);
			}
			if(stringNullEmptyValidator(httpError))
				httpError = String.valueOf(httpErrorCode).concat(TILDE_MARK_SYMBOL).concat(httpError);
		}
		return httpError;
	}
	
	/**
	 * Server Error 5xx
	 * Response status codes beginning with the digit "5" indicate cases in which the server is aware
	 * that it has erred or is incapable of performing the request. Except when responding to a HEAD request,
	 * the server SHOULD include an entity containing an explanation of the error situation, and whether it is a
	 * temporary or permanent condition. User agents SHOULD display any included entity to the user.
	 * These response codes are applicable to any request method.
	 *
	 * @return the hash map
	 */
	private static HashMap<Integer, String> httpServerError5xx(){
		HashMap<Integer, String> serverErrorHash = new HashMap<Integer, String>();
		serverErrorHash.put(500, "Internal Server Error");
		serverErrorHash.put(501, "Not Implemented");
		serverErrorHash.put(502, "Bad Gateway");
		serverErrorHash.put(503, "Service Unavailable");
		serverErrorHash.put(504, "Gateway Timeout");
		serverErrorHash.put(505, "HTTP Version Not Supported");
		return serverErrorHash;
	}
	
	/**
	 * Client Error 4xx
	 * The 4xx class of status code is intended for cases in which the client seems to have erred. Except when responding
	 * to a HEAD request, the server SHOULD include an entity containing an explanation of the error situation, and whether
	 * it is a temporary or permanent condition. These status codes are applicable to any request method.
	 * User agents SHOULD display any included entity to the user.
	 *
	 * @return the hash map
	 */
	private static HashMap<Integer, String> httpClientError4xx(){
		HashMap<Integer, String> serverErrorHash = new HashMap<Integer, String>();
		serverErrorHash.put(400, "Bad Request");
		serverErrorHash.put(401, "Unauthorized");
		serverErrorHash.put(402, "Payment Required");
		serverErrorHash.put(403, "Forbidden");
		serverErrorHash.put(404, "Not Found");
		serverErrorHash.put(405, "Method Not Allowed");
		serverErrorHash.put(406, "Not Acceptable");
		serverErrorHash.put(407, "Proxy Authentication Required");
		serverErrorHash.put(408, "Request Timeout");
		serverErrorHash.put(409, "Conflict");
		serverErrorHash.put(410, "Gone");
		serverErrorHash.put(411, "Length Required");
		serverErrorHash.put(412, "Precondition Failed");
		serverErrorHash.put(413, "Request Entity Too Large");
		serverErrorHash.put(414, "Request-URI Too Long");
		serverErrorHash.put(415, "Unsupported Media Type");
		serverErrorHash.put(416, "Requested Range Not Satisfiable");
		serverErrorHash.put(417, "Expectation Failed");
		return serverErrorHash;
	}
}
