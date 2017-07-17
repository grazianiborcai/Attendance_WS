package br.com.gda.dao.helper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.mind5.helper.Customer;

public class CustomerHelper extends GdaDB {

	public static final String TABLE = "Customer";

	public static final String FIELD01 = "Cod_customer";
	public static final String FIELD02 = "Phone";
	public static final String FIELD03 = "Password";
	public static final String FIELD04 = "Name";
	public static final String FIELD05 = "Cod_gender";
	public static final String FIELD06 = "CPF";
	public static final String FIELD07 = "Born_date";
	public static final String FIELD08 = "Email";
	public static final String FIELD09 = "Address1";
	public static final String FIELD10 = "Address2";
	public static final String FIELD11 = "Postalcode";
	public static final String FIELD12 = "City";
	public static final String FIELD13 = "Country";
	public static final String FIELD14 = "State_province";
	public static final String FIELD15 = "Cod_payment";

	public static final String ST_IN_ALL = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD02 + ", " + FIELD03
			+ ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", " + FIELD09 + ", "
			+ FIELD10 + ", " + FIELD11 + ", " + FIELD12 + ", " + FIELD13 + ", " + FIELD14 + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD02 + "=?" + ", " + FIELD03
			+ "=?" + ", " + FIELD04 + "=?" + ", " + FIELD05 + "=?" + ", " + FIELD06 + "=?" + ", " + FIELD07 + "=?"
			+ ", " + FIELD08 + "=?" + ", " + FIELD09 + "=?" + ", " + FIELD10 + "=?" + ", " + FIELD11 + "=?" + ", "
			+ FIELD12 + "=?" + ", " + FIELD13 + "=?" + ", " + FIELD14 + "=?" + ", " + FIELD15 + "=?" + " WHERE "
			+ FIELD01 + "=?";

	public static final String ST_UP_PASS = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD03 + "=?" + " WHERE "
			+ FIELD01 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT_LAST_INSERT_ID = "SELECT " + LAST_INSERT_ID;

	public Customer assignResult(ResultSet resultSet) throws SQLException {

		Customer customer = new Customer();

		customer.setCodCustomer(resultSet.getLong(TABLE + "." + FIELD01));
		customer.setPhone(resultSet.getString(TABLE + "." + FIELD02));
		customer.setPassword(resultSet.getString(TABLE + "." + FIELD03));
		customer.setName(resultSet.getString(TABLE + "." + FIELD04));
		customer.setCodGender(resultSet.getByte(TABLE + "." + FIELD05));
		customer.setCpf(resultSet.getString(TABLE + "." + FIELD06));
		Date date = resultSet.getDate(TABLE + "." + FIELD07);
		if (date != null)
			customer.setBornDate(date.toLocalDate());
		customer.setEmail(resultSet.getString(TABLE + "." + FIELD08));
		customer.setAddress1(resultSet.getString(TABLE + "." + FIELD09));
		customer.setAddress2(resultSet.getString(TABLE + "." + FIELD10));
		customer.setPostalcode(resultSet.getInt(TABLE + "." + FIELD11));
		customer.setCity(resultSet.getString(TABLE + "." + FIELD12));
		customer.setCountry(resultSet.getString(TABLE + "." + FIELD13));
		customer.setState(resultSet.getString(TABLE + "." + FIELD14));
		customer.setCodPayment(resultSet.getString(TABLE + "." + FIELD15));

		return customer;
	}

	public String prepareDelete(List<Long> codCustomer, List<String> phone, List<String> password, List<String> name,
			List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email, List<String> address1,
			List<String> address2, List<Integer> postalcode, List<String> city, List<String> country,
			List<String> state) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt, prepareCustomerWhere(codCustomer, phone, password, name, codGender, cpf,
				bornDate, email, address1, address2, postalcode, city, country, state));

		return stmt;
	}

	public String prepareSelect(List<Long> codCustomer, List<String> phone, List<String> password, List<String> name,
			List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email, List<String> address1,
			List<String> address2, List<Integer> postalcode, List<String> city, List<String> country,
			List<String> state) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt, prepareCustomerWhere(codCustomer, phone, password, name, codGender, cpf,
				bornDate, email, address1, address2, postalcode, city, country, state));
		return stmt;
	}

	private List<String> prepareCustomerWhere(List<Long> codCustomer, List<String> phone, List<String> password,
			List<String> name, List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, TABLE + "." + FIELD01, codCustomer);
		assignFilterString(where, TABLE + "." + FIELD02, phone);
		assignFilterString(where, TABLE + "." + FIELD03, password);
		assignFilterString(where, TABLE + "." + FIELD04, name);
		assignFilterByte(where, TABLE + "." + FIELD05, codGender);
		assignFilterString(where, TABLE + "." + FIELD06, cpf);
		assignFilterString(where, TABLE + "." + FIELD07, bornDate);
		assignFilterString(where, TABLE + "." + FIELD08, email);
		assignFilterString(where, TABLE + "." + FIELD09, address1);
		assignFilterString(where, TABLE + "." + FIELD10, address2);
		assignFilterInt(where, TABLE + "." + FIELD11, postalcode);
		assignFilterString(where, TABLE + "." + FIELD12, city);
		assignFilterString(where, TABLE + "." + FIELD13, country);
		assignFilterString(where, TABLE + "." + FIELD14, state);

		return where;
	}

}
