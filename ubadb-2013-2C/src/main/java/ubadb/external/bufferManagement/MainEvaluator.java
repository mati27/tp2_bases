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
		String[] scenarios = {"escenarioA", "escenarioA1", "escenarioA2", "escenarioB", "escenarioB1", 
				"escenarioB2", "escenarioC", "escenarioC1", "escenarioC2", "escenarioD", "escenarioD1", 
				"escenarioD2"};
		int minBufferPoolSize = 50;
		int maxBufferPoolSize = 500;
		int bufferPoolSizeGap = 50;
		
		for (String scenario : scenarios)
		{
			String traceFileName = "generated/" + scenario  + "/complete.trace";
			
			System.out.println("===================================================");
			System.out.println(scenario);
			System.out.println("===================================================");
			
			System.out.println("----");
			System.out.println("FIFO");
			System.out.println("----");
			for(int bufferPoolSize=minBufferPoolSize; bufferPoolSize <= maxBufferPoolSize; bufferPoolSize += bufferPoolSizeGap) {
				System.out.println("Buffer Pool Size: " + bufferPoolSize);
				evaluateStrategy(bufferPoolSize, traceFileName, new FIFOReplacementStrategy());
				System.out.println("\n");
			}
			
			System.out.println("----");
			System.out.println("LRU");
			System.out.println("----");
			for(int bufferPoolSize=minBufferPoolSize; bufferPoolSize <= maxBufferPoolSize; bufferPoolSize += bufferPoolSizeGap) {
				System.out.println("Buffer Pool Size: " + bufferPoolSize);
				evaluateStrategy(bufferPoolSize, traceFileName, new LRUReplacementStrategy());
				System.out.println("\n");
			}
			
			System.out.println("----");
			System.out.println("MRU");
			System.out.println("----");
			for(int bufferPoolSize=minBufferPoolSize; bufferPoolSize <= maxBufferPoolSize; bufferPoolSize += bufferPoolSizeGap) {
				System.out.println("Buffer Pool Size: " + bufferPoolSize);
				evaluateStrategy(bufferPoolSize, traceFileName, new MRUReplacementStrategy());
				System.out.println("\n");
			}
		
			System.out.println("----");
			System.out.println("TC");
			System.out.println("----");
			for(int bufferPoolSize=minBufferPoolSize; bufferPoolSize <= maxBufferPoolSize; bufferPoolSize += bufferPoolSizeGap) {
				System.out.println("Buffer Pool Size: " + bufferPoolSize);
				evaluateStrategy(bufferPoolSize, traceFileName, new TCReplacementStrategy(bufferPoolSize, 50, 2, 1));
				System.out.println("\n");
			}
		}
	}
	
	private static void evaluateStrategy(int bufferPoolSize, String traceFileName, PageReplacementStrategy strategy) 
	{
		try
		{
			evaluate(strategy, traceFileName, bufferPoolSize);
		}
		catch(Exception e)
		{
			System.out.println("FATAL ERROR (" + e.getMessage() + ")");
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
