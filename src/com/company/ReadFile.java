package com.company;

// Import necessary modules

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadFile {
	
	double count = 0;
	double count2 = 0;
	double startTime, endTime, totalTime;
	double startTime2, endTime2, totalTime2;
	ArrayList history = new ArrayList();
	ArrayList<Double> counts = new ArrayList<Double>();
	ArrayList<Double> counts2 = new ArrayList<Double>();
	ArrayList<Double> times = new ArrayList<Double>();
	ArrayList<Double> times2 = new ArrayList<Double>();
	
	public void parse() throws IOException, ParserConfigurationException, SAXException {
		try {
			String keyword1 = "cancer";
			String keyword2 = "obesity";
			// produce DOM object trees
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder(); // obtain document from XML
			Document document = builder.parse(new File("pubmed1221.xml")); // access the document
			document.getDocumentElement().normalize(); // method to put all text nodes into a normal form
			// NodeList object with a collection of all elements with the tag name Journal
			NodeList nodeList = document.getElementsByTagName("Journal");
			// iterate the node list
			for (int temp = 0; temp < nodeList.getLength(); temp++) {
				Node node = nodeList.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					try {
						// create a string with the tag name Title
						String str = element.getElementsByTagName("Title").item(0).getTextContent();
						String[] words = str.split(" ", 3); // split the string to search for the word
						for (String word : words) {
							
							if (word.equalsIgnoreCase(keyword1)) { // if the word is found print the contents
								count += 1;
								startTime = System.currentTimeMillis();
								history.add(keyword1);
								endTime = System.currentTimeMillis();
								times.add(endTime % 1000);
								counts.add(count);
								totalTime += endTime;
							} else if (word.equalsIgnoreCase(keyword2)) {
								count2 += 1;
								startTime2 = System.currentTimeMillis();
								history.add(keyword1);
								endTime2 = System.currentTimeMillis();
								times2.add(endTime2 % 1000);
								counts2.add(count2);
								totalTime2 += endTime2;
							}
							
						}
					} catch (Exception e) { // catch exceptions
						System.out.println(
								"Either the keyword does not exist or there is a problem with the "
										+ "file. Please try again.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		XYChart chart = QuickChart.getChart("Cancer", "Number of titles", "Elapsed time",
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
		XYChart chart = QuickChart.getChart("Obesity", "Number of titles", "Elapsed time",
				"y(x)",
				target,
				target1);
		new SwingWrapper(chart).displayChart();
	}
	
}















