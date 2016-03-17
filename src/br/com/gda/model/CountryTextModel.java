package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.CountryTextDAO;
import br.com.gda.helper.CountryText;

public class CountryTextModel extends JsonBuilder {

	public ArrayList<CountryText> selectCountryText(List<String> country,
			List<String> language, List<String> name) throws SQLException {

		return new CountryTextDAO().selectCountryText(country, language,
				name);
	}

	public JsonObject selectCountryTextJson(List<String> country,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectCountryText(country,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCountryTextResponse(List<String> country,
			List<String> language, List<String> name) {

		return response(selectCountryTextJson(country, language, name));
	}

}
