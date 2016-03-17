package br.com.gda.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.gda.servlet.ServletContainerGDA;

public class JsonBuilder {

	protected static final String OWNER = "Owner";
	protected static final String STORE = "Store";
	protected static final String BILL = "Bill";
	protected static final String BILL_ITEM = "BillItem";

	protected static final String UPDATE_CODE = "updateCode";
	protected static final String UPDATE_MESSAGE = "updateMessage";
	
	protected static final String UPDATE_IMAGE_CODE = "updateImageCode";
	protected static final String UPDATE_IMAGE_MESSAGE = "updateImageMessage";

	protected static final String SELECT_CODE = "selectCode";
	protected static final String SELECT_MESSAGE = "selectMessage";
	// protected static final String COUNT = "count";
	protected static final String RESULTS = "results";
	protected static final String RETURNED_SUCCESSFULLY = "The list was returned successfully";

	protected static final String UPDATED_PROCESS = "updatedProcess";
	
	protected static final String FILE_UPLOADED = "File uploaded";
	protected static final String FILE_NOT_FOUND = "File not found";
	
	protected static final String JPG = "jpg";
	protected static final String BAR = "/";

	protected final JsonObject getJsonObjectUpdate(SQLException e) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(UPDATE_CODE, e.getErrorCode());
		jsonObject.addProperty(UPDATE_MESSAGE, e.getMessage());

		return jsonObject;
	}
	
	protected final JsonObject getJsonObjectUpdateImage(SQLException e) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(UPDATE_IMAGE_CODE, e.getErrorCode());
		jsonObject.addProperty(UPDATE_IMAGE_MESSAGE, e.getMessage());

		return jsonObject;
	}

	protected final JsonObject getJsonObjectSelect(JsonElement jsonElement, SQLException e) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(SELECT_CODE, e.getErrorCode());
		jsonObject.addProperty(SELECT_MESSAGE, e.getMessage());
		// jsonObject.addProperty(COUNT, jsonElement.getAsJsonArray().size());
		jsonObject.add(RESULTS, jsonElement);

		return jsonObject;

	}

	protected final JsonObject getJsonObjectSelect(ArrayList<?> objectList, SQLException e) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(SELECT_CODE, e.getErrorCode());
		jsonObject.addProperty(SELECT_MESSAGE, e.getMessage());
		// jsonObject.addProperty(COUNT, jsonElement.getAsJsonArray().size());
		jsonObject.add(RESULTS, new Gson().toJsonTree(objectList));

		return jsonObject;

	}

	protected final JsonObject getJsonObjectSelect(JsonElement jsonElement, SQLException e, String process) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(SELECT_CODE, e.getErrorCode());
		jsonObject.addProperty(SELECT_MESSAGE, e.getMessage());
		jsonObject.addProperty(UPDATED_PROCESS, process);
		// jsonObject.addProperty(COUNT, jsonElement.getAsJsonArray().size());
		jsonObject.add(RESULTS, jsonElement);

		return jsonObject;

	}

	protected final JsonObject mergeJsonObject(JsonObject jOdject01, JsonObject jObject02) {

		for (Map.Entry<String, JsonElement> entry : jObject02.entrySet()) {
			jOdject01.add(entry.getKey(), entry.getValue());
		}

		return jOdject01;
	}

	protected final Response response(JsonObject jsonObject) {
		return Response.status(Response.Status.OK).entity(jsonObject.toString()).type(MediaType.APPLICATION_JSON)
				.build();
	}

	protected ArrayList<?> jsonToObjectList(String incomingData, Class<?> c) {

		ArrayList<Object> objectList = new ArrayList<Object>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		if (parser.parse(incomingData).isJsonArray()) {
			JsonArray array = parser.parse(incomingData).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				objectList.add(gson.fromJson(array.get(i), c));
			}
		} else {
			JsonObject object = parser.parse(incomingData).getAsJsonObject();
			objectList.add(gson.fromJson(object, c));
		}

		return objectList;
	}

	protected void sendMessage(ArrayList<?> objectList, SQLException exception, List<?> topicList, String topic,
			String process) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (Object eachTopic : topicList) {

					String fullTopic = null;
					String methodAux = null;
					switch (topic) {
					case OWNER:
						methodAux = "getCodOwner";
						fullTopic = OWNER + "/" + eachTopic.toString();
						break;

					case STORE:
						methodAux = "getCodStore";
						fullTopic = STORE + "/" + eachTopic.toString();
						break;

					case BILL:
						methodAux = "getCodBill";
						fullTopic = BILL + "/" + eachTopic.toString();
						break;

					case BILL_ITEM:
						methodAux = "getCodBillItem";
						fullTopic = BILL_ITEM + "/" + eachTopic.toString();
						break;
					}

					String methodS = methodAux;

					Predicate<Object> filter = new Predicate<Object>() {

						@Override
						public boolean test(Object object) {
							// TODO Auto-generated method stub
							boolean test = false;
							try {
								Method method = object.getClass().getMethod(methodS);
								test = method.invoke(object).equals(eachTopic);
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							return test;
						}
					};

					JsonObject jsonObject = getJsonObjectSelect(
							new Gson().toJsonTree(objectList.stream().filter(filter).collect(Collectors.toList())),
							exception, process);
					ServletContainerGDA.publisher.publishMessage(jsonObject.toString(), fullTopic.toString());
				}
			}
		});
		t.start();
	}

}
