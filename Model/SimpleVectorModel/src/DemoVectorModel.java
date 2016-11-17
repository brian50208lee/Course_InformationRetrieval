import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DemoVectorModel {
	static String docsPath = "/Users/selab/git/Course_InformationRetrieval/ParsedData/Keywords_doc/";
	public static void main(String[] args) throws IOException {
		SparseDocTermMatrix termDocMatrix = new SparseDocTermMatrix();
		File docCollection = new File(docsPath);
		for (String fileName : docCollection.list()) {
			File doc = new File(docsPath + fileName);
			if (doc.isHidden())continue;
			
			BufferedReader br = new BufferedReader(new FileReader(doc));
			for(String line = br.readLine(); line != null; line = br.readLine()){
				if (line.length()>1) {
					termDocMatrix.addElement(fileName, line);
				}
			}
			br.close();	
		}
		System.out.println(termDocMatrix.getRowLength());
		System.out.println(termDocMatrix.getColLength());
		//termDocMatrix.printMatrix();
	}

}
