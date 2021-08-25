package Computation;

interface ProgressBarListenerInterface{
	void changeProgressBarStatus(Renderer.Status s);
}

public abstract class ProgressBarListener implements ProgressBarListenerInterface{
	public abstract void changeProgressBarStatus(Renderer.Status s);
}