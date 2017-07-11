package br.com.gda.db;

import java.util.List;

public class GdaDB {

	protected static final String FROM = "@from";

	protected static final String SET = "SET ";
	public static final String EQ = " = ";
	protected static final String LAST_INSERT_ID = "LAST_INSERT_ID()";

	protected static final String VALID_END_DATE = "9999-12-31 00:00:00";

	private static final String BRACKET_END_STRING = "')";
	private static final String COMMA_STRING = "', '";
	private static final String IN_STRING = " IN ('";
	private static final String BRACKET_END = ")";
	private static final String COMMA = ", ";
	private static final String IN = " IN (";
	private static final String AND = " AND ";
	private static final String WHERE = " WHERE ";
	private static final String EQ_STRING = " = '";
	private static final String S_QUOTES = "'";

	private static final String GE_STRING = " >= '";
	public static final String GE = " >= ";
	private static final String LE_STRING = " <= '";
	public static final String LE = " <= ";
	private static final String GT_STRING = " > '";
	public static final String GT = " > ";
	private static final String LT_STRING = " < '";
	public static final String LT = " < ";
	private static final String NE_STRING = " <> '";
	public static final String NE = " <> ";

	protected static final String COD_OWNER = "Cod_owner";
	protected static final String RECORD_MODE = "Record_mode";
	protected static final String COD_MATERIAL = "Cod_material";
	protected static final String COD_DETAIL = "Cod_detail";
	protected static final String COD_ITEM_DT = "Cod_item_dt";
	protected static final String LANGUAGE = "Language";
	protected static final String NAME = "Name";
	protected static final String DESCRIPTION = "Description";
	protected static final String TEXT_LONG = "Text_long";
	protected static final String COD_MENU = "Cod_menu";
	protected static final String ITEM = "Item";
	protected static final String COD_STORE = "Cod_store";
	protected static final String ADDRESS1 = "Address1";
	protected static final String ADDRESS2 = "Address2";
	protected static final String POSTALCODE = "Postalcode";
	protected static final String CITY = "City";
	protected static final String COUNTRY = "CountryID";
	protected static final String STATE_PROVINCE = "State_province";
	protected static final String CPF = "CPF";
	protected static final String PASSWORD = "Password";
	protected static final String EMAIL = "Email";
	protected static final String COD_GENDER = "Cod_gender";
	protected static final String BORN_DATE = "Born_date";
	protected static final String PHONE = "Phone";
	protected static final String COD_EMPLOYEE = "Cod_employee";
	protected static final String COD_POSITION = "Cod_position";
	protected static final String WEEKDAY = "Weekday";
	protected static final String COD_TYPE = "Cod_type";
	protected static final String COD_CATEGORY = "Cod_category";
	protected static final String EMAIL_AUX = "Email_aux";
	protected static final String BARCODE = "Barcode";
	protected static final String DURATION = "Duration";
	protected static final String IMAGE = "Image";
	protected static final String PRICE = "Price";
	protected static final String QUANTITY = "Quantity";
	protected static final String QTD_MAT = "Qtd_mat";
	protected static final String NEXT = "Next";
	protected static final String FIRST_CHILD = "First_child";
	protected static final String ID_FATHER = "Id_father";
	protected static final String LEVEL = "Level";
	protected static final String IDENTIFIER = "Identifier";
	protected static final String HALF_PRICE_STORE = "Half_price_store";
	protected static final String PRICE_STORE = "Price_store";
	protected static final String RAZAO_SOCIAL = "Razao_social";
	protected static final String INSCRICAO_MUNICIPAL = "Inscricao_municipal";
	protected static final String INSCRICAO_ESTADUAL = "Inscricao_estadual";
	protected static final String CNPJ = "CNPJ";
	protected static final String QTD_SEATS = "Qtd_seats";
	protected static final String LONGITUDE = "Longitude";
	protected static final String LATITUDE = "Latitude";
	protected static final String VIEW_DATE = "View_date";
	protected static final String COD_TABLE = "Cod_table";
	protected static final String END_DATE = "End_date";
	protected static final String COD_CUSTOMER = "Cod_customer";
	protected static final String CONTROL_CODE = "Control_code";
	protected static final String COD_BILL = "Cod_bill";
	protected static final String COD_TP_PAYMENT = "Cod_tp_payment";
	protected static final String COD_B_ITEM_STATUS = "Cod_b_item_status";
	protected static final String COD_BILL_STATUS = "Cod_bill_status";
	protected static final String BEGIN_DATE = "Begin_date";
	protected static final String COD_CURR = "Cod_curr";

	protected static final String SCHEMA = "attendance";

	// protected static final String TABLE049 = "material_customer_view";
	//
	// protected static final String T049_FIELD01 = COD_OWNER;
	// protected static final String T049_FIELD02 = COD_STORE;
	// protected static final String T049_FIELD03 = COD_MATERIAL;
	// protected static final String T049_FIELD04 = COD_CUSTOMER;
	// protected static final String T049_FIELD05 = VIEW_DATE;
	// protected static final String T049_FIELD06 = LATITUDE;
	// protected static final String T049_FIELD07 = LONGITUDE;
	//
	// protected static final String TABLE050 = "menu_customer_view";
	//
	// protected static final String T050_FIELD01 = COD_OWNER;
	// protected static final String T050_FIELD02 = COD_STORE;
	// protected static final String T050_FIELD03 = COD_MENU;
	// protected static final String T050_FIELD04 = COD_CUSTOMER;
	// protected static final String T050_FIELD05 = VIEW_DATE;
	// protected static final String T050_FIELD06 = LATITUDE;
	// protected static final String T050_FIELD07 = LONGITUDE;

	protected String prepareWhereClause(String stmt, List<String> where) {
		for (int i = 0; i < where.size(); i++) {
			if (i == 0) {
				stmt = stmt + WHERE + where.get(i);
			} else {
				stmt = stmt + AND + where.get(i);
			}
		}
		return stmt;
	}

	protected void assignFilterLong(List<String> whereList, String field,
			List<Long> longList) {

		assign(whereList, field, longList, null, null, null);
	}

	protected void assignFilterInt(List<String> whereList, String field,
			List<Integer> intList) {

		assign(whereList, field, null, intList, null, null);
	}

	protected void assignFilterByte(List<String> whereList, String field,
			List<Byte> byteList) {

		assign(whereList, field, null, null, byteList, null);
	}

	protected void assignFilterString(List<String> whereList, String field,
			List<String> stringList) {

		assign(whereList, field, null, null, null, stringList);
	}

	private void assign(List<String> whereList, String field,
			List<Long> longList, List<Integer> intList, List<Byte> byteList,
			List<String> stringList) {

		String where = new String();

		if (longList != null && !longList.isEmpty()) {
			if (longList.size() == 1)
				where = singleFilterEQ(field, longList.get(0));
			else {
				where = field + IN;
				for (int i = 0; i < longList.size(); i++) {
					if (i == 0) {
						where = where + longList.get(i);
					} else {
						where = where + COMMA + longList.get(i);
					}
				}
				where = where + BRACKET_END;
			}
		}

		if (intList != null && !intList.isEmpty()) {
			if (intList.size() == 1)
				where = singleFilterEQ(field, intList.get(0));
			else {
				where = field + IN;
				for (int i = 0; i < intList.size(); i++) {
					if (i == 0) {
						where = where + intList.get(i);
					} else {
						where = where + COMMA + intList.get(i);
					}
				}
				where = where + BRACKET_END;
			}
		}

		if (byteList != null && !byteList.isEmpty()) {
			if (byteList.size() == 1)
				where = singleFilterEQ(field, byteList.get(0));
			else {
				where = field + IN;
				for (int i = 0; i < byteList.size(); i++) {
					if (i == 0) {
						where = where + byteList.get(i);
					} else {
						where = where + COMMA + byteList.get(i);
					}
				}
				where = where + BRACKET_END;
			}
		}

		if (stringList != null && !stringList.isEmpty()) {
			if (stringList.size() == 1)
				where = singleFilterEQ(field, stringList.get(0));
			else {
				where = field + IN_STRING;
				for (int i = 0; i < stringList.size(); i++) {
					if (i == 0) {
						where = where + stringList.get(i);
					} else {
						where = where + COMMA_STRING + stringList.get(i);
					}
				}
				where = where + BRACKET_END_STRING;
			}
		}

		if (!where.isEmpty())
			whereList.add(where);
	}

	protected void singleFilter(List<String> where, String field, String op,
			Long longValue) {
		if (longValue != null) {

			switch (op) {

			case EQ:
				where.add(singleFilterEQ(field, longValue));
				break;
			case GE:
				where.add(singleFilterGE(field, longValue));
				break;
			case LE:
				where.add(singleFilterLE(field, longValue));
				break;
			case GT:
				where.add(singleFilterGT(field, longValue));
				break;
			case LT:
				where.add(singleFilterLT(field, longValue));
				break;
			case NE:
				where.add(singleFilterNE(field, longValue));
				break;
			}
		}
	}

	protected void singleFilter(List<String> where, String field, String op,
			Integer intValue) {
		if (intValue != null) {

			switch (op) {

			case EQ:
				where.add(singleFilterEQ(field, intValue));
				break;
			case GE:
				where.add(singleFilterGE(field, intValue));
				break;
			case LE:
				where.add(singleFilterLE(field, intValue));
				break;
			case GT:
				where.add(singleFilterGT(field, intValue));
				break;
			case LT:
				where.add(singleFilterLT(field, intValue));
				break;
			case NE:
				where.add(singleFilterNE(field, intValue));
				break;
			}
		}
	}

	protected void singleFilter(List<String> where, String field, String op,
			Byte byteValue) {
		if (byteValue != null) {

			switch (op) {

			case EQ:
				where.add(singleFilterEQ(field, byteValue));
				break;
			case GE:
				where.add(singleFilterGE(field, byteValue));
				break;
			case LE:
				where.add(singleFilterLE(field, byteValue));
				break;
			case GT:
				where.add(singleFilterGT(field, byteValue));
				break;
			case LT:
				where.add(singleFilterLT(field, byteValue));
				break;
			case NE:
				where.add(singleFilterNE(field, byteValue));
				break;
			}
		}
	}

	protected void singleFilter(List<String> where, String field, String op,
			String stringValue) {
		if (stringValue != null) {

			switch (op) {

			case EQ:
				where.add(singleFilterEQ(field, stringValue));
				break;
			case GE:
				where.add(singleFilterGE(field, stringValue));
				break;
			case LE:
				where.add(singleFilterLE(field, stringValue));
				break;
			case GT:
				where.add(singleFilterGT(field, stringValue));
				break;
			case LT:
				where.add(singleFilterLT(field, stringValue));
				break;
			case NE:
				where.add(singleFilterNE(field, stringValue));
				break;
			}
		}
	}

	protected String singleFilterEQ(String field, Long longValue) {
		return field + EQ + longValue;
	}

	protected String singleFilterEQ(String field, Integer intValue) {
		return field + EQ + intValue;
	}

	protected String singleFilterEQ(String field, Byte byteValue) {
		return field + EQ + byteValue;
	}

	protected String singleFilterEQ(String field, String stringValue) {
		return field + EQ_STRING + stringValue + S_QUOTES;
	}

	protected String singleFilterNE(String field, Long longValue) {
		return field + NE + longValue;
	}

	protected String singleFilterNE(String field, Integer intValue) {
		return field + NE + intValue;
	}

	protected String singleFilterNE(String field, Byte byteValue) {
		return field + NE + byteValue;
	}

	protected String singleFilterNE(String field, String stringValue) {
		return field + NE_STRING + stringValue + S_QUOTES;
	}

	protected String singleFilterLT(String field, Long longValue) {
		return field + LT + longValue;
	}

	protected String singleFilterLT(String field, Integer intValue) {
		return field + LT + intValue;
	}

	protected String singleFilterLT(String field, Byte byteValue) {
		return field + LT + byteValue;
	}

	protected String singleFilterLT(String field, String stringValue) {
		return field + LT_STRING + stringValue + S_QUOTES;
	}

	protected String singleFilterGT(String field, Long longValue) {
		return field + GT + longValue;
	}

	protected String singleFilterGT(String field, Integer intValue) {
		return field + GT + intValue;
	}

	protected String singleFilterGT(String field, Byte byteValue) {
		return field + GT + byteValue;
	}

	protected String singleFilterGT(String field, String stringValue) {
		return field + GT_STRING + stringValue + S_QUOTES;
	}

	protected String singleFilterLE(String field, Long longValue) {
		return field + LE + longValue;
	}

	protected String singleFilterLE(String field, Integer intValue) {
		return field + LE + intValue;
	}

	protected String singleFilterLE(String field, Byte byteValue) {
		return field + LE + byteValue;
	}

	protected String singleFilterLE(String field, String stringValue) {
		return field + LE_STRING + stringValue + S_QUOTES;
	}

	protected String singleFilterGE(String field, Long longValue) {
		return field + GE + longValue;
	}

	protected String singleFilterGE(String field, Integer intValue) {
		return field + GE + intValue;
	}

	protected String singleFilterGE(String field, Byte byteValue) {
		return field + GE + byteValue;
	}

	protected String singleFilterGE(String field, String stringValue) {
		return field + GE_STRING + stringValue + S_QUOTES;
	}

}
