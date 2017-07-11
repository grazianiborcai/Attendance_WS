package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.PlanningTime;
import br.com.gda.helper.RecordMode;

public class PlanningTimeHelper extends GdaDB {

	private static final String DISTANCE = "56";
	public static final String SELECT_PLANNING_TIME = "selectPlanningTime";
	public static final String GET_BOOKED = "getBooked";
	public static final String GET_CART = "getCart";

	public static final String TABLE = "Planning_time";

	public static final String FIELD01 = COD_OWNER;
	public static final String FIELD02 = COD_STORE;
	public static final String FIELD03 = COD_EMPLOYEE;
	public static final String FIELD04 = "Begin_date";
	public static final String FIELD05 = "Begin_time";
	public static final String FIELD06 = "Begin_time_res";
	public static final String FIELD07 = WEEKDAY;
	public static final String FIELD08 = COD_MATERIAL;
	public static final String FIELD09 = RECORD_MODE;
	public static final String FIELD10 = PRICE_STORE;
	public static final String FIELD11 = "Cod_curr";
	public static final String FIELD12 = "Reserved_to";
	public static final String FIELD13 = "Cod_customer_reserve";
	public static final String FIELD14 = "Number";
	public static final String FIELD15 = "Part";
	public static final String FIELD16 = "Reserved_num";
	public static final String FIELD17 = "Pay_id";

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD01 + ", " + FIELD02
			+ ", " + FIELD03 + ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", "
			+ FIELD09 + ", " + FIELD10 + ", " + FIELD11 + ", " + FIELD12 + ", " + FIELD15 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD06 + "=?"
			+ ", " + FIELD07 + "=?" + ", " + FIELD08 + "=?" + ", " + FIELD09 + "=?" + ", " + FIELD10 + "=?" + ", "
			+ FIELD11 + "=?" + ", " + FIELD12 + "=?" + ", " + FIELD13 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02
			+ "=? AND " + FIELD03 + "=? AND " + FIELD04 + "=? AND " + FIELD05 + "=? AND ( ( (" + FIELD12 + " <?"
			+ " AND ( " + FIELD09 + " = '" + RecordMode.RECORD_OK + "' OR " + FIELD09 + " = '" + RecordMode.ISRESERVED
			+ "' ) )" + " OR ( " + FIELD12 + ">=? AND " + FIELD13 + "=? AND " + FIELD09 + " = '" + RecordMode.ISRESERVED
			+ "' ) ) )";

	public static final String ST_UP_RELEASE_ITEM = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD06 + "=?" + ", "
			+ FIELD09 + "=?" + ", " + FIELD12 + "=?" + ", " + FIELD13 + "=?" + ", " + FIELD08 + "=?" + ", " + FIELD14
			+ "=?" + ", " + FIELD16 + "=?" + ", " + FIELD17 + "=?" + " WHERE " + FIELD01 + "=? AND " + FIELD02
			+ "=? AND " + FIELD03 + "=? AND " + FIELD04 + "=? AND " + FIELD06 + "=? AND " + FIELD09 + "=? AND "
			+ FIELD13 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE + " INNER JOIN " + SCHEMA + "."
			+ EmployeeMaterialHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = " + EmployeeMaterialHelper.TABLE + "."
			+ EmployeeMaterialHelper.FIELD01 + " AND " + TABLE + "." + FIELD02 + " = " + EmployeeMaterialHelper.TABLE
			+ "." + EmployeeMaterialHelper.FIELD02 + " AND " + TABLE + "." + FIELD03 + " = "
			+ EmployeeMaterialHelper.TABLE + "." + EmployeeMaterialHelper.FIELD03

			+ " INNER JOIN " + SCHEMA + "." + MaterialStoreHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD01 + " AND " + TABLE + "." + FIELD02 + " = "
			+ MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD03 + " AND " + TABLE + "." + FIELD07 + " = "
			+ MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD08 + " AND " + EmployeeMaterialHelper.TABLE
			+ "." + EmployeeMaterialHelper.FIELD04 + " = " + MaterialStoreHelper.TABLE + "."
			+ MaterialStoreHelper.FIELD02;

	public static final String ST_GET_CART = "SELECT DISTINCT " + FIELD01 + ", " + FIELD02 + ", " + FIELD03 + ", "
			+ FIELD04 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", " + FIELD09 + ", " + FIELD10 + ", "
			+ FIELD11 + ", " + FIELD12 + ", " + FIELD13 + ", " + FIELD14 + ", " + FIELD15 + ", " + FIELD16 + ", "
			+ FIELD17 + ", count(" + FIELD06 + ") as " + EmployeeMaterialHelper.FIELD05 + " FROM " + SCHEMA + "."
			+ TABLE;

	public static final String ST_SELECT_WITH_LOCATION = "SELECT * FROM " + SCHEMA + "." + TABLE + " INNER JOIN "
			+ SCHEMA + "." + EmployeeMaterialHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ EmployeeMaterialHelper.TABLE + "." + EmployeeMaterialHelper.FIELD01 + " AND " + TABLE + "." + FIELD02
			+ " = " + EmployeeMaterialHelper.TABLE + "." + EmployeeMaterialHelper.FIELD02 + " AND " + TABLE + "."
			+ FIELD03 + " = " + EmployeeMaterialHelper.TABLE + "." + EmployeeMaterialHelper.FIELD03

			+ " INNER JOIN " + SCHEMA + "." + MaterialStoreHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD01 + " AND " + TABLE + "." + FIELD02 + " = "
			+ MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD03 + " AND " + TABLE + "." + FIELD07 + " = "
			+ MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD08 + " AND " + EmployeeMaterialHelper.TABLE
			+ "." + EmployeeMaterialHelper.FIELD04 + " = " + MaterialStoreHelper.TABLE + "."
			+ MaterialStoreHelper.FIELD02 + " WHERE " + TABLE + "." + FIELD02
			+ " IN ( SELECT Y.Cod_store FROM (SELECT Store.Cod_store, ( 6371 * acos( cos( radians(" + "?"
			+ ") ) * cos( radians( Store.Latitude ) ) * cos( radians( Store.Longitude ) - radians(" + "?"
			+ ") ) + sin( radians(" + "?" + ") ) * sin( radians( Store.Latitude ) ) ) ) AS distance FROM " + SCHEMA
			+ "." + "Store HAVING distance < " + DISTANCE + " ORDER BY distance LIMIT 0 , 20) Y)";

	public PlanningTime assignResultLoc(ResultSet resultSet, String from, LocalDate date, LocalTime time)
			throws SQLException {

		PlanningTime planningTime = null;

		LocalDate dateTimeSQL = resultSet.getDate(TABLE + "." + FIELD04).toLocalDate();
		LocalTime timeSQL = resultSet.getTime(TABLE + "." + FIELD05).toLocalTime();

		if ((dateTimeSQL.equals(date) && timeSQL.isAfter(time.plusHours(1))) || dateTimeSQL.isAfter(date))
			planningTime = assignResult(resultSet, from);

		return planningTime;
	}

	public PlanningTime assignResult(ResultSet resultSet, String from) throws SQLException {

		PlanningTime planningTime = null;

		// LocalTime beginTime = resultSet.getTime(TABLE + "." +
		// FIELD05).toLocalTime();
		// int part = resultSet.getInt(TABLE + "." + FIELD15);
		// int rate = resultSet.getInt(EmployeeMaterialHelper.TABLE + "." +
		// EmployeeMaterialHelper.FIELD05);
		int rate = resultSet.getInt(EmployeeMaterialHelper.FIELD05);
		// int min = part * rate;

		// LocalTime endTime = beginTime.plusMinutes(min);

		if (rate != 0) {

			planningTime = new PlanningTime();

			planningTime.setCodOwner(resultSet.getLong(TABLE + "." + FIELD01));
			planningTime.setCodStore(resultSet.getInt(TABLE + "." + FIELD02));
			planningTime.setCodEmployee(resultSet.getInt(TABLE + "." + FIELD03));
			planningTime.setBeginDate(resultSet.getDate(TABLE + "." + FIELD04).toLocalDate());
			if (from == GET_CART || from == GET_BOOKED) {
				planningTime.setBeginTime(resultSet.getTime(TABLE + "." + FIELD06).toLocalTime());
				planningTime.setCodMaterial(resultSet.getInt(TABLE + "." + FIELD08));
				planningTime.setPriceStore(resultSet.getBigDecimal(TABLE + "." + FIELD10));
				planningTime.setCodCurrStore(resultSet.getString(TABLE + "." + FIELD11));
			} else {
				planningTime.setBeginTime(resultSet.getTime(TABLE + "." + FIELD05).toLocalTime());
				planningTime.setCodMaterial(
						resultSet.getInt(EmployeeMaterialHelper.TABLE + "." + EmployeeMaterialHelper.FIELD04));
				planningTime.setPriceStore(
						resultSet.getBigDecimal(MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD04));
				planningTime.setCodCurrStore(
						resultSet.getString(MaterialStoreHelper.TABLE + "." + MaterialStoreHelper.FIELD07));
			}

			// planningTime.setEndTime(endTime);

			// planningTime.setGroup(resultSet.getInt(TABLE + "." + FIELD06));
			planningTime.setWeekday(resultSet.getInt(TABLE + "." + FIELD07));
			planningTime.setRecordMode(resultSet.getString(TABLE + "." + FIELD09));

			planningTime.setReservedTo(resultSet.getTimestamp(TABLE + "." + FIELD12).toLocalDateTime());
			planningTime.setCodCustomer(resultSet.getLong(TABLE + "." + FIELD13));
			planningTime.setNumber(resultSet.getLong(TABLE + "." + FIELD14));
			planningTime.setPart(resultSet.getInt(TABLE + "." + FIELD15));
			planningTime.setRate(rate);
			planningTime.setReservedNum(resultSet.getString(TABLE + "." + FIELD16));
			planningTime.setPayId(resultSet.getString(TABLE + "." + FIELD17));

		}

		return planningTime;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt, preparePlanningTimeWhere(codOwner, codStore, codEmployee, beginDate, beginTime,
				group, weekday, codMaterial, recordMode, null, null, null));

		stmt = stmt + " AND ( " + FIELD09 + " = '" + RecordMode.RECORD_OK + "' OR " + FIELD09 + " = '"
				+ RecordMode.ISRESERVED + "' )";

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo, List<Long> codCustomer,
			List<Long> number, String dateTime, String iniDate, String finDate) {

		String stmt = ST_SELECT;

		if (iniDate != null || finDate != null)
			stmt = prepareWhereClause(stmt, preparePlanningTimeWhere(codOwner, codStore, codEmployee, null, beginTime,
					group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number));
		else
			stmt = prepareWhereClause(stmt, preparePlanningTimeWhere(codOwner, codStore, codEmployee, beginDate,
					beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number));

		if (!stmt.equals(ST_SELECT))
			stmt = stmt + " AND ";
		else
			stmt = stmt + " WHERE ";

		if (iniDate != null || finDate != null)
			stmt = stmt + TABLE + "." + FIELD04 + " >= '" + iniDate + "' AND " + TABLE + "." + FIELD04 + " <= '"
					+ finDate + "' AND ";

		stmt = stmt + TABLE + "." + FIELD12 + " < '" + dateTime + "' AND ( " + TABLE + "." + FIELD09 + " = '"
				+ RecordMode.RECORD_OK + "' OR " + TABLE + "." + FIELD09 + " = '" + RecordMode.ISRESERVED + "' )";

		stmt = stmt + " ORDER BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02 + ", " + TABLE + "." + FIELD03
				+ ", " + TABLE + "." + FIELD04 + ", " + TABLE + "." + FIELD05;

		return stmt;
	}

	public String prepareGetCart(String recordMode, Long codCustomer, String dateTime) {

		String stmt = ST_GET_CART;

		stmt = stmt + " WHERE " + TABLE + "." + FIELD12 + " > '" + dateTime + "' AND " + TABLE + "." + FIELD13 + " = '"
				+ codCustomer + "' AND " + TABLE + "." + FIELD09 + " = '" + recordMode + "'";

		stmt = stmt + " GROUP BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02 + ", " + TABLE + "." + FIELD03
				+ ", " + TABLE + "." + FIELD04 + ", " + TABLE + "." + FIELD06;

		return stmt;
	}

	public String prepareGetBooked(String recordMode, Long codCustomer, String date) {

		String stmt = ST_GET_CART;

		stmt = stmt + " WHERE " + TABLE + "." + FIELD04 + " >= '" + date + "' AND " + TABLE + "." + FIELD13 + " = '"
				+ codCustomer + "' AND " + TABLE + "." + FIELD09 + " = '" + recordMode + "'";

		stmt = stmt + " GROUP BY " + TABLE + "." + FIELD01 + ", " + TABLE + "." + FIELD02 + ", " + TABLE + "." + FIELD03
				+ ", " + TABLE + "." + FIELD04 + ", " + TABLE + "." + FIELD06;

		return stmt;
	}

	public String prepareSelectLoc(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo, List<Long> codCustomer,
			List<Long> number, String dateTime, String iniDate, String finDate) {

		String stmt = ST_SELECT_WITH_LOCATION;

		List<String> where = preparePlanningTimeWhere(codOwner, codStore, codEmployee, beginDate, beginTime, group,
				weekday, codMaterial, recordMode, reservedTo, codCustomer, number);

		for (int i = 0; i < where.size(); i++) {
			stmt = stmt + " AND " + where.get(i);
		}

		stmt = stmt + " AND " + TABLE + "." + FIELD12 + " < '" + dateTime + "' AND ( " + TABLE + "." + FIELD09 + " = '"
				+ RecordMode.RECORD_OK + "' OR " + TABLE + "." + FIELD09 + " = '" + RecordMode.ISRESERVED + "' )";

		// stmt = stmt + " GROUP BY " + TABLE + "." + FIELD01 + ", " + TABLE +
		// "." + FIELD02 + ", " + TABLE + "." + FIELD03
		// + ", " + TABLE + "." + FIELD04 + ", " + TABLE + "." + FIELD06;

		return stmt;
	}

	public List<String> preparePlanningTimeWhere(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> beginDate, List<String> beginTime, List<Integer> group, List<Integer> weekday,
			List<Integer> codMaterial, List<String> recordMode, List<String> reservedTo, List<Long> codCustomer,
			List<Long> number) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codOwner);
		assignFilterInt(where, TABLE + "." + FIELD02, codStore);
		assignFilterInt(where, TABLE + "." + FIELD03, codEmployee);
		assignFilterString(where, TABLE + "." + FIELD04, beginDate);
		assignFilterString(where, TABLE + "." + FIELD05, beginTime);
		assignFilterInt(where, TABLE + "." + FIELD06, group);
		assignFilterInt(where, TABLE + "." + FIELD07, weekday);
		assignFilterInt(where, EmployeeMaterialHelper.TABLE + "." + EmployeeMaterialHelper.FIELD04, codMaterial);
		assignFilterString(where, TABLE + "." + FIELD09, recordMode);
		assignFilterString(where, TABLE + "." + FIELD12, reservedTo);
		assignFilterLong(where, TABLE + "." + FIELD13, codCustomer);
		assignFilterLong(where, TABLE + "." + FIELD14, number);

		return where;
	}

}
