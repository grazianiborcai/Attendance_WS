package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.dao.helper.CurrencyHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.CurrencyBase;

public class CurrencyDAO extends ConnectionBD {

	public CurrencyBase selectCurrency(String baseCode, String language) throws SQLException {

		CurrencyBase currencyBase = new CurrencyBase();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			CurrencyHelper currencyHelper = new CurrencyHelper();

			selectStmt = conn.prepareStatement(currencyHelper.prepareSelect(language));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				currencyHelper.assignResult(currencyBase, baseCode, resultSet);

			}

			if (currencyBase.getBaseCode() == null)
				throw new SQLException();

			return currencyBase;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
