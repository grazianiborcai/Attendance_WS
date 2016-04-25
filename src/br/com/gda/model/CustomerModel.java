package br.com.gda.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.gda.dao.CustomerDAO;
import br.com.gda.helper.Customer;
import br.com.gda.helper.RecordMode;
import moip.sdk.api.Phone;
import moip.sdk.base.APIContext;

public class CustomerModel extends JsonBuilder {

	public Response insertCustomer(String incomingData) {

		ArrayList<Customer> customerList = jsonToCustomerList(incomingData);

		SQLException exception = new CustomerDAO().insertCustomer(customerList);

		if (exception.getErrorCode() == 200) {

			ArrayList<Customer> customerUpdateList = new ArrayList<Customer>();

			ConcurrentHashMap<String, String> sdkConfig = new ConcurrentHashMap<String, String>();
			sdkConfig.put("mode", "sandbox");

			APIContext apiContext = new APIContext("8QLV3TOXIP0AND15ZOB5R4X5T0OYWHVR",
					"GLYGGCHTSEQO0LCUL9IJNQTEGNG2NZOHA53VRGYC", "OAuth cl6fpbl7fyqiqljnd8apq75satol8q9");
			apiContext.setConfigurationMap(sdkConfig);

			moip.sdk.api.Customer customerMoip = new moip.sdk.api.Customer();
			for (Customer customer : customerList) {
				customerMoip.setOwnId(customer.getCodCustomer().toString());
				customerMoip.setFullname(customer.getName());
				customerMoip.setEmail(customer.getEmail());
				customerMoip.setBirthDate(customer.getBornDate().toString());
				String phone = customer.getPhone();

				customerMoip.setPhone(new Phone().setCountryCode("55").setAreaCode(phone.substring(1, 3))
						.setNumber(phone.substring(5, 14)));

				moip.sdk.api.Customer customerMoipCreated;
				try {
					customerMoipCreated = customerMoip.create(apiContext);

					String id = customerMoipCreated.getId();

					customer.setCodPayment(id);
					customer.setRecordMode(RecordMode.ISUPDATED);

					customerUpdateList.add(customer);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return Response.status(Response.Status.OK).entity(e.getMessage()).type(MediaType.APPLICATION_JSON)
							.build();
				}
			}

			SQLException exceptionUpdate = new CustomerDAO().updateCustomer(customerUpdateList);

			if (exceptionUpdate.getErrorCode() != 200)
				exceptionUpdate.printStackTrace();

		}

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public Response updateCustomer(String incomingData, Long codCustomer, String email, String password,
			String codPayment) {

		ArrayList<Customer> customerList = jsonToCustomerList(incomingData);

		customerList.get(0).setCodCustomer(codCustomer);
		customerList.get(0).setEmail(email);
		customerList.get(0).setCodPayment(codPayment);

		if (customerList.get(0).getPassword() == null || customerList.get(0).getPassword().isEmpty())
			customerList.get(0).setPassword(password);

		SQLException exception = new CustomerDAO().updateCustomer(customerList);

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
				// exception = new SQLException(email+" : "+password+" :
				// "+emailList.get(0), null, 200);
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
