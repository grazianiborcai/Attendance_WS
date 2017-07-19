package br.com.gda.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gda.model.EnrollmentModel;

@Path("/Enrollment")
public class EnrollmentResource {

	private static final String INSERT_ENROLLMENT = "/insertEnrollment";
	private static final String SELECT_ENROLLMENT = "/selectEnrollment";
	
	@POST
	@Path(INSERT_ENROLLMENT)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertEnrollment(String incomingData) {

		return new EnrollmentModel().insertEnrollment(incomingData);
	}

	@GET
	@Path(SELECT_ENROLLMENT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectEnrollmentText(@QueryParam("classID") Long classID) {

		return new EnrollmentModel().selectEnrollmentResponse(classID);
	}

}
