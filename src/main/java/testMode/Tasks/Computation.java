package testMode.Tasks;

import Computation.helper;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import PictureAnalyse.compareColor;
import saveObjects.DatabaseObj;
import testMode.JSONObj;
import testMode.TestModeManager;
import testMode.Config.ComputationConfig;
import testMode.Config.TestConfig;

public class Computation implements Runnable{

	TestConfig config;
	
	public Computation(TestConfig config) {
		this.config = config;
	}
	
	private void testComputation(TestConfig AllConfig) {
		ComputationConfig config = AllConfig.getComputation();
		TestModeManager.getInstance().Log("start Test: Computation", false);
		
		Color[][] averageColorSections = new Color[config.getSectionsX()][config.getSectionsY()];
		Random rnd = new Random();
		for(int j=0;j<averageColorSections.length;j++) {
			for(int k=0;k<averageColorSections[0].length;k++) {
				averageColorSections[j][k] = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
			}
		}

		ArrayList<Color> averageColorFiles = new ArrayList<Color>();
		for(int j=0;j<config.getCount();j++) {
			averageColorFiles.add(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
		}

		for(int i=0;i<AllConfig.getRepeat();i++) {
			
			
			for(int j=0;j<AllConfig.getIncrease();j++) {
				averageColorFiles.add(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
			}
			File[] images = new File[averageColorFiles.size()];
			
			Color[] colorArray = new Color[averageColorFiles.size()];
			averageColorFiles.toArray(colorArray);
			
			DatabaseObj[] Obj = new DatabaseObj[1];
			Obj[0] = new DatabaseObj(images, colorArray);
			compareColor compare = new compareColor();
			
			long timeComputation0 = System.nanoTime();
	        File[][] choosen = compare.compare(averageColorSections, Obj,  config.getMaxUsage());
			long timeComputation1 = System.nanoTime();
			Log(timeComputation1 - timeComputation0,i);
		}
	}
	
	private void Log(long time, int ite) {
		if(ite % 50 == 0) {
			TestModeManager.getInstance().Log("Iteration: "+ ite + " time: "+ (time) + "ns", false);
			TestModeManager.getInstance().Log("Iteration: "+ ite + " time: " + helper.nanoToString(time), false);
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
		testComputation(this.config);
		TestModeManager.getInstance().Log("", true);
		TestModeManager.getInstance().Log("-+-+-+-", true);
		TestModeManager.getInstance().Log("", true);
		TestModeManager.getInstance().nextTask();
	}
}
