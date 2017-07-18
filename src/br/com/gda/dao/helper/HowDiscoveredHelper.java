package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.HowDiscovered;

public class HowDiscoveredHelper extends GdaDB {

	public static final String TABLE = "How_Discovered";

	public static final String FIELD01 = "WhereID";
	public static final String FIELD02 = "WhereDesc";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public HowDiscovered assignResult(ResultSet resultSet) throws SQLException {

		HowDiscovered howDiscovered = new HowDiscovered();

		howDiscovered.setWhereID(resultSet.getInt(FIELD01));
		howDiscovered.setWhereDesc(resultSet.getString(FIELD02));

		return howDiscovered;
	}

	public String prepareSelect() {

		String stmt = ST_SELECT;

		return stmt;
	}

}
