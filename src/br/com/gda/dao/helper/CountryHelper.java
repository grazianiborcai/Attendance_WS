package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.Country;

public class CountryHelper extends GdaDB {

	public static final String TABLE = "Country";

	public static final String FIELD01 = COUNTRY;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "."
			+ CountryTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ CountryTextHelper.TABLE + "." + CountryTextHelper.FIELD01;

	CountryTextHelper countryTextHelper;

	public CountryHelper() {
		countryTextHelper = new CountryTextHelper();
	}

	public CountryHelper(CountryTextHelper countryTextHelper) {
		this.countryTextHelper = countryTextHelper;
	}

	public void assignResult(ArrayList<Country> statelList, ResultSet resultSet)
			throws SQLException {

		if (resultSet.isFirst()
				|| !statelList.get(statelList.size() - 1).getCountry()
						.equals(resultSet.getString(TABLE + "." + FIELD01))) {

			Country eachCountry = new Country();

			eachCountry.setCountry(resultSet.getString(TABLE + "." + FIELD01));

			eachCountry.getCountryText().add(
					countryTextHelper.assignResult(resultSet));
			statelList.add(eachCountry);

		} else {

			statelList.get(statelList.size() - 1).getCountryText()
					.add(countryTextHelper.assignResult(resultSet));

		}
	}

	public String prepareSelect(List<String> country, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = prepareCountryWhereWithText(country, language,
				name);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public List<String> prepareCountryWhereWithText(List<String> country,
			List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterString(where, TABLE + "." + FIELD01, country);

		where.addAll(countryTextHelper.prepareCountryTextWhere(null, language,
				name));

		return where;
	}

}
