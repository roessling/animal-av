package gfgaa.gui;

import gfgaa.gui.components.ColorChooserComboBox;
import gfgaa.gui.components.SComponent;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.others.LanguageInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/** Object for the graph color settings that would later
  * be used for the animation.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public class ColorChooser extends JDialog implements LanguageInterface {

    /**
   * 
   */
  private static final long serialVersionUID = 8380845671758741151L;

    /** Panel des ColorChoosers Dialog.
      *
      * @author S. Kulessa
      * @version 0.97c
      */
    private final class ColorPanel extends SPanel {

        /**
       * 
       */
      private static final long serialVersionUID = 5475101644497374656L;

        /** Array that contains ColorChooser components. */
        ColorChooserComboBox[] chooser;

        private JLabel[] chooserLabel;
        private JButton apply;
        private JButton reset;
        private JButton exit;

        /** (constructor)<br>
          * Constructs the panel and his components.
          */
        public ColorPanel() {
            setLayout(null);

            chooser = new ColorChooserComboBox[8];
            chooserLabel = new JLabel[8];
            String[][] text = new String[][] {
                                {"Hintergrund", "Background"},
                                {"Kante I", "Edge I"},
                                {"Kante II", "Edge II"},
                                {"Gewicht", "Weight"},
                                {"Kantenspitze", "Edge Arrow"},
                                {"Rand der Knoten", "Node Borders"},
                                {"Knoten", "Node Background"},
                                {"Knoten Kennzeichnung", "Node Tag"}};

            for (int i = 0; i < 8; i++) {
                createChooser(i, text[i], 10 + 30 * i);
            }

            add(createResetButton());
            add(createApplyButton());
            add(createReturnButton());

            setColorSelection();
            this.changeLanguageSettings(mainclass.getLanguageSettings());
        }

        /** (internal display method)<br>
          * Applys the current settings to the panel components.
          */
        void setColorSelection() {
            for (int i = 0; i < chooser.length; i++) {

                if (mainclass.getGraphTyp()
                        == AbstractGraph.GRAPHTYP_RESIDUAL) {

                    switch (i) {
                    case COLOR_EDGE_ONE:
                        chooser[i].setColorSelected(colors[COLOR_EDGE_FLOW]);
                        break;
                    case COLOR_EDGE_TWO:
                        chooser[i].setColorSelected(colors[COLOR_EDGE_CAP]);
                        break;
                    case COLOR_EDGE_TOP:
                        chooser[i].setColorSelected(colors[COLOR_EDGE_RTOP]);
                        break;
                    default:
                        chooser[i].setColorSelected(colors[i]);
                    }
                } else {
                    chooser[i].setColorSelected(colors[i]);
                }
            }
        }

        /** (panel construction method)<br>
          * Creates the ColorChooserComboBoxes and the belonging labels.
          *
          * @param nr             Internal id
          * @param chooserText    Colorchooser text
          * @param pos            Y-Axis position in the panel
          */
        private void createChooser(final int nr, final String[] chooserText,
                                   final int pos) {
            chooserLabel[nr] = new JLabel();
            chooserLabel[nr].setFont(new Font("Serif", Font.BOLD, 14));
            chooserLabel[nr].setBounds(10, pos, 150, 25);

            this.add(chooserLabel[nr]);
            this.add(new SComponent(chooserLabel[nr], chooserText));

            chooser[nr] = new ColorChooserComboBox();
            chooser[nr].setBounds(210, pos, 100, 25);
            this.add(chooser[nr]);
        }

        /** (panel construction method)<br>
          * Creates the apply button.
          *
          * @return     Apply button
          */
        private JButton createApplyButton() {
            apply = new JButton();
            apply.setBounds(5, 260, 100, 25);

            add(new SComponent(apply,
                               new String[] {"Anpassen", "Apply"},
                               new String[] {"Speichert die Farbeinstellungen.",
                                             "Saves the color settings."}));

            apply.addActionListener(new ActionListener() {

                /** Zeichnet den Graphen mit den ausgewählten
                  * Frabeinstellungen
                  *
                  * @param e        ActionEvent
                  */
                public void actionPerformed(final ActionEvent e) {
                    Color[] col = new Color[8];
                    for (int i = 0; i < 8; i++) {
                        if (mainclass.getGraphTyp()
                                == AbstractGraph.GRAPHTYP_RESIDUAL) {
                            switch (i) {
                                case COLOR_EDGE_ONE:
                                    col[i] = chooser[COLOR_EDGE_FLOW]
                                                     .getColorSelected();
                                    break;
                                case COLOR_EDGE_TWO:
                                    col[i] = chooser[COLOR_EDGE_CAP]
                                                     .getColorSelected();
                                    break;
                                case COLOR_EDGE_TOP:
                                    col[i] = chooser[COLOR_EDGE_RTOP]
                                                     .getColorSelected();
                                    break;
                                default: col[i] = chooser[i].getColorSelected();
                            }
                        } else {
                            col[i] = chooser[i].getColorSelected();
                        }
                    }
                    drawer.setColorSettings(col);

                    /*GAModel gam = mainclass.getModel();
                    if (gam.getGraphComponentState()
                            == GAModel.COMPONENT_VALID) {
                        gam.setGraphComponentState(GAModel.COMPONENT_OUTDATED);
                    }*/
                    mainclass.repaint();
                }
            });

            return apply;
        }

        /** (panel construction method)<br>
          * Creates the reset button.
          *
          * @return     Reset button
          */
        private JButton createResetButton() {
            reset = new JButton();
            reset.setBounds(110, 260, 100, 25);

            add(new SComponent(reset,
                               new String[] {"Reset", "Reset"},
                               new String[] {"Stellt die Farbeinstellungen "
                                           + "wieder her, die beim Öffnen des "
                                           + "Fensters ausgewählt waren.",
                                             "Reloads the color settings that"
                                           + " was used when opening the"
                                           + " dialog."}));

            reset.addActionListener(new ActionListener() {

                /** Lädt die gesicherten Einstellungen in das Panel
                  * und zeichnet den Graphen wieder mit den ursprünglichen
                  * Einstellungen.
                  *
                  * @param e        ActionEvent
                  */
                public void actionPerformed(final ActionEvent e) {
                    drawer.setColorSettings(colors);
                    setColorSelection();

                    /*GAModel gam = mainclass.getModel();
                    if (gam.getGraphComponentState()
                            == GAModel.COMPONENT_VALID) {
                        gam.setGraphComponentState(GAModel.COMPONENT_OUTDATED);
                    }*/
                    mainclass.repaint();
                }
            });

            return reset;
        }

        /** (panel construction method)<br>
          * Creates the return button.
          *
          * @return     Return button
          */
        private JButton createReturnButton() {
            exit = new JButton();
            exit.setBounds(215, 260, 100, 25);

            add(new SComponent(exit,
                               new String[] {"Schließen", "Close"},
                               new String[] {"Schließt das Fenster.",
                                             "Closes the window."}));

            exit.addActionListener(new ActionListener() {

                /** Schliesst den Dialog
                  *
                  * @param e        ActionEvent
                  */
                public void actionPerformed(final ActionEvent e) {
                    close();
                }
            });

            return exit;
        }

        /** (panel display method)<br>
          * Draws the components of the panel.
          *
          * @param g        Graphical component of this panel
          */
        public void paint(final Graphics g) {
            Dimension size = this.getSize();

            int[] pos = new int[5];
            pos[0] = (size.width - 310) / 2;
            pos[1] = (size.width - 275) / 2;
            pos[2] = pos[1] + 175;

            pos[3] = (size.height - 275) / 2;
            pos[4] = pos[3] + 250;

            apply.setLocation(pos[0], pos[4]);
            reset.setLocation(pos[0] + 105, pos[4]);
            exit.setLocation(pos[0] + 210, pos[4]);

            for (int i = 0; i < 8; i++) {
                chooserLabel[i].setLocation(pos[1], pos[3] + i * 30);
                chooser[i].setLocation(pos[2], pos[3] + i * 30);
            }

            super.paint(g);
        }

        /** empty method - not in use.
          *
          * @deprecated
          * @see SPanel#refreshPanelComponents
          */
        public void refreshPanelComponents() {
        }
    }

    /** Internal array position of graph background color. */
    public static final int COLOR_BACKGROUND = 0;

    /** Internal array position of graph edgeI color. */
    public static final int COLOR_EDGE_ONE = 1;

    /** Internal array position of graph edgeII color. */
    public static final int COLOR_EDGE_TWO = 2;

    /** Internal array position of graph edge weight. */
    public static final int COLOR_EDGE_WEIGHT = 3;

    /** Internal array position of graph arrow color. */
    public static final int COLOR_EDGE_TOP = 4;

    /** Internal array position of graph node border color. */
    public static final int COLOR_NODE_BORDER = 5;

    /** Internal array position of graph node background color. */
    public static final int COLOR_NODE_BACKGROUND = 6;

    /** Internal array position of graph node tag color. */
    public static final int COLOR_NODE_TAG = 7;

    /** Internal array position of gragh edge flow. */
    public static final int COLOR_EDGE_FLOW = 8;

    /** Internal array position of graph edge capacity. */
    public static final int COLOR_EDGE_CAP  = 9;

    /** Internal array position of residual graph arrow color. */
    public static final int COLOR_EDGE_RTOP = 10;

    /** Reference to the mainclass of the projekt. */
    GraphAlgController mainclass;

    /** Reference to the Drawer object. */
    GraphDrawer drawer;

    /** Saved color settings for reset actions. */
    Color[] colors;

    /** (constructor)<br>
      * Construct a colorchooser dialog window.
      *
      * @param mainclass        Reference to projects mainclass
      */
    public ColorChooser(final GraphAlgController mainclass) {
        super(mainclass.getGUI(), true);
        this.mainclass = mainclass;

        drawer = mainclass.getGraphDrawer();
        colors = (Color[]) drawer.getColorSettings().clone();

        if (mainclass.getLanguageSettings() == LANGUAGE_GERMAN) {
            setTitle("Farbeinstellungen");
        } else {
            setTitle("Color Settings");
        }

        this.setSize(330, 320);
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width  - getSize().width)  / 2,
                    (d.height - getSize().height) / 2);

        this.setContentPane(new ColorPanel());
    }

    /** (internal display method)<br>
      * Sets the dialogs visible state to FALSE.
      */
    void close() {
        this.setVisible(false);
        this.dispose();
    }
}
