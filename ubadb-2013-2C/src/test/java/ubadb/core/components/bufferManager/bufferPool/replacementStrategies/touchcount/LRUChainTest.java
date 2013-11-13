package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
	public void testNewFramesAreInsertedInTheHotRegionWhileTheChainIsNotFull() throws Exception
	{
		int sizeColdRegion = 4;
		int sizeHotRegion = 4;
		int hotCriteria = 2;
		int coolCount = 1;
		
		LRUChain chain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
		
		TCBufferFrame frame = this.aFrame();
		
		chain.addNewFrame(frame);
		
		assertFalse(chain.coldRegion().contains(frame));
		assertTrue(chain.hotRegion().contains(frame));
		assertEquals(chain.hotRegion().getLast(), frame);
		assertEquals(0, frame.touchCount());
	}
	
	@Test
	public void testNewFramesAreInsertedInTheColdRegionWhenTheHotRegionIsFull() throws Exception
	{	
		LRUChain chain = anActiveChain();
		
		TCBufferFrame frame = this.aFrame();
		chain.addNewFrame(frame);
		
		assertTrue(chain.coldRegion().contains(frame));
		assertEquals(chain.coldRegion().size(), 2);
		assertEquals(chain.coldRegion().getLast(), frame);
		assertEquals(0, frame.touchCount());
	}
	
	@Test(expected=Exception.class)
	public void testInsertionFailsIfCapacityOfChainIsExceeded() throws Exception
	{
		int sizeColdRegion = 1;
		int sizeHotRegion = 1;
		int hotCriteria = 2;
		int coolCount = 1;
		
		LRUChain chain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
		
		TCBufferFrame frame1 = aFrame();
		TCBufferFrame frame2 = aFrame();
		TCBufferFrame frame3 = aFrame();
		
		chain.addNewFrame(frame1);
		chain.addNewFrame(frame2);
		chain.addNewFrame(frame3);
	}
	
	@Test
	public void testFramesWithCountGreaterThanHotCriteriaOnColdRegionAreMovedToHotRegionOnUpdate() throws Exception
	{
		LRUChain chain = anActiveChain();
		
		TCBufferFrame frameToBeMovedToHotRegion = aFrame();
		
		chain.addNewFrame(frameToBeMovedToHotRegion);
		frameToBeMovedToHotRegion.setTouchCount(3);
		
		chain.update();
		
		assertTrue(chain.hotRegion().contains(frameToBeMovedToHotRegion));
		assertEquals(0, frameToBeMovedToHotRegion.touchCount());
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
	
	@Test
	public void testFramesReturnsTheUnionOfColdRegionAndHotRegionFramesInOrder()
	{

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
	
	private LRUChain anActiveChain() throws Exception
	{
		LRUChain chain = new LRUChain(2, 2, 2, 1);
		
		TCBufferFrame frame1 = this.aFrame();
		TCBufferFrame frame2 = this.aFrame();
		TCBufferFrame frame3 = this.aFrame();
		
		chain.addNewFrame(frame1);
		chain.addNewFrame(frame2);
		chain.addNewFrame(frame3);
		
		return chain;
	}
	
}
