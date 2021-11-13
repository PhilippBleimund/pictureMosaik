package testMode.Panel;

public class TestConfig {

	OriginalImageConfig originalImage;
	ImagesConfig Images;
	ComputationConfig Computation;
	
	private Method method;
	
	public TestConfig(OriginalImageConfig originalImage, ImagesConfig Images, ComputationConfig Computation, Method method) {
		this.originalImage = originalImage;
		this.Images = Images;
		this.Computation = Computation;
		this.method = method;
	}

	public OriginalImageConfig getOriginalImage() {
		return originalImage;
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

	public enum Method{
		DOWNLOAD,
		GENERATE
	}
}
