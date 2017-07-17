package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.MenuItemText;

public class MenuItemTextHelper extends GdaDB {

	public static final String TABLE = "Menu_item_text";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MENU;
	public static final String FIELD03 = ITEM;
	public static final String FIELD04 = LANGUAGE;
	public static final String FIELD05 = NAME;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ", " + FIELD05 + ") " + "VALUES (?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD05 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD02 + "=? AND " + FIELD03 + "=? AND " + FIELD04
			+ "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public MenuItemText assignResult(ResultSet resultSet) throws SQLException {

		MenuItemText menuItemText = new MenuItemText();

		menuItemText.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		menuItemText.setCodMenu(resultSet.getInt(TABLE + "." + FIELD02));
		menuItemText.setItem(resultSet.getInt(TABLE + "." + FIELD03));
		menuItemText.setLanguage(resultSet.getString(TABLE + "." + FIELD04));
		menuItemText.setName(resultSet.getString(TABLE + "." + FIELD05));

		return menuItemText;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMenu,
			List<Integer> item, List<String> language, List<String> name) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareMenuItemTextWhere(codOwner, codMenu, item, language,
						name));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codMenu,
			List<Integer> item, List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(
				stmt,
				prepareMenuItemTextWhere(codOwner, codMenu, item, language,
						name));

		return stmt;
	}

	public List<String> prepareMenuItemTextWhere(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> language,
			List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, FIELD01, codOwner);
		assignFilterInt(where, FIELD02, codMenu);
		assignFilterInt(where, FIELD03, item);
		assignFilterString(where, FIELD04, language);
		assignFilterString(where, FIELD05, name);

		return where;
	}

}
