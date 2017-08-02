package br.com.gda.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.PeopleModel;

@Path("/People")
public class PeopleResource {

	private static final String LOGIN_PEOPLE = "/loginPeople";
	private static final String SELECT_PEOPLE = "/selectPeople";

	@GET
	@Path(LOGIN_PEOPLE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginPeople(@HeaderParam("email") String email, @HeaderParam("password") String password,
			@HeaderParam("user-agent") String userAgent, @Context HttpServletRequest request) {
		//System.out.println(request.getRemoteHost());
		return new PeopleModel().selectPeopleResponse(email, password, userAgent);
	}
	
	@GET
	@Path(SELECT_PEOPLE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectPeople(@HeaderParam("oAuth") String oAuth) {
		return new PeopleModel().selectPeopleResponse(oAuth);
	}

}
