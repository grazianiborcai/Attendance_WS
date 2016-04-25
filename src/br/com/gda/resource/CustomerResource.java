package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CustomerModel;

@Path("/Customer")
public class CustomerResource {

	private static final String INSERT_CUSTOMER = "/insertCustomer";
	private static final String UPDATE_CUSTOMER = "/updateCustomer";
	private static final String DELETE_CUSTOMER = "/deleteCustomer";
	private static final String SELECT_CUSTOMER = "/selectCustomer";
	private static final String LOGIN_CUSTOMER = "/loginCustomer";
	private static final String CHANGE_PASSWORD = "/changePassword";

	@POST
	@Path(INSERT_CUSTOMER)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertCustomer(String incomingData) {

		return new CustomerModel().insertCustomer(incomingData);
	}

	@POST
	@Path(UPDATE_CUSTOMER)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomer(String incomingData, @HeaderParam("codCustomer") Long codCustomer,
			@HeaderParam("email") String email, @HeaderParam("password") String password,
			@HeaderParam("codPayment") String codPayment) {

		return new CustomerModel().updateCustomer(incomingData, codCustomer, email, password, codPayment);
	}

	@DELETE
	@Path(DELETE_CUSTOMER)
	public Response deleteCustomer(@HeaderParam("codCustomer") List<Long> codCustomer,
			@QueryParam("phone") List<String> phone, @QueryParam("password") List<String> password,
			@QueryParam("name") List<String> name, @QueryParam("codGender") List<Byte> codGender,
			@QueryParam("cpf") List<String> cpf, @QueryParam("bornDate") List<String> bornDate,
			@QueryParam("email") List<String> email, @QueryParam("address1") List<String> address1,
			@QueryParam("address2") List<String> address2, @QueryParam("postalcode") List<Integer> postalcode,
			@QueryParam("city") List<String> city, @QueryParam("country") List<String> country,
			@QueryParam("state") List<String> state) {

		return new CustomerModel().deleteCustomer(codCustomer, phone, password, name, codGender, cpf, bornDate, email,
				address1, address2, postalcode, city, country, state);
	}

	@GET
	@Path(SELECT_CUSTOMER)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCustomer(@HeaderParam("codCustomer") List<Long> codCustomer,
			@QueryParam("phone") List<String> phone, @QueryParam("password") List<String> password,
			@QueryParam("name") List<String> name, @QueryParam("codGender") List<Byte> codGender,
			@QueryParam("cpf") List<String> cpf, @QueryParam("bornDate") List<String> bornDate,
			@QueryParam("email") List<String> email, @QueryParam("address1") List<String> address1,
			@QueryParam("address2") List<String> address2, @QueryParam("postalcode") List<Integer> postalcode,
			@QueryParam("city") List<String> city, @QueryParam("country") List<String> country,
			@QueryParam("state") List<String> state) {

		return new CustomerModel().selectCustomerResponse(codCustomer, phone, password, name, codGender, cpf, bornDate,
				email, address1, address2, postalcode, city, country, state);
	}

	@GET
	@Path(LOGIN_CUSTOMER)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginCustomer(@HeaderParam("email") String email, @HeaderParam("password") String password) {

		return new CustomerModel().selectCustomerResponse(email, password);
	}

	@GET
	@Path(CHANGE_PASSWORD)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@HeaderParam("codCustomer") Long codCustomer,
			@HeaderParam("newPassword") String newPassword) {

		return new CustomerModel().changePassword(codCustomer, newPassword);
	}

}
