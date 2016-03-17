package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.LanguageDAO;
import br.com.gda.helper.Language;

public class LanguageModel extends JsonBuilder {

	public ArrayList<Language> selectLanguage(List<String> language,
			List<String> name) throws SQLException {

		return new LanguageDAO().selectLanguage(language, name);

	}

	public JsonObject selectLanguageJson(List<String> language,
			List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectLanguage(language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectLanguageResponse(List<String> language,
			List<String> name) {

		return response(selectLanguageJson(language, name));
	}

}
