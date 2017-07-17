package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.gda.dao.helper.MaterialDetailHelper;
import br.com.gda.dao.helper.MaterialHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.MaterialDetail;
import br.com.mind5.helper.RecordMode;

public class MaterialDetailDAO extends ConnectionBD {

	@SuppressWarnings("resource")
	public SQLException insertMaterialDetail(
			ArrayList<MaterialDetail> materialDetailList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			// Check if the Material has the right type to match with Detail
			List<Long> codOwner = materialDetailList.stream()
					.map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			for (int j = 0; j < codOwner.size(); j++) {
				Long owner = codOwner.get(j);

				List<Integer> codMaterial = materialDetailList.stream()
						.filter(m -> m.getCodOwner().equals(owner))
						.map(m -> m.getCodMaterial())
						.collect(Collectors.toList());

				selectStmt = conn.prepareStatement(new MaterialHelper()
						.prepareSelectToMaterialDetail(owner, codMaterial));

				resultSet = selectStmt.executeQuery();

				if (resultSet.next())
					throw new SQLException();
			}

			insertStmtT01 = conn
					.prepareStatement(MaterialDetailHelper.ST_IN_ALL_FIELD);

			for (MaterialDetail materialDetail : materialDetailList) {

				prepareInsert(insertStmtT01, materialDetail);
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
			closeConnection(conn, insertStmtT01, selectStmt, resultSet);
		}

	}

	@SuppressWarnings("resource")
	public SQLException updateMaterialDetail(
			ArrayList<MaterialDetail> materialDetailList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement deleteStmtT01 = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		List<MaterialDetail> insertMaterialList = new ArrayList<MaterialDetail>();
		List<MaterialDetail> deleteMaterialList = new ArrayList<MaterialDetail>();

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn
					.prepareStatement(MaterialDetailHelper.ST_IN_ALL_FIELD);

			updateStmtT01 = conn
					.prepareStatement(MaterialDetailHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			MaterialDetailHelper materialDetailHelper = new MaterialDetailHelper();
			for (MaterialDetail materialDetail : materialDetailList) {

				if (materialDetail.getRecordMode() != null
						&& materialDetail.getRecordMode().equals(
								RecordMode.ISNEW)) {

					prepareInsert(insertStmtT01, materialDetail);

					insertMaterialList.add(materialDetail);

				} else {

					if (materialDetail.getRecordMode() != null
							&& (materialDetail.getRecordMode().equals(
									RecordMode.ISDELETED) || materialDetail
									.getRecordMode().equals(
											RecordMode.RECORD_DELETED))) {

						deleteStmtT01 = conn
								.prepareStatement(materialDetailHelper.prepareDelete(
										materialDetail.getCodOwner(),
										materialDetail.getCodMaterial(),
										materialDetail.getCodDetail()));

						deleteStmtT01.execute();

						deleteMaterialList.add(materialDetail);

					} else {

						updateStmtT01
								.setFloat(
										1,
										materialDetail.getQuantityMat() != 0 ? materialDetail
												.getQuantityMat()
												: materialDetail.getQuantity());
						updateStmtT01.setString(2, RecordMode.RECORD_OK);
						updateStmtT01.setLong(3, materialDetail.getCodOwner());
						updateStmtT01
								.setInt(4, materialDetail.getCodMaterial());
						updateStmtT01.setInt(5, materialDetail.getCodDetail());

						updateStmtT01.addBatch();
					}
				}
			}

			// Check if the Material has the right type to match with Detail
			List<Long> codOwner = insertMaterialList.stream()
					.map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			for (int j = 0; j < codOwner.size(); j++) {
				Long owner = codOwner.get(j);

				List<Integer> codMaterial = insertMaterialList.stream()
						.filter(m -> m.getCodOwner().equals(owner))
						.map(m -> m.getCodMaterial())
						.collect(Collectors.toList());

				selectStmt = conn.prepareStatement(new MaterialHelper()
						.prepareSelectToMaterialDetail(owner, codMaterial));

				resultSet = selectStmt.executeQuery();

				if (resultSet.next())
					throw new SQLException();
			}

			// Check if the Material is matched with at least one Detail
			codOwner = deleteMaterialList.stream().map(m -> m.getCodOwner())
					.distinct().collect(Collectors.toList());

			for (int j = 0; j < codOwner.size(); j++) {
				Long owner = codOwner.get(j);

				List<Integer> codMaterial = deleteMaterialList.stream()
						.filter(m -> m.getCodOwner().equals(owner))
						.map(m -> m.getCodMaterial())
						.collect(Collectors.toList());

				selectStmt = conn.prepareStatement(materialDetailHelper
						.prepareSelectToMaterialOrDetail(owner, codMaterial,
								null));

				resultSet = selectStmt.executeQuery();

				List<Integer> codMaterialCompare = new ArrayList<Integer>();

				while (resultSet.next()) {
					codMaterialCompare.add(new MaterialDetailHelper()
							.assignResult(resultSet).getCodMaterial());
				}

				codMaterialCompare = codMaterialCompare.stream().distinct()
						.collect(Collectors.toList());

				if (!codMaterial.equals(codMaterialCompare))
					throw new SQLException();
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
			closeConnection(conn, deleteStmtT01, insertStmtT01, updateStmtT01,
					selectStmt, resultSet);
		}
	}

	public SQLException deleteMaterialDetail(List<Long> codOwner,
			List<Integer> codMaterial, List<Integer> codDetail,
			List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn
					.prepareStatement(new MaterialDetailHelper().prepareDelete(
							codOwner, codMaterial, codDetail, recordMode));

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

	public ArrayList<MaterialDetail> selectMaterialDetail(List<Long> codOwner,
			List<Integer> codMaterial, List<Integer> codDetail,
			List<String> recordMode, List<String> language, List<String> name)
			throws SQLException {

		ArrayList<MaterialDetail> detailMatList = new ArrayList<MaterialDetail>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			MaterialDetailHelper materialDetailHelper = new MaterialDetailHelper();

			selectStmt = conn.prepareStatement(materialDetailHelper
					.prepareSelect(codOwner, codMaterial, codDetail,
							recordMode, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				materialDetailHelper.assignResultWithDetailAndText(
						detailMatList, resultSet);
			}

			return detailMatList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmtT01,
			MaterialDetail materialDetail) throws SQLException {

		insertStmtT01.setLong(1, materialDetail.getCodOwner());
		insertStmtT01.setInt(2, materialDetail.getCodMaterial());
		insertStmtT01.setInt(3, materialDetail.getCodDetail());
		insertStmtT01.setFloat(
				4,
				materialDetail.getQuantityMat() != 0 ? materialDetail
						.getQuantityMat() : materialDetail.getQuantity());
		insertStmtT01.setString(05, RecordMode.RECORD_OK);

		insertStmtT01.addBatch();
	}

}
