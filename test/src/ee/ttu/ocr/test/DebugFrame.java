package ee.ttu.ocr.test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DebugFrame extends JFrame {
	BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_3BYTE_BGR);
	
	public DebugFrame() {						
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
		        int width = getSize().width;
		        int height = getSize().height;
		        int imageW = image.getWidth();
		        int imageH = image.getHeight();					
		        super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D)g;
		        g2.drawImage(image,
		            (width - imageW) / 2, (height - imageH) /2, this);		      			
			}
		};
		setSize(700,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(panel, "Center");		    		    		    
	    setVisible(true);
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}
}
