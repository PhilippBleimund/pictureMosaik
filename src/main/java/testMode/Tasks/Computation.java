package testMode.Tasks;

import Computation.helper;

import java.awt.Color;
import java.io.File;
import java.util.Random;

import PictureAnalyse.compareColor;
import saveObjects.DatabaseObj;
import testMode.TestModeManager;
import testMode.Config.ComputationConfig;

public class Computation implements Runnable{

	ComputationConfig config;
	
	public Computation(ComputationConfig config) {
		this.config = config;
	}
	
	private void testComputation(ComputationConfig config) {
		TestModeManager.getInstance().Log("start Test: Computation", false);
		
		Color[][] averageColorSections = new Color[config.getSectionsX()][config.getSectionsY()];
		Random rnd = new Random();
		for(int i=0;i<averageColorSections.length;i++) {
			for(int j=0;j<averageColorSections[0].length;j++) {
				averageColorSections[i][j] = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
			}
		}
		
		File[] images = new File[config.getCount()];
		Color[] averageColorFiles = new Color[config.getCount()];
		for(int i=0;i<averageColorFiles.length;i++) {
			averageColorFiles[i] = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
		}
		
		DatabaseObj[] Obj = new DatabaseObj[1];
		Obj[0] = new DatabaseObj(images, averageColorFiles);
		compareColor compare = new compareColor();
		
		TestModeManager.getInstance().Log("prepared Images, OriginalImage and Database", false);
		long timeComputation0 = System.nanoTime();
        File[][] choosen = compare.compare(averageColorSections, Obj,  config.getMaxUsage());
		long timeComputation1 = System.nanoTime();
		Log(timeComputation1 - timeComputation0);
	}
	
	private void Log(long time) {
		TestModeManager.getInstance().Log("time elapsed: " + (time) + "ns", false);
		TestModeManager.getInstance().Log("time elapsed: " + helper.nanoToString(time), false);
		TestModeManager.getInstance().LogToJSON(Long.toString(time));
	}
	
	@Override
	public void run() {
		testComputation(this.config);
		TestModeManager.getInstance().Log("", true);
		TestModeManager.getInstance().Log("-+-+-+-", true);
		TestModeManager.getInstance().Log("", true);
		TestModeManager.getInstance().nextTask();
	}
}
