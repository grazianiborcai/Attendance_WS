package br.com.gda.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.EmployeeHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.Employee;
import br.com.gda.helper.RecordMode;

public class EmployeeDAO extends ConnectionBD {

	public SQLException insertEmployee(ArrayList<Employee> employeeList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(EmployeeHelper.ST_IN_ALL);

			for (Employee employee : employeeList) {

				prepareInsert(insertStmt, employee);
			}

			insertStmt.executeBatch();

			conn.commit();

			return new SQLException(INSERT_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, insertStmt);
		}

	}

	public SQLException updateEmployee(ArrayList<Employee> employeeList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement updateStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(EmployeeHelper.ST_IN_ALL);

			updateStmtT01 = conn.prepareStatement(EmployeeHelper.ST_UP_ALL);

			for (Employee employee : employeeList) {

				if ((employee.getRecordMode() != null && employee.getRecordMode().equals(RecordMode.ISNEW))
						|| employee.getCodEmployee() == null || employee.getCodEmployee().equals(0)) {

					prepareInsert(insertStmtT01, employee);

				} else {

					updateStmtT01.setString(1, employee.getCpf());
					updateStmtT01.setString(2, employee.getPassword());
					updateStmtT01.setString(3, employee.getName());
					updateStmtT01.setByte(4, employee.getCodPosition());
					updateStmtT01.setByte(5, employee.getCodGender());
					updateStmtT01.setDate(6, Date.valueOf(employee.getBornDate()));
					updateStmtT01.setString(7, employee.getEmail());
					updateStmtT01.setString(8, employee.getAddress1());
					updateStmtT01.setString(9, employee.getAddress2());
					updateStmtT01.setInt(10, employee.getPostalcode());
					updateStmtT01.setString(11, employee.getCity());
					updateStmtT01.setString(12, employee.getCountry());
					updateStmtT01.setString(13, employee.getState());
					updateStmtT01.setString(14, employee.getPhone());
					if (employee.getRecordMode() != null && (employee.getRecordMode().equals(RecordMode.ISDELETED)
							|| employee.getRecordMode().equals(RecordMode.RECORD_DELETED))) {
						updateStmtT01.setString(15, RecordMode.RECORD_DELETED);

					} else {
						updateStmtT01.setString(15, RecordMode.RECORD_OK);
					}
					updateStmtT01.setTime(16, Time.valueOf(employee.getBeginTime()));
					updateStmtT01.setTime(17, Time.valueOf(employee.getEndTime()));

					updateStmtT01.setLong(18, employee.getCodOwner());
					updateStmtT01.setInt(19, employee.getCodEmployee());

					updateStmtT01.addBatch();

				}
			}

			insertStmtT01.executeBatch();

			updateStmtT01.executeBatch();

			conn.commit();

			return new SQLException(UPDATE_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, insertStmtT01, updateStmtT01);
		}
	}

	public SQLException deleteEmployee(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf,
			List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new EmployeeHelper().prepareDelete(codOwner, codEmployee, cpf, password,
					name, codPosition, codGender, bornDate, email, address1, address2, postalcode, city, country, state,
					phone, recordMode));

			deleteStmt.execute();

			conn.commit();

			return new SQLException(DELETE_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, deleteStmt);
		}
	}

	public ArrayList<Employee> selectEmployee(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf,
			List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) throws SQLException {

		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			EmployeeHelper employeeHelper = new EmployeeHelper();

			selectStmt = conn.prepareStatement(
					employeeHelper.prepareSelect(codOwner, codEmployee, cpf, password, name, codPosition, codGender,
							bornDate, email, address1, address2, postalcode, city, country, state, phone, recordMode));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				employeeList.add(employeeHelper.assignResult(resultSet));
			}

			return employeeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt, Employee employee) throws SQLException {

		insertStmt.setLong(1, employee.getCodOwner());
		insertStmt.setString(2, employee.getCpf());
		insertStmt.setString(3, employee.getPassword());
		insertStmt.setString(4, employee.getName());
		insertStmt.setByte(5, employee.getCodPosition());
		insertStmt.setByte(6, employee.getCodGender());
		insertStmt.setDate(7, Date.valueOf(employee.getBornDate()));
		insertStmt.setString(8, employee.getEmail());
		insertStmt.setString(9, employee.getAddress1());
		insertStmt.setString(10, employee.getAddress2());
		insertStmt.setInt(11, employee.getPostalcode());
		insertStmt.setString(12, employee.getCity());
		insertStmt.setString(13, employee.getCountry());
		insertStmt.setString(14, employee.getState());
		insertStmt.setString(15, employee.getPhone());
		insertStmt.setString(16, RecordMode.RECORD_OK);
		insertStmt.setTime(17, Time.valueOf(employee.getBeginTime()));
		insertStmt.setTime(18, Time.valueOf(employee.getEndTime()));

		insertStmt.addBatch();
	}

}
