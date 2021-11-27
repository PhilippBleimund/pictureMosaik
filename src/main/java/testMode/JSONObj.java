package testMode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONObj extends JSONObject{

	private String name;
	
	public static final String valuesKey = "values";
	
	public JSONObj(String name) {
		super();
		this.name = name;
	}
	
	public JSONObj(JSONObj old) {
		super(old);
		this.name = old.name;
	}
	
	public double[] getValues() throws Exception{
		JSONArray objectArray = (JSONArray) this.get(valuesKey);
		if(objectArray == null)
			throw new Exception();
		double[] values = new double[objectArray.size()];
		for(int i=0;i<values.length;i++) {
			values[i] = Double.valueOf((String) objectArray.get(i));
		}
		return values;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
