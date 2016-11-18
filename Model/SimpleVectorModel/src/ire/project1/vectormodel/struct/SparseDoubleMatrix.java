package ire.project1.vectormodel.struct;


import java.util.HashMap;
import java.util.TreeSet;

public class SparseDoubleMatrix {
	HashMap<Integer, SparseDoubleArray> matrix;
	
	/* get/set */
	public SparseDoubleArray getRow(int row){
		if(matrix.get(row) == null)matrix.put(row, new SparseDoubleArray());
		return matrix.get(row);
	}
	public void set(int i, int j ,double value){
		if(matrix.get(i) == null)matrix.put(i, new SparseDoubleArray());
		matrix.get(i).set(j, value);
	}
	public double get(int i, int j ){
		return matrix.get(i)==null? 0.0 : matrix.get(i).get(j);
	}
	
	/* constructor */
	public SparseDoubleMatrix() {
		matrix = new HashMap<Integer, SparseDoubleArray>();
	}
	
	
	
	/* math */
	public void add(int i, int j ,double value){
		if(matrix.get(i) == null)matrix.put(i, new SparseDoubleArray());
		matrix.get(i).add(j, value);
	}
	public void mul(int i, int j ,double value){
		matrix.get(i).mul(j, value);
	}
	public void mulRow(int row,double value){
		matrix.get(row).mul(value);
	}
	public void mul(double value){
		for (int index : matrix.keySet()) {
			matrix.get(index).mul(value);
		}
	}
	

	
	
	
	@Override
	public String toString() {
		TreeSet<Integer> orderSet = new TreeSet<Integer>();
		orderSet.addAll(matrix.keySet());
		
		StringBuilder sb = new StringBuilder("");
		for (int idx : orderSet) {
			sb.append(String.format(" [%d]%s\n", idx, matrix.get(idx).toString()));
		}
		return sb.toString();
	}
	
}
