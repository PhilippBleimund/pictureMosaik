package saveObjects;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;

public class DatabaseObj {

	
	public fileAndColor[] filesAndColors;
	public File[] files;
	public Color[] colors;
	
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
		files = new File[filesAndColors.length];
		colors = new Color[filesAndColors.length];
		for(int i=0;i<filesAndColors.length;i++) {
			files[i] = filesAndColors[i].file;
			colors[i] = filesAndColors[i].color;
		}
	}
	
	
	
	/**
	 * Method from https://www.happycoders.eu/de/algorithmen/mergesort/#Mergesort_Java_Quellcode
	 * Sadly the Arrays.sort Method cant be used since the Arrays File[] and Color[] are connected
	 * 
	 * @param elements
	 */
	private fileAndColor[] sort(fileAndColor[] elements) {
	    int length = elements.length;
	    fileAndColor[] sorted = mergeSort(elements, 0, length - 1);
	    //arraycopy to prevent any pointer confusion
	    fileAndColor[] finalArr = new fileAndColor[elements.length];
	    System.arraycopy(sorted, 0, finalArr, 0, length);
	    return finalArr;
	}

	private fileAndColor[] mergeSort(fileAndColor[] elements, int left, int right) {
		// End of recursion reached?
		if (left == right) return new fileAndColor[]{elements[left]};

	    int middle = left + (right - left) / 2;
	    fileAndColor[] leftArray = mergeSort(elements, left, middle);
	    fileAndColor[] rightArray = mergeSort(elements, middle + 1, right);
	    return merge(leftArray, rightArray);
	}

	private fileAndColor[] merge(fileAndColor[] leftArray, fileAndColor[] rightArray) {
	    int leftLen = leftArray.length;
	    int rightLen = rightArray.length;
	
	    fileAndColor[] target = new fileAndColor[leftLen + rightLen];
	    int targetPos = 0;
	    int leftPos = 0;
	    int rightPos = 0;
	
	    // As long as both arrays contain elements...
	    while (leftPos < leftLen && rightPos < rightLen) {
	      // Which one is smaller?
	    	fileAndColor leftValue = leftArray[leftPos];
	    	fileAndColor rightValue = rightArray[rightPos];
	    	if (leftValue.color.getRGB() <= rightValue.color.getRGB()) {
	    		target[targetPos++] = leftValue;
	    		leftPos++;
	    	} else {
	    		target[targetPos++] = rightValue;
	    		rightPos++;
	    	}
	    }
	    // Copy the rest
	    while (leftPos < leftLen) {
	    	target[targetPos++] = leftArray[leftPos++];
	    }
	    while (rightPos < rightLen) {
	    	target[targetPos++] = rightArray[rightPos++];
	    }
	    return target;
	}
}
