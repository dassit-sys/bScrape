
//To-do:
//Borders
//Resize problem
//Cannot save jpegs

package Scraper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Pixture {
	public int[][] pixels;
	public BufferedImage pic;
	public Image image;
	public String strURL;
	
	public Pixture (Image imageIn, String strURLIn) throws IOException {
		this.pic = ImageIO.read(new File(strURLIn)); //Next try SwingFXUtils.fromFXImage //(BufferedImage)imageIn; doesn't work with some types of images
		this.image = imageIn;
		this.pixels = convertTo2D(this.pic);
	    this.strURL = strURLIn;
	}
	
	public Pixture() {
		this.pic = null;
		this.pixels = null;
		this.strURL = null;
	}
	
	public Object cropX(double amt, int mode) {
		if (this.pic != null) {
			return cropX(this.pic, amt, mode);
		}
		return null;
	}
	
	public Object cropX(BufferedImage in, double amt, int mode) { //mode 0 returns a BufferedImage, anything else returns an Image
		int originalWidth = in.getWidth();
		int newWidth = (int) (originalWidth*((100-amt)*.01));
		int widthMargin = (originalWidth-newWidth)/2;
		BufferedImage newImage = in.getSubimage(widthMargin, 0, newWidth, in.getHeight());
		if (mode == 0) {
			return newImage;
		} else {
			return SwingFXUtils.toFXImage(newImage, null);
		}
	}
	
	public Object cropY(double amt, int mode) {
		if (this.pic != null) {
			return cropY(this.pic, amt, mode);
		}
		return null;
	}
	
	public Object cropY(BufferedImage in, double amt, int mode) { //mode 0 returns a BufferedImage, anything else returns an Image
		int originalHeight = in.getHeight();
		int newHeight = (int) (originalHeight*((100-amt)*.01));
		int heightMargin = (originalHeight-newHeight)/2;
		BufferedImage newImage = in.getSubimage(0, heightMargin, in.getWidth(), newHeight);
		if (mode == 0) {
			return newImage;
		} else {
			return SwingFXUtils.toFXImage(newImage, null);
		}
	}
	
	public Object addBorders(int mode) {
		if (this.pic != null) {
			return addBorders(this.pic, mode);
		}
		return null;
	}
	
	public Object addBorders(BufferedImage in, int mode) { //Mode 0 returns a BufferedImage, anything else returns an Image
		int originalWidth = in.getWidth(); //Might not be needed?
		int originalHeight = in.getHeight();
		int newWidth = 600 + 36 + 36;
		int newHeight = 600 + 36 + 36;
		
		//Create an empty image
		BufferedImage result = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	
		//Paint the borders, top and bottom first
		for (int y = 0; y < 12; y++) {
			for (int x = 0; x < newWidth; x++) { //1st black stripe
				System.out.printf("Setting: %d, %d\n", x, y);
				result.setRGB(x, y, Color.BLACK.getRGB()); //Top row
				result.setRGB(x, newHeight-y-1, Color.BLACK.getRGB()); //Bottom row
			}
			for (int x = 12; x < newWidth-12; x++) { //White stripe
				System.out.printf("Setting: %d, %d\n", x+12, y+12);
				result.setRGB(x, y+12, Color.WHITE.getRGB()); //Top row
				result.setRGB(x, newHeight-12-y-1, Color.WHITE.getRGB()); //Bottom row
			}
			for (int x = 12+12; x < newWidth-12-12; x++) { //2nd black stripe
				System.out.printf("Setting: %d, %d\n", x, y+12+12);
				result.setRGB(x, y+12+12, Color.BLACK.getRGB()); //Top row
				result.setRGB(x, newHeight-12-12-y-1, Color.BLACK.getRGB()); //Bottom row
			}
		}
		
		//Left and right
		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < newHeight; y++) { //1st black stripe
				result.setRGB(x, y, Color.BLACK.getRGB()); //Left row
				result.setRGB(newWidth-x-1, y, Color.BLACK.getRGB()); //Right row
			}
			
			for (int y = 12; y < newHeight-12; y++) { //White stripe
				result.setRGB(x+12, y, Color.WHITE.getRGB()); //Left row
				result.setRGB(newWidth-x-12-1, y, Color.WHITE.getRGB()); //Right row
			}
			
			for (int y = 12+12; y < newHeight-12-12; y++) { //2nd black stripe
				result.setRGB(x+12+12, y, Color.BLACK.getRGB()); //Left row
				result.setRGB(newWidth-x-12-12-1, y, Color.BLACK.getRGB()); //Right row
			}
		}
		
		//Paste the original image in the middle
		Graphics2D g = result.createGraphics();
		g.drawImage(in, 36, 36, 600, 600, null);
				
		if (mode == 0) {
			return result;
		} else {
			return SwingFXUtils.toFXImage(result, null);
		}
	}
	
	public Object autoCrop(int mode) {
		if (this.pic != null) {
			return autoCrop(this.pic, mode);
		}
		return null;
	}
	
	public Object autoCrop(BufferedImage in, int mode) { //mode 0 returns a BufferedImage, anything else returns an Image
		//All coordinates are "cut-at", meaning its safe to cut there and not lose any important pixels
		int newX = -99;
		int newY = -99;
		int newHeight = -99;
		int newWidth = -99;
		int[][] picture = convertTo2D(in);
		int bgColorTotal = picture[0][0];	//First top-left pixel is assumed to be the background color. True for most photos tested
		int bgColorBlue = bgColorTotal & 0xff;
		int bgColorGreen = (bgColorTotal & 0xff00) >> 8;
		int bgColorRed = (bgColorTotal & 0xff0000) >> 16;
		
		for (int x = 0; x < picture.length; x++) {
			for (int y = 0; y < picture[x].length; y++) {
				int color = picture[x][y];
				int blue = color & 0xff;
				int green = (color & 0xff00) >> 8;
				int red = (color & 0xff0000) >> 16;
				System.out.printf("value: %d, blue: %d, green: %d, red: %d\n", picture[x][y], blue, green, red);
			}
		}
		
		for (int x = 0; x < picture.length; x++) { //Finds top
			for (int y = 0; y < picture[x].length; y++) {
				int currentColor = picture[x][y];
				int currentBlue = currentColor & 0xff;
				int currentGreen = (currentColor & 0xff00) >> 8;
				int currentRed = (currentColor & 0xff0000) >> 16;
				if ((currentGreen-bgColorGreen>20 || bgColorGreen-currentGreen>20) || (currentBlue-bgColorBlue>20 || bgColorBlue-currentBlue>20) || (currentRed-bgColorRed>20 || bgColorRed-currentRed>20)) {
					newY = x;
					break;
				}
			}
			if (newY != -99) {
				break;
			}
		}
		for (int x = picture.length-1; x >= 0; x--) { //Finds bottom
			for (int y = picture[x].length-1; y >= 0; y--) {
				int currentColor = picture[x][y];
				int currentBlue = currentColor & 0xff;
				int currentGreen = (currentColor & 0xff00) >> 8;
				int currentRed = (currentColor & 0xff0000) >> 16;
				if ((currentGreen-bgColorGreen>20 || bgColorGreen-currentGreen>20) || (currentBlue-bgColorBlue>20 || bgColorBlue-currentBlue>20) || (currentRed-bgColorRed>20 || bgColorRed-currentRed>20)) {
					newHeight = picture.length - newY - (picture.length-x);
					break;
				}
			}
			if (newHeight != -99) {
				break;
			}
		}
		for (int x = 0; x < picture[0].length; x++) { //Finds left
			for (int y = 0; y < picture.length; y++) {
				int currentColor = picture[y][x];
				int currentBlue = currentColor & 0xff;
				int currentGreen = (currentColor & 0xff00) >> 8;
				int currentRed = (currentColor & 0xff0000) >> 16;
				if ((currentGreen-bgColorGreen>20 || bgColorGreen-currentGreen>20) || (currentBlue-bgColorBlue>20 || bgColorBlue-currentBlue>20) || (currentRed-bgColorRed>20 || bgColorRed-currentRed>20)) {
					newX = x;
					break;
				}
			}
			if (newX != -99) {
				break;
			}
		}
		for (int x = picture[0].length-1; x >= 0; x--) { //Finds right
			for (int y = picture.length-1; y >= 0; y--) {
				int currentColor = picture[y][x];
				int currentBlue = currentColor & 0xff;
				int currentGreen = (currentColor & 0xff00) >> 8;
				int currentRed = (currentColor & 0xff0000) >> 16;
				if ((currentGreen-bgColorGreen>20 || bgColorGreen-currentGreen>20) || (currentBlue-bgColorBlue>20 || bgColorBlue-currentBlue>20) || (currentRed-bgColorRed>20 || bgColorRed-currentRed>20)) {
					newWidth = picture[0].length - newX - (picture[0].length-x)+1;
					break;
				}
			}
			if (newWidth != -99) {
				break;
			}
		}
		System.out.printf("newY: %d, newHeight: %d, newX: %d, newWidth: %d\n", newY, newHeight, newX, newWidth);
		BufferedImage newImage = in.getSubimage(newX, newY, newWidth, newHeight);
		if (mode == 0) {
			return newImage;
		} else {
			return SwingFXUtils.toFXImage(newImage, null);
		}
	}
	
	public String getBgColor(BufferedImage image) {
		String result = "";
		//get bg color
		return result;
	}
	
	
	  public static int[][] convertTo2D(BufferedImage image) {  
		  final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();	  
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

	      int[][] result = new int[height][width];
	      if (hasAlphaChannel) {
	         final int pixelLength = 4;
	         for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
	            argb += ((int) pixels[pixel + 1] & 0xff); // blue
	            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      } else {
	         final int pixelLength = 3;
	         for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      }
	      return result;
	   }
	  
		/*public List<BufferedImage> createCollage(List<BufferedImage> pics) {
		BufferedImage result;
		int numPics = pics.size();
		switch (numPics) {
		case 1: break;
		case 2: break;
		case 3: break;
		case 4: break;
		case 5: break;
		case 6: break;
		case 7: break;
		}
		
		//create the collage
		return result;
	}*/
}