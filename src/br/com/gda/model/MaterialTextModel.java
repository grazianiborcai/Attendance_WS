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

import br.com.gda.dao.MaterialTextDAO;
import br.com.mind5.helper.MaterialText;

public class MaterialTextModel extends JsonBuilder {

	private ArrayList<MaterialText> materialTextList = new ArrayList<MaterialText>();

	public Response insertMaterialText(String incomingData) {

		ArrayList<MaterialText> materialTextList = jsonToMaterialTextList(incomingData);

		SQLException exception = new MaterialTextDAO().insertMaterialText(materialTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = materialTextList.stream().map(m -> m.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codMaterial = materialTextList.stream().map(m -> m.getCodMaterial()).distinct()
				.collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject, selectMaterialTextJson(codOwner, codMaterial, null, null, null, null));

		return response(jsonObject);
	}

	public Response updateMaterialText(String incomingData) {

		ArrayList<MaterialText> materialTextList = jsonToMaterialTextList(incomingData);

		SQLException exception = new MaterialTextDAO().updateMaterialText(materialTextList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		List<Long> codOwner = materialTextList.stream().map(m -> m.getCodOwner()).distinct()
				.collect(Collectors.toList());

		List<Integer> codMaterial = materialTextList.stream().map(m -> m.getCodMaterial()).distinct()
				.collect(Collectors.toList());

		jsonObject = mergeJsonObject(jsonObject, selectMaterialTextJson(codOwner, codMaterial, null, null, null, null));

		return response(jsonObject);
	}

	public Response deleteMaterialText(List<Long> codOwner, List<Integer> codMaterial, List<String> language,
			List<String> name, List<String> description, List<String> textLong) {

		SQLException exception = new MaterialTextDAO().deleteMaterialText(codOwner, codMaterial, language, name,
				description, textLong);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<MaterialText> selectMaterialText(List<Long> codOwner, List<Integer> codMaterial,
			List<String> language, List<String> name, List<String> description, List<String> textLong)
					throws SQLException {

		return this.setMaterialTextList(
				new MaterialTextDAO().selectMaterialText(codOwner, codMaterial, language, name, description, textLong));

	}

	public JsonObject selectMaterialTextJson(List<Long> codOwner, List<Integer> codMaterial, List<String> language,
			List<String> name, List<String> description, List<String> textLong) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			jsonElement = new Gson()
					.toJsonTree(selectMaterialText(codOwner, codMaterial, language, name, description, textLong));

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectMaterialTextResponse(List<Long> codOwner, List<Integer> codMaterial, List<String> language,
			List<String> name, List<String> description, List<String> textLong) {

		return response(selectMaterialTextJson(codOwner, codMaterial, language, name, description, textLong));
	}

	private ArrayList<MaterialText> jsonToMaterialTextList(String incomingData) {

		ArrayList<MaterialText> materialTextList = new ArrayList<MaterialText>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				materialTextList.add(gson.fromJson(array.get(i), MaterialText.class));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			materialTextList.add(gson.fromJson(object, MaterialText.class));
		}

		return materialTextList;
	}

	public ArrayList<MaterialText> getMaterialTextList() {
		return materialTextList;
	}

	public ArrayList<MaterialText> setMaterialTextList(ArrayList<MaterialText> materialTextList) {
		this.materialTextList = materialTextList;
		return materialTextList;
	}

}
