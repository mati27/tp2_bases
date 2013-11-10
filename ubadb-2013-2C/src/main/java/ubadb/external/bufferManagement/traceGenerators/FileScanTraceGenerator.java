package ubadb.external.bufferManagement.traceGenerators;

import java.util.List;

import ubadb.core.common.PageId;
import ubadb.core.common.TransactionId;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;

public class FileScanTraceGenerator extends PageReferenceTraceGenerator
{
	public PageReferenceTrace generateFileScan(long transactionNumber, String fileName, int pageCount)
	{
		List<PageId> pages = generateSequentialPages(fileName, 0, pageCount);
		
		return buildRequestAndRelease(new TransactionId(transactionNumber), pages);
	}
}
