package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import java.util.Queue;

public class LRUChain
{
	private Queue<TCBufferFrame> coldRegion;
	private Queue<TCBufferFrame> hotRegion;
	
	public LRUChain(int size) 
	{
		
	}
	
	public void addNewFrame(TCBufferFrame frame) 
	{
		
	}
	
	public void update() 
	{
		
	}
	
	public Queue<TCBufferFrame> coldRegion()
	{
		return this.coldRegion;
	}
}
