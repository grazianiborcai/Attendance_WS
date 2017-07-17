package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.MenuText;

public class MenuTextHelper extends GdaDB {

	public static final String TABLE = "Menu_head_text";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MENU;
	public static final String FIELD03 = LANGUAGE;
	public static final String FIELD04 = NAME;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ") " + "VALUES (?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD04 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD02 + "=? AND " + FIELD03 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public MenuText assignResult(ResultSet resultSet) throws SQLException {

		MenuText menuText = new MenuText();

		menuText.setCodOwner(resultSet.getLong(FIELD01));
		menuText.setCodMenu(resultSet.getInt(FIELD02));
		menuText.setLanguage(resultSet.getString(FIELD03));
		menuText.setName(resultSet.getString(FIELD04));

		return menuText;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMenu,
			List<String> language, List<String> name) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt,
				prepareMenuTextWhere(codOwner, codMenu, language, name));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codMenu,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt,
				prepareMenuTextWhere(codOwner, codMenu, language, name));

		return stmt;
	}

	public List<String> prepareMenuTextWhere(List<Long> codOwner,
			List<Integer> codMenu, List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, FIELD01, codOwner);
		assignFilterInt(where, FIELD02, codMenu);
		assignFilterString(where, FIELD03, language);
		assignFilterString(where, FIELD04, name);

		return where;
	}

}
