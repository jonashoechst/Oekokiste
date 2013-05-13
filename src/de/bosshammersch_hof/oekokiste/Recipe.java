/* Author: 		Niklas Rappel
 * Created on: 	11.05.2013
 * Description:	This class describes a recipe-type for our RecipeDetailActivity and our RecipeActivity.
 */

package de.bosshammersch_hof.oekokiste;

public class Recipe {

	public String name;
	public int hitRate;
	
	
	
	//CONSTRUCTORS
	public Recipe(){
		this.name = "";
		this.hitRate = 0;
	}
	
	public Recipe(String name, int hitRate){
		this.name = name;
		this.hitRate = hitRate;
	}
}
