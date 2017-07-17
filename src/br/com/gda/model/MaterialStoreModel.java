package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.MaterialStoreDAO;
import br.com.mind5.helper.MaterialDetail;
import br.com.mind5.helper.MaterialStore;
import br.com.mind5.helper.RecordMode;

public class MaterialStoreModel extends JsonBuilder {

	private static final String MATERIAL_STORE = "MaterialStore";
	private ArrayList<MaterialStore> storeMaterialList = new ArrayList<MaterialStore>();

	public Response insertMaterialStore(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<MaterialStore> materialStoreList = (ArrayList<MaterialStore>) jsonToObjectList(incomingData,
				MaterialStore.class);

		SQLException exception = new MaterialStoreDAO().insertMaterialStore(materialStoreList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = materialStoreList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codStore = materialStoreList.stream().map(m -> m.getCodStore()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMaterialStoreJson(codOwner, null, codStore, null, null, null,
					null, recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateMaterialStore(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<MaterialStore> materialStoreList = (ArrayList<MaterialStore>) jsonToObjectList(incomingData,
				MaterialStore.class);

		SQLException exception = new MaterialStoreDAO().updateMaterialStore(materialStoreList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = materialStoreList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codStore = materialStoreList.stream().map(m -> m.getCodStore()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMaterialStoreJson(codOwner, null, codStore, null, null, null,
					null, recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response deleteMaterialStore(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codStore,
			List<String> recordMode) {

		SQLException exception = new MaterialStoreDAO().deleteMaterialStore(codOwner, codMaterial, codStore,
				recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<MaterialStore> selectMaterialStore(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codStore, List<Integer> codCategory, List<Integer> codType, List<String> image,
			List<String> barCode, List<String> recordMode, List<String> language, List<String> name,
			List<String> description, List<String> textLong) throws SQLException {

		ArrayList<MaterialStore> materialList = new MaterialStoreDAO().selectMaterialStore(codOwner, codMaterial,
				codStore, codCategory, codType, image, barCode, recordMode, language, name, description, textLong);

		ArrayList<MaterialDetail> detailMatList = new ArrayList<MaterialDetail>();

		List<Long> codOwnerList = new ArrayList<Long>();
		MaterialStore materialBefore = null;
		for (MaterialStore material : materialList) {
			if (materialBefore == null || material.getCodOwner() != materialBefore.getCodOwner()) {

				codOwnerList.clear();
				codOwnerList.add(material.getCodOwner());

				codMaterial = materialList.stream().filter(m -> m.getCodOwner().equals(material.getCodOwner()))
						.map(m -> m.getCodMaterial()).collect(Collectors.toList());

				detailMatList = new MaterialDetailModel().selectMaterialDetail(codOwnerList, codMaterial, null,
						recordMode, language, null);

				addDetailMat(detailMatList, material);
			} else {
				addDetailMat(detailMatList, material);
			}
			materialBefore = material;
		}

		return this.setStoreMaterialList(materialList);
	}

	private void addDetailMat(ArrayList<MaterialDetail> detailMatList, MaterialStore material) {
		material.getDetailMat().addAll(detailMatList.stream().filter(m -> m.getCodOwner().equals(material.getCodOwner())
				&& m.getCodMaterial().equals(material.getCodMaterial())).collect(Collectors.toList()));
	}

	public JsonObject selectMaterialStoreJson(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codStore,
			List<Integer> codCategory, List<Integer> codType, List<String> image, List<String> barCode,
			List<String> recordMode, List<String> language, List<String> name, List<String> description,
			List<String> textLong, boolean runMsg) {

		ArrayList<MaterialStore> materialStoreList = new ArrayList<MaterialStore>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			materialStoreList = selectMaterialStore(codOwner, codMaterial, codStore, codCategory, codType, image,
					barCode, recordMode, language, name, description, textLong);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(materialStoreList, exception);

		if (runMsg)
			sendMessage(materialStoreList, exception, codStore, STORE, MATERIAL_STORE);

		return jsonObject;
	}

	public Response selectMaterialStoreResponse(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codStore,
			List<Integer> codCategory, List<Integer> codType, List<String> image, List<String> barCode,
			List<String> recordMode, List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		return response(selectMaterialStoreJson(codOwner, codMaterial, codStore, codCategory, codType, image, barCode,
				recordMode, language, name, description, textLong, false));
	}

	public ArrayList<MaterialStore> getStoreMaterialList() {
		return storeMaterialList;
	}

	public ArrayList<MaterialStore> setStoreMaterialList(ArrayList<MaterialStore> storeMaterialList) {
		this.storeMaterialList = storeMaterialList;
		return storeMaterialList;
	}

}
