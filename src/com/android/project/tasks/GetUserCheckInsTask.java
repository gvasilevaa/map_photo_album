package com.android.project.tasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.project.model.AlbumItem;

import android.os.AsyncTask;
import android.util.Log;



public class GetUserCheckInsTask extends
		AsyncTask<String, Void, ArrayList<AlbumItem>> 
		 {

	private ArrayList<AlbumItem> places;

	@Override
	protected ArrayList<AlbumItem> doInBackground(String... params) {

		String url = params[0];
		HttpClient client = new DefaultHttpClient();

		HttpGet get = new HttpGet(url);
		Log.d("request:", url);
		try {
			HttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(jsonResponse);

			int code = (int) json.getJSONObject("status").getLong("code");

			if (code != 200) {
				return null;
			}

			places = AlbumItem.parseFromJSON(json);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		

		return places;

	}

}
