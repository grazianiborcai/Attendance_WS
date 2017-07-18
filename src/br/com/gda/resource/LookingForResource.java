package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.LookingForModel;

@Path("/LookingFor")
public class LookingForResource {

	private static final String SELECT_LookingFor_TEXT = "/selectLookingFor";

	@GET
	@Path(SELECT_LookingFor_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectLookingForText() {

		return new LookingForModel().selectLookingForResponse();
	}

}
