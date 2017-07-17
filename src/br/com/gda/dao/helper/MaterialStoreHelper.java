package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.MaterialStore;

public class MaterialStoreHelper extends GdaDB {

	public static final String TABLE = "Material_Store";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MATERIAL;
	public static final String FIELD03 = COD_STORE;
	public static final String FIELD04 = PRICE_STORE;
	public static final String FIELD05 = "Duration_store";
	public static final String FIELD06 = RECORD_MODE;
	public static final String FIELD07 = "Cod_curr_store";
	public static final String FIELD08 = WEEKDAY;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD04 + "=?" + ", " + FIELD05 + "=?"
			+ ", " + FIELD06 + "=?" + ", " + FIELD07 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02
			+ "=? AND " + FIELD03 + "=?";

	/************************** To attend MaterialDAO **************************/
	public static final String ST_UP_RECORD_MODE_BY_MATERIAL = "UPDATE "
			+ SCHEMA + "." + TABLE + " SET " + FIELD06 + "=?" + " WHERE "
			+ FIELD01 + "=? AND " + FIELD02 + "=?";
	/***************************************************************************/

	/************************** To attend StoreDAO *****************************/
	public static final String ST_UP_RECORD_MODE_BY_STORE = "UPDATE " + SCHEMA
			+ "." + TABLE + " SET " + FIELD06 + "=?" + " WHERE " + FIELD01
			+ "=? AND " + FIELD03 + "=?";
	/***************************************************************************/

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE + " LEFT JOIN " + SCHEMA + "." + MaterialHelper.TABLE
			+ " ON " + TABLE + "." + FIELD01 + " = " + MaterialHelper.TABLE
			+ "." + MaterialHelper.FIELD01 + " AND " + TABLE + "." + FIELD02
			+ " = " + MaterialHelper.TABLE + "." + MaterialHelper.FIELD02
			+ " LEFT JOIN " + SCHEMA + "." + MaterialTextHelper.TABLE + " ON "
			+ TABLE + "." + FIELD01 + " = " + MaterialTextHelper.TABLE + "."
			+ MaterialTextHelper.FIELD01 + " AND " + TABLE + "." + FIELD02
			+ " = " + MaterialTextHelper.TABLE + "."
			+ MaterialTextHelper.FIELD02;

	MaterialHelper materialHelper;
	MaterialTextHelper materialTextHelper;

	public MaterialStoreHelper() {
		materialHelper = new MaterialHelper();
		materialTextHelper = new MaterialTextHelper();
	}

	public MaterialStoreHelper(MaterialHelper materialHelper,
			MaterialTextHelper materialTextHelper) {
		this.materialHelper = materialHelper;
		this.materialTextHelper = materialTextHelper;
	}

	public void assignResult(ArrayList<MaterialStore> materialList,
			ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !materialList.get(materialList.size() - 1).getCodOwner()
						.equals(resultSet.getLong(TABLE + "." + FIELD01))
				|| !materialList.get(materialList.size() - 1).getCodStore()
						.equals(resultSet.getInt(TABLE + "." + FIELD03))
				|| !materialList.get(materialList.size() - 1).getCodMaterial()
						.equals(resultSet.getInt(TABLE + "." + FIELD02))) {

			MaterialStore material = assignResult(resultSet);

			material.getMaterialText().add(
					materialTextHelper.assignResult(resultSet));
			materialList.add(material);

		} else {

			materialList.get(materialList.size() - 1).getMaterialText()
					.add(materialTextHelper.assignResult(resultSet));
		}
	}

	private MaterialStore assignResult(ResultSet resultSet) throws SQLException {

		MaterialStore material = new MaterialStore(
				materialHelper.assignResult(resultSet));

		material.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		material.setCodStore(resultSet.getInt(TABLE + "." + FIELD03));
		material.setCodMaterial(resultSet.getInt(TABLE + "." + FIELD02));
		material.setPriceStore(resultSet.getBigDecimal(TABLE + "." + FIELD04));
		material.setDurationStore(resultSet.getInt(TABLE + "."
				+ FIELD05));
		material.setRecordMode(resultSet.getString(TABLE + "." + FIELD06));
		material.setCodCurrStore(resultSet.getString(TABLE + "." + FIELD07));

		return material;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codStore, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareMaterialStoreWhere(codOwner, codMaterial, codStore,
						recordMode));

		return stmt;
	}

	public String prepareDelete(String op01, Long codOwner, String op02,
			Integer codMaterial, String op03, Integer codStore) {

		String stmt = ST_DELETE;

		List<String> where = prepareMaterialStoreWhere(op01, codOwner, op02,
				codMaterial, op03, codStore);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public String prepareDeleteToStore(Long codOwner,
			List<Integer> codMaterial, Integer codStore) {

		String stmt = ST_DELETE;

		List<String> where = prepareMaterialStoreWhere(EQ, codOwner, null,
				null, EQ, codStore);

		where.addAll(prepareMaterialStoreWhere(null, codMaterial, null, null));

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codStore, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode,
			List<String> recordMode, List<String> language, List<String> name,
			List<String> description, List<String> textLong) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(
				stmt,
				prepareMaterialStoreWhereWithMaterialText(codOwner,
						codMaterial, codStore, codCategory, codType, image,
						barCode, recordMode, language, name, description,
						textLong));

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "."
				+ FIELD03 + ", " + TABLE + "." + FIELD02;

		return stmt;
	}

	private List<String> prepareMaterialStoreWhereWithMaterialText(
			List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codStore, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode,
			List<String> recordMode, List<String> language, List<String> name,
			List<String> description, List<String> textLong) {

		List<String> where = prepareMaterialStoreWhere(codOwner, codMaterial,
				codStore, recordMode);

		where.addAll(materialHelper.prepareMaterialWhere(null, null,
				codCategory, codType, image, barCode, null));

		where.addAll(materialTextHelper.prepareMaterialTextWhere(null, null,
				language, name, description, textLong));
		return where;
	}

	private List<String> prepareMaterialStoreWhere(List<Long> codOwner,
			List<Integer> codMaterial, List<Integer> codStore,
			List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMaterial);
		assignFilterInt(where, TABLE + "." + FIELD03, codStore);
		assignFilterString(where, TABLE + "." + FIELD06, recordMode);

		return where;
	}

	public List<String> prepareMaterialStoreWhere(String op01, Long codOwner,
			String op02, Integer codMaterial, String op03, Integer codStore) {

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, op01, codOwner);
		singleFilter(where, TABLE + "." + FIELD02, op02, codMaterial);
		singleFilter(where, TABLE + "." + FIELD03, op03, codStore);

		return where;
	}

}
