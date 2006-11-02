package ee.ttu.ann;

import ee.ttu.math.ActivationFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class NeuralNetworkImpl implements NeuralNetwork {
	protected ActivationFunction activationFunction;
	private List<Neuron> inputLayer;
	protected List<Neuron> outputLayer;
	private List<Neuron> neurons = new ArrayList<Neuron>();
	private boolean cleared = true;
		
	public NeuralNetworkImpl(ActivationFunction activationFunction, int inputNeuronsCount) {
		this.activationFunction = activationFunction;
		inputLayer = new ArrayList<Neuron>(inputNeuronsCount);
		for (int i=0; i < inputNeuronsCount; i++) {
			Neuron inputNeuron = createNeuron();
			inputLayer.add(inputNeuron);
			neurons.add(inputNeuron);
		}
		outputLayer = inputLayer;
	}
	
	/* (non-Javadoc)
	 * @see ee.ttu.ann.NeuralNetwork#addNetworkLayer(int)
	 */
	public void addNetworkLayer(int neuronsCount) {
		List<Neuron> layer = new ArrayList<Neuron>(neuronsCount);
		for (int i=0; i < neuronsCount; i++) {
			Neuron neuron = createNeuron();
            for (Neuron outputLayerNeuron : outputLayer) {
                neuron.addInput(outputLayerNeuron);
            }
			layer.add(neuron);
			neurons.add(neuron);
		}					
		outputLayer = layer;
	}
	
	protected Neuron createNeuron() {
		return new Neuron(activationFunction);
	}
	
	/* (non-Javadoc)
	 * @see ee.ttu.ann.NeuralNetwork#process(float[])
	 */
	public float[] process(float[] inputs) {
		if (!cleared)
			clear();
		float[] outputs = new float[outputLayer.size()];

		int i=0;
		for (ListIterator<Neuron> it = inputLayer.listIterator(); it.hasNext(); i++) {
            it.next().set(inputs[i]);
		}		
		i = 0;
		for (ListIterator<Neuron> it = outputLayer.listIterator(); it.hasNext(); i++) {
			outputs[i] = it.next().getOutput();
		}
		cleared = false; 
		return outputs;
	}
	
	/**
	 * Clears the neorons cached activity values.
	 */
	private void clear() {
        for (Neuron neuron : neurons) {
            neuron.clear();
        }
	}
	
	/* (non-Javadoc)
	 * @see ee.ttu.ann.NeuralNetwork#randomize()
	 */
	public void randomize() {
        for (Neuron neuron : neurons) {
            neuron.randomizeInputNodes();
        }
	}

	public float getMaxOutputValue() {
		return activationFunction.getMaxValue();
	}

	public float getMinOutputValue() {
		return activationFunction.getMinValue();
	}
}
