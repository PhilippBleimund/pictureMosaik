package saveObjects;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class ImageWithName {

	public BufferedImage Image;
	public Dimension dim;
	public String Name;
	public boolean flaged;
	
	public ImageWithName() {
		this.flaged = false;
	}
	
	public boolean flag(String Name, Dimension dim) {
		if(this.flaged == true)
			return false;
		this.dim = dim;
		this.Name = Name;
		this.flaged = true;
		return true;
	}
	
	public void set(BufferedImage Image) {
		this.Image = Image;
	}
	
	public ImageWithName(BufferedImage Image, String Name) {
		this.Image = Image;
		this.Name = Name;
		this.flaged = false;
	}
}
