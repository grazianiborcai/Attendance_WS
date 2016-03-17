package br.com.gda.resource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import br.com.gda.encrypt.MD5;

@Path("/Token")
public class TokenResource {

	private static final String GET_TOKEN = "getToken";

	@GET
	@Path(GET_TOKEN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectMaterial(@HeaderParam("path") String path, @HeaderParam("Authorization") String auth,
			@DefaultValue("Z") @HeaderParam("zoneId") String zoneId) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("token", new MD5().token(path, auth, zoneId));

		return Response.status(Response.Status.OK).entity(jsonObject.toString()).type(MediaType.APPLICATION_JSON)
				.build();

	}

}
