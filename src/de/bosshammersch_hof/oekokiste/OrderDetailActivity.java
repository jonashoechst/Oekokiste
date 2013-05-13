package de.bosshammersch_hof.oekokiste;

import java.util.LinkedList;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OrderDetailActivity extends Activity {
	
	class Article{	

		public String name;
		public int amount;
		public String price;
		
		public Article(String name, int amount, String price){
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
		
		articleList.add(new Article("Banane", 3, "0,89€"));
		articleList.add(new Article("Kohlrabi", 10, "0,78€"));
		articleList.add(new Article("Apfel", 9, "1,10€"));
		articleList.add(new Article("Schinken", 100, "30,45€"));
		articleList.add(new Article("Haxe", 1, "5,40€"));
		
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
		        priceTextView.setText(articleList.get(position).price);
		        
		        return row;
		    }
		
		};
		
		orderDetailArticleListView.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_detail, menu);
		return true;
	}

}
