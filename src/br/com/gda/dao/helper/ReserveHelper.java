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
	public static final String FIELD05 = "Pay_id";
	public static final String FIELD06 = "Reserved_num";

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD02 + ", " + FIELD03
			+ ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ") " + "VALUES (?, ?, ?, ?, ?)";

	public Reserve assignResult(ResultSet resultSet) throws SQLException {

		Reserve reserved = new Reserve();

		reserved.setNumber(resultSet.getLong(TABLE + "." + FIELD01));
		reserved.setCodOwner(resultSet.getLong(TABLE + "." + FIELD02));
		reserved.setCodStore(resultSet.getInt(TABLE + "." + FIELD03));
		reserved.setCodCustomer(resultSet.getLong(TABLE + "." + FIELD04));
		reserved.setPayId(resultSet.getString(TABLE + "." + FIELD05));
		reserved.setReservedNum(resultSet.getLong(TABLE + "." + FIELD06));

		return reserved;
	}

}
