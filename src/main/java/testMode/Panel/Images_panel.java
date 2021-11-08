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
import javax.swing.JCheckBox;

public class Images_panel extends JPanel {

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
		add(imagesColor_CheckBox, "cell 0 0");
		
		imagesDatabase_CheckBox = new JCheckBox("Database");
		add(imagesDatabase_CheckBox, "cell 0 1");
		
		imagesDownrender_CheckBox = new JCheckBox("downrender");
		add(imagesDownrender_CheckBox, "cell 0 2");
		
		JLabel computationImagesCount_lbl = new JLabel("count");
		add(computationImagesCount_lbl, "flowx,cell 0 3");
		
		computationImagesCount_spinner = new JSpinner();
		computationImagesCount_spinner.setModel(new SpinnerNumberModel(new Integer(1000), null, null, new Integer(10)));
		add(computationImagesCount_spinner, "cell 0 3");
		
		JLabel computationImagesSize_lbl = new JLabel("Size:");
		add(computationImagesSize_lbl, "flowx,cell 0 4");
		
		JLabel computationImagesSizeMemory_lbl = new JLabel("0MB");
		add(computationImagesSizeMemory_lbl, "cell 0 4");
		
		JLabel computationImagesSizeX_lbl = new JLabel("X");
		add(computationImagesSizeX_lbl, "flowx,cell 0 5");
		
		computationImagesSizeX_spinner = new JSpinner();
		add(computationImagesSizeX_spinner, "cell 0 5");
		
		JLabel computationImagesSizeY_lbl = new JLabel("X");
		add(computationImagesSizeY_lbl, "cell 0 5");
		
		computationImagesSizeY_spinner = new JSpinner();
		add(computationImagesSizeY_spinner, "cell 0 5");
		
		computationImagesMethod_ComboBox = new JComboBox();
		computationImagesMethod_ComboBox.setModel(new DefaultComboBoxModel(Method.values()));
		add(computationImagesMethod_ComboBox, "cell 0 6");
		
		JLabel ImagesNewSize_lbl = new JLabel("new Size:");
		add(ImagesNewSize_lbl, "cell 0 7");
		
		JLabel ImagesNewSizeX_lbl = new JLabel("X");
		add(ImagesNewSizeX_lbl, "flowx,cell 0 8");
		
		imagesNewSizeX_spinner = new JSpinner();
		add(imagesNewSizeX_spinner, "cell 0 8");
		
		JLabel ImagesNewSizeY_lbl = new JLabel("Y");
		add(ImagesNewSizeY_lbl, "cell 0 8,aligny baseline");
		
		imagesNewSizeY_spinner = new JSpinner();
		add(imagesNewSizeY_spinner, "cell 0 8");
	}
	
	public ImagesConfig getConfiguration() {
		return new ImagesConfig((int) computationImagesCount_spinner.getValue(),
				(int) computationImagesSizeX_spinner.getValue(),
				(int) computationImagesSizeY_spinner.getValue(),
				(int) imagesNewSizeX_spinner.getValue(),
				(int) imagesNewSizeY_spinner.getValue(),
				(Method) computationImagesMethod_ComboBox.getSelectedItem(),
				imagesColor_CheckBox.isSelected(),
				imagesDatabase_CheckBox.isSelected(),
				imagesDownrender_CheckBox.isSelected());
	}

}
