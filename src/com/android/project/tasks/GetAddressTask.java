package com.android.project.tasks;

import java.util.List;
import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class GetAddressTask extends AsyncTask<Void, Void, String> {

	private String streetName = null;
	// String cityName = null;
	private String address;
	private boolean running;
	private LatLng point;
	private Context mContext;

	public  GetAddressTask(LatLng point, Context context) {
	
		Log.e("CITY","constructor" );
		this.point= point;
		this.mContext= context;
		
	}

	@Override
	protected void onPreExecute() {
		running = true;
		Log.e("CITY","onPreExecute" );
	}

	@Override
	protected String doInBackground(Void... params) {
		if (Geocoder.isPresent()) { // if geocoder network is not crashed
			try {
				Geocoder geocoder = new Geocoder(mContext,
						Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(
						point.latitude, point.longitude,
						1);
				if (addresses.size() > 0) {
					address = addresses.get(0).getLocality();
				}
				for (Address address : addresses) {
					Log.i("my location ..",
							 address.getAddressLine(0));
					streetName = address.getAddressLine(0);
					this.address+= streetName;
				}
			} catch (Exception ignored) {
				// after a while, Geocoder start to throw
				// "Service not available" exception. really weird since it was
				// working before (same device, same Android version etc..
			}
		}

		if (address != null) { // Geocoder succeed

			return address;
		} else // Geocoder failed
		{
			return fetchCityNameUsingGoogleMap();
		}
	}

	// Geocoder failed :-(
	// My B Plan : Google Map
	private String fetchCityNameUsingGoogleMap() {
		
		
		String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ point.latitude
				+ ","
				+ point.longitude
				+ "&sensor=false&language=engl";

	
		HttpClient client = new DefaultHttpClient();
		try {
			JSONObject googleMapResponse = new JSONObject(
					client.execute(new HttpGet(googleMapUrl),
							new BasicResponseHandler()));

			// many nested loops.. not great -> use expression instead
			// loop among all results
			JSONArray results = (JSONArray) googleMapResponse.get("results");
			
		
			for (int i = 0; i < results.length(); i++) {
				// loop among all addresses within this result
				JSONObject result = results.getJSONObject(i);
				
				if(result.has("formatted_address")){
					address = result.getString("formatted_address");
				}
//				
				return address;
			}
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
		return null;
	}

}
