/**
 *
 * Copyright(c) ULTA, Inc. All Rights reserved.
 *
 *
 */
package com.ulta.core.conf;

import android.Manifest;

import com.ulta.core.conf.types.AppEnvironment;
import com.ulta.core.util.caching.UltaDataCache;

/**
 * The Class WebserviceConstants.
 *
 * @author Infosys
 */
public class WebserviceConstants {

	/**
	 * True to show the environment pop up.
	 */
	public static final boolean isShowEnvironmentPopUp = true;

	/**
	 * The flag for checking if the security is enabled or not. true : the calls
	 * will go as https false : the calls will go aa http
	 */
	public static final boolean isSecurityEnabled = true;
	// public static final boolean isSecurityEnabled = false;

	/**
	 * The server context. use rest/v2/bean for prod name use rest/bean for
	 * testing with IP address
	 */
	public static final String SERVER_CONTEXT = "rest/v2/bean";
	// public static final String SERVER_CONTEXT = "rest/bean";

	/**
	 * Font style file name
	 */
	public static final String FONT_STYLE_FILENAME = "Helvetica_Reg.ttf";

	/**
	 * The Constant Environment.
	 */
	public static final String ULTA_ENVIRONMENT = AppEnvironment.TEST
			.toString();
	/**
	 * The Constant HEADER_NAME.
	 */
	public static final String HEADER_NAME = "Content-Type";
	/**
	 * The Constant HEADER_VALUE_XML.
	 */
	public static final String HEADER_VALUE_XML = "application/xml";

	/**
	 * The Constant HEADER_VALUE_JSON.
	 */
	public static final String HEADER_VALUE_JSON = "application/json";
	/**
	 * The Constant FORMAT_XML.
	 */
	public static final String FORMAT_XML = "xml";

	/**
	 * The Constant FORMAT_JSON.
	 */
	public static final String FORMAT_JSON = "json";
	/**
	 * The Constant EMAIL_PLAIN_TEXT.
	 */
	public static final String EMAIL_PLAIN_TEXT = "Plain/Text";

	/**
	 * The Constant Encoding UTF-8.
	 */
	public static final String ECODING_UTF_8 = "UTF-8";

	/**
	 * The Constant WEBSERVICES_FLAG.
	 */
	public static final String WEBSERVICES_FLAG = "WEBSERVICE";

	/** The constant app_environment. */
	public static final String APP_ENVIRONMENT = UltaDataCache
			.getDataCacheInstance().getAppEnvironment();
	/**
	 * The server Address pertaining to the web services production server.
	 */
	public static final String WEBSERVICES_SERVER_ADDRESS_PROD = "www.ulta.com";
	/**
	 * The server URL pertaining to the web services server.
	 */
	public static final String WEBSERVICES_SERVER_ADDRESS = UltaDataCache
			.getDataCacheInstance().getServerAddress();

	/** The SOLR context. */
	public static final String SOLR_CONTEXT = "solr";

	/** The Constant for Default Buffer Size. */
	public static final int BUFFER_BLOCK_SIZE = 2 * 1024;

	/** Additional information google. */
	public static final String ADDITIONAL_INFO_GOOGLE_API = "GOOGLE";

	/** GOOGLE_APIS_CONTEXT. */
	public static final String GOOGLE_MAP_API_CONTEXT = "maps.googleapis.com";

	/** Ulta Phone number for contact */
	public static final String ULTA_PHONE_NUMBER = "866-983-8582";

	public static final String STAY_SIGNED_IN_SHAREDPREF = "staySignedIn";

	public static final String PUSH_NOTIFICATION_SHAREDPREF = "pushNotification";

	public static final String HOME_BANNER_REFRESH_SHAREDPREF = "homeBannerRefresh";

	public static final String MOBILE_OFFER_SHAREDPREF = "mobileOffer";

	public static final String MOBILE_OFFER_ARRAY = "mobileOfferArray";

	public static final String GUEST_USER_SHAREDPREF = "guestUserDetails";

	public static final String GUEST_USER_MAIL_ID = "guestUserMailId";

	public static final String GUEST_USER_PAYMENT_DETAILS = "guestUserPaymentDetails";

	public static final String BILLING_SAME_AS_SHIPPING = "billingSameAsShipping";

	public static final String IS_REFRESH_TIME_EXPIRED = "isRefreshTimeExpired";

	public static final String IS_REFRESH_TIME_EXPIRED_SHOP_PAGE = "isRefreshTimeExpiredShopPage";

	public static final String BANNER_REFRESH_TIME = "bannerRefreshTime";

	public static final String PROD_LARGE_IMAGE_URL = "prodLargeImageURL";

	public static final String IS_STAY_SIGNED_IN = "isStaySignedIn";

	public static final String IS_LOGGED_IN = "isLoggedIn";

	public static final String IS_ACTIVE = "isActive";

	public static final String IS_PUSH_NOTIFICATION_ON = "isPushNotificationOn";

	public static final String STAY_SIGNED_IN_PASSWORD = "loginPassword";

	public static final String STAY_SIGNED_IN_USERNAME = "loginUserName";

	public static final String STAY_SIGNED_IN_FIRST_NAME = "loginFirstName";

	public static final String STAY_SIGNED_IN_SECRET_KEY = "secretKey";

	public static final String STAY_SIGNED_IN_IS_LAST_ACTIVITY = "isLastActivity";

	public static final String COUNTS_PREFS_NAME = "CountPrefs";

	public static final String BASKET_COUNT = "basketCount";

	public static final String FAVORITES_COUNT = "favoritesCount";

	/*
	Reward page images
	 */

    public static final String GIFT_CLICK_ON_WEB = "http://www.ulta.com/ulta/giftcards/giftCard.jsp?categoryId=cat120341";
	public static final String BENIFITS_URL = "http://www.ulta.com/images/rewards/mobile-app/ur_benefits_m";
	public static final String EARNINGS_URL = "http://www.ulta.com/images/rewards/mobile-app/ur_earningredeeming_m";
	public static final String PLATINUMS_URL = "http://www.ulta.com/images/rewards/mobile-app/ur_platinum_m";



	public static final String SHORTENURL_LINK = "http://po.st/api/shorten?apiKey=50B5BCE1-C6FF-4484-A03C-8732E2A4B24C";

	public static final String SHIPPING_INFO_LINK = "http://www.ulta.com/ulta/guestservices/guestServicesCenterDetails.jsp#ShippingPolicy";
	
	public static final String FRAGRANCE_URL = "http://www.ulta.com/fragrancefinder";
	/*
	 * Server Address
	 */

	public static final String prodServerAddress = "api.ulta.com";
	public static final String stressServerAddress = "api-stress.ulta.com";
	public static final String stagingServerAddress = "apistg.ulta.com";
	public static final String UATAddress = "api-uat.ulta.com";

	public static final String qa01ServerAddress = "api-qa1.ulta.com";
	public static final String qa02ServerAddress = "api-qa2.ulta.com";
	public static final String qa03ServerAddress = "api-qa3.ulta.com";

	public static final String da01ServerAddress = "api-da1.ulta.com";
	public static final String da02ServerAddress = "api-da2.ulta.com";
	public static final String da03ServerAddress = "api-da3.ulta.com";

	public static  String ULTA_SITE_VALUE;
	public static  boolean  isULTA_SITE_VALUE=false;




	public static final String UAK="r3VvG8bmu9A9G7kAVe2Bi6viRRVRiKdYmK4psxTYF6TgZC7ckNerDfu634FbnsWQ";

	/**
	 * Push Notification Constants
	 */

	public static final String CHANNEL_ID_PREF = "registrationIdSharedPref";
	public static final String PUSH_CHANNEL_ID = "pushNotificationRegistrationId";


	public static final String SENDER_ID = "805371746350";

	public static final String TITLE = "title";
	public static final String MESSAGE = "message";
	public static final String PARAMETER = "sv";
	public static final String TYPE = "st";


	// -------------------------------------Cookie handling information :
	// BEGIN-------------------------------------

	/**
	 * Boolean for turning ON & OFF the cookie handling.
	 */
	public static final boolean isCookieHandlingRequired = true;

	/** The cookie name JSESSIONID used for session handling Expires : 30m in. */
	public static final String SESSION_ID_COOKIE = "JSESSIONID";
	public static final String AO_COOKIE = "ao";

	public static final String EXPIRY_DATE = "expirydate";

	/** SESSION_ID_COOKIE_EXPIRY time in seconds. */
	public static final int SESSION_ID_COOKIE_EXPIRY = 30 * 60;
	/**
	 * The cookie name DYN_USER_ID used for logged in user handling. Expires :
	 * 30 days
	 */
	public static final String DYN_USER_ID_COOKIE = "DYN_USER_ID";

	/** DYN_USER_ID_COOKIE time in seconds. */
	public static final int DYN_USER_ID_COOKIE_EXPIRY = 30 * 60 * 60 * 24;
	/**
	 * The cookie name DYN_USER_CONFIRM used for handling the logged in user.
	 * Expires : 30 days
	 */
	public static final String DYN_USER_CONFIRM_COOKIE = "DYN_USER_CONFIRM";

	/** DYN_USER_CONFIRM_COOKIE time in seconds. */
	public static final int DYN_USER_CONFIRM_COOKIE_EXPIRY = 30 * 60 * 60 * 24;

	/** expires - Cookie information. */
	public static final String EXPIRES_COOKIE = "expiry";

	/** domain - Cookie Information. */
	public static final String DOMAIN_COOKIE = "domain";

	/** path - Cookie Information. */
	public static final String PATH_COOKIE = "path";

	/** comment - Cookie Information. */
	public static final String COMMENT_COOKIE = "comment";

	/** max-age - Cookie Information. */
	public static final String MAX_AGE_COOKIE = "max-age";

	/** secure - Cookie Information. */
	public static final String SECURE_COOKIE = "secure";

	/** name - Cookie Information. */
	public static final String NAME_COOKIE = "name";

	/** value - Cookie Information. */
	public static final String VALUE_COOKIE = "value";

	/** Version - Cookie Information. */
	public static final String VERSION_COOKIE = "version";

	/** Connection Timeout */
	public static final int CONNECTION_TIMEOUT_IN_MILLI = 10000;

	/** Response Timeout */
	public static final int RESPONSE_TIMEOUT_IN_MILLI = 10000;

	public static final String ERROR_FOR_NETWORK_CHECK = "1111~Network Unavailable";

	// --------------------------------------Cookie handling information :
	// END-------------------------------------

	// --------------------------------------Constants For Web
	// View------------------------------------------------

	public static final int FROM_WEEKLYAd = 1;

	public static final int FROM_BLACK_FRIDAY = 2;

	public static final int FROM_GIFT_CARD = 3;

	public static final int FROM_PUSHNOTIFICATION = 4;

	public static final int FROM_ULTAMATE_CARD = 5;


	public static final int FROM_BENIFITS = 100;

	public static final int FROM_EARNINGSANDREDEEMING = 101;

	public static final int FROM_PLATINUM = 102;
//	/public static final String URL_FOR_AD = "http://storead.ulta.com?From=ultaNative";
	public static final String URL_FOR_AD = "http://ulta.com/";

	//URL for Ultamate card Images
	public static final String ULTAMATE_MC_CARD="http://images.ulta.com/is/image/Ulta/MobileApp-RewardsPage-UBCC-MC";
	public static final String ULTAMATE_CC_CARD="http://images.ulta.com/is/image/Ulta/MobileApp-RewardsPage-UBCC-CC";

	public static final String URL_FOR_CASH_STAR = "https://ulta-m-semiprod.cashstar.com/";

	// Contsants to be appended to freesamplesUrl;

	public static final String FOURTH_APPEND_TO_MDPI = "_release3_MDPI?scl=1";

	public static final String FOURTH_APPEND_TO_HDPI = "_release3_HDPI?scl=1";

	public static final String FOURTH_APPEND_TO_XHDPI = "_release3_XHDPI?scl=1";
	public static final String FOURTH_APPEND_TO_XXHDPI = "_release3_XXHDPI?scl=1";
	public static final String FOURTH_APPEND_TO_XXXHDPI = "_release3_XXXHDPI?scl=1";

	public static final String APPEND_TO_MDPI = "_mobile_release4@MDPI?scl=1";

	public static final String APPEND_TO_HDPI = "_mobile_release4@HDPI?scl=1";

	public static final String APPEND_TO_XHDPI = "_mobile_release4@XHDPI?scl=1";
	public static final String APPEND_TO_XXHDPI = "_mobile_release4@XXHDPI?scl=1";
	public static final String APPEND_TO_XXXHDPI = "_mobile_release4@XXXHDPI?scl=1";
	//cash star banner
	public static final String CASH_STAR_BANNER="http://images.ulta.com/is/image/Ulta/App-GiftCardBanner";

	//Social gallery banner
	public static final String SOCIAL_GALLERY_BANNER="http://images.ulta.com/is/image/Ulta/GalleryBanner-MobileApp";
	
	// URL for shop page
	public static final String SHOP_BANNER = "http://s7d5.scene7.com/is/image/Ulta/app_shop_";

	// URL for sale page
	public static final String SALE_BANNER = "http://images.ulta.com/is/image/Ulta/SaleBannerMobileApp";

	// URL for favorites page
	public static final String FAVORITES_BANNER = "http://images.ulta.com/is/image/Ulta/FavoritesHomeMobileApp";

	// URL for Rewards page
	public static final String REWARDS_BANNER = "http://images.ulta.com/is/image/Ulta/RewardsHomeMobileApp";

	// URL for Empty backet
	public static final String EMPTY_BAG = "http://images.ulta.com/is/image/Ulta/EmptyBagHomeMobileApp";

	//URL for ULTAMATE CREDIT CARD

	public static final String ULTAMATE_CREDIT_CARD_LANDING="http://images.ulta.com/is/image/Ulta/CardHomeMobileApp";

	// ---------------------------------------------WEB SERVICES
	// BEGIN---------------------------------------------

	public static final String PUSH_NOTIFICATION_SERVICE = "ulta/mobile/profile/PushNotificationsPreferences/processPushNotificationsPreferences";

	/** Service for fetching new arrivals */
	public static final String FETCH_NEW_ARRIVAL_ITEMS = "ulta/mobile/search/Search/fetchNewArrivals";

	/** Service for getting app configurables */
	public static final String APPCONFIGURABLES_SERVICE = "ulta/mobile/AppConfigHomePageSectionSlotInfo?";

	/** Service for getting the product details. */
	public static final String PRODUCTDETAILS_SERVICE = "ulta/mobile/catalog/MobileCatalogServices/fetchProductDetails?";

	/** Service for email opt in. */
	public static final String EMAIL_OPT_IN_SERVICE = "atg/userprofiling/B2CProfileFormHandler/subscribeEmail";

	/** Service for login. */
	public static final String LOGIN_SERVICE = "atg/userprofiling/B2CProfileFormHandler/login";

	/** Service for login. */
	public static final String USER_CREATION_SERVICE = "atg/userprofiling/B2CProfileFormHandler/ultaCreate";

	/** Service for forgot password. */
	public static final String FORGOT_PASSWORD_SERVICE = "atg/userprofiling/ForgotPasswordHandler/checkEmail";

	/** Service for change password. */
	public static final String CHANGE_PASSWORD_SERVICE = "atg/userprofiling/B2CProfileFormHandler/changePassword";

	/** Service for logout. */
	public static final String LOGOUT_SERVICE = "atg/userprofiling/B2CProfileFormHandler/logout";

	/** Service for rewards. */
	public static final String REWARDS_SERVICE = "atg/userprofiling/MyAccount/rewardsDetails?";

	/** Service for Activating Offer. */
	public static final String ACTIVATE_OFFER = "ulta/loyalty/UltaLoyaltyFormHandler/activateOfferId";

	/** Service for joining rewards. */
	public static final String JOIN_REWARDS = "ulta/loyalty/UltaLoyaltyFormHandler/joinRewardsProgram";

	/** Service for coupon */
	public static final String COUPON_SERVICE = "ulta/mobile/coupons/CouponXMLSaxParser/parseCouponXML";

	/** Service for billing address. */
	public static final String BILLING_ADDRESS_SERVICE = "atg/userprofiling/MyAccount/billingAddress?";

	/** Service for Commit Order service. */
	public static final String CHECKOUT_SERVICE = "atg/commerce/order/purchase/CartModifierFormHandler/checkoutItems";

	/** Service for shippment service. */
	public static final String SHIPPMENT_SERVICE = "atg/commerce/order/purchase/ShippingGroupFormHandler/verifyShippingAddress";

	/** Service for moveToBilling service. */
	public static final String MOVE_TO_BILLING_SERVICE = "atg/commerce/order/purchase/ShippingGroupFormHandler/moveToBilling";

	/** Service for gift Option service. */
	public static final String GIFT_OPTION_SERVICE = "atg/commerce/order/purchase/CartModifierFormHandler/updateGiftServices";

	/** Service for purchase history. */
	public static final String PURCHASE_HISTORY = "ulta/mobile/profile/MyAccountPurchaseHistory/getTransactionHistory";

	/** Service for payment service. */
	public static final String PAYMENT_SERVICE = "atg/commerce/order/purchase/PaymentGroupFormHandler/payment";

	/** Service for slide show */
	public static final String SLIDER_SERVICE = "ulta/mobile/slides/DisplaySlideShow/fetchSlides";

	/** Service for slide show and mobile offers */
	public static final String HOMEPAFEINFO_SERVICE = "ulta/mobile/homePageInfo?";

	/** Service for slide show and mobile offers */
	public static final String SHOP_PAGE_SLIDESHOW_SERVICE = "ulta/mobile/slides/ShopPageSlideShow/getBannerInfo";

	/** Service for home page banner */
	public static final String HOME_PAGE_SECTION_INFO = "ulta/mobile/slides/HomePageSectionInfo/getBannerInfo";

	/** Service for Promotions */
	public static final String PROMOTION = "ulta/mobile/catalog/MobileCatalogServices/getPromotions";

	/** Service for Product Listing by Promo id */
	public static final String PRODUCT_LISTING_BY_PROMO_ID = "ulta/mobile/catalog/MobileCatalogServices/getProductListingByPromotion";

	/** Service for Product Listing by Category id */
	public static final String PRODUCT_LISTING_BY_CATEGORY_ID = "ulta/mobile/catalog/MobileCatalogServices";

	/** Service for Commit Order service. */
	public static final String COMMIT_ORDER_SERVICE = "atg/commerce/order/purchase/CommitOrderFormHandler/commitOrder";

	/** Service for buy more save more promotions list */
	public static final String BUY_MORE_SAVE_MORE = "ulta/mobile/catalog/MobileCatalogServices/getPromotions";

	/**Webservice for credit card information Butterfly Authentiaction	 */

	public static final String BUTTERFLY_CREDIT_CARD_INFO="ulta/mobile/creditCardsInfo?";

	public static final String osName = "Android";

	public static final String versionNumber = "4.2";

	/**
	 * Olapic Constants
	 */

	public static final String OLAPIC_SERVICE = "//photorankapi-a.akamaihd.net/customers/216180/media/recent?";

	public static final String SHOP_THIS_LOOK = "shopThisLook";

	public static final String OLAPIC_PDP_HEADING = "Share socially with #ULTA";

	public static final String OLAPIC = "olapic";

	public static final String OLAPIC_VERSION = "v2.2";

	public static final String OLAPIC_AUTH_TOKEN = "35184924054d51039371a49df9c02bcda4613a636f7cb5223a0f08a19f9a0185";

	// fc7f57c502153ec0ffbc9cb0607ba9dc6c08ebdd4d0b088f80f84e0475ec5286

	public static final String OLAPIC_UPLOAD_MEDIA_FIRST = "//rest.photorank.me/users?auth_token=35184924054d51039371a49df9c02bcda4613a636f7cb5223a0f08a19f9a0185&version=v2.2";


	/** Service for Guest user login. */
	public static final String GUEST_USER_LOGIN_SERVICE = "atg/userprofiling/B2CProfileFormHandler/anonymousCheckout";

	/** Service for beauty preferences category */
	public static final String BEAUTY_PREF_SERVICE = "atg/userprofiling/B2CProfileFormHandler/newBeautyPreferences?";

	/** Service for beauty preferences saving and updating */
	public static final String SAVE_AND_UPDATE_BEAUTY_PREF_SERVICE = "atg/userprofiling/B2CProfileFormHandler/updateNewBeautyPreferences";

	/** Service to update beauty preferences */
	public static final String UPDATE_BEAUTY_PREFERENCES = "atg/userprofiling/B2CProfileFormHandler/updateBeautyPreferences";

	/** Service to get the list of beauty preferences */
	public static final String FETCH_BEAUTY_PREFERENCES = "atg/userprofiling/B2CProfileFormHandler/beautyPreferences?";

	/** Service for Anonymous User's Fetch Order Status. */
	public static final String FETCH_ORDER_DETAILS = "ulta/mobile/order/OrderHistory/fetchAnonymousOrderHistory";

	/** Service for Anonymous User's Fetch Order Details. */
	public static final String FETCH_ANONYMOUS_ORDER_DETAILS = "atg/commerce/ShoppingCart/fetchAnonymousOrderDetails";

	/** Service for Commit Order service. */
	public static final String VERIFY_ADDRESS_AND_COMMIT_ORDER_SERVICE = "atg/commerce/order/purchase/CommitOrderFormHandler/verifyAddressAndCommitOrder";

	/** Service for Commit Order service. */
	public static final String GET_ENTERED_SHIPPING_ADDRESS = "ulta/util/AddressUtil/getEnteredShipAddress";

	/** Service for Commit Order service. */
	public static final String GET_VERIFIED_SHIPPING_ADDRESS = "ulta/util/AddressUtil/getAddressesOnShipment?";

	/** Verify Shipping Address. */
	public static final String VERIFY_SHIPPING_ADDRESS = "ulta/util/AddressUtil/getAddressesOnShipment";

	/** New service to keep the address and move to shipping method. */

	public static final String KEEP_EXISTING_ADDRESS_AND_MOVE_TO_SHIPPING_METHOD = "atg/commerce/order/purchase/ShippingGroupFormHandler/keepEnteredShippingAddress";

	/** New Service for updating verified address. */
	public static final String UPDATE_VERIFIED_ADDRESS_AND_MOVE_TO_SHIPPING_METHOD = "atg/commerce/order/purchase/ShippingGroupFormHandler/setVerifiedShippingAddress";

	/** Service for Commit Order service. */
	public static final String KEEP_ADDRESS_AND_COMMIT_SERVICE = "atg/commerce/order/purchase/CommitOrderFormHandler/keepAddressAndCommitOrder";

	/** Service for Commit Order service. */
	public static final String SET_ADDRESS_AND_COMMIT_SERIVICE = "atg/commerce/order/purchase/CommitOrderFormHandler/setAddressAndCommitOrder";

	// **************End of New Web services for 3.2 release ****************//

	// **************Start of New Web services for 3.3 release
	// ****************//
	/** Service for fetching on sale items */
	public static final String FETCH_ONSALE_ITEMS = "ulta/mobile/search/Search/fetchOnSaleItems";

	/** Service to verify paypal payment status */
	public static final String PAYPAL_VERIFICATION_STATUS_SERVICE = "ulta/mobile/paypal/payment/MobilePayPalPayment/fetchVerificationStatus";

	/** Service to fetch all brand names */
	public static final String SHOP_BY_BRANDS_SERVICE = "ulta/mobile/catalog/MobileCatalogServices/shopByBrands";

	/** Service for fetching items of selected brand */
	public static final String FETCH_PRODUCTS_FOR_BRAND = "ulta/mobile/search/Search/fetchProductsForBrand";

	/** Service for product search using keyword */
	public static final String KEYWORD_SEARCH_SERVICE = "ulta/mobile/search/Search/fetchProductsForKeyword";

	/** Service for fetching products of selected leaf category */
	public static final String FETCH_PRODUCTS_FOR_LEAF_CATEGORY = "ulta/mobile/search/Search/fetchProductsForLeafCategory";

	public static final String FETCH_PRODUCTS_FOR_GWP_FROM_SALE = "ulta/mobile/search/Search/GWPLandingPage";

	public static final String FETCH_PRODUCTS_BY_PROMOTIONS = "ulta/mobile/catalog/MobileCatalogServices/getProductListingByPromotion";

	public static final String FETCH_PRODUCTS_FOR_PROMOTIONS = "ulta/mobile/search/Search/fetchProductsForPromotion";

	public static final String PAYPAL_PAYMENT_DETAILS = "ulta/mobile/paypal/payment/MobilePayPalPayment/mobilePaypalDetails";

	public static final String PAYPAL_CHECKOUT_SERVICE = "atg/commerce/order/purchase/CartModifierFormHandler/paypalExpressCheckout";

	public static final String FETCH_TAXONOMY_DETAILS = "ulta/mobile/search/Taxonomy/fetchTaxonomyDetails";

	public static final String EXPRESS_CHECKOUT_SANDBOX_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout-mobile&token=";
	public static final String EXPRESS_CHECKOUT_PRODUCTION_URL = "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout-mobile&token=";
	// **************End of New Web services for 3.3 release ****************//

	/** Service for state list. */
	public static final String STATE_LIST_SERVICE = "atg/userprofiling/MyAccount/stateList?";

	/** Service for order history. */
	public static final String ORDER_HISTORY = "ulta/mobile/order/OrderHistory/fetchOrderHistory";

	/** Service for order details. */
	public static final String ORDER_DETAIL = "atg/commerce/ShoppingCart/fetchOrderDetails";

	/** Service for getting the rootcategorydetails. */
	public static final String ROOTCATEGORYDETAILS_SERVICE = "ulta/mobile/catalog/MobileCatalogServices/getRootCategories";

	/** Service for add to cart. */
	public static final String ADDITEMTOORDER_SERVICE = "atg/commerce/order/purchase/CartModifierFormHandler/addItemToOrder";

	/** Service for view cart. */
	public static final String GETMOBILECART_SERVICE = "atg/commerce/ShoppingCart/mobileCart?";

	/** Service to update quantity. */
	public static final String UPDATE_SERVICE = "atg/commerce/order/purchase/CartModifierFormHandler/update";

	/** Service to remove coupon. */
	public static final String REMOVE_COUPON = "atg/commerce/order/purchase/CartModifierFormHandler/removeCoupon";

	/** Service to fetch free samples. */
	public static final String FETCHFREESAMPLES_SERVICE = "ulta/mobile/catalog/MobileCatalogServices/fetchFreeSamples";

	/** Service to fetch free samples. */
	public static final String ADD_FREESAMPLES_FORMOBILE_SERVICE = "atg/commerce/order/purchase/CartModifierFormHandler/addFreeSamplesForMobile";

	/** Service to remove item from cart. */
	public static final String REMOVEITEM_FROM_ORDERBY_RELATIONSHIPID_SERVICE = "atg/commerce/order/purchase/CartModifierFormHandler/removeItem";

	/** Service for getting the myProfiledetails. */
	public static final String MY_PROFILE_DETAILS_SERVICE = "atg/userprofiling/MyAccount/personalInfo?";

	/** Service for getting the MyPreffredShippingaddressdetails. */
	public static final String PREFFRED_SHIPPING_ADDRESS_DETAILS_SERVICE = "atg/userprofiling/MyAccount/defaultShippingAddress?";

	/** Service for applying the GiftCard */
	public static final String APPLY_GIFT_CARD = "atg/commerce/order/purchase/PaymentGroupFormHandler/applyGiftCard";

	/** Service for removing the GiftCard */
	public static final String REMOVE_GIFT_CARD = "atg/commerce/order/purchase/PaymentGroupFormHandler/removeGiftCard";

	/** Service for getting the MyPreffredBillingaddressdetails. */
	public static final String PREFFRED_BILLING_ADDRESS_DETAILS_SERVICE = "atg/userprofiling/MyAccount/billingAddress?";

	/** Service for getting the current Ads URL. */
	public static final String CURRENT_ADS_URL = UltaDataCache
			.getDataCacheInstance().getCurrentAdsURL();

	/** Service for getting List of Free Gifts */
	public static final String FREEGIFTDETAILS_SERVICE = "ulta/mobile/catalog/MobileCatalogServices/fetchFreeGifts";

	/** Service for getting the List of shipping addresses . */
	public static final String LISTOF_SHIPPING_ADDRESS_SERVICE = "atg/userprofiling/MyAccount/shippingAddresses?";

	/** Service for Adding new shipping address . */
	public static final String ADD_NEW_SHIPPING_ADDRESS_SERVICE = "atg/userprofiling/B2CProfileFormHandler/ultaNewAddress";

	/** Service for Delete shipping address . */
	public static final String DELETE_SHIPPING_ADDRESS_SERVICE = "atg/userprofiling/B2CProfileFormHandler/removeAddress";

	/** Service for setting default shipping address . */
	public static final String EDIT_SHIPPING_ADDRESS_SERVICE = "atg/userprofiling/B2CProfileFormHandler/ultaUpdateAddress";

	/** Service for setting default shipping address . */
	public static final String SET_AS_DEFAULT_SHIPPING_ADDRESS_SERVICE = "atg/userprofiling/B2CProfileFormHandler/defaultShippingAddress";
	/** Service for Checking Gif Card Balance . */
	public static final String CHECK_BALANCE = "ulta/common/UltaGiftCardFormHandler/checkBalance";

	/** Service for getting the MyPreffredShippingaddressdetails. */
	public static final String PAYMENT_METHOD_DETAILS_SERVICE = "atg/userprofiling/MyAccount/creditCards?";

	/** Service for setting default shipping address . */
	public static final String ADD_NEW_CREDIT_CARD_SERVICE = "atg/userprofiling/B2CProfileFormHandler/ultaCreateNewCreditCard";

	/** Service for setting default shipping address . */
	public static final String EDIT_CREDIT_CARD_SERVICE = "atg/userprofiling/B2CProfileFormHandler/ultaUpdateCard";

	/** Service for setting default shipping address . */
	public static final String SET_AS_DEFAULT_CREDIT_CARD_SERVICE = "atg/userprofiling/B2CProfileFormHandler/defaultCard";

	/** Service for Delete shipping address . */
	public static final String DELETE_CREDIT_CARD_SERVICE = "atg/userprofiling/B2CProfileFormHandler/removeCard";

	/** Service for getting the stores . */
	// public static final String
	// GET_STORE="ulta/mobile/utils/StoreLocatorFilter/fetchStore";

	public static final String GET_STORE = "ulta/mobile/storelocator/StoreLocatorFilter/fetchStores";

	/** Service for Electronic Gift Card. */
	public static final String ELECTRONIC_GIFT_CARD = "atg/commerce/order/ShoppingCartModifier/addEGiftCardToOrder";

	public static final String SAMPLE_EGIFT_CARD_LINK = "ulta/img/prodDetail/e-giftcard_sample.jpg";
	// ---------------------------------------------WEB SERVICES
	// END---------------------------------------------
	// ------------------------------POWER REVIEWS--WEB SERVICES
	// START-------------------------------------------
	/**
	 * Power reviews context : api.powerreviews.com
	 */
	public static final String POWER_REVIEWS_CONTEXT = "api.powerreviews.com";
	/**
	 * Write review context
	 */
	public static final String POWER_REVIEWS_WRITE_CONTEXT = "services.powerreviews.com";
	/**
	 * Power reviews API
	 */
	public static final String API_KEY_POWER_REVIEWS = "b60759f2-569d-417b-89bf-11b9bb93d9e7";
	/**
	 * Power reviews Merchant Id
	 */
	public static final String MERCHANT_ID_POWER_REVIEWS = "6406";
	/**
	 * Power reviews default page size Id
	 */
	public static final String PAGE_SIZE_POWER_REVIEWS = "10";
	/**
	 * Service for displaying the reviews.
	 */
	public static final String DISPLAY_REVIEWS_SERVICE = "display/reviews?";
	/**
	 * Service for displaying the reviews.
	 */
	public static final String DISPLAY_SNAPSHOT_SERVICE = "display/products/snapshot?";
	/**
	 * Service for writing the reviews.
	 */
	public static final String WRITE_REVIEWS_SERVICE = "api/reviews";

	// 3.5 web services for Q & A
	/**
	 * Service for writing the question for a product.
	 */
	public static final String WRITE_QUESTION_SERVICE = "api/questions";

	/**
	 * Service for fetching question count of a particular product
	 */
	public static final String GET_QUESTION_COUNT = "display/qa/question_count?";
	/**
	 * Service for fetching type of question user wants to enter in ask question
	 * screen
	 */
	public static final String TYPE_OF_QUESTION_SERVICE = "ulta/mobile/catalog/MobileCatalogServices/fetchTypeOfQuestion";

	public static final String GET_ANSWER_COUNT = "display/qa/answer_count?";
	/**
	 * Service for getting set of amenities or service types for filtering
	 */
	public static final String GET_BRANDS_FOR_FILTERING = "ulta/mobile/storelocator/StoreLocatorFilter/brandAmenityList";
	/**
	 * Service for checking availability of items in particular store
	 */
	public static final String DO_RTI_CHECK = "ulta/mobile/utils/MobileTools/doRTICheck";

	/** API KEY for questions and answers API */
	public static final String API_KEY_FOR_QnA = "c2187904-4cf2-4273-8052-eaa9ccc9d4fa";

	/**
	 * Q & A Merchant Id
	 */
	public static final String MERCHANT_ID_FOR_QnA = "6406";

	/**
	 * Service for getting list of questons for a product
	 */
	public static final String GET_QUESTIONS = "display/qa/questions-answers?";

	// --------------------------------POWER REVIEWS--WEB SERVICES
	// END---------------------------------------------

	/**
	 * Service for Add Favorites.
	 */
	public static final String ADD_FAVORITES_SERVICE = "atg/commerce/gifts/GiftlistFormHandler/addItemToFavorites";

	/**
	 * Service for View Favorites.
	 */
	public static final String VIEW_FAVORITES_SERVICE = "ulta/mobile/gifts/MobFavoritesHolder/mobileFavCart?";

	/**
	 * Service for Remove Favorites.
	 */
	public static final String REMOVE_FAVORITES_SERVICE = "atg/commerce/gifts/GiftlistFormHandler/deleteItemFromFavorites";

	// --------------------------------GOOGLE--------WEB SERVICES
	// START-------------------------------------------
	/**
	 * Service for handling GOOGLE Simple Access API Key
	 */
	public static final String GOOGLE_SIMPLE_ACCESS_API_KEY = "AIzaSyB9ofwmI6hdVnRMu3wpSTihSQ6_MX3PhF8";
	/**
	 * Service for handling GOOGLE URL Shortening
	 */
	public static final String GOOGLE_API_CONTEXT = "https://www.googleapis.com";
	/**
	 * Service for handling GOOGLE URL Shortening
	 */
	public static final String GOOGLE_URL_SHORTENING_SERVICE = GOOGLE_API_CONTEXT
			.concat("/urlshortener/v1/url?key=").concat(
					GOOGLE_SIMPLE_ACCESS_API_KEY);

	// --------------------------------GOOGLE--------WEB SERVICES
	// END---------------------------------------------

	// -----------------------------------OMINATURE---------------------------------------------------------------
	/*
	 * Constant for getting the device type
	 */
	public static final String DEVICE_TYPE = "Android";

	public static final String Channel = "ULTA Mobile App";

	public static final String REPORTING_SUITE = UltaDataCache
			.getDataCacheInstance().getReportingSuite();

	public static final String TRACKING_SERVER = UltaDataCache
			.getDataCacheInstance().getTrackingServer();

	// public static final String OMNITURE_SERVER = "ulta.122.2o7.net";

	// ------------------------------------OMINATURE
	// END-----------------------------------------------------------------------




	public static final String AUTHENTICATION_REQUIRED = "Authentication Required";




	// Constants in Map

	public static final int pointPosition = 1000000;

	// constants in omniture
	public static final String HOME_PAGE = "Homepage";
	public static final String HOME_PAGE_COUPON = "Homepage:Coupon";
	public static final String HOME_PAGE_WEEKILY_AD = "Weekly Ads";
	public static final String HOME_PAGE_NEW_ARRIVALS = "New Arrivals";
	public static final String HOME_PAGE_SALE = "Sale";
	public static final String HOME_PAGE_GIFT_CARD = "Gift Cards";
	public static final String HOME_PAGE_OLAPIC = "Olapic Gallery";
	public static final String FAVORITES_LOGGED_OUT = "Favorites - Logged Out";
	public static final String FAVORITES_LOGGED_IN = "Favorites - Logged In";
	public static final String SHOPPING_BAG = "Shopping Bag";
	public static final String SHIPPINGADDRESS_PAGE = "Checkout:Shipping";
	public static final String CHECKOUT = "Checkout:";
	public static final String LOGIN = "Login or Register";
	public static final String REGISTER = "Register";
	public static final String FREE_SAMPLES = "Checkout:Samples";
	public static final String SHOP = "Shop";
	public static final String SHOP_BY_BRANDS = "Shop By Brands";
	public static final String CATEGORY = "category:";
	public static final String LOCATE_STORE_MAP = "Stores:Map";
	public static final String LOCATE_STORE_LIST = "Stores:Stores";
	public static final String LOCATE_STORE_FILTER = "Stores:Filter";
	public static final String LOCATE_STORE_DETAILS = "Stores:Store Detail";
	public static final String MY_ACCOUNT = "My Account";
	public static final String GIFTCARD_BALANCE = "Gift Card Balance";
	public static final String HOME_MY_REWARDS_LOGGED_OUT = "My Rewards - Logged Out";
	public static final String HOME_MY_REWARDS_LOGGED_IN = "My Rewards - Logged In";
	public static final String PRIVACY_POLICY = "Info:Privacy Policy";
	public static final String LEGAL = "Info:Legal";
	public static final String ABOUT = "Info:About";
	public static final String ORDER_STATUS = "Account:Order Status Anonymous";
	public static final String ORDER_STATUS_LOGGED_IN = "Order History - Logged In";
	public static final String ORDER_STATUS_LOGGED_OUT = "Order Status - Logged Out";
	public static final String ACCOUNT_PAYMENT = "My Account:Payment";
	public static final String ACCOUNT_SHIPPING = "My Account:Shipping";
	public static final String ACCOUNT_ORDER_HISTORY = "Order History - Logged In";
	public static final String ACCOUNT_ORDER_HISTORY_TRACK = "Order History:Tracking Details";
	public static final String ACCOUNT_FAVORITES = "Favorites - Logged In";
	public static final String ACCOUNT_BEAUTY_PREFRENCES = "My Account:Beauty Preferences";
	public static final String VIEW_REWARDS_CARD = "ULTAmate Rewards:Rewards:Print Card";
	public static final String POINTS_HISTORY = "My Rewards:Point History";
	public static final String REWARDS_BENEFITS = "My Rewards:Benefits";
	public static final String REWARDS_BONUS_OFFER = "My Rewards:Bonus Offers";
	public static final String REWARDS_ALERT = "My Rewards:Alerts";
	public static final String REWARDS_EARNING_REDEEMING = "My Rewards:Earning and Redeeming";
	public static final String REWARDS_PLATINMUM_PROGRAM = "My Rewards:Platinum Program";
	public static final String ACCOUNT_FORGOT_PASSWORD_STEP1 = "Forgot Password";
	public static final String ACCOUNT_FORGOT_PASSWORD_THANKYOU = "Forgot Password:Thank You";
	public static final String ACCOUNT_CHANGE_PASSWORD_STEP1 = "My Account:Change Password";
	public static final String ACCOUNT_CHANGE_PASSWORD_THANKYOU = "Change Password:Thank You";
	public static final String ACCOUNT_MOBILE_OFFERS = "Mobile Offer";
	public static final String CHECKOUT_LOGIN = "Checkout:Login";
	public static final String CHECKOUT_LOGIN_GUEST = "Checkout:Login (Guest)";
	public static final String CHECKOUT_ADD_FREE_GIFT = "Shopping Bag:Free Gift";
	public static final String CHECKOUT_PAYPAL = "Checkout:Review Order - PayPal Express";
	public static final String CHECKOUT_FREESAMPLES_GUEST_MEMBER = "Checkout:Free Samples (Guest)";
	public static final String CHECKOUT_FREESAMPLES_LOYALITY_MEMBER = "Checkout:Free Samples (Reg - Loyalty Member)";
	public static final String CHECKOUT_FREESAMPLES_NON_LOYALITY_MEMBER = "Checkout:Free Samples (Reg - Non-Loyalty Member)";
	public static final String CHECKOUT_SHIPPING_ADDRESS_GUEST_MEMBER = "Checkout:Shipping Address (Guest)";
	public static final String CHECKOUT_SHIPPING_ADDRESS_LOYALITY_MEMBER = "Checkout:Shipping Address (Reg - Loyalty Member)";
	public static final String CHECKOUT_SHIPPING_ADDRESS_NON_LOYALITY_MEMBER = "Checkout:Shipping Address (Reg - Non-Loyalty Member)";
	public static final String CHECKOUT_SHIPPING_ADDRESS_VERIFICATION_GUEST_MEMBER = "Checkout:Shipping Address Verification (Guest)";
	public static final String CHECKOUT_SHIPPING_ADDRESS_VERIFICATION_LOYALITY_MEMBER = "Checkout:Shipping Address Verification (Reg - Loyalty Member)";
	public static final String CHECKOUT_SHIPPING_ADDRESS_VERIFICATION_NON_LOYALITY_MEMBER = "Checkout:Shipping Address Verification (Reg - Non-Loyalty Member)";
	public static final String CHECKOUT_SHIPPING_METHOD_GUEST_MEMBER = "Checkout:Shipping Method (Guest)";
	public static final String CHECKOUT_SHIPPING_METHOD_LOYALITY_MEMBER = "Checkout:Shipping Method (Reg - Loyalty Member)";
	public static final String CHECKOUT_SHIPPING_METHOD_NON_LOYALITY_MEMBER = "Checkout:Shipping Method (Reg - Non-Loyalty Member)";
	public static final String CHECKOUT_ORDER_CONFIRMATION_GUEST_MEMBER = "Checkout:Order Confirmation (Guest)";
	public static final String CHECKOUT_ORDER_CONFIRMATION_LOYALITY_MEMBER = "Checkout:Order Confirmation (Reg - Loyalty Member)";
	public static final String CHECKOUT_ORDER_CONFIRMATION_NON_LOYALITY_MEMBER = "Checkout:Order Confirmation (Reg - Non-Loyalty Member)";
	public static final String SCAN = "Scan";
	public static final String SEARCH_SUCCESS = "Search Result";
	public static final String SEARCH_FAILURE = "Search - No Results";
	public static final String ORDERCONFIRMATION_GUEST_PAGE = "Checkout:Order Confirmation (Guest)";
	public static final String ORDERCONFIRMATION_LOYALITY_PAGE = "Checkout:Order Confirmation (Reg - Loyalty Member)";
	public static final String ORDERCONFIRMATION_NON_LOYALITY_PAGE = "Checkout:Order Confirmation (Reg - Non-Loyalty Member)";
	public static final String PAYMENT_GUEST_PAGE = "Checkout:Payment (Guest)";
	public static final String PAYMENT_LOYALITY_PAGE = "Checkout:Payment (Reg - Loyalty Member)";
	public static final String PAYMENT_NON_LOYALITY_PAGE = "Checkout:Payment (Reg - Non-Loyalty Member)";
	public static final String REVIEW_ORDER_GUEST_PAGE = "Checkout:Review Order (Guest)";
	public static final String REVIEW_ORDER_LOYALITY_PAGE = "Checkout:Review Order (Reg - Loyalty Member)";
	public static final String REVIEW_ORDER_NON_LOYALITY_PAGE = "Checkout:Review Order (Reg - Non-Loyalty Member)";

	public static final String COUPON_CODE_KEY = "e.11";
	public static final String ORDER_ID_KEY = "e.16";
	public static final String COUPON_REDEMPTION_KEY = "e.64";
	public static final String LOYALTY_CODE_KEY = "e.21";
	public static final String PAYMENT_TYPE_KEY = "e.17";
	public static final String LOYALTY_OFFER_ACTIVATION_KEY = "e.70";
	public static final String CURRENCY_ACTION = "a.CurrencyCode";
	public static final String CURRENCY_KEY = "Currency Code";
	public static final String ORDERS_ACTION = "a.orders";
	public static final String ORDERS_KEY = "Orders";
	public static final String PURCHASE_ACTION = "a.purchaseID";
	public static final String PURCHASE_KEY = "Purchase ID";

	public static final String ERROR_CODE_KEY = "e.37";



	public static final String ERROR_CODE_EVENT_ACTION = "a.41";
	public static final String LOYALTY_ACCOUNT_CREATED_EVENT_ACTION = "a.5";
	public static final String CHECKOUT_STEP_1_EVENT_ACTION = "a.55";
	public static final String CHECKOUT_STEP_1_VISIT_EVENT_ACTION = "a.62";
	public static final String CHECKOUT_STEP_2_EVENT_ACTION = "a.56";
	public static final String CHECKOUT_STEP_2_VISIT_EVENT_ACTION = "a.63";
	public static final String CHECKOUT_STEP_3_EVENT_ACTION = "a.49";
	public static final String CHECKOUT_STEP_3_VISIT_EVENT_ACTION = "a.64";
	public static final String CHECKOUT_STEP_4_EVENT_ACTION = "a.50";
	public static final String CHECKOUT_STEP_4_VISIT_EVENT_ACTION = "a.65";
	public static final String CHECKOUT_STEP_5_EVENT_ACTION = "a.57";
	public static final String CHECKOUT_STEP_5_VISIT_EVENT_ACTION = "a.66";
	public static final String CHECKOUT_STEP_6_EVENT_ACTION = "a.58";
	public static final String CHECKOUT_STEP_6_VISIT_EVENT_ACTION = "a.67";
	public static final String CHECKOUT_STEP_7_EVENT_ACTION = "a.59";
	public static final String CHECKOUT_STEP_7_VISIT_EVENT_ACTION = "a.68";
	public static final String CHECKOUT_STEP_8_EVENT_ACTION = "a.60";
	public static final String CHECKOUT_STEP_8_VISIT_EVENT_ACTION = "a.69";

	public static final String LOGIN_ACTION = "a.13";
	public static final String REGISTRATION_ACTION = "a.14";
	public static final String REAL_PRODUCT_VIEWS = "a.21";
	public static final String PRODUCT_VIEW = "prodView";

	public static final String EVENT_SC_ADD = "a.scAdd";
	public static final String EVENT_SC_OPEN = "a.scOpen";
	public static final String EVENT_SC_REMOVE = "a.scRemove";
	public static final String EVENT_SC_VIEW = "a.scView";
	public static final String EVENT_SC_CHECKOUT = "a.scCheckout";
	public static final String EVENT_PURCHASE_ID = "a.purchaseID";
	public static final String EVENT_SHIPPING = "event9";
	public static final String EVENT_TAX = "event10";
	public static final String EVENT_COUPON_AMOUNT = "event11";

	public static final String PRODUCTS_KEY = "&&products";
	public static final String EVENT_KEY = "&&events";

	// Smart Link

	public static final String SMART_LINK_HOME = "home";
	public static final String SMART_LINK_SHOP = "shop";
	public static final String SMART_LINK_STORE = "store";
	public static final String SMART_LINK_SALE = "sale";
	public static final String SMART_LINK_NEW_ARRIVAL = "newarrival";

	//conversant tag

	public static final String[] LOGIN_EVENTS={"SignIn","login-success"};
	public static final String LOGIN_GROUP="SignIn";

	public static final String[] APP_lAUNCH_EVENTS={"ApplicationOpen","launch-success"};
	public static final String APP_lAUNCH_GROUP="ApplicationOpen";

	public static final String[] ORDER_EVENTS={"OrderComplete","order"};
	public static final String ORDER_GROUP="OrderComplete";

    //App permissions

    //Request code
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PHONE_REQUEST_CODE = 2;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 3;
    public static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 4;

    //permission names
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    //alert messages
    public static final String PERMISSION_CALL_PHONE_DIALOG_TITLE = "Access Phone Call";
    public static final String PERMISSION_CALL_PHONE_DIALOG_MESSAGE = "Permission required to contact Ulta by phone";
    public static final String PERMISSION_CAMERA_DIALOG_TITLE = "Access Camera";
    public static final String PERMISSION_CAMERA_DIALOG_MESSAGE = "Permission required to access device camera";
    public static final String WRITE_EXTERNAL_STORAGE_DIALOG_TITLE = "Access External Storage";
    public static final String WRITE_EXTERNAL_STORAGE_DIALOG_MESSAGE = "Permission required to save & share your look";
    public static final String ACCESS_FINE_LOCATION_DIALOG_TITLE = "Access Location Service";
    public static final String ACCESS_FINE_LOCATION_DIALOG_MESSAGE = "Permission required to find Ulta stores near you";

    //permission denied messages

    public static final String PERMISSION_CALL_PHONE_DENIED = "Permission Denied, You cannot access phone call.Please change in settings";
    public static final String PERMISSION_CAMERA_DENIED = "Permission Denied, You cannot access Camera.Please change in settings";
    public static final String WRITE_EXTERNAL_STORAGE_DENIED = "Permission Denied, You cannot access Storage.Please change in settings";
    public static final String ACCESS_FINE_LOCATION_DENIED = "Permission Denied, You cannot access Location Services.Please change in settings";



	//payment page card validation error message

	public static final String ENTER_CARD_HOLDER_NAME="Fill the cardholder's name.";
	public static final String ENTER_CARD_NUMBER="Fill the card number.";
	public static final String INVALID_CREDIT_CARD="The card number is not valid.";
	public static final String SELECT_EXPIRY_MONTH="Select Expiration Month.";
	public static final String SELECT_EXPIRY_YEAR="Select Expiration Year.";
	public static final String ENTER_SECURITY_CODE="Fill the Security Code.";

	//My Account->Payment->Add card page validation error message

	public static final String ENTER_CARD_HOLDER_NICK_NAME="Fill the cardholder's Nickname.";

	//My Account rewards Sign Up Success message
	public static final String JOIN_REWARDS_SUCCESS="Congratulations! Welcome to Ultamate Rewards!";
	public static final String ACTIVATE_REWARDS_SUCCESS="Congratulations! You have linked your membership!";

	//Create Account Success Message
	public static final String CREATE_ACCOUNT_SUCCESS="Congratulations! You have created your account!";
	public static final String CREATE_ACCOUNT_JOIN_REWARDS_SUCCESS="Congratulations! You have created your account & joined Ultamate Rewards!";
	public static final String CREATE_ACCOUNT_ACTIVATE_REWARDS_SUCCESS="Congratulations! You have created your account & linked your membership!";

	//Apply now user already having card
	public static final String USER_HAVING_CARD_MESSAGE="It looks like you already have an Ultamate Rewards credit card. For assistance, please call Guest Services at 1-866-983-8482.";

	//cash start sdk enable/disalble
	public static  boolean isCashStarSDKEnabled=false;

	//Home Page Ultamate Card SUb text
	public static final String GUEST_USER_SUB_TEXT="Earn even more Ultamate Rewards points. Apply Now!";
	public static final String LOGGED_IN_WITH_CARD_USER_SUB_TEXT="Manage my Ultamate Rewards Credit Card";

	public static final String CREDITCARD_FAQ_URL="http://www.ulta.com/mobile/app_faq.html";
	public static final String CREDITCARD_TERM_CONDITION_URL=" http://www.ulta.com/mobile/app_privacy.html";

	public static final String GIFTCARD_FAQ_URL="http://www.ulta.com/mobile/app_giftcard_faq.html";
	public static final String GIFTCARD_TERM_CONDITION_URL="http://www.ulta.com/mobile/app_giftcard_terms.html";

	public static final String CORPORATE_GIFTCARD_ALERT_TITLE="Corporate Gift Cards";
	public static final String CORPORATE_GIFTCARD_ALERT_MESSAGE="Contact us at giftcards@ulta.com to find out about the options available.";




}
