package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.DetailMatItemTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.DetailMatItemText;
import br.com.mind5.helper.RecordMode;

public class DetailMatItemTextDAO extends ConnectionBD {

	public SQLException insertDetailMatItemText(
			ArrayList<DetailMatItemText> detailMatItemTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(DetailMatItemTextHelper.ST_IN_ALL_FIELD);

			for (DetailMatItemText detailMatItemText : detailMatItemTextList) {

				prepareInsert(insertStmt, detailMatItemText);
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

	public SQLException updateDetailMatItemText(
			ArrayList<DetailMatItemText> detailMatItemTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(DetailMatItemTextHelper.ST_IN_ALL_FIELD);

			updateStmt = conn
					.prepareStatement(DetailMatItemTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			for (DetailMatItemText detailMatItemText : detailMatItemTextList) {

				if ((detailMatItemText.getRecordMode() != null && detailMatItemText
						.getRecordMode().equals(RecordMode.ISNEW))) {

					prepareInsert(insertStmt, detailMatItemText);

				} else {

					updateStmt.setString(1, detailMatItemText.getName());
					updateStmt.setString(2, detailMatItemText.getDescription());
					updateStmt.setString(3, detailMatItemText.getTextLong());

					updateStmt.setLong(4, detailMatItemText.getCodOwner());
					updateStmt.setInt(5, detailMatItemText.getCodDetail());
					updateStmt.setInt(6, detailMatItemText.getCodItem());
					updateStmt.setString(7, detailMatItemText.getLanguage());

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

	public SQLException deleteDetailMatItemText(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new DetailMatItemTextHelper()
					.prepareDelete(codOwner, codDetail, codItem, language,
							name, description, textLong));

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

	public ArrayList<DetailMatItemText> selectDetailMatItemText(
			List<Long> codOwner, List<Integer> codDetail,
			List<Integer> codItem, List<String> language, List<String> name,
			List<String> description, List<String> textLong)
			throws SQLException {

		ArrayList<DetailMatItemText> detailMatItemTextList = new ArrayList<DetailMatItemText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			DetailMatItemTextHelper detailMatItemTextHelper = new DetailMatItemTextHelper();

			selectStmt = conn.prepareStatement(detailMatItemTextHelper
					.prepareSelect(codOwner, codDetail, codItem, language,
							name, description, textLong));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				detailMatItemTextList.add(detailMatItemTextHelper
						.assignResult(resultSet));
			}

			return detailMatItemTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt,
			DetailMatItemText detailMatItemText) throws SQLException {

		insertStmt.setLong(1, detailMatItemText.getCodOwner());
		insertStmt.setInt(2, detailMatItemText.getCodDetail());
		insertStmt.setInt(3, detailMatItemText.getCodItem());
		insertStmt.setString(4, detailMatItemText.getLanguage());
		insertStmt.setString(5, detailMatItemText.getName());
		insertStmt.setString(6, detailMatItemText.getDescription());
		insertStmt.setString(7, detailMatItemText.getTextLong());

		insertStmt.addBatch();
	}

}
