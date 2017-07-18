package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Weekday;

public class WeekdayHelper extends GdaDB {

	public static final String TABLE = "Weekday";

	public static final String FIELD01 = "WeekdayID";
	public static final String FIELD02 = "WeekDesc";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Weekday assignResult(ResultSet resultSet) throws SQLException {

		Weekday weekday = new Weekday();

		weekday.setWeekdayID(resultSet.getInt(TABLE + "." + FIELD01));
		weekday.setWeekDesc(resultSet.getString(TABLE + "." + FIELD02));

		return weekday;
	}

	public String prepareSelect() {

		String stmt = ST_SELECT;

		return stmt;
	}

}
