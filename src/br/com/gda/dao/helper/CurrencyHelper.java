package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.CurrencyBase;
import br.com.mind5.helper.ExchangeRate;

public class CurrencyHelper extends GdaDB {

	public static final String TABLE01 = "Currency_unit";

	public static final String T01_FIELD01 = COD_CURR;

	public static final String TABLE02 = "Currency_unit_text";

	public static final String T02_FIELD01 = COD_CURR;
	public static final String T02_FIELD02 = LANGUAGE;
	public static final String T02_FIELD03 = NAME;

	public static final String ST_SELECT_WITH_TEXT = "SELECT * FROM " + SCHEMA + "." + TABLE01 + " LEFT JOIN " + SCHEMA
			+ "." + TABLE02 + " ON " + TABLE01 + "." + T01_FIELD01 + " = " + TABLE02 + "." + T02_FIELD01;

	public CurrencyHelper() {
	}

	public void assignResult(CurrencyBase currencyBase, String baseCode, ResultSet resultSet) throws SQLException {

		if (baseCode.equals(resultSet.getString(TABLE01 + "." + T01_FIELD01))) {
			currencyBase.setBaseCode(baseCode);
			currencyBase.setBaseCodeText(resultSet.getString(TABLE02 + "." + T02_FIELD03));
		}

		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setTargetCode(resultSet.getString(TABLE01 + "." + T01_FIELD01));
		exchangeRate.setTargetCodeText(resultSet.getString(TABLE02 + "." + T02_FIELD03));
//		exchangeRate.setFactor(ServletContainerGDA.exchangeRateProvider
//				.getExchangeRate(baseCode, resultSet.getString(TABLE01 + "." + T01_FIELD01)).getFactor().doubleValue());
		exchangeRate.setFactor(Double.valueOf(1));
		
		
		currencyBase.getExchangeRateGDA().add(exchangeRate);

	}

	public String prepareSelect(String language) {

		String stmt = ST_SELECT_WITH_TEXT;

		List<String> where = prepareCurrencyWhere(language);

		stmt = prepareWhereClause(stmt, where);

		return stmt;
	}

	public List<String> prepareCurrencyWhere(String language) {

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE02 + "." + T02_FIELD02, GdaDB.EQ, language);

		return where;
	}

}
