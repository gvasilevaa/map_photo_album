package com.android.project.db;

/**
 * Throwable class for general database exceptions
 * 
 */
@SuppressWarnings("serial")
public class DALException extends Exception
{
	@SuppressWarnings("unused")
	private DALException()
	{
	}

	/**
	 * @param detailMessage
	 */
	public DALException(String detailMessage)
	{
		super(detailMessage);
	}

	/**
	 * @param throwable
	 */
	public DALException(Throwable throwable)
	{
		super(throwable);
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public DALException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}
}
