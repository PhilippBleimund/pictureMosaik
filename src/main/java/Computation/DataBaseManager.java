package Computation;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import PictureAnalyse.calculateAverage;
import saveObjects.DatabaseObj;
import saveObjects.FolderSave;

public class DataBaseManager {

	public DataBaseManager() {
		
	}
	
	public void createDataBase(Color[] averageColorFiles, FolderSave FolderData) {

		JSONObject obj = new JSONObject();
		JSONArray Data = new JSONArray();
		
		for(int i=0;i<FolderData.selectedImages.length;i++) {
			Color c = averageColorFiles[i];
			File f = FolderData.selectedImages[i];
			JSONObject Info = new JSONObject();
			Info.put("FilePath", f.getAbsolutePath());
			Info.put("ColorValue", c.getRGB());
			
			Data.add(Info);
		}
		
		obj.put("Data", Data);
		FileWriter file = null;
		
		try {
            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter(FolderData.exportDatabase);
            file.write(obj.toJSONString());
 
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
 
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}
	
	public DatabaseObj readSingleDatabase(File DatabaseLocation) {
		ArrayList<File> FilesList = new ArrayList<File>();
		ArrayList<Color> ColorList = new ArrayList<Color>();
		
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject Database = (JSONObject) parser.parse(new FileReader(DatabaseLocation));
			JSONArray Data = (JSONArray) Database.get("Data");
			
			for(Object a : Data) {
				JSONObject A = (JSONObject)a;
				File FilePath = new File((String) A.get("FilePath"));
				int ColorValue = ((Long) A.get("ColorValue")).intValue();
				
				FilesList.add(FilePath);
				ColorList.add(new Color(ColorValue));
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File[] Files = new File[FilesList.size()];
		FilesList.toArray(Files);
		Color[] Colors = new Color[ColorList.size()];
		ColorList.toArray(Colors);
		
		return new DatabaseObj(Files, Colors);
	}
}
