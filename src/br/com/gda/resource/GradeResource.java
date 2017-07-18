package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.GradeModel;

@Path("/Grade")
public class GradeResource {

	private static final String SELECT_Grade_TEXT = "/selectGrade";

	@GET
	@Path(SELECT_Grade_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectGradeText() {

		return new GradeModel().selectGradeResponse();
	}

}
