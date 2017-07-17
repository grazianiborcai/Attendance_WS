package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.DetailMatTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.DetailMatText;
import br.com.mind5.helper.RecordMode;

public class DetailMatTextDAO extends ConnectionBD {

	public SQLException insertDetailMatText(
			ArrayList<DetailMatText> detailMatTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(DetailMatTextHelper.ST_IN_ALL_FIELD);

			for (DetailMatText detailMatText : detailMatTextList) {

				prepareInsert(insertStmt, detailMatText);
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

	public SQLException updateDetailMatText(
			ArrayList<DetailMatText> detailMatTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(DetailMatTextHelper.ST_IN_ALL_FIELD);

			updateStmt = conn
					.prepareStatement(DetailMatTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			for (DetailMatText detailMatText : detailMatTextList) {

				if ((detailMatText.getRecordMode() != null && detailMatText
						.getRecordMode().equals(RecordMode.ISNEW))) {

					prepareInsert(insertStmt, detailMatText);

				} else {

					updateStmt.setString(1, detailMatText.getName());

					updateStmt.setLong(2, detailMatText.getCodOwner());
					updateStmt.setInt(3, detailMatText.getCodDetail());
					updateStmt.setString(4, detailMatText.getLanguage());

					updateStmt.addBatch();

				}
			}

			insertStmt.executeBatch();

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
			closeConnection(conn, insertStmt, updateStmt);
		}
	}

	public SQLException deleteDetailMatText(List<Long> codOwner,
			List<Integer> codDetail, List<String> language, List<String> name) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new DetailMatTextHelper()
					.prepareDelete(codOwner, codDetail, language, name));

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

	public ArrayList<DetailMatText> selectDetailMatText(List<Long> codOwner,
			List<Integer> codDetail, List<String> language, List<String> name)
			throws SQLException {

		ArrayList<DetailMatText> detailMatTextList = new ArrayList<DetailMatText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			DetailMatTextHelper detailMatTextHelper = new DetailMatTextHelper();

			selectStmt = conn.prepareStatement(detailMatTextHelper
					.prepareSelect(codOwner, codDetail, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {
				detailMatTextList.add(detailMatTextHelper
						.assignResult(resultSet));
			}

			return detailMatTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt,
			DetailMatText detailMatText) throws SQLException {
		
		insertStmt.setLong(1, detailMatText.getCodOwner());
		insertStmt.setInt(2, detailMatText.getCodDetail());
		insertStmt.setString(3, detailMatText.getLanguage());
		insertStmt.setString(4, detailMatText.getName());

		insertStmt.addBatch();
	}

}
