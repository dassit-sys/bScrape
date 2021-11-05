package Scraper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Test {
	public static void main(String[] args) throws IOException {
		Pixture picasso = new Pixture();
		//plug in test photo here
		File tempFile = new File("C:\\buyma\\untitled.png");
		BufferedImage tempPic = ImageIO.read(tempFile);
		int[][] picture = picasso.convertTo2D(tempPic);
		
		//for (int x = 0; x < picture.length; x++) {
		//	for (int y = 0; y < picture[x].length; y++) {
		//		int color = picture[x][y];
		//		int blue = color & 0xff;
		//		int green = (color & 0xff00) >> 8;
		//		int red = (color & 0xff0000) >> 16;
		//		System.out.printf("value: %d, blue: %d, green: %d, red: %d\n", picture[x][y], blue, green, red);
		//	}
		//}
		
	}
}
