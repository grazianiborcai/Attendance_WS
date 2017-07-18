package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.gda.dao.helper.LookingForHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.LookingFor;

public class LookingForDAO extends ConnectionBD {

	public ArrayList<LookingFor> selectLookingForText() throws SQLException {

		ArrayList<LookingFor> LookingForTextList = new ArrayList<LookingFor>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			LookingForHelper lookingForHelper = new LookingForHelper();

			selectStmt = conn.prepareStatement(lookingForHelper.prepareSelect());

			resultSet = selectStmt.executeQuery();
			
			stmt.add(selectStmt);

			while (resultSet.next()) {

				LookingForTextList.add(lookingForHelper.assignResult(resultSet));
			}

			return LookingForTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}
