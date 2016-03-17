package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.GenderModel;

@Path("/Gender")
public class GenderResource {

	private static final String SELECT_GENDER = "/selectGender";

	@GET
	@Path(SELECT_GENDER)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectGender(
			@QueryParam("codGender") List<Integer> codGender,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new GenderModel().selectGenderResponse(codGender,
				language, name);
	}

}
