package de.bosshammersch_hof.oekokiste.webiste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import de.bosshammersch_hof.oekokiste.Constants;
import de.bosshammersch_hof.oekokiste.model.*;

public class UpdateArticle extends AsyncTask<Article, Integer, String[]> {

	/**
	 * 
	 */
	public String[] doInBackground(Article... params) {
		
		String[] html;
		LinkedList<String> ll = new LinkedList<String>();
		
		try{
			html = executeHttpGet(Constants.pathToArticleDescription + params[0].getId());
			Pattern pat = Pattern.compile("<img src=*.png");
			for(String s : html){
				Matcher m = pat.matcher(s);
				while(m.find())
				{
				    ll.add(m.group());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	public void onPostExecute(String[] s) {
		 // hier sollte was sinnvolles passieren!!!!
	 }
	
	@SuppressWarnings("null")
	public String[] executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String[] lines = null;
            String line = "";
            int i = 0;
            while ((line = in.readLine()) != null) {
               i++;
               lines[i] = line;
            }
            in.close();
            return lines;
            } finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
		
}
