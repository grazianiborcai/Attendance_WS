package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.gda.dao.helper.CountryHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.Country;

public class CountryDAO extends ConnectionBD {

	public ArrayList<Country> selectCountryText() throws SQLException {

		ArrayList<Country> countryTextList = new ArrayList<Country>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		ArrayList<PreparedStatement> stmt = new ArrayList<PreparedStatement>();

		try {

			conn = getConnection();

			CountryHelper countryHelper = new CountryHelper();

			selectStmt = conn.prepareStatement(countryHelper.prepareSelect());

			resultSet = selectStmt.executeQuery();
			
			stmt.add(selectStmt);

			while (resultSet.next()) {

				countryTextList.add(countryHelper.assignResult(resultSet));
			}

			return countryTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, stmt, resultSet);
		}
	}

}
