package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Enrollment;

public class EnrollmentHelper extends GdaDB {

	public static final String TABLE = "Enrollment";

	public static final String FIELD01 = "ClassID";
	public static final String FIELD02 = "PlaceID";
	public static final String FIELD03 = "PeopleID";
	public static final String FIELD04 = "EnrTypeID";
	public static final String FIELD05 = "BeginDate";
	public static final String FIELD06 = "EndDate";

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD01 + ", " + FIELD02
			+ ", " + FIELD03 + ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ") " + "VALUES (?, ?, ?, ?, ?, ?)";

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE + " inner join " + SCHEMA + "."
			+ PeopleHelper.TABLE + " on " + TABLE + "." + FIELD03 + " = " + PeopleHelper.TABLE + "."
			+ PeopleHelper.FIELD01;

	PeopleHelper peopleHelper;
	PlaceHelper placeHelper;

	public EnrollmentHelper() {
		peopleHelper = new PeopleHelper();
	}

	public EnrollmentHelper(PeopleHelper peopleHelper) {
		this.peopleHelper = peopleHelper;
	}

	public void assignResult(ArrayList<Enrollment> enrollmentList, ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !enrollmentList.get(enrollmentList.size() - 1).getClassID()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !enrollmentList.get(enrollmentList.size() - 1).getPlaceID()
						.equals(resultSet.getLong(TABLE + "." + FIELD02))) {

			Enrollment enrollment = assignResult(resultSet);

			enrollment.getPeople().add(peopleHelper.assignResult(resultSet));
			enrollmentList.add(enrollment);

		} else {

			enrollmentList.get(enrollmentList.size() - 1).getPeople().add(peopleHelper.assignResult(resultSet));
		}
	}

	public Enrollment assignResult(ResultSet resultSet) throws SQLException {

		Enrollment enrollment = new Enrollment();

		enrollment.setClassID(resultSet.getLong(TABLE + "." + FIELD01));
		enrollment.setPlaceID(resultSet.getLong(TABLE + "." + FIELD02));
		enrollment.setEnrTypeID(resultSet.getInt(TABLE + "." + FIELD04));
		enrollment.setBeginDate(resultSet.getDate(TABLE + "." + FIELD05).toLocalDate());
		resultSet.getInt(TABLE + "." + FIELD06);
		if (!resultSet.wasNull())
			enrollment.setEndDate(resultSet.getDate(TABLE + "." + FIELD06).toLocalDate());

		return enrollment;
	}

	private List<String> prepareSelectWhere(Long classID) {
		List<String> where = new ArrayList<String>();

		singleFilter(where, EnrollmentHelper.TABLE + "." + EnrollmentHelper.FIELD01, EQ, classID);

		return where;
	}

	public String prepareSelect(Long classID) {

		String stmt = ST_SELECT;

		if (classID != null && !classID.equals(Long.valueOf(0))) {

			List<String> where = prepareSelectWhere(classID);

			stmt = prepareWhereClause(stmt, where);

			stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02;
		}

		return stmt;
	}

}
