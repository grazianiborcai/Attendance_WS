package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.PositionDAO;
import br.com.mind5.helper.Position;

public class PositionModel extends JsonBuilder {

	public ArrayList<Position> selectPosition(List<Integer> codPosition,
			List<String> language, List<String> name) throws SQLException {

		return new PositionDAO().selectPosition(codPosition, language,
				name);
	}

	public JsonObject selectPositionJson(List<Integer> codPosition,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectPosition(codPosition,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectPositionResponse(List<Integer> codPosition,
			List<String> language, List<String> name) {

		return response(selectPositionJson(codPosition, language, name));
	}

}
