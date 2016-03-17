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

import br.com.gda.model.DetailMatTextModel;

@Path("/DetailMatText")
public class DetailMatTextResource {

	private static final String INSERT_DETAIL_MAT_TEXT = "/insertDetailMatText";
	private static final String UPDATE_DETAIL_MAT_TEXT = "/updateDetailMatText";
	private static final String DELETE_DETAIL_MAT_TEXT = "/deleteDetailMatText";
	private static final String SELECT_DETAIL_MAT_TEXT = "/selectDetailMatText";

	@POST
	@Path(INSERT_DETAIL_MAT_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertDetailMatText(String incomingData) {

		return new DetailMatTextModel().insertDetailMatText(incomingData);
	}

	@POST
	@Path(UPDATE_DETAIL_MAT_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDetailMatText(String incomingData) {

		return new DetailMatTextModel().updateDetailMatText(incomingData);
	}

	@DELETE
	@Path(DELETE_DETAIL_MAT_TEXT)
	public Response deleteDetailMatText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail, @QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new DetailMatTextModel().deleteDetailMatText(codOwner, codDetail, language, name);
	}

	@GET
	@Path(SELECT_DETAIL_MAT_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectDetailMatText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail, @QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new DetailMatTextModel().selectDetailMatTextResponse(codOwner, codDetail, language, name);
	}

}
