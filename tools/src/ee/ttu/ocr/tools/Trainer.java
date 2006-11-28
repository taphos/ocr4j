package ee.ttu.ocr.tools;

import ee.ttu.ann.learning.BackPropagationLearningNetwork;
import ee.ttu.math.BipolarSigmoidFunction;
import ee.ttu.ocr.OCRSerializer;
import ee.ttu.ocr.RandomReceptorEye;
import ee.ttu.ocr.teaching.OCRTeacher;
import ee.ttu.ocr.teaching.OCRTeachingCourse;
import ee.ttu.ocr.teaching.OCRTeachingException;
import ee.ttu.ocr.teaching.Statistics;

import java.io.IOException;
import java.util.Arrays;

public class Trainer {

	public static void main(String[] args) throws IOException, OCRTeachingException {						
		
		System.out.println("Generating training course");
		OCRTeachingCourse course = new OCRTeachingCourse();
		final char[] alphabet = course.getAlphabet();
		
		System.out.println("Initializing eye");
		//LocalContourDirectionEye eye = new LocalContourDirectionEye(3, 9, 0f);
		RandomReceptorEye eye = new RandomReceptorEye(400);
        eye.optimize(course, 0.39f);

		System.out.println("Number of eye receptors: "+eye.getReceptorsCount());
		
		System.out.println("Creating neural network");
		BackPropagationLearningNetwork network = new BackPropagationLearningNetwork(new BipolarSigmoidFunction(2), eye.getReceptorsCount());
		network.addNetworkLayer(100);
        network.addNetworkLayer(60);
        network.addNetworkLayer(course.getAlphabetSize());

        /*OCRSerializer serializer2 = new OCRSerializer("random_receptor_bipolar_sigmoid.ann");
        serializer2.read();
        LearningNeuralNetwork network = (LearningNeuralNetwork)serializer2.getNetwork();
        Eye eye = serializer2.getEye();
        System.out.println("Number of eye receptors: "+eye.getReceptorsCount());*/


        OCRTeacher teacher = new OCRTeacher(course, eye);
		
		Statistics statistics = new Statistics() {
			public boolean continueTeaching(int iteration, float error) {
				System.out.println(iteration+"\t"+error);
				try {
					 return System.in.available() == 0;
				} catch (IOException e) {}
				return false;
			}			
			
			public void addExaminationResponse(float[] correctAnswer, float[] answer) {
				int maxIndex = 0;
				float max = 0;
				for (int j=0; j<answer.length; j++) {
					if (answer[j] > max) {
						max = answer[j]; maxIndex=j;
					}
				}
				int correctMaxIndex = 0;
				max = 0;
				for (int j=0; j<correctAnswer.length; j++) {
					if (correctAnswer[j] > max) {
						max = correctAnswer[j]; correctMaxIndex=j;
					}
				}
				if (correctMaxIndex != maxIndex) {
					System.out.print(alphabet[correctMaxIndex]+" : "+alphabet[maxIndex]+ " ("+answer[correctMaxIndex]+" : "+answer[maxIndex]+")");
				}
				else {
					System.out.print(alphabet[correctMaxIndex]+" ("+answer[correctMaxIndex]+")");
				}
				Arrays.sort(answer);
				System.out.println("\t"+answer[answer.length-1]+"\t"+answer[answer.length-2]+"\t"+answer[answer.length-3]);
			}
		};
		
		System.out.println("Teaching neural network");
		teacher.teach(network, statistics);
		
		System.out.println("Examining network");
		teacher.examine(network, statistics);						
		
		System.out.println("Saving network to file");
		OCRSerializer serializer = new OCRSerializer("OCRBrain.ann");
		serializer.setAlphabet(alphabet);
		serializer.setEye(eye);
		serializer.setNetwork(network);
		serializer.write();		
	}			
}
