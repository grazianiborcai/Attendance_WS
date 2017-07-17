package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.TypeTextDAO;
import br.com.mind5.helper.TypeText;

public class TypeTextModel extends JsonBuilder {

	public ArrayList<TypeText> selectTypeText(List<Integer> codType,
			List<String> language, List<String> name) throws SQLException {

		return new TypeTextDAO().selectTypeText(codType, language, name);
	}

	public JsonObject selectTypeTextJson(List<Integer> codType,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectTypeText(codType,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectTypeTextResponse(List<Integer> codType,
			List<String> language, List<String> name) {

		return response(selectTypeTextJson(codType, language, name));
	}

}
