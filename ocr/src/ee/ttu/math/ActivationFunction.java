package ee.ttu.math;

import java.io.Serializable;

public interface ActivationFunction extends Serializable {	
	/**
	 * Calculate the output of the function.
	 * @param x
	 * @return y
	 */
	public float calculate(float x);
	
	/**
	 * Calculate the differential of the function. 
	 * @param y is the output of {@link ActivationFunction#calculate(float)} method
	 * @return derivative
	 */
	public float calculateDerivative(float y);
	
	/** 
	 * @return a maximum possible output value of the function
	 */
	public float getMaxValue();
	
	/** 
	 * @return a minimum possible output value of the function
	 */
	public float getMinValue();
}
