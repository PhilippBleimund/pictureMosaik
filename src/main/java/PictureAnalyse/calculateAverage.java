package PictureAnalyse;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class calculateAverage {
	
	public calculateAverage() {
		
	}
	
	public Color getAverage(BufferedImage bitmap, Method method) {
		
		int increase = determitIncrease(method, bitmap);
		
		long redBucket = 0;
		long greenBucket = 0;
		long blueBucket = 0;
		long pixelCount = bitmap.getHeight() * bitmap.getWidth();

		for (int y = 0; y < bitmap.getHeight(); y+=increase){
		    for (int x = 0; x < bitmap.getWidth(); x+=increase){
		        Color c = new Color(bitmap.getRGB(x, y));

		        redBucket += (long)c.getRed();
		        greenBucket += (long)c.getGreen();
		        blueBucket += (long)c.getBlue();
		    }
		}
		System.out.println(redBucket / pixelCount + " " +
		                                greenBucket / pixelCount + " " +
		                                blueBucket / pixelCount);
		return new Color((int)(redBucket / pixelCount), (int)(greenBucket / pixelCount), (int)(blueBucket / pixelCount));
	}
	
	public int determitIncrease(Method method, BufferedImage image) {
		if(method == Method.AUTOMATIC) {
			int pixelCount = image.getHeight() * image.getWidth();
			if(pixelCount > 50000000)
				method = Method.SPEED;
			else if(pixelCount > 20000000) 
				method = Method.BALANCED;
			else if(pixelCount > 5000000)
				method = Method.QUALITY;
			else
				method = Method.ULTRA_QUALITY;
		}
		switch (method) {
		case SPEED:
			return 8;
		case BALANCED:
			return 4;
		case QUALITY:
			return 2;
		case ULTRA_QUALITY:
			return 1;
		}
		return 1;
	}
	
	public static Method ScalrToThis(Scalr.Method method){
		return Method.values()[method.ordinal()];
	}
	
	public enum Method{
		AUTOMATIC,
		SPEED,
		BALANCED,
		QUALITY,
		ULTRA_QUALITY
	}
}
