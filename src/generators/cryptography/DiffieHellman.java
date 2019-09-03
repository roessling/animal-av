package generators.cryptography;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.cryptography.helpers.DiffieHellmanView;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class DiffieHellman implements ValidatingGenerator {
//    private Language lang;
    private int g;
    private int b;
    private int a;
    private int p;
    private int A;
    private int B;
    private int KA;
    private int KB;
    
    private DiffieHellmanView view;
    
    
    public DiffieHellman(){
      view = new DiffieHellmanView();
    }

    public DiffieHellman(Language l) {
    	view = new DiffieHellmanView(l);
    }

    public void init(){
    }
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
      
      fetchParameters(primitives);
      
      view.init(props);
      view.setStepMode();
      
		  doAlgorithmSteps(p, g);
		  
		  return view.langToString();
    }
    
    private void doAlgorithmSteps(int p, int g) {
    	      
      this.p = p;
      this.g = g;
      
      view.initForAlgorithm();
      view.pAndG(p, g);
      view.random_a(a);
      view.random_b(b);
      
      A = calcA();
      CheckpointUtils.checkpointEvent(this, "AEvent", new Variable ("A", A));
      view.calcA(A, g, a, p);
      
      view.sendAtoBob(A);
      
      B = calcB();
      CheckpointUtils.checkpointEvent(this, "BEvent", new Variable ("B", B));
      view.calcB(B, g, b, p);
      
      KB = calcK_B();
      CheckpointUtils.checkpointEvent(this, "KBEvent", new Variable ("KB", KB));
      view.calcK_B(KB, g, b, p);
      
      view.sendBtoAlice(B);
      
      KA = calcK_A();
      CheckpointUtils.checkpointEvent(this, "KAEvent", new Variable ("KA", KA));
      view.calcK_A(KA, B, a, p);
      
    }

    private int calcA() {
      int f = (int) Math.pow(g, a);
      int A = f % p;
      return A;
    }
    
    private int calcB() {
      int B = ((int) Math.pow(g, b)) % p;
      return B;
    }
    
    private int calcK_B() {
      int KB = ((int)Math.pow(A, b)) % p;
      return KB;
    }
    
    private int calcK_A() {
      int KA = ((int) Math.pow(B, a)) % p;
      return KA;
    }
    
    
    private void fetchParameters(Hashtable<String, Object> primitives) {
      
      if (primitives.get("g") instanceof String) {
        try {
          g = Integer.parseInt((String) primitives.get("g"));
        } catch (Exception e) {
        }
      }
      else g = (Integer)primitives.get("g");
      
      if (primitives.get("b") instanceof String) {
        try {
          b = Integer.parseInt((String) primitives.get("b"));
        } catch (Exception e) {
        }
      }
      else b = (Integer)primitives.get("b");
      
           
      if (primitives.get("a") instanceof String) {
         try {
           a = Integer.parseInt((String) primitives.get("a"));
        } catch (Exception e) {
        }
      }
      else a = (Integer)primitives.get("a");
      
      if (primitives.get("p") instanceof String) {
         try {
           p = Integer.parseInt((String) primitives.get("p"));
         } catch (Exception e) {
         }
      }
      else p = (Integer)primitives.get("p");
    }
    
    public String getName() {
      return "Diffie-Hellman Schlüsselaustausch";
    }

    public String getAlgorithmName() {
      return "Diffie-Hellman";
    }

    public String getAnimationAuthor() {
      return "Kristijan Madunic";
    }

    public String getDescription(){
      return "Der Diffie-Hellman-Schl&uuml;sselaustausch ist ein Protokol der Kryptografie."
                +"\n"
                +"Dieser beschreibt die M&ouml;glichkeit zum sicheren Austausch von symetrischen"
                +"\n"
                +"Schl&uuml;sseln &uuml;ber unsichere Kan&auml;le. Der Algorithmus wurde von Martin Hellman\""
                +"\n"
                +"gemeinsam mit Whitfield Diffie und Ralph Merkle an der Universit&auml;"
                +"\n"
                +"von Stanford ( Kalifornien; USA ) entwickelt und 1976 ver&uuml;ffentlicht.";
    }

    public String getCodeExample(){
      return "1. Alice und Bob einigen sich auf eine Primazahl p und Primitivwurzel g"
                +"\n"
                +"2. Alice w&auml;hlt eine Zufallszahl a"
                +"\n"
                +"3. Bob w&auml;hlt eine Zufallszahl b"
                +"\n"
                +"4. Alice berechnet A = g^a mod p"
                +"\n"
                +"5. Alice schickt A an Bob"
                +"\n"
                +"6. Bob berechnet B = g^b mod p"
                +"\n"
                +"7. Bob berechnet K = A^b mod p"
                +"\n"
                +"8. Bob schickt B an Alice"
                +"\n"
                +"9. Alice berechnet K = B^a mod p";
    }

    public String getFileExtension(){
      return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
      return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
      return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
      return Generator.PSEUDO_CODE_OUTPUT;
    }
  
    public static void main(String[] args) {
      Language l = new AnimalScript("Diffie-Hellman-Schluesselaustausch", "Kristijan Madunic", 640, 480);
      DiffieHellman hellman = new DiffieHellman(l);
      hellman.doAlgorithmSteps(13, 2);
      System.out.println(l);  
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
      
      if (primitives.get("g") instanceof String) {
        try {
          g = Integer.parseInt((String) primitives.get("g"));
        } catch (Exception e) {
          return false;
        }
      }
      else g = (Integer)primitives.get("g");
      
      if (primitives.get("b") instanceof String) {
        try {
          b = Integer.parseInt((String) primitives.get("b"));
        } catch (Exception e) {
          return false;
        }
      }
      else b = (Integer)primitives.get("b");
      
           
      if (primitives.get("a") instanceof String) {
         try {
           a = Integer.parseInt((String) primitives.get("a"));
        } catch (Exception e) {
          return false;
        }
      }
      else a = (Integer)primitives.get("a");
      
      if (primitives.get("p") instanceof String) {
         try {
           p = Integer.parseInt((String) primitives.get("p"));
         } catch (Exception e) {
           return false;
         }
      }
      else p = (Integer)primitives.get("p");

      return true;
    }
}