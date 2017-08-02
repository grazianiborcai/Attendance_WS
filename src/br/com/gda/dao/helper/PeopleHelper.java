package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.People;

public class PeopleHelper extends GdaDB {

	public static final String TABLE = "People";

	public static final String FIELD01 = "PeopleID";
	public static final String FIELD02 = "CountryID";
	public static final String FIELD03 = "RegionID";
	public static final String FIELD04 = "GradeID";
	public static final String FIELD05 = "Name";
	public static final String FIELD06 = "BirthDate";
	public static final String FIELD07 = "EnrollmentNumber";
	public static final String FIELD08 = "GradeDate";
	public static final String FIELD09 = "Email";
	public static final String FIELD10 = "Celphone";
	public static final String FIELD11 = "Phone";
	public static final String FIELD12 = "Address1";
	public static final String FIELD13 = "Address2";
	public static final String FIELD14 = "PostalCode";
	public static final String FIELD15 = "BloodType";
	public static final String FIELD16 = "Allergy";
	public static final String FIELD17 = "AllergyDesc";
	public static final String FIELD18 = "NextGradeExam";
	public static final String FIELD19 = "GenderID";
	public static final String FIELD20 = "WhereID";
	public static final String FIELD21 = "WhereOther";
	public static final String FIELD22 = "LookID";
	public static final String FIELD23 = "LookOther";
	public static final String FIELD24 = "Password";
	public static final String FIELD25 = "EnrTypeID";
	public static final String FIELD26 = "OAuth";
	public static final String FIELD27 = "OAuthDate";
	public static final String FIELD28 = "UserAgent";

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD02 + ", " + FIELD03
			+ ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", " + FIELD09 + ", "
			+ FIELD10 + ", " + FIELD11 + ", " + FIELD12 + ", " + FIELD13 + ", " + FIELD14 + ", " + FIELD15 + ", "
			+ FIELD16 + ", " + FIELD17 + ", " + FIELD18 + ", " + FIELD19 + ", " + FIELD20 + ", " + FIELD21 + ", "
			+ FIELD22 + ", " + FIELD23 + ", " + FIELD24 + ", " + FIELD25 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_OAUTH_BY_FULL_KEY = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD26 + "=?"
			+ ", " + FIELD27 + "=?" + ", " + FIELD28 + "=?" + " WHERE " + FIELD01 + "=?";

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE;

	public People assignResult(ResultSet resultSet) throws SQLException {

		People people = new People();

		people.setPeopleID(resultSet.getLong(TABLE + "." + FIELD01));
		people.setCountryID(resultSet.getString(TABLE + "." + FIELD02));
		people.setRegionID(resultSet.getString(TABLE + "." + FIELD03));
		people.setGradeID(resultSet.getInt(TABLE + "." + FIELD04));
		people.setName(resultSet.getString(TABLE + "." + FIELD05));
		people.setBirthDateS(resultSet.getString(TABLE + "." + FIELD06));
		people.setEnrollmentNumber(resultSet.getLong(TABLE + "." + FIELD07));
		people.setGradeDateS(resultSet.getString(TABLE + "." + FIELD08));
		people.setEmail(resultSet.getString(TABLE + "." + FIELD09));
		people.setCelphone(resultSet.getString(TABLE + "." + FIELD10));
		people.setPhone(resultSet.getString(TABLE + "." + FIELD11));
		people.setAddress1(resultSet.getString(TABLE + "." + FIELD12));
		people.setAddress2(resultSet.getString(TABLE + "." + FIELD13));
		people.setPostalCode(resultSet.getString(TABLE + "." + FIELD14));
		people.setBloodType(resultSet.getString(TABLE + "." + FIELD15));
		people.setAllergy(resultSet.getString(TABLE + "." + FIELD16));
		people.setAllergyDesc(resultSet.getString(TABLE + "." + FIELD17));
		people.setNextGradeExamS(resultSet.getString(TABLE + "." + FIELD18));
		people.setGenderID(resultSet.getInt(TABLE + "." + FIELD19));
		people.setWhereID(resultSet.getInt(TABLE + "." + FIELD20));
		people.setWhereOther(resultSet.getString(TABLE + "." + FIELD21));
		people.setLookID(resultSet.getInt(TABLE + "." + FIELD22));
		people.setLookingOther(resultSet.getString(TABLE + "." + FIELD23));
		people.setPassword(resultSet.getString(TABLE + "." + FIELD24));
		people.setEnrTypeID(resultSet.getInt(TABLE + "." + FIELD25));

		return people;
	}

	private List<String> prepareSelectWhere(String email, String password, String oAuth) {
		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD09, EQ, email);
		singleFilter(where, TABLE + "." + FIELD24, EQ, password);
		singleFilter(where, TABLE + "." + FIELD26, EQ, oAuth);
		singleFilter(where, TABLE + "." + FIELD25, EQ, 1);

		return where;
	}

	public String prepareSelect(String email, String password, String oAuth) {

		String stmt = ST_SELECT;

		List<String> where = prepareSelectWhere(email, password, oAuth);

		stmt = prepareWhereClause(stmt, where);

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02;

		return stmt;
	}

}
