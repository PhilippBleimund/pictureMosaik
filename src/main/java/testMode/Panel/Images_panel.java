package testMode.Panel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.imgscalr.Scalr.Method;

import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Images_panel extends JPanel {

	private JSpinner computationImagesCount_spinner;
	private JSpinner computationImagesSizeX_spinner;
	private JSpinner computationImagesSizeY_spinner;
	private JLabel computationImagesInfo_lbl;
	private JComboBox computationImagesMethod_ComboBox;
	private JSpinner imagesNewSizeX_spinner;
	private JSpinner imagesNewSizeY_spinner;

	/**
	 * Create the panel.
	 */
	public Images_panel() {
		setLayout(new MigLayout("", "[124.00,grow]", "[][][][][][][]"));
		
		computationImagesInfo_lbl = new JLabel("<html>Using settings of <br>'images'");
		add(computationImagesInfo_lbl, "cell 0 0");
		
		JLabel computationImagesCount_lbl = new JLabel("count");
		add(computationImagesCount_lbl, "flowx,cell 0 1");
		
		computationImagesCount_spinner = new JSpinner();
		computationImagesCount_spinner.setModel(new SpinnerNumberModel(new Integer(1000), null, null, new Integer(10)));
		add(computationImagesCount_spinner, "cell 0 1");
		
		JLabel computationImagesSize_lbl = new JLabel("Size:");
		add(computationImagesSize_lbl, "flowx,cell 0 2");
		
		JLabel computationImagesSizeMemory_lbl = new JLabel("0MB");
		add(computationImagesSizeMemory_lbl, "cell 0 2");
		
		JLabel computationImagesSizeX_lbl = new JLabel("X");
		add(computationImagesSizeX_lbl, "flowx,cell 0 3");
		
		computationImagesSizeX_spinner = new JSpinner();
		add(computationImagesSizeX_spinner, "cell 0 3");
		
		JLabel computationImagesSizeY_lbl = new JLabel("X");
		add(computationImagesSizeY_lbl, "cell 0 3");
		
		computationImagesSizeY_spinner = new JSpinner();
		add(computationImagesSizeY_spinner, "cell 0 3");
		
		computationImagesMethod_ComboBox = new JComboBox();
		computationImagesMethod_ComboBox.setModel(new DefaultComboBoxModel(Method.values()));
		add(computationImagesMethod_ComboBox, "cell 0 4");
		
		JLabel ImagesNewSize_lbl = new JLabel("new Size:");
		add(ImagesNewSize_lbl, "cell 0 5");
		
		JLabel ImagesNewSizeX_lbl = new JLabel("X");
		add(ImagesNewSizeX_lbl, "flowx,cell 0 6");
		
		imagesNewSizeX_spinner = new JSpinner();
		add(imagesNewSizeX_spinner, "cell 0 6");
		
		JLabel ImagesNewSizeY_lbl = new JLabel("Y");
		add(ImagesNewSizeY_lbl, "cell 0 6,aligny baseline");
		
		imagesNewSizeY_spinner = new JSpinner();
		add(imagesNewSizeY_spinner, "cell 0 6");
	}
	
	public void setInfo(String info) {
		computationImagesInfo_lbl.setText(info);
	}
	
	public ImagesConfig getConfiguration() {
		return new ImagesConfig((int) computationImagesCount_spinner.getValue(),
				(int) computationImagesSizeX_spinner.getValue(),
				(int) computationImagesSizeY_spinner.getValue(),
				(int) imagesNewSizeX_spinner.getValue(),
				(int) imagesNewSizeY_spinner.getValue(),
				(Method) computationImagesMethod_ComboBox.getSelectedItem());
	}

}
