package com.map.demo;

public class FFTSignalDatabase {
	// How many hits in a row before detection
	static final int DETECT_THRESHOLD = 7;
	static final int MAX_COUNT = 12;

	// Frequency range to search
	static final int MIN_FREQ = 1200;
	static final int MAX_FREQ = 2200;

	// Minimum amplitude to trigger a 'hit'
	static final int MIN_AMP = 200000;

	// Maximum allowable change in frequency between two samples
	// This is used to detect only relatively "smooth", continuous signals
	static final int MAX_FREQ_DELTA = 400;

	private int currentCount = 0;
	private int curFreq;
	private final int sampleRate;
	private final int bufferSize;
	private final int minIndex;
	private final int maxIndex;

	public FFTSignalDatabase(int sampleRate, int bufferSize) {
		this.sampleRate = sampleRate;
		this.bufferSize = bufferSize;
		minIndex = freqToIndex(MIN_FREQ);
		maxIndex = freqToIndex(MAX_FREQ);
	}

	public MAPFrame searchChunk(int vals[]) {
		int maxVal = -1;
		int maxLevel = -1;
		MAPFrame frame;

		// Search for the loudest frequency in the detection range
		for (int i = minIndex; i < maxIndex; i++) {
			int mag = Math.abs(vals[i]);
			if (mag > maxVal) {
				maxVal = mag;
				maxLevel = i;
			}
		}

		int maxFreq = indexToFreq(maxLevel);

		if (maxVal >= MIN_AMP) {
			// Signal strength was sufficient, check continuity
			if (currentCount > 0) {
				// We have a previous hit to compare the signal against
				if (Math.abs(maxFreq - curFreq) < MAX_FREQ_DELTA) {
					// Frequency is within expected range, accept
					currentCount++;
				} else {
					// Frequency is outside of expected range, reset
					currentCount--;
				}
			} else {
				// This is the first signal, just accept it
				currentCount = 1;
			}
			curFreq = maxFreq;
		} else {
			// Signal strength insufficient, reset regardless
			currentCount--;
		}

		if (currentCount >= DETECT_THRESHOLD) {
			// Enough hits detected in a row, signal detected
			frame = new MAPFrame(vals, new boolean[vals.length], curFreq);

			if (currentCount > MAX_COUNT) {
				currentCount = MAX_COUNT;
			}
		} else {
			frame = new MAPFrame(vals, new boolean[vals.length], -1);
		}

		return frame;
	}

	public int indexToFreq(int index) {
		return (index * sampleRate) / bufferSize;
	}

	public int freqToIndex(int freq) {
		return (freq * bufferSize) / sampleRate;
	}
}
