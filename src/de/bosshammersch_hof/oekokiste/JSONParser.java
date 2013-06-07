package de.bosshammersch_hof.oekokiste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	
	static InputStream input = null;
	static JSONObject jsonObject = null;
	static String jsonString = "";
	
	public JSONParser(){
		
	}
	
	public JSONObject getJSONFromUrl(String url){
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			input = httpEntity.getContent();
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				builder.append(line + "\n");
			}
			input.close();
			jsonString = input.toString();
		} catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
		
		try{
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		
		return jsonObject;
	}
}
