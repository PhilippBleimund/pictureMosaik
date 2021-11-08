package testMode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import Computation.computeAverageColor;
import Computation.smartSplitter;
import PictureAnalyse.SplitPicture;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.compareColor;
import PictureAnalyse.downrenderFiles;
import PictureAnalyse.splitObj;
import saveObjects.DatabaseObj;
import testMode.Panel.ComputationConfig;
import testMode.Panel.ImagesConfig;
import testMode.Panel.OriginalImageConfig;
import testMode.Panel.TestConfig;

public class RenderTest {

	String tmpdir = System.getProperty("java.io.tmpdir");

	public void runTest(TestConfig config) {
		
	}
	
	public Color[][] testOriginalImage(OriginalImageConfig config, boolean silent) {		
		BufferedImage image = new BufferedImage((int) config.getSize().getWidth(), (int) config.getSize().getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		Random rnd = new Random();
		graphics.setPaint(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		sendMessage("prepared Image");
		
		sendMessage("start Test");
		
		smartSplitter imageSplitter = new smartSplitter();
		sendMessage("calculate Splits");
		long timeSplits0 = System.nanoTime();
		splitObj splitImage = imageSplitter.splitImage(image, config.getSplitsX(), config.getSplitsY(), 1);
		long timeSplits1 = System.nanoTime();
		sendMessage("time elapsed: " + (timeSplits1 - timeSplits0));
		
		SplitPicture splitter = new SplitPicture();
		BufferedImage[][] biArr;
		sendMessage("split Image");
		long timeImageSplit0 = System.nanoTime();
		biArr = splitter.SpitPictureAndSave(splitImage);
		long timeImageSplit1 = System.nanoTime();
		sendMessage("time elapsed: " + (timeImageSplit1 - timeImageSplit0));
		
		computeAverageColor colorCalculator = new computeAverageColor();
		sendMessage("calculate average Color");
		long timeColor0 = System.nanoTime();
		Color[][] averageColorSections = colorCalculator.computeAverageColorSections(biArr);
		long timeColor1 = System.nanoTime();
		sendMessage("time elapsed: " + (timeColor1 - timeColor0));
		
		return averageColorSections;
	}
	
	public void testImages(ImagesConfig config, File[] images, boolean silent) {
		computeAverageColor colorCalculator = new computeAverageColor();
		sendMessage("start Test: Images");
		
		
	}
	
	public long testAverageColor(ImagesConfig config, File[] images) {
		computeAverageColor colorCalculator = new computeAverageColor();
		
		sendMessage("calculate average Color");
		long timeColor0 = System.nanoTime();
		Color[] averageColorFiles = colorCalculator.computeAverageColorFiles(images, calculateAverage.ScalrToThis(config.getMethod()));
		long timeColor1 = System.nanoTime();
		sendMessage("time elapsed: " + (timeColor1 - timeColor0));
		return timeColor1 - timeColor0;
	}
	
	public long testDatabase(ImagesConfig config) {
		File[] images = new File[config.getCount()];
		Color[] averageColorFiles = new Color[config.getCount()];
		Random rnd = new Random();
		for(int i=0;i<averageColorFiles.length;i++) {
			averageColorFiles[i] = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
		}
		
		sendMessage("create new Database");
		long timeDatabase0 = System.nanoTime();
		DatabaseObj Obj = new DatabaseObj(images, averageColorFiles);
		long timeDatabase1 = System.nanoTime();
		sendMessage("time elapsed: " + (timeDatabase1 - timeDatabase0));
		return timeDatabase1 - timeDatabase0;
	}
	
	public long testDownrender(ImagesConfig config, File[] images) {
		File[][] imagesChoosen = new File[images.length][1];
		for(int i=0;i<images.length;i++) {
			imagesChoosen[i][0] = images[i];
		}
		
		smartSplitter imageSplitter = new smartSplitter();
		BufferedImage image = new BufferedImage((int)config.getNewSize().getWidth(), (int)config.getNewSize().getHeight(), BufferedImage.TYPE_INT_ARGB);
		splitObj splitImage = imageSplitter.splitImage(image, images.length, 0, 1);
		
		downrenderFiles downrenderChoosen = new downrenderFiles();
		sendMessage("downrender Images");
		long timerDownrender0 = System.nanoTime();
        downrenderChoosen.downrenderFilesAndSave(imagesChoosen, splitImage, config.getMethod());
        long timerDownrender1 = System.nanoTime();
        sendMessage("time elapsed: " + (timerDownrender1 - timerDownrender0));
        return timerDownrender1 - timerDownrender0;
	}
	
	public void testComputation(ComputationConfig config, boolean silent) {
		sendMessage("start Test: Computation");
		
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
		
		sendMessage("prepared Images, OriginalImage and Database");
		long timeComputation0 = System.nanoTime();
        File[][] choosen = compare.compare(averageColorSections, Obj,  config.getMaxUsage());
		long timeComputation1 = System.nanoTime();
		sendMessage("time elapsed: " + (timeComputation1 - timeComputation0));
	}
	
	public void downloadImages(int count, Dimension size) {
		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(cores);
		class Picture implements Runnable {

			int i;

			public Picture(int i) {
				this.i = i;
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (int j = 0; j < 10; j++) {
					saveImageAsFile("https://picsum.photos/" + size.getWidth() + "/" + size.getHeight(),
							i + " " + j + ".jpg");
					System.out.println(i + " " + j);
				}
			}

			public void saveImageAsFile(String URLpicture, String name) {
				try (InputStream in = new URL(URLpicture).openStream()) {
					File tmp = new File(tmpdir + "\\" + name);
					Files.copy(in, tmp.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 10; i < count / 10; i++) {
			pool.execute(new Picture(i));
		}
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void generateImages(int count, Dimension size) {
		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(cores);
		class Picture implements Runnable {

			int i;

			public Picture(int i) {
				this.i = i;
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (int j = 0; j < 10; j++) {
					BufferedImage image = new BufferedImage((int) size.getWidth(), (int) size.getHeight(),
							BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics = image.createGraphics();
					Random rnd = new Random();
					graphics.setPaint(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
					graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
					saveImageAsFile(image, i + " " + j + ".jpg");
					System.out.println(i + " " + j);
				}
			}

			public void saveImageAsFile(BufferedImage image, String name) {
				try {
					File tmp = new File(tmpdir + "\\" + name);
					ImageIO.write(image, ".jpg", tmp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 10; i < count / 10; i++) {
			pool.execute(new Picture(i));
		}
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(String s) {
			System.out.println(s);
	}
}
