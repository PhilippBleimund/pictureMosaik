package testMode;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class JSONObjSelection implements Transferable, ClipboardOwner{

	JSONObj DATA;
	
	private static final int ALL = 0;
	
	private static final DataFlavor[] flavors = {
	        DataFlavor.allHtmlFlavor
	    };
	
	public JSONObjSelection(JSONObj DATA) {
		this.DATA = DATA;
	}
	

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return (DataFlavor[])flavors.clone();
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor.equals(flavors[ALL])) {
            return (Object)DATA;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
}
