package Scraper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ProductChecker {
	
	public static String path = "C:/buyma/checklist.txt"; //Path to the list of sites to check
	
	public ProductChecker() {
	}
	
	public Boolean check(String url, String[] lines1, String[] lines2) {
		System.out.println("Checking products...");
		boolean different = false;
		int x = 0;
		if (lines1.length == lines2.length){
			for (String line : lines1) {
				if (!line.equalsIgnoreCase(lines2[x++])) {
					different = true;
					System.out.println("Difference found!");
				}
			}
		} else {
			return true;
		}
		return different;
	}
	
}

/*
	public static void steps(int i) throws IOException {
		//Generate current date and time
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dateTime = dtf.format(now);
		String[] dateAndTime = dateTime.split(" ");
		String[] latest = {"0", "0", "0"};
		
		//Load latest entry
		boolean foundFolder = false; //For creating folders when necessary
		boolean madeFolder = false; //For annotating if a folder was created or not
		File library = new File("C:/Users/elija_000/Desktop/Dave_Tracker/"); //Library
		for (File currentFolder : library.listFiles()) { //Find latest one
			if (currentFolder.isDirectory()) { //Should be just folders here
				if (currentFolder.getName().matches(dateAndTime[0].replace("/", ""))) { //If true, today's folder already exists
					foundFolder = true;
					for (File currentFile : currentFolder.listFiles()) {
						String[] current = ((currentFile.getName()).replace(".txt", "").split("-"));
						if ((Integer.parseInt(latest[0]) < Integer.parseInt(current[0])) || (Integer.parseInt(latest[0]) == Integer.parseInt(current[0]) && Integer.parseInt(latest[1]) < Integer.parseInt(current[1])) || (Integer.parseInt(latest[0]) == Integer.parseInt(current[0]) && Integer.parseInt(latest[1]) == Integer.parseInt(current[1]) && Integer.parseInt(latest[2]) < Integer.parseInt(current[2]))) {
							latest = current;
						}
					}
					
				}
			}
		}
		if (!foundFolder) { //If today's folder not found, make it
			File dir = new File("C:/Users/elija_000/Desktop/Dave_Tracker/" + dateAndTime[0].replace("/", ""));
			try {
				Files.createDirectory(dir.toPath());
			} catch (NoSuchFileException e) {
				System.out.println(e.toString());
			}
			System.out.println("Directory created");
			madeFolder = true;
			Document doc = null;
			try {
				doc = Jsoup
					.connect(path)
					.userAgent("Mozilla/5.0")
					.maxBodySize(0)
					.timeout(10 * 10000)
					.get();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/elija_000/Desktop/Dave_Tracker/" + dateAndTime[0].replace("/",  "") + "/" + dateAndTime[1].replace(":", "-") + ".txt"));
			writer.write(doc.toString());
			writer.close();
		}
		if (!madeFolder && foundFolder) {
			File latestFile = new File("C:/Users/elija_000/Desktop/Dave_Tracker/" + dateAndTime[0].replace("/",  "") + "/" + latest[0] + "-" + latest[1] + "-" + latest[2] + ".txt");
			Document doc = null;
			try {
				doc = Jsoup
					.connect(path)
					.userAgent("Mozilla/5.0")
					.maxBodySize(0)
					.timeout(10 * 10000)
					.get();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			if (latestFile.exists()) {
				String[] doc1 = (doc.toString().replace("{", "").replace("}", "")).split("\\r?\\n");
				ArrayList<String> tempDoc2 = new ArrayList<String>();
				Scanner s = new Scanner(latestFile);
				while (s.hasNextLine()) {
					tempDoc2.add(s.nextLine().replace("{", "").replace("}", ""));
				}	
				s.close();
				String[] doc2 = Arrays.copyOf(tempDoc2.toArray(), tempDoc2.toArray().length, String[].class);
				if (checkIfDifferent(doc1, doc2)) { //Check if different
					System.out.printf("Difference detected at %s, %s\n", dateAndTime[0], dateAndTime[1]);
					BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/elija_000/Desktop/Dave_Tracker/" + dateAndTime[0].replace("/",  "") + "/" + dateAndTime[1].replace(":", "-") + ".txt"));
					writer.write(doc.toString());
					writer.close();
				} else {
					if (i%60 == 0) {
						System.out.printf("No difference detected at %s, %s. i=%d\n", dateAndTime[0], dateAndTime[1], i++);
					}
				}
			} else {
				File input = new File("C:\\Users\\elija_000\\Desktop\\Dave_Tracker\\" + dateAndTime[0].replace("/",  "") + "\\" + dateAndTime[1].replace(":", "-") + ".txt");
				PrintWriter writer = new PrintWriter(input, "UTF-8");
				writer.write(doc.html() ) ;
				writer.flush();
				writer.close();
			}
		}
	}
*/