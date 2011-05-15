package com.map.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.LinkedList;

import javax.swing.JPanel;

public class SpectrogramPanel extends JPanel {
	private static final long serialVersionUID = 5663782095672174332L;
	private final int WIDTH = 800;
	private final int HEIGHT = 512;
	private final int FRAMEW = 5;
	private LinkedList<BufferedImage> columns;

	SpectrogramPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);
		columns = new LinkedList<BufferedImage>();
	}

	synchronized public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int j = 0;
		for (BufferedImage column : columns) {
			g.drawImage(column, WIDTH - j - FRAMEW, 0, (ImageObserver) null);
			j += FRAMEW;
		}

		repaint();
	}

	public void pushFrame(MAPFrame frame) {
		BufferedImage column = new BufferedImage(FRAMEW, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < HEIGHT; i++) {
			int rgb = frame.getRGB(i, HEIGHT);
			for (int j = 0; j < FRAMEW; j++) {
				column.setRGB(j, HEIGHT - i - 1, rgb);
			}
		}

		int level = frame.getLevel();
		if (level >= 0) {
			int idx = level;
			for (int i = idx - 1; i <= idx + 1; i++) {
				for (int j = 0; j < FRAMEW; j++) {
					if (i >= 0 && i < HEIGHT)
						column.setRGB(j, HEIGHT - i - 1, Color.RED.getRGB());
				}
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
