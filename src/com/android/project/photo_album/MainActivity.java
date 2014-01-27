package com.android.project.photo_album;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.db.DBConnector;
import com.android.project.db.DBException;
import com.android.project.model.AlbumItem;
import com.android.project.model.AlbumItemManager;
import com.android.project.tasks.GetUserCheckInsTask;

public class MainActivity extends FragmentActivity {

	private final static String USER_PLACES = "http://int.f1rstt.com/api/users/me/checkins?auth_token=uojWKxf57QhqhxipAWqK&limit=10&page=1";
	private AlertDialog alert;
	private GridView gridView;
	private ListView listView;
	private GetUserCheckInsTask getPlaces;
	private ArrayList<AlbumItem> items;
	private AlbumGridAdapter gridAdapter;
	private AlbumListAdapter listAdapter;

	/* ------------------- flags ----------------- */
	private boolean menuTable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView_gallery);
		listView = (ListView) findViewById(R.id.listView_gallery);

		initDatabase();

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
		Cursor c = null;
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
				AlbumItem item = new AlbumItem(c.getString(1), c.getString(2),
						c.getString(3), c.getString(4), c.getString(5),
						c.getString(6), c.getString(7));
				items.add(item);
				c.moveToNext();
			}

			populateItems();

		} else {

			getPlaces = new GetUserCheckInsTask() {

				@Override
				protected void onPostExecute(final ArrayList<AlbumItem> places) {
					getPlaces = null;
					// showProgress(false);
					// if (dialog != null && dialog.isShowing()) {
					// dialog.dismiss();
					// }

					if (places != null && places.size() > 0) {
						items = places;

						populateItems();

					} else {

						Toast.makeText(MainActivity.this,
								"Sotty couldn't fetch places! ",
								Toast.LENGTH_SHORT).show();

					}

				}

				@Override
				protected void onCancelled() {
					getPlaces = null;

					// showProgress(false);
				}

			};

			getPlaces.execute(USER_PLACES);

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

	}

	/* ----------------- OnClick Methods ------------------- */
	public void menuOnClick(View view) {
		if (menuTable) {
			menuTable = false;
			gridView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			// view.setBackgroundResource(R.drawable.list_icon);
			view.setSelected(true);
		} else {
			menuTable = true;
			gridView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			view.setSelected(false);
			// view.setBackgroundResource(R.drawable.table_icon);
		}
	}

	public void sortOnClick(View view) {

		final String[] items = new String[] { "Item 1", "Item 2", "Item 3",
				"Item 4" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Sort by");

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View filterTitleView = inflater.inflate(R.layout.view_filter_items,
				null);

		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				TextView txt = (TextView) findViewById(R.id.txt);
				txt.setText(items[which]);
			}
		});

		builder.show();
	}

}
