package saveObjects;

import java.awt.Dimension;
import java.awt.Point;

public class ScaledImages {

	public ImageWithName[][] Array;
	
	public ScaledImages(int x, int y) {
		Array = new ImageWithName[x][y];
		for(int i=0;i<x;i++) {
			for(int j=0;j<y;j++) {
				Array[i][j] = new ImageWithName();
			}
		}
	}
	
	public Point exists(String search, Dimension dim) {
		for(int i=0;i<Array.length;i++) {
			for(int j=0;j<Array[0].length;j++) {
				if(Array[i][j].flaged == true && Array[i][j].Name.equals(search)
						&& Array[i][j].dim.equals(dim)) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}
}
