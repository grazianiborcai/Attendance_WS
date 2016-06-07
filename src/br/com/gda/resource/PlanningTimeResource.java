package br.com.gda.resource;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import br.com.gda.model.JsonBuilder;
import br.com.gda.model.PlanningTimeModel;

@Path("/PlanningTime")
public class PlanningTimeResource extends JsonBuilder {

	private static final String INSERT_PLANNINGTIME = "/insertPlanningTime";
	private static final String RESERVE_PLANNINGTIME = "/reservePlanningTime";
	private static final String RELEASE_PLANNINGTIME = "/releasePlanningTime";
	private static final String DELETE_PLANNINGTIME = "/deletePlanningTime";
	private static final String SELECT_PLANNINGTIME = "/selectPlanningTime";
	private static final String GET_CART = "/getCart";
	private static final String GET_BOOKED = "/getBooked";
	private static final String PAY_CART = "/payCart";
	private static final String REFUND = "/refund";
	private static final String GET_FEE = "/getFee";

	@POST
	@Path(INSERT_PLANNINGTIME)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertPlanningTime(String incomingData) {

		return new PlanningTimeModel().insertPlanningTime(incomingData);
	}

	@POST
	@Path(RESERVE_PLANNINGTIME)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response reservePlanningTime(String incomingData, @HeaderParam("codCustomer") Long codCustomer) {

		return new PlanningTimeModel().reservePlanningTime(incomingData, codCustomer);
	}

	@POST
	@Path(RELEASE_PLANNINGTIME)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response releasePlanningTime(String incomingData, @HeaderParam("codCustomer") Long codCustomer) {

		return new PlanningTimeModel().releasePlanningTime(incomingData, codCustomer);
	}

	@DELETE
	@Path(DELETE_PLANNINGTIME)
	public Response deletePlanningTime(@QueryParam("codOwner") List<Long> codOwner,
			@QueryParam("codStore") List<Integer> codStore, @QueryParam("codEmployee") List<Integer> codEmployee,
			@QueryParam("beginDate") List<String> beginDate, @QueryParam("beginTime") List<String> beginTime,
			@QueryParam("group") List<Integer> group, @QueryParam("weekday") List<Integer> weekday,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("recordMode") List<String> recordMode) {

		return new PlanningTimeModel().deletePlanningTime(codOwner, codStore, codEmployee, beginDate, beginTime, group,
				weekday, codMaterial, recordMode);
	}

	@GET
	@Path(SELECT_PLANNINGTIME)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectPlanningTime(@QueryParam("codOwner") List<Long> codOwner,
			@QueryParam("codStore") List<Integer> codStore, @QueryParam("codEmployee") List<Integer> codEmployee,
			@QueryParam("beginDate") List<String> beginDate, @QueryParam("beginTime") List<String> beginTime,
			@QueryParam("group") List<Integer> group, @QueryParam("weekday") List<Integer> weekday,
			@QueryParam("codMaterial") List<Integer> codMaterial, @QueryParam("recordMode") List<String> recordMode,
			@QueryParam("reservedTo") List<String> reservedTo, @QueryParam("codCustomer") List<Long> codCustomer,
			@QueryParam("number") List<Long> number, @QueryParam("iniDate") String iniDate,
			@QueryParam("finDate") String finDate) {

		Response response = new PlanningTimeModel().selectPlanningTimeResponse(codOwner, codStore, codEmployee,
				beginDate, beginTime, group, weekday, codMaterial, recordMode, reservedTo, codCustomer, number, iniDate,
				finDate);

		return response;
	}

	@GET
	@Path(GET_CART)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCart(@HeaderParam("codCustomer") Long codCustomer) {

		Response response = new PlanningTimeModel().getCartResponse(codCustomer);

		return response;
	}

	@GET
	@Path(GET_BOOKED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBooked(@HeaderParam("codCustomer") Long codCustomer) {

		Response response = new PlanningTimeModel().getBookedResponse(codCustomer);

		return response;
	}

	@POST
	@Path(PAY_CART)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response payCart(String incomingData, @HeaderParam("codCustomer") Long codCustomer,
			@HeaderParam("codPayment") String codPayment, @HeaderParam("phone") String phone) {

		return new PlanningTimeModel().payCart(incomingData, codCustomer, codPayment, phone);
	}

	@POST
	@Path(REFUND)
	@Produces(MediaType.APPLICATION_JSON)
	public Response refund(String incomingData, @HeaderParam("codCustomer") Long codCustomer) {

		return new PlanningTimeModel().refundResponse(incomingData, codCustomer);

	}

	@GET
	@Path(GET_FEE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTax(@QueryParam("codOwner") List<Long> codOwner,
			@QueryParam("codStore") List<Integer> codStore) {

		JsonObject jsonObject = new JsonObject();
		if ( codOwner != null && codOwner.size() != 0 && codStore != null && codStore.size() != 0) {
			jsonObject.addProperty("value", 2.00);
			jsonObject.addProperty("currency", "BRL");
		} else {
			SQLException exception = new SQLException("Please use the mandatory query parameters: codOwner and codStore", null, 88);
			jsonObject = getJsonObjectUpdate(exception);
		}
		
		return response(jsonObject);
	}

}
