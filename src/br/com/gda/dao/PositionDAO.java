package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.PositionHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Position;

public class PositionDAO extends ConnectionBD {

	public ArrayList<Position> selectPosition(List<Integer> codPosition,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Position> positionList = new ArrayList<Position>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			PositionHelper positionHelper = new PositionHelper();

			selectStmt = conn.prepareStatement(positionHelper.prepareSelect(
					codPosition, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				positionHelper.assignResult(positionList, resultSet);
			}

			return positionList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
