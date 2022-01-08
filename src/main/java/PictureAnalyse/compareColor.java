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

import Manager.customThreadFactory;
import saveObjects.DatabaseObj;
import saveObjects.fileAndColor;

public class compareColor {
	int[][][] compabilityArr;
	compareObj[] compareArr;
	public compareColor() {

	}

	public File[][] compare(Color[][] colorArr, DatabaseObj[] Databases, int maxTimes) {
		for (int i = 0; i < Databases.length; i++) {
			Databases[i].compability = new int[colorArr.length][colorArr[0].length];
		}

		System.out.println("best Color");
		int cores = Runtime.getRuntime().availableProcessors();

		ExecutorService pool = Executors.newFixedThreadPool(cores, new customThreadFactory());

		File[][] choosenImages = new File[colorArr.length][colorArr[0].length];

		class compareDatabases implements Runnable {

			private int x;
			private int y;
			public compareDatabases(int x, int y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public void run() {
				int[] compability = new int[Databases.length];
				for (int l = 0; l < Databases.length; l++) {
					int bestColor = getDatabaseBestColor(colorArr[x][y], Databases[l], maxTimes);
					Databases[l].compability[x][y] = bestColor;
				}
				for (int l = 0; l < Databases.length; l++) {
					int value = colorArr[x][y].getRGB()
							- Databases[l].filesAndColors[Databases[l].compability[x][y]].getColor().getRGB();
					compability[l] = (value > 0) ? value : -value;
				}
				int smalest = compability[0];
				int smalestIndex = 0;
				for (int l = 0; l < compability.length; l++) {
					if (smalest > compability[l]) {
						smalest = compability[l];
						smalestIndex = l;
					}
				}
				Databases[smalestIndex].filesAndColors[Databases[smalestIndex].compability[x][y]].increaseTimesUsed();
				choosenImages[x][y] = Databases[smalestIndex].filesAndColors[Databases[smalestIndex].compability[x][y]].getFile();
			}
		}

		ArrayList<compareDatabases> ThreadList = new ArrayList<compareDatabases>();
		for (int i = 0; i < colorArr.length; i++) {
			for (int j = 0; j < colorArr[0].length; j++) {
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
	public static int getDatabaseBestColor(Color color, DatabaseObj Database, int maxRepetition) {
		fileAndColor reference = new fileAndColor(new File(""), color);
		int result = Arrays.binarySearch(Database.filesAndColors, reference);
		if (result >= 0)
			return result;
		int insertionPoint = -result - 1;
		if (insertionPoint >= Database.filesAndColors.length) {
			insertionPoint = Database.filesAndColors.length - 1;
		}

		if (insertionPoint > 0) {
			insertionPoint = (Database.filesAndColors[insertionPoint].getColor().getRGB() - color.getRGB()) < (color.getRGB()
					- Database.filesAndColors[insertionPoint - 1].getColor().getRGB()) ? insertionPoint : insertionPoint - 1;
		}

		int leftBest = getLeftBestColor(insertionPoint, Database, maxRepetition);
		int rigthBest = getRigthBestColor(insertionPoint, Database, maxRepetition);

		int leftDistance;
		int rigthDistance;
		if (leftBest > -1) {
			leftDistance = color.getRGB() - Database.filesAndColors[leftBest].getColor().getRGB();
			leftDistance = (leftDistance < 0 ? -leftDistance : leftDistance);
		} else {
			leftDistance = Integer.MAX_VALUE;
			leftBest = insertionPoint;
		}

		if (rigthBest > -1) {
			rigthDistance = color.getRGB() - Database.filesAndColors[rigthBest].getColor().getRGB();
			rigthDistance = (rigthDistance < 0 ? -rigthDistance : rigthDistance);
		} else {
			rigthDistance = Integer.MAX_VALUE;
			rigthBest = insertionPoint;
		}

		insertionPoint = (leftDistance <= rigthDistance) ? leftBest : rigthBest;

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