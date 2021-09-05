package ImageSelector;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

import GUI.SynchronousJFXFileChooser;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;

public class ImageSelectorUI {

	private JFrame frame;

	private JTabbedPane tabbedPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageSelectorUI window = new ImageSelectorUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ImageSelectorUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 842, 665);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				List<File> selectedFilesList = null;
				File[] Files;
				
		        Platform.setImplicitExit(false);
		        try {
		            SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
		                FileChooser ch = new FileChooser();
		                ch.setTitle("Open any file you wish");
		                ch.getExtensionFilters().addAll(
		                	     new FileChooser.ExtensionFilter("PNG", "*.png")
		                	    ,new FileChooser.ExtensionFilter("JPG", "*.jpg")
		                	    ,new FileChooser.ExtensionFilter("ALL", new ArrayList<String>(Arrays.asList("*.png", "*.jpg")))
		                	);
		                return ch;
		            });
		            selectedFilesList = chooser.showOpenMultipleDialog();
		            // this will throw an exception:
		            //chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	if(selectedFilesList.size() > 0) {
		        		System.out.println("hehe");
		        		Files = new File[selectedFilesList.size()];
			        	selectedFilesList.toArray(Files);
			        	
			        	singleDatabase tab = new singleDatabase(Files);
			        	
			        	tabbedPane.addTab("New tab", null, tab, null);
			        	
		        	}
		        }
			}
		});
		panel_1.add(btnNewButton);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);
	}

}
