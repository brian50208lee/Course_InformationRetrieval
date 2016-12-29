package ir.utils;

import java.util.HashMap;
import java.util.Set;

public class BidirectionalIndexMap {
	HashMap<String, Integer> stringMap ;
	HashMap<Integer, String> indexMap ;
	
	
	public BidirectionalIndexMap() {
		stringMap = new HashMap<String, Integer>();
		indexMap = new HashMap<Integer, String>();
	}
	
	public int tryAppend(String str){
		if (stringMap.get(str) == null) {
			int nextIndex = stringMap.size();
			stringMap.put(str, nextIndex);
			indexMap.put(nextIndex, str);
		}
		return getIndex(str);
	}
	
	public int size(){
		return stringMap.size();
	}
	
	public Set<Integer> getIndexSet(){
		return indexMap.keySet();
	}
	
	public Set<String> getStringSet(){
		return stringMap.keySet();
	}
	
	
	public void put(String str, int index){
		stringMap.put(str, index);
		indexMap.put(index, str);
	}	
	public Integer getIndex(String str){
		return stringMap.get(str);
	}
	
	public String getString(int index){
		return indexMap.get(index);
	}
}
