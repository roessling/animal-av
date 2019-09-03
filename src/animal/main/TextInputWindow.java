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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import animal.misc.XProperties;

public class TextInputWindow extends JFrame {

	private JPanel panel = new JPanel();
	private JTextPane text = new JTextPane();
	private JScrollPane scrollPane;
	private JPanel buttonPanel = new JPanel();
	private JPanel menuPanel = new JPanel();
	private JButton okButton;
	private JButton cleanButton;
	private JButton bulletButton;
	private String bullet = "\u2022";
	private JComboBox sizes;
	private JLabel textSizeLabel;
	private JComboBox styles;
	private JLabel textStyleLabel;
	private JComboBox names;
	private JLabel textNameLabel;

	private Font defaultFont = new Font("Dialog", 0, 14);
	private Font buttonFont = new Font("Dialog.bold", 1, 10);
	private Font labelFont = new Font("Dialog.bold", 1, 12);

	private int height = 25;
	private int width = 25;
	private int zoomCounter = 0;


	public TextInputWindow( AnimalToPDFWindow controller) {
		super();

		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		scrollPane = new JScrollPane(text);
		Dimension scrollDim = new Dimension(780, 530);
		scrollPane.setPreferredSize(scrollDim);
		scrollPane.setMinimumSize(scrollDim);
		scrollPane.setMaximumSize(scrollDim);;

		this.add(panel);



		Dimension buttonSize = new Dimension(180, 30);
		Dimension buttonPanelSize = new Dimension(640, 30);

		text.addMouseListener(new MouseListener() {


			@Override
			public void mouseClicked(MouseEvent e) {
				if (text.getText().equals("text page added"))
				text.setText("");
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (text.getText().equals("text page added"))
				text.setText("");
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (text.getText().equals("text page added"))
				text.setText("");
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
		buttonPanel.setLayout(new BorderLayout(0, 0));
		okButton = new JButton("add text as pdf page");
		okButton.setFocusPainted(false);
		okButton.setFont(new Font(okButton.getFont().getFontName(), Font.PLAIN, 10));
		okButton.setMinimumSize(buttonSize);
		okButton.setMaximumSize(buttonSize);
		okButton.setPreferredSize(buttonSize);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.addTextAsPage(text.getText());
				text.setText("text page added");
				

			}
		});
		
		cleanButton = new JButton("clear text area");
		cleanButton.setFocusPainted(false);
		cleanButton.setFont(new Font(cleanButton.getFont().getFontName(), Font.PLAIN, 10));
		cleanButton.setMinimumSize(buttonSize);
		cleanButton.setMaximumSize(buttonSize);
		cleanButton.setPreferredSize(buttonSize);
		cleanButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("");

			}
		});

		bulletButton = new JButton("add " + bullet + " in a new line");
		bulletButton.setFocusPainted(false);
		bulletButton.setFont(new Font(bulletButton.getFont().getFontName(), Font.PLAIN, 10));
		bulletButton.setMinimumSize(buttonSize);
		bulletButton.setMaximumSize(buttonSize);
		bulletButton.setPreferredSize(buttonSize);
		bulletButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (text.getText().equals("")) {
					text.setText(bullet + " ");
					bulletButton.transferFocusBackward();
					okButton.transferFocusBackward();
				} else {
					text.setText(text.getText() + "\r\n" + bullet + " ");
					bulletButton.transferFocusBackward();
					okButton.transferFocusBackward();

				}
			}
		});


		String[] stringSizes = { "4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24", "28", "30" };
		sizes = new JComboBox(stringSizes);
		sizes.setSelectedIndex(3);
		sizes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				String stringSize = (String) cb.getSelectedItem();
				int size = Integer.valueOf(stringSize);
				changeSize(size);
				// text.setFont(new Font(text.getFont().getFontName(),
				// text.getFont().getStyle(), size));
				
			}
		});

		String[] stringSytle = { "Plain", "Bold", "Italic", "Bold & Italic" };
		styles = new JComboBox(stringSytle);
		styles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			
				int styleNumber = 0;

				JComboBox cb = (JComboBox) e.getSource();
				String stringStyle = (String) cb.getSelectedItem();
				changeStyle(stringStyle);

			}
		});

		String[] stringNames = { "Andale Mono", "Arial", "Arial Black", "Avant Garde", "Bookman", "Century Schoolbook",
				"Comic Sans MS", "Courier", "Courier New", "Garamond", "Georgia", "Helvetica", "Impact", "Palatino",
				"Tahoma", "Times", "Times New Roman", "Verdana" };
		names = new JComboBox(stringNames);
		names.setSelectedIndex(3);
		names.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();

				String fontName = (String) cb.getSelectedItem();

				text.setFont(
						new Font(fontName, text.getFont().getStyle(), text.getFont().getSize()));

			}
		});

		menuPanel.setMinimumSize(buttonPanelSize);
		menuPanel.setMaximumSize(buttonPanelSize);
		menuPanel.setPreferredSize(buttonPanelSize);



		textNameLabel = new JLabel();
		textNameLabel.setText("choose font:");
		menuPanel.add(textNameLabel);

		menuPanel.add(names);

		textStyleLabel = new JLabel();
		textStyleLabel.setText("choose text style:");
		menuPanel.add(textStyleLabel);

		menuPanel.add(styles);

		textSizeLabel = new JLabel();
		textSizeLabel.setText("choose text size:");
		menuPanel.add(textSizeLabel);
		
		menuPanel.add(sizes);




		panel.add(menuPanel);
		panel.add(scrollPane);
		buttonPanel.setMinimumSize(buttonPanelSize);
		buttonPanel.setMaximumSize(buttonPanelSize);
		buttonPanel.setPreferredSize(buttonPanelSize);
		buttonPanel.add(okButton, BorderLayout.LINE_START);
		buttonPanel.add(cleanButton, BorderLayout.LINE_END);
		buttonPanel.add(bulletButton, BorderLayout.CENTER);
		panel.add(buttonPanel);




	}

	public Image getImage() {
		Rectangle region = text.getBounds();
		BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.translate(-region.x, -region.y);
		g2d.setColor(text.getBackground());
		g2d.fillRect(region.x, region.y, region.width, region.height);
		text.paint(g2d);
		g2d.dispose();
		return image;
	}

	public void insertImage() {

	}

	private void changeStyle(String style) {
		StyledDocument doc = (StyledDocument) text.getDocument();
		int selectionEnd = text.getSelectionEnd();
		int selectionStart = text.getSelectionStart();
		if (selectionStart == selectionEnd) {
			return;
		}
		Element element = doc.getCharacterElement(selectionStart);
		AttributeSet as = element.getAttributes();

		MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());

		switch (style) {
		case "Plain":
			StyleConstants.setItalic(asNew, false);
			StyleConstants.setBold(asNew, false);
			break;
		case "Bold":
			StyleConstants.setBold(asNew, true);
			StyleConstants.setItalic(asNew, false);
			break;
		case "Italic":
			StyleConstants.setBold(asNew, false);
			StyleConstants.setItalic(asNew, true);
			break;

		case "Bold & Italic":
			StyleConstants.setBold(asNew, true);
			StyleConstants.setItalic(asNew, true);

			break;
		default:
			StyleConstants.setItalic(asNew, false);
			StyleConstants.setBold(asNew, false);

		}
		doc.setCharacterAttributes(selectionStart, text.getSelectedText().length(), asNew, true);


	}

	private void changeSize(int size) {
		StyledDocument doc = (StyledDocument) text.getDocument();
		int selectionEnd = text.getSelectionEnd();
		int selectionStart = text.getSelectionStart();
		if (selectionStart == selectionEnd) {
			return;
		}
		Element element = doc.getCharacterElement(selectionStart);
		AttributeSet as = element.getAttributes();

		MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());


		StyleConstants.setFontSize(asNew, size);

		doc.setCharacterAttributes(selectionStart, text.getSelectedText().length(), asNew, true);

	}

	/**
	 * @param zoomIn if true zooms in, if false zooms out
	 */
	public void zoom(boolean zoomIn) {



		Dimension dim = new Dimension(0, 0);
		Dimension dimPanel = new Dimension(0, 0);

		if (menuPanel != null)
			dimPanel = menuPanel.getSize();
		if (zoomIn) {
			if (zoomCounter < 6)
				zoomCounter++;
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
			if (zoomCounter > -1)
				zoomCounter--;
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

		setBounds(10, 10, 880 + 60 * zoomCounter, 640 + 6 * zoomCounter);
		setMinimumSize(new Dimension(200, 600));
		setPreferredSize(new Dimension(880 + 60 * zoomCounter, 640 + 6 * zoomCounter));

		if (panel != null) {

			dimPanel = new Dimension(800 + 60 * zoomCounter, 630 + 6 * zoomCounter);
			panel.setMinimumSize(dimPanel);
			panel.setMaximumSize(dimPanel);
			panel.setPreferredSize(dimPanel);
		}

		if (scrollPane != null) {

			dimPanel = new Dimension(730 + 60 * zoomCounter, 530 + 2 * zoomCounter);
			scrollPane.setMinimumSize(dimPanel);
			scrollPane.setMaximumSize(dimPanel);
			scrollPane.setPreferredSize(dimPanel);
		}

		if (menuPanel != null) {

			dimPanel = new Dimension(640 + 60 * zoomCounter, 30 + 2 * zoomCounter);
			menuPanel.setMinimumSize(dimPanel);
			menuPanel.setMaximumSize(dimPanel);
			menuPanel.setPreferredSize(dimPanel);
		}

		if (buttonPanel != null) {

			dimPanel = new Dimension(640 + 60 * zoomCounter, 30 + 2 * zoomCounter);
			buttonPanel.setMinimumSize(dimPanel);
			buttonPanel.setMaximumSize(dimPanel);
			buttonPanel.setPreferredSize(dimPanel);
		}



		if (text != null) {
			Dimension textDim = new Dimension(290 + zoomCounter * 10, 100 + 2 * zoomCounter);
			text.setPreferredSize(textDim);
			text.setMinimumSize(textDim);
			text.setMaximumSize(textDim);

		}

		if (textSizeLabel != null) {
			textSizeLabel.setFont(labelFont);
		}
		
		if (textStyleLabel != null) {
			textStyleLabel.setFont(labelFont);
		}
		if (textNameLabel != null) {
			textNameLabel.setFont(labelFont);
		}

		if (names != null) {
			names.setFont(labelFont);
		}

		if (sizes != null) {
			sizes.setFont(labelFont);
		}

		if (styles != null) {
			styles.setFont(labelFont);
		}

		
		if (okButton != null) {
			zoomButton(okButton, buttonFont);
		}
		
		if (cleanButton != null) {
			zoomButton(cleanButton, buttonFont);
		}
		
		if (bulletButton != null) {
			zoomButton(bulletButton, buttonFont);
		}



	}

	private void zoomButton(AbstractButton but, Font f) {

		if (but.getIcon() != null) {
			Image img = ((ImageIcon) but.getIcon()).getImage();
			Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newimg);
			but.setIcon(icon);
		}
		but.setFont(f);
		but.setPreferredSize(new Dimension(180 + 20 * zoomCounter, 30 + 2 * zoomCounter));

	}

}
