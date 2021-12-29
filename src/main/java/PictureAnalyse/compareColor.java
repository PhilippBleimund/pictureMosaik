package PictureAnalyse;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import saveObjects.DatabaseObj;
import saveObjects.ImageSector;
import saveObjects.fileAndColor;

public class compareColor {

	int[][][] compabilityArr;
	compareObj[] compareArr;

	public compareColor() {

	}

	public File[][] compare(ImageSector[][] sectionArr, DatabaseObj[] Databases, int maxTimes) {
		for (int i = 0; i < Databases.length; i++) {
			Databases[i].compability = new int[sectionArr.length][sectionArr[0].length];
		}

		System.out.println("best Color");

		int cores = Runtime.getRuntime().availableProcessors();

		ExecutorService pool = Executors.newFixedThreadPool(cores);

		File[][] choosenImages = new File[sectionArr.length][sectionArr[0].length];

		class compareDatabases implements Runnable {

			private int x;
			private int y;

			public compareDatabases(int x, int y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public void run() {
				fileAndColor[] compability = new fileAndColor[Databases.length];
				//Get the best color out of every Database
				for (int l = 0; l < Databases.length; l++) {
					int bestColor = getDatabaseBestColor(sectionArr[x][y], Databases[l], maxTimes);
					Databases[l].compability[x][y] = bestColor;
				}
				//prepare for the comparison between the databases
				for (int l = 0; l < Databases.length; l++) {
					fileAndColor value = Databases[l].filesAndColors[Databases[l].compability[x][y]];
					compability[l] = value;
				}
				//compare what Database can offer the best
				fileAndColor smalest = compability[0];
				int smalestIndex = 0;
				for (int l = 1; l < compability.length; l++) {
					if(smalest.compareTo(compability[l]) == 1) {
						smalest = compability[l];
					}
				}
				choosenImages[x][y] = Databases[smalestIndex].filesAndColors[Databases[smalestIndex].compability[x][y]].file;
			}

		}

		ArrayList<compareDatabases> ThreadList = new ArrayList<compareDatabases>();
		for (int i = 0; i < sectionArr.length; i++) {
			for (int j = 0; j < sectionArr[0].length; j++) {
				ThreadList.add(new compareDatabases(i, j));
			}
		}
		Collections.shuffle(ThreadList);
		for (int i = 0; i < ThreadList.size(); i++) {
			pool.execute(ThreadList.get(i));
		}
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("best Files");
		return choosenImages;
	}

	// static for thread safety
	public static int getDatabaseBestColor(ImageSector color, DatabaseObj Database, int maxRepetition) {
		fileAndColor reference = new fileAndColor(new File(""), color.getColors());
		int result = Arrays.binarySearch(Database.filesAndColors, reference);
		if (result >= 0)
			return result;

		int insertionPoint = -result - 1;

		if (insertionPoint >= Database.filesAndColors.length) {
			insertionPoint = Database.filesAndColors.length - 1;
		}
		
		if (insertionPoint > 0) {
			insertionPoint = (Database.filesAndColors[insertionPoint].compareTo(Database.filesAndColors[insertionPoint-1]) == -1) 
					? insertionPoint : insertionPoint - 1;
		}

		int leftBest = getLeftBestColor(insertionPoint, Database, maxRepetition);
		int rigthBest = getRigthBestColor(insertionPoint, Database, maxRepetition);

		insertionPoint = (Database.filesAndColors[leftBest].compareTo(Database.filesAndColors[rigthBest]) == 1) ? leftBest : rigthBest;

		Database.filesAndColors[insertionPoint].increaseTimesUsed();
		return insertionPoint;
	}

	public static int getLeftBestColor(int i, DatabaseObj Database, int maxRepetition) {
		while (Database.filesAndColors[i].getTimesUsed() >= maxRepetition) {
			if (i == 0)
				return -1;
			i--;
		}
		return i;
	}

	public static int getRigthBestColor(int i, DatabaseObj Database, int maxRepetition) {
		while (Database.filesAndColors[i].getTimesUsed() >= maxRepetition) {
			if (i == Database.filesAndColors.length - 1)
				return -1;
			i++;
		}
		return i;
	}
}
