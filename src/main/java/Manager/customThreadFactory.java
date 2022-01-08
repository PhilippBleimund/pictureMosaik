package Manager;

import java.util.concurrent.ThreadFactory;

public class customThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setPriority(Thread.MIN_PRIORITY);
		return t;
	}

}
