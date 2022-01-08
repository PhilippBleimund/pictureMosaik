package saveObjects;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseObj {

	
	public fileAndColor[] filesAndColors;

	/**
	 * The two dimensional Array represents the splitted Image.
	 * The compability refers to the index of the best image 
	 */
	public int[][] compability;
	
	public DatabaseObj() {

	}

	public DatabaseObj(File[] files, Color[] colors) {
		fileAndColor[] fileAndColorArr = new fileAndColor[files.length];
		for(int i=0;i<files.length;i++) {
			fileAndColorArr[i] = new fileAndColor(files[i], colors[i]);
 		}
		Arrays.sort(fileAndColorArr);
		filesAndColors = new fileAndColor[fileAndColorArr.length];
		System.arraycopy(fileAndColorArr, 0, filesAndColors, 0, fileAndColorArr.length);
	}
}
