package testMode;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.TransferHandler;

public class DragDropDemo2 extends JFrame {
    private JList<String> source;
    private JList<String> target;
    private DefaultListModel model = new DefaultListModel();
 
    public DragDropDemo2() {
        source = new JList<String>(new String[]{"Papaya", "Orange", "Apple", "Mango", "Pear", "Avakado"});
        target = new JList<String>();
        target.setModel(model);
     
        source.setDragEnabled(true);
        target.setDropMode(DropMode.ON_OR_INSERT);
     
        source.setTransferHandler(new ExportTransferHandler());
        target.setTransferHandler(new ImportTransferHandler());
     
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, source, target);
        JLabel label = new JLabel("Please drag your selection from the left list to the right");
     
        add(splitPane);
        add(label, BorderLayout.NORTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
 
    private class ExportTransferHandler extends TransferHandler {
        public int getSourceActions(JComponent c){
            return TransferHandler.COPY_OR_MOVE;
        }
     
        public Transferable createTransferable(JComponent c) {
            return new StringSelection(source.getSelectedValue());
        }
    }
 
    private class ImportTransferHandler extends TransferHandler {

        public boolean canImport(TransferHandler.TransferSupport supp) {
            if (!supp.isDataFlavorSupported(DataFlavor.allHtmlFlavor)) {
                return false;
            }
            return true;
        }
     
        public boolean importData(TransferHandler.TransferSupport supp) {
            // Fetch the Transferable and its data
            Transferable t = supp.getTransferable();
            String data = "";
            try {
                data = (String)t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }

            // Fetch the drop location
            JList.DropLocation loc = target.getDropLocation();
            int row = loc.getIndex();
            model.add(row, data);
            target.validate();
            return true;
        }
    }
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DragDropDemo2();
            }
        });
    }
}
