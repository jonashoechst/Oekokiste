package de.bosshammersch_hof.oekokiste;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import de.bosshammersch_hof.oekokiste.model.*;
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
	
	
	TextView orderDateTextView;
	TextView boxnameTextView;
	TextView priceTextView;
	
	/**
	 *   creats the order list 
	 *   ... and so on
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		final User user = getDummyUser();
		
		final ListView orderListView = (ListView) findViewById(R.id.orderListView);
		 
		ListAdapter adapter = new ArrayAdapter<Order>(this, R.layout.listview_item_order, user.getOrderList()){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		        View row = convertView;
		        
		        if(row == null){
		            LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
		            row = inflater.inflate(R.layout.listview_item_order, parent, false);
		        }
		        
		        TextView orderDateTextView = (TextView) row.findViewById(R.id.orderDateTextView);
		        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
		        orderDateTextView.setText(dateFormat.format(user.getOrderList().get(position).getDate()));
		        
		        TextView boxnameTextView = (TextView) row.findViewById(R.id.boxnameTextView);
		        boxnameTextView.setText(user.getOrderList().get(position).getName());
		        
		        TextView priceTextView = (TextView) row.findViewById(R.id.priceTextView);
		        priceTextView.setText(user.getOrderList().get(position).getTotalOrderValue());
		        
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
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 *   creats a list of orders with dummy data
	 *   and creats a new User with this list of orders
	 *   @return returns dummy User
	 */
	private User getDummyUser(){
		
		LinkedList<Order> orderList = new LinkedList<Order>();
		orderList.add(new Order(0, new Date(), "Speedykiste", null));
		orderList.add(new Order(0, new Date(), "Obstkiste", null));
		orderList.add(new Order(0, new Date(), "Schonkostkiste", null));
		orderList.add(new Order(0, new Date(), "Regianlkiste", null));
		orderList.add(new Order(0, new Date(), "Fruchtjoghurt-Karussel", null));
		orderList.add(new Order(0, new Date(), "Käsepaket", null));
		orderList.add(new Order(0, new Date(), "Single", null));
		orderList.add(new Order(0, new Date(), "Vollsortiment", null));
		orderList.add(new Order(0, new Date(), "Gemüsekiste", null));
		
		return new User(0, "Sterz", "Artur", orderList);
			
	}
}
