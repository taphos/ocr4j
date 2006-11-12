package ee.ttu.ocr;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ee.ttu.ann.NeuralNetwork;

public class OCR {
	
	private final static String SPACE = " ";
	private final static String LINE_BREAK = "\n";
	
	private NeuralNetwork network;
	private Eye eye;
	private char[] alphabet;
	
	public OCR(NeuralNetwork network, Eye eye, char[] alphabet) {
		this.network = network;
		this.eye = eye;
		this.alphabet = alphabet; 
	}
	
	public OCR(OCRSerializer serializer) {
		this.network = serializer.getNetwork();
		this.eye = serializer.getEye();
		this.alphabet = serializer.getAlphabet(); 
	}
	
	public OCR(String fileName) throws IOException {
		OCRSerializer serializer = new OCRSerializer(fileName);
		serializer.read();
		this.network = serializer.getNetwork();
		this.eye = serializer.getEye();
		this.alphabet = serializer.getAlphabet(); 
	}

    public OCR() throws IOException {
        OCRSerializer serializer = new OCRSerializer();
		serializer.read();
		this.network = serializer.getNetwork();
		this.eye = serializer.getEye();
		this.alphabet = serializer.getAlphabet();
    }

    public String recognise(BufferedImage image) {
		StringBuffer result = new StringBuffer();
		for (ImageClusterer clusterer = new ImageClusterer(image); clusterer.hasMoreClusters();) {
			Cluster cluster = clusterer.nextCluster();
			if (cluster.isSpace()) {
				result.append(SPACE);
			} 
			else if (cluster.isLineBreak()) {
				result.append(LINE_BREAK);
			}
			else {
				float[] answer = network.process(eye.lookAt(cluster.getImage()));
				int maxIndex = 0;
				float max = 0;
				for (int j=0; j<answer.length; j++) {
					if (answer[j] > max) {
						max = answer[j]; maxIndex=j;
					}
				}
				result.append(alphabet[maxIndex]);
			}
		}
		return result.toString();
	}
}
