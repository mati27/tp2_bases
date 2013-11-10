package ubadb.core.exceptions;

@SuppressWarnings("serial")
public class DiskManagerException extends Exception
{
	public DiskManagerException(String message)
	{
		super(message);
	}
	
	public DiskManagerException(String message, Exception e)
	{
		super(message,e);
	}
}
