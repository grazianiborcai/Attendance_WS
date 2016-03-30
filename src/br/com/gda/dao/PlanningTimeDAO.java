package br.com.gda.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import br.com.gda.dao.helper.PlanningTimeHelper;
import br.com.gda.dao.helper.ReserveHelper;
import br.com.gda.db.ConnectionBD;
import br.com.gda.helper.PlanningTime;
import br.com.gda.helper.RecordMode;
import br.com.gda.helper.Reserve;

public class PlanningTimeDAO extends ConnectionBD {

	private static final int _10 = 10;

	public SQLException insertPlanningTime(ArrayList<PlanningTime> planningTimeList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(PlanningTimeHelper.ST_IN_ALL_FIELD);

			for (PlanningTime planningTime : planningTimeList) {

				preparePlanningTimeInsert(insertStmtT01, planningTime);

				insertStmtT01.executeUpdate();
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
			closeConnection(conn, insertStmtT01);
		}

	}

	public SQLException reservePlanningTime(ArrayList<PlanningTime> planningTimeList, Long codCustomer,
			boolean error24) {

		Connection conn = null;
		PreparedStatement updateStmtT01 = null;
		PreparedStatement updateStmtT02 = null;
		ConcurrentHashMap<Integer, Integer> timeMap = new ConcurrentHashMap<Integer, Integer>();

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			updateStmtT01 = conn.prepareStatement(PlanningTimeHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);
			// updateStmtT02 =
			// conn.prepareStatement(PlanningTimeHelper.ST_UP_ALL_FIELD_BY_FULL_KEY);

			LocalDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
			LocalDateTime dateTimePlus = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime().plusMinutes(_10);

			int z = 0;
			int x = 0;
			for (PlanningTime planningTime : planningTimeList) {

				for (int i = 0; i < planningTime.getRate(); i++) {
					int part = planningTime.getPart();
					int min = part * i;
					LocalTime beginTime = planningTime.getBeginTime().plusMinutes(min);

					timeMap.put(x, z);
					// if (i == 0) {
					updateStmtT01.setTime(1, Time.valueOf(planningTime.getBeginTime()));
					updateStmtT01.setInt(2, planningTime.getWeekday());
					updateStmtT01.setInt(3, planningTime.getCodMaterial());
					updateStmtT01.setString(4, RecordMode.ISRESERVED);
					updateStmtT01.setBigDecimal(5, planningTime.getPriceStore());
					updateStmtT01.setString(6, planningTime.getCodCurrStore());
					updateStmtT01.setTimestamp(7, Timestamp.valueOf(dateTimePlus));
					updateStmtT01.setLong(8, codCustomer);

					updateStmtT01.setLong(9, planningTime.getCodOwner());
					updateStmtT01.setInt(10, planningTime.getCodStore());
					updateStmtT01.setInt(11, planningTime.getCodEmployee());
					updateStmtT01.setDate(12, Date.valueOf(planningTime.getBeginDate()));
					updateStmtT01.setTime(13, Time.valueOf(beginTime));
					updateStmtT01.setTimestamp(14, Timestamp.valueOf(dateTime));
					updateStmtT01.setTimestamp(15, Timestamp.valueOf(dateTime));
					updateStmtT01.setLong(16, codCustomer);

					updateStmtT01.addBatch();
					// } else {
					// updateStmtT02.setTime(1,
					// Time.valueOf(planningTime.getBeginTime()));
					// updateStmtT02.setInt(2, planningTime.getWeekday());
					// updateStmtT02.setInt(3, planningTime.getCodMaterial());
					// updateStmtT02.setString(4, RecordMode.ISRESERVED);
					// updateStmtT02.setBigDecimal(5,
					// planningTime.getPriceStore());
					// updateStmtT02.setString(6,
					// planningTime.getCodCurrStore());
					// updateStmtT02.setTimestamp(7,
					// Timestamp.valueOf(dateTimePlus));
					// updateStmtT02.setLong(8, codCustomer);
					//
					// updateStmtT02.setLong(9, planningTime.getCodOwner());
					// updateStmtT02.setInt(10, planningTime.getCodStore());
					// updateStmtT02.setInt(11, planningTime.getCodEmployee());
					// updateStmtT02.setDate(12,
					// Date.valueOf(planningTime.getBeginDate()));
					// updateStmtT02.setTime(13, Time.valueOf(beginTime));
					// updateStmtT02.setTimestamp(14,
					// Timestamp.valueOf(dateTime));
					// updateStmtT02.setTimestamp(15,
					// Timestamp.valueOf(dateTime));
					// updateStmtT02.setLong(16, codCustomer);
					//
					// updateStmtT02.addBatch();
					// }

					x++;
				}
				z++;
			}

			ArrayList<PlanningTime> planningTimeListAux1 = new ArrayList<PlanningTime>();
			ArrayList<PlanningTime> planningTimeListAux2 = new ArrayList<PlanningTime>();
			PlanningTime planningTimeAux;

			int[] returnCode = updateStmtT01.executeBatch();
			// updateStmtT02.executeBatch();

			for (int i = 0; i < returnCode.length; i++) {
				// if (planningTimeList.size() > i) {
				int j = returnCode[i];

				if (j <= 0) {
					planningTimeAux = planningTimeList.get(timeMap.get(i));
					if (planningTimeAux.getRecordMode().equals(RecordMode.ISNEW)) {
						if (!planningTimeListAux1.contains(planningTimeAux))
							planningTimeListAux1.add(planningTimeAux);
					} else if (!planningTimeListAux2.contains(planningTimeAux))
						planningTimeListAux2.add(planningTimeAux);
				}
				// }
			}

			planningTimeList.clear();

			if (planningTimeListAux1.size() != 0 || planningTimeListAux2.size() != 0) {

				// conn.rollback();

				SQLException exception = null;

				if (planningTimeListAux1.size() != 0 && !error24) {
					planningTimeList.addAll(planningTimeListAux1);
					exception = new SQLException("Message erro", null, 23);
				} else {
					planningTimeList.addAll(planningTimeListAux2);
					exception = new SQLException("Message erro", null, 24);
				}

				updateStmtT02 = releasePlanningTime(planningTimeList, codCustomer, conn);

				conn.commit();
				return exception;

			} else {
				conn.commit();
				return new SQLException(UPDATE_OK, null, 200);
			}

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, updateStmtT01, updateStmtT02);
		}
	}

	public SQLException releasePlanningTime(ArrayList<PlanningTime> planningTimeList, Long codCustomer) {

		Connection conn = null;
		PreparedStatement updateStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			updateStmtT01 = releasePlanningTime(planningTimeList, codCustomer, conn);

			conn.commit();

			return new SQLException(UPDATE_OK, null, 200);

		} catch (SQLException e) {
			try {
				conn.rollback();
				return e;
			} catch (SQLException e1) {
				return e1;
			}
		} finally {
			closeConnection(conn, updateStmtT01);
		}
	}

	private PreparedStatement releasePlanningTime(ArrayList<PlanningTime> planningTimeList, Long codCustomer,
			Connection conn) throws SQLException {

		PreparedStatement updateStmtT01;
		updateStmtT01 = conn.prepareStatement(PlanningTimeHelper.ST_UP_RELEASE_ITEM);

		for (PlanningTime planningTime : planningTimeList) {

			// for (int i = 0; i < planningTime.getRate(); i++) {
			// int part = planningTime.getPart();
			// int min = part * i;
			// LocalTime beginTime =
			// planningTime.getBeginTime().plusMinutes(min);

			updateStmtT01.setNull(1, Types.TIME);
			updateStmtT01.setString(2, RecordMode.RECORD_OK);
			updateStmtT01.setTimestamp(3, Timestamp.valueOf("1900-01-01 00:00:00"));
			updateStmtT01.setNull(4, Types.BIGINT);
			updateStmtT01.setNull(5, Types.BIGINT);

			updateStmtT01.setLong(6, planningTime.getCodOwner());
			updateStmtT01.setInt(7, planningTime.getCodStore());
			updateStmtT01.setInt(8, planningTime.getCodEmployee());
			updateStmtT01.setDate(9, Date.valueOf(planningTime.getBeginDate()));
			updateStmtT01.setTime(10, Time.valueOf(planningTime.getBeginTime()));
			updateStmtT01.setLong(11, codCustomer);

			updateStmtT01.executeUpdate();
			// }
		}
		return updateStmtT01;
	}

	public SQLException deletePlanningTime(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode) {

		Connection conn = null;
		PreparedStatement deleteStmt = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			deleteStmt = conn.prepareStatement(new PlanningTimeHelper().prepareDelete(codOwner, codStore, codEmployee,
					beginDate, beginTime, group, weekday, codMaterial, recordMode));

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

	public ArrayList<PlanningTime> selectPlanningTime(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<String> beginDate, List<String> beginTime, List<Integer> group,
			List<Integer> weekday, List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo,
			List<Long> codCustomer, List<Long> number, String iniDate, String finDate) throws SQLException {

		ArrayList<PlanningTime> planningTimeList = new ArrayList<PlanningTime>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			PlanningTimeHelper planningTimeHelper = new PlanningTimeHelper();

			LocalDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();

			selectStmt = conn.prepareStatement(planningTimeHelper.prepareSelect(codOwner, codStore, codEmployee,
					beginDate, beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number,
					dateTime.toString(), iniDate, finDate));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				PlanningTime planningTime = planningTimeHelper.assignResult(resultSet,
						PlanningTimeHelper.SELECT_PLANNING_TIME);

				if (planningTime != null)
					planningTimeList.add(planningTime);
			}

			return planningTimeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	public ArrayList<PlanningTime> getCart(Long codCustomer) throws SQLException {

		ArrayList<PlanningTime> planningTimeList = new ArrayList<PlanningTime>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			PlanningTimeHelper planningTimeHelper = new PlanningTimeHelper();

			LocalDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();

			selectStmt = conn.prepareStatement(
					planningTimeHelper.prepareGetCart(RecordMode.ISRESERVED, codCustomer, dateTime.toString()));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				PlanningTime planningTime = planningTimeHelper.assignResult(resultSet, PlanningTimeHelper.GET_CART);

				if (planningTime != null)
					planningTimeList.add(planningTime);
			}

			return planningTimeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	public ArrayList<PlanningTime> getBooked(Long codCustomer) throws SQLException {

		ArrayList<PlanningTime> planningTimeList = new ArrayList<PlanningTime>();
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {

			conn = getConnection();

			PlanningTimeHelper planningTimeHelper = new PlanningTimeHelper();

			LocalDate date = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate();

			selectStmt = conn.prepareStatement(
					planningTimeHelper.prepareGetBooked(RecordMode.ISPAYED, codCustomer, date.toString()));

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {

				PlanningTime planningTime = planningTimeHelper.assignResult(resultSet, PlanningTimeHelper.GET_BOOKED);

				if (planningTime != null)
					planningTimeList.add(planningTime);
			}

			return planningTimeList;

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, selectStmt, resultSet);
		}
	}

	public SQLException insertReserve(ArrayList<Reserve> reserveList) {

		Connection conn = null;
		PreparedStatement insertStmtT01 = null;

		try {

			conn = getConnection();
			conn.setAutoCommit(false);

			insertStmtT01 = conn.prepareStatement(ReserveHelper.ST_IN_ALL_FIELD);

			for (Reserve reserve : reserveList) {

				prepareReserveInsert(insertStmtT01, reserve);

				insertStmtT01.executeUpdate();
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
			closeConnection(conn, insertStmtT01);
		}

	}

	private void preparePlanningTimeInsert(PreparedStatement insertStmtT01, PlanningTime planningTime)
			throws SQLException {

		insertStmtT01.setLong(1, planningTime.getCodOwner());
		insertStmtT01.setInt(2, planningTime.getCodStore());
		insertStmtT01.setInt(3, planningTime.getCodEmployee());
		insertStmtT01.setDate(4, Date.valueOf(planningTime.getBeginDate()));
		insertStmtT01.setTime(5, Time.valueOf(planningTime.getBeginTime()));
		insertStmtT01.setInt(6, planningTime.getGroup());
		insertStmtT01.setInt(7, planningTime.getWeekday());
		insertStmtT01.setInt(8, planningTime.getCodMaterial());
		insertStmtT01.setString(9, RecordMode.RECORD_OK);
		insertStmtT01.setBigDecimal(10, planningTime.getPriceStore());
		insertStmtT01.setString(11, planningTime.getCodCurrStore());
		insertStmtT01.setTimestamp(12, Timestamp.valueOf("1900-01-01 00:00:00"));

	}

	private void prepareReserveInsert(PreparedStatement insertStmtT01, Reserve reserve) throws SQLException {

		insertStmtT01.setLong(1, reserve.getCodOwner());
		insertStmtT01.setInt(2, reserve.getCodStore());
		insertStmtT01.setLong(3, reserve.getCodCustomer());
		insertStmtT01.setTimestamp(4, Timestamp.valueOf(reserve.getReservedTime()));
		insertStmtT01.setString(5, reserve.getPayId());
		insertStmtT01.setLong(6, reserve.getReservedNum());

	}

}
