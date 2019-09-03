package interactionsupport.views;

import interactionsupport.controllers.InteractionController;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.InteractionModel;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


/**
 * View class showing a documentation in HTML format.
 * 
 * @author Gina Haeussge <huge(at)rbg.informatik.tu-darmstadt.de>, Simon
 *         Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class HtmlDocumentationView extends InteractionView {

  /** The generated serial version UID */
  private static final long serialVersionUID = 7662944806050674644L;

  /** The HTML renderer component */
  private JEditorPane       htmlView;

  public HtmlDocumentationView(String id, InteractionController interactionModule) {
    super(id);
    setInteractionModule(interactionModule);
  }

  /**
   * Gets the model object of this documentation.
   * 
   * @see HtmlDocumentationModel
   */
  @Override
  public HtmlDocumentationModel getModel() {
    InteractionModel interaction = getInteractionModule()
        .getInteractionModels().get(id);
    if (!(interaction instanceof HtmlDocumentationModel)) {
      System.err.println("Wrong ID to interaction mapping");
      return null;
    }
    return (HtmlDocumentationModel) interaction;
  }

  @Override
  public void makeGUI() {
    ImageIcon docIcon;
    JLabel headlineLabel;
    JScrollPane documentationScroller;

    String docURL = this.getModel().getTargetURI().toString();
    try {
      htmlView = new JEditorPane(docURL);
    } catch (Exception exception) {
      htmlView = new JEditorPane();
      htmlView.setText(InteractionController.translateMessage("docuNotFound",
          new String[] { docURL }));
    }

    htmlView.setEditable(false);

    // sets the layout manager of the object
    setLayout(new BorderLayout());

    // constructs the headline of the documentation window
    docIcon = new ImageIcon("DocGraphic.gif");
    headlineLabel = new JLabel(docIcon, SwingConstants.CENTER);

    documentationScroller = new JScrollPane(htmlView,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    add(BorderLayout.NORTH, headlineLabel);
    add(BorderLayout.CENTER, documentationScroller);
  }

}
