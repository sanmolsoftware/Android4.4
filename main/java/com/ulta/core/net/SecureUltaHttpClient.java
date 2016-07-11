/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net;

import com.ulta.core.util.log.Logger;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;



/**
 * The Class SecureUltaHttpClient.
 */
public class SecureUltaHttpClient extends DefaultHttpClient {
	
	/** The SSL_CONNECTION_ERROR. */
	/*private static final String SSL_CONNECTION_ERROR = "Untrusted";*/
	
	/** The Default HTTP Socket. */
	private static final int HTTP_SOCKET = 80;
	
	/** The Default HTTP SSL Socket. */
	private static final int HTTP_SSL_SOCKET = 443;
	
	/** The secure ulta client key store. */
	private KeyStore secureUltaClientKeyStore;
	
	/**
	 * Instantiates a new secure ulta http client.
	 *
	 * @param keyStore the key store
	 */
	public SecureUltaHttpClient(KeyStore keyStore) {
		Logger.Log("[SecureUltaHttpClient]{SecureUltaHttpClient}(keyStore)<ETNRY>");
		secureUltaClientKeyStore = keyStore;
	}
	
	/**
	 * Method returns the connection factory.
	 *
	 * @return the client connection manager
	 */
	@Override
	protected ClientConnectionManager createClientConnectionManager() {
	    SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_SOCKET));
	    //TODO : Should we allow the socket to open if the keystore is null. 
	    if (secureUltaClientKeyStore != null) {
	    	Logger.Log("[SecureUltaHttpClient]{createClientConnectionManager}(secureUltaClientKeyStore != null)");
	        registry.register(new Scheme("https", ultaSSLSocketFactory(), HTTP_SSL_SOCKET));
	    } else {
	    	Logger.Log("[SecureUltaHttpClient]{createClientConnectionManager}(secureUltaClientKeyStore == null)");
	        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), HTTP_SSL_SOCKET));
	    }
	    Logger.Log("[SecureUltaHttpClient]{createClientConnectionManager}(getParams())"+getParams());
	    return new SingleClientConnManager(getParams(), registry);
	}
	
	/**
	 * Method returns a SSL Socket Factory.
	 *
	 * @return the sSL socket factory
	 */
	private SSLSocketFactory ultaSSLSocketFactory() {
		SSLSocketFactory sslFactory = null;
		try {
			sslFactory = new SSLSocketFactory(secureUltaClientKeyStore);
			sslFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
		} catch (KeyManagementException keyManagementException) {
			Logger.Log(keyManagementException);
		} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			Logger.Log(noSuchAlgorithmException);
		} catch (KeyStoreException keyStoreException) {
			Logger.Log(keyStoreException);
		} catch (UnrecoverableKeyException unrecoverableKeyException) {
			Logger.Log(unrecoverableKeyException);
		}
		 Logger.Log("[SecureUltaHttpClient]{ultaSSLSocketFactory}(sslFactory)");
		return sslFactory;
	}}
