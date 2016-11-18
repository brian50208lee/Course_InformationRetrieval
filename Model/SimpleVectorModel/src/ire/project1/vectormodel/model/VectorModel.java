package ire.project1.vectormodel.model;
import ire.project1.vectormodel.struct.DocumentTermMatrix;

public class VectorModel {
	private DocumentTermMatrix documentTermMatrix;
	
	public VectorModel(DocumentTermMatrix documentTermMatrix) {
		this.documentTermMatrix = documentTermMatrix;
	}
}
