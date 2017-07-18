package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.HowDiscoveredDAO;
import br.com.mind5.helper.HowDiscovered;

public class HowDiscoveredModel extends JsonBuilder {

	public ArrayList<HowDiscovered> selectHowDiscovered() throws SQLException {

		return new HowDiscoveredDAO().selectHowDiscoveredText();
	}

	public JsonObject selectHowDiscoveredJson() {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectHowDiscovered());

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectHowDiscoveredResponse() {

		return response(selectHowDiscoveredJson());
	}

}
