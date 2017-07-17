package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.StateText;

public class StateTextHelper extends GdaDB {

	public static final String TABLE = "State_text";

	public static final String FIELD01 = COUNTRY;
	public static final String FIELD02 = STATE_PROVINCE;
	public static final String FIELD03 = LANGUAGE;
	public static final String FIELD04 = NAME;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public StateText assignResult(ResultSet resultSet) throws SQLException {

		StateText stateText = new StateText();

		stateText.setCountry(resultSet.getString(FIELD01));
		stateText.setState(resultSet.getString(FIELD02));
		stateText.setLanguage(resultSet.getString(FIELD03));
		stateText.setName(resultSet.getString(FIELD04));

		return stateText;
	}

	public String prepareSelect(List<String> country, List<String> state,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt,
				prepareStateTextWhere(country, state, language, name));

		return stmt;
	}

	public List<String> prepareStateTextWhere(List<String> country,
			List<String> state, List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterString(where, FIELD01, country);
		assignFilterString(where, FIELD02, state);
		assignFilterString(where, FIELD03, language);
		assignFilterString(where, FIELD04, name);

		return where;
	}

}
