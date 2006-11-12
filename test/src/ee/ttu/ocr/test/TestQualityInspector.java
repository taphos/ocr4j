package ee.ttu.ocr.test;

import ee.ttu.ocr.RandomReceptorEye;
import ee.ttu.ocr.teaching.EyeQualityInspector;
import ee.ttu.ocr.teaching.OCRTeachingCourse;
import ee.ttu.ocr.teaching.OCRTeachingException;

import java.io.IOException;

public class TestQualityInspector {
	public static void main(String[] args) throws IOException, OCRTeachingException {
		OCRTeachingCourse course = new OCRTeachingCourse();
		
		//SquareRegionDensityEye eye = new SquareRegionDensityEye(2, 9, -0.1f);
		//LocalContourDirectionEye eye = new LocalContourDirectionEye(3, 9, -0.15f);
		RandomReceptorEye eye = new RandomReceptorEye(500);
		eye.optimize(course, 0.4f);
		System.out.println(EyeQualityInspector.getEyeUsability(eye, course));
		System.out.println(eye.getReceptorsCount());
	}
}
