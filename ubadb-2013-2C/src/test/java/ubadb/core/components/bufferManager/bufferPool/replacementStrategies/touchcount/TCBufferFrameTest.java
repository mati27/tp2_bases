package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ubadb.core.testDoubles.DummyObjectFactory;

public class TCBufferFrameTest
{
	@Test
	public void testFrameIsTouchedOnPin() throws Exception
	{
		TCBufferFrame frame = new TCBufferFrame(DummyObjectFactory.PAGE);
		
		frame.pin();
		
		assertEquals(1, frame.touchCount());
	}
}
