package animal.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import animal.main.Animal;
import extras.animalsense.ui.StartUI;

public class ExerciseMenuBuilder {

	JMenu baseMenu;

  private LoadFromCollection loadFromColl;

	/**
	 * @param menuBar
	 */
	public ExerciseMenuBuilder(JMenu menuBar) {
		super();
		this.baseMenu = menuBar;
	}

	public void buildStudentMenu(final Component component) {

    JMenuItem submitAnExercise = new JMenuItem("Open own exercise");
		submitAnExercise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
        StartUI.doExercise(component);
			}

		});
		baseMenu.add(submitAnExercise);

    JMenuItem existingExercise = new JMenuItem("Open existing exercise");
		existingExercise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
        loadFromColl = new LoadFromCollection(Animal.get(),
            AnimalCollectionTypes.EXERCISES);
			}

		});
		baseMenu.add(existingExercise);
		
		if(Animal.get().isTeacherMode()) {
	    buildTeacherMenu(component);
		}
	}

	public void buildTeacherMenu(final Component component) {

    JMenuItem newExercise = new JMenuItem("Create new exercise");
		newExercise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StartUI.create();
			}

		});

    JMenuItem editExercise = new JMenuItem("Edit an exercise");
		editExercise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StartUI.edit(component);
			}

		});

		baseMenu.addSeparator();
		baseMenu.add(newExercise);
		baseMenu.add(editExercise);

	}

  /**
   * 
   * @param zoomIn
   *          if true zooms window in else zooms out
   */
  public void zoom(boolean zoomIn) {
    if (loadFromColl != null) {
      loadFromColl.zoom(zoomIn);
    }
  }
}
