package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.gda.dao.helper.GradeHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Grade;

public class GradeDAO extends ConnectionBD {

	public ArrayList<Grade> selectGradeText() throws SQLException {

		ArrayList<Grade> GradeTextList = new ArrayList<Grade>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			GradeHelper gradeHelper = new GradeHelper();

			selectStmt = conn.prepareStatement(gradeHelper.prepareSelect());

			resultSet = selectStmt.executeQuery();

			stmt.add(selectStmt);

			while (resultSet.next()) {

				GradeTextList.add(gradeHelper.assignResult(resultSet));
			}

			return GradeTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}
