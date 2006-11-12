package ee.ttu.ann;

import ee.ttu.math.ActivationFunction;
import ee.ttu.math.MathEx;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable {		
	protected Float cachedOutput;
	
	protected ArrayList<Node> inputNodes;	
	protected ActivationFunction activationFunction;
	
	public Neuron(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}
	
	/**
	 * Get output of the neuron. 
	 * If the internal value is not yet set, this method activates the whole network recursively
	 * to get the internal value.
	 * @return neuron value
	 */
	public float getOutput() {
		if (cachedOutput == null) {			
			float value = 0;
            for (Node inputNode : inputNodes) {
                value += inputNode.getOutput();
            }
			cachedOutput = activationFunction.calculate(value);
		}		
		return cachedOutput;
	}
		
	/**
	 * Sets the internal value of the neuron. This should be used only for input neurons.
	 * @param value
	 */
	public void set(Float value) {
		cachedOutput = activationFunction.calculate(value);		
	}
	
	public void addInput(Neuron neuron) {
		if (inputNodes == null)
			inputNodes = new ArrayList<Node>();
		inputNodes.add(new Node(neuron));
	}

    public void clear() {
		cachedOutput = null;
	}
	
	public void randomizeInputNodes() {
        for (Node node : inputNodes) {
            node.randomize();
        }
	}
	
	public class Node implements Serializable {
		private Neuron inputNeuron;
		protected float weight;
		
		public Node(Neuron inputNeuron) {
			this.inputNeuron = inputNeuron;
			randomize();
		}
		
		public float getWeight() {
			return weight;
		}
		
		public Neuron getInputNeuron() {
			return inputNeuron;
		}
		
		public float getOutput() {
			return inputNeuron.getOutput()*weight;
		}
				
		public void randomize() {
			weight = MathEx.random();
		}
	}
}
