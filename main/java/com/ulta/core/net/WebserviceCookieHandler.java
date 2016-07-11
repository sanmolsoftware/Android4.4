/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net;

import android.util.Log;

import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ulta.core.conf.UltaConstants.CLOSING_BRACE_SYMBOL;
import static com.ulta.core.conf.UltaConstants.COOKIE_SPLITTER_SYMBOL;
import static com.ulta.core.conf.UltaConstants.EMPTY_STRING;
import static com.ulta.core.conf.UltaConstants.OPEN_BRACE_SYMBOL;
import static com.ulta.core.conf.UltaConstants.TILDE_MARK_SYMBOL;
import static com.ulta.core.conf.WebserviceConstants.AO_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.COMMENT_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.DOMAIN_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.DYN_USER_CONFIRM_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.DYN_USER_ID_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.EXPIRES_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.EXPIRY_DATE;
import static com.ulta.core.conf.WebserviceConstants.MAX_AGE_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.NAME_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.PATH_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.SECURE_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.SESSION_ID_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.VALUE_COOKIE;
import static com.ulta.core.conf.WebserviceConstants.VERSION_COOKIE;
import static com.ulta.core.util.Utility.stringNullEmptyValidator;

/**
 * The Class WebserviceCookieHandler.
 *
 * @author viva
 * @since 10Aug12 Class for dong the custom handling of cookies.
 */
public class WebserviceCookieHandler {

	/**
	 * Method for handling the of the cookie information for request as well as
	 * response.
	 *
	 * @param cookies
	 *            the cookies
	 * @param isPersistenceRequired
	 *            the is persistence required
	 * @param isUserPersistenceRequired
	 */
	protected static void handleCookie(List<Cookie> cookies,
			boolean isPersistenceRequired, boolean isUserSessionClearingRequired) {
		if (cookies != null) {
			if (cookies.isEmpty()) {
				// Logger.Log("[WebserviceExecutionHelper]{handleCookie}()<NoCookies>>");
			} else {
				// Logger.Log("[WebserviceExecutionHelper]{handleCookie}()<BEGIN>>");
				String stringCookie = null;
				String nameCookie = null;
				Date expiryCookie = null;
				/* String valueCookie = null; */
				for (Cookie cooks : cookies) {
					stringCookie = cooks.toString();
					nameCookie = cooks.getName();
					expiryCookie = cooks.getExpiryDate();
					/* valueCookie = cooks.getValue(); */
					// Logger.Log("[WebserviceExecutionHelper]{handleCookie}()<STRING>"
					// + stringCookie);
					// Logger.Log("[WebserviceExecutionHelper]{handleCookie}()  <NAME>"
					// + nameCookie);
					// Logger.Log("[WebserviceExecutionHelper]{handleCookie}() <VALUE>"
					// + valueCookie);
					// String cookieString = cookies.get(i).getName() + "=" +
					// cookies.get(i).getValue() + "; domain=" +
					// cookies.get(i).getDomain();
					if (isPersistenceRequired) {
						stringCookie = isUserSessionClearingRequired ? null
								: stringCookie;
						if (SESSION_ID_COOKIE.equalsIgnoreCase(nameCookie)) {
							persistCookieInfo(SESSION_ID_COOKIE, stringCookie);
						}
						if (DYN_USER_ID_COOKIE.equalsIgnoreCase(nameCookie)) {
							persistCookieInfo(DYN_USER_ID_COOKIE, stringCookie);
						}
						if (DYN_USER_CONFIRM_COOKIE
								.equalsIgnoreCase(nameCookie)) {
							persistCookieInfo(DYN_USER_CONFIRM_COOKIE,
									stringCookie);
						}
						if (AO_COOKIE.equalsIgnoreCase(nameCookie)) {
							persistCookieInfo(AO_COOKIE, stringCookie);
							persistCookieInfo(EXPIRY_DATE,
									expiryCookie.toString());
						}
					}
				}
				// Logger.Log("[WebserviceExecutionHelper]{getWebserviceResponse}(http/https)<END>>");
			}
		} else {
			// Logger.Log("[WebserviceExecutionHelper]{getWebserviceResponse}(http/https)<NoCookies><NULL>");
		}
	}

	/**
	 * Method for checking the validity of a given cookie based on the Validity
	 * mentioned in the web services constants.
	 *
	 * @param cookieName
	 *            the cookie name
	 * @param expiryTime
	 *            the expiry time
	 */
	protected static void cookieValiditator(String cookieName, int expiryTime) {
		if (cookieName != null && cookieName.trim().length() > 0) {
			if (SESSION_ID_COOKIE.equalsIgnoreCase(cookieName)) {
				// TODO : Check with the 30 min validity
				// Current time - WebserviceConstants.SESSION_ID_COOKIE_EXPIRY
				// <= 0 then invalid else valid
			}
			if (DYN_USER_ID_COOKIE.equalsIgnoreCase(cookieName)) {
				// TODO : Check with the 30 days validity
				// Current time - WebserviceConstants.DYN_USER_ID_COOKIE_EXPIRY
				// <= 0 then invalid else valid
			}
			if (DYN_USER_CONFIRM_COOKIE.equalsIgnoreCase(cookieName)) {
				// TODO : Check with the 30 days validity
				// Current time -
				// WebserviceConstants.DYN_USER_CONFIRM_COOKIE_EXPIRY <= 0 then
				// invalid else valid
			}
		}
	}

	/**
	 * The method to parse the string from cookie.
	 *
	 * @param cookieString
	 *            the cookie string
	 * @return BasicClientCookie
	 * @throws UltaException
	 *             the ulta exception
	 */
	protected static BasicClientCookie basicClientCookieGenerator(
			String cookieString) throws UltaException {
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<ENTRY>");
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<cookieString>>"+cookieString);
		HashMap<String, String> cookieMap = cookieMapFormatter(cookieString);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<cookieMap>>"+cookieMap);
		for (String x : cookieMap.keySet()) {
			// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<x>><"+x+"><>"+cookieMap.get(x));
		}
		String exceptionMessage = null;
		BasicClientCookie cookie = null;
		String nameCookie = cookieMap.get(NAME_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<NAME_COOKIE>> "+NAME_COOKIE+" <> "+nameCookie);
		String valueCookie = cookieMap.get(VALUE_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<VALUE_COOKIE>> "+VALUE_COOKIE+" <> "+valueCookie);
		String secureCookie = cookieMap.get(SECURE_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<SECURE_COOKIE>> "+SECURE_COOKIE+" <> "+secureCookie);
		String expiresCookie = cookieMap.get(EXPIRES_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<EXPIRES_COOKIE>> "+EXPIRES_COOKIE+" <> "+expiresCookie);
		String maxAgeCookie = cookieMap.get(MAX_AGE_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<MAX_AGE_COOKIE>> "+MAX_AGE_COOKIE+" <> "+maxAgeCookie);
		String domainCookie = cookieMap.get(DOMAIN_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<DOMAIN_COOKIE>> "+DOMAIN_COOKIE+" <> "+domainCookie);
		String pathCookie = cookieMap.get(PATH_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<PATH_COOKIE>> "+PATH_COOKIE+" <> "+pathCookie);
		String commentCookie = cookieMap.get(COMMENT_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<COMMENT_COOKIE>> "+COMMENT_COOKIE+" <> "+commentCookie);
		String versionCookie = cookieMap.get(VERSION_COOKIE);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<COMMENT_COOKIE>> "+VERSION_COOKIE+" <> "+versionCookie);

		if (stringNullEmptyValidator(nameCookie)) {
			if (stringNullEmptyValidator(valueCookie)) {
				cookie = new BasicClientCookie(nameCookie, valueCookie);
			} else {
				exceptionMessage = "Invalid Cookie: Missing Value";
			}
		} else {
			exceptionMessage = "Invalid Cookie: Missing Name";
		}
		if (stringNullEmptyValidator(secureCookie)) {
			cookie.setSecure(Boolean.valueOf(secureCookie));
		}
		if (stringNullEmptyValidator(expiresCookie)) {
			Date expiryDate = Utility.cookieDateUtility(expiresCookie);// DateFormat.getDateTimeInstance(DateFormat.).parse(paramValue);
			cookie.setExpiryDate(expiryDate);
		}
		if (stringNullEmptyValidator(maxAgeCookie)) {
			long maxAge = Long.parseLong(maxAgeCookie);
			Date expiryDate = null;// new Date(System.getCurrentTimeMillis() +
									// maxAge);
			cookie.setExpiryDate(expiryDate);
		}
		if (stringNullEmptyValidator(domainCookie)) {
			cookie.setDomain(domainCookie);
		} else {
			exceptionMessage = "Invalid Cookie: Missing Domain";
		}
		if (stringNullEmptyValidator(pathCookie)) {
			cookie.setPath(pathCookie);
		} else {
			exceptionMessage = "Invalid Cookie: Missing Path";
		}
		if (stringNullEmptyValidator(commentCookie)) {
			cookie.setComment(commentCookie);
		}
		if (stringNullEmptyValidator(versionCookie)) {
			cookie.setVersion(Integer.parseInt(versionCookie));
		}
		if (stringNullEmptyValidator(exceptionMessage))
			throw new UltaException(exceptionMessage);

		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<cookie>>"+cookie);
		// Logger.Log("[WebserviceExecutionHelper]{basicClientCookieGenerator}(http/https)<RETURN>");
		return cookie;
	}

	/**
	 * Method to format the persisted cookie in the required format for handling
	 * the session.
	 *
	 * @param cookieString
	 *            the cookie string
	 * @return HashMap<String, String>
	 */
	protected static HashMap<String, String> cookieMapFormatter(
			String cookieString) {
		// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<ENTRY>");
		HashMap<String, String> cookieMap = null;
		// String cookieString
		// ="[version: 0][name: JSESSIONID][value: 301184C68867902EDBC95BF54AB3820D.d-a1-stgapp1][domain: 172.19.0.197][path: /][expiry: null]";
		// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<cookieString>"+cookieString);
		String cookieStringWithReplace1 = cookieString.replace(
				OPEN_BRACE_SYMBOL, EMPTY_STRING);
		// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<cookieString>"+cookieStringWithReplace1);
		String cookieStringWithReplace2 = cookieStringWithReplace1.replace(
				CLOSING_BRACE_SYMBOL, TILDE_MARK_SYMBOL);
		// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<cookieString>"+cookieStringWithReplace2);
		String[] cookieInformation = cookieStringWithReplace2
				.split(TILDE_MARK_SYMBOL);
		if (cookieInformation != null && cookieInformation.length > 0) {
			cookieMap = new HashMap<String, String>();
			String[] anotherSplitter = null;
			for (String ck : cookieInformation) {
				// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<ck>"+ck);
				anotherSplitter = ck.split(COOKIE_SPLITTER_SYMBOL);
				for (String as : anotherSplitter) {
					// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<anotherSplitter>"+as);
				}
				try {
					if (stringNullEmptyValidator(anotherSplitter[0])
							&& stringNullEmptyValidator(anotherSplitter[1]))
						cookieMap.put(anotherSplitter[0].trim(),
								anotherSplitter[1].trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<cookieMap>"+cookieMap);
		// Logger.Log("[WebserviceExecutionHelper]{cookieMapFormatter}(http/https)<RETURN>");
		return cookieMap;
	}

	/**
	 * Method for handling the cookie for the first time.
	 *
	 * @return true, if successful
	 */
	protected static boolean cookieFirstTimePresent() {
		boolean isCookiePresent = false;
		String sessionIdCookie = getPersistedCookieValue(SESSION_ID_COOKIE);
		String dynUserIdCookie = getPersistedCookieValue(DYN_USER_ID_COOKIE);
		String dynUserConfirmCookie = getPersistedCookieValue(DYN_USER_CONFIRM_COOKIE);
		Logger.Log("[WebserviceExecutionHelper]{cookieFirstTimePresent}<SESSION_ID_COOKIE>> "
				+ sessionIdCookie);
		Logger.Log("[WebserviceExecutionHelper]{cookieFirstTimePresent}<DYN_USER_ID_COOKIE>> "
				+ dynUserIdCookie);
		Logger.Log("[WebserviceExecutionHelper]{cookieFirstTimePresent}<DYN_USER_CONFIRM_COOKIE>> "
				+ dynUserConfirmCookie);
		// TODO if required extend the same to other cookies also.
		if (stringNullEmptyValidator(sessionIdCookie)) {
			isCookiePresent = true;
		}
		return isCookiePresent;
	}

	/**
	 * Forming cookie store for collecting all cookies.
	 *
	 * @param isUserSessionClearingRequired
	 *            the is user session clearing required
	 * @return CookieStore
	 * @throws UltaException
	 *             the ulta exception
	 */
	protected static CookieStore getCookieStore(
			boolean isUserSessionClearingRequired, boolean isBagRequest)
			throws UltaException {
		// Logger.Log("[WebserviceExecutionHelper]{getCookieStore}<ENTRY>");

		CookieStore cookieStore = new BasicCookieStore();

		String sessionIdCookie = getPersistedCookieValue(SESSION_ID_COOKIE);
		String dynUserIdCookie = getPersistedCookieValue(DYN_USER_ID_COOKIE);
		String dynUserConfirmCookie = getPersistedCookieValue(DYN_USER_CONFIRM_COOKIE);
		String aoCookie = getPersistedCookieValue(AO_COOKIE);

		// Logger.Log("[WebserviceExecutionHelper]{getCookieStore}<SESSION_ID_COOKIE>> "+sessionIdCookie);
		// Logger.Log("[WebserviceExecutionHelper]{getCookieStore}<DYN_USER_ID_COOKIE>> "+dynUserIdCookie);
		// Logger.Log("[WebserviceExecutionHelper]{getCookieStore}<DYN_USER_CONFIRM_COOKIE>> "+dynUserConfirmCookie);
		Logger.Log("[WebserviceExecutionHelper]{getCookieStore}<aoCookie>> "
				+ aoCookie);
		// Logger.Log("[WebserviceExecutionHelper]{getCookieStore}<isUserSessionClearingRequired>> "+isUserSessionClearingRequired);

		if (sessionIdCookie != null && sessionIdCookie.trim().length() > 0) {
			cookieStore.addCookie(basicClientCookieGenerator(sessionIdCookie));
		}
		if (isBagRequest) {
			if (aoCookie != null && aoCookie.trim().length() > 0) {
				if (getAOCookieExpired()) {
					clearAOCookie();
				} else {
					cookieStore.addCookie(basicClientCookieGenerator(aoCookie));
				}
			}
		}
		if (!isUserSessionClearingRequired) {
			if (dynUserIdCookie != null && dynUserIdCookie.trim().length() > 0) {
				cookieStore
						.addCookie(basicClientCookieGenerator(dynUserIdCookie));
			}
			if (dynUserConfirmCookie != null
					&& dynUserConfirmCookie.trim().length() > 0) {
				cookieStore
						.addCookie(basicClientCookieGenerator(dynUserConfirmCookie));
			}
		}
		// Logger.Log("[WebserviceExecutionHelper]{getCookieStore}<RETURN>");

		return cookieStore;
	}

	/**
	 * Method to get the value of the cookie.
	 *
	 * @param cookieRefId
	 *            the cookie ref id
	 * @return the persisted cookie value
	 */
	protected static String getPersistedCookieValue(String cookieRefId) {
		String persistedCookieInformation = Utility.getCookieValue(cookieRefId);
		// Logger.Log("[WebserviceExecutionHelper]{getCookiePersistedCookieValue}(persistedCookieInformation)>> "+"<"+cookieRefId+">=<"+persistedCookieInformation+">");
		return persistedCookieInformation;
	}

	/**
	 * Method to get the value of the cooke.
	 *
	 * @param cookieRefKey
	 *            the cookie ref key
	 * @param cookieValue
	 *            the cookie value
	 */
	protected static void persistCookieInfo(String cookieRefKey,
			String cookieValue) {
		// Logger.Log("[WebserviceExecutionHelper]{persistCookieInfo}(Persisting...)>> "+"cookieRefKey"+"> "+cookieRefKey+"<>"+"cookieValue"+"> "+cookieValue);
		Utility.saveCookie(cookieRefKey, cookieValue);
	}

	/**
	 * Method to return the UltaDataCache.
	 *
	 * @return the ulta data cache instance
	 */
	protected static UltaDataCache getUltaDataCacheInstance() {
		return UltaDataCache.getDataCacheInstance();
	}

	private static void clearAOCookie() {
		String aoCookie = Utility.getCookieValue(WebserviceConstants.AO_COOKIE);
		if (aoCookie != null && aoCookie.trim().length() > 0) {
			Utility.saveCookie(WebserviceConstants.AO_COOKIE, null);
			Utility.saveCookie(WebserviceConstants.EXPIRY_DATE, null);
			Log.e("COOKIE", "DELETED");
		}
	}

	private String convertDate(String date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"dd MMM yyyy hh:mm:ss");
			Date d = format.parse(date);
			SimpleDateFormat serverFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			return serverFormat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static boolean getAOCookieExpired() {
		String expiryDateString = getPersistedCookieValue(EXPIRY_DATE);
		Date convertedDate = parseTodaysDate(expiryDateString);
		Date currentDate = new Date();
		String currentdate = currentDate.toString();
		currentDate = parseTodaysDate(currentdate);
		if (null != currentDate && null != convertedDate) {
			if (convertedDate.before(currentDate)) {
				Log.i("date", "expired");
				return true;
			}
		}
		// SimpleDateFormat dateFormat = new SimpleDateFormat(
		// "dd MMM yyyy hh:mm:ss");
		// /* dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));*/
		// Date convertedDate = new Date();
		// try {
		// convertedDate = dateFormat.parse(expiryDateString);
		// Date currentDate = new Date();
		// String currentdate = currentDate.toGMTString();
		// currentDate = dateFormat.parse(currentdate);
		// if (convertedDate.before(currentDate)) {
		// Log.i("date", "expired");
		// return true;
		// }
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// Date expiryDate = new Date(expiryDateString);

		return false;
	}

	public static Date parseTodaysDate(String time) {

		String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";

		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

		Date date = null;

		try {
			date = inputFormat.parse(time);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
