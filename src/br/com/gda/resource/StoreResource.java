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

import br.com.gda.model.StoreModel;

@Path("/Store")
public class StoreResource {

	private static final String INSERT_STORE = "/insertStore";
	private static final String UPDATE_STORE = "/updateStore";
	private static final String DELETE_STORE = "/deleteStore";
	private static final String SELECT_STORE = "/selectStore";
	private static final String SELECT_STORE_LOCATION = "/selectStoreLoc";

	@POST
	@Path(INSERT_STORE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertStore(@DefaultValue("Z") @HeaderParam("zoneId") String zoneId, String incomingData) {

		return new StoreModel().insertStore(incomingData, zoneId);
	}

	@POST
	@Path(UPDATE_STORE)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStore(@DefaultValue("Z") @HeaderParam("zoneId") String zoneId, String incomingData) {

		return new StoreModel().updateStore(incomingData, zoneId);
	}

	@DELETE
	@Path(DELETE_STORE)
	public Response deleteStore(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codStore") List<Integer> codStore, @QueryParam("cnpj") List<String> cnpj,
			@QueryParam("inscEstadual") List<String> inscEstadual,
			@QueryParam("inscMunicipal") List<String> inscMunicipal,
			@QueryParam("razaoSocial") List<String> razaoSocial, @QueryParam("name") List<String> name,
			@QueryParam("address1") List<String> address1, @QueryParam("address2") List<String> address2,
			@QueryParam("postalcode") List<Integer> postalcode, @QueryParam("city") List<String> city,
			@QueryParam("country") List<String> country, @QueryParam("state") List<String> state,
			@QueryParam("codCurr") List<String> codCurr, @QueryParam("recordMode") List<String> recordMode) {

		return new StoreModel().deleteStore(codOwner, codStore, cnpj, inscEstadual, inscMunicipal, razaoSocial, name,
				address1, address2, postalcode, city, country, state, codCurr, recordMode);
	}

	@GET
	@Path(SELECT_STORE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectStore(@DefaultValue("Z") @HeaderParam("zoneId") String zoneId,
			@HeaderParam("codOwner") List<Long> codOwner, @QueryParam("codStore") List<Integer> codStore,
			@QueryParam("cnpj") List<String> cnpj, @QueryParam("inscEstadual") List<String> inscEstadual,
			@QueryParam("inscMunicipal") List<String> inscMunicipal,
			@QueryParam("razaoSocial") List<String> razaoSocial, @QueryParam("name") List<String> name,
			@QueryParam("address1") List<String> address1, @QueryParam("address2") List<String> address2,
			@QueryParam("postalcode") List<Integer> postalcode, @QueryParam("city") List<String> city,
			@QueryParam("country") List<String> country, @QueryParam("state") List<String> state,
			@QueryParam("phone") List<String> phone, @QueryParam("codCurr") List<String> codCurr,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language,
			@DefaultValue("true") @QueryParam("withMaterial") Boolean withMaterial,
			@DefaultValue("true") @QueryParam("withEmployee") Boolean withEmployee) {

		return new StoreModel().selectStoreResponse(codOwner, codStore, cnpj, inscEstadual, inscMunicipal, razaoSocial,
				name, address1, address2, postalcode, city, country, state, phone, codCurr, recordMode, language,
				withMaterial, withEmployee, zoneId);
	}

	@GET
	@Path(SELECT_STORE_LOCATION)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectStoreLoc(@DefaultValue("Z") @HeaderParam("zoneId") String zoneId,
			@HeaderParam("codOwner") List<Long> codOwner, @QueryParam("codStore") List<Integer> codStore,
			@QueryParam("cnpj") List<String> cnpj, @QueryParam("inscEstadual") List<String> inscEstadual,
			@QueryParam("inscMunicipal") List<String> inscMunicipal,
			@QueryParam("razaoSocial") List<String> razaoSocial, @QueryParam("name") List<String> name,
			@QueryParam("address1") List<String> address1, @QueryParam("address2") List<String> address2,
			@QueryParam("postalcode") List<Integer> postalcode, @QueryParam("city") List<String> city,
			@QueryParam("country") List<String> country, @QueryParam("state") List<String> state,
			@QueryParam("phone") List<String> phone, @QueryParam("codCurr") List<String> codCurr,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language,
			@DefaultValue("true") @QueryParam("withMaterial") Boolean withMaterial,
			@DefaultValue("true") @QueryParam("withEmployee") Boolean withEmployee,
			@QueryParam("latitude") String latitude, @QueryParam("longitude") String longitude) {

		Float latitudeF = Float.parseFloat(latitude);
		Float longitudeF = Float.parseFloat(longitude);

		return new StoreModel().selectStoreResponseLoc(codOwner, codStore, cnpj, inscEstadual, inscMunicipal,
				razaoSocial, name, address1, address2, postalcode, city, country, state, phone, codCurr, recordMode,
				language, withMaterial, withEmployee, zoneId, latitudeF, longitudeF);
	}

}
