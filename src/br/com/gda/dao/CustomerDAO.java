package br.com.gda.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.CustomerHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Customer;
import br.com.mind5.helper.RecordMode;

public class CustomerDAO extends ConnectionBD {

	public SQLException insertCustomer(ArrayList<Customer> customerList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(CustomerHelper.ST_IN_ALL);

			selectStmt = conn.prepareStatement(CustomerHelper.ST_SELECT_LAST_INSERT_ID);

			for (Customer customer : customerList) {

				prepareInsert(insertStmt, customer);

				insertStmt.executeUpdate();

				resultSet = selectStmt.executeQuery();

				if (resultSet.next())
					customer.setCodCustomer(resultSet.getLong(CustomerHelper.LAST_INSERT_ID));
			}

			conn.commit();

			return new SQLException(INSERT_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				if (e.getErrorCode() == 1062)
					e = new SQLException("Email j� cadastrado. Deseja solicitar a recupera��o de senha?",
							e.getSQLState(), e.getErrorCode());
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, insertStmt, selectStmt, resultSet);
		}

	}

	public SQLException updateCustomer(ArrayList<Customer> customerList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(CustomerHelper.ST_IN_ALL);

			updateStmt = conn.prepareStatement(CustomerHelper.ST_UP_ALL);

			selectStmt = conn.prepareStatement(CustomerHelper.ST_SELECT_LAST_INSERT_ID);

			for (Customer customer : customerList) {

				if ((customer.getRecordMode() != null && customer.getRecordMode().equals(RecordMode.ISNEW))
						|| customer.getCodCustomer() == null || customer.getCodCustomer().equals(0)) {

					prepareInsert(insertStmt, customer);

					insertStmt.executeUpdate();

					resultSet = selectStmt.executeQuery();

					if (resultSet.next())
						customer.setCodCustomer(resultSet.getLong(CustomerHelper.LAST_INSERT_ID));

				} else {

					updateStmt.setString(1, customer.getPhone());
					updateStmt.setString(2, customer.getPassword());
					updateStmt.setString(3, customer.getName());
					// updateStmt.setByte(4, customer.getCodGender());
					if (customer.getCodGender() != null)
						updateStmt.setByte(4, customer.getCodGender());
					else
						updateStmt.setNull(4, Types.NULL);
					updateStmt.setString(5, customer.getCpf());
					updateStmt.setDate(6, Date.valueOf(customer.getBornDate()));
					updateStmt.setString(7, customer.getEmail());
					updateStmt.setString(8, customer.getAddress1());
					updateStmt.setString(9, customer.getAddress2());
					// updateStmt.setInt(10, customer.getPostalcode());
					if (customer.getPostalcode() != null)
						updateStmt.setInt(10, customer.getPostalcode());
					else
						updateStmt.setNull(10, Types.NULL);
					updateStmt.setString(11, customer.getCity());
					updateStmt.setString(12, customer.getCountry());
					updateStmt.setString(13, customer.getState());
					updateStmt.setString(14, customer.getCodPayment());

					updateStmt.setLong(15, customer.getCodCustomer());

					updateStmt.addBatch();

				}
			}

			updateStmt.executeBatch();

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
			closeConnection(conn, insertStmt, updateStmt, selectStmt, resultSet);
		}
	}

	public SQLException changePassword(Long codCustomer, String newPassword) {

		Connection conn = null;
		PreparedStatement updateStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			updateStmt = conn.prepareStatement(CustomerHelper.ST_UP_PASS);

			updateStmt.setString(1, newPassword);

			updateStmt.setLong(2, codCustomer);

			updateStmt.execute();

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
			closeConnection(conn, updateStmt);
		}
	}

	public SQLException deleteCustomer(List<Long> codCustomer, List<String> phone, List<String> password,
			List<String> name, List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new CustomerHelper().prepareDelete(codCustomer, phone, password, name,
					codGender, cpf, bornDate, email, address1, address2, postalcode, city, country, state));

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

	public ArrayList<Customer> selectCustomer(List<Long> codCustomer, List<String> phone, List<String> password,
			List<String> name, List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state) throws SQLException {

		ArrayList<Customer> customerList = new ArrayList<Customer>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			CustomerHelper customerHelper = new CustomerHelper();

			String select = customerHelper.prepareSelect(codCustomer, phone, password, name, codGender, cpf, bornDate,
					email, address1, address2, postalcode, city, country, state);

			// email.clear();
			// email.add(select);

			selectStmt = conn.prepareStatement(select);

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				customerList.add(customerHelper.assignResult(resultSet));
			}

			return customerList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt, Customer customer) throws SQLException {

		insertStmt.setString(1, customer.getPhone());
		insertStmt.setString(2, customer.getPassword());
		insertStmt.setString(3, customer.getName());
		if (customer.getCodGender() != null)
			insertStmt.setByte(4, customer.getCodGender());
		else
			insertStmt.setNull(4, Types.NULL);
		insertStmt.setString(5, customer.getCpf());
		if (customer.getBornDate() != null)
			insertStmt.setDate(6, Date.valueOf(customer.getBornDate()));
		else
			insertStmt.setNull(6, Types.NULL);
		insertStmt.setString(7, customer.getEmail());
		insertStmt.setString(8, customer.getAddress1());
		insertStmt.setString(9, customer.getAddress2());
		if (customer.getPostalcode() != null)
			insertStmt.setInt(10, customer.getPostalcode());
		else
			insertStmt.setNull(10, Types.NULL);
		insertStmt.setString(11, customer.getCity());
		insertStmt.setString(12, customer.getCountry());
		insertStmt.setString(13, customer.getState());

		// insertStmt.addBatch();
	}

}
