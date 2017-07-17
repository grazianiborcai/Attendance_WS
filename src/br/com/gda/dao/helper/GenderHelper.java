package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Gender;

public class GenderHelper extends GdaDB {

	public static final String TABLE = "Gender";

	public static final String FIELD01 = COD_GENDER;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "."
			+ GenderTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ GenderTextHelper.TABLE + "." + GenderTextHelper.FIELD01;

	GenderTextHelper genderTextHelper;

	public GenderHelper() {
		genderTextHelper = new GenderTextHelper();
	}

	public GenderHelper(GenderTextHelper genderTextHelper) {
		this.genderTextHelper = genderTextHelper;
	}

	public void assignResult(ArrayList<Gender> genderlList, ResultSet resultSet)
			throws SQLException {

		if (resultSet.isFirst()
				|| !genderlList.get(genderlList.size() - 1).getCodGender()
						.equals(resultSet.getInt(TABLE + "." + FIELD01))) {

			Gender gender = new Gender();

			gender.setCodGender(resultSet.getInt(TABLE + "." + FIELD01));

			gender.getGenderText()
					.add(genderTextHelper.assignResult(resultSet));
			genderlList.add(gender);

		} else {

			genderlList.get(genderlList.size() - 1).getGenderText()
					.add(genderTextHelper.assignResult(resultSet));
		}
	}

	public String prepareSelect(List<Integer> codGender, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, TABLE + "." + FIELD01, codGender);

		where.addAll(genderTextHelper.prepareGenderTextWhere(null, language,
				name));

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

}
