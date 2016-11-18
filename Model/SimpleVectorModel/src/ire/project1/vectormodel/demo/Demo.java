package ire.project1.vectormodel.demo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import ire.project1.vectormodel.model.VectorModel;
import ire.project1.vectormodel.struct.DocumentTermMatrix;
import ire.project1.vectormodel.struct.SparseDoubleArray;

public class Demo {
	static String docsPath = "/Users/selab/git/Course_InformationRetrieval/ParsedData/Keywords_doc/";
	static String qrysPath = "/Users/selab/git/Course_InformationRetrieval/ParsedData/Keywords_qry/";
	static String resultPath = "/Users/selab/Desktop/result.csv";
	
	static DocumentTermMatrix documentTermMatrix;
	static VectorModel model;
	static ArrayList<String[]> queryList;
	static StringBuilder sb;
	
	public static void main(String[] args) throws IOException {
		/* create docmentTermMatrix */
		createDocTermMatrix();
		System.out.println("doc number: " + documentTermMatrix.getRowLength());
		System.out.println("term number: " + documentTermMatrix.getColLength());
		
		/* create model */
		model = new VectorModel(documentTermMatrix);
		model.tfidf();
		
		/* create queries */
		createQuery();
		
		/* sort query by id */
		queryList.sort(new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return Integer.compare(Integer.parseInt((String)o1[0]), Integer.parseInt((String)o2[0]));
			}
		});
		
		/* ranking and store result */
		ranking();
		
		/* output */
		BufferedWriter br = new BufferedWriter(new FileWriter(resultPath));
		br.write(sb.toString());
		System.out.println(sb.toString());
		br.close();
		
		
	}
	
	
	public static void createDocTermMatrix() throws IOException{
		documentTermMatrix = new DocumentTermMatrix();
		File docCollection = new File(docsPath);
		for (String fileName : docCollection.list()) {
			File doc = new File(docsPath + fileName);
			if (doc.isHidden())continue;
			
			BufferedReader br = new BufferedReader(new FileReader(doc));
			for(String line = br.readLine(); line != null; line = br.readLine()){
				if (line.length()>0) {
					documentTermMatrix.addElement(fileName, line);
				}
			}
			br.close();	
		}
	}
	
	public static void createQuery() throws IOException{
		queryList = new ArrayList<String[]>();
		
		File qryCollection = new File(qrysPath);
		for (String fileName : qryCollection.list()) {
			File qry = new File(qrysPath + fileName);
			if (qry.isHidden())continue;
			
			String id = fileName.split("\\.")[0];
			String keywords = "";
			
			BufferedReader br = new BufferedReader(new FileReader(qry));
			for(String line = br.readLine(); line != null; line = br.readLine()){
				keywords += line + " ";
			}
			queryList.add(new String[]{id, keywords});
		}
	}
	
	public static void ranking(){
		sb = new StringBuilder("Id,Rel_News\n");
		
		for (String q[] : queryList) {
			/* create queryVector */
			SparseDoubleArray queryVector = new SparseDoubleArray();
			String id = q[0];
			String keywords[] = q[1].split(" ");
			for (String keyword : keywords) {
				if (keyword.length()>0) {
					if (documentTermMatrix.getTerm(keyword) == null)continue;
					queryVector.add(documentTermMatrix.getTerm(keyword), 1);
				}
			}
			
			/* search model */
			System.out.println("query id: " + id);
			sb.append(id + ",");
			LinkedList<Object[]> result = model.Search(queryVector, 100);
			Iterator<Object[]> iterator = result.iterator();
			while(iterator.hasNext()){
				String docName = ((String)iterator.next()[0]).split("\\.")[0];
				sb.append(docName);
				
				/* csv padding */
				if (iterator.hasNext())sb.append(" ");
				else sb.append("\n");
			}
		}
	}

}
