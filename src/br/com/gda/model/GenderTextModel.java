package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.GenderTextDAO;
import br.com.gda.helper.GenderText;

public class GenderTextModel extends JsonBuilder {

	public ArrayList<GenderText> selectGenderText(List<Integer> codGender,
			List<String> language, List<String> name) throws SQLException {

		return new GenderTextDAO().selectGenderText(codGender, language,
				name);
	}

	public JsonObject selectGenderTextJson(List<Integer> codGender,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectGenderText(codGender,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectGenderTextResponse(List<Integer> codGender,
			List<String> language, List<String> name) {

		return response(selectGenderTextJson(codGender, language, name));
	}

}
