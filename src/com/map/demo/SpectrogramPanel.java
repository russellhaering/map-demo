package com.map.demo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.LinkedList;

import javax.swing.JPanel;

public class SpectrogramPanel extends JPanel {
	private static final long serialVersionUID = 5663782095672174332L;
	private final int WIDTH = 800;
	private final int HEIGHT = 512;
	private final int FRAMEW = 10;
	private LinkedList<BufferedImage> columns;
	//private LinkedList<MAPFrame> frames;

	SpectrogramPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);
		columns = new LinkedList<BufferedImage>();
		//frames = new LinkedList<MAPFrame>();
	}

	synchronized public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int j = 0;
		for (BufferedImage column : columns) {
			g.drawImage(column, WIDTH - j - FRAMEW, 0, (ImageObserver) null);
			j+= FRAMEW;
		}
		
		repaint();
	}

	public void pushFrame(MAPFrame frame) {
		BufferedImage column = new BufferedImage(FRAMEW, HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < HEIGHT; i++) {
			int rgb = frame.getRGB(i, HEIGHT * 8);
			for (int j = 0; j < FRAMEW; j++) {
				column.setRGB(j, HEIGHT - i - 1, rgb);
			}
		}
		
		synchronized (this) {
			columns.push(column);

			if (columns.size() > (WIDTH / FRAMEW)) {
				columns.removeLast();
			}
		}

	}
}
