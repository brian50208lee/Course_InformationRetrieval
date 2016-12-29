package ire.project1.topicmodel.plsa;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;


public class PLSAModel {

	/** number of documents */
	private int docNumber;
	
	/** number of terms */
	private int termNumber;

	/** topic number in latent space */
	private int topicNumber;
	
	/** iteration numert when training model */
	private int iterations;
	
	
	
	/** document-term matrix */ 
	private double[][] docTermMatrix;

	/** d*k P(z|d) probibility of topic given document */
	private double[][] pDocTop;
	
	/** k*t P(t|z) probibility of term given topic */
	private double[][] pTopTerm;
	
	/** d*t*t P(z|d,t) probibility of topic given document,term */
	private double[][][] pDocTermTopic;
	
	
	/**
	 * @param docTermMatrix document-term matrix
	 * @param topicNumber topic number in latent space
	 * @param iterations iteration numert when training model
	 */
	public PLSAModel(double[][] docTermMatrix,int topicNumber, int iterations) {
		/* init user parameter */
		this.topicNumber = topicNumber;
		this.iterations = iterations;
		this.docTermMatrix = docTermMatrix;
		
		/* init model variable */
		System.out.println("initial model");
		this.docNumber = docTermMatrix.length;
		this.termNumber = docTermMatrix[0].length;
		this.pDocTop = new double[docNumber][topicNumber];
		this.pTopTerm = new double[topicNumber][termNumber];
		this.pDocTermTopic = new double[docNumber][termNumber][topicNumber];
	}
	
	/** training model by EM alorithm */
	public void training(){
		initDistribution();
		for(int i = 1; i <= iterations; i++){
			EM();
			System.out.printf("iter:%d\n", i);
			//System.out.printf("likelihood:%.5f\n", logLikelihood());
		}
		System.out.println("done training");
	}
	
	/** inital P(z|d), P(t|z) by random probibility */
	private void initDistribution(){
		System.out.println("initial distribution");
		Random random = new Random();
		
		/* init document-topic distribution and nomalize to one */
		for (int d = 0; d < pDocTop.length; d++) {
			double normalizeSum = 0.0;
			/* random init value */
			for (int z = 0; z < pDocTop[d].length; z++) {
				pDocTop[d][z] = random.nextDouble();
				normalizeSum += pDocTop[d][z];
			}
			/* normalize P(z|d) to one */
			for (int z = 0; z < pDocTop[d].length; z++) {
				pDocTop[d][z] /= normalizeSum;
			}
		}
		
		/* init topic-term distribution and nomalize to one */
		for (int z = 0; z < pTopTerm.length; z++) {
			double normalizeSum = 0.0;
			/* random init value */
			for (int t = 0; t < pTopTerm[z].length; t++) {
				pTopTerm[z][t] = random.nextDouble();
				normalizeSum += pTopTerm[z][t];
			}
			/* normalize P(t|z) to one */
			for (int t = 0; t < pTopTerm[z].length; t++) {
				pTopTerm[z][t] /= normalizeSum;
			}
		}
	}
	
	/** EM algorithm */
	private void EM(){
		/* E step calculate P(z|d,w) */
		for (int d = 0; d < docNumber; d++) {//each document 
			for (int t = 0; t < termNumber; t++) {//each term
				double normalizeSum = 0.0;
				/* P(z|d,w) = P(t|z)*P(z|d) */
				for (int z = 0; z < topicNumber; z++) {//each topic
					pDocTermTopic[d][t][z] = pDocTop[d][z]*pTopTerm[z][t];
					normalizeSum += pDocTermTopic[d][t][z];
				}
				/* normalize P(z|d,w) to one */
				for (int z = 0; z < topicNumber; z++) {//each topic
					/* avoid zero */
					if(normalizeSum == 0.0)normalizeSum = Double.MIN_NORMAL;
					pDocTermTopic[d][t][z] /= normalizeSum; 
				}
			}
		}
		
		
		/* M step (1) update P(z|d) */
		for (int d = 0; d < docNumber; d++) {//each document
			double normalizeSum = 0.0;
			for (int z = 0; z < topicNumber; z++) {//each topic
				double newProbability = 0.0;
				for (int t = 0; t < termNumber; t++) {//each term
					newProbability += docTermMatrix[d][t] * pDocTermTopic[d][t][z];
				}
				pDocTop[d][z] = newProbability;
				normalizeSum += newProbability;
			}
			/* normalize P(z|d) to one */
			for (int z = 0; z < topicNumber; z++) {//each topic
				if(normalizeSum == 0.0)normalizeSum = Double.MIN_NORMAL;
				pDocTop[d][z] /= normalizeSum;
			}
		}
		
		
		
		/* M step (2) update P(t|z) */
		for (int z = 0; z < topicNumber; z++) {//each topic
			double normalizeSum = 0.0;
			for (int t = 0; t < termNumber; t++) {//each term
				double newProbability = 0.0;
				for (int d = 0; d < docNumber; d++) {//each document
					newProbability += docTermMatrix[d][t] * pDocTermTopic[d][t][z];
				}
				pTopTerm[z][t] = newProbability;
				normalizeSum += newProbability;
			}
			/* normalize P(t|z) to one */
			for (int t = 0; t < termNumber; t++) {//each term
				if(normalizeSum == 0.0)normalizeSum = Double.MIN_NORMAL;
				pTopTerm[z][t] /= normalizeSum;
			}
		}
	}
	
	/** L = Sum_d{Sum_t{ n(d,t) * logP(d,t) }} */
	private double logLikelihood(){
		double likelihoodValue = 0.0;
		for (int d = 0; d < docNumber; d++) {
			for (int t = 0; t < termNumber; t++) {
				double pDocTerm = 0.0;
				for (int z = 0; z < topicNumber; z++) {
					pDocTerm += pDocTop[d][z] * pTopTerm[z][t];
				}
				likelihoodValue += docTermMatrix[d][t] * Math.log(pDocTerm);
			}
		}
		return likelihoodValue;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param queryVector query array
	 * @param relevantDocNum how many number of document you want to retrieve
	 * @return LinkedList<Object[]> : Object[]{Integer docIdx, Double rankingScore}
	 */
	public LinkedList<Object[]> Search(double[] queryVector, int relevantDocNum) {
		/* construct model document-term matrix */
		double modelDocTermMatrix[][] = new double[docNumber][termNumber];
		for (int d = 0; d < docNumber; d++) {
			for (int t = 0; t < termNumber; t++) {
				for (int z = 0; z < topicNumber; z++) {
					modelDocTermMatrix[d][t] += pDocTop[d][z] * pTopTerm[z][t];
				}
			}
		}
		
		/* init result document list and sorting comparator */
		LinkedList<Object[]> mostRelevantDoc = new LinkedList<Object[]>();
		Comparator<Object[]> rankingComparator = new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				/* sorting by double score value in Object[1]*/
				return -Double.compare((double)o1[1], (double)o2[1]);
			}
		};
		
		/* for each doument get the double ranking score with query */
		double minScore = Double.MAX_VALUE;
		for (int i = 0; i < this.docTermMatrix.length; i++) {//each doc
			double[] docVector = modelDocTermMatrix[i];
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
		
		/* sort list before list return */
		mostRelevantDoc.sort(rankingComparator);
		System.out.println("result length: " + mostRelevantDoc.size());
		return mostRelevantDoc;
	}



	/** @return ranking score of two vector by consine */
	private double rankingScore(double[] queryVector, double[] docVector){
		return cosine(queryVector, docVector);
	}
	
	/** @return cosine value of vector1,vector2 */
	private double cosine(double[] vector1, double[] vector2){		
		
		/* check same length of vector */
		if (vector1.length != vector2.length) {
			throw new IllegalArgumentException("vec len not the same " + vector1.length +"," + vector2.length);	
		}
		
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
