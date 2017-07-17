package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.MenuItemTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.MenuItemText;
import br.com.mind5.helper.RecordMode;

public class MenuItemTextDAO extends ConnectionBD {

	public SQLException insertMenuItemText(
			ArrayList<MenuItemText> menuItemTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(MenuItemTextHelper.ST_IN_ALL_FIELD);

			for (MenuItemText menuItemText : menuItemTextList) {

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

	public SQLException updateMenuItemText(
			ArrayList<MenuItemText> menuItemTextList) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn
					.prepareStatement(MenuItemTextHelper.ST_IN_ALL_FIELD);

			updateStmt = conn
					.prepareStatement(MenuItemTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			for (MenuItemText menuItemText : menuItemTextList) {

				if ((menuItemText.getRecordMode() != null && menuItemText
						.getRecordMode().equals(RecordMode.ISNEW))) {

					prepareInsert(insertStmt, menuItemText);

				} else {

					updateStmt.setString(1, menuItemText.getName());

					updateStmt.setLong(2, menuItemText.getCodOwner());
					updateStmt.setInt(3, menuItemText.getCodMenu());
					updateStmt.setInt(4, menuItemText.getItem());
					updateStmt.setString(5, menuItemText.getLanguage());

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

	public SQLException deleteMenuItemText(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> language,
			List<String> name) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new MenuItemTextHelper()
					.prepareDelete(codOwner, codMenu, item, language, name));

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

	public ArrayList<MenuItemText> selectMenuItemText(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> language,
			List<String> name) throws SQLException {

		ArrayList<MenuItemText> menuItemTextList = new ArrayList<MenuItemText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			MenuItemTextHelper menuItemTextHelper = new MenuItemTextHelper();

			selectStmt = conn.prepareStatement(menuItemTextHelper
					.prepareSelect(codOwner, codMenu, item, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				menuItemTextList
						.add(menuItemTextHelper.assignResult(resultSet));
			}

			return menuItemTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt,
			MenuItemText menuItemText) throws SQLException {

		insertStmt.setLong(1, menuItemText.getCodOwner());
		insertStmt.setInt(2, menuItemText.getCodMenu());
		insertStmt.setInt(3, menuItemText.getItem());
		insertStmt.setString(4, menuItemText.getLanguage());
		insertStmt.setString(5, menuItemText.getName());

		insertStmt.addBatch();
	}

}
