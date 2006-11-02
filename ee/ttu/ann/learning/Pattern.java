package ee.ttu.ann.learning;

public class Pattern {
	
	private float[] inputs;
	private float[] outputs;
	
	public Pattern(float[] inputs, float[] outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	public float[] getInputs() {
		return inputs;
	}
	
	public float[] getOutputs() {
		return outputs;
	}

}
