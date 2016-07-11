/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.invoker;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.conf.types.ResponseParserFormat;
import com.ulta.core.conf.types.XmlParserIdentifier;
import com.ulta.core.net.handler.UltaHandler;

import java.util.Map;

import static com.ulta.core.conf.WebserviceConstants.APP_ENVIRONMENT;


/**
 * The Class InvokerParams.
 *
 * @param <T> the generic type
 * @author viva
 */
public class InvokerParams<T extends UltaBean > extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5660903874027821476L;

	/** The handler for handling ulta threads. */
	private Class<T> ultaBeanClazz;
	
	/** The handler for handling ulta threads. */
	private UltaHandler ultaHandler;
	/**
	 * Get, Post etc.
	 */
	private HttpMethod httpMethod;
	
	/** http, https. */
	private HttpProtocol httpProtocol;
	
	/** Service to invoker. */
	private String serviceToInvoke;
	
	/** Parameters required. */
	private Map<String, String> urlParameters;

	/** Akami Parameters required. */
	private Map<String, String> akamaiURLParameters;
	/**
	 * The type of parameter, use this only if specific content type is there.
	 */
	private String requestHeaderContentType;
	
	/** The byte array for storing the image data. */
	private byte[] imageByteArray;
	/**
	 *  The additional request information required.
	 */
	private String additionalRequestInformation;
	/**
	 * The format of response expected.
	 */
	private ResponseParserFormat responseParserFormat;
	/**
	 * The param that will be used to clear the user session.
	 */
	private boolean isUserSessionClearingRequired;
	/**
	 * The param that will be used to skip cookie handling
	 */
	private boolean isCookieHandlingSkip;

	/**
	 * The environment to which app is pointed
	 */
	private String appEnvironment;
	/**
	 * The customXMLParser.
	 */
	private XmlParserIdentifier customXMLParser;
	
	/**
	 * Gets the handler for handling ulta threads.
	 *
	 * @return the ultaBeanClazz
	 */
	public Class<T> getUltaBeanClazz() {
		return ultaBeanClazz;
	}
	
	/**
	 * Sets the handler for handling ulta threads.
	 *
	 * @param ultaBeanClazz the ultaBeanClazz to set
	 */
	public void setUltaBeanClazz(Class<T> ultaBeanClazz) {
		this.ultaBeanClazz = ultaBeanClazz;
	}
	
	/**
	 * Gets the handler for handling ulta threads.
	 *
	 * @return the ultaHandler
	 */
	public UltaHandler getUltaHandler() {
		return ultaHandler;
	}
	
	/**
	 * Sets the handler for handling ulta threads.
	 *
	 * @param ultaHandler the ultaHandler to set
	 */
	public void setUltaHandler(UltaHandler ultaHandler) {
		this.ultaHandler = ultaHandler;
	}
	
	/**
	 * Gets the get, Post etc.
	 *
	 * @return the httpMethod
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	
	/**
	 * Sets the get, Post etc.
	 *
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	/**
	 * Gets the http, https.
	 *
	 * @return the httpProtocol
	 */
	public HttpProtocol getHttpProtocol() {
		return httpProtocol;
	}
	
	/**
	 * Sets the http, https.
	 *
	 * @param httpProtocol the httpProtocol to set
	 */
	public void setHttpProtocol(HttpProtocol httpProtocol) {
		this.httpProtocol = httpProtocol;
	}
	
	/**
	 * Gets the service to invoker.
	 *
	 * @return the serviceToInvoke
	 */
	public String getServiceToInvoke() {
		return serviceToInvoke;
	}
	
	/**
	 * Sets the service to invoker.
	 *
	 * @param serviceToInvoke the serviceToInvoke to set
	 */
	public void setServiceToInvoke(String serviceToInvoke) {
		this.serviceToInvoke = serviceToInvoke;
	}
	
	/**
	 * Gets the parameters required.
	 *
	 * @return the urlParameters
	 */
	public Map<String, String> getUrlParameters() {
		return urlParameters;
	}
	
	/**
	 * Sets the parameters required.
	 *
	 * @param urlParameters the urlParameters to set
	 */
	public void setUrlParameters(Map<String, String> urlParameters) {
		this.urlParameters = urlParameters;
	}


	/**
	 * Akamai Data cache Gets the parameters required.
	 *
	 * @return the akamaiURLParameters
	 */
	public Map<String, String> getAkamaiURLParameters() {
		return akamaiURLParameters;
	}

	/**
	 * Sets the Akamai Data cache parameters required.
	 *
	 * @param akamaiURLParameters to set
	 */
	public void setAkamaiURLParameters(Map<String, String> akamaiURLParameters) {
		this.akamaiURLParameters = akamaiURLParameters;
	}

	/**
	 * Gets the type of parameter, use this only if specific content type is there.
	 *
	 * @return the requestHeaderContentType
	 */
	public String getRequestHeaderContentType() {
		return requestHeaderContentType;
	}
	
	/**
	 * Sets the type of parameter, use this only if specific content type is there.
	 *
	 * @param requestHeaderContentType the requestHeaderContentType to set
	 */
	public void setRequestHeaderContentType(String requestHeaderContentType) {
		this.requestHeaderContentType = requestHeaderContentType;
	}
	
	/**
	 * Gets the byte array for storing the image data.
	 *
	 * @return the imageByteArray
	 */
	public byte[] getImageByteArray() {
		return imageByteArray;
	}
	
	/**
	 * Sets the byte array for storing the image data.
	 *
	 * @param imageByteArray the imageByteArray to set
	 */
	public void setImageByteArray(byte[] imageByteArray) {
		this.imageByteArray = imageByteArray;
	}
	
	/**
	 * Gets the additional request information required.
	 *
	 * @return the additionalRequestInformation
	 */
	public String getAdditionalRequestInformation() {
		return additionalRequestInformation;
	}
	
	/**
	 * Sets the additional request information required.
	 *
	 * @param additionalRequestInformation the additionalRequestInformation to set
	 */
	public void setAdditionalRequestInformation(String additionalRequestInformation) {
		this.additionalRequestInformation = additionalRequestInformation;
	}
	
	/**
	 * Gets the format of response expected.
	 *
	 * @return the responseParserFormat
	 */
	public ResponseParserFormat getResponseParserFormat() {
		return responseParserFormat;
	}
	
	/**
	 * Sets the format of response expected.
	 *
	 * @param responseParserFormat the responseParserFormat to set
	 */
	public void setResponseParserFormat(ResponseParserFormat responseFormat) {
		this.responseParserFormat = responseFormat;
	}
	
	/**
	 * Checks if is user session clearing required.
	 *
	 * @return the isUserSessionClearingRequired
	 */
	public boolean isUserSessionClearingRequired() {
		return isUserSessionClearingRequired;
	}
	
	/**
	 * Sets the user session clearing required.
	 *
	 * @param isUserSessionClearingRequired the isUserSessionClearingRequired to set
	 */
	public void setUserSessionClearingRequired(boolean isUserSessionClearingRequired) {
		this.isUserSessionClearingRequired = isUserSessionClearingRequired;
	}


	/**
	 * @return the appEnvironment
	 */
	public String getAppEnvironment() {
		return appEnvironment != null? appEnvironment : APP_ENVIRONMENT;
	}

	/**
	 * @param appEnvironment the appEnvironment to set
	 */
	public void setAppEnvironment(String appEnvironment) {
		this.appEnvironment = appEnvironment;
	}

	/**
	 * @return the isCookieHandlingSkip
	 */
	public boolean isCookieHandlingSkip() {
		return isCookieHandlingSkip;
	}

	/**
	 * @param isCookieHandlingSkip the isCookieHandlingSkip to set
	 */
	public void setCookieHandlingSkip(boolean isCookieHandlingSkip) {
		this.isCookieHandlingSkip = isCookieHandlingSkip;
	}

	/**
	 * @return the customXMLParser
	 */
	public XmlParserIdentifier getCustomXMLParser() {
		return customXMLParser;
	}

	/**
	 * @param customXMLParser the customXMLParser to set
	 */
	public void setCustomXMLParser(XmlParserIdentifier customXMLParser) {
		this.customXMLParser = customXMLParser;
	}

	
	
	
	
}
