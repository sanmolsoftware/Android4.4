/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net;

import android.content.SharedPreferences;
import android.util.Log;

import com.ulta.core.Ulta;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.ulta.core.conf.UltaConstants.AMPERSAND_SYMBOL;
import static com.ulta.core.conf.UltaConstants.EMPTY_STRING;
import static com.ulta.core.conf.UltaConstants.EQUALS_SYMBOL;
import static com.ulta.core.conf.WebserviceConstants.OLAPIC;
import static com.ulta.core.conf.WebserviceConstants.POWER_REVIEWS_CONTEXT;
import static com.ulta.core.conf.WebserviceConstants.POWER_REVIEWS_WRITE_CONTEXT;
import static com.ulta.core.conf.WebserviceConstants.SHOP_THIS_LOOK;
import static com.ulta.core.conf.WebserviceConstants.SOLR_CONTEXT;
import static com.ulta.core.conf.WebserviceConstants.ULTA_ENVIRONMENT;
import static com.ulta.core.conf.types.HttpProtocol.http;
import static com.ulta.core.conf.types.HttpProtocol.https;
import static com.ulta.core.util.UltaException.UNSUPPORTED_HTTP_METHOD;



/**
 * The Class WebserviceExecutor.
 *
 * @author viva
 * Handling of execution of web service
 */
@SuppressWarnings("deprecation")
public class WebserviceExecutor extends WebserviceExecutionHelper {
	
	//ToDo : <ViVA> Exception to be defined properly : BEGIN
	/** The Constant CLIENT_PROTOCOL_EXCEPTION_MSG. */
	private static final String CLIENT_PROTOCOL_EXCEPTION_MSG = "CLIENTPROTOCOLEXCEPTION";
	
	/** The Constant IO_EXCEPTION_MSG. */
	private static final String IO_EXCEPTION_MSG = "IOEXCEPTION";
	
	/** The Constant UNSUPPORTED_ENCODING_EXCEPTION_MSG. */
	private static final String UNSUPPORTED_ENCODING_EXCEPTION_MSG = "UNSUPPORTEDENCODINGEXCEPTION";
	
	/** The Constant CONNECTION_TIMEOUT_EXCEPTION_MSG. */
	private static final String CONNECTION_TIMEOUT_EXCEPTION_MSG = "Network Unavailable";
	
	/** The Constant SOCKET_TIMEOUT_EXCEPTION_MSG. */
	private static final String SOCKET_TIMEOUT_EXCEPTION_MSG = "Network Unavailable";
	
	//ToDo : <ViVA> Exception to be defined properly : END
	/** Substitute the proper value of environment. */
	private static String appEnvironment = ULTA_ENVIRONMENT;
	
	/**
	 * Method for executing HTTP Web Service.
	 *
	 * @param httpMethod the http method
	 * @param serviceToInvoke the service to invoke
	 * @param urlParameters the url parameters
	 * @param imageByteArray the image byte array
	 * @param isUserSessionClearingRequired the is user session clearing required
	 * @return String
	 * @throws UltaException the ulta exception
	 */
//	public static String executeHttpWebservice(HttpMethod httpMethod,
//			String serviceToInvoke, Map<String, String> urlParameters, byte[] imageByteArray, boolean isUserSessionClearingRequired)
//			throws UltaException {
	public static InputStream executeHttpWebservice(InvokerParams<UltaBean> invokerParams)
			throws UltaException {
		
		InputStream responseInputStream = null;
		String url = null;
		
		UrlEncodedFormEntity urlEncodedFormEntity = null;
		String serviceToInvoke = invokerParams.getServiceToInvoke();
		Map<String, String> urlParameters = invokerParams.getUrlParameters();
		/*byte[] imageByteArray = invokerParams.getImageByteArray();*/
		String additionalInformation = invokerParams.getAdditionalRequestInformation();
		
		
			try {
				switch (invokerParams.getHttpMethod()) {
				case GET:
					url = createGetServiceUrl(serviceToInvoke, appEnvironment, urlParameters, http, additionalInformation);
					final HttpGet httpGet = new HttpGet(url);
					httpGet.setHeader("uak",WebserviceConstants.UAK);
					if(WebserviceConstants.isULTA_SITE_VALUE)
						httpGet.setHeader("ULTASITE",WebserviceConstants.ULTA_SITE_VALUE);
					responseInputStream = getWebserviceResponse(httpGet, invokerParams);
					break;
					
				case GET_OLAPIC:
					url = createGetServiceUrl(serviceToInvoke, appEnvironment, urlParameters, http, additionalInformation);
					final HttpGet httpGetOlapic = new HttpGet(url);
						httpGetOlapic.setHeader("Accept", "application/json");
						httpGetOlapic.setHeader("Content-Type","application/json");
					responseInputStream = getWebserviceResponse(httpGetOlapic, invokerParams);
					break;
					
				case POST:
					url = formUrlPart(serviceToInvoke, appEnvironment, http, additionalInformation);

					if(serviceToInvoke.equalsIgnoreCase(WebserviceConstants.PRODUCTDETAILS_SERVICE))
					{
						Map<String, String> urlAkamaiParameters = invokerParams.getAkamaiURLParameters();
						if(null!=url&& null!=urlAkamaiParameters)
							url=formAkamiUrlPart(url,urlAkamaiParameters);
					}

					final HttpPost httpPost = new HttpPost(url);
					httpPost.setHeader("uak",WebserviceConstants.UAK);
					if(WebserviceConstants.isULTA_SITE_VALUE)
						httpPost.setHeader("ULTASITE",WebserviceConstants.ULTA_SITE_VALUE);

					urlEncodedFormEntity = new UrlEncodedFormEntity(createPostServiceData(urlParameters));
					httpPost.setEntity(urlEncodedFormEntity);
					Logger.Log("<WebserviceExecutor><executeHttpWebservice><URI>>"+httpPost.getURI());
					responseInputStream = getWebserviceResponse(httpPost, invokerParams);
					break;
					
					
				case POST_OLAPIC:
					url = formUrlPart(serviceToInvoke, appEnvironment, http, additionalInformation);
					final HttpPost httpPostOlapic = new HttpPost(url);
					urlEncodedFormEntity = new UrlEncodedFormEntity(createPostServiceData(urlParameters));
					httpPostOlapic.setHeader("Accept", "application/json");
					httpPostOlapic.setEntity(urlEncodedFormEntity);
					Logger.Log("<WebserviceExecutor><executeHttpWebservice><URI>>"+httpPostOlapic.getURI());
					responseInputStream = getWebserviceResponse(httpPostOlapic, invokerParams);
					break;
					
				case MULTIPOST:
					url = formUrlPart(serviceToInvoke, appEnvironment, http,additionalInformation);
					final HttpPost httpMultiPost = new HttpPost(url);
					if(UltaDataCache.getDataCacheInstance().isOlapic() || UltaDataCache.getDataCacheInstance().isOlapicProdDetails()){
						httpMultiPost.setHeader("Accept", "application/json");
						}
					
					File file = new File(urlParameters.get("file"));
					final ContentBody cbFile = new FileBody(file, "image/jpeg");
					final StringBody caption = new StringBody(urlParameters.get("caption"));
//					final StringBody stream_uri = new StringBody(urlParameters.get("stream_uri"));
					
					final MultipartEntity multipartContent = new MultipartEntity();
		            multipartContent.addPart("caption", caption);
		            multipartContent.addPart("file", cbFile);
//		            multipartContent.addPart("stream_uri", stream_uri);
		            httpMultiPost.setEntity(multipartContent);
		            
		            responseInputStream = getWebserviceResponse(httpMultiPost, invokerParams);
		            Logger.Log("<WebserviceExecutor><executeHttpWebservice><MULTIPOST><IMAGEUPLOAD><RESPSTRING>"+ responseInputStream);
		            break;
				default:
					throw new UltaException(UNSUPPORTED_HTTP_METHOD);
				}
				Log.e("Webservice URL", url);
			} catch (ClientProtocolException clientProtocolException) {
				throw new UltaException(CLIENT_PROTOCOL_EXCEPTION_MSG,clientProtocolException);
			} catch (ConnectTimeoutException connectionTimeoutException) {
				throw new UltaException(CONNECTION_TIMEOUT_EXCEPTION_MSG,connectionTimeoutException);
			} catch (SocketTimeoutException socketTimeoutException) {
				throw new UltaException(SOCKET_TIMEOUT_EXCEPTION_MSG,socketTimeoutException);
			} catch (IOException ioException) {
				throw new UltaException(IO_EXCEPTION_MSG,ioException);
			} catch (KeyManagementException keyManagementException) {
				throw new UltaException(IO_EXCEPTION_MSG,keyManagementException);
			} catch (UnrecoverableKeyException unrecoverableKeyException) {
				throw new UltaException(IO_EXCEPTION_MSG,unrecoverableKeyException);
			} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
				throw new UltaException(IO_EXCEPTION_MSG,noSuchAlgorithmException);
			} catch (CertificateException certificateException) {
				throw new UltaException(IO_EXCEPTION_MSG,certificateException);
			} catch (KeyStoreException keyStoreException) {
				throw new UltaException(IO_EXCEPTION_MSG,keyStoreException);
			}
		
		return responseInputStream;
	}
	
	/**
	 * Method for executing HTTPS Web Service.
	 *
	 * @param httpMethod the http method
	 * @param serviceToInvoke the service to invoke
	 * @param urlParameters the url parameters
	 * @param additionalRequestInformation the additional request information
	 * @param isUserSessionClearingRequired the is user session clearing required
	 * @return String
	 * @throws UltaException the ulta exception
	 */
//	public static String executeHttpsWebservice(HttpMethod httpMethod,
//			String serviceToInvoke, Map<String, String> urlParameters, String additionalRequestInformation, boolean isUserSessionClearingRequired)
//			throws UltaException {
	public static InputStream executeHttpsWebservice(InvokerParams<UltaBean> invokerParams)
			throws UltaException {
		InputStream respInputStream = null;
		String url = null;
		UrlEncodedFormEntity urlEncodedFormEntity = null;
		
		String serviceToInvoke = invokerParams.getServiceToInvoke(); 
		Map<String, String> urlParameters = invokerParams.getUrlParameters(); 
		String additionalRequestInformation = invokerParams.getAdditionalRequestInformation(); 
		try {
			switch (invokerParams.getHttpMethod()) {
			case POST:
				url = formUrlPart(serviceToInvoke, appEnvironment, https, additionalRequestInformation);
				final HttpPost httpsPost = new HttpPost(url);
				httpsPost.setHeader("uak",WebserviceConstants.UAK);
				if(WebserviceConstants.isULTA_SITE_VALUE)
					httpsPost.setHeader("ULTASITE",WebserviceConstants.ULTA_SITE_VALUE);

				urlEncodedFormEntity = new UrlEncodedFormEntity(createPostServiceData(urlParameters));
				Log.d("urlEncodedFormEntity", ""+urlEncodedFormEntity);
				httpsPost.setEntity(urlEncodedFormEntity);

//				jsonResponseString = getWebserviceResponse(httpsPost, https, appEnvironment, isUserSessionClearingRequired, isCookieBasedSessionHandlingRequired);
				respInputStream = getWebserviceResponse(httpsPost, invokerParams);
				break;
			case GET:
				url = createGetServiceUrl(serviceToInvoke, appEnvironment, urlParameters, https, additionalRequestInformation);
				final HttpGet httpsGet = new HttpGet(url);
				httpsGet.setHeader("uak",WebserviceConstants.UAK);
				if(WebserviceConstants.isULTA_SITE_VALUE)
					httpsGet.setHeader("ULTASITE",WebserviceConstants.ULTA_SITE_VALUE);

//				responseString = getWebserviceResponse(httpsGet, https, appEnvironment,isUserSessionClearingRequired, isCookieBasedSessionHandlingRequired);
				respInputStream = getWebserviceResponse(httpsGet, invokerParams);
				break;
			default:
				throw new UltaException(UNSUPPORTED_HTTP_METHOD);
			}
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			throw new UltaException(UNSUPPORTED_ENCODING_EXCEPTION_MSG,unsupportedEncodingException);
		} catch (ClientProtocolException clientProtocolException) {
			throw new UltaException(CLIENT_PROTOCOL_EXCEPTION_MSG,clientProtocolException);
		} catch (ConnectTimeoutException connectionTimeoutException) {
			throw new UltaException(CONNECTION_TIMEOUT_EXCEPTION_MSG,connectionTimeoutException);
		} catch (SocketTimeoutException socketTimeoutException) {
			throw new UltaException(SOCKET_TIMEOUT_EXCEPTION_MSG,socketTimeoutException);
		} catch (IOException ioException) {
			throw new UltaException(IO_EXCEPTION_MSG,ioException);
		} catch (KeyManagementException keyManagementException) {
			throw new UltaException(IO_EXCEPTION_MSG,keyManagementException);
		} catch (UnrecoverableKeyException unrecoverableKeyException) {
			throw new UltaException(IO_EXCEPTION_MSG,unrecoverableKeyException);
		} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			throw new UltaException(IO_EXCEPTION_MSG,noSuchAlgorithmException);
		} catch (CertificateException certificateException) {
			throw new UltaException(IO_EXCEPTION_MSG,certificateException);
		} catch (KeyStoreException keyStoreException) {
			throw new UltaException(IO_EXCEPTION_MSG,keyStoreException);
		}
		return respInputStream;
	}
	
	
	/**
	 * Utility method for constructing the parameters for the web service request.
	 *
	 * @param urlToFire the url to fire
	 * @param environment the environment
	 * @param urlParamsMap the url params map
	 * @param httpProtocol the http protocol
	 * @param additionalRequestInformation the additional request information
	 * @return the string
	 */
	private static String createGetServiceUrl(String urlToFire,
			String environment, Map<String, String> urlParamsMap,
			HttpProtocol httpProtocol, String additionalRequestInformation) {
		StringBuffer urlBuffer = null;
		String formedUrl = null;
		if(null != urlParamsMap && !urlParamsMap.isEmpty()){
			urlBuffer = new StringBuffer(formUrlPart(urlToFire, environment,httpProtocol,additionalRequestInformation));
			final Iterator<Entry<String, String>> urlParamsIterator = urlParamsMap.entrySet().iterator();
			while (urlParamsIterator.hasNext()) {
				final Map.Entry<String, String> mapEnt = (Map.Entry<String, String>) urlParamsIterator.next();
				urlBuffer.append(mapEnt.getKey());
				urlBuffer.append(EQUALS_SYMBOL);
				urlBuffer.append(mapEnt.getValue());
				urlBuffer.append(AMPERSAND_SYMBOL);
			}
			
			urlBuffer.append("osName=" + WebserviceConstants.osName);
			urlBuffer.append(AMPERSAND_SYMBOL);
			urlBuffer.append("appVersion=" + WebserviceConstants.versionNumber);
			
//			if (isParam) {
//				urlBuffer.deleteCharAt(urlBuffer.length() - 1);
//			}
			formedUrl = urlBuffer.toString();
		}
		Logger.Log("<WebserviceExecutor><formedUrl><URL>>"+ formedUrl);
		return formedUrl;
	}
	
	/**
	 * Utility method for constructing the parameters for the web service request.
	 *
	 * @param urlParamsMap the url params map
	 * @return the list
	 */
	private static List<NameValuePair> createPostServiceData(Map<String, String> urlParamsMap) {
		List<NameValuePair> nameValuePairs = null;
		if(null != urlParamsMap && !urlParamsMap.isEmpty()){
			nameValuePairs = new ArrayList<NameValuePair>(urlParamsMap.size());
			for(String key : urlParamsMap.keySet()){
				nameValuePairs.add(new BasicNameValuePair(key, urlParamsMap.get(key)));
			}
		}
		nameValuePairs.add(new BasicNameValuePair("osName", WebserviceConstants.osName));
		nameValuePairs.add(new BasicNameValuePair("appVersion", WebserviceConstants.versionNumber));
		return nameValuePairs;
	}
	
	/**
	 * Method for getting the URL part alone.
	 *
	 * @param urlToFire the url to fire
	 * @param environment the environment
	 * @param httpProtocol the http protocol
	 * @param additionalRequestInformation the additional request information
	 * @return String
	 */
	private static String formUrlPart(String urlToFire, String environment,
			HttpProtocol httpProtocol, String additionalRequestInformation) {

		final String webserviceContext = (POWER_REVIEWS_CONTEXT
				.equalsIgnoreCase(additionalRequestInformation) || POWER_REVIEWS_WRITE_CONTEXT
				.equalsIgnoreCase(additionalRequestInformation)) || OLAPIC
				.equalsIgnoreCase(additionalRequestInformation) || SHOP_THIS_LOOK.equalsIgnoreCase(additionalRequestInformation) ? getWebserviceContext(
				httpProtocol, additionalRequestInformation)
				: getWebserviceContext(httpProtocol, environment,
				exceptionalCaseValidator(urlToFire));
		final String formedUrl = (new StringBuffer(webserviceContext)
				//Need to handle this part based on the get and post. As post requires no question mark
				.append(urlToFire)/*.append(QUESTION_MARK_SYMBOL)*/).toString();
		Logger.Log("<WebserviceExecutor><formUrlPart><formedUrlPart>>" + formedUrl);
		return formedUrl;
	 }
	/**
	 * Method for adding akamai cache in the URL part.
	 *
	 * @param urlToFire the url to fire
	 * @param urlParamsMap the input params
	* @return String
	 */
	private static String formAkamiUrlPart(String urlToFire,Map<String, String> urlParamsMap)
	{
		StringBuffer urlBuffer = new StringBuffer(urlToFire.toString());

		if(null != urlParamsMap && !urlParamsMap.isEmpty()) {

			final Iterator<Entry<String, String>> urlParamsIterator = urlParamsMap.entrySet().iterator();
			while (urlParamsIterator.hasNext()) {
				final Map.Entry<String, String> mapEnt = (Map.Entry<String, String>) urlParamsIterator.next();
				urlBuffer.append(mapEnt.getKey());
				urlBuffer.append(EQUALS_SYMBOL);
				urlBuffer.append(mapEnt.getValue());
				urlBuffer.append(AMPERSAND_SYMBOL);
			}
		  }

			urlBuffer.append("osName=" + WebserviceConstants.osName);
			urlBuffer.append(AMPERSAND_SYMBOL);
			urlBuffer.append("appVersion=" + WebserviceConstants.versionNumber);

			Logger.Log("<WebserviceExecutor><formAkamiUrlPart>" + urlBuffer.toString());
			return urlBuffer.toString();
	}
	
	

	/**
	 * Get the web service context.
	 *
	 * @param httpProtocol the http protocol
	 * @param additionalRequestInformation the additional request information
	 * @return String
	 */
	private static String getWebserviceContext(HttpProtocol httpProtocol,
		String additionalRequestInformation) {
		StringBuilder webserviceContextBuilder = (new StringBuilder(httpProtocol.toString()).append("://"));
		if(POWER_REVIEWS_CONTEXT.equalsIgnoreCase(additionalRequestInformation)){
			webserviceContextBuilder.append(POWER_REVIEWS_CONTEXT).append("/");
		}
		else if (POWER_REVIEWS_WRITE_CONTEXT.equalsIgnoreCase(additionalRequestInformation)){
			webserviceContextBuilder.append(POWER_REVIEWS_WRITE_CONTEXT).append("/");
		}else if (OLAPIC.equalsIgnoreCase(additionalRequestInformation)){
			webserviceContextBuilder = (new StringBuilder(httpProtocol.toString()).append(":"));
		} else if (SHOP_THIS_LOOK.equalsIgnoreCase(additionalRequestInformation)){
			webserviceContextBuilder = (new StringBuilder(httpProtocol.toString()));
			webserviceContextBuilder.append(":");
		}
		
		final String webserviceContext =   webserviceContextBuilder.toString();

		return webserviceContext;
	}
	
	/**
	 * Get the web service context.
	 *
	 * @param httpProtocol the http protocol
	 * @param environment the environment
	 * @param isExceptionalCase the is exceptional case
	 * @return String
	 */
	private static String getWebserviceContext(HttpProtocol httpProtocol,
			String environment, boolean isExceptionalCase) {
		
		SharedPreferences environmentSavedPref = Ulta.ultaInstance
                .getSharedPreferences("userdetails", 0);
 
        String serverAddress = environmentSavedPref.getString("serverAddress",
                WebserviceConstants.prodServerAddress);
        
        String serverContext = environmentSavedPref.getString("serverContext",
                WebserviceConstants.SERVER_CONTEXT);

		final String webserviceContext =  (new StringBuilder(httpProtocol.toString()).append("://")
				.append(serverAddress).append("/")		
				.append(isExceptionalCase ? EMPTY_STRING : serverContext).append(isExceptionalCase ? EMPTY_STRING : "/")).toString();

		return webserviceContext;
	}
	
	/**
	 * Method to check special cases.
	 *
	 * @param urlToFire the url to fire
	 * @return true, if successful
	 */
	private static boolean exceptionalCaseValidator(String urlToFire){
		boolean isSpecialCase = false;
		if(urlToFire.contains(SOLR_CONTEXT)){
			isSpecialCase = true;
		}

		return isSpecialCase;
	}
	
}
