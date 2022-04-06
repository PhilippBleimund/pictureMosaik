package Manager;

import java.awt.image.BufferedImage;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import ImageViewer.ImageViewerUI;
import Listener.ProgressEvent;
import Listener.ProgressListener;
import Listener.RenderQueueEvent;
import Listener.RenderQueueListener;
import Manager.Renderer.Status;
import PictureAnalyse.splitObj;

public class RenderQueue {

	private static RenderQueue INSTANCE;
	
	private BlockingQueue<Renderer> RenderQueue;
	private Renderer activeRender;
	
	private short RenderCounter;
	
	private List<RenderQueueListener> Listeners = new ArrayList<RenderQueueListener>();
	
	public RenderQueue() {
		RenderQueue = new LinkedBlockingQueue<Renderer>();
	}
	
	public void addListener(RenderQueueListener listener) {
		Listeners.add(listener);
	}
	
	private void notifyListener() {
		for(RenderQueueListener L : Listeners) {
			L.triggerQueueUpdate(new RenderQueueEvent(true));
		}
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

							WindowListener exitListener = new WindowAdapter() {
							    @Override
							    public void windowClosing(WindowEvent e) {
							        int confirm = JOptionPane.showOptionDialog(
							             null, "Are You Sure to Close Viewer? You could delete unsaved work!", 
							             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
							             JOptionPane.QUESTION_MESSAGE, null, null, null);
							        if (confirm == 0) {
							        	JFrame frame = (JFrame) e.getSource();
							        	frame.dispose();
							        	System.gc();
							    	}
							    }
							};
							viewer.addWindowListener(exitListener);
					        viewer.setVisible(true);
						}			        	
			        });
					
					nextRender();
					notifyListener();
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
