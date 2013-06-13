package de.bosshammersch_hof.oekokiste.ormlite;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

import de.bosshammersch_hof.oekokiste.model.*;

public class DatabaseFillMock {

	public static void main(String args[]){

		User u = new User();
		u.setFirstName("Niklas");
		u.setLastName("Rappel");
		u.setLoginName("nrappel");
		u.setId(1);
		
		Order o = new Order();
		o.setId(1);
		o.setName("Mock Order");
		o.setDate(new Date());
		
		OrderedArticle oa = new OrderedArticle();
		
		Article a = new Article();
		
		ArticleGroup ag = new ArticleGroup();
		ag.setName("Mock Article Group");
		Category c = new Category();
		c.setName("MockCat");
		c.addArticleGroup(ag);
		
		a.setName("Mock Article");
		a.setDescription("... isn't very tasty");
		a.setOrigin("42");
		
		ag.addArticle(a);
		
		oa.setArticle(a);
		oa.setAmount(2);
		oa.setAmountType("Pieces");
		oa.setId(1);
		oa.setPrice(1000);
		
		o.addOrderedArticle(oa);
		u.addOrder(o);
		
		try {
			u.create();
			o.create();
			oa.create();
			a.create();
			c.create();
			ag.create();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		OpenState os = new OpenState();
		
		os.setId(1);
		os.setLastUserId(1);
		
		DatabaseManager.saveOpenState(os);
		
		
		
		
		
		
		/*
		
		
		
		
		Article article = new Article(1, "Mock Article", "Ein Mock Artikel schmeckt nicht besonders...");
		OrderedArticle orderedArticle = new OrderedArticle(article, 2, "Stück", 299);
		
		Barcode barcode = new Barcode("A-123");
		
		@SuppressWarnings("deprecation")
		Order order = new Order(1, new Date(112,12,12), "Mock Order");
		order.addBarcode(barcode);
		order.addOrderedArticle(orderedArticle);
		try {
			order.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		User user = new User(1, "Name", "Mock", "mocklogin");
		user.addOrder(order);
		
		try {
			user.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create a category
		Category category = new Category("Mock Category");
		
		ArticleGroup articleGroup = new ArticleGroup("Mock Article Group");
		articleGroup.addArticle(article);
		articleGroup.setCategory(category);
		try {
			articleGroup.create();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		category.addArticleGroup(articleGroup);
		try {
			category.create();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Create an recipe
		String description = "Flammkuchen, Flammenkuchen oder Flammekueche (frz. Tarte flambée) ist ein herzhafter Kuchen aus dem Elsass (frz. Alsace) in Frankreich. Ursprünglich war Flammkuchen ein bäuerliches Essen, mit dem am Backtag getestet wurde, ob der dörfliche Backofen heiß genug war, bevor die Brote für die Woche gebacken wurden. Da der Backtag im dörflichen Arbeitsrhythmus ein Gemeinschaftsereignis war und der Backofen im Backhaus auch vor Feiertagen angeheizt wurde, entwickelte sich der Flammkuchen mit der Zeit zum Festtagsessen. Als Teig benutzte man Brotteig.";
		String instruction = "Den Brotteig in 45 Stücke zerteilen, mit einem Küchentuch abdecken und die Teigstücke 30 Minuten ruhen lassen. Den Quark (mit oder ohne Crème fraîche, siehe Varianten) mit Salz und Pfeffer verrühren. Zwiebel in dünne Ringe schneiden oder hobeln. Den Frühstücksspeck in Streifen schneiden. Den Teig auf einer bemehlten Arbeitsfläche so dünn wie möglich auswellen und mit dem Quark gleichmäßig dünn bestreichen. Mit Frühstücksspeck, Zwiebelringen (und Käse, siehe Varianten) belegen.";
		
		LinkedList<Cookware> cookware = new LinkedList<Cookware>();
		cookware.add(new Cookware("Nudelholz"));
		cookware.add(new Cookware("Backblech"));
		cookware.add(new Cookware("2 Bögen Backpapier"));
		cookware.add(new Cookware("Teigschaber"));
		cookware.add(new Cookware("Rührschüssel"));
		cookware.add(new Cookware("Messer"));
		for(Cookware c : cookware){
			try {
				c.create();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		LinkedList<CookingArticle> articleList = new LinkedList<CookingArticle>();

		Article article1 = new Article(1, "Mehl", "");
		try {
			article1.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		articleList.add(new CookingArticle(article1, 200, "g", true));
		Article article2 = new Article(2, "Schmand", "");
		try {
			article2.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		articleList.add(new CookingArticle(article2, 1, "Becher", false));
		Article article3 = new Article(3, "Speck", "");
		try {
			article3.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		articleList.add(new CookingArticle(article3, 250, "g", true));
		Article article4 = new Article(4, "Gouda", "");
		try {
			article4.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		articleList.add(new CookingArticle(article4, 1, "Block", true));
		Article article5 = new Article(5, "Zwiebeln", "");
		try {
			article5.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		articleList.add(new CookingArticle(article5, 3, "Stück", true));
		
		
		Recipe recipe = new Recipe("Flammkuchen", "", "", null, null, 1, 45, 30, 5, null, 80);
		recipe.setDescription(description);
		recipe.setInstructions(instruction);
		
		//recipe.setCookware(cookware);
		for(Cookware c : cookware)
			recipe.addCookware(c);
		
		//recipe.setIngredients(articleList);
		for(CookingArticle ca : articleList)
			recipe.addIngredient(ca);
		
		try {
			recipe.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		
		User user = new User();
		user.setId(1);
		user.setFirstName("Mock");
		user.setLastName("Name");
		user.setLoginName("mocklogin");
		
		Order o = new Order();
		o.setId(1);
		o.setName("Mock Order");
		o.setDate(new Date());
		
		user.addOrder(o);
		try {
			o.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			user.create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}
}
