package testMode.Panel;

import java.awt.Dimension;

public class OriginalImageConfig {

	int splitsX;
	int splitsY;
	Dimension size;
	
	public OriginalImageConfig(int splitsX, int splitsY, int size) {
		this.splitsX = splitsX;
		this.splitsY = splitsY;
		this.size = new Dimension(size, size);
	}

	public int getSplitsX() {
		return splitsX;
	}

	public int getSplitsY() {
		return splitsY;
	}

	public Dimension getSize() {
		return size;
	}
}
