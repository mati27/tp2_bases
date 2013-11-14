package ubadb.external.bufferManagement;

import ubadb.core.components.bufferManager.BufferManager;
import ubadb.core.components.bufferManager.BufferManagerImpl;
import ubadb.core.components.bufferManager.bufferPool.BufferPool;
import ubadb.core.components.bufferManager.bufferPool.pools.single.SingleBufferPool;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.fifo.FIFOReplacementStrategy;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.lru.LRUReplacementStrategy;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.mru.MRUReplacementStrategy;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.touchcount.TCReplacementStrategy;
import ubadb.core.components.catalogManager.CatalogManager;
import ubadb.core.components.catalogManager.CatalogManagerImpl;
import ubadb.core.components.diskManager.DiskManager;
import ubadb.core.exceptions.BufferManagerException;
import ubadb.external.bufferManagement.etc.BufferManagementMetrics;
import ubadb.external.bufferManagement.etc.FaultCounterDiskManagerSpy;
import ubadb.external.bufferManagement.etc.PageReference;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceTraceSerializer;

public class MainEvaluator
{
	private static final int PAUSE_BETWEEN_REFERENCES	= 0;
	
	public static void main(String[] args)
	{
		String[] scenarios = {"escenarioA", "escenarioB"};
		
		for (String scenario : scenarios)
		{
			int bufferPoolSize = 100;
			String traceFileName = "generated/" + scenario  + "/complete.trace";
			
			System.out.println("===================================================");
			System.out.println(scenario);
			System.out.println("===================================================");
			
			System.out.println("FIFO");
			evaluateFIFO(bufferPoolSize, traceFileName);
			
			System.out.println("LRU");
			evaluateLRU(bufferPoolSize, traceFileName);
			
			System.out.println("MRU");
			evaluateMRU(bufferPoolSize, traceFileName);
			
			System.out.println("TC");
			evaluateTC(bufferPoolSize, traceFileName);
			System.out.println("\n");
		}
	}

	private static void evaluateFIFO(int bufferPoolSize, String traceFileName) {
		PageReplacementStrategy pageReplacementStrategy = new FIFOReplacementStrategy();
		
		try
		{
			evaluate(pageReplacementStrategy, traceFileName, bufferPoolSize);
		}
		catch(Exception e)
		{
			System.out.println("FATAL ERROR (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}
	
	private static void evaluateLRU(int bufferPoolSize, String traceFileName) {
		PageReplacementStrategy pageReplacementStrategy = new LRUReplacementStrategy();
		
		try
		{
			evaluate(pageReplacementStrategy, traceFileName, bufferPoolSize);
		}
		catch(Exception e)
		{
			System.out.println("FATAL ERROR (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}
	
	private static void evaluateMRU(int bufferPoolSize, String traceFileName) {
		PageReplacementStrategy pageReplacementStrategy = new MRUReplacementStrategy();
		
		try
		{
			evaluate(pageReplacementStrategy, traceFileName, bufferPoolSize);
		}
		catch(Exception e)
		{
			System.out.println("FATAL ERROR (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}
	
	private static void evaluateTC(int bufferPoolSize, String traceFileName) {
		PageReplacementStrategy pageReplacementStrategy = new TCReplacementStrategy(bufferPoolSize, 50, 2, 1);
		
		try
		{
			evaluate(pageReplacementStrategy, traceFileName, bufferPoolSize);
		}
		catch(Exception e)
		{
			System.out.println("FATAL ERROR (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}

	private static void evaluate(PageReplacementStrategy pageReplacementStrategy, String traceFileName, int bufferPoolSize) throws Exception, InterruptedException, BufferManagerException
	{
		FaultCounterDiskManagerSpy faultCounterDiskManagerSpy = new FaultCounterDiskManagerSpy();
		CatalogManager catalogManager = new CatalogManagerImpl();
		BufferManager bufferManager = createBufferManager(faultCounterDiskManagerSpy, catalogManager, pageReplacementStrategy, bufferPoolSize);
		PageReferenceTrace trace = getTrace(traceFileName);
		
		for(PageReference pageReference : trace.getPageReferences())
		{
			//Pause references to have different dates in LRU and MRU
			Thread.sleep(PAUSE_BETWEEN_REFERENCES);
			
			switch(pageReference.getType())
			{
				case REQUEST:
				{
					try
					{
						bufferManager.readPage(pageReference.getPageId());
					}
					catch(BufferManagerException e)
					{
						System.out.println("NO MORE SPACE AVAILABLE, MEMORY FULL");
						throw e;
					}
					break;
				}
				case RELEASE:
				{
					bufferManager.releasePage(pageReference.getPageId());
					break;
				}
			}
		}
		
		BufferManagementMetrics metrics = new BufferManagementMetrics(trace, faultCounterDiskManagerSpy.getFaultsCount());
		metrics.showSummary();
	}

	private static PageReferenceTrace getTrace(String traceFileName) throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		return serializer.read(traceFileName);
	}

	private static BufferManager createBufferManager(DiskManager diskManager, CatalogManager catalogManager, PageReplacementStrategy pageReplacementStrategy, int bufferPoolSize)
	{
		BufferPool singleBufferPool = new SingleBufferPool(bufferPoolSize, pageReplacementStrategy);
		BufferManager bufferManager = new BufferManagerImpl(diskManager, catalogManager, singleBufferPool);
		
		return bufferManager;
	}
}
