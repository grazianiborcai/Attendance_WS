package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.gda.dao.helper.ClassHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Class;

public class ClassDAO extends ConnectionBD {

	public ArrayList<Class> selectClassText(Long peopleID) throws SQLException {

		ArrayList<Class> ClassTextList = new ArrayList<Class>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			ClassHelper classHelper = new ClassHelper();

			selectStmt = conn.prepareStatement(classHelper.prepareSelect(peopleID));

			resultSet = selectStmt.executeQuery();

			stmt.add(selectStmt);

			while (resultSet.next()) {

				classHelper.assignResult(ClassTextList, resultSet);
			}

			return ClassTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}
