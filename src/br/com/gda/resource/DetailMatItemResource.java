package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.DetailMatItemModel;

@Path("/DetailMatItem")
public class DetailMatItemResource {

	private static final String INSERT_DETAIL_MAT_ITEM = "/insertDetailMatItem";
	private static final String UPDATE_DETAIL_MAT_ITEM = "/updateDetailMatItem";
	private static final String DELETE_DETAIL_MAT_ITEM = "/deleteDetailMatItem";
	private static final String SELECT_DETAIL_MAT_ITEM = "/selectDetailMatItem";

	@POST
	@Path(INSERT_DETAIL_MAT_ITEM)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertDetailMatItem(String incomingData) {

		return new DetailMatItemModel().insertDetailMatItem(incomingData);
	}

	@POST
	@Path(UPDATE_DETAIL_MAT_ITEM)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDetailMatItem(String incomingData) {

		return new DetailMatItemModel().updateDetailMatItem(incomingData);
	}

	@DELETE
	@Path(DELETE_DETAIL_MAT_ITEM)
	public Response deleteDetailMatItem(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail, @QueryParam("codItem") List<Integer> codItem,
			@QueryParam("recordMode") List<String> recordMode) {

		return new DetailMatItemModel().deleteDetailMatItem(codOwner, codDetail, codItem, recordMode);
	}

	@GET
	@Path(SELECT_DETAIL_MAT_ITEM)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectDetailMatItem(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail, @QueryParam("codItem") List<Integer> codItem,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name,
			@QueryParam("description") List<String> description, @QueryParam("textLong") List<String> textLong) {

		return new DetailMatItemModel().selectDetailMatItemResponse(codOwner, codDetail, codItem, recordMode, language,
				name, description, textLong);
	}

}
