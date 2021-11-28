package testMode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import testMode.Config.ComputationConfig;
import testMode.Config.ImagesConfig;
import testMode.Config.TestConfig;

public class JSONObj extends JSONObject{

	private String name;
	
	private TestConfig config;
	
	public static final String valuesKey = "values";
	
	public JSONObj(String name) {
		super();
		this.name = name;
	}
	
	public JSONObj(String name, String config, JSONObject source) {
		super(source);
		this.name = name;
		addTestConfig(config);
	}
	
	public JSONObj(JSONObj old) {
		super(old);
		this.name = old.name;
		this.config = old.config;
	}
	
	public double[] getYValues() throws Exception{
		JSONArray objectArray = (JSONArray) this.get(valuesKey);
		if(objectArray == null)
			throw new Exception();
		double[] values = new double[objectArray.size()];
		for(int i=0;i<values.length;i++) {
			values[i] = Double.valueOf((String) objectArray.get(i));
		}
		return values;
	}
	
	public double[] getXValues() {
		int repeat = config.getRepeat();
		int increase = config.getIncrease();
		int start = 0;
		ComputationConfig computation = config.getComputation();
		ImagesConfig images = config.getImages();
		if(computation != null) {
			start = computation.getCount();
		}else if(images != null) {
			start = images.getCount();
		}
		
		double[] values = new double[repeat];
		for(int i=0;i<values.length;i++) {
			values[i] = start + (i * increase);
		}
		return values;
	}
	
	public void addTestConfig(String configObj) {
		Gson gson = new Gson();
		TestConfig fromJson = gson.fromJson(configObj, TestConfig.class);
		this.config = fromJson;
	}
	
	public void addTestConfig(TestConfig config) {
		this.config = config;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonInString = gson.toJson(config);
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(jsonInString);
			this.put("config", json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
