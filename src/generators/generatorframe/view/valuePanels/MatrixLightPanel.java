package generators.generatorframe.view.valuePanels;


//import generators.generatorframe.controller.FieldActionListener;
//import generators.generatorframe.controller.FieldTableModelListener;
import generators.generatorframe.controller.MatrixListener;
//import generators.generatorframe.controller.ToolTipActionListener;
import generators.generatorframe.view.image.GetIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import animal.main.Animal;
import translator.Translator;

/**
 * 
 * @author Nora Wester
 *
 */


public class MatrixLightPanel extends JPanel implements Translatable, Notifyable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//indicates ob es ein string array ist
		boolean string;
		JTable table;
		
		JButton infoButton;
		JButton add;
		JButton delete;
		
		Translator translator = new Translator("GeneratorFrame", Locale.US);
		
		String name;
    private JTextField xPositionField;
    private JLabel nValue;
    private JTextField valueField;
    private JButton edit;
    private Component yPositionField;
    private JButton addR;
    private JButton deleteR;
    private JLabel posi;
    private JLabel posiY;
    private JTable rowTable;

		public MatrixLightPanel(boolean string, Object value, String name){
			
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
//			infoButton = new JButton(icon);
//			infoButton.setMaximumSize(new Dimension(20, 20));
//			infoButton.setPreferredSize(new Dimension(20, 20));
//			infoButton.setMinimumSize(new Dimension(20, 20));
//			infoButton.setToolTipText(translator.translateMessage("infoArrayLabel"));
//			infoButton.setBackground(Color.white);
//			
//			tool = new ToolTipActionListener(ToolTipActionListener.ARRAY);
//			infoButton.addActionListener(tool);
//			
//			
//			labelPane.add(infoButton);
			
			add(Box.createVerticalStrut(50), BorderLayout.PAGE_START);
			//add(Box.createHorizontalStrut(5));
			
			JPanel textfields = new JPanel();
			textfields.setBackground(Color.white);
			textfields.setLayout(new BorderLayout());
			textfields.add(labelPane, BorderLayout.PAGE_START);
			

			
			String[][] a = arrayToString(value);
			int sizeColumn = a[0].length;
					
			table = new JTable(new DefaultTableModel(a, new String[sizeColumn]));
		
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setGridColor(Color.black);
			
			TableColumnModel columnModel = table.getColumnModel();
			
			table.setShowHorizontalLines(true);
			table.setShowVerticalLines(true);
			table.setCellSelectionEnabled(true);
			
			for(int i=0; i<sizeColumn; i++){
				int preferredWidth = calculate(a[0][i]);
				//System.out.println(preferredWidth);
				TableColumn c = columnModel.getColumn(i);
	      c.setPreferredWidth(preferredWidth);
	      c.setHeaderValue(translator.translateMessage("Col")+": "+i);
	     
			}	
			
			table.getTableHeader().resizeAndRepaint();
			//System.out.println(columnModel.getColumnMargin());
			//System.out.println(table.getColumnCount());
			MatrixListener listener = new MatrixListener(name, this.string);
			//table.getSelectionModel().addListSelectionListener(list);
		//	table.getModel().addTableModelListener(new FieldTableModelListener(name, this.string));
			table.getModel().addTableModelListener(listener);
			//table.addKeyListener(new FieldKeyListener(list));
			
			JPanel rowPane = new JPanel();
			rowPane.setLayout(new BorderLayout());
			rowTable = new JTable(new DefaultTableModel(getRowList(table.getRowCount()), new String[]{"P"}));
			rowPane.add(rowTable, BorderLayout.CENTER);
			rowPane.add(rowTable.getTableHeader(), BorderLayout.NORTH);
			//rowTable.setShowHorizontalLines(true);
			rowTable.setShowGrid(true);
      rowTable.setCellSelectionEnabled(false);
      rowTable.setGridColor(Color.black);
     
      //Zellen einfärben
      TableColumn rowC = rowTable.getColumnModel().getColumn(0);
      rowC.setCellRenderer(new DefaultTableCellRenderer(){
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column){
          
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            c.setBackground(new Color(218, 221, 228));
          
          return c;
        }
      });
     
			
			JPanel tablePane = new JPanel();
			tablePane.setLayout(new BorderLayout());
			tablePane.add(table, BorderLayout.CENTER);
			tablePane.add(table.getTableHeader(), BorderLayout.NORTH);
			textfields.add(rowPane, BorderLayout.WEST);
			textfields.add(tablePane, BorderLayout.CENTER);
			textfields.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
			
//			JPanel buttonPane = new JPanel();
//			buttonPane.setBackground(Color.WHITE);
//			
//		//	buttonPane.add(Box.createVerticalStrut(20));
//			
//			add = new JButton(translator.translateMessage("addC"));
//			add.setToolTipText(translator.translateMessage("addCToolTip"));
//			add.setName("add");
//			add.addActionListener(new FieldActionListener(name));
//			buttonPane.add(add);
//			
//			delete = new JButton(translator.translateMessage("deleteC"));
//			delete.setToolTipText(translator.translateMessage("deleteCToolTip"));
//			delete.setName("delete");
//			delete.addActionListener(new FieldActionListener(name));
//			buttonPane.add(delete);
//
//			addRow = new JButton(translator.translateMessage("addR"));
//			addRow.setToolTipText(translator.translateMessage("addRToolTip"));
//			addRow.setName("addRow");
//			addRow.addActionListener(new FieldActionListener(name));
//			buttonPane.add(addRow);
//			
//			deleteRow = new JButton(translator.translateMessage("deleteR"));
//			deleteRow.setToolTipText(translator.translateMessage("deleteRToolTip"));
//			deleteRow.setName("deleteRow");
//			deleteRow.addActionListener(new FieldActionListener(name));
//			buttonPane.add(deleteRow);
//			
//			textfields.add(buttonPane, BorderLayout.SOUTH);
//			
//			add(textfields, BorderLayout.CENTER);
			JPanel inputPane = new JPanel();
	    inputPane.setBackground(Color.WHITE);
	    
	    JPanel firstRow = new JPanel();
	    firstRow.setLayout(new BorderLayout());
	    
	    posi = new JLabel(translator.translateMessage("rPosition"));
	    firstRow.add(posi, BorderLayout.NORTH);
	    
	    xPositionField = new JTextField();
	    xPositionField.setName("xposition");
	    xPositionField.setMinimumSize(new Dimension(60, 30));
	    xPositionField.addKeyListener(listener);
	    firstRow.add(xPositionField, BorderLayout.CENTER);
	    firstRow.setBackground(Color.white);
	    
	    inputPane.add(firstRow);
	    inputPane.add(Box.createHorizontalStrut(15));
	    
	    //
	    JPanel thirdRow = new JPanel();
      thirdRow.setLayout(new BorderLayout());
      
      posiY = new JLabel(translator.translateMessage("cPosition"));
      thirdRow.add(posiY, BorderLayout.NORTH);
      
      yPositionField = new JTextField();
      yPositionField.setName("yposition");
      yPositionField.setMinimumSize(new Dimension(60, 30));
      yPositionField.addKeyListener(listener);
      thirdRow.add(yPositionField, BorderLayout.CENTER);
      thirdRow.setBackground(Color.white);
      
      inputPane.add(thirdRow);
      inputPane.add(Box.createHorizontalStrut(15));
	    //
	    
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
	    buttonPane.setLayout(new BorderLayout());
	    
	    buttonPane.add(Box.createVerticalStrut(10));
	    buttonPane.setBackground(Color.white);
	    
	    JPanel editP = new JPanel();
	    editP.setLayout(new BoxLayout(editP, BoxLayout.X_AXIS));
	    editP.setBackground(Color.WHITE);
	    
	    edit = new JButton(translator.translateMessage("editF"));
	    edit.setName("edit");
	    edit.setIcon(get.createEditIcon());
	    edit.addActionListener(listener);
	    edit.setToolTipText(translator.translateMessage("editFToolTipText"));
	    editP.add(edit);
	    buttonPane.add(editP, BorderLayout.NORTH);
	    
	    JPanel buttonOne = new JPanel();
	    buttonOne.setLayout(new BoxLayout(buttonOne, BoxLayout.X_AXIS));
	    buttonOne.setBackground(Color.WHITE);
	    JPanel buttonTwo = new JPanel();
	    buttonTwo.setLayout(new BoxLayout(buttonTwo, BoxLayout.X_AXIS));
	    buttonTwo.setBackground(Color.white);
	    add = new JButton(translator.translateMessage("addC"));
	    add.setName("addColumn");
	    add.setIcon(get.createAddColumnIcon());
	    add.setToolTipText(translator.translateMessage("addCToolTip"));
	    add.addActionListener(listener);
	    buttonOne.add(add);
	    
	    delete = new JButton(translator.translateMessage("deleteC"));
	    delete.setToolTipText(translator.translateMessage("deleteCToolTip"));
	    delete.setName("deleteColumn");
	    delete.setIcon(get.createDeleteColumnIcon());
	    delete.addActionListener(listener);
	    buttonTwo.add(delete);
	    
	   
	    
	    addR = new JButton(translator.translateMessage("addR"));
      addR.setName("addRow");
      addR.setIcon(get.createAddRowIcon());
      addR.setToolTipText(translator.translateMessage("addRToolTip"));
      addR.addActionListener(listener);
      buttonOne.add(addR);
      
      deleteR = new JButton(translator.translateMessage("deleteR"));
      deleteR.setToolTipText(translator.translateMessage("deleteRToolTip"));
      deleteR.setName("deleteRow");
      deleteR.setIcon(get.createDeleteRowIcon());
      deleteR.addActionListener(listener);
      buttonTwo.add(deleteR);
      
      buttonPane.add(buttonTwo, BorderLayout.SOUTH);
      buttonPane.add(buttonOne, BorderLayout.CENTER);
      
	    JPanel editor = new JPanel();
	    editor.setLayout(new BoxLayout(editor, BoxLayout.Y_AXIS));
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

		private String[][] getRowList(int rowCount) {
      // TODO Auto-generated method stub
		  String[][] list = new String[rowCount][1];
		  for(int i=0; i<rowCount; i++){
		    list[i][0] = "R: " + i;
		  }
      return list;
    }

    private int calculate(String string2) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			int length = string2.length();
			return length*12+2*3;
		}

		
		
		public void resetField(Object value){
			String[][] array = arrayToString(value);
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			int rowCount = model.getRowCount();
			
			for(int i=0; i<rowCount; i++)
				model.removeRow(0);
			
			model.setColumnCount(array[0].length);
			
			for(int i=0; i<array.length; i++){
				model.addRow(array[i]);
			}
			
			TableColumnModel columnModel = table.getColumnModel();
			int size = array[0].length;
			
			for(int i=0; i<size; i++){
				int preferredWidth = calculate(array[0][i]);
				TableColumn c = columnModel.getColumn(i);
        c.setPreferredWidth(preferredWidth);
        c.setHeaderValue(translator.translateMessage("Col")+": "+i);
			}	
			table.getTableHeader().resizeAndRepaint();
			
			resetRowTable(array.length);
		}


		private void resetRowTable(int length) {
      // TODO Auto-generated method stub
		  String[][] array = getRowList(length);
      
      DefaultTableModel model = (DefaultTableModel)rowTable.getModel();
      
      int rowCount = model.getRowCount();
      
      for(int i=0; i<rowCount; i++){
        model.removeRow(0);
      }
      
      for(int i=0; i<array.length; i++){
        model.addRow(array[i]);
      }
    }

    private String[][] arrayToString(Object primValue) {
			// TODO Auto-generated method stub
			String[][] a;
			int sizeRow;
			int sizeColumn;
			
			if(string){
				
				sizeRow = ((String[][]) primValue).length;
				sizeColumn = ((String[][]) primValue)[0].length;
				a = new String[sizeRow][sizeColumn];
				for(int i=0; i<sizeRow; i++)
					for(int j=0; j<sizeColumn; j++)
						a[i][j] = ((String[][]) primValue)[i][j];
				
				
			}else{
				
				sizeRow = ((int[][]) primValue).length;
				sizeColumn = ((int[][]) primValue)[0].length;
				a = new String[sizeRow][sizeColumn];
				for(int i=0; i<sizeRow; i++){
					for(int j=0; j<sizeColumn; j++){
						a[i][j] = Integer.toString(((int[][]) primValue)[i][j]);
					}
				}

			}
			
			return a;
		}
		
		 @Override
		  public void changeLocale() {
		    // TODO Auto-generated method stub
		    translator.setTranslatorLocale(Animal.getCurrentLocale());
		    add.setText(translator.translateMessage("addC"));
		    delete.setText(translator.translateMessage("deleteC"));
		    add.setToolTipText(translator.translateMessage("addCToolTip"));
		    delete.setToolTipText(translator.translateMessage("deleteCToolTip"));
		    addR.setText(translator.translateMessage("addR"));
        deleteR.setText(translator.translateMessage("deleteR"));
        addR.setToolTipText(translator.translateMessage("addRToolTip"));
        deleteR.setToolTipText(translator.translateMessage("deleteRToolTip"));
        nValue.setText(translator.translateMessage("value"));
        edit.setToolTipText(translator.translateMessage("editFToolTipText"));
        edit.setText(translator.translateMessage("editF"));
        posiY.setText(translator.translateMessage("cPosition"));
        posi.setText(translator.translateMessage("rPosition"));
        
        TableColumnModel columnModel = table.getColumnModel();
        for(int i=0; i<columnModel.getColumnCount(); i++){
          
          //System.out.println(preferredWidth);
          TableColumn c = columnModel.getColumn(i);
         
          c.setHeaderValue(translator.translateMessage("Col")+": "+i);
         
        } 
        
        table.getTableHeader().resizeAndRepaint();
        //		    tool.changeLocale();
//		    infoButton.setToolTipText(translator.translateMessage("infoArrayLabel"));
		  }

    @Override
    public String getElementName() {
      // TODO Auto-generated method stub
      return name;
    }

}
