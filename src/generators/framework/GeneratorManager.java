package generators.framework;

import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import translator.Translator;

public class GeneratorManager {

	private static Translator trans = new Translator("generator", Locale.US);

	private static HashMap<String, DefaultMutableTreeNode> 	generators; // =  new HashMap<String, DefaultMutableTreeNode>(101);
//  private static HashMap<String, Generator>  actualGenerators; // =  new HashMap<String, DefaultMutableTreeNode>(101);
	public static HashMap<String, Vector<String>> contentAuthors = new HashMap<String, Vector<String>>(
			237);
	
	public static HashMap<String, Generator> name2Generator;
	public static HashMap<Generator, DefaultMutableTreeNode> generator2Node;
	
	private static int nrGenerators = 0;

	public static HashMap<Integer, String> packageMapper = new HashMap<Integer, String>(
			23);

	public static final String BASE_DIRECTORY_NAME = "generators";
	public static boolean mode = false;
	
	static {
		packageMapper.put(GeneratorType.GENERATOR_TYPE_BACKTRACKING,
				BASE_DIRECTORY_NAME + ".backtracking");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_COMPRESSION,
				BASE_DIRECTORY_NAME + ".compression");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_CRYPT,
				BASE_DIRECTORY_NAME + ".cryptography");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE,
				BASE_DIRECTORY_NAME + ".datastructures");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_GRAPH,
				BASE_DIRECTORY_NAME + ".graph");
    packageMapper.put(GeneratorType.GENERATOR_TYPE_GRAPHICS,
        BASE_DIRECTORY_NAME + ".graphics");
    packageMapper.put(GeneratorType.GENERATOR_TYPE_HARDWARE,
        BASE_DIRECTORY_NAME + ".hardware");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_HASHING,
				BASE_DIRECTORY_NAME + ".hashing");
    packageMapper.put(GeneratorType.GENERATOR_TYPE_MATHS,
        BASE_DIRECTORY_NAME + ".maths");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_MORE,
				BASE_DIRECTORY_NAME + ".misc");
    packageMapper.put(GeneratorType.GENERATOR_TYPE_NETWORK,
        BASE_DIRECTORY_NAME + ".network");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_SEARCH,
				BASE_DIRECTORY_NAME + ".searching");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_SORT,
				BASE_DIRECTORY_NAME + ".sorting");
		packageMapper.put(GeneratorType.GENERATOR_TYPE_TREE,
				BASE_DIRECTORY_NAME + ".tree");
	}

	/**
	 * A private constructor to adapt the standards for an utility class.
	 */
	private GeneratorManager() {
		super();
	}

	public static void putGenerator(String chainPath,
			DefaultMutableTreeNode node) {
		getGenerators().put(chainPath, node);
	}

	static DefaultMutableTreeNode findOrCreatePath(String... pathInfo) {
		DefaultMutableTreeNode current = generators.get("/"), lookup = null;
		StringBuilder sb = new StringBuilder(128);
		for (String s : pathInfo) {
			sb.append('/').append(s);
			lookup = generators.get(sb.toString());
			if (lookup == null)
				lookup = addCategory(s, sb.toString(), current);
			current = lookup;
		}
		return current;
	}

	 static DefaultMutableTreeNode findOrCreateExplicitPath(String p1, String p2, String p3, String p4) {
	    DefaultMutableTreeNode current = generators.get("/"), lookup = null;
	    StringBuilder sb = new StringBuilder(128);
	    String[] pathInfo = new String[]{p1, p2, p3, p4};
	    for (String s : pathInfo) {
	      sb.append('/').append(s);
	      lookup = generators.get(sb.toString());
	      if (lookup == null)
	        lookup = addCategory(s, sb.toString(), current);
	      current = lookup;
	    }
	    return current;
	  }

	static void addGenerator(Generator generator, String chainPath,
			DefaultMutableTreeNode parent) {
		GeneratorTreeNode node = new GeneratorTreeNode(generator);
		StringBuffer name = new StringBuffer(256);
		name.append(chainPath).append("/");
		name.append(generator.getName().replace(' ', '_'));
		while (generators.get(name.toString()) != null) {
//		  System.out.println("adapting path " +name.toString());
		  name.append('_');
    }
		generators.put(name.toString(), node);
//		actualGenerators.put(generator.getClass().getName(), generator);
//		generators.put(chainPath + "/" + generator.getName(), node);
		if (parent != null)
			parent.add(node);
		String authorKey = generator.getAnimationAuthor();
		String className = generator.getClass().getName();
		if (!name2Generator.containsKey(className)) {
		  name2Generator.put(generator.getClass().getName(), generator);
//		  System.out.println(className +" => " +generator);
		}
		if (!generator2Node.containsKey(generator)) {
		  generator2Node.put(generator, node);
		}
		// new: split entries
		String[] authors = authorKey.split(",");
		for (String currentAuthor : authors) {
	    Vector<String> targetVector = null;
		  String thisAuthor = currentAuthor.trim();
		  // next lines: was authorKey, no split and for loop
		  if (!contentAuthors.containsKey(thisAuthor)) {
		    targetVector = new Vector<String>(5, 10);
		    contentAuthors.put(thisAuthor, targetVector);
		  } else {
		    targetVector = contentAuthors.get(thisAuthor);
		  }
	    StringBuilder contentEntry = new StringBuilder(100);
	    contentEntry.append("<li>").append(generator.getName());
	    contentEntry.append("</li>");
	    targetVector.add(contentEntry.toString());
		}
		nrGenerators++;
	}

	public static DefaultMutableTreeNode clearGenerators() {
		generators = new HashMap<String, DefaultMutableTreeNode>(803);
		generator2Node = new HashMap<Generator, DefaultMutableTreeNode>(803);
		name2Generator = new HashMap<String, Generator>(881);
		return addCategory("Generators", "/", null);
	}
	
	public static void createGenerators() {
		nrGenerators = 0;
		createGeneratorsForPackage("generators");
		for (String key : packageMapper.values()) {
			createGeneratorsForPackage(key);
		}
		//
		
//		System.err.println("Total " + nrGenerators + " generators loaded.");
	}

	public static void checkGenerators() {
		if ((generators == null) || (generators.size() < 2)) {			
			clearGenerators();
			createGenerators();
		}
	}
	/**
	 * @return the nrGenerators
	 */
	public static int getNrGenerators() {
		return nrGenerators;
	}

	private static void createGeneratorsForPackage(String packageName) {
//		int count = 0;

		Class<?> generatorDummy;
		try {
			generatorDummy = Class.forName(packageName + ".DummyGenerator");
			try {
				Object dummy = generatorDummy.newInstance();
				if (dummy instanceof GeneratorBundle) {
					Vector<Generator> localGens = ((GeneratorBundle) dummy)
							.getGenerators();
					for (Generator g : localGens) {
						// System.err.println(g.toString());
						insertGenerator(g);
//						count++;
					}
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		// System.err.println("***Done with package " +packageName +", count = "
		// +count);
	}

	/**
	 * @return the generators
	 */
	public static HashMap<String, DefaultMutableTreeNode> getGenerators() {
		return generators;
	}
	
//	 public static Generator getGenerator(String className) {
//	   System.err.println(generators.size() + " // " +actualGenerators.size());
//	   StringBuilder sb = new StringBuilder(32768);
//	   for (String s : actualGenerators.keySet())
//	     sb.append(s).append(',');
//	   System.err.println(sb.toString());
//	   System.err.println(actualGenerators.get(className));
//	   return actualGenerators.get(className);
//	  }

	static DefaultMutableTreeNode addCategory(String category,
			String chainPath, DefaultMutableTreeNode baseNode) {
		GeneratorNode node = new GeneratorNode(category);
		putGenerator(chainPath, node);
		// generators.put(chainPath, node);
		if (baseNode != null)
			baseNode.add(node);
		return node;

	}

	public static void insertGenerator(Generator gen) {
		if (gen == null)
			return;
		// Collect data:
		int generatorType = gen.getGeneratorType().getType();
		String plUsed = gen.getOutputLanguage(); // oops!
		String languageCode = gen.getContentLocale().getLanguage();
		String algoBaseName = gen.getAlgorithmName();
		StringBuilder genPath = new StringBuilder(64);
		genPath.append('/').append(languageCode).append('/').append(plUsed)
				.append('/');
		genPath.append(trans.translateMessage(String.valueOf(generatorType)));
		genPath.append('/').append(algoBaseName);
		if (GeneratorManager.mode) {

		  DefaultMutableTreeNode parent = findOrCreatePath(languageCode, plUsed,
		      trans.translateMessage(String.valueOf(generatorType)),
		      algoBaseName);
		  addGenerator(gen, genPath.toString(), parent);
		} else {
		  // GR Try
		  String assembly = "/"+trans.translateMessage(String.valueOf(generatorType))+"/"+algoBaseName+"/"+plUsed+"/"+languageCode;
		  DefaultMutableTreeNode parentNode = findOrCreatePath(trans.translateMessage(String.valueOf(generatorType)),
		      algoBaseName, plUsed, languageCode);
		  addGenerator(gen, assembly, parentNode);
		}
	}

}
