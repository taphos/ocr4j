package ee.ttu.ocr.test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ee.ttu.ocr.teaching.OCRTeachingCourse;
import ee.ttu.ocr.teaching.OCRTeachingException;

public class TestCourse {

	public static void main(String[] args) throws InterruptedException, OCRTeachingException {
		DebugFrame debugFrame = new DebugFrame();		
		for (OCRTeachingCourse course = new OCRTeachingCourse(); course.hasNextCharacter(); course.nextCharacter()) {
			List<BufferedImage> images = course.getCurrentCharacterImages();
            for (BufferedImage image : images) {
                debugFrame.setImage(image);
				debugFrame.repaint();
				Thread.sleep(100);
            }
		}
	}	
}
