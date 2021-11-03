package ImageSelector;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import GUI.MainGUI;
import GUI.SynchronousJFXFileChooser;
import GUI.WindowManager;
import ImageSelector.FolderTree.FolderTreeManager;
import ImageSelector.FolderTree.TreeValue;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;
import net.miginfocom.swing.MigLayout;

public class ImageSelectorUI {
	
	public JFrame frame;
	private FolderTreeManager manager;
	private JTree tree;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					
				}
				System.out.println(info.getClassName());
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageSelectorUI window = new ImageSelectorUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ImageSelectorUI() {
		manager = new FolderTreeManager();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 842, 665);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel choose_pnl = new JPanel();
		panel.add(choose_pnl, BorderLayout.WEST);
		choose_pnl.setLayout(new MigLayout("", "[]", "[][][][][][][][][][][][][][][][][][][][][]"));
		
		JButton addImages_btn = new JButton("add Images");
		addImages_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				List<File> selectedFilesList = null;
				
		        Platform.setImplicitExit(false);
		        try {
		            SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
		                FileChooser ch = new FileChooser();
		                ch.setTitle("Open any file you wish");
		                ch.getExtensionFilters().addAll(
		                	     new FileChooser.ExtensionFilter("PNG", "*.png")
		                	    ,new FileChooser.ExtensionFilter("JPG", "*.jpg")
		                	    ,new FileChooser.ExtensionFilter("ALL", new ArrayList<String>(Arrays.asList("*.png", "*.jpg")))
		                	);
		                return ch;
		            });
		            selectedFilesList = chooser.showOpenMultipleDialog();
		            // this will throw an exception:
		            //chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	if(selectedFilesList.size() > 0) {
		        		manager.addFiles(new ArrayList<File>(selectedFilesList));
		        		updateTree();
		        	}
		        }
		        
		        
			}
		});
		choose_pnl.add(addImages_btn, "cell 0 0");
		
		JButton addFolder_btn = new JButton("add Folder");
		choose_pnl.add(addFolder_btn, "cell 0 1");
		
		JButton addDatabase_btn = new JButton("add Database");
		addDatabase_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				JFXPanel dummy = new JFXPanel();
				List<File> selectedFilesList = null;
				
		        Platform.setImplicitExit(false);
		        try {
		            SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
		                FileChooser ch = new FileChooser();
		                ch.setTitle("Open any file you wish");
		                ch.getExtensionFilters().addAll(
		                		new FileChooser.ExtensionFilter("TXT", "*.txt")
		                	);
		                return ch;
		            });
		            selectedFilesList = chooser.showOpenMultipleDialog();
		            // this will throw an exception:
		            //chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
		        } finally {
		        	if(selectedFilesList.size() > 0) {
		        		manager.addDatabases(new ArrayList<File>(selectedFilesList));
		        		updateTree();
		        	}
		        }
		        
		        
			}
		});
		choose_pnl.add(addDatabase_btn, "cell 0 2");
		
		JButton render_btn = new JButton("save");
		render_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowManager.tansferFromSelectorToGUI();
				MainGUI guiInstance = WindowManager.getGuiInstance();
				guiInstance.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		
		JButton removeFolder_btn = new JButton("remove");
		removeFolder_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
				DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) lastPathComponent.getChildAt(0);
				Object userObject = childAt.getUserObject();
		        if(userObject instanceof TreeValue) {
		        	TreeValue userObjectCast = (TreeValue)userObject;
		        	manager.removeFolder(userObjectCast.getURI());
		        }
				updateTree();
			}
		});
		choose_pnl.add(removeFolder_btn, "cell 0 4");
		choose_pnl.add(render_btn, "cell 0 20");
		
		JPanel Tree_pnl = new JPanel();
		panel.add(Tree_pnl, BorderLayout.CENTER);
		Tree_pnl.setLayout(new BorderLayout(0, 0));
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("selected images");
		tree = new JTree(top);
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
		      private Icon DatabaseIcon = UIManager.getIcon("FileView.hardDriveIcon");
		      
		      @Override
		      public Component getTreeCellRendererComponent(JTree tree, Object value,
		          boolean selected, boolean expanded, boolean isLeaf, int row,
		          boolean focused) {
		        Component c = super.getTreeCellRendererComponent(tree, value, selected,
		            expanded, isLeaf, row, focused);
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		        Object userObject = node.getUserObject();
		        if(userObject instanceof TreeValue) {
		        	TreeValue userObjectCast = (TreeValue)userObject;
			        if(userObjectCast.getType() == TreeValue.type.DATABASE) {
			        	setLeafIcon(DatabaseIcon);
			        }
		        }
		        return c;
		      }
		    });
		Tree_pnl.add(tree, BorderLayout.CENTER);
	}

	private void updateTree() {
		DefaultTreeModel  model = (DefaultTreeModel) tree.getModel();
		model.setRoot(manager.getTreeModel(new DefaultMutableTreeNode("selected images")));
		model.reload();
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}
	
	public ArrayList<File> getFiles() {
		ArrayList<File> treeFiles = manager.getTreeFiles();
		return treeFiles;
	}
	
	public ArrayList<File> getDatabases() {
		ArrayList<File> treeDatabases = manager.getTreeDatabases();
		return treeDatabases;
	}
	
	enum TreeNoteType{
		Folder,
		Image
	}
}
