package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount;

import java.util.Collection;
import java.util.Date;

import javax.swing.tree.ExpandVetoException;

import ubadb.core.common.Page;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.exceptions.PageReplacementStrategyException;

public class TCReplacementStrategy implements PageReplacementStrategy
{
	private LRUChain lruChain; 
	
	public TCReplacementStrategy(int sizeLRUChain, int percentHotDefault, int hotCriteria, int coolCount)
	{
		initializeLRUChain(sizeLRUChain, percentHotDefault, hotCriteria, coolCount);
	}
	
	public BufferFrame findVictim(Collection<BufferFrame> bufferFrames) throws PageReplacementStrategyException
	{
		lruChain.update();
				
		boolean found = false;
		TCBufferFrame victim = null;
		
		for (TCBufferFrame possibleVictim : lruChain.coldRegion())
		{
			if(possibleVictim.canBeReplaced() && !found)
			{
				found = true;
				victim = possibleVictim;
			}
		}
		
		if(found) {
			removeFromColdRegion(victim);
			return victim;
		}
		
		throw new PageReplacementStrategyException("No page can be replaced");
	}

	private void removeFromColdRegion(TCBufferFrame possibleVictim)
	{
		/* TODO: cannot modify interface*/
		try {
			lruChain.removeFromColdRegion(possibleVictim);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BufferFrame createNewFrame(Page page)
	{
		TCBufferFrame newFrame = new TCBufferFrame(page);
		
		/* TODO: Cannot modify interface and addNewFrame may throw Exception */
		try {
			lruChain.addNewFrame(newFrame);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return newFrame;
	}
	
	private void initializeLRUChain(int sizeLRUChain, int percentHotDefault, int hotCriteria, int coolCount)
	{
		int sizeHotRegion = (int) Math.ceil(sizeLRUChain*percentHotDefault/100);
		int sizeColdRegion = sizeLRUChain - sizeHotRegion;
		
		lruChain = new LRUChain(sizeColdRegion, sizeHotRegion, hotCriteria, coolCount);
	}
}
