package ire.project1.vectormodel.struct;


import java.util.HashMap;
import java.util.TreeSet;


public class SparseDoubleArray {
	private HashMap<Integer, Double> array;
	
	/* get/set */
	public void set(int index ,double value){
		array.put(index, value);
	}
	public double get(int index){
		return array.get(index)==null? 0.0 : array.get(index);
	}
	
	/* constructor */
	public SparseDoubleArray() {
		array = new HashMap<Integer, Double>();
	}
	
	/* math */
	public void add(int index ,double value){
		array.put(index, get(index)+value);
	}
	public void mul(int index ,double value){
		array.put(index, get(index)*value);
	}
	public void mul(double value){
		for (int index : array.keySet()) {
			array.put(index, get(index)*value);
		}
	}
	

	
	
	/* vector */
	public double sum(){
		double sum = 0.0;
		for (int idx : array.keySet()) {
			sum += array.get(idx);
		}
		return sum;
	}
	public double norm(){
		double norm = 0.0;
		for (int idx : array.keySet()) {
			double value = array.get(idx);
			norm += value*value;
		}
		norm = Math.sqrt(norm);
		return norm;
	}
	public double dot(SparseDoubleArray secondArray){
		double dotValue = 0.0;
		for (int idx : this.array.keySet()) {
			dotValue += this.get(idx) * secondArray.get(idx);
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
