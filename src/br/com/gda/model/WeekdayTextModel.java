package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.WeekdayTextDAO;
import br.com.gda.helper.WeekdayText;

public class WeekdayTextModel extends JsonBuilder {

	public ArrayList<WeekdayText> selectWeekdayText(List<Integer> weekday,
			List<String> language, List<String> name) throws SQLException {

		return new WeekdayTextDAO().selectWeekdayText(weekday, language,
				name);
	}

	public JsonObject selectWeekdayTextJson(List<Integer> weekday,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectWeekdayText(weekday,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectWeekdayTextResponse(List<Integer> weekday,
			List<String> language, List<String> name) {

		return response(selectWeekdayTextJson(weekday, language, name));
	}

}
