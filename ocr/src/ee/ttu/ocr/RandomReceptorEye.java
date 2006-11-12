package ee.ttu.ocr;

import ee.ttu.ocr.teaching.EyeQualityInspector;
import ee.ttu.ocr.teaching.OCRTeachingCourse;
import ee.ttu.ocr.teaching.OCRTeachingException;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * This eye implementstion uses a number of random generated straight lines as a receptors
 * Receptor value is a procentage of line, covered by character glyph
 * 
 * If the line in fully out of the character glyph then receptors value is -0.5
 * Oterwise, if line is fully covered by character, its value is 0.5
 * 
 * Dont ask, why it is not 0 and 1. It just works better this way :-)
 * 
 * @author Filipp Keks
 */
public class RandomReceptorEye implements Eye {
    public static final long serialVersionUID = 8716502876866696187L;    

    private static final int DOTTS_PER_RECEPTOR = 100;
	private static final float MAX_LENGTH_OF_RECEPTOR = 0.4f;
	private static final float MIN_LENGTH_OF_RECEPTOR = 0.1f;
	private static final float MIN_DISTANCE_BETWEEN_RECEPTORS = 0.1f;
	
	private List<LineReceptor> receptors;
	private Random random = new Random(); 
	
	public RandomReceptorEye(int receptorsCount) {
		receptors = new ArrayList<LineReceptor>(receptorsCount);
		int i=0;
		do {
			LineReceptor receptor = new LineReceptor(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());
			if (receptor.getLength() <= MAX_LENGTH_OF_RECEPTOR && receptor.getLength() >= MIN_LENGTH_OF_RECEPTOR) {
				boolean goodReceptor = true;
                for (LineReceptor receptor1 : receptors) {
                    if ((Math.abs(receptor1.x1-receptor.x1)+Math.abs(receptor1.y1-receptor.y1) < MIN_DISTANCE_BETWEEN_RECEPTORS &&
						 Math.abs(receptor1.x2-receptor.x2)+Math.abs(receptor1.y2-receptor.y2) < MIN_DISTANCE_BETWEEN_RECEPTORS) ||
						(Math.abs(receptor1.x1-receptor.x2)+Math.abs(receptor1.y1-receptor.y2) < MIN_DISTANCE_BETWEEN_RECEPTORS &&
						 Math.abs(receptor1.x2-receptor.x1)+Math.abs(receptor1.y2-receptor.y1) < MIN_DISTANCE_BETWEEN_RECEPTORS)) {
						goodReceptor = false;
						break;
					}
                }
				if (goodReceptor) {
					receptors.add(receptor);
					i++;
				}
			}
		} while(i<receptorsCount);
	}		
	
	public int getReceptorsCount() {	
		return receptors.size();
	}

    public float getMaxReceptorValue() {
        return 0.5f;
    }

    public float getMinReceptorValue() {
        return -0.5f;
    }

    /**
	 * Though this is a RANDOM receptor eye, useless receptors can be thrown away by this method
	 * This should increase recognition quality and learning performance.
     *
	 * @param course - a teaching course eye should be optimized for
     * @param minUsability - minumal receptor quality coefficient
     * @throws OCRTeachingException
	 */
	public void optimize(OCRTeachingCourse course, float minUsability) throws OCRTeachingException {
		float[] receptorUsabilities = EyeQualityInspector.getEyeReceptorUsabilities(this, course);
		int i = 0;
		for(ListIterator<LineReceptor> it = receptors.listIterator(); it.hasNext(); i++) {
			it.next();
			if (receptorUsabilities[i] <= minUsability)
				it.remove();
		}
	}
			
	public float[] lookAt(BufferedImage image) {
		float[] result = new float[receptors.size()];
        int i=0;
		for (ListIterator<LineReceptor> it = receptors.listIterator(); it.hasNext();i++) {
			result[i] = it.next().getReceptorValue(image) - 0.5f;
        }
        return result;
	}
	
	public void drawReceptors(BufferedImage image) {
        for (LineReceptor receptor : receptors) {
            receptor.draw(image);
        }
	}
	
	private class LineReceptor implements Serializable {
        public static final long serialVersionUID = 3312441783304304774L;

        private float x1, y1, x2, y2;
        private static final int WHITE = 0xffffffff;
        private static final int RED = 0xffff0000;

        public LineReceptor(float x1, float y1, float x2, float y2) {
            this.x1 = x1;this.y1 = y1;
            this.x2 = x2;this.y2 = y2;
        }
		
		public float getReceptorValue(BufferedImage image) {
			int borderYWidth = image.getHeight()/5;
			int borderXWidth = image.getHeight()/5;
			int areaHeight = image.getHeight()+borderYWidth*2;
			int areaWidth = image.getWidth()+borderXWidth*2;
			if (areaWidth < areaHeight) {
				areaWidth = areaHeight;
				borderXWidth = (areaWidth-image.getWidth())/2;
			}
			
			float x1 = this.x1*areaWidth;
			float y1 = this.y1*areaHeight;
			float x2 = this.x2*areaWidth;
			float y2 = this.y2*areaHeight;
			
			float result = 0;
			for(int i=0; i<=DOTTS_PER_RECEPTOR; i++) {
				int x = (int)((i*x1+(DOTTS_PER_RECEPTOR-i)*x2)/DOTTS_PER_RECEPTOR)-borderXWidth; 
				int y = (int)((i*y1+(DOTTS_PER_RECEPTOR-i)*y2)/DOTTS_PER_RECEPTOR)-borderYWidth;				
				if (x < image.getWidth() & x >= 0 && y < image.getHeight() && y >= 0 && image.getRGB(x,y) != WHITE)
					result += 1;
			}
			
			return result/DOTTS_PER_RECEPTOR;
		}
				
		public void draw(BufferedImage image) {
			int x1 = (int)(this.x1*image.getWidth());
			int y1 = (int)(this.y1*image.getHeight());
			int x2 = (int)(this.x2*image.getWidth());
			int y2 = (int)(this.y2*image.getHeight());
			
			for(int i=0; i<=DOTTS_PER_RECEPTOR; i++) {
				int x = (i*x1+(DOTTS_PER_RECEPTOR-i)*x2)/DOTTS_PER_RECEPTOR; 
				int y = (i*y1+(DOTTS_PER_RECEPTOR-i)*y2)/DOTTS_PER_RECEPTOR;								
				image.setRGB(x,y,RED);
			}						
		}
		
		public float getLength() {
			return (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		}
	}		
}
