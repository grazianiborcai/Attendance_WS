package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.DetailMat;

public class DetailMatHelper extends GdaDB {

	public static final String TABLE = "Detail_mat";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_DETAIL;
	public static final String FIELD03 = QUANTITY;
	public static final String FIELD04 = RECORD_MODE;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD03 + ", " + FIELD04 + ") "
			+ "VALUES (?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD03 + "=?" + ", " + FIELD04 + "=?"
			+ " WHERE " + FIELD01 + "=? AND " + FIELD02 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE + " LEFT JOIN " + SCHEMA + "." + DetailMatTextHelper.TABLE
			+ " ON " + TABLE + "." + FIELD01 + " = "
			+ DetailMatTextHelper.TABLE + "." + DetailMatTextHelper.FIELD01
			+ " AND " + TABLE + "." + FIELD02 + " = "
			+ DetailMatTextHelper.TABLE + "." + DetailMatTextHelper.FIELD02;

	public static final String LAST_ID_DETAIL_MAT = "@last_id_detail_mat";

	public static final String VARIABLE = SET + LAST_ID_DETAIL_MAT + EQ
			+ LAST_INSERT_ID;

	DetailMatTextHelper detailMatTextHelper;

	public DetailMatHelper() {
		detailMatTextHelper = new DetailMatTextHelper();
	}

	public DetailMatHelper(DetailMatTextHelper detailMatTextHelper) {
		this.detailMatTextHelper = detailMatTextHelper;
	}

	public void assignResult(ArrayList<DetailMat> detailMatList,
			ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !detailMatList.get(detailMatList.size() - 1).getCodOwner()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !detailMatList.get(detailMatList.size() - 1).getCodDetail()
						.equals(resultSet.getInt(TABLE + "." + FIELD02))) {

			DetailMat detailMat = assignResult(resultSet);

			detailMat.getDetailText().add(
					detailMatTextHelper.assignResult(resultSet));
			detailMatList.add(detailMat);

		} else {

			detailMatList.get(detailMatList.size() - 1).getDetailText()
					.add(detailMatTextHelper.assignResult(resultSet));
		}
	}

	public DetailMat assignResult(ResultSet resultSet) throws SQLException {

		DetailMat detailMat = new DetailMat();

		detailMat.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		detailMat.setCodDetail(resultSet.getInt(TABLE + "." + FIELD02));
		detailMat.setQuantity(resultSet.getFloat(TABLE + "." + FIELD03));
		detailMat.setRecordMode(resultSet.getString(TABLE + "." + FIELD04));

		return detailMat;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codDetail,
			List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt,
				prepareDetailMatWhere(codOwner, codDetail, recordMode));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codDetail,
			List<String> recordMode, List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		List<String> where = prepareDetailMatWithTextWhere(codOwner, codDetail,
				recordMode, language, name);

		stmt = prepareWhereClause(stmt, where);

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD02;

		return stmt;
	}

	private List<String> prepareDetailMatWithTextWhere(List<Long> codOwner,
			List<Integer> codDetail, List<String> recordMode,
			List<String> language, List<String> name) {

		List<String> where = prepareDetailMatWhere(codOwner, codDetail,
				recordMode);

		where.addAll(detailMatTextHelper.prepareDetailMatTextWhere(null, null,
				language, name));

		return where;
	}

	private List<String> prepareDetailMatWhere(List<Long> codOwner,
			List<Integer> codDetail, List<String> recordMode) {
		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codDetail);
		assignFilterString(where, TABLE + "." + FIELD04, recordMode);

		return where;
	}

}
