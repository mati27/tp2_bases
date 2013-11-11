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
		LRUChain chain = new LRUChain(4, 5);
		
		assertTrue(chain.coldRegion().isEmpty());
		assertTrue(chain.hotRegion().isEmpty());
	}
	
	@Test
	public void testNewFramesAreInsertedInTheColdRegion() throws Exception
	{	
		LRUChain chain = new LRUChain(4, 4);
		
		TCBufferFrame frame = this.aFrameWithTouchCount(1);
		
		chain.addNewFrame(frame);
		
		assertTrue(chain.coldRegion().contains(frame));
		assertEquals(chain.coldRegion().getLast(), frame);
		assertEquals(0, frame.touchCount());
	}
		
	private TCBufferFrame aFrameWithTouchCount(int n)
	{
		TCBufferFrame frame = new TCBufferFrame(DummyObjectFactory.PAGE);
		frame.setTouchCount(n);
		
		return frame;
	}
	
}
