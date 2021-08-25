package Computation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import PictureAnalyse.SplitPicture;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.compareColor;
import PictureAnalyse.downrenderFiles;
import PictureAnalyse.mergeMosaik;
import PictureAnalyse.splitObj;
import saveObjects.DatabaseObj;
import saveObjects.FolderSave;
import saveObjects.ScaledImages;


public class Renderer extends SwingWorker{
	
	private splitObj imageData;
	private FolderSave FolderData;
	private int maxRepetition;
	private boolean transparent;
	private int valueTransparent;
	private Scalr.Method method;
	
	private List<ProgressBarListener> Listeners = new ArrayList<ProgressBarListener>();
	
	public Renderer(splitObj information, FolderSave FolderData, int maxRepetition, Scalr.Method method) {
		imageData = information;
		this.FolderData = FolderData;
		this.maxRepetition = maxRepetition;
		this.transparent = transparent;
		this.valueTransparent = valueTransparent;
		this.method = method;
	}
	
	public void addListener(ProgressBarListener listener) {
		Listeners.add(listener);
	}
	
	public enum Status{
		SPLITTER,
		AVERAGE_COLOR_PICTURE,
		AVERAGE_COLOR_FILES,
		DATABASE_MERGE,
		COMPUTATION,
		DOWNRENDER_IMAGES,
		MERGE_IMAGES,
		DONE
	}
	
	private void nodifyListener(Renderer.Status s) {
		for(ProgressBarListener L : Listeners) {
			L.changeProgressBarStatus(s);
		}
	}
	
	@Override
	protected Object doInBackground() throws Exception {
		
		long timer1 = System.nanoTime();
		
		SplitPicture splitter = new SplitPicture();
        BufferedImage[][] biArr;
        
        nodifyListener(Status.SPLITTER);
        biArr = splitter.SpitPictureAndSave(imageData);
        
        
        computeAverageColor colorCalculator = new computeAverageColor();
        
        nodifyListener(Status.AVERAGE_COLOR_PICTURE);
        Color[][] averageColorSections = colorCalculator.computeAverageColorSections(biArr);
        
        if(FolderData.selectedImages != null) {
        	nodifyListener(Status.AVERAGE_COLOR_FILES);
        	Color[] averageColorFiles = colorCalculator.computeAverageColorFiles(FolderData.selectedImages, calculateAverage.ScalrToThis(method));
        	
        	DatabaseObj Obj = new DatabaseObj(FolderData.selectedImages, averageColorFiles);
        	FolderData.selectedDatabasesList.add(Obj);
        }
		
		DatabaseObj[] DatabaseArray = new DatabaseObj[FolderData.selectedDatabasesList.size()];
		nodifyListener(Status.DATABASE_MERGE);
		FolderData.selectedDatabasesList.toArray(DatabaseArray);
		
		compareColor compare = new compareColor();
		nodifyListener(Status.COMPUTATION);
        File[][] choosen = compare.compare(averageColorSections, DatabaseArray,  maxRepetition);
        
        ScaledImages AllImages;
        
        downrenderFiles downrenderChoosen = new downrenderFiles();
        nodifyListener(Status.DOWNRENDER_IMAGES);
        AllImages = downrenderChoosen.downrenderFilesAndSave(choosen, imageData, method);
        
        System.out.println("downrender");
        
        mergeMosaik merger = new mergeMosaik();
        nodifyListener(Status.MERGE_IMAGES);
        merger.mergeMosaikAndSave(AllImages, imageData, choosen);
        
        long timer2 = System.nanoTime();
        
        System.out.println("Zeit in Nanosekunden: "+ (timer2 - timer1));
        
        nodifyListener(Status.DONE);
		return null;
	}
	
	private Object[] concatWithCollection(Object[] array1, Object[] array2) {
	    List<Object> resultList = new ArrayList<>(array1.length + array2.length);
	    Collections.addAll(resultList, array1);
	    Collections.addAll(resultList, array2);

	    Object[] resultArray = (Object[]) Array.newInstance(array1.getClass().getComponentType(), 0);
	    return resultList.toArray(resultArray);
	}
}
