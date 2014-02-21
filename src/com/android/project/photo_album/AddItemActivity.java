package com.android.project.photo_album;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.project.db.DBException;
import com.android.project.db.DBSQLException;
import com.android.project.imagefetcher.ImageFetcher;
import com.android.project.model.AlbumItem;
import com.android.project.model.AlbumItemManager;
import com.android.project.model.ApplicationConstants;

public class AddItemActivity extends FragmentActivity implements
		ApplicationConstants {

	private final static int ADDRESS_REQUEST_CODE = 1;

	private AlbumItem item;
	
	private EditText title_edittxt;
	private EditText address_edittxt;
	private EditText date_edittxt;
	private EditText description_edittxt;
	private ImageView photo;
	private ImageFetcher mImageFetcher;
	private String imageFilePath;
	private double lat;
	private double lon;

	private String name;
	private String address;
	private String description;
	private String imagePath;
	private String date;

	private File mPhotoFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);

		title_edittxt = (EditText) findViewById(R.id.title);
		address_edittxt = (EditText) findViewById(R.id.address);
		date_edittxt = (EditText) findViewById(R.id.created_at);
		description_edittxt = (EditText) findViewById(R.id.description);
		photo = (ImageView) findViewById(R.id.photo);

		mImageFetcher = com.android.project.imagefetcher.Utils
				.getImageFetcher(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADDRESS_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {
				String address = data.getStringExtra(ADDRESS);
				lat = data.getDoubleExtra(LAT, 0);
				lon = data.getDoubleExtra(LON, 0);
				// Log.e("RESULT ADDRESS", address);
				address_edittxt.setText(address);

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}

		// check request code
		if (requestCode == SELECT_PICTURE) {

			// data should not be null
			if (data != null) {

				/* === GALERY === */
				if (data.getData() != null) {

					Uri uri = data.getData();
					Cursor cursor = null;

					try {

						cursor = getContentResolver()
								.query(uri,
										new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
										null, null, null);
						cursor.moveToFirst();

					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(
								this,
								getResources().getString(
										R.string.cant_load_photo),
								Toast.LENGTH_SHORT).show();
						return;

					}

					try {

						if (cursor != null) {
							
//							Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//							FileOutputStream fOut = new FileOutputStream(compressedPictureFile);
//							boolean compressed = bitmap.compress(Bitmap.CompressFormat.PNG, 0, fOut);
//							fOut.flush();
//							fOut.close();
							// Link to the image
							imageFilePath = cursor.getString(0);
							mPhotoFile = new File(imageFilePath);
							cursor.close();
							Bitmap bmImg = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
							FileOutputStream fOut = new FileOutputStream(mPhotoFile);
							bmImg.compress(Bitmap.CompressFormat.PNG, 0, fOut);
							fOut.flush();
							fOut.close();
							photo.setImageBitmap(bmImg);
							

						}

					} catch (Exception e) {
						e.printStackTrace();

						Toast.makeText(
								this,
								getResources().getString(
										R.string.cant_load_photo),
								Toast.LENGTH_SHORT).show();
						return;

					} catch (OutOfMemoryError e) {
						e.printStackTrace();

						Toast.makeText(
								this,
								getResources().getString(
										R.string.cant_load_photo),
								Toast.LENGTH_SHORT).show();
						return;
					}

				}
				/* === CAMERA === */
				else {

					if (resultCode == RESULT_OK) {

						String extStorageDirectory = Environment
								.getExternalStorageDirectory().toString();
						OutputStream outStream = null;
						mPhotoFile = new File(extStorageDirectory,
								PLACE_PHOTO_FILE);

						try {

							Bitmap picture = (Bitmap) data.getExtras().get(
									KEY_DATA);

							outStream = new FileOutputStream(mPhotoFile);
							picture.compress(Bitmap.CompressFormat.PNG, 0,
									outStream);
							outStream.flush();
							outStream.close();
							
							photo.setImageBitmap(picture);

						} catch (FileNotFoundException e) {
							e.printStackTrace();

							Toast.makeText(
									this,
									getResources().getString(
											R.string.cant_load_photo),
									Toast.LENGTH_SHORT).show();
							return;

						} catch (IOException e) {
							e.printStackTrace();

							Toast.makeText(
									this,
									getResources().getString(
											R.string.cant_load_photo),
									Toast.LENGTH_SHORT).show();
							return;

						} catch (OutOfMemoryError e) {
							e.printStackTrace();

							Toast.makeText(
									this,
									getResources().getString(
											R.string.cant_load_photo),
									Toast.LENGTH_SHORT).show();
							return;

						}

					}

				}

			} else {
				// data is null
				Toast.makeText(this,
						getResources().getString(R.string.cant_load_photo),
						Toast.LENGTH_SHORT).show();
				return;

			}

		}

	}

	/* ------------------ onClick Methods ------------------------------ */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public void onAddressClick(View view) {

		Intent i = new Intent(AddItemActivity.this, MapActivity.class);

		startActivityForResult(i, ADDRESS_REQUEST_CODE);
	}

	public void onImageClick(View v) {

		Intent pickIntent = new Intent();
		pickIntent.setType("image/*");
		pickIntent.setAction(Intent.ACTION_GET_CONTENT);

		Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		Intent chooserIntent = Intent.createChooser(pickIntent,
				getResources().getString(R.string.photo_picker_place));
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				new Intent[] { takePhotoIntent });

		startActivityForResult(chooserIntent, SELECT_PICTURE);
	}

	/**
	 * Updates the record for the current item
	 * 
	 * @param view
	 * @throws DBSQLException
	 * @throws DBException
	 */
	public void OnSaveClick(View view) throws DBSQLException, DBException {

//		ContentValues values = new ContentValues();
//		values.put(AlbumItem.GET_COLUMNS.ID.getName(), item.getId());
//		values.put(AlbumItem.GET_COLUMNS.NAME.getName(), title_edittxt
//				.getText().toString());
//		values.put(AlbumItem.GET_COLUMNS.ADDRESS.getName(), address_edittxt
//				.getText().toString());
//		values.put(AlbumItem.GET_COLUMNS.LATITUDE.getName(), lat);
//		values.put(AlbumItem.GET_COLUMNS.LONGITUDE.getName(), lon);
//		values.put(AlbumItem.GET_COLUMNS.CREATED_AT.getName(), date_edittxt
//				.getText().toString());
//		values.put(AlbumItem.GET_COLUMNS.DESCRIPTION.getName(),
//				description_edittxt.getText().toString());

		name = title_edittxt.getText().toString();
		address = address_edittxt.getText().toString();
		date = date_edittxt.getText().toString();
		description = description_edittxt.getText().toString();

		 if (mPhotoFile.getAbsolutePath() != null) {
			imagePath = mPhotoFile.getAbsolutePath();
		}

		AlbumItem item = new AlbumItem(1, name, imagePath, String.valueOf(lat),
				String.valueOf(lon), address, date, description);

		AlbumItemManager.getInstance().saveProduct(item);

		// if (count > 0) {
		// Toast.makeText(EditDetailsActivity.this,
		// getResources().getString(R.string.success_update_item),
		// Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(EditDetailsActivity.this,
		// getResources().getString(R.string.fail_update_item),
		// Toast.LENGTH_SHORT).show();
		// }

		finish();
	}
}
