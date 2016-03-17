package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.CategoryTextDAO;
import br.com.gda.helper.CategoryText;

public class CategoryTextModel extends JsonBuilder {

	public ArrayList<CategoryText> selectCategoryText(
			List<Integer> codCategory, List<String> language, List<String> name)
			throws SQLException {

		return new CategoryTextDAO().selectCategoryText(codCategory,
				language, name);
	}

	public JsonObject selectCategoryTextJson(List<Integer> codCategory,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectCategoryText(codCategory,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCategoryTextResponse(List<Integer> codCategory,
			List<String> language, List<String> name) {

		return response(selectCategoryTextJson(codCategory, language, name));
	}

}
