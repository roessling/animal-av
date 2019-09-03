package htdptl.gui;

import htdptl.facade.Batch;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class BatchPage extends JComponent implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966743621831615914L;
	private JFileChooser source;
	private JLabel sourceLabel;
	private JFileChooser target;
	private JLabel targetLabel;
	private File sourceDirectory;
	private File targetDirectory;
	private JButton runButton;
  ProgressMonitor monitor;
	Timer timer;
  Batch batch;

	public BatchPage() {

		TopPanel topPanel = new TopPanel(
				"<html>Choose a source directory and a target directory. "
						+ "<br>For all .scm files in the source directory corresponding animations will be generated and saved in the target directory.");

		setLayout(new BorderLayout());

		source = new JFileChooser();
		source.setCurrentDirectory(new java.io.File("."));
		source.setDialogTitle("Choose the source directory");
		source.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		source.setAcceptAllFileFilterUsed(false);

		target = new JFileChooser();
		target.setCurrentDirectory(new java.io.File("."));
		target.setDialogTitle("Choose the source directory");
		target.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		target.setAcceptAllFileFilterUsed(false);

		JPanel sourcePanel = new JPanel();
		sourcePanel.setLayout(new BorderLayout());
		sourcePanel.setPreferredSize(new Dimension(600, 100));
		sourcePanel.setBorder(BorderFactory
				.createTitledBorder("source directory"));

		Box box = Box.createHorizontalBox();
		sourceLabel = new JLabel("...");
		box.add(sourceLabel);
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		sourcePanel.add(box, BorderLayout.PAGE_START);

		box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		JButton sourceButton = new JButton("Choose ...");
		sourceButton.setActionCommand("source");
		sourceButton.addActionListener(this);
		box.add(sourceButton);
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		sourcePanel.add(box, BorderLayout.CENTER);

		JPanel targetPanel = new JPanel();
		targetPanel.setLayout(new BorderLayout());
		targetPanel.setPreferredSize(new Dimension(600, 100));
		targetPanel.setBorder(BorderFactory
				.createTitledBorder("target directory"));

		box = Box.createHorizontalBox();
		targetLabel = new JLabel("...");
		box.add(targetLabel);
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		targetPanel.add(box, BorderLayout.PAGE_START);

		box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		JButton targetButton = new JButton("Choose ...");
		targetButton.setActionCommand("target");
		targetButton.addActionListener(this);
		box.add(targetButton);
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		targetPanel.add(box, BorderLayout.CENTER);

		runButton = new JButton("Start.");
		runButton.setActionCommand("run");
		runButton.addActionListener(this);
		// runButton.setEnabled(false);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
		centerPanel.add(sourcePanel);
		centerPanel.add(targetPanel);
		centerPanel.add(runButton);

		JLabel text = new JLabel(
				"<html>Please notice that it is not possible to apply filters in batch modus. "
						+ "<br>If the generated visualisation is too large no file is generated. "
						+ "<br>Please load these programs seperatly and apply filters.");
		text.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 25));
		centerPanel.add(text, BorderLayout.CENTER);
		add(topPanel, BorderLayout.PAGE_START);
		add(centerPanel, BorderLayout.CENTER);

	}

	public static void main(String[] args) {

		// Create and set up the window.
		JFrame frame = new JFrame("HtDP-TL Visualization Wizard");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		frame.getContentPane().add(new BatchPage());
		frame.getContentPane().setPreferredSize(new Dimension(750, 800));

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "source") {
			if (source.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				sourceDirectory = source.getSelectedFile();
				sourceLabel.setText(sourceDirectory.toString());
			}
			updateButton();
		} else if (e.getActionCommand() == "target") {
			if (target.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				targetDirectory = target.getSelectedFile();
				targetLabel.setText(targetDirectory.toString());
			}
			updateButton();
		} else if (e.getActionCommand() == "run") {
			batch = new Batch(sourceDirectory, targetDirectory);
			monitor = new ProgressMonitor(null, "Batch Modus.",
					"Initializing . . .", 0, 100);
			timer = new Timer(500, this);
			timer.start();
			batch.start();
			runButton.setEnabled(false);
		} else {
			SwingUtilities.invokeLater(new Update());
		}

	}

	private void updateButton() {
		runButton.setEnabled(source != null && target != null);
	}

	class Update implements Runnable {
		public void run() {
			if (monitor.isCanceled()) {
				System.out.println("done");
				monitor.close();
				timer.stop();
				batch.stop();
			} else {
				if (batch.totalFiles() == 0) {
					monitor.setNote("Initializing ... ");
				} else {
					Double progress = new Double(batch.processedFiles())
							/ new Double(batch.totalFiles()) * 100;
					monitor.setProgress(progress.intValue());
					monitor.setNote("Processing file "
							+ batch.getCurrentFileName());

					if (progress == 100.0) {
						timer.stop();
						
						int[] result = batch.getLog();
						File[] files = batch.getFiles();

						final JFrame frame = new JFrame();
						frame.setLocation(new Point(200,200));						
						Container pane = frame.getContentPane();	
						pane.setLayout(new BorderLayout());
						
						String[] columnNames = { "File", "Result" };
						Object[][] data = new Object[result.length][2];
						for (int i = 0; i < result.length; i++) {
							data[i][0] = files[i].getName();
							if (result[i]==-1) {
								data[i][1] = "Visualisation is too large!";
							}
							else {
								data[i][1] = "OK. ("+result[i]+" steps)";								
							}
						}

						final JTable table = new JTable(data, columnNames);
						table.setPreferredScrollableViewportSize(new Dimension(
								300, 400));
						table.setFillsViewportHeight(true);
						table.getColumnModel().getColumn(0).setPreferredWidth(120);
						table.getColumnModel().getColumn(1).setPreferredWidth(180);

						// Create the scroll pane and add the table to it.
						JScrollPane scrollPane = new JScrollPane(table);
						pane.add(scrollPane);
						
						JButton close = new JButton("OK");
						close.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								frame.setVisible(false);
								HtDPTLWizard.instance.cancel();
							}
							
						});
						pane.add(scrollPane, BorderLayout.CENTER);
						pane.add(close, BorderLayout.PAGE_END);
						
						frame.pack();
						frame.setVisible(true);

					}

				}

			}

		}
	}

}
