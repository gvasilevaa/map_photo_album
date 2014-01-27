package com.android.project.db;

/**
 * Throwable class for general database exceptions
 * 
 */
@SuppressWarnings("serial")
public class DBException extends DALException
{
	/**
	 * @param detailMessage
	 */
	public DBException(String detailMessage)
	{
		super(detailMessage);
	}

	/**
	 * @param throwable
	 */
	public DBException(Throwable throwable)
	{
		super(throwable);
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public DBException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}
}
