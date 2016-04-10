package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.Reserve;

public class ReserveHelper extends GdaDB {

	public static final String TABLE = "Reserve";

	public static final String FIELD01 = "Number";
	public static final String FIELD02 = COD_OWNER;
	public static final String FIELD03 = COD_STORE;
	public static final String FIELD04 = COD_CUSTOMER;
	public static final String FIELD05 = "Reserved_time";
	public static final String FIELD06 = "Pay_id";
	public static final String FIELD07 = "Reserved_num";

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD02 + ", " + FIELD03
			+ ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ") " + "VALUES (?, ?, ?, ?, ?, ?)";

	public Reserve assignResult(ResultSet resultSet) throws SQLException {

		Reserve reserved = new Reserve();

		reserved.setNumber(resultSet.getLong(TABLE + "." + FIELD01));
		reserved.setCodOwner(resultSet.getLong(TABLE + "." + FIELD02));
		reserved.setCodStore(resultSet.getInt(TABLE + "." + FIELD03));
		reserved.setCodCustomer(resultSet.getLong(TABLE + "." + FIELD04));
		reserved.setReservedTime(resultSet.getTimestamp(TABLE + "." + FIELD05).toLocalDateTime());
		reserved.setPayId(resultSet.getString(TABLE + "." + FIELD06));
		reserved.setReservedNum(resultSet.getString(TABLE + "." + FIELD07));

		return reserved;
	}

}
