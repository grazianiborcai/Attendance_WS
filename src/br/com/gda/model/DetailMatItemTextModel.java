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

import br.com.gda.dao.DetailMatItemTextDAO;
import br.com.mind5.helper.DetailMatItemText;

public class DetailMatItemTextModel extends JsonBuilder {

	public Response insertDetailMatItemText(String incomingData) {

		ArrayList<DetailMatItemText> detailMatItemTextList = jsonToDetailMatItemTextList(incomingData);

		SQLException exception = new DetailMatItemTextDAO()
				.insertDetailMatItemText(detailMatItemTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = detailMatItemTextList.stream()
				.map(d -> d.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codDetail = detailMatItemTextList.stream()
				.map(d -> d.getCodDetail()).distinct()
				.collect(Collectors.toList());

		List<Integer> codItem = detailMatItemTextList.stream()
				.map(d -> d.getCodItem()).distinct()
				.collect(Collectors.toList());

		jsonObject = mergeJsonObject(
				jsonObject,
				selectDetailMatItemTextJson(codOwner, codDetail, codItem, null,
						null, null, null));

		return response(jsonObject);
	}

	public Response updateDetailMatItemText(String incomingData) {

		ArrayList<DetailMatItemText> detailMatItemTextList = jsonToDetailMatItemTextList(incomingData);

		SQLException exception = new DetailMatItemTextDAO()
				.updateDetailMatItemText(detailMatItemTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = detailMatItemTextList.stream()
				.map(d -> d.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codDetail = detailMatItemTextList.stream()
				.map(d -> d.getCodDetail()).distinct()
				.collect(Collectors.toList());

		List<Integer> codItem = detailMatItemTextList.stream()
				.map(d -> d.getCodItem()).distinct()
				.collect(Collectors.toList());

		jsonObject = mergeJsonObject(
				jsonObject,
				selectDetailMatItemTextJson(codOwner, codDetail, codItem, null,
						null, null, null));

		return response(jsonObject);
	}

	public Response deleteDetailMatItemText(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		SQLException exception = new DetailMatItemTextDAO()
				.deleteDetailMatItemText(codOwner, codDetail, codItem,
						language, name, description, textLong);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<DetailMatItemText> selectDetailMatItemText(
			List<Long> codOwner, List<Integer> codDetail,
			List<Integer> codItem, List<String> language, List<String> name,
			List<String> description, List<String> textLong)
			throws SQLException {

		return new DetailMatItemTextDAO().selectDetailMatItemText(
				codOwner, codDetail, codItem, language, name, description,
				textLong);

	}

	public JsonObject selectDetailMatItemTextJson(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectDetailMatItemText(
					codOwner, codDetail, codItem, language, name, description,
					textLong));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectDetailMatItemTextResponse(List<Long> codOwner,
			List<Integer> codDetail, List<Integer> codItem,
			List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		return response(selectDetailMatItemTextJson(codOwner, codDetail,
				codItem, language, name, description, textLong));
	}

	private ArrayList<DetailMatItemText> jsonToDetailMatItemTextList(
			String incomingData) {

		ArrayList<DetailMatItemText> detailMatItemTextList = new ArrayList<DetailMatItemText>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				detailMatItemTextList.add(gson.fromJson(array.get(i),
						DetailMatItemText.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			detailMatItemTextList.add(gson.fromJson(object,
					DetailMatItemText.class));
		}

		return detailMatItemTextList;
	}

}
