package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.lru;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.exceptions.PageReplacementStrategyException;
import ubadb.core.testDoubles.DummyObjectFactory;
import ubadb.core.util.TestUtil;
import static org.junit.Assert.assertEquals;

public class LRUReplacementStrategyTest {
	private LRUReplacementStrategy strategy;
	
	@Before
	public void setUp()
	{
		strategy = new LRUReplacementStrategy();
	}
	
	@Test(expected=PageReplacementStrategyException.class)
	public void testNoPageToReplace() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		
		frame0.pin();
		frame1.pin();
		
		strategy.findVictim(Arrays.asList(frame0,frame1));
	}
	
	@Test
	public void testFirstFrameCreatedIsSelectedWhenNoFrameWasPinned() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
			
		List<BufferFrame> frames = Arrays.asList(frame0, frame1, frame2);
		assertEquals(this.strategy.findVictim(frames), frame0);
	}
	
	@Test
	public void testLeastRecentlyUsedFrameIsSelectedWhenMultipleFramesAvailable() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		
		Thread.sleep(TestUtil.PAUSE_INTERVAL);
		
		frame0.pin();
		frame2.pin();
		
		frame0.unpin();
		
		List<BufferFrame> frames = Arrays.asList(frame0, frame1, frame2);
		assertEquals(this.strategy.findVictim(frames), frame1);
	}
	
	@Test
	public void testFirstFramePinnedIsSelectedWhenAllFramesCanBeReplaced() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		
		Thread.sleep(TestUtil.PAUSE_INTERVAL);
			
		frame0.pin();
		frame1.pin();
		frame2.pin();
		
		frame0.unpin();
		frame1.unpin();
		frame2.unpin();
		
		List<BufferFrame> frames = Arrays.asList(frame0, frame1, frame2);
		assertEquals(this.strategy.findVictim(frames), frame0);
	}
}
