package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.WeekdayDAO;
import br.com.gda.helper.Weekday;

public class WeekdayModel extends JsonBuilder {

	public ArrayList<Weekday> selectWeekday(List<Integer> weekday,
			List<String> language, List<String> name) throws SQLException {

		return new WeekdayDAO().selectWeekday(weekday, language, name);
	}

	public JsonObject selectWeekdayJson(List<Integer> weekday,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectWeekday(weekday,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectWeekdayResponse(List<Integer> weekday,
			List<String> language, List<String> name) {

		return response(selectWeekdayJson(weekday, language, name));
	}

}
