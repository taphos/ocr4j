package ee.ttu.math;

public class MathEx {

    private static java.util.Random random = new java.util.Random();

	public static float random() {
		return random.nextFloat()-0.5f;
	}

    /**
	 * Calculate the entropy of the array values using correlative integral
	 * @param distance
	 * @param values
	 * @return correlative entropy
	 */
	public static float correlativeEntropy(float distance, float[] values) {
		int result = 0;
		for (int i=0; i<values.length; i++) {
			for (int j=i+1; j<values.length; j++) {
				if (Math.abs(values[i]-values[j]) < distance) {
					result++;
				}
			}
		}
		return (float)-Math.log(2*(float)result/(values.length*(values.length-1)));
	}

}
