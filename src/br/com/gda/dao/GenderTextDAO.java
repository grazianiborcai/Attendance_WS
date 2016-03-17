package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.GenderTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.GenderText;

public class GenderTextDAO extends ConnectionBD {

	public ArrayList<GenderText> selectGenderText(List<Integer> codGender,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<GenderText> genderTextList = new ArrayList<GenderText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			GenderTextHelper genderTextHelper = new GenderTextHelper();

			selectStmt = conn.prepareStatement(genderTextHelper.prepareSelect(
					codGender, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				genderTextList.add(genderTextHelper.assignResult(resultSet));
			}

			return genderTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
