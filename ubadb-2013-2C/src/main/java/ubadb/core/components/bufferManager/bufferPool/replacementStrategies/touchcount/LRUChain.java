package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import java.util.ArrayDeque;

public class LRUChain
{
	private int sizeColdRegion;
	private int sizeHotRegion;
	private ArrayDeque<TCBufferFrame> coldRegion;
	private ArrayDeque<TCBufferFrame> hotRegion;
	
	public LRUChain(int sizeColdRegion, int sizeHotRegion) 
	{
		this.sizeColdRegion = sizeColdRegion;
		this.sizeHotRegion = sizeHotRegion;
		coldRegion = new ArrayDeque<TCBufferFrame>(sizeColdRegion);
		hotRegion = new ArrayDeque<TCBufferFrame>(sizeHotRegion);
	}
	
	public void addNewFrame(TCBufferFrame frame) 
	{
		coldRegion.add(frame);
		frame.setTouchCount(0);
	}
	
	public void update() 
	{
		
	}
	
	public ArrayDeque<TCBufferFrame> coldRegion()
	{
		return this.coldRegion;
	}
	
	public ArrayDeque<TCBufferFrame> hotRegion()
	{
		return this.hotRegion;
	}
}
