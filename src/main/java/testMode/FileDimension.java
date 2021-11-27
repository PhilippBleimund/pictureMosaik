package testMode;

import java.io.File;
import java.awt.Dimension;

public class FileDimension {
	
	File file;
	Dimension dimension;
	
	public FileDimension(File file, Dimension dimension) {
		super();
		this.file = file;
		this.dimension = dimension;
	}

	public File getFile() {
		return file;
	}

	public Dimension getDimension() {
		return dimension;
	}
}
