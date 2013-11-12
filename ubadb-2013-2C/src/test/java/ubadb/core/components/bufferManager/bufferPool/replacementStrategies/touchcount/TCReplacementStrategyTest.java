package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.exceptions.PageReplacementStrategyException;
import ubadb.core.testDoubles.DummyObjectFactory;
import ubadb.core.util.TestUtil;


public class TCReplacementStrategyTest
{
	private TCReplacementStrategy strategy;
	
	@Before
	public void setUp()
	{
		int sizeLRUChain = 4;
		int percentHotDefault = 50;
		int hotCriteria = 2;
		int coolCount = 1;
		
		strategy = new TCReplacementStrategy(sizeLRUChain, percentHotDefault, hotCriteria, coolCount);
	}
	
	@Test(expected=PageReplacementStrategyException.class)
	public void testThereIsNoPageToReplaceWhenAllFramesInColdRegionArePinned() throws Exception
	{
		BufferFrame frame0 = createNewFrame();
		BufferFrame frame1 = createNewFrame();
		
		frame0.pin();
		frame1.pin();
		
		strategy.findVictim(Arrays.asList(frame0,frame1));
	}
	
	@Test(expected=PageReplacementStrategyException.class)
	public void testNoFramesToReplaceWhenAllFramesAreInHotRegion() throws Exception
	{
		BufferFrame frame0 = createNewFrame();
		BufferFrame frame1 = createNewFrame();
		
		setTouchCountTo(frame0, 3);
		setTouchCountTo(frame1, 3);
		
		strategy.findVictim(Arrays.asList(frame0,frame1));
	}
	
	@Test
	public void testLastFrameThatRemainsInColdRegionIsReplacedWhenLRUChainIsFull() throws Exception
	{
//		BufferFrame frame0 = createNewFrame();
//		BufferFrame frame1 = createNewFrame();
//		BufferFrame frame2 = createNewFrame();
//		BufferFrame frame3 = createNewFrame();
//		
//		
//		
//		assertEquals(frame0,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
	}
//
//	@Test
//	public void testMultiplePagesToReplaceButOldestOnePinned() throws Exception
//	{
//		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
//		Thread.sleep(TestUtil.PAUSE_INTERVAL);	//Add a sleep so that frame dates are different
//		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
//		Thread.sleep(TestUtil.PAUSE_INTERVAL);
//		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
//		
//		frame0.pin();
//		
//		assertEquals(frame1,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
//	}

	private BufferFrame createNewFrame() 
	{
		return strategy.createNewFrame(DummyObjectFactory.PAGE);
	}
	
	private void setTouchCountTo(BufferFrame frame, int touchCount)
	{
		TCBufferFrame tcBufferFrame = (TCBufferFrame) frame;
		tcBufferFrame.setTouchCount(touchCount);
	}
}
