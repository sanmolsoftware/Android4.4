package com.ulta.core.net.parser.xml;

public class UltaXmlParserException extends RuntimeException{

	/**
	 * Instantiates a new xml parser exception.
	 *
	 * @param cause the cause
	 */
	public UltaXmlParserException(Exception cause) {
		super(cause);
	}
	
	/**
	 * Instantiates a new UltaXmlParserException.
	 * @param errorCode the code associated with the exception
	 * @param message the message associated with the exception
	 */
	public UltaXmlParserException(String errorCode, String message) {
		super(errorCode.concat("~").concat(message));
	}
}
