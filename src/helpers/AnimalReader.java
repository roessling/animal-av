package helpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import animal.main.Animal;
import translator.Debug;

public class AnimalReader {
  
  private static AnimalReader aReader = new AnimalReader();
  
  private AnimalReader() {
    
  }
  
//  public static void main(String[] args) {
//    System.out.println(new File("C:\\Users\\Christian\\git\\gr26fabi-animal-av\\bin\\graphics\\Animal-large.jpg").exists());
//  }
  
  public static InputStream getInputStream(String resourceName) {
    String fileNameWithPath = getInputStreamNameWithPath(null, resourceName);
    return getInputStreamFromFileNameWithPath(fileNameWithPath);
  }
  
  public static InputStream getInputStream(String directory, String resourceName) {
    String fileNameWithPath = getInputStreamNameWithPath(directory, resourceName);
    return getInputStreamFromFileNameWithPath(fileNameWithPath);
  }
  
  public static InputStream getInputStreamOnLayer(Object classObject, String resourceName) {
    return getInputStreamOnLayer(classObject.getClass(), resourceName);
  }
  
  public static InputStream getInputStreamOnLayer(Class<? extends Object> classObject, String resourceName) {
    InputStream in = classObject.getResourceAsStream(resourceName);
    if(in!=null) {
      return in;
    } else {
      String directory = classObject.getProtectionDomain().getCodeSource().getLocation().getPath();
      String directoryPackages = classObject.getCanonicalName();
      if(directoryPackages.contains(".")) {
        directory = directory + directoryPackages.substring(0, directoryPackages.lastIndexOf(".")).replaceAll("\\\\.", File.separator);
      }
      String fileNameWithPath = getInputStreamNameWithPath(directory, resourceName);
      return getInputStreamFromFileNameWithPath(fileNameWithPath);
    }
  }
  
  private static String getInputStreamNameWithPath(String directory, String resourceName) {
    if(resourceName==null || resourceName.equals("")) {
      return null;
    }else {
      if(resourceName.contains(File.separator) || resourceName.contains(File.separator)) {
        String temp = getOnlyResourceName(resourceName);
        if(directory==null) {
          directory = "";
        }
        directory = directory + resourceName.replace(temp, "");
        resourceName = temp;
      }
      String filenameWithPath = "";
      if (directory == null)
        filenameWithPath = resourceName;
      else if (directory.endsWith(File.separator))
        filenameWithPath = directory + resourceName;
      else if (directory.endsWith("/"))
        filenameWithPath = directory + resourceName;
      else
        filenameWithPath = directory + File.separator + resourceName;
      return filenameWithPath;
    }
  }
  
  private static String getOnlyResourceName(String fileNameWithPath) {
    if(fileNameWithPath!=null && fileNameWithPath.contains(File.separator)) {
      return fileNameWithPath.substring(fileNameWithPath.lastIndexOf(File.separator)+1);
    } else if(fileNameWithPath!=null && fileNameWithPath.contains("/")) {
      return fileNameWithPath.substring(fileNameWithPath.lastIndexOf("/")+1);
    } else {
      return fileNameWithPath;
    }
  }

  private static InputStream getInputStreamFromFileNameWithPath(String fileNameWithPath) {
    return getInputStreamFromFileNameWithPath(fileNameWithPath, false);
  }
  private static InputStream getInputStreamFromFileNameWithPath(String fileNameWithPath, boolean loadRelatedTemp) {
    String filename = getOnlyResourceName(fileNameWithPath);
    InputStream inputStream = null;
    
    inputStream = ClassLoader.getSystemResourceAsStream(fileNameWithPath);
    
    if (inputStream == null) {
      try {
        inputStream = new FileInputStream(fileNameWithPath);
      } catch (FileNotFoundException e) {
//        e.printStackTrace();
      }
    }
    
    if (inputStream == null && fileNameWithPath.contains(File.separator+"bin"+File.separator)) {
      try {
        inputStream = new FileInputStream(fileNameWithPath.replaceFirst(File.separator+"bin"+File.separator, File.separator+"src"+File.separator));
      } catch (FileNotFoundException e) {
//        e.printStackTrace();
      }
    }
    if (inputStream == null && fileNameWithPath.contains("/bin/")) {
      try {
        inputStream = new FileInputStream(fileNameWithPath.replaceFirst("/bin/", "/src/"));
      } catch (FileNotFoundException e) {
//        e.printStackTrace();
      }
    }
    
    if (Animal.runsInApplet && inputStream == null) {
      try {
        URL targetURL = new URL(filename);
        // System.err.println("created URL: " +targetURL);
        inputStream = targetURL.openStream();
        // if (inputStream != null)
        // System.err.println("loaded by URL: "+resourceName +" / "
        // +inputStream);
      } catch (IOException ioExceptionApplet) {
        Debug
            .printlnMessage("Sorry, the applet cannot open the resource file for "
                + fileNameWithPath);
      }
    }
    
    if(inputStream!=null) {
      return inputStream;
    }
    
    if(fileNameWithPath.startsWith(File.separator) || fileNameWithPath.startsWith("/")) {
      return getInputStreamFromFileNameWithPath(fileNameWithPath.substring(1), loadRelatedTemp);
    } else if (fileNameWithPath.contains(File.separator) || fileNameWithPath.contains("/")) {
      return getInputStreamFromFileNameWithPath(getOnlyResourceName(fileNameWithPath), loadRelatedTemp);
    } else {
      String errorMsg = "Class, ClassLoader and local file IO cannot allocate file '"
          + filename + "'";
      Debug.printlnMessage(errorMsg);
      //System.err.println(errorMsg);
      //new FileNotFoundException(fileNameWithPath).printStackTrace();
      LinkedList<String> listOfRelatedFiles = getRelatedFiles(fileNameWithPath);
      printListOfRelatedFiles(filename, listOfRelatedFiles);
      if(!loadRelatedTemp && !filename.endsWith(".java") && !filename.endsWith(".class") && !listOfRelatedFiles.isEmpty()) {
        fileNameWithPath = listOfRelatedFiles.getFirst();
        System.out.println("#Try to load related file: "+fileNameWithPath);
        return getInputStreamFromFileNameWithPath(fileNameWithPath, true);
      } else {
        return null;
      }
    }
  }
  
  private static LinkedList<String> getRelatedFiles(String fileNameWithPath) {
    String resourceName = getOnlyResourceName(fileNameWithPath);
    LinkedList<String> listOfRelatedFiles = new LinkedList<String>();
    if(isJarLoaded()) {
      String jarPath = aReader.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      if(jarPath.endsWith("bin/")) {
        jarPath = jarPath.substring(0, jarPath.lastIndexOf("bin/"));
      }
      if(jarPath.endsWith(File.separator+"bin"+File.separator)) {
        jarPath = jarPath.substring(0, jarPath.lastIndexOf("bin"+File.separator));
      }
      jarPath = jarPath + "Animal.jar";
      try {
            @SuppressWarnings("resource")
            JarFile jarFile = new JarFile(AnimalReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            @SuppressWarnings("rawtypes")
            Enumeration allEntries = jarFile.entries();
            while (allEntries.hasMoreElements()) {
                JarEntry entry = (JarEntry) allEntries.nextElement();
                String path = entry.getName();
                if(path.endsWith(resourceName)) {
                  listOfRelatedFiles.add(path);
                } else if(resourceName.endsWith(".java") && path.endsWith(resourceName.replaceAll(".java", ".class"))) {
                  listOfRelatedFiles.add(path);
                }
            }
      } catch (IOException | URISyntaxException e) {
        e.printStackTrace();
      }
    } else {
      String directoryName = aReader.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      if(directoryName.startsWith(File.separator)) {
        directoryName = directoryName.substring(1);
      } else if(directoryName.startsWith("/")) {
        directoryName = directoryName.substring(1);
      }
      if(directoryName.contains(File.separator+"bin"+File.separator)) {
        directoryName = directoryName.replaceFirst(File.separator+"bin"+File.separator, "");
      } else if(directoryName.contains("/bin/")) {
        directoryName = directoryName.replaceFirst("/bin/", "");
      }
      for(String path : listOfFilesInDirectory(directoryName)) {
        if(path.endsWith(resourceName)) {
          listOfRelatedFiles.add(path);
        } else if(resourceName.endsWith(".java") && path.endsWith(resourceName.replaceAll(".java", ".class"))) {
          listOfRelatedFiles.add(path);
        }
      }
    }
    return listOfRelatedFiles;
  }
  
  private static void printListOfRelatedFiles(String resourceName, LinkedList<String> listOfRelatedFiles) {
    if(!listOfRelatedFiles.isEmpty()) {
      System.out.println("Related Files of \""+resourceName+"\":");
      for(String relatedFile : listOfRelatedFiles) {
        System.out.println("\t"+relatedFile);
      }
    } else {
      System.out.println("There are no related Files of \""+resourceName+"\"!");
    }
  }
  
  private static LinkedList<String> listOfFilesInDirectory(String directoryName) {
    LinkedList<String> listOfFiles = new LinkedList<String>();
    File directory = new File(directoryName);
    File[] fList = directory.listFiles();
    for (File file : fList) {
        if (file.isFile()) {
          listOfFiles.add(file.getPath());
        } else if (file.isDirectory()) {
          listOfFiles.addAll(listOfFilesInDirectory(file.getAbsolutePath()));
        }
    }
    return listOfFiles;
  }
  
  public static boolean isJarLoaded() {
    return ClassLoader.getSystemResourceAsStream("")==null && !aReader.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().endsWith("/bin/");
  }
  
  public static void printRelatedFiles(String resourceName) {
    printListOfRelatedFiles(resourceName, getRelatedFiles(resourceName));
  }
  
  public static Date getLastModifiedDateFromJar() {
    try {
      return isJarLoaded() ? new Date(new File(AnimalReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).lastModified()) : null;
    } catch (Exception e) {
      return null;
    }
  }
  
  public static String getNameOfJar() {
    try {
      return isJarLoaded() ? new File(AnimalReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getName() : null;
    } catch (Exception e) {
      return null;
    }
  }
  
  public static LinkedList<JarEntry> getAllEntriesInJar() {
    LinkedList<JarEntry> listWithJarEntries = new LinkedList<JarEntry>();
    if(isJarLoaded()) {
      try {
        @SuppressWarnings("resource")
        JarFile jarFile = new JarFile(AnimalReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        @SuppressWarnings("rawtypes")
        Enumeration allEntries = jarFile.entries();
        while (allEntries.hasMoreElements()) {
            JarEntry entry = (JarEntry) allEntries.nextElement();
            listWithJarEntries.add(entry);
        }
      } catch (IOException | URISyntaxException e) {
        e.printStackTrace();
      }
    }
    return listWithJarEntries;
  }
  
  public static ImageIcon getImageIcon(InputStream in) {
    ImageIcon icon = null;
    if(in != null) {
      BufferedImage bf;
      try {
        bf = ImageIO.read(in);
        icon = new ImageIcon(bf);
      } catch (IOException e) {
        //e.printStackTrace();
//        MessageDisplay.errorMsg("iconLoadingFailed", name + " - 1",
//            MessageDisplay.CONFIG_ERROR);
      }
    }
    return icon;
  }
  
}
