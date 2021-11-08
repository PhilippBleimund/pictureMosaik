package testMode;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import testMode.Panel.Computation_panel;
import testMode.Panel.Images_panel;
import testMode.Panel.OriginalImage_panel;

public class testModeUI {

	private JFrame frame;
	private OriginalImage_panel analyseOriginalImage_panel;
	private Images_panel images_panel;
	private OriginalImage_panel computationOriginalImage_panel;
	private JPanel computationImages_panel;
	private JPanel mergeOriginalImage_panel;
	private JPanel computation_panel;
	
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
		
		JPanel selectMenu_pnl = new JPanel();
		panel.add(selectMenu_pnl, BorderLayout.WEST);
		selectMenu_pnl.setLayout(new MigLayout("", "[grow][grow]", "[][][][][][][][][][][][][][][]"));
		
		JLabel generateDummyImages_lbl = new JLabel("generate dummy images");
		selectMenu_pnl.add(generateDummyImages_lbl, "cell 0 0");
		
		JComboBox generateDummyImages_ComboBox = new JComboBox();
		generateDummyImages_ComboBox.setModel(new DefaultComboBoxModel(new String[] {"download from Web", "generate"}));
		selectMenu_pnl.add(generateDummyImages_ComboBox, "cell 0 1,growx");
		
		JCheckBox AnalyseOriginalImage_CheckBox = new JCheckBox("analyse original image");
		AnalyseOriginalImage_CheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(!AnalyseOriginalImage_CheckBox.isSelected()) {
					analyseOriginalImage_panel.setVisible(false);
					analyseOriginalImage_panel.setMaximumSize(new Dimension(1, 1));
				}else {
					analyseOriginalImage_panel.setVisible(true);
					analyseOriginalImage_panel.setMaximumSize(new Dimension(150, 70));
					analyseOriginalImage_panel.setPreferredSize(new Dimension(150, 70));
				}
				selectMenu_pnl.revalidate();
			}
		});
		selectMenu_pnl.add(AnalyseOriginalImage_CheckBox, "cell 0 2");
		
		analyseOriginalImage_panel = new OriginalImage_panel();
		analyseOriginalImage_panel.setInfo("");
		selectMenu_pnl.add(analyseOriginalImage_panel, "cell 0 3");
		
		JCheckBox Images = new JCheckBox("Images");
		Images.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(!Images.isSelected()) {
					images_panel.setVisible(false);
					images_panel.setMaximumSize(new Dimension(1, 1));
				}else {
					images_panel.setVisible(true);
					images_panel.setMaximumSize(new Dimension(150, 70));
					images_panel.setPreferredSize(new Dimension(150, 100));
				}
			}
		});
		selectMenu_pnl.add(Images, "cell 0 4");
		
		images_panel = new Images_panel();
		selectMenu_pnl.add(images_panel, "cell 0 5");
		
		JCheckBox Computation_CheckBox = new JCheckBox("Computation");
		Computation_CheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(!Computation_CheckBox.isSelected()) {
					computation_panel.setVisible(false);
					computation_panel.setMaximumSize(new Dimension(1, 1));
				}else {
					computation_panel.setVisible(true);
					computation_panel.setMaximumSize(new Dimension(150, 70));
					computation_panel.setPreferredSize(new Dimension(150, 200));
				}
			}
		});
		selectMenu_pnl.add(Computation_CheckBox, "cell 0 6");
		
		computation_panel = new Computation_panel();
		computation_panel.setPreferredSize(new Dimension(150, 100));
		selectMenu_pnl.add(computation_panel, "cell 0 7");
		
		JCheckBox Merge_CheckBox = new JCheckBox("Merge");
		Merge_CheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(!Merge_CheckBox.isSelected()) {
					mergeOriginalImage_panel.setVisible(false);
					mergeOriginalImage_panel.setMaximumSize(new Dimension(1, 1));
				}else {
					mergeOriginalImage_panel.setVisible(true);
					mergeOriginalImage_panel.setMaximumSize(new Dimension(150, 100));
					mergeOriginalImage_panel.setPreferredSize(new Dimension(150, 100));
				}
			}
		});
		selectMenu_pnl.add(Merge_CheckBox, "cell 0 8");
		
		mergeOriginalImage_panel = new OriginalImage_panel();
		selectMenu_pnl.add(mergeOriginalImage_panel, "cell 0 9");
        
		JPanel information_pnl = new JPanel();
		panel.add(information_pnl, BorderLayout.CENTER);
		information_pnl.setLayout(new CardLayout(0, 0));
		
		JPanel informationAboutTest_pnl = new JPanel();
		information_pnl.add(informationAboutTest_pnl, "name_29908487180200");
		
		JPanel activeTestView_pnl = new JPanel();
		information_pnl.add(activeTestView_pnl, "name_29946164256000");
	}

}
