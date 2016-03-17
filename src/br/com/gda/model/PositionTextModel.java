package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.PositionTextDAO;
import br.com.gda.helper.PositionText;

public class PositionTextModel extends JsonBuilder {

	public ArrayList<PositionText> selectPositionText(
			List<Integer> codPosition, List<String> language, List<String> name)
			throws SQLException {

		return new PositionTextDAO().selectPositionText(codPosition,
				language, name);
	}

	public JsonObject selectPositionTextJson(List<Integer> codPosition,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectPositionText(codPosition,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectPositionTextResponse(List<Integer> codPosition,
			List<String> language, List<String> name) {

		return response(selectPositionTextJson(codPosition, language, name));
	}

}
