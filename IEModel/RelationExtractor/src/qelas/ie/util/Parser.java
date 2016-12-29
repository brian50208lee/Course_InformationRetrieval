package qelas.ie.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;

public class Parser {
	private static final List<String> stopWord = Arrays.asList(new String[]
			{		"","，","。","、","在","的","和","了","与","为",
					"是","后","被","有","他","之","而","也","并","她",	
					"由","及","曾","得","以"		});
	
	
	public static List<String> parseToKeyword(String sentence){
		JiebaSegmenter jieba = new JiebaSegmenter();
		List<String> segments = jieba.sentenceProcess(sentence);
		List<String> result = new LinkedList<String>();
		for (String segment : segments) {
			if (!isStopWordSymbol(segment)) {
				result.add(segment);
			}
		}
		return result;
	}
	public static boolean isStopWordSymbol(String word){
		word = word.replaceAll("\\s+", "");
		return stopWord.contains(word);
	}
}
