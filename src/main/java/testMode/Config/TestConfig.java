package testMode.Config;

public class TestConfig {

	ImagesConfig Images;
	ComputationConfig Computation;
	
	private Method method;
	
	private int repeat;
	
	private int increase;
	
	public TestConfig(ImagesConfig Images, ComputationConfig Computation, Method method, int repeat, int increase) {
		this.Images = Images;
		this.Computation = Computation;
		this.method = method;
		this.repeat = repeat;
		this.increase = increase;
	}

	public ImagesConfig getImages() {
		return Images;
	}

	public ComputationConfig getComputation() {
		return Computation;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public int getRepeat() {
		return repeat;
	}
	
	public int getIncrease() {
		return increase;
	}

	public enum Method{
		DOWNLOAD,
		GENERATE
	}
}
