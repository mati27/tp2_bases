package ubadb.core.exceptions;

@SuppressWarnings("serial")
public class BufferPoolException extends Exception
{
	public BufferPoolException(String message)
	{
		super(message);
	}
	
	public BufferPoolException(String message, Exception e)
	{
		super(message,e);
	}
}
