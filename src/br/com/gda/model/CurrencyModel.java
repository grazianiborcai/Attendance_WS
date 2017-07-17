package br.com.gda.model;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.CurrencyDAO;
import br.com.mind5.helper.CurrencyBase;

public class CurrencyModel extends JsonBuilder {

	public CurrencyBase selectCurrency(String baseCode, String language) throws SQLException {

		return new CurrencyDAO().selectCurrency(baseCode, language);
	}

	public JsonObject selectCurrencyJson(String baseCode, String language) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectCurrency(baseCode, language));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCurrencyResponse(String baseCode, String language) {

		return response(selectCurrencyJson(baseCode, language));
	}

}
