package generators.sorting.shakersort ;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

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
import algoanim.util.MsTiming;
import algoanim.util.Offset;


public class ShakerSortFJ implements Generator {
	public static void main( String[] args ) {
		int[] array = { 9, 3, 6, 8, 3, 2, 1, 0, 2, 3 } ;
		Language language = new AnimalScript( "ShakerSort", "Florian Jakob <f_jakob@rbg.informatik.tu-darmstadt.de>", 640, 480 ) ;
		ShakerSortFJ ss = new ShakerSortFJ( language ) ;
		
		// array props
		ArrayProperties arrayProps = new ArrayProperties() ;
		arrayProps.set( AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK ) ;
		arrayProps.set( AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY ) ;
		arrayProps.set( AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE ) ;
		arrayProps.set( AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK ) ;
		arrayProps.set( AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED ) ;
		arrayProps.set( AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.WHITE ) ;
		arrayProps.setName( "arrayProperties" ) ;
		
		// general source code properties
		SourceCodeProperties sourceCodeProps = new SourceCodeProperties() ;
		sourceCodeProps.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font( "Monospaced", Font.PLAIN, 14 ) ) ;
		sourceCodeProps.set( AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED ) ;
		sourceCodeProps.setName( "sourceProperties" ) ;
		
		// put properties into a property container
		AnimationPropertiesContainer props = new AnimationPropertiesContainer() ;
		props.add( arrayProps ) ;
		props.add( sourceCodeProps ) ;
		
		// feed the algorithm
		ss.visualSort( array, props ) ;
		System.out.println( language ) ;
	}

	private Language _language ;
	private IntArray _array ;
	private SourceCode _sourceCode ;
    private Text _title ;
    
	public ShakerSortFJ() {
	}

	public ShakerSortFJ(Language language) {
		_language = language ;
		_language.setStepMode( true ) ;
	}

	@Override
	public String generate( AnimationPropertiesContainer props, Hashtable<String, Object> primitives ) {
		init() ;
		visualSort((int[]) primitives.get( "array" ), props) ;
		return _language.toString() ;
	}

	@Override
	public String getAlgorithmName() {
		return "Shaker Sort" ;
	}

	@Override
	public String getAnimationAuthor() {
		return "Florian Jakob"; // <f_jakob@rbg.informatik.tu-darmstadt.de>" ;
	}

	@Override
	public String getCodeExample() {
		return "void shakerSort( int[] array ) {<br />" +
				"	int left = 1 ;<br />" +
				"	int right = array.length - 1 ;<br />" +
				"	int lastSwap = left ;<br />" +
				"	do {<br />" +
				"		for ( int i = left ; i <= right ; i++ ) {<br />" +
				"			if ( array[i] < array[i - 1] ) {<br />" +
				"				swap( array, i, i - 1 ) ;<br />" +
				"				lastSwap = i ;<br />" +
				"			}<br />" +
				"		}<br />" +
				"		right = lastSwap - 1 ;<br />" +
				"		for ( int i = right ; i >= left ; i-- ) {<br />" +
				"			if ( array[i] < array[i - 1] ) {<br />" +
				"				swap( array, i, i - 1 ) ;<br />" +
				"				lastSwap = i ;<br />" +
				"			}<br />" +
				"		}<br />" +
				"		left = lastSwap + 1 ;<br />" +
				"	} while ( left <= r ) ;<br />" +
				"}" ;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US ;
	}

	@Override
	public String getDescription() {
		return "Cocktail sort, also known as bidirectional bubble sort, cocktail shaker sort, shaker sort (which can also refer to a variant of selection sort), ripple sort, shuttle sort or happy hour sort, is a variation of bubble sort that is both a stable sorting algorithm and a comparison sort. The algorithm differs from bubble sort in that sorts in both directions each pass through the list. This sorting algorithm is only marginally more difficult than bubble sort to implement, and solves the problem with so-called turtles in bubble sort.<br />" +
				"<cite>http://en.wikipedia.org/wiki/Shaker_sort</cite>" ;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType( GeneratorType.GENERATOR_TYPE_SORT ) ;
	}

	@Override
	public String getName() {
		return "ShakerSort" ;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		_language = new AnimalScript( "ShakerSort", "Florian Jakob <f_jakob@rbg.informatik.tu-darmstadt.de>", 640, 480 ) ;
		_language.setStepMode( true ) ;
	}

	private void setup( int[] a, AnimationPropertiesContainer props) {
		// title
		TextProperties titleProps = new TextProperties() ;
		titleProps.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font( "SansSerif", Font.BOLD, 28 ) ) ;
		_title = _language.newText( new Coordinates( 10, 20 ), "ShakerSort", "title", null, titleProps ) ;

		// array initialization
		_array = _language.newIntArray( new Offset( 5, 50, _title, "SW" ), a, "a", null, (ArrayProperties) props.getPropertiesByName( "arrayProperties" ) ) ;

		// source code
		_sourceCode = _language.newSourceCode( new Offset( 0, 0, _array, "SW" ), "sourcecode", null, (SourceCodeProperties) props.getPropertiesByName( "sourceProperties" ) ) ;
		_sourceCode.addCodeLine( "void shakerSort( int[] array ) {", "", 0, null ) ;
		_sourceCode.addCodeLine( "int left = 1 ;", "", 1, null ) ;
		_sourceCode.addCodeLine( "int right = array.length - 1 ;", "", 1, null ) ;
		_sourceCode.addCodeLine( "int lastSwap = left ;", "", 1, null ) ;
		_sourceCode.addCodeLine( "do {", "", 1, null ) ;
		_sourceCode.addCodeLine( "for ( int i = left ; i <= right ; i++ ) {", "", 2, null ) ;
		_sourceCode.addCodeLine( "if ( array[i] < array[i - 1] ) {", "", 3, null ) ;
		_sourceCode.addCodeLine( "swap( array, i, i - 1 ) ;", "", 4, null ) ;
		_sourceCode.addCodeLine( "lastSwap = i ;", "", 4, null ) ;
		_sourceCode.addCodeLine( "}", "", 3, null ) ;
		_sourceCode.addCodeLine( "}", "", 2, null ) ;
		_sourceCode.addCodeLine( "right = lastSwap - 1 ;", "", 2, null ) ;
		_sourceCode.addCodeLine( "for ( int i = right ; i >= left ; i-- ) {", "", 2, null ) ;
		_sourceCode.addCodeLine( "if ( array[i] < array[i - 1] ) {", "", 3, null ) ;
		_sourceCode.addCodeLine( "swap( array, i, i - 1 ) ;", "", 4, null ) ;
		_sourceCode.addCodeLine( "lastSwap = i ;", "", 4, null ) ;
		_sourceCode.addCodeLine( "}", "", 3, null ) ;
		_sourceCode.addCodeLine( "}", "", 2, null ) ;
		_sourceCode.addCodeLine( "left = lastSwap + 1 ;", "", 2, null ) ;
		_sourceCode.addCodeLine( "} while ( left <= r ) ;", "", 1, null ) ;
		_sourceCode.addCodeLine( "}", "", 0, null ) ;
		
		_language.nextStep() ;
	}

	public void visualSort( int[] a, AnimationPropertiesContainer props ) {
		setup(a, props) ;
		
		_sourceCode.highlight( 0 ) ;
		_language.nextStep() ;
		
		_sourceCode.toggleHighlight( 0, 1 ) ;
		_language.nextStep() ;
		int left = 1 ;
		
		_sourceCode.toggleHighlight( 1, 2 ) ;
		_language.nextStep() ;
		int right = _array.getLength() - 1 ;
		
		_sourceCode.toggleHighlight( 2, 3 ) ;
		_language.nextStep() ;
		int lastSwap = left ;
		
		
		_sourceCode.toggleHighlight( 3, 4 ) ;
		_language.nextStep() ;
		_sourceCode.unhighlight( 4 ) ;
		do {
			_array.highlightCell( left - 1, right, null, null ) ;
			_sourceCode.highlight( 5 ) ;
			_language.nextStep() ;
			_sourceCode.unhighlight( 5 ) ;
			for ( int i = left ; i <= right ; i++ ) {
				_array.highlightElem( i - 1, i, null, null ) ;
				_sourceCode.highlight( 6 ) ;
				_language.nextStep() ;
				_sourceCode.unhighlight( 6 ) ;
				if ( _array.getData( i ) < _array.getData( i - 1 ) ) {
					_sourceCode.highlight( 7 ) ;
					_array.swap( i, i - 1, null, new MsTiming( 500 ) ) ;
					_language.nextStep() ;
					_sourceCode.toggleHighlight( 7, 8 ) ;
					_language.nextStep() ;
					_sourceCode.unhighlight( 8 ) ;
					lastSwap = i ;
				}
				_sourceCode.highlight( 5 ) ;
				_array.unhighlightElem( 0, _array.getLength() - 1, null, null ) ;
				_language.nextStep() ;
				_sourceCode.unhighlight( 5 ) ;
			}
			
			_sourceCode.highlight( 11 ) ;
			_language.nextStep() ;
			right = lastSwap - 1 ;
			
			_sourceCode.toggleHighlight( 11, 12 ) ;
			_array.unhighlightCell( 0, _array.getLength() - 1, null, null ) ;
			_array.highlightCell( left - 1, right, null, null ) ;
			_language.nextStep() ;
			_sourceCode.unhighlight( 12 ) ;
			
			for ( int i = right ; i >= left ; i-- ) {
				_array.highlightElem( i - 1, i, null, null ) ;
				_sourceCode.highlight( 13 ) ;
				_language.nextStep() ;
				_sourceCode.unhighlight( 13 ) ;
				if ( _array.getData( i ) < _array.getData( i - 1 ) ) {
					_sourceCode.highlight( 14 ) ;
					_array.swap( i, i - 1, null, new MsTiming( 500 ) ) ;
					_language.nextStep() ;
					
					_sourceCode.toggleHighlight( 14, 15 ) ;
					_language.nextStep() ;
					lastSwap = i ;
					
					_sourceCode.unhighlight( 15 ) ;
				}
				_sourceCode.highlight( 12 ) ;
				_array.unhighlightElem( 0, _array.getLength() - 1, null, null ) ;
				_language.nextStep() ;
				_sourceCode.unhighlight( 12 ) ;
			}
			
			_sourceCode.highlight( 18 ) ;
			_language.nextStep() ;
			left = lastSwap + 1 ;
			
			_sourceCode.toggleHighlight( 18, 19 ) ;
			_language.nextStep() ;
			_sourceCode.unhighlight( 19 ) ;
			_array.unhighlightElem( 0, _array.getLength() - 1, null, null ) ;
			_array.unhighlightCell( 0, _array.getLength() - 1, null, null ) ;

		} while ( left <= right ) ;
		_array.unhighlightElem( 0, _array.getLength() - 1, null, null ) ;
	}
}
