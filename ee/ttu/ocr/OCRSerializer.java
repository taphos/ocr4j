package ee.ttu.ocr;

import ee.ttu.ann.NeuralNetwork;

import java.io.*;

public class OCRSerializer {

    private static final String CLASSPATH_RESOURCE_NAME = "OCRBrain.ann";

    private File file;
	
	private char[] alphabet;
	private Eye eye;
	private NeuralNetwork network;
	
	public OCRSerializer(String fileName) {
		this.file = new File(fileName);
	}

    public OCRSerializer() {
    }

    public void write() throws IOException {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			try {
				out.writeObject(alphabet);
				out.writeObject(eye);
				out.writeObject(network);				
			} 
			catch (IOException e) {
				throw e;
			} 
			finally {
				try {
					out.close();
				} 
				catch (IOException e) {}	
			}						
	}
	
	public void read() throws IOException {
        ObjectInputStream in;
        if (file == null) {
            in = new ObjectInputStream(getClass().getResourceAsStream(CLASSPATH_RESOURCE_NAME));
        }
        else {
            in = new ObjectInputStream(new FileInputStream(file));
        }
        try {
			try {
				alphabet = (char[])in.readObject();
				eye = (Eye)in.readObject();
				network = (NeuralNetwork)in.readObject();
			} catch (ClassNotFoundException e) {
				// this should not happen
			}
		} 
		catch (IOException e) {
			throw e;
		} 
		finally {
			try {
				in.close();
			} 
			catch (IOException e) {}	
		}						
	}

	public char[] getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(char[] alphabet) {
		this.alphabet = alphabet;
	}

	public Eye getEye() {
		return eye;
	}

	public void setEye(Eye eye) {
		this.eye = eye;
	}

	public NeuralNetwork getNetwork() {
		return network;
	}

	public void setNetwork(NeuralNetwork network) {
		this.network = network;
	}		
}
