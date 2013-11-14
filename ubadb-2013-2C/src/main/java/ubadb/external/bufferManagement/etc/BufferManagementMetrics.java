package ubadb.external.bufferManagement.etc;

import java.util.HashMap;
import java.util.HashSet;

import ubadb.core.common.PageId;


public class BufferManagementMetrics
{
	private PageReferenceTrace trace;
	private int faultsCount;
	
	public BufferManagementMetrics(PageReferenceTrace trace, int faultsCount)
	{
		this.trace = trace;
		this.faultsCount = faultsCount;
	}

	private int countRequests()
	{
		int ret = 0;
		for(PageReference reference : trace.getPageReferences())
		{
			if(PageReferenceType.REQUEST.equals(reference.getType()))
				ret++;
		}
		
		return ret;
	}
	
	private int countFirstTimeRequests()
	{
		HashSet<PageId> pageIdsRequested = new HashSet<PageId>();
		
		for(PageReference reference : trace.getPageReferences())
		{
			if(PageReferenceType.REQUEST.equals(reference.getType()))
				pageIdsRequested.add(reference.getPageId());
		}
		
		return pageIdsRequested.size();
	}
	
	private double calculateHitRate()
	{
		return (double)(countRequests() - faultsCount)/(double)countRequests();
	}
	
	private double calculateHitRateWithMemory()
	{
		return (double)(countRequests() - faultsCount)/(double)(countRequests() - countFirstTimeRequests());
	}

	public void showSummary()
	{
		System.out.println("Hit rate: " + calculateHitRate());
		System.out.println("Hit rate: " + calculateHitRateWithMemory());
	}
}
