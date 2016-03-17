package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.StateTextDAO;
import br.com.gda.helper.StateText;

public class StateTextModel extends JsonBuilder {

	public ArrayList<StateText> selectStateText(List<String> country,
			List<String> state, List<String> language, List<String> name)
			throws SQLException {

		return new StateTextDAO().selectStateText(country, state,
				language, name);
	}

	public JsonObject selectStateTextJson(List<String> country,
			List<String> state, List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectStateText(country, state,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectStateTextResponse(List<String> country,
			List<String> state, List<String> language, List<String> name) {

		return response(selectStateTextJson(country, state, language, name));
	}

}
