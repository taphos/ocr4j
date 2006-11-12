package ee.ttu.ann.learning;

import ee.ttu.ann.NeuralNetwork;

public interface LearningNeuralNetwork extends NeuralNetwork {
	
	/**
	 * Teach the network to recognize the specified pattern.
	 * @param pattern
	 * @return error value of the network.
	 */
	public float learn(Pattern pattern, float learningRate);
	
}
