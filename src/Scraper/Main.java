package Scraper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Main extends Application {
	@FXML
	private Label label;
	@FXML
	private AnchorPane anchorPane;
	@FXML 
	TabPane tabPane;
	@FXML
	private TextArea textArea;
	@FXML
	private Button extractButton;
	@FXML
	private TextField feedbackField;
	@FXML
	private ImageView iv1 = new ImageView();
	@FXML
	private Button collageButton;
	@FXML
	private DirectoryChooser pick = new DirectoryChooser();//might be wrong
	@FXML
	private TextField pickerField;
	@FXML
	private ImageView imagePreview;
	@FXML
	private Button removeButton;
	@FXML
	private ListView<String> listView;
	@FXML
	private Label label2;
	@FXML
	private Label label3;
	@FXML
	private Label label4;
	@FXML
	private ImageView imageViewCalc;
	@FXML
	private TextField price;
	@FXML
	private TextField perOff;
	@FXML
	private TextField result;
	@FXML
	Button calculateButton;
	@FXML
	public Button picsChooser;
	@FXML
	public Button addBordersBtn;
	@FXML
	public Button addBordersAllBtn;
	@FXML
	public Button autoCropBtn;
	@FXML
	public Label picsLabel;
	@FXML
	public Button save;
	@FXML
	public Button saveAll;
	@FXML
	public Stage thisStage;
	@FXML
	public Slider cropSlider;
	@FXML
	public Button cropXBtn;
	@FXML
	public Button cropYBtn;
	@FXML
	public ToggleButton jpgToggle;
	@FXML
	public ToggleButton alwaysOnTopToggle;
	@FXML
	public ToggleGroup ext;
	@FXML
	public Button resizeBtn;
	public Boolean alwaysOnTop = true;
	public ArrayList<Image> images = new ArrayList<Image>();
	
	
	@Override
	public void start(Stage primaryStage) throws IOException, FileNotFoundException {
		try {
			this.thisStage = primaryStage;
			FXMLLoader loader = new FXMLLoader();
			Parent content = loader.load(getClass().getResource("Butt.fxml"));
			Scene scene = new Scene(content);
			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();
			
			//dont work
			/*textArea.setOnKeyPressed(e -> {
			    if (e.getCode() == KeyCode.INSERT || e.getCode() == KeyCode.V) {
					textArea.appendText("\n");
			    }
			});*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void extract() throws FileNotFoundException, IOException {
		PicExtractor fetch = new PicExtractor();
		if (textArea.getText().length() != 0) {
			String textAreaFeed = textArea.getText();
			String[] urls = textAreaFeed.split("\\n+");
			for (String url : urls) {
				fetch.extract(url, 0);
			}
		}
		feedbackField.setText(String.format("%d saved, %d skipped.", fetch.getImagesSaved(), fetch.getTotalSpams()));
	}
	
	public void autoCrop() throws IOException {
		//Get selected index from listView
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		//Get selected URL from listView
		String url = listView.getSelectionModel().getSelectedItem().toString();
		//Crop it using Pixture
		Pixture picasso = new Pixture(images.get(selectedIndex), url);
		Image autoCroppedImage = (Image) picasso.autoCrop(1);
		
		//Replace the image in the preview area
		imagePreview.setImage(autoCroppedImage);
		
		//Replace the image in the ArrayList tracker
		images.set(selectedIndex, autoCroppedImage);
	}
	
	public void displayPreview() {
		if (listView.getSelectionModel().getSelectedItems().size() > 0) {
			//Get selected index from listView
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			//Image preview = new Image("file:///" + tempURL); If from a file
			imagePreview.setImage(images.get(selectedIndex));
		}
	}
	
	public void alwaysOnTopToggleAction() {
		try {
            alwaysOnTop = !alwaysOnTop; //Invert alwaysOnTop
            this.thisStage = (Stage) anchorPane.getScene().getWindow();
			this.thisStage.setAlwaysOnTop(alwaysOnTop);
            alwaysOnTopToggle.setSelected(alwaysOnTop); //Update button
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void removeListView() {
		if (!(listView.getItems().size() > 0)) {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			listView.getItems().remove(selectedIndex);
		}
		imagePreview.setImage(null);
	}
	
	public void picsChooserButtonAction() throws IOException { //Used for selecting images in the Pics tab
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\users\\" + System.getProperty("user.name") + "\\Documents"));
		List<File> targetList = fileChooser.showOpenMultipleDialog(this.thisStage);
		if (targetList != null) {
			images.clear(); //Clear images ArrayList
			if (!(listView.getItems() == null)) {
				listView.getItems().clear(); //Clear listView if not clear
			}
			for (File file : targetList) {
				String path = file.getAbsolutePath();
				if (path.contains(".png") || path.contains(".PNG")) { //If its a png
					Image image = new Image("file:///" + file.getAbsolutePath());
					images.add(image);
					imagePreview.setImage(image);
					listView.getItems().add(path);
					picsLabel.setText(String.format("Imported %s", path));
				} else if (path.contains(".jp") || path.contains(".JP")) { //If its a jpeg
					BufferedImage bufferedImage = ImageIO.read(file); //Read a jpeg from an inputFile
					String newPath = path.substring(0, path.indexOf(".")) + ".png"; //Create new path for .png version
					File newFile = new File(newPath); //Create new file for .png version
					ImageIO.write(bufferedImage, "png", newFile); //Write the bufferedImage back to new file as .png
					Image newImage = new Image("file:///" + newPath);
					images.add(newImage);
					imagePreview.setImage(newImage);
					listView.getItems().add(newPath);
					picsLabel.setText(String.format("Convert&Imported: %s", path));
				}
			}
		}
		if (images.size() > 0) {
			listView.getSelectionModel().select(0);
		}
	}
	
	public void resizeToLimit() {
		int limit = 600; //Could be changed to any value
		int index = listView.getSelectionModel().getSelectedIndex();
		BufferedImage selectedImage = SwingFXUtils.fromFXImage(images.get(index), null);	//Convert png in images ArrayList to BufferedImage
		
		double x = selectedImage.getWidth(); //Check dimensions
		double y = selectedImage.getHeight();
		
		//Check if we're shrinking or growing to the limit
		Boolean shrinking = false;
		Boolean growing = false;
		
		if (x>limit || y>limit) {
			shrinking = true;
		} else if (x<limit || y<limit) {
			growing = true;
		} else if (x==limit && y==limit) { //Else they're the same size
			feedbackField.setText("Already 600x600");
			return;
		}
		
		//Resize to limit
		if (shrinking) {
			if (x>y) {
				/*
				BufferedImage before = getBufferedImage(encoded);
				int w = before.getWidth();
				int h = before.getHeight();
				BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				AffineTransform at = new AffineTransform();
				at.scale(2.0, 2.0);
				AffineTransformOp scaleOp = 
				   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				after = scaleOp.filter(before, after)*/
				
				double shrinkFactor = (x-limit)/x; //Calculate shrink factor (% to shrink by)
				java.awt.Image resized = selectedImage.getScaledInstance(limit, (int)(y-(y*shrinkFactor)), java.awt.Image.SCALE_AREA_AVERAGING);
				Image resized2 = SwingFXUtils.toFXImage((ToolkitImage) resized, null);
				imagePreview.setImage(resized2); //Replace in preview window
				images.set(index, resized2); //Replace in Arraylist tracker
			} else if (y>x) {
				double shrinkFactor = (y-limit)/y;
				java.awt.Image resized = selectedImage.getScaledInstance((int)(x-(x*shrinkFactor)), limit, java.awt.Image.SCALE_AREA_AVERAGING);
				Image resized2 = SwingFXUtils.toFXImage((BufferedImage)resized, null);
				imagePreview.setImage(resized2);
				images.set(index, resized2);
			} else if (y==x) {
				double shrinkFactor = (y-limit)/y;
				java.awt.Image resized = selectedImage.getScaledInstance((int)(x-(x*shrinkFactor)), (int)(y-(y*shrinkFactor)), java.awt.Image.SCALE_AREA_AVERAGING);
				Image resized2 = SwingFXUtils.toFXImage((BufferedImage) resized, null);
				imagePreview.setImage(resized2);
				images.set(index, resized2);
			}
		} else if (growing) {
			if (x>y) {
				double growFactor = limit/x;
				java.awt.Image resized = selectedImage.getScaledInstance((int)(growFactor*x), (int)(growFactor*y), java.awt.Image.SCALE_SMOOTH);
				Image resized2 = SwingFXUtils.toFXImage((BufferedImage) resized, null);
				imagePreview.setImage(resized2);
				images.set(index, resized2);
			} else if (y>x || x==y) { //May need to separate later
				double growFactor = limit/y;
				java.awt.Image resized = selectedImage.getScaledInstance((int)(growFactor*x), (int)(growFactor*y), java.awt.Image.SCALE_SMOOTH);
				Image resized2 = SwingFXUtils.toFXImage((BufferedImage) resized, null);
				imagePreview.setImage(resized2);
				images.set(index, resized2);
			}
		}
		
		/*if (x>y) {
			double shrinkFactor = (x-limit)/x; //Calculate shrink factor (% to shrink by)
			Image resized = selectedImage.getScaledInstance(limit, (int)(y-y*shrinkFactor), Image.SCALE_);
			
			BufferedImage resizedBI = new BufferedImage(limit, (int)(y-(y*shrinkFactor)), BufferedImage.TYPE_INT_RGB); //create a blank, RGB, same width and height
			resizedBI.createGraphics().drawImage(selectedImage, 0, 0, Color.WHITE, null); //Draw a white background and put the originalImage on it.   
			Image resized = SwingFXUtils.toFXImage(resizedBI, null);
			imagePreview.setImage(resized);
		} else if (y>x) {
			double shrinkFactor = (y-limit)/y;
			BufferedImage resizedBI = new BufferedImage((int)(x-(x*shrinkFactor)), limit, BufferedImage.TYPE_INT_RGB); //create a blank, RGB, same width and height
			resizedBI.createGraphics().drawImage(selectedImage, 0, 0, Color.WHITE, null); //Draw a white background and put the originalImage on it.   
			Image resized = SwingFXUtils.toFXImage(resizedBI, null);
			imagePreview.setImage(resized);
			System.out.printf("%f, %f, %f, %f, %f\n", x, y, resized.getWidth(), resized.getHeight(), shrinkFactor);
		} else if (x==y) {
			BufferedImage resizedBI = new BufferedImage(limit, limit, BufferedImage.TYPE_INT_RGB); //create a blank, RGB, same width and height
			Image resized = SwingFXUtils.toFXImage(resizedBI, null);
			imagePreview.setImage(resized);
		}*/
	}
	
	public void addBorders() throws IOException {
		String url = listView.getSelectionModel().getSelectedItem().toString(); //Get the url from listView
		int selectedIndex = listView.getSelectionModel().getSelectedIndex(); //Get the index from listView (which image)
		Pixture pixture = new Pixture(images.get(selectedIndex), url); //Instantiate pixture so as to have it convert Image to BufferedImage
		//Replace the image in the ArrayList tracker
		images.set(selectedIndex, (Image)pixture.addBorders(1)); //Add the borders
		imagePreview.setImage(images.get(selectedIndex)); //Replace the image in the preview area
	}

	public void addBordersAll() throws IOException {
		List<String> urls = listView.getItems();
		int index = 0;
		for (Image pic : images) { //For each image
			Pixture pixture = new Pixture(pic, urls.get(index)); //Load into Pixture
			Image result = (Image)pixture.addBorders(1); //Add borders
			images.set(index, result);
			imagePreview.setImage(images.get(index++)); //Replace the image in the preview area
		}
		
	}
	
	public void save() { //Saves edited images from Pics tab. This needs to be rewritten
		String listViewURL = listView.getSelectionModel().getSelectedItem().toString();	//Get save location from listView (still says png)
		File destFile = new File(listViewURL); //Make a File, point it to that URL (says png/jpg now)
		int selectedIndex = listView.getSelectionModel().getSelectedIndex(); //Get image index from listView
		BufferedImage newVersionBImage = SwingFXUtils.fromFXImage(images.get(selectedIndex), null);	//Convert png in images ArrayList to BufferedImage
		try { //Try to save it
				ImageIO.write(newVersionBImage, "png", destFile); //Always save png or tools stop working
				if (jpgToggle.isSelected()) { //If jpg selected, convert to JPG BufferedImage by stripping out the PNG version's alpha data, and save
					BufferedImage newBufferedImage = new BufferedImage(newVersionBImage.getWidth(), newVersionBImage.getHeight(), BufferedImage.TYPE_INT_RGB); //create a blank, RGB, same width and height
			        newBufferedImage.createGraphics().drawImage(newVersionBImage, 0, 0, Color.WHITE, null); // draw a white background and put the originalImage on it.   
			        ImageIO.write(newBufferedImage, "jpg", new File(destFile.getAbsolutePath().replace("png", "jpg"))); //Save as jpg
			        picsLabel.setText(String.format("Saved PNG&JPG: %s/.jpg", destFile.getAbsolutePath().substring(destFile.getAbsolutePath().lastIndexOf("/")+1, destFile.getAbsolutePath().length())));
			        System.out.printf("Saved PNG&JPG: %s/.jpg\n", destFile.getAbsolutePath().substring(destFile.getAbsolutePath().lastIndexOf("/")+1, destFile.getAbsolutePath().length()));
				} else {
					picsLabel.setText(String.format("Saved PNG: %s", destFile.getAbsolutePath().substring(destFile.getAbsolutePath().lastIndexOf("/")+1, destFile.getAbsolutePath().length())));
					System.out.printf("Saved PNG: %s\n", destFile.getAbsolutePath().substring(destFile.getAbsolutePath().lastIndexOf("/")+1, destFile.getAbsolutePath().length()));
				}
		} catch (IOException e) {
			picsLabel.setText(String.format("Error, didn't save %s", destFile.getAbsolutePath()));
			System.out.printf("Error, didn't save %s: %s\n", destFile.getAbsolutePath(), e.toString());
		}
	}
	
	public void saveAll() { //Also needs to be rewritten
		//Get save locations from listView
		List<String> urls = listView.getItems();
		int count = 0; //Replaces selectedIndex from save()
		
		for (String url : urls) { //Iterate them
			File destFile = new File(url); 	//Make a File, point it to that url
			String format = ext.getSelectedToggle().toString(); //Catches selected format from ext ToggleGroup
			//Old line: String format = url.substring(url.lastIndexOf(".")+1, url.length()); //Extract format to a substring
			BufferedImage newVersionBImage = SwingFXUtils.fromFXImage(images.get(count++), null); //Convert to BufferedImage
			try { //Try to save it
				ImageIO.write(newVersionBImage, format, destFile);
				picsLabel.setText(String.format("Saved %s", destFile.getAbsolutePath()));
				System.out.printf("Saved %s\n", destFile.getAbsolutePath());
			} catch (IOException e) {
				picsLabel.setText(String.format("Error, didn't save %s", destFile.getAbsolutePath()));
				System.out.printf("Error, didn't save %s: %s\n", destFile.getAbsolutePath(), e.toString());
				throw new RuntimeException(e);
			}
		}
	}
	
	public void cropXBtnAction() throws IOException {
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		List<String> urls = listView.getItems();
		double cropVal = cropSlider.getValue();
		Pixture picasso = new Pixture(images.get(selectedIndex), urls.get(selectedIndex));
		images.set(selectedIndex, (Image)picasso.cropX(cropVal, 1));
		imagePreview.setImage(images.get(selectedIndex));
	}
	
	public void cropYBtnAction() throws IOException {
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		List<String> urls = listView.getItems();
		double cropVal = cropSlider.getValue();
		Pixture picasso = new Pixture(images.get(selectedIndex), urls.get(selectedIndex));
		images.set(selectedIndex, (Image)picasso.cropY(cropVal, 1));
		imagePreview.setImage(images.get(selectedIndex));
	}
	
	/*public void collage() throws IOException {
		List<String> picPaths = (List<String>) listView.getItems();
		List<BufferedImage> pics = new ArrayList<BufferedImage>();
		
		//build list of BufferedImages
		for (String picPath : picPaths) {
			File tempFile = new File("file:///" + picPath);
			BufferedImage tempPic = ImageIO.read(tempFile);
			pics.add(tempPic);
		}
		Pixture picasso = new Pixture();
		List<BufferedImage> result = picasso.createCollage(pics);
	}*/
	
	public void calculate() {
		String priceText = price.getText();
		String perOffText = perOff.getText();
		String resultText = result.getText();

		if (priceText.equals("") && !perOffText.equals("") && !resultText.equals("")) {
			double currentPerOff = Double.parseDouble(perOff.getText());
			double currentResult = Double.parseDouble(result.getText());
			double currentPrice = ((100-currentPerOff)/100)/currentResult;
			currentPrice = 1.00/currentPrice;
			price.setText(String.format("%.2f", currentPrice));
		} else if (!priceText.equals("") && perOffText.equals("") && !resultText.equals("")) {
			double currentPrice = Double.parseDouble(price.getText());
			double currentResult = Double.parseDouble(result.getText());
			double currentPerOff = (100*currentResult)/currentPrice;
			currentPerOff = 100-currentPerOff;
			perOff.setText(String.format("%.0f", currentPerOff));
		} else if (!priceText.equals("") && !perOffText.equals("") && resultText.equals("")) {
			double currentPrice = Double.parseDouble(price.getText());
			double currentPerOff = Double.parseDouble(perOff.getText());
			double currentResult = currentPrice*((100-currentPerOff)/100);
			result.setText(String.format("%.2f", currentResult));
		} else {
			result.setText("Please leave 1 empty, 2 filled");
		}
	}
}

//For a removed feature, saved because it shows how to recursively list folder contents w/ files
//for (final File fileEntry : targetFolder.listFiles()) {
//	if (fileEntry.isDirectory()) {
//		populateListView(fileEntry);
//	} else {
//		listView.getItems().add(fileEntry.getAbsolutePath());
//	}
//}