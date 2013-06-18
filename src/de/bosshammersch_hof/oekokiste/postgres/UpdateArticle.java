package de.bosshammersch_hof.oekokiste.postgres;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.provider.SyncStateContract.Constants;
import de.bosshammersch_hof.oekokiste.model.Article;

public class UpdateArticle extends AsyncTask<Article, Integer, boolean[]> {

	public boolean[] doInBackground(Article... params) {
		
		String html;
		
		try {
			html = executeHttpGet(Constants.pathToArticleDescription);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	
	
	
	
	public String[] executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String[] lines = null;
            String line = "";
            String NL = System.getProperty("line.separator");
            int i = 0;
            while ((line = in.readLine()) != null) {
               i++;
               lines[i] = line;
            }
            in.close();
            String page = sb.toString();
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
