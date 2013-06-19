package de.bosshammersch_hof.oekokiste.model;

import java.sql.SQLException;

public interface CreateOrUpdateable {
	public void createOrUpdate() throws SQLException;
}
