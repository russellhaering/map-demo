package com.map.demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class DemoUI extends JFrame {
	private static final long serialVersionUID = -444782811267360620L;
	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 512;
	private final int WINDOW_HLOC = 100;
	private final int WINDOW_VLOC = 100;
	private final int CHUNK_SIZE = 4096;
	private SpectrogramPanel sPanel;
	private AudioHandler aHandler;
	private FFTSignalDatabase sigDatabase;
	
	DemoUI() {
		setTitle("M.A.P. Demo Interface");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(WINDOW_HLOC, WINDOW_VLOC);
		setResizable(false);
		sPanel = new SpectrogramPanel();
		add(sPanel);
		addWindowListener(new DemoWindowAdapter());
		
		try {
			aHandler = new AudioHandler();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		sigDatabase = new FFTSignalDatabase((int) aHandler.getSampleRate(), CHUNK_SIZE);
		
		setVisible(true);
		
		new Thread(audioProcessor).start();
	}
	
	private class DemoWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	private Runnable audioProcessor = new Runnable() {
		public void run() {
			while (true) {
				short[] chunk = aHandler.getChunk(CHUNK_SIZE);
				if (chunk == null) {
					break;
				}
				
				int[] vals = FFT.realFFT(chunk, 0, CHUNK_SIZE);
				MAPFrame frame = sigDatabase.searchChunk(vals);
				sPanel.pushFrame(frame);
			}
		}
	};

	public static void main(String[] args) {
		new DemoUI();
	}
}
