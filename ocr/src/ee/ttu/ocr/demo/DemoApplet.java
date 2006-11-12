package ee.ttu.ocr.demo;

import ee.ttu.ocr.OCR;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DemoApplet extends JApplet {

	static OCR ocr;

    public void init() {
        try {
            ocr = new OCR();
		    new DemoApplet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DemoApplet() {
		setSize(650,500);

		final DemoApplet.DrawingPanel drawingPannel = new DemoApplet.DrawingPanel();
		final JTextArea textArea = new JTextArea(5, 40);
		JButton recogniseButton = new JButton("Recognise");
		recogniseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String text = ocr.recognise(drawingPannel.getBackgroundImage());
            	System.out.println(text);
                textArea.setText(text);
                repaint();
            }});

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawingPannel.clearBackgroundImage();
            }});

		JPanel pane = new JPanel();
		pane.add(new JLabel("Draw letters and click Recognise"));
		//pane.add(Box.createRigidArea(new Dimension(0,5)));
		pane.add(drawingPannel);
		pane.add(new JLabel("Result"));
		pane.add(new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		pane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(recogniseButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(clearButton);

	    Container contentPane = getContentPane();
	    contentPane.add(pane, BorderLayout.CENTER);
	    contentPane.add(buttonPane, BorderLayout.PAGE_END);

	    setVisible(true);
	}

	private class DrawingPanel extends JPanel implements MouseInputListener {

		BufferedImage background;
		Graphics2D backgroundGraphics;
		int lastX,lastY;
		boolean drawing;
		int width = 500;
	   	int height = 200;

		public DrawingPanel() {
		   	this.setSize(width,height);
		   	background = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		   	backgroundGraphics = background.createGraphics();
		   	backgroundGraphics.setBackground(Color.WHITE);
		   	backgroundGraphics.setColor(Color.BLACK);
		   	backgroundGraphics.clearRect(0, 0, width, height);
		   	backgroundGraphics.setStroke(new BasicStroke(5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		   	this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		   	this.setPreferredSize(new Dimension(width,height));
		   	this.setMaximumSize(new Dimension(width,height));
		   	this.setMinimumSize(new Dimension(width,height));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(background, 0, 0, this);
		}

		public void mouseClicked(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
		    lastX = e.getX();
		    lastY = e.getY();
		}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mouseDragged(MouseEvent e) {
			backgroundGraphics.drawLine(lastX,lastY,e.getX(),e.getY());
			lastX = e.getX();
			lastY = e.getY();
			repaint();
			e.consume();
		}

		public void mouseMoved(MouseEvent e) {}

		public BufferedImage getBackgroundImage() {
			return background;
		}

		public void clearBackgroundImage() {
			backgroundGraphics.clearRect(0,0,width,height);
			repaint();
		}
	};
}
