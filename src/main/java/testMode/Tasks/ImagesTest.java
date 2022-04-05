package testMode.Tasks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import Computation.computeAverageColor;
import Computation.helper;
import Computation.smartSplitter;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.downrenderFiles;
import PictureAnalyse.splitObj;
import saveObjects.DatabaseObj;
import testMode.JSONObj;
import testMode.TestModeManager;
import testMode.Config.ImagesConfig;

public class ImagesTest implements Runnable{

	ImagesConfig config;
	File[] images;
	int TestId;
	
	public ImagesTest(ImagesConfig config, int TestId) {
		this.config = config;
		this.TestId = TestId;
	}
	
	private long testAverageColor(ImagesConfig config, File[] images) {
		computeAverageColor colorCalculator = new computeAverageColor();
		
		long timeColor0 = System.nanoTime();
		Color[] averageColorFiles = colorCalculator.computeAverageColorFiles(images, calculateAverage.ScalrToThis(config.getMethod()));
		long timeColor1 = System.nanoTime();
		Log(timeColor1 - timeColor0, 5);
		return timeColor1 - timeColor0;
	}
	
	private long testDatabase(ImagesConfig config) {
		File[] images = new File[config.getCount()];
		Color[] averageColorFiles = new Color[config.getCount()];
		Random rnd = new Random();
		for(int i=0;i<averageColorFiles.length;i++) {
			averageColorFiles[i] = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
		}
		
		long timeDatabase0 = System.nanoTime();
		DatabaseObj Obj = new DatabaseObj(images, averageColorFiles);
		long timeDatabase1 = System.nanoTime();
		Log(timeDatabase1 - timeDatabase0, 50);
		return timeDatabase1 - timeDatabase0;
	}
	
	private long testDownrender(ImagesConfig config, File[] images) {
		File[][] imagesChoosen = new File[images.length][1];
		for(int i=0;i<images.length;i++) {
			imagesChoosen[i][0] = images[i];
		}
		
		smartSplitter imageSplitter = new smartSplitter();
		Dimension d = new Dimension((int)config.getNewSize().getWidth()*images.length, (int)config.getNewSize().getHeight());
		splitObj splitImage = imageSplitter.splitImage(null, d, images.length, 0, 1);
		
		downrenderFiles downrenderChoosen = new downrenderFiles();
		long timerDownrender0 = System.nanoTime();
        downrenderChoosen.downrenderFilesAndSave(imagesChoosen, splitImage, config.getMethod());
        long timerDownrender1 = System.nanoTime();
        Log(timerDownrender1 - timerDownrender0, 1);
        return timerDownrender1 - timerDownrender0;
	}

	private void Log(long time, int threshold) {
		if(this.TestId % threshold == 0) {
			TestModeManager.getInstance().Log("Iteration: "+ TestId + " time: " + (time) + "ns", false);
			TestModeManager.getInstance().Log("Iteration: "+ TestId + " time: " + helper.nanoToString(time), false);
			TestModeManager.getInstance().Log("", true);
			TestModeManager.getInstance().Log("-+-+-+-", true);
			TestModeManager.getInstance().Log("", true);
		}
		TestModeManager.getInstance().LogToJSON(JSONObj.TimeValuesKey, Long.toString(time));
		Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory() - runtime.totalMemory();
        TestModeManager.getInstance().LogToJSON(JSONObj.RamValuesKey, Long.toString(memory));
        runtime.gc();
	}
	
	@Override
	public void run() {
		this.images = TestModeManager.getInstance().getImagesFile();
		
		switch(config.getType()) {
		case COLOR:
			testAverageColor(config, images);
			break;
		case DATABASE:
			testDatabase(config);
			break;
		case DOWNRENDER:
			testDownrender(config, images);
			break;
		}
		
		TestModeManager.getInstance().nextTask();
	}
}
