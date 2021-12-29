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

import GUI.WindowManager;
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

	public void submitRequest(TestConfig config, String name) {

		if (name != null) {
			startRecording(name, config);
		}

		int repeat = config.getRepeat();
		ImagesConfig images2 = config.getImages();
		ComputationConfig computation = config.getComputation();

		LinkedBlockingQueue request = new LinkedBlockingQueue();
		
		for (int i = 0; i < repeat; i++) {
			if (images2 != null) {
				ImagesConfig imagesConfig = new ImagesConfig(images2.getCount() + (config.getIncrease() * i),
						images2.getSize(), images2.getNewSize(), images2.getMethod(), images2.getType());

				ImagesTest imagesTest = new ImagesTest(imagesConfig);
				if (images2.getType() == ImagesConfig.Type.COLOR || images2.getType() == ImagesConfig.Type.DOWNRENDER) {
					PrepareImages prepareImages = new PrepareImages(new TestConfig(imagesConfig, computation,
							config.getMethod(), config.getRepeat(), config.getIncrease()));
					request.add(prepareImages);
				}
				request.add(imagesTest);
			} else if (computation != null) {
				ComputationConfig computationConfig = new ComputationConfig(computation.getSectionsX(),
						computation.getSectionsY(), computation.getCount() + (config.getIncrease() * i),
						computation.getMaxUsage());
				Computation computation2 = new Computation(computationConfig);
				request.add(computation2);
			}
		}
		tasks = request;
		nextTask();
	}

	public void nextTask() {
		Runnable poll = tasks.poll();
		if (poll != null) {
			Thread thread = new Thread(poll);
			activeThread = thread;
			activeThread.start();
		}else {
			stopRecording();
		}
	}

	public static TestModeManager getInstance() {
		if (INSTANCE == null)
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
		for (int i = 0; i < files.length; i++) {
			files[i] = this.images[i].getFile();
		}
		return files;
	}

	public void Log(String message, boolean silent) {
		String time = "";
		boolean clean = false;
		if (silent != true) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
			LocalDateTime now = LocalDateTime.now();
			time = "[" + dtf.format(now) + "] ";
		}
		if (message.equals("clean")) {
			clean = true;
		}
		WindowManager.testModeInstance.log(time + message, clean);
	}

	public void LogToJSON(String message) {
		if (activeTestResult != null) {
			JSONArray array = (JSONArray) activeTestResult.get("values");
			array.add(message);
			activeTestResult.put("values", array);
		}
	}

	public void startRecording(String nameRecording, TestConfig confing) {
		JSONObj jsonObject = new JSONObj(nameRecording);
		jsonObject.addTestConfig(confing);
		jsonObject.put("name", nameRecording);
		jsonObject.put("values", new JSONArray());
		activeTestResult = jsonObject;
	}

	public void stopRecording() {
		if (activeTestResult != null) {
			JSONObj jsonObject = new JSONObj(activeTestResult);
			activeTestResult = null;
			testResults.add(jsonObject);
		}
	}

	public JSONObj[] getTestResults() {
		JSONObj[] Results = new JSONObj[testResults.size()];
		for (int i = 0; i < testResults.size(); i++) {
			JSONObj jsonObject = testResults.get(i);
			Results[i] = jsonObject;
		}
		return Results;
	}

	public void removeTestResult(JSONObj target) {
		testResults.remove(target);
	}

	public void addTestResult(JSONObject source) {
		String name = (String) source.get("name");
		JSONObject configJSON = (JSONObject) source.get("config");
		String config = configJSON.toJSONString();
		JSONObj jsonObj = new JSONObj(name, config, source);
		testResults.add(jsonObj);
	}
}
