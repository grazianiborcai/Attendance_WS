package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.Country;

public class CountryTextHelper extends GdaDB {

	public static final String TABLE = "Country";

	public static final String FIELD01 = "CountryID";
	public static final String FIELD02 = "CountryDesc";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Country assignResult(ResultSet resultSet) throws SQLException {

		Country country = new Country();

//		country.setCountryID(resultSet.getString(FIELD01));
//		country.setLanguage(resultSet.getString(FIELD02));
//		country.setName(resultSet.getString(FIELD03));

		return country;
	}

	public String prepareSelect(List<String> country, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT;

		List<String> where = prepareCountryTextWhere(country, language, name);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public List<String> prepareCountryTextWhere(List<String> country,
			List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterString(where, FIELD01, country);
		assignFilterString(where, FIELD02, language);
//		assignFilterString(where, FIELD03, name);

		return where;
	}

}
