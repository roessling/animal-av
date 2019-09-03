package generators.generatorframe.loading;

/**
 * 
 * @author Nora Wester
 *
 */

public class HTMLParser {

	public static String parse(String text){
		if(text == null)
			return "";
		
		String s = text.replace("&quot;", "\"").replace("&amp;", "&").replace("&lt;", "<")
				.replace("&gt;", ">").replace("&nbsp;", " ").replace("<br>", " ")
				.replace("&uuml;", "ü").replace("&Uuml;", "Ü").replace("&ouml;", "ö")
				.replace("&Ouml;", "Ö").replace("&auml;", "ä").replace("&Auml;", "Ä")
				.replace("&szlig;", "ß").replace("</br>", "\n").replace("</ul>", "\n").replace("<br />", "\n")
				.replace("<li>", "\n - ");
		
//		s = cleanUp(s);
		return s;
	}
	
	private static String cleanUp(String string){
	  
	  StringBuffer sb = new StringBuffer(string);
	  int index = sb.indexOf("<");
	 
	  while(index != -1){
	    int indexT = sb.indexOf(">");
	    //String newString = string.substring(0, index) + string.substring(indexT+1);
	    //index = string.indexOf("<");
	    //cleanUp(newString);
	   // System.out.println(index + " " + indexT + " " + sb.length());
	    if(indexT != -1 && sb.length()>indexT && indexT>index){
	      sb.delete(index, indexT+1);
	    }else{
	      sb.deleteCharAt(index);
	    }
	    index = sb.indexOf("<");
	  }
	  
	  return sb.toString();
	}
}
