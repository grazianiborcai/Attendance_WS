package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.HowDiscoveredModel;

@Path("/HDiscovered")
public class HowDiscoveredResource {

	private static final String SELECT_HowDiscovered_TEXT = "/selectHowDiscovered";

	@GET
	@Path(SELECT_HowDiscovered_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectHowDiscoveredText() {

		return new HowDiscoveredModel().selectHowDiscoveredResponse();
	}

}
