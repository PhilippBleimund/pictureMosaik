package testMode.Panel;

public class TestConfig {

	OriginalImageConfig originalImage;
	ImagesConfig Images;
	ComputationConfig Computation;
	
	public TestConfig(OriginalImageConfig originalImage, ImagesConfig Images, ComputationConfig Computation) {
		this.originalImage = originalImage;
		this.Images = Images;
		this.Computation = Computation;
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
}
