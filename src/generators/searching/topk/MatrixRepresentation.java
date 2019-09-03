package generators.searching.topk;

import java.util.Map;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.Primitive;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class MatrixRepresentation {

	StringMatrix m;
	private Node upperLeft;
	private Language lang;
	private MatrixProperties props;
	private TwoValueCounter counter;
	private boolean countAccesses;

	/**
	 * creates a matrix with a header
	 * 
	 * @param lang
	 * @param array
	 *            - input matrix (array)
	 * @param upperLeft
	 */
	public MatrixRepresentation(Language lang, String array[][],
			Node upperLeft, MatrixProperties props,boolean countAccesses) {
		init(lang, upperLeft);
		String relationWithHeaders[][] = new String[array.length + 1][array[0].length];
		for (int k = 0; k < relationWithHeaders.length; k++) {
			for (int j = 0; j < relationWithHeaders[0].length; j++) {
				if (k == 0 && j > 0) // first row contains the header
					relationWithHeaders[k][j] = "A" + j;
				else if (k == 0 && j == 0)
					relationWithHeaders[k][j] = "";
				else
					relationWithHeaders[k][j] = array[k - 1][j];
			}
		}
		this.props = props;
		m = lang.newStringMatrix(upperLeft, relationWithHeaders, "", null,
				props);
		this.countAccesses=countAccesses;
		this.counter=lang.newCounter(m);
		this.counter.deactivateCounting();
	}

	/**
	 * creates a matrix with a header in first column
	 * 
	 * @param lang
	 * @param listOfMaps
	 * @param upperLeft
	 */
	public MatrixRepresentation(Language lang,
			Map<String, Double>[] listOfMaps, Node upperLeft,MatrixProperties props,boolean countAccesses) {

		init(lang, upperLeft);
		String array[][] = new String[listOfMaps[0].size() + 1][listOfMaps.length];

		int column = 0;
		for (Map<String, Double> tempMap : listOfMaps) {
			array[0][column] = "A" + (column + 1); // first row contains the
													// header

			int row = 1;
			for (String key : tempMap.keySet()) {
				array[row][column] = key + "," + tempMap.get(key);
				row++;
			}
			column++;
		}
		this.props=props;
		m = lang.newStringMatrix(upperLeft, array, "", null, this.props);
		this.countAccesses=countAccesses;
		this.counter=lang.newCounter(m);
		this.counter.deactivateCounting();
	}

	/**
	 * creates a Matrix
	 * 
	 * @param lang
	 * @param noCols
	 * @param upperLeft
	 * @param leaveFirstFieldBlank
	 *            : true=blank
	 */
	public MatrixRepresentation(Language lang, int noCols, Node upperLeft,
			boolean leaveFirstFieldBlank, MatrixProperties props,boolean countAccesses) {
		init(lang, upperLeft);
		int i = 0;
		String array[][] = new String[1][noCols];
		if (leaveFirstFieldBlank) {
			array[0][0] = " ";
			i = 1;
		}
		int j = 1;
		for (; i < noCols; i++) {
			array[0][i] = "A" + j;
			j++;
		}
		this.props=props;
		m = lang.newStringMatrix(this.upperLeft, array, "", null, this.props);
		this.countAccesses=countAccesses;
		this.counter=lang.newCounter(m);
		this.counter.deactivateCounting();
	}

	/**
	 * creates a String matrix
	 * 
	 * @param lang
	 * @param upperLeft
	 * @param array
	 * @return
	 */
	public static StringMatrix fastStringMatrix(Language lang, Node upperLeft,
			String array[][], MatrixProperties props) {
		return lang.newStringMatrix(upperLeft, array, "", null, props);
	}

	/**
	 * initialize the matrix and sets the printing properties
	 * 
	 */
	void init(Language lang, Node upperLeft) {
		this.upperLeft = upperLeft;
		this.lang = lang;
	}

	/**
	 * to modify the values of a cell
	 * ACHTUNG: NICHT GENERISCH
	 * 
	 * @param row
	 * @param column
	 * @param data
	 */
	public void set(int row, int column, String data) {
		// first row contains the header
		m.put(row + 1, column, data, null, null);
		if(column!=0)
			counter.assignmentsInc(1);
			
	}

	/**
	 * sets content to cell of header
	 * 
	 * @param column
	 *            : header column to be set
	 * @param data
	 *            : content to be set
	 */
	public void setHeader(int column, String data) {
		m.put(0, column, data, null, null);
		counter.assignmentsInc(1);
	}

	/**
	 * returns the value of a cell
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public String get(int row, int column) {
		// first row contains the header
		if(countAccesses)
			counter.accessInc(1);
		return m.getElement(row + 1, column);
		
	}
	
	/**
	 * returns the value of a cell without incrementing the access-counter
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public String getUncounted(int row, int column) {
		// first row contains the header
		return m.getElement(row + 1, column);
		
	}

	/**
	 * �berpr�ft, ob ein Element in einer spalte vorhanden ist
	 * 
	 * @param column
	 *            die zu durchsuchende spalte
	 * @param element
	 *            das zu findende Element
	 * @return wahr oder falsch...D'AAAHHH!!!
	 */
	public boolean containsElementInColumn(int column, String element) {
		return findElementRow(element, column) != -1;
	}

	/**
	 * Gibt die Zeile der Matrix zur�ck, in der sich ein Element befindet
	 * 
	 * @param element
	 * @param column
	 * @return
	 */
	public int findElementRow(String element, int column) {
		for (int i = 1; i < m.getNrRows(); i++)
			if (m.getElement(i, column).equals(element))
				return i - 1;
		return -1;
	}

	/**
	 * Adds a new line to the existing matrix by replacing the matrix with a new
	 * one
	 * 
	 * @return the index number of the row just added
	 */
	public int addNewLine() {
		String newMatrix[][] = new String[m.getNrRows() + 1][m.getNrCols()];
		for (int i = 0; i < m.getNrRows(); i++) {
			for (int j = 0; j < m.getNrCols(); j++) {
				newMatrix[i][j] = m.getElement(i, j);
			}
		}
		for (int j = 0; j < m.getNrCols(); j++)
			newMatrix[m.getNrRows()][j] = " ";
		m.hide();
		m = this.lang.newStringMatrix(this.upperLeft, newMatrix, "", null, props);
		// -1 wegen der Header-Zeile und -1 wegen der Indizierung - nicht der
		// reinen Zeilenzahl!
		return m.getNrRows() - 2;
	}

	/**
	 * Adds a new column to the existing matrix by it with a new one
	 * 
	 * @param columnTitle
	 * @return
	 */
	public int addNewColumn(String columnTitle) {
		String newMatrix[][] = new String[m.getNrRows()][m.getNrCols() + 1];
		for (int i = 0; i < m.getNrRows(); i++)
			for (int j = 0; j < m.getNrCols(); j++)
				newMatrix[i][j] = m.getElement(i, j);
		newMatrix[0][m.getNrCols()] = columnTitle;
		for (int j = 1; j < m.getNrRows(); j++)
			newMatrix[j][m.getNrCols()] = " ";

		m.hide();
		m = this.lang.newStringMatrix(this.upperLeft, newMatrix, "", null, props);
		// -1 wegen der Header-Zeile und -1 wegen der Indizierung - nicht der
		// reinen Zeilenzahl!
		return m.getNrCols() - 1;
	}

	/**
	 * Header-adjusted number of rows in this matrix
	 * 
	 * @return
	 */
	public int getNrRows() {
		return m.getNrRows() - 1;
	}
	
	/**
	 * Header-adjusted number of elements in this matrix
	 * 
	 * @return
	 */
	public int getNrElements() {
		return (m.getNrRows() - 1)*m.getNrCols();
	}

	/**
	 * Wrapper for moving the matrix
	 * 
	 * @param direction
	 * @param moveType
	 * @param baseNode
	 * @param x
	 * @param y
	 */
	public void moveTo(String direction, String moveType, Primitive baseNode,
			int x, int y) {
		m.moveTo(AnimalScript.DIRECTION_E, "translate", new Offset(x, y,
				baseNode, AnimalScript.DIRECTION_NE), null, new MsTiming(500));
	}

	/**
	 * calculates the sum of a row Hier d�rfen wir column bei eins beginnen
	 * lassen, da es ja auschlie�lich um attributesSeen geht
	 * 
	 * @param row
	 * @return
	 */
	public double getRowSum(int row) {
		double sum = 0;
		for (int column = 1; column < m.getNrCols(); column++) {
			if (m.getElement(row + 1, column) != " "){
				sum += Double.valueOf(m.getElement(row + 1, column));
				counter.accessInc(1);
			}
		}

		return sum;
	}

	/**
	 * Ermittelt innerhalb einer Spalte die Zeile mit dem angegebenen Namen.
	 * 
	 * @param column
	 * @param objectName
	 * @return
	 */
	public int getRowInColumnByName(int column, String objectName) {
		for (int row = 0; row < getNrRows(); row++)
			if (objectName.equals(getUncounted(row, column - 1).split("\\,")[0]))
				return row + 1;
		return -1;
	}
	
	public TwoValueCounter getCounter(){
		return counter;
	}
	
	public void accessInc(int i){
		counter.accessInc(i);
	}
}