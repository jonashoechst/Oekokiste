package de.bosshammersch_hof.oekokiste.test;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

public class OrmLiteTest{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DatabaseManager dbm = DatabaseManager.getInstance();

		Article article1 = new Article(1, "Apfel", "Ein Apfel ist ein Obst...");
		Article article2 = new Article(2, "Banane", "... fängt mit B an!");
		Article article3 = new Article(3, "Citron", "... uiiiiiiii, sauer!");
		
		
		
	}

}
