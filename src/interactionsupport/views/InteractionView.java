package interactionsupport.views;

import interactionsupport.controllers.InteractionController;
import interactionsupport.models.InteractionModel;

import javax.swing.JPanel;

import animal.main.Animal;

public abstract class InteractionView extends JPanel {

  private static final long   serialVersionUID = -7195752829890748432L;

  /** unique identifier of this interaction */
  protected String            id;

  /** the interaction module for this question */
  protected InteractionController interactionModule;

  /** the main panel of the interactions interface */
  protected JPanel            mainPanel;

  InteractionView(String id) {
    this.id = id;
  }

  /**
   * Creates the GUI for this interaction.
   */
  public abstract void makeGUI();

  /**
   * Retrieves the model object of this interaction view object.
   * 
   * @return the model object this view is associated to
   */
  public abstract InteractionModel getModel();

  /**
   * Retrieves the unique identifier of the interaction model this view
   * visualizes.
   * 
   * @return the unique identifier of the corresponding interaction model
   */
  public String getID() {
    return id;
  }

  public String getTitle() {
    return InteractionController.translateMessage("interaction");
  }

  /**
   * Retrieves the {@link InteractionController} for this interaction.
   * 
   * @return the {@link InteractionController} for this question
   */
  public InteractionController getInteractionModule() {
    if (interactionModule == null) {
      interactionModule = Animal.getAnimalConfiguration().getInteractionController();
    }
    return interactionModule;
  }

  protected void setInteractionModule(InteractionController module) {
    interactionModule = module;
  }

}
