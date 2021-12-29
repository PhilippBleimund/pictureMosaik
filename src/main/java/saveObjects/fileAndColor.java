package saveObjects;

import java.awt.Color;
import java.io.File;

public class fileAndColor implements Comparable<fileAndColor>{
	public File file;
	public Color[][] colors;
	public int timesUsed;
	
	public fileAndColor(File file, Color[][] colors) {
		this.file = file;
		this.colors = colors;
	}

	@Override
	public int compareTo(fileAndColor o) {
		return compareArrays(this.getArray(), o.getArray());
	}
	
	public int[] getArray() {
		int[] values = new int[colors.length * colors[0].length];
		for(int i=0;i<colors.length;i++) {
			for(int j=0;j<colors[0].length;j++) {
				values[i * j] = colors[i][j].getRGB();
			}
		}
		return values;
	}
	
	public int compareArrays(int[] arr1, int[] arr2) {
        long c1 = 0, c2 = 0;
        for(int i=0;i<arr1.length;i++) {
        	c1 += arr1[i];
        	c2 += arr2[i];
        }
        if(c1 == c2)
        	return 0;
        else if(c1 < c2)
        	return 1;
        return -1;
    }
	
	public int getTimesUsed() {
		return timesUsed;
	}
	
	public void increaseTimesUsed() {
		this.timesUsed += 1;
	}
}