package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class Barcode extends BaseDaoEnabled<Barcode, String>{

	@DatabaseField(id = true)
	private String barcodeString;
	
	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	private Order order;

	public Barcode(){
		this.setDao(DatabaseManager.getHelper().getBarcodeDao());
	}
	
	public Barcode(String barcodeString){
		this();
		this.barcodeString = barcodeString;
	}
	
	public String getBarcodeString() {
		return barcodeString;
	}

	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
