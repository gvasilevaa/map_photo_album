package com.android.project.photo_album;

import java.util.ArrayList;

import com.android.project.imagefetcher.ImageFetcher;
import com.android.project.model.AlbumItem;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<AlbumItem> mData;
	private LayoutInflater inflater = null;
	private ImageLoader mImageLoader;
	private ImageFetcher mImageFetcher;

	public AlbumListAdapter(FragmentActivity activity,Context context,
			ArrayList<AlbumItem> list) {
		mContext = context;
		mData = list;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader = new ImageLoader();
		mImageFetcher = com.android.project.imagefetcher.Utils.getImageFetcher(activity);

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

		View view = convertView;
		Holder holder;
		AlbumItem item = mData.get(position);

		if (view == null) {

			view = inflater.inflate(R.layout.gallery_list_item, null);
			holder = new Holder();

			// name
			holder.caption = (TextView) view.findViewById(R.id.item_title);

			// place photo
			holder.imagePlacePhoto = (ImageView) view
					.findViewById(R.id.item_image);

			view.setTag(holder);

		} else {
			holder = (Holder) view.getTag();
		}

		holder.caption.setText(item.getName());

		mImageFetcher.loadImage(item.getThumbnail(), holder.imagePlacePhoto);
		return view;

	}

	private class Holder {
		TextView caption;
		ImageView imagePlacePhoto;

	}
}
