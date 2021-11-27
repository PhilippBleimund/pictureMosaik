package testMode;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import testMode.Config.ComputationConfig;
import testMode.Config.ImagesConfig;
import testMode.Config.TestConfig;
import testMode.Config.TestConfig.Method;
import testMode.Tasks.Computation;
import testMode.Tasks.ImagesTest;
import testMode.Tasks.PrepareImages;

public class TestModeManager {

	private BlockingQueue<Runnable> tasks;
	private FileDimension[] images;
	
	private static TestModeManager INSTANCE;
	
	private Thread activeThread;
	private JSONObj activeTestResult;
	
	private ArrayList<JSONObj> testResults;
	
	public TestModeManager() {
		testResults = new ArrayList<JSONObj>();
		tasks = new LinkedBlockingQueue<Runnable>();
		images = new FileDimension[0];
	}
	
	public void submitRequest(TestConfig config) {
		int repeat = config.getRepeat();
		ImagesConfig images2 = config.getImages();
		ComputationConfig computation = config.getComputation();
		
		for(int i=0;i<repeat;i++) {
			if(images2 != null) {
				ImagesConfig imagesConfig = new ImagesConfig(images2.getCount() + (10 * i),
						images2.getSize(),
						images2.getNewSize(),
						images2.getMethod(),
						images2.getType());
				
				ImagesTest imagesTest = new ImagesTest(imagesConfig);
				if(images2.getType() == ImagesConfig.Type.COLOR || images2.getType() == ImagesConfig.Type.DOWNRENDER) {
					PrepareImages prepareImages = new PrepareImages(new TestConfig(imagesConfig, computation, config.getMethod(), config.getRepeat()));
					tasks.add(prepareImages);
				}
				tasks.add(imagesTest);
			}else if(computation != null) {
				Computation computation2 = new Computation(computation);
				tasks.add(computation2);
			}
		}
		if(activeThread == null || !activeThread.isAlive()) {
			nextTask();
		}
	}
	
	public void nextTask() {
		Runnable poll = tasks.poll();
		if(poll != null){
			Thread thread = new Thread(poll);
			activeThread = thread;
			activeThread.start();		
		}
	}
	
	public static TestModeManager getInstance() {
		if(INSTANCE == null)
			INSTANCE = new TestModeManager();
		return INSTANCE;
	}
	
	public void setImages(FileDimension[] images) {
		this.images = images;
	}
	
	public FileDimension[] getImages() {
		return this.images;
	}
	
	public File[] getImagesFile() {
		File[] files = new File[this.images.length];
		for(int i=0;i<files.length;i++) {
			files[i] = this.images[i].getFile();
		}
		return files;
	}
	
	public void Log(String message, boolean silent) {
		String time = "";
		boolean clean = false;
		if(silent != true) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");  
			LocalDateTime now = LocalDateTime.now();
			time = "[" + dtf.format(now) + "] "; 
		}
		if(message.equals("clean")) {
			clean = true;
		}
		
		testModeUI.INSTANCE.log(time + message, clean);
	}
	
	public void LogToJSON(String message) {
		if(activeTestResult != null) {
			JSONArray array = (JSONArray)activeTestResult.get("values");
			array.add(message);
			activeTestResult.put("values", array);
		}
	}
	
	public void startRecording(String nameRecording) {
		JSONObj jsonObject = new JSONObj(nameRecording);
		jsonObject.put("name", nameRecording);
		jsonObject.put("values", new JSONArray());
		activeTestResult = jsonObject;
	}
	
	public void stopRecording() {
		JSONObj jsonObject = new JSONObj(activeTestResult);
		testResults.add(jsonObject);
	}
	
	public JSONObject[] getTestResults() {
		JSONObj[] Results = new JSONObj[testResults.size()];
		for(int i=0;i<testResults.size();i++) {
			JSONObj jsonObject = testResults.get(i);
			Results[i] = jsonObject;
		}
		return Results;
	}
}
