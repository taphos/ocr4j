package ee.ttu.ocr;

import java.awt.image.BufferedImage;

public abstract class Cluster {
	
	/**
	 * Get a part of the big image, croped to the bounds of this cluster
	 * @return image, is null for line breaks and spaces
	 */
	public BufferedImage getImage() {
		return null;
	}
	
	/** 
	 * @return true if this cluster is a line break
	 */
	public boolean isLineBreak() {
		return false;
	}
	
	/** 
	 * @return true if this cluster is a space
	 */
	public boolean isSpace() {
		return false;
	}

}
