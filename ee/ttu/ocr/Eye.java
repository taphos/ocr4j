package ee.ttu.ocr;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * This is a common interface for character image feature extractors
 * @author Filipp Keks
 */
public interface Eye extends Serializable {
	
	/**
	 * Convert image into a sequence of float numbers (receptor values).
	 * @param image
	 * @return array of eye receptors values
	 */
	public float[] lookAt(BufferedImage image);
	
	/** 
	 * @return number of eye receptors
	 */
	public int getReceptorsCount();

    /**
     * get maximum possible value of the eye receptor
     * @return float value
     */
    public float getMaxReceptorValue();

    /**
     * get minimum possible value of the eye receptor
     * @return float value
     */
    public float getMinReceptorValue();
}
