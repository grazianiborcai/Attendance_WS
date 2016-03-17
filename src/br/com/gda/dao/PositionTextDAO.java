package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.PositionTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.PositionText;

public class PositionTextDAO extends ConnectionBD {

	public ArrayList<PositionText> selectPositionText(
			List<Integer> codPosition, List<String> language, List<String> name)
			throws SQLException {

		ArrayList<PositionText> positionTextList = new ArrayList<PositionText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			PositionTextHelper positionTextHelper = new PositionTextHelper();

			selectStmt = conn.prepareStatement(positionTextHelper
					.prepareSelect(codPosition, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				positionTextList
						.add(positionTextHelper.assignResult(resultSet));
			}

			return positionTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
