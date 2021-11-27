package testMode.Tasks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import Computation.computeAverageColor;
import Computation.helper;
import Computation.smartSplitter;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.downrenderFiles;
import PictureAnalyse.splitObj;
import saveObjects.DatabaseObj;
import testMode.TestModeManager;
import testMode.Config.ImagesConfig;

public class ImagesTest implements Runnable{

	ImagesConfig config;
	File[] images;
	
	public ImagesTest(ImagesConfig config) {
		this.config = config;
	}
	
	private long testAverageColor(ImagesConfig config, File[] images) {
		computeAverageColor colorCalculator = new computeAverageColor();
		
		TestModeManager.getInstance().Log("calculate average Color", false);
		long timeColor0 = System.nanoTime();
		Color[] averageColorFiles = colorCalculator.computeAverageColorFiles(images, calculateAverage.ScalrToThis(config.getMethod()));
		long timeColor1 = System.nanoTime();
		Log(timeColor1 - timeColor0);
		return timeColor1 - timeColor0;
	}
	
	private long testDatabase(ImagesConfig config) {
		File[] images = new File[config.getCount()];
		Color[] averageColorFiles = new Color[config.getCount()];
		Random rnd = new Random();
		for(int i=0;i<averageColorFiles.length;i++) {
			averageColorFiles[i] = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
		}
		
		TestModeManager.getInstance().Log("create new Database", false);
		long timeDatabase0 = System.nanoTime();
		DatabaseObj Obj = new DatabaseObj(images, averageColorFiles);
		long timeDatabase1 = System.nanoTime();
		Log(timeDatabase1 - timeDatabase0);
		return timeDatabase1 - timeDatabase0;
	}
	
	private long testDownrender(ImagesConfig config, File[] images) {
		File[][] imagesChoosen = new File[images.length][1];
		for(int i=0;i<images.length;i++) {
			imagesChoosen[i][0] = images[i];
		}
		
		smartSplitter imageSplitter = new smartSplitter();
		BufferedImage image = new BufferedImage((int)config.getNewSize().getWidth(), (int)config.getNewSize().getHeight(), BufferedImage.TYPE_INT_ARGB);
		splitObj splitImage = imageSplitter.splitImage(image, images.length, 0, 1);
		
		downrenderFiles downrenderChoosen = new downrenderFiles();
		TestModeManager.getInstance().Log("downrender Images", false);
		long timerDownrender0 = System.nanoTime();
        downrenderChoosen.downrenderFilesAndSave(imagesChoosen, splitImage, config.getMethod());
        long timerDownrender1 = System.nanoTime();
        Log(timerDownrender1 - timerDownrender0);
        return timerDownrender1 - timerDownrender0;
	}

	private void Log(long time) {
		TestModeManager.getInstance().Log("time elapsed: " + (time) + "ns", false);
		TestModeManager.getInstance().Log("time elapsed: " + helper.nanoToString(time), false);
		TestModeManager.getInstance().LogToJSON(Long.toString(time));
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
		
		TestModeManager.getInstance().Log("", true);
		TestModeManager.getInstance().Log("-+-+-+-", true);
		TestModeManager.getInstance().Log("", true);
		TestModeManager.getInstance().nextTask();
	}
}
