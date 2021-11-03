package ImageSelector.FolderTree;

import java.io.File;
import java.util.ArrayList;

public class FolderTreeNode {

	ArrayList<FolderTreeNode> children = new ArrayList<FolderTreeNode>();
	ArrayList<File> files = new ArrayList<File>();
	ArrayList<File> Databases = new ArrayList<File>();
	
	File folder;
	
	public FolderTreeNode(File folder) {
		this.folder = folder;
	}
	
	public FolderTreeNode(FolderTreeNode child, File file, File folder) {
		if(child != null)
			this.children.add(child);
		if(file != null)
			this.files.add(file);
		this.folder = folder;
	}
	
	public FolderTreeNode(ArrayList<FolderTreeNode> children, ArrayList<File> files, File folder) {
		if(children != null)
			this.children.addAll(children);
		if(files != null)
			this.files.addAll(files);
		this.folder = folder;
	}

	public void addNode(FolderTreeNode child) {
		this.children.add(child);
	}
	
	public void addNode(ArrayList<FolderTreeNode> children) {
		this.children.addAll(children);
	}
	
	public void addFiles(ArrayList<File> files) {
		this.files.addAll(files);
	}
	
	public void addDatabases(ArrayList<File> Databases) {
		this.Databases.addAll(Databases);
	}
	
	public ArrayList<FolderTreeNode> getChildren() {
		return this.children;
	}
	
	public FolderTreeNode getChildrenAt(int index) {
		return this.children.get(index);
	}
	
	public ArrayList<File> getFiles(){
		return this.files;
	}
	
	public ArrayList<File> getDatabases(){
		return this.Databases;
	}
	
	public File getFolder() {
		return this.folder;
	}
}
