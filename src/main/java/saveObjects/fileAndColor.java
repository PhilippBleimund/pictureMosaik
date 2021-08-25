package saveObjects;

import java.awt.Color;
import java.io.File;

public class fileAndColor implements Comparable<fileAndColor>{
	public File file;
	public Color color;
	public int timesUsed;
	
	public fileAndColor(File file, Color color) {
		this.file = file;
		this.color = color;
	}

	@Override
	public int compareTo(fileAndColor o) {
		if(this.color.getRGB() < o.color.getRGB())
			return -1;
		if(this.color.getRGB() == o.color.getRGB())
			return 0;
		return 1;
	}
	
	public int getTimesUsed() {
		return timesUsed;
	}
	
	public void increaseTimesUsed() {
		this.timesUsed += 1;
	}
}