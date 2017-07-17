package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.StateHelper;
import br.com.gda.db.ConnectionBD;
import br.com.mind5.helper.State;

public class StateDAO extends ConnectionBD {

	public ArrayList<State> selectState(List<String> country,
			List<String> state, List<String> language, List<String> name)
			throws SQLException {

		ArrayList<State> statelList = new ArrayList<State>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			StateHelper stateHelper = new StateHelper();

			selectStmt = conn.prepareStatement(stateHelper.prepareSelect(
					country, state, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				stateHelper.assignResult(statelList, resultSet);
			}

			return statelList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
