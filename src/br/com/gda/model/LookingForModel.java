package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.LookingForDAO;
import br.com.mind5.helper.LookingFor;

public class LookingForModel extends JsonBuilder {

	public ArrayList<LookingFor> selectLookingFor() throws SQLException {

		return new LookingForDAO().selectLookingForText();
	}

	public JsonObject selectLookingForJson() {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectLookingFor());

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectLookingForResponse() {

		return response(selectLookingForJson());
	}

}
