package com.map.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

public class SpectrogramPanel extends JPanel {
	private static final long serialVersionUID = 5663782095672174332L;
	private final int WIDTH = 800;
	private final int HEIGHT = 512;
	private final int FRAMEW = 10;
	private LinkedList<MAPFrame> frames;

	SpectrogramPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);
		frames = new LinkedList<MAPFrame>();
	}

	synchronized public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLUE);

		int j = 0;
		for (MAPFrame frame : frames) {
			for (int i = 0; i < HEIGHT; i++) {
				g.setColor(frame.getColor(i, HEIGHT * 8));
				g.fillRect(WIDTH - j - FRAMEW, HEIGHT - i - 1, FRAMEW, 1);
			}
			j+= FRAMEW;
		}
	}

	synchronized public void pushFrame(MAPFrame frame) {
		frames.addFirst(frame);

		if (frames.size() > (WIDTH / FRAMEW)) {
			frames.removeLast();
		}
		
		repaint();
	}
}
