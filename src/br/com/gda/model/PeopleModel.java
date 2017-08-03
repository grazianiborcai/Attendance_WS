package br.com.gda.model;

import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.gda.dao.PeopleDAO;
import br.com.mind5.helper.People;

public class PeopleModel extends JsonBuilder {

	public ArrayList<People> selectPeople(String email, String password, String oAuth) throws SQLException {

		return new PeopleDAO().selectPeople(email, password, oAuth);
	}

	public JsonObject selectPeopleJson(String email, String password, String userAgent) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		ArrayList<People> peopleList = new ArrayList<People>();

		try {

			peopleList = selectPeople(email, password, null);

			if (peopleList == null || peopleList.size() == 0) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
				// exception = new SQLException(email+" : "+password+" :
				// "+emailList.get(0), null, 200);
			}
			
			for (People people : peopleList) {
				
				UUID uuid = UUID.randomUUID();
				people.setoAuth(uuid.toString());
				people.setoAuthDate(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
				people.setUserAgent(userAgent);
				
			}
			
			SQLException login = new PeopleDAO().updatePeople(peopleList);
			
			if (login.getErrorCode() != 200) {
				throw new WebApplicationException(Status.EXPECTATION_FAILED);
			}

			jsonElement = new Gson().toJsonTree(peopleList);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectPeopleResponse(String email, String password, String userAgent) {

		return response(selectPeopleJson(email, password, userAgent));
	}
	
	public JsonObject selectPeopleJson(String oAuth) {

		JsonElement jsonElement = new JsonArray().getAsJsonArray();
		SQLException exception = new SQLException(RETURNED_SUCCESSFULLY, null, 200);

		ArrayList<People> peopleList = new ArrayList<People>();

		try {

			peopleList = selectPeople(null, null, oAuth);

			if (peopleList == null || peopleList.size() == 0) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}

			jsonElement = new Gson().toJsonTree(peopleList);

		} catch (SQLException e) {
			exception = e;
		}

		JsonObject jsonObject = getJsonObjectSelect(jsonElement, exception);

		return jsonObject;
	}

	public Response selectPeopleResponse(String oAuth) {

		return response(selectPeopleJson(oAuth));
	}

}
