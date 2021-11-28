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
import java.util.concurrent.TimeUnit;

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

import Computation.helper;
import GUI.MainGUI;
import GUI.SynchronousJFXDirectoryChooser;
import GUI.SynchronousJFXFileChooser;
import GUI.WindowManager;
import ImageSelector.FolderTree.FolderTreeManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class ImageSelectorUI {

	public JFrame frame;
	private FolderTreeManager manager;
	private JTree tree;

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
						ch.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"),
								new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("ALL",
										new ArrayList<String>(Arrays.asList("*.png", "*.jpg"))));
						return ch;
					});
					selectedFilesList = chooser.showOpenMultipleDialog();
					// this will throw an exception:
					// chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
				} finally {
					if (selectedFilesList != null && selectedFilesList.size() > 0) {
						manager.addFiles(new ArrayList<File>(selectedFilesList));
						updateTree();
					}
				}

			}
		});
		choose_pnl.setLayout(new GridLayout(0, 1, 0, 0));
		choose_pnl.add(addImages_btn);

		JButton addFolder_btn = new JButton("add Folder");
		addFolder_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFXPanel dummy = new JFXPanel();
				File file = null;
				Platform.setImplicitExit(false);
				try {
					SynchronousJFXDirectoryChooser chooser = new SynchronousJFXDirectoryChooser(() -> {
						DirectoryChooser ch = new DirectoryChooser();
						ch.setTitle("Open any file you wish");
						return ch;
					});
					file = chooser.showOpenDialog();
					System.out.println(file);
					// this will throw an exception:
					chooser.showDialog(ch -> ch.showDialog(null), 1, TimeUnit.NANOSECONDS);
				} finally {
					if (file != null) {
						ArrayList<ArrayList<File>> allFiles = helper.listFilesForFolder(file,
								new ArrayList<ArrayList<File>>());
						for (ArrayList<File> list : allFiles) {
							if (list.size() > 0)
								manager.addFiles(list);
						}
						updateTree();
					}
				}
			}
		});
		choose_pnl.add(addFolder_btn);

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
						ch.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
						return ch;
					});
					selectedFilesList = chooser.showOpenMultipleDialog();
					// this will throw an exception:
					// chooser.showDialog(ch -> ch.showOpenDialog(null), 1, TimeUnit.NANOSECONDS);
				} finally {
					if (selectedFilesList != null && selectedFilesList.size() > 0) {
						manager.addDatabases(new ArrayList<File>(selectedFilesList));
						updateTree();
					}
				}
			}
		});
		choose_pnl.add(addDatabase_btn);

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
				DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) tree.getSelectionPath()
						.getLastPathComponent();
				DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) lastPathComponent.getChildAt(0);
				Object userObject = childAt.getUserObject();
				manager.removeFolder(userObject);
				updateTree();
			}
		});

		JLabel label = new JLabel("");
		choose_pnl.add(label);
		choose_pnl.add(removeFolder_btn);

		JLabel label_1 = new JLabel("");
		choose_pnl.add(label_1);

		JLabel label_2 = new JLabel("");
		choose_pnl.add(label_2);

		JLabel label_3 = new JLabel("");
		choose_pnl.add(label_3);

		JLabel label_4 = new JLabel("");
		choose_pnl.add(label_4);

		JLabel label_5 = new JLabel("");
		choose_pnl.add(label_5);

		JLabel label_6 = new JLabel("");
		choose_pnl.add(label_6);

		JLabel label_7 = new JLabel("");
		choose_pnl.add(label_7);

		JLabel label_8 = new JLabel("");
		choose_pnl.add(label_8);

		JLabel label_9 = new JLabel("");
		choose_pnl.add(label_9);

		JLabel label_10 = new JLabel("");
		choose_pnl.add(label_10);

		JLabel label_11 = new JLabel("");
		choose_pnl.add(label_11);

		JLabel label_12 = new JLabel("");
		choose_pnl.add(label_12);

		JLabel label_13 = new JLabel("");
		choose_pnl.add(label_13);

		JLabel label_14 = new JLabel("");
		choose_pnl.add(label_14);

		JLabel label_15 = new JLabel("");
		choose_pnl.add(label_15);
		choose_pnl.add(render_btn);

		JPanel Tree_pnl = new JPanel();
		panel.add(Tree_pnl, BorderLayout.CENTER);
		Tree_pnl.setLayout(new BorderLayout(0, 0));

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("selected images");
		tree = new JTree(top);
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			private Icon DatabaseIcon = UIManager.getIcon("FileView.hardDriveIcon");

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
					boolean isLeaf, int row, boolean focused) {
				Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				Object userObject = node.getUserObject();
				/*
				 * if(userObject instanceof TreeValue) { TreeValue userObjectCast =
				 * (TreeValue)userObject; if(userObjectCast.getType() ==
				 * TreeValue.type.DATABASE) { setLeafIcon(DatabaseIcon); } }
				 */
				return c;
			}
		});
		Tree_pnl.add(tree, BorderLayout.CENTER);
	}

	private void updateTree() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
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

	enum TreeNoteType {
		Folder, Image
	}
}
