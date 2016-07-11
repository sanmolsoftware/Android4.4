/**
 *
 * Copyright(c) ULTA, Inc. All Rights reserved.
 *
 *
 */
package com.ulta.core.conf;

import com.ulta.core.util.caching.UltaDataCache;




/**
 * The Class UltaConstants.
 *
 * @author viva
 */
public class UltaConstants {

	/** If vo to string overriding is required make it true. */
	public static final boolean BEAN_LEGIBILITY_ENABLED = UltaDataCache.getDataCacheInstance()
			.isBeanLegibilityEnabled();

	/** The Constant ISDEBUG for enabling and disabling of the log. */
	public static final boolean ISDEBUG = UltaDataCache.getDataCacheInstance().isLogEnabled();

	public static final String CONTACT_US_EMAIL_SUBJECT = "Feedback from Android Ulta Beauty app";

	/**
	 * The Constant ampersand symbol.
	 */
	public static final String AMPERSAND_SYMBOL = "&";

	/** The constant for next line \n. */
	public static final String NEXT_LINE = "\n";

	/** The constant for question mark symbol. */
	public static final String QUESTION_MARK_SYMBOL = "?";

	/** The constant for asterisk mark symbol. */
	public static final String ASTERISK_MARK_SYMBOL = "*";

	/** The constant for tilde mark symbol. */
	public static final String TILDE_MARK_SYMBOL = "~";

	/** The constant for forward slash symbol. */
	public static final String FORWARD_SLASH_SYMBOL = "/";

	/** The constant for semicolon symbol. */
	public static final String SEMI_COLON_SYMBOL = ";";

	/** The constant for open brace symbol. */
	public static final String OPEN_BRACE_SYMBOL = "[";

	/** The constant for closing brace symbol. */
	public static final String CLOSING_BRACE_SYMBOL = "]";

	/** The constant for colon symbol. */
	public static final String COLON_SYMBOL = ":";

	/** The constant for cookie splitter symbol. */
	public static final String COOKIE_SPLITTER_SYMBOL = ": ";

	/** The constant for or mark symbol. */
	public static final String OR_SYMBOL = "or";

	/** The constant for equals symbol. */
	public static final String EQUALS_SYMBOL = "=";

	/** The constant for Empty. */
	public static final String EMPTY_STRING = "";

	/** The constant for Blank. */
	public static final String BLANK_STRING = " ";

	/** The constant for Deleting Text. */
	public static final String DELETING_PROGRESS_TEXT="Removing...";

	/** The constant for Updating Quantity. */
	public static final String UPDATING_PROGRESS_TEXT="Updating Quantity...";

	/** The constant for applying coupon code. */
	public static final String APPLYING_COUPON_CODE_PROGRESS_TEXT="Applying Coupon Code...";

	public static final String REMOVING_COUPON_CODE_PROGRESS_TEXT="Removing Coupon Code...";
	/** The constant for Loading Text. */
	public static final String LOADING_PROGRESS_TEXT = "Loading...";

	/** The constant for Removing Text. */
	public static final String REMOVING_PROGRESS_TEXT = "Removing...";

	public static final String COMMITTING_ORDER_TEXT = "Please wait.Your order is being prepared...";

	public static final String VERIFYING_ADDRESS = "Please wait.Your shipping address is being verified...";
	/** The constant for items to be fetched at a go. */
	public static final String HOW_MANY = "12";

	/** The constant for LOGGED_MAIL_ID. */
	public static final String REMEMBER_ME = "rememberMe";

	/** The constant for LOGGED_MAIL_ID. */
	public static final String EMAIL_OPT_IN = "emailOptIn";
	
	/** The constant for LOGGED_MAIL_ID. */
	public static final String RESPONSE_TIME = "responseTime";
	
	public static final String CONNECTION_TIMEOUT = "connectionTimeout";
	
//	public static final String IS_SCAN_CC = "isScanCC";

	/** The constant for REMEMBER_CLICKED. */
	public static final String REMEMBER_CLICKED = "clicked";

	/** The constant for Shared Preference Name. */
	public static final String PREFS_NAME = "UltaPrefsFile";

	/** The constant for LOGGED_MAIL_ID. */
	public static final String LOGGED_MAIL_ID = "loggedId";
	
	/** The constant for Reward Member. */
	public static final String REWARD_MEMBER = "rewardMember";
	

	public static final String IS_REWARD_MEMBER = "isRewardMember";

	public static final String BEAUTY_CLUB_NUMBER = "beautyClubNumber";

	//Ultamate Card Details
	public static final String ULTAMATE_CARD_TYPE = "ultaCardType";

	/** The Constant TOKEN_TYPE, used for Accounts. */
	public final static String TOKEN_TYPE = "com.ulta.core.account";

	/** The constant for Adding Free Gift. */
	public static final String ADD_FREE_GIFT = "Adding Free Gift...";
	/**
	 * The constant for email customer care
	 */
	//public static final String EMAIL_CUSTOMER_CARE = "guestmobility@ulta.com";
	public static final String EMAIL_CUSTOMER_CARE = "gethelp@ulta.com";


	/**
	 * The constant for email body
	 */
	public static final String SENT_FROM_ANDROID = "Sent from Android.";

	public static final int REQ_CODE_RELOGIN=10000;
}
