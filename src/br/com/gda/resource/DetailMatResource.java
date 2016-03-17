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

import br.com.gda.model.DetailMatModel;

@Path("/DetailMat")
public class DetailMatResource {

	private static final String INSERT_DETAIL_MAT = "/insertDetailMat";
	private static final String UPDATE_DETAIL_MAT = "/updateDetailMat";
	private static final String DELETE_DETAIL_MAT = "/deleteDetailMat";
	private static final String SELECT_DETAIL_MAT = "/selectDetailMat";

	@POST
	@Path(INSERT_DETAIL_MAT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertDetailMat(String incomingData) {

		return new DetailMatModel().insertDetailMat(incomingData);
	}

	@POST
	@Path(UPDATE_DETAIL_MAT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDetailMat(String incomingData) {

		return new DetailMatModel().updateDetailMat(incomingData);
	}

	@DELETE
	@Path(DELETE_DETAIL_MAT)
	public Response deleteDetailMat(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail, @QueryParam("recordMode") List<String> recordMode) {

		return new DetailMatModel().deleteDetailMat(codOwner, codDetail, recordMode);
	}

	@GET
	@Path(SELECT_DETAIL_MAT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectDetailMat(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codDetail") List<Integer> codDetail,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name) {

		return new DetailMatModel().selectDetailMatResponse(codOwner, codDetail, recordMode, language, name);
	}

}
