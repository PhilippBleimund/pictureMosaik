package ImageSelector.FolderTree;

import java.io.File;

public class TreeValue {
	
	String text;
	type type;
	File URIinTree;
	
	public TreeValue(String text, File URIinTree, type type) {
		this.text = text;
		this.type = type;
		this.URIinTree = URIinTree;
	}
	
	public type getType() {
		return this.type;
	}
	
	public File getURI() {
		return this.URIinTree;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public enum type{
		IMAGES,
		DATABASE
	}
}
