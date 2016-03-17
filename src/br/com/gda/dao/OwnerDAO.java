package br.com.gda.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.OwnerHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.Owner;
import br.com.gda.helper.RecordMode;

public class OwnerDAO extends ConnectionBD {

	public SQLException insertOwner(ArrayList<Owner> ownerList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(OwnerHelper.ST_IN_ALL_FIELD);

			selectStmt = conn.prepareStatement(OwnerHelper.ST_SELECT_LAST_INSERT_ID);

			for (Owner owner : ownerList) {

				prepareInsert(insertStmt, owner);

				insertStmt.executeUpdate();

				resultSet = selectStmt.executeQuery();

				if (resultSet.next())
					owner.setCodOwner(resultSet.getLong(OwnerHelper.LAST_INSERT_ID));
			}

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
			closeConnection(conn, insertStmt, selectStmt, resultSet);
		}

	}

	public SQLException updateOwner(ArrayList<Owner> ownerList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(OwnerHelper.ST_IN_ALL_FIELD);

			updateStmt = conn.prepareStatement(OwnerHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);
			
			selectStmt = conn.prepareStatement(OwnerHelper.ST_SELECT_LAST_INSERT_ID);

			for (Owner owner : ownerList) {

				if ((owner.getRecordMode() != null && owner.getRecordMode().equals(RecordMode.ISNEW))
						|| owner.getCodOwner() == null || owner.getCodOwner().equals(0)) {

					prepareInsert(insertStmt, owner);
					
					insertStmt.executeUpdate();

					resultSet = selectStmt.executeQuery();

					if (resultSet.next())
						owner.setCodOwner(resultSet.getLong(OwnerHelper.LAST_INSERT_ID));

				} else {

					updateStmt.setString(1, owner.getPassword());
					updateStmt.setString(2, owner.getName());
					updateStmt.setString(3, owner.getCpf());
					updateStmt.setString(4, owner.getPhone());
					updateStmt.setString(5, owner.getEmail());
					updateStmt.setString(6, owner.getEmailAux());
					updateStmt.setByte(7, owner.getCodGender());
					updateStmt.setDate(8, Date.valueOf(owner.getBornDate()));
					updateStmt.setInt(9, owner.getPostalcode());
					updateStmt.setString(10, owner.getAddress1());
					updateStmt.setString(11, owner.getAddress2());
					updateStmt.setString(12, owner.getCity());
					updateStmt.setString(13, owner.getCountry());
					updateStmt.setString(14, owner.getState());
					updateStmt.setString(15, owner.getCodCurr());
					if (owner.getRecordMode() != null && (owner.getRecordMode().equals(RecordMode.ISDELETED)
							|| owner.getRecordMode().equals(RecordMode.RECORD_DELETED))) {
						updateStmt.setString(16, RecordMode.RECORD_DELETED);
					} else {
						updateStmt.setString(16, RecordMode.RECORD_OK);
					}
					updateStmt.setLong(17, owner.getCodOwner());

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

	public SQLException deleteOwner(List<Long> codOwner, List<String> password, List<String> name, List<String> cpf,
			List<String> phone, List<String> email, List<String> emailAux, List<Byte> codGender, List<String> bornDate,
			List<Integer> postalcode, List<String> address1, List<String> address2, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(
					new OwnerHelper().prepareDelete(codOwner, password, name, cpf, phone, email, emailAux, codGender,
							bornDate, postalcode, address1, address2, city, country, state, codCurr, recordMode));

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

	public ArrayList<Owner> selectOwner(String email, String cpf, String password, List<String> recordMode)
			throws SQLException {

		ArrayList<Owner> ownerList = new ArrayList<Owner>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			OwnerHelper ownerHelper = new OwnerHelper();

			selectStmt = conn.prepareStatement(ownerHelper.prepareSelect(email, cpf, password, recordMode));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				ownerList.add(ownerHelper.assignResult(resultSet));
			}

			return ownerList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt, Owner owner) throws SQLException {

		insertStmt.setString(1, owner.getPassword());
		insertStmt.setString(2, owner.getName());
		insertStmt.setString(3, owner.getCpf());
		insertStmt.setString(4, owner.getPhone());
		insertStmt.setString(5, owner.getEmail());
		insertStmt.setString(6, owner.getEmailAux());
		insertStmt.setByte(7, owner.getCodGender());
		insertStmt.setDate(8, Date.valueOf(owner.getBornDate()));
		insertStmt.setInt(9, owner.getPostalcode());
		insertStmt.setString(10, owner.getAddress1());
		insertStmt.setString(11, owner.getAddress2());
		insertStmt.setString(12, owner.getCity());
		insertStmt.setString(13, owner.getCountry());
		insertStmt.setString(14, owner.getState());
		insertStmt.setString(15, owner.getCodCurr());
		insertStmt.setString(16, RecordMode.RECORD_OK);

		insertStmt.addBatch();
	}

}
