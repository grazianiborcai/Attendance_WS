package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Class;
import br.com.mind5.helper.Place;

public class ClassHelper extends GdaDB {

	public static final String TABLE = "Class";

	public static final String FIELD01 = "ClassID";
	public static final String FIELD02 = "PlaceID";
	public static final String FIELD03 = "Name";
	public static final String FIELD04 = "BeginTime";
	public static final String FIELD05 = "Duration";

	public static final String ST_SELECT = "SELECT " + TABLE + ".*, " + PlaceHelper.TABLE + "." + PlaceHelper.FIELD01
			+ ", " + PlaceHelper.TABLE + "." + PlaceHelper.FIELD02 + ", " + WeekdayHelper.TABLE + ".* FROM " + SCHEMA
			+ "." + TABLE + " inner join " + SCHEMA + "." + PlaceHelper.TABLE + " inner join " + SCHEMA + "."
			+ ClassWeekdayHelper.TABLE + " inner join " + SCHEMA + "." + WeekdayHelper.TABLE + " inner join " + SCHEMA
			+ "." + EnrollmentHelper.TABLE + " on " + TABLE + "." + FIELD02 + " = " + PlaceHelper.TABLE + "."
			+ PlaceHelper.FIELD01 + " and " + TABLE + "." + FIELD01 + " = " + ClassWeekdayHelper.TABLE + "."
			+ ClassWeekdayHelper.FIELD01 + " and " + TABLE + "." + FIELD02 + " = " + ClassWeekdayHelper.TABLE + "."
			+ ClassWeekdayHelper.FIELD02 + " and " + ClassWeekdayHelper.TABLE + "." + ClassWeekdayHelper.FIELD03 + " = "
			+ WeekdayHelper.TABLE + "." + WeekdayHelper.FIELD01 + " and " + EnrollmentHelper.TABLE + "."
			+ EnrollmentHelper.FIELD01 + " = " + TABLE + "." + FIELD01 + " and " + EnrollmentHelper.TABLE + "."
			+ EnrollmentHelper.FIELD02 + " = " + TABLE + "." + FIELD02;

	WeekdayHelper weekdayHelper;
	PlaceHelper placeHelper;

	public ClassHelper() {
		weekdayHelper = new WeekdayHelper();
		placeHelper = new PlaceHelper();
	}

	public ClassHelper(WeekdayHelper weekdayHelper) {
		this.weekdayHelper = weekdayHelper;
	}

	public ClassHelper(PlaceHelper placeHelper) {
		this.placeHelper = placeHelper;
	}

	public void assignResult(ArrayList<Class> classList, ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !classList.get(classList.size() - 1).getClassID().equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !classList.get(classList.size() - 1).getPlace().getPlaceID()
						.equals(resultSet.getLong(TABLE + "." + FIELD02))) {

			Class cClass = assignResult(resultSet);

			cClass.getWeekday().add(weekdayHelper.assignResult(resultSet));
			classList.add(cClass);

		} else {

			classList.get(classList.size() - 1).getWeekday().add(weekdayHelper.assignResult(resultSet));
		}
	}

	public Class assignResult(ResultSet resultSet) throws SQLException {

		Class cClass = new Class();

		cClass.setClassID(resultSet.getLong(TABLE + "." + FIELD01));

		Place place = placeHelper.assignResult(resultSet);
		cClass.setPlace(place);

		cClass.setName(resultSet.getString(TABLE + "." + FIELD03));
		cClass.setBeginTime(resultSet.getTime(TABLE + "." + FIELD04).toLocalTime());
		cClass.setDuration(resultSet.getInt(TABLE + "." + FIELD05));
		cClass.setEndTime(cClass.getBeginTime().plusMinutes(cClass.getDuration()));

		return cClass;
	}

	private List<String> prepareSelectWhere(Long peopleID) {
		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, EQ, peopleID);

		return where;
	}

	public String prepareSelect(Long peopleID) {

		String stmt = ST_SELECT;

		if (peopleID != null && peopleID.equals(Long.valueOf(0))) {

			List<String> where = prepareSelectWhere(peopleID);

			stmt = prepareWhereClause(stmt, where);

			stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02;
		}

		return stmt;
	}

}
