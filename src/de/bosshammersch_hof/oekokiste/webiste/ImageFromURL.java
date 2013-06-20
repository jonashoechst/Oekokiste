package de.bosshammersch_hof.oekokiste.webiste;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.bosshammersch_hof.oekokiste.ImageUpdatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;


public class ImageFromURL extends AsyncTask<String, Integer, Drawable> {

	public ImageUpdatable updateClass;
	
	/**
	 * Sucht zu URL ein Bild (wird im Hintergrund ausgeführt)
	 * 
	 * @params String[] params
	 * 
	 * @return Das gefundene Bild
	 */
	public Drawable doInBackground(String... params) {
		Drawable d = null;
		URL url;
		try {
			Log.i("ImagefromUrl", "url: "+params[0]);
			url = new URL (params[0]);
			InputStream is = (InputStream)url.getContent();
			d = Drawable.createFromStream(is, "");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return d;
	}
	
	/**
	 * Lädt Bild in Activity.
	 * 
	 * @params Drawable Das in doInBackground gefundene Bild.
	 */
	@Override
	public void onPostExecute(Drawable d) {
		Log.i("Download Image", "finished");
		if(updateClass != null) updateClass.updateImage(d);
	}
}