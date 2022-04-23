package GUI;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import org.imgscalr.Scalr.Method;

import net.miginfocom.swing.MigLayout;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerNumberModel;

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
		setLayout(new MigLayout("", "[][][]", "[][][][][][][][][][][][][][][][]"));
		
		chooseOriginal_btn = new JButton("chose Image");
		add(chooseOriginal_btn, "cell 0 0");
		
		dimensions_lbl = new JLabel("dimensions");
		add(dimensions_lbl, "cell 0 2");
		
		dimensionX_spnr = new JSpinner();
		dimensionX_spnr.setModel(new SpinnerNumberModel(2, 1, 999, 1));
		dimensionX_spnr.setPreferredSize(new Dimension(50, 25));
		dimensionX_spnr.setValue(500);
		add(dimensionX_spnr, "cell 1 2");
		
		dimensionY_spnr = new JSpinner();
		dimensionY_spnr.setModel(new SpinnerNumberModel(2, 1, 999, 1));
		dimensionY_spnr.setPreferredSize(new Dimension(50, 25));
		dimensionY_spnr.setValue(500);
		add(dimensionY_spnr, "cell 2 2");
		
		multiplier_lbl = new JLabel("multiplier");
		add(multiplier_lbl, "cell 0 3");
		
		multiplier_spnr = new JSpinner();
		multiplier_spnr.setModel(new SpinnerNumberModel(1, 0, 10, 1));
		multiplier_spnr.setPreferredSize(new Dimension(35, 25));
		multiplier_spnr.setValue(1);
		add(multiplier_spnr, "cell 1 3");
		
		maxRepetition_lbl = new JLabel("max. repetition");
		add(maxRepetition_lbl, "cell 0 4");
		
		maxRepetition_spnr = new JSpinner();
		maxRepetition_spnr.setMaximumSize(new Dimension(70, 25));
		SpinnerNumberModel model = new SpinnerNumberModel(50, 1, 99999, 1);
		maxRepetition_spnr.setModel(model);
		maxRepetition_spnr.setValue(50);
		add(maxRepetition_spnr, "cell 1 4");
		
		downrenderAccuracy_lbl = new JLabel("accuracy");
		add(downrenderAccuracy_lbl, "cell 0 5");
		
		accuracy_ComboBox = new JComboBox();
		accuracy_ComboBox.setPreferredSize(new Dimension(80, 25));
		accuracy_ComboBox.setModel(new DefaultComboBoxModel(Method.values()));
		add(accuracy_ComboBox, "cell 1 5 2 1");
		
		render_btn = new JButton("add Render");
		add(render_btn, "cell 0 14");
		
		makeDatabase_btn = new JButton("make Database");
		add(makeDatabase_btn, "cell 0 15");

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
