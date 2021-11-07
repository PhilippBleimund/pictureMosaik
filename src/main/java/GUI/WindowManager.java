package GUI;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import ImageSelector.ImageSelectorUI;

public class WindowManager {

	public static WindowManager INSTANCE;
	
	public static ImageSelectorUI selectorInstance;
	public static MainGUI mainGuiInstance;
	
	public static void main(String[] args) {
		
		System.setProperty("sun.java2d.opengl", "true");
		
		System.out.println(Runtime.getRuntime().maxMemory());
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().freeMemory());
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
				}
				System.out.println(info.getClassName());
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					selectorInstance = new ImageSelectorUI();
					selectorInstance.frame.setVisible(true);
					selectorInstance.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					WindowListener exitListener = new WindowAdapter() {
					    @Override
					    public void windowClosing(WindowEvent e) {
					    	if(!mainGuiInstance.frame.isVisible()) {
						        int confirm = JOptionPane.showOptionDialog(
						             null, "Are You Sure to Close Application?", 
						             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
						             JOptionPane.QUESTION_MESSAGE, null, null, null);
						        if (confirm == 0) {
						           System.exit(0);
						        }
					    	}else {
					    		selectorInstance.frame.setVisible(false);
					    	}
					    }
					};
					selectorInstance.frame.addWindowListener(exitListener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGuiInstance = new MainGUI();
					mainGuiInstance.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					WindowListener exitListener = new WindowAdapter() {
					    @Override
					    public void windowClosing(WindowEvent e) {
					        int confirm = JOptionPane.showOptionDialog(
					             null, "Are You Sure to Close Application?", 
					             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
					             JOptionPane.QUESTION_MESSAGE, null, null, null);
					        if (confirm == 0) {
					           System.exit(0);
					    	}
					    }
					};
					mainGuiInstance.frame.addWindowListener(exitListener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void tansferFromSelectorToGUI() {
		ArrayList<File> Images = selectorInstance.getFiles();
		mainGuiInstance.setImages(Images);
		ArrayList<File> databases = selectorInstance.getDatabases();
		mainGuiInstance.setDatabases(databases);
	}
	
	public static ImageSelectorUI getSelectorInstance() {
		return selectorInstance;
	}
	
	public static MainGUI getGuiInstance() {
		return mainGuiInstance;
	}
}
