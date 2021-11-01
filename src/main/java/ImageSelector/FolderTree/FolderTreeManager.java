package ImageSelector.FolderTree;

import java.io.File;
import java.util.ArrayList;

public class FolderTreeManager {

	FolderTreeNode tree = new FolderTreeNode(new File(""));
	
	public FolderTreeManager() {
		ArrayList<File> files = new ArrayList<File>();
		files.add(new File("C:\\Users\\Philipp Bleimund\\Pictures\\Uplay"));
		addFiles(files);
	}
	
	/**
	 * 
	 * @param files has to be in the same Folder
	 */
	public void addFiles(ArrayList<File> files) {
		File representive = files.get(0).getParentFile();
		FolderTreeNode node = treeSearch(tree, representive);
		node.addFiles(files);
	}
	
	/**
	 * 
	 * @param parentNode
	 * @param file
	 * @return the most nearest Node to the Path
	 */
	private FolderTreeNode treeSearch(FolderTreeNode parentNode, File file) {
		ArrayList<FolderTreeNode> children = parentNode.getChildren();
		for(int i=0;i<children.size();i++) {
			File folder = children.get(i).getFolder();
			if(parentOfFile(folder, file)) {
				return treeSearch(children.get(i), file);
			}
		}
		return parentNode;
	}
	
	private boolean parentOfFile(File parent, File child) {
		ArrayList<File> parentList = new ArrayList<File>();
		while(parent.getParentFile() != null) {
			parentList.add(parent.getParentFile());
			parent = parent.getParentFile();
		}
		
		ArrayList<File> childList = new ArrayList<File>();
		while(child.getParentFile() != null) {
			childList.add(child.getParentFile());
			child = child.getParentFile();
		}
		
		for(int i=0;i<parentList.size();i++) {
			if(!(parentList.get(i).equals(childList.get(i)))) {
				return false;
			}
		}
		return true;
	}
}
