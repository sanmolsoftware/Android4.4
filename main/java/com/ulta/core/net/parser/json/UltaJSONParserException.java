/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.json;



/**
 * The Class UltaJSONParserException.
 *
 * @author viva
 */
public class UltaJSONParserException extends RuntimeException {
	/**
	 * SERVER_UNDER_MAINTENANCE
	 * <h1>Service Temporarily Unavailable</h1>
	 *	 <p>The server is temporarily unable to service your
	 * 	  request due to maintenance downtime or capacity
	 *    problems. Please try again later.
	 *	 </p>
	 */
	public static final String SERVER_UNDER_MAINTENANCE = "Server under maintenance, Please try again later.";
	
	/** HTTP_ERROR_503 503 Service Temporarily Unavailable. */
	public static final String HTTP_ERROR_503 = "503";
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7578348276831024631L;
	
	/**
	 * Instantiates a new ulta json parser exception.
	 *
	 * @param cause the cause
	 */
	public UltaJSONParserException(Exception cause) {
		super(cause);
	}
	
	/**
	 * Instantiates a new UltaJSONParserException.
	 * @param errorCode the code associated with the exception
	 * @param message the message associated with the exception
	 */
	public UltaJSONParserException(String errorCode, String message) {
		super(errorCode.concat("~").concat(message));
	}
	

}
