package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.StateModel;

@Path("/State")
public class StateResource {

	private static final String SELECT_STATE = "/selectState";

	@GET
	@Path(SELECT_STATE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectState(@QueryParam("country") List<String> country,
			@QueryParam("state") List<String> state,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new StateModel().selectStateResponse(country, state,
				language, name);
	}

}
