/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util.caching;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ulta.core.bean.account.AppConfigurableBean;
import com.ulta.core.bean.account.AtgResponseBean;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.checkout.GuestUserDataBean;
import com.ulta.core.bean.product.CategoryBean;
import com.ulta.core.bean.product.RedeemPointBean;
import com.ulta.core.bean.product.StoreBrandFiler;
import com.ulta.core.bean.product.TypeOfQuestionBean;
import com.ulta.core.bean.store.StoreDetailBean;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

/**
 * The Class UltaDataCache.
 *
 * @author Infosys The cache application for Ulta Data Cache
 */

public class UltaDataCache {

	/**
	 * The cache instance for temporary storage.
	 */
	private static UltaDataCache dataCacheInstance;

	/**
	 * Gets the single instance of UltaDataCache.
	 * 
	 * @return single instance of UltaDataCache
	 */
	public static UltaDataCache getDataCacheInstance() {
		if (dataCacheInstance == null) {
			dataCacheInstance = new UltaDataCache();
		}
		return dataCacheInstance;
	}
	private String rewardsBeautyClubNumber;
	/**
	 * The App Environment.
	 */
	private String appEnvironment;

	/** The serverAddress. */
	private String serverAddress;

	private ArrayList<RedeemPointBean> redeemLevelPoints;

	public ArrayList<RedeemPointBean> getRedeemLevelPoints() {
		return redeemLevelPoints;
	}

	public void setRedeemLevelPoints(
			ArrayList<RedeemPointBean> redeemLevelPoints) {
		this.redeemLevelPoints = redeemLevelPoints;
	}

	/** The isLogEnabled. */
	private boolean isLogEnabled;

	/** The isBeanLegibilityEnabled. */
	private boolean isBeanLegibilityEnabled;

	private boolean onlyEgiftCard = false;

	private boolean errorHappened = false;

	private boolean redeemPoints = false;

	private boolean showButterfly=false;

	private AppConfigurableBean butterflyResponse=null;

	public boolean isRedeemPoints() {
		return redeemPoints;
	}

	public void setRedeemPoints(boolean redeemPoints) {
		this.redeemPoints = redeemPoints;
	}

	/**
	 * The indicator for if the values are loaded from properties file.
	 */
	private boolean isLoadedFromProperties = false;

	// 3.2 release
	/** Indicator to know whether user is anonymous or logged in */
	private boolean isAnonymousCheckout = false;
	/** Indicator to know order is submitted or not */
	private boolean isOrderSubmitted = false;

	/** Variable to know whether it is express checkout or not */
	private boolean isExpressCheckout = false;

	// 3.5 release
	private boolean isQuestionSubmitted = false;
	private TypeOfQuestionBean typeOfQuestionResponse;

	/** The storage for login details. */
	private SharedPreferences storedLoginPreference;

	/** The variable to show the logged in status. */
	private boolean isLoggedIn = false;

	private boolean isRewardMember = false;

	/** Flag to maintain the image in add Recipe. */
	private boolean isFromAddRecipe;

	/** Variable for getting the user name */
	private String loginName;

	private String rootCategory;

	private String category;

	private String productName;

	private String productSkuName;
	private String brandName;
	private List<StoreBrandFiler> storeBrandFilters;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductSkuName() {
		return productSkuName;
	}

	public void setProductSkuName(String productSkuName) {
		this.productSkuName = productSkuName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRootCategory() {
		return rootCategory;
	}

	public void setRootCategory(String rootCategory) {
		this.rootCategory = rootCategory;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * Olapic Changes
	 * 
	 */

	private boolean isOlapic;

	private String OlapicResponse;

	private boolean isOlapicProdDetails;

	private String olapicProdDetailsResponse;

	private boolean isStaySignedIn = true;

	private String cameraCapturedPath;

	private boolean fromCamera;

	private String encryptionKey;

	private String streamId;

	private String cameraCaption;

	private String couponCode = " ";

	private String products;

	private boolean isAppClosed;

	private boolean isAppLaunched = true;



	private boolean isThirdPartySDKEnabled =false;

	private byte[] encodedUserNameBytes;

	private byte[] encodedPasswordBytes;

	private String OlpaicHomeGalleryHeadingText = "#ULTA GALLERY";

	private SecretKeySpec secretKey;

	private double couponAmount;

	private String errorMessage;

	private String LoyaltyClubId;

	private String giftWrapPrice;

	private String paymentType;

	private String finalEvents;

	private boolean isAskRelogin = false;

	private String rewardsBalancePoints;

	private boolean isAddedTofavorites;

	private String quantity;

	private boolean isGiftTheOrder = false;

	private String orderTotal = "0.00";

	private int itemsInBasket;

	private String userFirstName = "";

	private String shippingType;

	private String giftCardUrl;

	private boolean isFreeSampleSelected;

	private Bitmap downloadedImage;

	private Bitmap downloadProdDetailsImage;

	private int imageInPlpPosition;

	private ImageView transitionView;

	private LinearLayout pinchZoomHeaderLayout;
	
	private String filepathToBeUploaded;
	
	private boolean ifNotSignedInClearSession;
	
	private boolean updateBasketAndFavCount;
	
	private boolean isCalledOnlyOnce = false;

	private String weeklyAdURL;

	public String getWeeklyAdURL() {
		return weeklyAdURL;
	}

	public void setWeeklyAdURL(String weeklyAdURL) {
		this.weeklyAdURL = weeklyAdURL;
	}

	public boolean isCalledOnlyOnce() {
		return isCalledOnlyOnce;
	}

	public void setCalledOnlyOnce(boolean isCalledOnlyOnce) {
		this.isCalledOnlyOnce = isCalledOnlyOnce;
	}

	public boolean isUpdateBasketAndFavCount() {
		return updateBasketAndFavCount;
	}

	public void setUpdateBasketAndFavCount(boolean updateBasketAndFavCount) {
		this.updateBasketAndFavCount = updateBasketAndFavCount;
	}

	public boolean isIfNotSignedInClearSession() {
		return ifNotSignedInClearSession;
	}

	public void setIfNotSignedInClearSession(boolean ifNotSignedInClearSession) {
		this.ifNotSignedInClearSession = ifNotSignedInClearSession;
	}

	public String getFilepathToBeUploaded() {
		return filepathToBeUploaded;
	}

	public void setFilepathToBeUploaded(String filepathToBeUploaded) {
		this.filepathToBeUploaded = filepathToBeUploaded;
	}

	public LinearLayout getPinchZoomHeaderLayout() {
		return pinchZoomHeaderLayout;
	}

	public void setPinchZoomHeaderLayout(LinearLayout pinchZoomHeaderLayout) {
		this.pinchZoomHeaderLayout = pinchZoomHeaderLayout;
	}

	public ImageView getTransitionView() {
		return transitionView;
	}

	public void setTransitionView(ImageView transitionView) {
		this.transitionView = transitionView;
	}

	public int getImageInPlpPosition() {
		return imageInPlpPosition;
	}

	public void setSetImageInPlpPosition(int imageInPlpPosition) {
		this.imageInPlpPosition = imageInPlpPosition;
	}

	private HashMap<Integer, Bitmap> plpHashMapOfImages = new HashMap<Integer, Bitmap>();

	private HashMap<Integer, Bitmap> pdpHashMapOfImages = new HashMap<Integer, Bitmap>();

	public HashMap<Integer, Bitmap> getPdpHashMapOfImages() {
		return pdpHashMapOfImages;
	}

	public void setPdpHashMapOfImages(int pos, Bitmap bitmap) {
		this.pdpHashMapOfImages.put(pos, bitmap);
	}

	public HashMap<Integer, Bitmap> getPlpHashMapOfImages() {
		return plpHashMapOfImages;
	}

	public void setPlpHashMapOfImages(int pos, Bitmap bitmap) {
		this.plpHashMapOfImages.put(pos, bitmap);
	}

	public Bitmap getDownloadProdDetailsImage() {
		return downloadProdDetailsImage;
	}

	public void setDownloadProdDetailsImage(Bitmap downloadProdDetailsImage) {
		this.downloadProdDetailsImage = downloadProdDetailsImage;
	}

	public Bitmap getDownloadedImage() {
		return downloadedImage;
	}

	public void setDownloadedImage(Bitmap downloadedImage) {
		this.downloadedImage = downloadedImage;
	}

	public boolean isFreeSampleSelected() {
		return isFreeSampleSelected;
	}

	public void setFreeSampleSelected(boolean isFreeSampleSelected) {
		this.isFreeSampleSelected = isFreeSampleSelected;
	}

	public String getGiftCardUrl() {
		return giftCardUrl;
	}

	public void setGiftCardUrl(String giftCardUrl) {
		this.giftCardUrl = giftCardUrl;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public int getItemsInBasket() {
		return itemsInBasket;
	}

	public void setItemsInBasket(int itemsInBasket) {
		this.itemsInBasket = itemsInBasket;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public boolean isGiftTheOrder() {
		return isGiftTheOrder;
	}

	public void setGiftTheOrder(boolean isGiftTheOrder) {
		this.isGiftTheOrder = isGiftTheOrder;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public boolean isAddedTofavorites() {
		return isAddedTofavorites;
	}

	public void setAddedTofavorites(boolean isAddedTofavorites) {
		this.isAddedTofavorites = isAddedTofavorites;
	}

	public String getRewardsBalancePoints() {
		return rewardsBalancePoints;
	}

	public void setRewardsBalancePoints(String rewardsBalancePoints) {
		this.rewardsBalancePoints = rewardsBalancePoints;
	}

	public boolean isAskRelogin() {
		return isAskRelogin;
	}

	public void setAskRelogin(boolean isAskRelogin) {
		this.isAskRelogin = isAskRelogin;
	}

	public String getFinalEvents() {
		return finalEvents;
	}

	public void setFinalEvents(String finalEvents) {
		this.finalEvents = finalEvents;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getGiftWrapPrice() {
		return giftWrapPrice;
	}

	public void setGiftWrapPrice(String giftWrapPrice) {
		this.giftWrapPrice = giftWrapPrice;
	}

	public String getLoyaltyClubId() {
		return LoyaltyClubId;
	}

	public void setLoyaltyClubId(String loyaltyClubId) {
		LoyaltyClubId = loyaltyClubId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public SecretKeySpec getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(SecretKeySpec secretKey) {
		this.secretKey = secretKey;
	}

	public String getOlpaicHomeGalleryHeadingText() {
		return OlpaicHomeGalleryHeadingText;
	}

	public void setOlpaicHomeGalleryHeadingText(
			String olpaicHomeGalleryHeadingText) {
		OlpaicHomeGalleryHeadingText = olpaicHomeGalleryHeadingText;
	}

	public byte[] getEncodedUserNameBytes() {
		return encodedUserNameBytes;
	}

	public void setEncodedUserNameBytes(byte[] encodedUserNameBytes) {
		this.encodedUserNameBytes = encodedUserNameBytes;
	}

	public byte[] getEncodedPasswordBytes() {
		return encodedPasswordBytes;
	}

	public void setEncodedPasswordBytes(byte[] encodedPasswordBytes) {
		this.encodedPasswordBytes = encodedPasswordBytes;
	}

	public boolean isAppClosed() {
		return isAppClosed;
	}

	public void setAppClosed(boolean isAppClosed) {
		this.isAppClosed = isAppClosed;
	}

	public boolean isAppLaunched() {
		return isAppLaunched;
	}

	public void setAppLaunched(boolean isAppLaunched) {
		this.isAppLaunched = isAppLaunched;
	}

	public boolean isThirdPartySDKEnabled() {
		return isThirdPartySDKEnabled;
	}

	public void setIsThirdPartySDKEnabed(boolean isThirdPartySDKEnabed) {
		this.isThirdPartySDKEnabled = isThirdPartySDKEnabed;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCameraCaption() {
		return cameraCaption;
	}

	public void setCameraCaption(String cameraCaption) {
		this.cameraCaption = cameraCaption;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public boolean isFromCamera() {
		return fromCamera;
	}

	public void setFromCamera(boolean fromCamera) {
		this.fromCamera = fromCamera;
	}

	public String getCameraCapturedPath() {
		return cameraCapturedPath;
	}

	public void setCameraCapturedPath(String cameraCapturedPath) {
		this.cameraCapturedPath = cameraCapturedPath;
	}

	public boolean isStaySignedIn() {
		return isStaySignedIn;
	}

	public void setStaySignedIn(boolean isStaySignedIn) {
		this.isStaySignedIn = isStaySignedIn;
	}

	public boolean isOlapicProdDetails() {
		return isOlapicProdDetails;
	}

	public void setOlapicProdDetails(boolean isOlapicProdDetails) {
		this.isOlapicProdDetails = isOlapicProdDetails;
	}

	public String getOlapicProdDetailsResponse() {
		return olapicProdDetailsResponse;
	}

	public void setOlapicProdDetailsResponse(String olapicProdDetailsResponse) {
		this.olapicProdDetailsResponse = olapicProdDetailsResponse;
	}

	private ArrayList<String> shopThisLookHref = new ArrayList<String>();
	
	private ArrayList<String> shopThisLookPDPHref = new ArrayList<String>();

	public ArrayList<String> getShopThisLookPDPHref() {
		return shopThisLookPDPHref;
	}

	public void setShopThisLookPDPHref(String shopThisLookPDPHref) {
		if (null!=shopThisLookPDPHref) {
			this.shopThisLookPDPHref.add(shopThisLookPDPHref);
		}
		else
		{
			this.shopThisLookPDPHref.clear();
		}
	}

	public ArrayList<String> getShopThisLookHref() {
		return shopThisLookHref;
	}

	public void setShopThisLookHref(String shopThisLookHref) {
		if (null!=shopThisLookHref) {
			this.shopThisLookHref.add(shopThisLookHref);
		}
	}

	public String getOlapicResponse() {
		return OlapicResponse;
	}

	public void setOlapicResponse(String OlapicResponse) {

		this.OlapicResponse = OlapicResponse;
	}

	public boolean isOlapic() {
		return isOlapic;
	}

	public void setOlapic(boolean isOlapic) {
		this.isOlapic = isOlapic;
	}

	private Location userLocation;

	/** HashMap with free samples selected. */
	private HashMap<String, String> hmWithFreeSamplesSelected;

	/** HashMap for freegifts with productid of parent and skuid of child. */
	private HashMap<String, String> hmWithFreeGifts;
	private HashMap<String, String> hmWithFreeGiftAndPromId;
	private HashMap<String, String> hmWithPromoAndFreeGifts;
	private HashMap<String, String> hmWithFreeGiftsReverse;

	private List<StoreDetailBean> stores;
	private int storeBeingViewed;

	/** LIST OF STATES. */
	private List<String> stateList;

	/** The category beans list. */
	List<CategoryBean> categoryBeansList;

	// Guest user details

	GuestUserDataBean guestUserDataBean;
	private HashMap<String, String> guestUserDeatails;

	public HashMap<String, String> getGuestUserDeatails() {
		return guestUserDeatails;
	}

	public void setGuestUserDeatails(HashMap<String, String> guestUserDeatails) {
		this.guestUserDeatails = guestUserDeatails;
	}

	public GuestUserDataBean getGuestUserDataBean() {
		return guestUserDataBean;
	}

	public void setGuestUserDataBean(GuestUserDataBean guestUserDataBean) {
		this.guestUserDataBean = guestUserDataBean;
	}

	public List<CategoryBean> getCategoryBeansList() {
		return categoryBeansList;
	}

	public void setCategoryBeansList(List<CategoryBean> categoryBeansList) {
		this.categoryBeansList = categoryBeansList;
	}

	/**
	 * the name of the page which need to be brought to front. This is used in
	 * checkout flow alone. Not to be reused anywhere else.
	 */
	private String moveBackTo;

	/**
	 * The flag will be set if the user is retracing the steps in checkout flow.
	 * Not to be reused anywhere else.
	 */
	private boolean isMovingBackInChekout;

	/**
	 * Gets the name of the page which need to be brought to front.
	 * 
	 * @return the name of the page which need to be brought to front
	 */
	public String getMoveBackTo() {
		return moveBackTo;
	}

	/**
	 * Sets the name of the page which need to be brought to front.
	 * 
	 * @param moveBackTo
	 *            the new name of the page which need to be brought to front
	 */
	public void setMoveBackTo(String moveBackTo) {
		this.moveBackTo = moveBackTo;
	}

	/**
	 * Checks if is moving back in chekout.
	 * 
	 * @return true, if is moving back in chekout
	 */
	public boolean isMovingBackInChekout() {
		return isMovingBackInChekout;
	}

	/**
	 * Sets the moving back in chekout.
	 * 
	 * @param isMovingBackInChekout
	 *            the new moving back in chekout
	 */
	public void setMovingBackInChekout(boolean isMovingBackInChekout) {
		this.isMovingBackInChekout = isMovingBackInChekout;
	}

	/**
	 * Gets the lIST OF STATES.
	 * 
	 * @return the lIST OF STATES
	 */
	public List<String> getStateList() {
		return stateList;
	}

	/**
	 * Sets the lIST OF STATES.
	 * 
	 * @param stateList
	 *            the new lIST OF STATES
	 */
	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}

	/**
	 * Gets the hashMap with free samples selected.
	 * 
	 * @return the hmWithFreeSamplesSelected
	 */
	public HashMap<String, String> getHmWithFreeSamplesSelected() {
		return hmWithFreeSamplesSelected;
	}

	/**
	 * Sets the hashMap with free samples selected.
	 * 
	 * @param hmWithFreeSamplesSelected
	 *            the hmWithFreeSamplesSelected to set
	 */
	public void setHmWithFreeSamplesSelected(
			HashMap<String, String> hmWithFreeSamplesSelected) {
		this.hmWithFreeSamplesSelected = hmWithFreeSamplesSelected;
	}

	/**
	 * Gets the serverAddress.
	 * 
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Sets the serverAddress.
	 * 
	 * @param serverAddress
	 *            the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * Checks if is log enabled.
	 * 
	 * @return the isLogEnabled
	 */
	public boolean isLogEnabled() {
		return isLogEnabled;
	}

	/**
	 * Sets the log enabled.
	 * 
	 * @param isLogEnabled
	 *            the isLogEnabled to set
	 */
	public void setLogEnabled(boolean isLogEnabled) {
		this.isLogEnabled = isLogEnabled;
	}

	/**
	 * Checks if is bean legibility enabled.
	 * 
	 * @return the isBeanLegibilityEnabled
	 */
	public boolean isBeanLegibilityEnabled() {
		return isBeanLegibilityEnabled;
	}

	/**
	 * Sets the bean legibility enabled.
	 * 
	 * @param isBeanLegibilityEnabled
	 *            the isBeanLegibilityEnabled to set
	 */
	public void setBeanLegibilityEnabled(boolean isBeanLegibilityEnabled) {
		this.isBeanLegibilityEnabled = isBeanLegibilityEnabled;
	}

	/**
	 * Sets the storage for login details.
	 * 
	 * @param storedLoginPreference
	 *            the storedLoginPreference to set
	 */
	public void setStoredLoginPreference(SharedPreferences storedLoginPreference) {
		this.storedLoginPreference = storedLoginPreference;
	}

	/**
	 * Gets the storage for login details.
	 * 
	 * @return the storedLoginPreference
	 */
	public SharedPreferences getStoredLoginPreference() {
		return storedLoginPreference;
	}

	/**
	 * Sets the logged in.
	 * 
	 * @param isLoggedIn
	 *            the new logged in
	 */
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	/**
	 * Checks if is logged in.
	 * 
	 * @return true, if is logged in
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	/**
	 * Gets the App Environment.
	 * 
	 * @return the appEnvironment
	 */
	public String getAppEnvironment() {
		return appEnvironment;
	}

	/**
	 * Sets the App Environment.
	 * 
	 * @param appEnvironment
	 *            the appEnvironment to set
	 */
	public void setAppEnvironment(String appEnvironment) {
		this.appEnvironment = appEnvironment;
	}

	// Setter getter methods and variables for checkout page to be removed later

	/**
	 * Checks if is loaded from properties.
	 * 
	 * @return the isLoadedFromProperties
	 */
	public boolean isLoadedFromProperties() {
		return isLoadedFromProperties;
	}

	/**
	 * Sets the loaded from properties.
	 * 
	 * @param isLoadedFromProperties
	 *            the isLoadedFromProperties to set
	 */
	public void setLoadedFromProperties(boolean isLoadedFromProperties) {
		this.isLoadedFromProperties = isLoadedFromProperties;
	}

	public HashMap<String, String> getHmWithFreeGifts() {
		return hmWithFreeGifts;
	}

	public void setHmWithFreeGifts(HashMap<String, String> hmWithFreeGifts) {
		this.hmWithFreeGifts = hmWithFreeGifts;
	}

	/**
	 * Checks if is from add recipe.
	 * 
	 * @return the isFromAddRecipe
	 */
	public boolean isFromAddRecipe() {
		return isFromAddRecipe;
	}

	/**
	 * Sets the from add recipe.
	 * 
	 * @param isFromAddRecipe
	 *            the isFromAddRecipe to set
	 */
	public void setFromAddRecipe(boolean isFromAddRecipe) {
		this.isFromAddRecipe = isFromAddRecipe;
	}

	/**
	 * Sets the shipping address.
	 * 
	 * @param shippingAddress
	 *            the shipping address
	 */
	public void setShippingAddress(HashMap<String, String> shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * Gets the shipping address.
	 * 
	 * @return the shipping address
	 */
	public HashMap<String, String> getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * Sets the shipping method.
	 * 
	 * @param shippingMethod
	 *            the shipping method
	 */
	public void setShippingMethod(HashMap<String, String> shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * Gets the shipping method.
	 * 
	 * @return the shipping method
	 */
	public HashMap<String, String> getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * Sets the gift option.
	 * 
	 * @param giftOption
	 *            the gift option
	 */
	public void setGiftOption(HashMap<String, String> giftOption) {
		this.giftOption = giftOption;
	}

	/**
	 * Gets the gift option.
	 * 
	 * @return the gift option
	 */
	public HashMap<String, String> getGiftOption() {
		return giftOption;
	}

	/**
	 * Sets the payment method.
	 * 
	 * @param paymentMethod
	 *            the payment method
	 */
	public void setPaymentMethod(HashMap<String, String> paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Gets the payment method.
	 * 
	 * @return the payment method
	 */
	public HashMap<String, String> getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Sets the billing address.
	 * 
	 * @param billingAddress
	 *            the billing address
	 */
	public void setBillingAddress(HashMap<String, String> billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * Gets the billing address.
	 * 
	 * @return the billing address
	 */
	public HashMap<String, String> getBillingAddress() {
		return billingAddress;
	}

	/**
	 * Sets the address book.
	 * 
	 * @param addressBook
	 *            the address book
	 */
	public void setAddressBook(HashMap<String, String> addressBook) {
		this.addressBook = addressBook;
	}

	/**
	 * Gets the address book.
	 * 
	 * @return the address book
	 */
	public HashMap<String, String> getAddressBook() {
		return addressBook;
	}

	/**
	 * Gets the credit card details.
	 * 
	 * @return the credit card details
	 */
	public HashMap<String, String> getCreditCardDetails() {
		return creditCardDetails;
	}

	/**
	 * Sets the credit card details.
	 * 
	 * @param creditCardDetails
	 *            the credit card details
	 */
	public void setCreditCardDetails(HashMap<String, String> creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}

	/** The address book. */
	private HashMap<String, String> addressBook;

	/** The shipping address. */
	private HashMap<String, String> shippingAddress;

	private String giftCards;

	/** The shipping method. */
	private HashMap<String, String> shippingMethod;

	/** The gift option. */
	private HashMap<String, String> giftOption;

	/** The payment method. */
	private HashMap<String, String> paymentMethod;

	/** The billing address. */
	private HashMap<String, String> billingAddress;

	/** The credit card details. */
	private HashMap<String, String> creditCardDetails;

	/** The file logging enabled. */
	private boolean fileLoggingEnabled;

	/** The cookie handling enabled. */
	private boolean cookieHandlingEnabled;

	/**
	 * Checks if is file logging enabled.
	 * 
	 * @return the fileLoggingEnabled
	 */
	public boolean isFileLoggingEnabled() {
		return fileLoggingEnabled;
	}
	public String getRewardsBeautyClubNumber() {
		return rewardsBeautyClubNumber;
	}

	public void setRewardsBeautyClubNumber(String rewardsBeautyClubNumber) {
		this.rewardsBeautyClubNumber = rewardsBeautyClubNumber;
	}

	/**
	 * Sets the file logging enabled.
	 * 
	 * @param fileLoggingEnabled
	 *            the fileLoggingEnabled to set
	 */
	public void setFileLoggingEnabled(boolean fileLoggingEnabled) {
		this.fileLoggingEnabled = fileLoggingEnabled;
	}

	/**
	 * Checks if is cookie handling enabled.
	 * 
	 * @return the cookieHandlingEnabled
	 */
	public boolean isCookieHandlingEnabled() {
		return cookieHandlingEnabled;
	}

	/**
	 * Sets the cookie handling enabled.
	 * 
	 * @param cookieHandlingEnabled
	 *            the cookieHandlingEnabled to set
	 */
	public void setCookieHandlingEnabled(boolean cookieHandlingEnabled) {
		this.cookieHandlingEnabled = cookieHandlingEnabled;
	}

	/**
	 * variable for storing the Ulta key store.
	 */
	private KeyStore ultaSecureStore;

	/**
	 * Gets the variable for storing the Ulta key store.
	 * 
	 * @return the ultaSecureStore
	 */
	public KeyStore getUltaSecureStore() {
		return ultaSecureStore;
	}

	/**
	 * Sets the variable for storing the Ulta key store.
	 * 
	 * @param ultaSecureStore
	 *            the ultaSecureStore to set
	 */
	public void setUltaSecureStore(KeyStore ultaSecureStore) {
		this.ultaSecureStore = ultaSecureStore;
	}

	/** Variable to hold the Current Ads URL. */
	private String currentAdsURL;

	/**
	 * Gets the variable to hold the Current Ads URL.
	 * 
	 * @return the currentAdsURL
	 */
	public String getCurrentAdsURL() {
		return currentAdsURL;
	}

	/**
	 * Sets the variable to hold the Current Ads URL.
	 * 
	 * @param currentAdsURL
	 *            the currentAdsURL to set
	 */
	public void setCurrentAdsURL(String currentAdsURL) {
		this.currentAdsURL = currentAdsURL;
	}

	public List<StoreDetailBean> getStores() {
		return stores;
	}

	public void setStores(List<StoreDetailBean> stores) {
		this.stores = stores;
	}

	public void setGiftCards(String giftCardNumber) {
		this.giftCards = giftCardNumber;
	}

	public boolean isOnlyEgiftCard() {
		return onlyEgiftCard;
	}

	public String getGiftCards() {
		return giftCards;
	}

	public Location getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}

	public void setOnlyEgiftCard(boolean onlyEgiftCard) {
		this.onlyEgiftCard = onlyEgiftCard;
	}

	public int getStoreBeingViewed() {
		return storeBeingViewed;
	}

	public void setStoreBeingViewed(int storeBeingViewed) {
		this.storeBeingViewed = storeBeingViewed;
	}

	public String reportingSuite;

	public String getReportingSuite() {
		return reportingSuite;
	}

	public void setReportingSuite(String reportingSuite) {
		this.reportingSuite = reportingSuite;
	}

	private String trackingServer;

	public String getTrackingServer() {
		return trackingServer;
	}

	public void setTrackingServer(String trackingServer) {
		this.trackingServer = trackingServer;
	}

	public void setErrorHappened(boolean errorHappened) {
		this.errorHappened = errorHappened;
	}

	public boolean isErrorHappened() {
		return errorHappened;
	}

	public void setHmWithPromoAndFreeGifts(
			HashMap<String, String> hmWithPromoAndFreeGifts) {
		this.hmWithPromoAndFreeGifts = hmWithPromoAndFreeGifts;
	}

	public HashMap<String, String> getHmWithPromoAndFreeGifts() {
		return hmWithPromoAndFreeGifts;
	}

	public void setHmWithFreeGiftAndPromId(
			HashMap<String, String> hmWithFreeGiftAndPromId) {
		this.hmWithFreeGiftAndPromId = hmWithFreeGiftAndPromId;
	}

	public HashMap<String, String> getHmWithFreeGiftAndPromId() {
		return hmWithFreeGiftAndPromId;
	}

	public void setHmWithFreeGiftsReverse(
			HashMap<String, String> hmWithFreeGiftsReverse) {
		this.hmWithFreeGiftsReverse = hmWithFreeGiftsReverse;
	}

	public HashMap<String, String> getHmWithFreeGiftsReverse() {
		return hmWithFreeGiftsReverse;
	}

	public boolean isAnonymousCheckout() {
		return isAnonymousCheckout;
	}

	public void setAnonymousCheckout(boolean isAnonymousCheckout) {
		this.isAnonymousCheckout = isAnonymousCheckout;
	}

	public boolean isOrderSubmitted() {
		return isOrderSubmitted;
	}

	public void setOrderSubmitted(boolean isOrderSubmitted) {
		this.isOrderSubmitted = isOrderSubmitted;
	}

	public boolean isPayPalPayment() {
		return payPalPayment;
	}

	public void setPayPalPayment(boolean payPalPayment) {
		this.payPalPayment = payPalPayment;
	}

	public boolean isExpressCheckout() {
		return isExpressCheckout;
	}

	public void setExpressCheckout(boolean isExpressCheckout) {
		this.isExpressCheckout = isExpressCheckout;
	}

	public boolean isQuestionSubmitted() {
		return isQuestionSubmitted;
	}

	public void setQuestionSubmitted(boolean isQuestionSubmitted) {
		this.isQuestionSubmitted = isQuestionSubmitted;
	}

	public TypeOfQuestionBean getTypeOfQuestion() {
		return typeOfQuestionResponse;
	}

	public void setTypeOfQuestion(TypeOfQuestionBean typeOfQuestionResponse) {
		this.typeOfQuestionResponse = typeOfQuestionResponse;
	}

	public List<StoreBrandFiler> getStoreBrandFilters() {
		return storeBrandFilters;
	}

	public void setStoreBrandFilters(List<StoreBrandFiler> storeBrandFilters) {
		this.storeBrandFilters = storeBrandFilters;
	}

	public boolean isRewardMember() {
		return isRewardMember;
	}

	public void setRewardMember(boolean isRewardMember) {
		this.isRewardMember = isRewardMember;
	}

	private boolean payPalPayment;

	/**
	 * To save credit card info for butterfly validation
	 */
	private List<CreditCardInfoBean> creditCardsInfo;

	public List<CreditCardInfoBean> getCreditCardsInfo() {
		return creditCardsInfo;
	}

	public void setCreditCardsInfo(List<CreditCardInfoBean> creditCardsInfo) {
		this.creditCardsInfo = creditCardsInfo;
	}
	private AtgResponseBean appConfig;

	public AtgResponseBean getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AtgResponseBean appConfig) {
		this.appConfig = appConfig;
	}



	public boolean isShowButterfly() {
		return showButterfly;
	}

	public void setShowButterfly(boolean showButterfly) {
		this.showButterfly = showButterfly;
	}

	public AppConfigurableBean getButterflyResponse() {
		return butterflyResponse;
	}

	public void setButterflyResponse(AppConfigurableBean butterflyResponse) {
		this.butterflyResponse = butterflyResponse;
	}
}