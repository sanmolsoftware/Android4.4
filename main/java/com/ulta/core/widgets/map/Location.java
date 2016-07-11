/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.widgets.map;



/**
 * The Class Location.
 */
public class Location {
	
	/** The latitude. */
	Double latitude;
	
	/** The longitude. */
	Double longitude;
	
	/** The name. */
	String name;
	String address;
	String city;
	
	String phone;

	String state;
	String zipcode;
	boolean isStoreOpen;
	/**
	 * Instantiates a new location.
	 *
	 * @param name the name
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	public Location(String name,String address,Double latitude, Double longitude,boolean isStoreOpen,String city,String state,String zipcode,String phone) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.address=address;
		this.isStoreOpen=isStoreOpen;
		this.city=city;
		this.state=state;
		this.zipcode=zipcode;
		this.phone=phone;
	}
	
	public boolean isStoreOpen() {
		return isStoreOpen;
	}

	public void setStoreOpen(boolean isStoreOpen) {
		this.isStoreOpen = isStoreOpen;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getPhone() {
		return phone;
	}

}
