package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Category;

public class CategoryHelper extends GdaDB {

	public static final String TABLE = "Category";

	public static final String FIELD01 = COD_CATEGORY;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "."
			+ CategoryTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ CategoryTextHelper.TABLE + "." + CategoryTextHelper.FIELD01;

	CategoryTextHelper categoryTextHelper;

	public CategoryHelper() {
		categoryTextHelper = new CategoryTextHelper();
	}

	public CategoryHelper(CategoryTextHelper categoryTextHelper) {
		this.categoryTextHelper = categoryTextHelper;
	}

	public void assignResult(ArrayList<Category> categoryList,
			ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !categoryList.get(categoryList.size() - 1).getCodCategory()
						.equals(resultSet.getInt(TABLE + "." + FIELD01))) {

			Category category = assignResult(resultSet);

			category.getCategoryText().add(
					categoryTextHelper.assignResult(resultSet));
			categoryList.add(category);

		} else {

			categoryList.get(categoryList.size() - 1).getCategoryText()
					.add(categoryTextHelper.assignResult(resultSet));
		}
	}

	public Category assignResult(ResultSet resultSet) throws SQLException {

		Category category = new Category();

		category.setCodCategory(resultSet.getInt(TABLE + "." + FIELD01));

		return category;
	}

	public String prepareSelect(List<Integer> codCategory,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = prepareCategoryWhereWithText(codCategory,
				language, name);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public List<String> prepareCategoryWhere(List<Integer> codCategory) {

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, TABLE + "." + FIELD01, codCategory);

		return where;
	}

	public List<String> prepareCategoryWhereWithText(List<Integer> codCategory,
			List<String> language, List<String> name) {

		List<String> where = prepareCategoryWhere(codCategory);

		where.addAll(categoryTextHelper.prepareCategoryTextWhere(null,
				language, name));

		return where;
	}

}
