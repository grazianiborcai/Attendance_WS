package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Language;

public class LanguageHelper extends GdaDB {

	public static final String TABLE = "Language";

	public static final String FIELD01 = LANGUAGE;
	public static final String FIELD02 = NAME;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Language assignResult(ResultSet resultSet) throws SQLException {

		Language eachLanguage = new Language();

		eachLanguage.setLanguage(resultSet.getString(FIELD01));
		eachLanguage.setName(resultSet.getString(FIELD02));

		return eachLanguage;
	}

	public String prepareSelect(List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		List<String> where = new ArrayList<String>();

		assignFilterString(where, FIELD01, language);
		assignFilterString(where, FIELD02, name);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

}
