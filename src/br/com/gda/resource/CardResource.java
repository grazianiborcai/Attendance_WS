package br.com.gda.resource;

import java.sql.SQLException;
import java.util.ArrayList;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.model.CardModel;
import br.com.gda.model.CustomerModel;
import br.com.gda.model.JsonBuilder;
import br.com.gda.model.PlanningTimeModel;
import br.com.mind5.helper.CreditCardAG;
import br.com.mind5.helper.HolderAG;
import br.com.mind5.helper.PayCart;
import br.com.mind5.helper.TaxDocumentAG;

@Path("/Card")
public class CardResource extends JsonBuilder {

	private static final String INSERT_CARD = "/insertCard";
	private static final String GET_CARDS = "/getCards";
	private static final String DELETE_CARD = "/deleteCard/{last4}";

	@POST
	@Path(INSERT_CARD)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertCard(String incomingData, @HeaderParam("codCustomer") Long codCustomer) {

			return new CardModel().insertCreditCard(incomingData, codCustomer);
	}

	@GET
	@Path(GET_CARDS)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCards(@HeaderParam("codCustomer") Long codCustomer) {

		return new CardModel().selectCreditCardResponse(codCustomer);
	}

	@POST
	@Path(DELETE_CARD)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCard(@HeaderParam("codCustomer") Long codCustomer, @PathParam("last4") String last4) {

		return new CardModel().deleteCreditCard(codCustomer, last4);
	}

}
