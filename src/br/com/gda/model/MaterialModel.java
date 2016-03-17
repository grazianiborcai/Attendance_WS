package br.com.gda.model;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import br.com.gda.dao.MaterialDAO;
import br.com.gda.helper.MaterialDetail;
import br.com.gda.helper.Material;
import br.com.gda.helper.RecordMode;

public class MaterialModel extends JsonBuilder {

	private static final String MATERIAL = "Material";
	private ArrayList<Material> materialList = new ArrayList<Material>();

	public Response insertMaterial(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<Material> materialList = (ArrayList<Material>) jsonToObjectList(incomingData, Material.class);

		SQLException exception = new MaterialDAO().insertMaterial(materialList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = materialList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMaterialJson(codOwner, null, null, null, null, null,
					recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateMaterial(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<Material> materialList = (ArrayList<Material>) jsonToObjectList(incomingData, Material.class);

		SQLException exception = new MaterialDAO().updateMaterial(materialList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = materialList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMaterialJson(codOwner, null, null, null, null, null,
					recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);

	}

	public Response deleteMaterial(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode) {

		SQLException exception = new MaterialDAO().deleteMaterial(codOwner, codMaterial, codCategory, codType, image,
				barCode, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<Material> selectMaterial(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode,
			List<String> language, List<String> name, List<String> description, List<String> textLong)
					throws SQLException {

		ArrayList<Material> materialList = new MaterialDAO().selectMaterial(codOwner, codMaterial, codCategory, codType,
				image, barCode, recordMode, language, name, description, textLong);

		ArrayList<MaterialDetail> detailMatList = new ArrayList<MaterialDetail>();

		List<Long> codOwnerList = new ArrayList<Long>();
		Material materialBefore = null;
		for (Material material : materialList) {
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

		return this.setMaterialList(materialList);
	}

	private void addDetailMat(ArrayList<MaterialDetail> detailMatList, Material material) {
		material.getDetailMat().addAll(detailMatList.stream().filter(m -> m.getCodOwner().equals(material.getCodOwner())
				&& m.getCodMaterial().equals(material.getCodMaterial())).collect(Collectors.toList()));
	}

	public JsonObject selectMaterialJson(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode,
			List<String> language, List<String> name, List<String> description, List<String> textLong, boolean runMsg) {

		ArrayList<Material> materialList = new ArrayList<Material>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			materialList = selectMaterial(codOwner, codMaterial, codCategory, codType, image, barCode, recordMode,
					language, name, description, textLong);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(materialList, exception);

		if (runMsg)
			sendMessage(materialList, exception, codOwner, OWNER, MATERIAL);

		return jsonObject;
	}

	public Response selectMaterialResponse(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codCategory,
			List<Integer> codType, List<String> image, List<String> barCode, List<String> recordMode,
			List<String> language, List<String> name, List<String> description, List<String> textLong) {

		return response(selectMaterialJson(codOwner, codMaterial, codCategory, codType, image, barCode, recordMode,
				language, name, description, textLong, false));
	}

	public ArrayList<Material> getMaterialList() {
		return materialList;
	}

	public ArrayList<Material> setMaterialList(ArrayList<Material> materialList) {
		this.materialList = materialList;
		return materialList;
	}

	public Response insertMaterialWithImage(FormDataMultiPart multiPart) {

		Map<String, List<FormDataBodyPart>> params = multiPart.getFields();
		String incomingData = params.get("json").get(0).getValue();

		@SuppressWarnings("unchecked")
		ArrayList<Material> materialList = (ArrayList<Material>) jsonToObjectList(incomingData, Material.class);

		SQLException exception = new MaterialDAO().insertMaterial(materialList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {

			SQLException exceptionImage = new SQLException(FILE_UPLOADED, null, 200);

			for (Material material : materialList) {

//				FormDataContentDisposition contentDispositionHeader = params.get(material.getImage()).get(0)
//						.getFormDataContentDisposition();
				InputStream fileInputStream = params.get(material.getImage()).get(0).getValueAs(InputStream.class);

				String filePath = material.getCodOwner() + BAR + material.getCodMaterial()
						+ "." + JPG;

				SQLException exceptionReturn = new FileModel().saveCompressedFile(fileInputStream, filePath);
				if (exceptionReturn.getErrorCode() != 200)
					exceptionImage = exceptionReturn;

			}

			jsonObject = mergeJsonObject(jsonObject, getJsonObjectUpdateImage(exceptionImage));

			List<Long> codOwner = materialList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMaterialJson(codOwner, null, null, null, null, null,
					recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}

		return response(jsonObject);

	}

	public Response updateMaterialWithImage(FormDataMultiPart multiPart) {

		Map<String, List<FormDataBodyPart>> params = multiPart.getFields();
		String incomingData = params.get("json").get(0).getValue();

		@SuppressWarnings("unchecked")
		ArrayList<Material> materialList = (ArrayList<Material>) jsonToObjectList(incomingData, Material.class);

		SQLException exception = new MaterialDAO().updateMaterial(materialList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {

			SQLException exceptionImage = new SQLException(FILE_UPLOADED, null, 200);

			for (Material material : materialList) {

//				FormDataContentDisposition contentDispositionHeader = params.get(material.getImage()).get(0)
//						.getFormDataContentDisposition();
				InputStream fileInputStream = params.get(material.getImage()).get(0).getValueAs(InputStream.class);

				String filePath = material.getCodOwner() + BAR + material.getCodMaterial()
						+ "." + JPG;

				SQLException exceptionReturn = new FileModel().saveCompressedFile(fileInputStream, filePath);
				if (exceptionReturn.getErrorCode() != 200)
					exceptionImage = exceptionReturn;

			}

			jsonObject = mergeJsonObject(jsonObject, getJsonObjectUpdateImage(exceptionImage));

			List<Long> codOwner = materialList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectMaterialJson(codOwner, null, null, null, null, null,
					recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}

		return response(jsonObject);

	}

}
