package ire.project1.vectormodel.model;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;


import ire.project1.vectormodel.struct.DocumentTermMatrix;
import ire.project1.vectormodel.struct.SparseDoubleArray;

public class VectorModel {
	private DocumentTermMatrix documentTermMatrix;
	
	public VectorModel(DocumentTermMatrix documentTermMatrix) {
		this.documentTermMatrix = documentTermMatrix;
	}
	
	public void tfidf(){
		System.out.println("tf-idf");
		HashMap<String, Double> newValueSet = new HashMap<String, Double>();
		for (int i : documentTermMatrix.indexSet()) {
			SparseDoubleArray docVector = documentTermMatrix.getDocmentVector(i);
			for (int j : docVector.indexSet()) {
				System.out.println(i+","+j);
				SparseDoubleArray termVector = documentTermMatrix.getTermVector(j);
				double tf = 1 + Math.log(documentTermMatrix.getValue(i, j));
				double idf = Math.log(1+documentTermMatrix.getRowLength()/documentTermMatrix.getTermVector(j).non0());
				
				newValueSet.put(i+","+j, tf*idf);
			}
		}
		
		for (String string : newValueSet.keySet()) {
			int i = Integer.parseInt(string.split("\\,")[0]);
			int j = Integer.parseInt(string.split("\\,")[1]);
			double value = newValueSet.get(string);
			documentTermMatrix.setValue(i, j, value);
		}
	}
	
	/** @return Object[]{String docName, Double rankingScore}*/
	public LinkedList<Object[]> Search(SparseDoubleArray queryVector, int retrivalDocNumber){
		LinkedList<Object[]> mostRelevantDoc = new LinkedList<Object[]>();
		Comparator<Object[]> rankingComparator = new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				/* ranking by score */
				return -Double.compare((double)o1[1], (double)o2[1]);
			}
		};
		
		
		double minScore = Double.MAX_VALUE;
		for (int i = 0; i < documentTermMatrix.getRowLength(); i++) {
			SparseDoubleArray docVector = documentTermMatrix.getDocmentVector(i);
			String docName = documentTermMatrix.getDoc(i);
			double score = rankingScore(queryVector, docVector);
			Object result[] = {docName, score};
			
			if (mostRelevantDoc.size() < retrivalDocNumber ) {
				mostRelevantDoc.add(result);
				minScore = Math.min(minScore, score);
			}else if (score > minScore) {
				mostRelevantDoc.add(result);
				mostRelevantDoc.sort(rankingComparator);
				mostRelevantDoc.removeLast();
				minScore = (double)mostRelevantDoc.peekLast()[1];
			}
		}
		
		mostRelevantDoc.sort(rankingComparator);
		System.out.println("result length: " + mostRelevantDoc.size());
		return mostRelevantDoc;
	}
	
	private double rankingScore(SparseDoubleArray queryVector, SparseDoubleArray docVector){
		return cosine(queryVector, docVector);
	}
	
	private double cosine(SparseDoubleArray vector1, SparseDoubleArray vector2){
		return vector1.dot(vector2)/(vector1.norm()*vector2.norm());
	}
}
