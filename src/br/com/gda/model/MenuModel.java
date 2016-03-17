package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.MenuDAO;
import br.com.gda.helper.Menu;
import br.com.gda.helper.MenuItem;
import br.com.gda.helper.RecordMode;

public class MenuModel extends JsonBuilder {

	private ArrayList<Menu> menuList = new ArrayList<Menu>();

	private static final String MENU = "menu";

	public Response insertMenu(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<Menu> menuList = (ArrayList<Menu>) jsonToObjectList(incomingData, Menu.class);

		SQLException exception = new MenuDAO().insertMenu(menuList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = menuList.stream().map(m -> m.getCodOwner()).distinct().collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMenuJson(codOwner, null, recordMode, null, null, true));

		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}

		return response(jsonObject);
	}

	public Response updateMenu(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<Menu> menuList = (ArrayList<Menu>) jsonToObjectList(incomingData, Menu.class);

		SQLException exception = new MenuDAO().updateMenu(menuList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = menuList.stream().map(m -> m.getCodOwner()).distinct().collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMenuJson(codOwner, null, recordMode, null, null, true));

		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}

		return response(jsonObject);
	}

	public Response deleteMenu(List<Long> codOwner, List<Integer> codMenu, List<String> recordMode) {

		SQLException exception = new MenuDAO().deleteMenu(codOwner, codMenu, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<Menu> selectMenu(List<Long> codOwner, List<Integer> codMenu, List<String> recordMode,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<Menu> menuList = new MenuDAO().selectMenu(codOwner, codMenu, recordMode, language, name);

		ArrayList<MenuItem> menuItemList = new ArrayList<MenuItem>();

		List<Long> codOwnerList = new ArrayList<Long>();
		Menu menuBefore = null;
		for (Menu menu : menuList) {
			if (menuBefore == null || menu.getCodOwner() != menuBefore.getCodOwner()) {

				codOwnerList.clear();
				codOwnerList.add(menu.getCodOwner());

				codMenu = menuList.stream().filter(m -> m.getCodOwner().equals(menu.getCodOwner()))
						.map(m -> m.getCodMenu()).collect(Collectors.toList());

				menuItemList = new MenuItemModel().selectMenuItem(codOwnerList, null, codMenu, language, recordMode);

				addMenuItem(menuItemList, menu);
			} else {
				addMenuItem(menuItemList, menu);
			}
			menuBefore = menu;
		}

		return this.setMenuList(menuList);
	}

	private void addMenuItem(ArrayList<MenuItem> menuItemList, Menu menu) {
		menu.getMenuItem()
				.addAll(menuItemList.stream().filter(
						m -> m.getCodOwner().equals(menu.getCodOwner()) && m.getCodMenu().equals(menu.getCodMenu()))
				.collect(Collectors.toList()));
	}

	public JsonObject selectMenuJson(List<Long> codOwner, List<Integer> codMenu, List<String> recordMode,
			List<String> language, List<String> name, boolean runMsg) {

		ArrayList<Menu> menuList = new ArrayList<Menu>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			menuList = selectMenu(codOwner, codMenu, recordMode, language, name);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(menuList, exception);

		if (runMsg)
			sendMessage(menuList, exception, codOwner, OWNER, MENU);

		return jsonObject;
	}

	public Response selectMenuResponse(List<Long> codOwner, List<Integer> codMenu, List<String> recordMode,
			List<String> language, List<String> name) {

		return response(selectMenuJson(codOwner, codMenu, recordMode, language, name, false));
	}

	public ArrayList<Menu> getMenuList() {
		return menuList;
	}

	public ArrayList<Menu> setMenuList(ArrayList<Menu> menuList) {
		this.menuList = menuList;
		return menuList;
	}

}
