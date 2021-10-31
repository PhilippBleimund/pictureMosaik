package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

import org.imgscalr.Scalr;

import Computation.DataBaseManager;
import Computation.helper;
import Computation.smartSplitter;
import Listener.ProgressEvent;
import Listener.ProgressListener;
import Manager.RenderQueue;
import Manager.Renderer;
import Manager.Renderer.Status;
import PictureAnalyse.SplitPicture;
import PictureAnalyse.calculateAverage;
import PictureAnalyse.compareColor;
import PictureAnalyse.mergeMosaik;
import PictureAnalyse.splitObj;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import saveObjects.DatabaseObj;
import saveObjects.FolderSave;

import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JProgressBar;

public class MainGUI {

	private JFrame frame;
	MapBild bild;
	
	calculateAverage pictureAverage;
	JSpinner spinnerMosaik;
	BufferedImage originalPicture;
	FolderSave FolderData = new FolderSave();
	JSpinner multiplier;
	JSpinner maxRepetition_spinner;
	JSlider OverlayTransparency_Slider;
	JCheckBox originalPictureOverlay_CBox;
	smartSplitter imageSplitter;
	JComboBox<?> downrenderSettings_CBox;
	JProgressBar progressBar;
	JComboBox<String> RenderQueueComboBox;
	
	helper helperClass = new helper();
	
	/**
	 * Launch the application.
	 */
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
		} catch (Exception e) {
			
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
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
	public MainGUI() {
		imageSplitter = new smartSplitter();
		pictureAverage = new calculateAverage();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(200, 500));
		scrollPane.setRowHeaderView(controlPanel);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton chooseOriginal = new JButton("choose Original");
		chooseOriginal.addActionListener(new ActionListener() {
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
			        	
			        	splitObj paintedLines = imageSplitter.splitImage(helper.deepCopy(originalPicture), (int)spinnerMosaik.getValue(), 1);
			        	
			        	paintLines(paintedLines);
			        	
			        	bild.setImage(paintedLines.image);
			        	bild.revalidate();
			        	bild.repaint();
		        	}
		        }
                
				
			}
		});
		controlPanel.add(chooseOriginal);
		
		JButton chooseFiles = new JButton("choose Files");
		chooseFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				List<File> selectedFilesList = null;
				
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
		        		FolderData.selectedImages = new File[selectedFilesList.size()];
			        	selectedFilesList.toArray(FolderData.selectedImages);
		        	}
		        }
		        
		        
			}
		});
		controlPanel.add(chooseFiles);
		
		JButton chooseFolder_btn = new JButton("choose folder");
		chooseFolder_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				File folder = null;
				
				Platform.setImplicitExit(false);
				try {
		            SynchronousJFXDirectoryChooser chooser = new SynchronousJFXDirectoryChooser(() -> {
		                DirectoryChooser ch = new DirectoryChooser();
		                ch.setTitle("Open any folder you wish");
		                return ch;
		            });
		            folder = chooser.showOpenDialog();
		            // this will throw an exception:
		            //chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	if(folder != null) {
		        		System.out.println("hehe");
			        	ArrayList<File> Images = helper.listFilesForFolder(folder, new ArrayList<File>());
			        	FolderData.selectedImages = new File[Images.size()];
			        	Images.toArray(FolderData.selectedImages);
			        	System.out.println(folder);
		        	}
		        }
			}
		});
		controlPanel.add(chooseFolder_btn);
		
		spinnerMosaik = new JSpinner();
		spinnerMosaik.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
		        
				splitObj paintedLines = imageSplitter.splitImage(helper.deepCopy(originalPicture), (int)spinnerMosaik.getValue(), 1);
		        
		        paintLines(paintedLines);
		        
				bild.setImage(paintedLines.image);
				bild.revalidate();
				bild.repaint();
			}
		});
		spinnerMosaik.setPreferredSize(new Dimension(50, 20));
		controlPanel.add(spinnerMosaik);
		
		JSlider sliderMosaik = new JSlider(2, 200);
		sliderMosaik.setValue(2);
		sliderMosaik.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spinnerMosaik.setValue(sliderMosaik.getValue());
			}
		});
		controlPanel.add(sliderMosaik);
		
		JButton resetMosaik = new JButton("reset Mosaik");
		resetMosaik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bild.setImage(originalPicture);
				bild.revalidate();
				bild.repaint();
			}
		});
		controlPanel.add(resetMosaik);
		
		JButton renderStart = new JButton("start Render");
		renderStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		
        		splitObj imageData = imageSplitter.splitImage(helper.deepCopy(originalPicture), (int)spinnerMosaik.getValue(), (int)multiplier.getValue());
	        	
	        	DataBaseManager manager = new DataBaseManager();
	        	ArrayList<DatabaseObj> Databasen = new ArrayList<DatabaseObj>();
	        	if(FolderData.selectedDatabases != null) {
	        		for(int i=0;i<FolderData.selectedDatabases.length;i++) {
	        			Databasen.add(manager.readSingleDatabase(FolderData.selectedDatabases[i]));
	        		}
	        	}
	        	FolderData.selectedDatabasesList = Databasen;
	        	
	        	RenderQueue instance = RenderQueue.getInstance();
	        	
	        	Renderer render = new Renderer(imageData, FolderData, (int)maxRepetition_spinner.getValue(), (Scalr.Method)downrenderSettings_CBox.getSelectedItem());
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
	        				progressBar.setString("Render #" + renderId + "average color of subsection");
	        				break;
	        			case AVERAGE_COLOR_FILES:
	        				progressBar.setValue(28);
	        				progressBar.setString("Render #" + renderId + "average color of selected images");
	        				break;
	        			case DATABASE_MERGE:
	        				progressBar.setValue(42);
	        				progressBar.setString("Render #" + renderId + "merging of databases");
	        				break;
	        			case COMPUTATION:
	        				progressBar.setValue(56);
	        				progressBar.setString("Render #" + renderId + "the best combination gets calculated");
	        				break;
	        			case DOWNRENDER_IMAGES:
	        				progressBar.setValue(70);
	        				progressBar.setString("Render #" + renderId + "the choosen images are rendered down");
	        				break;
	        			case MERGE_IMAGES:
	        				progressBar.setValue(84);
	        				progressBar.setString("Render #" + renderId + "the down rendered images are getting merged");
	        				break;
	        			case DONE:
	        				progressBar.setValue(0);
	        				progressBar.setString(null);
	        				RenderQueueComboBox.setModel(new DefaultComboBoxModel<String>(instance.getQueueAsArray()));
	        				break;
	        			}
	        		}
	        		
	        	});
	        	instance.addRender(render);
	        	
			}
		});
		controlPanel.add(renderStart);
		
		JLabel mutiplier_lbl = new JLabel("multiplier");
		controlPanel.add(mutiplier_lbl);
		
		multiplier = new JSpinner();
		multiplier.setValue(1);
		controlPanel.add(multiplier);
		
		JLabel maxRepetition_lbl = new JLabel("              max. repetition");
		controlPanel.add(maxRepetition_lbl);
		
		maxRepetition_spinner = new JSpinner();
		maxRepetition_spinner.setValue(50);
		maxRepetition_spinner.setPreferredSize(new Dimension(50, 20));
		controlPanel.add(maxRepetition_spinner);
		
		JLabel downrenderSettings_lbl = new JLabel("downrender Settings:");
		controlPanel.add(downrenderSettings_lbl);
		
		downrenderSettings_CBox = new JComboBox(Scalr.Method.values());
		controlPanel.add(downrenderSettings_CBox);
		
		//at the end because the state change event is fired
		spinnerMosaik.setValue(2);
		
		JButton createDatabaseWithFiles_btn = new JButton("create Database with files");
		createDatabaseWithFiles_btn.addActionListener(new ActionListener() {
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
			        	manager.createDataBase(FolderData, (Scalr.Method)downrenderSettings_CBox.getSelectedItem());
			        	
			        	long timer2 = System.currentTimeMillis();
			        	System.out.println(timer2 - timer1);
		        	}
		        }
				
			}
		});
		controlPanel.add(createDatabaseWithFiles_btn);
		
		JButton chooseDatabases_btn = new JButton("choose Databases");
		chooseDatabases_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				List<File> selectedFilesList = null;
				
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
		            selectedFilesList = chooser.showOpenMultipleDialog();
		            // this will throw an exception:
		            chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	System.out.println("hehe");
		        	FolderData.selectedDatabases = new File[selectedFilesList.size()];
		        	selectedFilesList.toArray(FolderData.selectedDatabases);
		        }
			}
		});
		controlPanel.add(chooseDatabases_btn);
		
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
