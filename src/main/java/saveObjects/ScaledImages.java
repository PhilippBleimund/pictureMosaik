package saveObjects;

import java.awt.Point;

public class ScaledImages {

	public ImageWithName[][] Array;
	
	public ScaledImages(int x, int y) {
		Array = new ImageWithName[x][y];
	}
	
	public Point exists(String search) {
		for(int i=0;i<Array.length;i++) {
			for(int j=0;j<Array[0].length;j++) {
				if(Array[i][j] != null && Array[i][j].Name.equals(search)) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}
}
