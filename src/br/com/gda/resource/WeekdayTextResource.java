package br.com.gda.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.WeekdayTextModel;

@Path("/WeekdayText")
public class WeekdayTextResource {

	private static final String SELECT_WEEKDAY_TEXT = "/selectWeekdayText";

	@GET
	@Path(SELECT_WEEKDAY_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectWeekdayText(
			@QueryParam("weekday") List<Integer> weekday,
			@QueryParam("language") List<String> language,
			@QueryParam("name") List<String> name) {

		return new WeekdayTextModel().selectWeekdayTextResponse(weekday,
				language, name);
	}

}
