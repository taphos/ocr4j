package ee.ttu.ocr;

import java.awt.image.BufferedImage;

/**
 * This is a simpliest feature extraction algorithm.
 * Image is divided into X*Y squares and the average density is computed for each square
 * 
 * @author Filipp Keks
 */
public class SquareRegionDensityEye implements Eye {
	
	private final static int WHITE = 0xffffffff;
	
	private int xSquares;
	private int ySquares;
	private float overlapping;

	public SquareRegionDensityEye(int xSquares, int ySquares, float overlapping) {
		this.xSquares = xSquares;
		this.ySquares = ySquares;
		this.overlapping = overlapping;
	}
	
	public float[] lookAt(BufferedImage image) {
		float[] result = new float[getReceptorsCount()];
		int index = 0;
		int width = image.getWidth();
		int height = image.getHeight();
		float squareHeight = height/ySquares;
		float squareWidth = width/xSquares;
		int yOverlapping = (int)(squareHeight*overlapping);
		int xOverlapping = (int)(squareWidth*overlapping); 
		for (int i=0; i<ySquares; i++) {
			for (int j=0; j<xSquares; j++) {
				int top = (int)(i*squareHeight-yOverlapping);
				int bottom = (int)((i+1)*squareHeight+yOverlapping);
				int left = (int)(j*squareWidth-xOverlapping);
				int right = (int)((j+1)*squareWidth+xOverlapping);
				if (top < 0) top = 0;
				if (bottom > height) bottom = height;
				if (left < 0) left = 0;
				if (right > width) right = width;
				
				result[index] = getSquareDensity(image, top, bottom, left, right);
				index++;
			}
		}
		return result;
	}
	
	private float getSquareDensity(BufferedImage image, int top, int bottom, int left, int right) {
		int pixels = 0;
		int blackPixels = 0;
		for (int i = top; i < bottom; i++) {
			for (int j = left; j < right; j++) {
				pixels++;
				if (image.getRGB(j,i) != WHITE)
					blackPixels++;
			}
		}
		return (float)blackPixels/pixels;
	}
		
	public int getReceptorsCount() {
		return xSquares*ySquares;
	}

    public float getMaxReceptorValue() {
        return 1;
    }

    public float getMinReceptorValue() {
        return 0;
    }
}