package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.LanguageModel;

@Path("/Language")
public class LanguageResource {

	private static final String SELECT_LANGUAGE = "/selectLanguage";

	@GET
	@Path(SELECT_LANGUAGE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectLanguage(
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new LanguageModel().selectLanguageResponse(language, name);
	}

}
