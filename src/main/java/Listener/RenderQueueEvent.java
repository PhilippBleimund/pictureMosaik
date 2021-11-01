package Listener;

public class RenderQueueEvent {
	private boolean update;
	
	public RenderQueueEvent(boolean update) {
		this.update = update;
	}
	
	public boolean getUpdate() {
		return this.update;
	}
}
