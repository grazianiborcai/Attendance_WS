package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.GenderModel;

@Path("/Gender")
public class GenderResource {

	private static final String SELECT_Gender_TEXT = "/selectGender";

	@GET
	@Path(SELECT_Gender_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectGenderText() {

		return new GenderModel().selectGenderResponse();
	}

}
