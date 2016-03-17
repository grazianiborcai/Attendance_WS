package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.WeekdayTextHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.WeekdayText;

public class WeekdayTextDAO extends ConnectionBD {

	public ArrayList<WeekdayText> selectWeekdayText(List<Integer> weekday,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<WeekdayText> weekdayTextList = new ArrayList<WeekdayText>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			WeekdayTextHelper weekdayTextHelper = new WeekdayTextHelper();

			selectStmt = conn.prepareStatement(weekdayTextHelper.prepareSelect(
					weekday, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				weekdayTextList.add(weekdayTextHelper.assignResult(resultSet));
			}

			return weekdayTextList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
