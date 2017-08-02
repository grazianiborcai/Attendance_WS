package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import br.com.gda.dao.helper.PeopleHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.People;

public class PeopleDAO extends ConnectionBD {

	public ArrayList<People> selectPeople(String email, String password, String oAuth) throws SQLException {

		ArrayList<People> PeopleTextList = new ArrayList<People>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			PeopleHelper peopleHelper = new PeopleHelper();

			selectStmt = conn.prepareStatement(peopleHelper.prepareSelect(email, password, oAuth));

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
	
	public SQLException updatePeople(ArrayList<People> PeopleList) {

		Connection conn = null;
		PreparedStatement updateStmtT01 = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			updateStmtT01 = conn
					.prepareStatement(PeopleHelper.ST_UP_OAUTH_BY_FULL_KEY);

			for (People people : PeopleList) {

					updateStmtT01.setString(1, people.getoAuth());
					updateStmtT01.setTimestamp(2, Timestamp.valueOf(people.getoAuthDate()));
					updateStmtT01.setString(3, people.getUserAgent());
					updateStmtT01.setLong(4, people.getPeopleID());
					
					updateStmtT01.executeUpdate();
			}
			
			stmt.add(updateStmtT01);

			conn.commit();

			return new SQLException(UPDATE_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, stmt, null);
		}
	}

}
