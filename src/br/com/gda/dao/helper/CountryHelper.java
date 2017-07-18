package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Country;

public class CountryHelper extends GdaDB {

	public static final String TABLE = "Country";

	public static final String FIELD01 = "CountryID";
	public static final String FIELD02 = "CountryDesc";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Country assignResult(ResultSet resultSet) throws SQLException {

		Country country = new Country();

		country.setCountryID(resultSet.getInt(FIELD01));
		country.setCountryDesc(resultSet.getString(FIELD02));

		return country;
	}

	public String prepareSelect() {

		String stmt = ST_SELECT;

		return stmt;
	}

}
