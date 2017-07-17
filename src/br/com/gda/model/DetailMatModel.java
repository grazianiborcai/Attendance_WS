package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.DetailMatDAO;
import br.com.mind5.helper.DetailMat;
import br.com.mind5.helper.DetailMatItem;
import br.com.mind5.helper.MaterialDetail;
import br.com.mind5.helper.RecordMode;

public class DetailMatModel extends JsonBuilder {

	private ArrayList<DetailMat> detailMatList = new ArrayList<DetailMat>();

	private static final String DETAIL_MAT = "DetailMat";

	public Response insertDetailMat(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<DetailMat> detailMatList = (ArrayList<DetailMat>) jsonToObjectList(incomingData, DetailMat.class);

		SQLException exception = new DetailMatDAO().insertDetailMat(detailMatList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = detailMatList.stream().map(d -> d.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectDetailMatJson(codOwner, null, recordMode, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateDetailMat(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<DetailMat> detailMatList = (ArrayList<DetailMat>) jsonToObjectList(incomingData, DetailMat.class);

		SQLException exception = new DetailMatDAO().updateDetailMat(detailMatList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = detailMatList.stream().map(d -> d.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject, selectDetailMatJson(codOwner, null, recordMode, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}

		return response(jsonObject);
	}

	public Response deleteDetailMat(List<Long> codOwner, List<Integer> codDetail, List<String> recordMode) {

		SQLException exception = new DetailMatDAO().deleteDetailMat(codOwner, codDetail, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<DetailMat> selectDetailMat(List<Long> codOwner, List<Integer> codDetail, List<String> recordMode,
			List<String> language, List<String> name) throws SQLException {

		ArrayList<DetailMat> detailMatList = new DetailMatDAO().selectDetailMat(codOwner, codDetail, recordMode,
				language, name);

		ArrayList<DetailMatItem> detailMatItemList = new ArrayList<DetailMatItem>();

		List<Long> codOwnerList = new ArrayList<Long>();
		DetailMat detailMatBefore = new MaterialDetail();
		for (DetailMat detailMat : detailMatList) {
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

	private void addDetailMatItem(ArrayList<DetailMatItem> detailMatItemList, DetailMat detailMat) {
		detailMat
				.getDetailItem().addAll(
						detailMatItemList.stream()
								.filter(d -> d.getCodOwner().equals(detailMat.getCodOwner())
										&& d.getCodDetail().equals(detailMat.getCodDetail()))
						.collect(Collectors.toList()));
	}

	public JsonObject selectDetailMatJson(List<Long> codOwner, List<Integer> codDetail, List<String> recordMode,
			List<String> language, List<String> name, boolean runMsg) {

		ArrayList<DetailMat> detailMatList = new ArrayList<DetailMat>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			detailMatList = selectDetailMat(codOwner, codDetail, recordMode, language, name);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(detailMatList, exception);

		if (runMsg)
			sendMessage(detailMatList, exception, codOwner, OWNER, DETAIL_MAT);

		return jsonObject;
	}

	public Response selectDetailMatResponse(List<Long> codOwner, List<Integer> codDetail, List<String> recordMode,
			List<String> language, List<String> name) {

		return response(selectDetailMatJson(codOwner, codDetail, recordMode, language, name, false));
	}

	public ArrayList<DetailMat> getDetailMatList() {
		return detailMatList;
	}

	public ArrayList<DetailMat> setDetailMatList(ArrayList<DetailMat> detailMatList) {
		this.detailMatList = detailMatList;
		return detailMatList;
	}

}
