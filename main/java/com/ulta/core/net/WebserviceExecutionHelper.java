/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net;

import com.ulta.core.Ulta;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.ulta.core.conf.WebserviceConstants.isCookieHandlingRequired;
import static com.ulta.core.conf.types.AppEnvironment.PROD;
import static com.ulta.core.conf.types.HttpProtocol.http;
import static com.ulta.core.conf.types.HttpProtocol.https;
import static com.ulta.core.net.handler.HttpErrorHandler.handleHttpError;
import static com.ulta.core.util.Utility.stringNullEmptyValidator;

/**
 * The Class WebserviceExecutionHelper.
 * 
 * @author viva
 */
@SuppressWarnings("deprecation")
public abstract class WebserviceExecutionHelper extends WebserviceCookieHandler {

	/**
	 * Getting HTTP/HTTPS Response based on GET of POST.
	 * 
	 * @param httpRequestType
	 *            the http request type
	 * @param invokerParams
	 *            the invokerParams
	 * @return String
	 * @throws ClientProtocolException
	 *             the client protocol exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws KeyManagementException
	 *             the key management exception
	 * @throws UnrecoverableKeyException
	 *             the unrecoverable key exception
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws CertificateException
	 *             the certificate exception
	 * @throws KeyStoreException
	 *             the key store exception
	 * @throws UltaException
	 *             the ulta exception
	 */
	protected static InputStream getWebserviceResponse(
			HttpRequestBase httpRequestType,
			InvokerParams<UltaBean> invokerParams)
			throws ClientProtocolException, IOException,
			KeyManagementException, UnrecoverableKeyException,
			NoSuchAlgorithmException, CertificateException, KeyStoreException,
			UltaException {
		HttpClient httpClient = null;
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;
		InputStream responseStream = null;
		String applicationEnvironment = invokerParams.getAppEnvironment();
		boolean isUserSessionClearingRequired = invokerParams
				.isUserSessionClearingRequired();
		boolean isCookieHandlingSkip = invokerParams.isCookieHandlingSkip();

		if (httpRequestType != null) {
			if (https == invokerParams.getHttpProtocol()) {
				if (PROD.toString().equalsIgnoreCase(applicationEnvironment)) {
					httpClient = new SecureUltaHttpClient(UltaDataCache
							.getDataCacheInstance().getUltaSecureStore());
				} else {
					httpClient = getHttpClient();
					truster();
				}
			} else {
				httpClient = new DefaultHttpClient();
			}
			if (isCookieHandlingRequired && !isCookieHandlingSkip) {
				if (!cookieFirstTimePresent()) {
					((AbstractHttpClient) httpClient)
							.setCookieStore(new BasicCookieStore());
				} else {
					boolean isBagRequest = false;
					String callingService = invokerParams.getServiceToInvoke();
					String bagService = WebserviceConstants.GETMOBILECART_SERVICE;
					if (callingService.equals(bagService)
							&& callingService.length() == bagService.length()) {
						isBagRequest = true;
					}
					((AbstractHttpClient) httpClient)
							.setCookieStore(getCookieStore(
									isUserSessionClearingRequired, isBagRequest));
				}
				List<Cookie> cookies = ((AbstractHttpClient) httpClient)
						.getCookieStore().getCookies();
				handleCookie(cookies, false, isUserSessionClearingRequired);
			}

			if (null != Utility.retrieveFromSharedPreference(
					UltaConstants.CONNECTION_TIMEOUT, Ulta.getUltaInstance())
					&& !Utility
							.retrieveFromSharedPreference(
									UltaConstants.CONNECTION_TIMEOUT,
									Ulta.getUltaInstance()).trim().isEmpty()) {
				int connectionTimeOut = Integer.parseInt(Utility
						.retrieveFromSharedPreference(
								UltaConstants.RESPONSE_TIME,
								Ulta.getUltaInstance())) * 1000;
				HttpConnectionParams.setConnectionTimeout(
						httpClient.getParams(), connectionTimeOut);
			} else {
				HttpConnectionParams.setConnectionTimeout(
						httpClient.getParams(),
						WebserviceConstants.CONNECTION_TIMEOUT_IN_MILLI);
			}

			if (invokerParams.getHttpMethod() != HttpMethod.GET) {
				// for the first time appconfig web service is not called so
				// checking if there is value in shared preference.If not taking
				// default value 10000
				if (null != Utility.retrieveFromSharedPreference(
						UltaConstants.RESPONSE_TIME, Ulta.getUltaInstance())
						&& !Utility
								.retrieveFromSharedPreference(
										UltaConstants.RESPONSE_TIME,
										Ulta.getUltaInstance()).trim()
								.isEmpty()) {
					int responseTime = Integer.parseInt(Utility
							.retrieveFromSharedPreference(
									UltaConstants.RESPONSE_TIME,
									Ulta.getUltaInstance())) * 1000;
					HttpConnectionParams.setSoTimeout(httpClient.getParams(),
							responseTime);
				} else {
					HttpConnectionParams.setSoTimeout(httpClient.getParams(),
							WebserviceConstants.RESPONSE_TIMEOUT_IN_MILLI);
				}

			}

			httpResponse = httpClient.execute(httpRequestType);
			String httpError = handleHttpError(httpResponse);
			if (stringNullEmptyValidator(httpError)) {
				Logger.Log("[WebserviceExecutionHelper]{getWebserviceResponse}(http/https)<httpError>>"
						+ httpError + httpRequestType.getURI());
				if (!UltaDataCache.getDataCacheInstance().isOlapicProdDetails()) {
					throw new UltaException(Utility.httpDisplayError(httpError));
				}

			}
			httpEntity = httpResponse.getEntity();
			responseStream = httpEntity.getContent();
			if (isCookieHandlingRequired && !isCookieHandlingSkip) {
				if (httpEntity != null) {
					List<Cookie> cookies = ((AbstractHttpClient) httpClient)
							.getCookieStore().getCookies();
					handleCookie(cookies, true, isUserSessionClearingRequired);
				}
			}
		}
		return responseStream;
	}

	/**
	 * Gets the new HttpClient.
	 * 
	 * @return the getHttpClient
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws CertificateException
	 *             the certificate exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws KeyManagementException
	 *             the key management exception
	 * @throws UnrecoverableKeyException
	 *             the unrecoverable key exception
	 * @throws KeyStoreException
	 *             the key store exception
	 */
	private static HttpClient getHttpClient() throws NoSuchAlgorithmException,
			CertificateException, IOException, KeyManagementException,
			UnrecoverableKeyException, KeyStoreException {
		final KeyStore trustStore = KeyStore.getInstance(KeyStore
				.getDefaultType());
		trustStore.load(null, null);
		final SSLSocketFactory ultaSSLSocketFactory = new UltaSSLSocketFactory(
				trustStore);
		ultaSSLSocketFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		final HttpParams httpParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
		final SchemeRegistry schemaRegistry = new SchemeRegistry();
		schemaRegistry.register(new Scheme(http.toString(), PlainSocketFactory
				.getSocketFactory(), 80));
		schemaRegistry.register(new Scheme(https.toString(),
				ultaSSLSocketFactory, 443));
		final ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(
				httpParams, schemaRegistry);
		return new DefaultHttpClient(clientConnectionManager, httpParams);
	}

	/**
	 * Method for trusting Trust all hosts.
	 */
	private static void truster() {
		// Create a trust manager that does not validate certificate chains
		final TrustManager[] certTruster = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };
		try {
			final SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, certTruster, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (final Exception exception) {
			Logger.Log(exception);
			Utility.savelog("Exception: " + exception);
		}
	}

}
