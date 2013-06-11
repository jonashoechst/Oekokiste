package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class OpenState {
	
	@DatabaseField(id = true)
	int id;

	@DatabaseField
	int lastUserId;
	
	private OpenState(){
	}

	public int getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(int lastUserId) {
		this.lastUserId = lastUserId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
