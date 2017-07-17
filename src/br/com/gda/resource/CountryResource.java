package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CountryModel;

@Path("/Country")
public class CountryResource {

	private static final String SELECT_COUNTRY_TEXT = "/selectCountry";

	@GET
	@Path(SELECT_COUNTRY_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCountryText() {

		return new CountryModel().selectCountryResponse();
	}

}
