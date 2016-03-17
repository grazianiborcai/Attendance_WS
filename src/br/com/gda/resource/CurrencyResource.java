package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CurrencyModel;

@Path("/Currency")
public class CurrencyResource {

	private static final String SELECT_CURRENCY = "/selectCurrency/{baseCode}/{language}";

	@GET
	@Path(SELECT_CURRENCY)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCategory(@PathParam("baseCode") String baseCode, @PathParam("language") String language) {

		return new CurrencyModel().selectCurrencyResponse(baseCode, language);
	}

}
