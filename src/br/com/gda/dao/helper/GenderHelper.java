package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Gender;

public class GenderHelper extends GdaDB {

	public static final String TABLE = "Gender";

	public static final String FIELD01 = "GenderID";
	public static final String FIELD02 = "GenderDesc";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Gender assignResult(ResultSet resultSet) throws SQLException {

		Gender gender = new Gender();

		gender.setGenderID(resultSet.getInt(FIELD01));
		gender.setGenderDesc(resultSet.getString(FIELD02));

		return gender;
	}

	public String prepareSelect() {

		String stmt = ST_SELECT;

		return stmt;
	}

}
