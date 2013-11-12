package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ubadb.core.testDoubles.DummyObjectFactory;

public class LRUChainTest 
{
	@Test
	public void testRegionsAreEmptyOnCreation() throws Exception
	{		
		int sizeColdRegion = 4;
		int sizeHotRegion = 4;
		int hotCriteria = 2;
		int coolCount = 1;
		
		LRUChain chain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
		
		assertTrue(chain.coldRegion().isEmpty());
		assertTrue(chain.hotRegion().isEmpty());
	}
	
	@Test
	public void testNewFramesAreInsertedInTheColdRegion() throws Exception
	{	
		int sizeColdRegion = 4;
		int sizeHotRegion = 4;
		int hotCriteria = 2;
		int coolCount = 1;
		
		LRUChain chain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
		
		TCBufferFrame frame = this.aFrameWithTouchCount(1);
		
		chain.addNewFrame(frame);
		
		assertTrue(chain.coldRegion().contains(frame));
		assertEquals(chain.coldRegion().getLast(), frame);
		assertEquals(0, frame.touchCount());
	}
	
	@Test(expected=Exception.class)
	public void testInsertionFailsIfCapacityOfColdRegionIsExceeded() throws Exception
	{
		int sizeColdRegion = 1;
		int sizeHotRegion = 4;
		int hotCriteria = 2;
		int coolCount = 1;
		
		LRUChain chain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
		
		TCBufferFrame frame1 = aFrame();
		TCBufferFrame frame2 = aFrame();
		
		chain.addNewFrame(frame1);
		chain.addNewFrame(frame2);
	}
	
	@Test
	public void testFramesWithCountGreaterThanHotCriteriaOnColdRegionAreMovedToHotRegionOnUpdate() throws Exception
	{
		int sizeColdRegion = 4;
		int sizeHotRegion = 4;
		int hotCriteria = 2;
		int coolCount = 1;
		
		LRUChain chain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
		
		TCBufferFrame frameToBeMovedToHotRegion = aFrame();
		TCBufferFrame frameThatStaysInColdRegion = aFrame();
		
		chain.addNewFrame(frameToBeMovedToHotRegion);
		chain.addNewFrame(frameThatStaysInColdRegion);
		
		frameThatStaysInColdRegion.setTouchCount(hotCriteria-1);
		frameToBeMovedToHotRegion.setTouchCount(hotCriteria+1);
		
		chain.update();
		
		assertTrue(chain.coldRegion().contains(frameThatStaysInColdRegion));
		assertTrue(chain.hotRegion().contains(frameToBeMovedToHotRegion));
		assertEquals(0, frameToBeMovedToHotRegion.touchCount());
		assertEquals(1, frameThatStaysInColdRegion.touchCount());
	}
	
	@Test
	public void testLastFrameInHotRegionIsMovedToColdRegionWhenOtherFrameIsAddedToHotRegion() throws Exception
	{
		int hotCriteria = 2;
		int coolCount = 1;
		int sizeColdRegion = 2;
		int sizeHotRegion = 1;
		
		LRUChain chain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
		
		TCBufferFrame frame1 = aFrame();
		TCBufferFrame frame2 = aFrame();
		
		chain.addNewFrame(frame1);
		chain.addNewFrame(frame2);
		
		frame1.setTouchCount(hotCriteria+1);
		chain.update();
		frame2.setTouchCount(hotCriteria+1);
		chain.update();
		
		assertTrue(chain.coldRegion().contains(frame1));
		assertTrue(chain.hotRegion().contains(frame2));
		assertEquals(coolCount, frame1.touchCount());
		assertEquals(0, frame2.touchCount());
	}
	
	/* Factories */
	private TCBufferFrame aFrame() 
	{
		return new TCBufferFrame(DummyObjectFactory.PAGE);
	}
		
	private TCBufferFrame aFrameWithTouchCount(int n)
	{
		TCBufferFrame frame = aFrame();
		frame.setTouchCount(n);
		
		return frame;
	}
	
}
