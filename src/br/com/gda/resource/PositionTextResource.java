package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.PositionTextModel;

@Path("/PositionText")
public class PositionTextResource {

	private static final String SELECT_POSITION_TEXT = "/selectPositionText";

	@GET
	@Path(SELECT_POSITION_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectPositionText(
			@QueryParam("codPosition") List<Integer> codPosition,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new PositionTextModel().selectPositionTextResponse(
				codPosition, language, name);
	}

}
