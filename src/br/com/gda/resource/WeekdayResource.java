package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.WeekdayModel;

@Path("/Weekday")
public class WeekdayResource {

	private static final String SELECT_WEEKDAY = "/selectWeekday";

	@GET
	@Path(SELECT_WEEKDAY)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectWeekday(@QueryParam("weekday") List<Integer> weekday,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new WeekdayModel().selectWeekdayResponse(weekday,
				language, name);
	}

}
