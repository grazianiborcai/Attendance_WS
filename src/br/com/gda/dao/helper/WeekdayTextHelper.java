package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.WeekdayText;

public class WeekdayTextHelper extends GdaDB {

	public static final String TABLE = "Weekday_text";

	public static final String FIELD01 = WEEKDAY;
	public static final String FIELD02 = LANGUAGE;
	public static final String FIELD03 = NAME;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public WeekdayText assignResult(ResultSet resultSet) throws SQLException {

		WeekdayText weekdayText = new WeekdayText();

		weekdayText.setWeekday(resultSet.getInt(FIELD01));
		weekdayText.setLanguage(resultSet.getString(FIELD02));
		weekdayText.setName(resultSet.getString(FIELD03));

		return weekdayText;
	}

	public String prepareSelect(List<Integer> weekday, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt,
				prepareWeekdayTextWhere(weekday, language, name));

		return stmt;
	}

	public List<String> prepareWeekdayTextWhere(List<Integer> weekday,
			List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, FIELD01, weekday);
		assignFilterString(where, FIELD02, language);
		assignFilterString(where, FIELD03, name);

		return where;
	}
}
