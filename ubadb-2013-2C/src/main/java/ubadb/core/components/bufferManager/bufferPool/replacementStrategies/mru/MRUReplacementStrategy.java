package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.mru;

import java.util.Collection;
import java.util.Date;

import ubadb.core.common.Page;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.exceptions.PageReplacementStrategyException;

public class MRUReplacementStrategy implements PageReplacementStrategy {

	@Override
	public BufferFrame findVictim(Collection<BufferFrame> bufferFrames)
			throws PageReplacementStrategyException {
		RUBufferFrame victim = null;
		Date mostRecentlyTimeUsed = null;
		
		for(BufferFrame bufferFrame : bufferFrames)
		{
			RUBufferFrame mruBufferFrame = (RUBufferFrame) bufferFrame;
			if(mruBufferFrame.canBeReplaced() && (mostRecentlyTimeUsed==null || mruBufferFrame.lastTimeUsed().after(mostRecentlyTimeUsed)))
			{
				victim = mruBufferFrame;
				mostRecentlyTimeUsed = mruBufferFrame.lastTimeUsed();
			}
		}
		
		if(victim == null)
			throw new PageReplacementStrategyException("No page can be removed from pool");
		else
			return victim;
	}

	@Override
	public BufferFrame createNewFrame(Page page) {
		return new RUBufferFrame(page);
	}

}
