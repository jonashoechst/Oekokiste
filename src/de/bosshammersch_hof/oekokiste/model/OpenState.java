package de.bosshammersch_hof.oekokiste.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import de.bosshammersch_hof.oekokiste.ormlite.DatabaseManager;

@DatabaseTable
public class OpenState extends BaseDaoEnabled<OpenState, Integer>{
	
	@DatabaseField(id = true)
	private static final int id = 1;

	@DatabaseField(foreign = true,foreignAutoCreate = true,foreignAutoRefresh = true)
	User user;
	
	public OpenState(){
		this.setDao(DatabaseManager.getHelper().getOpenStateDao());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
