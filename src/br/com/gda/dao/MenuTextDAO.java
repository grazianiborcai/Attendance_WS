package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.MenuTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.MenuText;
import br.com.mind5.helper.RecordMode;

public class MenuTextDAO extends ConnectionBD {

	public SQLException insertMenuText(ArrayList<MenuText> menuTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(MenuTextHelper.ST_IN_ALL_FIELD);

			for (MenuText menuItemText : menuTextList) {

				prepareInsert(insertStmt, menuItemText);
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

	public SQLException updateMenuText(ArrayList<MenuText> menuTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(MenuTextHelper.ST_IN_ALL_FIELD);

			updateStmt = conn
					.prepareStatement(MenuTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			for (MenuText menuItemText : menuTextList) {

				if ((menuItemText.getRecordMode() != null && menuItemText
						.getRecordMode().equals(RecordMode.ISNEW))) {

					prepareInsert(insertStmt, menuItemText);

				} else {

					updateStmt.setString(1, menuItemText.getName());

					updateStmt.setLong(2, menuItemText.getCodOwner());
					updateStmt.setInt(3, menuItemText.getCodMenu());
					updateStmt.setString(4, menuItemText.getLanguage());

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

	public SQLException deleteMenuText(List<Long> codOwner,
			List<Integer> codMenu, List<String> language, List<String> name) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new MenuTextHelper()
					.prepareDelete(codOwner, codMenu, language, name));

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

	public ArrayList<MenuText> selectMenuText(List<Long> codOwner,
			List<Integer> codMenu, List<String> language, List<String> name)
			throws SQLException {

		ArrayList<MenuText> menuTextList = new ArrayList<MenuText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			MenuTextHelper menuTextHelper = new MenuTextHelper();

			selectStmt = conn.prepareStatement(menuTextHelper.prepareSelect(
					codOwner, codMenu, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {
				menuTextList.add(menuTextHelper.assignResult(resultSet));
			}

			return menuTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt,
			MenuText menuItemText) throws SQLException {

		insertStmt.setLong(1, menuItemText.getCodOwner());
		insertStmt.setInt(2, menuItemText.getCodMenu());
		insertStmt.setString(3, menuItemText.getLanguage());
		insertStmt.setString(4, menuItemText.getName());

		insertStmt.addBatch();
	}

}
