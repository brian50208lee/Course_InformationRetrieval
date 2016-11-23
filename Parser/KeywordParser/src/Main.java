import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.ansj.splitWord.analysis.ToAnalysis;



public class Main {

	static final String intputFolder = "./Data/";
	static final String outputFolder = "./Result/";
	
	
	public static void main(String[] args) throws IOException {
		parseQuery();
		//parseDoc();
	}
	
	public static void parseDoc() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(intputFolder + "document_collection_1029update.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFolder + "document_collection_1029update.txt")));

		for (String line = br.readLine(); line != null; line = br.readLine()) {
			/* eash doc */
			String qeury[] = line.split("\t");
			String id = qeury[0];
			String title = qeury[1];
			String content = qeury[2];
			String keyContent = String.format("%s %s", title, content);
			
			String contentResult[] = ToAnalysis.parse(keyContent).toString().split(",");
			
			bw.write(id + "\t");
			for (String word : contentResult) {
				if ((word.contains("n")) && !word.contains("en") ) {
					bw.write(word.split("/")[0] + " ");
				}
			}
			bw.write("\n");
		}
		br.close();
		bw.close();
	}
	
	public static void parseQuery() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(intputFolder + "queries.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFolder + "queries.txt")));
		
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			/* eash query */
			String qeury[] = line.split("\t");
			String id = qeury[0];
			String title = qeury[1];
			String desc = qeury[2];
			String conc = qeury[3];
			String keyContent = title + " " + desc + " " + conc;
			
			String contentResult[] = ToAnalysis.parse(keyContent).toString().split(",");
			HashSet<String> keywordsSet = new HashSet<String>();
			
			bw.write(id + "\t");
			for (String word : contentResult) {
				if ((word.contains("n")) && !word.contains("en") && !keywordsSet.contains(word.split("/")[0])) {
					keywordsSet.add(word.split("/")[0]);
					bw.write(word.split("/")[0] + " ");
				}
			}
			bw.write("\n");
		}
		br.close();
		bw.close();
	}

}
