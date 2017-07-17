package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.StoreEmployeeHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.db.GdaDB;
import br.com.mind5.helper.RecordMode;
import br.com.mind5.helper.StoreEmployee;

public class StoreEmployeeDAO extends ConnectionBD {

	public SQLException insertStoreEmployee(ArrayList<StoreEmployee> storeList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn
					.prepareStatement(StoreEmployeeHelper.ST_IN_ALL);

			for (StoreEmployee storeEmployee : storeList) {

				prepareInsert(insertStmtT01, storeEmployee);
			}

			insertStmtT01.executeBatch();

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
			closeConnection(conn, insertStmtT01);
		}

	}

	public SQLException updateStoreEmployee(ArrayList<StoreEmployee> storeList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement deleteStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn
					.prepareStatement(StoreEmployeeHelper.ST_IN_ALL);

			updateStmtT01 = conn
					.prepareStatement(StoreEmployeeHelper.ST_UP_ALL_BY_FULL_KEY);

			StoreEmployeeHelper storeEmployeeHelper = new StoreEmployeeHelper();
			for (StoreEmployee storeEmployee : storeList) {

				if (storeEmployee.getRecordMode() != null
						&& storeEmployee.getRecordMode().equals(
								RecordMode.ISNEW)) {

					prepareInsert(insertStmtT01, storeEmployee);

				} else {

					if (storeEmployee.getRecordMode() != null
							&& (storeEmployee.getRecordMode().equals(
									RecordMode.ISDELETED) || storeEmployee
									.getRecordMode().equals(
											RecordMode.RECORD_DELETED))) {

						deleteStmtT01 = conn
								.prepareStatement(storeEmployeeHelper
										.prepareDelete(GdaDB.EQ,
												storeEmployee.getCodOwner(),
												GdaDB.EQ,
												storeEmployee.getCodStore(),
												GdaDB.EQ,
												storeEmployee.getCodEmployee()));

						deleteStmtT01.execute();

					} else {

						updateStmtT01.setInt(
								1,
								storeEmployee.getCodPositionStore() != null
										&& !storeEmployee.getCodPositionStore()
												.equals(0) ? storeEmployee
										.getCodPositionStore() : storeEmployee
										.getCodPosition());
						updateStmtT01.setString(2, RecordMode.RECORD_OK);
						updateStmtT01.setLong(3, storeEmployee.getCodOwner());
						updateStmtT01.setInt(4, storeEmployee.getCodStore());
						updateStmtT01.setInt(5, storeEmployee.getCodEmployee());

						updateStmtT01.addBatch();
					}
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
			closeConnection(conn, deleteStmtT01, insertStmtT01, updateStmtT01);
		}
	}

	public SQLException deleteStoreEmployee(List<Long> codOwner,
			List<Integer> codStore, List<Integer> codEmployee,
			List<Byte> codPosition, List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new StoreEmployeeHelper()
					.prepareDelete(codOwner, codStore, codEmployee,
							codPosition, recordMode));

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

	public ArrayList<StoreEmployee> selectStoreEmployee(List<Long> codOwner,
			List<Integer> codStore, List<Integer> codEmployee,
			List<String> cpf, List<String> password, List<String> name,
			List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1,
			List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) throws SQLException {

		ArrayList<StoreEmployee> employeeList = new ArrayList<StoreEmployee>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			StoreEmployeeHelper storeEmployeeHelper = new StoreEmployeeHelper();

			selectStmt = conn.prepareStatement(storeEmployeeHelper
					.prepareSelect(codOwner, codStore, codEmployee, cpf,
							password, name, codPosition, codGender, bornDate,
							email, address1, address2, postalcode, city,
							country, state, phone, recordMode));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				employeeList.add(storeEmployeeHelper.assignResult(resultSet));
			}

			return employeeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmtT01,
			StoreEmployee storeEmployee) throws SQLException {

		insertStmtT01.setLong(1, storeEmployee.getCodOwner());
		insertStmtT01.setInt(2, storeEmployee.getCodStore());
		insertStmtT01.setInt(3, storeEmployee.getCodEmployee());
		insertStmtT01
				.setInt(4,
						storeEmployee.getCodPositionStore() != null
								&& !storeEmployee.getCodPositionStore().equals(
										0) ? storeEmployee
								.getCodPositionStore() : storeEmployee
								.getCodPosition());
		insertStmtT01.setString(5, RecordMode.RECORD_OK);

		insertStmtT01.addBatch();
	}

}
