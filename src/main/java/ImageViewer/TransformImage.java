package ImageViewer;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

public class TransformImage {

	
	public TransformImage() {
		
	}
	
	public static BufferedImage addTransparency(BufferedImage work, BufferedImage original, float valueTransparent) {
		Graphics2D g = work.createGraphics();
		int rule = AlphaComposite.SRC_OVER;
    	Composite comp = AlphaComposite.getInstance(rule , ((float)valueTransparent)/100);
    	g.setComposite(comp);
    	    	
    	g.drawImage(original, 0, 0, null);
    	g.dispose();
    	return work;
	}
}
