package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import java.util.ArrayDeque;
import java.util.ArrayList;

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
		if(coldRegion.size() == sizeColdRegion && hotRegion.size() == sizeHotRegion) {
			throw new Exception("LRU chain capacity exceeded");
		}
		
		if(hotRegion.size() < sizeHotRegion) {
			hotRegion.add(frame);
		} else {
			coldRegion.add(frame);
		}
		
		frame.setTouchCount(0);
	}
	
	public void update() 
	{
		for(TCBufferFrame frame : coldRegion) 
		{
			if(frame.touchCount() > hotCriteria)
			{
				// Java throw an exception if the add with an active iterator
				moveFrameFromColdToHot(frame);
				frame.setTouchCount(0);
			}
		}
		
		while(hotRegion.size() > sizeHotRegion) {
			moveLastFrameFromHotToCold();
		}
	}
	
	public void removeFromColdRegion(TCBufferFrame frame) throws Exception
	{
		if(!coldRegion.contains(frame)) {
			throw new Exception("Frame is not in cold region");
		}
		
		coldRegion.remove();
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
		coldRegion.remove();
		
/*		if(hotRegion.size() == sizeHotRegion)
		{
			moveLastFrameFromHotToCold();
		}
*/
		
		hotRegion.add(frame);
	}

	private void moveLastFrameFromHotToCold() {
		TCBufferFrame frameToRemove = hotRegion.poll();
		
		coldRegion.add(frameToRemove);
		
		frameToRemove.setTouchCount(coolCount);
	}
	
	public ArrayList<TCBufferFrame> framesInOrder() {
		ArrayList<TCBufferFrame> framesInOrder = new ArrayList();
		
		for(TCBufferFrame frame : coldRegion) 
		{
			framesInOrder.add(frame);
		}
		for(TCBufferFrame frame : hotRegion) 
		{
			framesInOrder.add(frame);
		}		
		return framesInOrder;
		
	}
}
