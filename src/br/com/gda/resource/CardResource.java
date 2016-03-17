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

import br.com.gda.helper.CreditCardAG;
import br.com.gda.helper.HolderAG;
import br.com.gda.helper.PayCart;
import br.com.gda.helper.TaxDocumentAG;
import br.com.gda.model.JsonBuilder;
import br.com.gda.model.PlanningTimeModel;

@Path("/Card")
public class CardResource extends JsonBuilder {

	private static final String INSERT_CARD = "/insertCard";
	private static final String GET_CARDS = "/getCards";
	private static final String DELETE_CARD = "/deleteCard/{codCard}";

	@POST
	@Path(INSERT_CARD)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertCard(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<CreditCardAG> creditCard = (ArrayList<CreditCardAG>) jsonToObjectList(incomingData,
				CreditCardAG.class);

		if (creditCard.get(0).getHash() == null) {

			SQLException exception = new SQLException("Message erro", null, 30);

			JsonObject jsonObject = getJsonObjectUpdate(exception);

			jsonObject.add(RESULTS, new JsonArray());

			return response(jsonObject);
		} else {
			SQLException exception = new SQLException("Cartão inserido", null, 200);

			JsonObject jsonObject = getJsonObjectUpdate(exception);

			creditCard.get(0).setCodCard((long) 1);

			jsonObject.add(RESULTS, new Gson().toJsonTree(creditCard));

			return response(jsonObject);
		}
	}

	@GET
	@Path(GET_CARDS)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCards(@HeaderParam("codCustomer") Long codCustomer) {

		CreditCardAG creditCard = new CreditCardAG();
		HolderAG holder = new HolderAG();
		TaxDocumentAG taxDocument = new TaxDocumentAG();

		taxDocument.setType("CPF");
		taxDocument.setNumber("33333333333");

		holder.setFullname("Jose Portador da Silva");
		holder.setBirthdate("1988-12-30");
		holder.setTaxDocument(taxDocument);

		creditCard.setCodCard((long) 1);
		creditCard.setHash(
				"FSFKSdEtUSJBdf5rANxQ2cqlWI74rRb7PX8PzwdZqWDN6zTSxDQIay9ThokIKa8zbXaDwTHXD8Wp9YEhDpomZBvyli9C6UHSfrJKAIP/RLzn7C8IrPuY3FTWq0eFj1nDSgrIyDgW5mQjFdpzyUXHNAuyQReKMu3nOO6U+uDBWk8EesQYDVQoA4TOFPWyKHXPQHr/0J//DipiUh6gj0i+x7NsHC9z41U5M6Q+srsmCldOQIVndD1xsHKNDx4gYYRRrMmkbRbY4GFQ2GQCFhreElpwcTXAuLVshjzqyRUzNCwT5sXKMQa6Fub2TzEENhmMWjBqN2BojaFt8f4tOgpx8w==");
		creditCard.setLast4("0876");
		creditCard.setBrand("VISA");
		creditCard.setHolder(holder);

		ArrayList<CreditCardAG> creditCardList = new ArrayList<CreditCardAG>();

		creditCardList.add(creditCard);

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		jsonElement = new Gson().toJsonTree(creditCardList);

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return response(jsonObject);
	}

	@GET
	@Path(DELETE_CARD)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCard(@HeaderParam("codCustomer") Long codCustomer, @PathParam("codCard") Long codCard) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException("Cartão eliminado com sucesso", null, 200);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

}
