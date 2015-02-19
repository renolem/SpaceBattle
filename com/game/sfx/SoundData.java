package com.game.sfx;

import javax.sound.sampled.AudioFormat;

public class SoundData {

	private final byte[] data;
	
	private final AudioFormat format;

	public SoundData(final byte[] data, final AudioFormat format) {
		this.data = data;
		this.format = format;
	}

	public byte[] getData() {
		return data;
	}

	public AudioFormat getFormat() {
		return format;
	}
}
