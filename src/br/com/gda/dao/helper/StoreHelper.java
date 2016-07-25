package br.com.gda.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.gda.db.GdaDB;
import br.com.gda.helper.Store;

public class StoreHelper extends GdaDB {

	protected static final String TABLE = "Store";

	protected static final String FIELD01 = COD_OWNER;
	protected static final String FIELD02 = COD_STORE;
	protected static final String FIELD03 = CNPJ;
	protected static final String FIELD04 = INSCRICAO_ESTADUAL;
	protected static final String FIELD05 = INSCRICAO_MUNICIPAL;
	protected static final String FIELD06 = RAZAO_SOCIAL;
	protected static final String FIELD07 = NAME;
	protected static final String FIELD08 = ADDRESS1;
	protected static final String FIELD09 = ADDRESS2;
	protected static final String FIELD10 = POSTALCODE;
	protected static final String FIELD11 = CITY;
	protected static final String FIELD12 = COUNTRY;
	protected static final String FIELD13 = STATE_PROVINCE;
	protected static final String FIELD14 = PHONE;
	protected static final String FIELD15 = COD_CURR;
	protected static final String FIELD16 = RECORD_MODE;
	protected static final String FIELD17 = "Cod_payment";

	public static final String ST_IN_ALL_FIELD = "INSERT INTO " + SCHEMA + "." + TABLE + " (" + FIELD01 + ", " + FIELD03
			+ ", " + FIELD04 + ", " + FIELD05 + ", " + FIELD06 + ", " + FIELD07 + ", " + FIELD08 + ", " + FIELD09 + ", "
			+ FIELD10 + ", " + FIELD11 + ", " + FIELD12 + ", " + FIELD13 + ", " + FIELD14 + ", " + FIELD15 + ", "
			+ FIELD16 + ") " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String ST_UP_ALL_FIELD_BY_FULL_KEY = "UPDATE " + SCHEMA + "." + TABLE + " SET " + FIELD03 + "=?"
			+ ", " + FIELD04 + "=?" + ", " + FIELD05 + "=?" + ", " + FIELD06 + "=?" + ", " + FIELD07 + "=?" + ", "
			+ FIELD08 + "=?" + ", " + FIELD09 + "=?" + ", " + FIELD10 + "=?" + ", " + FIELD11 + "=?" + ", " + FIELD12
			+ "=?" + ", " + FIELD13 + "=?" + ", " + FIELD14 + "=?" + ", " + FIELD15 + "=?" + ", " + FIELD16 + "=?"
			+ " WHERE " + FIELD01 + "=? AND " + FIELD02 + "=?";

	public static final String ST_DELETE = "DELETE FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT = "SELECT * FROM " + SCHEMA + "." + TABLE;

	public static final String ST_SELECT_WITH_LOCATION = "SELECT *, ( 6371 * acos( cos( radians(" + "?"
			+ ") ) * cos( radians( Store.Latitude ) ) * cos( radians( Store.Longitude ) - radians(" + "?"
			+ ") ) + sin( radians(" + "?" + ") ) * sin( radians( Store.Latitude ) ) ) ) AS distance FROM " + SCHEMA
			+ "." + TABLE;

	public static final String LAST_ID_STORE = "@last_id_store";

	public static final String VARIABLE = SET + LAST_ID_STORE + EQ + LAST_INSERT_ID;

	public Store assignResult(ResultSet resultSet) throws SQLException {

		Store store = new Store();

		store.setCodOwner(resultSet.getLong(FIELD01));
		store.setCodStore(resultSet.getInt(FIELD02));
		store.setCnpj(resultSet.getString(FIELD03));
		store.setInscEstadual(resultSet.getString(FIELD04));
		store.setInscMunicipal(resultSet.getString(FIELD05));
		store.setRazaoSocial(resultSet.getString(FIELD06));
		store.setName(resultSet.getString(FIELD07));
		store.setAddress1(resultSet.getString(FIELD08));
		store.setAddress2(resultSet.getString(FIELD09));
		store.setPostalcode(resultSet.getInt(FIELD10));
		store.setCity(resultSet.getString(FIELD11));
		store.setCountry(resultSet.getString(FIELD12));
		store.setState(resultSet.getString(FIELD13));
		store.setPhone(resultSet.getString(FIELD14));
		store.setCodCurr(resultSet.getString(FIELD15));
		store.setRecordMode(resultSet.getString(FIELD16));
		store.setCodPayment(resultSet.getString(FIELD17));

		return store;
	}

	public String prepareDelete(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> recordMode) {

		String stmt = ST_DELETE;

		stmt = prepareWhereClause(stmt, prepareStoreWhere(codOwner, codStore, cnpj, inscEstadual, inscMunicipal,
				razaoSocial, name, address1, address2, postalcode, city, country, state, null, codCurr, recordMode));

		return stmt;
	}

	public String prepareSelect(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr,
			List<String> recordMode) {

		String stmt = ST_SELECT;

		stmt = prepareWhereClause(stmt, prepareStoreWhere(codOwner, codStore, cnpj, inscEstadual, inscMunicipal,
				razaoSocial, name, address1, address2, postalcode, city, country, state, phone, codCurr, recordMode));

		return stmt;
	}
	
	public String prepareSelectLoc(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr,
			List<String> recordMode) {

		String stmt = ST_SELECT_WITH_LOCATION;

		stmt = prepareWhereClause(stmt, prepareStoreWhere(codOwner, codStore, cnpj, inscEstadual, inscMunicipal,
				razaoSocial, name, address1, address2, postalcode, city, country, state, phone, codCurr, recordMode));
		
		stmt = stmt + " HAVING distance < 56 ORDER BY distance LIMIT 0 , 20";

		return stmt;
	}

	public List<String> prepareStoreWhere(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> phone,
			List<String> recordMode) {

		List<String> where = new ArrayList<String>();

		assignFilterLong(where, FIELD01, codOwner);
		assignFilterInt(where, FIELD02, codStore);
		assignFilterString(where, FIELD03, cnpj);
		assignFilterString(where, FIELD04, inscEstadual);
		assignFilterString(where, FIELD05, inscMunicipal);
		assignFilterString(where, FIELD06, razaoSocial);
		assignFilterString(where, FIELD07, name);
		assignFilterString(where, FIELD08, address1);
		assignFilterString(where, FIELD09, address2);
		assignFilterInt(where, FIELD10, postalcode);
		assignFilterString(where, FIELD11, city);
		assignFilterString(where, FIELD12, country);
		assignFilterString(where, FIELD13, state);
		assignFilterString(where, FIELD14, phone);
		assignFilterString(where, FIELD15, codCurr);
		assignFilterString(where, FIELD16, recordMode);

		return where;
	}

}
