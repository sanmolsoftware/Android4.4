/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ulta.core.Ulta;
import com.ulta.core.bean.GoogleURLShortenBean;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static com.ulta.core.conf.UltaConstants.EQUALS_SYMBOL;
import static com.ulta.core.conf.UltaConstants.NEXT_LINE;
import static com.ulta.core.conf.UltaConstants.QUESTION_MARK_SYMBOL;
import static com.ulta.core.conf.WebserviceConstants.BUFFER_BLOCK_SIZE;
import static com.ulta.core.conf.WebserviceConstants.ECODING_UTF_8;
import static com.ulta.core.conf.WebserviceConstants.GOOGLE_URL_SHORTENING_SERVICE;
import static com.ulta.core.conf.WebserviceConstants.HEADER_NAME;
import static com.ulta.core.conf.WebserviceConstants.HEADER_VALUE_JSON;
import static com.ulta.core.conf.WebserviceConstants.WEBSERVICES_FLAG;
import static com.ulta.core.conf.WebserviceConstants.WEBSERVICES_SERVER_ADDRESS;

/**
 * The Class WebserviceUtility.
 * 
 * @author viva
 */
@SuppressWarnings("deprecation")
public class WebserviceUtility {

	/**
	 * Gets the image url.
	 * 
	 * @param imageName
	 *            the image name
	 * @param imageContext
	 *            the image context
	 * @param environment
	 *            the environment
	 * @return the image url
	 */
	public static String getImageUrl(String imageName, String imageContext,
			String environment) {
		final String formedImageUrl;
		// Logger.Log("<WebserviceUtility><getImageUrl><imageName>>"+imageName);
		// Logger.Log("<WebserviceUtility><getImageUrl><imageContext>>"+imageContext);
		// Logger.Log("<WebserviceUtility><getImageUrl><environment>>"+environment);
		formedImageUrl = (new StringBuilder(getImagesContext())
				.append("Images").append("/").append(imageContext).append("/")
				.append(imageName)).toString();
		;
		// Logger.Log("<WebserviceUtility><getImageUrl><webserviceContext>>"+formedImageUrl);
		return formedImageUrl;
	}

	/**
	 * Method to convert Object to JSON String.
	 * 
	 * @param ultaBean
	 *            the ulta bean
	 * @return <String> jsonString
	 */
	public static String convertUltaBeanToJSON(UltaBean ultaBean) {
		String jsonString = null;
		// Logger.Log("<WebserviceUtility><convertUltaBeanToJSON><ultaBean>>"+
		// ultaBean);
		if (ultaBean != null) {
			jsonString = new GsonBuilder().create().toJson(ultaBean);
		}
		Logger.Log("<WebserviceUtility><convertUltaBeanToJSON><jsonString>>"
				+ jsonString);
		return jsonString;
	}

	/**
	 * Method for getting the context for images.
	 * 
	 * @return the images context
	 */
	private static String getImagesContext() {
		return getUrlBasedOnService(null, null, null);
	}

	/**
	 * Method for getting the context for webservices.
	 * 
	 * @param serviceToInvoke
	 *            the service to invoke
	 * @return the webservices context
	 */
	private static String getWebservicesContext(String serviceToInvoke) {
		return getUrlBasedOnService(serviceToInvoke, null, WEBSERVICES_FLAG);
	}

	/**
	 * Method for getting the URL part alone.
	 * 
	 * @param serviceToInvoke
	 *            the service to invoke
	 * @param environment
	 *            the environment
	 * @param conditionParam
	 *            the condition param
	 * @return String
	 */
	private static String getUrlBasedOnService(String serviceToInvoke,
			String environment, String conditionParam) {
		final String formedUrlBasedOnService;
		SharedPreferences environmentSavedPref = Ulta.ultaInstance
				.getSharedPreferences("userdetails", 0);
		String serverContext = environmentSavedPref.getString("serverContext",
				WebserviceConstants.SERVER_CONTEXT);
		final StringBuilder urlBasedOnServiceBuilder = new StringBuilder(
				HttpProtocol.http.toString()).append("://")
				.append(WEBSERVICES_SERVER_ADDRESS).append("/");
		if (WEBSERVICES_FLAG.equalsIgnoreCase(conditionParam)) {
			urlBasedOnServiceBuilder.append(serverContext).append("/")
					.append(serviceToInvoke).append(QUESTION_MARK_SYMBOL);
		}
		formedUrlBasedOnService = urlBasedOnServiceBuilder.toString();
		// Logger.Log("<WebserviceUtility><getUrlBasedOnService><formedUrlBasedOnService>>"+formedUrlBasedOnService);
		return formedUrlBasedOnService;
	}

	/**
	 * Method for getting the formed url for News.
	 * 
	 * @param serviceToInvoke
	 *            the service to invoke
	 * @param newsId
	 *            the news id
	 * @return the formed url for news
	 */
	public static String getFormedUrlForNews(String serviceToInvoke,
			String newsId) {
		final String formedUrlForNews;
		formedUrlForNews = (new StringBuilder(
				getWebservicesContext(serviceToInvoke)).append("newsId")
				.append(EQUALS_SYMBOL).append(newsId)).toString();
		// Logger.Log("<WebserviceUtility><getFormedUrlForNews><formedUrlForNews>>"+
		// formedUrlForNews);
		return formedUrlForNews;
	}

	/**
	 * Method for getting the dimensions of image.
	 * 
	 * @param widthOfImage
	 *            the width of image
	 * @param heightOfImage
	 *            the height of image
	 * @return <String>
	 */
	public static String formImageDimensions(int widthOfImage, int heightOfImage) {
		return String.valueOf(widthOfImage).concat("x")
				.concat(String.valueOf(heightOfImage));
	}

	/**
	 * Method for getting the image folder Address.
	 * 
	 * @param folderName
	 *            the folder name
	 * @param widthOfImage
	 *            the width of image
	 * @param heightOfImage
	 *            the height of image
	 * @return <String>
	 */
	public static String formImageFolderAddress(String folderName,
			int widthOfImage, int heightOfImage) {
		return folderName.concat("/").concat(
				formImageDimensions(widthOfImage, heightOfImage));
	}

	/**
	 * Method to encodeURL parameters
	 * 
	 * @param paramToEncode
	 * @return
	 */
	public static String encodeURLParameter(String paramToEncode) {
		String encodedString = null;
		try {
			encodedString = java.net.URLEncoder.encode(
					paramToEncode.toString(), "ISO-8859-1");
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			unsupportedEncodingException.printStackTrace();
		}
		return encodedString;
	}

	/**
	 * Utility method for converting the responseStream to String.
	 * 
	 * @param responseInputStream
	 *            the response input stream
	 * @return String
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String formatResponseToString(InputStream responseInputStream)
			throws IOException {
		String responseString = null;
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(responseInputStream);
			final BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader, BUFFER_BLOCK_SIZE);
			final StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line + NEXT_LINE);
			}
			bufferedReader.close();
			responseString = sb.toString();
		} catch(Exception e)
		{
			Logger.Log("[WebserviceUtility]{formatResponseToString}");
		}
		finally {
			inputStreamReader.close();
		}
		Logger.Log("<WebserviceExecutionHelper><formatResponseToString><responseString>>"
				+ responseString);
		if (UltaDataCache.getDataCacheInstance().isFileLoggingEnabled()) {
			Logger.writeLog(responseString);
		}
		if (UltaDataCache.getDataCacheInstance().isOlapic()) {
			UltaDataCache.getDataCacheInstance().setOlapicResponse(
					responseString);
		}

		if (UltaDataCache.getDataCacheInstance().isOlapicProdDetails()) {
			UltaDataCache.getDataCacheInstance().setOlapicProdDetailsResponse(
					responseString);
		}
		return responseString;
	}

	/**
	 * Method for serializing ulta bean to JSON
	 * 
	 * @param ultaBeanObj
	 * @return
	 */
	public static String jsonMarshller(Object obj) {
		return new GsonBuilder().create().toJson(obj);
	}

	/**
	 * Method to switch the protocol based on the enabling of the secure calls.
	 * 
	 * @return
	 */
	public static HttpProtocol securityEnabler() {

		SharedPreferences httpPreferences = Ulta.ultaInstance
				.getSharedPreferences("userdetails", 0);

		boolean isSecurityEnabled = httpPreferences.getBoolean(
				"isSecurityEnabled", true);

		HttpProtocol httpProtocol = HttpProtocol.http;
		if (isSecurityEnabled) {
			httpProtocol = HttpProtocol.https;
		}
		return httpProtocol;
	}

	/**
	 * 
	 * @param longUrl
	 * @return
	 * @throws UltaException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws ParseException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 */
	public static String urlShorten(String longUrl) throws UltaException {
		String shortUrl = longUrl;
		if (Utility.stringNullEmptyValidator(longUrl)) {
			try {
				// Logger.Log("[WebserviceUtility]{urlShorten}(longUrl)><longUrl>>"+
				// longUrl);
				final HttpPost httpsPost = new HttpPost(
						GOOGLE_URL_SHORTENING_SERVICE);
				// Logger.Log("[WebserviceUtility]{urlShorten}(longUrl)><GOOGLE_URL_SHORTENING_SERVICE>>"
				// + GOOGLE_URL_SHORTENING_SERVICE);
				httpsPost.setHeader(HEADER_NAME, HEADER_VALUE_JSON);
				StringEntity googleLongUrlEntity = new StringEntity(
						getURLShortenerRequestParam(longUrl), ECODING_UTF_8);
				httpsPost.setEntity(googleLongUrlEntity);
				// HttpClient httpsClient = WebserviceExecutor.getHttpClient();
				// WebserviceExecutor.truster();
				// HttpResponse response = httpsClient.execute(httpsPost);
				// HttpEntity entity = response.getEntity();
				// String jsonResponseString = EntityUtils.toString(entity);
				String jsonResponseString = EntityUtils
						.toString(new DefaultHttpClient().execute(httpsPost)
								.getEntity());
				// Logger.Log("[WebserviceUtility]{urlShorten}(longUrl)><jsonResponseString>>"
				// + jsonResponseString);
				if (Utility.stringNullEmptyValidator(jsonResponseString)) {
					shortUrl = parseJSONResponse(GoogleURLShortenBean.class,
							jsonResponseString);
				}
			} catch (UnsupportedEncodingException unsupportedEncodingException) {
				Logger.Log("[WebserviceUtility]{urlShorten}(longUrl)><UnsupportedEncodingException>");
				throw new UltaException("UnsupportedEncodingException",
						unsupportedEncodingException);
			} catch (ClientProtocolException clientProtocolException) {
				Logger.Log("[WebserviceUtility]{urlShorten}(longUrl)><ClientProtocolException>");
				throw new UltaException("ClientProtocolException",
						clientProtocolException);
			} catch (IOException ioException) {
				Logger.Log("[WebserviceUtility]{urlShorten}(longUrl)><IOException>");
				throw new UltaException("IOException", ioException);
			}/*
			 * catch (KeyManagementException keyManagementException) {
			 * Logger.Log
			 * ("[WebserviceUtility]{urlShorten}(longUrl)><KeyManagementException>"
			 * ); throw new
			 * UltaException("KeyManagementException",keyManagementException); }
			 * catch (UnrecoverableKeyException unrecoverableKeyException) {
			 * Logger.Log(
			 * "[WebserviceUtility]{urlShorten}(longUrl)><UnrecoverableKeyException>"
			 * ); throw new
			 * UltaException("UnrecoverableKeyException",unrecoverableKeyException
			 * ); } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			 * Logger.Log(
			 * "[WebserviceUtility]{urlShorten}(longUrl)><NoSuchAlgorithmException>"
			 * ); throw new
			 * UltaException("NoSuchAlgorithmException",noSuchAlgorithmException
			 * ); } catch (CertificateException certificateException) {
			 * Logger.Log
			 * ("[WebserviceUtility]{urlShorten}(longUrl)><CertificateException>"
			 * ); throw new
			 * UltaException("CertificateException",certificateException); }
			 * catch (KeyStoreException keyStoreException) { Logger.Log(
			 * "[WebserviceUtility]{urlShorten}(longUrl)><KeyStoreException>");
			 * throw new UltaException("KeyStoreException",keyStoreException); }
			 */
			return shortUrl;
		} else {
			throw new UltaException("No Shortening URL Found");
		}
	}

	/**
	 * Again a parsing option
	 * 
	 * @param responseBeanClazz
	 * @param jsonResponseString
	 * @return
	 */
	private static String parseJSONResponse(
			Class<? extends UltaBean> responseBeanClazz,
			String jsonResponseString) {
		UltaBean googleUrlShortenedResponseBean = null;
		googleUrlShortenedResponseBean = instantiateGson().fromJson(
				jsonResponseString, responseBeanClazz);
		GoogleURLShortenBean googleURLShortenBean = (GoogleURLShortenBean) googleUrlShortenedResponseBean;
		return googleURLShortenBean.getShortUrl();
	}

	/**
	 * 
	 * @param longUrl
	 * @return <String>
	 */
	private static String getURLShortenerRequestParam(String longUrl) {
		StringBuilder sbuilder = new StringBuilder("{\"longUrl\": \"").append(
				longUrl).append("\"}");
		String requestPart = sbuilder.toString();
		Logger.Log("[WebserviceUtility]{urlShorten}(longUrl)><requestPart>>"
				+ requestPart);
		return requestPart;
	}

	/**
	 * Utility method for instantiating Gson.
	 * 
	 * @return Gson
	 */
	private static Gson instantiateGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson;
	}

}
