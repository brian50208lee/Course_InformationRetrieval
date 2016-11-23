package ire.project1.vectormodel.lsa;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.SingularValueDecomposition;

public class SVD {
	public static double[][] transform(double[][] docTermMatrix, int dimToReduce){
		System.out.println("SVD ...");
		int docNum = docTermMatrix.length;
		int trmNum = docTermMatrix[0].length;
		
		if (dimToReduce >= Math.min(docNum, trmNum)) {
			throw new IllegalArgumentException("number of dimention to reduce too big " + dimToReduce);
		} 
		
		Algebra alg = new Algebra();
		DoubleMatrix2D coltMatrix = new DenseDoubleMatrix2D(docTermMatrix);
		coltMatrix = alg.transpose(coltMatrix);
		SingularValueDecomposition coltSVD = new SingularValueDecomposition(coltMatrix);
		DoubleMatrix2D coltS = coltSVD.getS();
		
		// reduce dimension
		for (int i = coltS.toArray().length - dimToReduce ; i < coltS.toArray().length; i++) {
			coltS.set(i, i, 0);
		}
		
		//new matrix
		DoubleMatrix2D newMatrix = coltSVD.getU().zMult(coltS, null).zMult(alg.transpose(coltSVD.getV()), null);
		double returnMatrix[][] = alg.transpose(newMatrix).toArray();

		return returnMatrix;
	}
}
