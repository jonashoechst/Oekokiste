package de.bosshammersch_hof.oekokiste.model;

import java.util.List;

public class User {

	private int id;
	
	private String lastName;
	private String firstName;
	
	private List<Order> orderList;
	
	public User(int id, String lastName, String firstName, List<Order> orderList) {
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

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
}
