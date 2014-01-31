package com.android.project.photo_album;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.project.db.DBException;
import com.android.project.db.DBSQLException;
import com.android.project.imagefetcher.ImageFetcher;
import com.android.project.model.AlbumItem;
import com.android.project.model.AlbumItemManager;
import com.android.project.model.ApplicationConstants;

public class EditDetailsActivity extends FragmentActivity implements
		ApplicationConstants {

	private final static int ADDRESS_REQUEST_CODE = 1;

	private AlbumItem item;
	private EditText title_edittxt;
	private EditText address_edittxt;
	private EditText date_edittxt;
	private EditText description_edittxt;
	private ImageView photo;
	private ImageFetcher mImageFetcher;

	private double lat;

	private double lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_details);

		item = (AlbumItem) getIntent().getSerializableExtra(ITEMS);

		title_edittxt = (EditText) findViewById(R.id.title);
		address_edittxt = (EditText) findViewById(R.id.address);
		date_edittxt = (EditText) findViewById(R.id.created_at);
		description_edittxt = (EditText) findViewById(R.id.description);
		photo = (ImageView) findViewById(R.id.photo);

		mImageFetcher = com.android.project.imagefetcher.Utils
				.getImageFetcher(this);

		populateItem();
	}

	private void populateItem() {

		mImageFetcher.loadImage(item.getThumbnail(), photo);
		title_edittxt.setText(item.getName());
		address_edittxt.setText(item.getAddress());
		date_edittxt.setText(item.getCreatedAt());
		description_edittxt.setText(item.getDesctioption());

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADDRESS_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {
				String address = data.getStringExtra(ADDRESS);
				lat = data.getDoubleExtra(LAT, 0);
				lon = data.getDoubleExtra(LON, 0);
				//Log.e("RESULT ADDRESS", address);
				address_edittxt.setText(address);

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	/* ------------------ onClick Methods ------------------------------ */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public void onAddressClick(View view) {
		ArrayList<AlbumItem> items = new ArrayList<AlbumItem>();
		items.add(item);
		Intent i = new Intent(EditDetailsActivity.this, MapActivity.class);
		i.putExtra(ITEMS, items);
		startActivityForResult(i, ADDRESS_REQUEST_CODE);
	}

	public void OnSaveClick(View view) throws DBSQLException, DBException {

		ContentValues values = new ContentValues();
		values.put(AlbumItem.GET_COLUMNS.ID.getName(), item.getId());
		values.put(AlbumItem.GET_COLUMNS.NAME.getName(), title_edittxt.getText().toString());
		values.put(AlbumItem.GET_COLUMNS.ADDRESS.getName(), address_edittxt.getText().toString());
		values.put(AlbumItem.GET_COLUMNS.LATITUDE.getName(), lat);
		values.put(AlbumItem.GET_COLUMNS.LONGITUDE.getName(), lon);
		values.put(AlbumItem.GET_COLUMNS.CREATED_AT.getName(), date_edittxt.getText().toString());
		values.put(AlbumItem.GET_COLUMNS.DESCRIPTION.getName(), description_edittxt.getText().toString());
		int count = AlbumItemManager.getInstance().updateItem(values);
		
		if(count>0){
			Toast.makeText(EditDetailsActivity.this,getResources().getString(R.string.success_update_item), Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(EditDetailsActivity.this,getResources().getString(R.string.fail_update_item), Toast.LENGTH_SHORT).show();
		}
			
		Log.e("COUNT UPDATED ITEMS", count + "");
	}
}
