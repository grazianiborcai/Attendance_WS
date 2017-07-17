package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.CategoryText;

public class CategoryTextHelper extends GdaDB {

	public static final String TABLE = "Category_text";

	public static final String FIELD01 = COD_CATEGORY;
	public static final String FIELD02 = LANGUAGE;
	public static final String FIELD03 = NAME;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public CategoryText assignResult(ResultSet resultSet) throws SQLException {

		CategoryText categoryText = new CategoryText();

		categoryText.setCodCategory(resultSet.getInt(FIELD01));
		categoryText.setLanguage(resultSet.getString(FIELD02));
		categoryText.setName(resultSet.getString(FIELD03));

		return categoryText;
	}

	public String prepareSelect(List<Integer> codCategory,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt,
				prepareCategoryTextWhere(codCategory, language, name));

		return stmt;
	}

	public List<String> prepareCategoryTextWhere(List<Integer> codCategory,
			List<String> language, List<String> name) {

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, FIELD01, codCategory);
		assignFilterString(where, FIELD02, language);
		assignFilterString(where, FIELD03, name);

		return where;
	}

}
