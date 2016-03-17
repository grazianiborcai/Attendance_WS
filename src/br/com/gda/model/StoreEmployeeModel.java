package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.StoreEmployeeDAO;
import br.com.gda.helper.StoreEmployee;
import br.com.gda.helper.RecordMode;

public class StoreEmployeeModel extends JsonBuilder {

	private static final String STORE_EMPLOYEE = "StoreEmployee";
	private ArrayList<StoreEmployee> storeEmployeeList = new ArrayList<StoreEmployee>();

	public Response insertStoreEmployee(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<StoreEmployee> storeEmployeeList = (ArrayList<StoreEmployee>) jsonToObjectList(incomingData,
				StoreEmployee.class);

		SQLException exception = new StoreEmployeeDAO().insertStoreEmployee(storeEmployeeList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = storeEmployeeList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codStore = storeEmployeeList.stream().map(m -> m.getCodStore()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectStoreEmployeeJson(codOwner, codStore, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null, null, recordMode, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateStoreEmployee(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<StoreEmployee> storeEmployeeList = (ArrayList<StoreEmployee>) jsonToObjectList(incomingData,
				StoreEmployee.class);

		SQLException exception = new StoreEmployeeDAO().updateStoreEmployee(storeEmployeeList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = storeEmployeeList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codStore = storeEmployeeList.stream().map(m -> m.getCodStore()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectStoreEmployeeJson(codOwner, codStore, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null, null, recordMode, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response deleteStoreEmployee(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<Byte> codPosition, List<String> recordMode) {

		SQLException exception = new StoreEmployeeDAO().deleteStoreEmployee(codOwner, codStore, codEmployee,
				codPosition, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<StoreEmployee> selectStoreEmployee(List<Long> codOwner, List<Integer> codStore,
			List<Integer> codEmployee, List<String> cpf, List<String> password, List<String> name,
			List<Byte> codPosition, List<Byte> codGender, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> recordMode) throws SQLException {

		return this.setStoreEmployeeList(new StoreEmployeeDAO().selectStoreEmployee(codOwner, codStore, codEmployee,
				cpf, password, name, codPosition, codGender, bornDate, email, address1, address2, postalcode, city,
				country, state, phone, recordMode));

	}

	public JsonObject selectStoreEmployeeJson(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> cpf, List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode, boolean runMsg) {

		ArrayList<StoreEmployee> storeEmployeeList = new ArrayList<StoreEmployee>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			storeEmployeeList = selectStoreEmployee(codOwner, codStore, codEmployee, cpf, password, name, codPosition,
					codGender, bornDate, email, address1, address2, postalcode, city, country, state, phone,
					recordMode);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(storeEmployeeList, exception);

		if (runMsg)
			sendMessage(storeEmployeeList, exception, codStore, STORE, STORE_EMPLOYEE);

		return jsonObject;
	}

	public Response selectStoreEmployeeResponse(List<Long> codOwner, List<Integer> codStore, List<Integer> codEmployee,
			List<String> cpf, List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) {

		return response(selectStoreEmployeeJson(codOwner, codStore, codEmployee, cpf, password, name, codPosition,
				codGender, bornDate, email, address1, address2, postalcode, city, country, state, phone, recordMode,
				false));
	}

	public ArrayList<StoreEmployee> getStoreEmployeeList() {
		return storeEmployeeList;
	}

	public ArrayList<StoreEmployee> setStoreEmployeeList(ArrayList<StoreEmployee> storeEmployeeList) {
		this.storeEmployeeList = storeEmployeeList;
		return storeEmployeeList;
	}

}
