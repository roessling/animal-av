package htdptl.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import translator.ResourceLocator;

public class ExampleCollection extends JComponent implements
		TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	private JTree tree;
	private JTextArea textArea;

	public ExampleCollection() {

		textArea = new JTextArea();
		Font font = new Font("Courier", Font.PLAIN, 12);
		textArea.setFont(font);

		TopPanel topPanel = new TopPanel(
				"<html>Please choose a program from the example collection. You can select the expressions to visualize on the next page.");
		setLayout(new BorderLayout());

		// File examples = new File("./src/htdptl_examples");
		// File[] fileArray = examples.listFiles();
		// TreeMap<String, TreeMap<String, File>> folders = new TreeMap<String,
		// TreeMap<String, File>>();
		// for (int i = 0; i < fileArray.length; i++) {
		// TreeMap<String, File> folder = buildFolder(fileArray[i]);
		// String name = fileArray[i].getName();
		// folders.put(name, folder);
		// files.putAll(folder);
		// }
		// Object[] hierarchy = new Object[folders.size() + 1];
		// hierarchy[0] = "examples";
		// int i = 0;
		// for (Iterator<String> iterator = folders.keySet().iterator();
		// iterator
		// .hasNext();) {
		// String key = iterator.next();
		// ArrayList<Object> folder = new ArrayList<Object>();
		// folder.add(key);
		// folder.addAll(folders.get(key).keySet());
		// hierarchy[i + 1] = folder.toArray();
		// i++;
		// }

		Object[] hierarchy = new Object[] {
				"examples",
        new Object[] { "GdI1_WS2009", "T0.19.scm", "T1.13.scm",
//				new Object[] { "GDI 1 (WS 2009-2010)", "T0.19.scm", "T1.13.scm",
						"T1.21.scm", "T1.22.scm", "T1.33.scm", "T1.41.scm", "T1.48.scm", "T1.57.scm",
						"T1.9.scm", "T2.14-29.scm", "T2.32-34.scm", "T2.35-37.scm", "T2.6.scm",
						"T3.19.scm", "T3.22.scm", "T3.23.scm", "T3.26.scm", "T3.27.scm",
						"T3.32-33.scm", "T3.36.scm", "T3.4.scm", "T3.40-45.scm", "T3.6.scm",
						"T3.64-71.scm", "T3.74-82.scm", "T5.13.scm",
						"T5.16-18.scm", "T5.20.scm", "T5.39-50.scm", "T5.5-7.scm", "T5.54.scm",
						"T5.57-59.scm", "T5.64-65.scm", "T5.9-12.scm",
						"T6.21-22.scm", "T7.32-33.scm",
						"T7.38.scm", "T7.65-70.scm" },
				new Object[] { "fold", "foldl.scm", "foldr.scm" },
				new Object[] { "map", "map.scm" }
				};

		tree = ExampleTree.create(hierarchy);
		tree.addTreeSelectionListener(this);

		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());
		left.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPaneLeft = new JScrollPane(tree);
		scrollPaneLeft.setBorder(new LineBorder(Color.gray, 1));
		left.add(scrollPaneLeft, BorderLayout.CENTER);

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPaneRight = new JScrollPane(textArea);
		scrollPaneRight.setBorder(new LineBorder(Color.gray, 1));
		right.add(scrollPaneRight);

		add(topPanel, BorderLayout.PAGE_START);
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.CENTER);
	}

	public TreeMap<String, File> buildFolder(File file) {
		TreeMap<String, File> result = new TreeMap<String, File>();
		File[] fileArray = file.listFiles();
		for (int i = 0; i < fileArray.length; i++) {
			if (fileArray[i].isFile()) {
				String name = fileArray[i].getName();
				if (name.lastIndexOf('.') > -1) {
					name = name.substring(0, name.lastIndexOf('.'));
				}
				result.put(name, fileArray[i]);
			}
		}
		return result;

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null)
			// Nothing is selected.
			return;

		
		
		if (node.isLeaf()) {
			
			String resource = node.toString();
			while (node.getParent()!=null) {
				resource = node.getParent().toString()+"/"+resource;
				node = node.getParent();
			}
			resource = "htdptl/"+resource;
			try {
				InputStream is = ResourceLocator.getResourceLocator().getResourceStream(resource);
				final char[] buffer = new char[0x10000];
				StringBuilder out = new StringBuilder();
				Reader in;
				in = new InputStreamReader(is, "UTF-8");
				int read;
				do {
				  read = in.read(buffer, 0, buffer.length);
				  if (read>0) {
				    out.append(buffer, 0, read);
				  }
				} while (read>=0);
				textArea.setText(out.toString());
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	public String getProgram() {
		return textArea.getText();
	}

}
