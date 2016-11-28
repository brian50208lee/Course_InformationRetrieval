package ire.project1.vectormodel.lsa;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.SingularValueDecomposition;

public class SVD {
	
	public static double[][] transform(double[][] docTermMatrix, int latentDimention){
		System.out.println("SVD ...");
		int docNum = docTermMatrix.length;
		int trmNum = docTermMatrix[0].length;
		
		if (latentDimention >= Math.min(docNum, trmNum)) {
			throw new IllegalArgumentException("latent Dimention too big " + latentDimention);
		} 
		
		Algebra alg = new Algebra();
		DoubleMatrix2D coltMatrix = new DenseDoubleMatrix2D(docTermMatrix);
		if (docNum < trmNum) coltMatrix = alg.transpose(coltMatrix);
		SingularValueDecomposition coltSVD = new SingularValueDecomposition(coltMatrix);
		DoubleMatrix2D coltS = coltSVD.getS();
		
		// reduce dimension
		for (int i = latentDimention; i < coltS.toArray().length; i++) {
			coltS.set(i, i, 0);
		}
		
		// result matrix
		DoubleMatrix2D newMatrix = coltSVD.getU().zMult(coltS, null).zMult(alg.transpose(coltSVD.getV()), null);
		if (docNum < trmNum)newMatrix = alg.transpose(newMatrix);

		return newMatrix.toArray();
	}
}
