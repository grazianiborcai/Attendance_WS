package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.LanguageHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Language;

public class LanguageDAO extends ConnectionBD {

	public ArrayList<Language> selectLanguage(List<String> language,
			List<String> name) throws SQLException {

		ArrayList<Language> languageList = new ArrayList<Language>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			LanguageHelper languageHelper = new LanguageHelper();

			selectStmt = conn.prepareStatement(languageHelper.prepareSelect(
					language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				languageList.add(languageHelper.assignResult(resultSet));
			}

			return languageList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
