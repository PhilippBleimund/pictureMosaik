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
		int RGB = betterCompare(this.getArray(), o.getArray());
		if(RGB == 0)
			return 0;
		else if(RGB < 382)
			return -1;
		return 1;
	}
	
	public Color[] getArray() {
		Color[] values = new Color[colors.length * colors[0].length];
		for(int i=0;i<colors.length;i++) {
			for(int j=0;j<colors[0].length;j++) {
				values[i * j] = colors[i][j];
			}
		}
		return values;
	}
	
	public int betterCompare(Color[] arr1, Color[] arr2) {
		int rgb = 0;
		for(int i=0;i<arr1.length;i++) {
			int r = Math.abs(arr1[i].getRed() - arr2[i].getRed());
			int g = Math.abs(arr1[i].getGreen() - arr2[i].getGreen());
			int b = Math.abs(arr1[i].getBlue() - arr2[i].getBlue());
			rgb += r+g+b;
		}
		return rgb / arr1.length;
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