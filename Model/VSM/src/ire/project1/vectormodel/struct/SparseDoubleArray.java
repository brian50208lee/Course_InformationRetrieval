package ire.project1.vectormodel.struct;


import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;


public class SparseDoubleArray {
	private HashMap<Integer, Double> array;
	
	private double vectorNorm;
	private double vectorSum;
	private double vectorMax;
	private int vectorNon0;
	private boolean vectorValueUptodate;
	
	/* get/set */
	public void set(int index ,double value){
		vectorValueUptodate = false;
		array.put(index, value);
	}
	public double get(int index){
		return array.get(index)==null? 0.0 : array.get(index);
	}
	public Set<Integer> indexSet(){return this.array.keySet();}
	
	/* constructor */
	public SparseDoubleArray() {
		array = new HashMap<Integer, Double>();
		
		vectorNorm = 0.0;
		vectorSum = 0.0;
		vectorMax = 0.0;
		vectorNon0 = 0;
		vectorValueUptodate = true;
	}
	
	/* math */
	public void add(int index ,double value){
		vectorValueUptodate = false;
		array.put(index, get(index)+value);
	}
	public void mul(int index ,double value){
		vectorValueUptodate = false;
		array.put(index, get(index)*value);
	}
	public void mul(double value){
		vectorValueUptodate = false;
		for (int index : array.keySet()) {
			array.put(index, get(index)*value);
		}
	}
	

	
	
	/* vector */
	private void calSumNormMaxNz(){
		double sum = 0.0;
		double norm = 0.0;
		double max = 0.0;
		int vectorNon0 = 0;
		for (int idx : array.keySet()) {
			double value = array.get(idx);
			sum += value;
			norm += value*value;
			max = Math.max(max, value);
			if (value != 0.0)vectorNon0++;
		}
		norm = Math.sqrt(norm);
		
		this.vectorSum = sum;
		this.vectorNorm = norm;
		this.vectorMax = max;
		this.vectorNon0 = vectorNon0;
		
	}
	public double non0(){
		if(!vectorValueUptodate){
			calSumNormMaxNz();
		}
		return this.vectorNon0;
	}
	public double max(){
		if(!vectorValueUptodate){
			calSumNormMaxNz();
		}
		return this.vectorMax;
	}
	public double sum(){
		if(!vectorValueUptodate){
			calSumNormMaxNz();
		}
		return this.vectorSum;
	}
	public double norm(){
		if(!vectorValueUptodate){
			calSumNormMaxNz();
		}
		return this.vectorNorm;
	}
	public double dot(SparseDoubleArray secondArray){
		SparseDoubleArray lessEleArray;
		SparseDoubleArray moreEleArray;
		if (this.array.keySet().size() < secondArray.array.keySet().size()) {
			lessEleArray = this;
			moreEleArray = secondArray;
		} else {
			lessEleArray = secondArray;
			moreEleArray = this;
		}		
		
		double dotValue = 0.0;
		for (int idx : lessEleArray.array.keySet()) {
			dotValue += lessEleArray.get(idx) * moreEleArray.get(idx);
		}
		return dotValue;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* object */
	@Override
	public String toString() {
		TreeSet<Integer> orderSet = new TreeSet<Integer>();
		orderSet.addAll(array.keySet());
		
		StringBuilder sb = new StringBuilder("[");
		for (int idx : orderSet) {
			sb.append(String.format(" (%d)%f", idx, array.get(idx)));
		}
		sb.append(" ]");
		return sb.toString();
	}

}
