package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.TypeText;

public class TypeTextHelper extends GdaDB {

	public static final String TABLE = "Type_text";

	public static final String FIELD01 = COD_TYPE;
	public static final String FIELD02 = LANGUAGE;
	public static final String FIELD03 = NAME;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public TypeText assignResult(ResultSet resultSet) throws SQLException {

		TypeText typeText = new TypeText();

		typeText.setCodType(resultSet.getInt(FIELD01));
		typeText.setLanguage(resultSet.getString(FIELD02));
		typeText.setName(resultSet.getString(FIELD03));

		return typeText;
	}

	public String prepareSelect(List<Integer> codType, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt, prepareTypeTextWhere(codType, language, name));

		return stmt;
	}

	public List<String> prepareTypeTextWhere(List<Integer> codType,
			List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, FIELD01, codType);
		assignFilterString(where, FIELD02, language);
		assignFilterString(where, FIELD03, name);

		return where;
	}

}
