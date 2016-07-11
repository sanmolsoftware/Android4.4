package com.ulta.core.util.map;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class GeocoderTask extends
		AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

	private OnGeocodeFoundListener onGeocodeFoundListener;
	private Double latitude;
	private Double longitude;
	private Coordinates geoLocation;
	private ArrayList<SimpleGeocodeResult> mPossibleLocationsList;
	private String responseString;

	@Override
	protected ArrayList<String> doInBackground(ArrayList<String>... passing) {
		ArrayList<String> passed = passing[0];
		StringBuffer stringBuffer = new StringBuffer(
				"http://maps.googleapis.com/maps/api/geocode/json?");
		stringBuffer.append("address=");
		String encodedEnteredText;
		try {
			encodedEnteredText = URLEncoder.encode(passed.get(0), "UTF_8");
		} catch (UnsupportedEncodingException e) {
			encodedEnteredText = "";
			e.printStackTrace();
		}
		stringBuffer.append(encodedEnteredText);

		stringBuffer.append("&sensor=");
		stringBuffer.append("true");
		responseString = invokePost(stringBuffer.toString());
		// responseString =
		// invokePost("http://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=true");
		if (null != responseString) {
			parseResponseList(responseString);
		}
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		if (mPossibleLocationsList != null && mPossibleLocationsList.size() > 0) {
			onGeocodeFoundListener.onGeocodesFound(mPossibleLocationsList);
		} else {
			try {
				onGeocodeFoundListener.onGeocodeFound(new Coordinates(latitude,
						longitude));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onPostExecute(result);
	}

	public OnGeocodeFoundListener getOnGeocodeFoundListener() {
		return onGeocodeFoundListener;
	}

	public void setOnGeocodeFoundListener(
			OnGeocodeFoundListener onGeocodeFoundListener) {
		this.onGeocodeFoundListener = onGeocodeFoundListener;
	}

	public String invokePost(String url) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		/*
		 * params.add(new BasicNameValuePair("value.oldpassword","jdeli123"));
		 * params.add(new BasicNameValuePair("value.password","jdeli123"));
		 * params.add(new
		 * BasicNameValuePair("value.confirmpassword","jdeli456"));
		 */

		HttpPost request = new HttpPost(url);
		if (!params.isEmpty()) {
			try {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		HttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
			/*
			 * int responseCode = httpResponse.getStatusLine().getStatusCode();
			 * String message = httpResponse.getStatusLine().getReasonPhrase();
			 */
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				// writeLog(instream);
				String response = convertStreamToString(instream);
				// writeLog(response);
				instream.close();
				return response;
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		is.close();

		return sb.toString();
	}

	private void parseResponse(String responseString2) {
		try {
			JSONObject jsonObj = new JSONObject(responseString);
			JSONArray resultArray = null;
			resultArray = jsonObj.optJSONArray("results");
			JSONObject jsonObj2 = resultArray.getJSONObject(0);
			if (jsonObj2.has("geometry")) {
				JSONObject jsonObj3 = jsonObj2.getJSONObject("geometry");
				JSONObject jsonObj4 = jsonObj3.getJSONObject("location");
				latitude = jsonObj4.getDouble("lat");
				longitude = jsonObj4.getDouble("lng");
				this.geoLocation = new Coordinates(latitude, longitude);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			latitude = 0.0;
			longitude = 0.0;
			this.geoLocation = new Coordinates(latitude, longitude);
		}

	}

	private void parseResponseList(String responseString2) {
		try {
			JSONObject jsonObj = new JSONObject(responseString);
			JSONArray resultArray = null;
			resultArray = jsonObj.optJSONArray("results");

			if (resultArray.length() > 1) {
				ArrayList<SimpleGeocodeResult> possibleLocationList = new ArrayList<SimpleGeocodeResult>();
				// we have more than one listing
				// create a list of possible objects
				for (int i = 0; i < resultArray.length(); i++) {
					JSONObject obj = resultArray.getJSONObject(i);
					SimpleGeocodeResult sgResult = new SimpleGeocodeResult(obj);
					if (sgResult != null) {
						possibleLocationList.add(sgResult);
					}
				}
				mPossibleLocationsList = possibleLocationList;
			} else {
				parseResponse(responseString2);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			latitude = 0.0;
			longitude = 0.0;
			this.geoLocation = new Coordinates(latitude, longitude);
		}

	}
}
