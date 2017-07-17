package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.MaterialText;

public class MaterialTextHelper extends GdaDB {

	public static final String TABLE = "Material_text";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_MATERIAL;
	public static final String FIELD03 = LANGUAGE;
	public static final String FIELD04 = NAME;
	public static final String FIELD05 = DESCRIPTION;
	public static final String FIELD06 = TEXT_LONG;

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "."
			+ TABLE + " (" + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD = "UPDATE " + SCHEMA + "."
			+ TABLE + " SET " + FIELD04 + "=?" + ", " + FIELD05 + "=?" + ", "
			+ FIELD06 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02
			+ "=? AND " + FIELD03 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public MaterialText assignResult(ResultSet resultSet) throws SQLException {

		MaterialText materialText = new MaterialText();

		materialText.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
		materialText.setCodMaterial(resultSet.getInt(TABLE + "." + FIELD02));
		materialText.setLanguage(resultSet.getString(TABLE + "." + FIELD03));
		materialText.setName(resultSet.getString(TABLE + "." + FIELD04));
		materialText.setDescription(resultSet.getString(TABLE + "." + FIELD05));
		materialText.setTextLong(resultSet.getString(TABLE + "." + FIELD06));

		return materialText;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codMaterial,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(
				stmt,
				prepareMaterialTextWhere(codOwner, codMaterial, language,
						name, description, textLong));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codMaterial,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(
				stmt,
				prepareMaterialTextWhere(codOwner, codMaterial, language,
						name, description, textLong));

		return stmt;
	}

	public List<String> prepareMaterialTextWhere(List<Long> codOwner,
			List<Integer> codMaterial, List<String> language,
			List<String> name, List<String> description, List<String> textLong) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codMaterial);
		assignFilterString(where, TABLE + "." + FIELD03, language);
		assignFilterString(where, TABLE + "." + FIELD04, name);
		assignFilterString(where, TABLE + "." + FIELD05, description);
		assignFilterString(where, TABLE + "." + FIELD06, textLong);

		return where;
	}

}
