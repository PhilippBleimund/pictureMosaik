package PictureAnalyse;

import java.awt.Color;

public class compareObj {
	
	public Color color;
	public int x;
	public int y;
	public int compability[];
	
	public compareObj(Color color, int[] compability, int x, int y) {
		this.color = color;
		this.compability = compability;
		this.x = x;
		this.y = y;
	}
}
