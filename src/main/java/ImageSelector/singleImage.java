package ImageSelector;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class singleImage extends JPanel {

	public String ImageName;
	public File ImageLocation;
	public BufferedImage preview;
	public boolean delete = false;
	/**
	 * Create the panel.
	 */
	public singleImage(File ImageLocation, BufferedImage preview) {
		this.ImageLocation = ImageLocation;
		this.preview = preview;
		setLayout(new BorderLayout(0, 0));
		
		JLabel ImagePreview_lbl = new JLabel(new ImageIcon(preview));
		add(ImagePreview_lbl, BorderLayout.CENTER);
		
		JLabel ImageName_lbl = new JLabel(ImageLocation.getName());
		ImageName_lbl.setPreferredSize(new Dimension(25,100));
		add(ImageName_lbl, BorderLayout.SOUTH);
		
		JButton delete_btn = new JButton("delete");
		delete_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete = !delete;
			}
		});
		add(delete_btn, BorderLayout.NORTH);
	}

}
