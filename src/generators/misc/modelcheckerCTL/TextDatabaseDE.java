/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL;

import generators.misc.modelcheckerCTL.token.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextDatabaseDE extends TextDatabase{

  public  TextDatabaseDE() {
      super();
    }

    @Override
   void initFormulaFunctionTexts() {
    formulaFunctionTexts = new HashMap<>();

    List<String> tmp = new ArrayList<>();
    tmp.add("Atomare Formel");
    formulaFunctionTexts.put(Type.TERMINAL, tmp);

    tmp = new ArrayList<>();
    tmp.add(Type.TRUE.toString());
    formulaFunctionTexts.put(Type.TRUE, tmp);

    tmp = new ArrayList<>();
    tmp.add(Type.FALSE.toString());
    formulaFunctionTexts.put(Type.FALSE, tmp);

    tmp = new ArrayList<>();
    tmp.add(Type.NOT.toString()+psi+" : SAT("+psi+");");
    formulaFunctionTexts.put(Type.NOT, tmp);

    tmp = new ArrayList<>();
    tmp.add(phi1+" "+Type.AND.toString()+" "+phi2+" : SAT("+phi1+"); SAT("+phi2+");");
    formulaFunctionTexts.put(Type.AND, tmp);

    tmp = new ArrayList<>();
    tmp.add(phi1+" "+Type.OR.toString()+" "+phi2+" : SAT("+phi1+"); SAT("+phi2+");");
    formulaFunctionTexts.put(Type.OR, tmp);

    tmp = new ArrayList<>();
    tmp.add(phi1+" "+Type.IMPL.toString()+" "+phi2+" : SAT( "+Type.NOT.toString()+phi1+" ); SAT( "+phi2+" );");
    formulaFunctionTexts.put(Type.IMPL, tmp);

    tmp = new ArrayList<>();
    tmp.add(Type.EX.toString()+" "+psi+" :");
    tmp.add("begin");
    tmp.add("SAT( "+psi+" )");
    tmp.add("markiere alle Zustände"+state+" aus "+stateSet+" mit "+phi+",");
    tmp.add("für die es einen Zustand "+state0+" gibt,");
    tmp.add("mit "+state+Type.IMPL+state0+" und");
    tmp.add("der bereits mit "+psi+" markiert ist");
    tmp.add("end");
    formulaFunctionTexts.put(Type.EX, tmp);

    tmp = new ArrayList<>();
    tmp.add(Type.AF.toString()+" "+psi+" :");
    tmp.add("begin");
    tmp.add("SAT( "+psi+" );");
    tmp.add(Yset+" ist die Menge aller Zustände,");
    tmp.add("die mit "+psi+" markiert wurden;");
    tmp.add(Xset+"="+stateSet+";");
    tmp.add("repeat until "+Xset+"="+Yset);
    tmp.add(Xset+":="+Yset+";");
    tmp.add("füge zu "+Yset+" Zustände "+state+" aus "+stateSet+" hinzu,");
    tmp.add("für die für alle Zustände "+state0);
    tmp.add("mit "+state+Type.IMPL+state0+" gilt, dass "+state0+" in "+Yset+" ist");
    tmp.add("end repeat");
    tmp.add("markiere alle Zustände in "+Yset+" mit "+phi);
    tmp.add("end");
    formulaFunctionTexts.put(Type.AF, tmp);

    tmp = new ArrayList<>();
    tmp.add(Type.EU_START.toString()+" "+phi1+Type.EU_END.toString()+" "+phi2+" :");
    tmp.add("begin");
    tmp.add("SAT( "+phi1+" ); SAT( "+phi2+" );");
    tmp.add(Yset+" ist die Menge aller Zustände,");
    tmp.add("die mit "+phi2+" markiert wurden;");
    tmp.add(Xset+"="+stateSet+";");
    tmp.add("repeat until "+Xset+"="+Yset);
    tmp.add(Xset+":="+Yset+";");
    tmp.add("füge zu "+Yset+" Zustände "+state+" aus "+stateSet+" hinzu,");
    tmp.add("die mit "+phi1+" markiert sind");
    tmp.add("und für die es einen Zustand "+state0+" gibt");
    tmp.add("mit "+state+Type.IMPL+state0+", dass "+state0+" in "+Yset+" ist");
    tmp.add("end repeat");
    tmp.add("markiere alle Zustände in "+Yset+" mit "+phi);
    tmp.add("end");
    formulaFunctionTexts.put(Type.EU, tmp);
  }

  @Override
  void initFormulaInfoTexts() {
    formulaInfoTexts = new HashMap<>();

    List<String> tmp = new ArrayList<>();
    tmp.add("-markiere alle Zustände, welche die");
    tmp.add(" atomare Eigenschaft besitzen, mit "+phi);
    formulaInfoTexts.put(Type.TERMINAL, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere alle Zustände "+stateSet+" mit "+phi);
    formulaInfoTexts.put(Type.TRUE, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere keine Zustände");
    formulaInfoTexts.put(Type.FALSE, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere alle Zustände mit "+phi);
    tmp.add(" die nicht mit "+psi+" markiert sind");
    formulaInfoTexts.put(Type.NOT, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere alle Zustände mit "+phi);
      tmp.add(" die mit "+phi1+" UND "+phi2+" markiert sind");
    formulaInfoTexts.put(Type.AND, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere alle Zustände mit "+phi);
    tmp.add(" die mit "+phi1+" ODER "+phi2+" markiert sind");
    formulaInfoTexts.put(Type.OR, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere alle Zustände mit "+phi);
    tmp.add(" die mit "+Type.NOT+phi1+" oder "+phi2+" markiert sind");
    formulaInfoTexts.put(Type.IMPL, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere zuerst alle Zustände,");
    tmp.add(" in denen "+psi+" gilt");
    tmp.add("-in allen Vorgängern muss dann");
    tmp.add(" " +phi+" = "+Type.EX+psi+"  gelten");
    formulaInfoTexts.put(Type.EX, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere zuerst alle Zustände,");
    tmp.add(" in denen "+psi+" gilt");
    tmp.add("-füge jeweils Zustände hinzu, bei welchen ");
    tmp.add(" alle Nachfolger markiert sind");
    tmp.add("-Fixpunktiteration: Terminiere den Prozess,");
    tmp.add(" wenn keine neue Markierung gefunden");
      tmp.add(" wird");
    formulaInfoTexts.put(Type.AF, tmp);

    tmp = new ArrayList<>();
    tmp.add("-markiere zuerst alle Zustände mit "+phi+",");
    tmp.add(" in denen "+phi2+" gilt");
    tmp.add("-Fixpunktiteration: Markiere alle Vorgänger,");
    tmp.add(" wenn sie mit "+phi1+" markiert sind");
    formulaInfoTexts.put(Type.EU, tmp);
  }

  @Override
  void initIntroTexts() {
    introTexts = new HashMap<>();
    List<String> tmp = new ArrayList<>();
    tmp.add("Der Markierungsalgorithmus für CTL Model-Checking Probleme dient zur Überprüfung,");
    tmp.add("ob eine Formel auf der gegebenen Kripke-Struktur erfüllt ist. Dazu markiert er alle Zustände,");
    tmp.add("in denen die Formel gilt, und iteriert (von innen nach außen) über alle Teilformeln. ");
    tmp.add("Ist der Anfangszustand am Ende markiert, ist die Formel auf der Kripke-Struktur erfüllt.");
    introTexts.put("intro", tmp);

    tmp = new ArrayList<>();
    tmp.add("Kurzer Überblick: Kripke Strukturen");
    tmp.add("sind an endliche Automaten angelehnt, kennen aber keinen Endzustand, ein Pfad in einer Struktur");
    tmp.add("ist somit eine UNENDLICHE Sequenz (von Zuständen).");
    tmp.add("");
    tmp.add("Eine Kripke Struktur   M = ("+stateSet+",I,R,L)   über eine Menge P von Atomen ist definiert als:");
    tmp.add("-eine endliche Menge an Zuständen "+stateSet+"  und Anfangszuständen I aus S");
    tmp.add("-einer Übergangsrelation R");
    tmp.add("-einer Abbildung L, welche jedem Zustand aus S eine Menge von P-Atomen zuweist ");
    introTexts.put("kripke", tmp);

    tmp = new ArrayList<>();
    tmp.add("Kurzer Überblick: Computational Tree Logic");
    tmp.add("CTL erlaubt Aussagen über das temporale Verhalten eines Systems. Dazu wird Aussagenlogik um");
    tmp.add("Pfad- und Zustandsquantoren erweitert. Ausgewertet wird immer bezüglich einer Kripke-Struktur und");
    tmp.add("einem Anfangszustand. Die Quantoren immer als Paar (Pfad-Zustand) auf.");
    tmp.add("");
    tmp.add("Pfadquantoren:");
    tmp.add("A  \"für jeden Pfad gilt\"");
    tmp.add("E  \"es existiert ein Pfad auf dem gilt\"");
      tmp.add("");
      tmp.add("Zustandsquantoren:");
      tmp.add("X  \"im nächsten Zustand gilt\"");
      tmp.add("F  \"in Zukunft gilt irgendwann\"");
      tmp.add("G  \"in Zukunft gilt immer\"");
      tmp.add("U  \"es gilt eine Eigenschaft, bis eine andere gilt\"");

    introTexts.put("ctl", tmp);

    tmp = new ArrayList<>();
    tmp.add("Die genutzte minimale Quantorenmenge ist:  EX, AF, EU");
    tmp.add("Die Unformungsregeln lauten:");
    tmp.add(Type.AG+phi+"  =  "+Type.NOT+Type.EF+"("+Type.NOT+phi+")");
    tmp.add(Type.EF+phi+"  =  "+Type.EU_START+"("+Type.TRUE+")"+Type.EU_END+"("+phi+")");
    tmp.add(Type.AU_START+"("+phi1+")"+Type.AU_END+"("+phi2+")  =  "+Type.NOT+"(("+Type.EU_START+"("+Type.NOT+phi1+")"+Type.EU_END+"(("+Type.NOT+phi1+")"+Type.AND+Type.NOT+"("+phi2+"))"+Type.OR+Type.EG+"("+Type.NOT+phi2+"))");
    tmp.add(Type.EG+phi+"  =  "+Type.NOT+Type.AF+"("+Type.NOT+phi+")");
    tmp.add(Type.AX+phi+"  =  "+Type.NOT+Type.EX+"("+Type.NOT+phi+")");
    tmp.add("");
    tmp.add("Dementsprechend wird die gegebene Formel transformiert:");
    introTexts.put("min", tmp);

      tmp = new ArrayList<>();
      tmp.add("Inhalt und Formeln basieren auf den Folien zu CTL");
      tmp.add("aus dem Kurs \"Formale Methoden des Softwareentwurfs\"(Stand 2017) von Prof. Katzenbeisser");

      introTexts.put("author", tmp);


      tmp = new ArrayList<>();
      tmp.add("//Die CTL-Formel wird hier als Baum-Struktur betrachtet, wobei die atomaren Formeln die Blätter");
      tmp.add("//und die gesamte Formel die Wurzel bilden.");
      tmp.add("//Pseudo-Code:");
      tmp.add("");
      tmp.add("function checkModel(CTLFormula node){");
      tmp.add("  if(node == null)");
      tmp.add("     return null");
      tmp.add("  else {");
      tmp.add("     KripkeStates currentLeft = checkModel(node.left)");
      tmp.add("     KripkeStates currentRight = checkModel(node.right)");
      tmp.add("     //auswerten der aktuellen (Teil-)Formel mit den Ergebnissen ihrer Teilformeln");
      tmp.add("     return evaluateFormula(node.operator, currentLeft, currentRight)}");
      tmp.add("}");
      tmp.add("");
      tmp.add("// \"evaluateFormula\" gibt Zustände gemäß der gegebenen (einfachen) CTL-Formel zurück, siehe Beschreibung");

      introTexts.put("codeExample", tmp);
  }


}
