package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.CategoryTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.CategoryText;

public class CategoryTextDAO extends ConnectionBD {

	public ArrayList<CategoryText> selectCategoryText(
			List<Integer> codCategory, List<String> language, List<String> name)
			throws SQLException {

		ArrayList<CategoryText> categoryTextList = new ArrayList<CategoryText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			CategoryTextHelper categoryTextHelper = new CategoryTextHelper();

			selectStmt = conn.prepareStatement(categoryTextHelper
					.prepareSelect(codCategory, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				categoryTextList
						.add(categoryTextHelper.assignResult(resultSet));
			}

			return categoryTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
