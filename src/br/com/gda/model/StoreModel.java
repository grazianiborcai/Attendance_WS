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

import br.com.gda.dao.StoreDAO;
import br.com.gda.helper.StoreEmployee;
import br.com.gda.helper.MaterialStore;
import br.com.gda.helper.RecordMode;
import br.com.gda.helper.Store;

public class StoreModel extends JsonBuilder {

	private ArrayList<Store> storeList = new ArrayList<Store>();

	public Response insertStore(String incomingData, String zoneId) {

		ArrayList<Store> storeList = jsonToStoreList(incomingData);

		SQLException exception = new StoreDAO().insertStore(storeList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = storeList.stream().map(m -> m.getCodOwner()).distinct().collect(Collectors.toList());

		List<String> recordMode = new ArrayList<String>();
		recordMode.add(RecordMode.RECORD_OK);

		jsonObject = mergeJsonObject(jsonObject, selectStoreJson(codOwner, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, recordMode, null, true, true, zoneId));

		return response(jsonObject);
	}

	public Response updateStore(String incomingData, String zoneId) {

		ArrayList<Store> storeList = jsonToStoreList(incomingData);

		SQLException exception = new StoreDAO().updateStore(storeList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = storeList.stream().map(m -> m.getCodOwner()).distinct().collect(Collectors.toList());

		List<String> recordMode = new ArrayList<String>();
		recordMode.add(RecordMode.RECORD_OK);

		jsonObject = mergeJsonObject(jsonObject, selectStoreJson(codOwner, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, recordMode, null, true, true, zoneId));

		return response(jsonObject);

	}

	public Response deleteStore(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> codCurr, List<String> recordMode) {

		SQLException exception = new StoreDAO().deleteStore(codOwner, codStore, cnpj, inscEstadual, inscMunicipal,
				razaoSocial, name, address1, address2, postalcode, city, country, state, codCurr, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);

	}

	public ArrayList<Store> selectStore(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode,
			List<String> language, Boolean withMaterial, Boolean withEmployee, String zoneId) throws SQLException {

		final ArrayList<Store> storeList = new StoreDAO().selectStore(codOwner, codStore, cnpj, inscEstadual,
				inscMunicipal, razaoSocial, name, address1, address2, postalcode, city, country, state, phone, codCurr,
				recordMode);

		if (withMaterial || withEmployee) {

			codOwner = storeList.stream().map(s -> s.getCodOwner()).distinct().collect(Collectors.toList());

			for (Long eachCodOwner : codOwner) {

				List<Long> codOwnerList = new ArrayList<Long>();
				codOwnerList.add(eachCodOwner);

				List<Integer> codStoreList = storeList.stream().filter(s -> s.getCodOwner().equals(eachCodOwner))
						.map(s -> s.getCodStore()).collect(Collectors.toList());

				MaterialStoreModel materialStoreModel = new MaterialStoreModel();
				StoreEmployeeModel storeEmployeeModel = new StoreEmployeeModel();

				if (codStoreList != null && !codStoreList.isEmpty()) {

					Thread tStoreMaterial = null;
					Thread tStoreEmployee = null;

					if (withMaterial) {

						tStoreMaterial = new Thread(new Runnable() {
							public void run() {
								try {
									materialStoreModel.selectMaterialStore(codOwnerList, null, codStoreList, null, null,
											null, null, recordMode, language, null, null, null);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						tStoreMaterial.start();
					}

					if (withEmployee) {

						tStoreEmployee = new Thread(new Runnable() {
							public void run() {
								try {
									storeEmployeeModel.selectStoreEmployee(codOwnerList, codStoreList, null, null, null,
											null, null, null, null, null, null, null, null, null, null, null, null,
											recordMode);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						tStoreEmployee.start();
					}

					try {
						if (tStoreMaterial != null)
							tStoreMaterial.join();
						if (tStoreEmployee != null)
							tStoreEmployee.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					final ArrayList<MaterialStore> materialStoreList = materialStoreModel.getStoreMaterialList();
					final ArrayList<StoreEmployee> storeEmployeeList = storeEmployeeModel.getStoreEmployeeList();

					storeList.stream().filter(s -> s.getCodOwner().equals(eachCodOwner)).forEach(s -> {
						s.getMaterial()
								.addAll(materialStoreList.stream()
										.filter(m -> s.getCodOwner().equals(m.getCodOwner())
												&& s.getCodStore().equals(m.getCodStore()))
										.collect(Collectors.toList()));
						s.getEmployee()
								.addAll(storeEmployeeList.stream()
										.filter(e -> s.getCodOwner().equals(e.getCodOwner())
												&& s.getCodStore().equals(e.getCodStore()))
										.collect(Collectors.toList()));
					});
				}
			}
		}

		return this.setStoreList(storeList);
	}

	public ArrayList<Store> selectStoreLoc(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode,
			List<String> language, Boolean withMaterial, Boolean withEmployee, String zoneId, Float latitude,
			Float longitude) throws SQLException {

		final ArrayList<Store> storeList = new StoreDAO().selectStoreLoc(codOwner, codStore, cnpj, inscEstadual,
				inscMunicipal, razaoSocial, name, address1, address2, postalcode, city, country, state, phone, codCurr,
				recordMode, latitude, longitude);

		if (withMaterial || withEmployee) {

			codOwner = storeList.stream().map(s -> s.getCodOwner()).distinct().collect(Collectors.toList());

			for (Long eachCodOwner : codOwner) {

				List<Long> codOwnerList = new ArrayList<Long>();
				codOwnerList.add(eachCodOwner);

				List<Integer> codStoreList = storeList.stream().filter(s -> s.getCodOwner().equals(eachCodOwner))
						.map(s -> s.getCodStore()).collect(Collectors.toList());

				MaterialStoreModel materialStoreModel = new MaterialStoreModel();
				StoreEmployeeModel storeEmployeeModel = new StoreEmployeeModel();

				if (codStoreList != null && !codStoreList.isEmpty()) {

					Thread tStoreMaterial = null;
					Thread tStoreEmployee = null;

					if (withMaterial) {

						tStoreMaterial = new Thread(new Runnable() {
							public void run() {
								try {
									materialStoreModel.selectMaterialStore(codOwnerList, null, codStoreList, null, null,
											null, null, recordMode, language, null, null, null);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						tStoreMaterial.start();
					}

					if (withEmployee) {

						tStoreEmployee = new Thread(new Runnable() {
							public void run() {
								try {
									storeEmployeeModel.selectStoreEmployee(codOwnerList, codStoreList, null, null, null,
											null, null, null, null, null, null, null, null, null, null, null, null,
											recordMode);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						tStoreEmployee.start();
					}

					try {
						if (tStoreMaterial != null)
							tStoreMaterial.join();
						if (tStoreEmployee != null)
							tStoreEmployee.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					final ArrayList<MaterialStore> materialStoreList = materialStoreModel.getStoreMaterialList();
					final ArrayList<StoreEmployee> storeEmployeeList = storeEmployeeModel.getStoreEmployeeList();

					storeList.stream().filter(s -> s.getCodOwner().equals(eachCodOwner)).forEach(s -> {
						s.getMaterial()
								.addAll(materialStoreList.stream()
										.filter(m -> s.getCodOwner().equals(m.getCodOwner())
												&& s.getCodStore().equals(m.getCodStore()))
										.collect(Collectors.toList()));
						s.getEmployee()
								.addAll(storeEmployeeList.stream()
										.filter(e -> s.getCodOwner().equals(e.getCodOwner())
												&& s.getCodStore().equals(e.getCodStore()))
										.collect(Collectors.toList()));
					});
				}
			}
		}

		return this.setStoreList(storeList);
	}

	public JsonObject selectStoreJson(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode,
			List<String> language, Boolean withMaterial, Boolean withEmployee, String zoneId) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectStore(codOwner, codStore, cnpj, inscEstadual, inscMunicipal,
					razaoSocial, name, address1, address2, postalcode, city, country, state, phone, codCurr, recordMode,
					language, withMaterial, withEmployee, zoneId));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}
	
	public JsonObject selectStoreJsonLoc(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode,
			List<String> language, Boolean withMaterial, Boolean withEmployee, String zoneId, Float latitude,
			Float longitude) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson().toJsonTree(selectStoreLoc(codOwner, codStore, cnpj, inscEstadual, inscMunicipal,
					razaoSocial, name, address1, address2, postalcode, city, country, state, phone, codCurr, recordMode,
					language, withMaterial, withEmployee, zoneId, latitude, longitude));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectStoreResponse(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode,
			List<String> language, Boolean withMaterial, Boolean withEmployee, String zoneId) {

		return response(selectStoreJson(codOwner, codStore, cnpj, inscEstadual, inscMunicipal, razaoSocial, name,
				address1, address2, postalcode, city, country, state, phone, codCurr, recordMode, language,
				withMaterial, withEmployee, zoneId));
	}
	
	public Response selectStoreResponseLoc(List<Long> codOwner, List<Integer> codStore, List<String> cnpj,
			List<String> inscEstadual, List<String> inscMunicipal, List<String> razaoSocial, List<String> name,
			List<String> address1, List<String> address2, List<Integer> postalcode, List<String> city,
			List<String> country, List<String> state, List<String> phone, List<String> codCurr, List<String> recordMode,
			List<String> language, Boolean withMaterial, Boolean withEmployee, String zoneId, Float latitude,
			Float longitude) {

		return response(selectStoreJsonLoc(codOwner, codStore, cnpj, inscEstadual, inscMunicipal, razaoSocial, name,
				address1, address2, postalcode, city, country, state, phone, codCurr, recordMode, language,
				withMaterial, withEmployee, zoneId, latitude, longitude));
	}

	public ArrayList<Store> jsonToStoreList(String incomingData) {

		ArrayList<Store> storeList = new ArrayList<Store>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				storeList.add(gson.fromJson(array.get(i), Store.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			storeList.add(gson.fromJson(object, Store.class));
		}

		return storeList;
	}

	public ArrayList<Store> getStoreList() {
		return storeList;
	}

	public ArrayList<Store> setStoreList(ArrayList<Store> storeList) {
		this.storeList = storeList;
		return storeList;
	}

}
