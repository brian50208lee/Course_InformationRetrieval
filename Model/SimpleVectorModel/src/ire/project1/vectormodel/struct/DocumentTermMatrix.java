package ire.project1.vectormodel.struct;
import java.util.HashMap;

public class DocumentTermMatrix {
	private SparseDoubleMatrix docTermMatrix;
	
	
	private int rowLength = 0;
	private int colLength = 0;

	private HashMap<String, Integer> docIdxMap;
	private HashMap<String, Integer> terIdxMap;
	private HashMap<Integer, String> idxDocMap;
	private HashMap<Integer, String> idxTerMap;
	
	public int getRowLength(){return this.rowLength;}
	public int getColLength(){return this.colLength;}
	public int getTerm(String term){return this.terIdxMap.get(term);}
	public int getDoc(String doc){return this.docIdxMap.get(doc);}
	public String getTerm(int term){return this.idxTerMap.get(term);}
	public String getDoc(int doc){return this.idxDocMap.get(doc);}
	
	
	public DocumentTermMatrix() {
		this.docTermMatrix = new SparseDoubleMatrix();

		this.docIdxMap = new HashMap<String,Integer>();
		this.terIdxMap = new HashMap<String,Integer>();
		this.idxDocMap = new HashMap<Integer,String>();
		this.idxTerMap = new HashMap<Integer,String>();
	}
	
	
	public void addElement(String doc, String term){
		if (docIdxMap.get(doc) == null) {
			docIdxMap.put(doc, rowLength);
			idxDocMap.put(rowLength, doc);
			rowLength ++;
		}
		if (terIdxMap.get(term) == null) {
			terIdxMap.put(term, colLength);
			idxTerMap.put(colLength, term);
			colLength ++;
		}
		
		int docIdx = docIdxMap.get(doc);
		int termIdx = terIdxMap.get(term);
		
		docTermMatrix.add(docIdx, termIdx, 1);
	}
	
	public double getValue(int i, int j) {
		if (i >= rowLength || i < 0 ) {
			throw new RuntimeException("row index out of bound:"+i);
		} 
		if (j >= colLength || j < 0 ) {
			throw new RuntimeException("colum index out of bound:"+j);
		} 
		
		return docTermMatrix.get(i, j);
	}	
	
	
	public void setValue(int i, int j, double value){
		if (i >= rowLength || i < 0 ) {
			throw new RuntimeException("row index out of bound:"+i);
		} 
		if (j >= colLength || j < 0 ) {
			throw new RuntimeException("colum index out of bound:"+j);
		} 
		
		docTermMatrix.set(i, j, value);
	}
	
	public String getTermList(){
		StringBuilder sb = new StringBuilder("[ ");
		for (int i = 0; i < colLength; i++){
			sb.append(getTerm(i)+" ");
			if(i == colLength-1)sb.append("]");
		}
		return sb.toString();
	}
	
	public String getDocList(){
		StringBuilder sb = new StringBuilder("[ ");
		for (int i = 0; i < rowLength; i++){
			sb.append(getDoc(i)+" ");
			if(i == rowLength-1)sb.append("]");
		}
		return sb.toString();
	}

}
