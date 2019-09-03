package generators.framework.properties;

import generators.framework.PropertiesGUI;
import generators.framework.components.ColorChooserComboBox;
import generators.framework.properties.tree.PropertiesTreeNode;
import gfgaa.gui.GraphScriptPanel;

import java.awt.Color;
import java.awt.Font;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import algoanim.primitives.Graph;
import algoanim.properties.AnimationProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.Visitor;
import algoanim.properties.items.AnimationPropertyItem;
import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.DoublePropertyItem;
import algoanim.properties.items.EnumerationPropertyItem;
import algoanim.properties.items.FontPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.items.StringPropertyItem;


/**
 * This class is a Visitor that is used to load objects from XML. It can load
 * AnimationPropertyItems (The XML DOM Node nodeStart is used therefore) and it
 * can also load a whole AnimationProperties Object from an XML DOM Node. This
 * Visitor is used by the AnimationPropertiesContainer to deserialize itself.
 * 
 * @author T. Ackermann
 */
public class LoadFromXMLVisitor implements Visitor {

	/** objValue stores the value that was loaded from XML. */
	Object objValue;

	/** nodeStart stores the Start-Node to load from XML. */
	Node nodeStart;

	// *****************************************************************************
	// *** ONLY THE TWO FOLLOWING MAIN METHODS ARE USED FROM OUTSIDE
	// *****************************************************************************

	/**
	 * loadPropsFromXML loads all the values for a given
	 * AnimationProperties-Object by a given Node in the XML DOM.
	 * 
	 * @param n
	 *          The Node in the XML DOM.
	 * @return the deserialized AnimationProperties Object.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public AnimationProperties loadPropsFromXML(Node n)
			throws IllegalArgumentException {
		AnimationProperties ap = null;
		String type;

		// list all attributes
		NamedNodeMap attributes = n.getAttributes();

		if (!(attributes.getLength() == 1 || attributes.item(0).getLocalName()
				.equals("type")))
			throw new IllegalArgumentException("'AnimationProperties' needs a "
					+ "'type' attribute!");

		// create 'AnimationProperties' -> Reflection
		type = attributes.item(0).getNodeValue();
		if ("graph".equalsIgnoreCase(type))
		  System.err.println("GRAPH");
		ap = createAnimationPropertiesByName(type);

		if (ap == null)
			throw new IllegalArgumentException(type + " is not a "
					+ "valid type for 'AnimationProperties'!");

		// fill all the children (AnimationPropertyItems)...
		Node child = null;
		Node item = null;
		String name;
		Object value;
		boolean isEditable;
		String label;
		child = n.getFirstChild();
		while (child != null) {

			// skip blank lines in XML
			if (child.getNodeType() == Node.TEXT_NODE && child.getLocalName() == null) {
				child = child.getNextSibling();
				continue;
			}

			// parse the children... (AnimationPropertyItems)
			if (!"AnimationPropertyItem".equals(child.getLocalName()))
				throw new IllegalArgumentException(
						"Child Element is not 'AnimationPropertyItem'!");

			// get type of item
			attributes = child.getAttributes();
			if (!(attributes.getLength() == 1 || attributes.item(0).getLocalName()
					.equals("type")))
				throw new IllegalArgumentException("'AnimationPropertyItem' "
						+ "needs a 'type' attribute!");
			type = attributes.item(0).getNodeValue();

			// get name
			item = child.getFirstChild();
			while (item != null && item.getNodeType() == Node.TEXT_NODE
					&& item.getLocalName() == null) {
				item = item.getNextSibling();
				continue;
			}
			if (item == null || item.getChildNodes().getLength() == 0)
				throw new IllegalArgumentException(
						"'AnimationPropertyItem' must have a 'name'!");

			name = item.getChildNodes().item(0).getNodeValue();

			// get isEditable
			item = item.getNextSibling();
			while (item != null && item.getNodeType() == Node.TEXT_NODE
					&& item.getLocalName() == null) {
				item = item.getNextSibling();
				continue;
			}

			if (item == null || item.getAttributes().getLength() == 0)
				throw new IllegalArgumentException(
						"'AnimationPropertyItem' must have a 'isEditable'!");
			isEditable = getBooleanValueByString(
					item.getAttributes().item(0).getNodeValue()).booleanValue();

			// get label
			item = item.getNextSibling();
			while (item != null && item.getNodeType() == Node.TEXT_NODE
					&& item.getLocalName() == null) {
				item = item.getNextSibling();
				continue;
			}
			if (item == null)
				throw new IllegalArgumentException(
						"'AnimationPropertyItem' must have a 'label'!");
			if (item.getChildNodes().getLength() == 0)
				label = "";
			else
				label = item.getChildNodes().item(0).getNodeValue();

			// get value
			item = item.getNextSibling();
			while (item != null && item.getNodeType() == Node.TEXT_NODE
					&& item.getLocalName() == null) {
				item = item.getNextSibling();
				continue;
			}
			if (item == null)
				throw new IllegalArgumentException(
						"'AnimationPropertyItem' must have a 'value'!");

			value = getValueForPropertyItemByName(type, item);

			// now we can set these values
			ap.setLabel(name, label);
			ap.setIsEditable(name, isEditable);
			ap.set(name, value);
			// Madieha
      if (ap instanceof GraphProperties) {
        Graph g = PropertiesGUI.getGraphFromScriptFile();
        if (g != null) {
          ap.set("weighted", g.getProperties().get("weighted"));
          ap.set("directed", g.getProperties().get("directed"));
        }
      }
//      else 
//        System.err.println("ap is not an instance of GraphProperties, ignore...");
      
      AnimationPropertyItem apiDefault = (AnimationPropertyItem) ap.getItem(
          name).clone();

			ap.setDefault(name, apiDefault);

			// next child
			child = child.getNextSibling();
		}
		return ap;
	}

	/**
	 * loadPrimitiveFromXML loads all the values for a given Primitive by a given
	 * Node in the XML DOM.
	 * 
	 * @param n
	 *          The Node in the XML DOM.
	 * @return the deserialized PropertiesTreeNode.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public PropertiesTreeNode loadPrimitiveFromXML(Node n)
			throws IllegalArgumentException {
		String theType = null;
		String theName = null;
		Object theValue = null;

		// list all attributes
		NamedNodeMap attributes = n.getAttributes();

		if (!(attributes.getLength() == 1 || attributes.item(0).getLocalName()
				.equals("type")))
			throw new IllegalArgumentException("'Primitive' needs a "
					+ "'type' attribute!");

		theType = attributes.item(0).getNodeValue();

		// get the name
		
		Node item = n.getFirstChild();
		while (item != null && item.getNodeType() == Node.TEXT_NODE
				&& item.getLocalName() == null) {
			item = item.getNextSibling();
			continue;
		}
		if (item == null || item.getChildNodes().getLength() == 0)
			throw new IllegalArgumentException("'Primitive' must have a 'name'!");
		

		theName = item.getChildNodes().item(0).getNodeValue();
		


		// get the description
		item = item.getNextSibling();
		while (item != null && item.getNodeType() == Node.TEXT_NODE
				&& item.getLocalName() == null) {
			item = item.getNextSibling();
			continue;
		}
		
		// get the value
		if (item == null || item.getChildNodes().getLength() == 0)
			throw new IllegalArgumentException("'Primitive' must have a 'value'!");

		String theValueType = item.getChildNodes().item(0).getNodeName();

		if (!theValueType.equals(theType))
			throw new IllegalArgumentException(
					"The types for Primitive and 'value' don't match!");

		if (theType.equals("String"))
			theValue = loadStringValueFromNode(item);
		if (theType.equals("int"))
			theValue = loadIntegerValueFromNode(item);
		if (theType.equals("boolean"))
			theValue = loadBooleanValueFromNode(item);
		if (theType.equals("double"))
			theValue = loadDoubleValueFromNode(item);
    if (theType.equals("doubleArray"))
      theValue = loadDoubleArrayValueFromNode(item);
		if (theType.equals("intArray"))
			theValue = loadIntArrayValueFromNode(item);
		if (theType.equals("StringArray"))
			theValue = loadStringArrayValueFromNode(item);
		if (theType.equals("Color"))
			theValue = loadColorValueFromNode(item);
		if (theType.equals("Font"))
			theValue = loadFontValueFromNode(item);
    if (theType.equals("doubleMatrix"))
      theValue = loadDoubleMatrixValueFromNode(item);
		if (theType.equals("intMatrix"))
			theValue = loadIntMatrixValueFromNode(item);
		if (theType.equals("StringMatrix"))
			theValue = loadStringMatrixValueFromNode(item);
	    if (theType.equals("Graph"))
	      theValue = loadGraphValueFromNode(item);

		if (theValue == null)
			throw new IllegalArgumentException("The value for type '" + theType
					+ "' was wrong!");


		item = item.getNextSibling();
		while (item != null && item.getNodeType() == Node.TEXT_NODE
				&& item.getLocalName() == null) {
			item = item.getNextSibling();
			continue;
		}
		String theDescription = null;
		if(item!=null && item.getNodeName().equals("description")){
			theDescription = item.getChildNodes().item(0).getNodeValue();
			
		}

		if(theDescription==null){
			return new PropertiesTreeNode(theName, theValue);
		}else{
			return new PropertiesTreeNode(theName, theValue, theDescription);
		}
	}

	// *****************************************************************************
	// *** END OF THE TWO MAIN METHODS
	// *****************************************************************************

	// *****************************************************************************
	// *** BELOW ARE THE VISITOR METHODS
	// *****************************************************************************

	/**
	 * (non-Javadoc)
	 * 
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.StringPropertyItem)
	 */
	public void visit(StringPropertyItem spi) {
		if (spi == null)
			return;
		this.objValue = loadStringValueFromNode(this.nodeStart);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.IntegerPropertyItem)
	 */
	public void visit(IntegerPropertyItem ipi) {
		if (ipi == null)
			return;
		this.objValue = loadIntegerValueFromNode(this.nodeStart);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.BooleanPropertyItem)
	 */
	public void visit(BooleanPropertyItem bpi) {
		if (bpi == null)
			return;
		this.objValue = loadBooleanValueFromNode(this.nodeStart);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.DoublePropertyItem)
	 */
	public void visit(DoublePropertyItem dpi) {
		if (dpi == null)
			return;
		this.objValue = loadDoubleValueFromNode(this.nodeStart);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.ColorPropertyItem)
	 */
	public void visit(ColorPropertyItem cpi) {
		if (cpi == null)
			return;
		this.objValue = loadColorValueFromNode(this.nodeStart);
	}

	public void visit(EnumerationPropertyItem epi) {
	  if (epi == null)
	    return;
      objValue = loadEnumerationValueFromNode(nodeStart);
	}
   /**
	 * (non-Javadoc)
	 * 
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.FontPropertyItem)
	 */
	public void visit(FontPropertyItem fpi) {
		if (fpi == null)
			return;
		this.objValue = loadFontValueFromNode(this.nodeStart);
	}

	// *****************************************************************************
	// *** END OF VISITOR METHODS
	// *****************************************************************************

	// *****************************************************************************
	// *** BEGIN OF: load???ValueFromNode
	// *****************************************************************************

	/**
	 * loadStringValueFromNode loads the given (by method name) type of Object
	 * from the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private String loadStringValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("String")) {
				if (helper.getChildNodes().getLength() == 0)
					return "";
				if (helper.getChildNodes().getLength() == 1)
					return helper.getChildNodes().item(0).getNodeValue().replace("\\n", System.getProperty("line.separator"));
			}
		}
		throw new IllegalArgumentException("Error while loading String!");
	}
	
	//Madieha
	private GraphScriptPanel loadGraphValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("Graph")
					&& helper.getChildNodes().getLength() == 1) {
				return getGraphValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading Graph!");
	}

	/**
	 * loadIntegerValueFromNode loads the given (by method name) type of Object
	 * from the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private Integer loadIntegerValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("int")
					&& helper.getChildNodes().getLength() == 1) {
				return getIntegerValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading int!");
	}

	/**
	 * loadBooleanValueFromNode loads the given (by method name) type of Object
	 * from the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private Boolean loadBooleanValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("boolean")
					&& helper.getAttributes().getLength() == 1) {
				return getBooleanValueByString(helper.getAttributes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading boolean!");
	}

	/**
	 * loadDoubleValueFromNode loads the given (by method name) type of Object
	 * from the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private Double loadDoubleValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("double")
					&& helper.getChildNodes().getLength() == 1) {
				return getDoubleValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading double!");
	}


  /**
   * loadArrayValueFromNode loads the given (by method name) type of Object from
   * the given XML Node.
   * 
   * @param n
   *          The XML DOM Node.
   * @return The given Node as the given type.
   */
  private double[] loadDoubleArrayValueFromNode(Node n) {
    Node helper;
    if (n.getChildNodes().getLength() == 1) {
      helper = n.getChildNodes().item(0);
      if (helper.getLocalName().equals("doubleArray")
          && helper.getChildNodes().getLength() == 1) {
        return getDoubleArrayValueByString(helper.getChildNodes().item(0)
            .getNodeValue());
      }
    }
    throw new IllegalArgumentException("Error while loading intArray!");
  }
	
	/**
	 * loadArrayValueFromNode loads the given (by method name) type of Object from
	 * the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private int[] loadIntArrayValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("intArray")
					&& helper.getChildNodes().getLength() == 1) {
				return getIntArrayValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading intArray!");
	}
	
	 /**
   * loadArrayValueFromNode loads the given (by method name) type of Object from
   * the given XML Node.
   * 
   * @param n
   *          The XML DOM Node.
   * @return The given Node as the given type.
   */
  private double[][] loadDoubleMatrixValueFromNode(Node n) {
    Node helper;
    if (n.getChildNodes().getLength() == 1) {
      helper = n.getChildNodes().item(0);
      if (helper.getLocalName().equals("doubleMatrix")
          && helper.getChildNodes().getLength() == 1) {
        return getDoubleMatrixValueByString(helper.getChildNodes().item(0)
            .getNodeValue());
      }
    }
    throw new IllegalArgumentException("Error while loading doubleMatrix!");
  }
	
	 /**
   * loadArrayValueFromNode loads the given (by method name) type of Object from
   * the given XML Node.
   * 
   * @param n
   *          The XML DOM Node.
   * @return The given Node as the given type.
   */
  private int[][] loadIntMatrixValueFromNode(Node n) {
    Node helper;
    if (n.getChildNodes().getLength() == 1) {
      helper = n.getChildNodes().item(0);
      if (helper.getLocalName().equals("intMatrix")
          && helper.getChildNodes().getLength() == 1) {
        return getIntMatrixValueByString(helper.getChildNodes().item(0)
            .getNodeValue());
      }
    }
    throw new IllegalArgumentException("Error while loading intMatrix!");
  }

	
	/**
	 * loadStringMatrixValueFromNode loads the given (by method name) type of Object from
	 * the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private String[][] loadStringMatrixValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("StringMatrix")
					&& helper.getChildNodes().getLength() == 1) {
				return getStringMatrixValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading StringMatrix!");
	}


	/**
	 * loadArrayValueFromNode loads the given (by method name) type of Object from
	 * the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private String[] loadStringArrayValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equalsIgnoreCase("StringArray")
					&& helper.getChildNodes().getLength() == 1) {
				return getStringArrayValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading StringArray!");
	}

	/**
	 * loadColorValueFromNode loads the given (by method name) type of Object from
	 * the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private Color loadColorValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("Color")
					&& helper.getChildNodes().getLength() == 1) {
				return getColorValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading Color!");
	}

	/**
	 * loadFontValueFromNode loads the given (by method name) type of Object from
	 * the given XML Node.
	 * 
	 * @param n
	 *          The XML DOM Node.
	 * @return The given Node as the given type.
	 */
	private Font loadFontValueFromNode(Node n) {
		Node helper;
		if (n.getChildNodes().getLength() == 1) {
			helper = n.getChildNodes().item(0);
			if (helper.getLocalName().equals("Font")
					&& helper.getChildNodes().getLength() == 1) {
				return getFontValueByString(helper.getChildNodes().item(0)
						.getNodeValue());
			}
		}
		throw new IllegalArgumentException("Error while loading Font!");
	}
    

	private Object loadEnumerationValueFromNode(Node n) {
        Node helper;
        if (n.getChildNodes().getLength() == 1) {
            helper = n.getChildNodes().item(0);
            if (helper.getLocalName().equals("Enumeration")
                    && helper.getChildNodes().getLength() == 1) {
                return getEnumerationValueByString(helper.getChildNodes().item(0)
                        .getNodeValue());
            }
        }
        throw new IllegalArgumentException("Error while loading Font!");

	}



	// *****************************************************************************
	// *** END OF: load???ValueFromNode
	// *****************************************************************************

	// *****************************************************************************
	// *** BELOW ARE THE get???ValueByString HELPERS
	// *****************************************************************************

	//	Madieha 
  public GraphScriptPanel getGraphValueByString(String value)throws IllegalArgumentException {
    PropertiesGUI.start();
    GraphScriptPanel parts=new GraphScriptPanel(PropertiesGUI.mainclass);
    
      try {
        parts.input.setText(value);
      } catch (Exception e) {
        throw new IllegalArgumentException(value
            + " is not a valid value for a Graph!");
      }
    return parts;
  }

  /**
	 * getIntegerValueByString returns the value of the given String as a Integer.
	 * 
	 * @param value
	 *          The String holding the Integer.
	 * @return A Integer that holds the int given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public Integer getIntegerValueByString(String value)
			throws IllegalArgumentException {
		try {
			Integer retVal = new Integer(value);
			return retVal;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(value + " is not a valid value"
					+ "for an int!");
		}
	}

	/**
	 * getBooleanValueByString returns the value of the given String as a Boolean.
	 * 
	 * @param value
	 *          The String holding the Boolean.
	 * @return A Boolean that holds the boolean given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public Boolean getBooleanValueByString(String value)
			throws IllegalArgumentException {
		if (value.equalsIgnoreCase("true"))
			return Boolean.valueOf(true);
		if (value.equals("1"))
			return Boolean.valueOf(true);
		if (value.equalsIgnoreCase("false"))
			return new Boolean(false);
		if (value.equals("0"))
			return new Boolean(false);
		throw new IllegalArgumentException(value + " is not a valid value for a"
				+ " boolean!");
	}

	/**
	 * getDoubleValueByString returns the value of the given String as a Double.
	 * 
	 * @param value
	 *          The String holding the Double.
	 * @return A Double that holds the double given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public Double getDoubleValueByString(String value)
			throws IllegalArgumentException {
		try {
			Double retVal = new Double(value);
			return retVal;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(value + " is not a valid value"
					+ "for a double!");
		}
	}

	 /**
   * getArrayValueByString returns the value of the given String as a double
   * array.
   * 
   * @param value
   *          The String holding the double array.
   * @return A double array that holds the doubles given by the value String.
   * @throws IllegalArgumentException
   *           if an error occurs.
   */
  public double[] getDoubleArrayValueByString(String value)
      throws IllegalArgumentException {
    String[] parts = value.split(",", 128);
    double[] tmp = new double[parts.length];
    for (int i = 0; i < parts.length; i++)
      try {
        tmp[i] = Double.valueOf(parts[i].trim());
      } catch (Exception e) {
        throw new IllegalArgumentException(value
            + " is not a valid value for an intArray!");
      }
    return tmp;
  }

  /**
   * getArrayValueByString returns the value of the given String as a double
   * matrix.
   * 
   * @param value
   *          The String holding the double matrix.
   * @return A double matrixthat holds the doubles given by the value String.
   * @throws IllegalArgumentException
   *           if an error occurs.
   */
  public double[][] getDoubleMatrixValueByString(String value)
      throws IllegalArgumentException {
    String[] parts = value.split(";", 128);
    double[][] tmp = new double[parts.length][];
    for (int i = 0; i < parts.length; i++) {
      String[] localParts = parts[i].split(",", 128);
      tmp[i] = new double[localParts.length];
      for (int j = 0; j < localParts.length; j++) {
        try {
          tmp[i][j] = Double.valueOf(localParts[j].trim());
        } catch (Exception e) {
          throw new IllegalArgumentException(value
              + " is not a valid value for an doubleMatrix!");
        }
      }
    }
    return tmp;
  }
	
	
	/**
	 * getArrayValueByString returns the value of the given String as an int
	 * array.
	 * 
	 * @param value
	 *          The String holding the int array.
	 * @return An int array that holds the ints given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public int[] getIntArrayValueByString(String value)
			throws IllegalArgumentException {
		String[] parts = value.split(",", 128);
		int[] tmp = new int[parts.length];
		for (int i = 0; i < parts.length; i++)
			try {
				tmp[i] = Integer.parseInt(parts[i].trim());
			} catch (Exception e) {
				throw new IllegalArgumentException(value
						+ " is not a valid value for an intArray!");
			}
		return tmp;
	}

	/**
	 * getArrayValueByString returns the value of the given String as an int
	 * array.
	 * 
	 * @param value
	 *          The String holding the int array.
	 * @return An int array that holds the ints given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public int[][] getIntMatrixValueByString(String value)
			throws IllegalArgumentException {
		String[] parts = value.split(";", 128);
		int[][] tmp = new int[parts.length][];
		for (int i = 0; i < parts.length; i++) {
			String[] localParts = parts[i].split(",", 128);
			tmp[i] = new int[localParts.length];
 			for (int j = 0; j < localParts.length; j++) {
				try {
					tmp[i][j] = Integer.parseInt(localParts[j].trim());
				} catch (Exception e) {
					throw new IllegalArgumentException(value
							+ " is not a valid value for an intMatrix!");
				}
			}
		}
		return tmp;
	}
	
	
	
	
	/**
	 * getArrayValueByString returns the value of the given String as an String
	 * array.
	 * 
	 * @param value
	 *          The String holding the String array.
	 * @return An String array that holds the String given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public String[][] getStringMatrixValueByString(String value)
			throws IllegalArgumentException {
		String[] parts = value.split(";", 128);
		String[][] tmp = new String[parts.length][];
		for (int i = 0; i < parts.length; i++) {
			String[] localParts = parts[i].split(",", 128);
			tmp[i] = new String[localParts.length];
 			for (int j = 0; j < localParts.length; j++) {
				try {
					tmp[i][j] = localParts[j].trim();
				} catch (Exception e) {
					throw new IllegalArgumentException(value
							+ " is not a valid value for an intMatrix!");
				}
			}
		}
		return tmp;
	}

	/**
	 * getArrayValueByString returns the value of the given String as an int
	 * array.
	 * 
	 * @param value
	 *          The String holding the int array.
	 * @return An int array that holds the ints given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public String[] getStringArrayValueByString(String value)
			throws IllegalArgumentException {
		String[] parts = value.split(",", 128);
		String[] tmp = new String[parts.length];
		for (int i = 0; i < parts.length; i++)
			tmp[i] = parts[i].trim();
		return tmp;
	}

	/**
	 * getColorValueByString returns the value of the given String as a Color.
	 * 
	 * @param value
	 *          The String holding the Color.
	 * @return A Color that represents the Color given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public Color getColorValueByString(String value)
			throws IllegalArgumentException {
		try {
			Color retVal = ColorChooserComboBox.getColorForString(value);
			return retVal;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(value + " is not a valid value"
					+ "for a Color!");
		}
	}

	/**
	 * getFontValueByString returns the value of the given String as a Font.
	 * 
	 * @param value
	 *          The String holding the Font.
	 * @return A Font that represents the Font given by the value String.
	 * @throws IllegalArgumentException
	 *           if an error occurs.
	 */
	public Font getFontValueByString(String value)
			throws IllegalArgumentException {
		Font retVal = new Font(value, Font.PLAIN, 12);
		return retVal;
	}

    private String getEnumerationValueByString(String value)
    throws IllegalArgumentException {
      return new String(value);
    }



	// *****************************************************************************
	// *** END OF: get???ValueByString
	// *****************************************************************************

	/**
	 * createAnimationPropertiesByName creates an AnimationProperties object of
	 * the Class specified by the String className.
	 * 
	 * @param className
	 *          The name of the new Objects Class.
	 * @return An Object of the Class specified by the String className.
	 */
	@SuppressWarnings("unchecked")
	private static AnimationProperties createAnimationPropertiesByName(
			String className) {
		if (className == null)
			return null;
		AnimationProperties object = null;
		String pack = AnimationProperties.class.getPackage().getName();

		try {
			Class<AnimationProperties> genClass = 
				(Class<AnimationProperties>)Class.forName(pack + "." + className);
			object = genClass.newInstance();
			return object;
		} catch (Exception e) {
			// maybe the Class is abstract (InstantiationException)
			// we are not allowed to access the Class (IllegalAccessException)
			// the Class does not exist (ClassNotFoundException)
			// className is not a subclass of AnimationProperties (ClassCastException)
			return null;
		}
	}

	/**
	 * getValueForPropertyItemByName returns the given value as an Object that can
	 * be set to the AnimationPropertyItem.
	 * 
	 * @param className
	 *          The name of the new Objects Class.
	 * @param node
	 *          The value (as a XML Node).
	 * @return An Object that can be passed to the set method of the given class.
	 * @throws IllegalArgumentException
	 *           if something goes wrong.
	 */
	@SuppressWarnings("unchecked")
	private Object getValueForPropertyItemByName(String className, Node node)
			throws IllegalArgumentException {
		if (className == null || node == null)
			return null;
		AnimationPropertyItem object = null;
		String pack = AnimationPropertyItem.class.getPackage().getName();

		try {
			Class<AnimationPropertyItem> genClass = 
				(Class<AnimationPropertyItem>)Class.forName(pack + "." + className);
			object = genClass.newInstance();
			this.nodeStart = node;
			object.accept(this);
			return this.objValue;
		} catch (Exception e) {
			// maybe the Class is abstract (InstantiationException)
			// we are not allowed to access the Class (IllegalAccessException)
			// the Class does not exist (ClassNotFoundException)
			// className is not a subclass of AnimationProperties (ClassCastException)
			throw new IllegalArgumentException(className
					+ " is not a valid type for an 'AnimationPropertyItem'!");
		}
	}

}
