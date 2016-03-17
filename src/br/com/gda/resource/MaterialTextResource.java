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

import br.com.gda.model.MaterialTextModel;

@Path("/MaterialText")
public class MaterialTextResource {

	private static final String INSERT_MATERIAL_TEXT = "/insertMaterialText";
	private static final String UPDATE_MATERIAL_TEXT = "/updateMaterialText";
	private static final String DELETE_MATERIAL_TEXT = "/deleteMaterialText";
	private static final String SELECT_MATERIAL_TEXT = "/selectMaterialText";

	@POST
	@Path(INSERT_MATERIAL_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMaterialText(String incomingData) {

		return new MaterialTextModel().insertMaterialText(incomingData);
	}

	@POST
	@Path(UPDATE_MATERIAL_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMaterialText(String incomingData) {

		return new MaterialTextModel().updateMaterialText(incomingData);
	}

	@DELETE
	@Path(DELETE_MATERIAL_TEXT)
	public Response deleteMaterialText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name, @QueryParam("description") List<String> description,
			@QueryParam("textLong") List<String> textLong) {

		return new MaterialTextModel().deleteMaterialText(codOwner, codMaterial, language, name, description, textLong);
	}

	@GET
	@Path(SELECT_MATERIAL_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMaterialText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name, @QueryParam("description") List<String> description,
			@QueryParam("textLong") List<String> textLong) {

		return new MaterialTextModel().selectMaterialTextResponse(codOwner, codMaterial, language, name, description,
				textLong);
	}

}
