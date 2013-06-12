package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Barcode {

	@DatabaseField(id = true)
	private String barcodeString;

	public Barcode(){
		
	}
	
	public Barcode(String barcodeString){
		this.barcodeString = barcodeString;
	}
	
	public String getBarcodeString() {
		return barcodeString;
	}

	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}
	
}
