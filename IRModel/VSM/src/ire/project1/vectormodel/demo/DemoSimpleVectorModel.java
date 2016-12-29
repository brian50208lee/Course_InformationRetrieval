package ire.project1.vectormodel.demo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import ire.project1.vectormodel.model.VectorModel;
import ire.project1.vectormodel.struct.DocumentTermMatrix;
import ire.project1.vectormodel.struct.SparseDoubleArray;

public class DemoSimpleVectorModel {
	private static final String DOCS_PATH = "/Users/selab/git/Course_InformationRetrieval/ParsedData/document_collection_1029update.txt";
	private static final String QRYS_PATH = "/Users/selab/git/Course_InformationRetrieval/ParsedData/queries.txt/";
	private static final String RESULT_PATH = "/Users/selab/Desktop/result.csv";
	private static final int RELEVANT_DOC_NUM = 100;	
	
	private static DocumentTermMatrix documentTermMatrix;
	private static VectorModel model;
	private static ArrayList<String[]> queryList;
	private static StringBuilder csvResult;
	
	public static void main(String[] args) throws IOException {
		/* create docmentTermMatrix */
		createDocTermMatrix();
		System.out.println("doc number: " + documentTermMatrix.getRowLength());
		System.out.println("term number: " + documentTermMatrix.getColLength());
		
		/* create model */
		model = new VectorModel(documentTermMatrix);
		model.build();
		
		/* create queries */
		createQuery();
		
		/* ranking and store result */
		ranking();
		
		/* output top relevant to csv */
		BufferedWriter bw = new BufferedWriter(new FileWriter(RESULT_PATH));
		bw.write(csvResult.toString());
		bw.close();
		//System.out.println(csvResult.toString());
		
	}
	
	
	public static void createDocTermMatrix() throws IOException{
		documentTermMatrix = new DocumentTermMatrix();
		BufferedReader br = new BufferedReader(new FileReader(new File(DOCS_PATH)));
		for (String line=br.readLine(); line!=null; line=br.readLine()) {
			/* each doc */
			System.out.println("read document num: " + documentTermMatrix.getRowLength());
			String doc[] = line.split("\t");
			String id = doc[0];
			String keywords[] = doc[1].split(" ");
			
			for (String word : keywords) {
				if (word.length()>0) {
					documentTermMatrix.addElement(id, word);
				}
			}
		}
		br.close();
	}
	
	public static void createQuery() throws IOException{
		queryList = new ArrayList<String[]>();
		BufferedReader br = new BufferedReader(new FileReader(new File(QRYS_PATH)));
		for (String line=br.readLine(); line!=null; line=br.readLine()) {
			/* each query */
			String qry[] = line.split("\t");
			String num = qry[0];
			String keywords = qry[1];
			
			queryList.add(new String[]{num, keywords});
		}
		br.close();
	}
	
	public static void ranking(){
		csvResult = new StringBuilder("Id,Rel_News\n");
		for (String qry[] : queryList) {
			/* create queryVector */
			SparseDoubleArray queryVector = new SparseDoubleArray();
			String num = qry[0];
			String keywords[] = qry[1].split(" ");
			for (String keyword : keywords) {
				if (keyword.length()>0) {
					if (documentTermMatrix.getTerm(keyword) == null)continue;
					queryVector.add(documentTermMatrix.getTerm(keyword), 1);
				}
			}
			
			/* search model */
			System.out.println("search query num: " + num);
			csvResult.append(num + ",");
			LinkedList<Object[]> rankingList = model.Search(queryVector, RELEVANT_DOC_NUM);
			
			/* append ranking to result string */
			Iterator<Object[]> ranking = rankingList.iterator();
			while(ranking.hasNext()){
				String docID = ((String)ranking.next()[0]).split("\\.")[0];
				csvResult.append(docID);
				
				/* csv padding */
				if (ranking.hasNext())csvResult.append(" ");
				else csvResult.append("\n");
			}
		}
	}

}
