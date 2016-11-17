import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class SparseDocTermMatrix extends HashMap<String, Double>{
	private int rowLength = 0;
	private int colLength = 0;

	private HashMap<String, Integer> docIdxMap;
	private HashMap<String, Integer> terIdxMap;
	private HashMap<Integer, String> idxDocMap;
	private HashMap<Integer, String> idxTerMap;
	private LinkedList<String> elementOrderList;

	public int getRowLength(){return this.rowLength;}
	public int getColLength(){return this.colLength;}
	public int getTerm(String term){return this.terIdxMap.get(term);}
	public int getDoc(String doc){return this.docIdxMap.get(doc);}
	public String getTerm(int term){return this.idxTerMap.get(term);}
	public String getDoc(int doc){return this.idxDocMap.get(doc);}
	
	public SparseDocTermMatrix(int rowLength, int colLength) {
		this.rowLength = rowLength;
		this.colLength = colLength;

		docIdxMap = new HashMap<String,Integer>();
		terIdxMap = new HashMap<String,Integer>();
		idxDocMap = new HashMap<Integer,String>();
		idxTerMap = new HashMap<Integer,String>();
		elementOrderList = new LinkedList<String>();
	}
	
	public SparseDocTermMatrix(){
		this(0,0);
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
		
		setValue(docIdx, termIdx, getValue(docIdx, termIdx)+1);
	}
	
	public double getValue(int i, int j) {
		if (i>=rowLength || j>=colLength || i<0 || j<0) {
			new Exception("matrix index out of bound." + i +"," +j).printStackTrace();
			System.exit(1);
		} 
		
		return get(i+","+j) == null ? 0.0 :get(i+","+j).doubleValue();
	}	
	
	
	public void setValue(int i, int j, double value){
		if (i>=rowLength || j>=colLength || i<0 || j<0) {
			new Exception("matrix index out of bound." + i +"," +j).printStackTrace();
			System.exit(1);
		} 
		
		if (get(i+","+j) == null)elementOrderList.add(i+","+j);
		put(i+","+j, value);
	}
	
	
	public String getTermList(){
		StringBuilder sb = new StringBuilder("");
		sb.append("term: ");
		for (int i = 0; i < colLength; i++){
			sb.append(getTerm(i)+" ");
			if(i == colLength-1)sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getDocList(){
		StringBuilder sb = new StringBuilder("");
		sb.append("doc: ");
		for (int i = 0; i < rowLength; i++){
			sb.append(getDoc(i)+" ");
			if(i == rowLength-1)sb.append("\n");
		}
		return sb.toString();
	}

	
	
	public void printMatrix() {
		StringBuilder sb = new StringBuilder("");
		
		
		Iterator<String> iterator = elementOrderList.iterator();
		int currentRow = -1;
		while(iterator.hasNext()){
			String idx[] = iterator.next().split(",");
			if (currentRow != Integer.parseInt(idx[0]) ) {
				System.out.println(sb.toString());
				sb = new StringBuilder("");
				currentRow = Integer.parseInt(idx[0]);
				sb.append("\n"+idx[0]+":");
				System.out.println(idx[0]);
			}
			sb.append(" "+idx[1]+"-"+get(idx[0]+","+idx[1]));
		}
	}
}
