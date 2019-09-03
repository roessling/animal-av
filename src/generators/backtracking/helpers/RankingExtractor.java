package generators.backtracking.helpers;

import algoanim.primitives.generators.Language;
import algoanim.primitives.IntMatrix;

public class RankingExtractor {
  Person[] men, women;
  int[][]  menRanking, womenRanking;
  IntMatrix menRequest, menReject, womenRequest, womenReject;
  String    string;

  public RankingExtractor(String menParam, String womenParam, Language lang)
      throws IllegalArgumentException {
    String string = "";
    for (int i = 0; i < menParam.length() - 1; i++) {
      if (menParam.substring(i, i + 1).equals("\\")
          && menParam.substring(i + 1, i + 2).equals("n")) {
        string = string + "\n";
        i++;
      } else
        string = string + menParam.substring(i, i + 1);
    }
    String myMen = string + menParam.substring(menParam.length() - 1, menParam.length());

    string = "";
    for (int i = 0; i < womenParam.length() - 1; i++) {
      if (womenParam.substring(i, i + 1).equals("\\")
          && womenParam.substring(i + 1, i + 2).equals("n")) {
        string = string + "\n";
        i++;
      } else
        string = string + womenParam.substring(i, i + 1);
    }
    String myWomen = string + womenParam.substring(womenParam.length() - 1, womenParam.length());

    men = buildPersonArray(myMen);
    women = buildPersonArray(myWomen);

    if (men.length != women.length)
      throw new IllegalArgumentException(
          "Es muss gleich viele Männer und Frauen geben.");

    int[][] matrix = new int[men.length][men.length];

    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        matrix[i][j] = 0;

    for (Person p : men)
      p.createRanking(men.length);
    for (Person p : women)
      p.createRanking(women.length);

    buildRanking(women, men, menParam, lang);
    buildRanking(men, women, womenParam, lang);
  }

  private Person[] buildPersonArray(String string) {
    Person[] a;
    String s = string.trim();
    int n = 0;
    while (s.length() > 0) {
      if (Character.isLetter(s.charAt(0)))
        n++;
      else if (s.startsWith("//"))
        ;
      else
        throw new IllegalArgumentException(
            "Jede Zeile muss mit einem Buchstaben beginnen.");
      if (s.contains("\n"))
        s = s.substring(s.indexOf("\n") + 1, s.length()).trim();
      else
        s = "";
    }

    a = new Person[n];

    s = string.trim();

    for (int i = 0; i < n; i++) {
      if (Character.isLetter(s.charAt(0))) {
        String[] array = nextName(s);
        s = array[1];
        for (int j = 0; j < i; j++)
          if (array[0].charAt(0) == a[j].getName().charAt(0))
            throw new IllegalArgumentException(
                "Zwei Personen des gleichen Geschlechts dürfen nicht mit den gleichen Anfangsbuchstaben anfangen.");
        a[i] = new Person(array[0]);
      }
      if (s.contains("\n"))
        s = s.substring(s.indexOf("\n") + 1, s.length()).trim();
    }
    return a;
  }

  public void buildRanking(Person[] persons, Person[] persons2, String s,
      Language l) throws IllegalArgumentException {
    String s1 = s.trim();
    String s2;

    for (int i = 0; i < persons.length; i++) {
      s2 = (s1 + "\n").substring(0, (s1 + "\n").indexOf("\n"));
      s1 = s1.substring((s1 + "\n").indexOf("\n"), s1.length()).trim();

      s2 = nextName(s2)[1];
      if (s2.charAt(0) != ':')
        throw new IllegalArgumentException(
            "Zwischen dem Namen einer Person und seiner Rankingliste, muss ein Doppelpunkt stehen");
      s2 = s2.substring(1, s2.length()).trim();

      int k = 0;
      while (k < persons.length) {
        if (s2.length() == 0)
          throw new IllegalArgumentException(
              "Jede Person muss jeder Person des anderen Geschlechts eine Priorität zuweisen.");
        String[] array = nextName(s2);
        boolean found = false;
        for (int j = 0; j <= persons.length && !found; j++) {
          if (j == persons.length)
            throw new IllegalArgumentException(
                "Jede Person muss jeder Person des anderen Geschlechts eine Priorität zuweisen.");
          if (array[0].equals(persons[j].getName())) {
            persons2[i].setRanking(k, persons[j]);
            found = true;
          }

        }
        s2 = array[1];
        if (s2.length() > 0 && s2.charAt(0) == ',')
          s2 = s2.substring(1, s2.length()).trim();
        k++;
      }
    }

    for (int i = 0; i < persons.length; i++)
      for (int j = 0; j < persons.length; j++)
        for (int k = 0; k < persons.length; k++)
          if (j != i
              && persons2[k].getPersonAt(i).getName()
                  .equals(persons2[k].getPersonAt(j).getName()))
            throw new IllegalArgumentException(
                "Jede Person muss jeder Person des anderen Geschlechts eine Priorität zuweisen.");
  }

  private String[] nextName(final String s) {
    String name = "";
    String myS = s;
    while (myS.length() > 0 && Character.isLetter(myS.charAt(0))) {
      name = name + myS.substring(0, 1);
      myS = myS.substring(1, myS.length());
    }
    myS = myS.trim();
    String[] array = new String[2];
    array[0] = name;
    array[1] = myS;
    return array;
  }

  public Person[] getMen() {
    return men;
  }

  public Person[] getWomen() {
    return women;
  }
}
