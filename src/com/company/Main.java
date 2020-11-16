package com.company;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.xml.sax.SAXException;

public class Main {
	
	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, ParseException {
		ReadFile readFile = new ReadFile();
		LuceneSearch lucene = new LuceneSearch();
		readFile.parse();
		readFile.graphCancer();
		readFile.graphObesity();
		lucene.search();
		lucene.graphCancer();
		lucene.graphObesity();
		
	}
}
