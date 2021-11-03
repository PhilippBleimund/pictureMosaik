package GUI;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
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
					selectorInstance.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGuiInstance = new MainGUI();
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
