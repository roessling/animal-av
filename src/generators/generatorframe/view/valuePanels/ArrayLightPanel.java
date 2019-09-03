package generators.generatorframe.view.valuePanels;

import generators.generatorframe.controller.ArrayListener;
import generators.generatorframe.controller.ToolTipActionListener;
import generators.generatorframe.view.image.GetIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;






import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

















import animal.main.Animal;
import translator.Translator;




public class ArrayLightPanel extends JPanel implements Translatable, Notifyable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//indicates ob es ein string array ist
	boolean string;
	
	//Object value;
	JTable table;
	
	JButton add;
	JButton delete;
	JButton edit;
	
	JTextField positionField;
	JTextField valueField;
	
	JLabel nValue;
	
	String name;
	
	Translator translator = new Translator("GeneratorFrame", Animal.getCurrentLocale());
	
	ToolTipActionListener tool;
	
	public ArrayLightPanel(boolean string, Object value, String name){
		
		this.name = name;
		this.string = string;
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		JPanel labelPane = new JPanel();
		labelPane.setBackground(Color.white);
		
		JLabel label = new JLabel(name);
		label.setMinimumSize(new Dimension(100, 30));
		
		labelPane.add(label);
		
		GetIcon get = new GetIcon();
//		infoButton = new JButton(icon);
//		infoButton.setMaximumSize(new Dimension(20, 20));
//		infoButton.setPreferredSize(new Dimension(20, 20));
//		infoButton.setMinimumSize(new Dimension(20, 20));
//		infoButton.setToolTipText(translator.translateMessage("infoArrayLabel"));
//		infoButton.setBackground(Color.white);
//		
//		tool = new ToolTipActionListener(ToolTipActionListener.ARRAY);
//		infoButton.addActionListener(tool);
//		infoButton.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
////				String text = "Mehrere Suchwoerter durch Komma trennen. "
////						+ "Soll das Wort nicht in einem anderen integiert sein,"
////						+ "dann setzen Sie es in ' ' ";
//				new InfoFrame(translator.translateMessage("infoArrayLabel"), translator.translateMessage("infoArray"), null);
//			}
//			
//		});
		
	//	labelPane.add(infoButton);
		
		add(Box.createVerticalStrut(50), BorderLayout.PAGE_START);
		//add(Box.createHorizontalStrut(5));
		
		JPanel textfields = new JPanel();
		textfields.setBackground(Color.white);
		textfields.setLayout(new BorderLayout());
		textfields.add(labelPane, BorderLayout.PAGE_START);
		

		
		ArrayListener listener = new ArrayListener(this.name, this.string);
		
		String[][] a = arrayToString(value);
		
		int size = a[0].length;
		table = new JTable(new DefaultTableModel(a, new String[size]));
	
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setGridColor(Color.black);
		
		TableColumnModel columnModel = table.getColumnModel();
		
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setCellSelectionEnabled(true);
		
		
	//	StringBuffer b = new StringBuffer();
		
		for(int i=0; i<size; i++){
			int preferredWidth = calculate(a[0][i]);
			//System.out.println(preferredWidth);
			TableColumn c = columnModel.getColumn(i);
			c.setPreferredWidth(preferredWidth);
			c.setHeaderValue("P: "+i);
//			b.append(i);
//			for(int j=0; j<preferredWidth; j++){
//			  b.append(" ");
//			}
//			b.append("|");
		}	
		
		table.getTableHeader().resizeAndRepaint();
		//System.out.println(columnModel.getColumnMargin());
		//System.out.println(table.getColumnCount());
//		FieldListSelectionListener listen = new FieldListSelectionListener(table, name, this.string);
//		
//		table.getSelectionModel().addListSelectionListener(listen);
		table.getModel().addTableModelListener(listener);
		
		JPanel labelP = new JPanel();
		labelP.setLayout(new BorderLayout());
//		JLabel positions = new JLabel(b.toString());
//		labelP.add(positions);
		labelP.add(table.getTableHeader(), BorderLayout.NORTH);
		labelP.add(table, BorderLayout.CENTER);
		textfields.add(labelP, BorderLayout.CENTER);
		textfields.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		
		JPanel inputPane = new JPanel();
		inputPane.setBackground(Color.WHITE);
		
		JPanel firstRow = new JPanel();
		firstRow.setLayout(new BorderLayout());
		
		JLabel posi = new JLabel("Position");
		firstRow.add(posi, BorderLayout.NORTH);
		
		positionField = new JTextField();
		positionField.setName("position");
		positionField.setMinimumSize(new Dimension(50, 30));
		positionField.addKeyListener(listener);
		firstRow.add(positionField, BorderLayout.CENTER);
		firstRow.setBackground(Color.white);
		
		inputPane.add(firstRow);
		inputPane.add(Box.createHorizontalStrut(15));
		
		JPanel secondRow = new JPanel();
    secondRow.setLayout(new BorderLayout());
    secondRow.setBackground(Color.white);
    
    nValue = new JLabel(translator.translateMessage("value"));
    secondRow.add(nValue, BorderLayout.NORTH);
    
    valueField = new JTextField();
    valueField.setName("value");
    valueField.addKeyListener(listener);
    valueField.setMinimumSize(new Dimension(100, 30));
    secondRow.add(valueField, BorderLayout.CENTER);
    
    inputPane.add(secondRow);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		
		buttonPane.add(Box.createVerticalStrut(10));
		buttonPane.setBackground(Color.white);
		
		edit = new JButton(translator.translateMessage("editF"));
		edit.setName("edit");
		edit.setIcon(get.createEditIcon());
		edit.addActionListener(listener);
		edit.setToolTipText(translator.translateMessage("editFToolTipText"));
		buttonPane.add(edit);
		
		add = new JButton(translator.translateMessage("addF"));
		add.setName("add");
		add.setIcon(get.createAddIcon());
		add.setToolTipText(translator.translateMessage("addFToolTip"));
		add.addActionListener(listener);
		buttonPane.add(add);
		
		delete = new JButton(translator.translateMessage("deleteF"));
		delete.setToolTipText(translator.translateMessage("deleteFToolTip"));
		delete.setName("delete");
		delete.setIcon(get.createDeleteIcon());
		delete.addActionListener(listener);
		buttonPane.add(delete);

		JPanel editor = new JPanel();
		editor.setLayout(new BoxLayout(editor, BoxLayout.X_AXIS));
		editor.add(inputPane);
		editor.add(buttonPane);
		
		JPanel spacer = new JPanel();
		spacer.setLayout(new BoxLayout(spacer, BoxLayout.Y_AXIS));
		spacer.add(Box.createVerticalStrut(15));
		spacer.setBackground(Color.white);
		spacer.add(editor);
		
		textfields.add(spacer, BorderLayout.SOUTH);
		
		add(textfields, BorderLayout.CENTER);
		
	}

	private int calculate(String string2) {
		// TODO Auto-generated method stub
		int length = string2.length();
		return length*12+2*3;
	}
	
	private String[][] arrayToString(Object v){
		String[][] a;
		int size; 
		
		if(string){
			
			size = ((String[]) v).length;
			a = new String[1][size];
			for(int i=0; i<size; i++)
				a[0][i] = ((String[]) v)[i];
			
			
		}else{
			
			size = ((int[]) v).length;
			a = new String[1][size];
			for(int i=0; i<size; i++){
				a[0][i] = Integer.toString(((int[]) v)[i]);
			}

		}
		
		return a;
		
	}
	
	public void resetField(Object value){
		String[][] array = arrayToString(value);
		
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.removeRow(0);
		
		model.setColumnCount(array[0].length);
		
		for(int i=0; i<array.length; i++){
			model.addRow(array[i]);
		}
		
		TableColumnModel columnModel = table.getColumnModel();
		
		for(int i=0; i<array[0].length; i++){
			int preferredWidth = calculate(array[0][i]);
			TableColumn c = columnModel.getColumn(i);
      c.setPreferredWidth(preferredWidth);
      c.setHeaderValue("P: "+i);
		}	
		
		table.getTableHeader().resizeAndRepaint();
	}
	
	

  @Override
  public void changeLocale() {
    // TODO Auto-generated method stub
    translator.setTranslatorLocale(Animal.getCurrentLocale());
    add.setText(translator.translateMessage("addF"));
    delete.setText(translator.translateMessage("deleteF"));
    add.setToolTipText(translator.translateMessage("addFToolTip"));
    delete.setToolTipText(translator.translateMessage("deleteFToolTip"));
    edit.setText(translator.translateMessage("editF"));
    edit.setToolTipText(translator.translateMessage("editFToolTipText"));
    nValue.setText(translator.translateMessage("value"));

    //tool.changeLocale();
    //infoButton.setToolTipText(translator.translateMessage("infoArrayLabel"));
  }

  @Override
  public String getElementName() {
    // TODO Auto-generated method stub
    return name;
  }
	

		
}
