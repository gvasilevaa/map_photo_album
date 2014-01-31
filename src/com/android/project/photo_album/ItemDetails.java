package com.android.project.photo_album;

import com.android.project.imagefetcher.ImageFetcher;
import com.android.project.model.AlbumItem;
import com.android.project.model.ApplicationConstants;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetails extends FragmentActivity implements ApplicationConstants {

	private AlbumItem item;
	private TextView title;
	private TextView address;
	private TextView date;
	private TextView description;
	private ImageView photo;
	private ImageFetcher mImageFetcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_details);

		item = (AlbumItem) getIntent().getSerializableExtra(ITEMS);
		title = (TextView) findViewById(R.id.title);
		address = (TextView) findViewById(R.id.address);
		date = (TextView) findViewById(R.id.created_at);
		description = (TextView) findViewById(R.id.description);
		photo = (ImageView) findViewById(R.id.photo);

		mImageFetcher = com.android.project.imagefetcher.Utils
				.getImageFetcher(this);

		populateItem();
	}

	private void populateItem() {

		mImageFetcher.loadImage(item.getThumbnail(), photo);
		title.setText(item.getName());
		address.setText(item.getAddress());
		date.setText(item.getCreatedAt());
		description.setText(item.getDesctioption());

	}

}
