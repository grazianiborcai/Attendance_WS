package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.GenderHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.Gender;

public class GenderDAO extends ConnectionBD {

	public ArrayList<Gender> selectGender(List<Integer> codGender,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Gender> genderlList = new ArrayList<Gender>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			GenderHelper genderHelper = new GenderHelper();

			selectStmt = conn.prepareStatement(genderHelper.prepareSelect(
					codGender, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				genderHelper.assignResult(genderlList, resultSet);
			}

			return genderlList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
