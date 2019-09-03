package generators.generatorframe.loading;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.framework.GeneratorType;

/**
 * 
 * @author Nora Wester
 *
 */

public class Loader {

	Class<?> dummyClass;
	GeneratorBundle bundle;
	Vector<Generator> generators;
	
	public Loader(String category){ 
		Generator localGenerator = Animal.localGenerator;
		if(category.equals("#local") && localGenerator!=null){
			generators = new Vector<>();
			generators.add(localGenerator);
			return;
		}
		
		try {
	        dummyClass = Class
	            .forName(setRightName(category));
	      } catch (ClassNotFoundException cfe) {
	        System.err.println("DummyGenerator could not be found!");
	      }
		
		try {
			bundle = (GeneratorBundle) dummyClass.newInstance();
			generators = bundle.getGenerators();
			//generators sortieren
			Collections.sort(generators, new Comparator<Generator>(){

				@Override
				public int compare(Generator arg0, Generator arg1) {
					// TODO Auto-generated method stub
					return arg0.getAlgorithmName().compareTo(arg1.getAlgorithmName());
				}
				
			});
			
//      java.util.HashMap<String,Generator> hm = GeneratorsMap.generatorMap;
//			for (Generator g : generators) {
//			  String s = generateChainPathName(g);
////			  System.out.println(s);
//			  hm.put(s, g);
//			}
//			System.out.println(hm.keySet());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("No GeneratorBundle");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("No GeneratorBundle");
		}
	}
	
	// GR
	@SuppressWarnings("unused")
	private String generateChainPathName(Generator g) {
	  StringBuilder sb = new StringBuilder(256);
	  sb.append('/').append(g.getContentLocale().getLanguage()).append('/');
	  sb.append(g.getOutputLanguage()).append('/');
	  sb.append(GeneratorType.getStringForType(g.getGeneratorType().getType())).append('/');
	  sb.append(g.getAlgorithmName()).append('/');
	  sb.append(g.getName());
	  return sb.toString();
	}

	private String setRightName(String category) {
		// TODO Auto-generated method stub
		String name = "generators." + category + ".DummyGenerator";
		
		if(category.compareTo("crypt") == 0){
			name = "generators.cryptography.DummyGenerator";
		}
		
		if(category.compareTo("more") == 0){
			name = "generators.misc.DummyGenerator";
		}
		
		if(category.compareTo("search") == 0){
			name = "generators.searching.DummyGenerator";
		}
		
		if(category.compareTo("sort") == 0){
			name = "generators.sorting.DummyGenerator";
		}
		
		return name;
	}


	protected Vector<Generator> getGeneratorList(){
		return generators;
	}

}
