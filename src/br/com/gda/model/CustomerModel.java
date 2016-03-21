package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.gda.dao.CustomerDAO;
import br.com.gda.helper.Customer;

public class CustomerModel extends JsonBuilder {

	public Response insertCustomer(String incomingData) {

		SQLException exception = new CustomerDAO().insertCustomer(jsonToCustomerList(incomingData));

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public Response updateCustomer(String incomingData) {

		SQLException exception = new CustomerDAO().updateCustomer(jsonToCustomerList(incomingData));

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public Response changePassword(Long codCustomer, String newPassword) {

		SQLException exception = new CustomerDAO().changePassword(codCustomer, newPassword);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public Response deleteCustomer(List<Long> codCustomer, List<String> phone, List<String> password, List<String> name,
			List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email, List<String> address1,
			List<String> address2, List<Integer> postalcode, List<String> city, List<String> country,
			List<String> state) {

		SQLException exception = new CustomerDAO().deleteCustomer(codCustomer, phone, password, name, codGender, cpf,
				bornDate, email, address1, address2, postalcode, city, country, state);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<Customer> selectCustomer(List<Long> codCustomer, List<String> phone, List<String> password,
			List<String> name, List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state) throws SQLException {

		return new CustomerDAO().selectCustomer(codCustomer, phone, password, name, codGender, cpf, bornDate, email,
				address1, address2, postalcode, city, country, state);
	}

	public JsonObject selectCustomerJson(List<Long> codCustomer, List<String> phone, List<String> password,
			List<String> name, List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectCustomer(codCustomer, phone, password, name, codGender, cpf,
					bornDate, email, address1, address2, postalcode, city, country, state));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCustomerResponse(List<Long> codCustomer, List<String> phone, List<String> password,
			List<String> name, List<Byte> codGender, List<String> cpf, List<String> bornDate, List<String> email,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state) {

		return response(selectCustomerJson(codCustomer, phone, password, name, codGender, cpf, bornDate, email,
				address1, address2, postalcode, city, country, state));
	}

	public JsonObject selectCustomerJson(String email, String password) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		ArrayList<Customer> customerList = new ArrayList<Customer>();

		try {

			List<String> emailList = new ArrayList<String>();
			emailList.add(email);

			List<String> passwordList = new ArrayList<String>();
			passwordList.add(password);

			CustomerDAO customerDAO = new CustomerDAO();

			customerList = customerDAO.selectCustomer(null, null, passwordList, null, null, null, null, emailList, null,
					null, null, null, null, null);

			if (customerList == null || customerList.size() == 0) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
//				exception = new SQLException(email+" : "+password+" : "+emailList.get(0), null, 200);
			}

			jsonElement = new Gson().toJsonTree(customerList);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCustomerResponse(String email, String password) {

		return response(selectCustomerJson(email, password));
	}

	private ArrayList<Customer> jsonToCustomerList(String incomingData) {

		ArrayList<Customer> customerList = new ArrayList<Customer>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				customerList.add(gson.fromJson(array.get(i), Customer.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			customerList.add(gson.fromJson(object, Customer.class));
		}

		return customerList;
	}

}
