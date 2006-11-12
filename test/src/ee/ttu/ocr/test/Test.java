package ee.ttu.ocr.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ee.ttu.math.BipolarSigmoidFunction;
import ee.ttu.math.HyperbolicTangentFunction;
import ee.ttu.math.MathEx;
import ee.ttu.ann.NeuralNetwork;
import ee.ttu.math.SigmoidFunction;
import ee.ttu.ann.learning.BackPropagationLearningNetwork;
import ee.ttu.ann.learning.Pattern;
import ee.ttu.ocr.OCR;
import ee.ttu.ocr.OCRSerializer;

public class Test {		

	public static void main(String[] args) throws IOException {				
											
		OCR ocr = new OCR("ocr.brain");		
		
		DebugFrame debugFrame = new DebugFrame();
		BufferedImage image = new BufferedImage(280, 150, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D graphics = image.createGraphics();		
		graphics.setBackground(Color.WHITE);
		graphics.setColor(Color.BLACK);		
		graphics.clearRect(0,0,280,150);
		
		graphics.setFont(new Font("Tahoma", Font.PLAIN, 50));
		graphics.drawString("HELLO", 50, 70);
		/*graphics.drawString("E", 90, 80);
		graphics.drawString("L", 120, 67);
		graphics.drawString("L", 155, 77);
		graphics.drawString("O", 190, 70);*/
		
		graphics.setFont(new Font("Tahoma", Font.PLAIN, 13));
		graphics.drawString("CRUEL WORLD", 30, 130);
		
		
		debugFrame.setImage(image);
		debugFrame.repaint();

		System.out.println(ocr.recognise(image));

		/*ImageClusterer clusterer = new ImageClusterer(image);
		BufferedImage newImage = clusterer.nextCluster().getImage();
		Eye eye = new LocalContourDirectionEye(4, 0.5f);
		float[] result = eye.lookAt(newImage);
		for (int i = 0; i<result.length; i++)				
			System.out.print(new DecimalFormat("0.00").format(result[i])+"\t");
		System.out.println();
		
		newImage = clusterer.nextCluster().getImage();
		eye = new LocalContourDirectionEye(4, 0f);
		result = eye.lookAt(newImage);
		for (int i = 0; i<result.length; i++)
			System.out.print(new DecimalFormat("0.00").format(result[i])+"\t");
		System.out.println();
		
		debugFrame.setImage(newImage);
		debugFrame.repaint();
		
		for (ImageClusterer clusterer = new ImageClusterer(image); clusterer.hasMoreClusters();) {
			//debugFrame.setImage(clusterer.nextCluster().getImage());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
			Cluster cluster = clusterer.nextCluster();
			if (cluster.isSpace()) System.out.print(" ");
			if (cluster.isLineBreak()) System.out.print("\n");
			if (cluster.getImage()!=null) System.out.print("A");
			debugFrame.repaint();
		}*/
		
		//System.out.println(new OCR(network, eye, alphabet).recognise(image));
	}		
}
