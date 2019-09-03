package gfgaa.gui.components;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

/** Projects component class<br>
  * Inherits methods for language selection.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public class SComponent extends JComponent {

    /**
   * 
   */
  private static final long serialVersionUID = 3936386413363309213L;

    /** Reference to a component. */
    private JComponent comp;

    /** Array position of this component. */
    private int compId;

    /** Identifier who shows that the component is a JButton object. */
    private static final int COMPONENT_IS_JBUTTON = 1;

    /** Identifier who shows that the component is a JLabel object. */
    private static final int COMPONENT_IS_JLABEL = 2;

    /** Identifier who shows that the component is a JRadiButton object. */
    private static final int COMPONENT_IS_JRADIOBUTTON = 3;

    /** Identifier who shows that the component is a JTabbedPane object. */
    private static final int COMPONENT_IS_JTABBEDPANE = 4;

    /** Identifier who shows that the component is a tooltip component. */
    private static final int TOOLTIP_COMPONENT = 5;

    /** Identifier who shows that the component is a JCheckBox object. */
    private static final int COMPONENT_IS_JCHECKBOX = 6;

    /** Translations for the component text. */
    private String[] translations = null;

    /** Translations for the components tooltips. */
    private String[] tooltipTranslations = null;

    /** Language specific position of the component. */
    private int[][] positions = null;

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JComponent
      * @param tooltipTranslations      Tooltip translations
      */
    public SComponent(final JComponent comp,
                      final String[] tooltipTranslations) {

        this.compId = TOOLTIP_COMPONENT;
        this.comp = comp;
        setTooltipTranslations(tooltipTranslations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JTabbedPane
      * @param translations             Tab translations
      */
    public SComponent(final JTabbedPane comp, final String[] translations) {
        this.compId = COMPONENT_IS_JTABBEDPANE;
        this.comp = comp;
        setTranslations(translations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JButton
      * @param translations             Text translations
      */
    public SComponent(final JButton comp, final String[] translations) {
        this.compId = COMPONENT_IS_JBUTTON;
        this.comp = comp;
        setTranslations(translations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JButton
      * @param translations             Text translations
      * @param tooltipTranslations      Tooltip translations
      */
    public SComponent(final JButton comp, final String[] translations,
                      final String[] tooltipTranslations) {
        this.compId = COMPONENT_IS_JBUTTON;
        this.comp = comp;
        setTranslations(translations);
        setTooltipTranslations(tooltipTranslations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JRadionButton
      * @param translations             Text translations
      */
    public SComponent(final JRadioButton comp, final String[] translations) {
        this.compId = COMPONENT_IS_JRADIOBUTTON;
        this.comp = comp;
        setTranslations(translations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JCheckBox
      * @param translations             Text translations
      */
    public SComponent(final JCheckBox comp, final String[] translations) {
        this.compId = COMPONENT_IS_JCHECKBOX;
        this.comp = comp;
        setTranslations(translations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JCheckBox
      * @param translations             Text translations
      * @param tooltipTranslation       Tooltip translations
      */
    public SComponent(final JCheckBox comp, final String[] translations,
                      final String[] tooltipTranslation) {
        this.compId = COMPONENT_IS_JCHECKBOX;
        this.comp = comp;
        setTranslations(translations);
        setTooltipTranslations(tooltipTranslations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JLabel
      * @param translations             Text translations
      */
    public SComponent(final JLabel comp, final String[] translations) {
        this.compId = COMPONENT_IS_JLABEL;
        this.comp = comp;
        setTranslations(translations);
    }

    /** (constructor)<br>
      * Creates a new SComponent object.
      *
      * @param comp                     Reference to a JLabel
      * @param translations             Text translations
      * @param positions                Language specific positions
      */
    public SComponent(final JLabel comp, final String[] translations,
                      final int[][] positions) {
        this.compId = COMPONENT_IS_JLABEL;
        this.comp = comp;
        setTranslations(translations);
        setPositions(positions);
    }

    /** (internal data method)<br>
      * Sets the text translations for this component.
      *
      * @param translations             Text translations
      */
    protected void setTranslations(final String[] translations) {
        this.translations = translations;
    }

    /** (internal data method)<br>
      * Sets the tooltip translations for this components.
      *
      * @param tooltipTranslations      Tooltip translations
      */
    protected void setTooltipTranslations(final String[] tooltipTranslations) {
        this.tooltipTranslations = tooltipTranslations;
    }

    /** (internal data method)<br>
      * Sets the position for this component.
      *
      * @param positions                Language specific positions
      */
    protected void setPositions(final int[][] positions) {
        this.positions = positions;
    }

    /** (internal language method)<br>
      * Changes the current used language settings.
      *
      * @param languageFlag             New language settings
      */
    protected void changeLanguageSettings(final int languageFlag) {
        switch (compId) {
            case COMPONENT_IS_JLABEL:
                ((JLabel) comp).setText(translations[languageFlag]);

                if (positions != null) {
                    ((JLabel) comp).setBounds(positions[languageFlag][0],
                                              positions[languageFlag][1],
                                              positions[languageFlag][2],
                                              positions[languageFlag][3]);
                }

                if (tooltipTranslations != null) {
                    comp.setToolTipText(tooltipTranslations[languageFlag]);
                }
                break;
            case COMPONENT_IS_JBUTTON:
                ((JButton) comp).setText(translations[languageFlag]);

                if (positions != null) {
                    ((JLabel) comp).setBounds(positions[languageFlag][0],
                                              positions[languageFlag][1],
                                              positions[languageFlag][2],
                                              positions[languageFlag][3]);
                }

                if (tooltipTranslations != null) {
                    comp.setToolTipText(tooltipTranslations[languageFlag]);
                }
                break;
            case COMPONENT_IS_JRADIOBUTTON:
                ((JRadioButton) comp).setText(translations[languageFlag]);

                if (positions != null) {
                    ((JLabel) comp).setBounds(positions[languageFlag][0],
                                              positions[languageFlag][1],
                                              positions[languageFlag][2],
                                              positions[languageFlag][3]);
                }

                if (tooltipTranslations != null) {
                    comp.setToolTipText(tooltipTranslations[languageFlag]);
                }
                break;
            case COMPONENT_IS_JTABBEDPANE:
                JTabbedPane jTab = ((JTabbedPane) comp);

                int anz = jTab.getTabCount();
                for (int i = 0, j = languageFlag; i < anz; i++, j += 2) {
                                    // 2 ist die Anzahl der bekannten Sprachen
                    jTab.setTitleAt(i, translations[j]);
                }

                if (tooltipTranslations != null) {
                    comp.setToolTipText(tooltipTranslations[languageFlag]);
                }
                break;
            case COMPONENT_IS_JCHECKBOX:
                ((JCheckBox) comp).setText(translations[languageFlag]);

                if (tooltipTranslations != null) {
                    comp.setToolTipText(tooltipTranslations[languageFlag]);
                }
                break;
            case TOOLTIP_COMPONENT:
                comp.setToolTipText(tooltipTranslations[languageFlag]);
                break;
        }
    }
}
