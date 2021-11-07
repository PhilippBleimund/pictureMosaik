package Computation;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;

public class helper {

	public helper() {
		
	}
	
	public static ArrayList<ArrayList<File>> listFilesForFolder(final File folder, ArrayList<ArrayList<File>> ImageList) {
		ArrayList<File> currentDirectory = new ArrayList<File>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry, ImageList);
	        } else {
	        	if(getFileFormat(fileEntry.getName()).equals("jpg"))
	        		currentDirectory.add(fileEntry);
	        	else if(getFileFormat(fileEntry.getName()).equals("png"))
	        		currentDirectory.add(fileEntry);
	        }
	    }
	    ImageList.add(currentDirectory);
	    return ImageList;
	}
	
	//not using substring for further implementation of more and longer file formats
	public static String getFileFormat(String name) {
		String FileFormat = "";
		for(int i=name.length()-1;i>0;i-=1) {
			if(name.charAt(i) != '.')
				FileFormat = FileFormat + name.charAt(i);
			else
				i = 0;
		}
		String reversed = "";
		for(int i=FileFormat.length()-1;i>-1;i-=1) {
			reversed = reversed + FileFormat.charAt(i);
		}
		return reversed;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
	   	 ColorModel cm  = bi.getColorModel();
	   	 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	   	 WritableRaster raster = bi.copyData(null);
	   	 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	   }
}
