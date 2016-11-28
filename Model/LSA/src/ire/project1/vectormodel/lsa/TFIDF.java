package ire.project1.vectormodel.lsa;

public class TFIDF {
	public static double[][] transform(double[][] docTermMatrix){
		/*
		 * tf = f / total f
		 * idf = log(N/ni)
		 */
		System.out.println("TF-IDF ...");
		int docNum = docTermMatrix.length;
		int trmNum = docTermMatrix[0].length;

		/* preprocessing */
		double[] totalWordInDoc = new double[docNum];
		double[] occurreOfWord = new double[trmNum]; 
		for (int i = 0; i < docNum; i++) {
			for (int j = 0; j < trmNum; j++) {
				if (docTermMatrix[i][j] > 0) {
					totalWordInDoc[i] += docTermMatrix[i][j];
					occurreOfWord[j] ++;	
				}
			}
		}
		
		/* TF-IDF */
		for (int i = 0; i < docNum; i++) {
			for (int j = 0; j < trmNum; j++) {
				if (docTermMatrix[i][j] > 0) {
					double tf = docTermMatrix[i][j] / totalWordInDoc[i];
					double idf = Math.log(docNum / occurreOfWord[j]);
					docTermMatrix[i][j] = tf*idf;
				}
			}
		}
		return docTermMatrix;
	}
}
