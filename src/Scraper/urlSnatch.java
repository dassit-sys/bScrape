package Scraper;

import java.util.ArrayList;
import java.util.Arrays;

public class urlSnatch {	//Website dependent

	public urlSnatch() {
	}
	
	public urlSnatch(String urlMess, String format) {
		snatch(urlMess, format);
	}
	
	public ArrayList<String> snatch(String urlMess, String format) {
		//Boolean diagnostics = false; //For debugging
		
		ArrayList<String> foundurls = new ArrayList<String>();		
		ArrayList<String> possibleBeginnings = new ArrayList<String>(Arrays.asList(
				"https://", 
                "http://", 
                "www.")); //Default, do not change
		//Add website-specific beginnings here
		if (urlMess.contains("gucci") && !urlMess.contains("ekseption")) {	//For gucci pages
			possibleBeginnings.add("//media.gucci.com/");
		}
		if (urlMess.contains("ounass.qa")) {
			possibleBeginnings.add("//ounass");
			possibleBeginnings.remove("http://");
			possibleBeginnings.remove("https://");
		}
		if (urlMess.contains("wiseboutique")) {
			possibleBeginnings.add("/public");
		}
		
		int count = (urlMess.length() - (urlMess.replaceAll(("." + format), "")).length())/format.length()+1;
		
		//1. find
		//System.out.printf("\n\n%s\n\n", urlMess); //For use in debugging
		while (urlMess.contains("." + format) && checkForElementsInString(urlMess, possibleBeginnings)) {
				int end = urlMess.indexOf("." + format)+(format.length()+1); //Find end point
				int start = 0;
				for (int y = 0; y<possibleBeginnings.size(); y++) {	//Loops through possible beginnings
					int currentStart = urlMess.lastIndexOf(possibleBeginnings.get(y), end-1);
					if (currentStart != -1 && currentStart>start) {	//Keeps the closest one to avoid overcutting (can still overcut occasionally)
						start = currentStart;
					}
				}

				//2. copy
				String temp = urlMess.substring(start, end);
				if (!temp.contains(" ")) {
					System.out.printf("Step 2 - copy: %s\n", temp);
				} else {
					System.out.printf("Step 2 - copy: Skipped spaces: %s\n", temp);
					urlMess = urlMess.replace(temp, "");
					continue;
				}
							
				//3. save to AL
				foundurls.add(temp);

				//4. remove
				urlMess = urlMess.replace(temp, "");
			}
		if (count != foundurls.size()) {
			System.out.printf("possibleBeginnings incomplete: %d count, %d cut\n", count, foundurls.size());
		}

		//5. format
		for (int x = 0; x<foundurls.size(); x++) {
			if (foundurls.get(x).startsWith("www.")) {		//Adds "https://" to URLs beginning in "www.", NOT site dependent
				foundurls.set(x, "https://" + foundurls.get(x));
			}
			if (foundurls.get(x).startsWith("//media.gucci.com/")) { //For gucci formatting
				foundurls.set(x, foundurls.get(x).replace("//media.gucci.com/", "https://media.gucci.com/"));
			}
			if (foundurls.get(x).startsWith("//ounass")) {  //For Ounass pages
				foundurls.set(x, foundurls.get(x).replace("//ounass", "https://ounass"));
			}
			if (foundurls.get(x).startsWith("/public")) { //For WiseBoutique pages
				foundurls.set(x, foundurls.get(x).replace("/public", "https://www.wiseboutique.com/public"));
			}
		}
		return foundurls;
		
		//if (diagnostics) {
		//	System.out.printf("urlSnatch Diagnostics:\nFormat: %s\nCount: %d\nFoundURLS: %d", format, count, foundurls.size());
		//}
	}
	
	public Boolean checkForElementsInString(String stringToSearch, ArrayList<String> list) {
	    Boolean inString = false;
	    for(int i = 0; i < list.size(); i++){ 
	    	if (stringToSearch.contains(list.get(i))) {
	    		inString = true;
	    	}
	    }
	    return inString;
	}
}
