package br.com.gda.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.CardHelper;
import br.com.gda.dao.helper.CustomerHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.CreditCardAG;
import br.com.mind5.helper.Customer;
import br.com.mind5.helper.RecordMode;

public class CardDAO extends ConnectionBD {

	public SQLException insertCreditCard(ArrayList<CreditCardAG> creditCardAGList, Long codCustomer) {

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmt = conn.prepareStatement(CardHelper.ST_IN_ALL);

			for (CreditCardAG creditCardAG : creditCardAGList) {

				prepareInsert(insertStmt, creditCardAG, codCustomer);

				insertStmt.executeUpdate();
			}

			conn.commit();

			return new SQLException(INSERT_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, insertStmt, selectStmt, resultSet);
		}

	}

	public SQLException deleteCreditCard(Long codCustomer, String last4) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new CardHelper().prepareDelete(codCustomer, last4));

			deleteStmt.execute();

			conn.commit();

			return new SQLException(DELETE_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, deleteStmt);
		}
	}

	public ArrayList<CreditCardAG> selectCreditCard(Long codCustomer) throws SQLException {

		ArrayList<CreditCardAG> creditCardAGList = new ArrayList<CreditCardAG>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			CardHelper cardHelper = new CardHelper();

			String select = cardHelper.prepareSelect(codCustomer);

			// email.clear();
			// email.add(select);

			selectStmt = conn.prepareStatement(select);

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				creditCardAGList.add(cardHelper.assignResult(resultSet));
			}

			return creditCardAGList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	private void prepareInsert(PreparedStatement insertStmt, CreditCardAG creditCardAG, Long codCustomer)
			throws SQLException {

		insertStmt.setLong(1, codCustomer);
		insertStmt.setString(2, creditCardAG.getLast4());
		insertStmt.setString(3, creditCardAG.getHash());
		insertStmt.setString(4, creditCardAG.getBrand());
		insertStmt.setString(5, creditCardAG.getHolder().getFullname());
		insertStmt.setDate(6, Date.valueOf(creditCardAG.getHolder().getBirthDate()));
		insertStmt.setString(7, creditCardAG.getHolder().getTaxDocument().getType());
		insertStmt.setString(8, creditCardAG.getHolder().getTaxDocument().getNumber());
	}

}
