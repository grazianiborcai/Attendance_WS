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

import br.com.gda.dao.MenuTextDAO;
import br.com.mind5.helper.MenuText;

public class MenuTextModel extends JsonBuilder {

	public Response insertMenuText(String incomingData) {

		ArrayList<MenuText> menuTextList = jsonToMenuTextList(incomingData);

		SQLException exception = new MenuTextDAO()
				.insertMenuText(menuTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = menuTextList.stream().map(m -> m.getCodOwner())
				.distinct().collect(Collectors.toList());

		List<Integer> codMenu = menuTextList.stream().map(m -> m.getCodMenu())
				.distinct().collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject,
				selectMenuTextJson(codOwner, codMenu, null, null));

		return response(jsonObject);
	}

	public Response updateMenuText(String incomingData) {

		ArrayList<MenuText> menuTextList = jsonToMenuTextList(incomingData);

		SQLException exception = new MenuTextDAO()
				.updateMenuText(menuTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = menuTextList.stream().map(m -> m.getCodOwner())
				.distinct().collect(Collectors.toList());

		List<Integer> codMenu = menuTextList.stream().map(m -> m.getCodMenu())
				.distinct().collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject,
				selectMenuTextJson(codOwner, codMenu, null, null));

		return response(jsonObject);
	}

	public Response deleteMenuText(List<Long> codOwner, List<Integer> codMenu,
			List<String> language, List<String> name) {

		SQLException exception = new MenuTextDAO().deleteMenuText(
				codOwner, codMenu, language, name);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<MenuText> selectMenuText(List<Long> codOwner,
			List<Integer> codMenu, List<String> language, List<String> name)
			throws SQLException {

		return new MenuTextDAO().selectMenuText(codOwner, codMenu,
				language, name);

	}

	public JsonObject selectMenuTextJson(List<Long> codOwner,
			List<Integer> codMenu, List<String> language, List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectMenuText(codOwner,
					codMenu, language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectMenuTextResponse(List<Long> codOwner,
			List<Integer> codMenu, List<String> language, List<String> name) {

		return response(selectMenuTextJson(codOwner, codMenu, language, name));
	}

	private ArrayList<MenuText> jsonToMenuTextList(String incomingData) {

		ArrayList<MenuText> menuTextList = new ArrayList<MenuText>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				menuTextList.add(gson.fromJson(array.get(i), MenuText.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			menuTextList.add(gson.fromJson(object, MenuText.class));
		}

		return menuTextList;
	}

}
