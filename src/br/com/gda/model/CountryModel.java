package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.CountryDAO;
import br.com.mind5.helper.Country;

public class CountryModel extends JsonBuilder {

	public ArrayList<Country> selectCountry() throws SQLException {

		return new CountryDAO().selectCountryText();
	}

	public JsonObject selectCountryJson() {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectCountry());

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCountryResponse() {

		return response(selectCountryJson());
	}

}
