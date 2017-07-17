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
import br.com.gda.dao.helper.MaterialTextHelper;
import br.com.gda.dao.helper.MenuItemHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Material;
import br.com.mind5.helper.MaterialDetail;
import br.com.mind5.helper.MaterialText;
import br.com.mind5.helper.RecordMode;

public class MaterialDAO extends ConnectionBD {

	public SQLException insertMaterial(ArrayList<Material> materialList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;
		PreparedStatement insertStmtT03 = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(MaterialHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn.prepareStatement(MaterialTextHelper.ST_IN_ALL_FIELD);

			insertStmtT03 = conn.prepareStatement(MaterialDetailHelper.ST_IN_ALL_FIELD);

			selectStmt = conn.prepareStatement(MaterialHelper.ST_SELECT_LAST_INSERT_ID);

			for (Material material : materialList) {

				prepareMaterialInsert(insertStmtT01, material);

				for (MaterialText materialText : material.getMaterialText()) {

					prepareMaterialTextInsert(insertStmtT02, material, materialText);
				}

				for (MaterialDetail detailMat : material.getDetailMat()) {

					prepareMaterialDetailInsert(insertStmtT03, material, detailMat);
				}

				insertStmtT01.executeBatch();

				resultSet = selectStmt.executeQuery();

				if (resultSet.next())
					material.setCodMaterial(resultSet.getInt(MaterialHelper.LAST_INSERT_ID));

				insertStmtT02.executeBatch();

				insertStmtT03.executeBatch();
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
			closeConnection(conn, insertStmtT01, insertStmtT02, insertStmtT03, selectStmt, resultSet);
		}

	}

	@SuppressWarnings("resource")
	public SQLException updateMaterial(ArrayList<Material> materialList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;
		PreparedStatement insertStmtT03 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement updateStmtT02 = null;
		PreparedStatement updateStmtT03 = null;
		PreparedStatement updateStmtT04 = null;
		PreparedStatement updateStmtT05 = null;
		PreparedStatement updateStmtT06 = null;
		PreparedStatement deleteStmtT01 = null;
		PreparedStatement deleteStmtT02 = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		List<Material> deleteMaterialList = new ArrayList<Material>();

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(MaterialHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn.prepareStatement(MaterialTextHelper.ST_IN_ALL_FIELD);

			insertStmtT03 = conn.prepareStatement(MaterialDetailHelper.ST_IN_ALL_FIELD);

			updateStmtT01 = conn.prepareStatement(MaterialHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT02 = conn.prepareStatement(MaterialTextHelper.ST_UP_ALL_FIELD);

			updateStmtT03 = conn.prepareStatement(MaterialDetailHelper.ST_UP_ALL_FIELD_BY_MATERIAL);

			updateStmtT04 = conn.prepareStatement(MaterialDetailHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT06 = conn.prepareStatement(MenuItemHelper.ST_UP_RECORD_MODE_BY_MATERIAL);
			
			selectStmt = conn.prepareStatement(MaterialHelper.ST_SELECT_LAST_INSERT_ID);

			MaterialDetailHelper materialDetailHelper = new MaterialDetailHelper();
			for (Material material : materialList) {

				List<Integer> codDetail = new ArrayList<Integer>();

				if ((material.getRecordMode() != null && material.getRecordMode().equals(RecordMode.ISNEW))
						|| material.getCodMaterial() == null || material.getCodMaterial().equals(0)) {

					prepareMaterialInsert(insertStmtT01, material);

				} else {
					updateStmtT01.setBigDecimal(1, material.getPrice());
					updateStmtT01.setInt(2, material.getCodCategory());
					updateStmtT01.setInt(3, material.getCodType());
					updateStmtT01.setString(4, material.getImage());
					updateStmtT01.setInt(5, material.getDuration());
					updateStmtT01.setString(6, material.getBarCode());

					updateStmtT01.setString(8, material.getCodCurr());

					updateStmtT01.setLong(9, material.getCodOwner());
					updateStmtT01.setInt(10, material.getCodMaterial());

					if (material.getRecordMode() != null && (material.getRecordMode().equals(RecordMode.ISDELETED)
							|| material.getRecordMode().equals(RecordMode.RECORD_DELETED))) {
						updateStmtT01.setString(7, RecordMode.RECORD_DELETED);

						updateStmtT03.setString(1, RecordMode.RECORD_DELETED);
						updateStmtT03.setLong(2, material.getCodOwner());
						updateStmtT03.setInt(3, material.getCodMaterial());

						updateStmtT03.addBatch();

						updateStmtT06.setString(1, RecordMode.RECORD_DELETED);
						updateStmtT06.setLong(2, material.getCodOwner());
						updateStmtT06.setInt(3, material.getCodMaterial());

						updateStmtT06.addBatch();

					} else {
						updateStmtT01.setString(7, RecordMode.RECORD_OK);
					}

					updateStmtT01.addBatch();

					if (material.getCodType().equals(RecordMode._2)) {

						deleteStmtT02 = conn
								.prepareStatement(materialDetailHelper.prepareDeleteToMaterial(material, null));

						deleteStmtT02.execute();
					}
				}

				for (MaterialText materialText : material.getMaterialText()) {

					if ((material.getRecordMode() != null && material.getRecordMode().equals(RecordMode.ISNEW))
							|| (materialText.getRecordMode() != null
									&& materialText.getRecordMode().equals(RecordMode.ISNEW))
							|| material.getCodMaterial() == null || material.getCodMaterial().equals(0)) {

						prepareMaterialTextInsert(insertStmtT02, material, materialText);

					} else {

						updateStmtT02.setString(1, materialText.getName());
						updateStmtT02.setString(2, materialText.getDescription());
						updateStmtT02.setString(3, materialText.getTextLong());

						updateStmtT02.setLong(4, material.getCodOwner());
						updateStmtT02.setInt(5, material.getCodMaterial());
						updateStmtT02.setString(6, materialText.getLanguage());

						updateStmtT02.addBatch();
					}
				}

				for (MaterialDetail detailMat : material.getDetailMat()) {

					if ((detailMat.getRecordMode() != null && detailMat.getRecordMode().equals(RecordMode.ISNEW))
							|| material.getCodMaterial() == null || material.getCodMaterial().equals(0)) {

						prepareMaterialDetailInsert(insertStmtT03, material, detailMat);

					} else
						if ((material.getRecordMode() != null && !material.getRecordMode().equals(RecordMode.ISDELETED)
								&& !material.getRecordMode().equals(RecordMode.RECORD_DELETED))
								|| material.getRecordMode() == null) {

						if (detailMat.getRecordMode() != null && (detailMat.getRecordMode().equals(RecordMode.ISDELETED)
								|| detailMat.getRecordMode().equals(RecordMode.RECORD_DELETED))) {

							codDetail.add(detailMat.getCodDetail());
							if (material.getCodType().equals(RecordMode._1))
								deleteMaterialList.add(material);

						} else {

							updateStmtT04.setFloat(1, detailMat.getQuantityMat() != 0 ? detailMat.getQuantityMat()
									: detailMat.getQuantity());
							updateStmtT04.setString(2, RecordMode.RECORD_OK);
							updateStmtT04.setLong(3, material.getCodOwner());
							updateStmtT04.setInt(4, material.getCodMaterial());
							updateStmtT04.setInt(5, detailMat.getCodDetail());

							updateStmtT04.addBatch();
						}
					}
				}

				if (codDetail != null && !codDetail.isEmpty()) {

					deleteStmtT01 = conn
							.prepareStatement(materialDetailHelper.prepareDeleteToMaterial(material, codDetail));

					deleteStmtT01.execute();
				}

				insertStmtT01.executeBatch();
				
				resultSet = selectStmt.executeQuery();

				if (resultSet.next())
					material.setCodMaterial(resultSet.getInt(MaterialHelper.LAST_INSERT_ID));

				insertStmtT02.executeBatch();

				insertStmtT03.executeBatch();
			}
			
			closeConnection(selectStmt, resultSet);

			List<Long> codOwner = deleteMaterialList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			for (int j = 0; j < codOwner.size(); j++) {
				Long owner = codOwner.get(j);

				List<Integer> codMaterial = deleteMaterialList.stream().filter(m -> m.getCodOwner().equals(owner))
						.map(m -> m.getCodMaterial()).collect(Collectors.toList());

				selectStmt = conn.prepareStatement(
						materialDetailHelper.prepareSelectToMaterialOrDetail(owner, codMaterial, null));

				resultSet = selectStmt.executeQuery();

				List<Integer> codMaterialCompare = new ArrayList<Integer>();

				while (resultSet.next()) {
					codMaterialCompare.add(materialDetailHelper.assignResult(resultSet).getCodMaterial());
				}

				codMaterialCompare = codMaterialCompare.stream().distinct().collect(Collectors.toList());

				if (!codMaterial.equals(codMaterialCompare))
					throw new SQLException();
			}

			updateStmtT01.executeBatch();

			updateStmtT02.executeBatch();

			updateStmtT03.executeBatch();

			updateStmtT04.executeBatch();

			updateStmtT06.executeBatch();

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
			closeConnection(conn, deleteStmtT01, deleteStmtT02, insertStmtT01, insertStmtT02, insertStmtT03,
					updateStmtT01, updateStmtT02, updateStmtT03, updateStmtT04, updateStmtT05, updateStmtT06,
					selectStmt, resultSet);
		}
	}

	public SQLException deleteMaterial(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new MaterialHelper().prepareDelete(codOwner, codMaterial, codCategory,
					codType, image, barCode, recordMode));

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

	public ArrayList<Material> selectMaterial(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode,
			List<String> language, List<String> name, List<String> description, List<String> textLong)
					throws SQLException {

		ArrayList<Material> materialList = new ArrayList<Material>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			MaterialHelper materialHelper = new MaterialHelper();

			selectStmt = conn.prepareStatement(materialHelper.prepareSelect(codOwner, codMaterial, codCategory, codType,
					image, barCode, recordMode, language, name, description, textLong));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				materialHelper.assignResultWithText(materialList, resultSet);
			}

			return materialList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareMaterialInsert(PreparedStatement insertStmtT01, Material material) throws SQLException {

		material.setCodMaterial(Integer.valueOf(0));

		insertStmtT01.setLong(1, material.getCodOwner());
		insertStmtT01.setBigDecimal(2, material.getPrice());
		insertStmtT01.setInt(3, material.getCodCategory());
		insertStmtT01.setInt(4, material.getCodType());
		insertStmtT01.setString(5, material.getImage());
		insertStmtT01.setInt(6, material.getDuration());
		insertStmtT01.setString(7, material.getBarCode());
		insertStmtT01.setString(8, RecordMode.RECORD_OK);
		insertStmtT01.setString(9, material.getCodCurr());

		if (material.getMaterialText() == null || material.getMaterialText().size() == 0)
			throw new SQLException();
		if (material.getCodType().equals(RecordMode._2) && material.getDetailMat() != null
				&& material.getDetailMat().size() != 0)
			throw new SQLException();
		if (material.getCodType().equals(RecordMode._1)
				&& (material.getDetailMat() == null || material.getDetailMat().size() == 0))
			throw new SQLException();

		insertStmtT01.addBatch();
	}

	private void prepareMaterialTextInsert(PreparedStatement insertStmtT02, Material material,
			MaterialText materialText) throws SQLException {

		insertStmtT02.setLong(1, material.getCodOwner());
		insertStmtT02.setInt(2, material.getCodMaterial());
		insertStmtT02.setString(3, materialText.getLanguage());
		insertStmtT02.setString(4, materialText.getName());
		insertStmtT02.setString(5, materialText.getDescription());
		insertStmtT02.setString(6, materialText.getTextLong());

		insertStmtT02.addBatch();
	}

	private void prepareMaterialDetailInsert(PreparedStatement insertStmtT03, Material material,
			MaterialDetail detailMat) throws SQLException {

		insertStmtT03.setLong(1, material.getCodOwner());
		insertStmtT03.setInt(2, material.getCodMaterial());
		insertStmtT03.setInt(3, detailMat.getCodDetail());
		insertStmtT03.setFloat(4,
				detailMat.getQuantityMat() != 0 ? detailMat.getQuantityMat() : detailMat.getQuantity());
		insertStmtT03.setString(5, RecordMode.RECORD_OK);

		insertStmtT03.addBatch();
	}

}
