package ubadb.core.components.bufferManager.bufferPool.replacementStrategies;

import java.util.Date;

import ubadb.core.common.Page;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;

public class RUBufferFrame extends BufferFrame {
	private Date lastTimeUsed;
 
	public RUBufferFrame(Page page)
	{
		super(page);
		this.updateLastTimeUsed();
	}
	
	public void pin()
	{
		super.pin();
		
		this.updateLastTimeUsed();
	}
	
	public Date lastTimeUsed()
	{
		return this.lastTimeUsed;
	}
	
	private void updateLastTimeUsed()
	{
		this.lastTimeUsed = new Date();
	}
}
