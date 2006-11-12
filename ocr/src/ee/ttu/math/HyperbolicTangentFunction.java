package ee.ttu.math;

/**
 * Hyperbolic Tangent activation function
 * Output range: [-1, 1]
 *
 *                         exp(alpha * x) - exp(-alpha * x)
 * f(x) = tanh(alpha * x) = ------------------------------
 *                         exp(alpha * x) + exp(-alpha * x)
 *
 * @author Filipp Keks
 */
public class HyperbolicTangentFunction implements ActivationFunction {
	
	private float alpha;
	
	public HyperbolicTangentFunction(float alpha) {
		this.alpha = alpha;
	}	

	public float calculate(float x) {		
		return (float)Math.tanh(alpha*x);
	}

	public float calculateDerivative(float y) {		
		return alpha * (1 - y*y);
	}

	public float getMaxValue() {	
		return 1;
	}

	public float getMinValue() {
		return -1;
	}
}
