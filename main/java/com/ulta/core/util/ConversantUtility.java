/**
 *
 * Copyright(c) ULTA, Inc. All Rights reserved.
 *
 *  ConversantTag to capture information regarding what customers view and purchase in the app
 */


package com.ulta.core.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.ulta.core.conf.WebserviceConstants;
import conversant.tagmanager.sdk.CNVRTagManager;
import conversant.tagmanager.sdk.CNVRTagSyncEvent;

public class ConversantUtility {


	public static CNVRTagManager sdk;
	public static CNVRTagSyncEvent.Builder builder;

	// Obtain the sdk singleton
	public static void startTag()
	{
		 sdk = CNVRTagManager.getSdk();
	}

	public static void  loginTag(String username)
	{
		username=username.toLowerCase();

		final CNVRTagSyncEvent event = new CNVRTagSyncEvent.Builder(WebserviceConstants.LOGIN_EVENTS, WebserviceConstants.LOGIN_GROUP)
					.withExtra("email", stringToMD5(username))
					.build();

			fireEvent(event);

	}

	public static void orderConfirmation(String orderId, StringBuffer productInfo, String orderTotal)
	{
		if(null!=orderId && null!=productInfo && null!=orderTotal) {
			final CNVRTagSyncEvent event = new CNVRTagSyncEvent.Builder(WebserviceConstants.ORDER_EVENTS, WebserviceConstants.ORDER_GROUP)
					.withExtra("dtmc_transaction_id", orderId) // order id
					.withExtra("dtm_items", productInfo.toString()) // sku;amount|sku;amount
					.withExtra("dtm_conv_val", orderTotal) // order value
					.build();

			fireEvent(event);
		}
	}


	public static void lauchApp()
	{
		final CNVRTagSyncEvent event = new CNVRTagSyncEvent.Builder(WebserviceConstants.APP_lAUNCH_EVENTS, WebserviceConstants.APP_lAUNCH_GROUP)
				.build();
		fireEvent(event);
	}

	public static void  fireEvent(CNVRTagSyncEvent event)
	{
		// Fire an event to Tag server
		if (null != sdk && null != event)
			sdk.fireEvent(event);
	}

	private static String stringToMD5(String userName) {

			// Convert MD5 Hash string

			try {
				if(null!=userName && userName.trim().length()>0) {
					MessageDigest msgDigt = MessageDigest.getInstance("MD5");
					byte[] messageDigest = msgDigt.digest(userName.getBytes());
					BigInteger number = new BigInteger(1, messageDigest);
					String hashtext = number.toString(16);
					// To make it  complete 32 chars.
					while (hashtext.length() < 32) {
						hashtext = "0" + hashtext;
					}
					return hashtext;
				}
				return "";

			}
			catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
	}

}
