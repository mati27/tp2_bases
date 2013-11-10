package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import java.util.Collection;
import java.util.Date;

import ubadb.core.common.Page;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.exceptions.PageReplacementStrategyException;

public class TCReplacementStrategy implements PageReplacementStrategy
{
	public BufferFrame findVictim(Collection<BufferFrame> bufferFrames) throws PageReplacementStrategyException
	{
		TCBufferFrame victim = null;
		Date oldestReplaceablePageDate = null;
		
		for(BufferFrame bufferFrame : bufferFrames)
		{
			TCBufferFrame fifoBufferFrame = (TCBufferFrame) bufferFrame; //safe cast as we know all frames are of this type
			if(fifoBufferFrame.canBeReplaced() && (oldestReplaceablePageDate==null || fifoBufferFrame.getCreationDate().before(oldestReplaceablePageDate)))
			{
				victim = fifoBufferFrame;
				oldestReplaceablePageDate = fifoBufferFrame.getCreationDate();
			}
		}
		
		if(victim == null)
			throw new PageReplacementStrategyException("No page can be removed from pool");
		else
			return victim;
	}

	public BufferFrame createNewFrame(Page page)
	{
		return new TCBufferFrame(page);
	}
}
