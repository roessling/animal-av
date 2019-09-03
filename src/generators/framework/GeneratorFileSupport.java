package generators.framework;


public class GeneratorFileSupport {
  
      /*
  public static boolean generateGeneratorContent(boolean directlySendToAnimal) {
    // write output to the selected file
    String animalGenerator = trans.translateMessage("animalGenerator");
    File selFile = new File(txtFileName.getText());
//    boolean directlySendToAnimal = txtFileName.getText().length() == 0
//        && myAnimalInstance != null;
    activeCard = MAIN_PANEL;
    if (!directlySendToAnimal && selFile.exists()) {
      if (!selFile.canWrite()) {
        JOptionPane.showMessageDialog(myFrame, trans.translateMessage(
            "invalidFilename", new String[] { selFile.getPath() }),
            animalGenerator, JOptionPane.ERROR_MESSAGE);
        return false;
      }

      int ret = JOptionPane.showConfirmDialog(myFrame, trans.translateMessage(
          "overwriteFile", new String[] { selFile.getPath() }),
          animalGenerator, JOptionPane.YES_NO_OPTION);
      if (ret == JOptionPane.NO_OPTION)
        return false;
    }

    if (!directlySendToAnimal) {
      FileWriter out;
      BufferedWriter buf = null;
      try {
        out = new FileWriter(selFile);
        buf = new BufferedWriter(out);
        // PropertiesTreeModel aModel = (PropertiesTreeModel) editor
        // .getPropertiesPanel().getTree().getModel();
        // buf.write(currentGenerator.generate(aModel.getPropertiesContainer(),
        // aModel.getPrimitivesContainer()));
        PropertiesTreeModel aModel = (PropertiesTreeModel)editor.getPropertiesPanel().getTree().getModel();
        String contents = currentGenerator.generate(aModel.getPropertiesContainer(), aModel
            .getPrimitivesContainer());

        buf.write(contents);
        buf.flush();
        buf.close();
      } catch (IOException e1) {
        JOptionPane.showMessageDialog(myFrame, trans.translateMessage(
            "errorWriting", new String[] { selFile.getPath() }),
            animalGenerator, JOptionPane.ERROR_MESSAGE);
        return false;
      } finally {
        try {
          if (buf != null)
            buf.close();
        } catch (Exception e2) {
          // do nothing
        }
      }
    } else {
      String format = "animation/animalscript";
      AnimationImporter animationImporter = null;
      animationImporter = AnimationImporter.getImporterFor(format);
      Animation animation = null;
      String animContent = createContent();
      if (animationImporter != null) {
        animation = animationImporter.importAnimationFrom(null, animContent);
      }
      if (animation != null) {
        myAnimalInstance.setAnimation(animation);
        myAnimalInstance.setAnimalScriptCode(animContent);
      }
    }

    int choice = JOptionPane.showConfirmDialog(myFrame, trans
        .translateMessage("exitNow"), animalGenerator,
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      myFrame.dispose();
      ((CardLayout) mainContentPanel.getLayout()).show(mainContentPanel,
          MAIN_PANEL);
      myTree.setSelectionRow(0);
    }
    // activeCard = MAIN_PANEL;
    ((CardLayout) mainContentPanel.getLayout()).show(mainContentPanel,
        MAIN_PANEL);
    confirmButton.setEnabled(true);
    return true;
  }
*/
}
