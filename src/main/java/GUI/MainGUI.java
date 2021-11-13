package GUI;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.imgscalr.Scalr;

import Computation.DataBaseManager;
import Computation.helper;
import Computation.smartSplitter;
import ImageSelector.ImageSelectorUI;
import Listener.ProgressEvent;
import Listener.ProgressListener;
import Listener.RenderQueueEvent;
import Listener.RenderQueueListener;
import Manager.RenderQueue;
import Manager.Renderer;
import Manager.Renderer.Status;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.splitObj;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;
import saveObjects.DatabaseObj;
import saveObjects.FolderSave;

public class MainGUI {

	public JFrame frame;
	MapBild bild;
	
	calculateAverage pictureAverage;
	BufferedImage originalPicture;
	FolderSave FolderData = new FolderSave();
	smartSplitter imageSplitter;
	JProgressBar progressBar;
	JComboBox<String> RenderQueueComboBox;
	
	helper helperClass = new helper();
	
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public MainGUI() {
		imageSplitter = new smartSplitter();
		pictureAverage = new calculateAverage();
		initialize();
	}

	public void setImages(ArrayList<File> files) {
		FolderData.selectedImages = files.toArray(new File[files.size()]);
	}
	
	public void setDatabases(ArrayList<File> Databases) {
		FolderData.selectedDatabases = Databases.toArray(new File[Databases.size()]);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		RenderQueue.getInstance().addListener(new RenderQueueListener() {
			@Override
			public void triggerQueueUpdate(RenderQueueEvent e) {
				RenderQueueComboBox.setModel(new DefaultComboBoxModel<String>(RenderQueue.getInstance().getQueueAsArray()));
			}
		});
		
		frame = new JFrame();
		frame.setBounds(100, 100, 840, 587);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
        BufferedImage a = null;
        try {
            a = ImageIO.read(MainGUI.class.getResourceAsStream("Enchanting-Travels-Japan-Tours-Colorful-Autumn-Season-and-Mountain-Fuji-with-morning-fog-and-red-leaves-at-lake-Kawaguchiko-is-one-of-the-best-places-in-Japan.jpg"));
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        originalPicture = helper.deepCopy(a);
        
        bild = new MapBild(a);
		scrollPane.setViewportView(bild);
		
		JPanel zoomPanel = new JPanel();
		scrollPane.setColumnHeaderView(zoomPanel);
		
		JSlider zoomSlider = new JSlider(0, 200);
		zoomSlider.setValue(100);
		zoomSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				bild.setScale((zoomSlider.getValue() / 100f) + 0.2f);
				bild.revalidate();
				bild.repaint();
			}
		});
		zoomPanel.add(zoomSlider);
		
		JButton resetScale = new JButton("reset");
		resetScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomSlider.setValue(100);
				bild.setScale(1);
				bild.revalidate();
				bild.repaint();
			}
		});
		zoomPanel.add(resetScale);
		
		ControlPanel controlPanel = new ControlPanel();
		
		controlPanel.getChooseOriginal_btn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				File selectedFile = null;
				
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
		            selectedFile = chooser.showOpenDialog();
		            // this will throw an exception:
		            chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	if(selectedFile != null) {
			        	BufferedImage a = null;
			        	try {
			        		a = ImageIO.read(selectedFile);
			        	} catch (IOException ex) {
			        		ex.printStackTrace();
			        	}
			        	
			        	originalPicture = helper.deepCopy(a);
			        	
			        	splitObj paintedLines = imageSplitter.splitImage(helper.deepCopy(originalPicture), (int)controlPanel.getDimensionX_spnr().getValue(), (int)controlPanel.getDimensionY_spnr().getValue(), 1);
			        	
			        	paintLines(paintedLines);
			        	
			        	bild.setImage(paintedLines.image);
			        	bild.revalidate();
			        	bild.repaint();
		        	}
		        }
			}
		});
		
		controlPanel.getDimensionX_spnr().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
		        
				splitObj paintedLines = imageSplitter.splitImage(helper.deepCopy(originalPicture), (int)controlPanel.getDimensionX_spnr().getValue(), (int)controlPanel.getDimensionY_spnr().getValue(), 1);
		        
		        paintLines(paintedLines);
		        
				bild.setImage(paintedLines.image);
				bild.revalidate();
				bild.repaint();
			}
		});
		
		controlPanel.getDimensionY_spnr().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
		        
				splitObj paintedLines = imageSplitter.splitImage(helper.deepCopy(originalPicture), (int)controlPanel.getDimensionX_spnr().getValue(), (int)controlPanel.getDimensionY_spnr().getValue(), 1);
		        
		        paintLines(paintedLines);
		        
				bild.setImage(paintedLines.image);
				bild.revalidate();
				bild.repaint();
			}
		});
		
		controlPanel.getMultiplier_spnr();
		
		controlPanel.getMaxRepetition_spnr();
		
		controlPanel.getAccuracy_ComboBox();
		
		controlPanel.getrender_btn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		
        		splitObj imageData = imageSplitter.splitImage(helper.deepCopy(originalPicture), (int)controlPanel.getDimensionX_spnr().getValue(), (int)controlPanel.getDimensionY_spnr().getValue(), (int)controlPanel.getMultiplier_spnr().getValue());
	        	
	        	DataBaseManager manager = new DataBaseManager();
	        	ArrayList<DatabaseObj> Databasen = new ArrayList<DatabaseObj>();
	        	if(FolderData.selectedDatabases != null) {
	        		for(int i=0;i<FolderData.selectedDatabases.length;i++) {
	        			Databasen.add(manager.readSingleDatabase(FolderData.selectedDatabases[i]));
	        		}
	        	}
	        	FolderData.selectedDatabasesList = Databasen;
	        	
	        	RenderQueue instance = RenderQueue.getInstance();
	        	
	        	Renderer render = new Renderer(imageData, FolderData, (int)controlPanel.getMaxRepetition_spnr().getValue(), (Scalr.Method)controlPanel.getAccuracy_ComboBox().getSelectedItem());
	        	render.addListener(new ProgressListener() {
	        		
	        		@Override
	        		public void changeProgressStatus(ProgressEvent e) {
	        			Status status = e.getStatus();
	        			short renderId = e.getRenderId();
	        			switch(status) {
	        			case SPLITTER:
	        				progressBar.setValue(0);
	        				progressBar.setString("Render #" + renderId + ": Image gets splited");
	        				break;
	        			case AVERAGE_COLOR_PICTURE:
	        				progressBar.setValue(14);
	        				progressBar.setString("Render #" + renderId + ": average color of subsection");
	        				break;
	        			case AVERAGE_COLOR_FILES:
	        				progressBar.setValue(28);
	        				progressBar.setString("Render #" + renderId + ": average color of selected images");
	        				break;
	        			case DATABASE_MERGE:
	        				progressBar.setValue(42);
	        				progressBar.setString("Render #" + renderId + ": merging of databases");
	        				break;
	        			case COMPUTATION:
	        				progressBar.setValue(56);
	        				progressBar.setString("Render #" + renderId + ": the best combination gets calculated");
	        				break;
	        			case DOWNRENDER_IMAGES:
	        				progressBar.setValue(70);
	        				progressBar.setString("Render #" + renderId + ": the choosen images are rendered down");
	        				break;
	        			case MERGE_IMAGES:
	        				progressBar.setValue(84);
	        				progressBar.setString("Render #" + renderId + ": the down rendered images are getting merged");
	        				break;
	        			case DONE:
	        				progressBar.setValue(0);
	        				progressBar.setString(null);
	        				break;
	        			}
	        		}
	        		
	        	});
	        	instance.addRender(render);
	        	RenderQueueComboBox.setModel(new DefaultComboBoxModel<String>(instance.getQueueAsArray()));
			}
		});
		
		controlPanel.getMakeDatabase_btn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFXPanel dummy = new JFXPanel();
				File selectedFile = null;
				
		        Platform.setImplicitExit(false);
		        try {
		            SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
		                FileChooser ch = new FileChooser();
		                ch.setTitle("Open any file you wish");
		                ch.getExtensionFilters().addAll(
		                	     new FileChooser.ExtensionFilter("TXT", "*.txt")
		                	);
		                return ch;
		            });
		            selectedFile = chooser.showSaveDialog();
		            // this will throw an exception:
		            chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	if(selectedFile != null) {
			        	long timer1 = System.currentTimeMillis();
			        	
			        	FolderData.exportDatabase = selectedFile;
			        	
			        	DataBaseManager manager = new DataBaseManager();
			        	manager.createDataBase(FolderData, (Scalr.Method)controlPanel.getAccuracy_ComboBox().getSelectedItem());
			        	
			        	long timer2 = System.currentTimeMillis();
			        	System.out.println(timer2 - timer1);
		        	}
		        }
				
			}
		});
		
		scrollPane.setRowHeaderView(controlPanel);
		
		JPanel ProgressBarPanel = new JPanel();
		panel.add(ProgressBarPanel, BorderLayout.SOUTH);
		ProgressBarPanel.setLayout(new BorderLayout(0, 0));
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		ProgressBarPanel.add(progressBar);
		
		JPanel RenderQueueRanel = new JPanel();
		ProgressBarPanel.add(RenderQueueRanel, BorderLayout.WEST);
		
		RenderQueueComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[] {"empty Queue"}));
		RenderQueueRanel.add(RenderQueueComboBox);
		
		JMenuBar menuBar = new JMenuBar();
		panel.add(menuBar, BorderLayout.NORTH);
		
		JMenu File_menu = new JMenu("File");
		menuBar.add(File_menu);
		
		JMenuItem File_ChangeFIles_menuButton = new JMenuItem("change Files...");
		File_ChangeFIles_menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageSelectorUI selectorInstance = WindowManager.getSelectorInstance();
				selectorInstance.frame.setVisible(true);
			}
		});
		File_menu.add(File_ChangeFIles_menuButton);
		
		JMenuItem Test_Mode_menuButton = new JMenuItem("test Mode");
		Test_Mode_menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		File_menu.add(Test_Mode_menuButton);
	}
	
	public void paintLines(splitObj splits) {
		Graphics g = splits.image.getGraphics();
		
		for(int i=0;i<splits.coordsSplitsX.length;i++) {
			g.drawLine(splits.coordsSplitsX[i], 0, splits.coordsSplitsX[i], splits.image.getHeight());
		}
		for(int i=0;i<splits.coordsSplitsY.length;i++) {
			g.drawLine(0, splits.coordsSplitsY[i], splits.image.getWidth(), splits.coordsSplitsY[i]);
		}
		
		g.dispose();
	}
}
