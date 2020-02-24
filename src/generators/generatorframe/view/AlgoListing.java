package generators.generatorframe.view;

import generators.generatorframe.controller.CategoryInfoActionListener;
import generators.generatorframe.controller.FilterActionListener;
import generators.generatorframe.controller.GeneratorListSelectionListener;
import generators.generatorframe.filter.*;
import generators.generatorframe.view.image.GetIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import animal.main.Animal;
import translator.TranslatableGUIElement;
import translator.Translator;

/**
 * 
 * @author Nora Wester
 *
 */

public class AlgoListing extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4050226265590669785L;

	JLabel type;
	AbstractButton infoButton;
	TranslatableGUIElement trans;
	Translator translator;
	CategoryInfoActionListener categoryInfo;
	// JTextField search;
	JComboBox<String> code;
	JComboBox<String> language;

	JTable list;
	private JTableHeader listHead;

	String[] codeArray;
	String[] langArray;

	int highestRow = -1;

	// new global

	private JPanel filterBar;
	private JLabel labelTwo;
	private JLabel labelThree;
	private int zoomCounter = 0;

	private static AlgoListing latestAlgoListing = null;
	private int width;

	public AlgoListing(int width, String[] codeArray, String[] langArray, TranslatableGUIElement trans) {

		super();
		super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		super.setBackground(Color.white);
		this.trans = trans;
		translator = new Translator("GeneratorFrame", Animal.getCurrentLocale());
		this.codeArray = codeArray;
		this.langArray = langArray;
		this.width = width;
		setContent(width);

		latestAlgoListing = this;
	}

	public static AlgoListing getLatestAlgoListing() {
		return latestAlgoListing;
	}

	// width indicates the width of the main FilterBarPanel
	private void setContent(int width) {
		// TODO Auto-generated method stub

		super.add(setFilterBar(width));

		// panel for displaying selected category
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

		// some space on the left
		p.add(Box.createHorizontalStrut(10));

		p.setBackground(Color.white);

		p.setPreferredSize(new Dimension(width, 30));
		p.setMaximumSize(new Dimension(width, 30));

		// display selected category
		type = new JLabel();
		type.setBackground(Color.white);
		p.add(type);

		// some space between display of the category and info button
		p.add(Box.createHorizontalStrut(10));

		// ist �berhaupt eine Kategorie angegben oder eine Mischung
		// if(category.compareTo("") != 0){
		// here are some infos about the category
//			ImageIcon icon = new GetIcon().createImageIcon();
		infoButton = trans.generateJButton("categoryInfo");
		infoButton.setMaximumSize(new Dimension(20, 20));
		infoButton.setPreferredSize(new Dimension(20, 20));
		infoButton.setMinimumSize(new Dimension(20, 20));
		// infoButton.setToolTipText("Some information about the category");
		infoButton.setBackground(Color.white);

		categoryInfo = new CategoryInfoActionListener();

		// set Listener
		infoButton.addActionListener(categoryInfo);

		p.add(infoButton);
		// da man nicht wei� ob durch suche oder Kategoriewahl das Panel angezeigt wird
		// muss
		// der Button standardm��ig unsichtbar sein
		infoButton.setVisible(false);

		p.add(Box.createHorizontalGlue());

		super.add(p);

		// some space between category display and list
		super.add(Box.createVerticalStrut(5));

		// ein extra Panel f�r die Liste
		JPanel listPane = new JPanel(new BorderLayout());
		listPane.setBackground(Color.white);

		String[] tableHeader = new String[] { "Name", "Code", "Language" };

		String[][] content = new String[][] {};
		list = new JTable(new DefaultTableModel(content, tableHeader) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		});
		listHead = list.getTableHeader();
		// set width for each column
		TableColumn column = list.getColumnModel().getColumn(0);
		column.setPreferredWidth(width - 200);

		column = list.getColumnModel().getColumn(1);
		column.setPreferredWidth(94);

		column = list.getColumnModel().getColumn(2);
		column.setPreferredWidth(50);

		list.setRowHeight(30);

//		column = list.getColumnModel().getColumn(3);
//		column.setPreferredWidth(190);

		// nur ein Algorithmus kann ausgew�hlt werden
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// nur die ganze Reihe soll ausw�hlbar sein
		list.setRowSelectionAllowed(true);
		list.setCellSelectionEnabled(true);
		list.setColumnSelectionAllowed(false);

		// set ToolTip
		list.setToolTipText(translator.translateMessage("tableAlgo"));

		// TODO NullPointerException
//		DefaultCellEditor edit = (DefaultCellEditor)list.getCellEditor();
//		edit.setClickCountToStart(Integer.MAX_VALUE);

		GeneratorListSelectionListener listener = new GeneratorListSelectionListener();
		// set listListener
		list.getSelectionModel().addListSelectionListener(listener);
		list.addMouseListener(listener);
		list.addKeyListener(listener);

		listPane.add(list.getTableHeader(), BorderLayout.PAGE_START);
		listPane.add(list, BorderLayout.CENTER);

		// set scroll
		JScrollPane scroll = new JScrollPane(listPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(10);

		super.add(scroll);
		scroll.setFocusable(true);

		super.add(Box.createVerticalGlue());
	}

	private void setClick(int row) {
		if (highestRow < row) {
			DefaultCellEditor edit = (DefaultCellEditor) list.getCellEditor(row, 0);
			// System.out.println(list.getCellEditor());
			// System.out.println(list.getCellEditor(row, 0));
			edit.setClickCountToStart(Integer.MAX_VALUE);
			highestRow = row;
		}
	}

	private JPanel setFilterBar(int width) {
		// TODO Auto-generated method stub
		System.out.println("setFilterBar");
		filterBar = new JPanel();

		filterBar.setBackground(Color.white);
		filterBar.setPreferredSize(new Dimension(width, 80));
		filterBar.setMaximumSize(new Dimension(width, 80));

		// set border for abgrenzen the filterBar
		CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createLoweredBevelBorder());
		filterBar.setBorder(border);

		// space on the left
		filterBar.add(Box.createHorizontalGlue());

//		//first column for searching
//		JPanel firstCol = new JPanel();
//		firstCol.setLayout(new BoxLayout(firstCol, BoxLayout.Y_AXIS));
//		firstCol.setBackground(Color.white);
//		
//		JLabel label = trans.generateJLabel("search");
//		label.setPreferredSize(new Dimension(100, 30));
//		label.setMinimumSize(new Dimension(100, 30));
//		label.setOpaque(true);
//		label.setBackground(Color.white);
//		
//		search = new JTextField();
//		search.setMinimumSize(new Dimension(100, 30));
//		search.setPreferredSize(new Dimension(100, 30));
//		search.setOpaque(true);
//		//set a hint for the user
//		search.setToolTipText(translator.translateMessage("searchTextList"));
//		//set listener for textfield searching
//		search.addKeyListener(new SearchKeyListener(FilterCoordinator.TEXT));
//
//		
//		firstCol.add(label);
//		firstCol.add(search);
//		
//		filterBar.add(firstCol);

		// second column for filtering code language
		JPanel secondCol = new JPanel(new BorderLayout());

		// secondCol.setLayout(new BoxLayout(secondCol, BoxLayout.Y_AXIS));
		secondCol.setBackground(Color.white);

		labelTwo = trans.generateJLabel("codeLang");
		labelTwo.setMinimumSize(new Dimension(90, 30));
		labelTwo.setPreferredSize(new Dimension(90, 30));
		labelTwo.setOpaque(true);
		labelTwo.setBackground(Color.white);

		// TODO get Information out of ANIMAL

		// code = new JComboBox<String>(codeArray);
		code = trans.generateJComboBox("comboCode", null, codeArray);
		code.setMinimumSize(new Dimension(90, 20));
		code.setPreferredSize(new Dimension(90, 20));
		code.setOpaque(true);

		// set a hint for the user
		// code.setToolTipText("select preferred code language");

		// selt listener for choosing code language
		code.addActionListener(new FilterActionListener(FilterCoordinator.CODE));

		secondCol.add(labelTwo, BorderLayout.NORTH);
		secondCol.add(code, BorderLayout.CENTER);

		filterBar.add(secondCol);

		filterBar.add(Box.createHorizontalStrut(30));

		// third column for filtering language
		JPanel thirdCol = new JPanel(new BorderLayout());
		// thirdCol.setLayout(new BoxLayout(thirdCol, BoxLayout.Y_AXIS));
		thirdCol.setBackground(Color.white);

		labelThree = trans.generateJLabel("lang");
		labelThree.setMinimumSize(new Dimension(68, 30));
		labelThree.setPreferredSize(new Dimension(68, 30));
		labelThree.setOpaque(true);
		labelThree.setBackground(Color.white);

		// TODO get Information out of ANIMAL
		// String[] langTyps = sI.getLanguageArray();
		// language = new JComboBox<String>(langArray);
		language = trans.generateJComboBox("comboLang", null, langArray);
		language.setMinimumSize(new Dimension(68, 20));
		language.setPreferredSize(new Dimension(68, 20));
		language.setOpaque(true);
		// language.setSelectedIndex(comboLang);
		// set a hint for the user
		// language.setToolTipText("select preferred language");

		// set Listener for choosing laguage
		language.addActionListener(new FilterActionListener(FilterCoordinator.LANG));

		thirdCol.add(labelThree, BorderLayout.NORTH);
		thirdCol.add(language, BorderLayout.CENTER);
		filterBar.setBackground(Color.white);

		filterBar.add(thirdCol);

		// space on the left
		filterBar.add(Box.createHorizontalGlue());

		return filterBar;

	}

	public void changeTableContent(String[][] content) {

		DefaultTableModel model = (DefaultTableModel) list.getModel();
		int number = model.getRowCount();
		for (int i = 0; i < number; i++) {
			model.removeRow(0);
		}

		for (int j = 0; j < content.length; j++) {
			model.addRow(content[j]);
			setClick(j);
		}

	}

	public void setNoFilter() {
		// search.setText("");
		code.setSelectedIndex(0);
		language.setSelectedIndex(0);
	}

	public void setCategory(String category) {
		type.setText(category);

		if (category.compareTo("") != 0) {
			ImageIcon icon = new GetIcon().createImageIcon();
			infoButton.setIcon(icon);
			infoButton.setVisible(true);
			categoryInfo.setCategory(category);
		} else {
			infoButton.setVisible(false);
		}
	}

	public void clearSelected() {
		int index = list.getSelectedRow();
		list.removeRowSelectionInterval(index, index);
	}

	public void changeLocale() {
		categoryInfo.changeLocale();
		translator.setTranslatorLocale(Animal.getCurrentLocale());
		list.setToolTipText(translator.translateMessage("tableAlgo"));
		// search.setToolTipText(translator.translateMessage("searchTextList"));

	}

	/**
	 * zooms the listing
	 * 
	 * @param zoomIn if true zooms in, if false zooms out
	 */
	public void zoom(boolean zoomIn) {

		Font f1 = type.getFont();
		Font f2 = infoButton.getFont();
		Font f3 = code.getFont();
		Font f4 = language.getFont();
		Font f5 = list.getFont();
		Font f6 = listHead.getFont();
		Dimension dim = this.getSize();
		Dimension dimC = code.getSize();
		Dimension dimL = language.getSize();

		Dimension dimFB = filterBar.getSize();

		if (zoomIn) {
			if (zoomCounter < 6)
				zoomCounter++;
			if (f1.getSize() < 24)
				f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() + 2);
			if (f2.getSize() < 24)
				f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() + 2);
			if (f3.getSize() < 24)
				f3 = new Font(f3.getName(), f3.getStyle(), f3.getSize() + 2);
			if (f4.getSize() < 24)
				f4 = new Font(f4.getName(), f4.getStyle(), f4.getSize() + 2);
			if (f5.getSize() < 24)
				f5 = new Font(f5.getName(), f5.getStyle(), f5.getSize() + 2);
			if (f6.getSize() < 24)
				f6 = new Font(f6.getName(), f6.getStyle(), f6.getSize() + 2);
			if (dim.width <= 800)
				dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
			if (dimFB.height <= 110)
				dimFB.setSize(dimFB.getWidth(), dimFB.getHeight() + 10);
			if (dimC.height <= 40)
				dimC.setSize(dimC.getWidth() + 16, dimC.getHeight() + 2);
			if (dimL.height <= 40)
				dimL.setSize(dimL.getWidth() + 16, dimL.getHeight() + 2);

		} else {
			if (zoomCounter > -1)
				zoomCounter--;
			if (f1.getSize() > 10)
				f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() - 2);
			if (f2.getSize() > 10)
				f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() - 2);
			if (f3.getSize() > 10)
				f3 = new Font(f3.getName(), f3.getStyle(), f3.getSize() - 2);
			if (f4.getSize() > 10)
				f4 = new Font(f4.getName(), f4.getStyle(), f4.getSize() - 2);
			if (f5.getSize() > 10)
				f5 = new Font(f5.getName(), f5.getStyle(), f5.getSize() - 2);
			if (f6.getSize() > 10)
				f6 = new Font(f6.getName(), f6.getStyle(), f6.getSize() - 2);
			if (dim.width >= 700)
				dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);

			if (dimFB.height >= 80)
				dimFB.setSize(dimFB.getWidth(), dimFB.getHeight() - 10);
			if (dimC.height >= 20)
				dimC.setSize(dimC.getWidth() - 16, dimC.getHeight() - 2);
			if (dimL.height >= 20)
				dimL.setSize(dimL.getWidth() - 16, dimL.getHeight() - 2);

		}

		if (dim.getHeight() < 461 + zoomCounter * 20)
			dim.setSize(dim.getWidth(), 461 + zoomCounter * 20);
		if (dim.getWidth() < width + zoomCounter * 20)
			dim.setSize(width + zoomCounter * 20, dim.getHeight());

		this.setSize(dim);

		type.setFont(f1);
		infoButton.setFont(f2);
		code.setFont(f3);
		language.setFont(f4);
		list.setFont(f5);

		list.repaint();
		if (categoryInfo != null)
			categoryInfo.zoom(zoomIn);

		if (dimC.getWidth() < 90 + zoomCounter * 16)
			dimC.setSize(90 + zoomCounter * 16, dimC.getWidth());
		if (dimC.getHeight() < 20 + zoomCounter * 2)
			dimC.setSize(dimC.getWidth(), 20 + zoomCounter * 2);
		code.setSize(dimC);
		code.setMinimumSize(dimC);
		code.setPreferredSize(dimC);
		code.repaint();
		if (dimL.getWidth() < 68 + zoomCounter * 16)
			dimL.setSize(68 + zoomCounter * 16, dimL.getWidth());
		if (dimL.getHeight() < 20 + zoomCounter * 2)
			dimL.setSize(dimL.getWidth(), 20 + zoomCounter * 2);
		language.setSize(dimL);
		language.setMinimumSize(dimL);
		language.setPreferredSize(dimL);
		language.repaint();

		Dimension listDim = new Dimension(this.getWidth() - 10, list.getHeight());
		list.setSize(listDim);
		// System.out.println("list size: " + list.getSize().toString());
		// secondCol.setSize(dimSC);
		if (dimFB.getHeight() < 80 + zoomCounter * 10)
			dimFB.setSize(dimFB.getWidth(), 80 + zoomCounter * 10);
		dimFB.setSize(dim.width - 20, dimFB.height);
		filterBar.setPreferredSize(dimFB);
		filterBar.setMaximumSize(dimFB);
		filterBar.setSize(dimFB);

		labelTwo.setFont(f3);
		labelThree.setFont(f3);
		// list.setFont(f3);

		listHead.setFont(f6);

		this.repaint();
	}

}