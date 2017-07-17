package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.gda.dao.helper.DetailMatHelper;
import br.com.gda.dao.helper.DetailMatItemHelper;
import br.com.gda.dao.helper.DetailMatItemTextHelper;
import br.com.gda.dao.helper.DetailMatTextHelper;
import br.com.gda.dao.helper.MaterialDetailHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.DetailMat;
import br.com.mind5.helper.DetailMatItem;
import br.com.mind5.helper.DetailMatItemText;
import br.com.mind5.helper.DetailMatText;
import br.com.mind5.helper.RecordMode;

public class DetailMatDAO extends ConnectionBD {

	public SQLException insertDetailMat(ArrayList<DetailMat> detailMatList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;
		PreparedStatement insertStmtT03 = null;
		PreparedStatement insertStmtT04 = null;
		PreparedStatement variableStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn
					.prepareStatement(DetailMatHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn
					.prepareStatement(DetailMatTextHelper.ST_IN_ALL_FIELD);

			insertStmtT03 = conn
					.prepareStatement(DetailMatItemHelper.ST_IN_ALL_FIELD);

			insertStmtT04 = conn
					.prepareStatement(DetailMatItemTextHelper.ST_IN_ALL_FIELD);

			variableStmt = conn.prepareStatement(DetailMatHelper.VARIABLE);

			for (DetailMat detailMat : detailMatList) {

				prepareDetailMatInsert(insertStmtT01, variableStmt, detailMat);

				for (DetailMatText detailMatText : detailMat.getDetailText()) {

					prepareDetailMatTextInsert(insertStmtT02, detailMat,
							detailMatText);
				}

				insertStmtT02.executeBatch();

				for (DetailMatItem detailMatItem : detailMat.getDetailItem()) {

					prepareDetailMatItemInsert(insertStmtT03, detailMat,
							detailMatItem);

					for (DetailMatItemText detailMatItemText : detailMatItem
							.getDetailItemText()) {

						prepareDetailMatItemTextInsert(insertStmtT04,
								detailMat, detailMatItem, detailMatItemText);
					}

					insertStmtT04.executeBatch();
				}
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
			closeConnection(conn, insertStmtT01, insertStmtT02, insertStmtT03,
					insertStmtT04, variableStmt);
		}

	}

	@SuppressWarnings("resource")
	public SQLException updateDetailMat(ArrayList<DetailMat> detailMatList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;
		PreparedStatement insertStmtT03 = null;
		PreparedStatement insertStmtT04 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement updateStmtT03 = null;
		PreparedStatement updateStmtT02 = null;
		PreparedStatement updateStmtT04 = null;
		PreparedStatement updateStmtT05 = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		PreparedStatement variableStmt = null;

		List<DetailMat> materialDetailList = new ArrayList<DetailMat>();

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn
					.prepareStatement(DetailMatHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn
					.prepareStatement(DetailMatTextHelper.ST_IN_ALL_FIELD);

			insertStmtT03 = conn
					.prepareStatement(DetailMatItemHelper.ST_IN_ALL_FIELD);

			insertStmtT04 = conn
					.prepareStatement(DetailMatItemTextHelper.ST_IN_ALL_FIELD);

			variableStmt = conn.prepareStatement(DetailMatHelper.VARIABLE);

			updateStmtT01 = conn
					.prepareStatement(DetailMatHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT02 = conn
					.prepareStatement(DetailMatTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT03 = conn
					.prepareStatement(DetailMatItemHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT04 = conn
					.prepareStatement(DetailMatItemTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT05 = conn
					.prepareStatement(DetailMatItemHelper.ST_UP_ALL_FIELD_BY_DETAIL);

			for (DetailMat detailMat : detailMatList) {

				if ((detailMat.getRecordMode() != null && detailMat
						.getRecordMode().equals(RecordMode.ISNEW))
						|| detailMat.getCodDetail() == null
						|| detailMat.getCodDetail().equals(0)) {

					prepareDetailMatInsert(insertStmtT01, variableStmt,
							detailMat);

				} else {

					updateStmtT01.setFloat(1, detailMat.getQuantity());
					if (detailMat.getRecordMode() != null
							&& (detailMat.getRecordMode().equals(
									RecordMode.ISDELETED) || detailMat
									.getRecordMode().equals(
											RecordMode.RECORD_DELETED))) {
						updateStmtT01.setString(2, RecordMode.RECORD_DELETED);

						updateStmtT05.setString(1, RecordMode.RECORD_DELETED);
						updateStmtT05.setLong(2, detailMat.getCodOwner());
						updateStmtT05.setInt(3, detailMat.getCodDetail());
						updateStmtT05.addBatch();

						materialDetailList.add(detailMat);

					} else {
						updateStmtT01.setString(2, RecordMode.RECORD_OK);
					}
					updateStmtT01.setLong(3, detailMat.getCodOwner());
					updateStmtT01.setInt(4, detailMat.getCodDetail());

					updateStmtT01.addBatch();

				}

				for (DetailMatText detailMatText : detailMat.getDetailText()) {

					if ((detailMat.getRecordMode() != null && detailMat
							.getRecordMode().equals(RecordMode.ISNEW))
							|| (detailMatText.getRecordMode() != null && detailMatText
									.getRecordMode().equals(RecordMode.ISNEW))
							|| detailMat.getCodDetail() == null
							|| detailMat.getCodDetail().equals(0)) {

						prepareDetailMatTextInsert(insertStmtT02, detailMat,
								detailMatText);

					} else {

						updateStmtT02.setString(1, detailMatText.getName());
						updateStmtT02.setLong(2, detailMat.getCodOwner());
						updateStmtT02.setInt(3, detailMat.getCodDetail());
						updateStmtT02.setString(4, detailMatText.getLanguage());

						updateStmtT02.addBatch();
					}

				}

				insertStmtT02.executeBatch();

				for (DetailMatItem detailMatItem : detailMat.getDetailItem()) {

					if ((detailMat.getRecordMode() != null
							&& !detailMat.getRecordMode().equals(
									RecordMode.ISDELETED) && !detailMat
							.getRecordMode().equals(RecordMode.RECORD_DELETED))
							|| detailMat.getRecordMode() == null) {

						if ((detailMat.getRecordMode() != null && detailMat
								.getRecordMode().equals(RecordMode.ISNEW))
								|| (detailMatItem.getRecordMode() != null && detailMatItem
										.getRecordMode().equals(
												RecordMode.ISNEW))
								|| detailMat.getCodDetail() == null
								|| detailMat.getCodDetail().equals(0)
								|| detailMatItem.getCodItem() == null
								|| detailMatItem.getCodItem().equals(0)) {

							prepareDetailMatItemInsert(insertStmtT03,
									detailMat, detailMatItem);

						} else {

							if (detailMatItem.getRecordMode() != null
									&& (detailMatItem.getRecordMode().equals(
											RecordMode.ISDELETED) || detailMatItem
											.getRecordMode().equals(
													RecordMode.RECORD_DELETED))) {
								updateStmtT03.setString(1,
										RecordMode.RECORD_DELETED);
							} else {
								updateStmtT03
										.setString(1, RecordMode.RECORD_OK);
							}
							updateStmtT03.setLong(2, detailMat.getCodOwner());
							updateStmtT03.setInt(3, detailMat.getCodDetail());
							updateStmtT03.setInt(4, detailMatItem.getCodItem());
							updateStmtT03.addBatch();

						}

						for (DetailMatItemText detailMatItemText : detailMatItem
								.getDetailItemText()) {

							if ((detailMat.getRecordMode() != null && detailMat
									.getRecordMode().equals(RecordMode.ISNEW))
									|| (detailMatItem.getRecordMode() != null && detailMatItem
											.getRecordMode().equals(
													RecordMode.ISNEW))
									|| (detailMatItemText.getRecordMode() != null && detailMatItemText
											.getRecordMode().equals(
													RecordMode.ISNEW))
									|| detailMat.getCodDetail() == null
									|| detailMat.getCodDetail().equals(0)
									|| detailMatItem.getCodItem() == null
									|| detailMatItem.getCodItem().equals(0)) {

								prepareDetailMatItemTextInsert(insertStmtT04,
										detailMat, detailMatItem,
										detailMatItemText);

							} else {

								updateStmtT04.setString(1,
										detailMatItemText.getName());
								updateStmtT04.setString(2,
										detailMatItemText.getDescription());
								updateStmtT04.setString(3,
										detailMatItemText.getTextLong());

								updateStmtT04.setLong(4,
										detailMat.getCodOwner());
								updateStmtT04.setInt(5,
										detailMat.getCodDetail());
								updateStmtT04.setLong(6,
										detailMatItem.getCodItem());
								updateStmtT04.setString(7,
										detailMatItemText.getLanguage());

								updateStmtT04.addBatch();

							}
						}

						insertStmtT04.executeBatch();
					}
				}
			}

			if (materialDetailList != null && !materialDetailList.isEmpty()) {

				List<Long> codOwner = materialDetailList.stream()
						.map(m -> m.getCodOwner()).distinct()
						.collect(Collectors.toList());

				MaterialDetailHelper materialDetailHelper = new MaterialDetailHelper();
				for (int j = 0; j < codOwner.size(); j++) {
					Long owner = codOwner.get(j);

					List<Integer> codDetail = materialDetailList.stream()
							.filter(m -> m.getCodOwner().equals(owner))
							.map(m -> m.getCodDetail())
							.collect(Collectors.toList());

					selectStmt = conn.prepareStatement(materialDetailHelper
							.prepareSelectToMaterialOrDetail(owner, null,
									codDetail));

					resultSet = selectStmt.executeQuery();

					if (resultSet.next())
						throw new SQLException();
				}
			}

			updateStmtT01.executeBatch();

			updateStmtT05.executeBatch();

			updateStmtT02.executeBatch();

			updateStmtT03.executeBatch();

			updateStmtT04.executeBatch();

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
			closeConnection(conn, selectStmt, insertStmtT01, insertStmtT02,
					insertStmtT03, insertStmtT04, updateStmtT01, updateStmtT05,
					updateStmtT02, updateStmtT03, updateStmtT04, variableStmt,
					resultSet);
		}
	}

	public SQLException deleteDetailMat(List<Long> codOwner,
			List<Integer> codDetail, List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new DetailMatHelper()
					.prepareDelete(codOwner, codDetail, recordMode));

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

	public ArrayList<DetailMat> selectDetailMat(List<Long> codOwner,
			List<Integer> codDetail, List<String> recordMode,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<DetailMat> detailMatList = new ArrayList<DetailMat>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			DetailMatHelper detailMatHelper = new DetailMatHelper();

			selectStmt = conn.prepareStatement(detailMatHelper.prepareSelect(
					codOwner, codDetail, recordMode, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				detailMatHelper.assignResult(detailMatList, resultSet);
			}

			return detailMatList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareDetailMatInsert(PreparedStatement insertStmtT01,
			PreparedStatement variableStmt, DetailMat detailMat)
			throws SQLException {

		insertStmtT01.setLong(1, detailMat.getCodOwner());
		insertStmtT01.setFloat(2, detailMat.getQuantity());
		insertStmtT01.setString(3, RecordMode.RECORD_OK);
		if (detailMat.getDetailText() == null
				|| detailMat.getDetailText().size() == 0
				|| detailMat.getDetailItem() == null
				|| detailMat.getDetailItem().size() == 0)
			throw new SQLException();

		insertStmtT01.executeUpdate();
		variableStmt.execute();
		detailMat.setCodDetail(0);
	}

	private void prepareDetailMatTextInsert(PreparedStatement insertStmtT02,
			DetailMat detailMat, DetailMatText detailMatText)
			throws SQLException {

		insertStmtT02.setLong(1, detailMat.getCodOwner());
		insertStmtT02.setInt(2, detailMat.getCodDetail());
		insertStmtT02.setString(3, detailMatText.getLanguage());
		insertStmtT02.setString(4, detailMatText.getName());

		insertStmtT02.addBatch();
	}

	private void prepareDetailMatItemInsert(PreparedStatement insertStmtT03,
			DetailMat detailMat, DetailMatItem detailMatItem)
			throws SQLException {

		insertStmtT03.setLong(1, detailMat.getCodOwner());
		insertStmtT03.setInt(2, detailMat.getCodDetail());
		insertStmtT03.setString(3, RecordMode.RECORD_OK);
		if (detailMatItem.getDetailItemText() == null
				|| detailMatItem.getDetailItemText().size() == 0)
			throw new SQLException();

		insertStmtT03.executeUpdate();
		detailMatItem.setCodItem(0);
	}

	private void prepareDetailMatItemTextInsert(
			PreparedStatement insertStmtT04, DetailMat detailMat,
			DetailMatItem detailMatItem, DetailMatItemText detailMatItemText)
			throws SQLException {

		insertStmtT04.setLong(1, detailMat.getCodOwner());
		insertStmtT04.setInt(2, detailMat.getCodDetail());
		insertStmtT04.setLong(3, detailMatItem.getCodItem());
		insertStmtT04.setString(4, detailMatItemText.getLanguage());
		insertStmtT04.setString(5, detailMatItemText.getName());
		insertStmtT04.setString(6, detailMatItemText.getDescription());
		insertStmtT04.setString(7, detailMatItemText.getTextLong());

		insertStmtT04.addBatch();
	}

}
