package ee.ttu.ocr.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ee.ttu.ocr.Cluster;
import ee.ttu.ocr.ImageClusterer;

public class ClustererTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DebugFrame debugFrame = new DebugFrame();
		File f = new File("training_data/Z.png");
		BufferedImage image = ImageIO.read(f);
		debugFrame.setImage(image);
		for (ImageClusterer clusterer = new ImageClusterer(image); clusterer.hasMoreClusters();) {
			Cluster cluster = clusterer.nextCluster();
			debugFrame.repaint();
		}
	}

}
