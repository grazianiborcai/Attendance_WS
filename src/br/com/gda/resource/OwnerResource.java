package br.com.gda.resource;

import java.time.LocalDateTime;
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

import br.com.gda.model.OwnerModel;

@Path("/Owner")
public class OwnerResource {

	private static final String INSERT_OWNER = "/insertOwner";
	private static final String UPDATE_OWNER = "/updateOwner";
	private static final String DELETE_OWNER = "/deleteOwner";
	private static final String SELECT_OWNER = "/selectOwner";

	@POST
	@Path(INSERT_OWNER)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertOwner(String incomingData) {

		return new OwnerModel().insertOwner(incomingData);
	}

	@POST
	@Path(UPDATE_OWNER)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOwner(String incomingData) {

		return new OwnerModel().updateOwner(incomingData);
	}

	@DELETE
	@Path(DELETE_OWNER)
	public Response deleteOwner(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("password") List<String> password, @QueryParam("name") List<String> name,
			@QueryParam("cpf") List<String> cpf, @QueryParam("phone") List<String> phone,
			@QueryParam("email") List<String> email, @QueryParam("emailAux") List<String> emailAux,
			@QueryParam("codGender") List<Byte> codGender, @QueryParam("bornDate") List<String> bornDate,
			@QueryParam("postalcode") List<Integer> postalcode, @QueryParam("address1") List<String> address1,
			@QueryParam("address2") List<String> address2, @QueryParam("city") List<String> city,
			@QueryParam("country") List<String> country, @QueryParam("state") List<String> state,
			@QueryParam("codCurr") List<String> codCurr, @QueryParam("recordMode") List<String> recordMode) {

		return new OwnerModel().deleteOwner(codOwner, password, name, cpf, phone, email, emailAux, codGender, bornDate,
				postalcode, address1, address2, city, country, state, codCurr, recordMode);
	}

	@GET
	@Path(SELECT_OWNER)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectOwner(@DefaultValue("Z") @HeaderParam("zoneId") String zoneId,
			@HeaderParam("email") String email, @HeaderParam("password") String password,
			@HeaderParam("cpf") String cpf, @QueryParam("language") List<String> language,
			@DefaultValue("true") @QueryParam("withDetailMat") Boolean withDetailMat,
			@DefaultValue("true") @QueryParam("withMaterial") Boolean withMaterial,
			@DefaultValue("true") @QueryParam("withMenu") Boolean withMenu,
			@DefaultValue("true") @QueryParam("withStore") Boolean withStore,
			@DefaultValue("true") @QueryParam("withEmployee") Boolean withEmployee,
			@DefaultValue("true") @QueryParam("withStoreMenu") Boolean withStoreMenu,
			@DefaultValue("true") @QueryParam("withStoreMaterial") Boolean withStoreMaterial,
			@DefaultValue("true") @QueryParam("withStoreEmployee") Boolean withStoreEmployee,
			@DefaultValue("true") @QueryParam("withStoreTables") Boolean withStoreTables,
			@DefaultValue("true") @QueryParam("withStoreBill") Boolean withStoreBill) {
		
		System.out.println(LocalDateTime.now());
		
		Response r = new OwnerModel().selectOwnerResponse(email, cpf, password, language, withDetailMat, withMaterial,
				withMenu, withStore, withEmployee, withStoreMenu, withStoreMaterial, withStoreEmployee, withStoreTables,
				withStoreBill, zoneId);
	
		System.out.println(LocalDateTime.now());
		
		return r;
	}

}
