package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Type;

public class TypeHelper extends GdaDB {

	public static final String TABLE = "Type";

	public static final String FIELD01 = COD_TYPE;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "." + TypeTextHelper.TABLE
			+ " ON " + TABLE + "." + FIELD01 + " = " + TypeTextHelper.TABLE
			+ "." + TypeTextHelper.FIELD01;

	TypeTextHelper typeTextHelper;

	public TypeHelper() {
		typeTextHelper = new TypeTextHelper();
	}

	public TypeHelper(TypeTextHelper typeTextHelper) {
		this.typeTextHelper = typeTextHelper;
	}

	public void assignResult(ArrayList<Type> typeList, ResultSet resultSet)
			throws SQLException {

		if (resultSet.isFirst()
				|| !typeList.get(typeList.size() - 1).getCodType()
						.equals(resultSet.getInt(TABLE + "." + FIELD01))) {

			Type type = new Type();

			type.setCodType(resultSet.getInt(TABLE + "." + FIELD01));

			type.getTypeText().add(typeTextHelper.assignResult(resultSet));
			typeList.add(type);

		} else {

			typeList.get(typeList.size() - 1).getTypeText()
					.add(typeTextHelper.assignResult(resultSet));
		}
	}

	public String prepareSelect(List<Integer> codType, List<String> language,
			List<String> name) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, TABLE + "." + FIELD01, codType);

		typeTextHelper.prepareSelect(null, language, name);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

}
