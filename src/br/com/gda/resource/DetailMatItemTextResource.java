package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.DetailMatItemTextModel;

@Path("/DetailMatItemText")
public class DetailMatItemTextResource {

	private static final String INSERT_DETAIL_MAT_ITEM_TEXT = "/insertDetailMatItemText";
	private static final String UPDATE_DETAIL_MAT_ITEM_TEXT = "/updateDetailMatItemText";
	private static final String DELETE_DETAIL_MAT_ITEM_TEXT = "/deleteDetailMatItemText";
	private static final String SELECT_DETAIL_MAT_ITEM_TEXT = "/selectDetailMatItemText";

	@POST
	@Path(INSERT_DETAIL_MAT_ITEM_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertDetailMatItemText(String incomingData) {

		return new DetailMatItemTextModel().insertDetailMatItemText(incomingData);
	}

	@POST
	@Path(UPDATE_DETAIL_MAT_ITEM_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDetailMatItemText(String incomingData) {

		return new DetailMatItemTextModel().updateDetailMatItemText(incomingData);
	}

	@DELETE
	@Path(DELETE_DETAIL_MAT_ITEM_TEXT)
	public Response deleteDetailMatItemText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail, @QueryParam("codItem") List<Integer> codItem,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name,
			@QueryParam("description") List<String> description, @QueryParam("textLong") List<String> textLong) {

		return new DetailMatItemTextModel().deleteDetailMatItemText(codOwner, codDetail, codItem, language, name,
				description, textLong);
	}

	@GET
	@Path(SELECT_DETAIL_MAT_ITEM_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectDetailMatItemText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail, @QueryParam("codItem") List<Integer> codItem,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name,
			@QueryParam("description") List<String> description, @QueryParam("textLong") List<String> textLong) {

		return new DetailMatItemTextModel().selectDetailMatItemTextResponse(codOwner, codDetail, codItem, language,
				name, description, textLong);
	}

}
