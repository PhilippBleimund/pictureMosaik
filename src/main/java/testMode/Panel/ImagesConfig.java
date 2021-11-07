package testMode.Panel;

import java.awt.Dimension;

import org.imgscalr.Scalr.Method;

public class ImagesConfig {
	private int count;
	private Dimension size;
	private Dimension newSize;
	private Method method;
	
	public ImagesConfig(int count, int width, int height, int newWidth, int newHeight, Method method) {
		this(count, new Dimension(width, height), new Dimension(newWidth, newHeight), method);
	}
	
	public ImagesConfig(int count, Dimension size, Dimension newSize, Method method) {
		this.count = count;
		this.size = size;
		this.newSize = newSize;
		this.method = method;
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
