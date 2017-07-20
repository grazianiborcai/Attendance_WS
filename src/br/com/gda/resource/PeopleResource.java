package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.PeopleModel;

@Path("/People")
public class PeopleResource {

	private static final String SELECT_People_TEXT = "/loginPeople";

	@GET
	@Path(SELECT_People_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginPeople(@HeaderParam("email") String email, @HeaderParam("password") String password) {

		return new PeopleModel().selectPeopleResponse(email, password);
	}

}
