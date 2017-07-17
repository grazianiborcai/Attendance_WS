package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Material;
import br.com.mind5.helper.RecordMode;

public class MaterialHelper extends GdaDB {

	public static final String TABLE = "Material";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MATERIAL;
	public static final String FIELD03 = PRICE;
	public static final String FIELD04 = COD_CATEGORY;
	public static final String FIELD05 = COD_TYPE;
	public static final String FIELD06 = IMAGE;
	public static final String FIELD07 = DURATION;
	public static final String FIELD08 = BARCODE;
	public static final String FIELD09 = RECORD_MODE;
	public static final String FIELD10 = COD_CURR;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD01 + ", " + FIELD03
			+ ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", " + FIELD09 + ", "
			+ FIELD10 + ") " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD03 + "=?"
			+ ", " + FIELD04 + "=?" + ", " + FIELD05 + "=?" + ", " + FIELD06 + "=?" + ", " + FIELD07 + "=?" + ", "
			+ FIELD08 + "=?" + ", " + FIELD09 + "=?" + ", " + FIELD10 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02
			+ "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA + "." + TABLE + " LEFT JOIN " + SCHEMA
			+ "." + MaterialTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = " + MaterialTextHelper.TABLE + "."
			+ MaterialTextHelper.FIELD01 + " AND " + TABLE + "." + FIELD02 + " = " + MaterialTextHelper.TABLE + "."
			+ MaterialTextHelper.FIELD02;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT_LAST_INSERT_ID = "SELECT " + LAST_INSERT_ID;

	MaterialTextHelper materialTextHelper;

	public MaterialHelper() {
		materialTextHelper = new MaterialTextHelper();
	}

	public MaterialHelper(MaterialTextHelper materialTextHelper) {
		this.materialTextHelper = materialTextHelper;
	}

	public void assignResultWithText(ArrayList<Material> materialList, ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !materialList.get(materialList.size() - 1).getCodOwner()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !materialList.get(materialList.size() - 1).getCodMaterial()
						.equals(resultSet.getInt(TABLE + "." + FIELD02))) {

			Material material = assignResult(resultSet);

			material.getMaterialText().add(materialTextHelper.assignResult(resultSet));
			materialList.add(material);

		} else {

			materialList.get(materialList.size() - 1).getMaterialText().add(materialTextHelper.assignResult(resultSet));

		}
	}

	public Material assignResult(ResultSet resultSet) throws SQLException {

		Material material = new Material();

		material.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		material.setCodMaterial(resultSet.getInt(TABLE + "." + FIELD02));
		material.setPrice(resultSet.getBigDecimal(TABLE + "." + FIELD03));
		material.setCodCategory(resultSet.getInt(TABLE + "." + FIELD04));
		material.setCodType(resultSet.getInt(TABLE + "." + FIELD05));
		material.setImage(resultSet.getString(TABLE + "." + FIELD06));
		material.setDuration(resultSet.getInt(TABLE + "." + FIELD07));
		material.setBarCode(resultSet.getString(TABLE + "." + FIELD08));
		material.setRecordMode(resultSet.getString(TABLE + "." + FIELD09));
		material.setCodCurr(resultSet.getString(TABLE + "." + FIELD10));

		return material;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt,
				prepareMaterialWhere(codOwner, codMaterial, codCategory, codType, image, barCode, recordMode));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode,
			List<String> language, List<String> name, List<String> description, List<String> textLong) {

		String stmt = ST_SELECT_WITH_TEXT;

		stmt = prepareWhereClause(stmt, prepareMaterialWithTextWhere(codOwner, codMaterial, codCategory, codType, image,
				barCode, recordMode, language, name, description, textLong));

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02;

		return stmt;
	}

	public String prepareSelectToMaterialDetail(Long owner, List<Integer> codMaterial) {

		String stmtSelect = ST_SELECT;

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, GdaDB.EQ, owner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMaterial);
		singleFilter(where, TABLE + "." + FIELD05, GdaDB.EQ, RecordMode._2);

		stmtSelect = prepareWhereClause(stmtSelect, where);

		return stmtSelect;
	}

	private List<String> prepareMaterialWithTextWhere(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codCategory, List<Integer> codType, List<String> image, List<String> barCode,
			List<String> recordMode, List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		List<String> where = prepareMaterialWhere(codOwner, codMaterial, codCategory, codType, image, barCode,
				recordMode);

		where.addAll(materialTextHelper.prepareMaterialTextWhere(null, null, language, name, description, textLong));

		return where;
	}

	public List<String> prepareMaterialWhere(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMaterial);
		assignFilterInt(where, TABLE + "." + FIELD04, codCategory);
		assignFilterInt(where, TABLE + "." + FIELD05, codType);
		assignFilterString(where, TABLE + "." + FIELD06, image);
		assignFilterString(where, TABLE + "." + FIELD08, barCode);
		assignFilterString(where, TABLE + "." + FIELD09, recordMode);

		return where;
	}

}
