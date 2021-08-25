package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.JComponent;

//TODO find Source
public class MapBild extends JComponent {

    private Image img;
    private float scale = 1.0f;

    public MapBild(BufferedImage img) {
        this.img = img;
        int w = img.getWidth(this);
        int h = img.getHeight(this);

        setPreferredSize(new Dimension(w, h));
    }

    public void setImage(Image img) {
    	this.img = img;
    }
    
    public Image getImage() {
    	return this.img;
    }
    
    public void setScale(float scalep) {
        this.scale = scalep;
    }

    public float getScale() {
        return scale;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.scale(scale, scale);
        int w = (int) (img.getWidth(null) * scale);
        int h = (int) (img.getHeight(null) * scale);
        g2.drawImage(img, 0, 0, null);
        setPreferredSize(new Dimension(w, h));
        revalidate();
    }
} 
