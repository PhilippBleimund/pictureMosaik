package Manager;

import java.awt.image.BufferedImage;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ImageViewer.ImageViewerUI;
import Listener.ProgressEvent;
import Listener.ProgressListener;
import Manager.Renderer.Status;
import PictureAnalyse.splitObj;

public class RenderQueue {

	private static RenderQueue INSTANCE;
	
	private BlockingQueue<Renderer> RenderQueue;
	private Renderer activeRender;
	
	private short RenderCounter;
	
	public RenderQueue() {
		RenderQueue = new LinkedBlockingQueue<Renderer>();
	}
	
	public void addRender(Renderer r) {
		r.setRenderId((short)RenderCounter);
		RenderCounter++;
		if(activeRender == null || activeRender.isDone()) {
			startRender(r);
		}else {
			RenderQueue.add(r);			
		}
	}
	
	public void nextRender() {
		Renderer R = RenderQueue.poll();
		if(R != null)
			startRender(R);
	}
	
	public void startRender(Renderer R) {
		R.addListener(new ProgressListener() {

			@Override
			public void changeProgressStatus(ProgressEvent e) {
				Status status = e.getStatus();
				if(status == Status.DONE) {
					Renderer rendererIntance = e.getRendererIntance();
					splitObj imageData = rendererIntance.getImageData();
					BufferedImage finishedRender = rendererIntance.getFinishedRender();
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							ImageViewerUI viewer = new ImageViewerUI(imageData.image, finishedRender);
					        viewer.setVisible(true);
						}			        	
			        });
					nextRender();
				}
			}
			
		});
		activeRender = R;
		activeRender.execute();
	}
	
	public String[] getQueueAsArray() {
		String[] array;
		if(RenderQueue.size() == 0 && (activeRender == null || activeRender.isDone())) {
			array = new String[1];
			array[0] = "empty queue";
		}else {
			ArrayList<Renderer> RenderList = new ArrayList<Renderer>(RenderQueue);
			array = new String[RenderQueue.size() + 1];
			array[0] = "Render #" + activeRender.getRenderId() + " ";
			for(int i=1;i<array.length;i++) {
				String s = "Render #" + RenderList.get(i-1).getRenderId();
				array[i] = s;
			}
		}
		return array;
	}
	
	public static RenderQueue getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new RenderQueue();
		}
		return INSTANCE;
	}
}
