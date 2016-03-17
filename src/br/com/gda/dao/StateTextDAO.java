package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.StateTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.StateText;

public class StateTextDAO extends ConnectionBD {

	public ArrayList<StateText> selectStateText(List<String> country,
			List<String> state, List<String> language, List<String> name)
			throws SQLException {

		ArrayList<StateText> stateTextList = new ArrayList<StateText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			StateTextHelper stateTextHelper = new StateTextHelper();

			selectStmt = conn.prepareStatement(stateTextHelper.prepareSelect(
					country, state, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				stateTextList.add(stateTextHelper.assignResult(resultSet));
			}

			return stateTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
