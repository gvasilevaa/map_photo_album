package com.android.project.photo_album;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
import android.text.TextUtils;
import android.util.Log;
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

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;

public class AddItemActivity extends FragmentActivity implements
		ApplicationConstants, ConnectionCallbacks {

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

	private UiLifecycleHelper uiHelper;

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

//		uiHelper = new UiLifecycleHelper(this, statusCallback);
//		uiHelper.onCreate(savedInstanceState);
//
//		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//		Session session = Session.getActiveSession();
//		if (session == null) {
//			if (savedInstanceState != null) {
//				session = Session.restoreSession(this, null, statusCallback,
//						savedInstanceState);
//			}
//			if (session == null) {
//				session = new Session(this);
//			}
//			Session.setActiveSession(session);
//			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//				session.openForRead(new Session.OpenRequest(this)
//						.setCallback(statusCallback));
//			}
//		}
//	}

//	private String facebookToken = null;
//	private Session.StatusCallback statusCallback = new SessionStatusCallback();
//
//	private Session session;
//
//	private class SessionStatusCallback implements Session.StatusCallback {
//		@Override
//		public void call(Session session, SessionState state,
//				Exception exception) {
//			// getUser();
//		}
//	}
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

							// Bitmap bitmap =
							// BitmapFactory.decodeFile(imagePath);
							// FileOutputStream fOut = new
							// FileOutputStream(compressedPictureFile);
							// boolean compressed =
							// bitmap.compress(Bitmap.CompressFormat.PNG, 0,
							// fOut);
							// fOut.flush();
							// fOut.close();
							// Link to the image
							imageFilePath = cursor.getString(0);
							mPhotoFile = new File(imageFilePath);
							cursor.close();
							Bitmap bmImg = BitmapFactory.decodeFile(mPhotoFile
									.getAbsolutePath());
							FileOutputStream fOut = new FileOutputStream(
									mPhotoFile);
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

		Intent chooserIntent = Intent.createChooser(pickIntent, getResources()
				.getString(R.string.photo_picker_place));
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

		// ContentValues values = new ContentValues();
		// values.put(AlbumItem.GET_COLUMNS.ID.getName(), item.getId());
		// values.put(AlbumItem.GET_COLUMNS.NAME.getName(), title_edittxt
		// .getText().toString());
		// values.put(AlbumItem.GET_COLUMNS.ADDRESS.getName(), address_edittxt
		// .getText().toString());
		// values.put(AlbumItem.GET_COLUMNS.LATITUDE.getName(), lat);
		// values.put(AlbumItem.GET_COLUMNS.LONGITUDE.getName(), lon);
		// values.put(AlbumItem.GET_COLUMNS.CREATED_AT.getName(), date_edittxt
		// .getText().toString());
		// values.put(AlbumItem.GET_COLUMNS.DESCRIPTION.getName(),
		// description_edittxt.getText().toString());

		name = title_edittxt.getText().toString();
		address = address_edittxt.getText().toString();
		date = date_edittxt.getText().toString();
		description = description_edittxt.getText().toString();
		
		if(name==null || name.equals("")){
			Toast.makeText(AddItemActivity.this, " Please enter Name",Toast.LENGTH_SHORT).show();
			return;
		}
		

		if (mPhotoFile!=null && mPhotoFile.getAbsolutePath() != null) {
			imagePath = mPhotoFile.getAbsolutePath();
		}else{
			Toast.makeText(AddItemActivity.this, "Please select and image",Toast.LENGTH_SHORT).show();
			return;
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

//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		Session session = Session.getActiveSession();
//		Session.saveSession(session, outState);
//	}
//
//	private void onClickLogin() {
//		session = Session.getActiveSession();
//		if (!session.isOpened() && !session.isClosed()) {
//			session.openForRead(new Session.OpenRequest(this)
//					.setCallback(statusCallback));
//		} else {
//			Session.openActiveSession(this, true, statusCallback);
//		}
//	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

//	public void onShareClick(View v) {
//
//		
//
//		// Part 1: create callback to get URL of uploaded photo
//		Request.Callback uploadPhotoRequestCallback = new Request.Callback() {
//			@Override
//			public void onCompleted(Response response) {
//				String fbPhotoAddress = null;
//				// safety check
//				if (isFinishing()) {
//					return;
//				}
//				if (response.getError() != null) { // [IF Failed Posting]
//					Log.d("Log",
//							"photo upload problem. Error="
//									+ response.getError());
//				} // [ENDIF Failed Posting]
//
//				Object graphResponse = response.getGraphObject().getProperty(
//						"id");
//				if (graphResponse == null || !(graphResponse instanceof String)
//						|| TextUtils.isEmpty((String) graphResponse)) { // [IF
//																		// Failed
//																		// upload/no
//																		// results]
//					Log.d("Log", "failed photo upload/no response");
//				} else { // [ELSEIF successful upload]
//					fbPhotoAddress = "https://www.facebook.com/photo.php?fbid="
//							+ graphResponse;
//				} // [ENDIF successful posting or not]
//			} // [END onCompleted]
//		};
//
//		if (mPhotoFile.getAbsolutePath() != null) {
//			imagePath = mPhotoFile.getAbsolutePath();
//		}
//
//		Bitmap bm = ImageFetcher.decodeSampledBitmapFromFile(imagePath, 50, 50);
//		// Part 2: upload the photo
//		Request request = Request.newUploadPhotoRequest(session, bm,
//				uploadPhotoRequestCallback);
//		request.executeAsync();
//
//	}
}
