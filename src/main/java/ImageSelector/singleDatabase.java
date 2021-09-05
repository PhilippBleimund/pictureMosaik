package ImageSelector;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class singleDatabase extends JPanel {

	public File[] Images;
	public singleImage[] ImagePanels;
	
	private JScrollPane scrollPane;
	private JPanel panel;
	private JPanel BigImagePanel;
	private JPanel ListImagePanel;
	private JComboBox comboBox;
	private JTable table;
	/**
	 * Create the panel.
	 */
	public singleDatabase(File[] Images) {
		this.Images = Images;
		
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new CardLayout(0, 0));
		
		comboBox = new JComboBox(DisplaySettings.values());
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedItem() == DisplaySettings.BIG) {
					CardLayout cl = (CardLayout) (panel_1.getLayout());
					cl.show(panel_1, "name_31435751261900");
				}
				else if(comboBox.getSelectedItem() == DisplaySettings.LIST) {
					CardLayout cl = (CardLayout) (panel_1.getLayout());
					cl.show(panel_1, "name_31435770279300");
				}
			}
		});
		panel.add(comboBox, BorderLayout.NORTH);
		
		BigImagePanel = new JPanel();
		BigImagePanel.add(new JLabel("hehe"));
		panel_1.add(BigImagePanel, "name_31435751261900");
		
		ListImagePanel = new JPanel();
		panel_1.add(ListImagePanel, "name_31435770279300");
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
			},
			new String[] {
				"File", "Name", "Type", "Size"
			}
		));
		ListImagePanel.add(table);
		
		setupImages();
	}
	
	public void setupImages() {
		
		class SwingCapsule extends SwingWorker{

			@Override
			protected Object doInBackground() throws Exception {
				int cores = Runtime.getRuntime().availableProcessors();
				ExecutorService pool = Executors.newFixedThreadPool(cores);
				
				ImagePanels = new singleImage[Images.length];
				
				class RenderPreview implements Runnable{

					private int index;
					
					public RenderPreview(int index) {
						this.index = index;
					}
					
					@Override
					public void run() {
						BufferedImage b = null;
						try {
							b = ImageIO.read(Images[index]);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ImagePanels[index] = new singleImage(Images[index], scaleSpeed(b, 100, 100));
						BigImagePanel.add(ImagePanels[index]);
						BigImagePanel.revalidate();
						BigImagePanel.repaint();
					}
					
				}
				for(int i=0;i<Images.length;i++) {
					pool.execute(new RenderPreview(i));
				}
				pool.shutdown();
				try {
					pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			public void done() {
				/*for(int i=0;i<Images.length;i++) {
					BigImagePanel.add(ImagePanels[i]);
				}*/
				BigImagePanel.revalidate();
				BigImagePanel.repaint();
			}
			
		}
		SwingCapsule capsule = new SwingCapsule();
		capsule.execute();
	}

	static BufferedImage scaleSpeed(BufferedImage src, int w, int h)
	{
	  BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	  int x, y;
	  int ww = src.getWidth() / w;
	  int hh = src.getHeight() / h;
	  for (x = 0; x < w; x++) {
	    for (y = 0; y < h; y++) {
	      int col = src.getRGB(x * ww, y * hh);
	      img.setRGB(x, y, col);
	    }
	  }
	  return img;
	}
	
	enum DisplaySettings{
		LIST,
		BIG
	}
}
