package saveObjects;

import java.io.File;
import java.util.ArrayList;

public class FolderSave {

	public ArrayList<DatabaseObj> selectedDatabasesList;
	public File[] selectedDatabases;
	public File[] selectedImages;
	public File exportDatabase;
	
	public FolderSave() {
		exportDatabase = new File(System.getProperty("user.home") + "\\Pictures\\Database.txt");
	}
}
