package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Place;

public class PlaceHelper extends GdaDB {

	public static final String TABLE = "Place";

	public static final String FIELD01 = "PlaceID";
	public static final String FIELD02 = "Name";
	public static final String FIELD03 = "CountryID";
	public static final String FIELD04 = "RegionID";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Place assignResult(ResultSet resultSet) throws SQLException {

		Place place = new Place();

		place.setPlaceID(resultSet.getLong(TABLE + "." + FIELD01));
		place.setName(resultSet.getString(TABLE + "." + FIELD02));

		return place;
	}

	public String prepareSelect() {

		String stmt = ST_SELECT;

		return stmt;
	}

}
