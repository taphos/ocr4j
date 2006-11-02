package ee.ttu.ocr;

import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImageClusterer {
	
	private final static int BLACK = 0xff000000;
	
	private BufferedImage image;
	private Queue<Point> queue = new ConcurrentLinkedQueue<Point>();
	
	// this is a holder for space and line break clusters
	private Cluster foundCluster;
	
	private int numberOfFoundClusters;
	private ClusterBoundaries lastClusterBoundaries;
	private float averageClusterWidth;
	
	private Point foundClusterPoint;
	private Point clusterSearchStartPoint;	
	
	public ImageClusterer(BufferedImage image) {
		this.image = image;
	}
	
	public boolean hasMoreClusters() {
		if (foundClusterPoint != null || foundCluster != null)
			return true;
		if (clusterSearchStartPoint != null) {
			for (int x=clusterSearchStartPoint.getX(); x<image.getWidth(); x++) {
				if (image.getRGB(x,clusterSearchStartPoint.getY())==BLACK) {
					foundClusterPoint = new Point(x,clusterSearchStartPoint.getY());
					return true;
				}	
				//image.setRGB(x,clusterSearchStartPoint.getY(), 0xff00ff00);
			}
			// Next character is not found in this line, looks like a line break.			
			foundCluster = new LineBreakCluster();
			lastClusterBoundaries = null;			
			clusterSearchStartPoint = null;			
		}
		for(int y=0; y<image.getHeight(); y+=3) {
			for(int i=0; i<image.getWidth() && y-i>=0; i++) {				
				if (image.getRGB(i,y-i)==BLACK) {
					foundClusterPoint = new Point(i,y-i);
					return true;
				}	
				//image.setRGB(i,y-i, 0xff00ff00);
			}
		}
		for(int x=1; x<image.getWidth(); x+=3) {
			for(int i=0; i<image.getHeight() && x+i<image.getWidth(); i++) {				
				if (image.getRGB(x+i,image.getHeight()-i-1)==BLACK) {
					foundClusterPoint = new Point(x+i,image.getHeight()-i-1);					
					return true;
				}	
				//image.setRGB(x+i,image.getHeight()-i-1, 0xff00ff00);
			}
		}						
		return false;
	}
	
	private boolean isSpace(ClusterBoundaries boundaries1, ClusterBoundaries boundaries2) {
		if (boundaries1.getWidth() > averageClusterWidth*2 || boundaries1.getWidth()*2 < averageClusterWidth) {
			averageClusterWidth = boundaries1.getWidth();
			numberOfFoundClusters=1;
		}
		else {
			averageClusterWidth = (averageClusterWidth*numberOfFoundClusters + boundaries1.getWidth()) /(numberOfFoundClusters+1);
			numberOfFoundClusters++;
		}
		return (boundaries2.getLeft()-boundaries1.getRight())*1.5 > averageClusterWidth;
	}
	
	public Cluster nextCluster() {
		if (!hasMoreClusters())
			return null;
		// cluster is already found
		if (foundCluster != null) {
			Cluster cluster = foundCluster;
			foundCluster = null;			
			return cluster;
		}
		ClusterBoundaries boundaries = floodFill(foundClusterPoint);		
		foundClusterPoint = null;
		clusterSearchStartPoint = new Point(boundaries.getRight(), (boundaries.getTop()+boundaries.getBottom())/2);		
				
		Cluster cluster = new ImageCluster(image.getSubimage(boundaries.getLeft(), boundaries.getTop(), boundaries.getWidth()+1, boundaries.getHeight()+1));
		
		if (lastClusterBoundaries != null && isSpace(lastClusterBoundaries, boundaries)) {
			foundCluster = cluster;
			cluster = new SpaceCluster();
		}
		lastClusterBoundaries = boundaries;
		return cluster;				
	}

	private ClusterBoundaries floodFill(Point startPoint) {
		ClusterBoundaries boundaries = new ClusterBoundaries();
		queue.offer(startPoint);
		Point point;
		do {
			point = queue.poll();
			if (point==null || !isPointSet(point.getX(), point.getY())) {
				continue;	
			}				
			int y = point.getY();
			boundaries.setTop(y);boundaries.setBottom(y);
			int w,e;
			for (w = point.getX(); isPointSet(w-1,y); w--);
			for (e = point.getX(); isPointSet(e+1,y); e++);
			boundaries.setLeft(w);boundaries.setRight(e);
			for (int x = w; x <= e; x++) {
				unsetPoint(x,y);				
				if (isPointSet(x,y-1))								
					queue.offer(new Point(x,y-1));
				if (isPointSet(x,y+1))
					queue.offer(new Point(x,y+1));					
			}
			if (isPointSet(w-1,y-1))								
				queue.offer(new Point(w-1,y-1));
			if (isPointSet(w-1,y+1))
				queue.offer(new Point(w-1,y+1));
			if (isPointSet(e+1,y-1))								
				queue.offer(new Point(e+1,y-1));
			if (isPointSet(e+1,y+1))
				queue.offer(new Point(e+1,y+1));					

		} while(point!=null);
		return boundaries;
	}
	
	private boolean isPointSet(int x, int y) {
		return x < image.getWidth() && x >= 0 && y < image.getHeight() && y >= 0 && image.getRGB(x,y)==BLACK;						
	}
	
	private void unsetPoint(int x, int y) {
		if (x < image.getWidth() && x >= 0 && y < image.getHeight() && y >= 0) {
			image.setRGB(x,y,0xffff0000);
		}
	}	
	
	private class Point {
		private int x,y;
		
		public Point(int x, int y) {
			this.x = x; this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
	}
	
	private class ClusterBoundaries {
		private int right, left, top, bottom;

		public int getBottom() {
			return bottom;
		}

		public void setBottom(int bottom) {
			if (this.bottom < bottom)
				this.bottom = bottom;
		}

		public int getLeft() {			
			return left;
		}

		public void setLeft(int left) {
			if (this.left==0 || this.left > left)
				this.left = left;
		}

		public int getRight() {
			return right;
		}

		public void setRight(int right) {
			if (this.right < right)
				this.right = right;
		}

		public int getTop() {
			return top;
		}

		public void setTop(int top) {
			if (this.top==0 || this.top > top)
				this.top = top;
		}			
		
		public int getWidth() {
			return right-left;
		}
		
		public int getHeight() {
			return bottom-top;
		}
	}
	
	private class ImageCluster extends Cluster {		
		private BufferedImage image;
		
		public ImageCluster(BufferedImage image) {
			this.image = image;
		}

		public BufferedImage getImage() {
			return image;
		}
	}
	
	private class LineBreakCluster extends Cluster {
		public boolean isLineBreak() {
			return true;
		}		
	}

	private class SpaceCluster extends Cluster {
		public boolean isSpace() {
			return true;
		}		
	}
}
