package config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class information {

  public static final String OS = System.getProperty("os.name");

  public static final String user = System.getProperty("user.name");

  public static final String tmpdir = System.getProperty("java.io.tmpdir");

  public static final String tmpBackup = "Backups";

  public static File getTmpDir(String... folder) {
    if (OS.indexOf("Windows") >= 0) {
      File f = new File(tmpdir + "\\PictureMosaik" +
                        ((folder != null) ? "" : "\\" + folder));
      createFolder(f);
      return f;
    } else if (OS.contains("Linux")) {
      File f = new File(tmpdir + "/PictureMosaik" +
                        ((folder != null) ? "" : "/" + folder));
      createFolder(f);
      return f;
    }
    return null;
  }

  public static void createFolder(File folder) {
    try {
      Files.createDirectory(Paths.get(folder.toURI()));
    } catch (IOException e) {
    }
  }
}
