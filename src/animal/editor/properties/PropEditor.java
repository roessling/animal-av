package animal.editor.properties;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import translator.AnimalTranslator;
import translator.Translator;
import animal.main.PropertiedObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class PropEditor implements ActionListener {
	public static final String EDITORS_FILENAME = "properties.cfg";

	public static final String EDITOR_PATH = "animal.editor.";

	XProperties editors = null;

	JFrame frame;

	PropertiedObject currentComponent;

	PropertyEditor[] realEditors = null;

	public void buildFrameFor(String type, PropertiedObject component) {
		// try to extract from properties...
		if (!editors.containsKey(type)) {
			MessageDisplay.errorMsg("noSuchKeyException", new Object[] { type });
			return;
		}
		currentComponent = component;
		XProperties subEditors = (XProperties) editors.get(type);
		String orderOfComponents = subEditors.getProperty("order", "");
		StringTokenizer stok = new StringTokenizer(orderOfComponents);
		realEditors = new PropertyEditor[stok.countTokens()];
		int i = 0;
		frame = new JFrame();
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new GridLayout(0, 2));

		while (stok.hasMoreTokens()) {
			String currentKey = stok.nextToken().trim();
			XProperties localProps = (XProperties) subEditors.get(currentKey);
			PropertyEditor localEditor = (PropertyEditor) localProps.get("editor");
			localEditor.setTargetObject(component);
			localEditor.addEditorTo(contentPane);
			realEditors[i++] = localEditor;
		}
		AnimalTranslator.setTranslatorLocale(Translator.DEFAULT_LOCALE);
		contentPane.add(AnimalTranslator.getGUIBuilder().generateJButton("ok",
				null, false, this));
		contentPane.add(AnimalTranslator.getGUIBuilder().generateJButton("cancel",
				null, false, this));
		frame.pack();
		frame.setVisible(true);
	}

	public void actionPerformed( ActionEvent e) {
		frame.setVisible(false);
		frame.dispose();
		for (int i = 0; i < realEditors.length; i++)
			if (realEditors[i] != null)
				realEditors[i].storeProperty();
		System.exit(0);
	}

	public JComponent getEditComponent() {
		return new JLabel(getClass().getName());
	}

	public void addEditorTo(Container container) {
		container.add(new JLabel(getClass().getName()));
		container.add(AnimalTranslator.getGUIBuilder().generateJLabel(
				"noValueAssigned"));
	}

	@SuppressWarnings({ "unchecked", "resource" })
	public XProperties getEditors() {
		// only read the Editors once. If they are initialized, return the
		// previously read ones.
		XProperties newEditors = new XProperties(), localHash = new XProperties(), propertyHash = null;
		String s = null, key = "UNKNOWN";
		@SuppressWarnings("rawtypes")
    Class c;
		String className = "";

		// a sequential number for the editors. Required for preserving
		// the order the Editors had in the file.
		// int num = 0;
		Object ed;
		@SuppressWarnings("rawtypes")
    Class[] constructorParams = new Class[] { key.getClass(),
				newEditors.getClass() };
		try {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(EDITORS_FILENAME);
			} catch (FileNotFoundException notFoundException) {
				inputStream = ClassLoader.getSystemResourceAsStream(EDITORS_FILENAME);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			while ((s = br.readLine()) != null) {
				if (s != null && s.length() != 0) {
					// second try block to continue reading Editor classes
					// even if one is not yet implemented or erraneous!
					if (s.indexOf(':') == s.trim().length() - 1) {
						if (localHash.size() != 0)
							newEditors.put(key, localHash);
						key = s.trim().substring(0, s.length() - 1);
						localHash = new XProperties();
					} else {
						int startPos = 0, nextSpacePos = s.indexOf(' ', startPos);
						String component = s.substring(startPos, nextSpacePos), currentEntry = null, property = null;
						startPos = nextSpacePos + 1;
						className = EDITOR_PATH
								+ Character.toUpperCase(component.charAt(0))
								+ component.substring(1) + "PropertyEditor";
						propertyHash = new XProperties();
						propertyHash.put("type", component.toLowerCase());

						int pos = 0;
						do {
							if (s.charAt(startPos) == '"') {
								nextSpacePos = s.indexOf('"', startPos + 1);
								if (nextSpacePos != -1)
									currentEntry = s.substring(startPos + 1, nextSpacePos);
								else
									currentEntry = s.substring(startPos + 1, s.length() - 1);
								nextSpacePos++;
							} else {
								nextSpacePos = s.indexOf(' ', startPos);
								if (nextSpacePos != -1)
									currentEntry = s.substring(startPos, nextSpacePos);
								else
									currentEntry = s.substring(startPos);
							}

							startPos = nextSpacePos + 1;
							if (pos == 0) {
								property = currentEntry;
								propertyHash.put("property", currentEntry);
								localHash.put("order", localHash.getProperty("order", "") + " "
										+ currentEntry);

							} else
								propertyHash.put("param" + (pos - 1), currentEntry);
							pos++;
						} while (nextSpacePos != -1 && startPos < s.length());
						localHash.put(property, propertyHash);
						try {
							c = Class.forName(className);
							@SuppressWarnings("rawtypes")
              Constructor constructor = c.getDeclaredConstructor(constructorParams);
							ed = constructor.newInstance(new Object[] { key, propertyHash });
							propertyHash.put("editor", ed);
						} catch (ClassNotFoundException e) {
							// this should be just a configuration problem,
							// as no non-existing class should be listed
							// in the file. So either the file is corrupt
							// or the class really doesn't exist.
							MessageDisplay.errorMsg(
									AnimalTranslator.translateMessage("classNotFound",
											new String[] {className, e.getMessage()}),
											MessageDisplay.RUN_ERROR);
						} catch (InstantiationException e) {
							MessageDisplay.errorMsg(
									AnimalTranslator.translateMessage("errorInstantiating",
											new String[] {className, e.getMessage()}),
											MessageDisplay.RUN_ERROR);
						} catch (InvocationTargetException e) {
							MessageDisplay.errorMsg(
									AnimalTranslator.translateMessage("invocTExc",
											new String[] {className, e.getMessage()}),
											MessageDisplay.RUN_ERROR);
						} catch (NoSuchMethodException e) {
							MessageDisplay.errorMsg(
									AnimalTranslator.translateMessage("nsmEx",
											new String[] {className, e.getMessage()}),
											MessageDisplay.RUN_ERROR);
						} catch (IllegalAccessException e) {
							// the constructor of a class to be instantiated
							// via <code>newInstance()</code> has to be
							// public!
							MessageDisplay.errorMsg(
									AnimalTranslator.translateMessage("illegalAccessExc",
											new String[] {className, e.getMessage()}),
									MessageDisplay.RUN_ERROR);
						} catch (IllegalArgumentException e) {
							MessageDisplay.errorMsg(
									AnimalTranslator.translateMessage("illegalArgumentExc",
											new String[] {className, e.getMessage()}),
									MessageDisplay.RUN_ERROR);
						}
					} // if (isKey)
				} // if s != null
			} // while loop
			if (!newEditors.containsKey(key))
				newEditors.put(key, localHash);
			br.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			MessageDisplay.errorMsg(
					AnimalTranslator.translateMessage("fileNotFoundExc",
							new String[] {className, e.getMessage()}),
					MessageDisplay.RUN_ERROR);
		} catch (IOException e) {
			MessageDisplay.errorMsg(
					AnimalTranslator.translateMessage("ioException",
							new String[] {className, e.getMessage()}),
					MessageDisplay.RUN_ERROR);
		}
		// don't catch Throwable as then it would be difficult to find
		// errors in the constructors' implementations.
		// Possible problems are:
		// a) the class doesn't have a noargs constructor
		// b) the implementation of the constructor is buggy
		// (null-pointer-exception)
		// return the previously or newly read editors
		editors = newEditors;
		return newEditors;
	}
}
