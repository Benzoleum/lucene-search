package com.company;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class LuceneSearch {
	
	static double startTime, endTime;
	static double startTime2, endTime2;
	static ArrayList<Double> times = new ArrayList<Double>();
	static ArrayList<Double> times2 = new ArrayList<Double>();
	double count = 0;
	double count2 = 0;
	ArrayList<Double> counts = new ArrayList<Double>();
	ArrayList<Double> counts2 = new ArrayList<Double>();
	ScoreDoc[] hits1;
	ScoreDoc[] hits2;
	
	// addDoc method
	private static void addDoc(IndexWriter w, String title, String filePath) throws IOException {
		Document doc = new Document();  // instance of document
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new StringField("path", filePath, Field.Store.YES));
		FileReader fr = new FileReader(filePath);
		doc.add(new TextField("contents", fr));
		w.addDocument(doc);
	}
	
	public void search() throws IOException, ParseException {
		try {
			FSDirectory index = FSDirectory
					.open(Paths.get("/Users/benzoleum/Desktop/Nudavay/")); // store index file in the file
			// system
			StandardAnalyzer analyzer = new StandardAnalyzer(); // instantiate an analyzer
			IndexWriterConfig config = new IndexWriterConfig(analyzer); // instantiate index writer config
			IndexWriter w = new IndexWriter(index, config);// instantiate index writer
			addDoc(w, "pubmed1221", "/Users/benzoleum/Desktop/Nudavay/" + "pubmed1221.xml"); //
			// implement addDoc method
//			addDoc(w, "pubmed20n1223", "/Users/benzoleum/Desktop/Nudavay/" + "pubmed20n1223.xml");  //
			// implement addDoc method
			w.close(); // close writer
			String queryStr1 = "cancer"; // string to parse
			String queryStr2 = "obesity";
			Query q1 = new QueryParser("contents", analyzer).parse(queryStr1); //search against contents
			Query q2 = new QueryParser("contents", analyzer).parse(queryStr2);
			IndexReader reader = DirectoryReader.open(index);  // access the index
			IndexSearcher searcher = new IndexSearcher(reader); // search the index
			TopDocs docs1 = searcher.search(q1, Integer.MAX_VALUE);
			TopDocs docs2 = searcher.search(q2, Integer.MAX_VALUE);
			hits1 = docs1.scoreDocs; // store number of hits
			hits2 = docs2.scoreDocs;
			TopScoreDocCollector collector = TopScoreDocCollector.create(1, Integer.MAX_VALUE);
			for (int i = 0; i < hits1.length; i++) { // iterate the hits1 array
				int docId = hits1[i].doc;
				Document d = searcher.doc(docId);
				startTime = System.currentTimeMillis(); // start time
				searcher.search(q1, collector);  // search the provided index
				endTime = System.currentTimeMillis(); // end time
				count += 1;
				times.add(endTime % 1000); // add the times to the arraylist
				counts.add(count);  // add the counts to the array list
			}
			for (int i = 0; i < hits2.length; i++) {
				int docId = hits2[i].doc;
				Document d = searcher.doc(docId);
				startTime2 = System.currentTimeMillis();
				searcher.search(q1, collector);
				endTime2 = System.currentTimeMillis();
				count2 += 1;
				times2.add(endTime2 % 1000);
				counts2.add(count2);
			}
			reader.close();  // close the reader
		} catch (IOException | ParseException ex) {  // catch exceptions if any
			ex.printStackTrace();
		}
	}
	
	public void graphCancer() {
		double[] target = new double[counts.size()];
		for (int i = 0; i < target.length; i++) {
			target[i] = counts.get(i);
		}
		double[] target1 = new double[times.size()];
		for (int i = 0; i < target1.length; i++) {
			target1[i] = times.get(i);
		}
		XYChart chart = QuickChart.getChart("Cancer", "Number of hits", "Elapsed time",
				"y(x)",
				target,
				target1);
		new SwingWrapper(chart).displayChart();
		
	}
	
	public void graphObesity() {
		double[] target = new double[counts2.size()];
		for (int i = 0; i < target.length; i++) {
			target[i] = counts2.get(i);
		}
		double[] target1 = new double[times2.size()];
		for (int i = 0; i < target1.length; i++) {
			target1[i] = times2.get(i);
		}
		XYChart chart = QuickChart.getChart("Obesity", "Number of hits", "Elapsed time",
				"y(x)",
				target,
				target1);
		new SwingWrapper(chart).displayChart();
		
	}
}
