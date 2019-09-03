/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generators.framework.wizard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jerome, Dominik Fischer
 */
public class GeneratorWriter {

    private Writer writer;
    private File file;
    private String tab = "    ";
    private String nextLine = System.getProperty("line.separator");
    /**
     * Starts an inline string
     */
    private String s = "\"";
    private StringBuffer bufferImports = new StringBuffer();
    private StringBuffer bufferVariables = new StringBuffer();
    private StringBuffer bufferMethods = new StringBuffer();
    private StringBuffer bufferCopyright = new StringBuffer();

    private HashMap<String, String> packageLookup = new HashMap<String, String>(31);
    
    
    private void addImport(String importString) {
        bufferImports.append("import " + importString + ";");
        bufferImports.append(nextLine);
    }

    private void addVariable(String variable) {
        bufferVariables.append(tab + variable);
        bufferVariables.append(nextLine);
    }

    private void addMethod(String title, String body) throws IOException {
        bufferMethods.append(tab + title + nextLine);
        bufferMethods.append(tab + tab + body + nextLine);
        bufferMethods.append(tab + "}" + nextLine + nextLine);
    }
    
    private void addCopyright(String author, String className, boolean permitDisclosure) {
      bufferCopyright.append("/*");
      bufferCopyright.append(nextLine);
      bufferCopyright.append(" * ");
      bufferCopyright.append(className);
      bufferCopyright.append(".java");
      bufferCopyright.append(nextLine);
      bufferCopyright.append(" * ");
      bufferCopyright.append(author);
      bufferCopyright.append(", ");
      bufferCopyright.append(Calendar.getInstance().get(Calendar.YEAR));
      bufferCopyright.append(" for the Animal project at TU Darmstadt.");
      bufferCopyright.append(nextLine);
      if (permitDisclosure) {
        bufferCopyright.append(" * " +
      "Copying this file for educational purposes is permitted without further authorization.");
        bufferCopyright.append(nextLine);
      }
      bufferCopyright.append(" */");
      bufferCopyright.append(nextLine);
    }

//    private String getPackageTitle(String type) {
//        String title = type.toLowerCase();
//
//        if(title.equals("crypt")){
//            title = "cryptography";
//        }else if(title.equals("data_structures")){
//            title = "datastructures";
//        }else if(title.equals("search")){
//            title = "searching";
//        }else if(title.equals("sort")){
//            title = "sorting";
//        }else if (title.equals("more")){
//            title = "misc";
//        }
//        return "package generators." + title + ";"
//                + nextLine + nextLine;
//    }

    // should look roughly like this:
    // public String generate(..., ...) {
    //   now iterate over the Hashmap name -> type
    //   create entries of form
    //     myType myName = (myType)props.get("myName");
    // or
    //     myType myName = (myType)primitives.get("myName");
    // as shown in "most" existing generators
    public StringBuffer fillMap(HashMap<String, String> hashmap,
            boolean extractPrims, boolean extractProps) throws IOException {

        StringBuffer buffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> i = hashmap.entrySet().iterator();

        while (i.hasNext()) {
            Map.Entry<String, String> me = (Map.Entry<String, String>) i.next();

            String arg = "props.getPropertiesByName";
            String caste = me.getValue().toString();
            String type = "";
            String name = me.getKey().toString();

            boolean isPrimitive = caste.indexOf("algoanim.properties.") == -1;


            if (isPrimitive) {  // if this is a primitive
                arg = "primitives.get";
            } else { // If it is a property
                // Check that its import exists
                if (bufferImports.indexOf(caste) == -1) {
                    // If not add it
                    addImport(caste);
                }
                // With the import the direct type name can be called
                caste = caste.replaceAll("algoanim.properties.", "");
            }
            type = caste;

            if (type.equals("Integer")) {
                type = "int";
            } else if (type.equals("Boolean")) {
                type = "boolean";
            }

            String ending = " = (" + caste
                    + ")" + arg + "(" + s
                    + name + s + ");";

            if (isPrimitive) {
                if (extractPrims) {
                    buffer.append(name + ending);
                    addVariable("private " + type + " " + name + ";");
                } else {
                    buffer.append(type + " " + name + ending);
                }
            } else {
                if (extractProps) {
                    buffer.append(name + ending);
                    addVariable("private " + caste + " " + name + ";");
                } else {
                    buffer.append(type + " " + name + ending);
                }
            }
            buffer.append(nextLine);
            buffer.append(tab + tab);
        }
        return buffer;
    }
    
    private String normalizeString(String s) {
      StringTokenizer stok = new StringTokenizer(s, "\n", true);
      StringBuffer sb = new StringBuffer(s.length()+80);
      int lineNr = 0;
      while (stok.hasMoreTokens()) {
          String newString = stok.nextToken();
          if (lineNr > 0)
            sb.append("\n +");
          sb.append("\"").append(newString.replace("\n","\\n").
              replace("\"", "\\\"")).append("\"");
          lineNr++;
          // sb.append("\n");
      }
      return sb.toString();
    }

    public GeneratorWriter(String filename, String location,
            String algorithmName, String title, String generatorType,
            String author, String description, String codeExample,
            String locale, String fileExtension, String outputLanguage,
            HashMap<String, String> hashmap, boolean extractPrims,
            boolean extractProps, boolean permitDisclosure, boolean overwrite)
                throws FileAlreadyExistsException {

        String strFile = location;
        initHashMap();
        // here we add the extension,if no extension has been given...
        if (strFile.indexOf(".") == -1) {
            strFile = strFile.concat(".java");
        }
        //    if(filename.get)
        file = new File(strFile);
        if (file.exists()) {
          if (!file.isFile()) {
            throw new IllegalArgumentException(
                file.getPath() + "is not a valid name "
                + "for a file. Please choose another name!");
          }
          if (!overwrite) {
            throw new FileAlreadyExistsException(file.getPath());
          }
        }

        try {
            writer = new BufferedWriter(new FileWriter(file));

            addImport("generators.framework.Generator");
            addImport("generators.framework.GeneratorType");
            addImport("java.util.Locale");
            addImport("algoanim.primitives.generators.Language");
            addImport("java.util.Hashtable");
            addImport("generators.framework.properties.AnimationPropertiesContainer");
            addImport("algoanim.animalscript.AnimalScript");
            if (hashmap.containsValue("Color")) {
              addImport("java.awt.Color");
            }
            if (hashmap.containsValue("Font")) {
              addImport("java.awt.Font");
            }

            String className = filename.replace(".java", "");
            String classString = "public class " + className
                    + " implements Generator {";
            
            addCopyright(author, className, permitDisclosure);

            addVariable("private Language lang;");

            addMethod("public void init(){", "lang = new AnimalScript(\""
                    + title + "\", \"" + author + "\", 800, 600);");


            addMethod("public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {",
                    fillMap(hashmap, extractPrims, extractProps).toString()
                    + nextLine + tab + tab + "return lang.toString();");

            addMethod("public String getName() {",
                    "return \"" + title + "\";");

            addMethod("public String getAlgorithmName() {",
                    "return \"" + algorithmName + "\";");

            addMethod("public String getAnimationAuthor() {",
                    "return \"" + author + "\";");

            addMethod("public String getDescription(){",
                    "return " + normalizeString(description) + ";");

            addMethod("public String getCodeExample(){",
                    "return " + normalizeString(codeExample) + ";");

            addMethod("public String getFileExtension(){",
                    "return \"" + fileExtension + "\";");

            addMethod("public Locale getContentLocale() {",
                    "return Locale." + locale + ";");

            addMethod("public GeneratorType getGeneratorType() {",
                    "return new GeneratorType(GeneratorType.GENERATOR_TYPE_" + generatorType + ");");

            addMethod("public String getOutputLanguage() {",
                    "return Generator." + outputLanguage + "_OUTPUT;");

            String packageTitle = packageLookup.get(generatorType);
            if (packageTitle == null)
              packageTitle = generatorType.toLowerCase();
//            writer.append(getPackageTitle(GeneratorType));
            writer.append(bufferCopyright);
            writer.append("package generators.").append(packageTitle).append(";");
            writer.append(nextLine).append(nextLine);
            writer.append(bufferImports);
            writer.append(nextLine);
            writer.append(classString);
            writer.append(nextLine);
            writer.append(bufferVariables);
            writer.append(nextLine);
            writer.append(bufferMethods);
            writer.append("}");

        } catch (IOException ex) {
            Logger.getLogger(GeneratorWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initHashMap() {
      packageLookup.put("BACKTRACKING", "backtracking");
      packageLookup.put("COMPRESSION", "compression");
      packageLookup.put("CRYPT", "cryptography");
      packageLookup.put("DATA_STRUCTURE", "datastructures");
      packageLookup.put("GRAPH", "graph");
      packageLookup.put("GRAPHICS", "graphics");
      packageLookup.put("HARDWARE", "hardware");
      packageLookup.put("HASHING", "hashing");
      packageLookup.put("MATHS", "maths");
      packageLookup.put("MORE", "misc");
      packageLookup.put("NETWORK", "network");
      packageLookup.put("SEARCH", "searching");
      packageLookup.put("SORT", "sorting");
      packageLookup.put("TREE", "tree");
    }
}
