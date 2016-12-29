package ire.project1.vectormodel.lsa;

import java.util.Comparator;
import java.util.LinkedList;


public class LSAModel {
	private double[][] docTermMatrix;
	private int topicNumber = 0;
	public LSAModel(double[][] docTermMatrix,int topicNumber) {
		this.docTermMatrix = docTermMatrix;
		this.topicNumber = topicNumber;
	}
	
	public void training(){
		this.docTermMatrix = TFIDF.transform(docTermMatrix);
		this.docTermMatrix = SVD.transform(docTermMatrix, topicNumber);
		System.out.println("done training");
	}
	
	/** @return object[]{Integer docIdx, Double rankingScore} */
	public LinkedList<Object[]> Search(double[] queryVector, int relevantDocNum) {
		LinkedList<Object[]> mostRelevantDoc = new LinkedList<Object[]>();
		Comparator<Object[]> rankingComparator = new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				/* ranking by score */
				return -Double.compare((double)o1[1], (double)o2[1]);
			}
		};
		
		
		double minScore = Double.MAX_VALUE;
		for (int i = 0; i < this.docTermMatrix.length; i++) {
			double[] docVector = docTermMatrix[i];
			Integer docIdx = i;
			double score = rankingScore(queryVector, docVector);
			Object result[] = {docIdx, score};
			
			if (mostRelevantDoc.size() < relevantDocNum ) {
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
	
	private double rankingScore(double[] queryVector, double[] docVector){
		return cosine(queryVector, docVector);
	}
	
	private double cosine(double[] vector1, double[] vector2){
		double norm1 = 0.0;
		double norm2 = 0.0;
		double dotValue = 0.0;
		
		for (int i = 0; i < vector1.length; i++) {
			dotValue += vector1[i]*vector2[i];
			norm1 += vector1[i]*vector1[i];
			norm2 += vector2[i]*vector2[i];
		}
		norm1 = Math.sqrt(norm1);
		norm2 = Math.sqrt(norm2);
		
		return dotValue / (norm1 * norm2);
	}

}
