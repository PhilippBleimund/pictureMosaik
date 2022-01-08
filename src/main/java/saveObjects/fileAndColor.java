package saveObjects;

import java.awt.Color;
import java.io.File;

public class fileAndColor implements Comparable<fileAndColor>{
	private final File file;
	private final Color color;
	private int timesUsed;

	public fileAndColor(File file, Color color) {
		this.file = file;
		this.color = color;
	}

	@Override
	synchronized public int compareTo(fileAndColor o) {
		if(this.color.getRGB() < o.color.getRGB())
			return -1;
		if(this.color.getRGB() == o.color.getRGB())
			return 0;
		return 1;
	}
	
	synchronized public File getFile() {
		return file;
	}

	synchronized public Color getColor() {
		return color;
	}
	
	synchronized public int getTimesUsed() {
		return timesUsed;
	}
	
	synchronized public void increaseTimesUsed() {
		this.timesUsed += 1;
	}
}