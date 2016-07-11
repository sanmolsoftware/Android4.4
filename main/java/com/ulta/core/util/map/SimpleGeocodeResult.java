package com.ulta.core.util.map;

import org.json.JSONException;
import org.json.JSONObject;

public class SimpleGeocodeResult {
	private String mFormattedAddress;
	private Coordinates mLocation;

	public SimpleGeocodeResult(JSONObject resultDictObject) {
		try {
			mFormattedAddress = resultDictObject.getString("formatted_address");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			JSONObject jGeometry = resultDictObject.getJSONObject("geometry");
			JSONObject jLocation = jGeometry.getJSONObject("location");

			mLocation = new Coordinates(jLocation.getDouble("lat"), jLocation.getDouble("lng"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getFormattedAddress() {
		return mFormattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		mFormattedAddress = formattedAddress;
	}

	public Coordinates getLocation() {
		return mLocation;
	}

	public void setLocation(Coordinates location) {
		mLocation = location;
	}
}
