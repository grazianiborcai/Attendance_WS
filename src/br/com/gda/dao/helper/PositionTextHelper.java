package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.PositionText;

public class PositionTextHelper extends GdaDB {

	public static final String TABLE = "Position_text";

	public static final String FIELD01 = COD_POSITION;
	public static final String FIELD02 = LANGUAGE;
	public static final String FIELD03 = NAME;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public PositionText assignResult(ResultSet resultSet) throws SQLException {

		PositionText positionText = new PositionText();

		positionText.setCodPosition(resultSet.getInt(FIELD01));
		positionText.setLanguage(resultSet.getString(FIELD02));
		positionText.setName(resultSet.getString(FIELD03));

		return positionText;
	}

	public String prepareSelect(List<Integer> codPosition,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		List<String> where = preparePositionTextWhere(codPosition, language,
				name);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public List<String> preparePositionTextWhere(List<Integer> codPosition,
			List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, FIELD01, codPosition);
		assignFilterString(where, FIELD02, language);
		assignFilterString(where, FIELD03, name);

		return where;
	}

}
