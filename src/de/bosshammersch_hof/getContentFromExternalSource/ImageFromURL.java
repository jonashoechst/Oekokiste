package de.bosshammersch_hof.getContentFromExternalSource;

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
			url = new URL ("http://2.bp.blogspot.com/-QVb-8Ec3rAc/Tc5_W6_lLVI/AAAAAAAAEGE/pEPdeot9GXs/s1600/Obatzter.jpg");
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