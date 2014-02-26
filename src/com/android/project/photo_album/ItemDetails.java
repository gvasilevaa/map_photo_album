package com.android.project.photo_album;

import com.android.project.imagefetcher.ImageFetcher;
import com.android.project.model.AlbumItem;
import com.android.project.model.ApplicationConstants;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

public class ItemDetails extends DialogFragment implements ApplicationConstants {

	/**
	 * Constansts
	 */
	private final static String TITLE = "title";
	private final static String DATE = "date";
	private final static String ADDRESS = "address";
	private final static String PHOTO = "photo";
	private final static String DESCRIPTION = "description";

	private TextView title;
	private TextView address;
	private TextView date;
	private TextView description;
	private ImageView photo;
	private static ImageFetcher mImageFetcher;
	private LayoutInflater inflater;

	/**
	 * 
	 * @param item
	 * @param activity
	 * @return DialogFragment
	 */
	public static ItemDetails instance(AlbumItem item, FragmentActivity activity) {

		mImageFetcher = com.android.project.imagefetcher.Utils
				.getImageFetcher(activity);
		ItemDetails newFragment = new ItemDetails();
		Bundle args = new Bundle();
		args.putString(TITLE, item.getName());
		args.putString(ADDRESS, item.getAddress());
		args.putString(PHOTO, item.getThumbnail());
		args.putString(DATE, item.getCreatedAt());
		args.putString(DESCRIPTION, item.getDesctioption());
		newFragment.setArguments(args);
		return newFragment;
	}

	public ItemDetails() {

	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.item_details_layout, null, false);

		AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());

		photo = (ImageView) view.findViewById(R.id.photo);
		title = (TextView) view.findViewById(R.id.title);
		address = (TextView) view.findViewById(R.id.address);
		date = (TextView) view.findViewById(R.id.created_at);
		description = (TextView) view.findViewById(R.id.description);

		// load data
		mImageFetcher.loadImage(getPhoto(), photo);
		title.setText(getTitle());
		address.setText(getAddress());
		date.setText(getDate());
		description.setText(getDescription());

		
		
        
        mDialog.setCustomTitle(view);
		return mDialog.create();

	}

	/**
	 * Get parameters from Bundle
	 * 
	 * @return String
	 */
	private String getTitle() {
		return getArguments().getString(TITLE);
	}

	private String getDate() {
		return getArguments().getString(DATE);
	}

	private String getDescription() {
		return getArguments().getString(DESCRIPTION);
	}

	private String getPhoto() {
		return getArguments().getString(PHOTO);
	}

	private String getAddress() {
		return getArguments().getString(ADDRESS);
	}



}
