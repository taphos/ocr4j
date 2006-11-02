package ee.ttu.ocr.teaching;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ee.ttu.ann.learning.LearningNeuralNetwork;
import ee.ttu.ann.learning.Pattern;
import ee.ttu.ocr.Eye;

/**
 * Neural network teacher
 * This class is responsible for teaching network to recognise characters, srecified in {@link OCRTeachingCourse}
 * @author Filipp Keks
 */
public class OCRTeacher { 		
	
	private final static float LEARNING_RATE = 1f;
		
	private Eye eye;	
	private OCRTeachingCourse course;
	private List<Pattern> patterns;
	
	public OCRTeacher(OCRTeachingCourse course, Eye eye) {
		this.course = course;
		this.eye = eye; 
	}
	
	/**
	 * Theach the network
	 * @param network
	 * @throws OCRTeachingException 
	 */
	public void teach(LearningNeuralNetwork network, Statistics statistics) throws OCRTeachingException {
		patterns = createPatterns(network.getMaxOutputValue(), network.getMinOutputValue());
		int i=0;
		float error;
		do {			
			error = 0;
            for (Pattern pattern : patterns) {
                error += network.learn(pattern, LEARNING_RATE);
            }
            i++;
		} while (statistics.continueTeaching(i, error/patterns.size()));		
	}
	
	private List<Pattern> createPatterns(float matchValue, float unmatchValue) throws OCRTeachingException {
		List<Pattern> patterns = new ArrayList<Pattern>(course.getAlphabetSize());
				
		for (course.reset(); course.hasNextCharacter(); course.nextCharacter()) {
			float[] output = new float[course.getAlphabetSize()];
			Arrays.fill(output, unmatchValue);
			output[course.getCurrentCharacterIndex()] = matchValue;

			List<BufferedImage> images = course.getCurrentCharacterImages();
            for (BufferedImage image : images) {
                patterns.add(new Pattern(eye.lookAt(image), output));
            }
		}

		return patterns;
	}
	
	/**
	 * Check, how good network knows the course
	 * Examine network and add info to the statistics
	 * Used for debugging 
	 * @param network
	 * @param statistics
	 */
	public void examine(LearningNeuralNetwork network, Statistics statistics) {
        for (Pattern pattern : patterns) {
            statistics.addExaminationResponse(pattern.getOutputs(), network.process(pattern.getInputs()));
        }
	}
}
