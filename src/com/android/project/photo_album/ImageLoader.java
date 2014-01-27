package com.android.project.photo_album;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageLoader {

	// returns bitmap from a uri path
	public void loadImage(String path, ImageView imgView) {

		Bitmap myBitmap = null;
		File imgFile = new File(path);
		if (imgFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			imgView.setImageBitmap(myBitmap);
		}

	}
}
