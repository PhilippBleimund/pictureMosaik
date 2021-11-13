package GUI;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import org.imgscalr.Scalr.Method;

import net.miginfocom.swing.MigLayout;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ControlPanel extends JPanel {

	JButton chooseOriginal_btn;
	JSpinner dimensionX_spnr;
	JSpinner dimensionY_spnr;
	JSpinner multiplier_spnr;
	JSpinner maxRepetition_spnr;
	JComboBox accuracy_ComboBox;
	JButton render_btn;
	JButton makeDatabase_btn;
	
	JLabel dimensions_lbl;
	JLabel multiplier_lbl;
	JLabel maxRepetition_lbl;
	
	JLabel downrenderAccuracy_lbl;
	/**
	 * Create the panel.
	 */
	public ControlPanel() {
		setLayout(new MigLayout("", "[::78.00px,grow][::45px,grow][::45]", "[][][][][][][][][][][][][][][]"));
		
		chooseOriginal_btn = new JButton("chose Image");
		add(chooseOriginal_btn, "cell 0 0");
		
		JCheckBox showSettings_CBox = new JCheckBox("show settings");
		showSettings_CBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = showSettings_CBox.isSelected();
				if(selected) {
					dimensionX_spnr.setVisible(true);
					dimensionY_spnr.setVisible(true);
					multiplier_spnr.setVisible(true);
					maxRepetition_spnr.setVisible(true);
					dimensions_lbl.setVisible(true);
					multiplier_lbl.setVisible(true);
					maxRepetition_lbl.setVisible(true);
				}else {
					dimensionX_spnr.setVisible(false);
					dimensionY_spnr.setVisible(false);
					multiplier_spnr.setVisible(false);
					maxRepetition_spnr.setVisible(false);
					dimensions_lbl.setVisible(false);
					multiplier_lbl.setVisible(false);
					maxRepetition_lbl.setVisible(false);
				}
			}
		});
		add(showSettings_CBox, "cell 0 1");
		
		dimensions_lbl = new JLabel("dimensions");
		add(dimensions_lbl, "cell 0 2");
		
		dimensionX_spnr = new JSpinner();
		dimensionX_spnr.setValue(5);
		add(dimensionX_spnr, "cell 1 2");
		
		dimensionY_spnr = new JSpinner();
		dimensionY_spnr.setValue(5);
		add(dimensionY_spnr, "cell 2 2");
		
		multiplier_lbl = new JLabel("multiplier");
		add(multiplier_lbl, "cell 0 3");
		
		multiplier_spnr = new JSpinner();
		multiplier_spnr.setValue(1);
		add(multiplier_spnr, "cell 1 3");
		
		maxRepetition_lbl = new JLabel("max. repetition");
		add(maxRepetition_lbl, "cell 0 4");
		
		maxRepetition_spnr = new JSpinner();
		maxRepetition_spnr.setValue(50);
		add(maxRepetition_spnr, "cell 1 4");
		
		downrenderAccuracy_lbl = new JLabel("accuracy");
		add(downrenderAccuracy_lbl, "cell 0 5,alignx trailing");
		
		accuracy_ComboBox = new JComboBox();
		accuracy_ComboBox.setModel(new DefaultComboBoxModel(Method.values()));
		add(accuracy_ComboBox, "cell 1 5 2 1,growx");
		
		render_btn = new JButton("add Render");
		add(render_btn, "cell 0 13");
		
		makeDatabase_btn = new JButton("make Database");
		add(makeDatabase_btn, "cell 0 14");

	}
	public JButton getChooseOriginal_btn() {
		return chooseOriginal_btn;
	}
	public JSpinner getDimensionX_spnr() {
		return dimensionX_spnr;
	}
	public JSpinner getDimensionY_spnr() {
		return dimensionY_spnr;
	}
	public JSpinner getMultiplier_spnr() {
		return multiplier_spnr;
	}
	public JSpinner getMaxRepetition_spnr() {
		return maxRepetition_spnr;
	}
	public JComboBox getAccuracy_ComboBox() {
		return accuracy_ComboBox;
	}
	public JButton getrender_btn() {
		return render_btn;
	}
	public JButton getMakeDatabase_btn() {
		return makeDatabase_btn;
	}

}
