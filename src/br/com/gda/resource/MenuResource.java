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

import br.com.gda.model.MenuModel;

@Path("/Menu")
public class MenuResource {

	private static final String INSERT_MENU = "/insertMenu";
	private static final String UPDATE_MENU = "/updateMenu";
	private static final String DELETE_MENU = "/deleteMenu";
	private static final String SELECT_MENU = "/selectMenu";

	@POST
	@Path(INSERT_MENU)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMenu(String incomingData) {

		return new MenuModel().insertMenu(incomingData);
	}

	@POST
	@Path(UPDATE_MENU)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMenu(String incomingData) {

		return new MenuModel().updateMenu(incomingData);
	}

	@DELETE
	@Path(DELETE_MENU)
	public Response deleteMenu(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu, @QueryParam("recordMode") List<String> recordMode) {

		return new MenuModel().deleteMenu(codOwner, codMenu, recordMode);
	}

	@GET
	@Path(SELECT_MENU)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMenu(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name) {

		return new MenuModel().selectMenuResponse(codOwner, codMenu, recordMode, language, name);
	}

}
