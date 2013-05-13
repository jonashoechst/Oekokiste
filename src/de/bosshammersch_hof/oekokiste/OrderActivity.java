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
	
	Order one = new Order("Bestellung von 13.14.2012", "Test1", 35.95f);
	Order two = new Order("Bestellung von 11.11.2011", "Test2", 3.95f);
	Order three = new Order("Bestellung von 03.04.2010", "Test3", 35.90f);
	Order four = new Order("Bestellung von 15.14.2012", "Test4", 359.95f);
	Order five = new Order("Bestellung von 17.14.2019", "Test5", 352.95f);
	Order six = new Order("Bestellung von 18.14.2017", "testkiste", 2.95f);
	Order seven = new Order("Bestellung von 12.11.1900", "hallo", 35.05f);
	Order eight = new Order("Bestellung von 02.17.2013", "wasnoch", 3643.98f);
	Order nine = new Order("Bestellung von 01.13.2012", "Test1", 35.95f);
	
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
		 
		ListAdapter adapter = new ArrayAdapter<Order>(this, R.layout.order_item, orderList){
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
		        View row = convertView;
		        
		        if(row == null){
		            LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            
		            row = inflater.inflate(R.layout.order_item, parent, false);

		        }
		        
		        TextView orderDateTextView = (TextView) row.findViewById(R.id.orderDateTextView);
		        orderDateTextView.setText(orderList.get(position).getDate());
		        
		        TextView boxnameTextView = (TextView) row.findViewById(R.id.boxnameTextView);
		        boxnameTextView.setText(orderList.get(position).getName());
		        
		        TextView priceTextView = (TextView) row.findViewById(R.id.priceTextView);
		        priceTextView.setText("" + orderList.get(position).getPrice());
		        
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
		
	}

}
