import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.spreada.utils.chinese.ZHConverter;

public class Main {
	static final String intputFolder = "./Data/";
	static final String outputFolder = "./Result/";
	
	
	static ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	public static void main(String[] args) throws IOException {
		convertQuery();
		//convertDocument();
	}
	
	
	private static void convertQuery() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(intputFolder + "queries.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFolder + "queries.txt")));
		
		String line = "";
		while((line = br.readLine()) != null){
			String qryInfo[] = line.split("\t");
			String id = converter.convert(qryInfo[0]);
			String q1 = converter.convert(qryInfo[1]);
			String q2 = converter.convert(qryInfo[2]);
			String q3 = converter.convert(qryInfo[3]);
			bw.write(id + "\t");
			bw.write(q1 + "\t");
			bw.write(q2 + "\t");
			bw.write(q3 + "\n");
		}

		br.close();
		bw.close();
	}


	public static void convertDocument() throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(new File(intputFolder + "document_collection_1029update.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFolder + "document_collection_1029update.txt")));
		
		String line = "";
		while((line = br.readLine()) != null){
			String docInfo[] = line.split("\t");
			String id = docInfo[0];
			String title = converter.convert(docInfo[1]);
			String content = docInfo.length >2 ? converter.convert(docInfo[2]) : " ";
				
			bw.write(id + "\t");
			bw.write(title + "\t");
			bw.write(content +"\n");
		}
		br.close();
		bw.close();
	}

}
