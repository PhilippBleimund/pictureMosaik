package Manager;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingWorker;

import org.imgscalr.Scalr;

import Computation.computeAverageColor;
import Listener.ProgressEvent;
import Listener.ProgressListener;
import PictureAnalyse.SplitPicture;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.compareColor;
import PictureAnalyse.downrenderFiles;
import PictureAnalyse.mergeMosaik;
import PictureAnalyse.splitObj;
import saveObjects.DatabaseObj;
import saveObjects.FolderSave;
import saveObjects.ImageSector;
import saveObjects.ScaledImages;


public class Renderer extends SwingWorker{
	
	private BufferedImage finishedRender;
	
	private splitObj imageData;
	private FolderSave FolderData;
	private int maxRepetition;
	private Scalr.Method method;
	
	private List<ProgressListener> Listeners = new ArrayList<ProgressListener>();
	
	private short RenderId;
	
	public Renderer(splitObj information, FolderSave FolderData, int maxRepetition, Scalr.Method method) {
		imageData = information;
		this.FolderData = FolderData;
		this.maxRepetition = maxRepetition;
		this.method = method;
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
	
	public void addListener(ProgressListener listener) {
		Listeners.add(listener);
	}
	
	private void notifyListener(Renderer.Status s) {
		for(ProgressListener L : Listeners) {
			L.changeProgressStatus(new ProgressEvent(s, this.RenderId, this));
		}
	}
	
	@Override
	protected Object doInBackground() throws Exception {
		
		long timer1 = System.nanoTime();
		
		SplitPicture splitter = new SplitPicture();
        BufferedImage[][] biArr;
        
        notifyListener(Status.SPLITTER);
        
        biArr = splitter.SpitPictureAndSave(imageData);
        
        computeAverageColor colorCalculator = new computeAverageColor();
        
        notifyListener(Status.AVERAGE_COLOR_PICTURE);
        ImageSector[][] sections = colorCalculator.prepareSections(biArr);
        
        if(FolderData.selectedImages != null && FolderData.selectedImages.length > 0) {
        	notifyListener(Status.AVERAGE_COLOR_FILES);
        	ImageSector[] averageColorFiles = colorCalculator.computeAverageColorFiles(FolderData.selectedImages, calculateAverage.ScalrToThis(method));
        	ArrayList<Color[][]> colorsFiles = new ArrayList<Color[][]>();
        	for(int i=0;i<averageColorFiles.length;i++) {
        		colorsFiles.add(averageColorFiles[i].getColors());
        	}
        	DatabaseObj Obj = new DatabaseObj(FolderData.selectedImages, colorsFiles);
        	FolderData.selectedDatabasesList.add(Obj);
        }
		
        notifyListener(Status.DATABASE_MERGE);
		DatabaseObj[] DatabaseArray = new DatabaseObj[FolderData.selectedDatabasesList.size()];
		FolderData.selectedDatabasesList.toArray(DatabaseArray);
		
		notifyListener(Status.COMPUTATION);
		compareColor compare = new compareColor();
        File[][] choosen = compare.compare(sections, DatabaseArray,  maxRepetition);
        
        ScaledImages AllImages;
        
        notifyListener(Status.DOWNRENDER_IMAGES);
        downrenderFiles downrenderChoosen = new downrenderFiles();
        AllImages = downrenderChoosen.downrenderFilesAndSave(choosen, imageData, method);
        
        System.out.println("downrenders");
        
        notifyListener(Status.MERGE_IMAGES);
        mergeMosaik merger = new mergeMosaik();
        finishedRender = merger.mergeMosaik(AllImages, imageData, choosen);
        
        long timer2 = System.nanoTime();
        
        System.out.println("Zeit in Nanosekunden: "+ (timer2 - timer1));
		return null;
	}
	
	public void setRenderId(short RenderId) {
		this.RenderId = RenderId;
	}
	
	public short getRenderId() {
		return this.RenderId;
	}

	public splitObj getImageData() {
		return this.imageData;
	}
	
	public BufferedImage getFinishedRender() {
		if(this.isDone()) {
			return this.finishedRender;
		}else {
			return null;
		}
	}
	
	@Override
	public void done() {
		notifyListener(Status.DONE);
	}
}
