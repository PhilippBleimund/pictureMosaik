package testMode.Config;

import java.awt.Dimension;

import org.imgscalr.Scalr.Method;

public class ImagesConfig {
	private int count;
	private Dimension size;
	private Dimension newSize;
	private Method method;
	private Type type;
	
	public ImagesConfig(int count, int width, int height, int newWidth, int newHeight, Method method, Type type) {
		this(count, new Dimension(width, height), new Dimension(newWidth, newHeight), method, type);
	}
	
	public ImagesConfig(int count, Dimension size, Dimension newSize, Method method, Type type) {
		this.count = count;
		this.size = size;
		this.newSize = newSize;
		this.method = method;
		this.type = type;
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
	
	public Type getType() {
		return type;
	}
	
	public enum Type{
		COLOR,
		DATABASE,
		DOWNRENDER
	}
}
