package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import java.util.ArrayDeque;

public class LRUChain
{
	private int sizeColdRegion;
	private int sizeHotRegion;
	private int hotCriteria;
	private int coolCount;
	private ArrayDeque<TCBufferFrame> coldRegion;
	private ArrayDeque<TCBufferFrame> hotRegion;
	
	public LRUChain(int sizeColdRegion, int sizeHotRegion, int hotCriteria, int coolCount) 
	{
		this.sizeColdRegion = sizeColdRegion;
		this.sizeHotRegion = sizeHotRegion;
		this.hotCriteria = hotCriteria;
		this.coolCount = coolCount;
		coldRegion = new ArrayDeque<TCBufferFrame>(sizeColdRegion);
		hotRegion = new ArrayDeque<TCBufferFrame>(sizeHotRegion);
	}
	
	public void addNewFrame(TCBufferFrame frame) throws Exception
	{
		if(coldRegion.size() >= sizeColdRegion) {
			throw new Exception("LRU chain capacity exceeded");
		}
		
		coldRegion.add(frame);
		frame.setTouchCount(0);
	}
	
	public void update() 
	{
		for(TCBufferFrame frame : coldRegion) 
		{
			if(frame.touchCount() > hotCriteria)
			{
				moveFrameFromColdToHot(frame);
				frame.setTouchCount(0);
			}
		}
	}
	
	public void removeFromColdRegion(TCBufferFrame frame) throws Exception
	{
		if(coldRegion.contains(frame)) {
			throw new Exception("Frame is not in cold region");
		}
		
		coldRegion.remove(frame);
	}
	
	public ArrayDeque<TCBufferFrame> coldRegion()
	{
		return this.coldRegion;
	}
	
	public ArrayDeque<TCBufferFrame> hotRegion()
	{
		return this.hotRegion;
	}
	
	private void moveFrameFromColdToHot(TCBufferFrame frame)
	{
		coldRegion.remove(frame);
		
		if(hotRegion.size() == sizeHotRegion)
		{
			moveLastFrameFromHotToCold();
		}
		
		hotRegion.add(frame);
	}

	private void moveLastFrameFromHotToCold() {
		TCBufferFrame frameToRemove = hotRegion.poll();
		
		coldRegion.add(frameToRemove);
		
		frameToRemove.setTouchCount(coolCount);
	}
}
