package com.android.project.db;

/**
 * Throwable class for exceptions in SQL statements
 */
@SuppressWarnings("serial")
public class DBSQLException extends DBException
{
	public DBSQLException(String detailMessage)
	{
		super(detailMessage);
	}

	public DBSQLException(Exception e)
	{
		super(e);
	}
}
