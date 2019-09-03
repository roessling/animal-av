package prerender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;

import animal.misc.MessageDisplay;
import generators.framework.Generator;
import generators.framework.GeneratorManager;
import generators.generatorframe.loading.GeneratorLoader;
import generators.generatorframe.store.SaveInfos;
import helpers.AnimalReader;

@SuppressWarnings("unused")
public class PreRender {

	public static final String PATH_BIN = PreRender.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	public static final String PATH_SRC = PATH_BIN.replace("/bin/", "/src/");
	public static final String PATH_EXERCISES = PATH_SRC+"examples/Exercises/";
	public static final String FILE_PATH = "prerender/";
  public static final String FILE_EXERCISES = "TextFileExercises.txt";
  public static final String FILE_LOADEDGENERATORS = "TextFileWithAllGeneratorsLoaded.txt";

	public static void main(String[] args) {
		makeTextFileExercises();
//		getLineListFromFile(FILE_EXERCISES);
//		makeTextFileWithAllGeneratorsLoaded();
	}
	
	private static boolean writeListToFile(File file, LinkedList<String> list){
		System.out.print("\t"+"Built File ("+file.getAbsolutePath()+") --> ");
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			for(String s: list) {
				writer.write(s+"\n");
			}
			writer.close();
			System.out.println("Successful");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Failed");
			return false;
		}
		return true;
	}
	

	@SuppressWarnings("unused")
	private static LinkedList<String> getListOfAllInFolder(String pathFolder){
		LinkedList<String> list = new LinkedList<String>();
		list.addAll(getListOfFilesInFolder(pathFolder));
		list.addAll(getListOfFoldersInFolder(pathFolder));
		return list;
	}
	private static LinkedList<String> getListOfFilesInFolder(String pathFolder, String ending){
		LinkedList<String> list = new LinkedList<String>();
		File folder = new File(pathFolder);
		if(folder.exists() && folder.isDirectory()){
			for (final File fileEntry : folder.listFiles()) {
				if(!fileEntry.isDirectory() && (ending==null || fileEntry.getName().endsWith(ending))){
					list.add(fileEntry.getName());
				}
			}
	    }
		return list;
	}
	private static LinkedList<String> getListOfFilesInFolder(String pathFolder){
		return getListOfFilesInFolder(pathFolder, null);
	}
	private static LinkedList<String> getListOfFoldersInFolder(String pathFolder){
		LinkedList<String> list = new LinkedList<String>();
		File folder = new File(pathFolder);
		if(folder.exists() && folder.isDirectory()){
			for (final File fileEntry : folder.listFiles()) {
				if(fileEntry.isDirectory()){
					list.add(fileEntry.getName());
				}
			}
	    }
		return list;
	}
	
	public static LinkedList<String> getLineListFromFile(File file){
		LinkedList<String> list = new LinkedList<String>();
		if(file!=null && file.exists() && file.isFile()){
			Scanner s;
			try {
				s = new Scanner(file);
				while (s.hasNextLine()){
					String text = s.nextLine();
				    list.add(text);
				}
				s.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("File not found: "+file.getAbsolutePath());
		}
		return list;
	}
	
	public static LinkedList<String> getLineListFromFile(String filePath){
	  //MessageDisplay.message("Load: "+filePath);
		LinkedList<String> list = new LinkedList<String>();
//    InputStream stream = PreRender.class.getResourceAsStream(filePath);
    InputStream stream = AnimalReader.getInputStreamOnLayer(PreRender.class, filePath);
		if(stream!=null){
			Scanner s = new Scanner(stream);
			while (s.hasNextLine()){
				String text = s.nextLine();
			    list.add(text);
			}
			s.close();
		}else{
			System.out.println("File not found: "+filePath);
		}
		return list;
	}
	
	
	
	
	
	private static void makeTextFileExercises(){
		System.out.println("makeTextFileExercises:");
		System.out.println("\t"+"Parsing --> ");
		LinkedList<String> listAllExercisesPaths = new LinkedList<String>();
		LinkedList<String> listCategories = getListOfFoldersInFolder(PATH_EXERCISES);
		for(String c : listCategories){
			LinkedList<String> listExercises = getListOfFilesInFolder(PATH_EXERCISES+"/"+c,".xml");
			for(String e : listExercises){
				String exerciseFilePath = c + "/" +e;
				listAllExercisesPaths.add(exerciseFilePath);
				System.out.println("\t\t"+exerciseFilePath);
			}
		}

		writeListToFile(new File(PATH_SRC+FILE_PATH+FILE_EXERCISES), listAllExercisesPaths);
		//writeListToFile(new File(PATH_BIN+FILE_PATH+FILE_EXERCISES), listAllExercisesPaths);
	}
	


  public static void makeTextFileWithAllGeneratorsLoaded() {
    System.out.println("makeTextFileWithAllGeneratorsLoaded:");
    System.out.println("\t"+"Parsing --> ");
    
    LinkedList<Generator> listOfGenerators = new LinkedList<Generator>();
    
    LinkedList<Object[]> listOfGeneratorsArray = new LinkedList<Object[]>();
    new GeneratorLoader();
    SaveInfos sI = SaveInfos.getInstance();
    for (Entry<String, Integer> entry : sI.hm.entrySet()) {
      @SuppressWarnings("unused")
      String key = entry.getKey();
      Integer value = entry.getValue();
      Generator g = sI.getGenerator(value);
      listOfGeneratorsArray.add(new Object[] {g,key});
    }
    
    // Sortiert nach Name
    Collections.sort(listOfGeneratorsArray, new Comparator<Object[]>() {
      @Override
      public int compare(Object[] o1, Object[] o2) {
          Generator g1 = ((Generator) o1[0]);
          Generator g2 = ((Generator) o2[0]);
          return Collator.getInstance().compare(g1.getAlgorithmName(), g2.getAlgorithmName());
      }
    });
    
    for(Object[] gen : listOfGeneratorsArray) {
      Generator g = ((Generator) gen[0]);
      listOfGenerators.add(g);
    }

    LinkedList<String> listOfGeneratorStrings = new LinkedList<String>();
    for(Generator gen : listOfGenerators) {
      String packageString = GeneratorManager.packageMapper.get(gen.getGeneratorType().getType());
      packageString = packageString.replace("generators.", "");
      String category = Character.toUpperCase(packageString.charAt(0)) + packageString.substring(1);
      String genString = gen.getAlgorithmName()+" {"+gen.getName()+"}"+"\t"+gen.getContentLocale()+"\t"+gen.getOutputLanguage()+"\t"+category+"\t"+gen.getAnimationAuthor();
      System.out.println("\t\t"+genString);
      listOfGeneratorStrings.add(genString);
    }
    System.out.println("\t --> "+listOfGenerators.size()+" Generators added!");
    writeListToFile(new File(PATH_SRC+FILE_PATH+FILE_LOADEDGENERATORS), listOfGeneratorStrings);
  }

}
