package examples.Exercises;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import prerender.PreRender;

public class ExercisesHelpFunctions {

	public static Object[] getAllExercises() {
		List<String> lines = PreRender.getLineListFromFile(PreRender.FILE_EXERCISES);
		HashMap<String, LinkedList<String>> exercisesMap = new HashMap<String, LinkedList<String>>();
		for(String l : lines){
			String[] infos = l.split("/");
			if(!exercisesMap.containsKey(infos[0])){
				exercisesMap.put(infos[0], new LinkedList<String>());
			}
			LinkedList<String> list = exercisesMap.get(infos[0]);
			list.add(infos[1]);
			exercisesMap.put(infos[0], list);
		}
		
		List<String> categories = new LinkedList<String>();
		for (String key  : exercisesMap.keySet()){
			categories.add(key);
		}
		Object[] arrAllExercises = new Object[categories.size()+1];
		arrAllExercises[0] = "Exercises";
		for(int i=0 ; i<categories.size() ; i++){
			String c = categories.get(i);
			List<String> exercises = exercisesMap.get(c);
			Object[] arrCatExercises = new Object[exercises.size()+1];
			arrCatExercises[0] = c;
			for(int j=0 ; j<exercises.size() ; j++){
				String e = exercises.get(j);
				arrCatExercises[j+1] = e;
			}
			arrAllExercises[i+1] = arrCatExercises;
		}
		
		return arrAllExercises;
	}

	
	
//	public static Object[] getAllExercisesOld() {
//		if (ExercisesHelpFunctions.class.getProtectionDomain().getCodeSource().getLocation().toString().equals("rsrc:./")){
//			HashMap<String, LinkedList<String>> exercisesMap = getAllFilesFromJar();
//			List<String> categories = new LinkedList<String>();
//			for (String key  : exercisesMap.keySet()){
//				categories.add(key);
//			}
//			Object[] arrAllExercises = new Object[categories.size()+1];
//			arrAllExercises[0] = "Exercises";
//			for(int i=0 ; i<categories.size() ; i++){
//				String c = categories.get(i);
//				List<String> exercises = exercisesMap.get(c);
//				Object[] arrCatExercises = new Object[exercises.size()+1];
//				arrCatExercises[0] = c;
//				for(int j=0 ; j<exercises.size() ; j++){
//					String e = exercises.get(j);
//					arrCatExercises[j+1] = e;
//				}
//				arrAllExercises[i+1] = arrCatExercises;
//			}
//			return arrAllExercises;
//		}else{
//			List<String> categories = getListFromDic("");
//			categories.remove("ExercisesHelpFunctions.class");
//			Object[] arrAllExercises = new Object[categories.size()+1];
//			arrAllExercises[0] = "Exercises";
//			for(int i=0 ; i<categories.size() ; i++){
//				String c = categories.get(i);
//				List<String> exercises = getListFromDic(c);
//				Object[] arrCatExercises = new Object[exercises.size()+1];
//				arrCatExercises[0] = c;
//				for(int j=0 ; j<exercises.size() ; j++){
//					String e = exercises.get(j);
//					arrCatExercises[j+1] = e;
//				}
//				arrAllExercises[i+1] = arrCatExercises;
//			}
//			return arrAllExercises;
//		}
//	}
//
//	public static List<String> getListFromDic(String subDic) {
//		List<String> filenames = new ArrayList<>();
//		try (
//				InputStream in = ExercisesHelpFunctions.class.getResourceAsStream(subDic);
//				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//			String resource;
//			while ((resource = br.readLine()) != null) {
//				if(resource.endsWith(".xml") || !resource.contains(".")){
//					filenames.add(resource);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return filenames;
//	}
//	
//	private static HashMap<String, LinkedList<String>> getAllFilesFromJar(){
//		HashMap<String, LinkedList<String>> exercisesMap = new HashMap<String, LinkedList<String>>();
//		final String path = "examples/Exercises/";
//		String pathJar = new File(".").getAbsolutePath();
//		pathJar = pathJar.substring(0,pathJar.length()-1) + "Animal.jar";
//		File jarFile = new File(pathJar);
//		try{
//			if(!jarFile.exists()){
//				String animalJarFileName = "Animal.jar";
//				for (final File fileEntry : new File(pathJar.substring(0,pathJar.length()-1)).listFiles()) {
//			        if (!fileEntry.isDirectory() && fileEntry.getName().contains("Animal") && fileEntry.getName().endsWith(".jar")) {
//			        	animalJarFileName = fileEntry.getName();
//			        	break;
//			        }
//			    }
//				pathJar = pathJar.substring(0,pathJar.length()-1) + animalJarFileName;
//				jarFile = new File(pathJar);
//			}
//			if(jarFile.exists() && jarFile.isFile()) {  // Run with JAR file
//			    final JarFile jar = new JarFile(jarFile);
//			    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
//			    while(entries.hasMoreElements()) {
//			        String name = entries.nextElement().getName();
//			        if (name.startsWith(path)) { //filter according to the path
//			        	name = name.replace(path, "");
//			        	if(name.contains("/")){
//			        		if(name.endsWith("/")){
//			        			exercisesMap.put(name.replace("/", ""), new LinkedList<>());
//			        		}else{
//			        			String[] infos = name.split("/");
//			        			if(infos[1].endsWith(".xml")){
//				        			LinkedList<String> list = exercisesMap.get(infos[0]);
//				        			list.add(infos[1]);
//				        			exercisesMap.put(infos[0], list);
//			        			}
//			        		}
//			        	}
//			        }
//			    }
//			    jar.close();
//			}else{
//				System.out.println("No Jar!");
//			}
//		} catch (IOException e) {
//		}
//		return exercisesMap;
//	}
}
