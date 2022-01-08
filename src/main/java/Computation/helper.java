package Computation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class helper {

	public helper() {

	}

	public static ArrayList<ArrayList<File>> listFilesForFolder(final File folder,
			ArrayList<ArrayList<File>> ImageList) {
		ArrayList<File> currentDirectory = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry, ImageList);
			} else {
				if (getFileFormat(fileEntry.getName()).equals("jpg"))
					currentDirectory.add(fileEntry);
				else if (getFileFormat(fileEntry.getName()).equals("png"))
					currentDirectory.add(fileEntry);
			}
		}
		ImageList.add(currentDirectory);
		return ImageList;
	}

	// not using substring for further implementation of more and longer file
	// formats
	public static String getFileFormat(String name) {
		String FileFormat = "";
		for (int i = name.length() - 1; i > 0; i -= 1) {
			if (name.charAt(i) != '.')
				FileFormat = FileFormat + name.charAt(i);
			else
				i = 0;
		}
		String reversed = "";
		for (int i = FileFormat.length() - 1; i > -1; i -= 1) {
			reversed = reversed + FileFormat.charAt(i);
		}
		return reversed;
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static String nanoToString(long time) {
		long millis = time / 1000000;
		return formatMillis(millis);
	}

	static private String formatMillis(long val) {
		StringBuilder buf = new StringBuilder(20);
		String sgn = "";

		if (val < 0) {
			sgn = "-";
			val = Math.abs(val);
		}

		append(buf, sgn, 0, (val / 3600000));
		val %= 3600000;
		append(buf, ":", 2, (val / 60000));
		val %= 60000;
		append(buf, ":", 2, (val / 1000));
		val %= 1000;
		append(buf, ".", 3, (val));
		return buf.toString();
	}

	/**
	 * Append a right-aligned and zero-padded numeric value to a `StringBuilder`.
	 */
	static private void append(StringBuilder tgt, String pfx, int dgt, long val) {
		tgt.append(pfx);
		if (dgt > 1) {
			int pad = (dgt - 1);
			for (long xa = val; xa > 9 && pad > 0; xa /= 10) {
				pad--;
			}
			for (int xa = 0; xa < pad; xa++) {
				tgt.append('0');
			}
		}
		tgt.append(val);
	}
	
	public static double[] interpolate(double[] xValues, double[] yValues) {
		LinearInterpolator inperpolator = new LinearInterpolator();
		PolynomialSplineFunction interpolate = inperpolator.interpolate(xValues, yValues);
		double[] linear = new double[xValues.length];
		for(int i=0;i<xValues.length;i++) {
			linear[i] = interpolate.value(xValues[i]);
		}
		return linear;
	}
	
	public static Dimension getDimension(File F) {
		try(ImageInputStream in = ImageIO.createImageInputStream(F)){
		    final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
		    if (readers.hasNext()) {
		        ImageReader reader = readers.next();
		        try {
		            reader.setInput(in);
		            return new Dimension(reader.getWidth(0), reader.getHeight(0));
		        } finally {
		            reader.dispose();
		        }
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
