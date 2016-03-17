package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.EmployeeDAO;
import br.com.gda.helper.Employee;
import br.com.gda.helper.RecordMode;

public class EmployeeModel extends JsonBuilder {

	private static final String EMPLOYEE = "Employee";
	private ArrayList<Employee> employeeList = new ArrayList<Employee>();

	public Response insertEmployee(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<Employee> employeeList = (ArrayList<Employee>) jsonToObjectList(incomingData, Employee.class);

		SQLException exception = new EmployeeDAO().insertEmployee(employeeList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = employeeList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectEmployeeJson(codOwner, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, recordMode, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateEmployee(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<Employee> employeeList = (ArrayList<Employee>) jsonToObjectList(incomingData, Employee.class);

		SQLException exception = new EmployeeDAO().updateEmployee(employeeList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = employeeList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectEmployeeJson(codOwner, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, recordMode, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response deleteEmployee(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf,
			List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) {

		SQLException exception = new EmployeeDAO().deleteEmployee(codOwner, codEmployee, cpf, password, name,
				codPosition, codGender, bornDate, email, address1, address2, postalcode, city, country, state, phone,
				recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<Employee> selectEmployee(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf,
			List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) throws SQLException {

		return this.setEmployeeList(
				new EmployeeDAO().selectEmployee(codOwner, codEmployee, cpf, password, name, codPosition, codGender,
						bornDate, email, address1, address2, postalcode, city, country, state, phone, recordMode));

	}

	public JsonObject selectEmployeeJson(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf,
			List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode, boolean runMsg) {

		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			employeeList = selectEmployee(codOwner, codEmployee, cpf, password, name, codPosition, codGender, bornDate,
					email, address1, address2, postalcode, city, country, state, phone, recordMode);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(employeeList, exception);

		if (runMsg)
			sendMessage(employeeList, exception, codOwner, OWNER, EMPLOYEE);

		return jsonObject;
	}

	public Response selectEmployeeResponse(List<Long> codOwner, List<Integer> codEmployee, List<String> cpf,
			List<String> password, List<String> name, List<Byte> codPosition, List<Byte> codGender,
			List<String> bornDate, List<String> email, List<String> address1, List<String> address2,
			List<Integer> postalcode, List<String> city, List<String> country, List<String> state, List<String> phone,
			List<String> recordMode) {

		return response(selectEmployeeJson(codOwner, codEmployee, cpf, password, name, codPosition, codGender, bornDate,
				email, address1, address2, postalcode, city, country, state, phone, recordMode, false));
	}

	public ArrayList<Employee> getEmployeeList() {
		return employeeList;
	}

	public ArrayList<Employee> setEmployeeList(ArrayList<Employee> employeeList) {
		this.employeeList = employeeList;
		return employeeList;
	}

}
