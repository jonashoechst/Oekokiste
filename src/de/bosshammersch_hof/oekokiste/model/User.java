package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class User extends BaseDaoEnabled<User, Integer>{

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String lastName;
	
	@DatabaseField
	private String firstName;
	
	@DatabaseField
	private String loginName;

	@ForeignCollectionField(eager = true)
	private Collection<Order> orderCollection;
	
	public User(){
		this.setDao(DatabaseManager.getHelper().getUserDao());
		this.orderCollection = (Collection<Order>)  new Vector<Order>();
	}
	
	public User(int id, String lastName, String firstName, String loginName) {
		this();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.loginName = loginName;
	}
	
	public int create() throws SQLException{
		for(Order o : orderCollection){
			o.create();
		}
		return super.create();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Collection<Order> getOrderCollection() {
		return (Collection<Order>) orderCollection;
	}

	public List<Order> getOrderList() {
		
		List<Order> orderList = new LinkedList<Order>();
		for(Order order : orderCollection)
			orderList.add(order);
		return orderList;
	}
	
	public void addOrder(Order order){
		order.setUser(this);
		this.orderCollection.add(order);
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
