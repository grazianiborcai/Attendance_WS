package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.Material;
import br.com.gda.helper.MaterialDetail;

public class MaterialDetailHelper extends GdaDB {

	public static final String TABLE = "Material_Detail";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MATERIAL;
	public static final String FIELD03 = COD_DETAIL;
	public static final String FIELD04 = QTD_MAT;
	public static final String FIELD05 = RECORD_MODE;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ", " + FIELD05 + ") " + "VALUES (?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD04 + "=?" + ", " + FIELD05 + "=?"
			+ " WHERE " + FIELD01 + "=? AND " + FIELD02 + "=? AND " + FIELD03
			+ "=?";

	/************************** To attend MaterialDao **************************/

	public static final String ST_UP_ALL_FIELD_BY_MATERIAL = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD05 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD02 + "=?";

	/**************************************************************************/

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT_WITH_DETAIL_AND_TEXT = "SELECT * FROM "
			+ SCHEMA
			+ "."
			+ TABLE
			+ " LEFT JOIN "
			+ SCHEMA
			+ "."
			+ DetailMatHelper.TABLE
			+ " ON "
			+ TABLE
			+ "."
			+ FIELD01
			+ " = "
			+ DetailMatHelper.TABLE
			+ "."
			+ DetailMatHelper.FIELD01
			+ " AND "
			+ TABLE
			+ "."
			+ FIELD03
			+ " = "
			+ DetailMatHelper.TABLE
			+ "."
			+ DetailMatHelper.FIELD02
			+ " LEFT JOIN "
			+ SCHEMA
			+ "."
			+ DetailMatTextHelper.TABLE
			+ " ON "
			+ DetailMatHelper.TABLE
			+ "."
			+ DetailMatHelper.FIELD01
			+ " = "
			+ DetailMatTextHelper.TABLE
			+ "."
			+ DetailMatTextHelper.FIELD01
			+ " AND "
			+ DetailMatHelper.TABLE
			+ "."
			+ DetailMatHelper.FIELD02
			+ " = "
			+ DetailMatTextHelper.TABLE
			+ "." + DetailMatTextHelper.FIELD02;

	private static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	DetailMatHelper detailMatHelper;
	DetailMatTextHelper detailMatTextHelper;

	public MaterialDetailHelper() {
		detailMatHelper = new DetailMatHelper();
		detailMatTextHelper = new DetailMatTextHelper();
	}

	public MaterialDetailHelper(DetailMatHelper detailMatHelper,
			DetailMatTextHelper materialTextHelper) {
		this.detailMatHelper = detailMatHelper;
		this.detailMatTextHelper = materialTextHelper;
	}

	public void assignResultWithDetailAndText(
			ArrayList<MaterialDetail> detailMatList, ResultSet resultSet)
			throws SQLException {

		if (resultSet.isFirst()
				|| !detailMatList.get(detailMatList.size() - 1).getCodOwner()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !detailMatList.get(detailMatList.size() - 1)
						.getCodMaterial()
						.equals(resultSet.getInt(TABLE + "." + FIELD02))
				|| !detailMatList.get(detailMatList.size() - 1).getCodDetail()
						.equals(resultSet.getInt(TABLE + "." + FIELD03))) {

			MaterialDetail detailMat = assignResultWithDetail(resultSet);

			detailMat.getDetailText().add(
					detailMatTextHelper.assignResult(resultSet));
			detailMatList.add(detailMat);

		} else {

			detailMatList.get(detailMatList.size() - 1).getDetailText()
					.add(detailMatTextHelper.assignResult(resultSet));
		}
	}

	public MaterialDetail assignResultWithDetail(ResultSet resultSet)
			throws SQLException {

		MaterialDetail detailMat = assignResult(resultSet);

		detailMat.setQuantity(detailMatHelper.assignResult(resultSet)
				.getQuantity());

		return detailMat;
	}

	public MaterialDetail assignResult(ResultSet resultSet) throws SQLException {

		MaterialDetail detailMat = new MaterialDetail();

		detailMat.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		detailMat.setCodMaterial(resultSet.getInt(TABLE + "." + FIELD02));
		detailMat.setCodDetail(resultSet.getInt(TABLE + "." + FIELD03));
		detailMat.setQuantityMat(resultSet.getFloat(TABLE + "." + FIELD04));
		detailMat.setRecordMode(resultSet.getString(TABLE + "." + FIELD05));

		return detailMat;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codDetail, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareMaterialDetailWhere(codOwner, codMaterial, codDetail,
						recordMode));

		return stmt;
	}

	public String prepareDelete(Long codOwner, Integer codMaterial,
			Integer codDetail) {

		String stmt = ST_DELETE;

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, GdaDB.EQ, codOwner);
		singleFilter(where, TABLE + "." + FIELD02, GdaDB.EQ, codMaterial);
		singleFilter(where, TABLE + "." + FIELD03, GdaDB.EQ, codDetail);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public String prepareDeleteToMaterial(Material material,
			List<Integer> codDetail) {

		String stmt = ST_DELETE;

		List<String> where = new ArrayList<String>();
		singleFilter(where, TABLE + "." + FIELD01, GdaDB.EQ,
				material.getCodOwner());
		singleFilter(where, TABLE + "." + FIELD02, GdaDB.EQ,
				material.getCodMaterial());
		assignFilterInt(where, TABLE + "." + FIELD03, codDetail);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codDetail, List<String> recordMode,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT_WITH_DETAIL_AND_TEXT;

		List<String> where = prepareMaterialDetailWhere(codOwner, codMaterial,
				codDetail, recordMode);

		where.addAll(detailMatTextHelper.prepareDetailMatTextWhere(null, null,
				language, name));

		stmt = prepareWhereClause(stmt, where);

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD02 + ", " + TABLE + "." + FIELD03;

		return stmt;
	}

	public String prepareSelectToMaterialOrDetail(Long owner,
			List<Integer> codMaterial, List<Integer> codDetail) {

		String stmt = ST_SELECT;

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, GdaDB.EQ, owner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMaterial);
		assignFilterInt(where, TABLE + "." + FIELD03, codDetail);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	private List<String> prepareMaterialDetailWhere(List<Long> codOwner,
			List<Integer> codMaterial, List<Integer> codDetail,
			List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMaterial);
		assignFilterInt(where, TABLE + "." + FIELD03, codDetail);
		assignFilterString(where, TABLE + "." + FIELD05, recordMode);

		return where;
	}

}
