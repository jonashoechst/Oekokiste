package de.bosshammersch_hof.oekokiste;

import java.io.File;
import java.util.LinkedList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.LayoutInflater;
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
	
	class Article{	

		public String name;
		public int amount;
		public double price;
		
		public Article(String name, int amount, double price){
			this.name = name;
			this.amount = amount;
			this.price = price;
		}	
	}
	
	LinkedList<Article> articleList = new LinkedList<Article>();;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		
		articleList.add(new Article("Banane", 3, 0.89));
		articleList.add(new Article("Kohlrabi", 10, 0.78));
		articleList.add(new Article("Apfel", 9, 1.10));
		articleList.add(new Article("Schinken", 100, 30.45));
		articleList.add(new Article("Haxe", 1, 5.40));
		articleList.add(new Article("Käse", 3, 2.63));
		articleList.add(new Article("Bier", 10, 9.99));
		articleList.add(new Article("Döner", 1, 4.00));
		articleList.add(new Article("Salat", 2, 2.50));
		articleList.add(new Article("Champignon-Köpfe (geputzt)", 20, 5.40));
		
		ListView orderDetailArticleListView = (ListView) findViewById(R.id.orderDetailArticleListView);
		
		ListAdapter adapter = new ArrayAdapter<Article>(this, R.layout.listview_item_order_detail, articleList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
		        View row = convertView;
		        
		        if(row == null)
		        {
		            LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            
		            row = inflater.inflate(R.layout.listview_item_order_detail, parent, false);

		        }

		        TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
		        TextView amountTextView = (TextView) row.findViewById(R.id.amountTextView);
		        TextView priceTextView = (TextView) row.findViewById(R.id.priceTextView);
		        
		        nameTextView.setText(articleList.get(position).name);
		        amountTextView.setText(articleList.get(position).amount+"");
		        priceTextView.setText(String.format("%.2f€", articleList.get(position).price));
		        
		        return row;
		    }
		
		};
		
		// creating and filling the sum line
		LayoutInflater inflater = getLayoutInflater();
        View sumRow = inflater.inflate(R.layout.listview_item_order_detail_sum, null);
        
        double finalPrice = 0;
        for(Article article : articleList) finalPrice += article.price; 
        
        TextView nameTextView = (TextView) sumRow.findViewById(R.id.nameTextView);
        TextView finalPriceTextView = (TextView) sumRow.findViewById(R.id.finalPriceTextView);

        nameTextView.setText("Summe: ");
        finalPriceTextView.setText(String.format("%.2f€",finalPrice));
        
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
				
				if (arg2 >= articleList.size()) return;

				Intent intent = new Intent(OrderDetailActivity.this,ArticleDetailActivity.class);
				intent.putExtra(ARTICLE_NAME_KEY, articleList.get(arg2).name);
				startActivity(intent);
			}
		});
		
		
	
	}
	
	public void viewBillClicked(View view){

		String link = "http://vcp-kurhessen.info/wordpress/wp-content/uploads/2011/08/2011-08-09_regionsordnung.pdf";
		
		Uri path = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } 
        catch (ActivityNotFoundException e) {
           //Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();

    		Intent webIntent = new Intent(this, WebPDFViewActivity.class);
    		webIntent.setDataAndType(path, "application/pdf");
    		startActivity(webIntent);
        }
		
	}

}
