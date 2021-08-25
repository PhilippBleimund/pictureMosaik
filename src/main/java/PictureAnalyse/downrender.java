package PictureAnalyse;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;

import saveObjects.ImageWithName;
import saveObjects.ScaledImages;

public class downrender {
	
	public downrender() {
		
	}
	
	public void scaleImage(ScaledImages AllImages, Point location, File locationImage, int x, int y, Scalr.Method method) {
		
		BufferedImage bi = prepareImage(locationImage);
        
		Dimensions D = getDimension(bi, x, y);
		
		Point p = AllImages.exists(locationImage.getName());
		
		if(p == null) {
			
			BufferedImage resize = null;
			
			resize = Scalr.resize(bi, method, Scalr.Mode.FIT_EXACT, D.width, D.height);

			BufferedImage croped = cropImage(resize, D, x, y);

			AllImages.Array[location.x][location.y] = new ImageWithName(croped, locationImage.getName());
		}else {
			AllImages.Array[location.x][location.y] = AllImages.Array[p.x][p.y];
		}
	}
	
	public BufferedImage cropImage(BufferedImage Image, Dimensions dm, int x, int y) {
		
		BufferedImage croped = null;
		
		//TODO in the future smart cropping
		if(dm.axis == 'x') {
			//calculate the y value
			int newY = (dm.height/2) - (y/2);
			croped = Image.getSubimage(0, newY, dm.width, y);
		}else if(dm.axis == 'y') {
			//calculate the x value
			int newX = (dm.width/2) - (x/2);
			croped = Image.getSubimage(newX, 0, x, dm.height);
		}
		BufferedImage copyOfImage = new BufferedImage(croped.getWidth(), croped.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = copyOfImage.createGraphics();
		g.drawImage(croped, 0, 0, null);
		return copyOfImage; //or use it however you want
	}
	
	public Dimensions getDimension(BufferedImage bi, int x, int y) {
		int xBi = bi.getWidth();
		int yBi = bi.getHeight();
		
		//scale xBi to x
		double scaleX = (double)x / (double)xBi;
		int newY = (int)(yBi * scaleX);
		if(newY >= y) {
			//scaling on x is the right choice
			return new Dimensions(x, newY, 'x');
		}
		
		//scale yBi to y
		double scaleY = (double)y / (double)yBi;
		int newX = (int)(xBi * scaleY);
		if(newX >= x) {
			//scaling on y is the right choice
			return new Dimensions(newX, y, 'y');
		}
		return null;
	}
	
	public String getFileFormat(String name) {
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
	
	class Dimensions{
		int width;
		int height;
		
		char axis;
		
		public Dimensions() {
			this(0, 0);
		}
		
		public Dimensions(int width, int height) {
			this(width, height, 'a');
		}
		
		public Dimensions(int width, int height, char axis) {
			this.width = width;
			this.height = height;
			this.axis = axis;
		}
	}
	
	// Inner class containing image information
	class ImageInformation {
	    public final int orientation;
	    public final int width;
	    public final int height;

	    public ImageInformation(int orientation, int width, int height) {
	        this.orientation = orientation;
	        this.width = width;
	        this.height = height;
	    }

	    public String toString() {
	        return String.format("%dx%d,%d", this.width, this.height, this.orientation);
	    }
	}


	public ImageInformation readImageInformation(File imageFile)  throws IOException, MetadataException, ImageProcessingException {
	    Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
	    Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
	    JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

	    int orientation = 1;
	    try {
	        orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
	    } catch (MetadataException me) {
	        me.printStackTrace();
	    }
	    int width = jpegDirectory.getImageWidth();
	    int height = jpegDirectory.getImageHeight();

	    return new ImageInformation(orientation, width, height);
	}
	
	public AffineTransform getExifTransformation(ImageInformation info) {

	    AffineTransform t = new AffineTransform();

	    switch (info.orientation) {
	    case 1:
	        break;
	    case 2: // Flip X
	        t.scale(-1.0, 1.0);
	        t.translate(-info.width, 0);
	        break;
	    case 3: // PI rotation 
	        t.translate(info.width, info.height);
	        t.rotate(Math.PI);
	        break;
	    case 4: // Flip Y
	        t.scale(1.0, -1.0);
	        t.translate(0, -info.height);
	        break;
	    case 5: // - PI/2 and Flip X
	        t.rotate(-Math.PI / 2);
	        t.scale(-1.0, 1.0);
	        break;
	    case 6: // -PI/2 and -width
	        t.translate(info.height, 0);
	        t.rotate(Math.PI / 2);
	        break;
	    case 7: // PI/2 and Flip
	        t.scale(-1.0, 1.0);
	        t.translate(-info.height, 0);
	        t.translate(0, info.width);
	        t.rotate(  3 * Math.PI / 2);
	        break;
	    case 8: // PI / 2
	        t.translate(0, info.width);
	        t.rotate(  3 * Math.PI / 2);
	        break;
	    }

	    return t;
	}
	
	public BufferedImage transformImage(BufferedImage image, AffineTransform transform) throws Exception {

	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

	    BufferedImage destinationImage = op.createCompatibleDestImage(image, (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image.getColorModel() : null );
	    Graphics2D g = destinationImage.createGraphics();
	    g.setBackground(Color.WHITE);
	    g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
	    destinationImage = op.filter(image, destinationImage);
	    return destinationImage;
	}	
	
	public BufferedImage prepareImage(File location) {
		BufferedImage image = null;
		ImageInformation information;
		AffineTransform transform;
		try {
			image = ImageIO.read(location);
			//information = readImageInformation(location);
			//transform = getExifTransformation(information);
			//BufferedImage rotatedImage = transformImage(image, transform);
			//return rotatedImage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
}
