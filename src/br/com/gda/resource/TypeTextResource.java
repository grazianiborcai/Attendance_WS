package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.TypeTextModel;

@Path("/TypeText")
public class TypeTextResource {

	private static final String SELECT_TYPE_TEXT = "/selectTypeText";

	@GET
	@Path(SELECT_TYPE_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectTypeText(
			@QueryParam("codType") List<Integer> codType,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new TypeTextModel().selectTypeTextResponse(codType,
				language, name);
	}

}
