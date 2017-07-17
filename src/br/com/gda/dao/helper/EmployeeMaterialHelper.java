package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.EmployeeMaterial;

public class EmployeeMaterialHelper extends GdaDB {

	public static final String TABLE = "Employee_Material";

	public static final String FIELD01 = "Cod_owner";
	public static final String FIELD02 = "Cod_store";
	public static final String FIELD03 = "Cod_employee";
	public static final String FIELD04 = "Cod_material";
	public static final String FIELD05 = "Rate";

	public static final String ST_IN_ALL = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD01 + ", " + FIELD02
			+ ", " + FIELD03 + ", " + FIELD04 + ", " + FIELD05 + ") " + "VALUES (?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_BY_FULL_KEY = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD04 + "=?"
			+ ", " + FIELD05 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02 + "=? AND " + FIELD03 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SEL_ALL_W_EMP = "SELECT * FROM " + SCHEMA + "." + TABLE;

	public EmployeeMaterial assignResult(ResultSet resultSet) throws SQLException {

		EmployeeMaterial employee = new EmployeeMaterial();

		employee.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		employee.setCodStore(resultSet.getInt(TABLE + "." + FIELD02));
		employee.setCodEmployee(resultSet.getInt(TABLE + "." + FIELD03));
		employee.setCodMaterial(resultSet.getLong(TABLE + "." + FIELD04));
		employee.setRate(resultSet.getInt(TABLE + "." + FIELD05));

		return employee;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<Long> codMaterial, List<Integer> rate) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt, prepareStoreEmployeeWhere(codOwner, codStore, codEmployee, codMaterial, rate));

		return stmt;
	}

	public String prepareDelete(String op01, Long codOwner, String op02, Integer codStore, String op03,
			Integer codEmployee) {

		String stmtDeleteT01 = ST_DELETE;

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, op01, codOwner);
		singleFilter(where, TABLE + "." + FIELD02, op02, codStore);
		singleFilter(where, TABLE + "." + FIELD03, op03, codEmployee);

		stmtDeleteT01 = prepareWhereClause(stmtDeleteT01, where);

		return stmtDeleteT01;
	}

	public String prepareDeleteToStore(Long codOwner, Integer codStore, List<Integer> codEmployee) {

		String stmtDeleteT01 = ST_DELETE;

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, EQ, codOwner);
		singleFilter(where, TABLE + "." + FIELD02, EQ, codStore);
		assignFilterInt(where, TABLE + "." + FIELD03, codEmployee);

		stmtDeleteT01 = prepareWhereClause(stmtDeleteT01, where);

		return stmtDeleteT01;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<Long> codMaterial, List<Integer> rate) {

		String stmt = ST_SEL_ALL_W_EMP;

		List<String> where = prepareStoreEmployeeWhere(codOwner, codStore, codEmployee, codMaterial, rate);

		stmt = prepareWhereClause(stmt, where);

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02 + ", " + TABLE + "."
				+ FIELD03;
		return stmt;
	}

	public List<String> prepareStoreEmployeeWhere(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<Long> codMaterial, List<Integer> rate) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codStore);
		assignFilterInt(where, TABLE + "." + FIELD03, codEmployee);
		assignFilterLong(where, TABLE + "." + FIELD04, codMaterial);
		assignFilterInt(where, TABLE + "." + FIELD05, rate);

		return where;
	}

}
