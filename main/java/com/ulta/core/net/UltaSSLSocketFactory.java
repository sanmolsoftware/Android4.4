/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



/**
 * A factory for creating UltaSSLSocketFactory objects.
 */
public class UltaSSLSocketFactory extends SSLSocketFactory {
	/**
	 * The sslContext.
	 */
	SSLContext sslContext = SSLContext.getInstance("TLS");

	/**
	 * Instantiates a new mySSLSocketFactory.
	 * 
	 * @param truststore the truststore
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyManagementException the key management exception
	 * @throws KeyStoreException the key store exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 */
	public UltaSSLSocketFactory(KeyStore truststore)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		super(truststore);

		final TrustManager tm = new X509TrustManager() {
			/**
			 * 
			 */
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {

			}
			/**
			 * 
			 */
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
			/**
			 * 
			 */
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	/**
	 * Creates a new UltaSSLSocket object.
	 *
	 * @param socket the socket
	 * @param host the host
	 * @param port the port
	 * @param autoClose the auto close
	 * @return the socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnknownHostException the unknown host exception
	 * @see org.apache.http.conn.ssl.SSLSocketFactory#createSocket(java.net.Socket,
	 * java.lang.String, int, boolean)
	 */
	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host,
				port, autoClose);
	}

	/**
	 * Creates a new UltaSSLSocket object.
	 *
	 * @return the socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see org.apache.http.conn.ssl.SSLSocketFactory#createSocket()
	 */
	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}
}
