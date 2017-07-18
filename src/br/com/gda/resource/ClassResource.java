package br.com.gda.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.ClassModel;

@Path("/Class")
public class ClassResource {

	private static final String SELECT_Class_TEXT = "/selectClass";

	@GET
	@Path(SELECT_Class_TEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectClassText(@QueryParam("peopleID") Long peopleID) {

		return new ClassModel().selectClassResponse(peopleID);
	}

}
