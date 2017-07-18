package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.GradeDAO;
import br.com.mind5.helper.Grade;

public class GradeModel extends JsonBuilder {

	public ArrayList<Grade> selectGrade() throws SQLException {

		return new GradeDAO().selectGradeText();
	}

	public JsonObject selectGradeJson() {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectGrade());

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectGradeResponse() {

		return response(selectGradeJson());
	}

}
