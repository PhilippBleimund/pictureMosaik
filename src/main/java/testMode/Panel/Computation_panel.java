package testMode.Panel;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;

public class Computation_panel extends JPanel {

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
		OriginalImage_panel.add(originalImageX_spinner, "cell 0 2");
		
		JLabel OriginalImageY_lbl = new JLabel("Y");
		OriginalImage_panel.add(OriginalImageY_lbl, "cell 0 2");
		
		originalImageY_spinner = new JSpinner();
		OriginalImage_panel.add(originalImageY_spinner, "cell 0 2");
		
		JPanel Images_panel = new JPanel();
		add(Images_panel, "cell 0 1,grow");
		Images_panel.setLayout(new MigLayout("", "[123.00px]", "[][14px][]"));
		
		JLabel ImagesInfo_lbl = new JLabel("Settings for images");
		Images_panel.add(ImagesInfo_lbl, "cell 0 0");
		
		JLabel ImagesCount_lbl = new JLabel("count");
		Images_panel.add(ImagesCount_lbl, "flowx,cell 0 1,alignx left,aligny top");
		
		imagesCount_spinner = new JSpinner();
		Images_panel.add(imagesCount_spinner, "cell 0 1");
		
		JLabel ImagesMaxRepetition_lbl = new JLabel("max Repetition");
		Images_panel.add(ImagesMaxRepetition_lbl, "flowx,cell 0 2");
		
		imagesMaxRepetition_spinner = new JSpinner();
		Images_panel.add(imagesMaxRepetition_spinner, "cell 0 2");
	}
	
	public ComputationConfig getConfig() {
		return new ComputationConfig((int) originalImageX_spinner.getValue(),
				(int) originalImageY_spinner.getValue(),
				(int) imagesCount_spinner.getValue(),
				(int) imagesMaxRepetition_spinner.getValue());
	}

}
