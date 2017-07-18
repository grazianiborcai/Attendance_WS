package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.GenderDAO;
import br.com.mind5.helper.Gender;

public class GenderModel extends JsonBuilder {

	public ArrayList<Gender> selectGender() throws SQLException {

		return new GenderDAO().selectGenderText();
	}

	public JsonObject selectGenderJson() {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectGender());

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectGenderResponse() {

		return response(selectGenderJson());
	}

}
