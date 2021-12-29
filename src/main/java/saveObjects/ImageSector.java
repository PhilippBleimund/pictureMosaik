package saveObjects;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Computation.computeAverageColor;
import Computation.smartSplitter;
import GUI.WindowManager;
import Manager.Renderer.Status;
import PictureAnalyse.SplitPicture;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.splitObj;

public class ImageSector {

	private BufferedImage[][] subSectors;
	private Color[][] avgColorSubSectors;
	private splitObj split;
	
	private int splitsX;
	private int splitsY;
	
	public Color[][] getColors(){
		return this.avgColorSubSectors;
	}
	
	public ImageSector(BufferedImage sector, calculateAverage.Method method) {
		splitsX = WindowManager.mainGuiInstance.subSplitsX;
		splitsY = WindowManager.mainGuiInstance.subSplitsY;
		smartSplitter smartSplitter = new smartSplitter();
		split = smartSplitter.splitImage(sector, splitsX, splitsY, 1);
		SplitPicture splitter = new SplitPicture();
		subSectors = splitter.SpitPictureAndSave(split);
		computeAverageColor colorCalculator = new computeAverageColor();
        avgColorSubSectors = colorCalculator.computeAverageColorSections(subSectors, method);
	}
}
