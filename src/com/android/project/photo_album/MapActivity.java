package com.android.project.photo_album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.android.project.model.AlbumItem;
import com.android.project.model.ApplicationConstants;
import com.android.project.tasks.DownloadImageTask;
import com.android.project.tasks.GetAddressTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		ApplicationConstants {

	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private ArrayList<AlbumItem> items;
	private GetAddressTask getAddress;
	private double lat;
	private double lon;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5 * 60 * 1000) // 5 mins
			.setFastestInterval(3 * 60 * 1000) // 3 minutes
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mMap = mapFragment.getMap();

		items = (ArrayList<AlbumItem>) getIntent().getSerializableExtra(ITEMS);

		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				Log.e("LAT", point.latitude + "");
				Log.e("LON", point.longitude + "");

				lat = point.latitude;
				lon = point.longitude;
				getCityName(point);
			}
		});
		// setUpMap();
	}

	@Override
	protected void onResume() {
		super.onResume();

		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener

		}
	}

	private void setUpMapIfNeeded() {

		if (mMap != null) {

			/*
			 * set map settings: normal map, no zoom buttons, no compass, no
			 * location button
			 */
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			UiSettings settings = mMap.getUiSettings();
			settings.setZoomControlsEnabled(false);
			settings.setScrollGesturesEnabled(true);

			mMap.setMyLocationEnabled(true);

			settings.setMyLocationButtonEnabled(false);

			// mMap.setOnCameraChangeListener(getCameraChangeListener());

		}

	}

	public OnCameraChangeListener getCameraChangeListener() {
		return new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {

			}
		};
	}

	@Override
	public void onLocationChanged(Location location) {

		if (mLocationClient != null && mLocationClient.isConnected()) {
			setUpMap();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener

	}

	@Override
	public void onDisconnected() {
		// Do nothing
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	private void setUpMap() {

		Location location = mLocationClient.getLastLocation();
		double lat = Double.parseDouble(items.get(0).getLatitude());
		double lon = Double.parseDouble(items.get(0).getLongitude());

		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,
				lon), 15));

		mLocationClient.removeLocationUpdates(this);

		for (int i = 0; i < items.size(); i++) {
			Log.d("Item", items.get(i).getLatitude());
			createMarker(items.get(i));

		}

		// getFriendsCheckins(lat, lon);

	}

	/* ------------------------- Create markers methods ----------------------- */

	private void createMarker(final AlbumItem place) {

		final MarkerOptions markerOp = new MarkerOptions();

		final double lat = Double.parseDouble(place.getLatitude());
		final double lng = Double.parseDouble(place.getLongitude());

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View viewNormalMarker = inflater.inflate(
				R.layout.map_single_marker, null);
		final ImageView imageViewNormal = (ImageView) viewNormalMarker
				.findViewById(R.id.imageClusterIcon);

		DownloadImageTask downloadImageTask = new DownloadImageTask() {
			@Override
			protected void onPostExecute(final Bitmap image) {

				/* set marker normal */
				imageViewNormal.setImageBitmap(image);
				markerOp.position(new LatLng(lat, lng));
				BitmapDescriptor icon = BitmapDescriptorFactory
						.fromBitmap(createDrawableFromView(MapActivity.this,
								viewNormalMarker));
				markerOp.icon(icon);
				markerOp.visible(true);
				mMap.addMarker(markerOp);

				// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				// new LatLng(lat, lng), 15));

			}
		};

		downloadImageTask.execute(place.getThumbnail());
	}

	// Convert a view to bitmap
	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	/*-------- Get Address from geopoint ----------------------*/
	public void getCityName(LatLng point) {

		getAddress = new GetAddressTask(point, MapActivity.this) {

			@Override
			protected void onPostExecute(String address) {

				getAddress = null;
				if (address != null) {
					// Log.e("CITY", address);

					Intent returnIntent = new Intent();
					returnIntent.putExtra(ADDRESS, address);
					returnIntent.putExtra(LAT, lat);
					returnIntent.putExtra(LON, lon);
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}

		};

		getAddress.execute();
	}

}
