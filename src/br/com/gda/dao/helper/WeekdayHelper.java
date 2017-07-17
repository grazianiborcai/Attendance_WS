package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Weekday;

public class WeekdayHelper extends GdaDB {

	public static final String TABLE = "Weekday";

	public static final String FIELD01 = WEEKDAY;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "."
			+ WeekdayTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ WeekdayTextHelper.TABLE + "." + WeekdayTextHelper.FIELD01;

	WeekdayTextHelper weekdayTextHelper;

	public WeekdayHelper() {
		weekdayTextHelper = new WeekdayTextHelper();
	}

	public WeekdayHelper(WeekdayTextHelper weekdayTextHelper) {
		this.weekdayTextHelper = weekdayTextHelper;
	}

	public void assignResult(ArrayList<Weekday> weekdayList, ResultSet resultSet)
			throws SQLException {

		if (resultSet.isFirst()
				|| !weekdayList.get(weekdayList.size() - 1).getWeekday()
						.equals(resultSet.getInt(TABLE + "." + FIELD01))) {

			Weekday eachWeekday = new Weekday();

			eachWeekday.setWeekday(resultSet.getInt(TABLE + "." + FIELD01));

			eachWeekday.getWeekdayText().add(
					weekdayTextHelper.assignResult(resultSet));
			weekdayList.add(eachWeekday);

		} else {

			weekdayList.get(weekdayList.size() - 1).getWeekdayText()
					.add(weekdayTextHelper.assignResult(resultSet));
		}
	}

	public String prepareSelect(List<Integer> weekday, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, TABLE + "." + FIELD01, weekday);

		where.addAll(weekdayTextHelper.prepareWeekdayTextWhere(null, language,
				name));

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

}
