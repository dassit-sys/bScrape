package Scraper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;

public class colorTagger {		//Website dependent in find()
	public ArrayList<String> colorURLs = new ArrayList<String>();
	
	public colorTagger() {
	}
	
	public colorTagger(String url, Document doc) throws FileNotFoundException, IOException {
		this.find(url, doc);
	}
	
	public void find(String url, Document doc) throws FileNotFoundException, IOException {		//Website dependence spot
		//find and save all color urls to colorURLs arraylist
		ArrayList<String> colorURLs = new ArrayList<String>();
		String urlMess = doc.toString();
		
		urlSnatch fetch = new urlSnatch();
				
		if (colorURLs.size() > 0) {
			for (String colorURL : colorURLs) {		//Get the colorTags
				this.get(colorURL);
			}
		}
	}
	
	public void get(String colorURL) throws FileNotFoundException, IOException {
		PicExtractor fetch = new PicExtractor(colorURL, 1);
	}
}
