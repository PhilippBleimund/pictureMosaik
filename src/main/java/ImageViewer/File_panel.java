package ImageViewer;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

public class File_panel extends JPanel {

	/**
	 * Create the panel.
	 */
	public File_panel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton SaveAs_btn = new JButton("Save As...");
		GridBagConstraints gbc_SaveAs_btn = new GridBagConstraints();
		gbc_SaveAs_btn.insets = new Insets(0, 0, 5, 5);
		gbc_SaveAs_btn.gridx = 0;
		gbc_SaveAs_btn.gridy = 1;
		add(SaveAs_btn, gbc_SaveAs_btn);
		
		JButton Close_btn = new JButton("Close");
		GridBagConstraints gbc_Close_btn = new GridBagConstraints();
		gbc_Close_btn.insets = new Insets(0, 0, 5, 5);
		gbc_Close_btn.gridx = 0;
		gbc_Close_btn.gridy = 2;
		add(Close_btn, gbc_Close_btn);

	}
}
