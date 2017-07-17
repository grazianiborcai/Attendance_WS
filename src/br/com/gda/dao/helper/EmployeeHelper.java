package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Employee;

public class EmployeeHelper extends GdaDB {

	public static final String TABLE = "Employee";

	public static final String FIELD01 = "Cod_owner";
	public static final String FIELD02 = "Cod_employee";
	public static final String FIELD03 = "CPF";
	public static final String FIELD04 = "Password";
	public static final String FIELD05 = "Name";
	public static final String FIELD06 = "Cod_position";
	public static final String FIELD07 = "Cod_gender";
	public static final String FIELD08 = "Born_date";
	public static final String FIELD09 = "Email";
	public static final String FIELD10 = "Address1";
	public static final String FIELD11 = "Address2";
	public static final String FIELD12 = "Postalcode";
	public static final String FIELD13 = "City";
	public static final String FIELD14 = "Country";
	public static final String FIELD15 = "State_province";
	public static final String FIELD16 = "Phone";
	public static final String FIELD17 = "Record_mode";
	public static final String FIELD18 = "Begin_time";
	public static final String FIELD19 = "End_time";

	public static final String ST_IN_ALL = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD01 + ", " + FIELD03
			+ ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", " + FIELD09 + ", "
			+ FIELD10 + ", " + FIELD11 + ", " + FIELD12 + ", " + FIELD13 + ", " + FIELD14 + ", " + FIELD15 + ", "
			+ FIELD16 + ", " + FIELD17 + ", " + FIELD18 + ", " + FIELD19 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD03 + "=?" + ", " + FIELD04
			+ "=?" + ", " + FIELD05 + "=?" + ", " + FIELD06 + "=?" + ", " + FIELD07 + "=?" + ", " + FIELD08 + "=?"
			+ ", " + FIELD09 + "=?" + ", " + FIELD10 + "=?" + ", " + FIELD11 + "=?" + ", " + FIELD12 + "=?" + ", "
			+ FIELD13 + "=?" + ", " + FIELD14 + "=?" + ", " + FIELD15 + "=?" + ", " + FIELD16 + "=?" + ", " + FIELD17
			+ "=?" + ", " + FIELD18 + "=?" + ", " + FIELD19 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE;

	public Employee assignResult(ResultSet resultSet) throws SQLException {

		Employee employee = new Employee();

		employee.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		employee.setCodEmployee(resultSet.getInt(TABLE + "." + FIELD02));
		employee.setCpf(resultSet.getString(TABLE + "." + FIELD03));
		employee.setPassword(resultSet.getString(TABLE + "." + FIELD04));
		employee.setName(resultSet.getString(TABLE + "." + FIELD05));
		employee.setCodPosition(resultSet.getByte(TABLE + "." + FIELD06));
		employee.setCodGender(resultSet.getByte(TABLE + "." + FIELD07));
		employee.setBornDate(resultSet.getDate(TABLE + "." + FIELD08).toLocalDate());
		employee.setEmail(resultSet.getString(TABLE + "." + FIELD09));
		employee.setAddress1(resultSet.getString(TABLE + "." + FIELD10));
		employee.setAddress2(resultSet.getString(TABLE + "." + FIELD11));
		employee.setPostalcode(resultSet.getInt(TABLE + "." + FIELD12));
		employee.setCity(resultSet.getString(TABLE + "." + FIELD13));
		employee.setCountry(resultSet.getString(TABLE + "." + FIELD14));
		employee.setState(resultSet.getString(TABLE + "." + FIELD15));
		employee.setPhone(resultSet.getString(TABLE + "." + FIELD16));
		employee.setRecordMode(resultSet.getString(TABLE + "." + FIELD17));
		employee.setBeginTime(resultSet.getTime(TABLE + "." + FIELD18).toLocalTime());
		employee.setEndTime(resultSet.getTime(TABLE + "." + FIELD19).toLocalTime());
		return employee;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf, List<String> password,
			List<String> name, List<Byte> codPosition, List<Byte> codGender, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt, prepareEmployeeWhere(codOwner, codEmployee, cpf, password, name, codPosition,
				codGender, bornDate, email, address1, address2, postalcode, city, country, state, phone, recordMode));
		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf, List<String> password,
			List<String> name, List<Byte> codPosition, List<Byte> codGender, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> recordMode) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt, prepareEmployeeWhere(codOwner, codEmployee, cpf, password, name, codPosition,
				codGender, bornDate, email, address1, address2, postalcode, city, country, state, phone, recordMode));

		return stmt;
	}

	public List<String> prepareEmployeeWhere(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf,
			List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codEmployee);
		assignFilterString(where, TABLE + "." + FIELD03, cpf);
		assignFilterString(where, TABLE + "." + FIELD04, password);
		assignFilterString(where, TABLE + "." + FIELD05, name);
		assignFilterByte(where, TABLE + "." + FIELD06, codPosition);
		assignFilterByte(where, TABLE + "." + FIELD07, codGender);
		assignFilterString(where, TABLE + "." + FIELD08, bornDate);
		assignFilterString(where, TABLE + "." + FIELD09, email);
		assignFilterString(where, TABLE + "." + FIELD10, address1);
		assignFilterString(where, TABLE + "." + FIELD11, address2);
		assignFilterInt(where, TABLE + "." + FIELD12, postalcode);
		assignFilterString(where, TABLE + "." + FIELD13, city);
		assignFilterString(where, TABLE + "." + FIELD14, country);
		assignFilterString(where, TABLE + "." + FIELD15, state);
		assignFilterString(where, TABLE + "." + FIELD16, phone);
		assignFilterString(where, TABLE + "." + FIELD17, recordMode);

		return where;
	}

}
