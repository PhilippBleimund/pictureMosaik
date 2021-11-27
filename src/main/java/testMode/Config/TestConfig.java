package testMode.Config;

public class TestConfig {

	ImagesConfig Images;
	ComputationConfig Computation;
	
	private Method method;
	
	private int repeat;
	
	public TestConfig(ImagesConfig Images, ComputationConfig Computation, Method method, int repeat) {
		this.Images = Images;
		this.Computation = Computation;
		this.method = method;
		this.repeat = repeat;
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

	public enum Method{
		DOWNLOAD,
		GENERATE
	}
}
