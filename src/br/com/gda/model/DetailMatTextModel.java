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
import com.google.gson.JsonParser;

import br.com.gda.dao.DetailMatTextDAO;
import br.com.mind5.helper.DetailMatText;

public class DetailMatTextModel extends JsonBuilder {

	public Response insertDetailMatText(String incomingData) {

		ArrayList<DetailMatText> detailMatTextList = jsonToDetailMatTextList(incomingData);

		SQLException exception = new DetailMatTextDAO()
				.insertDetailMatText(detailMatTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = detailMatTextList.stream()
				.map(d -> d.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codDetail = detailMatTextList.stream()
				.map(d -> d.getCodDetail()).distinct()
				.collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject,
				selectDetailMatTextJson(codOwner, codDetail, null, null));

		return response(jsonObject);
	}

	public Response updateDetailMatText(String incomingData) {

		ArrayList<DetailMatText> detailMatTextList = jsonToDetailMatTextList(incomingData);

		SQLException exception = new DetailMatTextDAO()
				.updateDetailMatText(detailMatTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = detailMatTextList.stream()
				.map(d -> d.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codDetail = detailMatTextList.stream()
				.map(d -> d.getCodDetail()).distinct()
				.collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject,
				selectDetailMatTextJson(codOwner, codDetail, null, null));

		return response(jsonObject);
	}

	public Response deleteDetailMatText(List<Long> codOwner,
			List<Integer> codDetail, List<String> language, List<String> name) {

		SQLException exception = new DetailMatTextDAO()
				.deleteDetailMatText(codOwner, codDetail, language, name);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<DetailMatText> selectDetailMatText(List<Long> codOwner,
			List<Integer> codDetail, List<String> language, List<String> name)
			throws SQLException {

		return new DetailMatTextDAO().selectDetailMatText(codOwner,
				codDetail, language, name);

	}

	public JsonObject selectDetailMatTextJson(List<Long> codOwner,
			List<Integer> codDetail, List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectDetailMatText(codOwner,
					codDetail, language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectDetailMatTextResponse(List<Long> codOwner,
			List<Integer> codDetail, List<String> language, List<String> name) {

		return response(selectDetailMatTextJson(codOwner, codDetail, language,
				name));
	}

	private ArrayList<DetailMatText> jsonToDetailMatTextList(String incomingData) {

		ArrayList<DetailMatText> detailMatTextList = new ArrayList<DetailMatText>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				detailMatTextList.add(gson.fromJson(array.get(i),
						DetailMatText.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			detailMatTextList.add(gson.fromJson(object, DetailMatText.class));
		}

		return detailMatTextList;
	}

}
