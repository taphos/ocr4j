package ee.ttu.math;

/**
 * Sigmoid activation function
 * Output range: [-0.5, 0.5]
 *
 *               1
 * f(x) = ------------------- - 0.5
 *        1 + exp(-alpha * x)
 *
 * @author Filipp Keks
 */ 
public class BipolarSigmoidFunction implements ActivationFunction {
	
	private float alpha;
		
	public BipolarSigmoidFunction(float alpha) {
		this.alpha = alpha;
	}
	
	public float calculate(float x) {
		return (float)(1/(1+Math.exp(-alpha*x))-0.5);
	}

	public float calculateDerivative(float y) {
		return alpha*(0.25f - y*y);
	}

	public float getMaxValue() {
		return 0.5f;
	}

	public float getMinValue() {
		return -0.5f;
	}
}
