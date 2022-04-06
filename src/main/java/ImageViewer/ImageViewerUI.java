package ImageViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.imgscalr.Scalr;

import Computation.helper;
import GUI.MainGUI;
import GUI.MapBild;
import GUI.SynchronousJFXFileChooser;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;

import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.GridBagLayout;
import javax.swing.JProgressBar;

public class ImageViewerUI extends JFrame {

	private JPanel contentPane;
	private MapBild bild;
	private BufferedImage originalPicture;
	private BufferedImage workPicture;
	
	private JSlider Tranparency_slider;
	
	/**
	 * Create the frame.
	 */
	public ImageViewerUI(BufferedImage original, BufferedImage work) {
		
		originalPicture = helper.deepCopy(original);
		workPicture = helper.deepCopy(work);
		originalPicture = Scalr.resize(originalPicture, Scalr.Method.SPEED, workPicture.getWidth(), workPicture.getHeight(), null);
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 1024, 694);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane, BorderLayout.CENTER);
		

		bild = new MapBild(workPicture);
		scrollPane.setViewportView(bild);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JSlider zoomSlider = new JSlider(0, 200);
		zoomSlider.setValue(100);
		zoomSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				bild.setScale((zoomSlider.getValue() / 100f) + 0.2f);
				bild.revalidate();
				bild.repaint();
			}
		});
		panel_1.add(zoomSlider, BorderLayout.EAST);
		
		
		
		JProgressBar progressBar = new JProgressBar();
		panel_1.add(progressBar, BorderLayout.WEST);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JButton resetScale = new JButton("reset");
		resetScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomSlider.setValue(100);
				bild.setScale(1);
				bild.revalidate();
				bild.repaint();
			}
		});
		panel_2.add(resetScale, BorderLayout.EAST);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.WEST);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{88};
		gbl_panel_3.rowHeights = new int[]{286, 27, 14, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JButton SaveAs_btn = new JButton("Save As...");
		SaveAs_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				File selectedFile = null;
				
		        Platform.setImplicitExit(false);
		        try {
		            SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
		                FileChooser ch = new FileChooser();
		                ch.setTitle("Open any file you wish");
		                ch.getExtensionFilters().addAll(
		                	      new FileChooser.ExtensionFilter("JPG", "*.jpg")
		                	     ,new FileChooser.ExtensionFilter("PNG", "*.png")
		                	     ,new FileChooser.ExtensionFilter("GIF", "*.gif")
		                	     ,new FileChooser.ExtensionFilter("BMP", "*.bmp")
		                	     ,new FileChooser.ExtensionFilter("WBMP", "*.wbmp")
		                	);
		                return ch;
		            });
		            selectedFile = chooser.showSaveDialog();
		            // this will throw an exception:
		            chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	if(selectedFile != null) {
		        		
		        		class Export extends SwingWorker{

		        			File file;
		        			
		        			public Export(File file) {
		        				this.file = file;
		        			}
		        			
							@Override
							protected Object doInBackground() throws Exception {
								originalPicture = Scalr.resize(originalPicture, Scalr.Method.ULTRA_QUALITY, workPicture.getWidth(), workPicture.getHeight(), null);
					        	BufferedImage image = TransformImage.addTransparency(helper.deepCopy(workPicture), originalPicture, Tranparency_slider.getValue());
					        	try {
									ImageIO.write(image, helper.getFileFormat(file.getName()), file);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								return null;
							}
							
							@Override
							protected void done() {
								progressBar.setIndeterminate(false);
							}
		        			
		        		}
		        		
		        		progressBar.setIndeterminate(true);
			        	Export export = new Export(selectedFile);
			        	export.execute();
		        	}
		        }
			}
		});
		GridBagConstraints gbc_SaveAs_btn = new GridBagConstraints();
		gbc_SaveAs_btn.insets = new Insets(0, 0, 5, 0);
		gbc_SaveAs_btn.gridx = 0;
		gbc_SaveAs_btn.gridy = 1;
		panel_3.add(SaveAs_btn, gbc_SaveAs_btn);
		
		JLabel Transparency_lbl = new JLabel("Add Transparency");
		GridBagConstraints gbc_Transparency_lbl = new GridBagConstraints();
		gbc_Transparency_lbl.insets = new Insets(0, 0, 5, 0);
		gbc_Transparency_lbl.gridx = 0;
		gbc_Transparency_lbl.gridy = 2;
		panel_3.add(Transparency_lbl, gbc_Transparency_lbl);
		
		Tranparency_slider = new JSlider();
		Tranparency_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				BufferedImage image = TransformImage.addTransparency(helper.deepCopy(workPicture), originalPicture, Tranparency_slider.getValue());
				System.out.println(Tranparency_slider.getValue());
				bild.setImage(image);
	        	bild.revalidate();
	        	bild.repaint();
			}
		});
		GridBagConstraints gbc_Tranparency_slider = new GridBagConstraints();
		gbc_Tranparency_slider.insets = new Insets(0, 0, 5, 0);
		gbc_Tranparency_slider.gridx = 0;
		gbc_Tranparency_slider.gridy = 3;
		panel_3.add(Tranparency_slider, gbc_Tranparency_slider);
	}

}
