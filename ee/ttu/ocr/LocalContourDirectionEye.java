package ee.ttu.ocr;

import java.awt.image.BufferedImage;

public class LocalContourDirectionEye implements Eye {
	
	private final static int WHITE = 0xffffffff;
	private final static int RECEPTORS_PER_SQUARE = 3;
	
	private int xSquares;
	private int ySquares;
	private float overlapping;

	public LocalContourDirectionEye(int xSquares, int ySquares, float overlapping) {
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
				
				Cell cell = getCellFeatures(image, top, bottom, left, right);
				result[index] = cell.density;
				result[index+1] = cell.directionFeature1;
				result[index+2] = cell.directionFeature2;								
				index+=RECEPTORS_PER_SQUARE;
			}
		}
		return result;
	}
		
	private Cell getCellFeatures(BufferedImage image, int top, int bottom, int left, int right) {
		int width = image.getWidth();
		int height = image.getHeight();	
		
		int verticalDirection = 0;
		int horizontalDirection = 0;
		int density = 0;		
		int vertIncrement = 1;

		for (int i=top; i<bottom; i++) {
			for (int j=left; j<right; j++) {
				// is a character dott
				if (image.getRGB(j, i) != WHITE) {
					density++;
					boolean[][] surround = new boolean[3][3];
					
					surround[0][1] = j - 1 >= 0 && image.getRGB(j - 1, i) != WHITE;
					surround[1][0] = i - 1 >= 0 && image.getRGB(j, i - 1) != WHITE;
					surround[0][0] = !(j - 1 < 0 || i - 1 < 0) && image.getRGB(j - 1, i - 1) != WHITE;
					surround[0][2] = j - 1 >= 0 && i + 1 < height && image.getRGB(j - 1, i + 1) != WHITE;
					
					surround[2][1] = j + 1 < width && image.getRGB(j + 1, i) != WHITE;
					surround[1][2] = i + 1 < height && image.getRGB(j, i + 1) != WHITE;
					surround[2][2] = !(j + 1 >= width || i + 1 >= height) && image.getRGB(j + 1, i + 1) != WHITE;
					surround[2][0] = !(j + 1 >= width || i - 1 < 0) && image.getRGB(j + 1, i - 1) != WHITE;

					// is contour dott
					if (!surround[0][1] || !surround[2][1] || !surround[1][0] || !surround[1][2]) { 			
						if (surround[0][1] && surround[2][1])
							horizontalDirection++;
						else if (surround[1][0] && surround[1][2])
							verticalDirection+=vertIncrement;
						else {
							if (surround[0][0] && surround[2][2])
								vertIncrement = 1;						
							else if (surround[2][0] && surround[0][2])
								vertIncrement = -1;
							else if ((surround[0][0] && (surround[1][2] || surround[2][1])) || 
									 (surround[2][2] && (surround[1][0] || surround[0][1])))
								vertIncrement = 1;						
							else if (surround[2][0] && (surround[0][1] || surround[1][2]) || 
									(surround[0][2] && (surround[1][0] || surround[2][1])))
								vertIncrement = -1;
							horizontalDirection++;
							verticalDirection+=vertIncrement;
						}
					}
				}
			}
		}					
				
		Cell cell = new Cell();
		cell.density = (float)density/((right-left)*(bottom-top));
		float k = (float)verticalDirection/horizontalDirection;
		if (Float.isNaN(k)) {
			cell.directionFeature1 = 0;
			cell.directionFeature2 = 0;			
		}
		else if (Float.isInfinite(k)) {
			cell.directionFeature1 = 0;
			cell.directionFeature2 = -1;						
		}
		else {			
			float sqK = k*k;		
			cell.directionFeature1 = 2*k / (1+sqK);
			cell.directionFeature2 = (1-sqK) / (1+sqK);
		}

        // Uncomment to draw the detected directions on the image
        /*for (int i=left; i<right; i++) {
			image.setRGB(i, top, 0xffaaaaaa);
			image.setRGB(i, bottom-1, 0xffaaaaaa);
		}
		for (int i=top; i<bottom; i++) {
			image.setRGB(left, i, 0xffaaaaaa);
			image.setRGB(right-1, i, 0xffaaaaaa);
		}			
		if (!Float.isNaN(k) && !Float.isInfinite(k)) {
			int width1 = right-left-1;
			int height1 = bottom-top-1;
            for (int i=-width1/2; i<=width1/2; i++) {
				int y = (int)(k*i*height1/width1);
				if (y<=height1/2 && y>-height1/2) {
					image.setRGB(i+left+width1/2, y+top+height1/2, 0xff000000);
				}
            }
		}*/
		//System.out.println(cell.density + " :    "+cell.directionFeature1+" : "+cell.directionFeature2);

		return cell;
	}
	
	public int getReceptorsCount() {
		return xSquares*ySquares*RECEPTORS_PER_SQUARE;
	}

    public float getMaxReceptorValue() {
        return 1;
    }

    public float getMinReceptorValue() {
        return -1;
    }

    private class Cell {
		private float directionFeature1;
		private float directionFeature2;
		private float density;
	}

}
