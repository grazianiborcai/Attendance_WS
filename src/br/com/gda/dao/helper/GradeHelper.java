package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Grade;

public class GradeHelper extends GdaDB {

	public static final String TABLE = "Grade";

	public static final String FIELD01 = "GradeID";
	public static final String FIELD02 = "GradeDesc";
	
	
	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "."
			+ TABLE;

	public Grade assignResult(ResultSet resultSet) throws SQLException {

		Grade grade = new Grade();

		grade.setGradeID(resultSet.getInt(FIELD01));
		grade.setGradeDesc(resultSet.getString(FIELD02));

		return grade;
	}

	public String prepareSelect() {

		String stmt = ST_SELECT;

		return stmt;
	}

}
