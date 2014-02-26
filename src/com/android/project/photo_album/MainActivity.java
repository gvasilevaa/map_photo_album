package com.android.project.photo_album;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.android.project.db.DBConnector;
import com.android.project.db.DBException;
import com.android.project.model.AlbumItem;
import com.android.project.model.AlbumItemManager;
import com.android.project.model.ApplicationConstants;
import com.android.project.tasks.GetUserCheckInsTask;
import com.android.project.utils.CustomProgressDialog;
import com.android.project.zoom.ZoomImagesActivity;

;

public class MainActivity extends FragmentActivity implements
		ApplicationConstants {

	private final static String USER_PLACES = "http://int.f1rstt.com/api/users/me/checkins?auth_token=uojWKxf57QhqhxipAWqK&limit=10&page=1";
	private final static String DETAILS = "details";

	private AlertDialog alert;
	private GridView gridView;
	private ListView listView;
	private GetUserCheckInsTask getPlaces;
	private ArrayList<AlbumItem> items;
	private AlbumGridAdapter gridAdapter;
	private AlbumListAdapter listAdapter;
	private Cursor c = null;
	private LayoutInflater inflater;

	private CustomProgressDialog dialog;

	/* ------------------- flags ----------------- */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView_gallery);
		listView = (ListView) findViewById(R.id.listView_gallery);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		initDatabase();

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parentView,
					View childView, int position, long id) {
				// Intent i = new Intent(MainActivity.this,
				// EditDetailsActivity.class);
				// i.putExtra(ITEMS, items.get(position));
				// startActivity(i);
				ItemDetails.instance(items.get(position), MainActivity.this)
						.show(getSupportFragmentManager(), DETAILS);
				return true;
			}
		});

		gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parentView,
					View childView, int position, long id) {

				// Intent i = new Intent(MainActivity.this,
				// EditDetailsActivity.class);
				// i.putExtra(ITEMS, items.get(position));
				// startActivity(i);
				ItemDetails.instance(items.get(position), MainActivity.this)
						.show(getSupportFragmentManager(), DETAILS);

				return true;
			}
		});

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(MainActivity.this,
						ZoomImagesActivity.class);
				i.putExtra(ITEMS, items);
				i.putExtra(POSITION, position);
				startActivity(i);
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(MainActivity.this,
						ZoomImagesActivity.class);
				i.putExtra(ITEMS, items);
				i.putExtra(POSITION, position);
				startActivity(i);
			}
		});

		dialog = new CustomProgressDialog(MainActivity.this);
	}

	@Override
	protected void onDestroy() {
		if (getPlaces != null) {
			getPlaces.cancel(true);
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {

		if (getPlaces != null) {
			getPlaces.cancel(true);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		
			getPlaces();
		
		super.onResume();
	}

	/**
	 * Initialize the database.
	 */
	private void initDatabase() {
		try {
			// Initialize DB instance
			DBConnector.initialize(this);
		} catch (DBException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false)
					.setTitle(R.string.exc_error)
					.setMessage(e.getLocalizedMessage())
					.setNeutralButton(R.string.exc_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									MainActivity.this.finish();
								}
							});
			alert = builder.create();
			alert.show();
		}
	}

	private void getPlaces() {

		try {
			c = AlbumItemManager.getInstance().getAllItems();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.moveToFirst();
		if (c != null && c.getCount() > 0) {
			items = new ArrayList<AlbumItem>();
			for (int i = 0; i < c.getCount(); i++) {
				AlbumItem item = new AlbumItem(c.getInt(0), c.getString(1),
						c.getString(2), c.getString(3), c.getString(4),
						c.getString(5), c.getString(6), c.getString(7));
				items.add(item);
				c.moveToNext();
			}

			populateItems();

		} else {

//			dialog.show();
//
//			getPlaces = new GetUserCheckInsTask() {
//
//				@Override
//				protected void onPostExecute(final ArrayList<AlbumItem> places) {
//					getPlaces = null;
//					// showProgress(false);
//					if (dialog != null && dialog.isShowing()) {
//						dialog.dismiss();
//					}
//
//					if (places != null && places.size() > 0) {
//						items = places;
//
//						populateItems();
//
//					} else {
//
//						Toast.makeText(MainActivity.this,
//								"Sotty couldn't fetch places! ",
//								Toast.LENGTH_SHORT).show();
//
//					}
//
//				}
//
//				@Override
//				protected void onCancelled() {
//					getPlaces = null;
//					if (dialog != null && dialog.isShowing()) {
//						dialog.dismiss();
//					}
//					// showProgress(false);
//				}
//
//			};
//
//			getPlaces.execute(USER_PLACES);
//
		}

	}

	public void populateItems() {

		gridAdapter = new AlbumGridAdapter(MainActivity.this,
				MainActivity.this, items);
		gridView.setAdapter(gridAdapter);
		gridAdapter.notifyDataSetChanged();

		listAdapter = new AlbumListAdapter(MainActivity.this,
				MainActivity.this, items);
		listView.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

	}

	/* ----------------- OnClick Methods ------------------- */
	public void menuOnClick(View view) {

		final Dialog dialogMenu = new Dialog(this, R.style.CustomDialog);

		View settingsView = inflater.inflate(R.layout.gallery_settings_layout,
				null);

		((TextView) settingsView.findViewById(R.id.title))
				.setText(getResources().getString(R.string.view_as));

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		dialogMenu.setContentView(settingsView, params);

		TextView viewAsTable = (TextView) settingsView.findViewById(R.id.grid);
		TextView viewAsList = (TextView) settingsView.findViewById(R.id.list);
		TextView viewAsMap = (TextView) settingsView.findViewById(R.id.map);

		viewAsTable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMenu.dismiss();
				gridView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);

			}
		});

		viewAsList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMenu.dismiss();
				gridView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
			}
		});

		viewAsMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMenu.dismiss();
				Intent i = new Intent(MainActivity.this, MapActivity.class);
				i.putExtra(ITEMS, items);
				startActivity(i);
			}
		});

		dialogMenu.show();

		// if (menuTable) {
		// menuTable = false;
		// gridView.setVisibility(View.GONE);
		// listView.setVisibility(View.VISIBLE);
		// // view.setBackgroundResource(R.drawable.list_icon);
		// view.setSelected(true);
		// } else {
		// menuTable = true;
		// gridView.setVisibility(View.VISIBLE);
		// listView.setVisibility(View.GONE);
		// view.setSelected(false);
		// // view.setBackgroundResource(R.drawable.table_icon);
		// }
	}
	
	public void onCreateClick(View v){
		Intent i = new Intent(MainActivity.this, AddItemActivity.class);
		startActivity(i);
	}

	public void sortOnClick(View view) {

		final Dialog dialog = new Dialog(this, R.style.CustomDialog);

		View sortByView = inflater.inflate(R.layout.view_filter_items, null);

		// View customTitleView = inflater.inflate(
		// R.layout.view_filter_title, null);

		((TextView) sortByView.findViewById(R.id.filterText))
				.setText(getResources().getString(R.string.sort_by));
		// builder.setView(sortByView);
		// builder.setCustomTitle(customTitleView);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dialog.setContentView(sortByView, params);

		TextView sortByName = (TextView) sortByView.findViewById(R.id.by_name);
		TextView sortByDate = (TextView) sortByView.findViewById(R.id.by_date);

		sortByDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// builder.
				dialog.dismiss();
				sortByDate();

			}
		});

		sortByName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				sortByName();

			}
		});

		dialog.show();

	}

	protected void sortByDate() {

		try {
			c = AlbumItemManager.getInstance().getByDateItems();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.moveToFirst();
		if (c != null && c.getCount() > 0) {
			items = new ArrayList<AlbumItem>();
			for (int i = 0; i < c.getCount(); i++) {
				AlbumItem item = new AlbumItem(c.getInt(0), c.getString(1),
						c.getString(2), c.getString(3), c.getString(4),
						c.getString(5), c.getString(6), c.getString(7));
				items.add(item);
				c.moveToNext();
			}

			populateItems();
		}

	}

	protected void sortByName() {
		try {
			c = AlbumItemManager.getInstance().getByNameItems();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.moveToFirst();
		if (c != null && c.getCount() > 0) {
			items = new ArrayList<AlbumItem>();
			for (int i = 0; i < c.getCount(); i++) {
				AlbumItem item = new AlbumItem(c.getInt(0), c.getString(1),
						c.getString(2), c.getString(3), c.getString(4),
						c.getString(5), c.getString(6), c.getString(7));
				items.add(item);
				c.moveToNext();
			}

			populateItems();
		}

	}

	
}
