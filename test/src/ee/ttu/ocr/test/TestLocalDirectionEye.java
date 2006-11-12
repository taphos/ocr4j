package ee.ttu.ocr.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ee.ttu.ocr.ImageClusterer;
import ee.ttu.ocr.LocalContourDirectionEye;
import ee.ttu.ocr.RandomReceptorEye;
import ee.ttu.ocr.teaching.OCRTeachingCourse;
import ee.ttu.ocr.teaching.OCRTeachingException;

public class TestLocalDirectionEye {
	
	public static void main(String[] args) throws OCRTeachingException, InterruptedException {
		DebugFrame debugFrame = new DebugFrame();
		
		LocalContourDirectionEye eye = new LocalContourDirectionEye(3, 8, -0.15f);
		System.out.println(eye.getReceptorsCount());
		
		BufferedImage image = new BufferedImage(850, 400, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D graphics = image.createGraphics();		
		graphics.setBackground(Color.WHITE);
		graphics.setColor(Color.BLACK);	
		
		graphics.clearRect(0,0,850,400);
		graphics.setFont(new Font("Tahoma", Font.PLAIN, 400));
		graphics.drawString("AOK", 50, 350);
        debugFrame.setImage(image);
		debugFrame.repaint();

        ImageClusterer clusterer = new ImageClusterer(image);
		eye.lookAt(clusterer.nextCluster().getImage());
        eye.lookAt(clusterer.nextCluster().getImage());
        eye.lookAt(clusterer.nextCluster().getImage());

        debugFrame.setImage(image);
		debugFrame.repaint();
	}

}
