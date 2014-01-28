package com.android.project.tasks;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	@Override
	protected Bitmap doInBackground(String... params) {

		String imageUrl = params[0];
		Bitmap image = null;

		try {
			InputStream in = new java.net.URL(imageUrl).openStream();
			image = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return image;
	}

}
