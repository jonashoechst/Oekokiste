package de.bosshammersch_hof.oekokiste.model;

import java.util.LinkedList;
import com.j256.ormlite.field.DatabaseField;
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
	private LinkedList<Order> orderList;
	
	public User(int id, String lastName, String firstName, LinkedList<Order> orderList) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.orderList = orderList;
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

	public LinkedList<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(LinkedList<Order> orderList) {
		this.orderList = orderList;
	}
}
