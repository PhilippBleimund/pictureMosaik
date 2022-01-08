package PictureAnalyse;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.imgscalr.Scalr;

import Manager.customThreadFactory;
import saveObjects.ScaledImages;

public class downrenderFiles {

	public downrenderFiles() {
		
	}
	
	public ScaledImages downrenderFilesAndSave(File[][] choosen, splitObj imageData, Scalr.Method method) {
		ScaledImages AllImages = new ScaledImages(choosen.length, choosen[0].length);
		
		int cores = Runtime.getRuntime().availableProcessors();
    	
		ExecutorService pool = Executors.newFixedThreadPool(cores, new customThreadFactory());
		
		class renderImageDown implements Runnable{

			int X;
			int Y;
			File image;
			downrender render;
			
			public renderImageDown(File image, int X, int Y) {
				this.image = image;
				this.X = X;
				this.Y = Y;
				render = new downrender();
			}
			
			@Override
			public void run() {
				render.scaleImage(AllImages, new Point(X, Y), image, new Dimension((imageData.multipliedValuesSectionsX[X]), (imageData.multipliedValuesSectionsY[Y])), method);
			}
		}
		
		for(int i=0;i<choosen.length;i++) {
			for(int j=0;j<choosen[0].length;j++) {
				pool.execute(new renderImageDown(choosen[i][j], i, j));
			}
		}
		pool.shutdown();
    	try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return AllImages;
	}
}
