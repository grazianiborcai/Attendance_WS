package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.gda.dao.OwnerDAO;
import br.com.mind5.helper.DetailMat;
import br.com.mind5.helper.Employee;
import br.com.mind5.helper.Material;
import br.com.mind5.helper.Menu;
import br.com.mind5.helper.Owner;
import br.com.mind5.helper.RecordMode;
import br.com.mind5.helper.Store;

public class OwnerModel extends JsonBuilder {

	public Response insertOwner(String incomingData) {
		
		ArrayList<Owner> ownerList = jsonToOwnerList(incomingData);

		SQLException exception = new OwnerDAO().insertOwner(ownerList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);
		
		if (exception.getErrorCode() == 200) {
			SQLException selectException = new SQLException(RETURNED_SUCCESSFULLY, null, 200);
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(ownerList, selectException));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}

		return response(jsonObject);
	}

	public Response updateOwner(String incomingData) {
		
		ArrayList<Owner> ownerList = jsonToOwnerList(incomingData);

		SQLException exception = new OwnerDAO().updateOwner(ownerList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);
		
		if (exception.getErrorCode() == 200) {
			SQLException selectException = new SQLException(RETURNED_SUCCESSFULLY, null, 200);
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(ownerList, selectException));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}

		return response(jsonObject);
	}

	public Response deleteOwner(List<Long> codOwner, List<String> password, List<String> name, List<String> cpf,
			List<String> phone, List<String> email, List<String> emailAux, List<Byte> codGender, List<String> bornDate,
			List<Integer> postalcode, List<String> address1, List<String> address2, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> recordMode) {

		SQLException exception = new OwnerDAO().deleteOwner(codOwner, password, name, cpf, phone, email, emailAux,
				codGender, bornDate, postalcode, address1, address2, city, country, state, codCurr, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<Owner> selectOwner(String email, String cpf, String password, 
			List<String> language, Boolean withDetailMat, Boolean withMaterial, Boolean withMenu, Boolean withStore,
			Boolean withEmployee, Boolean withStoreMenu, Boolean withStoreMaterial, Boolean withStoreEmployee,
			Boolean withStoreTables, Boolean withStoreBill, String zoneId) throws SQLException {

		List<String> recordMode = new ArrayList<String>();
		recordMode.add(RecordMode.RECORD_OK);
		
		final ArrayList<Owner> ownerList = new OwnerDAO().selectOwner(email, cpf, password, recordMode);

		if (withDetailMat || withMaterial || withMenu || withStore || withEmployee) {

			List<Long> codOwnerList = ownerList.stream().map(s -> s.getCodOwner()).distinct()
					.collect(Collectors.toList());

			DetailMatModel detailMatModel = new DetailMatModel();
			MaterialModel materialModel = new MaterialModel();
			MenuModel menuModel = new MenuModel();
			StoreModel storeModel = new StoreModel();
			EmployeeModel employeeModel = new EmployeeModel();

			if (codOwnerList != null && !codOwnerList.isEmpty()) {

				Thread tDetailMat = null;
				Thread tMaterial = null;
				Thread tMenu = null;
				Thread tStore = null;
				Thread tEmployee = null;

				if (withDetailMat) {

					tDetailMat = new Thread(new Runnable() {
						public void run() {
							try {
								detailMatModel.selectDetailMat(codOwnerList, null, recordMode, language, null);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					tDetailMat.start();
				}

				if (withMaterial) {

					tMaterial = new Thread(new Runnable() {
						public void run() {
							try {
								materialModel.selectMaterial(codOwnerList, null, null, null, null, null, recordMode,
										language, null, null, null);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					tMaterial.start();
				}

				if (withMenu) {

					tMenu = new Thread(new Runnable() {
						public void run() {
							try {
								menuModel.selectMenu(codOwnerList, null, recordMode, language, null);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					tMenu.start();

				}

				if (withStore) {

					tStore = new Thread(new Runnable() {
						public void run() {
							try {
								storeModel.selectStore(codOwnerList, null, null, null, null, null, null, null, null,
										null, null, null, null, null, null, recordMode, language,
										withStoreMaterial, withStoreEmployee, zoneId);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					tStore.start();
				}

				if (withEmployee) {

					tEmployee = new Thread(new Runnable() {
						public void run() {
							try {
								employeeModel.selectEmployee(codOwnerList, null, null, null, null, null, null, null,
										null, null, null, null, null, null, null, null, recordMode);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					tEmployee.start();
				}

				try {
					if (tDetailMat != null)
						tDetailMat.join();
					if (tMaterial != null)
						tMaterial.join();
					if (tMenu != null)
						tMenu.join();
					if (tStore != null)
						tStore.join();
					if (tEmployee != null)
						tEmployee.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				final ArrayList<DetailMat> ownerDetailMatList = detailMatModel.getDetailMatList();
				final ArrayList<Material> ownerMaterialList = materialModel.getMaterialList();
				final ArrayList<Menu> ownerMenuList = menuModel.getMenuList();
				final ArrayList<Store> ownerStoreList = storeModel.getStoreList();
				final ArrayList<Employee> ownerEmployeeList = employeeModel.getEmployeeList();

				ownerList.stream().forEach(o -> {
					o.getDetailMat().addAll(ownerDetailMatList.stream()
							.filter(m -> o.getCodOwner().equals(m.getCodOwner())).collect(Collectors.toList()));
					o.getMaterial().addAll(ownerMaterialList.stream()
							.filter(m -> o.getCodOwner().equals(m.getCodOwner())).collect(Collectors.toList()));
					o.getMenu().addAll(ownerMenuList.stream().filter(m -> o.getCodOwner().equals(m.getCodOwner()))
							.collect(Collectors.toList()));
					o.getStore().addAll(ownerStoreList.stream().filter(t -> o.getCodOwner().equals(t.getCodOwner()))
							.collect(Collectors.toList()));
					o.getEmployee().addAll(ownerEmployeeList.stream()
							.filter(e -> o.getCodOwner().equals(e.getCodOwner())).collect(Collectors.toList()));
				});
			}
		}
		
		if (ownerList == null || ownerList.size() == 0)
			throw new WebApplicationException(Status.UNAUTHORIZED);

		return ownerList;

	}

	public JsonObject selectOwnerJson(String email, String cpf, String password, List<String> language, Boolean withDetailMat, Boolean withMaterial, Boolean withMenu, Boolean withStore,
			Boolean withEmployee, Boolean withStoreMenu, Boolean withStoreMaterial, Boolean withStoreEmployee,
			Boolean withStoreTables, Boolean withStoreBill, String zoneId) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectOwner(email, cpf, password, language, withDetailMat, withMaterial, withMenu, withStore, withEmployee, withStoreMenu,
					withStoreMaterial, withStoreEmployee, withStoreTables, withStoreBill, zoneId));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectOwnerResponse(String email, String cpf, String password, List<String> language, Boolean withDetailMat, Boolean withMaterial, Boolean withMenu, Boolean withStore,
			Boolean withEmployee, Boolean withStoreMenu, Boolean withStoreMaterial, Boolean withStoreEmployee,
			Boolean withStoreTables, Boolean withStoreBill, String zoneId) {

		return response(selectOwnerJson(email, cpf, password, language, withDetailMat,
				withMaterial, withMenu, withStore, withEmployee, withStoreMenu, withStoreMaterial, withStoreEmployee,
				withStoreTables, withStoreBill, zoneId));
	}

	private ArrayList<Owner> jsonToOwnerList(String incomingData) {

		ArrayList<Owner> ownerList = new ArrayList<Owner>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				ownerList.add(gson.fromJson(array.get(i), Owner.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			ownerList.add(gson.fromJson(object, Owner.class));
		}

		return ownerList;
	}

}
