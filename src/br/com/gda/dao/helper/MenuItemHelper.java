package br.com.gda.dao.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.MenuItem;
import br.com.mind5.helper.MenuItemText;
import br.com.mind5.helper.RecordMode;

public class MenuItemHelper extends GdaDB {

	public static final String TABLE = "Menu_item";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MENU;
	public static final String FIELD03 = ITEM;
	public static final String FIELD04 = IDENTIFIER;
	public static final String FIELD05 = LEVEL;
	public static final String FIELD06 = ID_FATHER;
	public static final String FIELD07 = FIRST_CHILD;
	public static final String FIELD08 = NEXT;
	public static final String FIELD09 = COD_MATERIAL;
	public static final String FIELD10 = RECORD_MODE;
	public static final String FIELD11 = "Icon";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN (" + SCHEMA + "."
			+ MaterialHelper.TABLE + " LEFT JOIN " + SCHEMA + "."
			+ MaterialTextHelper.TABLE + " ON " + MaterialHelper.TABLE + "."
			+ MaterialHelper.FIELD01 + " = " + MaterialTextHelper.TABLE + "."
			+ MaterialTextHelper.FIELD01 + " AND " + MaterialHelper.TABLE + "."
			+ MaterialHelper.FIELD02 + " = " + MaterialTextHelper.TABLE + "."
			+ MaterialTextHelper.FIELD02 + ") ON " + TABLE + "." + FIELD01
			+ " = " + MaterialHelper.TABLE + "." + MaterialHelper.FIELD01
			+ " AND " + TABLE + "." + FIELD09 + " = " + MaterialHelper.TABLE
			+ "." + MaterialHelper.FIELD02 + " LEFT JOIN " + SCHEMA + "."
			+ MenuItemTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ MenuItemTextHelper.TABLE + "." + MenuItemTextHelper.FIELD01
			+ " AND " + TABLE + "." + FIELD02 + " = "
			+ MenuItemTextHelper.TABLE + "." + MenuItemTextHelper.FIELD02
			+ " AND " + TABLE + "." + FIELD03 + " = "
			+ MenuItemTextHelper.TABLE + "." + MenuItemTextHelper.FIELD03;

	/************************** To attend MaterialDAO **************************/
	public static final String ST_UP_RECORD_MODE_BY_MATERIAL = "UPDATE "
			+ SCHEMA + "." + TABLE + " SET " + FIELD10 + "=?" + " WHERE "
			+ FIELD01 + "=? AND " + FIELD09 + "=?";
	/***************************************************************************/

	private static final String ID_ITEM = "@id_item_";

	MaterialHelper materialHelper;
	MaterialTextHelper materialTextHelper;
	MenuItemTextHelper menuItemTextHelper;

	public MenuItemHelper() {
		materialHelper = new MaterialHelper();
		materialTextHelper = new MaterialTextHelper();
		menuItemTextHelper = new MenuItemTextHelper();
	}

	public MenuItemHelper(MaterialHelper materialHelper,
			MaterialTextHelper materialTextHelper,
			MenuItemTextHelper menuItemTextHelper) {
		this.materialHelper = materialHelper;
		this.materialTextHelper = materialTextHelper;
		this.menuItemTextHelper = menuItemTextHelper;
	}

	public void assignResultWithText(Integer codStore, ResultSet resultSet,
			ArrayList<MenuItem> menuItemList) throws SQLException {

		if (resultSet.isFirst()
				|| !menuItemList.get(menuItemList.size() - 1).getCodOwner()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !menuItemList.get(menuItemList.size() - 1).getCodMenu()
						.equals(resultSet.getInt(TABLE + "." + FIELD02))
				|| !menuItemList.get(menuItemList.size() - 1).getItem()
						.equals(resultSet.getInt(TABLE + "." + FIELD03))) {

			MenuItem menuItem = assignResult(resultSet);

			if (menuItem.getIdentifier().equals(RecordMode.F)) {

				menuItem.setMaterial(materialHelper.assignResult(resultSet));

				menuItem.getMaterial().setCodMaterial(
						resultSet.getInt(TABLE + "." + FIELD09));

				menuItem.getMaterial().getMaterialText()
						.add(materialTextHelper.assignResult(resultSet));

			} else {

				menuItem.getMenuItemText().add(
						menuItemTextHelper.assignResult(resultSet));

			}

			menuItemList.add(menuItem);

		} else {

			if (menuItemList.get(menuItemList.size() - 1).getIdentifier()
					.equals(RecordMode.F)) {

				menuItemList.get(menuItemList.size() - 1).getMaterial()
						.getMaterialText()
						.add(materialTextHelper.assignResult(resultSet));

			} else {

				menuItemList.get(menuItemList.size() - 1).getMenuItemText()
						.add(menuItemTextHelper.assignResult(resultSet));

			}
		}
	}

	public MenuItem assignResult(ResultSet resultSet) throws SQLException {

		MenuItem menuItem = new MenuItem();

		menuItem.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		menuItem.setCodMenu(resultSet.getInt(TABLE + "." + FIELD02));
		menuItem.setItem(resultSet.getInt(TABLE + "." + FIELD03));
		menuItem.setIdentifier(resultSet.getString(TABLE + "." + FIELD04));
		menuItem.setLevel(resultSet.getByte(TABLE + "." + FIELD05));
		menuItem.setIdFather(resultSet.getInt(TABLE + "." + FIELD06));
		menuItem.setFirstChild(resultSet.getInt(TABLE + "." + FIELD07));
		menuItem.setNext(resultSet.getInt(TABLE + "." + FIELD08));
		menuItem.setRecordMode(resultSet.getString(TABLE + "." + FIELD10));
		menuItem.setIcon(resultSet.getString(TABLE + "." + FIELD11));

		return menuItem;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMenu,
			List<Integer> item, List<String> recordMode) {

		String stmt = ST_DELETE;

		List<String> where = prepareMenuItemWhere(codOwner, codMenu, item,
				recordMode);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, Integer codStore,
			List<Integer> codMenu, List<String> language,
			List<String> recordMode) {

		String stmt = ST_SELECT_WITH_TEXT;

		stmt = prepareMenuItemWhereWithLanguage(codOwner, codMenu, language,
				recordMode, stmt);

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD02 + ", " + TABLE + "." + FIELD03;

		return stmt;
	}

	public List<String> prepareMenuItemWhere(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMenu);
		assignFilterInt(where, TABLE + "." + FIELD03, item);
		assignFilterString(where, TABLE + "." + FIELD10, recordMode);

		return where;
	}

	public String prepareMenuItemWhereWithLanguage(List<Long> codOwner,
			List<Integer> codMenu, List<String> language,
			List<String> recordMode, String stmt) {

		List<String> where = prepareMenuItemWhere(codOwner, codMenu, null,
				recordMode);

		if (language != null && !language.isEmpty()) {
			String s = new String();
			String s2 = new String();
			String s3 = new String();
			s = MenuItemTextHelper.TABLE + "." + MenuItemTextHelper.FIELD04
					+ " IN ('";
			s2 = MenuItemTextHelper.TABLE + "." + MenuItemTextHelper.FIELD03
					+ " IN ('";
			for (int i = 0; i < language.size(); i++) {
				if (i == 0) {
					s = s + language.get(i);
					s2 = s2 + language.get(i);
				} else {
					s = s + "', '" + language.get(i);
					s2 = s2 + "', '" + language.get(i);
				}
			}
			s = s + "')";
			s2 = s2 + "')";
			s3 = "( " + s + " OR " + s2 + " )";
			where.add(s3);
		}

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public void saveMenuItemList(List<MenuItem> hierDone, Connection conn,
			String recordMode) throws SQLException {

		saveMenuItemList(hierDone, conn, null, recordMode, null, null, false,
				0, 0);
	}

	public void saveMenuItemList(List<MenuItem> hierDone, Connection conn)
			throws SQLException {

		saveMenuItemList(hierDone, conn, null, null, null, null, false, 0, 0);
	}

	private void saveMenuItemList(List<MenuItem> hierDone, Connection conn,
			String idFather, String recordMode, Long codOwner, Integer codMenu,
			boolean isHead, int cont, int level) throws SQLException {

		if (idFather == null)
			idFather = "0";

		for (int i = 0; i < hierDone.size(); i++) {
			MenuItem menuItem = hierDone.get(i);

			if (isHead && codMenu == null)
				menuItem.setCodMenu(0);

			PreparedStatement in01 = null;
			PreparedStatement in02 = null;
			PreparedStatement up01 = null;
			PreparedStatement up02 = null;
			PreparedStatement up03 = null;
			PreparedStatement up04 = null;
			PreparedStatement var = null;

			try {

				String stmtInsert01 = "INSERT INTO " + SCHEMA + "." + TABLE
						+ " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD04
						+ ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD09
						+ ", " + FIELD10 + ", " + FIELD11 + ") " + "VALUES (?, ?, ?, ?, "
						+ idFather + ", ?, ?, ?)";

				in01 = conn.prepareStatement(stmtInsert01);

				in02 = conn
						.prepareStatement(MenuItemTextHelper.ST_IN_ALL_FIELD);

				String stmtUpdate01 = "UPDATE " + SCHEMA + "." + TABLE
						+ " SET " + FIELD04 + "=?" + ", " + FIELD05 + "=?"
						+ ", " + FIELD06 + "=" + idFather + ", " + FIELD09
						+ "=?" + ", " + FIELD10 + "=?" + ", " + FIELD11 + "=?" + " WHERE " + FIELD01
						+ "=? AND " + FIELD02 + "=? AND " + FIELD03 + "=?";

				up01 = conn.prepareStatement(stmtUpdate01);

				up02 = conn
						.prepareStatement(MenuItemTextHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

				String sFather;
				String idItem;

				if ((recordMode != null && recordMode.equals(RecordMode.ISNEW))
						|| (menuItem.getRecordMode() != null && menuItem
								.getRecordMode().equals(RecordMode.ISNEW))
						|| menuItem.getItem() == null
						|| menuItem.getItem().equals(0)) {

					in01.setLong(
							1,
							codOwner != null ? codOwner : menuItem
									.getCodOwner());
					in01.setInt(
							2,
							codMenu != null ? codMenu
									: menuItem.getCodMenu() != null ? menuItem
											.getCodMenu() : 0);
					if (((menuItem.getMenuItemText() == null || menuItem
							.getMenuItemText().size() == 0) && menuItem
							.getMaterial().getCodMaterial() == null)
							|| (menuItem.getMenuItemText() != null
									&& menuItem.getMenuItemText().size() != 0 && menuItem
									.getMaterial().getCodMaterial() != null))
						throw new SQLException();
					if (menuItem.getMaterial().getCodMaterial() != null) {
						in01.setString(3, RecordMode.F);
						in01.setInt(5, menuItem.getMaterial().getCodMaterial());
						in01.setNull(7, Types.VARCHAR);
					} else {
						in01.setString(3, RecordMode.H);
						in01.setNull(5, Types.INTEGER);
						in01.setString(7, menuItem.getIcon());
					}
					in01.setInt(4, level);
					in01.setString(6, RecordMode.RECORD_OK);

					in01.executeUpdate();
					menuItem.setItem(0);

					idItem = LAST_INSERT_ID;

				} else {

					if (((menuItem.getMenuItemText() == null || menuItem
							.getMenuItemText().size() == 0) && menuItem
							.getMaterial().getCodMaterial() == null)
							|| (menuItem.getMenuItemText() != null
									&& menuItem.getMenuItemText().size() != 0 && menuItem
									.getMaterial().getCodMaterial() != null))
						throw new SQLException();
					if (menuItem.getMaterial().getCodMaterial() != null) {
						up01.setString(1, RecordMode.F);
						up01.setInt(3, menuItem.getMaterial().getCodMaterial());
						up01.setNull(5, Types.VARCHAR);
					} else {
						up01.setString(1, RecordMode.H);
						up01.setNull(3, Types.INTEGER);
						up01.setString(5, menuItem.getIcon());
					}
					up01.setInt(2, level);

					up01.setLong(
							6,
							codOwner != null ? codOwner : menuItem
									.getCodOwner());
					up01.setInt(7,
							codMenu != null ? codMenu : menuItem.getCodMenu());
					up01.setInt(8, menuItem.getItem());

					if ((recordMode != null && (recordMode
							.equals(RecordMode.ISDELETED) || recordMode
							.equals(RecordMode.RECORD_DELETED)))
							|| (menuItem.getRecordMode() != null && (menuItem
									.getRecordMode().equals(
											RecordMode.ISDELETED) || menuItem
									.getRecordMode().equals(
											RecordMode.RECORD_DELETED)))) {
						up01.setString(4, RecordMode.RECORD_DELETED);
					} else {
						up01.setString(4, RecordMode.RECORD_OK);
					}

					up01.addBatch();

					idItem = Integer.toString(menuItem.getItem());
				}

				cont = cont + 1;
				sFather = ID_ITEM + Integer.toString(cont);
				String stmt = SET + sFather + EQ + idItem;
				var = conn.prepareStatement(stmt);
				var.execute();

				for (MenuItemText menuItemText : menuItem.getMenuItemText()) {

					if ((recordMode != null && recordMode
							.equals(RecordMode.ISNEW))
							|| (menuItem.getRecordMode() != null && menuItem
									.getRecordMode().equals(RecordMode.ISNEW))
							|| (menuItemText.getRecordMode() != null && menuItemText
									.getRecordMode().equals(RecordMode.ISNEW))
							|| menuItem.getItem() == null
							|| menuItem.getItem().equals(0)) {

						in02.setLong(
								1,
								codOwner != null ? codOwner : menuItem
										.getCodOwner());
						in02.setInt(
								2,
								codMenu != null ? codMenu : menuItem
										.getCodMenu() != null ? menuItem
										.getCodMenu() : 0);
						in02.setInt(
								3,
								codMenu != null ? 0
										: menuItem.getItem() != null ? menuItem
												.getItem() : 0);
						in02.setString(4, menuItemText.getLanguage());

						in02.setString(5, menuItemText.getName());

						in02.addBatch();

					} else {

						up02.setString(1, menuItemText.getName());
						up02.setLong(
								2,
								codOwner != null ? codOwner : menuItem
										.getCodOwner());
						up02.setInt(
								3,
								codMenu != null ? codMenu : menuItem
										.getCodMenu());
						up02.setInt(4, menuItem.getItem());
						up02.setString(5, menuItemText.getLanguage());

						up02.addBatch();
					}

				}

				in02.executeBatch();

				up01.executeBatch();

				up02.executeBatch();

				// NextID
				if (i != 0) {

					String stmtUpdate03;

					if (isHead && codMenu == null) {

						stmtUpdate03 = "UPDATE " + SCHEMA + "." + TABLE
								+ " SET " + FIELD08 + "=" + sFather + " WHERE "
								+ FIELD01 + "=? AND " + FIELD02 + "="
								+ MenuHelper.LAST_ID_MENU_HEAD + " AND "
								+ FIELD03 + "=" + ID_ITEM
								+ Integer.toString(cont - 1);

						up03 = conn.prepareStatement(stmtUpdate03);

						up03.setLong(
								1,
								codOwner != null ? codOwner : menuItem
										.getCodOwner());
					} else {

						stmtUpdate03 = "UPDATE " + SCHEMA + "." + TABLE
								+ " SET " + FIELD08 + "=" + sFather + " WHERE "
								+ FIELD01 + "=? AND " + FIELD02 + "=? AND "
								+ FIELD03 + "=" + ID_ITEM
								+ Integer.toString(cont - 1);

						up03 = conn.prepareStatement(stmtUpdate03);

						up03.setLong(
								1,
								codOwner != null ? codOwner : menuItem
										.getCodOwner());
						up03.setInt(
								2,
								codMenu != null ? codMenu : menuItem
										.getCodMenu());

					}

					up03.executeUpdate();

					// FirstChild
				} else if (idFather != null && !idFather.equals("0")) {

					String stmtUpdate04;

					if (isHead && codMenu == null) {

						stmtUpdate04 = "UPDATE " + SCHEMA + "." + TABLE
								+ " SET " + FIELD07 + "=" + sFather + " WHERE "
								+ FIELD01 + "=? AND " + FIELD02 + "="
								+ MenuHelper.LAST_ID_MENU_HEAD + " AND "
								+ FIELD03 + "=" + idFather;

						up04 = conn.prepareStatement(stmtUpdate04);

						up04.setLong(
								1,
								codOwner != null ? codOwner : menuItem
										.getCodOwner());
					} else {

						stmtUpdate04 = "UPDATE " + SCHEMA + "." + TABLE
								+ " SET " + FIELD07 + "=" + sFather + " WHERE "
								+ FIELD01 + "=? AND " + FIELD02 + "=? AND "
								+ FIELD03 + "=" + idFather;

						up04 = conn.prepareStatement(stmtUpdate04);

						up04.setLong(
								1,
								codOwner != null ? codOwner : menuItem
										.getCodOwner());
						up04.setInt(
								2,
								codMenu != null ? codMenu : menuItem
										.getCodMenu());
					}

					up04.executeUpdate();
				}

				String recordModeNext;

				if (recordMode != null
						&& (recordMode.equals(RecordMode.ISNEW)
								|| recordMode.equals(RecordMode.ISDELETED) || recordMode
									.equals(RecordMode.RECORD_DELETED)))
					recordModeNext = recordMode;
				else
					recordModeNext = menuItem.getRecordMode();

				closeConnection(in01, in02, var, up01, up02, up03, up04);
				if (menuItem.getChildren() != null
						|| menuItem.getChildren().size() != 0)
					saveMenuItemList(menuItem.getChildren(), conn, sFather,
							recordModeNext, codOwner, codMenu, isHead, cont,
							level + 1);

			} catch (SQLException e) {
				throw e;
			} finally {
				closeConnection(in01, in02, var, up01, up02, up03, up04);
			}
		}
	}

	public void saveMenuItemList(List<MenuItem> hierDone, Connection conn,
			String recordMode, Long codOwner) throws SQLException {

		saveMenuItemList(hierDone, conn, null, recordMode, codOwner, null,
				true, 0, 0);
	}

	public void saveMenuItemList(List<MenuItem> hierDone, Connection conn,
			String recordMode, Long codOwner, Integer codMenu)
			throws SQLException {

		saveMenuItemList(hierDone, conn, null, recordMode, codOwner, codMenu,
				true, 0, 0);
	}

	private void closeConnection(PreparedStatement stmt,
			PreparedStatement stmt2, PreparedStatement stmt3,
			PreparedStatement stmt4, PreparedStatement stmt5,
			PreparedStatement stmt6, PreparedStatement stmt7) {

		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (stmt2 != null) {
				stmt2.close();
				stmt2 = null;
			}
			if (stmt3 != null) {
				stmt3.close();
				stmt3 = null;
			}
			if (stmt4 != null) {
				stmt4.close();
				stmt4 = null;
			}
			if (stmt5 != null) {
				stmt5.close();
				stmt5 = null;
			}
			if (stmt6 != null) {
				stmt6.close();
				stmt6 = null;
			}
			if (stmt7 != null) {
				stmt7.close();
				stmt7 = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
