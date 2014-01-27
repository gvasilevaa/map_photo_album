package com.android.project.photo_album;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.project.model.AlbumItem;
import com.despark.f1rst.imagefetcher.ImageFetcher;

public class AlbumGridAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<AlbumItem> mData;

	private ImageLoader mImageLoader;
	private ImageFetcher mImageFetcher;
	private ImageView imageView;

	public AlbumGridAdapter(FragmentActivity activity, Context context,
			ArrayList<AlbumItem> list) {
		mContext = context;
		mData = list;
		mImageLoader = new ImageLoader();
		mImageFetcher = com.despark.f1rst.imagefetcher.Utils
				.getImageFetcher(activity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// parent.getContext();
		AlbumItem item = mData.get(position);
		imageView = null;
		if (convertView == null) {

			imageView = new ImageView(mContext);
			DisplayMetrics metrics = new DisplayMetrics();
			((Activity) this.mContext).getWindowManager().getDefaultDisplay()
					.getMetrics(metrics);
			int size = (int) (70 * metrics.density);
			imageView.setLayoutParams(new GridView.LayoutParams(size, size));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(0, 0, 0, 0);
			imageView.setVisibility(View.VISIBLE);

		} else {

			imageView = (ImageView) convertView;
		}

		mImageFetcher.loadImage(item.getThumbnail(), imageView);

		return imageView;

	}

}
