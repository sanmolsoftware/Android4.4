package com.ulta.core.bean.store;

import com.ulta.core.bean.UltaBean;

import java.util.List;


@SuppressWarnings("serial")
public class StoreDetailBean extends UltaBean {
	private String contactNumber;
	private String rewardsName;
	private String address1;
	private String comingSoon;
	private String address2;
	private String city;
	private String displayName;
	private Double latitude;
	private Double longitude;
	private String state;
	private String zipCode;
	private String storeId;
	private List<String> storeTimingsDetails;
	private List<String> storeAmenityDetails;
	private List<AmenitiesDetailsBean> storeAmenityImageDetails;
	private List<StoreEventBean> storeEvents;
    private String openDate;
	private String message;



	//3.5 release
	private boolean isStoreOpen;
	private String globalMessage;
	
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getRewardsName() {
		return rewardsName;
	}
	public void setRewardsName(String rewardsName) {
		this.rewardsName = rewardsName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public List<String> getStoreTimingsDetails() {
		return storeTimingsDetails;
	}
	public void setStoreTimingsDetails(List<String> storeTimingsDetails) {
		this.storeTimingsDetails = storeTimingsDetails;
		this.storeTimingsDetails = storeTimingsDetails;
	}
	public List<String> getStoreAmenityDetails() {
		return storeAmenityDetails;
	}
	public void setStoreAmenityDetails(List<String> storeAmenityDetails) {
		this.storeAmenityDetails = storeAmenityDetails;
	}
	public List<StoreEventBean> getStoreEvents() {
		return storeEvents;
	}
	public void setStoreEvents(List<StoreEventBean> storeEvents) {
		this.storeEvents = storeEvents;
	}
	public void setStoreAmenityImageDetails(List<AmenitiesDetailsBean> storeAmenityImageDetails) {
		this.storeAmenityImageDetails = storeAmenityImageDetails;
	}
	public List<AmenitiesDetailsBean> getStoreAmenityImageDetails() {
		return storeAmenityImageDetails;
	}
	public void setComingSoon(String comingSoon) {
		this.comingSoon = comingSoon;
	}
	public String getComingSoon() {
		return comingSoon;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public boolean isStoreOpen() {
		return isStoreOpen;
	}
	public void setStoreOpen(boolean isStoreOpen) {
		this.isStoreOpen = isStoreOpen;
	}
	public String getGlobalMessage() {
		return globalMessage;
	}
	public void setGlobalMessage(String globalMessage) {
		this.globalMessage = globalMessage;
	}
	
}
