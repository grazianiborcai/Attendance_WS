package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CategoryTextModel;

@Path("/CategoryText")
public class CategoryTextResource {

	private static final String SELECT_CATEGORY_TEXT = "/selectCategoryText";

	@GET
	@Path(SELECT_CATEGORY_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCategoryText(
			@QueryParam("codCategory") List<Integer> codCategory,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new CategoryTextModel().selectCategoryTextResponse(
				codCategory, language, name);
	}

}
