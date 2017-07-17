package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Gender;

public class GenderTextHelper extends GdaDB {

	public static final String TABLE = "Gender_text";

	public static final String FIELD01 = COD_GENDER;
	public static final String FIELD02 = LANGUAGE;
	public static final String FIELD03 = NAME;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Gender assignResult(ResultSet resultSet) throws SQLException {

		Gender genderText = new Gender();

		genderText.setCodGender(resultSet.getInt(FIELD01));
		genderText.setLanguage(resultSet.getString(FIELD02));
		genderText.setName(resultSet.getString(FIELD03));

		return genderText;
	}

	public String prepareSelect(List<Integer> codGender, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt,
				prepareGenderTextWhere(codGender, language, name));

		return stmt;
	}

	public List<String> prepareGenderTextWhere(List<Integer> codGender,
			List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, FIELD01, codGender);
		assignFilterString(where, FIELD02, language);
		assignFilterString(where, FIELD03, name);

		return where;
	}

}
