package PictureAnalyse;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.imgscalr.Scalr;

public class splitObj {

	public int[] valueSectionsX;
	public int[] multipliedValuesSectionsX;
	public int[] valueSectionsY;
	public int[] multipliedValuesSectionsY;

	public int[] coordsSplitsX;
	public int[] multipliedCoordsSectionsX;
	public int[] coordsSplitsY;
	public int[] multipliedCoordsSectionsY;
	
	public int[] coordsSplitsBorderX;
	public int[] multipliesCoordsSplitsBorderX;
	public int[] coordsSplitsBorderY;
	public int[] multipliesCoordsSplitsBorderY;
	
	public int multiplier;
	
	public BufferedImage image;
	
	public splitObj(Dimension d, int[] ArrX, int[] ArrY, int multiplier, Method Method) {
		this.costructObject(d, ArrX, ArrY, multiplier, Method);
	}
	
	/**
	 * When Method.COORDS is used the upper bound of the picture must be included.
	 * For example the last coordinate of a 2000x1000 picture is 1500. Then the 2000 must be included as the last index in the array.
	 * Otherwise the method can't calculate the values properly.
	 * @param ArrX
	 * @param ArrY
	 * @param Method
	 */
	public splitObj(BufferedImage image, int[] ArrX, int[] ArrY, int multiplier, Method Method) {
		Dimension d = new Dimension(image.getWidth(), image.getHeight());
		this.costructObject(d, ArrX, ArrY, multiplier, Method);
		this.image = image;
	}
	
	private void costructObject(Dimension d, int[] ArrX, int[] ArrY, int multiplier, Method Method) {
		this.multiplier = multiplier;
		if(Method == Method.VALUE) {
			this.valueSectionsX = ArrX;
			this.valueSectionsY = ArrY;
			this.coordsSplitsX = mapValuesToCoords(ArrX);
			this.coordsSplitsY = mapValuesToCoords(ArrY);
		}else if(Method == Method.COORDS) {
			this.coordsSplitsX = removeLastCoord(ArrX);
			this.coordsSplitsY = removeLastCoord(ArrY);
			this.valueSectionsX = mapCoordsToValues(ArrX);
			this.valueSectionsY = mapCoordsToValues(ArrY);
		}
		this.coordsSplitsBorderX = addBordersToCoords(this.coordsSplitsX, (int) d.getWidth());
		this.coordsSplitsBorderY = addBordersToCoords(this.coordsSplitsY, (int) d.getHeight());
		this.multipliedValuesSectionsX = applyMultiplierToValues(this.valueSectionsX, this.multiplier);
		this.multipliedValuesSectionsY = applyMultiplierToValues(this.valueSectionsY, this.multiplier);
		this.multipliedCoordsSectionsX = mapValuesToCoords(this.multipliedValuesSectionsX);
		this.multipliedCoordsSectionsY = mapValuesToCoords(this.multipliedValuesSectionsY);
		this.multipliesCoordsSplitsBorderX = addBordersToCoords(this.multipliedCoordsSectionsX, (int) (d.getWidth() * multiplier));
		this.multipliesCoordsSplitsBorderY = addBordersToCoords(this.multipliedCoordsSectionsY, (int) (d.getHeight() * multiplier));
	}
	
	private int[] applyMultiplierToValues(int[] values, int multiplier) {
		int[] multipliedValues = new int[values.length];
		for(int i=0;i<values.length;i++) {
			multipliedValues[i] = values[i] * multiplier;
		}
		return multipliedValues;
	}
	
	private int[] mapValuesToCoords(int[] values) {
		int coords[] = new int[values.length - 1];
		for(int i=0;i<coords.length;i++) {
			if(i == 0)
				coords[i] = values[i];
			else
			coords[i] = values[i] + coords[i-1];
		}
		return coords;
	}
	
	/**
	 * The coords inputed in this methods need to include the upper bound of the picture too. This will be removed in the construction process.
	 * @param coords
	 * @return
	 */
	private int[] mapCoordsToValues(int[] coords) {
		int values[] = new int[coords.length];
		int leftBorder = 0;
		for(int i=0;i<values.length;i++) {
			values[i] = coords[i] - leftBorder;
			leftBorder = coords[i];
		}
		return values;
	}
	
	private int[] removeLastCoord(int[] coords) {
		int[] newCoords = Arrays.copyOf(coords, coords.length-1);
		return newCoords;
	}
	
	private int[] addBordersToCoords(int[] coords, int boorder) {
		int[] newCoords = new int[coords.length + 2];
		for(int i=0;i<coords.length;i++) {
			newCoords[i+1] = coords[i];
		}
		newCoords[0] = 0;
		newCoords[newCoords.length-1] = boorder;
		return newCoords;
	}
	
	public enum Method{
		/**
		 * Init the splitObj with the values of sections. Coordinates are generated automatically.
		 */
		VALUE,
		/**
		 * Init the splitObj with the coordinates of the splits. The values are generated.
		 */
		COORDS
	}
}