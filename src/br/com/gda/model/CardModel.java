package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.CardDAO;
import br.com.gda.dao.CategoryDAO;
import br.com.gda.dao.OwnerDAO;
import br.com.mind5.helper.Category;
import br.com.mind5.helper.CreditCardAG;
import br.com.mind5.helper.Owner;

public class CardModel extends JsonBuilder {

	public Response insertCreditCard(String incomingData, Long codCustomer) {

		ArrayList<CreditCardAG> creditCardAGList = (ArrayList<CreditCardAG>) jsonToObjectList(incomingData,
				CreditCardAG.class);

		if (!creditCardAGList.get(0).isComplete()) {

			SQLException exception = new SQLException("Message erro", null, 30);

			JsonObject jsonObject = getJsonObjectUpdate(exception);

			jsonObject.add(RESULTS, new JsonArray());

			return response(jsonObject);
		}

		SQLException exception = new CardDAO().insertCreditCard(creditCardAGList, codCustomer);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		// if (exception.getErrorCode() == 200) {
		// SQLException selectException = new
		// SQLException(RETURNED_SUCCESSFULLY, null, 200);
		// jsonObject = mergeJsonObject(jsonObject,
		// getJsonObjectSelect(creditCardAGList, selectException));
		// } else {
		// JsonElement jsonElement = new JsonArray();
		// SQLException ex = new SQLException();
		// jsonObject = mergeJsonObject(jsonObject,
		// getJsonObjectSelect(jsonElement, ex));
		// }

		return response(jsonObject);
	}

	public Response deleteCreditCard(Long codCustomer, String last4) {

		SQLException exception = new CardDAO().deleteCreditCard(codCustomer, last4);

		JsonObject jsonObject = getJsonObjectUpdate(exception);
		
		return response(jsonObject);
	}

	public ArrayList<CreditCardAG> selectCreditCard(Long codCustomer) throws SQLException {

		return new CardDAO().selectCreditCard(codCustomer);
	}

	public JsonObject selectCreditCardJson(Long codCustomer) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectCreditCard(codCustomer));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCreditCardResponse(Long codCustomer) {

		return response(selectCreditCardJson(codCustomer));
	}

}
