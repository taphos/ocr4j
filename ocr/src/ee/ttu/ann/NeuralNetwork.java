package ee.ttu.ann;

import java.io.Serializable;

public interface NeuralNetwork extends Serializable {

	/**
	 * Create a new network layer with specified number of neurons
	 * last created layer is is the output layer of the network
	 * @param neuronsCount
	 */
	public void addNetworkLayer(int neuronsCount);

	/**
	 * Process the network
	 * @param inputs
	 * @return output values of the network
	 */
	public float[] process(float[] inputs);

	/**
	 * Randomize all node weights
	 */
	public void randomize();
	
	/** 
	 * @return maximum output value of the network
	 */
	public float getMaxOutputValue();
	
	/** 
	 * @return minimum output value of the network
	 */
	public float getMinOutputValue();
}