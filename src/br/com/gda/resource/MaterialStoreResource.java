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

import br.com.gda.model.MaterialStoreModel;

@Path("/MaterialStore")
public class MaterialStoreResource {

	private static final String INSERT_MATERIAL_STORE = "/insertMaterialStore";
	private static final String UPDATE_MATERIAL_STORE = "/updateMaterialStore";
	private static final String DELETE_MATERIAL_STORE = "/deleteMaterialStore";
	private static final String SELECT_MATERIAL_STORE = "/selectMaterialStore";

	@POST
	@Path(INSERT_MATERIAL_STORE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMaterialStore(String incomingData) {

		return new MaterialStoreModel().insertMaterialStore(incomingData);
	}

	@POST
	@Path(UPDATE_MATERIAL_STORE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMaterialStore(String incomingData) {

		return new MaterialStoreModel().updateMaterialStore(incomingData);
	}

	@DELETE
	@Path(DELETE_MATERIAL_STORE)
	public Response deleteMaterialStore(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("codStore") List<Integer> codStore,
			@QueryParam("recordMode") List<String> recordMode) {

		return new MaterialStoreModel().deleteMaterialStore(codOwner, codMaterial, codStore, recordMode);
	}

	@GET
	@Path(SELECT_MATERIAL_STORE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMaterialStore(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("codStore") List<Integer> codStore,
			@QueryParam("codCategory") List<Integer> codCategory, @QueryParam("codType") List<Integer> codType,
			@QueryParam("image") List<String> image, @QueryParam("barCode") List<String> barCode,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name,
			@QueryParam("description") List<String> description, @QueryParam("textLong") List<String> textLong) {

		return new MaterialStoreModel().selectMaterialStoreResponse(codOwner, codMaterial, codStore, codCategory,
				codType, image, barCode, recordMode, language, name, description, textLong);
	}

}
