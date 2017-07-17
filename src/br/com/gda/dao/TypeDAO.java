package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.TypeHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Type;

public class TypeDAO extends ConnectionBD {

	public ArrayList<Type> selectType(List<Integer> codType,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Type> typeList = new ArrayList<Type>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			TypeHelper typeHelper = new TypeHelper();

			selectStmt = conn.prepareStatement(typeHelper.prepareSelect(
					codType, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				typeHelper.assignResult(typeList, resultSet);
			}

			return typeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
