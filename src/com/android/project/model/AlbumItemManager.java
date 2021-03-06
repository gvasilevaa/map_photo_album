package com.android.project.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.android.project.db.DBConnector;
import com.android.project.db.DBException;
import com.android.project.db.DBSQLException;

public class AlbumItemManager {

	private static AlbumItemManager instance;
	public AlbumItem mItem = null;

	/**
	 * Get a singleton self reference object of AlbumItemManager class
	 */
	public static synchronized AlbumItemManager getInstance() {
		if (instance == null) {
			instance = new AlbumItemManager();
		}

		return instance;
	}

	/**
	 * Saves/adds a Item to the local database
	 * 
	 */
	public long saveProduct(AlbumItem item) throws DBException {
		long result = -1l;
		if (item != null) {
			result = DBConnector.getInstance().insert(AlbumItem.SQL_TABLE_NAME,
					item.getContentValues());
			mItem = item;
			
		} else {

		}
		return result;
	}

	/**
	 * Get item form DB
	 * @param itemId
	 * @return Cursor
	 * @throws DBException
	 */
	 
	public Cursor getItem(long itemId) throws DBException {

		return DBConnector.getInstance().query(
				AlbumItem.SQL_TABLE_NAME,
				new String[] { AlbumItem.GET_COLUMNS.ID.getName() + " as _id",
						AlbumItem.GET_COLUMNS.NAME.getName(),
						AlbumItem.GET_COLUMNS.THUMBNAIL.getName(),
						AlbumItem.GET_COLUMNS.DESCRIPTION.getName(),
						AlbumItem.GET_COLUMNS.LONGITUDE.getName(),
						AlbumItem.GET_COLUMNS.LATITUDE.getName(),
						AlbumItem.GET_COLUMNS.CREATED_AT.getName() },
				AlbumItem.GET_COLUMNS.ID.getName() + "=" + itemId, null, null);
	}

	/**
	 * Gets all Items from the local DB
	 * 
	 * @throws DBException
	 */
	public Cursor getAllItems() throws DBException {

		return DBConnector.getInstance().rawQuery(
				"select * from " + AlbumItem.SQL_TABLE_NAME);
	}

	/**
	 * 
	 * @param values
	 * @return int count of updated items
	 * @throws DBSQLException
	 * @throws DBException
	 */

	public int updateItem(ContentValues values) throws DBSQLException,
			DBException {
		return DBConnector.getInstance().update(
				AlbumItem.SQL_TABLE_NAME,
				values,
				AlbumItem.GET_COLUMNS.ID.getName()
						+ "="
						+ values.getAsInteger(AlbumItem.GET_COLUMNS.ID
								.getName()), null);

	}

	/**
	 * Gets all items sorted by name
	 * 
	 * @return Cursor
	 * @throws DBException
	 */
	public Cursor getByNameItems() throws DBException {

		return DBConnector.getInstance().query(
				AlbumItem.SQL_TABLE_NAME,
				new String[] { AlbumItem.GET_COLUMNS.ID.getName() + " as _id",
						AlbumItem.GET_COLUMNS.NAME.getName(),
						AlbumItem.GET_COLUMNS.THUMBNAIL.getName(),
						AlbumItem.GET_COLUMNS.LATITUDE.getName(),
						AlbumItem.GET_COLUMNS.LONGITUDE.getName(),
						AlbumItem.GET_COLUMNS.ADDRESS.getName(),
						AlbumItem.GET_COLUMNS.CREATED_AT.getName(),
						AlbumItem.GET_COLUMNS.DESCRIPTION.getName() }, null,
				null, AlbumItem.GET_COLUMNS.NAME.getName() + " ASC");
	}

	/**
	 * Gets sorted items by date
	 * 
	 * @return Cursor
	 * @throws DBException
	 */
	public Cursor getByDateItems() throws DBException {

		return DBConnector.getInstance().query(
				AlbumItem.SQL_TABLE_NAME,
				new String[] { AlbumItem.GET_COLUMNS.ID.getName() + " as _id",
						AlbumItem.GET_COLUMNS.NAME.getName(),
						AlbumItem.GET_COLUMNS.THUMBNAIL.getName(),
						AlbumItem.GET_COLUMNS.LATITUDE.getName(),
						AlbumItem.GET_COLUMNS.LONGITUDE.getName(),
						AlbumItem.GET_COLUMNS.ADDRESS.getName(),
						AlbumItem.GET_COLUMNS.CREATED_AT.getName(),
						AlbumItem.GET_COLUMNS.DESCRIPTION.getName() }, null,
				null, AlbumItem.GET_COLUMNS.CREATED_AT.getName() + " ASC");
	}

	/**
	 * Deletes all Items from the local DB
	 * 
	 * @throws DBException
	 */
	public void deleteAllItems() throws DBException {
		DBConnector.getInstance().delete(AlbumItem.SQL_TABLE_NAME, null, null);
	}

	/**
	 * Deletes a particular Item from the local DB
	 * 
	 */
	public void deleteItem(int itemId) throws DBException {
		DBConnector.getInstance().delete(AlbumItem.SQL_TABLE_NAME,
				AlbumItem.GET_COLUMNS.ID.getName() + "=" + itemId, null);

	}

}
