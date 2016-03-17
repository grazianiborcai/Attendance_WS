package br.com.gda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.dao.helper.WeekdayHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.Weekday;

public class WeekdayDAO extends ConnectionBD {

	public ArrayList<Weekday> selectWeekday(List<Integer> weekday,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Weekday> weekdayList = new ArrayList<Weekday>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			WeekdayHelper weekdayHelper = new WeekdayHelper();

			selectStmt = conn.prepareStatement(weekdayHelper.prepareSelect(
					weekday, language, name));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				weekdayHelper.assignResult(weekdayList, resultSet);
			}

			return weekdayList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

}
