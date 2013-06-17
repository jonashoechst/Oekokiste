package de.bosshammersch_hof.oekokiste;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.bosshammersch_hof.oekokiste.model.*;
import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class OrderActivity extends Activity {
	
	
	/*TextView orderDateTextView;
	TextView boxnameTextView;
	TextView priceTextView;*/
	
	User user;
	
	ListView orderListView;
	
	/**
	 *   creats the order list 
	 *   ... and so on
	 *   @param Bundle saved Instance State
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		setupUser();
		
		updateUi();
		
		getActionBar().setHomeButtonEnabled(true);
	}

	private void updateUi() {
		orderListView = (ListView) findViewById(R.id.orderListView);
		 
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
				intent.putExtra(Constants.keyOrder, user.getOrderList().get(arg2).getId());
				startActivity(intent);
			}
		});
	}

	private void setupUser() {
		// get User Id and matching User 
		int userId = getIntent().getIntExtra(Constants.keyUser, 1);
		Log.i(OrderActivity.class.getName(), "userId found: "+userId);
		user = DatabaseManager.getUser(userId);
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
}