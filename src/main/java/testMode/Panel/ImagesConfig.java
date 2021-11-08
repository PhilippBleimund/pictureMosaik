package testMode.Panel;

import java.awt.Dimension;

import org.imgscalr.Scalr.Method;

public class ImagesConfig {
	private int count;
	private Dimension size;
	private Dimension newSize;
	private Method method;
	private boolean Color;
	private boolean Databse;
	private boolean downrender;
	
	public ImagesConfig(int count, int width, int height, int newWidth, int newHeight, Method method, boolean Color, boolean Database, boolean downrender) {
		this(count, new Dimension(width, height), new Dimension(newWidth, newHeight), method, Color, Database, downrender);
	}
	
	public ImagesConfig(int count, Dimension size, Dimension newSize, Method method, boolean Color, boolean Database, boolean downrender) {
		this.count = count;
		this.size = size;
		this.newSize = newSize;
		this.method = method;
		this.Color = Color;
		this.Databse = Database;
		this.downrender = downrender;
	}

	public boolean isColor() {
		return Color;
	}

	public boolean isDatabse() {
		return Databse;
	}

	public boolean isDownrender() {
		return downrender;
	}

	public int getCount() {
		return count;
	}

	public Dimension getSize() {
		return size;
	}
	
	public Dimension getNewSize() {
		return newSize;
	}
	
	public Method getMethod() {
		return method;
	}
}
