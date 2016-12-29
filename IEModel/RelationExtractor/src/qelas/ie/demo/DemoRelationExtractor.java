package qelas.ie.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import qelas.ie.classifier.RelationClassifier;
import qelas.ie.util.Parser;

public class DemoRelationExtractor {
	public static void main(String[] args) throws IOException {
		BufferedReader text_reader = new BufferedReader(new FileReader("/Users/selab/git/Course_InformationRetrieval/Data/Project2/ref_text.txt"));
		BufferedReader train_reader = new BufferedReader(new FileReader("/Users/selab/git/Course_InformationRetrieval/Data/Project2/train.csv"));
		BufferedReader test_reader = new BufferedReader(new FileReader("/Users/selab/git/Course_InformationRetrieval/Data/Project2/test.csv"));
		BufferedWriter result_writer = new BufferedWriter(new FileWriter("/Users/selab/git/Course_InformationRetrieval/Data/Project2/result.csv"));
		
		// read text
		ArrayList<String> referenceSentence = new ArrayList<String>();
		for (String line = text_reader.readLine() ; line != null ; line = text_reader.readLine()) {
			referenceSentence.add(line);
		}
		text_reader.close();
		
		// train classifier
		HashMap<String, RelationClassifier> classifierMap = new HashMap<String, RelationClassifier>();
		train_reader.readLine(); // ignore title
		for (String line = train_reader.readLine() ; line != null ; line = train_reader.readLine()) {
			String attributes[] = line.split(",");
			int id = Integer.parseInt(attributes[0]);
			String entity1 = attributes[1];
			String entity2 = attributes[2];
			String relation = attributes[3];
			
			for(String sentence : referenceSentence){
				if (sentence.contains(entity1) && sentence.contains(entity2)) {
					if (classifierMap.get(relation) == null) classifierMap.put(relation, new RelationClassifier(relation));
					classifierMap.get(relation).train(entity1, entity2, Parser.parseToKeyword(sentence));
				}
			}
		}
		train_reader.close();
		
		
		for(String className : classifierMap.keySet()){
			classifierMap.get(className).printRelatedPattern();
		}
		
		// first test
		StringBuilder resultString = new StringBuilder("Id,Property\n");
		test_reader.readLine(); // ignore title
		for (String line = test_reader.readLine() ; line != null ; line = test_reader.readLine()) {
			String attributes[] = line.split(",");
			int id = Integer.parseInt(attributes[0]);
			String entity1 = attributes[1];
			String entity2 = attributes[2];
			
			HashMap<String, Integer> classScore = new HashMap<String, Integer>();
			for(String sentence : referenceSentence){
				if (sentence.contains(entity1) && sentence.contains(entity2)) {
					List<String> patternList = Parser.parseToKeyword(sentence);
					for (String pattern : patternList) {
						for (String className : classifierMap.keySet()) {
							if(classifierMap.get(className).inTopRelatedPatterns(pattern)){
								classScore.put(className, classScore.getOrDefault(className, 0) + 1);
							}
						}
					}
				}
			}
			String maxRelation = "unknown";
			int maxRelationScore = 0;
			for (String className : classScore.keySet()) {
				if(classScore.get(className) > maxRelationScore){
					maxRelation = className;
					maxRelationScore = classScore.get(className);
				}
			}
			
			// second test if unknown
			if (maxRelation.equals("unknown")) {
				for(String sentence : referenceSentence){
					if (sentence.contains(entity1) || sentence.contains(entity2)) { //score by sigle word appear
						List<String> patternList = Parser.parseToKeyword(sentence);
						for (String pattern : patternList) {
							for (String className : classifierMap.keySet()) {
								if(classifierMap.get(className).inTopRelatedPatterns(pattern)){
									classScore.put(className, classScore.getOrDefault(className, 0) + 1);
								}
							}
						}
					}
				}
				for (String className : classScore.keySet()) {
					if(classScore.get(className) > maxRelationScore){
						maxRelation = className;
						maxRelationScore = classScore.get(className);
					}
				}
			}
			
			// final test
			if (maxRelation.equals("unknown")) maxRelation = "birthPlace";

			// add output
			resultString.append(id + ",");
			resultString.append(maxRelation + "\n");
			System.out.printf("%-8d%-20s%-20s%-15s%-5d\n",id, entity1, entity2, maxRelation, maxRelationScore);
			
			

		}
		test_reader.close();
		result_writer.write(resultString.toString());
		result_writer.close();
	}

}
