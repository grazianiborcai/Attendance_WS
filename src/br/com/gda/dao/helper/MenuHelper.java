package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Menu;

public class MenuHelper extends GdaDB {

	public static final String TABLE = "Menu_head";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MENU;
	public static final String FIELD03 = RECORD_MODE;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD03 + ") " + "VALUES (?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD03 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD02 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE + " LEFT JOIN " + SCHEMA + "." + MenuTextHelper.TABLE
			+ " ON " + TABLE + "." + FIELD01 + " = " + MenuTextHelper.TABLE
			+ "." + MenuTextHelper.FIELD01 + " AND " + TABLE + "." + FIELD02
			+ " = " + MenuTextHelper.TABLE + "." + MenuTextHelper.FIELD02;

	public static final String LAST_ID_MENU_HEAD = "@last_id_menu_head";

	public static final String VARIABLE = SET + LAST_ID_MENU_HEAD + EQ
			+ LAST_INSERT_ID;

	MenuTextHelper menuTextHelper;

	public MenuHelper() {
		menuTextHelper = new MenuTextHelper();
	}

	public MenuHelper(MenuTextHelper menuTextHelper) {
		this.menuTextHelper = menuTextHelper;
	}

	public void assignResult(ArrayList<Menu> menuList, ResultSet resultSet)
			throws SQLException {

		if (resultSet.isFirst()
				|| !menuList.get(menuList.size() - 1).getCodOwner()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !menuList.get(menuList.size() - 1).getCodMenu()
						.equals(resultSet.getInt(TABLE + "." + FIELD02))) {

			Menu menu = assignResult(resultSet);

			menu.getMenuText().add(menuTextHelper.assignResult(resultSet));
			menuList.add(menu);

		} else {

			menuList.get(menuList.size() - 1).getMenuText()
					.add(menuTextHelper.assignResult(resultSet));

		}
	}

	public Menu assignResult(ResultSet resultSet) throws SQLException {

		Menu menu = new Menu();

		menu.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		menu.setCodMenu(resultSet.getInt(TABLE + "." + FIELD02));
		menu.setRecordMode(resultSet.getString(TABLE + "." + FIELD03));

		return menu;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMenu,
			List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt,
				prepareMenuWhere(codOwner, codMenu, recordMode));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codMenu,
			List<String> recordMode, List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(
				stmt,
				prepareMenuWhereWithText(codOwner, codMenu, recordMode,
						language, name));

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD02;

		return stmt;
	}

	public List<String> prepareMenuWhereWithText(List<Long> codOwner,
			List<Integer> codMenu, List<String> recordMode,
			List<String> language, List<String> name) {

		List<String> where = prepareMenuWhere(codOwner, codMenu, recordMode);

		where.addAll(menuTextHelper.prepareMenuTextWhere(null, null, language,
				name));

		return where;
	}

	public List<String> prepareMenuWhere(List<Long> codOwner,
			List<Integer> codMenu, List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMenu);
		assignFilterString(where, TABLE + "." + FIELD03, recordMode);

		return where;
	}

}
