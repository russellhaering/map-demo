package com.map.demo;

import java.awt.Color;

public class MAPFrame {
	private static float MAXVAL = 800000;
	private final int[] vals;
	private final boolean[] hits;
	private final int level;

	MAPFrame(int[] vals, boolean[] hits, int level) {
		this.vals = vals;
		this.hits = hits;
		this.level = level;
	}

	public int[] getVals() {
		return vals;
	}

	public boolean[] getHits() {
		return hits;
	}

	public int getLevel() {
		return level;
	}

	public int getRGB(int offset, int packTo) {
		int count = vals.length / packTo;
		float total = 0;
		for (int i = offset; i < offset + count; i++) {
			total += Math.abs(vals[offset]);
		}
		float percent = ((total - (MAXVAL / 4)) / count) / MAXVAL;
		if (percent > 1) {
			percent = 1;
		}
		if (percent < 0) {
			percent = 0;
		}

		percent *= 0.7;

		boolean hit = false;

		for (int i = offset; i < offset + count; i++) {
			if (hits[i]) {
				hit = true;
				break;
			}
		}

		if (hit) {
			return new Color(0, (1 - percent), 0).getRGB();
		} else {
			return new Color((1 - percent), (1 - percent), (1 - percent))
					.getRGB();
		}
	}
}
