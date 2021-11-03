package ImageSelector.FolderTree;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.tree.DefaultMutableTreeNode;

import ImageSelector.FolderTree.TreeValue.type;

public class FolderTreeManager {

	FolderTreeNode tree = new FolderTreeNode(new File(""));
	
	public FolderTreeManager() {

	}
	
	/**
	 * 
	 * @param files has to be in the same Folder
	 */
	public void addFiles(ArrayList<File> files) {
		File representive = files.get(0).getParentFile();
		LinkedBlockingQueue<File> queue = getFileAsQueue(new LinkedBlockingQueue<File>(), representive);
		FolderTreeNode addQueueToTree = addQueueToTree(queue, tree);
		files.removeAll(addQueueToTree.getFiles());
		addQueueToTree.addFiles(files);
	}
	
	/**
	 * 
	 * @param Databases has to be in the same Folder
	 */
	public void addDatabases(ArrayList<File> Databases) {
		File representive = Databases.get(0).getParentFile();
		LinkedBlockingQueue<File> queue = getFileAsQueue(new LinkedBlockingQueue<File>(), representive);
		FolderTreeNode addQueueToTree = addQueueToTree(queue, tree);
		Databases.removeAll(addQueueToTree.getDatabases());
		addQueueToTree.addDatabases(Databases);
	}
	
	public void removeFolder(File folder) {
		LinkedBlockingQueue<File> queue = getFileAsQueue(new LinkedBlockingQueue<File>(), folder);
		//no new Path will be created during this process since only available path could be selected
		FolderTreeNode addQueueToTree = addQueueToTree(queue, tree);
		addQueueToTree.getFiles().clear();
		addQueueToTree.getDatabases().clear();
	}
	
	private FolderTreeNode addQueueToTree(LinkedBlockingQueue<File> queue, FolderTreeNode subTree) {
		File file = queue.poll();
		if(file != null) {
			FolderTreeNode existing = null;
			ArrayList<FolderTreeNode> children = subTree.getChildren();
			for(int i=0;i<children.size();i++) {
				if(children.get(i).getFolder().compareTo(file) == 0) {
					existing = children.get(i);
				}
			}
			if(existing == null) {
				existing = new FolderTreeNode(file);
				subTree.addNode(existing);
			}
			return addQueueToTree(queue, existing);
		}
		return subTree;
	}
	
	private LinkedBlockingQueue<File> getFileAsQueue(LinkedBlockingQueue<File> queue, File file){
		if(file != null) {
			LinkedBlockingQueue<File> queue2 = getFileAsQueue(queue, file.getParentFile());
			queue2.add(file);
			return queue2;
		}
		return queue;
	}
	
	public DefaultMutableTreeNode getTreeModel(DefaultMutableTreeNode model) {
		ArrayList<FolderTreeNode> children = tree.getChildren();
		for(int i=0;i<children.size();i++) {
			TreeToModel(model, children.get(i));
		}
		return model;
	}
	
	private void TreeToModel(DefaultMutableTreeNode model, FolderTreeNode subTree) {
		ArrayList<File> files = subTree.getFiles();
		ArrayList<File> Databases = subTree.getDatabases();
		ArrayList<FolderTreeNode> children = subTree.getChildren();
		if(files.size() == 0 && Databases.size() == 0 && children.size() <= 1) {
			for(int i=0;i<children.size();i++) {
				TreeToModel(model, children.get(i));
			}
		}else {
			DefaultMutableTreeNode subModel = new DefaultMutableTreeNode(subTree.getFolder().getName());
			if(files.size() > 0) {
				subModel.add(new DefaultMutableTreeNode(new TreeValue(files.size() + " imgages", files.get(0).getParentFile(), type.IMAGES)));
			}
			if(Databases.size() > 0) {
				for(int i=0;i<Databases.size();i++) {
					DefaultMutableTreeNode database = new DefaultMutableTreeNode(new TreeValue(Databases.get(i).getName(), Databases.get(0).getParentFile(), type.DATABASE));
					subModel.add(database);
				}
			}
			model.add(subModel);
			for(int i=0;i<children.size();i++) {
				TreeToModel(subModel, children.get(i));
			}
		}
	}
	
	public ArrayList<File> getTreeFiles() {
		ArrayList<File> files = new ArrayList<File>();
		getFiles(files, tree);
		return files;
	}
	
	private void getFiles(ArrayList<File> allFiles, FolderTreeNode subTree) {
		ArrayList<File> files = subTree.getFiles();
		ArrayList<FolderTreeNode> children = subTree.getChildren();
		if(files.size() == 0) {
			for(int i=0;i<children.size();i++) {
				getFiles(allFiles, children.get(i));
			}
		}else {
			allFiles.addAll(files);
			for(int i=0;i<children.size();i++) {
				getFiles(allFiles, children.get(i));
			}
		}
	}
	
	public ArrayList<File> getTreeDatabases() {
		ArrayList<File> Databases = new ArrayList<File>();
		getDatabases(Databases, tree);
		return Databases;
	}
	
	private void getDatabases(ArrayList<File> allDatabases, FolderTreeNode subTree) {
		ArrayList<File> files = subTree.getDatabases();
		ArrayList<FolderTreeNode> children = subTree.getChildren();
		if(files.size() == 0) {
			for(int i=0;i<children.size();i++) {
				getDatabases(allDatabases, children.get(i));
			}
		}else {
			allDatabases.addAll(files);
			for(int i=0;i<children.size();i++) {
				getDatabases(allDatabases, children.get(i));
			}
		}
	}
}
