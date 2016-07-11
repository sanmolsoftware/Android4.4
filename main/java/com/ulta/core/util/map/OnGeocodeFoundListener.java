package com.ulta.core.util.map;

import java.util.ArrayList;


public interface OnGeocodeFoundListener {
	public void onGeocodeFound(Coordinates coordinates);
	public void onGeocodesFound(ArrayList<SimpleGeocodeResult> possibleLocations);
}
