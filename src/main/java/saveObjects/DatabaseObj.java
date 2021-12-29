package saveObjects;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseObj {

	
	public fileAndColor[] filesAndColors;
	public File[] files;
	
	/**
	 * The two dimensional Array represents the splitted Image.
	 * The compability refers to the index of the best image 
	 */
	public int[][] compability;
	
	public DatabaseObj() {
		
	}
	
	public DatabaseObj(File[] files, ArrayList<Color[][]> colors) {
		fileAndColor[] fileAndColorArr = new fileAndColor[files.length];
		for(int i=0;i<files.length;i++) {
			fileAndColorArr[i] = new fileAndColor(files[i], colors.get(i));
 		}
		Arrays.sort(fileAndColorArr);
		filesAndColors = new fileAndColor[fileAndColorArr.length];
		System.arraycopy(fileAndColorArr, 0, filesAndColors, 0, fileAndColorArr.length);
		this.files = new File[filesAndColors.length];
		for(int i=0;i<filesAndColors.length;i++) {
			files[i] = filesAndColors[i].file;
		}
	}
}
