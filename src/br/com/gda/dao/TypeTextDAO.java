package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.TypeTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.TypeText;

public class TypeTextDAO extends ConnectionBD {

	public ArrayList<TypeText> selectTypeText(List<Integer> codType,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<TypeText> typeTextList = new ArrayList<TypeText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			TypeTextHelper typeTextHelper = new TypeTextHelper();

			selectStmt = conn.prepareStatement(typeTextHelper.prepareSelect(
					codType, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				typeTextList.add(typeTextHelper.assignResult(resultSet));
			}

			return typeTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
