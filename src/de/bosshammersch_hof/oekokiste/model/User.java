package de.bosshammersch_hof.oekokiste.model;

import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class User {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String lastName;
	
	@DatabaseField
	private String firstName;
	
	@DatabaseField
	private String loginName;

	@ForeignCollectionField(eager = false)
	private List<Order> orderList;
	
	private User(){
		this.orderList = new LinkedList<Order>();
	}
	
	public User(int id, String lastName, String firstName, String loginName) {
		this();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.loginName = loginName;
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

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(LinkedList<Order> orderList) {
		this.orderList = orderList;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
