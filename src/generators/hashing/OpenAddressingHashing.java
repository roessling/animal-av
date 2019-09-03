package generators.hashing ;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class OpenAddressingHashing implements Generator {

	@Deprecated
	public static void main( String[] args ) {
		LinkedList<Action> list = new LinkedList<Action>() ;
		Language language = new AnimalScript( "Hashing", "Flo", 640, 480 ) ;

		OpenAddressingHashing hash = new OpenAddressingHashing( language ) ;

		ArrayProperties arrayProps = new ArrayProperties() ;
		arrayProps.set( AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK ) ;
		arrayProps.set( AnimationPropertiesKeys.FILL_PROPERTY, Color.DARK_GRAY ) ;
		arrayProps.set( AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE ) ;
		arrayProps.set( AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK ) ;
		arrayProps.set( AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED ) ;
		arrayProps.set( AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.LIGHT_GRAY ) ;
		arrayProps.set( AnimationPropertiesKeys.DIRECTION_PROPERTY, true ) ;
		arrayProps.setName( "Table properties" ) ;

		SourceCodeProperties sourceProps = new SourceCodeProperties() ;
		sourceProps.set( AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED ) ;
		sourceProps.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font( "monospaced", Font.PLAIN, 14 ) ) ;
		sourceProps.setName( "SourceCode properties" ) ;

		AnimationPropertiesContainer propContainer = new AnimationPropertiesContainer() ;
		propContainer.add( arrayProps ) ;
		propContainer.add( sourceProps ) ;

		Random random = new Random() ;
		for ( int i = 0 ; i < 10 ; i++ ) {
			list.add( hash.new Action( ActionName.INSERT, random.nextInt( 20 ) ) ) ;
		}
		for ( int i = 0 ; i < 3 ; i++ ) {
			list.add( hash.new Action( ActionName.DELETE, random.nextInt( 20 ) ) ) ;
		}
		for ( int i = 0 ; i < 10 ; i++ ) {
			list.add( hash.new Action( ActionName.INSERT, random.nextInt( 20 ) ) ) ;
		}
		for ( int i = 0 ; i < 3 ; i++ ) {
			list.add( hash.new Action( ActionName.DELETE, random.nextInt( 20 ) ) ) ;
		}

		hash.setup( propContainer, new Hashtable<String, Object>() ) ;

		hash.run( list ) ;
		System.out.println( language ) ;
	}
	
	
	/**
	 * An action consists of a value and what to do with that value.
	 * @author Florian Jakob <f_jakob@rbg.informatik.tu-darmstadt.de>
	 * @see ActionName
	 *
	 */
	class Action {
		ActionName _action ;
		int _value ;

		Action(ActionName action, int value) {
			_value = value ;
			_action = action ;
		}

		ActionName getAction() {
			return _action ;
		}

		int getValue() {
			return _value ;
		}

		/**
		 * Returns the action name concatenated with the value.
		 * Example: INSERT 23
		 */
		public String toString() {
			return _action.toString() + " " + _value ;
		}
	}

	/**
	 * Enumeration to differentiate between possible actions.
	 * @author Florian Jakob <f_jakob@rbg.informatik.tu-darmstadt.de>
	 *
	 */
	enum ActionName {
		DELETE, INSERT, SEARCH ;
		
		/**
		 * Extract an action out of a String.
		 * A String beginning with a...
		 * ..."d" is assumed to describe a delete-action
		 * ..."s" is assumed to describe a search-action.
		 * Anything else is assumed to describe a insert-action.
		 * @param string String to check
		 * @return assumed ActionName
		 */
		static ActionName fromString(String string) {
			String normalizedString = string.toUpperCase() ;
			if (normalizedString.startsWith( "D" )) {
				return ActionName.DELETE ;
			} else if (normalizedString.startsWith( "S" )) {
				return ActionName.SEARCH ;
			} else {
				return ActionName.INSERT ;
			}
		}
	}

	abstract class Hashfunction {
		abstract int hash( int key ) ;
		abstract String toString( int key ) ;
	}

	class DivisionMethod extends Hashfunction {
		@Override
		int hash( int k ) {
			int result = calc(k) ;
			_hashfunctionActual.setText( this.toString(k), null, null ) ;
			return result ;
		}

		int calc( int k ) {
			return k % _size ;
		}
		
		public String toString() {
			return "h(k) = k mod m" ;
		}

		@Override
		String toString( int k ) {
			return "h(" + k + ") = " + k + " mod " + _size + " = " + calc(k) ;
		}
	}

	class MultiplicationMethod extends Hashfunction {
		private double _multConstant ;

		MultiplicationMethod() {
			_multConstant = ( Math.sqrt( 5 ) - 1 ) / 2 ;
		}

		@Override
		int hash( int k ) {
			int result = calc(k) ;
			_hashfunctionActual.setText( this.toString(k), null, null ) ;
			return result ;
		}
		
		int calc(int k) {
			Double tmp = k * _multConstant ;
			return (int) Math.floor( _size * ( tmp - Math.floor( tmp ) ) ) ;
		}

		public String toString() {
			return "h(k) = floor(m * ((k * ((sqrt(5)-1)/2)) mod 1))" ;
		}

		@Override
		String toString( int k ) {
			return "h(" + k + ") = floor(" + _size + " * ((" + k + " * ((sqrt(5)-1)/2)) mod 1)) = " + calc(k)  ;
		}
	}

	abstract class HashTable {
		int _numEntries ;
		int _size ;

		HashTable(int size) {
			_size = size ;
		}

		float getLoadFactor() {
			return (float) _numEntries / _size ;
		}

		abstract int contains( int value ) ;
		abstract int delete( int value ) ;
		abstract int put( int value ) throws TableFullException ;
	}

	@Deprecated
	class ChainingHashTable extends HashTable {
		Hashfunction _hashfunction ;
		LinkedList<Integer>[] _table ;

		@SuppressWarnings("unchecked")
    ChainingHashTable(int size, Hashfunction hashfunction) {
			super( size ) ;
			_hashfunction = hashfunction ;
			_table = new LinkedList[_size] ;
			for ( int i = 0 ; i < _size ; i++ ) {
				_table[i] = new LinkedList<Integer>() ;
			}
		}

		@Override
		int contains( int value ) {
			int k = _hashfunction.hash( value ) ;
			if ( _table[k].contains( value ) ) {
				return k ;
			} else {
				return -1 ;
			}
		}

		@Override
		int delete( int value ) {
			int k = _hashfunction.hash( value ) ;
			if ( _table[k].removeFirstOccurrence( value ) ) {
				_numEntries-- ;
				return k ;
			} else {
				return -1 ;
			}
		}

		@Override
		int put( int value ) {
			int k = _hashfunction.hash( value ) ;
			if ( !_table[k].contains( value ) ) {
				_table[k].addFirst( value ) ;
				_numEntries++ ;
				return k ;
			} else {
				return -1 ;
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder() ;
			builder.append( "Hashmap:\n" ) ;
			builder.append( "Size: " + _size + " Entries: " + _numEntries + " Factor: " + getLoadFactor() + "\n" ) ;
			for ( int i = 0 ; i < _size ; i++ ) {
				builder.append( "[" + i + "]" ) ;
				for ( Integer j : _table[i] ) {
					builder.append( " <- " + j ) ;
				}
				builder.append( "\n" ) ;
			}
			return builder.toString() ;
		}
	}

	class OpenAddressingHashTable extends HashTable {
		Probe _probe ;
		int[] _table ;

		final int EMPTY = -1 ;
		final int DELETED = -2 ;
		final int NOT_FOUND = -3 ;
		final int DUPLICATE = -4 ;

		OpenAddressingHashTable(int size, Probe probe) {
			super( size ) ;
			_probe = probe ;
			_table = new int[_size] ;
			for ( int i = 0 ; i < _size ; i++ ) {
				_table[i] = EMPTY ;
			}

			_visualTable = _language.newIntArray( new Offset( 0, 30, _title, "SW" ), _table, "Array", null, _tableProperties ) ;
		}

		@Override
		int contains( int k ) {
			_sourceContains.highlight( "head" ) ;
			_language.nextStep() ;

			_sourceContains.toggleHighlight( "head", "i" ) ;
			_language.nextStep() ;
			int i = 0 ;

			_sourceContains.toggleHighlight( "i", "while" ) ;
			_language.nextStep() ;
			_sourceContains.unhighlight( "while" ) ;
			while ( i < _size ) {
				_sourceContains.highlight( "j" ) ;
				_language.nextStep() ;

				int j = _probe.probe( k, i ) ;
				_visualTable.highlightElem( j, null, null ) ;
				_sourceContains.toggleHighlight( "j", "if" ) ;
				_language.nextStep() ;
				_sourceContains.unhighlight( "if" ) ;
				if ( ( k == _table[j] ) ) {
					_sourceContains.highlight( "returnj" ) ;
					_language.nextStep() ;
					_sourceContains.unhighlight( "returnj" ) ;
					_visualTable.unhighlightElem( j, null, null ) ;
					return j ;
				} else if ( _table[j] == EMPTY ) {
					_sourceContains.highlight( "elseif" ) ;
					_language.nextStep() ;
					_sourceContains.toggleHighlight( "elseif", "not_found1" ) ;
					_language.nextStep() ;
					_sourceContains.unhighlight( "not_found1" ) ;
					_visualTable.unhighlightElem( j, null, null ) ;
					return NOT_FOUND ;
				} else {
					_sourceContains.highlight( "elseif" ) ;
					_language.nextStep() ;
					_sourceContains.toggleHighlight( "elseif", "else" ) ;
					_language.nextStep() ;
					_sourceContains.toggleHighlight( "else", "i++" ) ;
					_language.nextStep() ;
					i++ ;
					_sourceContains.unhighlight( "i++" ) ;
				}
				_visualTable.unhighlightElem( j, null, null ) ;
				_sourceContains.highlight( "while" ) ;
				_language.nextStep() ;
				_sourceContains.unhighlight( "while" ) ;
			}
			_sourceContains.highlight( "not_found2" ) ;
			_language.nextStep() ;
			_sourceContains.unhighlight( "not_found2" ) ;
			return NOT_FOUND ;
		}

		@Override
		int delete( int k ) {
			_sourceDelete.highlight( "head" ) ;
			_language.nextStep() ;

			_sourceDelete.toggleHighlight( "head", "contains" ) ;
			_language.nextStep() ;

			int j = this.contains( k ) ;
			_visualTable.highlightElem( j, null, null ) ;
			_sourceDelete.toggleHighlight( "contains", "if" ) ;
			_language.nextStep() ;
			_sourceDelete.unhighlight( "if" ) ;
			if ( j != NOT_FOUND ) {
				_sourceDelete.highlight( "delete" ) ;
				_language.nextStep() ;

				_table[j] = DELETED ;
				_visualTable.put( j, DELETED, null, null ) ;
				_visualTable.unhighlightCell( j, null, null ) ;
				_sourceDelete.toggleHighlight( "delete", "numEntries" ) ;
				_language.nextStep() ;

				_numEntries-- ;
				updateLoadFactor() ;
				_sourceDelete.unhighlight( "numEntries" ) ;
			}
			_sourceDelete.highlight( "return" ) ;
			_language.nextStep() ;
			_sourceDelete.unhighlight( "return" ) ;
			_visualTable.unhighlightElem( j, null, null ) ;
			return j ;
		}

		@Override
		int put( int k ) throws TableFullException {
			_sourceInsert.highlight( "head" ) ;
			_language.nextStep() ;

			_sourceInsert.toggleHighlight( "head", "seti" ) ;
			_language.nextStep() ;
			int i = 0 ;

			_sourceInsert.toggleHighlight( "seti", "while" ) ;
			_language.nextStep() ;
			_sourceInsert.unhighlight( "while" ) ;
			while ( i < _size ) {
				_sourceInsert.highlight( "hash" ) ;
				_language.nextStep() ;
				int j = _probe.probe( k, i ) ;

				_visualTable.highlightElem( j, null, null ) ;
				_sourceInsert.toggleHighlight( "hash", "if" ) ;
				_language.nextStep() ;
				_sourceInsert.unhighlight( "if" ) ;
				if ( _table[j] == k ) {
					_sourceInsert.highlight( "returnDuplicate" ) ;
					_language.nextStep() ;
					_visualTable.unhighlightElem( j, null, null ) ;
					_sourceInsert.unhighlight( "returnDuplicate" ) ;
					return DUPLICATE ;
				} else if ( _table[j] == EMPTY || _table[j] == DELETED ) {
					_sourceInsert.highlight( "elseif" ) ;
					_language.nextStep() ;
					_sourceInsert.toggleHighlight( "elseif", "table" ) ;
					_language.nextStep() ;

					_table[j] = k ;
					_visualTable.put( j, k, null, null ) ;
					_sourceInsert.toggleHighlight( "table", "numEntries" ) ;

					_visualTable.highlightCell( j, null, null ) ;

					_language.nextStep() ;

					_numEntries++ ;
					updateLoadFactor() ;
					_sourceInsert.toggleHighlight( "numEntries", "returnj" ) ;
					_language.nextStep() ;
					_sourceInsert.unhighlight( "returnj" ) ;
					return j ;
				} else {
					_sourceInsert.highlight( "elseif" ) ;
					_language.nextStep() ;

					_sourceInsert.toggleHighlight( "elseif", "else" ) ;
					_language.nextStep() ;

					_sourceInsert.toggleHighlight( "else", "i++" ) ;
					_language.nextStep() ;
					_sourceInsert.unhighlight( "i++" ) ;
					i++ ;
				}
				_sourceInsert.highlight( "while" ) ;
				_visualTable.unhighlightElem( j, null, null ) ;
				_language.nextStep() ;
				_sourceInsert.unhighlight( "while" ) ;
			}
			_sourceInsert.highlight( "exception" ) ;
			_language.nextStep() ;
			_sourceInsert.unhighlight( "exception" ) ;

			throw new TableFullException() ;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder() ;
			builder.append( "OpenAddressHashmap:\n" ) ;
			builder.append( "Size: " + _size + " Entries: " + _numEntries + " Factor: " + getLoadFactor() + "\n" ) ;
			for ( int i = 0 ; i < _size ; i++ ) {
				builder.append( "[" + i + "] " ) ;
				if ( _table[i] >= 0 ) {
					builder.append( _table[i] ) ;
				}
				builder.append( "\n" ) ;
			}
			return builder.toString() ;
		}

	}

	class TableFullException extends Exception {
		private static final long serialVersionUID = 8369962505923426462L ;
	}

	abstract class Probe {
		Hashfunction _hashfunction ;

		Probe(Hashfunction hashfunction) {
			_hashfunction = hashfunction ;
		}

		Hashfunction getHashfunction() {
			return _hashfunction ;
		}

		abstract int probe( int key, int i ) ;
		abstract String toString( int key, int i ) ;
	}

	class LinearProbe extends Probe {
		LinearProbe(Hashfunction hashfunction) {
			super( hashfunction ) ;
		}

		@Override
		int probe( int k, int i ) {
			int result = calc(k, i) ;
			_probeActual.setText( this.toString( k, i ), null, null ) ;
			return result ;
		}
		
		int calc(int k, int i) {
			return ( _hashfunction.hash( k ) + i ) % _size ;
		}

		public String toString() {
			return "f(k,i) = (h(k) + i) mod m" ;
		}

		@Override
		String toString( int key, int i ) {
			return "f(" + key + "," + i + ") = (h(" + key + ") + " + i + ") mod " + _size + " = " + calc(key, i) ;
		}

	}

	class QuadraticProbe extends Probe {
		double _c1, _c2 ;

		QuadraticProbe(Hashfunction hashfunction, double c1, double c2) {
			super( hashfunction ) ;
			_c1 = c1 ;
			_c2 = c2 ;
		}

		@Override
		int probe( int key, int i ) {
			int result = calc(key, i) ;
			_probeActual.setText( this.toString(key,i), null, null ) ;
			return result ;
		}
		
		int calc( int key, int i) {
			return (int) ( _hashfunction.hash( key ) + ( _c1 * i ) + ( _c2 * i * i ) ) % _size ;
		}

		public String toString() {
			return "f(k,i) = h(k) + " + Double.toString( _c1 ) + " * i + " + Double.toString( _c2 ) + " * i^2" ;
		}

		@Override
		String toString( int key, int i ) {
			return "f(" + key + "," + i + ") = h(" + key + ") + " + Double.toString( _c1 ) + " * " + i + " + " + Double.toString( _c2 ) + " * " + i + "^2 = " + calc(key, i) ;
		}
	}

	

	ArrayProperties _tableProperties ;
	Language _language ;
	private HashTable _hashTable ;
	IntArray _visualTable ;
	Text _title ;
	private Text _labelLoadFactor ;
	private Text _varLoadFactor ;
	private Text _labelHashfunction ;
	private Text _labelProbe ;
	private Text _labelNext ;
	private Text _listNext ;
	private Text _labelAction ;
	private Text _action ;
	SourceCode _sourceInsert ;
	int _size ;
	SourceCode _sourceDelete ;
	SourceCode _sourceContains ;
	Text _hashfunctionActual ;
	Text _probeActual ;
	private Text _labelSize ;
	private Text _labelNumEntries ;
	private Text _varNumEntries ;

	public OpenAddressingHashing() {

	}

	public OpenAddressingHashing(Language language) {
		_language = language ;
		_language.setStepMode( true ) ;
	}

	@Override
	public String generate( AnimationPropertiesContainer properties, Hashtable<String, Object> primitives ) {
		init() ;
		setup(properties, primitives) ;
		
		int[] values = (int[]) primitives.get( "Values" ) ;
		String[] actions = (String[]) primitives.get("Actions") ;
		
		run(getActionList( values, actions )) ;
		
		return _language.toString() ;
	}

	@Override
	public String getAlgorithmName() {
		return "Open Addressing Hashing" ;
	}

	@Override
	public String getAnimationAuthor() {
		return "Florian Jakob"; // <f_jakob@rbg.informatik.tu-darmstadt.de>" ;
	}

	@Override
	public String getCodeExample() {
		String sourceContains = "int contains(k) {<br />" +
								"	int i = 0 ;<br />" +
								"	while( i &lt; _size) {<br />" +
								"		int j = f(k, i) ;<br />" +
								"		if ( k == _table[j] ) {<br />" +
								"			return j ;<br />" +
								"		} else if ( _table[j] == EMPTY ) {<br />" +
								"			return NOT_FOUND ;<br />" +
								"		} else {<br />" +
								"			i++ ;<br />" +
								"		}<br />" +
								"	}<br />" +
								"	return NOT_FOUND ;<br />" +
								"}" ;

		String sourceDelete = 	"int delete( int k ) {<br />" +
								"	int j = contains(k) ;<br />" +
								"	if ( j != NOT_FOUND ) {<br />" +
								"		_table[k] = DELETED ;<br />" +
								"		_numEntries-- ;<br />" +
								"	}<br />" +
								"	return j<br />" +
								"}" ;

		String insertCode = "int insert(k) throws TableFullException {<br />" +
							"	i = 0 ;<br />" +
							"	while (i &lt; _size) {<br />" +
							"		j = f(k, i) ;<br />" +
							"		if ( _table[j] == k ) {<br />" +
							"			return DUPLICATE ;<br />" +
							"		} else if ( _table[j] == EMPTY || _table[j] == DELETED) {<br />" +
							"			_table[j] = k ;<br />" +
							"			_numEntries++ ;<br />" +
							"			return j ;<br />" +
							"		} else {<br />" +
							"			i++ ;<br />" +
							"		}<br />" +
							"	}<br />" +
							"	throw new TableFullException() ;<br />" +
							"}" ;
		
		
		return insertCode + "<br /><br />\n" +  
				sourceContains + "<br /><br />\n" +
				sourceDelete;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US ;
	}

	@Override
	public String getDescription() {
		return "This generator visualizes open addressing hashing. See http://en.wikipedia.org/wiki/Open_Addressing for further information.<br />" +
				"The table will only hold values in the range [0 ; 9999]. Values -1 and -2 are special values, they represent EMPTY and DELETED respectively.<br />" +
				"The algorithms used are adapted from 'Introduction to Algorithms' by Cormen, Leiserson, Rivest and Stein. " +
				"Delete() is implemented, it doesn't work correct, though. " +
				"It is possible to have the same key more than once inside the table, with use of delete() followed by insert(). <br />" +
				"Rehashing is not implemented - this way you can watch how bad the performance gets with high load factors.<br />" +
				"In the options menu, you can choose to use the multiplication method instead of the division method or quadratic probing instead of linear probing. c1 and c2 are the factors used by quadratic probing.<br />" +
				"Try for yourself and enjoy this generator." ;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	@Override
	public String getName() {
		return "Open Addressing Hashing" ;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		_language = new AnimalScript( "Open Addressing Hashing", "Florian Jakob <f_jakob@rbg.informatik.tu-darmstadt.de", 640, 480 ) ;
		_language.setStepMode( true ) ;
	}

	private void run( LinkedList<Action> actions ) {
		while ( actions.size() > 0 ) {
			Action action = actions.poll() ;
			_action.setText( action.toString(), null, null ) ;
			updateNextList( actions, 5 ) ;
			_hashfunctionActual.setText( "", null, null ) ;
			_probeActual.setText( "", null, null ) ;
			_language.nextStep() ;

			switch ( action.getAction() ) {
			case INSERT:
				_sourceInsert.show() ;
				_language.nextStep() ;
				try {
					_hashTable.put( action.getValue() ) ;
				} catch ( TableFullException e ) {
					 e.printStackTrace() ;
				}
				_sourceInsert.hide() ;
				break ;

			case DELETE:
				_sourceDelete.show() ;
				_sourceContains.show() ;
				_language.nextStep() ;
				_hashTable.delete( action.getValue() ) ;
				_sourceDelete.hide() ;
				_sourceContains.hide() ;
				break ;

			case SEARCH:
				_sourceContains.show() ;
				_language.nextStep() ;
				_hashTable.contains( action.getValue() ) ;
				_sourceContains.hide() ;
				break ;
			}
		}
		
		_action.setText( "FINISHED", null, null ) ;
		_hashfunctionActual.setText( "", null, null ) ;
		_probeActual.setText( "", null, null ) ;
		_language.nextStep() ;

	}

	private void updateNextList( LinkedList<Action> actions, int num ) {
		StringBuilder string = new StringBuilder() ;
		for ( int i = 0 ; i < num && actions.size() > i ; i++ ) {
			string.append( actions.get( i ).toString() ) ;
			if ( i < actions.size() - 1 ) {
				string.append( ", " ) ;
			}
		}
		if ( num < actions.size() ) {
			string.append( "..." ) ;
		}
		_listNext.setText( string.toString(), null, null ) ;
	}

	private void setup(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {

		// table properties
		_size = (Integer) primitives.get("Size") ;
		_tableProperties = (ArrayProperties) properties.getPropertiesByName( "Table properties" ) ;
		Hashfunction hashfunction = getHashfunction( primitives ) ;
		Probe probe = getProbe( hashfunction, primitives ) ;
		
		// title
		TextProperties titleProps = new TextProperties() ;
		titleProps.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font( "SansSerif", Font.BOLD, 28 ) ) ;
		_title = _language.newText( new Coordinates( 10, 20 ), "Open Addressing Hashing", "title", null, titleProps ) ;

		// variable properties
		TextProperties varLabelProps = new TextProperties() ;
		varLabelProps.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font( "Monospaced", Font.PLAIN, 14 ) ) ;
		TextProperties varProps = varLabelProps ;

		// hash table initialization
		_hashTable = new OpenAddressingHashTable( _size, probe ) ;
		// variables
		_labelSize = _language.newText( new Offset( 0, 10, _visualTable, "SW" ), "size (m): " + _size, "labelSize", null, varLabelProps ) ;
		_labelNumEntries = _language.newText( new Offset( 0, 0, _labelSize, "SW" ), "# entries: ", "labelNumEntries", null, varLabelProps ) ;
		_varNumEntries = _language.newText( new Offset( 5, 0, _labelNumEntries, "NE" ), "0", "varNumEntries", null, varProps ) ;
		_labelLoadFactor = _language.newText( new Offset( 0, 0, _labelNumEntries, "SW" ), "load factor:", "labelLoadFactor", null, varLabelProps ) ;
		_varLoadFactor = _language.newText( new Offset( 5, 0, _labelLoadFactor, "NE" ), Float.toString( _hashTable.getLoadFactor() ), "varLoadFactor", null,
				varProps ) ;

		// actions
		_labelAction = _language.newText( new Offset( 100, 20, _title, "SW" ), "Action:", "labelAction", null, varLabelProps ) ;
		_action = _language.newText( new Offset( 0, 0, _labelAction, "SW" ), "none", "action", null, varProps ) ;
		_labelNext = _language.newText( new Offset( 200, 20, _title, "SW" ), "Next actions:", "labelNext", null, varLabelProps ) ;
		_listNext = _language.newText( new Offset( 0, 0, _labelNext, "SW" ), "", "listNext", null, varProps ) ;
		
		// Used functions
		_labelHashfunction = _language.newText( new Offset( 0, 20, _listNext, "SW" ), probe.getHashfunction().toString(), "labelHashfunction", null, varProps ) ;
		_labelProbe = _language.newText( new Offset( 0, 0, _labelHashfunction, "SW" ), probe.toString(), "labelProbe", null, varProps ) ;
		
		// actual values
		_hashfunctionActual = _language.newText( new Offset( 0, 20, _labelProbe, "SW"), "", "", null, varProps ) ;
		_probeActual = _language.newText( new Offset( 0, 0, _hashfunctionActual, "SW"), "", "", null, varProps ) ;

		// contains source
		_sourceContains = _language.newSourceCode( new Offset( 0, 20, _labelLoadFactor, "SW" ), "sourceContains", null, (SourceCodeProperties) properties
				.getPropertiesByName( "SourceCode properties" ) ) ;
		_sourceContains.addCodeLine( "int contains(k) {", "head", 0, null ) ;
		_sourceContains.addCodeLine( "int i = 0 ;", "i", 1, null ) ;
		_sourceContains.addCodeLine( "while( i < _size) {", "while", 1, null ) ;
		_sourceContains.addCodeLine( "int j = f(k, i) ;", "j", 2, null ) ;
		_sourceContains.addCodeLine( "if ( k == _table[j] ) {", "if", 2, null ) ;
		_sourceContains.addCodeLine( "return j ;", "returnj", 3, null ) ;
		_sourceContains.addCodeLine( "} else if ( _table[j] == EMPTY) {", "elseif", 2, null ) ;
		_sourceContains.addCodeLine( "return NOT_FOUND ;", "not_found1", 3, null ) ;
		_sourceContains.addCodeLine( "} else {", "else", 2, null ) ;
		_sourceContains.addCodeLine( "i++ ;", "i++", 3, null ) ;
		_sourceContains.addCodeLine( "}", "", 2, null ) ;
		_sourceContains.addCodeLine( "}", "", 1, null ) ;
		_sourceContains.addCodeLine( "return NOT_FOUND ;", "not_found2", 1, null ) ;
		_sourceContains.addCodeLine( "}", "", 0, null ) ;
		_sourceContains.hide() ;
		
		// delete source
		// animal has problems locating the north east coordinates of source code
		_sourceDelete = _language.newSourceCode( new Offset( 20, -15, _sourceContains, "NE" ), "sourceDelete", null, (SourceCodeProperties) properties
				.getPropertiesByName( "SourceCode properties" ) ) ;
		_sourceDelete.addCodeLine( "int delete( int k ) {", "head", 0, null ) ;
		_sourceDelete.addCodeLine( "int j = contains(k) ;", "contains", 1, null ) ;
		_sourceDelete.addCodeLine( "if ( j != NOT_FOUND ) {", "if", 1, null ) ;
		_sourceDelete.addCodeLine( "_table[k] = DELETED ;", "delete", 2, null ) ;
		_sourceDelete.addCodeLine( "_numEntries-- ;", "numEntries", 2, null ) ;
		_sourceDelete.addCodeLine( "}", "", 1, null ) ;
		_sourceDelete.addCodeLine( "return j", "return", 1, null ) ;
		_sourceDelete.addCodeLine( "}", "", 0, null ) ;
		_sourceDelete.hide() ;

		// insert source
		_sourceInsert = _language.newSourceCode( new Offset( 0, 20, _labelLoadFactor, "SW" ), "sourceInsert", null, (SourceCodeProperties) properties
				.getPropertiesByName( "SourceCode properties" ) ) ;
		_sourceInsert.addCodeLine( "int insert(k) throws TableFullException {", "head", 0, null ) ;
		_sourceInsert.addCodeLine( "i = 0 ;", "seti", 1, null ) ;
		_sourceInsert.addCodeLine( "while (i < _size) {", "while", 1, null ) ;
		_sourceInsert.addCodeLine( "j = f(k, i) ;", "hash", 2, null ) ;
		_sourceInsert.addCodeLine( "if ( _table[j] == k ) {", "if", 2, null ) ;
		_sourceInsert.addCodeLine( "return DUPLICATE ;", "returnDuplicate", 3, null ) ;
		_sourceInsert.addCodeLine( "} else if ( _table[j] == EMPTY || _table[j] == DELETED) {", "elseif", 2, null ) ;
		_sourceInsert.addCodeLine( "_table[j] = k ;", "table", 3, null ) ;
		_sourceInsert.addCodeLine( "_numEntries++ ;", "numEntries", 3, null ) ;
		_sourceInsert.addCodeLine( "return j ;", "returnj", 3, null ) ;
		_sourceInsert.addCodeLine( "} else {", "else", 2, null ) ;
		_sourceInsert.addCodeLine( "i++ ;", "i++", 3, null ) ;
		_sourceInsert.addCodeLine( "}", "", 2, null ) ;
		_sourceInsert.addCodeLine( "}", "", 1, null ) ;
		_sourceInsert.addCodeLine( "throw new TableFullException() ;", "exception", 1, null ) ;
		_sourceInsert.addCodeLine( "}", "", 0, null ) ;
		_sourceInsert.hide() ;

	}

	void updateLoadFactor() {
		_varNumEntries.setText( Integer.toString(_hashTable._numEntries), null, null ) ;
		_varLoadFactor.setText( Float.toString( _hashTable.getLoadFactor() ), null, null ) ;
	}
	
	private LinkedList<Action> getActionList(int[] values, String[] actions) {
		LinkedList<Action> actionList = new LinkedList<Action>() ;
		for (int i = 0 ; i < values.length ; i++) {
			int positiveValue = values[i] ;
			if (positiveValue < 0) {
				positiveValue = 0 ;
			} else if (positiveValue > 9999) {
				positiveValue = 9999 ;
			}
			
			if (i < actions.length) {
				actionList.addLast( new Action(ActionName.fromString( actions[i] ), positiveValue) ) ;
			} else {
				actionList.addLast( new Action(ActionName.INSERT, positiveValue)) ;
			}
		}
		
		return actionList ;
	}
	
	private Hashfunction getHashfunction(Hashtable<String, Object> primitives) {
		Hashfunction hashfunction ;
		
		if ( (Boolean) primitives.get( "MultiplicationMethod" ) ) {
			hashfunction = new MultiplicationMethod() ;
		} else {
			hashfunction = new DivisionMethod() ;
		}
		return hashfunction ;
	}
	
	private Probe getProbe(Hashfunction hashfunction, Hashtable<String, Object> primitives) {
		Probe probe ;
		if ((Boolean) primitives.get( "QuadraticProbing" )) {
			double c1 = (Double) primitives.get( "c1" ) ;
			double c2 = (Double) primitives.get( "c2" ) ;
			probe = new QuadraticProbe( hashfunction, c1, c2 ) ;
		} else {
			probe = new LinearProbe( hashfunction ) ;
		}
		
		return probe ;
	}
}
