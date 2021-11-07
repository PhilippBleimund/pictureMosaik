package testMode;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.hyperlink.HyperlinkAction;

public class JXTaskPaneBsp {

    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("JXTaskPanel-Beispiel", true);
        final JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        final JLabel centerLabel = new JLabel("Labeltext", JLabel.CENTER);
        centerPanel.add(centerLabel);

        final JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
        final ImageIcon icon1 = (ImageIcon) UIManager.getIcon("FileView.hardDriveIcon");
        Icon icon = UIManager.getIcon("CheckBox.icon");
        SafeIcon iconSafe = new SafeIcon(icon);
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        icon.paintIcon(new JCheckBox(), image.getGraphics(), 0, 0);
        final JXTaskPane pane1 = new JXTaskPane(new ImageIcon(image));
        final JXTaskPane pane2 = new JXTaskPane();
        final JXTaskPane pane3 = new JXTaskPane();

        pane1.setTitle("Aufgabe 1");
        pane1.setSpecial(true);
        pane1.add(new AbstractAction("Markieren", new ImageIcon(image)) {
            public void actionPerformed(ActionEvent e) {
                String txt = centerLabel.getText();
                centerLabel.setText("<html><u>" + txt + "</u></html>");
                processCenter(centerPanel, centerLabel);
                collapseAll(taskPaneContainer);
            }
        });
        taskPaneContainer.add(pane1);

        pane2.setTitle("Aufgabe 2");
        pane2.add(new AbstractAction("Ändern", new ImageIcon(image)) {
            public void actionPerformed(ActionEvent e) {
                centerLabel.setText("Text ge\u00E4ndert!");
                processCenter(centerPanel, centerLabel);
                collapseAll(taskPaneContainer);
            }
        });
        taskPaneContainer.add(pane2);

        pane3.setTitle("Aufgabe 3");
        pane3.add(new AbstractAction("Hyperlink setzen", new ImageIcon(image)) {
            public void actionPerformed(ActionEvent e) {
                HyperlinkAction linkAction = null;
                try {
                    linkAction = HyperlinkAction.createHyperlinkAction(new URI(
                            "http://javabeginners.de"));
                    linkAction.putValue(Action.NAME, "Javabeginners");
                } catch (URISyntaxException use) {
                    use.printStackTrace();
                }
                centerPanel.removeAll();
                JXHyperlink link = new JXHyperlink(linkAction);
                link.setHorizontalAlignment(SwingConstants.CENTER);
                processCenter(centerPanel, link);
                collapseAll(taskPaneContainer);
            }
        });
        taskPaneContainer.add(pane3);
        
        collapseAll(taskPaneContainer);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(taskPaneContainer, BorderLayout.EAST);
        frame.setStartPosition(JXFrame.StartPosition.CenterInScreen);
        frame.setLocationByPlatform(true);
        frame.setSize(300, 185);
        frame.setVisible(true);
    }

    private static void collapseAll(JXTaskPaneContainer container) {
        Component[] contArr = container.getComponents();
        for (Component c : contArr) {
            if (c instanceof JXTaskPane)
                ((JXTaskPane) c).setCollapsed(true);
        }
    }

    private static void processCenter(JPanel p, Component c) {
        p.removeAll();
        p.add(c);
        p.revalidate();
        p.repaint();
    }
}