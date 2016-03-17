package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.MenuItemHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.MenuItem;
import br.com.gda.helper.RecordMode;

public class MenuItemDAO extends ConnectionBD {

	public SQLException insertMenuItem(ArrayList<MenuItem> menuItemList) {

		Connection conn = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			new MenuItemHelper().saveMenuItemList(menuItemList, conn,
					RecordMode.ISNEW);

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
			closeConnection(conn);
		}
	}

	public SQLException updateMenuItem(ArrayList<MenuItem> menuItemList) {

		Connection conn = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			new MenuItemHelper().saveMenuItemList(menuItemList, conn);

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
			closeConnection(conn);
		}
	}

	public SQLException deleteMenuItem(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new MenuItemHelper()
					.prepareDelete(codOwner, codMenu, item, recordMode));

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

	public ArrayList<MenuItem> selectMenuItem(List<Long> codOwner,
			Integer codStore, List<Integer> codMenu, List<String> language,
			List<String> recordMode) throws SQLException {

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			ArrayList<MenuItem> menuItemList = new ArrayList<MenuItem>();

			conn = getConnection();

			MenuItemHelper menuItemHelper = new MenuItemHelper();

			selectStmt = conn.prepareStatement(menuItemHelper.prepareSelect(
					codOwner, codStore, codMenu, language, recordMode));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				menuItemHelper.assignResultWithText(codStore, resultSet,
						menuItemList);
			}

			return menuItemList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
