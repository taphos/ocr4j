package ee.ttu.math;

/**
 * Sigmoid activation function
 * Output range: [0, 1]
 *
 *               1
 * f(x) = ------------------
 *        1 + exp(-alpha * x)
 *
 * @author Filipp Keks
 */ 
public class SigmoidFunction implements ActivationFunction {
	
	private float alpha;
		
	public SigmoidFunction(float alpha) {
		this.alpha = alpha;
	}
	
	public float calculate(float x) {
		return (float)(1/(1+Math.exp(-alpha*x)));
	}

	public float calculateDerivative(float y) {
		return alpha*y*(1-y);
	}

	public float getMaxValue() {
		return 1;
	}

	public float getMinValue() {
		return 0;
	}
}
