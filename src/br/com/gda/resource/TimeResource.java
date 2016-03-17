package br.com.gda.resource;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

@Path("/Time")
public class TimeResource {

	private static final String GET_TIME = "getTime";

	@GET
	@Path(GET_TIME)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectTime() {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("zoneId", "Z");
		jsonObject.addProperty("time", ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().toString());

		return Response.status(Response.Status.OK).entity(jsonObject.toString()).type(MediaType.APPLICATION_JSON)
				.build();

	}

}
