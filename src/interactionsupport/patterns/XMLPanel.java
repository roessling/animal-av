/*
 * Created on 24.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
package interactionsupport.patterns;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLPanel implements ActionListener {
	protected JPanel panel;
	protected SpringLayout layout;
	protected JScrollPane treePane;
	protected JLabel deleteL;
	protected JComboBox<String> deleteBox;
	protected JButton delete;
	protected PatternUpdate updater;
	protected JTree tree;
	protected Node patterns;
	
	public XMLPanel(PatternUpdate updater) {
		updater.setXMLPanel(this);
		this.updater = updater;
		
		panel = new JPanel();
		layout = new SpringLayout();
		panel.setLayout(layout);
        
        //Construct the tree.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setUserObject("InteractionPatterns.xml");
        patterns = updater.getRoot();
        buildTreeModelByTreeWalk(patterns, root);
		tree = new JTree(root);
        treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(615, 370));
        
        String[] state = updater.getUids();
        deleteBox = new JComboBox<String>(state);
        if(state.length != 0) deleteBox.setSelectedIndex(0);
        
        deleteL = new JLabel("Select Pattern");
        
        delete = new JButton("Delete Interaction Pattern");
        delete.setMnemonic(KeyEvent.VK_D);
        delete.addActionListener(this);
        
        layout.putConstraint(SpringLayout.WEST, treePane,  5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, treePane, 5, SpringLayout.NORTH, panel);
        
        
        layout.putConstraint(SpringLayout.WEST, deleteL,  10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, deleteL, 16, SpringLayout.SOUTH, treePane);
        layout.putConstraint(SpringLayout.WEST, deleteBox,  20, SpringLayout.EAST, deleteL);
        layout.putConstraint(SpringLayout.NORTH, deleteBox, 12, SpringLayout.SOUTH, treePane);
        layout.putConstraint(SpringLayout.EAST, delete,  0, SpringLayout.EAST, treePane);
        layout.putConstraint(SpringLayout.NORTH, delete, 12, SpringLayout.SOUTH, treePane);
        
        panel.add(treePane);
        panel.add(deleteL);
        panel.add(deleteBox);
        panel.add(delete);
	}
	
	public JComponent getPanel() {
		return panel;
	}

	public void actionPerformed(ActionEvent arg0) {
		updater.delete((String)deleteBox.getSelectedItem());
		update();
	}
 
 	private void buildTreeModelByTreeWalk(Node node, DefaultMutableTreeNode parentTreeNode) {
 		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode();
 
 		switch (node.getNodeType()) {
 		case Node.COMMENT_NODE: {
 			treeNode.setUserObject(node.getTextContent());
 
 		}
 			break;
 		case Node.ELEMENT_NODE: {
 			if(node.getParentNode().equals(patterns)) {
 				treeNode.setUserObject("[" + node.getNodeName() + "] " +
 						node.getAttributes().getNamedItem("uid").getNodeValue());
 			} else {
 				treeNode.setUserObject(node.getNodeName());
 			}
 		}
 			break;
 		case Node.TEXT_NODE: {
 			String textContent = node.getTextContent().trim();
 			if (textContent.equals("")) {
 				return;
 			}
 			treeNode.setUserObject(textContent);
 		}
 			break;
 		}
 
 		parentTreeNode.add(treeNode);
 
 		if (node.hasChildNodes()) {
 			NodeList list = node.getChildNodes();
 			for (int i = 0, len = list.getLength(); i < len; i++) {
 				buildTreeModelByTreeWalk(list.item(i), treeNode);
 			}
 		}
 	}
 	
 	public void update() {
 		 //Construct the tree.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setUserObject("InteractionPatterns.xml");
        patterns = updater.getRoot();
        buildTreeModelByTreeWalk(patterns, root);
		tree = new JTree(root);
		treePane.setViewportView(tree);
        
        String[] state = updater.getUids();
        deleteBox.removeAllItems();
        for(int j=0; j < state.length; j++) {
        	deleteBox.addItem(state[j]);
        }
 	}
}
