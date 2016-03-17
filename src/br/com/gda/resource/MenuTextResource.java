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

import br.com.gda.model.MenuTextModel;

@Path("/MenuText")
public class MenuTextResource {

	private static final String INSERT_MENU_TEXT = "/insertMenuText";
	private static final String UPDATE_MENU_TEXT = "/updateMenuText";
	private static final String DELETE_MENU_TEXT = "/deleteMenuText";
	private static final String SELECT_MENU_TEXT = "/selectMenuText";

	@POST
	@Path(INSERT_MENU_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMenuText(String incomingData) {

		return new MenuTextModel().insertMenuText(incomingData);
	}

	@POST
	@Path(UPDATE_MENU_TEXT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMenuText(String incomingData) {

		return new MenuTextModel().updateMenuText(incomingData);
	}

	@DELETE
	@Path(DELETE_MENU_TEXT)
	public Response deleteMenuText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu, @QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new MenuTextModel().deleteMenuText(codOwner, codMenu, language, name);
	}

	@GET
	@Path(SELECT_MENU_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMenuText(@HeaderParam("codOwner") List<Long> codOwner,
			@QueryParam("codMenu") List<Integer> codMenu, @QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new MenuTextModel().selectMenuTextResponse(codOwner, codMenu, language, name);
	}

}
