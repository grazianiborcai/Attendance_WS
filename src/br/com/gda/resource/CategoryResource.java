package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.CategoryModel;

@Path("/Category")
public class CategoryResource {

	private static final String SELECT_CATEGORY = "/selectCategory";

	@GET
	@Path(SELECT_CATEGORY)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectCategory(
			@QueryParam("codCategory") List<Integer> codCategory,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new CategoryModel().selectCategoryResponse(codCategory,
				language, name);
	}

}
