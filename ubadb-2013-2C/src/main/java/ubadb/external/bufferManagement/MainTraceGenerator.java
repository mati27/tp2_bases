package ubadb.external.bufferManagement;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ubadb.core.common.TransactionId;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceTraceSerializer;
import ubadb.external.bufferManagement.traceGenerators.BNLJTraceGenerator;
import ubadb.external.bufferManagement.traceGenerators.FileScanTraceGenerator;
import ubadb.external.bufferManagement.traceGenerators.IndexScanTraceGenerator;
import ubadb.external.bufferManagement.traceGenerators.MixedTraceGenerator;


public class MainTraceGenerator
{
	public static void main(String[] args) throws Exception
	{
		createTP2Traces();
	}
	
	private static void basicDataSet() throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		
		//File Scan
		String fileNameA1 = "generated/fileScan-Company.trace";
		PageReferenceTrace traceA1 = new FileScanTraceGenerator().generateFileScan(1, "Company", 10);
		serialize(fileNameA1, traceA1, serializer);
		
		String fileNameA2 = "generated/fileScan-Product.trace";
		PageReferenceTrace traceA2 = new FileScanTraceGenerator().generateFileScan(1, "Product", 100);
		serialize(fileNameA2, traceA2, serializer);
		
		String fileNameA3 = "generated/fileScan-Sale.trace";
		PageReferenceTrace traceA3 = new FileScanTraceGenerator().generateFileScan(1,"Sale", 1000);
		serialize(fileNameA3, traceA3, serializer);
		
		//Index Scan Clustered
		String fileNameB1 = "generated/indexScanClustered-Product.trace";
		PageReferenceTrace traceB1 = new IndexScanTraceGenerator().generateIndexScanClustered(1,"Product", 3, 10, 50);
		serialize(fileNameB1, traceB1, serializer);
		
		String fileNameB2 = "generated/indexScanClustered-Sale.trace";
		PageReferenceTrace traceB2 = new IndexScanTraceGenerator().generateIndexScanClustered(1,"Sale", 4, 200, 100);
		serialize(fileNameB2, traceB2, serializer);
		
		//Index Scan Unclustered
		String fileNameC1 = "generated/indexScanUnclustered-Product.trace";
		PageReferenceTrace traceC1 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Product", 3, 40, 100);
		serialize(fileNameC1, traceC1, serializer);
		
		String fileNameC2 = "generated/indexScanUnclustered-Sale.trace";
		PageReferenceTrace traceC2 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Sale", 4, 250, 1000);
		serialize(fileNameC2, traceC2, serializer);
		
		//BNLJ
		String fileNameD1 = "generated/BNLJ-ProductXSale-group_50.trace";
		PageReferenceTrace traceD1 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 100, "Sale", 1000, 50);
		serialize(fileNameD1, traceD1, serializer);
		
		String fileNameD2 = "generated/BNLJ-ProductXSale-group_75.trace";
		PageReferenceTrace traceD2 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 100, "Sale", 1000, 75);
		serialize(fileNameD2, traceD2, serializer);
		
		String fileNameD3 = "generated/BNLJ-ProductXSale-group_100.trace";
		PageReferenceTrace traceD3 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 100, "Sale", 1000, 100);
		serialize(fileNameD3, traceD3, serializer);
		
		String fileNameD4 = "generated/BNLJ-SaleXProduct-group_100.trace";
		PageReferenceTrace traceD4 = new BNLJTraceGenerator().generateBNLJ(1,"Sale", 1000, "Product", 100, 100);
		serialize(fileNameD4, traceD4, serializer);

		String fileNameD5 = "generated/BNLJ-SaleXProduct-group_250.trace";
		PageReferenceTrace traceD5 = new BNLJTraceGenerator().generateBNLJ(1,"Sale", 1000, "Product", 100, 250);
		serialize(fileNameD5, traceD5, serializer);
	}
	
	private static void serialize(String fileName, PageReferenceTrace trace, PageReferenceTraceSerializer serializer) throws Exception
	{
		serializer.write(trace, fileName);
		System.out.println("File '" + fileName + "' generated!!");
	}
	
	private static void complexDataSet() throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		String folderA = "generated/escenario3/originalesA";
		String fileA = "generated/mixedA_tot4_conc2.trace";
		mixTraces(fileA,folderA,4,2,serializer);

		String folderB = "generated/escenario3/originalesB";
		String fileB = "generated/mixedB_tot10_conc2.trace";
		mixTraces(fileB,folderB,10,2,serializer);

		String folderC = "generated/escenario3/originalesC";
		String fileC = "generated/mixedC_tot50_conc2.trace";
		mixTraces(fileC,folderC,50,2,serializer);

		String folderD = "generated/escenario3/originalesD";
		String fileD = "generated/mixedD_tot50_conc5.trace";
		mixTraces(fileD,folderD,50,5,serializer);

		String folderE = "generated/escenario3/originalesE";
		String fileE = "generated/mixedE_tot100_conc5.trace";
		mixTraces(fileE,folderE,100,5,serializer);
	}
	
	private static void createTP2Traces() throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		
		escenarioA(serializer);
		escenarioA1(serializer);
		escenarioA2(serializer);
		
		escenarioB(serializer);
		escenarioB1(serializer);
		escenarioB2(serializer);
		
		escenarioC(serializer);
		escenarioC1(serializer);
		escenarioC2(serializer);
		
		escenarioD(serializer);
		escenarioD1(serializer);
		escenarioD2(serializer);
	}

	private static void escenarioA(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceA1 = "generated/escenarioA/traces/BNLJ-ProductxProduct-10.trace";
		PageReferenceTrace traceA1 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 100, "Product", 100, 10);
		serialize(filenameTraceA1, traceA1, serializer);
		
		mixTraces("generated/escenarioA/complete.trace", "generated/escenarioA/traces", 1, 1, serializer);
	}
	
	private static void escenarioA1(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceA11 = "generated/escenarioA1/traces/BNLJ-ProductxProduct-10.trace";
		PageReferenceTrace traceA11 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 500, "Product", 500, 10);
		serialize(filenameTraceA11, traceA11, serializer);
		
		mixTraces("generated/escenarioA1/complete.trace", "generated/escenarioA1/traces", 1, 1, serializer);
	}
	
	private static void escenarioA2(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceA21 = "generated/escenarioA2/traces/BNLJ-ProductxProduct-10.trace";
		PageReferenceTrace traceA21 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 1000, "Product", 1000, 10);
		serialize(filenameTraceA21, traceA21, serializer);
		
		mixTraces("generated/escenarioA2/complete.trace", "generated/escenarioA2/traces", 1, 1, serializer);
	}
	
	private static void escenarioB(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceB1 = "generated/escenarioB/traces/fileScan-Product.trace";
		PageReferenceTrace traceB1 = new FileScanTraceGenerator().generateFileScan(1, "Product", 100);
		serialize(filenameTraceB1, traceB1, serializer);
		
		String filenameTraceB2 = "generated/escenarioB/traces/BNLJ-ProductxSeller-20.trace";
		PageReferenceTrace traceB2 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 100, "Seller", 100, 20);
		serialize(filenameTraceB2, traceB2, serializer);
		
		mixTraces("generated/escenarioB/complete.trace", "generated/escenarioB/traces", 2, 2, serializer);
	}
	
	private static void escenarioB1(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceB11 = "generated/escenarioB1/traces/fileScan-Product.trace";
		PageReferenceTrace traceB11 = new FileScanTraceGenerator().generateFileScan(1, "Product", 500);
		serialize(filenameTraceB11, traceB11, serializer);
		
		String filenameTraceB12 = "generated/escenarioB1/traces/BNLJ-ProductxSeller-20.trace";
		PageReferenceTrace traceB12 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 500, "Seller", 250, 20);
		serialize(filenameTraceB12, traceB12, serializer);
		
		mixTraces("generated/escenarioB1/complete.trace", "generated/escenarioB1/traces", 2, 2, serializer);
	}
	
	private static void escenarioB2(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceB21 = "generated/escenarioB2/traces/fileScan-Product.trace";
		PageReferenceTrace traceB21 = new FileScanTraceGenerator().generateFileScan(1, "Product", 1000);
		serialize(filenameTraceB21, traceB21, serializer);
		
		String filenameTraceB22 = "generated/escenarioB2/traces/BNLJ-ProductxSeller-20.trace";
		PageReferenceTrace traceB22 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 1000, "Seller", 500, 50);
		serialize(filenameTraceB22, traceB22, serializer);
		
		mixTraces("generated/escenarioB2/complete.trace", "generated/escenarioB2/traces", 2, 2, serializer);
	}
	
	private static void escenarioC(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceC1 = "generated/escenarioC/traces/fileScan-Product.trace";
		PageReferenceTrace traceC1 = new FileScanTraceGenerator().generateFileScan(1, "Product", 500);
		serialize(filenameTraceC1, traceC1, serializer);
		
		String filenameTraceC2 = "generated/escenarioC/traces/indexScanClustered-Product.trace";
		PageReferenceTrace traceC2 = new IndexScanTraceGenerator().generateIndexScanClustered(1,"Product", 4, 100, 200);
		serialize(filenameTraceC2, traceC2, serializer);
		
		mixTraces("generated/escenarioC/complete.trace", "generated/escenarioC/traces", 2, 2, serializer);
	}
	
	private static void escenarioC1(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceC11 = "generated/escenarioC1/traces/fileScan-Product.trace";
		PageReferenceTrace traceC11 = new FileScanTraceGenerator().generateFileScan(1, "Product", 1000);
		serialize(filenameTraceC11, traceC11, serializer);
		
		String filenameTraceC12 = "generated/escenarioC1/traces/indexScanClustered-Product.trace";
		PageReferenceTrace traceC12 = new IndexScanTraceGenerator().generateIndexScanClustered(1,"Product", 4, 100, 400);
		serialize(filenameTraceC12, traceC12, serializer);
		
		mixTraces("generated/escenarioC1/complete.trace", "generated/escenarioC1/traces", 2, 2, serializer);
	}
	
	private static void escenarioC2(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceC21 = "generated/escenarioC2/traces/fileScan-Product.trace";
		PageReferenceTrace traceC21 = new FileScanTraceGenerator().generateFileScan(1, "Product", 2000);
		serialize(filenameTraceC21, traceC21, serializer);
		
		String filenameTraceC22 = "generated/escenarioC2/traces/indexScanClustered-Product.trace";
		PageReferenceTrace traceC22 = new IndexScanTraceGenerator().generateIndexScanClustered(1,"Product", 4, 200, 800);
		serialize(filenameTraceC22, traceC22, serializer);
		
		mixTraces("generated/escenarioC2/complete.trace", "generated/escenarioC2/traces", 2, 2, serializer);
	}
	
	private static void escenarioD(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceD1 = "generated/escenarioD/traces/BNLJ-ProductxSale-20.trace";
		PageReferenceTrace traceD1 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 100, "Sale", 80, 20);
		serialize(filenameTraceD1, traceD1, serializer);
		
		String filenameTraceD2 = "generated/escenarioD/traces/BNLJ-SalexProduct-20.trace";
		PageReferenceTrace traceD2 = new BNLJTraceGenerator().generateBNLJ(1,"Sale", 80, "Product", 100, 20);
		serialize(filenameTraceD2, traceD2, serializer);
		
		mixTraces("generated/escenarioD/complete.trace", "generated/escenarioD/traces", 2, 2, serializer);
	}
	
	private static void escenarioD1(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceD11 = "generated/escenarioD1/traces/BNLJ-ProductxSale-50.trace";
		PageReferenceTrace traceD11 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 500, "Sale", 300, 50);
		serialize(filenameTraceD11, traceD11, serializer);
		
		String filenameTraceD12 = "generated/escenarioD1/traces/BNLJ-SalexProduct-50.trace";
		PageReferenceTrace traceD12 = new BNLJTraceGenerator().generateBNLJ(1,"Sale", 300, "Product", 500, 50);
		serialize(filenameTraceD12, traceD12, serializer);
		
		mixTraces("generated/escenarioD1/complete.trace", "generated/escenarioD1/traces", 2, 2, serializer);
	}
	
	private static void escenarioD2(PageReferenceTraceSerializer serializer) throws Exception {
		String filenameTraceD21 = "generated/escenarioD2/traces/BNLJ-ProductxSale-75.trace";
		PageReferenceTrace traceD21 = new BNLJTraceGenerator().generateBNLJ(1,"Product", 1000, "Sale", 750, 75);
		serialize(filenameTraceD21, traceD21, serializer);
		
		String filenameTraceD22 = "generated/escenarioD2/traces/BNLJ-SalexProduct-75.trace";
		PageReferenceTrace traceD22 = new BNLJTraceGenerator().generateBNLJ(1,"Sale", 750, "Product", 1000, 75);
		serialize(filenameTraceD22, traceD22, serializer);
		
		mixTraces("generated/escenarioD2/complete.trace", "generated/escenarioD2/traces", 2, 2, serializer);
	}
	
	private static void mixTraces(String fileNameForNewTrace, String folderName, int totalTracesCount, int maxConcurrentTracesCount, PageReferenceTraceSerializer serializer) throws Exception
	{
		List<PageReferenceTrace> tracesToMix = buildTracesToMix(folderName,totalTracesCount,serializer);
		
		PageReferenceTrace mixedTrace = new MixedTraceGenerator().generateMixedTrace(tracesToMix, totalTracesCount, maxConcurrentTracesCount);
		
		serializer.write(mixedTrace, fileNameForNewTrace);
		System.out.println("File " + fileNameForNewTrace + " generated");
	}

	private static List<PageReferenceTrace> buildTracesToMix(String folderName, int totalTracesCount, PageReferenceTraceSerializer serializer) throws Exception
	{
		List<PageReferenceTrace> tracesToMix = new ArrayList<>();
		Random random = new Random(System.currentTimeMillis());
		
		String[] traceFiles = Paths.get(folderName).toFile().list();
		
		for(int i=0; i < totalTracesCount; i++)
		{
			int anyTraceIndex = random.nextInt(traceFiles.length); 
			PageReferenceTrace anyTrace = serializer.read(folderName + "/" + traceFiles[anyTraceIndex]);
			anyTrace.changeTransactionId(new TransactionId(i));
			
			tracesToMix.add(anyTrace);
		}
		
		return tracesToMix;
	}
}
