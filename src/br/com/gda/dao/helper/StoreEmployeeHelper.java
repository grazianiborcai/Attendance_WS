package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.StoreEmployee;

public class StoreEmployeeHelper extends GdaDB {

	public static final String TABLE = "Store_Emp";

	public static final String FIELD01 = "Cod_owner";
	public static final String FIELD02 = "Cod_store";
	public static final String FIELD03 = "Cod_employee";
	public static final String FIELD04 = "Cod_position_store";
	public static final String FIELD05 = "Record_mode";

	public static final String ST_IN_ALL = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ", " + FIELD05 + ") " + "VALUES (?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_BY_FULL_KEY = "UPDATE " + SCHEMA + "."
			+ TABLE + " SET " + FIELD04 + "=?" + ", " + FIELD05 + "=?"
			+ " WHERE " + FIELD01 + "=? AND " + FIELD02 + "=? AND " + FIELD03
			+ "=?";

	/************************** To attend EmployeeDao **************************/
	public static final String ST_UP_RECORD_BY_EMPLOYEE = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD05 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD03 + "=?";
	/***************************************************************************/

	/************************** To attend StoreDAO *****************************/
	public static final String ST_UP_RECORD_BY_STORE = "UPDATE " + SCHEMA + "."
			+ TABLE + " SET " + FIELD05 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD02 + "=?";
	/***************************************************************************/

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SEL_ALL_W_EMP = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "." + EmployeeHelper.TABLE
			+ " ON " + TABLE + "." + FIELD01 + " = " + EmployeeHelper.TABLE
			+ "." + EmployeeHelper.FIELD01 + " AND " + TABLE + "." + FIELD03
			+ " = " + EmployeeHelper.TABLE + "." + EmployeeHelper.FIELD02;

	EmployeeHelper employeeHelper;

	public StoreEmployeeHelper() {
		employeeHelper = new EmployeeHelper();
	}

	public StoreEmployeeHelper(EmployeeHelper employeeHelper) {
		this.employeeHelper = employeeHelper;
	}

	public StoreEmployee assignResult(ResultSet resultSet) throws SQLException {

		StoreEmployee employee = new StoreEmployee(
				employeeHelper.assignResult(resultSet));

		employee.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		employee.setCodStore(resultSet.getInt(TABLE + "." + FIELD02));
		employee.setCodEmployee(resultSet.getInt(TABLE + "." + FIELD03));
		employee.setCodPositionStore(resultSet.getByte(TABLE + "." + FIELD04));
		employee.setRecordMode(resultSet.getString(TABLE + "." + FIELD05));

		return employee;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<Byte> codPosition,
			List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareStoreEmployeeWhere(codOwner, codStore, codEmployee,
						codPosition, recordMode));

		return stmt;
	}

	public String prepareDelete(String op01, Long codOwner, String op02,
			Integer codStore, String op03, Integer codEmployee) {

		String stmtDeleteT01 = ST_DELETE;

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, op01, codOwner);
		singleFilter(where, TABLE + "." + FIELD02, op02, codStore);
		singleFilter(where, TABLE + "." + FIELD03, op03, codEmployee);

		stmtDeleteT01 = prepareWhereClause(stmtDeleteT01, where);

		return stmtDeleteT01;
	}

	public String prepareDeleteToStore(Long codOwner, Integer codStore,
			List<Integer> codEmployee) {

		String stmtDeleteT01 = ST_DELETE;

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, EQ, codOwner);
		singleFilter(where, TABLE + "." + FIELD02, EQ, codStore);
		assignFilterInt(where, TABLE + "." + FIELD03, codEmployee);

		stmtDeleteT01 = prepareWhereClause(stmtDeleteT01, where);

		return stmtDeleteT01;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<String> cpf, List<String> password,
			List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1,
			List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) {

		String stmt = ST_SEL_ALL_W_EMP;

		List<String> where = prepareStoreEmployeeWhereWithEmployee(codOwner,
				codStore, codEmployee, cpf, password, name, codPosition,
				codGender, bornDate, email, address1, address2, postalcode,
				city, country, state, phone, recordMode);

		stmt = prepareWhereClause(stmt, where);

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD02 + ", " + TABLE + "." + FIELD03;
		return stmt;
	}

	public List<String> prepareStoreEmployeeWhere(List<Long> codOwner,
			List<Integer> codStore, List<Integer> codEmployee,
			List<Byte> codPosition, List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codStore);
		assignFilterInt(where, TABLE + "." + FIELD03, codEmployee);
		assignFilterByte(where, TABLE + "." + FIELD04, codPosition);
		assignFilterString(where, TABLE + "." + FIELD05, recordMode);

		return where;
	}

	public List<String> prepareStoreEmployeeWhereWithEmployee(
			List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<String> cpf, List<String> password,
			List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1,
			List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) {

		List<String> where = prepareStoreEmployeeWhere(codOwner, codStore,
				codEmployee, codPosition, recordMode);

		where.addAll(employeeHelper.prepareEmployeeWhere(null, null, cpf,
				password, name, null, codGender, bornDate, email, address1,
				address2, postalcode, city, country, state, phone, null));

		return where;
	}

}
