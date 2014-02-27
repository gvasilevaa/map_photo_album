package com.android.project.zoom;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.android.project.model.AlbumItem;
import com.android.project.model.ApplicationConstants;
import com.android.project.photo_album.R;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;


public class ZoomImagesActivity extends FragmentActivity implements
		ApplicationConstants {

	private ViewPager mZoomView;
	private ArrayList<AlbumItem> photos;
	private int currerntposition;
	private String place_name;
	
//	private  UiLifecycleHelper uiHelper;
//	private Session session;
//	private  Session.StatusCallback statusCallback = new SessionStatusCallback();
//
//	private class SessionStatusCallback implements Session.StatusCallback {
//		@Override
//		public void call(Session session, SessionState state,
//				Exception exception) {
//			// getUser();
//		}
//	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zoom_images);

		
		
		if (getIntent().hasExtra(ITEMS)) {
			this.photos = (ArrayList<AlbumItem>) getIntent()
					.getSerializableExtra(ITEMS);

			this.currerntposition = getIntent().getIntExtra(POSITION, 0);
		}

		
//		mZoomView = (ViewPager) findViewById(R.id.zoomView);
//		mZoomView.setAdapter(new ZoomAdapter(this, getSupportFragmentManager(),
//				photos, ZoomImagesActivity.this));
//
//		mZoomView.setCurrentItem(currerntposition);

		// mZoomView.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		//
		// Intent i = new Intent(ZoomImagesActivity.this,
		// ZoomImagesActivity.class);
		// i.putExtra(EXTRA_PHOTOS, photos);
		// i.putExtra(EXTRA_NAME, );
		// i.putExtra(EXTRA_POSITION, 0);
		// startActivity(i);
		// return true;
		// }
		// return false;
		// }
		// });
		// mZoomView.setPageTransformer(true, new DepthPageTransformer());
	}
	@Override
	protected void onResume() {
		mZoomView = (ViewPager) findViewById(R.id.zoomView);
		mZoomView.setAdapter(new ZoomAdapter(this, getSupportFragmentManager(),
				photos, ZoomImagesActivity.this));

		mZoomView.setCurrentItem(currerntposition);

		super.onResume();
	}
	
	
	
	
}
