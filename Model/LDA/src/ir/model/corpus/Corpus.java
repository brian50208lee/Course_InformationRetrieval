package ir.model.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import ir.utils.BidirectionalIndexMap;


public class Corpus {
	/** corpous list */
	private ArrayList<Integer[]> corpusList;
	
	/** word <-> index map */
	private BidirectionalIndexMap wordsMap;
	
	/** @retrun words map */
	public BidirectionalIndexMap getWordsMap(){return this.wordsMap;}
	
	/** @return matrix documentTerm[doc][term] = number of word occure in doc */
	public int[][] getDocTermMatrix(){
		int docTermMatrix[][] = new int[corpusList.size()][wordsMap.size()];
		for (int docIdx = 0; docIdx < corpusList.size(); docIdx++) {
			Integer doc[] = corpusList.get(docIdx);
			for (int word = 0; word < doc.length; word++) {
				docTermMatrix[docIdx][doc[word]]++;
			}
		}
		return docTermMatrix;
	}
	
	/**
	 * @return return word index list e.g. <br>
	 * doc1 = {1, 1, 2, 2} <br>
	 * doc2 = {3, 2, 1}
	 */
	public ArrayList<Integer[]> getDocTermIndexList(){
		return this.corpusList;
	}
	
	
	/** Constructor. */
	public Corpus(){
		this.corpusList = new ArrayList<Integer[]>();
		this.wordsMap = new BidirectionalIndexMap();
	}
	
	/**
	 * Load corpus form file or folder. <br>
	 * Ignore hidden file. <br><br>
	 * File format: <br>
	 * line1 (doc1) -> w1 w2 ... <br>
	 * line2 (doc2) -> w1 w2 ... <br>
	 * @param path file or folder contain training documents
	 * @throws IOException 
	 */
	public void load(String path) throws IOException{
		File file = new File(path);
		if (file.isDirectory()) {//directory
			for (File mFile : file.listFiles()) {
				loadSigleFile(mFile);
			}
		} else {//file
			loadSigleFile(file);
		}
	}
	
	/**
	 * Load corpus form single file. <br>
	 * Ignore hidden file. <br>
	 * @param file file object
	 * @throws IOException 
	 */
	private void loadSigleFile(File file) throws IOException{
		/* ignore hidden file */
		if(file.isHidden())return;
		
		/* read file to list and update words map */
		BufferedReader reader = new BufferedReader(new FileReader(file));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			/* parsing by space */
			String[] words = line.split("\\s++");
			
			/* change to word index */
			Integer[] wordsIdxArr = new Integer[words.length];
			for (int idx = 0; idx < words.length; idx++) {
				wordsIdxArr[idx] = wordsMap.tryAppend(words[idx]);
			}
			
			/* append to list */
			corpusList.add(wordsIdxArr);
		}
	}
}
