package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.State;

public class StateHelper extends GdaDB {

	public static final String TABLE = "State";

	public static final String FIELD01 = COUNTRY;
	public static final String FIELD02 = STATE_PROVINCE;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "."
			+ StateTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ StateTextHelper.TABLE + "." + StateTextHelper.FIELD01 + " AND "
			+ TABLE + "." + FIELD02 + " = " + StateTextHelper.TABLE + "."
			+ StateTextHelper.FIELD02;

	StateTextHelper stateTextHelper;

	public StateHelper() {
		stateTextHelper = new StateTextHelper();
	}

	public StateHelper(StateTextHelper stateTextHelper) {
		this.stateTextHelper = stateTextHelper;
	}

	public void assignResult(ArrayList<State> statelList, ResultSet resultSet)
			throws SQLException {

		if (resultSet.isFirst()
				|| !statelList.get(statelList.size() - 1).getCountry()
						.equals(resultSet.getString(TABLE + "." + FIELD01))
				|| !statelList.get(statelList.size() - 1).getState()
						.equals(resultSet.getString(TABLE + "." + FIELD02))) {

			State eachState = new State();

			eachState.setCountry(resultSet.getString(TABLE + "." + FIELD01));
			eachState.setState(resultSet.getString(TABLE + "." + FIELD02));

			eachState.getStateText().add(
					stateTextHelper.assignResult(resultSet));
			statelList.add(eachState);

		} else {

			statelList.get(statelList.size() - 1).getStateText()
					.add(stateTextHelper.assignResult(resultSet));
		}
	}

	public String prepareSelect(List<String> country, List<String> state,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = prepareStateWhereWithText(country, state,
				language, name);

		stmt = prepareWhereClause(stmt, where);

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD02;

		return stmt;
	}

	public List<String> prepareStateWhere(List<String> country,
			List<String> state) {

		List<String> where = new ArrayList<String>();

		assignFilterString(where, TABLE + "." + FIELD01, country);
		assignFilterString(where, TABLE + "." + FIELD02, state);

		return where;
	}

	public List<String> prepareStateWhereWithText(List<String> country,
			List<String> state, List<String> language, List<String> name) {

		List<String> where = prepareStateWhere(country, state);

		where.addAll(stateTextHelper.prepareStateTextWhere(null, null,
				language, name));

		return where;
	}

}
