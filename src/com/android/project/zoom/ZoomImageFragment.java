package com.android.project.zoom;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.project.db.DBException;
import com.android.project.imagefetcher.ImageFetcher;
import com.android.project.imagefetcher.ImageWorker;
import com.android.project.model.AlbumItem;
import com.android.project.model.AlbumItemManager;
import com.android.project.model.ApplicationConstants;
import com.android.project.photo_album.EditDetailsActivity;
import com.android.project.photo_album.MapActivity;
import com.android.project.photo_album.R;
import com.facebook.Request;
import com.facebook.Response;

public class ZoomImageFragment extends Fragment implements ApplicationConstants {

	private static final String TAG = ZoomImageFragment.class.getName();

	private static ImageFetcher mImageFetcher;

	public static final String ARG_PAGE = "page";

	private ImageView imageView;
	private TextView edit;

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
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		ViewGroup zoomView = (ViewGroup) inflater.inflate(
				R.layout.zoom_image_item, container, false);
		imageView = (ImageView) zoomView.findViewById(R.id.zoom_image);
		edit = (TextView) zoomView.findViewById(R.id.edit);

		AlbumItem item = (AlbumItem) getArguments().getSerializable(ITEMS);
		if (getArguments().get(ITEMS) != null) {
			mImageFetcher.loadImage(item.getThumbnail(), imageView);
		}

		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(getActivity(),
						R.style.CustomDialog);

				View menu = inflater.inflate(R.layout.edit_image_dialog, null);

				((TextView) menu.findViewById(R.id.filterText))
						.setText(getResources().getString(R.string.edit));

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				dialog.setContentView(menu, params);

				TextView delete = (TextView) menu.findViewById(R.id.delete);
				TextView map = (TextView) menu.findViewById(R.id.map);
				TextView edit_details = (TextView) menu
						.findViewById(R.id.edit_details);
				
				TextView share = (TextView)menu.findViewById(R.id.share);

				edit_details.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								EditDetailsActivity.class);
						i.putExtra(ITEMS, getArguments().getSerializable(ITEMS));
						startActivity(i);
						dialog.dismiss();
					}
				});

				delete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
						final Dialog dialog = new Dialog(getActivity(),
								R.style.CustomDialog);

						View view = inflater.inflate(R.layout.delete_dialog_layout, null);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
						
						
						Button yes = (Button) view.findViewById(R.id.yes);
						Button no = (Button) view.findViewById(R.id.no);
						
						yes.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								try {
									AlbumItemManager.getInstance().deleteItem(getItem().getId());
									getActivity().finish();
								} catch (DBException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						});
						
						no.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								
							}
						});
						
						
						dialog.setContentView(view, params);
						
						dialog.show();

					}
				});

				map.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ArrayList<AlbumItem> arrayItems = new ArrayList<AlbumItem>();
						arrayItems.add(getItem());
						Intent i = new Intent(getActivity(), MapActivity.class);
						i.putExtra(ITEMS, arrayItems);
						startActivity(i);
						
						dialog.dismiss();
					}
				});
				
				share.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});

				dialog.show();

			}
		});

		return zoomView;
	}

	
	

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ImageWorker.cancelWork(imageView);

	}

	private AlbumItem getItem() {
		if (getArguments().get(ITEMS) instanceof AlbumItem) {
			return (AlbumItem) getArguments().get(ITEMS);
		} else {
			return null;
		}
	}
}
