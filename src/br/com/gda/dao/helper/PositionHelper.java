package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Position;

public class PositionHelper extends GdaDB {

	public static final String TABLE = "Position";

	public static final String FIELD01 = COD_POSITION;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA
			+ "." + TABLE + " LEFT JOIN " + SCHEMA + "."
			+ PositionTextHelper.TABLE + " ON " + TABLE + "." + FIELD01 + " = "
			+ PositionTextHelper.TABLE + "." + PositionTextHelper.FIELD01;

	PositionTextHelper positionTextHelper;

	public PositionHelper() {
		positionTextHelper = new PositionTextHelper();
	}

	public PositionHelper(PositionTextHelper positionTextHelper) {
		this.positionTextHelper = positionTextHelper;
	}

	public void assignResult(ArrayList<Position> positionList,
			ResultSet resultSet) throws SQLException {

		if (resultSet.isFirst()
				|| !positionList.get(positionList.size() - 1).getCodPosition()
						.equals(resultSet.getInt(TABLE + "." + FIELD01))) {

			Position position = new Position();

			position.setCodPosition(resultSet.getInt(TABLE + "." + FIELD01));

			position.getPositionText().add(
					positionTextHelper.assignResult(resultSet));
			positionList.add(position);

		} else {

			positionList.get(positionList.size() - 1).getPositionText()
					.add(positionTextHelper.assignResult(resultSet));
		}
	}

	public String prepareSelect(List<Integer> codPosition,
			List<String> language, List<String> name) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = new ArrayList<String>();

		assignFilterInt(where, TABLE + "." + FIELD01, codPosition);

		where.addAll(positionTextHelper.preparePositionTextWhere(null,
				language, name));

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

}
