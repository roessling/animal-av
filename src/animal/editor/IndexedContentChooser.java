package animal.editor;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.graphics.PTGraphicObject;
import animal.graphics.meta.IndexableContentContainer;

public class IndexedContentChooser implements ActionListener{
	
	class IndexableObjectInformation {
		/**
		 * An translateable identifier of the kind of Object
		 */
		protected String identifier;
		
		/**
		 * The translateable identifiers of the indexed dimensions
		 */
		private String[] dimensions;
		
		/**
		 * key: The translated Identifier
		 * value: The translateable names of the applieable methods.
		 */
		private Hashtable<String,String> methodIdentifiers;
		
		/**
		 * The Model which should be applied to the methodchoose combobox,
		 *  after the kind of Object changed.
		 */
		private DefaultComboBoxModel<String> methodModel;
		
		/**
		 * The Data of the primary dimension
		 */
		private DimensionData primaryDimension;
		
		
		//-------------------------------
		//Constructors
		//-------------------------------
//TODO Dimensionidentifeier uebersetzen und hier abfragbar machen
		public IndexableObjectInformation(String identifier){
			this.identifier = identifier;
			this.methodModel = new DefaultComboBoxModel<String>();
			this.methodIdentifiers = new Hashtable<String,String>();
			this.dimensions = new String[0];
		}
		//-------------------------------
		//Attribute accessing
		//-------------------------------
		public String getIdentifier() {
			return identifier;
		}

		public String[] getDimensions() {
			return dimensions;
		}

		public ComboBoxModel<String> getMethodModel() {
			return methodModel;
		}

		public DimensionData getPrimaryDimension() {
			return primaryDimension;
		}
		
		public int getDimensionCount(){
			if(this.dimensions == null)
				return 0;
			else return this.dimensions.length;
		}
		
		public String getIdentifierOfSelectedMethod(){
			if(this.methodModel != null && this.methodModel.getSelectedItem() != null)			
				return this.methodIdentifiers.get(this.methodModel.getSelectedItem());
			return null;
		}

		//-------------------------------
		//Attribute setting
		//-------------------------------

		public void setMethodIdentifiers(String[] methodIdentifiers) {
			this.methodModel.removeAllElements();
			this.methodIdentifiers.clear();
			String[] translatedMethods = getTranslatedStrings(methodIdentifiers);
			for(int i = 0;i < translatedMethods.length;++i){
				this.methodModel.addElement(translatedMethods[i]);
				this.methodIdentifiers.put(translatedMethods[i],
						methodIdentifiers[i]);
			}
		}

		public void setPrimaryDimension(DimensionData primaryDimension) {
			this.primaryDimension = primaryDimension;
			Vector<String> dimensionIdentifiers = new Vector<String>(this.getDimensionCount());
			primaryDimension.getDimensionIdentifiers(dimensionIdentifiers);
			this.dimensions = dimensionIdentifiers.toArray(this.dimensions);
		}

		//-------------------------------
		//internal methods
		//-------------------------------
		private String[] getTranslatedStrings(String[] values) {
			String[] ret = new String[values.length];
			for(int i = 0;i < ret.length;++i){
				ret[i] = AnimalTranslator.translateMessage(values[i]);
			}
			return ret;
		}
	}
	
	class DimensionData{
		
		public static final String DIMENSION_INDEX_ALL = "all";
		/**
		 * An translateable identifier of the dimension
		 */
		private String identifier;
		
		/**
		 * The translated Version of the identifier of the dimension
		 */
		private String translatedIdentifier;
		
		/**
		 * The Data of the subdimension
		 */
		private DimensionData[] subDimensionData;
		
		/**
		 * The Model which should be applied to the dimensionchoose combobox,
		 *  after the super dimension index changed.
		 */
		private DefaultComboBoxModel<String> dimensionModel;
		
		private int indexCount = 0;
		
		//-------------------------------
		//Constructor
		//-------------------------------
		
		public DimensionData(String identifier, String translatedIdentifier){
			this.identifier = identifier;
			this.translatedIdentifier = translatedIdentifier;
			this.dimensionModel = new DefaultComboBoxModel<String>();
		}
		
		//-------------------------------
		//Attribute accessing
		//-------------------------------
		
		public int getIndexCount(){
			return this.indexCount;
		}

		public void getDimensionIdentifiers(Vector<String> methodIdentifiers) {
			methodIdentifiers.add(this.translatedIdentifier);
			if( this.subDimensionData != null && this.subDimensionData.length > 0){
				this.subDimensionData[0].getDimensionIdentifiers(methodIdentifiers);
			}
		}
		//TO DO translatedIdentifier uebers Object beziehen, da nicht klar ist, fuer welche Indeices hier die tiefste Dimension erreicht wird
		public void getTranslatedDimensionIdentifiers(Vector<String> methodIdentifiers) {
			methodIdentifiers.add(this.translatedIdentifier);
			if( this.subDimensionData != null && this.subDimensionData.length > 0){
				this.subDimensionData[0].getDimensionIdentifiers(methodIdentifiers);
			}
		}
		
		public DimensionData getDimensionDataAt(int index){
			if(this.subDimensionData != null &&
					index >= 0 && index < this.subDimensionData.length)
				return this.subDimensionData[index];
			return null;
		}
		
		public DimensionData[] getDimensionData(){			
			return this.subDimensionData;
		}
		
		public ComboBoxModel<String> getComboBoxModel(int depth,Vector<Integer> indices){
			//one needs n-1 indices to reach dimension n
			if(depth < indices.size()+1)
				return getDimensionComboBoxModel(depth, 0, indices);
			return null;
		}
		private ComboBoxModel<String> getDimensionComboBoxModel(int depth, int actualDepth,
				Vector<Integer> indices) {
		  int theDepth = actualDepth;
			if(depth == theDepth)
				return this.dimensionModel;
			if( this.subDimensionData != null && this.subDimensionData.length > 0){
				if(indices.get(theDepth) == -1 ){					
					return this.subDimensionData[0].getIntersectedComboBoxModel(depth,++theDepth,indices,this.subDimensionData);
				}else
					if(indices.get(actualDepth)>=0 && indices.get(theDepth) < this.getIndexCount())
						return this.subDimensionData[indices.get(theDepth)].getDimensionComboBoxModel(depth, ++theDepth, indices);
			}
			return null;
		}

		private ComboBoxModel<String> getIntersectedComboBoxModel(int depth, int actualDepth,
				Vector<Integer> indices, DimensionData[] neighbors) {
			if(depth == actualDepth)
				return this.getIntersectedComboBoxModel(neighbors);
			if( this.subDimensionData != null && this.subDimensionData.length > 0){
				if(indices.get(actualDepth) == -1 ){					
					return this.subDimensionData[0].getIntersectedComboBoxModel(
							depth,actualDepth+1,indices,getSubdimensionNeighbors(neighbors,indices.get(actualDepth)));
				}else
					if(indices.get(actualDepth)>=0 && indices.get(actualDepth) < this.getIndexCount())
						return this.subDimensionData[indices.get(actualDepth)].getIntersectedComboBoxModel(
								depth,actualDepth+1,indices,getSubdimensionNeighbors(neighbors,indices.get(actualDepth)));
			}
			return null;
		}

		private DimensionData[] getSubdimensionNeighbors(DimensionData[] neighbors, int index) {
			Vector<DimensionData> returnValues = new Vector<DimensionData>();
			//all neighbors
			if(index == -1)
				for(int i = 0; i < neighbors.length;++i){
					if(neighbors[i].getDimensionData() != null){
						DimensionData[] tmpDimensionData = neighbors[i].getDimensionData();
						for(int j= 0; j < tmpDimensionData.length; ++j)
							returnValues.add(tmpDimensionData[j]);
					}
				}
			//indexed neighbors
			else
				for(int i = 0; i < neighbors.length;++i){
					if(neighbors[i].getDimensionDataAt(index) != null){
							returnValues.add(neighbors[i].getDimensionDataAt(index));
					}
				}
			DimensionData[] result = new DimensionData[returnValues.size()];
			int pos = 0;
			for (DimensionData data : returnValues)
			  result[pos++] = data;
//			DimensionData[] result = (DimensionData[])returnValues.toArray();
			return result;
		}

		private ComboBoxModel<String> getIntersectedComboBoxModel(DimensionData[] neighbors) {
			//Strings des ersten Models auslesen
			TreeSet<String> values = new TreeSet<String>();
			values.addAll(this.getSelectableStrings());
			//Mit allen anderen Modellen vergleichen und nur die behalten, die auch in den anderen Modellen sind
			for(int i = 1;i < neighbors.length;++i){
				values.retainAll(neighbors[i].getSelectableStrings());
			}
			//neues Model erstellen
			String[] res = new String[values.size()];
			int pos = 0;
			for (String entry: values)
			  res[pos++] = entry;
      return new DefaultComboBoxModel<String>(res);
//			return new DefaultComboBoxModel<String>(values.toArray());
		}

		private Set<String> getSelectableStrings() {
			Set<String> ret = new HashSet<String>();
			if(this.dimensionModel != null)
				for(int i = 0;i < this.dimensionModel.getSize();++i)
					ret.add(this.dimensionModel.getElementAt(i).toString());
			return ret;
		}
		
		public String getIdentifier(){
			return this.identifier;
		}

		//-------------------------------
		//Attribute setting
		//-------------------------------
		public void setIndexCount(int value){
			this.indexCount = value;
			this.dimensionModel.removeAllElements();
			for(int i = 0; i< this.getIndexCount();++i){
				this.dimensionModel.addElement(String.valueOf(i));
			}
			this.dimensionModel.addElement(AnimalTranslator.getTranslator().translateMessage(DIMENSION_INDEX_ALL));
			this.dimensionModel.setSelectedItem(this.dimensionModel.getElementAt(0));
		}
		
		public void setSubDimensions(DimensionData[] subDimensions){
			this.subDimensionData = subDimensions;			
		}
		
		public void disableIndexEntries(Vector<Integer> indices){
			if(indices != null && indices.size()>0){
				deleteIndices(0,indices);				
			}
		}
		
		private int deleteIndices(int i, Vector<Integer> indices) {				
				if(indices.get(i) == -1){
					for(int j = 0; j < this.getIndexCount();++j){
						//If either this is a leaf of the dimensionDataTree or the subdimension of
						//this at index j has no further index to choose in its own list, delete index j
						if((this.subDimensionData == null || this.subDimensionData.length < 1)||
								this.subDimensionData[j].deleteIndices(i+1, indices) < 1 )
							deleteIndex(j);
					}
				}else if((indices.get(i)>=0 && indices.get(i) < this.getIndexCount()) &&
						((this.subDimensionData == null || this.subDimensionData.length < 1)||
						this.subDimensionData[indices.get(i)].deleteIndices(i+1, indices) < 1))
					deleteIndex(indices.get(i));
			
			//dimensionModel should have one more entry than this.getIndexCount(). this entry is "all"
			//if all indices are still selectable
			if(this.dimensionModel.getSize() <= this.getIndexCount())
				deleteIndexAll();
			return this.dimensionModel.getSize();
		}

		private void deleteIndexAll() {
			if(((String)this.dimensionModel.getElementAt(this.dimensionModel.getSize()-1)).
					equals(AnimalTranslator.getTranslator().translateMessage(DIMENSION_INDEX_ALL)))
				this.dimensionModel.removeElementAt(this.dimensionModel.getSize()-1);			
		}

		private void deleteIndex(int i) {
			String searchString = String.valueOf(i);
			String toDelete = null;
			for(int j = 0; j < this.dimensionModel.getSize();++j)
				if(searchString.equals((String) this.dimensionModel.getElementAt(j)))
					toDelete = (String) this.dimensionModel.getElementAt(j);
			if(toDelete != null)
				this.dimensionModel.removeElement(toDelete);			
		}

		//indices geben den weg durch den Baum vor. Wenn eine untere Stufe meldet, dass kein Blatt/Ast mehr da ist, dann wird der EIntrag auch aus dieser Stufe entfernt
		public void enableIndexEntries(Vector<Integer> indices){
			if(indices != null && indices.size()>0){
				addIndices(0,indices);				
			}
		}

		private void addIndices(int i, Vector<Integer> indices) {
			if(indices.get(i)>=-1 && indices.get(i) < this.getIndexCount())
				if(indices.get(i) == -1){
					for(int j = 0; j < this.getIndexCount();++j){
						if( this.subDimensionData != null && this.subDimensionData.length >= this.getIndexCount())
							this.subDimensionData[j].addIndices(i+1, indices);
						addIndex(j);
					}
				}else{
					if((indices.get(i)>=0 && indices.get(i) < this.getIndexCount())&&
							this.subDimensionData != null && this.subDimensionData.length > indices.get(i))
						this.subDimensionData[indices.get(i)].addIndices(i+1, indices);
					addIndex(indices.get(i));
				}
			//dimensionModel should have one more entry than this.getIndexCount(). this entry is "all"
			//if all indices a still selectable
			if(this.dimensionModel.getSize() == this.getIndexCount())
				addIndexAll();
			
		}

		private void addIndexAll() {
			if(((String)this.dimensionModel.getElementAt(this.dimensionModel.getSize()-1)).
					equals(AnimalTranslator.getTranslator().translateMessage(DIMENSION_INDEX_ALL)))
				//Something went wrong and all should not be there so delete it
				this.dimensionModel.removeElementAt(this.dimensionModel.getSize()-1);	
			else
				this.dimensionModel.addElement(AnimalTranslator.getTranslator().translateMessage(DIMENSION_INDEX_ALL));
		}

		private void addIndex(int j) {
//			if(this.dimensionModel.getIndexOf(String.valueOf(j))>0){
				int index = this.dimensionModel.getSize();
				for(int i = 0; i < this.dimensionModel.getSize();++i)
					if(Integer.valueOf((String) this.dimensionModel.getElementAt(i))>=j){
						index = i;
						break;
					}
				//Dont insert Values twice
				if(index == this.dimensionModel.getSize())
					this.dimensionModel.addElement(String.valueOf(j));
				else if(this.dimensionModel.getSize() < 1 ||!this.dimensionModel.getElementAt(index).toString().equals(String.valueOf(j)))
						this.dimensionModel.insertElementAt(String.valueOf(j), index);		
			}
	//	}
	}

	private PTGraphicObject[] objects;
	
	public static final String INDEXED_CONTENT_CHOOSER_KIND_OF_OBJECT_LABEL = "kindOfObjectLabel";
	
	public static final String INDEXED_CONTENT_CHOOSER_METHOD_LABEL = "AnimatorEditor.methodLabel";
	
	public static final String INDEXED_CONTENT_CHOOSER_NO_METHOD_ENTRY = "AnimatorEditor.noAppropriateMethod";
	
	private Box contentBox;
	
	private Box indexBox;
	
	private boolean showOnlyObjectsWithMethods = false;
	
	protected JComboBox<String> kindOfObjectCB, methodCB;
	
	protected Vector<JComboBox<String>> dimensionCBs;
	
	private Vector<JLabel> dimensionLabels;
	
	protected Hashtable<String,IndexableObjectInformation> indexableObjects;
	
	private Hashtable<String,String> translationOfIndexableObjects;
	
	/*
	 * Contructor:
	 */
	public IndexedContentChooser(String boxLabel){
		
		TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
		contentBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS, boxLabel);
		//Erstelle kindOfObject Box
		Box kindOfObjectBox = new Box(BoxLayout.LINE_AXIS);
		kindOfObjectBox.add(generator.generateJLabel(INDEXED_CONTENT_CHOOSER_KIND_OF_OBJECT_LABEL));
		kindOfObjectCB = new JComboBox<String>();
		kindOfObjectCB.addActionListener(this);
		kindOfObjectBox.add(kindOfObjectCB);
		contentBox.add(kindOfObjectBox);
		//Erstelle MethodBox
		Box methodBox = new Box(BoxLayout.LINE_AXIS);
		methodBox.add(generator.generateJLabel(INDEXED_CONTENT_CHOOSER_METHOD_LABEL));
		methodCB = new JComboBox<String>();
		methodCB.addActionListener(this);
		methodBox.add(methodCB);
		contentBox.add(methodBox);
		
		indexBox= new Box(BoxLayout.PAGE_AXIS);
		contentBox.add(indexBox);
		
		indexableObjects = new Hashtable<String,IndexableObjectInformation>();
		translationOfIndexableObjects = new Hashtable<String,String>();

		dimensionCBs = new Vector<JComboBox<String>>();
		dimensionLabels = new Vector<JLabel>();
	}
	
	public IndexedContentChooser(Container cp, String boxLabel)  {
	  TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    contentBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS, boxLabel);

    kindOfObjectCB = new JComboBox<String>();
    kindOfObjectCB.addActionListener(this);
    
    cp.add(generator.generateJLabel(INDEXED_CONTENT_CHOOSER_KIND_OF_OBJECT_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    kindOfObjectCB = new JComboBox<String>();
    kindOfObjectCB.addActionListener(this);
    cp.add(kindOfObjectCB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
   // Box methodBox = new Box(BoxLayout.LINE_AXIS);
    
    cp.add(generator.generateJLabel(INDEXED_CONTENT_CHOOSER_METHOD_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
 //   methodBox.add(generator.generateJLabel(INDEXED_CONTENT_CHOOSER_METHOD_LABEL));
    methodCB = new JComboBox<String>();
    methodCB.addActionListener(this);
    
    cp.add(methodCB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
  //  methodBox.add(methodCB);
    
 //   cp.add(methodBox);
    
  //  contentBox.add(methodBox);
    
    indexBox = new Box(BoxLayout.PAGE_AXIS);
    //contentBox.add(indexBox);
    
    //cp.add(contentBox, Editor.LAYOUT_PARAGRAPH_GAP);
    cp.add(indexBox, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    indexableObjects = new Hashtable<String,IndexableObjectInformation>();
    translationOfIndexableObjects = new Hashtable<String,String>();

    dimensionCBs = new Vector<JComboBox<String>>();
    dimensionLabels = new Vector<JLabel>();
	}
	
	public void setData(PTGraphicObject[] objects,Vector<String> methods){
		this.setObjects(objects);
		this.setMethods(methods);		
	}
	
	protected void setObjects(PTGraphicObject[] objects){
		if(objects != this.objects){
			this.objects = objects;
			updateKindOfObjectData();
		}
	}

	private void updateKindOfObjectData() {
		//Gibt es Objekte und sind alle IndexedContentContainer
		if((objects != null)&&(objects.length > 0 && allObjectsAreIndexableContentContainer())){			
			//erstelle Schnittmenge der indexierbaren Objekte
			createSetOfIndexableObjects();
			//erstelle Schnittmenge der Indexinformationen
			createIndexTrees();
			//setze Anzeige (erstelle Comboboxmodel)
			initializeKindOfObjectBoxModel();			
		}else{
			this.kindOfObjectCB.setModel(new DefaultComboBoxModel<String>());
		}
	}
	
	private void initializeKindOfObjectBoxModel() {
		DefaultComboBoxModel<String> kindOfObjectModel = new DefaultComboBoxModel<String>();
		IndexableObjectInformation tmpInfo = null;
		String identifierTranslation;
		for(Iterator<IndexableObjectInformation> it = this.indexableObjects.values().iterator();
			it.hasNext();){
			tmpInfo = it.next();			
			if(!showOnlyObjectsWithMethods || (tmpInfo.getMethodModel() != null &&
					tmpInfo.getMethodModel().getSize() > 0)){
				identifierTranslation = AnimalTranslator.translateMessage(tmpInfo.getIdentifier());
				this.translationOfIndexableObjects.put(identifierTranslation, tmpInfo.getIdentifier());
				kindOfObjectModel.addElement(identifierTranslation);
			}
		}
		this.kindOfObjectCB.setModel(kindOfObjectModel);
		//ueberpruefen, ob es ueberhaupt einen Eintrag gibt
		if(this.kindOfObjectCB.getModel().getSize() > 0)
			//den ersten vorhandenen Eintrag setzen				
			this.kindOfObjectCB.setSelectedIndex(0);
	}

	private void createIndexTrees() {
		//ueber alle Objekte Iterieren
		IndexableObjectInformation tmpInfo = null;
		for(Iterator<IndexableObjectInformation> it = this.indexableObjects.values().iterator();
			it.hasNext();){
			tmpInfo = it.next();
			//Fuer jedes Objekte die Indexsets erstellen
			createIndexInformation(tmpInfo);
		}		
	}

	private void createIndexInformation(IndexableObjectInformation tmpInfo) {
		//ueberpruefen, dass alle Objekte fuer die kindOfObject die gleiche Anzahl Dimensionen haben
		boolean equalAmountOfDimensions = true;
		String[] dimensionIdentifiers = ((IndexableContentContainer) objects[0]).
			getDimensionIdentifiers(tmpInfo.getIdentifier());
		for(int i = 1;i < objects.length;++i){
			if(dimensionIdentifiers.length != ((IndexableContentContainer) objects[i]).
			getDimensionIdentifiers(tmpInfo.getIdentifier()).length)
				equalAmountOfDimensions = false;
		}
		//If there is a different Amount of Dimensions for kindOfObject == tmpInfo.getIdentifier()
		//delete kindOfObject
		if(!equalAmountOfDimensions)
			this.indexableObjects.remove(tmpInfo.getIdentifier());
		else{
			//erstes DimensionData Objekt
			Vector<Integer> indices = new Vector<Integer>(dimensionIdentifiers.length);
			DimensionData root = new DimensionData(dimensionIdentifiers[0],
					AnimalTranslator.translateMessage(dimensionIdentifiers[0]));
			createDimensionDataTree(root,dimensionIdentifiers,0,indices,tmpInfo.getIdentifier());
			tmpInfo.setPrimaryDimension(root);
		}
		
	}
/**
 * indices muss die L&auml;nge dimensionIdentifiers.length haben
 * @param root
 * @param dimensionIdentifiers
 * @param depth
 * @param indices
 * @param kindOfObject
 */
	
	private void createDimensionDataTree(DimensionData root,
			String[] dimensionIdentifiers, int depth, Vector<Integer> indices,String kindOfObject) {
		//set max Index for root
		int dimensionLength = getDimensionLengthIntersection(kindOfObject,indices,root.getIdentifier());
		root.setIndexCount(dimensionLength);
		int theDepth = depth + 1;
		//create DataStructures for roots subdimension
		if (theDepth < dimensionIdentifiers.length){
			DimensionData tmpData;
			DimensionData[] subDimensionDataObjects = new DimensionData[dimensionLength];
			String translatedIdentifier = AnimalTranslator.translateMessage(dimensionIdentifiers[theDepth]);
			for(int i = 0; i < dimensionLength;++i){
				tmpData = new DimensionData(dimensionIdentifiers[theDepth],translatedIdentifier);
				subDimensionDataObjects[i]=(tmpData);
				indices.add(i);
				createDimensionDataTree(tmpData,dimensionIdentifiers,theDepth,indices,kindOfObject);
				indices.remove(indices.size()-1);
			}
			root.setSubDimensions(subDimensionDataObjects);
		}		
	}
/**
 * intersection of the Count of Indices for the dimension of all IndexableContentContainers
 * @param kindOfObject
 * @param indices
 * @param dimensionIdentifier
 * @return
 */
	private int getDimensionLengthIntersection(String kindOfObject,
			Vector<Integer> indices, String dimensionIdentifier) {
		int length = ((IndexableContentContainer) objects[0]).
		getDimensionLength(kindOfObject, dimensionIdentifier, indices);
//		get the smallest length
		for(int i = 1; i< objects.length;++i){
			if(((IndexableContentContainer) objects[i]).
					getDimensionLength(kindOfObject, dimensionIdentifier, indices) < length)
				length = ((IndexableContentContainer) objects[i]).
				getDimensionLength(kindOfObject, dimensionIdentifier, indices);
		}
		return length;
	}
	
	private void createSetOfIndexableObjects() {
		//Map erstellen
		IndexableContentContainer tmpObject = (IndexableContentContainer) objects[0];
		//fuer das erste Objekt die kindsOfObject auslesen
		String[] tmpKindsOfObject = tmpObject.getKindsOfObjects();
		//fuer die restlichen Objekte den Schnitt zur bisherigen Menge bilden
		for(int i = 1;i < objects.length;++i){
			tmpKindsOfObject = intersect(tmpKindsOfObject,((IndexableContentContainer) objects[i]).
					getKindsOfObjects());
		}
		//fuer jede uebriggebliebenen Objektart die Datenstrukturen erstellen	
		indexableObjects.clear();
		for(int i = 0; i< tmpKindsOfObject.length;++i){
			indexableObjects.put(tmpKindsOfObject[i], new IndexableObjectInformation(tmpKindsOfObject[i]));
		}
	}

	public String[] intersect(String[] a, String[] b) {
		boolean found;
		Vector<String> tmpValues = new Vector<String>();
		for(int i = 0;i < a.length;++i){
			found = false;
			for(int j = 0;j < b.length;++j){
				if(b[j].equals(a[i]))
					found = true;
			}
			if(found)
				tmpValues.add(a[i]);
		}
		String[] ret = new String[tmpValues.size()];
		return tmpValues.toArray(ret);		
	}

	private boolean allObjectsAreIndexableContentContainer() {
		for(int i = 0; i<objects.length;++i){
			if(!(objects[i] instanceof IndexableContentContainer))
				return false;
		}
		return true;
	}

	protected void setMethods(Vector<String> methods){
		//Erstelle fuer verschiedene Arten von Objekten die Methodenmenge
		Hashtable<String,HashSet<String>> objectMethods = new Hashtable<String,HashSet<String>>();
		String [] tokens;
		String methodIdentifier;
		for(int i = 0; i < methods.size();++i){
			tokens = methods.get(i).split("\\s");
			if((tokens.length > 1) && this.indexableObjects.containsKey(tokens[0])){
				//recreate method Identifier by inserting tokens
				methodIdentifier = "";
				for(int j = 1; j < tokens.length;++j){
					methodIdentifier += tokens[j];
					if(j < tokens.length-1)
						methodIdentifier += " ";
				}
				if(objectMethods.containsKey(tokens[0])){					
					objectMethods.get(tokens[0]).add(methodIdentifier);
				}else{
					HashSet<String> tmpSet = new HashSet<String>();
					tmpSet.add(methodIdentifier);
					objectMethods.put(tokens[0], tmpSet);
				}
			}
		}
		//uebergebe jeder Objektart die Methodenidentifizierer
		String objectIdentifier;
		String[] tmpArray;
		for(Iterator<String> it = this.indexableObjects.keySet().iterator();it.hasNext();){
			objectIdentifier = it.next();
			if(objectMethods.containsKey(objectIdentifier)){
				tmpArray = new String[objectMethods.get(objectIdentifier).size()];
				this.indexableObjects.get(objectIdentifier).setMethodIdentifiers(objectMethods.get(objectIdentifier).toArray(tmpArray));				
			}
		}
		if(this.showOnlyObjectsWithMethods)
			initializeKindOfObjectBoxModel();
		setObjectWithMethods();
		//Set methodCB for chosen object
		if(this.kindOfObjectCB != null && this.kindOfObjectCB.getModel() != null &&
				this.kindOfObjectCB.getModel().getSelectedItem() != null){
			String selectedMethod = this.getSelectedMethod();
			setMethodsBox(this.getObjectInformation(
					(String)this.kindOfObjectCB.getModel().getSelectedItem()).
					getIdentifier());			
			if(selectedMethod != null)
				methodCB.setSelectedItem(selectedMethod);
		}
	}

	/**
	 * sets chosen Value of kindOfObjectBox to an object for which applieable methods exist
	 * If there are methods for the actual chosen object, this object stays selected.
	 */
	private void setObjectWithMethods() {
		if(this.kindOfObjectCB != null && this.kindOfObjectCB.getModel() != null){
		//check methods for already selected object
			String selectedObject = this.getSelectedKindOfObject();
			if(selectedObject == null || this.indexableObjects.get(selectedObject).getMethodModel() == null ||
					this.indexableObjects.get(selectedObject).getMethodModel().getSize() == 0){
			//selected object has no methods, choose another kind of object
				for(IndexableObjectInformation info : indexableObjects.values()){
					if(info.getMethodModel() != null && info.getMethodModel().getSize() > 0){
						this.kindOfObjectCB.setSelectedItem(AnimalTranslator.translateMessage(info.identifier));
						break;
					}					
				}
			}
				
		}
	}

	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == kindOfObjectCB ) {
			setMethodsBox(getObjectInformation((String)kindOfObjectCB.getSelectedItem()).getIdentifier());
		}else if(e.getSource() == methodCB){
			setIndexBoxes((String)kindOfObjectCB.getSelectedItem(),
					this.getSelectedMethod());
		}else{
			handleIndexChange(e);
		}		
	}

	private void setIndexBoxes(String selectedObject, String selectedMethod) {
		IndexableObjectInformation tmpInfo = this.getObjectInformation(selectedObject); 
		int dimensionCount = tmpInfo.getDimensionCount();
		if(this.dimensionCBs == null){
			this.dimensionCBs = new Vector<JComboBox<String>>();			
		}
		if(this.dimensionLabels == null)
			this.dimensionLabels = new Vector<JLabel>();
		JComboBox<String> tmpCB;
		int toAdd = dimensionCount-this.dimensionCBs.size();
		for(int i = 0; i < toAdd;++i){
			tmpCB = new JComboBox<String>();
			tmpCB.addActionListener(this);
			this.dimensionCBs.add(tmpCB);
		}
		toAdd = dimensionCount-this.dimensionLabels.size();
		for(int i = 0; i < toAdd;++i)
			this.dimensionLabels.add(new JLabel());
		Vector<String> translatedDimensionIdentifiers = new Vector<String>();
		this.getObjectInformation(selectedObject).
			getPrimaryDimension().getTranslatedDimensionIdentifiers(translatedDimensionIdentifiers);
		//fehlende Label und Boxen hinzufuegen
		Box tmpBox;
		for(int i = this.indexBox.getComponentCount(); i < this.dimensionCBs.size();++i){
			tmpBox = new Box(BoxLayout.LINE_AXIS);
			tmpBox.add(this.dimensionLabels.get(i));
			tmpBox.add(this.dimensionCBs.get(i));
			this.indexBox.add(tmpBox);
		}
		//Labels und Boxen sichtbar machen und fuer erste Dimension index setzen
		for(int i = 0 ; i < dimensionCount;++i){
			this.dimensionLabels.get(i).setVisible(true);
			this.dimensionLabels.get(i).setText(translatedDimensionIdentifiers.get(i));
			this.dimensionCBs.get(i).setVisible(true);			
		}
		for(int i = dimensionCount ; i < this.dimensionCBs.size();++i){
			this.dimensionLabels.get(i).setVisible(false);
			this.dimensionCBs.get(i).setVisible(false);
		}
		if(this.dimensionCBs.size() > 0 && this.dimensionCBs.get(0) != null){
			this.dimensionCBs.get(0).setModel(tmpInfo.getPrimaryDimension().
					getComboBoxModel(0, new Vector<Integer>()));
			if(this.dimensionCBs.get(0).getModel().getSize() > 0)
				this.dimensionCBs.get(0).setSelectedIndex(0);
		}						
	}

	protected IndexableObjectInformation getObjectInformation(String selectedObject) {
			return this.indexableObjects.get(
					this.translationOfIndexableObjects.get(selectedObject));
	}

	private void setMethodsBox(String selectedItem) {
		this.methodCB.removeActionListener(this);
		this.methodCB.setModel(this.indexableObjects.get(selectedItem).getMethodModel());
		this.methodCB.addActionListener(this);
		if(this.methodCB.getModel() != null && this.methodCB.getModel().getSize() > 0)
			this.methodCB.setSelectedIndex(0);
		else{
			this.methodCB.removeActionListener(this);
			String noMethod = AnimalTranslator.translateMessage(INDEXED_CONTENT_CHOOSER_NO_METHOD_ENTRY);
			String[] methodEntries = new String[1];
			methodEntries[0] = noMethod;
			this.methodCB.setModel(new DefaultComboBoxModel<String>(methodEntries));
			hideIndexBoxes();
		}
	}

	private void hideIndexBoxes() {
		if(this.dimensionCBs != null)
		for(int i = 0; i < this.dimensionCBs.size();++i){			
			this.dimensionCBs.get(i).setVisible(false);
		}
		if(this.dimensionLabels != null)
			for(int i = 0; i < this.dimensionLabels.size();++i){			
				this.dimensionLabels.get(i).setVisible(false);
			}		
	}

	private void handleIndexChange(ActionEvent e) {
		Vector<Integer> chosenIndices = new Vector<Integer>();
		int i =0;
		for(; i < dimensionCBs.size();++i){
			if(this.dimensionCBs.get(i).getModel().getSize() > 0 &&
					this.dimensionCBs.get(i).getSelectedIndex() > -1)
				chosenIndices.add(getSelectedIntValue(i));
			if(dimensionCBs.get(i) == e.getSource())	{
				i++;
				break;
			}
		}		
		//mit den gewaehlten Indices die Combobox aktualisieren
		//nur, wenn alle vorigen Boxen selektierte Werte enthielten
		if(chosenIndices.size() == i)
			updateDimensionBoxModels(chosenIndices);
		else
			clearBoxes(i);
	}

	private void clearBoxes(int i) {
	  int nr = i;
		for(; nr < dimensionCBs.size();++nr)
			dimensionCBs.get(nr).setModel(new DefaultComboBoxModel<String>());		
	}

	protected void updateDimensionBoxModels(Vector<Integer> chosenIndices) {
		int i = chosenIndices.size();
		String selectedValue;
		IndexableObjectInformation tmpInfo = this.getObjectInformation((String)this.kindOfObjectCB.getSelectedItem());
		int indexChangeIndex = i;
		for(;i < tmpInfo.getDimensionCount();++i){
			this.dimensionCBs.get(i).removeActionListener(this);
			selectedValue =(String)this.dimensionCBs.get(i).getSelectedItem();
			//Model holen
			this.dimensionCBs.get(i).setModel(tmpInfo.getPrimaryDimension().getComboBoxModel(i, chosenIndices));
			//SelectedValue setzen
			if(selectedValue != null)
				this.dimensionCBs.get(i).setSelectedItem(selectedValue);
			else if(this.dimensionCBs.get(i).getModel().getSize() > 0)
				this.dimensionCBs.get(i).setSelectedIndex(0);
			else
				//Abbruch, wenn das aktuelle Model keinen Wert enthaelt
				i = tmpInfo.getDimensionCount();
			//Fuer die Subdimension den gerade gesetzten Wert merken
			chosenIndices.add(getSelectedIntValue(i));				
			
		}
		for(;indexChangeIndex < tmpInfo.getDimensionCount();++indexChangeIndex)
			this.dimensionCBs.get(indexChangeIndex).addActionListener(this);
		
	}

	protected Integer getSelectedIntValue(int i) {
		if(this.dimensionCBs != null && i>= 0 && i < this.dimensionCBs.size() &&
				this.dimensionCBs.get(i).getModel().getSize() > 0){
			String selectedString = (String)this.dimensionCBs.get(i).getSelectedItem();			
			try{
				return Integer.parseInt(selectedString);
			}catch(NumberFormatException e){
				if(((String)this.dimensionCBs.get(i).getSelectedItem()).equals(AnimalTranslator.translateMessage(DimensionData.DIMENSION_INDEX_ALL)))
					return -1;
			}
		}			
		return null;
	}
	
	public Box getContentBox(){
		return this.contentBox;
	}
	
	public String getSelectedKindOfObject(){
		if(this.kindOfObjectCB.getSelectedItem() != null)
			return this.getObjectInformation(this.kindOfObjectCB.getSelectedItem().toString()).getIdentifier();
		return null;
	}
	
	public String getSelectedMethod(){
		if(this.kindOfObjectCB != null && this.kindOfObjectCB.getSelectedItem() != null){
			IndexableObjectInformation tmpInfo = this.getObjectInformation(this.kindOfObjectCB.getSelectedItem().toString());
			//TO sDO regeln niederschreiben, wie kindofObject identifier und methodidentifier aufgebaut sein muessen
			if(tmpInfo != null)
				return tmpInfo.getIdentifier()+ " " + tmpInfo.getIdentifierOfSelectedMethod();
		}
		return "";
	}
	
	public Vector<Integer> getSelectedIndices(){
		Vector<Integer> returnValue = new Vector<Integer>();
		if(this.kindOfObjectCB.getSelectedItem() != null){
			Integer tmp;
			for(int i = 0;i < this.getObjectInformation(this.kindOfObjectCB.getSelectedItem().toString()).getDimensionCount();++i){
				tmp = getSelectedIntValue(i);
				if(null != tmp)
					returnValue.add(tmp);
				else
					return new Vector<Integer>();
			}
		}
		return returnValue;
	}
		
	public void updateIndexSets(){
		Vector<Integer> selectedIndices = this.getSelectedIndices();
		this.createIndexTrees();		
		setIndexBoxes((String)kindOfObjectCB.getSelectedItem(),
				this.getSelectedMethod());
		for(int i = 0; i < selectedIndices.size();++i){
			if(selectedIndices.get(i)!= -1)
				this.dimensionCBs.get(i).setSelectedItem(String.valueOf(selectedIndices.get(i)));
			else
				this.dimensionCBs.get(i).setSelectedItem(AnimalTranslator.
						translateMessage(DimensionData.DIMENSION_INDEX_ALL));
		}
	}

	/**
	 * Set state that kindOfObjectCB only shows objects for which applieable methods exist
	 * State must be changed, before objects and methods are set.
	 * A change in state after the setting wont change the data model.
	 * @param value
	 */
	public void setShowOnlyObjectsWithMethods(boolean value) {
		this.showOnlyObjectsWithMethods = value;
	}
}
