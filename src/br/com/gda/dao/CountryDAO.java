package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.CountryHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.Country;

public class CountryDAO extends ConnectionBD {

	public ArrayList<Country> selectCountry(List<String> country,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Country> statelList = new ArrayList<Country>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			CountryHelper countryHelper = new CountryHelper();

			selectStmt = conn.prepareStatement(countryHelper.prepareSelect(
					country, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				countryHelper.assignResult(statelList, resultSet);
			}

			return statelList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
