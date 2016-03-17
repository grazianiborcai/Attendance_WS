package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.CategoryHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.Category;

public class CategoryDAO extends ConnectionBD {

	public ArrayList<Category> selectCategory(List<Integer> codCategory,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Category> categoryList = new ArrayList<Category>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			CategoryHelper categoryHelper = new CategoryHelper();

			selectStmt = conn.prepareStatement(categoryHelper.prepareSelect(
					codCategory, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				categoryHelper.assignResult(categoryList, resultSet);

			}

			return categoryList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
