/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



/**
 * The Class CryptoUtil.
 *
 * @author viva
 */
public class CryptoUtil {
	
	/** Algorithm for doing cryptography. */
	private static final String CRYPTO_ALGORITHM = "AES";
	
	/** The ecipher. */
	Cipher ecipher;
	
	/** The dcipher. */
	Cipher dcipher;

	/**
	 * Input a string that will be md5 hashed to create the key.
	 * 
	 * @return void, cipher initialized
	 */
	public CryptoUtil() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance(CRYPTO_ALGORITHM);
			keyGen.init(128);
			this.setupCrypto(keyGen.generateKey());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Instantiates a new crypto util.
	 *
	 * @param cryptoKey the crypto key
	 */
	public CryptoUtil(String cryptoKey) {
		SecretKeySpec skey = new SecretKeySpec(getMD5(cryptoKey),
				CRYPTO_ALGORITHM);
		this.setupCrypto(skey);
	}
	
	/**
	 * Method for setting up the crypto.
	 *
	 * @param key the new up crypto
	 */
	private void setupCrypto(SecretKey key) {
		// Create an 8-byte initialization vector
		byte[] iv = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
				0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		try {
			ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			// CBC requires an initialization vector
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	// Buffer used to transport the bytes from one stream to another
	/** The buf. */
	byte[] buf = new byte[1024];

	/**
	 * Input is a string to encrypt.
	 *
	 * @param plainText the plain text
	 * @return a Hex string of the byte array
	 */
	public String encrypt(String plainText) {
		String cipheredText = null;
		try {
			byte[] cipherText = ecipher.doFinal(plainText.getBytes("UTF-8"));
			cipheredText = this.byteToHex(cipherText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipheredText;
	}

	/**
	 * Method to decipher the same.
	 *
	 * @param inputStream the input stream
	 * @param outputStream the output stream
	 */
	public void decrypt(InputStream inputStream, OutputStream outputStream) {
		try {
			// Bytes read from in will be decrypted
			inputStream = new CipherInputStream(inputStream, dcipher);
			// Read in the decrypted bytes and write the cleartext to out
			int numRead = 0;
			while ((numRead = inputStream.read(buf)) >= 0) {
				outputStream.write(buf, 0, numRead);
			}
			outputStream.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Input encrypted String represented in HEX.
	 *
	 * @param hexCipherText the hex cipher text
	 * @return a string decrypted in plain text
	 */
	public String decrypt(String hexCipherText) {
		try {
			String plaintext = new String(dcipher.doFinal(this
					.hexToByte(hexCipherText)), "UTF-8");
			return plaintext;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Method for getting the MD5.
	 *
	 * @param input the input
	 * @return the m d5
	 */
	private static byte[] getMD5(String input) {
		try {
			byte[] bytesOfMessage = input.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(bytesOfMessage);
		} catch (Exception e) {
			return null;
		}
	}

	/** The Constant HEXES. */
	static final String HEXES = "0123456789ABCDEF";
	
	/**
	 * Method to convert a String to hex.
	 *
	 * @param raw the raw
	 * @return the string
	 */
	public static String byteToHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	
	/**
	 * Method to convert hex string to Byte.
	 *
	 * @param hexString the hex string
	 * @return the byte[]
	 */
	public static byte[] hexToByte(String hexString) {
		int len = hexString.length();
		byte[] ba = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			ba[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
					.digit(hexString.charAt(i + 1), 16));
		}
		return ba;
	}

}
