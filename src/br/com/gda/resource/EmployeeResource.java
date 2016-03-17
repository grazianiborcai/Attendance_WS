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

import br.com.gda.model.EmployeeModel;

@Path("/Employee")
public class EmployeeResource {

	private static final String INSERT_EMPLOYEE = "/insertEmployee";
	private static final String UPDATE_EMPLOYEE = "/updateEmployee";
	private static final String DELETE_EMPLOYEE = "/deleteEmployee";
	private static final String SELECT_EMPLOYEE = "/selectEmployee";

	@POST
	@Path(INSERT_EMPLOYEE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertOwner(String incomingData) {

		return new EmployeeModel().insertEmployee(incomingData);
	}

	@POST
	@Path(UPDATE_EMPLOYEE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOwner(String incomingData) {

		return new EmployeeModel().updateEmployee(incomingData);
	}

	@DELETE
	@Path(DELETE_EMPLOYEE)
	public Response deleteEmployee(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codEmployee") List<Integer> codEmployee, @QueryParam("cpf") List<String> cpf,
			@QueryParam("password") List<String> password, @QueryParam("name") List<String> name,
			@QueryParam("codPosition") List<Byte> codPosition, @QueryParam("codGender") List<Byte> codGender,
			@QueryParam("bornDate") List<String> bornDate, @QueryParam("email") List<String> email,
			@QueryParam("address1") List<String> address1, @QueryParam("address2") List<String> address2,
			@QueryParam("postalcode") List<Integer> postalcode, @QueryParam("city") List<String> city,
			@QueryParam("country") List<String> country, @QueryParam("state") List<String> state,
			@QueryParam("phone") List<String> phone, @QueryParam("recordMode") List<String> recordMode) {

		return new EmployeeModel().deleteEmployee(codOwner, codEmployee, cpf, password, name, codPosition, codGender,
				bornDate, email, address1, address2, postalcode, city, country, state, phone, recordMode);
	}

	@GET
	@Path(SELECT_EMPLOYEE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectEmployee(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codEmployee") List<Integer> codEmployee, @QueryParam("cpf") List<String> cpf,
			@QueryParam("password") List<String> password, @QueryParam("name") List<String> name,
			@QueryParam("codPosition") List<Byte> codPosition, @QueryParam("codGender") List<Byte> codGender,
			@QueryParam("bornDate") List<String> bornDate, @QueryParam("email") List<String> email,
			@QueryParam("address1") List<String> address1, @QueryParam("address2") List<String> address2,
			@QueryParam("postalcode") List<Integer> postalcode, @QueryParam("city") List<String> city,
			@QueryParam("country") List<String> country, @QueryParam("state") List<String> state,
			@QueryParam("phone") List<String> phone,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode) {

		return new EmployeeModel().selectEmployeeResponse(codOwner, codEmployee, cpf, password, name, codPosition,
				codGender, bornDate, email, address1, address2, postalcode, city, country, state, phone, recordMode);
	}

}
