package testMode.Tasks;

import Manager.customThreadFactory;
import config.information;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.sound.sampled.Line.Info;
import testMode.Config.TestConfig;
import testMode.FileDimension;
import testMode.TestModeManager;

public class PrepareImages implements Runnable {

  FileDimension[] download;
  int size;
  TestConfig config;

  public PrepareImages(TestConfig config) {
    this.size = config.getImages().getCount();
    this.config = config;
  }

  @Override
  public void run() {
    TestModeManager instance = TestModeManager.getInstance();
    this.download = instance.getImages();
    changeArray(this.size, this.config);
    instance.setImages(this.download);
    instance.Log("finished preparing " + size + " images.", false);
    instance.nextTask();
  }

  private void changeArray(int size, TestConfig config) {
    FileDimension[] newImages = new FileDimension[size];

    Dimension d = new Dimension(config.getImages().getSize());

    if (size > download.length) {

      if ((download.length == 0) || download[0].getDimension().equals(d)) {
        if (config.getMethod() == TestConfig.Method.DOWNLOAD) {
          System.out.println("start downloading");
          downloadImages(size - download.length, d);
        } else if (config.getMethod() == TestConfig.Method.GENERATE) {
          generateImages(size - download.length, d);
        }
      } else {
        deleteOldImages();
        if (config.getMethod() == TestConfig.Method.DOWNLOAD) {
          System.out.println("start downloading");
          downloadImages(size, d);
        } else if (config.getMethod() == TestConfig.Method.GENERATE) {
          generateImages(size, d);
        }
      }

      File tmpDir = information.getTmpDir();
      File[] dirImages = tmpDir.listFiles();
      for (int i = 0; i < size; i++) {
        newImages[i] = new FileDimension(dirImages[i], d);
      }
    } else {
      for (int i = 0; i < size; i++) {
        newImages[i] = download[i];
      }
    }
    download = newImages;
  }

  private void deleteOldImages() {
    File dir = information.getTmpDir();
    File[] listFiles = dir.listFiles();
    if (listFiles == null)
      return;
    for (int i = 0; i < listFiles.length; i++) {
      listFiles[i].delete();
      System.out.println(listFiles[i].toString());
    }
  }

  private void downloadImages(int count, Dimension size) {
    int cores = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(
        (cores > 50 ? cores : cores * 2), new customThreadFactory());
    class Picture implements Runnable {

      int i;
      int more;

      public Picture(int i, int more) {
        this.i = i;
        this.more = more;
      }

      @Override
      public void run() {
        // TODO Auto-generated method stub
        for (int j = 0; j < 10 + more; j++) {
          saveImageAsFile("https://picsum.photos/" + (int)size.getWidth() +
                          "/" + (int)size.getHeight());
          System.out.println(i + " " + j);
        }
      }

      public void saveImageAsFile(String URLpicture) {
        try (InputStream in = new URL(URLpicture).openStream()) {
          File tmp = information.getTmpDir();
          File createTempFile = File.createTempFile("image ", ".jpg", tmp);
          System.out.println(createTempFile.toPath().toString());
          createTempFile.deleteOnExit();
          Files.copy(in, createTempFile.toPath(),
                     StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    for (int i = 0; i < count / 10; i++) {
      TestModeManager.getInstance().countMore++;
      pool.execute(new Picture(
          i, (TestModeManager.getInstance().countMore % 50 == 0) ? 1 : 0));
    }
    pool.shutdown();
    try {
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void generateImages(int count, Dimension size) {
    int cores = Runtime.getRuntime().availableProcessors();
    ExecutorService pool =
        Executors.newFixedThreadPool(cores, new customThreadFactory());
    class Picture implements Runnable {

      int i;

      public Picture(int i) {
        this.i = i;
      }

      @Override
      public void run() {
        // TODO Auto-generated method stub
        for (int j = 0; j < 10; j++) {
          BufferedImage image =
              new BufferedImage((int)size.getWidth(), (int)size.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
          Random rnd = new Random();
          Graphics2D graphics = image.createGraphics();
          graphics.setPaint(
              new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
          graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
          graphics.dispose();
          saveImageAsFile(image);
          System.out.println(i + " " + j);
        }
      }

      public void saveImageAsFile(BufferedImage image) {
        try {
          File tmp = information.getTmpDir();
          System.out.println(tmp.toString());
          File createTempFile = File.createTempFile("image ", ".jpg", tmp);
          System.out.println(createTempFile.toString());
          createTempFile.deleteOnExit();
          ImageIO.write(image, "jpg", createTempFile);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    for (int i = 0; i < count / 10; i++) {
      pool.execute(new Picture(i));
    }
    pool.shutdown();
    try {
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
