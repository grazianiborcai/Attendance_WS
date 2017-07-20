package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.gda.dao.helper.PeopleHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.People;

public class PeopleDAO extends ConnectionBD {

	public ArrayList<People> selectPeople(String email, String password) throws SQLException {

		ArrayList<People> PeopleTextList = new ArrayList<People>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			PeopleHelper peopleHelper = new PeopleHelper();

			selectStmt = conn.prepareStatement(peopleHelper.prepareSelect(email, password));

			resultSet = selectStmt.executeQuery();

			stmt.add(selectStmt);

			while (resultSet.next()) {

				PeopleTextList.add(peopleHelper.assignResult(resultSet));
			}

			return PeopleTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}
