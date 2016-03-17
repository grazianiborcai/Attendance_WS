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

import br.com.gda.model.MenuItemModel;

@Path("/MenuItem")
public class MenuItemResource {

	private static final String INSERT_MENU_ITEM = "/insertMenuItem";
	private static final String UPDATE_MENU_ITEM = "/updateMenuItem";
	private static final String DELETE_MENU_ITEM = "/deleteMenuItem";
	private static final String SELECT_MENU_ITEM = "/selectMenuItem";

	@POST
	@Path(INSERT_MENU_ITEM)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMenuItem(String incomingData) {

		return new MenuItemModel().insertMenuItem(incomingData);
	}

	@POST
	@Path(UPDATE_MENU_ITEM)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMenuItem(String incomingData) {

		return new MenuItemModel().updateMenuItem(incomingData);
	}

	@DELETE
	@Path(DELETE_MENU_ITEM)
	public Response deleteMenuItem(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu, @QueryParam("item") List<Integer> item,
			@QueryParam("recordMode") List<String> recordMode) {

		return new MenuItemModel().deleteMenuItem(codOwner, codMenu, item, recordMode);
	}

	@GET
	@Path(SELECT_MENU_ITEM)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMenuItem(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu, @QueryParam("language") List<String> language,
			@DefaultValue(" ") @QueryParam("recordMode") List<String> recordMode) {

		return new MenuItemModel().selectMenuItemResponse(codOwner, null, codMenu, language, recordMode);
	}

}
