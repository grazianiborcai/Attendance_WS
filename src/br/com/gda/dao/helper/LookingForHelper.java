package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.LookingFor;

public class LookingForHelper extends GdaDB {

	public static final String TABLE = "Looking_For";

	public static final String FIELD01 = "LookID";
	public static final String FIELD02 = "LookDesc";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public LookingFor assignResult(ResultSet resultSet) throws SQLException {

		LookingFor lookingFor = new LookingFor();

		lookingFor.setLookID(resultSet.getInt(FIELD01));
		lookingFor.setLookDesc(resultSet.getString(FIELD02));

		return lookingFor;
	}

	public String prepareSelect() {

		String stmt = ST_SELECT;

		return stmt;
	}

}
