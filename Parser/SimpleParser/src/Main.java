import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.spreada.utils.chinese.ZHConverter;

public class Main {
	static final String intputFolder = "./Data/";
	static final String outputDocFolder = "./DocResult/";
	static final String outputQryFolder = "./QryResult/";
	
	
	static ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	public static void main(String[] args) throws IOException {
		convertQuery();
		//convertDocument();
	}
	
	
	private static void convertQuery() throws IOException {
		File qyeries = new File(intputFolder + "queries.txt");
		BufferedReader br = new BufferedReader(new FileReader(qyeries));
		
		String line = "";
		while((line = br.readLine()) != null){
			String qryInfo[] = line.split("\t");
			String id = converter.convert(qryInfo[0]);
			String q1 = converter.convert(qryInfo[1]);
			String q2 = converter.convert(qryInfo[2]);
			String q3 = converter.convert(qryInfo[3]);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputQryFolder + id + ".txt")));
			bw.write(q1 + "\n");
			bw.write(q2 + "\n");
			bw.write(q3 + "\n");
			bw.close();
		}
	}


	public static void convertDocument() throws IOException{
		
		File documentCollection = new File(intputFolder + "document_collection_1029update.txt");
		
		BufferedReader br = new BufferedReader(new FileReader(documentCollection));
		
		String line = "";
		while((line = br.readLine()) != null){
			String docInfo[] = line.split("\t");
			String id = docInfo[0];
			String title = converter.convert(docInfo[1]);
			String content = docInfo.length >2 ? converter.convert(docInfo[2]) : "";
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputDocFolder + id + ".txt")));
			bw.write(title + "\n");
			bw.write(content);
			bw.close();
		}
		
	}

}
