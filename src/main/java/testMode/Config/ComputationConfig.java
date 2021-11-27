package testMode.Config;

public class ComputationConfig {

	int sectionsX;
	int sectionsY;
	int count;
	int maxUsage;
	
	public ComputationConfig(int sectionsX, int sectionsY, int count, int maxUsage) {
		this.sectionsX = sectionsX;
		this.sectionsY = sectionsY;
		this.count = count;
		this.maxUsage = maxUsage;
	}

	public int getMaxUsage() {
		return maxUsage;
	}

	public int getSectionsX() {
		return sectionsX;
	}

	public int getSectionsY() {
		return sectionsY;
	}

	public int getCount() {
		return count;
	}
}
