package ee.ttu.ocr.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import ee.ttu.ocr.ImageClusterer;
import ee.ttu.ocr.RandomReceptorEye;
import ee.ttu.ocr.teaching.OCRTeachingCourse;
import ee.ttu.ocr.teaching.OCRTeachingException;

public class TestRandomReceptorEye {

	public static void main(String[] args) throws OCRTeachingException, InterruptedException {
		DebugFrame debugFrame = new DebugFrame();
		
		OCRTeachingCourse course = new OCRTeachingCourse();
		RandomReceptorEye eye = new RandomReceptorEye(250);
		System.out.println("optimizing");
		eye.optimize(course, 0.2f);
		System.out.println(eye.getReceptorsCount());
		
/*		BufferedImage image = new BufferedImage(280, 150, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D graphics = image.createGraphics();		
		graphics.setBackground(Color.WHITE);
		graphics.setColor(Color.BLACK);	
		
		graphics.clearRect(0,0,280,150);		
		graphics.setFont(new Font("Tahoma", Font.PLAIN, 150));
		graphics.drawString("A", 50, 145);		
		ImageClusterer clusterer = new ImageClusterer(image);
		System.out.println(Arrays.toString(eye.lookAt(clusterer.nextCluster().getImage())));
				
		graphics.clearRect(0,0,280,150);		
		graphics.setFont(new Font("Tahoma", Font.PLAIN, 30));
		graphics.drawString("A", 50, 70);		
		clusterer = new ImageClusterer(image);
		System.out.println(Arrays.toString(eye.lookAt(clusterer.nextCluster().getImage())));*/
		//debugFrame.setImage(clusterer.nextCluster().getImage());
				
		eye.drawReceptors(debugFrame.getImage());
		debugFrame.repaint();
	}

}
