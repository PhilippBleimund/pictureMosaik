package Listener;

import Manager.Renderer;

public class ProgressEvent{
	private Renderer.Status status;
	private short RenderId;
	private Renderer renderer;
	private int averageProgress;
	
	public ProgressEvent(Renderer.Status status, int averageProgress) {
		this.status = status;
		this.averageProgress = averageProgress;
	}
	
	public ProgressEvent(Renderer.Status status, short RenderId, Renderer renderer){
		this.status = status;
		this.RenderId = RenderId;
		this.renderer = renderer;
	}

	public Renderer.Status getStatus() {
		return status;
	}

	public short getRenderId() {
		return RenderId;
	}
	
	public Renderer getRendererIntance() {
		return this.renderer;
	}
	
	public int getAverageProgress() {
		return this.averageProgress;
	}
}