package com.android.project.db;

import java.util.Map;

import com.android.project.model.AlbumItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

;

/**
 * Provides data base access
 */
public class DBConnector extends SQLiteOpenHelper {
	private static final String TAG = "DBConnector";

	private static final String DATABASE_NAME = "Showrooms.db";
	private static final int DB_VERSION = 5;

	public static final String DB_COLUMN_STATE = "record_state";
	public static final String DB_COLUMN_RESULT = "operation_result";
	public static final String DB_COLUMN_ID = "_id";
	public static final String DB_RECORD_CREATION_TIME = "creation_timestamp";

	private static DBConnector instance = null;

	private SQLiteDatabase sqlite;

	/**
	 * Escapes quotes from a string
	 * 
	 * @param s
	 */
	public static String Q(String s) {
		return s.replace("'", "''");
	}

	/**
	 * Open database
	 * 
	 * @throws DBException
	 */
	private void openDataBase() throws DBException {
		try {
			this.sqlite = this.getWritableDatabase();
		} catch (SQLiteException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Close database
	 * 
	 * @throws DBException
	 */
	private void closeDataBase() throws DBException {
		if (this.sqlite != null && this.sqlite.isOpen()) {
			try {
				this.sqlite.close();
			} catch (SQLiteException e) {
				throw new DBException(e);
			} finally {
				this.sqlite = null;
			}
		} else {
			throw new DBException(
					"Attempt to call closeDataBase() on closed database");
		}
	}

	public DBConnector(Context ctx) throws DBException {
		super(ctx, DATABASE_NAME, null, DB_VERSION);
	}

	/**
	 * Initializes the database connector. Must be called once on application
	 * initialization.
	 */
	public synchronized static void initialize(Context ctx) throws DBException {
		if (instance == null) {
			instance = new DBConnector(ctx);
			instance.openDataBase();
			Log.i(DBConnector.class.getSimpleName(), "Database initialized");
		}
	}

	/**
	 * Finalizes the database connector. Must be called once on application
	 * finalization.
	 */
	public static void finish() throws DBException {
		if (instance != null) {
			instance.closeDataBase();
			instance = null;
			Log.i(DBConnector.class.getSimpleName(), "Database finalized");
		}
	}

	/**
	 * Executes raw SQL statement with args
	 */
	public synchronized void rawSQL(String sql, Object[] bindArgs)
			throws DBSQLException {
		try {
			this.sqlite.execSQL(sql, bindArgs);
		} catch (SQLException e) {
			throw new DBSQLException(e);
		}
	}

	/**
	 * Executes raw SQL statement
	 */
	public synchronized void rawSQL(String sql) throws DBSQLException {
		try {
			this.sqlite.execSQL(sql, new Object[] {});
		} catch (SQLException e) {
			throw new DBSQLException(e);
		}
	}

	/**
	 * Executes sequence of SQL statements
	 */
	public synchronized void rawSQL(String[] sqls) throws DBSQLException {
		for (String sql : sqls) {
			rawSQL(sql);
		}
	}

	/**
	 * Executes update statement
	 */
	public synchronized int update(String table, ContentValues values,
			String whereClause, String[] whereArgs) throws DBSQLException {
		try {
			return this.sqlite.update(table, values, whereClause, whereArgs);
		} catch (SQLException e) {
			throw new DBSQLException(e);
		}
	}

	/**
	 * Executes insert statement
	 */
	public synchronized long insert(String table, ContentValues values)
			throws DBSQLException {
		try {
			Log.d("DBConnector", "inserting values:"+values+" in table :"+table);
			return this.sqlite.insert(table, null, values);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSQLException(e);
		}
	}

	/**
	 * Executes delete statement
	 */
	public synchronized long delete(String table, String selection,
			String[] selectionArgs) throws DBSQLException {
		try {
			return this.sqlite.delete(table, selection, selectionArgs);
		} catch (SQLException e) {
			throw new DBSQLException(e);
		}
	}

	/**
	 * Executes formated SQL queries
	 */
	public synchronized Cursor query(String table, String[] columns,
			String selection, String[] selectionArgs, String orderBy)
			throws DBSQLException {
		try {
			
			Log.d("DBConnection", "table: "+table+" columns: "+columns+" selection: "+selection);
			return this.sqlite.query(table, columns, selection, selectionArgs,
					null /*
						 * no groupding
						 */, null /*
								 * no having
								 */, orderBy);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSQLException(e);
		}
	}

	/**
	 * Executes formated SQL queries
	 */
	public synchronized Cursor query(String table, String[] columns)
			throws DBSQLException {
		return query(table, columns, null, null, null);
	}

	/**
	 * Executes SQL prepared statement
	 */
	public synchronized Cursor rawQuery(String sql) throws DBSQLException {
		try {
			return this.sqlite.rawQuery(sql, null);
		} catch (SQLException e) {
			throw new DBSQLException(e);
		}
	}

	/**
	 * Begin transaction
	 */
	public synchronized void beginTransaction() throws DBSQLException {
		try {
			this.sqlite.beginTransaction();
		} catch (SQLException e) {
			throw new DBSQLException(e);
		}
	}

	/**
	 * End transaction
	 */
	public synchronized void endTransaction() {
		this.sqlite.endTransaction();
	}

	/**
	 * set successful transaction
	 */
	public synchronized void setTransactionSuccessful() throws DBSQLException {
		this.sqlite.setTransactionSuccessful();
	}

	/**
	 * Executes formated SQL queries
	 * 
	 * @param table
	 * @param columnMap
	 *            - binds new column names to old column names
	 * @param projectionIn
	 *            - columns to be returned (should match the new column names
	 *            provided in the columnMap)
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 * @throws DBSQLException
	 */
	public synchronized Cursor queryWithProjectionMap(String table,
			Map<String, String> columnMap, String[] projectionIn,
			String selection, String[] selectionArgs, String orderBy)
			throws DBSQLException {
		try {
			SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
			builder.setTables(table);
			builder.setProjectionMap(columnMap);
			Cursor cursor = builder.query(this.sqlite, projectionIn, selection,
					selectionArgs, null, null, orderBy);
			return cursor;
		} catch (SQLException e) {
			throw new DBSQLException(e);
		}
	}

	/**
	 * Singleton access method
	 * 
	 * @return DBConnector
	 * @throws DBException
	 */
	public static synchronized DBConnector getInstance() throws DBException {
		if (instance == null)
			throw new DBException(
					"initialize() must be called prior obtaining an instance");

		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(TAG, "Creating database version:" + DB_VERSION);

		db.execSQL(AlbumItem.SQL_CREATETE_TABLE);


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version = <" + oldVersion
				+ "> to version = <" + newVersion + ">");

		db.execSQL("DROP TABLE IF EXISTS " + AlbumItem.SQL_TABLE_NAME);
		

		// Recreate the tables...
		onCreate(db);
	}
}
