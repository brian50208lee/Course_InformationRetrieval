package ir.model.demo;

import java.io.IOException;

import ir.model.corpus.Corpus;

public class DemoLDA {

	public static void main(String[] args) throws IOException {
		Corpus corpus = new Corpus();
		corpus.load("./data/");

	}

}
