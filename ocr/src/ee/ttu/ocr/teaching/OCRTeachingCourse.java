package ee.ttu.ocr.teaching;

import ee.ttu.ocr.Cluster;
import ee.ttu.ocr.ImageClusterer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a holder of character images, user for teaching neural networks.
 * @author Filipp Keks
 */
public class OCRTeachingCourse {
	
	private char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	
	private static final String IMAGE_PATH = "training_data/";
	private static final String IMAGE_FILE_SUFFIX = ".png";

	private static final int IMAGE_WIDTH = 200;
	private static final int IMAGE_HEIGHT = 200;
	private static final String[] fonts = {"Tahoma", "TimesRoman"};
	private static final int[] fontSizes = {150, 30, 13, 12};
	private static final int[] fontStyles = {Font.PLAIN, Font.ITALIC};

    private int currentCharacterIndex;
    
    public OCRTeachingCourse() {
    }

    public OCRTeachingCourse(char[] alphabet) {
        this.alphabet = alphabet;
    }
	
    public void reset() {
		currentCharacterIndex = 0;
	}
	
	public char[] getAlphabet() {
		return alphabet;
	}
	
	public boolean hasNextCharacter() {
		return currentCharacterIndex < alphabet.length;
	}
	
	public char nextCharacter() {
		char character =  alphabet[currentCharacterIndex];
		currentCharacterIndex++;
		return character;
	}
	
	public int getCurrentCharacterIndex() {
		return currentCharacterIndex;
	}
	
	public int getAlphabetSize() {
		return alphabet.length;
	}

    public List<BufferedImage> getCurrentCharacterImages() throws OCRTeachingException {
		List<BufferedImage> images = new ArrayList<BufferedImage>();

        // Generate images from fonts
        for (String font : fonts) {
            for (int fontSize : fontSizes) {
                for (int fontStyle : fontStyles) {
                    BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
                    Graphics2D graphics = image.createGraphics();
                    graphics.setBackground(Color.WHITE);
                    graphics.setColor(Color.BLACK);
                    graphics.clearRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                    graphics.setFont(new Font(font, fontStyle, fontSize));
                    graphics.drawString(Character.toString(alphabet[currentCharacterIndex]), 20, IMAGE_HEIGHT - 40);
                    images.add(new ImageClusterer(image).nextCluster().getImage());
                    image.flush();
                }
            }
        }
		
        // Get images from files        
        try {
            File f = new File(IMAGE_PATH + alphabet[currentCharacterIndex] + IMAGE_FILE_SUFFIX);
            for (ImageClusterer clusterer = new ImageClusterer(ImageIO.read(f)); clusterer.hasMoreClusters();) {
                Cluster cluster = clusterer.nextCluster();
                if (!cluster.isLineBreak() && !cluster.isSpace()) {
                    images.add(cluster.getImage());
                }
            }
        } catch (IOException e) {
            throw new OCRTeachingException(e);
        }
        return images;
	}	
}
