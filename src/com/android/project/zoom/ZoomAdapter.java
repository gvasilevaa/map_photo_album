package com.android.project.zoom;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.project.model.AlbumItem;


public class ZoomAdapter extends FragmentStatePagerAdapter {
	@SuppressWarnings("unused")
	private static final String TAG = ZoomAdapter.class.getName();
	// private final String[] mWonderTitles;
	// private final int[] mWonderImages;
	private FragmentActivity mActivity;
	private ArrayList<AlbumItem> photos;
	private String place_name;

	public ZoomAdapter(Context context, FragmentManager fm,
			ArrayList<AlbumItem> mPhotos, FragmentActivity activity) {
		super(fm);
	
		this.mActivity = activity;
		this.photos = mPhotos;
	}

	@Override
	public ZoomImageFragment getItem(int position) {

		return ZoomImageFragment.newInstance(position, photos.get(position),
				 mActivity);
	}

	@Override
	public int getCount() {
		return photos.size();
	}
}
