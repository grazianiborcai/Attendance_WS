package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.gda.dao.helper.GenderHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Gender;

public class GenderDAO extends ConnectionBD {

	public ArrayList<Gender> selectGenderText() throws SQLException {

		ArrayList<Gender> GenderTextList = new ArrayList<Gender>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			GenderHelper genderHelper = new GenderHelper();

			selectStmt = conn.prepareStatement(genderHelper.prepareSelect());

			resultSet = selectStmt.executeQuery();
			
			stmt.add(selectStmt);

			while (resultSet.next()) {

				GenderTextList.add(genderHelper.assignResult(resultSet));
			}

			return GenderTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}
