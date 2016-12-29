package ire.project1.vectormodel.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ire.project1.vectormodel.lsa.LSAModel;
import ire.project1.vectormodel.struct.BidirectionalIndexMap;

public class DemoLSA {
	private static final String DOCS_PATH = "/Users/selab/git/Course_InformationRetrieval/ParsedData2/document_collection_1029update.txt";
	private static final String QRYS_PATH = "/Users/selab/git/Course_InformationRetrieval/ParsedData2/5000relevantQry.txt/";
	private static final String RESULT_PATH = "/Users/selab/Desktop/result.csv";
	private static final int RELEVANT_DOC_NUM = 500;
	private static final int RESULT_DOC_NUM = 100;

	private static BidirectionalIndexMap docMap;
	private static BidirectionalIndexMap trmMap;

	private static double[][] documentTermMatrix;
	private static LSAModel model;
	private static ArrayList<String[]> queryList;
	private static StringBuilder csvResult;

	public static void main(String[] args) throws IOException {
		csvResult = new StringBuilder("Id,Rel_News\n");

		/* create queries */
		createQuery();
		
		for (String query[] : queryList) {//each query
			/* create doc and term map */
			creatMap(query);

			/* create docmentTermMatrix */
			createDocTermMatrix(query);
			System.out.println("doc number: " + docMap.size());
			System.out.println("term number: " + trmMap.size());

			/* create model */
			model = new LSAModel(documentTermMatrix, 100);
			model.training();

			/* ranking and store result */
			ranking(query);
		}
		
		/* output top relevant to csv */
		outputCSV();

	}

	private static void creatMap(String qry[]) throws IOException {
		docMap = new BidirectionalIndexMap();
		trmMap = new BidirectionalIndexMap();

		
		List<String> relevantDocs = Arrays.asList(qry[2].split(" "));
		BufferedReader br = new BufferedReader(new FileReader(new File(DOCS_PATH)));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			/* each doc */
			String doc[] = line.split("\t");
			String id = doc[0];
			String keywords[] = doc[1].split(" ");

			/* doc not in relevant list */
			if (!relevantDocs.contains(id))continue;
			
			/* add doc name and word to map */
			docMap.tryAppend(id);
			for (String word : keywords) {
				if (word.length() > 0) {
					trmMap.tryAppend(word);
				}
			}
		}
		br.close();
	}

	public static void createDocTermMatrix(String qry[]) throws IOException {
		documentTermMatrix = new double[docMap.size()][trmMap.size()];
		
		List<String> relevantDocs = Arrays.asList(qry[2].split(" "));
		BufferedReader br = new BufferedReader(new FileReader(new File(DOCS_PATH)));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			/* each doc */
			String doc[] = line.split("\t");
			String id = doc[0];
			String keywords[] = doc[1].split(" ");
			
			/* doc not in relevant list */
			if (!relevantDocs.contains(id))continue;
			
			/* add document vector to matrix */
			for (String word : keywords) {
				if (word.length() > 0) {
					documentTermMatrix[docMap.getIndex(id)][trmMap.getIndex(word)]++;
				}
			}
		}
		br.close();
	}

	public static void createQuery() throws IOException {
		queryList = new ArrayList<String[]>();
		BufferedReader br = new BufferedReader(new FileReader(new File(QRYS_PATH)));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			/* each query */
			String qry[] = line.split("\t");
			String num = qry[0];
			String keywords = qry[1];
			String relevantDocsArray[] = qry[2].split(" ");
			
			/* document filter */
			String relevantDocs = "";
			for (int i = 0; i < RELEVANT_DOC_NUM; i++) {
				relevantDocs += relevantDocsArray[i] + " ";
			}
			
			/* add to query list */
			queryList.add(new String[] { num, keywords, relevantDocs });
		}
		br.close();
	}

	public static void ranking(String qry[]) {
		/* create queryVector */
		double[] queryVector = new double[trmMap.size()];
		String num = qry[0];
		String keywords[] = qry[1].split(" ");
		for (String keyword : keywords) {
			if (keyword.length() > 0) {
				if (trmMap.getIndex(keyword) == null)
					continue;
				queryVector[trmMap.getIndex(keyword)]++;
			}
		}

		/* search model */
		System.out.println("search query num: " + num);
		csvResult.append(num + ",");
		LinkedList<Object[]> rankingList = model.Search(queryVector, RESULT_DOC_NUM);

		/* append ranking to result string */
		Iterator<Object[]> ranking = rankingList.iterator();
		while (ranking.hasNext()) {
			Integer docIdx = (Integer) ranking.next()[0];
			String docID = docMap.getString(docIdx);
			csvResult.append(docID);

			/* csv padding */
			if (ranking.hasNext())
				csvResult.append(" ");
			else
				csvResult.append("\n");
		}
	}

	public static void outputCSV() throws IOException {
		/* output result to csv file */
		BufferedWriter bw = new BufferedWriter(new FileWriter(RESULT_PATH));
		bw.write(csvResult.toString());
		bw.close();
	}

}
