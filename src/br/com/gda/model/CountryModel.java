package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.CountryDAO;
import br.com.gda.helper.Country;
import br.com.gda.helper.State;

public class CountryModel extends JsonBuilder {

	public ArrayList<Country> selectCountry(List<String> country,
			List<String> language, List<String> name) throws SQLException {

		final ArrayList<Country> countryList = new CountryDAO()
				.selectCountry(country, language, name);

		country = countryList.stream().map(c -> c.getCountry())
				.collect(Collectors.toList());

		if (country != null && !country.isEmpty()) {

			final ArrayList<State> stateList = new StateModel()
					.selectState(country, null, language, null);

			countryList.stream().forEach(
					c -> c.getState().addAll(
							stateList
									.stream()
									.filter(s -> c.getCountry().equals(
											s.getCountry()))
									.collect(Collectors.toList())));
		}

		return countryList;
	}

	public JsonObject selectCountryJson(List<String> country,
			List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectCountry(country,
					language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectCountryResponse(List<String> country,
			List<String> language, List<String> name) {

		return response(selectCountryJson(country, language, name));
	}

}
