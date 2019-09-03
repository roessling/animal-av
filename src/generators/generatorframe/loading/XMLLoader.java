package generators.generatorframe.loading;


import generators.framework.properties.AnimationPropertiesContainer;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;














import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import algoanim.properties.AnimationProperties;


import algoanim.properties.items.AnimationPropertyItem;

/**
 * 
 * @author Nora Wester
 *
 */

public class XMLLoader {

	AnimationPropertiesContainer prop;

	Hashtable<String, Object> prim;
	Hashtable<String, String> primDescription;
	
	public XMLLoader(URL path){//TODO set Path for local and jar
		
		Document document;
		SAXBuilder builder = new SAXBuilder();
		
		try {
			document = builder.build(path.openStream());
		
			Element element = document.getRootElement();
			
			Element root = (Element) element.getChildren().get(0);
			

			prim = new Hashtable<String, Object>();
			primDescription = new Hashtable<String, String>();
			
			searchForPrimitive(root);
			
			
			prop = new AnimationPropertiesContainer();
			
			searchForProperties(root);

		
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
	}

	private void setPrimTable(List<Element> primitive) {
		
		for(int j=0; j<primitive.size(); j++){
			
			Element temp = primitive.get(j);
			String type = temp.getAttributeValue("type");
			Element child = temp.getChild("value");
			  if(temp.getChild("description")!=null){
				  primDescription.put(temp.getChildTextNormalize("name"), temp.getChild("description").getValue());
			  }

			String val = child.getChildTextNormalize(type);
			if (val == null) {
			  Element nameEntry = temp.getChild("name");
			  System.err.println("XML spec: type mismatch for primitive '" + nameEntry.getTextNormalize() +"' and type " +type);
			} else {
			  //fuer den Fall, dass es nicht in <value></value> steht sondern im typeTag
			  if (val.compareTo("") == 0){
			    val = temp.getChild("value").getChild(type).getAttributeValue("value");
			  }

			  prim.put(temp.getChildTextNormalize("name"), ClassFactory.getObject(temp.getAttributeValue("type"), val));
			  
			
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void searchForProperties(Element root) {
		// TODO Auto-generated method stub
		List<Element> props = (List<Element>) root.getChildren("AnimationProperties");
		
		if(!props.isEmpty()){
			setPropContainer(props);
		}
		
		//are there any folders in the root?
		List<Element> folder = (List<Element>) root.getChildren("Folder");
		for(int i=0; i<folder.size(); i++){
			searchForProperties(folder.get(i));
		}
	}

	@SuppressWarnings("unchecked")
	private void setPropContainer(List<Element> props) {
		// TODO Auto-generated method stub
		for(int i=0; i<props.size(); i++){
			
			Element temp = props.get(i);
			String type = temp.getAttributeValue("type");
			
			AnimationProperties ap = null;
			try {
				Class<?> aPClass = Class.forName("algoanim.properties." + type);
				ap = (AnimationProperties)aPClass.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<Element> items = temp.getChildren("AnimationPropertyItem");
			for(int j=0; j<items.size(); j++){
				
				Element item = items.get(j);
				String itemType = item.getAttributeValue("type");
				String name = item.getChildTextNormalize("name");
				
				ap.setIsEditable(name, Boolean.valueOf(item.getChild("isEditable").getAttributeValue("value")));
				ap.setLabel(name, item.getChildTextNormalize("label"));
				
				String rightCast = getRightCast(itemType, true);
				if(rightCast == "")
					throw new IllegalArgumentException("Type could not be cast to PropertyItem");
				
				String val = item.getChild("value").getChildTextNormalize(rightCast);
				
				//fuer den Fall, dass es nicht in <value></value> steht sondern im typeTag
				if(val.compareTo("") == 0){
					val = item.getChild("value").getChild(rightCast).getAttributeValue("value");
				}
				
		
				ap.set(name, ClassFactory.getObject(getRightCast(itemType, false), val));
				
//				if (ap instanceof GraphProperties) {
//			        Graph g = PropertiesGUI.getGraphFromScriptFile();
//			        if (g != null) {
//			          ap.set("weighted", g.getProperties().get("weighted"));
//			          ap.set("directed", g.getProperties().get("directed"));
//			        }
//			    }
				
				
				AnimationPropertyItem apiDefault = (AnimationPropertyItem) ap.getItem(name).clone();

				ap.setDefault(name, apiDefault);
			
			}
			
			prop.add(ap);
		}
	}

	/**
	 * 
	 * @param itemType
	 * @param xml ob die Abfrage f�r das XML-Dokument ist
	 * @return
	 */
	private String getRightCast(String itemType, boolean xml) {
		// TODO Auto-generated method stub
		
		if(itemType.compareTo("BooleanPropertyItem") == 0)
			return "boolean";
		if(itemType.compareTo("ColorPropertyItem") == 0)
			return "Color";
		if(itemType.compareTo("DoublePropertyItem") == 0)
			return "double";
		if(itemType.compareTo("EnumerationPropertyItem") == 0 && !xml)
			return "String";
		if(itemType.compareTo("EnumerationPropertyItem") == 0 && xml)
			return "Enumeration";
		if(itemType.compareTo("FontPropertyItem") == 0)
			return "Font";
		if(itemType.compareTo("IntegerPropertyItem") == 0)
			return "int";
		if(itemType.compareTo("StringPropertyItem") == 0)
			return "String";
		
		return "";
	}

	@SuppressWarnings("unchecked")
	private void searchForPrimitive(Element root) {
		// TODO Auto-generated method stub
		List<Element> primitive = (List<Element>) root.getChildren("Primitive");
		
		if(!primitive.isEmpty()){
			setPrimTable(primitive);
		}
		
		//are there any folders in the root?
		List<Element> folder = (List<Element>) root.getChildren("Folder");
		for(int i=0; i<folder.size(); i++){
			searchForPrimitive(folder.get(i));
		}
		
	}
	
	public Hashtable<String, Object> getPrim(){
		return prim;
	}
	
	public Hashtable<String, String> getPrimDescription(){
		return primDescription;
	}

	public AnimationPropertiesContainer getProps() {
		// TODO Auto-generated method stub
		return prop;
	}
	
}
