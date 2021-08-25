package PictureAnalyse;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import Computation.helper;
import ImageViewer.ImageViewerUI;
import saveObjects.ScaledImages;

public class mergeMosaik {

	downrender render = new downrender();
	
	public mergeMosaik() {
		
	}
	
	public void mergeMosaikAndSave(ScaledImages AllImages, splitObj imageData, File[][] choosen) {
		BufferedImage combined = new BufferedImage(imageData.image.getWidth() * imageData.multiplier, imageData.image.getHeight() * imageData.multiplier, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combined.createGraphics();
    	
    	BufferedImage rezise = null;
    	
        for(int i=0;i<choosen.length;i++) {
        	for(int j=0;j<choosen[0].length;j++) {
        		g.drawImage(AllImages.Array[i][j].Image, imageData.multipliesCoordsSplitsBorderX[i], imageData.multipliesCoordsSplitsBorderY[j], null);
        	}
        }
        
        g.dispose();
        
        ImageViewerUI viewer = new ImageViewerUI(imageData.image, combined);
        viewer.setVisible(true);
	}
}
