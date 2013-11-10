package ubadb.core.components.bufferManager.bufferPool.replacementStrategies;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.RUBufferFrame;
import ubadb.core.testDoubles.DummyObjectFactory;
import ubadb.core.util.TestUtil;

public class RUBufferFrameTest {
	@Test
	public void testFrameIsUsedOnPin() throws Exception
	{
		RUBufferFrame frame = new RUBufferFrame(DummyObjectFactory.PAGE);
		
		Date lastTimeUsedBeforePin = frame.lastTimeUsed();
				
		Thread.sleep(TestUtil.PAUSE_INTERVAL); //Sleep to guarantee that the second frame is created some time after the first one

		frame.pin();
		
		Date lastTimeUsedAfterPin = frame.lastTimeUsed();

		assertTrue(lastTimeUsedBeforePin.before(lastTimeUsedAfterPin));
	}
}
