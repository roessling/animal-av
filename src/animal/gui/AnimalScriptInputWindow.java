package animal.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.zip.GZIPOutputStream;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.misc.AnimalFileChooser;
import animal.misc.MessageDisplay;
import animalscript.core.AnimalScriptParser;

/**
 * Pops up a control window for editing and parsing AnimalScript "on the fly".
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 0.7 20050422
 */
public class AnimalScriptInputWindow implements ActionListener {
	private AbstractButton scriptingInputPaste;
	private AbstractButton scriptingInputCopy;
  private AbstractButton scriptingInputApply;

	private AbstractButton scriptingInputClear;

	private AbstractButton scriptingInputOK;

	private AbstractButton scriptingInputSave;

	private JTextArea scriptingInputArea;

	private JFrame scriptingInputFrame;

	private String lastScriptingInput = null;

	private Animal animalInstance = null;

	private AnimalScriptParser scriptParser = null;

  private Font               defaultFont        = new Font("Dialog", 0, 14);
  private int                height             = 20;
  private int                width              = 20;

	/**
	 * generates a new instance of the AnimalScriptInputWindow
	 * 
	 * @param animal the Animal instance on which the system works 
	 */
	public AnimalScriptInputWindow(Animal animal) {
		animalInstance = animal;
		if (animalInstance == null)
			animalInstance = Animal.get();
	}
  
  public void doClick_ScriptingInputOK(){
    scriptingInputOK.doClick();;
  }
  
  public void doClick_ScriptingInputSAVE(){
    scriptingInputSave.doClick();;
  }
	
	/** 
	 * initializes the scripting GUI by instantiating (if required) all
	 * graphic components and packing them inside a panel
	 */
	public void initScriptingGUI() {
		if (scriptingInputArea == null) {
			scriptingInputArea = new JTextArea(20, 60);
		}

		if (lastScriptingInput != null) {
			scriptingInputArea.setText(lastScriptingInput);
		}

		if (scriptingInputSave == null) {
			scriptingInputSave = AnimalTranslator.getGUIBuilder().generateJButton("save");
			scriptingInputSave.addActionListener(this);
		}

		if (scriptingInputApply == null) {
			scriptingInputApply = AnimalTranslator.getGUIBuilder().generateJButton("GenericEditor.apply");
			scriptingInputApply.addActionListener(this);
		}

    if (scriptingInputOK == null) {
      scriptingInputOK = AnimalTranslator.getGUIBuilder().generateJButton("GenericEditor.ok");
      scriptingInputOK.addActionListener(this);
    }
		
		if (scriptingInputCopy == null) {
			scriptingInputCopy = AnimalTranslator.getGUIBuilder().generateJButton("copy");
			scriptingInputCopy.addActionListener(this);
		}

		if (scriptingInputPaste == null) {
			scriptingInputPaste = AnimalTranslator.getGUIBuilder().generateJButton("paste");
			scriptingInputPaste.addActionListener(this);
		}

		if (scriptingInputClear == null) {
			scriptingInputClear = AnimalTranslator.getGUIBuilder().generateJButton("clear");
			scriptingInputClear.addActionListener(this);
		}

		if (scriptingInputFrame == null) {
			scriptingInputFrame = 
				AnimalTranslator.getGUIBuilder().generateJFrame("scriptSourcePane");
		}

		JPanel p = new JPanel();
		scriptingInputFrame.getContentPane().add(BorderLayout.CENTER, 
				new JScrollPane(scriptingInputArea));
		p.add(scriptingInputSave);
		p.add(scriptingInputClear);
		p.add(scriptingInputCopy);
		p.add(scriptingInputPaste);
    p.add(scriptingInputApply);
		p.add(scriptingInputOK);
		scriptingInputFrame.getContentPane().add(BorderLayout.SOUTH, p);
		scriptingInputFrame.pack();
	}

	/**
	 * save the scripting input provided by the user
	 * 
	 * @param input
	 *            the content of the scripting window
	 */
	private void saveScriptingInput(String input) {
	  String theInput = input;
		AnimalFileChooser fileChooser = Animal.getFileChooser();
		fileChooser.setAnimalScriptingFilters();

		String filename = fileChooser.openForSave(animalInstance);

		if (filename != null) {
			String type = fileChooser.getExtension();

			if (!filename.endsWith("." + type)) {
				filename += ("." + type);
			}

			try {
				long timeTaken = System.currentTimeMillis();
				OutputStream out = new BufferedOutputStream(
						new FileOutputStream(filename));

				if (type.equals("asc")) {
					out = new GZIPOutputStream(out);
				}

				java.io.PrintWriter pw = new java.io.PrintWriter(out);

				if (!theInput.endsWith(MessageDisplay.LINE_FEED)) {
				  theInput += " " + MessageDisplay.LINE_FEED;
				}

				pw.print(theInput);
				pw.close();
				out.close();

				StringBuilder sb = new StringBuilder(180);
				sb.append(animalInstance.getTime());

				if (type.equals("ac")) {
					sb.append("compressed ");
				}

				sb.append("AnimalScript");

				File f = new File(filename);
				sb.append("(").append(f.length()).append(" Bytes, ");
				sb.append(timeTaken).append(" ms).");
				f = null;
				MessageDisplay.message(sb.toString());
				sb = null;
			} catch (IOException e) {
				MessageDisplay.errorMsg("scriptSaveFailed", e.getMessage(),
						MessageDisplay.PROGRAM_ERROR);
			}
		}
	}
	
	
	/**
	 * Assign the AnimalScript code to be shown
	 * 
	 * @param content the AnimalScript code to be shown
	 */
  public void setScriptingContent(String content) {
    scriptingInputArea.setText(content);
    scriptingInputArea.setCaretPosition(0);
  }
  
  public String getScriptingContent() {
    return scriptingInputArea.getText();
  }

	/**
	 * set the frame's visibility
	 * @param isVisible
	 */
	protected void setVisible(boolean isVisible) {
		scriptingInputFrame.setVisible(isVisible);
	}
	
	/**
	 * reacts to the menu items. Not a very entertaining method :)
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == scriptingInputClear) {
			scriptingInputArea.setText(null);
			scriptingInputArea.requestFocusInWindow();
		} else if (e.getSource() == scriptingInputSave) {
			saveScriptingInput(scriptingInputArea.getText());
		} else if (e.getSource() == scriptingInputOK || e.getSource() == scriptingInputApply) {
			lastScriptingInput = scriptingInputArea.getText();

			if (lastScriptingInput != null) {
				long timeNow = System.currentTimeMillis();
 
				scriptParser = new AnimalScriptParser();
                // new code
				scriptParser.generateStreamTokenizer(lastScriptingInput, false);
				Animation tmpAnim = scriptParser.importAnimationFrom(new StringReader(lastScriptingInput), true);
                // end new code
//				Animation tmpAnim = scriptParser.programImport(
//						lastScriptingInput, false);

				if (timeNow > System.currentTimeMillis()) {
					timeNow -= System.currentTimeMillis();
				} else {
					timeNow = System.currentTimeMillis() - timeNow;
				}

				if (animalInstance.setAnimation(tmpAnim)) {
					animalInstance.getAnimation().resetChange();
					MessageDisplay.message("parsedInternalScripting", 
							Integer.valueOf((int)timeNow));
					animalInstance.setFilename("localBuffer");

					AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).setTitle(
							"Draw Window - " 
							+AnimalConfiguration.getDefaultConfiguration().getCurrentFilename());
					if (e.getSource() == scriptingInputOK)
					  scriptingInputFrame.setVisible(false);
				}
			}
		} else if (e.getSource() == scriptingInputCopy) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(scriptingInputArea.getText()), null);
			scriptingInputArea.requestFocusInWindow();
		} else if (e.getSource() == scriptingInputPaste) {
			scriptingInputArea.requestFocusInWindow();
			try {
				scriptingInputArea.setText(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString());
			} catch (HeadlessException|UnsupportedFlavorException|IOException e1) {
				scriptingInputArea.setText(null);
			}
		}
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Image img;
    Image newimg;
    ImageIcon icon;
    Dimension dim = new Dimension(0,0); 
    if (scriptingInputFrame != null) {
      dim = scriptingInputFrame.getSize();
    }



    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (dim.getWidth() < 1000) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }
      if (height < 30) {
        height = height + 5;
        width = width + 5;
      }
    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (dim.getWidth() > 600) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

      if (height > 10) {
        height = height - 5;
        width = width - 5;
      }

    }



    if (scriptingInputPaste != null)
      scriptingInputPaste.setFont(defaultFont);

    if (scriptingInputCopy != null)
      scriptingInputCopy.setFont(defaultFont);

    if (scriptingInputApply != null)
      scriptingInputApply.setFont(defaultFont);

    if (scriptingInputClear != null) {
      scriptingInputClear.setFont(defaultFont);
      ;
      img = ((ImageIcon) scriptingInputClear.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      scriptingInputClear.setIcon(icon);
    }
    if (scriptingInputOK != null)
      scriptingInputOK.setFont(defaultFont);

    if (scriptingInputSave != null) {
      scriptingInputSave.setFont(defaultFont);
      img = ((ImageIcon) scriptingInputSave.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      scriptingInputSave.setIcon(icon);

    }

    if (scriptingInputArea != null)
      scriptingInputArea.setFont(defaultFont);

    if (scriptingInputFrame != null) {
      scriptingInputFrame.setSize(dim);
    }
  }

}