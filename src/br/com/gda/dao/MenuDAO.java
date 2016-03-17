package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.MenuHelper;
import br.com.gda.dao.helper.MenuItemHelper;
import br.com.gda.dao.helper.MenuTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.Menu;
import br.com.gda.helper.MenuText;
import br.com.gda.helper.RecordMode;

public class MenuDAO extends ConnectionBD {

	public SQLException insertMenu(ArrayList<Menu> menuList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;
		PreparedStatement variableStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(MenuHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn
					.prepareStatement(MenuTextHelper.ST_IN_ALL_FIELD);

			variableStmt = conn.prepareStatement(MenuHelper.VARIABLE);

			MenuItemHelper menuItemHelper = new MenuItemHelper();
			for (Menu menu : menuList) {

				prepareMenuInsert(insertStmtT01, variableStmt, menu);

				for (MenuText menuText : menu.getMenuText()) {

					prepareMenuTextInsert(insertStmtT02, menu, menuText);
				}

				insertStmtT02.executeBatch();

				menuItemHelper.saveMenuItemList(menu.getMenuItem(), conn,
						RecordMode.ISNEW, menu.getCodOwner());
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
			closeConnection(conn, insertStmtT01, insertStmtT02, variableStmt);
		}

	}

	public SQLException updateMenu(ArrayList<Menu> menuList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;
		PreparedStatement insertStmtT02 = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement updateStmtT02 = null;
		PreparedStatement deleteStmtT01 = null;
		PreparedStatement variableStmt = null;

		List<Menu> storeMenuList = new ArrayList<Menu>();

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(MenuHelper.ST_IN_ALL_FIELD);

			insertStmtT02 = conn
					.prepareStatement(MenuTextHelper.ST_IN_ALL_FIELD);

			updateStmtT01 = conn
					.prepareStatement(MenuHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			updateStmtT02 = conn
					.prepareStatement(MenuTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			variableStmt = conn.prepareStatement(MenuHelper.VARIABLE);

			MenuItemHelper menuItemHelper = new MenuItemHelper();
			for (Menu menu : menuList) {

				if ((menu.getRecordMode() != null && menu.getRecordMode()
						.equals(RecordMode.ISNEW))
						|| menu.getCodMenu() == null
						|| menu.getCodMenu().equals(0)) {

					prepareMenuInsert(insertStmtT01, variableStmt, menu);

				} else {

					if (menu.getRecordMode() != null
							&& (menu.getRecordMode().equals(
									RecordMode.ISDELETED) || menu
									.getRecordMode().equals(
											RecordMode.RECORD_DELETED))) {
						updateStmtT01.setString(1, RecordMode.RECORD_DELETED);

						storeMenuList.add(menu);

					} else {
						updateStmtT01.setString(1, RecordMode.RECORD_OK);
					}
					updateStmtT01.setLong(2, menu.getCodOwner());
					updateStmtT01.setInt(3, menu.getCodMenu());

					updateStmtT01.addBatch();

				}

				for (MenuText menuText : menu.getMenuText()) {

					if ((menu.getRecordMode() != null && menu.getRecordMode()
							.equals(RecordMode.ISNEW))
							|| (menuText.getRecordMode() != null && menuText
									.getRecordMode().equals(RecordMode.ISNEW))
							|| menu.getCodMenu() == null
							|| menu.getCodMenu().equals(0)) {

						prepareMenuTextInsert(insertStmtT02, menu, menuText);

					} else {

						updateStmtT02.setString(1, menuText.getName());
						updateStmtT02.setLong(2, menu.getCodOwner());
						updateStmtT02.setInt(3, menu.getCodMenu());
						updateStmtT02.setString(4, menuText.getLanguage());

						updateStmtT02.addBatch();
					}

				}

				insertStmtT02.executeBatch();

				updateStmtT01.executeBatch();

				updateStmtT02.executeBatch();

				menuItemHelper.saveMenuItemList(menu.getMenuItem(), conn,
						menu.getRecordMode(), menu.getCodOwner(),
						menu.getCodMenu());
			}

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
			closeConnection(conn, deleteStmtT01, insertStmtT01, insertStmtT02,
					updateStmtT01, updateStmtT02, variableStmt);
		}
	}

	public SQLException deleteMenu(List<Long> codOwner, List<Integer> codMenu,
			List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new MenuHelper().prepareDelete(
					codOwner, codMenu, recordMode));

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

	public ArrayList<Menu> selectMenu(List<Long> codOwner,
			List<Integer> codMenu, List<String> recordMode,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Menu> menuList = new ArrayList<Menu>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			MenuHelper menuHelper = new MenuHelper();

			selectStmt = conn.prepareStatement(menuHelper.prepareSelect(
					codOwner, codMenu, recordMode, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				menuHelper.assignResult(menuList, resultSet);
			}

			return menuList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareMenuInsert(PreparedStatement insertStmtT01,
			PreparedStatement variableStmt, Menu menu) throws SQLException {

		insertStmtT01.setLong(1, menu.getCodOwner());
		insertStmtT01.setString(2, RecordMode.RECORD_OK);
		if (menu.getMenuText() == null || menu.getMenuText().size() == 0
				|| menu.getMenuItem() == null || menu.getMenuItem().size() == 0)
			throw new SQLException();

		insertStmtT01.executeUpdate();
		variableStmt.execute();

		menu.setCodMenu(null);
	}

	private void prepareMenuTextInsert(PreparedStatement insertStmtT02,
			Menu menu, MenuText menuText) throws SQLException {

		insertStmtT02.setLong(1, menu.getCodOwner());
		insertStmtT02.setInt(2, menu.getCodMenu() != null ? menu.getCodMenu()
				: 0);
		insertStmtT02.setString(3, menuText.getLanguage());
		insertStmtT02.setString(4, menuText.getName());

		insertStmtT02.addBatch();
	}

}
