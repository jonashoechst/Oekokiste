package de.bosshammersch_hof.oekokiste.model;

/*import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;*/

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Article {

	@DatabaseField(generatedId = true)
	private final int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	//private Bitmap image;
	
	public Article(int id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
	
	/*public void setBitmapForUrl(String urlString){
		InputStream in = null;
		
		try {  
			URL url = new URL(urlString);
			in = url.openStream();
			image = BitmapFactory.decodeStream(in);
		} catch (MalformedURLException e) {
			System.out.println("Could not parse the given urlString.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not open Stream for Url.");
			e.printStackTrace();
		} finally {
			if (in != null) {
		       try {
				in.close();
		       } catch (IOException e) {
		    	   System.out.println("Could not close input Stream.");
		    	   e.printStackTrace();
		       }
			}
		}
		
	}
	public Bitmap getBitmap(){
		return image;
	}*/
	
}
