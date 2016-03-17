package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.DetailMatItemHelper;
import br.com.gda.dao.helper.DetailMatItemTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.DetailMatItem;
import br.com.gda.helper.DetailMatItemText;
import br.com.gda.helper.RecordMode;

public class DetailMatItemDAO extends ConnectionBD {

	public SQLException insertDetailMatItem(
			ArrayList<DetailMatItem> detailMatItemList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn
					.prepareStatement(DetailMatItemHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn
					.prepareStatement(DetailMatItemTextHelper.ST_IN_ALL_FIELD);

			for (DetailMatItem detailMatItem : detailMatItemList) {

				prepareDetailMatItemInsert(insertStmtT01, detailMatItem);

				for (DetailMatItemText detailMatItemText : detailMatItem
						.getDetailItemText()) {

					prepareDetailMatItemTextInsert(insertStmtT02,
							detailMatItem, detailMatItemText);
				}

				insertStmtT02.executeBatch();

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
			closeConnection(conn, insertStmtT01, insertStmtT02);
		}

	}

	public SQLException updateDetailMatItem(
			ArrayList<DetailMatItem> detailMatItemList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement updateStmtT02 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn
					.prepareStatement(DetailMatItemHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn
					.prepareStatement(DetailMatItemTextHelper.ST_IN_ALL_FIELD);

			updateStmtT01 = conn
					.prepareStatement(DetailMatItemHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT02 = conn
					.prepareStatement(DetailMatItemTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			for (DetailMatItem detailMatItem : detailMatItemList) {

				if (detailMatItem.getRecordMode() != null
						&& detailMatItem.getRecordMode().equals(
								RecordMode.ISNEW)
						|| detailMatItem.getCodItem() == null
						|| detailMatItem.getCodItem().equals(0)) {

					prepareDetailMatItemInsert(insertStmtT01, detailMatItem);

				} else if (detailMatItem.getRecordMode() != null
						&& (detailMatItem.getRecordMode().equals(
								RecordMode.ISDELETED) || detailMatItem
								.getRecordMode().equals(
										RecordMode.RECORD_DELETED))) {

					updateStmtT01.setString(1, RecordMode.RECORD_DELETED);
					updateStmtT01.setLong(2, detailMatItem.getCodOwner());
					updateStmtT01.setInt(3, detailMatItem.getCodDetail());
					updateStmtT01.setInt(4, detailMatItem.getCodItem());

					updateStmtT01.addBatch();

				}

				for (DetailMatItemText detailMatItemText : detailMatItem
						.getDetailItemText()) {

					if ((detailMatItem.getRecordMode() != null && detailMatItem
							.getRecordMode().equals(RecordMode.ISNEW))
							|| (detailMatItemText.getRecordMode() != null && detailMatItemText
									.getRecordMode().equals(RecordMode.ISNEW))
							|| detailMatItem.getCodItem() == null
							|| detailMatItem.getCodItem().equals(0)) {

						prepareDetailMatItemTextInsert(insertStmtT02,
								detailMatItem, detailMatItemText);

					} else {

						updateStmtT02.setString(1, detailMatItemText.getName());
						updateStmtT02.setString(2,
								detailMatItemText.getDescription());
						updateStmtT02.setString(3,
								detailMatItemText.getTextLong());

						updateStmtT02.setLong(4, detailMatItem.getCodOwner());
						updateStmtT02.setInt(5, detailMatItem.getCodDetail());
						updateStmtT02.setInt(6, detailMatItem.getCodItem());
						updateStmtT02.setString(7,
								detailMatItemText.getLanguage());

						updateStmtT02.addBatch();

					}
				}

				insertStmtT02.executeBatch();
			}

			updateStmtT01.executeBatch();

			updateStmtT02.executeBatch();

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
			closeConnection(conn, insertStmtT01, insertStmtT02, updateStmtT01,
					updateStmtT02);
		}
	}

	public SQLException deleteDetailMatItem(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new DetailMatItemHelper()
					.prepareDelete(codOwner, codDetail, codItem, recordMode));

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

	public ArrayList<DetailMatItem> selectDetailMatItem(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> recordMode, List<String> language, List<String> name,
			List<String> description, List<String> textLong)
			throws SQLException {

		ArrayList<DetailMatItem> detailMatItemList = new ArrayList<DetailMatItem>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			DetailMatItemHelper detailMatItemHelper = new DetailMatItemHelper();

			selectStmt = conn.prepareStatement(detailMatItemHelper
					.prepareSelect(codOwner, codDetail, codItem, recordMode,
							language, name, description, textLong));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				detailMatItemHelper.assignResult(detailMatItemList, resultSet);
			}

			return detailMatItemList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareDetailMatItemInsert(PreparedStatement insertStmtT01,
			DetailMatItem detailMatItem) throws SQLException {

		detailMatItem.setCodItem(0);

		insertStmtT01.setLong(1, detailMatItem.getCodOwner());
		insertStmtT01.setInt(2, detailMatItem.getCodDetail());
		insertStmtT01.setString(3, RecordMode.RECORD_OK);
		if (detailMatItem.getDetailItemText() == null
				|| detailMatItem.getDetailItemText().size() == 0)
			throw new SQLException();

		insertStmtT01.executeUpdate();
	}

	private void prepareDetailMatItemTextInsert(
			PreparedStatement insertStmtT02, DetailMatItem detailMatItem,
			DetailMatItemText detailMatItemText) throws SQLException {

		insertStmtT02.setLong(1, detailMatItem.getCodOwner());
		insertStmtT02.setInt(2, detailMatItem.getCodDetail());
		insertStmtT02.setInt(3, detailMatItem.getCodItem());
		insertStmtT02.setString(4, detailMatItemText.getLanguage());
		insertStmtT02.setString(5, detailMatItemText.getName());
		insertStmtT02.setString(6, detailMatItemText.getDescription());
		insertStmtT02.setString(7, detailMatItemText.getTextLong());

		insertStmtT02.addBatch();
	}

}
