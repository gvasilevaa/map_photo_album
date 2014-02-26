package com.android.project.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GetFacebookName extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		String uri = params[0];

		HttpGet get = new HttpGet(uri);

		try {
			HttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(jsonResponse);

			return json.getString("first_name");

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

	}

}