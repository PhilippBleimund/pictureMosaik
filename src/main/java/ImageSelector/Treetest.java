package ImageSelector;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//from w w  w  .j  a v  a2s . c  om
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class Treetest extends JFrame {
  private JTree tree;

  public Treetest() {
    DefaultMutableTreeNode contacts = new DefaultMutableTreeNode("Contacts");
    createNodes(contacts);
    tree = new JTree(contacts);
    tree.setCellRenderer(new MyTreeCellRenderer());
    JScrollPane treeView = new JScrollPane(tree);
    add(treeView);
    setSize(400, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  void createNodes(DefaultMutableTreeNode top) {
    DefaultMutableTreeNode contactName;
    List<Contact> contacts = new ArrayList<Contact>();
    contacts.add(new Contact("Me", true));
    contacts.add(new Contact("You"));

    Iterator<Contact> contactItr = contacts.iterator();
    while (contactItr.hasNext()) {
      contactName = new DefaultMutableTreeNode(contactItr.next());
      top.add(contactName);
    }
  }

  public static void main(String[] args) {
    new Treetest();
  }
}
class MyTreeCellRenderer extends DefaultTreeCellRenderer {

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
        hasFocus);
    if (value instanceof DefaultMutableTreeNode) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      if (node.getUserObject() instanceof String) {
        setIcon(UIManager.getIcon("FileView.computerIcon"));
      } else if (node.getUserObject() instanceof Contact) {
        Contact contact = (Contact) node.getUserObject();
        if (contact.isSomeProperty()) {
          setIcon(UIManager.getIcon("FileView.hardDriveIcon"));
        } else {
          setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
        }
      }
    }
    return this;
  }
}

class Contact {
  private boolean someProperty;
  private String name;

  public Contact(String name) {
    this(name, false);
  }

  public Contact(String name, boolean property) {
    this.someProperty = property;
    this.name = name;
  }

  public boolean isSomeProperty() {
    return someProperty;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}