package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.StoreEmployeeModel;

@Path("/StoreEmployee")
public class StoreEmployeeResource {

	private static final String INSERT_STORE_EMPLOYEE = "/insertStoreEmployee";
	private static final String UPDATE_STORE_EMPLOYEE = "/updateStoreEmployee";
	private static final String DELETE_STORE_EMPLOYEE = "/deleteStoreEmployee";
	private static final String SELECT_STORE_EMPLOYEE = "/selectStoreEmployee";

	@POST
	@Path(INSERT_STORE_EMPLOYEE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertStoreEmployee(String incomingData) {

		return new StoreEmployeeModel().insertStoreEmployee(incomingData);
	}

	@POST
	@Path(UPDATE_STORE_EMPLOYEE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStoreEmployee(String incomingData) {

		return new StoreEmployeeModel().updateStoreEmployee(incomingData);
	}

	@DELETE
	@Path(DELETE_STORE_EMPLOYEE)
	public Response deleteStoreEmployee(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codStore") List<Integer> codStore, @QueryParam("codEmployee") List<Integer> codEmployee,
			@QueryParam("codPosition") List<Byte> codPosition, @QueryParam("recordMode") List<String> recordMode) {

		return new StoreEmployeeModel().deleteStoreEmployee(codOwner, codStore, codEmployee, codPosition, recordMode);
	}

	@GET
	@Path(SELECT_STORE_EMPLOYEE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectStoreEmployee(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codStore") List<Integer> codStore, @QueryParam("codEmployee") List<Integer> codEmployee,
			@QueryParam("cpf") List<String> cpf, @QueryParam("password") List<String> password,
			@QueryParam("name") List<String> name, @QueryParam("codPosition") List<Byte> codPosition,
			@QueryParam("codGender") List<Byte> codGender, @QueryParam("bornDate") List<String> bornDate,
			@QueryParam("email") List<String> email, @QueryParam("address1") List<String> address1,
			@QueryParam("address2") List<String> address2, @QueryParam("postalcode") List<Integer> postalcode,
			@QueryParam("city") List<String> city, @QueryParam("country") List<String> country,
			@QueryParam("state") List<String> state, @QueryParam("phone") List<String> phone,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode) {

		return new StoreEmployeeModel().selectStoreEmployeeResponse(codOwner, codStore, codEmployee, cpf, password,
				name, codPosition, codGender, bornDate, email, address1, address2, postalcode, city, country, state,
				phone, recordMode);
	}

}
