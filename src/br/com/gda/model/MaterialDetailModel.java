package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.MaterialDetailDAO;
import br.com.gda.helper.MaterialDetail;
import br.com.gda.helper.DetailMatItem;
import br.com.gda.helper.RecordMode;

public class MaterialDetailModel extends JsonBuilder {

	private static final String MATERIAL_DETAIL = "MaterialDetail";
	private ArrayList<MaterialDetail> detailMatList = new ArrayList<MaterialDetail>();

	public Response insertMaterialDetail(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<MaterialDetail> materialDetailList = (ArrayList<MaterialDetail>) jsonToObjectList(incomingData,
				MaterialDetail.class);

		SQLException exception = new MaterialDetailDAO().insertMaterialDetail(materialDetailList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = materialDetailList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject,
					selectMaterialDetailJson(codOwner, null, null, recordMode, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateMaterialDetail(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<MaterialDetail> materialDetailList = (ArrayList<MaterialDetail>) jsonToObjectList(incomingData,
				MaterialDetail.class);

		SQLException exception = new MaterialDetailDAO().updateMaterialDetail(materialDetailList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = materialDetailList.stream().map(m -> m.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject,
					selectMaterialDetailJson(codOwner, null, null, recordMode, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response deleteMaterialDetail(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codDetail,
			List<String> recordMode) {

		SQLException exception = new MaterialDetailDAO().deleteMaterialDetail(codOwner, codMaterial, codDetail,
				recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<MaterialDetail> selectMaterialDetail(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codDetail, List<String> recordMode, List<String> language, List<String> name)
					throws SQLException {

		ArrayList<MaterialDetail> detailMatList = new MaterialDetailDAO().selectMaterialDetail(codOwner, codMaterial,
				codDetail, recordMode, language, name);

		ArrayList<DetailMatItem> detailMatItemList = new ArrayList<DetailMatItem>();

		List<Long> codOwnerList = new ArrayList<Long>();
		MaterialDetail detailMatBefore = new MaterialDetail();
		for (MaterialDetail detailMat : detailMatList) {
			if (detailMatBefore == null || detailMat.getCodOwner() != detailMatBefore.getCodOwner()) {
				codOwnerList.clear();
				codOwnerList.add(detailMat.getCodOwner());

				codDetail = detailMatList.stream().filter(d -> d.getCodOwner().equals(detailMat.getCodOwner()))
						.map(d -> d.getCodDetail()).collect(Collectors.toList());

				detailMatItemList = new DetailMatItemModel().selectDetailMatItem(codOwnerList, codDetail, null,
						recordMode, language, null, null, null);

				addDetailMatItem(detailMatItemList, detailMat);
			} else
				addDetailMatItem(detailMatItemList, detailMat);

			detailMatBefore = detailMat;
		}

		return this.setDetailMatList(detailMatList);
	}

	private void addDetailMatItem(ArrayList<DetailMatItem> detailMatItemList, MaterialDetail detailMat) {
		detailMat
				.getDetailItem().addAll(
						detailMatItemList.stream()
								.filter(d -> d.getCodOwner().equals(detailMat.getCodOwner())
										&& d.getCodDetail().equals(detailMat.getCodDetail()))
						.collect(Collectors.toList()));
	}

	public JsonObject selectMaterialDetailJson(List<Long> codOwner, List<Integer> codMaterial, List<Integer> codDetail,
			List<String> recordMode, List<String> language, List<String> name, boolean runMsg) {

		ArrayList<MaterialDetail> materialDetailList = new ArrayList<MaterialDetail>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			materialDetailList = selectMaterialDetail(codOwner, codMaterial, codDetail, recordMode, language, name);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(materialDetailList, exception);

		if (runMsg)
			sendMessage(materialDetailList, exception, codOwner, OWNER, MATERIAL_DETAIL);

		return jsonObject;
	}

	public Response selectMaterialDetailResponse(List<Long> codOwner, List<Integer> codMaterial,
			List<Integer> codDetail, List<String> recordMode, List<String> language, List<String> name) {

		return response(selectMaterialDetailJson(codOwner, codMaterial, codDetail, recordMode, language, name, false));
	}

	public ArrayList<MaterialDetail> getDetailMatList() {
		return detailMatList;
	}

	public ArrayList<MaterialDetail> setDetailMatList(ArrayList<MaterialDetail> detailMatList) {
		this.detailMatList = detailMatList;
		return detailMatList;
	}

}
