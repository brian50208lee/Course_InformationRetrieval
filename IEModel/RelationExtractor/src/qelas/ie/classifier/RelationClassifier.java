package qelas.ie.classifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RelationClassifier implements Serializable{
	private String className = null;
	private HashMap<String, PatternWord> patternsCounter = null;
	private ArrayList<PatternWord> topRelatedPattern = null;
	private boolean topRelatedPattern_upToDate = false;
	private int topNumber = 50;
	public RelationClassifier(String className){
		this.className = className;
		this.patternsCounter = new HashMap<String, PatternWord>();
	}
	
	public String getClassName(){
		return this.className;
	}
	
	public void train(String entity1, String entity2, List<String> relatedPattern){
		for (String pattern : relatedPattern) {
			if(pattern.equals(entity1) || pattern.equals(entity2)) continue;
			if(patternsCounter.get(pattern) == null) patternsCounter.put(pattern, new PatternWord(pattern));
			patternsCounter.get(pattern).count++;
		}
		topRelatedPattern_upToDate = false;
	}
	
	public void printRelatedPattern(){
		findTopRelatedPattern(topNumber);
		
		for (PatternWord patternWord : topRelatedPattern) {
			System.out.printf("%s\t%s\t%s\n", className, patternWord.pattern, patternWord.count);
		}
	}
	
	private ArrayList<PatternWord> findTopRelatedPattern(int topNumber){
		if (topRelatedPattern_upToDate) {
			return topRelatedPattern;
		}
		ArrayList<PatternWord> topPatterns = new ArrayList<PatternWord>();
		for (int i = 1; i <= topNumber; i++) {
			if (i > patternsCounter.size()) break;
			PatternWord maxPatternWord = new PatternWord("nullPattern");
			for(PatternWord patternWord : patternsCounter.values()){
				if (patternWord.count > maxPatternWord.count && !topPatterns.contains(patternWord)) {
					maxPatternWord = patternWord;
				}
			}
			topPatterns.add(maxPatternWord);
		}
		topRelatedPattern = topPatterns;
		topRelatedPattern_upToDate = true;
		return topPatterns;
	}
	
	public boolean inTopRelatedPatterns(String pattern){
		findTopRelatedPattern(topNumber);
		for(PatternWord patternWord : topRelatedPattern){
			if(patternWord.pattern.equals(pattern)) return true;
		}
		return false;
	}
	
	
}
