package animal.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.ImageView;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import animal.misc.XProperties;

//todo
public class CreatePDFWindow extends JFrame
{

	private JPanel panel = new JPanel();
	// JTextPane text = new JTextPane();
	private JScrollPane scrollPane;
	private JPanel buttonPanel = new JPanel();
	private ImagePanel imgPanel;
	private JButton createButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton removeButton;
	private JButton cancelButton;
	private JButton rotateButton;
	private JLabel page = new JLabel("Page:");
	private JTextField pageNumber;
	private Image img;
	private int pageN = 1;
	private boolean rotate = false;
	
	private JTextField pageHeightField;
	private JTextField pageWidthField;
	private int pageHeight;
	private int pageWidth;
	private JLabel lPageSize = new JLabel("Page Size:");

	private int zoomCounter = 0;

	private Font defaultFont = new Font("Dialog", 0, 14);
	private Font buttonFont = new Font("Dialog.bold", 1, 10);
	private Font labelFont = new Font("Dialog.bold", 1, 12);

	private int height = 25;
	private int width = 25;
	private Dimension buttonSize = new Dimension(125, 40);
	private Dimension directSize = new Dimension(70, 40);
	
	 private JFileChooser chooser = new JFileChooser();


	LinkedList<JButton> buttons = new LinkedList<JButton>();


	public CreatePDFWindow(CreatePDFWindowCoordinator controller) {
		super();
		page.setFont(labelFont);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		img = controller.firstImage();
		imgPanel = new ImagePanel(img);
		scrollPane = new JScrollPane(imgPanel);
		Dimension scrollDim = new Dimension(1040, 640);
		scrollPane.setPreferredSize(scrollDim);
		scrollPane.setMinimumSize(scrollDim);
		scrollPane.setMaximumSize(scrollDim);


		this.add(panel);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pdf file(.pdf)", "pdf");

		chooser.setFileFilter(filter);

		Dimension buttonSize = new Dimension(125, 40);
		Dimension pageSize = new Dimension(20, 40);
		Dimension labelSize = new Dimension(60, 40);
		Dimension buttonPanelSize = new Dimension(1040, 50);


		pageNumber = new JTextField();
		pageNumber.setFont(defaultFont);
		pageNumber.setText(Integer.toString(pageN));

		pageNumber.setMinimumSize(pageSize);
		pageNumber.setMaximumSize(pageSize);
		pageNumber.setPreferredSize(pageSize);

		chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				File file = chooser.getSelectedFile();

				System.out.println("save file" + file.getPath());
				controller.createPDF(file.getPath(), rotate, Integer.parseInt(pageHeightField.getText()), Integer.parseInt(pageWidthField.getText()));

			}
		});

		createButton = new JButton("create PDF");
		createButton.setFocusPainted(false);
		createButton.setFont(buttonFont);
		createButton.setMinimumSize(buttonSize);
		createButton.setMaximumSize(buttonSize);
		createButton.setPreferredSize(buttonSize);
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				chooser.showSaveDialog(null);


			}
		});
		
		buttons.add(createButton);

		nextButton = new JButton("next >");
		nextButton.setFocusPainted(false);
		nextButton.setFont(buttonFont);
		nextButton.setMinimumSize(buttonSize);
		nextButton.setMaximumSize(buttonSize);
		nextButton.setPreferredSize(buttonSize);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.nextImage();

			}
		});

		buttons.add(nextButton);

		previousButton = new JButton("< page");
		previousButton.setFocusPainted(false);
		previousButton.setFont(buttonFont);
		previousButton.setMinimumSize(buttonSize);
		previousButton.setMaximumSize(buttonSize);
		previousButton.setPreferredSize(buttonSize);
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.previousPage();
			}
		});


		buttons.add(previousButton);

		removeButton = new JButton("remove ");
		removeButton.setFocusPainted(false);
		removeButton.setFont(buttonFont);
		removeButton.setMinimumSize(buttonSize);
		removeButton.setMaximumSize(buttonSize);
		removeButton.setPreferredSize(buttonSize);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.removePage();
			}
		});

		buttons.add(removeButton);

		cancelButton = new JButton("cancel");
		cancelButton.setFocusPainted(false);
		cancelButton.setFont(buttonFont);
		cancelButton.setMinimumSize(buttonSize);
		cancelButton.setMaximumSize(buttonSize);
		cancelButton.setPreferredSize(buttonSize);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.hide();
			}
		});

		buttons.add(cancelButton);

		rotateButton = new JButton("rotate");
		rotateButton.setToolTipText(
				"rotates the page in the resulting pdf counterclockwise, rotates the page back if clicked again");
		rotateButton.setFocusPainted(false);
		rotateButton.setFont(buttonFont);
		rotateButton.setMinimumSize(buttonSize);
		rotateButton.setMaximumSize(buttonSize);
		rotateButton.setPreferredSize(buttonSize);
		rotateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rotate = !rotate;

				if (rotate)
					rotateButton.setText("rotate back");
				else
					rotateButton.setText("rotate");
			}
		});
		
		pageHeightField = new JTextField();
		pageHeightField.setFont(defaultFont);
		pageHeightField.setText("900");
		
		pageWidthField = new JTextField();
		pageWidthField.setFont(defaultFont);
		pageWidthField.setText("730");

		/*pageNumber.setMinimumSize(pageSize);
		pageNumber.setMaximumSize(pageSize);
		pageNumber.setPreferredSize(pageSize);*/

		buttons.add(rotateButton);

		panel.add(scrollPane);
		buttonPanel.setMinimumSize(buttonPanelSize);
		buttonPanel.setMaximumSize(buttonPanelSize);
		buttonPanel.setPreferredSize(buttonPanelSize);

		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(removeButton);

		buttonPanel.add(page);
		buttonPanel.add(pageNumber);
		buttonPanel.add(lPageSize);
		buttonPanel.add(pageHeightField);
		buttonPanel.add(pageWidthField);
		buttonPanel.add(rotateButton);
		buttonPanel.add(createButton);
		buttonPanel.add(cancelButton);
		
		
		panel.add(buttonPanel);



	}

	public void showImage(Image image) {
		scrollPane.setBackground(Color.WHITE);
		
		img = image;
		imgPanel.setImage(img);
		imgPanel.paint(getGraphics());
		scrollPane.repaint();

	}

	public void setPageNumber(int page) {
		pageNumber.setText(Integer.toString(page));
	}

	/**
	 * @param zoomIn if true zooms in, if false zooms out
	 */
	public void zoom(boolean zoomIn) {

		/****************************************************
		 * 
		 */
		Dimension dim = new Dimension(0, 0);
		Dimension dimPanel = new Dimension(0, 0);

		if (buttonPanel != null)
			dim = buttonPanel.getSize();

		boolean zoom = false;

		if (zoomIn) {
			if (zoomCounter < 6) {
				zoomCounter++;

				zoom = true;
			}
			if (defaultFont.getSize() < 24) {
				defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() + 2);
			}

			if (labelFont.getSize() < 16) {
				labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(), labelFont.getSize() + 1);
			}

			if (height < 45) {
				height = height + 5;
				width = width + 5;
			}

		} else {
			if (zoomCounter > -1) {
				zoomCounter--;

				zoom = true;
			}
			if (defaultFont.getSize() > 10) {
				defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() - 2);
			}

			if (height > 20) {
				height = height - 5;
				width = width - 5;
			}
			if (labelFont.getSize() > 9) {
				labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(), labelFont.getSize() - 1);
			}

		}

		buttonFont = new Font(buttonFont.getFontName(), buttonFont.getStyle(), defaultFont.getSize() - 2);

		if (zoom) {


			Dimension dimFrame = new Dimension(1060 + 40 * zoomCounter, 730 + 2 * zoomCounter);
			this.setSize(dimFrame);

			this.setBounds((int) this.getLocation().getX(), (int) this.getLocation().getY(), dimFrame.width,
					dimFrame.height);

			Dimension scrollDim = new Dimension(1040 + 40 * zoomCounter, 640 + 2 * zoomCounter);
			scrollPane.setPreferredSize(scrollDim);
			scrollPane.setMinimumSize(scrollDim);
			scrollPane.setMaximumSize(scrollDim);

			dimPanel = new Dimension(1040 + 60 * zoomCounter, 50 + 2 * zoomCounter);

			if (buttonPanel != null) {

				buttonPanel.setMinimumSize(dimPanel);
				buttonPanel.setMaximumSize(dimPanel);
				buttonPanel.setPreferredSize(dimPanel);
			}



			if (zoomIn) {
				buttonSize.setSize(buttonSize.getWidth() + 5, buttonSize.getHeight() + 2);
				directSize.setSize(directSize.getWidth() + 2, directSize.getHeight() + 2);

			} else {
				buttonSize.setSize(buttonSize.getWidth() - 5, buttonSize.getHeight() - 2);
				directSize.setSize(directSize.getWidth() - 2, directSize.getHeight() - 2);
			}
			for (JButton button : buttons)
				zoomButton(button, buttonFont, buttonSize);

		
			if (page != null) {
				page.setFont(labelFont);
			}

			if (pageNumber != null) {
				pageNumber.setFont(defaultFont);
			}

			if (lPageSize != null) {
				lPageSize.setFont(defaultFont);
			}

			if (pageHeightField != null) {
				pageHeightField.setFont(defaultFont);
			}

			if (pageWidthField != null) {
				pageWidthField.setFont(defaultFont);
			}

		}

	}

	private void zoomButton(AbstractButton but, Font f, Dimension buttonDim) {

		if (but.getIcon() != null) {
			Image img = ((ImageIcon) but.getIcon()).getImage();
			Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newimg);
			but.setIcon(icon);
		}
		but.setFont(f);

		but.setPreferredSize(buttonDim);
		but.setMinimumSize(buttonDim);
		but.setMaximumSize(buttonDim);

	}

}
