package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CountryModel;

@Path("/Country")
public class CountryResource {

	private static final String SELECT_COUNTRY = "/selectCountry";

	@GET
	@Path(SELECT_COUNTRY)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCountry(@QueryParam("country") List<String> country,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new CountryModel().selectCountryResponse(country,
				language, name);
	}

}
