package ee.ttu.ocr.teaching;

import ee.ttu.math.MathEx;
import ee.ttu.ocr.Eye;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * This can bu used for statistics or eye algorithm optimization
 * 
 * @author Filipp Keks
 */
public class EyeQualityInspector {
	
	private static int characterImagesCount;
		
	/**
	 * Measure a relative usability of the eye for recognizing the given course
	 * Usability measurement algorithm is based on entropy of eye receptor values.
	 *  
	 * @param eye
	 * @param course
	 * @return average usability of eye receptors
	 * @throws OCRTeachingException 
	 */
	public static float getEyeUsability(Eye eye, OCRTeachingCourse course) throws OCRTeachingException {
		Map<Character, List<float[]>> eyeReceptorValues = getEyeReceptorValues(eye, course);
		float sumUsability = 0;		
		for(int receptorIndex = 0; receptorIndex < eye.getReceptorsCount(); receptorIndex++) {							
			float avgInnerEntropy = getReceptorInnerEntropy(eyeReceptorValues, receptorIndex, eye.getMaxReceptorValue(), eye.getMinReceptorValue());
			float avgOuterEntropy = getReceptorOuterEntropy(eyeReceptorValues, receptorIndex, eye.getMaxReceptorValue(), eye.getMinReceptorValue());            
            sumUsability += avgOuterEntropy * (1-avgInnerEntropy);
		}		
		return sumUsability/eye.getReceptorsCount();
	}

	/**
	 * Measure a relative usability of the eye for recognizing the given course
	 * Usability measurement algorithm is based on entropy of eye receptor values.
	 *  
	 * @param eye
	 * @param course
	 * @return array of receptor usabilities
	 * @throws OCRTeachingException 
	 */
	public static float[] getEyeReceptorUsabilities(Eye eye, OCRTeachingCourse course) throws OCRTeachingException {
		Map<Character, List<float[]>> eyeReceptorValues = getEyeReceptorValues(eye, course);        
        float[] result = new float[eye.getReceptorsCount()];
		for(int receptorIndex = 0; receptorIndex < eye.getReceptorsCount(); receptorIndex++) {							
			float avgInnerEntropy = getReceptorInnerEntropy(eyeReceptorValues, receptorIndex, eye.getMaxReceptorValue(), eye.getMinReceptorValue());
			float avgOuterEntropy = getReceptorOuterEntropy(eyeReceptorValues, receptorIndex, eye.getMaxReceptorValue(), eye.getMinReceptorValue());
			result[receptorIndex] = avgOuterEntropy * (1-avgInnerEntropy);
		}		
		return result;
	}

	private static float getReceptorOuterEntropy(Map<Character, List<float[]>> eyeReceptorValues, int receptorIndex, float max, float min) {
		float sumOuterEntropy = 0;
		for (int i=0; i< characterImagesCount; i++) {
			float[] values = new float[eyeReceptorValues.size()];
			int j=0;
			for (Iterator<Character> it = eyeReceptorValues.keySet().iterator(); it.hasNext(); j++) {
				values[j] = eyeReceptorValues.get(it.next()).get(i)[receptorIndex];				
			}
			sumOuterEntropy += MathEx.correlativeEntropy((max-min)/5, values);
		}
        return sumOuterEntropy/characterImagesCount;
	}

	private static float getReceptorInnerEntropy(Map<Character, List<float[]>> eyeReceptorValues, int receptorIndex, float max, float min) {
		characterImagesCount = 999;
		float sumInnerEntropy = 0;
        for (Character character : eyeReceptorValues.keySet()) {
            List<float[]> receptorValues = eyeReceptorValues.get(character);
            if (characterImagesCount > receptorValues.size()) {
                characterImagesCount = receptorValues.size();
            }
            float[] values = new float[receptorValues.size()];
            int i = 0;
            for (ListIterator<float[]> iterator = receptorValues.listIterator(); iterator.hasNext(); i++) {
                values[i] = iterator.next()[receptorIndex];
            }
            sumInnerEntropy += MathEx.correlativeEntropy((max-min)/5, values);
        }
        return sumInnerEntropy/eyeReceptorValues.size();
	}

	private static Map<Character, List<float[]>> getEyeReceptorValues(Eye eye, OCRTeachingCourse course) throws OCRTeachingException {
		Map<Character, List<float[]>> eyeReceptorValues = new HashMap<Character, List<float[]>>();
		char currentCharacter = 0;
		for (course.reset(); course.hasNextCharacter(); currentCharacter = course.nextCharacter()) {
			List<BufferedImage> images = course.getCurrentCharacterImages();
			List<float[]> values = new ArrayList<float[]>(images.size());
            for (BufferedImage image : images) {
                values.add(eye.lookAt(image));
            }
			eyeReceptorValues.put(currentCharacter, values);
		}
		return eyeReceptorValues;
	}

}
