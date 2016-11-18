package ire.project1.vectormodel.demo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ire.project1.vectormodel.struct.DocumentTermMatrix;

public class Demo {
	static String docsPath = "/Users/selab/git/Course_InformationRetrieval/ParsedData/Keywords_doc/";
	public static void main(String[] args) throws IOException {
		DocumentTermMatrix documentTermMatrix = new DocumentTermMatrix();
		File docCollection = new File(docsPath);
		for (String fileName : docCollection.list()) {
			File doc = new File(docsPath + fileName);
			if (doc.isHidden())continue;
			
			BufferedReader br = new BufferedReader(new FileReader(doc));
			for(String line = br.readLine(); line != null; line = br.readLine()){
				if (line.length()>1) {
					documentTermMatrix.addElement(fileName, line);
				}
			}
			br.close();	
		}
		System.out.println(documentTermMatrix.getRowLength());
		System.out.println(documentTermMatrix.getColLength());
		//termDocMatrix.printMatrix();
	}

}
