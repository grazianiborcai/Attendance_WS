package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.DetailMatText;

public class DetailMatTextHelper extends GdaDB {

	public static final String TABLE = "Detail_mat_text";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_DETAIL;
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

	public DetailMatText assignResult(ResultSet resultSet) throws SQLException {

		DetailMatText detailMatItemText = new DetailMatText();

		detailMatItemText.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		detailMatItemText.setCodDetail(resultSet.getInt(TABLE + "." + FIELD02));
		detailMatItemText.setLanguage(resultSet
				.getString(TABLE + "." + FIELD03));
		detailMatItemText.setName(resultSet.getString(TABLE + "." + FIELD04));

		return detailMatItemText;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codDetail,
			List<String> language, List<String> name) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt,
				prepareDetailMatTextWhere(codOwner, codDetail, language, name));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codDetail,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt,
				prepareDetailMatTextWhere(codOwner, codDetail, language, name));

		return stmt;
	}

	public List<String> prepareDetailMatTextWhere(List<Long> codOwner,
			List<Integer> codDetail, List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codDetail);
		assignFilterString(where, TABLE + "." + FIELD03, language);
		assignFilterString(where, TABLE + "." + FIELD04, name);

		return where;
	}

}
