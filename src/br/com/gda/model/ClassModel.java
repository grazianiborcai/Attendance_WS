package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.ClassDAO;
import br.com.mind5.helper.Class;

public class ClassModel extends JsonBuilder {

	public ArrayList<Class> selectClass(Long peopleID) throws SQLException {

		return new ClassDAO().selectClassText(peopleID);
	}

	public JsonObject selectClassJson(Long peopleID) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectClass(peopleID));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectClassResponse(Long peopleID) {

		return response(selectClassJson(peopleID));
	}

}
