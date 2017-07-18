package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.gda.dao.helper.HowDiscoveredHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.HowDiscovered;

public class HowDiscoveredDAO extends ConnectionBD {

	public ArrayList<HowDiscovered> selectHowDiscoveredText() throws SQLException {

		ArrayList<HowDiscovered> HowDiscoveredTextList = new ArrayList<HowDiscovered>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			HowDiscoveredHelper howDiscoveredHelper = new HowDiscoveredHelper();

			selectStmt = conn.prepareStatement(howDiscoveredHelper.prepareSelect());

			resultSet = selectStmt.executeQuery();
			
			stmt.add(selectStmt);

			while (resultSet.next()) {

				HowDiscoveredTextList.add(howDiscoveredHelper.assignResult(resultSet));
			}

			return HowDiscoveredTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}
