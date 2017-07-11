package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CountryTextModel;

@Path("/CountryText")
public class CountryTextResource {

	private static final String SELECT_COUNTRY_TEXT = "/selectCountryText";

	@GET
	@Path(SELECT_COUNTRY_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCountryText(
			@QueryParam("country") List<String> country,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new CountryTextModel().selectCountryTextResponse(country,
				language, name);
	}

}
