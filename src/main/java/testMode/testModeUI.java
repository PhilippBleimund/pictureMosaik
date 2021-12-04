package testMode;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.imgscalr.Scalr;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

import Computation.helper;
import GUI.SynchronousJFXFileChooser;
import net.miginfocom.swing.MigLayout;
import testMode.Config.ComputationConfig;
import testMode.Config.ImagesConfig;
import testMode.Config.TestConfig;
import testMode.Config.ImagesConfig.Type;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import PictureAnalyse.calculateAverage.Method;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;

import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class testModeUI {

	public JFrame frame;
	private Images_panel images_panel;
	private Computation_panel computation_panel;
	private JTextArea LogAread_TField;
	private JScrollPane scrollPane;
	private JSpinner increaseRepeat_spinner;
	
	private JPanel activeGraph_pnl;
	
	private JList allGraphs_list;
	private JList selectedGraphs_list;
	
	private JPopupMenu popupMenuListItem;
	
	public static testModeUI INSTANCE;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
				}
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testModeUI window = new testModeUI();
					INSTANCE = window;
					INSTANCE.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testModeUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 614, 736);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel leftMenu_pnl = new JPanel();
		panel.add(leftMenu_pnl, BorderLayout.WEST);
		leftMenu_pnl.setLayout(new CardLayout(0, 0));
		
		JPanel selectMenu_pnl = new JPanel();
		leftMenu_pnl.add(selectMenu_pnl, "name_293749872374");
		selectMenu_pnl.setLayout(new MigLayout("", "[grow][grow]", "[][][][][][][][][][][][][][][][]"));
		
		JLabel generateDummyImages_lbl = new JLabel("generate dummy images");
		selectMenu_pnl.add(generateDummyImages_lbl, "cell 0 0");
		
		JComboBox generateDummyImages_ComboBox = new JComboBox();
		generateDummyImages_ComboBox.setModel(new DefaultComboBoxModel(TestConfig.Method.values()));
		selectMenu_pnl.add(generateDummyImages_ComboBox, "cell 0 1,growx");
		
		JCheckBox Images = new JCheckBox("Images");
		Images.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(!Images.isSelected()) {
					images_panel.setVisible(false);
					images_panel.setMaximumSize(new Dimension(1, 1));
				}else {
					images_panel.setVisible(true);
					images_panel.setMaximumSize(new Dimension(150, 230));
					images_panel.setPreferredSize(new Dimension(150, 230));
				}
			}
		});
		
		JLabel TestRepeat_lbl = new JLabel("Repeats: ");
		selectMenu_pnl.add(TestRepeat_lbl, "flowx,cell 0 2");
		selectMenu_pnl.add(Images, "cell 0 3");
		
		images_panel = new Images_panel();
		images_panel.setVisible(false);
		images_panel.setMaximumSize(new Dimension(1, 1));
		selectMenu_pnl.add(images_panel, "cell 0 4");
		
		JCheckBox Computation_CheckBox = new JCheckBox("Computation");
		Computation_CheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(!Computation_CheckBox.isSelected()) {
					computation_panel.setVisible(false);
					computation_panel.setMaximumSize(new Dimension(1, 1));
				}else {
					computation_panel.setVisible(true);
					computation_panel.setMaximumSize(new Dimension(150, 200));
					computation_panel.setPreferredSize(new Dimension(150, 200));
				}
			}
		});
		selectMenu_pnl.add(Computation_CheckBox, "cell 0 5");
		
		computation_panel = new Computation_panel();
		computation_panel.setVisible(false);
		computation_panel.setMaximumSize(new Dimension(1, 1));
		computation_panel.setPreferredSize(new Dimension(150, 200));
		selectMenu_pnl.add(computation_panel, "cell 0 6");
		
		JSpinner TestRepeat_spinner = new JSpinner();
		TestRepeat_spinner.setPreferredSize(new Dimension(50, 20));
		TestRepeat_spinner.setModel(new SpinnerNumberModel(new Integer(1), null, null, new Integer(1)));
		selectMenu_pnl.add(TestRepeat_spinner, "cell 0 2");
		
		JCheckBox startRecord_CBox = new JCheckBox("record Test");
		selectMenu_pnl.add(startRecord_CBox, "cell 0 15");

		JButton startTest_btn = new JButton("start Test");
		startTest_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = null;
				if(startRecord_CBox.isSelected()) {
					String m = JOptionPane.showInputDialog("Enter name");
					if(m.length() == 0) {
						Random rnd = new Random();
						m = "test_" + Integer.toString(rnd.nextInt());
					}
					name = m;
				}
				
				ImagesConfig imagesConfig = null;
				ComputationConfig computationConfig = null;
				if(Images.isSelected()) {
					imagesConfig = images_panel.getConfiguration();
				}
				if(Computation_CheckBox.isSelected()) {
					computationConfig = computation_panel.getConfiguration();
				}
				TestConfig config = new TestConfig(imagesConfig, computationConfig, (testMode.Config.TestConfig.Method) generateDummyImages_ComboBox.getSelectedItem() , (int)TestRepeat_spinner.getValue(), (int)increaseRepeat_spinner.getValue());
				TestModeManager instance = TestModeManager.getInstance();
				instance.submitRequest(config, name);
			}
		});
		selectMenu_pnl.add(startTest_btn, "cell 0 15");
		
		
		JLabel increaseRepeat_lbl = new JLabel("increase per repeat");
		selectMenu_pnl.add(increaseRepeat_lbl, "cell 0 2");
		
		increaseRepeat_spinner = new JSpinner();
		increaseRepeat_spinner.setPreferredSize(new Dimension(50, 20));
		increaseRepeat_spinner.setModel(new SpinnerNumberModel(new Integer(10), null, null, new Integer(1)));
		selectMenu_pnl.add(increaseRepeat_spinner, "cell 0 2");
		
		JPanel selectGraph_pnl = new JPanel();
		leftMenu_pnl.add(selectGraph_pnl, "name_2057598540600");
		selectGraph_pnl.setLayout(new MigLayout("", "[grow]", "[grow][grow][]"));
		
		JPanel allGraphs_pnl = new JPanel();
		selectGraph_pnl.add(allGraphs_pnl, "cell 0 0,grow");
		allGraphs_pnl.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane allGraphs_scroll = new JScrollPane();
		allGraphs_pnl.add(allGraphs_scroll, "cell 0 0,grow");
				
		allGraphs_list = new JList();
		allGraphs_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int button = e.getButton();
				if(SwingUtilities.isRightMouseButton(e)) {
					System.out.println(allGraphs_list.getSelectedIndex());
					popupMenuListItem.show(allGraphs_list, e.getX(), e.getY());
				}
			}
		});
		allGraphs_list.setDragEnabled(true);
		allGraphs_list.setTransferHandler(new ExportTransferHandler());
		allGraphs_list.setModel(new DefaultListModel());
		allGraphs_scroll.setViewportView(allGraphs_list);
		
		popupMenuListItem = new JPopupMenu();
		
		JMenuItem exportListItem_btn = new JMenuItem("export...");
		exportListItem_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				File selectedFile = null;
				
		        Platform.setImplicitExit(false);
		        try {
		            SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
		                FileChooser ch = new FileChooser();
		                ch.setTitle("export");
		                ch.getExtensionFilters().addAll(
		                	     new FileChooser.ExtensionFilter("JSON", "*.json")
		                	    ,new FileChooser.ExtensionFilter("TXT", "*.txt")
		                	);
		                return ch;
		            });
		            selectedFile = chooser.showSaveDialog();
		        } finally {
		        	if(selectedFile != null) {
		        		JList invoker = (JList) popupMenuListItem.getInvoker();
						int selectedIndex = invoker.getSelectedIndex();
						DefaultListModel model = (DefaultListModel) invoker.getModel();
						JSONObj object = (JSONObj) model.get(selectedIndex);
						String fileFormat = helper.getFileFormat(selectedFile.getName());
						String exportString = "";
						switch(fileFormat) {
						case "json":
							exportString = object.toJSONString();
							break;
						case "txt":
							try {
								double[] values = object.getYValues();
								for(int i=0;i<values.length;i++) {
									exportString.concat(Double.toString(values[i]) + "\n");
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							break;
						}
						try (FileWriter file = new FileWriter(selectedFile)) {
				            file.write(exportString);
				        } catch (IOException exception) {
				        	exception.printStackTrace();
				        }
		        	}
		        }
			}
		});
		popupMenuListItem.add(exportListItem_btn);
		
		JMenuItem deleteListItem_btn = new JMenuItem("delete");
		deleteListItem_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JList invoker = (JList) popupMenuListItem.getInvoker();
				int selectedIndex = invoker.getSelectedIndex();
				DefaultListModel model = (DefaultListModel) invoker.getModel();
				JSONObj object = (JSONObj) model.get(selectedIndex);
				TestModeManager.getInstance().removeTestResult(object);
				model.remove(selectedIndex);
			}
		});
		popupMenuListItem.add(deleteListItem_btn);
		
		JPanel selectedGraphs_pnl = new JPanel();
		selectGraph_pnl.add(selectedGraphs_pnl, "cell 0 1,grow");
		selectedGraphs_pnl.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane selectedGraphs_sroll = new JScrollPane();
		selectedGraphs_pnl.add(selectedGraphs_sroll, "cell 0 0,grow");
		
		selectedGraphs_list = new JList();
		selectedGraphs_list.setDropMode(DropMode.ON_OR_INSERT);
		selectedGraphs_list.setTransferHandler(new ImportTransferHandler());
		selectedGraphs_list.setModel(new DefaultListModel());
		selectedGraphs_sroll.setViewportView(selectedGraphs_list);
		
		JButton updateChartView_btn = new JButton("refresh");
		updateChartView_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel model = (DefaultListModel) selectedGraphs_list.getModel();
				JSONObj[] selectedTests = new JSONObj[model.getSize()];
				for(int i=0;i<selectedTests.length;i++) {
					selectedTests[i] = (JSONObj) model.get(i);
				}
				ArrayList<double[]> valuesY = new ArrayList<double[]>();
				ArrayList<double[]> valuesX = new ArrayList<double[]>();
				for(int i=0;i<selectedTests.length;i++) {
					JSONObj jsonObj = selectedTests[i];
					try {
						double[] YValues = jsonObj.getYValues();
						valuesY.add(YValues);
						double[] XValues = jsonObj.getXValues();
						valuesX.add(XValues);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
				// Create Chart
				XYChart chart = new XYChartBuilder().title("results").xAxisTitle("iterations").yAxisTitle("time in ns").theme(ChartTheme.GGPlot2).build();
				chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
				chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
				chart.getStyler().setZoomEnabled(true);
				chart.getStyler().setZoomResetByDoubleClick(false);
				chart.getStyler().setZoomResetByButton(true);
				chart.getStyler().setZoomSelectionColor(new Color(0,0 , 192, 128));
				chart.getStyler().setCursorEnabled(true);
				chart.getStyler().setCursorColor(Color.BLACK);
				chart.getStyler().setCursorLineWidth(2f);
				chart.getStyler().setCursorFont(new Font("Verdana", Font.BOLD, 12));
				chart.getStyler().setCursorFontColor(Color.WHITE);
				chart.getStyler().setCursorBackgroundColor(Color.LIGHT_GRAY);
				for(int i=0;i<valuesY.size();i++) {
					chart.addSeries(selectedTests[i].getName(), valuesX.get(i), valuesY.get(i));
				}
				JPanel chartPanel = new XChartPanel<XYChart>(chart);
				activeGraph_pnl.removeAll();
				activeGraph_pnl.add(chartPanel, BorderLayout.CENTER);
				activeGraph_pnl.revalidate();
			}
		});
		selectGraph_pnl.add(updateChartView_btn, "cell 0 2");
        
		JPanel information_pnl = new JPanel();
		panel.add(information_pnl, BorderLayout.CENTER);
		information_pnl.setLayout(new CardLayout(0, 0));
		
		JPanel activeTestView_pnl = new JPanel();
		information_pnl.add(activeTestView_pnl, "name_1122568408600");
		activeTestView_pnl.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		scrollPane = new JScrollPane();
		activeTestView_pnl.add(scrollPane, "cell 0 0,grow");
		
		LogAread_TField = new JTextArea();
		scrollPane.setViewportView(LogAread_TField);
		
		JPanel viewGraph_pnl = new JPanel();
		information_pnl.add(viewGraph_pnl, "name_1129874075100");
		viewGraph_pnl.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		activeGraph_pnl = new JPanel();
		viewGraph_pnl.add(activeGraph_pnl, "cell 0 0,grow");
		activeGraph_pnl.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu View_Menu = new JMenu("View");
		menuBar.add(View_Menu);
		
		JMenu selectView_btn = new JMenu("select View");
		View_Menu.add(selectView_btn);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("test");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)leftMenu_pnl.getLayout();
				cl.show(leftMenu_pnl, "name_293749872374");
				CardLayout cl2 = (CardLayout)information_pnl.getLayout();
				cl2.show(information_pnl, "name_1122568408600");
			}
		});
		selectView_btn.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("graph");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)leftMenu_pnl.getLayout();
				cl.show(leftMenu_pnl, "name_2057598540600");
				CardLayout cl2 = (CardLayout)information_pnl.getLayout();
				cl2.show(information_pnl, "name_1129874075100");
				DefaultListModel listModel = (DefaultListModel)allGraphs_list.getModel();
				listModel.removeAllElements();
				DefaultListModel model = (DefaultListModel) selectedGraphs_list.getModel();
				model.removeAllElements();
				TestModeManager instance2 = TestModeManager.getInstance();
				JSONObject[] testResults = instance2.getTestResults();
				for(int i=0;i<testResults.length;i++) {
					listModel.addElement(testResults[i]);
				}
			}
		});
		selectView_btn.add(mntmNewMenuItem_1);
		
		JMenu File_menu = new JMenu("File");
		menuBar.add(File_menu);
		
		JMenuItem importTestResults_btn = new JMenuItem("import...");
		importTestResults_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				List<File> selectedFiles = null;
				
		        Platform.setImplicitExit(false);
		        try {
		            SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
		                FileChooser ch = new FileChooser();
		                ch.setTitle("export");
		                ch.getExtensionFilters().addAll(
		                	     new FileChooser.ExtensionFilter("JSON", "*.json")
		                	    ,new FileChooser.ExtensionFilter("TXT", "*.txt")
		                	);
		                return ch;
		            });
		            selectedFiles = chooser.showOpenMultipleDialog();
		        } finally {
		        	if(selectedFiles != null && selectedFiles.size() > 0) {
		        		for(int i=0;i<selectedFiles.size();i++) {
		        			try {
								Reader reader = new FileReader(selectedFiles.get(i));
								JSONParser parser = new JSONParser();
								JSONObject jsonObject = (JSONObject) parser.parse(reader);
								TestModeManager.getInstance().addTestResult(jsonObject);
							} catch (IOException|ParseException e1) {
								e1.printStackTrace();
							}
		        			
		        		}
		        	}
		        }
			}
		});
		File_menu.add(importTestResults_btn);
	}
	
	public void log(String s, boolean clear) {
		if(LogAread_TField.getText().length() > 30000) 
			clear = true;
		LogAread_TField.setText(LogAread_TField.getText() + "\n" + s);
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
		if(clear)
			LogAread_TField.setText("");
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	private class ExportTransferHandler extends TransferHandler {
        public int getSourceActions(JComponent c){
            return TransferHandler.COPY_OR_MOVE;
        }
     
        public Transferable createTransferable(JComponent c) {
            return new JSONObjSelection((JSONObj) allGraphs_list.getSelectedValue());
        }
    }
 
    private class ImportTransferHandler extends TransferHandler {

        public boolean canImport(TransferHandler.TransferSupport supp) {
            if (!supp.isDataFlavorSupported(DataFlavor.allHtmlFlavor)) {
                return false;
            }
            return true;
        }
     
        public boolean importData(TransferHandler.TransferSupport supp) {
            // Fetch the Transferable and its data
            Transferable t = supp.getTransferable();
            JSONObj data = null;
            try {
                data = (JSONObj)t.getTransferData(DataFlavor.allHtmlFlavor);
            } catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }

            // Fetch the drop location
            JList.DropLocation loc = selectedGraphs_list.getDropLocation();
            int row = loc.getIndex();
            DefaultListModel model = (DefaultListModel) selectedGraphs_list.getModel();
            model.add(row, data);
            selectedGraphs_list.validate();
            return true;
        }
    }
}

class Computation_panel extends JPanel {

	private JSpinner originalImageX_spinner;
	private JSpinner originalImageY_spinner;
	private JSpinner imagesCount_spinner;
	private JSpinner imagesMaxRepetition_spinner;

	/**
	 * Create the panel.
	 */
	public Computation_panel() {
		setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		
		JPanel OriginalImage_panel = new JPanel();
		add(OriginalImage_panel, "cell 0 0,grow");
		OriginalImage_panel.setLayout(new MigLayout("", "[]", "[][][]"));
		
		JLabel OriginalImageInfo_lbl = new JLabel("Settings for original image");
		OriginalImage_panel.add(OriginalImageInfo_lbl, "cell 0 0");
		
		JLabel OriginalImageSections_lbl = new JLabel("Sections:");
		OriginalImage_panel.add(OriginalImageSections_lbl, "cell 0 1");
		
		JLabel OriginalImageX_lbl = new JLabel("X");
		OriginalImage_panel.add(OriginalImageX_lbl, "flowx,cell 0 2");
		
		originalImageX_spinner = new JSpinner();
		originalImageX_spinner.setPreferredSize(new Dimension(50, 20));
		OriginalImage_panel.add(originalImageX_spinner, "cell 0 2");
		
		JLabel OriginalImageY_lbl = new JLabel("Y");
		OriginalImage_panel.add(OriginalImageY_lbl, "cell 0 2");
		
		originalImageY_spinner = new JSpinner();
		originalImageY_spinner.setPreferredSize(new Dimension(50, 20));
		OriginalImage_panel.add(originalImageY_spinner, "cell 0 2");
		
		JPanel Images_panel = new JPanel();
		add(Images_panel, "cell 0 1,grow");
		Images_panel.setLayout(new MigLayout("", "[123.00px]", "[][14px][]"));
		
		JLabel ImagesInfo_lbl = new JLabel("Settings for images");
		Images_panel.add(ImagesInfo_lbl, "cell 0 0");
		
		JLabel ImagesCount_lbl = new JLabel("count");
		Images_panel.add(ImagesCount_lbl, "flowx,cell 0 1,alignx left,aligny top");
		
		imagesCount_spinner = new JSpinner();
		imagesCount_spinner.setPreferredSize(new Dimension(50, 20));
		Images_panel.add(imagesCount_spinner, "cell 0 1");
		
		JLabel ImagesMaxRepetition_lbl = new JLabel("max Repetition");
		Images_panel.add(ImagesMaxRepetition_lbl, "flowx,cell 0 2");
		
		imagesMaxRepetition_spinner = new JSpinner();
		imagesMaxRepetition_spinner.setPreferredSize(new Dimension(50, 20));
		Images_panel.add(imagesMaxRepetition_spinner, "cell 0 2");
	}
	
	public ComputationConfig getConfiguration() {
		return new ComputationConfig((int) originalImageX_spinner.getValue(),
				(int) originalImageY_spinner.getValue(),
				(int) imagesCount_spinner.getValue(),
				(int) imagesMaxRepetition_spinner.getValue());
	}

}

class Images_panel extends JPanel {

	private JSpinner computationImagesCount_spinner;
	private JSpinner computationImagesSizeX_spinner;
	private JSpinner computationImagesSizeY_spinner;
	private JComboBox computationImagesMethod_ComboBox;
	private JSpinner imagesNewSizeX_spinner;
	private JSpinner imagesNewSizeY_spinner;
	private JCheckBox imagesColor_CheckBox;
	private JCheckBox imagesDatabase_CheckBox;
	private JCheckBox imagesDownrender_CheckBox;

	/**
	 * Create the panel.
	 */
	public Images_panel() {
		setLayout(new MigLayout("", "[124.00,grow]", "[][][][][][][][][]"));
		
		imagesColor_CheckBox = new JCheckBox("average Color");
		imagesColor_CheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(imagesColor_CheckBox.isSelected()) {
					imagesDatabase_CheckBox.setSelected(false);
					imagesDownrender_CheckBox.setSelected(false);
				}
			}
		});
		add(imagesColor_CheckBox, "cell 0 0");
		
		imagesDatabase_CheckBox = new JCheckBox("Database");
		imagesDatabase_CheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(imagesDatabase_CheckBox.isSelected()) {
					imagesColor_CheckBox.setSelected(false);
					imagesDownrender_CheckBox.setSelected(false);
				}
			}
		});
		add(imagesDatabase_CheckBox, "cell 0 1");
		
		imagesDownrender_CheckBox = new JCheckBox("downrender");
		imagesDownrender_CheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(imagesDownrender_CheckBox.isSelected()) {
					imagesColor_CheckBox.setSelected(false);
					imagesDatabase_CheckBox.setSelected(false);
				}
			}
		});
		add(imagesDownrender_CheckBox, "cell 0 2");
		
		JLabel computationImagesCount_lbl = new JLabel("count");
		add(computationImagesCount_lbl, "flowx,cell 0 3");
		
		computationImagesCount_spinner = new JSpinner();
		computationImagesCount_spinner.setPreferredSize(new Dimension(50, 20));
		computationImagesCount_spinner.setModel(new SpinnerNumberModel(new Integer(1000), null, null, new Integer(10)));
		add(computationImagesCount_spinner, "cell 0 3");
		
		JLabel computationImagesSize_lbl = new JLabel("Size:");
		add(computationImagesSize_lbl, "flowx,cell 0 4");
		
		JLabel computationImagesSizeMemory_lbl = new JLabel("0MB");
		add(computationImagesSizeMemory_lbl, "cell 0 4");
		
		JLabel computationImagesSizeX_lbl = new JLabel("X");
		add(computationImagesSizeX_lbl, "flowx,cell 0 5");
		
		computationImagesSizeX_spinner = new JSpinner();
		computationImagesSizeX_spinner.setPreferredSize(new Dimension(50, 20));
		add(computationImagesSizeX_spinner, "cell 0 5");
		
		JLabel computationImagesSizeY_lbl = new JLabel("X");
		add(computationImagesSizeY_lbl, "cell 0 5");
		
		computationImagesSizeY_spinner = new JSpinner();
		computationImagesSizeY_spinner.setPreferredSize(new Dimension(50, 20));
		add(computationImagesSizeY_spinner, "cell 0 5");
		
		computationImagesMethod_ComboBox = new JComboBox();
		computationImagesMethod_ComboBox.setModel(new DefaultComboBoxModel(Scalr.Method.values()));
		add(computationImagesMethod_ComboBox, "cell 0 6");
		
		JLabel ImagesNewSize_lbl = new JLabel("new Size:");
		add(ImagesNewSize_lbl, "cell 0 7");
		
		JLabel ImagesNewSizeX_lbl = new JLabel("X");
		add(ImagesNewSizeX_lbl, "flowx,cell 0 8");
		
		imagesNewSizeX_spinner = new JSpinner();
		imagesNewSizeX_spinner.setPreferredSize(new Dimension(50, 20));
		add(imagesNewSizeX_spinner, "cell 0 8");
		
		JLabel ImagesNewSizeY_lbl = new JLabel("Y");
		add(ImagesNewSizeY_lbl, "cell 0 8,aligny baseline");
		
		imagesNewSizeY_spinner = new JSpinner();
		imagesNewSizeY_spinner.setPreferredSize(new Dimension(50, 20));
		add(imagesNewSizeY_spinner, "cell 0 8");
	}
	
	public ImagesConfig getConfiguration() {
		Type type = null;
		if(imagesColor_CheckBox.isSelected())
			type = Type.COLOR;
		else if(imagesDatabase_CheckBox.isSelected())
			type = Type.DATABASE;
		else if(imagesDownrender_CheckBox.isSelected())
			type = Type.DOWNRENDER;
		
		return new ImagesConfig((int) computationImagesCount_spinner.getValue(),
				(int) computationImagesSizeX_spinner.getValue(),
				(int) computationImagesSizeY_spinner.getValue(),
				(int) imagesNewSizeX_spinner.getValue(),
				(int) imagesNewSizeY_spinner.getValue(),
				(Scalr.Method) computationImagesMethod_ComboBox.getSelectedItem(),
				type);
	}
}