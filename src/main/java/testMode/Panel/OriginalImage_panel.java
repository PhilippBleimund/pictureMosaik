package testMode.Panel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import net.miginfocom.swing.MigLayout;

public class OriginalImage_panel extends JPanel {

	private JSpinner mergeOriginalImageX_spinner;
	private JSpinner mergeOriginalImageY_spinner;
	private JSpinner mergeOriginalImageSize_spinner;
	private JLabel mergeOriginalImageInfo_lbl;

	/**
	 * Create the panel.
	 */
	public OriginalImage_panel() {
		setLayout(new MigLayout("", "[]", "[][][]"));
		
		mergeOriginalImageInfo_lbl = new JLabel("<html>Using settings of<br>'analyse original image'<html>");
		add(mergeOriginalImageInfo_lbl, "cell 0 0,alignx right");
		
		JLabel MergeOriginalImageX_lbl = new JLabel("X");
		add(MergeOriginalImageX_lbl, "flowx,cell 0 1");
		
		mergeOriginalImageX_spinner = new JSpinner();
		add(mergeOriginalImageX_spinner, "cell 0 1");
		
		JLabel MergeOriginalImageY_lbl = new JLabel("Y");
		add(MergeOriginalImageY_lbl, "cell 0 1");
		
		mergeOriginalImageY_spinner = new JSpinner();
		add(mergeOriginalImageY_spinner, "cell 0 1");
		
		JLabel MergeOriginalImageSize_lbl = new JLabel("Size (px)");
		add(MergeOriginalImageSize_lbl, "flowx,cell 0 2");
		
		mergeOriginalImageSize_spinner = new JSpinner();
		add(mergeOriginalImageSize_spinner, "cell 0 2");
	}

	public void setInfo(String info) {
		mergeOriginalImageInfo_lbl.setText(info);
	}
	
	public OriginalImageConfig getConfig() {
		return new OriginalImageConfig((int)mergeOriginalImageX_spinner.getValue(),
				(int)mergeOriginalImageY_spinner.getValue(),
				(int)mergeOriginalImageSize_spinner.getValue());
	}
}
