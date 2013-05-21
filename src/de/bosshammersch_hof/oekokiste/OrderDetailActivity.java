package de.bosshammersch_hof.oekokiste;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import de.bosshammersch_hof.oekokiste.model.*;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class OrderDetailActivity extends Activity {
	
	final static String ARTICLE_NAME_KEY = "ARTICLE_NAME_KEY";
	
	/** 
	 *   creats the detail-view of order
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		
		ListView orderDetailArticleListView = (ListView) findViewById(R.id.orderDetailArticleListView);
		
		final Order dummyOrder = getDummyOrder();
		
		ListAdapter adapter = new ArrayAdapter<OrderedArticle>(this, R.layout.listview_item_order_detail, dummyOrder.getArticleList()){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       	 	View row = convertView;
		        
		        	if(row == null){
		            	    LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		        	    row = inflater.inflate(R.layout.listview_item_order_detail, parent, false);
		        	}

		        	TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
		        	TextView amountTextView = (TextView) row.findViewById(R.id.amountTextView);
		        	TextView priceTextView = (TextView) row.findViewById(R.id.priceTextView);
		        
		        	nameTextView.setText(dummyOrder.getArticleList().get(position).getName());
		        	amountTextView.setText(dummyOrder.getArticleList().get(position).getCount()+"");
		        	int price = dummyOrder.getArticleList().get(position).getTotalPrice();
		        	priceTextView.setText((price/100)+","+(price%100)+"€");
		        
		        	return row;
			}
		};
		
		// creating and filling the sum line
		LayoutInflater inflater = getLayoutInflater();
      	  	View sumRow = inflater.inflate(R.layout.listview_item_order_detail_sum, null);
        
        	int finalPrice = 0;
        	for(OrderedArticle article : dummyOrder.getArticleList()){
        		finalPrice += article.getTotalPrice(); 
        	}
        
        	TextView nameTextView = (TextView) sumRow.findViewById(R.id.nameTextView);
        	TextView finalPriceTextView = (TextView) sumRow.findViewById(R.id.finalPriceTextView);

        	nameTextView.setText("Summe: ");
        	finalPriceTextView.setText((finalPrice/100)+","+(finalPrice%100)+"€");
        
		orderDetailArticleListView.addFooterView(sumRow);

		// creating and filling the recipe finder line
        	View recipeFindRow = inflater.inflate(R.layout.listview_item_order_detail_recipe_button, null);
        
        	Button recipeFindButton = (Button) recipeFindRow.findViewById(R.id.recipeFindButton);
        
        	recipeFindButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(OrderDetailActivity.this, RecipeActivity.class);
				startActivity(intent);
			}
		});
        
		orderDetailArticleListView.addFooterView(recipeFindRow);

		orderDetailArticleListView.setAdapter(adapter);
		
		orderDetailArticleListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				if (arg2 >= dummyOrder.getArticleList().size()){
					return;
				}

				Intent intent = new Intent(OrderDetailActivity.this,ArticleDetailActivity.class);
				startActivity(intent);
			}
		});
		
		getActionBar().setHomeButtonEnabled(true);
		
	}
	
	/**
	 *   if the app icon in action bar is clicked => go home
	 *   else the super constructor of the function is called
	 *   @param MenuItem which was selected
	 *   @return boolean 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Sends an intent to read .pdf-files. 
	 *
	 * @param	view	The clicked view.
	 */
	public void viewBillClicked(View view){

		String link = "http://vcp-kurhessen.info/wordpress/wp-content/uploads/2011/08/2011-08-09_regionsordnung.pdf";
		
		Uri path = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        	Intent webIntent = new Intent(this, WebPDFViewActivity.class);
    		webIntent.setDataAndType(path, "application/pdf");
    		startActivity(webIntent);
        }
	}
	/**
	 *   supplies dummy data for testing the app
	 *   @return Order returns a new Order
	 */
	@SuppressWarnings("deprecation")
	private Order getDummyOrder(){
		
		LinkedList<OrderedArticle> articleList = new LinkedList<OrderedArticle>();
		
		articleList.add(new OrderedArticle(0, "Gouda", "", 245, 2));
		articleList.add(new OrderedArticle(0, "Kohlrabi", "", 115, 1));
		articleList.add(new OrderedArticle(0, "Blattsalat", "", 120, 2));
		articleList.add(new OrderedArticle(0, "Gurke", "", 70, 3));
		articleList.add(new OrderedArticle(0, "Möhren (500g)", "", 219, 1));
		articleList.add(new OrderedArticle(0, "Nackensteak (200g)", "", 550, 2));
		articleList.add(new OrderedArticle(0, "Bierschinken (50g)", "", 100, 5));
		articleList.add(new OrderedArticle(0, "Geflügelwürtchen (180g)", "", 459, 2));
		articleList.add(new OrderedArticle(0, "Joghurt (500g)", "", 239, 1));
		articleList.add(new OrderedArticle(0, "ROCKSTAR ENGERY DRINK (483 ml)", "", 279, 24));
		
		return new Order(0, new Date(2012, 3, 24), "Beispielkiste", articleList);

		
		
	}
}
