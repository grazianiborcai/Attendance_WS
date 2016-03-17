package br.com.gda.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.DetailMatItemDAO;
import br.com.gda.helper.DetailMatItem;
import br.com.gda.helper.RecordMode;

public class DetailMatItemModel extends JsonBuilder {

	private static final String DETAIL_MAT_ITEM = "DetailMatItem";

	public Response insertDetailMatItem(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<DetailMatItem> detailMatItemList = (ArrayList<DetailMatItem>) jsonToObjectList(incomingData,
				DetailMatItem.class);

		SQLException exception = new DetailMatItemDAO().insertDetailMatItem(detailMatItemList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = detailMatItemList.stream().map(d -> d.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codDetail = detailMatItemList.stream().map(d -> d.getCodDetail()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject,
					selectDetailMatItemJson(codOwner, codDetail, null, recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response updateDetailMatItem(String incomingData) {

		@SuppressWarnings("unchecked")
		ArrayList<DetailMatItem> detailMatItemList = (ArrayList<DetailMatItem>) jsonToObjectList(incomingData,
				DetailMatItem.class);

		SQLException exception = new DetailMatItemDAO().updateDetailMatItem(detailMatItemList);

		JsonObject jsonObject = getJsonObjectUpdate(exception);
		if (exception.getErrorCode() == 200) {
			List<Long> codOwner = detailMatItemList.stream().map(d -> d.getCodOwner()).distinct()
					.collect(Collectors.toList());

			List<Integer> codDetail = detailMatItemList.stream().map(d -> d.getCodDetail()).distinct()
					.collect(Collectors.toList());

			List<String> recordMode = new ArrayList<String>();
			recordMode.add(RecordMode.RECORD_OK);

			jsonObject = mergeJsonObject(jsonObject,
					selectDetailMatItemJson(codOwner, codDetail, null, recordMode, null, null, null, null, true));
		} else {
			JsonElement jsonElement = new JsonArray();
			SQLException ex = new SQLException();
			jsonObject = mergeJsonObject(jsonObject, getJsonObjectSelect(jsonElement, ex));
		}
		return response(jsonObject);
	}

	public Response deleteDetailMatItem(List<Long> codOwner, List<Integer> codDetail, List<Integer> codItem,
			List<String> recordMode) {

		SQLException exception = new DetailMatItemDAO().deleteDetailMatItem(codOwner, codDetail, codItem, recordMode);

		JsonObject jsonObject = getJsonObjectUpdate(exception);

		return response(jsonObject);
	}

	public ArrayList<DetailMatItem> selectDetailMatItem(List<Long> codOwner, List<Integer> codDetail,
			List<Integer> codItem, List<String> recordMode, List<String> language, List<String> name,
			List<String> description, List<String> textLong) throws SQLException {

		return new DetailMatItemDAO().selectDetailMatItem(codOwner, codDetail, codItem, recordMode, language, name,
				description, textLong);
	}

	public JsonObject selectDetailMatItemJson(List<Long> codOwner, List<Integer> codDetail, List<Integer> codItem,
			List<String> recordMode, List<String> language, List<String> name, List<String> description,
			List<String> textLong, boolean runMsg) {

		ArrayList<DetailMatItem> detailMatItemList = new ArrayList<DetailMatItem>();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		try {

			detailMatItemList = selectDetailMatItem(codOwner, codDetail, codItem, recordMode, language, name,
					description, textLong);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(detailMatItemList, exception);

		if (runMsg)
			sendMessage(detailMatItemList, exception, codOwner, OWNER, DETAIL_MAT_ITEM);

		return jsonObject;
	}

	public Response selectDetailMatItemResponse(List<Long> codOwner, List<Integer> codDetail, List<Integer> codItem,
			List<String> recordMode, List<String> language, List<String> name, List<String> description,
			List<String> textLong) {

		return response(selectDetailMatItemJson(codOwner, codDetail, codItem, recordMode, language, name, description,
				textLong, false));
	}

}
