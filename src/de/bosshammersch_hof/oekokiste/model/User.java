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
public class User extends BaseDaoEnabled<User, Integer> implements CreateOrUpdateable{

	@DatabaseField(id = true)
	private int id;
	
	@DatabaseField
	private String lastName;
	
	@DatabaseField
	private String firstName;
	
	@DatabaseField
	private String loginName;
	
	@DatabaseField
	private String passwordSha;

	@ForeignCollectionField(eager = true)
	private Collection<Order> orderCollection;
	
	public User(){
		this.setDao(DatabaseManager.getHelper().getUserDao());
		this.orderCollection = new Vector<Order>();
	}
	
	public void createOrUpdate() throws SQLException{
		DatabaseManager.getHelper().getUserDao().createOrUpdate(this);
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
		return orderCollection;
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

	public String getPasswordSha() {
		return passwordSha;
	}

	public void setPassword(String password) {
		this.passwordSha = password;
		/*MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			this.passwordSha = new String(digest, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			Log.e("�kokiste - Login", "Algorithm for sha-256 is not present.");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Log.e("�kokiste - Login", "Encoding in password conversion is not supported.");
			e.printStackTrace();
		}*/
	}
	
}
