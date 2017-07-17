package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Owner;

public class OwnerHelper extends GdaDB {

	public static final String TABLE = "Owner";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = PASSWORD;
	public static final String FIELD03 = NAME;
	public static final String FIELD04 = CPF;
	public static final String FIELD05 = PHONE;
	public static final String FIELD06 = EMAIL;
	public static final String FIELD07 = EMAIL_AUX;
	public static final String FIELD08 = COD_GENDER;
	public static final String FIELD09 = BORN_DATE;
	public static final String FIELD10 = POSTALCODE;
	public static final String FIELD11 = ADDRESS1;
	public static final String FIELD12 = ADDRESS2;
	public static final String FIELD13 = CITY;
	public static final String FIELD14 = COUNTRY;
	public static final String FIELD15 = STATE_PROVINCE;
	public static final String FIELD16 = COD_CURR;
	public static final String FIELD17 = RECORD_MODE;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD02 + ", " + FIELD03 + ", " + FIELD04 + ", "
			+ FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", "
			+ FIELD09 + ", " + FIELD10 + ", " + FIELD11 + ", " + FIELD12 + ", "
			+ FIELD13 + ", " + FIELD14 + ", " + FIELD15 + ", " + FIELD16 + ", " + FIELD17 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD02 + "=?" + ", " + FIELD03 + "=?"
			+ ", " + FIELD04 + "=?" + ", " + FIELD05 + "=?" + ", " + FIELD06
			+ "=?" + ", " + FIELD07 + "=?" + ", " + FIELD08 + "=?" + ", "
			+ FIELD09 + "=?" + ", " + FIELD10 + "=?" + ", " + FIELD11 + "=?"
			+ ", " + FIELD12 + "=?" + ", " + FIELD13 + "=?" + ", " + FIELD14
			+ "=?" + ", " + FIELD15 + "=?" + ", " + FIELD16 + "=?" + ", " + FIELD17 + "=?" + " WHERE "
			+ FIELD01 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;
	
	public static final String ST_SELECT_LAST_INSERT_ID = "SELECT " + LAST_INSERT_ID;

	public Owner assignResult(ResultSet resultSet) throws SQLException {

		Owner owner = new Owner();

		owner.setCodOwner(resultSet.getLong(FIELD01));
		owner.setPassword(resultSet.getString(FIELD02));
		owner.setName(resultSet.getString(FIELD03));
		owner.setCpf(resultSet.getString(FIELD04));
		owner.setPhone(resultSet.getString(FIELD05));
		owner.setEmail(resultSet.getString(FIELD06));
		owner.setEmailAux(resultSet.getString(FIELD07));
		owner.setCodGender(resultSet.getByte(FIELD08));
		owner.setBornDate(resultSet.getDate(FIELD09).toLocalDate());
		owner.setPostalcode(resultSet.getInt(FIELD10));
		owner.setAddress1(resultSet.getString(FIELD11));
		owner.setAddress2(resultSet.getString(FIELD12));
		owner.setCity(resultSet.getString(FIELD13));
		owner.setCountry(resultSet.getString(FIELD14));
		owner.setState(resultSet.getString(FIELD15));
		owner.setCodCurr(resultSet.getString(FIELD16));
		owner.setRecordMode(resultSet.getString(FIELD17));

		return owner;
	}

	public String prepareDelete(List<Long> codOwner, List<String> password,
			List<String> name, List<String> cpf, List<String> phone,
			List<String> email, List<String> emailAux, List<Byte> codGender,
			List<String> bornDate, List<Integer> postalcode,
			List<String> address1, List<String> address2, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareOwnerWhereDelete(codOwner, password, name, cpf, phone, email,
						emailAux, codGender, bornDate, postalcode, address1,
						address2, city, country, state, codCurr, recordMode));

		return stmt;
	}

	public String prepareSelect(String email, String cpf, String password, List<String> recordMode) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(
				stmt,
				prepareOwnerWhere(email, cpf, password, recordMode));

		return stmt;
	}

	public List<String> prepareOwnerWhere(String email, String cpf, String password,  List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		singleFilter(where, FIELD02, EQ, password);
		singleFilter(where, FIELD04, EQ, cpf);
		singleFilter(where, FIELD06, EQ, email);
		assignFilterString(where, FIELD17, recordMode);

		return where;
	}
	
	public List<String> prepareOwnerWhereDelete(List<Long> codOwner, List<String> password,
			List<String> name, List<String> cpf, List<String> phone,
			List<String> email, List<String> emailAux, List<Byte> codGender,
			List<String> bornDate, List<Integer> postalcode,
			List<String> address1, List<String> address2, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, FIELD01, codOwner);
		assignFilterString(where, FIELD02, password);
		assignFilterString(where, FIELD03, name);
		assignFilterString(where, FIELD04, cpf);
		assignFilterString(where, FIELD05, phone);
		assignFilterString(where, FIELD06, email);
		assignFilterString(where, FIELD07, emailAux);
		assignFilterByte(where, FIELD08, codGender);
		assignFilterString(where, FIELD09, bornDate);
		assignFilterInt(where, FIELD10, postalcode);
		assignFilterString(where, FIELD11, address1);
		assignFilterString(where, FIELD12, address2);
		assignFilterString(where, FIELD13, city);
		assignFilterString(where, FIELD14, country);
		assignFilterString(where, FIELD15, state);
		assignFilterString(where, FIELD16, codCurr);
		assignFilterString(where, FIELD17, recordMode);

		return where;
	}

}
