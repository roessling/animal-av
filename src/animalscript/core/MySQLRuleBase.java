package animalscript.core;

import java.util.Enumeration;
import java.util.Hashtable;

import animal.main.Animal;

public class MySQLRuleBase {
	AnimalScriptParser parser;

	public MySQLRuleBase() {
		Animal animal = Animal.get();
		animal.getEditors();
		parser = new AnimalScriptParser();
	}

	public void printRules() {
		Hashtable<String, String> rulesHash = BasicParser.registeredRules;

		System.err.println("CREATE TABLE AnimalScriptRules IF NOT EXISTS \n\t(id INT NOT NULL DEFAULT 0 AUTO_INCREMENT PRIMARY KEY,\n\t keyword TEXT NOT NULL,\n\t rule TEXT NOT NULL,\n\t description TEXT);");
		Enumeration<String> e = rulesHash.keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			System.err.println("INSERT INTO AnimalScriptRules VALUES\n\t(0, '" + key
					+ "',\n\t '" + rulesHash.get(key) + "',\n\t '');");
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		MySQLRuleBase m = new MySQLRuleBase();
		m.printRules();
	}
}
