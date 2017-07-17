package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.MaterialStoreHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.db.GdaDB;
import br.com.mind5.helper.MaterialStore;
import br.com.mind5.helper.RecordMode;

public class MaterialStoreDAO extends ConnectionBD {

	public SQLException insertMaterialStore(ArrayList<MaterialStore> materialStoreList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(MaterialStoreHelper.ST_IN_ALL_FIELD);

			for (MaterialStore materialStore : materialStoreList) {

				prepareInsert(insertStmtT01, materialStore);
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

	public SQLException updateMaterialStore(ArrayList<MaterialStore> materialStoreList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement deleteStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(MaterialStoreHelper.ST_IN_ALL_FIELD);

			updateStmtT01 = conn.prepareStatement(MaterialStoreHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			MaterialStoreHelper materialStoreHelper = new MaterialStoreHelper();
			for (MaterialStore materialStore : materialStoreList) {

				if (materialStore.getRecordMode() != null && materialStore.getRecordMode().equals(RecordMode.ISNEW)) {

					prepareInsert(insertStmtT01, materialStore);

				} else {

					if (materialStore.getRecordMode() != null
							&& (materialStore.getRecordMode().equals(RecordMode.ISDELETED)
									|| materialStore.getRecordMode().equals(RecordMode.RECORD_DELETED))) {

						deleteStmtT01 = conn.prepareStatement(
								materialStoreHelper.prepareDelete(GdaDB.EQ, materialStore.getCodOwner(), GdaDB.EQ,
										materialStore.getCodMaterial(), GdaDB.EQ, materialStore.getCodStore()));

						deleteStmtT01.execute();

					} else {
						updateStmtT01.setBigDecimal(1,
								materialStore.getPriceStore() != null && !materialStore.getPriceStore().equals(0)
										? materialStore.getPriceStore() : materialStore.getPrice());
						updateStmtT01.setInt(2,
								materialStore.getDurationStore() != null
										&& !materialStore.getDurationStore().equals(0)
												? materialStore.getDurationStore() : materialStore.getDuration());
						updateStmtT01.setString(3, RecordMode.RECORD_OK);
						updateStmtT01.setString(4, materialStore.getCodCurrStore());
						updateStmtT01.setLong(5, materialStore.getCodOwner());
						updateStmtT01.setInt(6, materialStore.getCodMaterial());
						updateStmtT01.setInt(7, materialStore.getCodStore());

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

	public SQLException deleteMaterialStore(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codStore,
			List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(
					new MaterialStoreHelper().prepareDelete(codOwner, codMaterial, codStore, recordMode));

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

	public ArrayList<MaterialStore> selectMaterialStore(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codStore, List<Integer> codCategory, List<Integer> codType, List<String> image,
			List<String> barCode, List<String> recordMode, List<String> language, List<String> name,
			List<String> description, List<String> textLong) throws SQLException {

		ArrayList<MaterialStore> materialList = new ArrayList<MaterialStore>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			MaterialStoreHelper materialStoreHelper = new MaterialStoreHelper();

			selectStmt = conn.prepareStatement(materialStoreHelper.prepareSelect(codOwner, codMaterial, codStore,
					codCategory, codType, image, barCode, recordMode, language, name, description, textLong));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				materialStoreHelper.assignResult(materialList, resultSet);
			}

			return materialList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmtT01, MaterialStore materialStore) throws SQLException {

		insertStmtT01.setLong(1, materialStore.getCodOwner());
		insertStmtT01.setInt(2, materialStore.getCodMaterial());
		insertStmtT01.setInt(3, materialStore.getCodStore());
		insertStmtT01.setBigDecimal(4, materialStore.getPriceStore() != null && !materialStore.getPriceStore().equals(0)
				? materialStore.getPriceStore() : materialStore.getPrice());
		insertStmtT01.setInt(5,
				materialStore.getDurationStore() != null && !materialStore.getDurationStore().equals(0)
						? materialStore.getDurationStore() : materialStore.getDuration());
		insertStmtT01.setString(6, RecordMode.RECORD_OK);
		insertStmtT01.setString(7, materialStore.getCodCurrStore());

		insertStmtT01.addBatch();
	}

}
