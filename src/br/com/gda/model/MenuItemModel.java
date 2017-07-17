package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.MenuItemDAO;
import br.com.mind5.helper.MaterialDetail;
import br.com.mind5.helper.MenuItem;
import br.com.mind5.helper.RecordMode;

public class MenuItemModel extends JsonBuilder {

	private static final String MENU_ITEM = "MenuItem";

	public Response insertMenuItem(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<MenuItem> menuItemList = (ArrayList<MenuItem>) jsonToObjectList(incomingData, MenuItem.class);

		SQLException exception = new MenuItemDAO().insertMenuItem(menuItemList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = menuItemList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codMenu = menuItemList.stream().map(m -> m.getCodMenu()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject,
					selectMenuItemJson(codOwner, null, codMenu, null, recordMode, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateMenuItem(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<MenuItem> menuItemList = (ArrayList<MenuItem>) jsonToObjectList(incomingData, MenuItem.class);

		SQLException exception = new MenuItemDAO().updateMenuItem(menuItemList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = menuItemList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codMenu = menuItemList.stream().map(m -> m.getCodMenu()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject,
					selectMenuItemJson(codOwner, null, codMenu, null, recordMode, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response deleteMenuItem(List<Long> codOwner, List<Integer> codMenu, List<Integer> item,
			List<String> recordMode) {

		SQLException exception = new MenuItemDAO().deleteMenuItem(codOwner, codMenu, item, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<MenuItem> selectMenuItem(List<Long> codOwner, Integer codStore, List<Integer> codMenu,
			List<String> language, List<String> recordMode) throws SQLException {

		ArrayList<MenuItem> menuItemList = new MenuItemDAO().selectMenuItem(codOwner, codStore, codMenu, language,
				recordMode);

		ArrayList<MaterialDetail> detailMatList = new ArrayList<MaterialDetail>();

		List<Long> codOwnerList = new ArrayList<Long>();
		MenuItem menuItemBefore = null;
		for (MenuItem menuItem : menuItemList) {
			if (menuItem.getIdentifier().equals(RecordMode.F) && menuItem.getMaterial().getCodType() != null
					&& menuItem.getMaterial().getCodType().equals(RecordMode._1)) {
				if (menuItemBefore == null || menuItem.getCodOwner() != menuItemBefore.getCodOwner()) {

					codOwnerList.clear();
					codOwnerList.add(menuItem.getCodOwner());

					List<Integer> codMaterial = menuItemList.stream()
							.filter(m -> m.getCodOwner().equals(menuItem.getCodOwner()))
							.map(m -> m.getMaterial().getCodMaterial()).collect(Collectors.toList());

					detailMatList = new MaterialDetailModel().selectMaterialDetail(codOwnerList, codMaterial, null,
							recordMode, language, null);

					addDetailMat(detailMatList, menuItem);
				} else
					addDetailMat(detailMatList, menuItem);
				menuItemBefore = menuItem;
			}
		}

		return hierarchy(menuItemList, null);
	}

	private void addDetailMat(ArrayList<MaterialDetail> detailMatList, MenuItem menuItem) {
		menuItem.getMaterial().getDetailMat()
				.addAll(detailMatList.stream()
						.filter(m -> m.getCodOwner().equals(menuItem.getCodOwner())
								&& m.getCodMaterial().equals(menuItem.getMaterial().getCodMaterial()))
				.collect(Collectors.toList()));
	}

	private ArrayList<MenuItem> hierarchy(List<MenuItem> menuItemList, List<MenuItem> hierDone) {
		if (hierDone == null)
			hierDone = menuItemList.stream().filter(m -> m.getIdFather().equals(0)).collect(Collectors.toList());
		menuItemList.removeAll(hierDone);
		for (int i = 0; i < hierDone.size(); i++) {
			for (int z = 0; z < menuItemList.size(); z++) {
				if (hierDone.get(i).getCodOwner().equals(menuItemList.get(z).getCodOwner())
						&& hierDone.get(i).getCodMenu().equals(menuItemList.get(z).getCodMenu())
						&& hierDone.get(i).getItem().equals(menuItemList.get(z).getIdFather())) {
					// listDao.get(z).setFather(list.get(i));
					hierDone.get(i).getChildren().add(menuItemList.get(z));
				}
			}
			if (hierDone.get(i).getChildren().size() != 0) {
				hierarchy(menuItemList, hierDone.get(i).getChildren());
			}
		}
		return (ArrayList<MenuItem>) hierDone;
	}

	public JsonObject selectMenuItemJson(List<Long> codOwner, Integer codStore, List<Integer> codMenu,
			List<String> language, List<String> recordMode, boolean runMsg) {

		ArrayList<MenuItem> menuItemList = new ArrayList<MenuItem>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			menuItemList = selectMenuItem(codOwner, null, codMenu, language, recordMode);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(menuItemList, exception);

		if (runMsg)
			sendMessage(menuItemList, exception, codOwner, OWNER, MENU_ITEM);

		return jsonObject;
	}

	public Response selectMenuItemResponse(List<Long> codOwner, Integer codStore, List<Integer> codMenu,
			List<String> language, List<String> recordMode) {

		return response(selectMenuItemJson(codOwner, null, codMenu, language, recordMode, false));
	}

}
