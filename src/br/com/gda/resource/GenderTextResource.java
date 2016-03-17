package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.GenderTextModel;

@Path("/GenderText")
public class GenderTextResource {

	private static final String SELECT_GENDER_TEXT = "/selectGenderText";

	@GET
	@Path(SELECT_GENDER_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectGenderText(
			@QueryParam("codGender") List<Integer> codGender,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new GenderTextModel().selectGenderTextResponse(codGender,
				language, name);
	}

}
