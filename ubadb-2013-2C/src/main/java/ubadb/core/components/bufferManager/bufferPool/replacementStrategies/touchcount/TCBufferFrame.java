package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import java.util.Date;

import ubadb.core.common.Page;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;

public class TCBufferFrame extends BufferFrame
{
	private Date creationDate;
	
	private int touchCounter;
	
	public TCBufferFrame(Page page)
	{
		super(page);
		creationDate = new Date();
		touchCounter = 0;
	}
	
	public Date getCreationDate()
	{
		return creationDate;
	}
	
	public void pin() {
		super.pin();
		touch();
	}
	
	public void touch()
	{
		touchCounter++;
	}
}
