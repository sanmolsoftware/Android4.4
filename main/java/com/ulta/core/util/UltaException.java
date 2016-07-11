/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util;



/**
 * The Class UltaException.
 *
 * @author viva
 */
public class UltaException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -225797855696240326L;
	
	/** Unsupported http method. */
//	public static final String COMMON_ERROR_TITLE = "Inconvenience Regretted";
	public static final String COMMON_ERROR_TITLE = "Sorry";  
	
	/** Unsupported http method. */
	public static final String UNSUPPORTED_HTTP_METHOD = "Unsupported HTTP Method";

	/** Network Error due to IOException. */
	public static final String NETWORK_ERROR_IO_EXCEPTION = "Network Unavailable";
	/**
	 * The error to be displayed if there is a connectivity issue.
	 * 
	 */
//	public static final String SERVICE_UNAVAILABLE = "Unable to connect to the server. Please check your internet connection and try again.";
//	public static final String SERVICE_UNAVAILABLE = "Service Temporarily Unavailable. Please try after sometime.";
	public static final String SERVICE_UNAVAILABLE = "Service Temporarily Unavailable due to server maintenance! Please try after some time.";
	/**
	 * The error to be displayed if there is a connectivity issue.
	 * 
	 */
//	public static final String SERVICE_UNAVAILABLE = "Unable to connect to the server. Please check your internet connection and try again.";
//	public static final String CLIENT_ERROR = "OOPS! Service Temporarily Unavailable";
	public static final String CLIENT_ERROR = SERVICE_UNAVAILABLE;
	
	/**
	 * The error to be displayed if there is a load failure.
	 * 
	 */
	public static final String LOAD_BROCHURE_FAILURE = "Could not load brochure";

	/**
	 * Instantiates a new UltaException.
	 * 
	 * @param message the message associated with the exception
	 */
	public UltaException(String message) {
		super(message);
	}
	/**
	 * Instantiates a new UltaException.
	 * @param errorCode the code associated with the exception
	 * @param message the message associated with the exception
	 */
	public UltaException(String errorCode, String message) {
		super(errorCode.concat("~").concat(message));
	}
	/**
	 * Instantiates a new UltaException.
	 * 
	 * @param message the message associated with the exception
	 * @param throwable the throwable exception.
	 */
	public UltaException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
