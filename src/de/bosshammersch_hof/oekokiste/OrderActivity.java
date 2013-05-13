package de.bosshammersch_hof.oekokiste;

import java.util.LinkedList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class OrderActivity extends Activity {
	
	Order one = new Order("Bestellung von 13.14.2012", "Speedykiste", 18.00f);
	Order two = new Order("Bestellung von 11.11.2011", "Obstkiste", 5.00f);
	Order three = new Order("Bestellung von 03.04.2010", "Schonkostkiste", 21.00f);
	Order four = new Order("Bestellung von 15.05.2012", "Regianlkiste", 15.00f);
	Order five = new Order("Bestellung von 17.05.2010", "Fruchtjoghurt-Karussel", 1.99f);
	Order six = new Order("Bestellung von 18.01.2012", "Käsepaket", 11.00f);
	Order seven = new Order("Bestellung von 12.11.2013", "Single", 15.00f);
	Order eight = new Order("Bestellung von 02.08.2013", "Vollsortiment", 21.00f);
	Order nine = new Order("Bestellung von 01.10.2012", "Gemüsekiste", 18.00f);
	
	TextView orderDateTextView;
	TextView boxnameTextView;
	TextView priceTextView;
	
	LinkedList<Order> orderList = new LinkedList<Order>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		orderList.add(one);
		orderList.add(two);
		orderList.add(three);
		orderList.add(four);
		orderList.add(five);
		orderList.add(six);
		orderList.add(seven);
		orderList.add(eight);
		orderList.add(nine);
		
		final ListView orderListView = (ListView) findViewById(R.id.orderListView);
		 
		ListAdapter adapter = new ArrayAdapter<Order>(this, R.layout.listview_item_order, orderList){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		        View row = convertView;
		        
		        if(row == null){
		            LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            row = inflater.inflate(R.layout.listview_item_order, parent, false);
		        }
		        
		        TextView orderDateTextView = (TextView) row.findViewById(R.id.orderDateTextView);
		        orderDateTextView.setText(orderList.get(position).getDate());
		        
		        TextView boxnameTextView = (TextView) row.findViewById(R.id.boxnameTextView);
		        boxnameTextView.setText(orderList.get(position).getName());
		        
		        TextView priceTextView = (TextView) row.findViewById(R.id.priceTextView);
		        priceTextView.setText(orderList.get(position).getPrice() + "€");
		        
		        return row;
		    }
		};
		
		orderListView.setAdapter(adapter);
		
		orderListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(OrderActivity.this,OrderDetailActivity.class);
				startActivity(intent);
			}
		});
		
		getActionBar().setHomeButtonEnabled(true);
	}
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
}
