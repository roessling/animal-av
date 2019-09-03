package gfgaa.gui.components;

import gfgaa.gui.others.LanguageInterface;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** Projects panel class<br>
  * Inherits methods for language selection, updates and subpanel selection.
  *
  * @author S. Kulessa
  * @version 0.97
  */
public abstract class SPanel extends JPanel implements LanguageInterface {

    /**
   * 
   */
  private static final long serialVersionUID = -7451788822691266894L;

    /** Reference to the tabbed pane inside this pane. */
    private JTabbedPane tabs;

    /** Reference to the known subpanels of this pane. */
    private Vector<SPanel> containedPanels = new Vector<SPanel>(1, 1);

    /** Vector containing languageComponents. */
    private Vector<SComponent> langComponents = new Vector<SComponent>(1, 1);

    /** (internal method)<br>
      * Converts a two dimensional string array into
      * a one dimensional string array.
      *
      * @param translations         Two dimensional string array
      * @return                     One dimensional string array
      */
    private String[] convertArray(final String[][] translations) {
        int t = 0;
        String[] trans = new String[translations.length
                * translations[0].length];
        for (int i = 0; i < translations[0].length; i++) {
            for (int j = 0; j < translations.length; j++) {
                trans[t++] = translations[j][i];
            }
        }
        return trans;
    }

    /** (internal data method)<br>
      * Returns a sub array of the given array.
      *
      * @param iArray       Full Array
      * @param start        Start position
      * @param end          End position + 1
      * @return             Sub Array
      */
    private int[] subArray(final int[] iArray, final int start,
                                 final int end) {
        int[] nArray = new int[end - start];
        for (int j = 0, i = start; i < end; j++, i++) {
            nArray[j] = iArray[i];
        }
        return nArray;
    }

/*+TABS+AND+SUBPANELS+++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Adds a subpanel reference to this panel.
      *
      * @param panel        Subpanel reference
      * @param bFlag        Wheter the panel should also be added to JPanel
      *                     directly or not
      */
    protected void add(final SPanel panel, final boolean bFlag) {
        containedPanels.add(panel);
        if (bFlag) {
            this.add(panel);
        }
    }

    /** (internal data method)<br>
      * Adds a TabbedPane reference to this panel.
      *
      * @param tabs                 TabbedPane reference
      * @param translations         Translations
      */
    protected void setTabbedPane(final JTabbedPane tabs,
                                 final String[][] translations) {
        add(new SComponent(tabs, convertArray(translations)));
        this.tabs = tabs;
    }

    /** (internal data method)<br>
      * Returns the panel at tabposition pos.
      *
      * @param pos          Tabposition
      * @return             Panel
      */
    protected SPanel getPanelAtTab(final int pos) {
        return (SPanel) this.containedPanels.get(pos);
    }

    /** (internal info method)<br>
      * Returns the number of tabs.
      *
      * @return         Number of tabs
      */
    protected int getNumberOfTabs() {
        return containedPanels.size();
    }

    /** (internal info method)<br>
      * Returns wheter the panel has tabs or not.
      *
      * @return         Tabbed Flag
      */
    protected boolean hasTabbedPanels() {
        return (tabs != null);
    }

/*+DISPLAY+METHODS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal display method)<br>
      * Changes the LanguageSetting of this panel, his language Components
      * and all subpanel of this panel.
      *
      * @param languageFlag         Language id
      */
    public void changeLanguageSettings(final int languageFlag) {

        // Aktualisierung aller Unter Panel
        for (int i = 0; i < this.containedPanels.size(); i++) {
            ((SPanel) this.containedPanels.get(i))
                    .changeLanguageSettings(languageFlag);
        }

        // Aktualisierung aller Componenten des Panels
        for (int i = 0; i < this.langComponents.size(); i++) {
            ((SComponent) this.langComponents.get(i))
                    .changeLanguageSettings(languageFlag);
        }
    }

    /** (internal display method)<br>
      * Set the selected panel to the top.
      *
      * @param panelId      Subpanel id array
      */
    public void setTopLevelPanel(final int[] panelId) {

        int l = panelId.length;
        if ((panelId == null) || !(l > 0)) {
            return;
        }

        if ((panelId[0] > -1) && (panelId[0] < getNumberOfTabs())) {
            tabs.setSelectedIndex(panelId[0]);

            if (l > 1) {
                SPanel sub = getPanelAtTab(panelId[0]);
                if (sub.hasTabbedPanels()) {
                    sub.setTopLevelPanel(subArray(panelId, 1, panelId.length));
                }
            }
            this.repaint();
        }
    }

    /** (internal display method)<br>
      * Updates the panel and his components.
      */
    public abstract void refreshPanelComponents();

/*+LANGUAGE+COMPONENTS++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Add a SComponent reference to this pane.
      *
      * @param comp         SCompoent reference
      */
    protected void add(final SComponent comp) {
        langComponents.add(comp);
    }

    /** (internal data method)<br>
      * Returns the contained SComponent at position pos.
      *
      * @param pos          Position of the SComponent in the
      *                     langComponent Vector
      * @return             SComponent
      */
    protected SComponent get(final int pos) {
        return (SComponent) langComponents.get(pos);
    }

    /** (internal data method)<br>
      * Returns the contained object at position pos.
      *
      * @param pos          Position of the object in the
      *                     langComponent Vector
      * @return             SComponent as object
      */
    protected Object getAsObject(final int pos) {
        return langComponents.get(pos);
    }

    /** (internal info method)<br>
      * Returns the number of containend SComponents.
      *
      * @return             size of the langComponent Vector
      */
    protected int getLangComponentCount() {
        return langComponents.size();
    }
}
