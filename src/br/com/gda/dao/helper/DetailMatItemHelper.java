package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.DetailMatItem;

public class DetailMatItemHelper extends GdaDB {

	public static final String TABLE = "Item_dt_mat";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_DETAIL;
	public static final String FIELD03 = COD_ITEM_DT;
	public static final String FIELD04 = RECORD_MODE;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD04 + ") "
			+ "VALUES (?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD04 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD02 + "=? AND " + FIELD03 + "=?";

	/************************** To attend DetailMatDao **************************/
	public static final String ST_UP_ALL_FIELD_BY_DETAIL = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD04 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD02 + "=?";
	/****************************************************************************/

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE + " LEFT JOIN " + SCHEMA + "."
			+ DetailMatItemTextHelper.TABLE + " ON " + TABLE + "." + FIELD01
			+ " = " + DetailMatItemTextHelper.TABLE + "."
			+ DetailMatItemTextHelper.FIELD01 + " AND " + TABLE + "." + FIELD02
			+ " = " + DetailMatItemTextHelper.TABLE + "."
			+ DetailMatItemTextHelper.FIELD02 + " AND " + TABLE + "." + FIELD03
			+ " = " + DetailMatItemTextHelper.TABLE + "."
			+ DetailMatItemTextHelper.FIELD03;

	DetailMatItemTextHelper detailMatItemTextHelper;

	public DetailMatItemHelper() {
		detailMatItemTextHelper = new DetailMatItemTextHelper();
	}

	public DetailMatItemHelper(DetailMatItemTextHelper detailMatItemTextHelper) {
		this.detailMatItemTextHelper = detailMatItemTextHelper;
	}

	public void assignResult(ArrayList<DetailMatItem> detailMatItemList,
			ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !detailMatItemList.get(detailMatItemList.size() - 1)
						.getCodOwner()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !detailMatItemList.get(detailMatItemList.size() - 1)
						.getCodDetail()
						.equals(resultSet.getInt(TABLE + "." + FIELD02))
				|| !detailMatItemList.get(detailMatItemList.size() - 1)
						.getCodItem()
						.equals(resultSet.getInt(TABLE + "." + FIELD03))) {

			DetailMatItem detailMatItem = assignResult(resultSet);

			detailMatItem.getDetailItemText().add(
					detailMatItemTextHelper.assignResult(resultSet));
			detailMatItemList.add(detailMatItem);

		} else {

			detailMatItemList.get(detailMatItemList.size() - 1)
					.getDetailItemText()
					.add(detailMatItemTextHelper.assignResult(resultSet));
		}
	}

	public DetailMatItem assignResult(ResultSet resultSet) throws SQLException {

		DetailMatItem detailMatItem = new DetailMatItem();

		detailMatItem.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		detailMatItem.setCodDetail(resultSet.getInt(TABLE + "." + FIELD02));
		detailMatItem.setCodItem(resultSet.getInt(TABLE + "." + FIELD03));
		detailMatItem.setRecordMode(resultSet.getString(TABLE + "." + FIELD04));

		return detailMatItem;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codDetail,
			List<Integer> codItem, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareDetailMatItemWhere(codOwner, codDetail, codItem,
						recordMode));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codDetail,
			List<Integer> codItem, List<String> recordMode,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(
				stmt,
				prepareDetailMatItemWithTextWhere(codOwner, codDetail, codItem,
						recordMode, language, name, description, textLong));

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD02 + ", " + TABLE + "." + FIELD03;

		return stmt;
	}

	private List<String> prepareDetailMatItemWithTextWhere(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> recordMode, List<String> language, List<String> name,
			List<String> description, List<String> textLong) {

		List<String> where = prepareDetailMatItemWhere(codOwner, codDetail,
				codItem, recordMode);

		where.addAll(detailMatItemTextHelper.prepareDetailMatItemTextWhere(
				null, null, null, language, name, description, textLong));

		return where;
	}

	private List<String> prepareDetailMatItemWhere(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codDetail);
		assignFilterInt(where, TABLE + "." + FIELD03, codItem);
		assignFilterString(where, TABLE + "." + FIELD04, recordMode);

		return where;
	}

}
