package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.MaterialTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.MaterialText;
import br.com.gda.helper.RecordMode;

public class MaterialTextDAO extends ConnectionBD {

	public SQLException insertMaterialText(
			ArrayList<MaterialText> materialTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(MaterialTextHelper.ST_IN_ALL_FIELD);

			for (MaterialText materialText : materialTextList) {

				prepareInsert(insertStmt, materialText);
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

	public SQLException updateMaterialText(
			ArrayList<MaterialText> materialTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(MaterialTextHelper.ST_IN_ALL_FIELD);

			updateStmt = conn
					.prepareStatement(MaterialTextHelper.ST_UP_ALL_FIELD);

			for (MaterialText materialText : materialTextList) {

				if (materialText.getRecordMode() != null
						&& materialText.getRecordMode()
								.equals(RecordMode.ISNEW)) {

					prepareInsert(insertStmt, materialText);

				} else {

					updateStmt.setString(1, materialText.getName());
					updateStmt.setString(2, materialText.getDescription());
					updateStmt.setString(3, materialText.getTextLong());

					updateStmt.setLong(4, materialText.getCodOwner());
					updateStmt.setInt(5, materialText.getCodMaterial());
					updateStmt.setString(6, materialText.getLanguage());

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

	public SQLException deleteMaterialText(List<Long> codOwner,
			List<Integer> codMaterial, List<String> language,
			List<String> name, List<String> description, List<String> textLong) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new MaterialTextHelper()
					.prepareDelete(codOwner, codMaterial, language, name,
							description, textLong));

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

	public ArrayList<MaterialText> selectMaterialText(List<Long> codOwner,
			List<Integer> codMaterial, List<String> language,
			List<String> name, List<String> description, List<String> textLong)
			throws SQLException {

		ArrayList<MaterialText> materialTextList = new ArrayList<MaterialText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			MaterialTextHelper materialTextHelper = new MaterialTextHelper();

			selectStmt = conn.prepareStatement(materialTextHelper
					.prepareSelect(codOwner, codMaterial, language, name,
							description, textLong));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				materialTextList
						.add(materialTextHelper.assignResult(resultSet));
			}

			return materialTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt,
			MaterialText materialText) throws SQLException {

		insertStmt.setLong(1, materialText.getCodOwner());
		insertStmt.setInt(2, materialText.getCodMaterial());
		insertStmt.setString(3, materialText.getLanguage());
		insertStmt.setString(4, materialText.getName());
		insertStmt.setString(5, materialText.getDescription());
		insertStmt.setString(6, materialText.getTextLong());

		insertStmt.addBatch();
	}

}
