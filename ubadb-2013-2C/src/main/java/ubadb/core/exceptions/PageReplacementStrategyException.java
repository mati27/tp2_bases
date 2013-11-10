package ubadb.core.exceptions;

@SuppressWarnings("serial")
public class PageReplacementStrategyException extends Exception
{
	public PageReplacementStrategyException(String message)
	{
		super(message);
	}
	
	public PageReplacementStrategyException(String message, Exception e)
	{
		super(message,e);
	}
}
