package ee.ttu.ann.learning;

import ee.ttu.ann.NeuralNetworkImpl;
import ee.ttu.ann.Neuron;
import ee.ttu.math.ActivationFunction;
import ee.ttu.math.SigmoidFunction;

import java.util.List;
import java.util.ListIterator;

public class BackPropagationLearningNetwork extends NeuralNetworkImpl implements LearningNeuralNetwork {
	
	public BackPropagationLearningNetwork(ActivationFunction activationFunction, int inputNeuronsCount) {
		super(activationFunction, inputNeuronsCount);
	}
	
	public BackPropagationLearningNetwork(int inputNeuronsCount) {
		super(new SigmoidFunction(1), inputNeuronsCount);
	}

	protected Neuron createNeuron() {
		return new BackPropagationLearningNeuron(activationFunction);
	}
	
	/**
	 * Teach the network. and return network error value
	 * 	error = 1/2 * sum(y - d)^2 
	 * 
	 * @param pattern
	 * @param learningRate
	 * @return network error
	 */
	public float learn(Pattern pattern, float learningRate) {		
		process(pattern.getInputs());
		int i = 0;
		float error = 0;
		float[] outputs = pattern.getOutputs();
		for (ListIterator<Neuron> it = outputLayer.listIterator(); it.hasNext(); i++) {			
			BackPropagationLearningNeuron neuron = (BackPropagationLearningNeuron)it.next();
			float delta = neuron.getOutput() - outputs[i];
            error += delta*delta;
			neuron.increaseError(learningRate*delta);
			neuron.learn();
		}	
		List<Neuron.Node> nodeLayer = ((BackPropagationLearningNeuron)outputLayer.get(0)).getInputNodes();
		do {			
			teachLayer(nodeLayer);
			nodeLayer = ((BackPropagationLearningNeuron)nodeLayer.get(0).getInputNeuron()).getInputNodes();
		} while (nodeLayer != null);
		return error/2;
	}

	private void teachLayer(List<Neuron.Node> layer) {
        for (Neuron.Node node : layer) {
            ((BackPropagationLearningNeuron)node.getInputNeuron()).learn();
        }
	}
}
