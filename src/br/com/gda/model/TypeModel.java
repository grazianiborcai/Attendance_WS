package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.TypeDAO;
import br.com.gda.helper.Type;

public class TypeModel extends JsonBuilder {

	public ArrayList<Type> selectType(List<Integer> codType,
			List<String> language, List<String> name) throws SQLException {

		return new TypeDAO().selectType(codType, language, name);
	}

	public JsonObject selectTypeJson(List<Integer> codType,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectType(codType, language,
					name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectTypeResponse(List<Integer> codType,
			List<String> language, List<String> name) {

		return response(selectTypeJson(codType, language, name));
	}

}
