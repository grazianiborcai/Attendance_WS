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

import br.com.gda.dao.MenuItemTextDAO;
import br.com.gda.helper.MenuItemText;

public class MenuItemTextModel extends JsonBuilder {

	public Response insertMenuItemText(String incomingData) {

		ArrayList<MenuItemText> menuItemTextList = jsonToMenuItemTextList(incomingData);

		SQLException exception = new MenuItemTextDAO()
				.insertMenuItemText(menuItemTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = menuItemTextList.stream()
				.map(m -> m.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codMenu = menuItemTextList.stream()
				.map(m -> m.getCodMenu()).distinct()
				.collect(Collectors.toList());

		List<Integer> item = menuItemTextList.stream().map(m -> m.getItem())
				.distinct().collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject,
				selectMenuItemTextJson(codOwner, codMenu, item, null, null));

		return response(jsonObject);
	}

	public Response updateMenuItemText(String incomingData) {

		ArrayList<MenuItemText> menuItemTextList = jsonToMenuItemTextList(incomingData);

		SQLException exception = new MenuItemTextDAO()
				.updateMenuItemText(menuItemTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = menuItemTextList.stream()
				.map(m -> m.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codMenu = menuItemTextList.stream()
				.map(m -> m.getCodMenu()).distinct()
				.collect(Collectors.toList());

		List<Integer> item = menuItemTextList.stream().map(m -> m.getItem())
				.distinct().collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject,
				selectMenuItemTextJson(codOwner, codMenu, item, null, null));

		return response(jsonObject);
	}

	public Response deleteMenuItemText(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> language,
			List<String> name) {

		SQLException exception = new MenuItemTextDAO()
				.deleteMenuItemText(codOwner, codMenu, item, language, name);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<MenuItemText> selectMenuItemText(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> language,
			List<String> name) throws SQLException {

		return new MenuItemTextDAO().selectMenuItemText(codOwner,
				codMenu, item, language, name);

	}

	public JsonObject selectMenuItemTextJson(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> language,
			List<String> name) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null,
				200);

		try {

			jsonElement = new Gson().toJsonTree(selectMenuItemText(codOwner,
					codMenu, item, language, name));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectMenuItemTextResponse(List<Long> codOwner,
			List<Integer> codMenu, List<Integer> item, List<String> language,
			List<String> name) {

		return response(selectMenuItemTextJson(codOwner, codMenu, item,
				language, name));
	}

	private ArrayList<MenuItemText> jsonToMenuItemTextList(String incomingData) {

		ArrayList<MenuItemText> menuItemTextList = new ArrayList<MenuItemText>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				menuItemTextList.add(gson.fromJson(array.get(i),
						MenuItemText.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			menuItemTextList.add(gson.fromJson(object, MenuItemText.class));
		}

		return menuItemTextList;
	}

}
