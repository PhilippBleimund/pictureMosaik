package Computation;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import PictureAnalyse.splitObj;
import saveObjects.DatabaseObj;

public class smartSplitter {

	public smartSplitter() {
		
	}
	
	//With splits is refereed to the sections not the actual splits.
	//The problem of the normal splitting is that it can occur that the splits would have point numbers. However Images can't have half pixels.
	//My solution is to use the minimal pixel count for the part Images and distribute the leftover pixel to the rest.
	public splitObj splitImage(BufferedImage bi, int splitsX, int splitsY, int multiplier) {
		int xBi = bi.getWidth();
		int yBi = bi.getHeight();
		
		int numberXSplits = 0;
		int numberYSplits = 0;
		if(splitsX > 0)
			numberXSplits = xBi / splitsX;
		if(splitsY > 0)
			numberYSplits = yBi / splitsY;
		
		int restX = xBi - (numberXSplits * splitsX);
		int restY = yBi - (numberYSplits * splitsY);
		
		//The rest pixels are now distributed along the other Splits
		int[] valuesX = distributeValues(splitsX, numberXSplits, restX);
		int[] valuesY = distributeValues(splitsY, numberYSplits, restY);
		
		return new splitObj(bi, valuesX, valuesY, multiplier, splitObj.Method.VALUE);
	}
	
	public int[] distributeValues(int splits, int numberSplits, int rest) {
		if(splits == 0) {
			return new int[] {rest};
		}
		
		int[] random = new int[splits];
		int[] values = new int[splits];
		for(int i=0;i<random.length;i++) {
			random[i] = i;
			values[i] = numberSplits;
		}
		shuffleArray(random);
		for(int i=0;i<rest;i++) {
			values[random[i]] = values[random[i]] + 1; 
		}
		return values;
	}
	
	public void shuffleArray(int[] ar){
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--){
	    	int index = rnd.nextInt(i + 1);
	      	int a = ar[index];
	      	ar[index] = ar[i];
	      	ar[i] = a;
	    }
	}
}
