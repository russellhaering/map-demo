package com.map.demo;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioHandler {
	//private AudioInputStream istream;
	private final int SAMPLE_RATE = 44100;
	private TargetDataLine line;
	private AudioFormat fmt;

	AudioHandler() throws LineUnavailableException {
		/*
		AudioInputStream fstream = AudioSystem.getAudioInputStream(new File(path));
		AudioFormat temp = fstream.getFormat();
		if (temp.getChannels() != 1) {
			throw new UnsupportedAudioFileException("Only 'mono' audio files are supported.");
		}
		AudioFormat fmt = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				temp.getSampleRate(), 16,
				temp.getChannels(), temp.getChannels() * 2,
				temp.getSampleRate(), temp.isBigEndian());
		*/
		//istream = AudioSystem.getAudioInputStream(fmt, fstream);
		fmt = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SAMPLE_RATE, 16, 1, 2, SAMPLE_RATE, true);
		line = AudioSystem.getTargetDataLine(fmt);
		System.out.println("Using " + line.getLineInfo());
		line.open(fmt, 4096 * 32);
		line.start();
	}
	
	float getSampleRate() {
		return fmt.getSampleRate();
	}
	
	short[] getChunk(int chunkSize) {
		// Read enough bytes for chunkSize shorts
		byte[] buf = new byte[chunkSize * 2];
		try {
			int count = line.read(buf, 0, buf.length);
			// If not enough bytes remained, return null
			if (count != buf.length) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		
		// Wrap the byte array into a ShortBuffer
		ByteBuffer bb = ByteBuffer.wrap(buf);
		ShortBuffer sb = bb.asShortBuffer();
		
		// Read an array of shorts from the buffer
		short[] chunk = new short[chunkSize];
		sb.get(chunk, 0, chunkSize);
		
		return chunk;
	}
}
