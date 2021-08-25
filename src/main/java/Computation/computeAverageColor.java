package Computation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import PictureAnalyse.calculateAverage;

public class computeAverageColor {

	private int cores;
	private ExecutorService pool;
	
	public computeAverageColor() {
		cores = Runtime.getRuntime().availableProcessors();
		pool = Executors.newFixedThreadPool(cores);
	}
	public Color[] computeAverageColorFiles(File[] files, calculateAverage.Method method) {
		Color[] averageColorFiles = new Color[files.length];;
		class AverageColorOfFile implements Runnable{
			
			int taskNum;
			File file;
			
	        calculateAverage pictureAverage;
			
			public AverageColorOfFile(int i, File file) {
				taskNum = i;
				this.file = file;
				pictureAverage = new calculateAverage();
			}
			
			@Override
			public void run() {
				
				/*long allocatedMemory      = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
				long presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;
				System.out.println(presumableFreeMemory);
				
				long filesize = file.length();
				
				int counter = 0;
				
				while(presumableFreeMemory < filesize) {
					allocatedMemory      = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
					presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					counter++;
					if(counter >= 2000)
						Thread.currentThread().interrupt();
				}*/
				
				BufferedImage b = null;
	        	try {
	                b = ImageIO.read(file);
	            } catch (IOException ex) {
	            	ex.printStackTrace();
	            }
	        	averageColorFiles[taskNum] = pictureAverage.getAverage(b, method);
			}
		}
		for(int i=0;i<files.length;i++) {
			pool.execute(new AverageColorOfFile(i, files[i]));
		}
		pool.shutdown();
    	try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	pool = Executors.newFixedThreadPool(cores);
    	
    	return averageColorFiles;
	}
	
	public Color[][] computeAverageColorSections(BufferedImage[][] BiArr) {
		Color[][] averageColorSections = new Color[BiArr.length][BiArr[0].length];
		class AverageColorOfSegment implements Runnable{

			int X;
			int Y;
			BufferedImage segmentBi;
			calculateAverage pictureAverage;
			
			public AverageColorOfSegment(int X, int Y, BufferedImage segmentBi) {
				this.X = X;
				this.Y = Y;
				this.segmentBi = segmentBi;
				this.pictureAverage = new calculateAverage();
			}
			
			@Override
			public void run() {
				averageColorSections[X][Y] = pictureAverage.getAverage(segmentBi, calculateAverage.Method.ULTRA_QUALITY);
			}
		}
		for(int i=0;i<BiArr.length;i++) {
			for(int j=0;j<BiArr[0].length;j++) {
				pool.execute(new AverageColorOfSegment(i, j, BiArr[i][j]));
			}
		}
		pool.shutdown();
    	try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	pool = Executors.newFixedThreadPool(cores); 
    	
    	return averageColorSections;
	}
}
