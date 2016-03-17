package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CodePasswordModel;

@Path("/CodePassword")
public class CodePasswordResource {

	private static final String GET_CODE = "/getCode";

	@GET
	@Path(GET_CODE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginCustomer(@HeaderParam("email") String email, @HeaderParam("password") String password, @HeaderParam("name") String name) {

		return new CodePasswordModel().getCodeResponse(email, password, name);
	}

}
