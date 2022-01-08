package PictureAnalyse;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class SplitPicture {
	
	public BufferedImage[][] SpitPictureAndSave(splitObj information) {
		BufferedImage[][] segmentArr = new BufferedImage[information.valueSectionsX.length][information.valueSectionsY.length];
		
		for(int i=0;i<information.coordsSplitsBorderX.length-1;i++) {
			for(int j=0;j<information.coordsSplitsBorderY.length-1;j++) {
				segmentArr[i][j] = information.image.getSubimage(information.coordsSplitsBorderX[i], information.coordsSplitsBorderY[j],
																 information.valueSectionsX[i], information.valueSectionsY[j]);
			}
		}
        return segmentArr;
	}
}
