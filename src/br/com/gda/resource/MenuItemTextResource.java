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

import br.com.gda.model.MenuItemTextModel;

@Path("/MenuItemText")
public class MenuItemTextResource {

	private static final String INSERT_MENU_ITEM_TEXT = "/insertMenuItemText";
	private static final String UPDATE_MENU_ITEM_TEXT = "/updateMenuItemText";
	private static final String DELETE_MENU_ITEM_TEXT = "/deleteMenuItemText";
	private static final String SELECT_MENU_ITEM_TEXT = "/selectMenuItemText";

	@POST
	@Path(INSERT_MENU_ITEM_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMenuItemText(String incomingData) {

		return new MenuItemTextModel().insertMenuItemText(incomingData);
	}

	@POST
	@Path(UPDATE_MENU_ITEM_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMenuItemText(String incomingData) {

		return new MenuItemTextModel().updateMenuItemText(incomingData);
	}

	@DELETE
	@Path(DELETE_MENU_ITEM_TEXT)
	public Response deleteMenuItemText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu, @QueryParam("item") List<Integer> item,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name) {

		return new MenuItemTextModel().deleteMenuItemText(codOwner, codMenu, item, language, name);
	}

	@GET
	@Path(SELECT_MENU_ITEM_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMenuItemText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu, @QueryParam("item") List<Integer> item,
			@QueryParam("language") List<String> language, @QueryParam("name") List<String> name) {

		return new MenuItemTextModel().selectMenuItemTextResponse(codOwner, codMenu, item, language, name);
	}

}
