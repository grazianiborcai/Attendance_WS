package br.com.gda.dao.helper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.CreditCardAG;
import br.com.mind5.helper.Customer;
import moip.sdk.api.TaxDocument;

public class CardHelper extends GdaDB {

	public static final String TABLE = "Credit_card";

	public static final String FIELD01 = "Cod_customer";
	public static final String FIELD02 = "Last4";
	public static final String FIELD03 = "Hash";
	public static final String FIELD04 = "Brand";
	public static final String FIELD05 = "Full_name";
	public static final String FIELD06 = "Birth_date";
	public static final String FIELD07 = "Type";
	public static final String FIELD08 = "Number";

	public static final String ST_IN_ALL = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD01 + ", " + FIELD02
			+ ", " + FIELD03 + ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD03 + "=?" + ", " + FIELD04
			+ "=?" + ", " + FIELD05 + "=?" + ", " + FIELD06 + "=?" + ", " + FIELD07 + "=?" + ", " + FIELD08 + "=?"
			+ " WHERE " + FIELD01 + "=? AND " + FIELD02 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE;

	public CreditCardAG assignResult(ResultSet resultSet) throws SQLException {

		CreditCardAG creditCardAG = new CreditCardAG();

		creditCardAG.setCodCustomer(resultSet.getLong(TABLE + "." + FIELD01));
		creditCardAG.setLast4(resultSet.getString(TABLE + "." + FIELD02));
		creditCardAG.setHash(resultSet.getString(TABLE + "." + FIELD03));
		creditCardAG.setBrand(resultSet.getString(TABLE + "." + FIELD04));

		moip.sdk.api.Customer holder = new moip.sdk.api.Customer()
				.setFullname(resultSet.getString(TABLE + "." + FIELD05))
				.setBirthDate(resultSet.getDate(TABLE + "." + FIELD06).toString())
				.setTaxDocument(new TaxDocument().setType(resultSet.getString(TABLE + "." + FIELD07))
						.setNumber(resultSet.getString(TABLE + "." + FIELD08)));

		creditCardAG.setHolder(holder);

		return creditCardAG;
	}

	public String prepareDelete(Long codCustomer, String last4) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt, prepareCustomerWhere(codCustomer, last4));

		return stmt;
	}

	public String prepareSelect(Long codCustomer) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt, prepareCustomerWhere(codCustomer, null));
		return stmt;
	}

	private List<String> prepareCustomerWhere(Long codCustomer, String last4) {

		List<String> where = new ArrayList<String>();

		singleFilter(where, TABLE + "." + FIELD01, GdaDB.EQ, codCustomer);
		singleFilter(where, TABLE + "." + FIELD02, GdaDB.EQ, last4);

		return where;
	}

}
