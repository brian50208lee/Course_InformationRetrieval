import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.ansj.splitWord.analysis.ToAnalysis;


public class Main {
	static final String intputDocFolder = "./Documents/";
	static final String intputQryFolder = "./Queries/";
	static final String outputDocFolder = "./Keywords_doc/";
	static final String outputQryFolder = "./Keywords_qry/";
	
	
	public static void main(String[] args) throws IOException {
		parseDoc();
		parseQuery();

	}
	
	public static void parseDoc() throws IOException{
		File folder = new File(intputDocFolder);
		for (String fileName : folder.list()) {
			File file = new File(intputDocFolder + fileName);
			if (file.isHidden())continue;
			BufferedReader br = new BufferedReader(new FileReader(file));
			String title = br.readLine();
			String content = br.readLine();
			br.close();
			

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputDocFolder + fileName)));
			String contentResult[] = ToAnalysis.parse(title + content).toString().split(",");
			for (String word : contentResult) {
				if (word.contains("n") && !word.contains("en")) {
					bw.write(word.split("/")[0] + "\n");
				}
			}
			bw.close();
		}
	}
	
	public static void parseQuery() throws IOException{
		File folder = new File(intputQryFolder);
		for (String fileName : folder.list()) {
			File file = new File(intputQryFolder + fileName);
			if (file.isHidden())continue;
			BufferedReader br = new BufferedReader(new FileReader(file));
			String content = "";
			String line = "";
			while((line = br.readLine()) != null){
				content += line;
			}
			br.close();
			

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputQryFolder + fileName)));
			String contentResult[] = ToAnalysis.parse(content).toString().split(",");
			HashSet<String> set = new HashSet<String>();
			for (String word : contentResult) {
				if (word.contains("n") && !word.contains("en") &&!set.contains(word.split("/")[0])) {
					set.add(word.split("/")[0]);
					bw.write(word.split("/")[0] + "\n");
				}
			}
			bw.close();
		}
	}

}
