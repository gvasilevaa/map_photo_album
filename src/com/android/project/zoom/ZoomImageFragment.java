package com.android.project.zoom;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.project.imagefetcher.ImageFetcher;
import com.android.project.imagefetcher.ImageWorker;
import com.android.project.model.AlbumItem;
import com.android.project.model.ApplicationConstants;
import com.android.project.photo_album.R;

public class ZoomImageFragment extends Fragment implements ApplicationConstants {

	private static final String TAG = ZoomImageFragment.class.getName();

	private static ImageFetcher mImageFetcher;

	public static final String ARG_PAGE = "page";

	private ImageView imageView;

	public static ZoomImageFragment newInstance(int position, AlbumItem item,
			FragmentActivity mActivity) {
		mImageFetcher = com.android.project.imagefetcher.Utils
				.getImageFetcher(mActivity);

		Bundle args = new Bundle();
		if (item != null) {
			args.putInt(ARG_PAGE, position);
			args.putSerializable(ITEMS, item);
		}

		ZoomImageFragment fragment = new ZoomImageFragment();
		fragment.setArguments(args);
		return fragment;
	}

	public ZoomImageFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ImageFetcher.cancelWork(imageView);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup zoomView = (ViewGroup) inflater.inflate(
				R.layout.zoom_image_item, container, false);
		this.imageView = (ImageView) zoomView.findViewById(R.id.zoom_image);

		AlbumItem item = (AlbumItem) getArguments().getSerializable(ITEMS);
		if (getArguments().get(ITEMS) != null) {
			mImageFetcher.loadImage(item.getThumbnail(), imageView);
		}

		return zoomView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ImageWorker.cancelWork(imageView);

	}

}
