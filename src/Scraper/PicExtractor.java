package Scraper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PicExtractor {		//Website dependent in two spots: line 58 (step 3), discriminator, and line 159 (downloadimages, currently only for furla)
	private static String IMAGE_DESTINATION_FOLDER = "C:/buyma/";
	private int imagesSaved;
	private int totalSpams;
	private int directoriesCreated;
	private ArrayList<String> alreadyGot = new ArrayList<String>();
	
	public PicExtractor() {
		this.imagesSaved = 0;
		this.totalSpams = 0;
		this.alreadyGot.clear();
		this.directoriesCreated = 0;
	}
	
    public PicExtractor(String strURL, int mode) throws FileNotFoundException, IOException {
    	this.imagesSaved = 0;
    	this.totalSpams = 0;
    	this.extract(strURL, mode);
    	this.directoriesCreated = 0;
    }
    
    public void extract(String strURL, int mode) throws IOException, FileNotFoundException {
    	if (strURL==null || strURL=="") {
    		System.out.println("null/empty URL string! skipped\n");
    		return;
    	}
    	
    	//1. Connect to site, get document
    	Document doc = null;
    	String page = null;
    	if (strURL.contains("nickis.com")) { //For pages with dynamic content
    		FirefoxDriver driver = new FirefoxDriver();
    		WebDriverWait wait = new WebDriverWait(driver, 30);
    		driver.navigate().to(strURL);
    		page = driver.findElement(By.tagName("body")).getText();
    	} else { //For pages with no dynamic content    	
        	doc = Jsoup
            		.connect(strURL)
            		.userAgent("Mozilla/5.0")
            		.maxBodySize(0)
            		.timeout(10 * 10000)
            		.get();
    	}
    	
    	//2. Save source code to string
    	String urlMess = null;
    	if (doc == null) {
    		urlMess = page;
    	} else {
    		urlMess = doc.toString();
    	}
    	directoriesCreated++;
    	System.out.printf("JSoup sees:\n%s\n", urlMess);
    	//3. Extract all image source URLs from that string
    	urlSnatch fetch = new urlSnatch();
    	ArrayList<String> urls = new ArrayList<String>();
    	if (strURL.contains("melijoe.com") || strURL.contains("nickis.com") || strURL.contains("giglio") || strURL.contains("theclutcher.com") || strURL.contains("giglio.com") || strURL.contains("buyma.com") || strURL.contains("ekseption.com") || strURL.contains("balenciaga.com") || strURL.contains("loewe.com") || strURL.contains("ysl.com") || strURL.contains("gucci.com") || strURL.contains("farfetch.com") || strURL.contains("childsplayclothing.com") || strURL.contains("ounass.qa") || strURL.contains("childrensalon.com")) { //Snatches by each website's individual photo format
    		urls = fetch.snatch(urlMess, "jpg");
    	} else if (strURL.contains("wiseboutique.com")) {
    		urls = fetch.snatch(urlMess, "JPG"); //Not sure if capitalization matters
    	} else if (strURL.contains("furla.com")) {
    		urls = fetch.snatch(urlMess, "png?scalemode=product");
    	}
    	
    	//4. Discriminate
    	System.out.printf("Before descrimination, size %d:\n", urls.size());
    	for (String url : urls) {
    		System.out.printf("\n%s\n", url);
    	}
    	ArrayList<String> urlList = discriminator(urls, strURL);
    	System.out.printf("After descrimination, size %d:\n", urls.size());
    	for (String url : urlList) {
    		System.out.printf("\n%s\n", url);
    	}
    	
    	//5. Save all surviving pics
    	String folderName = strURL;
    	folderName = folderName.replaceAll("[^a-zA-Z0-9\\.\\-]", "");
    	
    	for (String url : urls) {
    		downloadImage(url, strURL, folderName);
    	}
    	
    	/*//7. Repeat for colorTags using colorTagger
    	if (mode==0) {			//Prevents infinite colorTagging
    		colorTagger picasso = new colorTagger(strURL, doc);
    	}*/
    }
    
    public ArrayList<String> discriminator(ArrayList<String> urls, String strURL) {
    		
    	if (strURL.contains("ekseption.com")) {		//For ekseption URLs
    		for (int x = 0; x<urls.size(); x++) {
    			if (urls.get(x).contains("multicolor")) {
    				urls.remove(x--);
    			}
    		}
    	}
    	
    	if (strURL.contains("farfetch.com")) {		//For farfetch URLs
        	for (int x = 0; x<urls.size(); x++) {
        		if (!(urls.get(x).contains("_480"))) {
        			urls.remove(x--);
        		}
        	}
    	}
    
    	if (strURL.contains("loewe.com")) {			//For loewe URLs
    		for (int x = 0; x<urls.size(); x++) {
    			if (!(urls.get(x).contains("v2"))) {
    				urls.remove(x--);
    			}
    		}
    	}
    	
    	if (strURL.contains("gucci.com")) {			//For gucci URLs
    		for (int x = 0; x<urls.size(); x++) {
    			if (!(urls.get(x).contains("1200x1200"))) {
    				urls.remove(x--);
    			}
    		}
    	}
    		
    	if (strURL.contains("ysl.com")) {			//For ysl URLs
    		for (int x = 0; x<urls.size(); x++) {
    			if (!urls.get(x).contains("_14_")) {
    				urls.remove(x--);
    			}
    		}
    	}
    		
    	if (strURL.contains("furla.com")) {			//For furla URLs
    		for (int x = 0; x<urls.size(); x++) {
    			if (!urls.get(x).contains("2000x2000")) {
    				urls.remove(x--);
    			}
    		}
    	}
    		
    	if (strURL.contains("balenciaga.com")) {	//For balenciaga URLs
    		for (int x = 0; x<urls.size(); x++) {
    			if (!urls.get(x).contains("_14_")) {
    				urls.remove(x--);
    			}
    		}
    	}
    	
    	if (strURL.contains("buyma.com")) {			//For buyma URLs
    		for (int x = 0; x<urls.size(); x++) {
    			if (!urls.get(x).contains(strURL.substring(strURL.indexOf("item/")+5, strURL.length()-2)) || !urls.get(x).contains("428.jpg")) {
    					urls.remove(x--);
    			}
    		}
    	}
    	
    	if (strURL.contains("childsplayclothing.com")) {
    		
    	}
    	
    	if (strURL.contains("ounass.qa")) {
    		String itemID = strURL.substring(strURL.lastIndexOf("-")+1, strURL.lastIndexOf("_")); //Strips the product ID out of the URL for filtering
    		for (int x = 0; x<urls.size(); x++) {
    			if (!urls.get(x).contains(itemID) || !urls.get(x).contains("https://ounass") || urls.get(x).contains("\"") || urls.get(x).contains("Nav")) {
    				urls.remove(x--);
    			}
    		}
    	}
    	
    	if (strURL.contains("childrensalon.com")) {
    	}
    	
    	if (strURL.contains("wiseboutique.com")) {
    		for (int x = 0; x<urls.size(); x++) {
    			if (urls.get(x).contains("%")) {
    				urls.remove(x--);
    			}
    		}
    	}
    	
    	if (strURL.contains("theclutcher.com")) {
    		for (int x = 0; x<urls.size(); x++) {
    			if (!urls.get(x).contains("-")) {
    				urls.remove(x--);
    			}
    		}
    	}	
    	return urls;
    }
    
    private void downloadImage(String strImageURL, String strURL, String folderName){ 		//Very Website dependent
        folderName = folderName.substring(folderName.length()-8, folderName.length()) + directoriesCreated; //Fixes an issue where URLs make the folder+pic name longer than windows max path length
    	String strImageName = strImageURL.substring(strImageURL.lastIndexOf("/") + 1);     	//get file name from image path
        System.out.printf("Saving: %s, from: %s\n", strImageName, strImageURL);
        try {  																				//open the stream from strImageURL            
        	URLConnection urlConnection = new URL(strImageURL).openConnection();			//hybrid (allows request headers)
        	
        	urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0");
        	if (strImageURL.contains("ysl.com")) {											//Prevents issue with gucci pages where corrupt, empty images are saved
        		urlConnection.addRequestProperty("Accept-Encoding", "gzip, deflate, br");	//Scraper-blocker countermeasures
        	}

        	InputStream in = urlConnection.getInputStream();
            byte[] buffer = new byte[4096];
            int n = -1;
            strImageName = strImageName.replaceAll(".png", String.format("%d.png", this.getImagesSaved())).replaceAll(".jpg", String.format("%d.jpg", this.getImagesSaved())).replaceAll(".jpeg", String.format("%d.jpeg", this.getImagesSaved())); //adds number to file name
            if (strURL.contains("furla.com")) {												//WEBSITE DEPENDENT
            	strImageName = strImageName.replace("?", "").replace("scalemode=product", ""); //Prevents bad save names (ones w/ "?") from furla pages
            }
            //Creates folder if needed
            File directory = new File(IMAGE_DESTINATION_FOLDER + "/" + folderName + "/");
            //Pixture picasso = new Pixture(); <--for autocropping
            if (directory.exists() && directory.isDirectory()) {
            	
                OutputStream os = new FileOutputStream(IMAGE_DESTINATION_FOLDER + "/" + folderName + "/" + strImageName); 
                while ((n = in.read(buffer)) != -1 ){	//write bytes to the output stream
                	os.write(buffer, 0, n);	//writes the image content to file
                }
                os.close(); 				//closes the stream
        		imagesSaved++;
                System.out.printf("Image saved\n");
                
            } else {
                if(directory.mkdir()) {
                    System.out.printf("Directory Created: %s\n", directory.getAbsolutePath()); //Newcomer
                    OutputStream os = new FileOutputStream(IMAGE_DESTINATION_FOLDER + "/" + folderName + "/" + strImageName); 
                    while ((n = in.read(buffer)) != -1 ){	//write bytes to the output stream
                    	os.write(buffer, 0, n);	//writes the image content to file
                    }
                    os.close(); 				//closes the stream
            		imagesSaved++;
                    System.out.printf("Image saved\n");
                    
                } else {
                    System.out.println("Directory is not created, image not saved");
                }
            }
        } catch (IOException e) {
        	System.out.printf("\nDone fucked up: %s\n", strImageURL);
            e.printStackTrace();
        }
    }
	public int getImagesSaved() {
		return this.imagesSaved;
	}
	public int getTotalSpams() {
		return this.totalSpams;
	}
}


//Adding new site:
//1. Analyze item page (which image format? Add to step 3 line 53, add to discriminator and develop filter to keep only desired photos)
//2. Test page
//3. Add formatting if statement if needed
//Notes
//Java must be 9 or newer or you will get "Fatal alert: handshake_failure" when fetching the page