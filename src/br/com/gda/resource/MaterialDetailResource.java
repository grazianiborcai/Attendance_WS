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

import br.com.gda.model.MaterialDetailModel;

@Path("/MaterialDetail")
public class MaterialDetailResource {

	private static final String INSERT_MATERIAL_DETAIL = "/insertMaterialDetail";
	private static final String UPDATE_MATERIAL_DETAIL = "/updateMaterialDetail";
	private static final String DELETE_MATERIAL_DETAIL = "/deleteMaterialDetail";
	private static final String SELECT_MATERIAL_DETAIL = "/selectMaterialDetail";

	@POST
	@Path(INSERT_MATERIAL_DETAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMaterialDetail(String incomingData) {

		return new MaterialDetailModel().insertMaterialDetail(incomingData);
	}

	@POST
	@Path(UPDATE_MATERIAL_DETAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMaterialDetail(String incomingData) {

		return new MaterialDetailModel().updateMaterialDetail(incomingData);
	}

	@DELETE
	@Path(DELETE_MATERIAL_DETAIL)
	public Response deleteMaterialDetail(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("codDetail") List<Integer> codDetail,
			@QueryParam("recordMode") List<String> recordMode) {

		return new MaterialDetailModel().deleteMaterialDetail(codOwner, codMaterial, codDetail, recordMode);
	}

	@GET
	@Path(SELECT_MATERIAL_DETAIL)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMaterialDetail(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("codDetail") List<Integer> codDetail,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name) {

		return new MaterialDetailModel().selectMaterialDetailResponse(codOwner, codMaterial, codDetail, recordMode,
				language, name);
	}

}
