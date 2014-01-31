package com.android.project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.project.db.DBException;
import com.android.project.utils.StringUtils;

import android.content.ContentValues;

public class AlbumItem implements ApplicationConstants, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum GET_COLUMNS {

		ID("id"), NAME("name"), THUMBNAIL("thumbnail"), LATITUDE("latitude"), LONGITUDE(
				"longitude"), ADDRESS("address"), CREATED_AT("created_at"), DESCRIPTION(
				"description");

		private String name;

		private GET_COLUMNS(String val) {
			name = val;
		}

		public String getName() {
			return name;
		}
	}

	public static final String SQL_TABLE_NAME = "album_list";
	public static final String SQL_CREATETE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ SQL_TABLE_NAME
			+ "( "
			+ GET_COLUMNS.ID.getName()
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ GET_COLUMNS.NAME.getName()
			+ " nvarchar(50), "
			+ GET_COLUMNS.THUMBNAIL.getName()
			+ " nvarchar(70), "
			+ GET_COLUMNS.LATITUDE.getName()
			+ " nvarchar(70), "
			+ GET_COLUMNS.LONGITUDE.getName()
			+ " nvarchar(70), "
			+ GET_COLUMNS.ADDRESS.getName()
			+ " nvarchar(70), "
			+ GET_COLUMNS.CREATED_AT.getName()
			+ " nvarchar(70), "
			+ GET_COLUMNS.DESCRIPTION.getName() + " text);";

	private int id;
	private String name;
	private String thumbnail;
	private String latitude;
	private String longitude;
	private String address;
	private String createdAt;
	private String desctioption;
	

	/*
	 * Create empty AlbumItem
	 * 
	 * **
	 */
	public AlbumItem() {

	}

	/**
	 * Creates a new AlbumItem object from a params
	 * @param di 
	 * 
	 * 
	 */
	public AlbumItem(int id, String name, String thumbnail, String latitude,
			String longitude, String address, String createdAt,
			String desctioption) {
		this.id = id;
		this.address = address;
		this.createdAt = createdAt;
		this.desctioption = desctioption;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.thumbnail = thumbnail;

	}

	/**
	 * 
	 * @return ContentValues object containing data from the calling AlbumItem
	 *         object
	 */
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(GET_COLUMNS.ID.getName(), this.id);
		values.put(GET_COLUMNS.NAME.getName(), this.name);
		values.put(GET_COLUMNS.THUMBNAIL.getName(), this.thumbnail);
		values.put(GET_COLUMNS.LATITUDE.getName(), this.latitude);
		values.put(GET_COLUMNS.LONGITUDE.getName(), this.longitude);
		values.put(GET_COLUMNS.ADDRESS.getName(), this.address);
		values.put(GET_COLUMNS.CREATED_AT.getName(), this.createdAt);
		values.put(GET_COLUMNS.DESCRIPTION.getName(), this.desctioption);

		return values;
	}

	/*----------------- Parse information from JSONObject--------------------------*/
	public static ArrayList<AlbumItem> parseFromJSON(JSONObject json) {
		ArrayList<AlbumItem> items = new ArrayList<AlbumItem>();
		try {

			JSONArray jsonData = json.getJSONArray(DATA);
			for (int i = 0; i < jsonData.length(); i++) {
				AlbumItem place = new AlbumItem();
				JSONObject jsonItem = jsonData.getJSONObject(i);

				// date
				place.createdAt = StringUtils.parseUnixTimeToDate(jsonItem
						.getLong(CREATED_AT));

				// get place
				JSONObject jsonPlace = jsonItem.getJSONObject(PLACE);

				place.name = jsonPlace.optString(NAME);

				// avatar
				JSONObject jsonPlaceAvatar = jsonPlace.getJSONObject(AVATAR);

				place.thumbnail = jsonPlaceAvatar.getString(ACTIVITY);

				// address
				JSONObject jsonPlaceAddr = jsonPlace.getJSONObject(ADDRESS);
				place.address = jsonPlaceAddr.getString(STREET);

				place.latitude = jsonPlaceAddr.getJSONObject(LOCATION)
						.getString(LAT);
				place.longitude = jsonPlaceAddr.getJSONObject(LOCATION)
						.getString(LON);

				AlbumItemManager.getInstance().saveProduct(place);
				items.add(place);

			}

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;

	}

	// getter an setters for Item fields
	

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getDesctioption() {
		return desctioption;
	}

	public void setDesctioption(String desctioption) {
		this.desctioption = desctioption;
	}

}