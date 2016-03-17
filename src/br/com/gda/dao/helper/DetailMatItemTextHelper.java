package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.DetailMatItemText;

public class DetailMatItemTextHelper extends GdaDB {

	public static final String TABLE = "Item_dt_mat_text";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_DETAIL;
	public static final String FIELD03 = COD_ITEM_DT;
	public static final String FIELD04 = LANGUAGE;
	public static final String FIELD05 = NAME;
	public static final String FIELD06 = DESCRIPTION;
	public static final String FIELD07 = TEXT_LONG;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD05 + "=?" + ", " + FIELD06 + "=?"
			+ ", " + FIELD07 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02
			+ "=? AND " + FIELD03 + "=? AND " + FIELD04 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public DetailMatItemText assignResult(ResultSet resultSet)
			throws SQLException {

		DetailMatItemText detailMatItemText = new DetailMatItemText();

		detailMatItemText.setCodOwner(resultSet.getLong(FIELD01));
		detailMatItemText.setCodDetail(resultSet.getInt(FIELD02));
		detailMatItemText.setCodItem(resultSet.getInt(FIELD03));
		detailMatItemText.setLanguage(resultSet.getString(FIELD04));
		detailMatItemText.setName(resultSet.getString(FIELD05));
		detailMatItemText.setDescription(resultSet.getString(FIELD06));
		detailMatItemText.setTextLong(resultSet.getString(FIELD07));

		return detailMatItemText;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codDetail,
			List<Integer> codItem, List<String> language, List<String> name,
			List<String> description, List<String> textLong) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareDetailMatItemTextWhere(codOwner, codDetail, codItem,
						language, name, description, textLong));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codDetail,
			List<Integer> codItem, List<String> language, List<String> name,
			List<String> description, List<String> textLong) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(
				stmt,
				prepareDetailMatItemTextWhere(codOwner, codDetail, codItem,
						language, name, description, textLong));

		return stmt;
	}

	public List<String> prepareDetailMatItemTextWhere(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, FIELD01, codOwner);
		assignFilterInt(where, FIELD02, codDetail);
		assignFilterInt(where, FIELD03, codItem);
		assignFilterString(where, FIELD04, language);
		assignFilterString(where, FIELD05, name);
		assignFilterString(where, FIELD06, description);
		assignFilterString(where, FIELD07, textLong);

		return where;
	}

}
